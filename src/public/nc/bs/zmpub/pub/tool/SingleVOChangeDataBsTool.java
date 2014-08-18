package nc.bs.zmpub.pub.tool;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.logging.Logger;
import nc.bs.pf.change.AbstractConversion;
import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.pf.INotifyNextBillMaker;
import nc.vo.pub.pf.IPfBillLock;
import nc.vo.pub.pf.IPfRetBackCheckInfo;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 该工具类主要用于单表之间的数据交换
 * 交换类的交换字段必须以H_开头
 * 交换类必须放到public包里面
 * @author zhf  mlr
 */
public class SingleVOChangeDataBsTool {
	/**
	 * 根据类名获取交换类的实例,不支持前台动态编译交换类的获取
	 * @author zhf
	 * @说明 
	 * @时间 2010-9-26上午10:47:52
	 * @param classname
	 * @return
	 */
	public static nc.vo.pf.change.IchangeVO getChangeClass(String classname)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(classname) == null)
			return null;
		try {
			Class c = Class.forName(classname);
			Object o = c.newInstance();
			return (nc.vo.pf.change.IchangeVO) o;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}
	/**
	 * 获取vo转换类实例支持从从var和extension包获取实施人员动态配置的交换类，优先在目的模块查找交换类
	 * @author mlr
	 * @时间 2010-9-26上午10:47:52
	 * @param classname  交换类名称
	 * @param srcmoudle  来源单据所在模块
	 * @param destmoudle 目的单据所在模块
	 * @return
	 */
	public static nc.vo.pf.change.IchangeVO getChangeClassEdit(String classname,String srcmoudle,String destmoudle)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(classname) == null)
			return null;
		try {
			Object imp=findBSChangeScriptClass(classname,srcmoudle,destmoudle);
			return (nc.vo.pf.change.IchangeVO) imp;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}
    /**
     * 根据来源模块和目的模块查找实施人员前台动态配置的交换,优先在目的模块查找交换类
     * @param classname  交换类名称
	 * @param srcmoudle  来源单据所在模块
	 * @param destmoudle 目的单据所在模块
     * @return
     * @throws BusinessException
     */
	public static Object findBSChangeScriptClass(String classname,
			String srcmoudle, String destmoudle) throws BusinessException {

		Logger.debug("查询BS交换类开始......");

		Object changeImpl = null;

		String fullyQualifiedClassName = classname;
			Logger.debug("尝试实例化类=" + fullyQualifiedClassName);
			changeImpl = newVoMappingObject(srcmoudle, destmoudle,
					fullyQualifiedClassName);
			Logger.debug("查询BS交换类结束...." + changeImpl);
			return changeImpl;	
	}
	 /**
     * 根据来源模块和目的模块查找实施人员前台动态配置的交换,优先在目的模块查找交换类
     * @param classname  交换类名称
	 * @param srcmoudle  来源单据所在模块
	 * @param destmoudle 目的单据所在模块
     * @return
     * @throws BusinessException
     */
	public static Object newVoMappingObject(String moduleOfSrc,
			String moduleOfDest, String fullyQualifiedClassName)
			throws BusinessException {
		Object changeObj = null;

		if (moduleOfDest == null || moduleOfDest.trim().length() == 0) {
			if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
				throw new PFBusinessException("错误：源或目的单据类型都没有注册模块名");
			else {
				changeObj = NewObjectService.newInstance(moduleOfSrc,
						fullyQualifiedClassName);
				Logger.debug("OK-目的单据没有所属模块，但在来源单据所属模块" + moduleOfSrc
						+ "中找到交换类=" + changeObj);
			}
		} else {
			// 优先在目的单据类型所属模块 查找类
			try {
				changeObj = NewObjectService.newInstance(moduleOfDest,
						fullyQualifiedClassName);
				Logger.debug("OK-在目的单据所属模块" + moduleOfDest + "中找到交换类="
						+ changeObj);
			} catch (Exception e) {
				if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
					throw new PFBusinessException(
							"错误：VO交换类不在目的单据类型所属模块中，且来源单据没有注册所属模块");
				else {
					changeObj = NewObjectService.newInstance(moduleOfSrc,
							fullyQualifiedClassName);
					Logger.debug("OK-在目的单据所属模块中找不到交换类，但在来源单据所属模块" + moduleOfSrc
							+ "中找到交换类=" + changeObj);
				}
			}
		}

		Class c = changeObj.getClass();
		Logger.debug(">>>OBJ=" + changeObj + ";CL="
				+ c.getProtectionDomain().getClassLoader());
		Logger.debug(">>>LOC="
				+ c.getProtectionDomain().getCodeSource().getLocation());
		return changeObj;
	}
	
	/**
	 * 单vo数据交换该方法后台调用前台不能使用
	 * @author zhf
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc
	 *            
	 * @throws Exception
	 */
	public static void runChangeVO(
			SuperVO souVo, SuperVO tarVo, String chanclassname)
			throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
								// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();

		preBillVo.setParentVO(souVo);
		preBillVo.setChildrenVO(new SuperVO[]{souVo});
		tarBillVo.setParentVO(tarVo);
		tarBillVo.setChildrenVO(new SuperVO[]{tarVo});
		AbstractConversion achange = (AbstractConversion) change;
		achange.retChangeBusiVO(preBillVo, tarBillVo);

	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用
	 * @author zhf
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVoClass
	 *            目标vo class对象
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc
	 *            
	 * @throws Exception
	 */
	public static SuperVO[] runChangeVOAry(
			SuperVO[] souVos, Class tarVoClass, String chanclassname)
	throws Exception {

		int len = souVos.length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * @author zhf
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVoClass
	 *            目标vo class对象
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc
	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	public static SuperVO[] runChangeVOAry(
			SuperVO[] souVos, Class tarVoClass, String chanclassname,String  srcmod,String destmod)
	throws Exception {

		int len = souVos.length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname,srcmod,destmod);
		return tarVos;
	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * 按标题交换
	 * @author zhf
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVoClass
	 *            目标vo class对象
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以B_开头
	 *            如：B_cdeptid->B_pk_deptdoc
	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] runChangeVOAryBody(
			CircularlyAccessibleValueObject[] souVos, Class tarVoClass, String chanclassname,String  srcmod,String destmod)
	throws Exception {

		int len = souVos.length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAryBody(souVos, tarVos, chanclassname,srcmod,destmod);
		return tarVos;
	}
	
	/**
	 * 单vo数据交换该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * 按标题交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVoClass
	 *            目标vo class对象
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以B_开头
	 *            如：B_cdeptid->B_pk_deptdoc
	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	public static SuperVO[] runChangeVOAryBody(
			SuperVO[] souVos, Class tarVoClass, String chanclassname,String  srcmod,String destmod)
	throws Exception {

		int len = souVos.length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAryBody(souVos, tarVos, chanclassname,srcmod,destmod);
		return tarVos;
	}
	
	/**
	 * 支持聚合vo->单表vo的数据交换
	 * 该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * 按标题交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVoClass
	 *            目标vo class对象
	 * @param chanclassname

	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	public static SuperVO[] runChangeVOAry(
			AggregatedValueObject souVos, Class tarVoClass, String chanclassname,String srcmod,String desmod)
	throws Exception {
        if(souVos==null){
        	return null;
        }
		
		int len = souVos.getChildrenVO().length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname,srcmod,desmod);

		return tarVos;
	}
	

	/**
	 * 支持聚合vo->单表vo的数据交换
	 * 该方法后台调用前台不能使用
	 * 按标题交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVoClass
	 *            目标vo class对象
	 * @param chanclassname

	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	public static SuperVO[] runChangeVOAry(
			AggregatedValueObject souVos, Class tarVoClass, String chanclassname)
	throws Exception {
        if(souVos==null){
        	return null;
        }
		
		int len = souVos.getChildrenVO().length;
		if(len<=0)
			return null;
		SuperVO[] tarVos = (SuperVO[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		SuperVO tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (SuperVO)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}
	/**
	 * 支持聚合vo->单表vo的数据交换
	 * 该方法后台调用前台不能使用
	 * 按表体交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVos
	 *            目标vo对象
	 * @param chanclassname

	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	private static void runChangeVOAry(AggregatedValueObject souVos,
			SuperVO[] tarVos, String chanclassname) throws BusinessException {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = souVos;
		
		AggregatedValueObject tarBillVo = getTmpBIllVo22();	
			tarBillVo.setChildrenVO(tarVos);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
		
	}
	/**
	 * 支持聚合vo->单表vo的数据交换
	 * 该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * 按标题交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVos
	 *            目标vo对象
	 * @param chanclassname

	 * @param srcmod 单据类型来源模块
	 * @param destmod 单据类型目的模块
	 *            
	   @throws Exception
	 */
	private static void runChangeVOAry(AggregatedValueObject souVos,
			SuperVO[] tarVos, String chanclassname,String srcmod,String desmod) throws BusinessException {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,desmod);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = souVos;
		
		AggregatedValueObject tarBillVo = getTmpBIllVo22();	
		    if(tarVos!=null&&tarVos.length>0)
		    tarBillVo.setParentVO(tarVos[0]);	
			tarBillVo.setChildrenVO(tarVos);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
		
	}

	/**
	 * 单表数据交换
	 * 支持CircularlyAccessibleValueObject类型
	 * @author mlr 
	 * @说明：（鹤岗矿业）
	 * 2011-10-21下午02:14:50
	 * @param souVos 来源vo
	 * @param tarVoClass 目的vo class对象
	 * @param chanclassname 交换类
	 * @return
	 * @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] runChangeVOAry(CircularlyAccessibleValueObject[] souVos, Class tarVoClass, String chanclassname)
	throws Exception {

		int len = souVos.length;
		if(len<=0)
			return null;
		CircularlyAccessibleValueObject[] tarVos = (CircularlyAccessibleValueObject[])java.lang.reflect.Array.newInstance(tarVoClass, len);
		CircularlyAccessibleValueObject tmp = null;
		for(int i = 0;i<len;i++){
			tmp = (CircularlyAccessibleValueObject)tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc
	 *            
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			CircularlyAccessibleValueObject[] souVos, CircularlyAccessibleValueObject[] tarVos, String chanclassname)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo11();
		AggregatedValueObject tarBillVo = getTmpBIllVo22();
		int index = 0;
		for(CircularlyAccessibleValueObject souVo:souVos){
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index ++;
		}
	}
	
	/**
	 * 单vo数据交换该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc
	 * @param srcmod   单据类型来源模块
	 * @param desmod   单据类型目的模块        
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			CircularlyAccessibleValueObject[] souVos, CircularlyAccessibleValueObject[] tarVos, String chanclassname,String srcmod,String desmod)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,desmod);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo11();
		AggregatedValueObject tarBillVo = getTmpBIllVo22();
		int index = 0;
		for(CircularlyAccessibleValueObject souVo:souVos){
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index ++;
		}
	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * 支持按表体交换
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以B_开头
	 *            如：B_cdeptid->B_pk_deptdoc
	 * @param srcmod   单据类型来源模块
	 * @param desmod   单据类型目的模块        
	 * @throws Exception
	 */
	public static void runChangeVOAryBody(
			CircularlyAccessibleValueObject[] souVos, CircularlyAccessibleValueObject[] tarVos, String chanclassname,String srcmod,String desmod)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,desmod);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo11();
		AggregatedValueObject tarBillVo = getTmpBIllVo22();
		int index = 0;
		preBillVo.setChildrenVO(souVos);
		tarBillVo.setChildrenVO(tarVos);
		AbstractConversion achange = (AbstractConversion) change;
		achange.retChangeBusiVO(preBillVo, tarBillVo);
//		for(CircularlyAccessibleValueObject souVo:souVos){
//			preBillVo.setParentVO(souVo);
//			tarBillVo.setParentVO(tarVos[index]);
//			AbstractConversion achange = (AbstractConversion) change;
//			achange.retChangeBusiVO(preBillVo, tarBillVo);
//			index ++;
//		}
	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用,支持按实施人员动态配置的交换类进行数据交换
	 * 
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc
	 * @param srcmod   单据类型来源模块
	 * @param desmod   单据类型目的模块        
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			SuperVO[] souVos, SuperVO[] tarVos, String chanclassname,String srcmod,String destmod)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,destmod);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();
		preBillVo.setChildrenVO(souVos);
		tarBillVo.setChildrenVO(tarVos);
		int index = 0;
		for(SuperVO souVo:souVos){
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index ++;
		}

	}
	/**
	 * 单vo数据交换该方法后台调用前台不能使用
	 * 
	 * @author mlr
	 * @说明 
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 *            vo交换类的书写方式
	 *            都以H_开头
	 *            如：H_cdeptid->H_pk_deptdoc     
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			SuperVO[] souVos, SuperVO[] tarVos, String chanclassname)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();
		int index = 0;
		preBillVo.setChildrenVO(souVos);
		tarBillVo.setChildrenVO(tarVos);
		for(SuperVO souVo:souVos){
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index ++;
		}

	}

	private static HYBillVO tmpBillVo1 = null;

	private static HYBillVO getTmpBIllVo1() {
		if (tmpBillVo1 == null) {
			tmpBillVo1 = new HYBillVO();
		}
		return tmpBillVo1;
	}

	private static HYBillVO tmpBillVo2 = null;

	private static HYBillVO getTmpBIllVo2() {
		if (tmpBillVo2 == null) {
			tmpBillVo2 = new HYBillVO();
		}
		return tmpBillVo2;
	}
	private static MyBillVO tmpBillVo11 = null;

	private static MyBillVO getTmpBIllVo11() {
		if (tmpBillVo11 == null) {
			tmpBillVo11= new MyBillVO();
		}
		return tmpBillVo11;
	}

	private static MyBillVO tmpBillVo22 = null;

	private static MyBillVO getTmpBIllVo22() {
		if (tmpBillVo22 == null) {
			tmpBillVo22 = new MyBillVO();
		}
		return tmpBillVo22;
	}
/**
 * 	聚合vo
 * @author mlr
 *
 */
private static class MyBillVO extends AggregatedValueObject implements IPfBillLock,
	IPfRetBackCheckInfo, IPfRetCheckInfo, INotifyNextBillMaker {
/**
 * 
 */
private static final long serialVersionUID = 1L;

//主表VO
CircularlyAccessibleValueObject m_headVo = null;

//子表VO
CircularlyAccessibleValueObject[] m_itemVos = null;

//消息提示
private String m_hintMessage = null;

//是否发送工作流或消息
private Boolean m_isSendMessage = new Boolean(false);

//判定是否进行单据锁定
private boolean m_isBillLock = true;

//对应的单据字段常量信息
private IBillField m_billField = null;

/**
 * @return 返回 m_billField。
 */
public IBillField getM_billField() {
	return m_billField;
}

/**
 * @param field
 *            要设置的 m_billField。
 */
public void setM_billField(IBillField field) {
	m_billField = field;
}

/**
 * YcBillVO 构造子注解。
 */
public MyBillVO() {
	super();
}

/**
 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
 * 
 * @return nc.vo.pub.ValueObject[]
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
	return m_itemVos;
}

/**
 * 返回锁的数组。 创建日期：(2003-6-8 8:01:54)
 * @return java.lang.String[]
 */
public java.lang.String[] getLockIdAry() throws BusinessException {
	if (!isBillLock())
		return null;
	Hashtable billKeyHas = new Hashtable();
	if (m_headVo.getPrimaryKey() != null
			&& m_headVo.getPrimaryKey().trim().length() != 0)
		billKeyHas.put(m_headVo.getPrimaryKey(), m_headVo.getPrimaryKey());
	if (m_itemVos != null)
		for (int i = 0; i < m_itemVos.length; i++) {
			String vLastId = (String) m_itemVos[i]
					.getAttributeValue(BillField.getInstance()
							.getField_LastBillId());
			if (vLastId == null || vLastId.trim().length() == 0)
				continue;
			else {
				if (billKeyHas.containsKey(vLastId))
					continue;
				else
					billKeyHas.put(vLastId, vLastId);
			}
		}
	String[] keyAry = null;
	if (billKeyHas.size() > 0) {
		keyAry = new String[billKeyHas.size()];
		Enumeration e = billKeyHas.keys();
		int i = 0;
		while (e.hasMoreElements())
			keyAry[i++] = (String) e.nextElement();
	}
	return keyAry;

}

/**
 * 此处插入方法说明。 创建日期：(2003-7-3 8:47:41)
 * 
 * @return java.lang.String
 */
public java.lang.String getMessage() {
	return m_hintMessage;
}

/**
 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
 * 
 * @return nc.vo.pub.ValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {
	return m_headVo;
}

/**
 * 此处插入方法说明。 创建日期：(2004-3-12 10:55:39)
 * 
 * @return boolean
 */
public boolean isBillLock() {
	return m_isBillLock;
}

/**
 * 是否发送工作流。 默认不进行工作流发送 创建日期：(2003-7-6 12:30:36)
 * 
 * @return java.lang.Boolean
 */
public java.lang.Boolean isSendMessage() {
	return m_isSendMessage;
}

/**
 * 设置单据反审后的审批人 创建日期：(2002-10-16 13:39:32)
 * 
 * @param icheckState
 *            int
 */
public void setCheckMan(java.lang.String approveid) {
	getParentVO().setAttributeValue(
			BillField.getInstance().getField_CheckMan(), approveid);
}

/**
 * 设置批语 创建日期：(2002-10-16 13:40:13)
 * 
 * @param strCheckNote
 *            java.lang.String
 */
public void setCheckNote(java.lang.String strCheckNote) {
	getParentVO().setAttributeValue(
			BillField.getInstance().getField_CheckNote(), strCheckNote);
}

/**
 * 设置审批状态 创建日期：(2002-10-16 13:39:32)
 * 
 * @param icheckState
 *            int
 */
public void setCheckState(int icheckState) {
	getParentVO().setAttributeValue(
			BillField.getInstance().getField_BillStatus(),
			new Integer(icheckState));
}

/**
 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
 * 
 * @return nc.vo.pub.ValueObject[]
 */
public void setChildrenVO(
		nc.vo.pub.CircularlyAccessibleValueObject[] children) {
	if(children==null)
	{
		m_itemVos = null;
	}
	else if(children.length==0)
	{
		try {
			m_itemVos = (CircularlyAccessibleValueObject[]) children;
		} catch (ClassCastException e) {
			m_itemVos = null;
		}
	}
	else
	{
		List l = Arrays.asList(children);
		m_itemVos = (CircularlyAccessibleValueObject[]) l.toArray((Object[]) Array.newInstance(children[0].getClass(),0));
	}
}

/**
 * 此处插入方法说明。 创建日期：(2004-3-12 10:55:39)
 * 
 * @param newIsBillLock
 *            boolean
 */
public void setIsBillLock(boolean newIsBillLock) {
	m_isBillLock = newIsBillLock;
}

/**
 * 此处插入方法说明。 创建日期：(2003-7-4 14:37:37)
 * 
 * @param msg
 *            java.lang.String
 */
public void setMessage(java.lang.String msg) {
	m_hintMessage = msg;
}

/**
 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
 * 
 * @return nc.vo.pub.ValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {
	m_headVo = (CircularlyAccessibleValueObject) parent;
}

/**
 * 设置是否发送消息标志。 创建日期：(2003-7-6 15:04:37)
 * 
 * @param param
 *            java.lang.Boolean
 */
public void setSendMessage(java.lang.Boolean param) {
	m_isSendMessage = param;
}
}
}
