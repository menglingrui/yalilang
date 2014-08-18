package nc.vo.zmpub.formula;

import java.util.List;

import nc.vo.pub.formulaset.IFormulaConstant;
import nc.vo.pub.formulaset.core.ParseException;
import nc.vo.pub.formulaset.function.NcInnerFunction;
import nc.vo.pub.lang.UFDouble;

/**
 * 取余函数
 * @author zpm
 *
 */
@SuppressWarnings("all")
public class Mod extends NcInnerFunction {

	public Mod() {
		numberOfParameters = 2;
		functionType = IFormulaConstant.FUN_MATH;
		functionDesc = "取余函数！";
	}

	public Object function(List param) throws ParseException {
		if (param == null || param.size() != 2)
			throw new ParseException("取余函数,参数设置不正确！");
		UFDouble param1 = convertToUFDouble(param.get(0));
		UFDouble param2 = convertToUFDouble(param.get(1));
		if(param2 == null || param2.doubleValue() == 0){
			throw new ParseException("取余函数,除数不能为0,或者为空！");
		}
		return calculate(param1,param2);
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

	private UFDouble calculate(UFDouble param1,UFDouble param2) throws ParseException {
		if(param1 == null || param2 == null){
			return null;
		}
		double m = param1.doubleValue() % param2.doubleValue();
		return new UFDouble(Math.floor(m));
	}
}