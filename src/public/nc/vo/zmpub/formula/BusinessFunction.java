package nc.vo.zmpub.formula;

import nc.vo.pub.SuperVO;

/**
 * ҵ����ע��vo��
 * 
 * @author zpm
 * 
 */
public class BusinessFunction extends SuperVO {

	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;

	public String id = null;

	public String vitemcode = null;// ���ο���ע�ắ��code

	public String vitemname = null; // ���ο���ע�ắ������

	public String resid = null;//��������

	public String itemClass = null;// ���ο���ע�ắ������

	public int paramsCount = 0; // ��������Ϊ0
	
	public String readme = null;//����˵����

	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}

	public String getPKFieldName() {
		return "id";
	}

	public String getParentPKFieldName() {
		return "";
	}

	public String getTableName() {
		return "zz_innerfunction1";
	}

	public String toString() {
		return String.valueOf(vitemname);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}

	public int getParamsCount() {
		return paramsCount;
	}

	public void setParamsCount(int paramsCount) {
		this.paramsCount = paramsCount;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	public String getVitemcode() {
		return vitemcode;
	}

	public void setVitemcode(String vitemcode) {
		this.vitemcode = vitemcode;
	}

	public String getVitemname() {
		return vitemname;
	}

	public void setVitemname(String vitemname) {
		this.vitemname = vitemname;
	}
}
