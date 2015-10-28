package csdc.action.selfspace;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;

import csdc.action.BaseAction;
import csdc.model.Account;
import csdc.model.Person;
import csdc.model.Product;
import csdc.model.SolicitPapers;
import csdc.service.IPersonInfoService;
import csdc.tool.HSSFExport;
import csdc.tool.HqlTool;
import csdc.tool.bean.AccountType;
import csdc.tool.bean.LoginInfo;
import csdc.tool.info.GlobalInfo;
import csdc.tool.info.RightInfo;

@Component
@Scope(value = "prototype")
public class PersonInfoAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4856000556466257974L;
	
	private static final String PAGE_NAME = "personInfoPage";// 列表页面名称
	private static final String DATE_FORMAT = "yyyy-MM-dd";// 列表时间格式
	private static final String PAGE_BUFFER_ID = "paper.id";// 上下条查看时用于查找缓存的字段
	private static final String TMP_ENTITY_ID = "personId";// 用于session缓存实体的ID名称

	private String oldPassword;
	private String newPassword;
	private String reNewPassword;
	private Person person;
	private Account account;
	
	@Autowired
	private IPersonInfoService personInfoService;
	
	private String fileFileName;
	

	public String toModifyPassword(){
		return SUCCESS;
	}
	
	public String modifyPassword(){
		LoginInfo  loginer = (LoginInfo) ActionContext.getContext().getSession().get(GlobalInfo.LOGINER);
		String username = loginer.getAccount().getUsername();
		
		if(!reNewPassword.equals(newPassword)){
			this.addFieldError(GlobalInfo.ERROR_INFO, "两次输入密码不一致");
			jsonMap.put("tag", "3");
			return INPUT;
		}
		if(!personInfoService.checkPassword(oldPassword, username)){
			this.addFieldError(GlobalInfo.ERROR_INFO, "用户密码错误");
			jsonMap.put("tag", "2");
			return INPUT;
		}else {
			
			personInfoService.modifyPassword(oldPassword, newPassword, username);
			jsonMap.put("tag", "1");
		}
		
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

	/**
	 * 修改个人信息
	 */
	@Override
	public String toModify() {
		
		LoginInfo loginer = (LoginInfo) ActionContext.getContext().getSession().get(GlobalInfo.LOGINER);
		account = loginer.getAccount();
		return SUCCESS;
	}

	@Override
	public String modify() {
		LoginInfo  loginer = (LoginInfo) ActionContext.getContext().getSession().get(GlobalInfo.LOGINER);
		account = loginer.getAccount();
		person = account.getPerson();
		personInfoService.modifyPersonInfo(person, null);
		jsonMap.put("tag", "2");
		return SUCCESS;
	}

	@Override
	public String view() {
		LoginInfo  loginer = (LoginInfo) ActionContext.getContext().getSession().get(GlobalInfo.LOGINER);
		 account = loginer.getAccount();
		
		/*List  person = personInfoService.getPersonByAccount(account);
		jsonMap.put("person", person);*/
		jsonMap.put("account", account);
		
		List<Product> products =personInfoService.getProductByAccount(account);
	
		jsonMap.put("products", products);
		
		return SUCCESS;
	}

	@Override
	public String toView() {
		return SUCCESS;
	}

	
	@Override
	public Object[] simpleSearchCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] advSearchCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pageName() {
		
		return this.PAGE_NAME;
	}

	@Override
	public String pageBufferId() {
		
		return this.PAGE_BUFFER_ID;
	}

	@Override
	public String[] sortColumn() {
		
		return null;
	}

	@Override
	public String dateFormat() {
		
		return this.DATE_FORMAT;
	}

	
	
	
	
	
	/**
	 * 确认导出一览表
	 * @return
	 * @author xn
	 */
	public String confirmExportOverView(){
		
		return SUCCESS;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InputStream getDownloadFile() throws UnsupportedEncodingException{
		String header = "项目申报一览表";//表头
		
		StringBuffer hql4Export = new StringBuffer();
		String[] title = new String[]{};//标题
			title = new String[]{
					"序号",
					"项目名称",
					"项目类型",
					"提交时间",
					"批准号",
					"作者",
					
				};
			hql4Export.append("select pd.id ,pd.name,pd.authorName,pd.type,pd.researchType,pd.submitDate from Product pd where 1=1" );
			
			
		fileFileName = header + ".xls";
		fileFileName = new String(fileFileName.getBytes("UTF-8"), "ISO8859-1");
		
		
		List<Object[]> list = dao.query(hql4Export.toString());
		List dataList = new ArrayList();
		Map<Object, Object[]> lastData = new HashMap<Object, Object[]>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		int index = 1;
		//若是基地项目则需要查询基地名称
	
			for (Object object : list) {
				Object[] o = (Object[]) object;
				Object[] data = new Object[6];
				data[0] = index++;
				data[1] = o[1];//项目名称
				data[2] = o[3];//项目类别
				data[3] = "2015";//项目年度
				data[4] = o[0];//项目批准号
				data[5] = o[2];//项目负责人
				
				dataList.add(data);
			}
			
			InputStream is = HSSFExport.commonExportExcel(dataList, header, title);
		return is;
	}
	
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getReNewPassword() {
		return reNewPassword;
	}

	public void setReNewPassword(String reNewPassword) {
		this.reNewPassword = reNewPassword;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	
	

	
	
	
}
