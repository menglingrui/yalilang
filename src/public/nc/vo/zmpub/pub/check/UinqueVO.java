package nc.vo.zmpub.pub.check;

import nc.vo.pub.SuperVO;
/**
 * 数据校验vo
 * @author mlr
 *
 */
public class UinqueVO extends SuperVO{
//	unique varchar(1000),
//	iscontain char(20),
//	checktable char(20),
//	hcontrol varchar(1000),
//	bcontrol varchar(1000),
//    hvo varchar(1000),
//    bvo varchar(1000)
	/**
	 * 交唯一性的字段 pk_invcl:pk_invmandoc
	 */
	private String  unique;
	/**
	 * 交叉字段 存货分类，项目档案有上下级关系的，校验在某个条件下，不能交叉   pk_invcl
	 */     
	private String  iscontain;
	/**
	 * 校验的表
	 */
	private String  checktable;
	/**
	 * year:month:version  表头控制范围
	 */
	private String  hcontrol;
	/**
	 * year:month:version  表体控制范围
	 */
	private String  bcontrol;
	/**
	 * 表头全路径
	 */
	private String  hvo;
	/**
	 * 表体全路径
	 */
	private String  bvo;
	/**
	 * 校验类
	 */
	private String checkclass;
	/**
	 * 模块名
	 */
	private String checkmodule;
	
	

	public String getCheckmodule() {
		return checkmodule;
	}

	public void setCheckmodule(String checkmodule) {
		this.checkmodule = checkmodule;
	}

	public String getCheckclass() {
		return checkclass;
	}

	public void setCheckclass(String checkclass) {
		this.checkclass = checkclass;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	public String getIscontain() {
		return iscontain;
	}

	public void setIscontain(String iscontain) {
		this.iscontain = iscontain;
	}

	public String getChecktable() {
		return checktable;
	}

	public void setChecktable(String checktable) {
		this.checktable = checktable;
	}

	public String getHcontrol() {
		return hcontrol;
	}

	public void setHcontrol(String hcontrol) {
		this.hcontrol = hcontrol;
	}

	public String getBcontrol() {
		return bcontrol;
	}

	public void setBcontrol(String bcontrol) {
		this.bcontrol = bcontrol;
	}

	public String getHvo() {
		return hvo;
	}

	public void setHvo(String hvo) {
		this.hvo = hvo;
	}
   

	public String getBvo() {
		return bvo;
	}

	public void setBvo(String bvo) {
		this.bvo = bvo;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
