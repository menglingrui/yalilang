package nc.bs.zmpub.formula.calc;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.BusinessException;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.IAllSummary;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 计算通类
 * 
 * @author mlr
 */
@SuppressWarnings("all")

public class CalcCommon {
    /**
     * 公式变量参数
     */
	private FormulaDefineVO formulaItem = null;// 公式类信息
	/**
	 * 公式取值数据
	 */
	private ReportBaseVO[] vos=null;
	public CalcCommon(ReportBaseVO[] vos){
		
		this.vos=vos;
	}
	

	public List doCalcDouble() throws BusinessException {
		List list = null;
		try {
			if (formulaItem == null) {
				return null;
			}
			list = new ArrayList();
			String calclassname = formulaItem.getClassname();
			String invcode = formulaItem.getInvcode();// 存货编码
			String name = formulaItem.getName();
			String iqushutype = formulaItem.getIqushutype();
			String attriName = formulaItem.getAttriname();
			Object instance = Class.forName(calclassname).newInstance();
			if (instance instanceof IAllSummary) {// 整单--明细
				ReportBaseVO[] details = vos;
				if (details != null && details.length > 0) {
					for (ReportBaseVO detail : details) {
						IAllSummary notSum = (IAllSummary) instance;
						Object v = notSum.calculateInvRow(detail, attriName);
						list.add(v);
					}
				}
			} 
		} catch (Exception e) {
			throw new BusinessException(e.getMessage() + "计算错误");
		}
		return list;
	}


	public ReportBaseVO[] getVos() {
		return vos;
	}


	public void setVos(ReportBaseVO[] vos) {
		this.vos = vos;
	}


	public FormulaDefineVO getFormulaItem() {
		return formulaItem;
	}

	public void setFormulaItem(FormulaDefineVO formulaItem) {
		this.formulaItem = formulaItem;
	}

}