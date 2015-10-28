package csdc.action.product;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;

import csdc.action.BaseAction;
import csdc.model.Account;
import csdc.model.Article;
import csdc.service.IAppealService;
import csdc.tool.bean.LoginInfo;
import csdc.tool.info.GlobalInfo;

@Component
@Scope(value = "prototype")
public class AppealAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -821014937551687334L;
	
	private  static final String HQL = "select a.id ,a.title,a.content,a.author,a.createDate from Article a where a.systemOption.id='appeal'";
	private static final String[] COLUMN = {
		"a.id",
		"a.title",
		"a.content",
		"a.author",
		"a.createDate"
	};// 用于拼接的排序列
	
	private static final String PAGE_NAME = "appealPage";// 列表页面名称
	private static final String DATE_FORMAT = "yyyy-MM-dd";// 列表时间格式
	private static final String PAGE_BUFFER_ID = "a.id";// 上下条查看时用于查找缓存的字段
	
	private Article article;
	private String type;
	
	@Autowired
	private IAppealService appealService;

	@Override
	public String toAdd() {
		return SUCCESS;
	}

	@Override
	public String add() {
		Map map = new HashMap();
		Account account = ((LoginInfo)ActionContext.getContext().getSession().get(GlobalInfo.LOGINER)).getAccount();
		map.put("account", account);
		map.put("type", type);
		appealService.addAppleal(article, map);
		return SUCCESS;
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
		return SUCCESS;
	}

	@Override
	public String toView() {
		return SUCCESS;
	}

	@Override
	public Object[] simpleSearchCondition() {

		keyword = (keyword == null) ? "" : keyword.toLowerCase().trim();// 预处理关键字
		StringBuffer hql = new StringBuffer();
		Map map = new HashMap();
		hql.append(HQL);
		if (keyword != null && !keyword.isEmpty()) {
			if (searchType == 1) {
				hql.append("and LOWER(a.title) like :keyword");
				map.put("keyword", "%" + keyword + "%");
			} else if (searchType == 2) {
				hql.append("and LOWER(a.content) like :keyword");
				map.put("keyword", "%" + keyword + "%");
			} else if (searchType == 3) {
				hql.append("and (LOWER(a.author) like :keyword ");
				map.put("and keyword", "%" + keyword + "%");
			}else {
				hql.append("and (LOWER(a.createDate) like :keyword ");
				map.put("and keyword", "%" + keyword + "%");
			}
		}
		
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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
}
