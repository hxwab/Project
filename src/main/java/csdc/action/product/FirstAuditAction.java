package csdc.action.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mysql.fabric.xmlrpc.base.Data;
import com.opensymphony.xwork2.ActionContext;
import csdc.action.BaseAction;
import csdc.model.Account;
import csdc.model.Product;
import csdc.service.IFirstAuditService;
import csdc.tool.bean.LoginInfo;
import csdc.tool.info.GlobalInfo;

/**
 * 初审（非高校直接由省社科联审核；高校先由高校审核，高校审核通过后再由社科联审核）
 * 1、高校只能审核本校的申报成果（根据高校管理员的账户所属机构找出该机构的所有申报成果）
 * 2、社科联管理员可以审核所有申报成果（非高校+高校审核通过部分）
 * 3、高校管理员3，社科联管理员2
 * 
 * @author huangxw
 *
 */

@Component
@Scope(value="prototype")
public class FirstAuditAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5543945160710526586L;

	
	private  static final String HQL = "select p.id,p.number,p.name,p.authorName,p.agencyName,p.researchType ,p.updateDate,p.applyYear,a.type from Product p left join p.agency a where 1=1";
	private static final String[] COLUMN = {
		"p.id",
		"p.number",
		"p.name",
		"p.author",
		"p.agencyName",
		"p.researchType",
		"p.applyYear",
		"p.updateDate",
		"a.type"
		
	};// 用于拼接的排序列
	
	private static final String PAGE_NAME = "firstAuditPage";// 列表页面名称
	private static final String DATE_FORMAT = "yyyy-MM-dd";// 列表时间格式
	private static final String PAGE_BUFFER_ID = "p.id";// 上下条查看时用于查找缓存的字段
	
	
	private Account account;

	private Product product;
	
	private String result;
	
	private String opinion;
	
	private List<String> opinions;
	
	@Autowired
	private IFirstAuditService firstAuditService;
	
	
	public String toFirstCheck(){
		
		return SUCCESS;
	}
	
	public String firstCheck(){
		LoginInfo loginer =  (LoginInfo) ActionContext.getContext().getSession().get(GlobalInfo.LOGINER);
		Map map = new HashMap();
		account = loginer.getAccount();
		map.put("account", account);
		map.put("result", result);
		map.put("opinion", opinion);
		firstAuditService.setFirstCheckRestults(entityIds, map);
		return SUCCESS;
	}
	
	

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

	@Override
	public String toModify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String view() {
		product = firstAuditService.getProductById(entityId);
		jsonMap.put("product", product);
		return SUCCESS;
	}

	@Override
	public String toView() {
		return SUCCESS;
	}

	@Override
	public Object[] simpleSearchCondition() {
		LoginInfo loginer =  (LoginInfo) ActionContext.getContext().getSession().get(GlobalInfo.LOGINER);
		account = loginer.getAccount();
		int currentYear = new Date().getYear()+1900;
		StringBuffer hql = new StringBuffer();
		Map map = new HashMap();
		hql.append(HQL);
		//高校管理员
		if(account.getType()==3){
			hql.append("and p.agencyName = :agencyName");
			map.put("agencyName", account.getAgency().getName());
			
		}else if(account.getType()==2){
			hql.append(" and p.universityAuditResult='2' or p.agency.type='0'");
			
		}else if(account.getType()==1){
			
			//超级管理员所有的都可查询出来，不做任何限制
		}else {
			//非管理员用查询出来的为空
			hql.append("and p.id = :id");
			map.put("id", "");
		}
		
		hql.append("and p.applyYear = :applyYear");
		map.put("applyYear", Integer.toString(currentYear));
		
		
		return new Object[]{
				hql.toString(),
				map,
				0,
				null
			};		
	}

	@Override
	public Object[] advSearchCondition() {
		
		
		return null;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public List<String> getOpinions() {
		return opinions;
	}

	public void setOpinions(List<String> opinions) {
		this.opinions = opinions;
	}
	

}
