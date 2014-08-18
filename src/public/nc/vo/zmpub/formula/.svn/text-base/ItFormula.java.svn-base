package nc.vo.zmpub.formula;

import java.util.ArrayList;

import nc.vo.pub.BusinessException;


/**
 * 公式取值 框
 * @author zpm
 *
 */
public interface ItFormula  {
	
	/**
	 * 读取--项目内容xml文件
	 */
	public ArrayList<FormulaDefineVO> readDataXML() throws BusinessException;//读取zzjs_plugin文件,返回结果
	/**
	 * 读取--系统函数xml文件
	 */
	public ArrayList<SystemfunctionVO> readFunctionXML() throws BusinessException;//读取[系统函数]
	
	/**
	 * 读取--参照内容xml文件
	 */
	public ArrayList<FormulaDefineVO> readRefDataXML() throws BusinessException;//读取zzjs_refmodel文件,返回结果
	

	/**
	 * 读取--品种设置xml文件
	 */
	public ArrayList<FormulaDefineVO> readPZXML() throws BusinessException;//读取 zzjs_pingzhong.xml文件,返回结果

}
