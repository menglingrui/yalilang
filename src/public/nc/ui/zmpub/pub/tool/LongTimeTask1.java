package nc.ui.zmpub.pub.tool;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nc.ui.scm.service.LocalCallService;
import nc.vo.scm.service.ServcallVO;

public class LongTimeTask1 {
	/**
	 * 调用方法。
	 * 创建日期：(2003-6-9)
	 * @param mainBill
	 *            nc.vo.ybstep2.bill.MainBillVO
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public static Object callService(String modulename, int iCallPubServerType,
			String classname, Object runobj, String methodname,
			Class[] ParameterTypes, Object[] ParameterValues) throws Exception {
		Object oret = null;
		if (classname == null || methodname == null)
			return oret;

		try {

			if (iCallPubServerType == 1 || iCallPubServerType == 2)
				return callRemoteService(modulename, classname, methodname,
						ParameterTypes, ParameterValues, iCallPubServerType);

			Class cl = runobj == null ? Class.forName(classname) : runobj
					.getClass();
			if (cl == null)
				return oret;
			Method m = cl.getMethod(methodname, ParameterTypes);
			if (m == null)
				return oret;
			if (Modifier.isStatic(m.getModifiers())) {
				oret = m.invoke(null, ParameterValues);
			} else {
				oret = m.invoke(runobj == null ? cl.newInstance() : runobj,
						ParameterValues);
			}
		} catch (java.lang.reflect.InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof Exception) {
				throw (Exception) ex;
			} else {
				throw new Exception(e.getMessage());
			}
		}
		return oret;
	}
	
	public static Object callRemoteService(String modulename,
			String classname, String methodname, Class[] ParameterTypes,
			Object[] ParameterValues, int iCallPubServerType) throws Exception {
		ServcallVO[] scd = new ServcallVO[1];
		Object oret = null;
		scd[0] = new ServcallVO();
		scd[0].setBeanName(classname);
		scd[0].setMethodName(methodname);
		scd[0].setParameterTypes(ParameterTypes);
		scd[0].setParameter(ParameterValues);
		// modifeid by lirr 2008-10-14
		// Object[] otemps = LocalCallService.callEJBService(CTConst.MODULE_IC,
		// scd);
		Object[] otemps = null;
		if (iCallPubServerType == 1) {
			otemps = LocalCallService.callEJBService(modulename, scd);
		} else if (iCallPubServerType == 2) {
			otemps = LocalCallService.callService(modulename, scd);
		}
		// = LocalCallService.callEJBService(modulename, scd);
		if (otemps != null && otemps.length > 0)
			oret = otemps[0];
		return oret;
	}
}
