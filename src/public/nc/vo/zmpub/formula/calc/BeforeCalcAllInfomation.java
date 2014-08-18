package nc.vo.zmpub.formula.calc;

import java.io.Serializable;
import java.util.Map;

import nc.vo.pub.SuperVO;
import nc.vo.scm.pattern.context.IPrecision;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 计算信息--计算数据封装
 * @author zpm
 *
 */
public class BeforeCalcAllInfomation extends SuperVO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4693316679536478826L;	
	
	private Integer ibusinessobj;//业务对象
	
	/**
	 * 执行公式
	 */
	private String expresscode=null;
	/**
	 * 执行公式中文显示名称
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



	private ReportBaseVO[] calcobjectdetail = null;//公式执行对象
	
	
	private Map<String, FormulaDefineVO>  formula_map = null;//公式定义项内容<公式编码、公式定义VO>
	
	private IPrecision precision = null;//设置精度
	
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
