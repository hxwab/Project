package csdc.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import csdc.model.Product;
import csdc.model.ProductReview;
import csdc.model.Reward;
import csdc.service.IFinalAuditService;

@Service
public class FinalAuditService extends BaseService implements IFinalAuditService{

	
	@Override
	public Product getProduct(String productId) {
		return dao.query(Product.class, productId);
	}

	@Override
	public ProductReview getProductReview(String productId) {
		Map map = new HashMap();
		map.put("id", productId);
		String hql = "select pr from ProductReview where pr.id=:id and pr.type=0";
		return (ProductReview) dao.queryUnique(hql, map);
	}

	@Override
	public Reward getReward(String productId) {
		Map map = new HashMap();
		map.put("id", productId);
		String hql = "select r from Product p left join p.reward r where p.id=:id ";
		return (Reward) dao.queryUnique(hql, map);
	}

	@Override
	public List<Object[]> getProductInfos(List<String> productId) {
		
		Map map = new HashMap();
		List<Object[]> list= new ArrayList<Object[]>();
		Object[] o;
		String hql="select p.id,p.number,p.name,p.authorName,p.agencyName,p.researchType,r.year,r.level,r.type ,r.bonus from Product p left join p.reward r where p.id=:id order by p.number" ;

		for(String id:productId){
			map.put("id", id);
			list.addAll(dao.query(hql, map));
			map.clear();
		}
		
		return list;
	}

}
