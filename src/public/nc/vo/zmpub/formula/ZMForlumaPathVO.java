package nc.vo.zmpub.formula;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * ¹«Ê½Â·¾¶vo
 * @author mlr
 */
public class ZMForlumaPathVO extends SuperVO{
	private static final long serialVersionUID = 4848111746922445372L;
	private String id;
	private String vfilename;
	private String vfilepath;
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private Integer dr;
	private UFDateTime ts;

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVfilename() {
		return vfilename;
	}

	public void setVfilename(String vfilename) {
		this.vfilename = vfilename;
	}

	public String getVfilepath() {
		return vfilepath;
	}

	public void setVfilepath(String vfilepath) {
		this.vfilepath = vfilepath;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "id";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "zmpub_forlumapath";
	}

}
