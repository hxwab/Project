package csdc.service.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import csdc.model.Agency;
import csdc.model.Person;
import csdc.service.IAgencyService;

@Service
public class AgencyService extends BaseService implements IAgencyService {

	/**
	 * 机构名  -> 机构
	 */
	@Override
	public Agency findAgencyByName(String agencyName) {
		List list = dao.query("select agency from Agency agency where agency.name = '" + agencyName + "'");
		return list.isEmpty() ? null : (Agency)list.get(0);
	}

	@Override
	public boolean checkAgency(Agency agency) {
		StringBuffer hql = new StringBuffer("from Agency ag where ag.name=:name");
		Map map = new HashMap();
		map.put("name", agency.getName());
		if(agency.getCode()!=null) {
			hql.append("and ag.code=:code");
			map.put("code", agency.getCode());
		}
		List list = dao.query(hql.toString(), map);
		return list.isEmpty()? false:true;
	}

	@Override
	public String addAgency(Agency agency) {
		return dao.add(agency);
	}

	@Override
	public String addPerson(Person person) {
		StringBuffer str ;
		Map map =new HashMap();
		map.put("birthday", person.getBirthday());
		map.put("agencyName", person.getAgencyName());
		map.put("name", person.getName());
		String hql ="select p from Person p where p.name = :name and p.birthday = :birthday and p.agencyName= :agencyName";
		Person p =(Person) dao.queryUnique(hql, map);
		
		if(p==null){
			return dao.add(person);
		} else {
			if(person.getOfficePhone()!=null){
				if(p.getOfficePhone()!=null&&!p.getOfficePhone().equals(person.getOfficePhone())){
					str = new StringBuffer();
					str.append(person.getOfficePhone()+";").append(p.getOfficePhone());
					p.setOfficePhone(str.toString());
				} else {
					p.setOfficePhone(person.getOfficePhone());
				}
			}
			
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
			
			if(person.getAddress()!=null){
				if(p.getAddress()!=null && !p.getAddress().equals(person.getAddress())){
					str = new StringBuffer();
					str.append(person.getAddress()+";").append(p.getAddress());
					p.setAddress(str.toString());
				} else {
					p.setAddress(person.getAddress());
				}
			}
			
		
			if(person.getEthnic()!=null){
				p.setEthnic(person.getEthnic());
			}
			p.setIntroduction(person.getIntroduction());
			p.setUpdateDate(new Date());
			
			dao.modify(p);
			return p.getId();
			
		}
	}

	@Override
	public void updateDirector(String agencyId, Person person) {
		Agency agency = dao.query(Agency.class, agencyId);
		agency.setDirectorName(person.getName());
		agency.setDirectory(person);
		dao.modify(agency);
	}

	@Override
	public String modifyAgency(Agency oldAgency, Agency agency) {
		oldAgency.setName(agency.getName());
		oldAgency.setEnglishName(agency.getEnglishName());
		oldAgency.setType(agency.getType());
		oldAgency.setCode(agency.getCode());
		oldAgency.setAbbr(agency.getAbbr());
		oldAgency.setAddress(agency.getAddress());
		oldAgency.setPostCode(agency.getPostCode());
		oldAgency.setMobilePhone(agency.getMobilePhone());
		oldAgency.setOfficePhone(agency.getOfficePhone());
		oldAgency.setOfficeFax(agency.getOfficePhone());
		oldAgency.setEmail(agency.getEmail());
		oldAgency.setHomePage(agency.getHomePage());
		oldAgency.setIntroduction(agency.getIntroduction());
		oldAgency.setUpdateDate(new Date());
		oldAgency.setDirectory(null);
		dao.modify(agency);
		return oldAgency.getId();
	}

	@Override
	public String modifyDirector(Person oldDirector, Person director,
			String agencyName) {
		/**
		 * 如果机构负责人的名字发生变化，则新建一条人员信息，并更删除旧的机构负责人
		 * 否则只更旧的人员信息
		 */
		String directorId = oldDirector.getId();
		if(!oldDirector.getName().equals(director.getName())) {
			director.setAgencyName(agencyName);
			director.setAgency(oldDirector.getAgency());
			director.setCreateDate(new Date());
			directorId = this.addPerson(director);
			dao.delete(Person.class, oldDirector.getId());//删除旧的机构负责人
		} else {
			oldDirector.setAgencyName(agencyName);
			oldDirector.setGender(director.getGender());
			oldDirector.setAddress(director.getAddress());
			oldDirector.setEthnic(director.getEthnic());
			oldDirector.setBirthday(director.getBirthday());
			oldDirector.setOfficePhone(director.getOfficePhone());
			oldDirector.setHomePhone(director.getHomePhone());
			oldDirector.setPostCode(director.getPostCode());
			oldDirector.setEmail(director.getEmail());
			oldDirector.setMobilePhone(director.getMobilePhone());
			oldDirector.setIdcardNumber(director.getIdcardNumber());
			oldDirector.setIntroduction(director.getIntroduction());
			oldDirector.setUpdateDate(new Date());
			dao.modify(oldDirector);
		}
		return directorId;
	}

	@Override
	public int deleteAgency(List<String> entityIds) {
		try {
			for(String entityId: entityIds) {
				Agency ag = dao.query(Agency.class, entityId);
				//获取机构负责人信息
				Person director = ag.getDirectory();
				//首先删除机构中的负责人信息
				ag.setDirectory(null);
				dao.modify(ag);
				//再删除负责人的Person信息
				dao.delete(director.getId());
				//最后再删除机构信息
				dao.delete(entityId);
			}
		} catch (Exception e) {
			return 0;//删除失败
		}
		return 1;
	}

	@Override
	public Agency viewAgency(String entityId) {
		return (Agency)dao.query(Agency.class, entityId);
	}

}
