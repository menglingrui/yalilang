package nc.vo.zmpub.pub.check;

import nc.vo.pub.SuperVO;
/**
 * ����У��vo
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
	 * ��Ψһ�Ե��ֶ� pk_invcl:pk_invmandoc
	 */
	private String  unique;
	/**
	 * �����ֶ� ������࣬��Ŀ���������¼���ϵ�ģ�У����ĳ�������£����ܽ���   pk_invcl
	 */     
	private String  iscontain;
	/**
	 * У��ı�
	 */
	private String  checktable;
	/**
	 * year:month:version  ��ͷ���Ʒ�Χ
	 */
	private String  hcontrol;
	/**
	 * year:month:version  ������Ʒ�Χ
	 */
	private String  bcontrol;
	/**
	 * ��ͷȫ·��
	 */
	private String  hvo;
	/**
	 * ����ȫ·��
	 */
	private String  bvo;
	/**
	 * У����
	 */
	private String checkclass;
	/**
	 * ģ����
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
