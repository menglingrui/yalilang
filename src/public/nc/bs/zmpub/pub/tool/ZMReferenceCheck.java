package nc.bs.zmpub.pub.tool;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.vo.pub.BusinessException;
/**
 *  基本档案是否被应用校验
 *  此类的使用需要注册bd_ref_relation表
 * @author mlr
 * 
 */
public class ZMReferenceCheck {
	private static IReferenceCheck referenceCheck = null;

	public static IReferenceCheck getReferenceCheck() {
		if (referenceCheck == null) {
			referenceCheck = (IReferenceCheck) NCLocator.getInstance().lookup(
					IReferenceCheck.class.getName());
		}
		return referenceCheck;
	}
	/**
	 * 查询tableName中主键字段的值为key的记录是否被引用了
	 * 
	 * @param tableName
	 * @param key
	 * @return ture 如果被引用,否则为false
	 * @throws BusinessException
	 */
	public static boolean isReferenced(String tableName, String key)
			throws BusinessException {
		return getReferenceCheck().isReferenced(tableName, key);
	}
}
