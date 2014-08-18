package nc.vo.zmpub.formula.calc;

import java.io.Serializable;
import java.util.Map;

import nc.vo.pub.SuperVO;
import nc.vo.scm.pattern.context.IPrecision;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * ������Ϣ--�������ݷ�װ
 * @author zpm
 *
 */
public class BeforeCalcAllInfomation extends SuperVO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4693316679536478826L;	
	
	private Integer ibusinessobj;//ҵ�����
	
	/**
	 * ִ�й�ʽ
	 */
	private String expresscode=null;
	/**
	 * ִ�й�ʽ������ʾ����
	 */
	private String expressname=null;
	
	
	public String getExpresscode() {
		return expresscode;
	}

	public void setExpresscode(String expresscode) {
		this.expresscode = expresscode;
	}

	public String getExpressname() {
		return expressname;
	}

	public void setExpressname(String expressname) {
		this.expressname = expressname;
	}



	private ReportBaseVO[] calcobjectdetail = null;//��ʽִ�ж���
	
	
	private Map<String, FormulaDefineVO>  formula_map = null;//��ʽ����������<��ʽ���롢��ʽ����VO>
	
	private IPrecision precision = null;//���þ���
	
	/********************************************************************/



	public ReportBaseVO[] getCalcobjectdetail() {
		return calcobjectdetail;
	}

	public void setCalcobjectdetail(ReportBaseVO[] calcobjectdetail) {
		this.calcobjectdetail = calcobjectdetail;
	}


	
	




	public Map<String, FormulaDefineVO> getFormula_map() {
		return formula_map;
	}

	public void setFormula_map(Map<String, FormulaDefineVO> formula_map) {
		this.formula_map = formula_map;
	}
	
	public IPrecision getPrecision() {
		return precision;
	}
	
	public void setPrecision(IPrecision precision) {
		this.precision = precision;
	}



	public Integer getIbusinessobj() {
		return ibusinessobj;
	}



	public void setIbusinessobj(Integer ibusinessobj) {
		this.ibusinessobj = ibusinessobj;
	}



	@Override
	public String getPKFieldName() {
		return null;
	}



	@Override
	public String getParentPKFieldName() {
		return null;
	}



	@Override
	public String getTableName() {
		return null;
	}
}
