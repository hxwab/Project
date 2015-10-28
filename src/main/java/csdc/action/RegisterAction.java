package csdc.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import csdc.dao.HibernateBaseDao;
import csdc.model.Account;
import csdc.model.Agency;
import csdc.model.Mail;
import csdc.model.Person;
import csdc.service.IMailService;
import csdc.service.IRegisterService;
import csdc.tool.MD5;
import csdc.tool.info.GlobalInfo;
import csdc.tool.mail.Mailer;

@Component
@Scope(value="prototype")
public class RegisterAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4559757818242826762L;
	
	private Account account;
	private Person person;
	private String agencyId;
	private String rePassword;
	private Map jsonMap = new HashMap();
	
	
	@Autowired
	private IRegisterService registerService;
	@Autowired
	private IMailService mailService;
	
	public String toRegister(){
		
		return SUCCESS;
	}
	
	public String register(){
		Map map = new HashMap();
		
		if(registerService.isExistUsername(account.getUsername())){
			jsonMap.put("tag", "1");
			return INPUT;
		}
		
		if(account.getPassword().equals(rePassword)){
			account.setPassword(MD5.getMD5(rePassword));
		}else {
			jsonMap.put("tag", "2");
			return INPUT;
		}
		
		
		if(registerService.isExistEmail(account.getMail())){
			jsonMap.put("tag", "3");
			return INPUT;
		}
		
		map.put("agencyId", agencyId);
		
		registerService.addAccount(account, person, map);
		
		/**
		 * 发送邮件
		 * 
		 */
		Mail mail =  new Mail();
		mail.setAccount(account);
		mail.setSendTo(account.getMail());
		mail.setSubject("湖北省社科联系统账号注册邮件");
		mail.setBody("您好！您已成功注册湖北省社科联成果评审系统账号。账号名为："+account.getUsername()+"您的账户在管理员审核后方可登录。");
		mail.setCreateDate(new Date());
		mail.setSendTimes(0);
		mail.setIsHtml(0);
		
		
		try {
			mailService.addMail(mail);
			Mailer.send(mail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jsonMap.put("tag", "4");
		return SUCCESS;
	}

	
	
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}


	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public Map getJsonMap() {
		return jsonMap;
	}

	public void setJsonMap(Map jsonMap) {
		this.jsonMap = jsonMap;
	}

	

	
}
