package nc.bs.zmpub.formula.calc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.itf.uif.pub.IUifService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.formulaset.function.PostfixMathCommandI;
import nc.vo.pub.lang.UFDouble;
import nc.vo.xew.pub.SemPubVO;
import nc.vo.zmpub.formula.BusinessFunction;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.calc.BeforeCalcAllInfomation;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 公式解析类
 * @author zpm
 */
public class FormulaAnalytical {
	
	public FormulaAnalytical(){
		
	}
	
	private CalcCommon commonCalc = null;//计算通类
	  /**
     * 公式变量参数
     */
	private FormulaDefineVO formulaItem = null;// 公式类信息
	/**
	 * 公式取值数据
	 */
	private ReportBaseVO[] vos=null;
	
	private FormulaParse parser = new FormulaParse();
	public FormulaAnalytical(FormulaDefineVO formulaItem,ReportBaseVO[] vos){
	 this.formulaItem=formulaItem;
	 this.vos=vos;
	}
	

	
	public FormulaAnalytical(BeforeCalcAllInfomation beforeinfo) {
		this.vos=beforeinfo.getCalcobjectdetail();
	}



	/**
	 * 解析表达式
	 * @param lstExpress表达式
	 * @param map<公式编码、公司定义VO>
	 * @throws BusinessException
	 */
	public List<UFDouble> computeFormula(String expressname,String express,Map<String, FormulaDefineVO> formula_map) 
			throws BusinessException {
		// 增加业务函数
		addBusinessFunction();
		
		// 给解析器设置公式
		List<UFDouble> list = new ArrayList<UFDouble>();
		String [] newExpress = express.split(";");
		Map<String, List> tempValue = new HashMap<String, List>();
		if(newExpress.length == 1){
			list =   computeFormula(tempValue, expressname, express, formula_map);
		}else if (newExpress.length > 1){
			for(int i=0;i<newExpress.length;i++){
				String rowExpress = newExpress[i];
				if(i< newExpress.length-1){
					int offIndex = rowExpress.indexOf("->");
					String leftName = rowExpress.substring(0, offIndex);
					if(offIndex <0){
						throw new BusinessException("多行公示，非最后一行必须制定临时变量,例 temp ->*** ;");
					}
					List<UFDouble>  rowList = computeFormula(tempValue, expressname, rowExpress, formula_map);
					tempValue.put(leftName, rowList);
				}else{
					list =  computeFormula(tempValue, expressname, rowExpress, formula_map);
				}
			}			
		}
		return list;
	}
	/**
	 * 
	 * @author lyf
	 * @说明 
	 * @param mapValue 临时值
	 * @param express
	 * @param formula_map
	 * @return
	 * @throws BusinessException
	 * @时间 2012-11-26下午04:12:31
	 *
	 */
	public  List<UFDouble> computeFormula(Map<String, List> tempValue,String expressname,String express,Map<String, FormulaDefineVO> formula_map)throws BusinessException {
		List<UFDouble> list = new ArrayList<UFDouble>();
		parser.setExpress(express);
		// 得到公式中所有的变量
		VarryVO[] varryVOs  = parser.getVarryArray();
		Map<String, List> mapValue = new HashMap<String, List>();
		for(VarryVO varryVO:varryVOs ){
			String[] itemCodes = varryVO.getVarry();
			for (String item : itemCodes) {
				FormulaDefineVO formulaItem = formula_map.get(item);
				getCommonCalc().setFormulaItem(formulaItem);
				List lstValue = getCommonCalc().doCalcDouble();
				mapValue.put(item, lstValue);
				mapValue.putAll(tempValue);
			}
		}
		parser.setDataSArray(mapValue);// 设置参数表
		Object[][] obj = parser.getValueOArray();
		if(obj == null || obj.length == 0){
			Logger.error(" 计算结果为空，请检查计算数据、公式设置的正确性！ 公式："+expressname);
			return null;
//			throw new BusinessException(" 计算结果为空，请检查计算数据、公式设置的正确性！ 公式："+expressname);
		}
		Object[] o = obj[obj.length-1];
		if(o == null || o.length == 0){
			Logger.error(" 计算结果为空，请检查计算数据、公式设置的正确性！ 公式："+expressname);
			return null;
//			throw new BusinessException(" 计算结果为空，请检查计算数据、公式设置的正确性！ 公式："+expressname);
		}
//		UFDouble money  = UFDouble.ZERO_DBL;
		for (Object value : o) {
			UFDouble temvalue = SemPubVO.getUFDouble_NullAsZero(value);
//			// 设置精度[金额精度]
//			money = money.add(temvalue);
			list.add(temvalue);
		}
	//	money = money.setScale(getPrecision().getMoneyBusinessDigit(), UFDouble.ROUND_HALF_UP);
	//	list.add(money);
		return list;
	}
	public void addBusinessFunction() throws BusinessException {
		try {
			// 查询二次开发业务函数
			IUifService service = (IUifService) NCLocator.getInstance().lookup(
					IUifService.class.getName());
			BusinessFunction[] funVos = (BusinessFunction[]) service
					.queryByCondition(BusinessFunction.class,
							" isnull(dr,0) = 0 ");
			// 加入公式容器进行解析
			if (funVos != null && funVos.length > 0) {
				for (int i = 0; i < funVos.length; i++) {
					PostfixMathCommandI classs = (PostfixMathCommandI) Class
							.forName(funVos[i].getItemClass()).newInstance();
					parser.addFunction(funVos[i].getVitemname(), classs);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	
	public CalcCommon getCommonCalc() {
		if(commonCalc   == null){
			commonCalc = new CalcCommon(vos);
		}
		return commonCalc;
	}
	
	public void setCommonCalc(CalcCommon commonCalc) {
		this.commonCalc = commonCalc;
	}
	

}