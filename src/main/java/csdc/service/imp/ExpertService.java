package csdc.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import csdc.model.Expert;
import csdc.service.IExpertService;

@Service
public class ExpertService extends BaseService implements IExpertService {
	
	@Override
	public String addExpert(Expert expert) {
		expert.setNumber(generateNewNumber());
		String expertId = (String) dao.add(expert);
		return expertId;
	}
	
	@Override
	public Expert findExpert(String entityId) {
		return dao.query(Expert.class, entityId);
	}
	
	@Override
	public Expert findExpertByPersonId(String personId) {
		return (Expert)dao.queryUnique("select e from Expert e where e.person.id='" + personId + "'");
	}
	
	@Override
	public Expert findExpertByNumber(String number) {
		return (Expert)dao.queryUnique("select e from Expert e where e.number='" + number + "'");
	}

	@Override
	public Expert findExpertByIdCard(String idcardNumber) {
		return (Expert) dao.queryUnique("select e from Expert e left join e.person p where p.idcardNumber= '" + idcardNumber + "'");
	}
	
	@Override
	public Expert findExpertByIdCardAndName(String idcardNumber, String name) {
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("idcardNumber", idcardNumber);
		paraMap.put("name", name);
		return (Expert) dao.queryUnique("select e from Expert e left join e.person p where p.idcardNumber=:idcardNumber and p.name=:name", paraMap);
	}
	
	@Override
	public void deleteExperts(List<String> expertIds) {
		if (expertIds != null) {
			for (int i = 0; i < expertIds.size(); i++) {
				dao.delete(Expert.class, expertIds.get(i));
			}
		}
	}
	
	@Override
	public void modifyExpert(Expert newExpert, Expert oldExpert) {
		oldExpert.setSpecialityTitle(newExpert.getSpecialityTitle());
		oldExpert.setResearchField(newExpert.getResearchField());
		oldExpert.setLastDegree(newExpert.getLastDegree());
		oldExpert.setLastEducation(newExpert.getLastEducation());
		oldExpert.setNote(newExpert.getNote());
		dao.modify(oldExpert);
	}
	
	@Override
	public String generateNewNumber() {
		String maxNumber = dao.query("select max(e.number) from Expert e").toString();
		Matcher matcher = Pattern.compile("[1-9]+").matcher(maxNumber);
		if (!matcher.find()) {
			return null;
		}
		int nextNumber = Integer.parseInt(matcher.group(0).trim()) + 1;
		int len=0;
		for (int tmp = nextNumber; tmp > 0 ; len++) {
			tmp = nextNumber / 10;
		}
		String zero = "";
		for (int i=0; i<7-len ; i++) {
			zero += "0"; 
		}
		
		return "E" + zero + nextNumber;
	}
	
}
