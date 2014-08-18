package nc.bs.zmpub.formula;

import nc.bs.framework.common.RuntimeEnv;

/**
 * 默认公式路径设置
 * @author mlr
 *
 */
public class FormulaPath {	
	
	public static String NCHOME = RuntimeEnv.getInstance().getNCHome();
	
	public static String file1 = NCHOME+"/xew/xew_plugin.xml";
	
	public static String file2 = NCHOME+"/xew/systemfunction.xml";
	
	public static String file3 = NCHOME+"/xew/xew_refmodel.xml";
	
	public static String file4 = NCHOME+"/xew/xew_pingzhong.xml";
}
