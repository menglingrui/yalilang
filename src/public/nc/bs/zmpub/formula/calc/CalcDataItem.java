package nc.bs.zmpub.formula.calc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.calc.BeforeCalcAllInfomation;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * ͨ����ʽ������������м���
 * @author mlr
 */
public class CalcDataItem {
	/**
	 * ���ʽ�����ֶ�
	 */
	public static String expresscodeName="expresscode";
	/**
	 * ���ʽ������ʾ�����ֶ�
	 */
	public static String expresscodeNameCH="expressname";
	
	public CalcDataItem(){
		
	}
	BeforeCalcAllInfomation beforeinfo=null;
	
	public CalcDataItem(BeforeCalcAllInfomation beforeinfo) {
		this.beforeinfo=beforeinfo;
	}


	private FormulaAnalytical analytical = null;//��ʽ������
	//����
	public List<UFDouble> doCalc()throws BusinessException{
		Map<String, FormulaDefineVO> formula_map = beforeinfo.getFormula_map();
		ReportBaseVO[] vos=beforeinfo.getCalcobjectdetail();
		/**
		 * ��Ž����
		 */
		List<UFDouble> result=new ArrayList<UFDouble>();
		String expresscode=beforeinfo.getExpresscode();
		String expressname=beforeinfo.getExpressname();
	    result = getAnalytical().computeFormula(expressname,expresscode,formula_map);
		return result;
	}
	public FormulaAnalytical getAnalytical() {
		analytical = new FormulaAnalytical(beforeinfo);
		return analytical;
	}

	public void setAnalytical(FormulaAnalytical analytical) {
		this.analytical = analytical;
	}
}
