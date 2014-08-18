package nc.ui.zmpub.formula;

import java.util.ArrayList;

import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.ui.zmpub.pub.tool.MapCacheToolClient;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.SystemfunctionVO;
import nc.vo.zmpub.pub.consts.ZmPubConst;


/**
 * 读取xml配置文件
 * @author zpm
 *
 */
public class ReadXML {

	private static String beaName = "nc.bs.zmpub.formula.FormulaImpl";
	
	private static ArrayList<FormulaDefineVO>  formulaDefvo = null;//项目、内容设置
	
	private static ArrayList<SystemfunctionVO>  functionvos = null;//系统函数配置文件
	
	private static ArrayList<FormulaDefineVO>  refModelDefvo = null;//参照设置文件
	
	private static ArrayList<FormulaDefineVO>  pzformulaDefvo = null;//品种设置vo
	/**
	 * 读取xml文件数据
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private static void readXML() throws Exception{
		if(formulaDefvo == null || 
				functionvos == null || 
				refModelDefvo == null ||
				pzformulaDefvo == null){
			
				formulaDefvo = (ArrayList<FormulaDefineVO>) LongTimeTask.calllongTimeService(ZmPubConst.module, null, 
		                "....", 1, beaName, null, 
		                "readDataXML", new Class[0], new Object[0]);
				
				refModelDefvo = (ArrayList<FormulaDefineVO>) LongTimeTask.calllongTimeService(ZmPubConst.module, null, 
		                "....", 1, beaName, null, 
		                "readRefDataXML", new Class[0], new Object[0]);
				
				functionvos = (ArrayList<SystemfunctionVO>) LongTimeTask.calllongTimeService(ZmPubConst.module, null, 
		                "....", 1, beaName, null, 
		                "readFunctionXML", new Class[0], new Object[0]);
				
				pzformulaDefvo = (ArrayList<FormulaDefineVO>) LongTimeTask.calllongTimeService(ZmPubConst.module, null, 
		                "....", 1, beaName, null, 
		                "readPZXML",new Class[0], new Object[0]);
								
		}
	}
	public static ArrayList<FormulaDefineVO> getFormulaDefvo() throws Exception{
		if(MapCacheToolClient.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheToolClient.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return formulaDefvo;
	}
	public static ArrayList<SystemfunctionVO> getFunctionvos() throws Exception{
		if(MapCacheToolClient.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheToolClient.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return functionvos;
	}
	
	public static ArrayList<FormulaDefineVO> getrefModelDefvo() throws Exception{
		if(MapCacheToolClient.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheToolClient.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return refModelDefvo;
	}
	
	public static ArrayList<FormulaDefineVO> getPZDefvo() throws Exception{
		if(MapCacheToolClient.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheToolClient.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return pzformulaDefvo;
	}
}
