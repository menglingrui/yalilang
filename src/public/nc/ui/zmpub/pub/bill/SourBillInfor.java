package nc.ui.zmpub.pub.bill;
/**
 * ��Դ������Ϣע��
 * @author Administrator
 *
 */
public class SourBillInfor {
	/**
	 * ��ͷ����
	 */
	private String headtable;
	/**
	 * �����������
	 */
	private String[] bodytables;
	/**
	 * ��ͷ����
	 */
	private String headpk;
	/**
	 * �������������
	 */
	private String[] bodypks;

	public String getHeadtable() {
		return headtable;
	}

	public void setHeadtable(String headtable) {
		this.headtable = headtable;
	}

	public String[] getBodytables() {
		return bodytables;
	}

	public void setBodytables(String[] bodytables) {
		this.bodytables = bodytables;
	}

	public String getHeadpk() {
		return headpk;
	}

	public void setHeadpk(String headpk) {
		this.headpk = headpk;
	}

	public String[] getBodypks() {
		return bodypks;
	}

	public void setBodypks(String[] bodypks) {
		this.bodypks = bodypks;
	}

}
