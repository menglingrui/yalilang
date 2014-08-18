package nc.bs.zmpub.formula.calc;
import java.util.ArrayList;

import nc.bs.zmpub.formula.FormulaImpl;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.SystemfunctionVO;
import nc.vo.zmpub.pub.consts.ZmPubConst;
import nc.vo.zmpub.pub.tool.MapCacheTool;
/**
 * 读取xml配置文件
 * @author mlr
 */
public class ReadXML {
	
	private static ArrayList<FormulaDefineVO>  formulaDefvo = null;//项目、内容设置
	
	private static ArrayList<SystemfunctionVO>  functionvos = null;//系统函数配置文件
	
	private static ArrayList<FormulaDefineVO>  refModelDefvo = null;//参照设置文件
	
	private static ArrayList<FormulaDefineVO>  pzformulaDefvo = null;//品种设置vo
	/**
	 * 读取xml文件数据
	 * @throws Exception 
	 */

	private static void readXML() throws Exception{
		if(formulaDefvo == null || 
				functionvos == null || 
				refModelDefvo == null ||
				pzformulaDefvo == null){
			formulaDefvo =new   FormulaImpl().readDataXML();
			refModelDefvo=new   FormulaImpl().readRefDataXML();
			functionvos = new   FormulaImpl().readFunctionXML();
			pzformulaDefvo =new FormulaImpl().readPZXML();						
		}
	}
	public static ArrayList<FormulaDefineVO> getFormulaDefvo() throws Exception{
		if(MapCacheTool.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheTool.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return formulaDefvo;
	}
	public static ArrayList<SystemfunctionVO> getFunctionvos() throws Exception{
		if(MapCacheTool.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheTool.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return functionvos;
	}
	
	public static ArrayList<FormulaDefineVO> getrefModelDefvo() throws Exception{
		if(MapCacheTool.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheTool.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return refModelDefvo;
	}
	
	public static ArrayList<FormulaDefineVO> getPZDefvo() throws Exception{
		if(MapCacheTool.getMapObject(ZmPubConst.formula_cache, "isExeQuery")==null){
			readXML();
			MapCacheTool.pubMapObject(ZmPubConst.formula_cache, "isExeQuery", "isExeQuery");			
		}
		return pzformulaDefvo;
	}
}
