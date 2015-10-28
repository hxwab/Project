package csdc.service.imp;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import csdc.model.Account;
import csdc.model.Article;
import csdc.model.SystemOption;
import csdc.service.IAppealService;

@Service
public class AppealService extends BaseService implements IAppealService {

	@Override
	public String addAppleal(Article article, Map map) {
		Account account = (Account) map.get("account");
		String typeId = map.get("type").toString();
		String author = account.getPerson().getName();
		SystemOption systemOption = getSystemOptionById(typeId);
		
		article.setAuthor(author);
		article.setCreateDate(new Date());
		article.setViewCount(0);
		article.setSystemOption(systemOption);
		article.setIsOpen(0);
		return dao.add(article);
	}


	private SystemOption getSystemOptionById(String typeId) {
		
		return dao.query(SystemOption.class, typeId);
	}
}
