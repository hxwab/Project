package csdc.action.expert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import csdc.action.BaseAction;
import csdc.dao.SystemOptionDao;
import csdc.model.Discipline;
import csdc.model.Expert;
import csdc.model.Person;
import csdc.model.SystemOption;
import csdc.service.IExpertService;
import csdc.service.IPersonInfoService;
import csdc.service.imp.AgencyService;
import csdc.service.imp.DisciplineService;
import csdc.tool.InputValidate;
import csdc.tool.info.GlobalInfo;
import csdc.tool.info.RightInfo;

@Component
@Scope(value="prototype")
@SuppressWarnings("unchecked")
public class ExpertAction extends BaseAction {

	private static final long serialVersionUID = -7305780982528326793L;
	protected static final String DATE_FORMAT = "yyyy-MM";// 列表时间格式

	private static final String HQL = "select p.name, p.agencyName, e.specialityTitle, dis.name, e.addYears, e.id from Expert e left join e.person p left join e.discipline dis where 1=1";
	private static final String PAGE_NAME = "expertPage";
	private static final String PAGE_BUFFER_ID = "e.id";//缓存id
	private static final String column[] = {
		"p.name",
		"p.agencyName",
		"e.specialityTitle",
		"dis.name",
		"e.addYears"
	};// 排序用的列
	
	private final static String[] SEARCH_CONDITIONS = new String[]{
		"LOWER(p.name) like :keyword",
		"LOWER(p.agencyName) like :keyword",
		"LOWER(e.specialityTitle) like :keyword",
		"LOWER(dis.name) like :keyword",
		"LOWER(e.addYears) like :keyword"
	};//初级检索所用条件范围
	
	private Expert expert;
	private Person person;
	private Discipline discipline;
	private IExpertService expertService;
	private IPersonInfoService personInfoService;
	private DisciplineService disciplineService;
	private AgencyService agencyService;
	private SystemOptionDao soDao;
	private List<SystemOption> ethnics;
	private List<Discipline> disciplines;
	private List<SystemOption> titles;
	
	private InputValidate inputValidate = new InputValidate();//校验工具类
	
	public void validateAdd() {
		validateBasicInfo(person);
		validateExpertInfo(expert);
		validateContactInfo(person);
		validateAddress(person);
	}
	
	@Override
	public String toAdd() {
		ethnics = soDao.queryChildren("GB3304-91");
		disciplines = disciplineService.getAllDiscipline();
		titles = soDao.queryChildren("GBT8561-2001");
		return SUCCESS;
	}

	@Transactional
	@Override
	public String add() {
		Person existPerson = personInfoService.findPerson(person.getName(), person.getAgencyName(), person.getBirthday());
		if (existPerson != null) {
			Expert existExpert = expertService.findExpertByPersonId(existPerson.getId());
			if (existExpert != null) {
				this.addFieldError("expert", "专家已存在");
				return ERROR;
			}
			expert.setPerson(existPerson);
		} else {
			person.setAgency(agencyService.findAgencyByName(person.getAgencyName()));
			personInfoService.addPerson(person);
			expert.setPerson(person);
		}
		expert.setDiscipline(disciplineService.getDiscipline(discipline.getName()));
		entityId = expertService.addExpert(expert);
		return SUCCESS;
	}

	@Transactional
	@Override
	public String delete() {
		expertService.deleteExperts(entityIds);
		return SUCCESS;
	}

	public void validateModify() {
		validateBasicInfo(person);
		validateExpertInfo(expert);
		validateContactInfo(person);
		validateAddress(person);
	}
	
	@Override
	public String toModify() {
		expert = expertService.findExpert(entityId);
		if (expert == null) {
			jsonMap.put(GlobalInfo.ERROR_INFO, "该专家已不存在");
			return INPUT;
		} else { 
			person = expert.getPerson();
		}
		ethnics = soDao.queryChildren("GB3304-91");
		disciplines = disciplineService.getAllDiscipline();
		titles = soDao.queryChildren("GBT8561-2001");
		discipline = expert.getDiscipline();
		return SUCCESS;
	}

	@Transactional
	@Override
	public String modify() {
		Expert orginExpert = expertService.findExpert(entityId);
		if (orginExpert == null) {
			jsonMap.put(GlobalInfo.ERROR_INFO, "该专家已不存在");
			return INPUT;
		}
		
		personInfoService.modifyPersonInfo(person, orginExpert.getPerson());
		if (discipline.getName() != null && !discipline.getName().trim().equals("")) {
			orginExpert.setDiscipline(disciplineService.getDiscipline(discipline.getName()));
		}
		expertService.modifyExpert(expert, orginExpert);
		return SUCCESS;
	}

	@Override
	public String view() {
		expert = expertService.findExpert(entityId);
		if (expert == null) {
			jsonMap.put(GlobalInfo.ERROR_INFO, "该专家已不存在");
			return INPUT;
		} else { 
			person = expert.getPerson();
		}
		
		jsonMap.put("expert", expert);
		jsonMap.put("person", person);
		return SUCCESS;
	}

	@Override
	public String toView() {
		return SUCCESS;
	}
	
	public void validateToView() {
		if (entityId == null || entityId.trim().isEmpty()) {
			this.addFieldError(GlobalInfo.ERROR_INFO, RightInfo.ERROR_VIEW_NULL);
		}
	}
	
	/**
	 * 查看信息时校验ID不得为空
	 */
	public void validateView() {
		if (entityId == null || entityId.trim().isEmpty()) {
			this.addFieldError(GlobalInfo.ERROR_INFO, RightInfo.ERROR_VIEW_NULL);
			jsonMap.put(GlobalInfo.ERROR_INFO, RightInfo.ERROR_VIEW_NULL);
		}
	}

	@Override
	public Object[] simpleSearchCondition() {
		StringBuffer hql = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		hql.append(HQL);
		if (keyword != null && !keyword.trim().isEmpty()) {
			boolean flag = false;
			String[] sc = searchConditions();
			for (int i = 0; !flag && i < sc.length; i++) {
				if (searchType == i) {
					hql.append(" and ").append(sc[i]);
					flag = true;
				}
			}
			map.put("keyword", "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%");
		}
		return new Object[]{
			hql.toString(),
			map,
			0,
			null
		};
	}
	
	/**
	 * 校验人员基本信息
	 * @param person 人员
	 */
	public void validateBasicInfo(Person person) {
		if (person.getName() == null || person.getName().trim().isEmpty()){
			this.addFieldError("person.name", "基本信息 —— 姓名不应为空");
		} else if(!Pattern.matches("^.{1,50}$", person.getName().trim())){
			this.addFieldError("person.name", "基本信息 —— 中文名不合法");
		}
		if (person.getIdcardNumber().trim().length() > 18){
			this.addFieldError("person.idcardNumber", "基本信息 —— 证件号过长");
		}		
		if (person.getEthnic() != null && person.getEthnic().trim().length() > 20){
			this.addFieldError("person.ethnic", "基本信息 —— 民族过长");
		}		
		if (person.getBirthday() == null) {
			this.addFieldError("person.birthday", "基本信息 —— 出生日期不能为空");
		}
		if (person.getBirthday() != null && person.getBirthday().compareTo(new Date()) > 0) {
			this.addFieldError("person.birthday", "基本信息 —— 不合理的出生日期");
		}
		if (person.getAgencyName() == null || person.getAgencyName().trim().isEmpty()){
			this.addFieldError("person.agencyName", "所在单位名称不应为空");
		}
	}
	
	/**
	 * 校验人员联系信息
	 */
	public void validateContactInfo(Person person) {
		if (person.getHomePhone() != null && !inputValidate.checkFixedphone(person.getHomePhone().trim())){
			this.addFieldError("person.homePhone", "联系信息 —— 住宅电话不合法");
		}
		if (person.getOfficePhone() != null && !inputValidate.checkFixedphone(person.getOfficePhone().trim())){
			this.addFieldError("person.officePhone", "联系信息 —— 办公电话不合法");
		}
		if (person.getEmail() == null || person.getEmail().trim().isEmpty()){
			this.addFieldError("person.email", "联系信息 —— 邮箱不应为空");
		} else {
			String[] mail = person.getEmail().split(";");
			for (int i = 0; i < mail.length; i++) {
				String	email = mail[i];
				if(!inputValidate.checkEmail(email.trim())){
					this.addFieldError("person.email", "联系信息 —— 邮箱不合法");
				}
			}
		}
		if (person.getMobilePhone() != null && !inputValidate.checkCellphone(person.getMobilePhone().trim())){
			this.addFieldError("person.mobilePhone", "联系信息 —— 移动电话不合法");
		}
	}
	
	/**
	 * 校验人员地址信息
	 * @param person
	 */
	public void validateAddress(Person person) {
		if (person.getPostCode() != null && !inputValidate.checkPostcode(person.getPostCode())){
			this.addFieldError("person.postCode", "联系信息 —— 邮编不合法");
		}
		if (person.getAddress() != null && person.getAddress().trim().length() > 400){
			this.addFieldError("person.address", "联系信息 —— 地址过长");
		}
	}
	
	/**
	 * 校验学科门类信息
	 * @param expert
	 */
	public void validateExpertInfo(Expert expert) {
		if (discipline == null || "".equals(discipline.getName())) {
			this.addFieldError("expert.discipline", "专家信息 —— 学科门类不能为空");
		} else if (disciplineService.getDiscipline(discipline.getName()) == null) {
			this.addFieldError("expert.discipline", "专家信息 —— 学科门类不合法");
		}
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
		return ExpertAction.PAGE_BUFFER_ID;
	}

	@Override
	public String[] sortColumn() {
		return column;
	}

	@Override
	public String dateFormat() {
		return DATE_FORMAT;
	}
	
	public String[] searchConditions() {
		return SEARCH_CONDITIONS;
	}
	
	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}
	
	public List<SystemOption> getEthnics() {
		return ethnics;
	}

	public void setEthnics(List<SystemOption> ethnics) {
		this.ethnics = ethnics;
	}
	
	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public List<SystemOption> getTitles() {
		return titles;
	}

	public void setTitles(List<SystemOption> titles) {
		this.titles = titles;
	}

	public IExpertService getExpertService() {
		return expertService;
	}

	@Resource
	public void setExpertService(IExpertService expertService) {
		this.expertService = expertService;
	}
	
	public IPersonInfoService getPersonInfoService() {
		return personInfoService;
	}

	@Resource
	public void setPersonInfoService(IPersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}
	
	public DisciplineService getDisciplineService() {
		return disciplineService;
	}

	@Resource
	public void setDisciplineService(DisciplineService disciplineService) {
		this.disciplineService = disciplineService;
	}
	
	public AgencyService getAgencyService() {
		return agencyService;
	}

	@Resource
	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}
	
	public SystemOptionDao getSoDao() {
		return soDao;
	}

	@Resource
	public void setSoDao(SystemOptionDao soDao) {
		this.soDao = soDao;
	}

}
