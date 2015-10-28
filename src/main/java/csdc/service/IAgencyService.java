package csdc.service;

import java.util.List;

import csdc.model.Agency;
import csdc.model.Person;

public interface IAgencyService {

	public Agency findAgencyByName(String agencyName);
	/**
	 * 校验机构是否已存在 以机构名称和机构代码(如果不为空)作为唯一机构的代表
	 * @param agency 待校验的机构
	 * @return
	 */
	public boolean checkAgency(Agency agency);
	/**
	 * 添加机构
	 * @param agency 要添加的机构
	 * @return 添加成功后的机构id
	 */
	public String addAgency(Agency agency);
	/**
	 * 添加机构负责人，采用合并策略，若数据库中存在该人员，则更新信息，否则直接添加
	 * @param person 要添加的机构负责人
	 * @return 添加后的负责人Id
	 */
	public String addPerson(Person person);
	/**
	 * 更新机构负责人信息，在机构中添加负责人信息(负责人姓名和负责人Id)
	 * @param agencyId 要更新的机构Id
	 * @param person 要添加的负责人信息
	 */
	public void updateDirector(String agencyId, Person person);
	/**
	 * 修改机构信息
	 * @param oldAgency 旧的机构对象
	 * @param agency 新的机构对象
	 * @return 机构Id
	 */
	public String modifyAgency(Agency oldAgency, Agency agency);
	/**
	 * 更新机构负责人信息
	 * @param oldDirector 旧的机构负责人
	 * @param director 新的机构负责人
	 * @param agencyName 新的机构名称 
	 * @return 更新后的机构负责人Id
	 */
	public String modifyDirector(Person oldDirector, Person director,
			String agencyName);
	/**
	 * 删除机构，会连同本机构的负责人信息一起删除
	 * @param entityId
	 * @return 是否删除成功的标志
	 */
	public int deleteAgency(List<String> entityIds);
	/**
	 * 查看机构信息
	 * @param entityId 机构Id
	 * @return 要查看的机构对象
	 */
	public Agency viewAgency(String entityId);

	
}
