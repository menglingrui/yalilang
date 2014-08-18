package nc.vo.zmpub.formula;

import java.util.List;

import nc.vo.pub.formulaset.IFormulaConstant;
import nc.vo.pub.formulaset.core.ParseException;
import nc.vo.pub.formulaset.function.NcInnerFunction;
import nc.vo.pub.lang.UFDouble;

/**
 * 向下取整
 * @author zpm
 *
 */
@SuppressWarnings("all")
public class DownToInteger extends NcInnerFunction {

	public DownToInteger() {
		numberOfParameters = 1;
		functionType = IFormulaConstant.FUN_MATH;
		functionDesc = "对数据进行向下取整，向下取整(0.5) = 0，向下取整(1.5) = 1";
	}

	public Object function(List param) throws ParseException {
		if (param == null || param.size() != 1)
			throw new ParseException("向下取整,参数设置不正确！");
		UFDouble param1 = convertToUFDouble(param.get(0));
		return calculate(param1);
	}

	/**
	 * 转化Object为UFDouble
	 */
	private UFDouble convertToUFDouble(Object param) throws ParseException {
		if (param instanceof UFDouble) {
			return (UFDouble) param;
		} else {
			if (param instanceof Number) {
				return new UFDouble(((Number) param).doubleValue());
			} else {
				throw new ParseException(
						"Invalid parameter type in compare of Ncinnerfunction");
			}
		}
	}

	private UFDouble calculate(UFDouble param1) throws ParseException {
		if(param1 == null){
			return null;
		}		
		return new UFDouble(Math.floor(param1.doubleValue()));
	}
}