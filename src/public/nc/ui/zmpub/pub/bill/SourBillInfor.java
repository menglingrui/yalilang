package nc.ui.zmpub.pub.bill;
/**
 * 来源单据信息注册
 * @author Administrator
 *
 */
public class SourBillInfor {
	/**
	 * 表头表名
	 */
	private String headtable;
	/**
	 * 表体表名数组
	 */
	private String[] bodytables;
	/**
	 * 表头主键
	 */
	private String headpk;
	/**
	 * 表体表主键数组
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
