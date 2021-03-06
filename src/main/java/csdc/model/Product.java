package csdc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_PRODUCT")
@JsonIgnoreProperties({"password"})
public class Product implements Serializable {
	private static final long serialVersionUID = 7471030555609919429L;

	@Id	
	@Column(name="C_ID", unique = true, nullable = false, length=40)
	@GeneratedValue(generator="idGenerator")
    @GenericGenerator(name="idGenerator", strategy="uuid")
	private String id; //成果id（PK）
	
	@Column(name="c_number", length=20)
	private String number; //成果编号
	
	@Column(name="c_review_number", length=20)
	private String reviewNumber; //评审编号
	
	@Column(name="C_TYPE", length=20)
	private String type; //成果类型[著作; 单篇论文; 系列论文; 调研报告]
	
	@Column(name="c_name", length=800, nullable = false)
	private String name; //名称
	
	@Column(name="c_research_type", length=20)
	private String researchType; //研究类型[基础类; 应用对策类]
	
	@ManyToOne
	@JoinColumn(name="C_DISCIPLINE_ID")
	private Discipline discipline; //学科门类id（FK）
	
	@Column(name="C_FILE", length=100)
	private String file; //成果电子档文件
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="c_submit_datetime")
	private Date submitDate; //提交时间
	
	@ManyToOne
	@JoinColumn(name="C_AUTHOR_ID")
	private Person author; //作者id（FK）
	
	@Column(name="C_AUTHOR_NAME", length=200)
	private String authorName; //第一作者姓名
	
	@Column(name="c_major", length=200)
	private String major; //第一作者专业
	
	@ManyToOne
	@JoinColumn(name="C_AGENCY_ID")
	private Agency agency; //所属机构id（FK）
	
	@Column(name="C_AGENCY_NAME", length=200)
	private String agencyName; //机构名称
	
	@Column(name="C_SUBMIT_STATUS")  
	private Integer submitStatus; //提交状态[0：默认；1：暂存；2：提交]
	
	@Column(name="c_publish_name", length=100)
	private String publishName; //出版社或发表刊物名称
	
	@Column(name="c_publish_level", length=100)
	private String publishLevel; //出版社或发表刊物级别[国家级出版社; 权威报刊; 核心报刊]
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="c_publish_date")
	private Date publishDate; //出版社或发表刊物发表时间
	
	@Column(name="c_abstract", length=2000)
	private String abstractStr; //成果概要
	
	@Column(name="c_social_effect", length=5000)
	private String socialEffect; //成果社会反映
	
	@Column(name="c_introduction", length=1000)
	private String introduction; //成果简介
	
	@Column(name="c_recommendation", length=400)
	private String recommendation; //所在单位或省级学会或市州社科联推荐意见
	
	@Column(name="c_status")  
	private Integer status; //评审状态[0:默认; 1:申报; 2:高校审核; 3:省社科联申报审核; 4:专家初评; 5:初评审核; 6:专家复评; 7:社科联终审]
	
	@ManyToOne
	@JoinColumn(name="c_chief_review_id")
	private Person chiefReviewer; //复评主审专家id(FK)
	
	@Column(name="c_university_audit_result")  
	private Integer universityAuditResult; //高校审核结果[0:默认; 1:不同意; 2:同意]
	
	@Column(name="c_university_audit_opinion", length=500)
	private String universityAuditOpinion; //高校审核意见
	
	@Column(name="c_hsas_apply_audit_result")  
	private Integer hsasApplyAuditResult; //省社科联申报审核结果[0:默认; 1:不同意; 2:同意]
	
	@Column(name="c_hsas_apply_audit_opinion", length=500)
	private String hsasApplyAuditOpinion; //省社科联申报审核意见
	
	@Column(name="c_hsas_review_audit_result")  
	private Integer hsasReviewAuditResult; //省社科联初评审核结果[0:默认; 1:不同意; 2:同意]
	
	@Column(name="c_hsas_final_audit_result")  
	private Integer hsasFinalAuditResult; //省社科联终审审核结果[0:默认; 1:不同意; 2:同意]
	
	@Column(name="c_hsas_final_audit_opinion", length=500)
	private String hsasFinalAuditOpinion; //省社科联终审审核意见
	
	@Column(name="c_apply_year", length=40)
	private String applyYear; //省社科联终审审核意见
	
	@Column(name="c_average_score")  
	private Integer averageScore; //初评平均分
	
	@ManyToOne
	@JoinColumn(name="c_reward_id")
	private Reward reward; //奖励等级(FK)

	@Column(name="C_NOTE", length=800)
	private String note; //备注
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="c_create_datetime")
	private Date createDate; //创建时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="c_updatetime_datetime")
	private Date updateDate; //更新时间
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	 @JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public Integer getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(Integer submitStatus) {
		this.submitStatus = submitStatus;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(String reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResearchType() {
		return researchType;
	}

	public void setResearchType(String researchType) {
		this.researchType = researchType;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public String getPublishLevel() {
		return publishLevel;
	}

	public void setPublishLevel(String publishLevel) {
		this.publishLevel = publishLevel;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getAbstractStr() {
		return abstractStr;
	}

	public void setAbstractStr(String abstractStr) {
		this.abstractStr = abstractStr;
	}

	public String getSocialEffect() {
		return socialEffect;
	}

	public void setSocialEffect(String socialEffect) {
		this.socialEffect = socialEffect;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Person getChiefReviewer() {
		return chiefReviewer;
	}

	public void setChiefReviewer(Person chiefReviewer) {
		this.chiefReviewer = chiefReviewer;
	}

	public Integer getUniversityAuditResult() {
		return universityAuditResult;
	}

	public void setUniversityAuditResult(Integer universityAuditResult) {
		this.universityAuditResult = universityAuditResult;
	}

	public String getUniversityAuditOpinion() {
		return universityAuditOpinion;
	}

	public void setUniversityAuditOpinion(String universityAuditOpinion) {
		this.universityAuditOpinion = universityAuditOpinion;
	}

	public Integer getHsasApplyAuditResult() {
		return hsasApplyAuditResult;
	}

	public void setHsasApplyAuditResult(Integer hsasApplyAuditResult) {
		this.hsasApplyAuditResult = hsasApplyAuditResult;
	}

	public String getHsasApplyAuditOpinion() {
		return hsasApplyAuditOpinion;
	}

	public void setHsasApplyAuditOpinion(String hsasApplyAuditOpinion) {
		this.hsasApplyAuditOpinion = hsasApplyAuditOpinion;
	}

	public Integer getHsasReviewAuditResult() {
		return hsasReviewAuditResult;
	}

	public void setHsasReviewAuditResult(Integer hsasReviewAuditResult) {
		this.hsasReviewAuditResult = hsasReviewAuditResult;
	}

	public Integer getHsasFinalAuditResult() {
		return hsasFinalAuditResult;
	}

	public void setHsasFinalAuditResult(Integer hsasFinalAuditResult) {
		this.hsasFinalAuditResult = hsasFinalAuditResult;
	}

	public String getHsasFinalAuditOpinion() {
		return hsasFinalAuditOpinion;
	}

	public void setHsasFinalAuditOpinion(String hsasFinalAuditOpinion) {
		this.hsasFinalAuditOpinion = hsasFinalAuditOpinion;
	}

	public Integer getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Integer averageScore) {
		this.averageScore = averageScore;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public String getApplyYear() {
		return applyYear;
	}

	public void setApplyYear(String applyYear) {
		this.applyYear = applyYear;
	}
	
}
