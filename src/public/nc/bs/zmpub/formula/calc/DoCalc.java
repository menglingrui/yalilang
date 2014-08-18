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
 * ��ʽִ����
 * @author mlr
 */
public class DoCalc {	
		
	private static Map<String, FormulaDefineVO> formula_map = null;
	
	/**
	 * 
	 * @author mlr
	 * @˵�� :���й�ʽ����
	 * @param b1vos ִ�й�ʽ��vo
	 * @param formula_map  ��ʽ������vo
	 * @param expresscode Ҫִ�еĹ�ʽ
	 * @param expressname Ҫִ�й�ʽ��������ʾ����
	 * @return
	 * @throws Exception 
	 * @ʱ�� 2012-11-6����01:36:43
	 *
	 */
	public List<UFDouble>  doCalcStart(String expresscode,String expressname,ReportBaseVO[] vos)throws Exception{
		if(vos == null || vos.length==0){
			return null;
		}
		//��ѯ��ʽ
		 readFormulaDefineVOInfo();
//		//��ѯ����
//        readPrecision(pk_corp);
        //�������ǰ��Ϣ
		BeforeCalcAllInfomation beforeinfo = constructBeforeCalcInfo(expresscode,expressname,vos,formula_map);
		//��ʼ����������
		CalcDataItem calc = new CalcDataItem(beforeinfo);
		//����
		List<UFDouble> list = calc.doCalc();
		return list;
	}	
	//��װ����ǰ��Ϣ
	public BeforeCalcAllInfomation constructBeforeCalcInfo(String expresscode,String expressname,ReportBaseVO[] details,Map<String, FormulaDefineVO> formula_map){
		BeforeCalcAllInfomation info = new BeforeCalcAllInfomation();
		info.setCalcobjectdetail(details);
		info.setFormula_map(formula_map);
		info.setExpresscode(expresscode);
		info.setExpressname(expressname);
		return info;
	}
	/**
	 * ��ѯ��ʽ����VO
	 * @throws Exception 
	 */
	protected Map<String, FormulaDefineVO> readFormulaDefineVOInfo() throws Exception{
		if(formula_map == null || formula_map.size() == 0){
			ArrayList<FormulaDefineVO> list1 =ReadXML.getFormulaDefvo();//��Ŀ���ݶ���
			ArrayList<FormulaDefineVO> list2 =ReadXML.getPZDefvo();//Ʒ�����ù�ʽ
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
