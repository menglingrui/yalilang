package nc.bs.zmpub.formula.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pub.lang.UFDouble;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.calc.BeforeCalcAllInfomation;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 公式执行类
 * @author mlr
 */
public class DoCalc {	
		
	private static Map<String, FormulaDefineVO> formula_map = null;
	
	/**
	 * 
	 * @author mlr
	 * @说明 :进行公式计算
	 * @param b1vos 执行公式的vo
	 * @param formula_map  公式定义项vo
	 * @param expresscode 要执行的公式
	 * @param expressname 要执行公式的中文显示名称
	 * @return
	 * @throws Exception 
	 * @时间 2012-11-6下午01:36:43
	 *
	 */
	public List<UFDouble>  doCalcStart(String expresscode,String expressname,ReportBaseVO[] vos)throws Exception{
		if(vos == null || vos.length==0){
			return null;
		}
		//查询公式
		 readFormulaDefineVOInfo();
//		//查询精度
//        readPrecision(pk_corp);
        //构造计算前信息
		BeforeCalcAllInfomation beforeinfo = constructBeforeCalcInfo(expresscode,expressname,vos,formula_map);
		//开始计算数据项
		CalcDataItem calc = new CalcDataItem(beforeinfo);
		//计算
		List<UFDouble> list = calc.doCalc();
		return list;
	}	
	//组装计算前信息
	public BeforeCalcAllInfomation constructBeforeCalcInfo(String expresscode,String expressname,ReportBaseVO[] details,Map<String, FormulaDefineVO> formula_map){
		BeforeCalcAllInfomation info = new BeforeCalcAllInfomation();
		info.setCalcobjectdetail(details);
		info.setFormula_map(formula_map);
		info.setExpresscode(expresscode);
		info.setExpressname(expressname);
		return info;
	}
	/**
	 * 查询公式定义VO
	 * @throws Exception 
	 */
	protected Map<String, FormulaDefineVO> readFormulaDefineVOInfo() throws Exception{
		if(formula_map == null || formula_map.size() == 0){
			ArrayList<FormulaDefineVO> list1 =ReadXML.getFormulaDefvo();//项目内容定义
			ArrayList<FormulaDefineVO> list2 =ReadXML.getPZDefvo();//品种设置公式
			formula_map = new HashMap<String, FormulaDefineVO>();
			List<FormulaDefineVO> list = new ArrayList<FormulaDefineVO>();
			list.addAll(list1);
			list.addAll(list2);
			putValuetoMap(list,formula_map);
		}		
		return formula_map;
	}

	protected void putValuetoMap(List<FormulaDefineVO> list,Map<String, FormulaDefineVO> map){
		if(list == null || list.size() == 0){
			return;
		}
		if(map == null){
			map = new HashMap<String, FormulaDefineVO>();
		}
		for(FormulaDefineVO definevo : list){
			List<FormulaDefineVO> n_list = definevo.getFormualdefvo();
			putValuetoMap(n_list,map);
			String key = definevo.getCode();
			map.put(key, definevo);
		}
	}



}
