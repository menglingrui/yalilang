package nc.bs.zmpub.formula;
import java.util.ArrayList;

import nc.vo.pub.BusinessException;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.ItFormula;
import nc.vo.zmpub.formula.SystemfunctionVO;
/**
 * 公式实现类
 * @author mlr
 *
 */
public class FormulaImpl implements ItFormula{
	
	private ReadForXML xml = new ReadForXML();
	/**
	 * 读取--项目内容xml文件
	 */
	public ArrayList<FormulaDefineVO> readDataXML() throws BusinessException{
		ArrayList<FormulaDefineVO> list = xml.readInfoXml();
		return list;
	}
	/**
	 * 读取--系统函数xml文件
	 */
	public ArrayList<SystemfunctionVO> readFunctionXML() throws BusinessException {
		ArrayList<SystemfunctionVO> list = xml.readFunctionXML();
		return list;
	}
	
	/**
	 * 读取--参照内容xml文件
	 */
	public ArrayList<FormulaDefineVO> readRefDataXML() throws BusinessException{
		ArrayList<FormulaDefineVO> list = xml.readRefModelXml();
		return list;
	}
	
	/**
	 * 读取--品种设置xml文件
	 */
	public ArrayList<FormulaDefineVO> readPZXML() throws BusinessException {
		ArrayList<FormulaDefineVO> list = xml.readPZXML();
		return list;
	}

}
