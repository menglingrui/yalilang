package nc.vo.zmpub.formula;

import java.util.List;

import nc.vo.pub.formulaset.IFormulaConstant;
import nc.vo.pub.formulaset.core.ParseException;
import nc.vo.pub.formulaset.function.NcInnerFunction;
import nc.vo.pub.lang.UFDouble;
/**
 * 向上取整
 * @author zpm
 *
 */
@SuppressWarnings("all")
public class UpToInteger extends NcInnerFunction {

	public UpToInteger() {
		numberOfParameters = 2;
		functionType = IFormulaConstant.FUN_MATH;
		functionDesc = "向上取整( n1,n2)。n1代表需要向上取整的小数。n2取数区间 (0,1)，当n1的小数位大于或等于n2才能向上取整，否则向下取整。";
	}

	public Object function(List param) throws ParseException {
		if (param == null || param.size() != 2)
			throw new ParseException("向下取整,参数设置不正确！");
		UFDouble param1 = convertToUFDouble(param.get(0));
		UFDouble param2 = convertToUFDouble(param.get(1));
		if(param2 == null){
			throw new ParseException("请设置参数2！");
		}
		if(param2.doubleValue() <= 0 ||  param2.doubleValue() >= 1){
			throw new ParseException("参数2设置不正确，它的取数区间为(0,1)之间！");
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
		double fanweivalue = param2.getDouble();//向上取整的范围
		double retvalue = param1.doubleValue();
		double floor = Math.floor(retvalue);
		if(fanweivalue<=(retvalue-floor)){
			return new UFDouble(Math.ceil(param1.doubleValue()));//向上取整
		}else{
			return new UFDouble(Math.floor(param1.doubleValue()));//向下取整
		}
	}
}