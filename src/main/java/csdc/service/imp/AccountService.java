package csdc.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import csdc.model.Account;
import csdc.model.Agency;
import csdc.model.Mail;
import csdc.model.Person;
import csdc.service.IAccountService;
import csdc.tool.MD5;
import csdc.tool.bean.AccountType;
import csdc.tool.bean.LoginInfo;
import csdc.tool.mail.Mailer;
/**
 * 账号管理，包括账号的增删查改 及相应权限等的查询操作
 * 
 * 批量添加账号操作时人员信息怎么搞（尤其是非空字段）
 * @author huangxw
 *
 */
@Service
@Transactional
public class AccountService extends BaseService implements IAccountService {
	
	public Account getAccountByName(String accountName) {
		Account account = (Account) dao.queryUnique("select acc from Account acc where acc.username =?", accountName);
		return account;
	}
	/**
	 * 获取当前通过认证的账号名称
	 */
	public String securityUsername() {
		// 获得当前通过认证的用户上下文信息
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// 获得当前通过认证用户的账号名
		String username;
		if (principal instanceof UserDetails) {// 如果上下文信息为UserDails实例，则调用该接口的getUsername方法获取账号名
			username = ((UserDetails)principal).getUsername();
		} else {// 如果上下文信息不是UserDails实例，则它就是账号名
			username = principal.toString();
		}
		return username;
	}
	
	public String addAccount(Account account) {
		//待完善
		if (account.getType().equals("-1")) {
			account.setType(1);
		}
		account.setPassword(MD5.getMD5(account.getPassword()));
		account.setStatus(1);
		return dao.add(account).toString();
	}
	@Override
	public String modifyAccount(Account account, Account oldAccount) {
	
		if (!account.getIsSuperUser().equals(oldAccount.getIsSuperUser())) {
			oldAccount.setIsSuperUser(account.getIsSuperUser());
		}
		if (!account.getUsername().equals(oldAccount.getUsername())) {
			oldAccount.setUsername(account.getUsername());
		}
		if (!account.getType().equals(oldAccount.getType())) {
			oldAccount.setType(account.getType());
		}
		dao.modify(oldAccount);
		return oldAccount.getId();
	
	}
	
	
	@Override
	public void enable(List<String> ids, Date date) {
		
		Account account;
		for(String id :ids){
			
			account = dao.query(Account.class, id);
			account.setStatus(1); //设置为启用
			account.setExpireDate(date);//设置有效期
			dao.modify(account);
			
		}
	}
	
	@Override
	public void disable(List<String> ids) {
		// TODO Auto-generated method stub
		Account account;
		for(String id :ids) {
			
			account = dao.query(Account.class,id);
			account.setStatus(0);
			dao.modify(account);
			
		}
	}
	
	
	@Override
	public void delete(List<String> ids) {

		for(String id :ids){
			dao.delete(Account.class, id);
		}
	}
	
	/**
	 * 根据账号名获得账号权限
	 * @param name账号名
	 * @return 如果账号不存在，则返回null；
	 * 如果账号存在，则系统管理员账号返回所有权限；
	 * 其它账号，返回拥有角色所对应的权限。
	 * 返回结果均无重复项。
	 */
	@Override
	public List<String> getRightByAccountName(String accountName) {
		if (accountName == null || accountName.isEmpty()) {// 如果账号名为空，则直接返回null
			return null;
		} else {// 如果账号名非空，则先查找该账号对象，根据账号的类型读取相应的权限。
			Map map = new HashMap();
			map.put("name", accountName);
			Account account = (Account) dao.query("from Account ac where ac.username = :name", map).get(0);
			map.remove("name");
			
			if (account == null) {// 账号不存在，直接返回空
				return null;
			} else {// 账号存在，则根据账号类型查询相应的权限
				List<String> re;// 权限集合
				if (account.getIsSuperUser() == 1 ) {// 系统管理员读取所有权限
					re = dao.query("select distinct r.code from Right r");
				} else if (account.getType()==2||account.getType()==3 ) {// 测试
					re = dao.query("select distinct r.code from Right r");
				} 
				else {// 其它账号，根据账号、角色、权限关联关系，读取自己拥有的权限
					map.put("accountId", account.getId());
					re = dao.query("select distinct r.code from Right r, RoleRight rr, AccountRole ar where ar.account.id = :accountId and ar.role.id = rr.role.id and rr.right.id = r.id", map);
				}
				return re;
			}
		}
	}
	
	
	@Override
	public Account getAccountById(String accountId) {

		if(accountId==null ||accountId.equals("")){
			
			return null;
		} else {
			
			return dao.query(Account.class, accountId);
		}
	}
	
	
	@Override
	public List<String> getRightByAccountId(String accountId) {
		
		if(accountId==null||accountId.equals("")){
			return null;
		}else {
			Map map = new HashMap();
			Account account = dao.query(Account.class, accountId);
			List<String> list;
			if(account.getType().equals(AccountType.SUPERADMIN)){
				list = dao.query("select r.code from Right r");
			}else {
				
				map.put("accountId", account.getId());
				list = dao.query("select distinct r.code from Right ,RoleRight rr,AccountRole ar where ar.account.id = :accountId and ar.role.id=rr.role.id and rr.right.id = r.id",map );
			}
			
			return list;
		}
		
	}
	
	
	@Override
	public void assignRole(LoginInfo loginer, List<String> accountIds,
			String[] roleIds, int type) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getRoleName(String accountId) {
		
		return null;
	}
	
	
	@Override
	public boolean checkAccountName(String accountName) {

		if (accountName == null || accountName.isEmpty()) {// 如果账号名为空，则视为已存在
			return true;
		} else {// 如果账号名非空，则查询数据库，判断此账号名是否存在
			Map map = new HashMap();
			map.put("accountName", accountName);
			List<String> re = dao.query("select a.id from Account a  where a.name = :accountName", map);// 获取指定名称的账号
			return !re.isEmpty();
		}
	}
	@Override
	public Map viewAccount(Account account, Map jsonMap, LoginInfo loginer) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 添加账号
	 */
	@Override
	public String addAccount(Account account, Map map) {
		Person person = (Person) map.get("person");
		Agency agency = dao.query(Agency.class,map.get("agencyId").toString());
		int type = (Integer) map.get("type");
		
		person.setAgency(agency);
		person.setAgencyName(agency.getName());
		person.setEmail(account.getMail());
		
		String personId = addPerson(person);
		Person p = dao.query(Person.class, personId);
		account.setPerson(p);
		account.setAgency(agency);
		
		//Type要区分
		account.setType(type);
		account.setStatus(0);
		account.setStartDate(new Date());
		account.setExpireDate(new Date(117, 10, 25, 23, 59, 59));
		account.setCreateType(0);
		account.setIsSuperUser(0);
		return dao.add(account);
		
	}
	
	
	/**
	 * 批量添加账号（账号名为skl+6位数字，初始密码为666666）
	 * 前台传personId以及账号类型
	 * 
	 */
	@Override
	public String addAccounts(List<String> personIds, String type) {
		
		int accountType = Integer.parseInt(type);
		Person person;
		Account account ;
		MailService mailService ;
		Random random = new Random();
		int randomNumber=0;
		Set<Integer> randoms = new HashSet<Integer>();
		
		for(String personId :personIds){
			person = dao.query(Person.class, personId);
			//生成账号后六位
			boolean isUsed = false;
			  while(!isUsed){
				  randomNumber=	random.nextInt(100000);
				  isUsed= randoms.add(randomNumber);
			  }
			account = new Account();
			
			account.setUsername("skl"+randomNumber);
			account.setPassword(MD5.getMD5("66666"));
			account.setType(accountType);
			account.setStatus(1);
			account.setStartDate(new Date());
			account.setApproveTime(new Date());
			account.setExpireDate(new Date(117, 10, 25, 23, 59, 59));
			account.setCreateType(0);
			account.setAgency(person.getAgency());
			account.setPerson(person);
			account.setMail(person.getEmail());
			account.setIsSuperUser(0);
			dao.add(account);
			
			
			Mail mail =  new Mail();
			mail.setAccount(account);
			mail.setSendTo(account.getMail());
			mail.setSubject("湖北省社科联系统账号分配邮件");
			mail.setBody("您好！您已被选为账号名为："+account.getUsername()+"。");
			mail.setCreateDate(new Date());
			mail.setSendTimes(0);
			mail.setIsHtml(0);
			mailService = new MailService();
			
			try {
				mailService.addMail(mail);
				Mailer.send(mail);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private String addPerson(Person person) {
		
		
		StringBuffer str ;
		Map map =new HashMap();
		map.put("name",person.getName());
		map.put("birthday", person.getBirthday());
		map.put("agencyName", person.getAgencyName());
		String hql ="select p from Person p where p.name = :name and p.birthday = :birthday and p.agencyName= :agencyName";
		Person p =(Person) dao.queryUnique(hql, map);
		
		if(p==null){
			person.setCreateDate(new Date());
			return dao.add(person);
		} else {
			
			
			if(person.getMobilePhone()!=null){
				if(p.getMobilePhone()!=null&&!p.getMobilePhone().equals(person.getMobilePhone())){
					str = new StringBuffer();
					str.append(person.getMobilePhone()+";").append(p.getMobilePhone());
					p.setMobilePhone(str.toString());
				} else {
					p.setMobilePhone(person.getMobilePhone());
				}
			}
			
			if(person.getEmail()!=null){
				if(p.getEmail()!=null && !p.getEmail().equals(person.getEmail())){
					str = new StringBuffer();
					str.append(person.getEmail()+";").append(p.getEmail());
					p.setEmail(str.toString());
				} else {
					p.setEmail(person.getEmail());
				}
			}
			
			p.setUpdateDate(new Date());
			
			 dao.modify(p);
			 return p.getId();
			
		}
		
	}
	
	
	
}
