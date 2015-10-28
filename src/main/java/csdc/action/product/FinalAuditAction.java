package csdc.action.product;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import csdc.action.BaseAction;
import csdc.model.Product;
import csdc.model.ProductReview;
import csdc.model.Reward;
import csdc.service.IFinalAuditService;
import csdc.tool.HSSFExport;

@Component
@Scope(value = "prototype")
public class FinalAuditAction extends BaseAction {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7118166128512170277L;
	
	private  static final String HQL = "select p.id,p.number,p.name,p.authorName,p.agencyName,p.researchType,p.updateDate,r.year,r.level,r.type from Product p left join p.reward r where 1=1 ";
	private static final String[] COLUMN = {
		"p.id",
		"p.number",
		"p.name",
		"p.author",
		"p.agencyName",
		"p.researchType",
		"p.updateDate",
		"r.year",
		"r.level",
		"r.type"
	};// 用于拼接的排序列
	
	private static final String PAGE_NAME = "finalAuditPage";// 列表页面名称
	private static final String DATE_FORMAT = "yyyy-MM-dd";// 列表时间格式
	private static final String PAGE_BUFFER_ID = "p.id";// 上下条查看时用于查找缓存的字段
	
	private Product product;
	private ProductReview productReview;
	private Reward reward;
	private String fileFileName;
	@Autowired
	private  IFinalAuditService finalAuditService;
	

	@Override
	public String toAdd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String add() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		return null;
	}

	//只能修改奖励等级信息
	@Override
	public String toModify() {
		product = finalAuditService.getProduct(entityId);		
		return SUCCESS;
	}

	@Override
	public String modify() {
		return SUCCESS ;
	}

	@Override
	public String view() {
		
		product = finalAuditService.getProduct(entityId);
		productReview = finalAuditService.getProductReview(entityId);
		reward = finalAuditService.getReward(entityId);
		jsonMap.put("product", product);
		jsonMap.put("productReview", productReview);
		jsonMap.put("reward", reward);
		return SUCCESS;
	}

	@Override
	public String toView() {
		return SUCCESS;
	}

	//账户类型为一般管理员及以上用户可用
	@Override
	public Object[] simpleSearchCondition() {
		
		int currentYear = new Date().getYear()+1900;
		
		keyword = (keyword == null) ? "" : keyword.toLowerCase().trim();// 预处理关键字
		StringBuffer hql = new StringBuffer();
		Map map = new HashMap();
		hql.append(HQL);
		if (keyword != null && !keyword.isEmpty()) {
			if (searchType == 1) {
				hql.append("and LOWER(p.agencyName) like :keyword");
				map.put("keyword", "%" + keyword + "%");
			} else if (searchType == 2) {
				hql.append("and LOWER(p.researchType) like :keyword");
				map.put("keyword", "%" + keyword + "%");
			} else if (searchType == 3) {
				hql.append("and (LOWER(r.level) like :keyword ");
				map.put("and keyword", "%" + keyword + "%");
			}
		}
		//默认为当前年份
		keyword = searchType==4 ? keyword :Integer.toString(currentYear);
		hql.append("and r.year = :keyword ");
		map.put("keyword", keyword);
		
		// 调用初级检索功能
		return new Object[]{
			hql.toString(),
			map,
			0,
			null
		};
	}

	@Override
	public Object[] advSearchCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 导出成果获奖一览表
	 * @return
	 * @author xn
	 */
	public String confirmExportOverView(){
		
		return SUCCESS;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InputStream getDownloadFile() throws UnsupportedEncodingException{
		String header = "成果获奖一览表";//表头
		
		StringBuffer hql4Export = new StringBuffer();
		String[] title = new String[]{};//标题
			title = new String[]{
					"序号",
					"成果编号",
					"成果名称",
					"第一作者",
					"依托机构",
					"成果类型",
					"申请年份",
					"获奖等级",
					"获奖类型",
					"获奖金额"
					
				};
			hql4Export.append("select p.id,p.number,p.name,p.authorName,p.agencyName,p.researchType,r.year,r.level,r.type ,r.bonus from Product p left join p.reward r where r.year=:year order by p.number" );
		
		int currentYear = new Date().getYear()+1900;
		Map map = new HashMap();
		map.put("year", Integer.toString(currentYear));
		
		fileFileName =currentYear+"年"+ header + ".xls";
		fileFileName = new String(fileFileName.getBytes("UTF-8"), "ISO8859-1");
		List<Object[]> list;
		
		//默认为导出当前年份的获奖成果，也可以导出选中的获奖成果
		if(entityIds.isEmpty()){
			
			 list = dao.query(hql4Export.toString(),map);
		} else {
			 list = finalAuditService.getProductInfos(entityIds);
		}
		List dataList = new ArrayList();
		int index = 1;
	
			for (Object object : list) {
				Object[] o = (Object[]) object;
				Object[] data = new Object[10];
				data[0] = index++;
				data[1] = o[1];//成果编号
				data[2] = o[2];//成果名称
				data[3] = o[3];//第一作者
				data[4] = o[4];//依托机构
				data[5] = o[5];//成果类型
				data[6] = o[6];//申请年份
				data[7] = rewardLevel(Integer.parseInt(o[7].toString()));//获奖等级
				data[8] = rewardType(Integer.parseInt(o[8].toString()));//获奖类型
				data[9] = o[9];//获奖金额
				
				dataList.add(data);
			}
			 
		return HSSFExport.commonExportExcel(dataList, header, title);
	}
	
	/**
	 * 获奖等级转换
	 * @param type
	 * @return
	 */
	private String rewardLevel(int type){
		String level="";
		switch (type) {
		case 0:
			level="特等奖";
			break;
		case 1:
			level="一等奖";
			break;
		case 2:
			level="二等奖";
			break;
		case 3:
			level="三等奖";
			break;

		default:
			break;
		}
		
		return level;
		
	}
	
	private String  rewardType(int type){
		String  rewardType = "";
		switch (type) {
		
		case 1:
			rewardType="著作";
			break;
		case 2:
			rewardType="论文";
			break;
	
		default:
			break;
		}
		return rewardType;
		
	}

	@Override
	public String pageName() {
		return PAGE_NAME;
	}

	@Override
	public String pageBufferId() {
		return PAGE_BUFFER_ID;
	}

	@Override
	public String[] sortColumn() {
		return COLUMN;
	}

	@Override
	public String dateFormat() {
		return DATE_FORMAT;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductReview getProductReview() {
		return productReview;
	}

	public void setProductReview(ProductReview productReview) {
		this.productReview = productReview;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	
	

}
