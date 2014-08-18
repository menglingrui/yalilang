package nc.vo.zmpub.formula;
import java.util.List;
import java.util.Stack;

import nc.vo.pub.formulaset.core.ParseException;
import nc.vo.pub.formulaset.function.PostfixMathCommand;

/**
 * 求和---产品求和存在问题nc.vo.pub.formulaset.function.Sum这是产品的求和类
 * @author zpm
 *
 */
@SuppressWarnings("all")
public class Sum extends PostfixMathCommand {

	public Sum() {
		numberOfParameters = -1;
	}

	public void run(Stack stack) throws ParseException {
		checkStack(stack); 
		Object product = stack.pop();
		int i = 0;
		Object obj = 0;
		if(product !=null && product instanceof List){
			List list = (List)product;
			while (i < list.size()) {
				obj = sum(list.get(i), obj);
				i++;
			}
		}
		stack.push(obj);
	}

	private Object sum(Object param, Object result) throws ParseException {
		if (param instanceof Integer && result instanceof Integer) {
			int sumval = ((Integer) result).intValue()+ ((Integer) param).intValue();
			result = new Integer(sumval);
		} else if (param instanceof Number && result instanceof Number) {
			double sumval = ((Number) result).doubleValue()+ ((Number) param).doubleValue();
			result = new Double(sumval);
		} else {
			return null;
		}
		return result;
	}
}
