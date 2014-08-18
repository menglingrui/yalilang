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
 * �ù�������Ҫ���ڵ���֮������ݽ���
 * ������Ľ����ֶα�����H_��ͷ
 * ���������ŵ�public������
 * @author zhf  mlr
 */
public class SingleVOChangeDataBsTool {
	/**
	 * ����������ȡ�������ʵ��,��֧��ǰ̨��̬���뽻����Ļ�ȡ
	 * @author zhf
	 * @˵�� 
	 * @ʱ�� 2010-9-26����10:47:52
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
	 * ��ȡvoת����ʵ��֧�ִӴ�var��extension����ȡʵʩ��Ա��̬���õĽ����࣬������Ŀ��ģ����ҽ�����
	 * @author mlr
	 * @ʱ�� 2010-9-26����10:47:52
	 * @param classname  ����������
	 * @param srcmoudle  ��Դ��������ģ��
	 * @param destmoudle Ŀ�ĵ�������ģ��
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
     * ������Դģ���Ŀ��ģ�����ʵʩ��Աǰ̨��̬���õĽ���,������Ŀ��ģ����ҽ�����
     * @param classname  ����������
	 * @param srcmoudle  ��Դ��������ģ��
	 * @param destmoudle Ŀ�ĵ�������ģ��
     * @return
     * @throws BusinessException
     */
	public static Object findBSChangeScriptClass(String classname,
			String srcmoudle, String destmoudle) throws BusinessException {

		Logger.debug("��ѯBS�����࿪ʼ......");

		Object changeImpl = null;

		String fullyQualifiedClassName = classname;
			Logger.debug("����ʵ������=" + fullyQualifiedClassName);
			changeImpl = newVoMappingObject(srcmoudle, destmoudle,
					fullyQualifiedClassName);
			Logger.debug("��ѯBS���������...." + changeImpl);
			return changeImpl;	
	}
	 /**
     * ������Դģ���Ŀ��ģ�����ʵʩ��Աǰ̨��̬���õĽ���,������Ŀ��ģ����ҽ�����
     * @param classname  ����������
	 * @param srcmoudle  ��Դ��������ģ��
	 * @param destmoudle Ŀ�ĵ�������ģ��
     * @return
     * @throws BusinessException
     */
	public static Object newVoMappingObject(String moduleOfSrc,
			String moduleOfDest, String fullyQualifiedClassName)
			throws BusinessException {
		Object changeObj = null;

		if (moduleOfDest == null || moduleOfDest.trim().length() == 0) {
			if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
				throw new PFBusinessException("����Դ��Ŀ�ĵ������Ͷ�û��ע��ģ����");
			else {
				changeObj = NewObjectService.newInstance(moduleOfSrc,
						fullyQualifiedClassName);
				Logger.debug("OK-Ŀ�ĵ���û������ģ�飬������Դ��������ģ��" + moduleOfSrc
						+ "���ҵ�������=" + changeObj);
			}
		} else {
			// ������Ŀ�ĵ�����������ģ�� ������
			try {
				changeObj = NewObjectService.newInstance(moduleOfDest,
						fullyQualifiedClassName);
				Logger.debug("OK-��Ŀ�ĵ�������ģ��" + moduleOfDest + "���ҵ�������="
						+ changeObj);
			} catch (Exception e) {
				if (moduleOfSrc == null || moduleOfSrc.trim().length() == 0)
					throw new PFBusinessException(
							"����VO�����಻��Ŀ�ĵ�����������ģ���У�����Դ����û��ע������ģ��");
				else {
					changeObj = NewObjectService.newInstance(moduleOfSrc,
							fullyQualifiedClassName);
					Logger.debug("OK-��Ŀ�ĵ�������ģ�����Ҳ��������࣬������Դ��������ģ��" + moduleOfSrc
							+ "���ҵ�������=" + changeObj);
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��
	 * @author zhf
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc
	 *            
	 * @throws Exception
	 */
	public static void runChangeVO(
			SuperVO souVo, SuperVO tarVo, String chanclassname)
			throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
								// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��
	 * @author zhf
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVoClass
	 *            Ŀ��vo class����
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * @author zhf
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVoClass
	 *            Ŀ��vo class����
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc
	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * �����⽻��
	 * @author zhf
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVoClass
	 *            Ŀ��vo class����
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����B_��ͷ
	 *            �磺B_cdeptid->B_pk_deptdoc
	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * �����⽻��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVoClass
	 *            Ŀ��vo class����
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����B_��ͷ
	 *            �磺B_cdeptid->B_pk_deptdoc
	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
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
	 * ֧�־ۺ�vo->����vo�����ݽ���
	 * �÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * �����⽻��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVoClass
	 *            Ŀ��vo class����
	 * @param chanclassname

	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
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
	 * ֧�־ۺ�vo->����vo�����ݽ���
	 * �÷�����̨����ǰ̨����ʹ��
	 * �����⽻��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVoClass
	 *            Ŀ��vo class����
	 * @param chanclassname

	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
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
	 * ֧�־ۺ�vo->����vo�����ݽ���
	 * �÷�����̨����ǰ̨����ʹ��
	 * �����彻��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVos
	 *            Ŀ��vo����
	 * @param chanclassname

	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
	 *            
	   @throws Exception
	 */
	private static void runChangeVOAry(AggregatedValueObject souVos,
			SuperVO[] tarVos, String chanclassname) throws BusinessException {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
		}
		AggregatedValueObject preBillVo = souVos;
		
		AggregatedValueObject tarBillVo = getTmpBIllVo22();	
			tarBillVo.setChildrenVO(tarVos);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
		
	}
	/**
	 * ֧�־ۺ�vo->����vo�����ݽ���
	 * �÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * �����⽻��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVos
	 *            Ŀ��vo����
	 * @param chanclassname

	 * @param srcmod ����������Դģ��
	 * @param destmod ��������Ŀ��ģ��
	 *            
	   @throws Exception
	 */
	private static void runChangeVOAry(AggregatedValueObject souVos,
			SuperVO[] tarVos, String chanclassname,String srcmod,String desmod) throws BusinessException {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,desmod);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
	 * �������ݽ���
	 * ֧��CircularlyAccessibleValueObject����
	 * @author mlr 
	 * @˵�������׸ڿ�ҵ��
	 * 2011-10-21����02:14:50
	 * @param souVos ��Դvo
	 * @param tarVoClass Ŀ��vo class����
	 * @param chanclassname ������
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc
	 *            
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			CircularlyAccessibleValueObject[] souVos, CircularlyAccessibleValueObject[] tarVos, String chanclassname)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc
	 * @param srcmod   ����������Դģ��
	 * @param desmod   ��������Ŀ��ģ��        
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			CircularlyAccessibleValueObject[] souVos, CircularlyAccessibleValueObject[] tarVos, String chanclassname,String srcmod,String desmod)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,desmod);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * ֧�ְ����彻��
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����B_��ͷ
	 *            �磺B_cdeptid->B_pk_deptdoc
	 * @param srcmod   ����������Դģ��
	 * @param desmod   ��������Ŀ��ģ��        
	 * @throws Exception
	 */
	public static void runChangeVOAryBody(
			CircularlyAccessibleValueObject[] souVos, CircularlyAccessibleValueObject[] tarVos, String chanclassname,String srcmod,String desmod)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,desmod);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��,֧�ְ�ʵʩ��Ա��̬���õĽ�����������ݽ���
	 * 
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc
	 * @param srcmod   ����������Դģ��
	 * @param desmod   ��������Ŀ��ģ��        
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			SuperVO[] souVos, SuperVO[] tarVos, String chanclassname,String srcmod,String destmod)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClassEdit(chanclassname,srcmod,destmod);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
	 * ��vo���ݽ����÷�����̨����ǰ̨����ʹ��
	 * 
	 * @author mlr
	 * @˵�� 
	 * @ʱ�� 2010-9-26����11:30:42
	 * @param souVo
	 *            ��Դvo
	 * @param tarVo
	 *            Ŀ��vo
	 * @param chanclassname
	 *            vo������
	 *            vo���������д��ʽ
	 *            ����H_��ͷ
	 *            �磺H_cdeptid->H_pk_deptdoc     
	 * @throws Exception
	 */
	public static void runChangeVOAry(
			SuperVO[] souVos, SuperVO[] tarVos, String chanclassname)
	throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// ���ܴ�������ת���쳣 �˴�Ҫ��
			// changeClassName����Ҫ�̳�VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("����ת������쳣��" + change.toString());
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
 * 	�ۺ�vo
 * @author mlr
 *
 */
private static class MyBillVO extends AggregatedValueObject implements IPfBillLock,
	IPfRetBackCheckInfo, IPfRetCheckInfo, INotifyNextBillMaker {
/**
 * 
 */
private static final long serialVersionUID = 1L;

//����VO
CircularlyAccessibleValueObject m_headVo = null;

//�ӱ�VO
CircularlyAccessibleValueObject[] m_itemVos = null;

//��Ϣ��ʾ
private String m_hintMessage = null;

//�Ƿ��͹���������Ϣ
private Boolean m_isSendMessage = new Boolean(false);

//�ж��Ƿ���е�������
private boolean m_isBillLock = true;

//��Ӧ�ĵ����ֶγ�����Ϣ
private IBillField m_billField = null;

/**
 * @return ���� m_billField��
 */
public IBillField getM_billField() {
	return m_billField;
}

/**
 * @param field
 *            Ҫ���õ� m_billField��
 */
public void setM_billField(IBillField field) {
	m_billField = field;
}

/**
 * YcBillVO ������ע�⡣
 */
public MyBillVO() {
	super();
}

/**
 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:36:56)
 * 
 * @return nc.vo.pub.ValueObject[]
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
	return m_itemVos;
}

/**
 * �����������顣 �������ڣ�(2003-6-8 8:01:54)
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
 * �˴����뷽��˵���� �������ڣ�(2003-7-3 8:47:41)
 * 
 * @return java.lang.String
 */
public java.lang.String getMessage() {
	return m_hintMessage;
}

/**
 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:32:28)
 * 
 * @return nc.vo.pub.ValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {
	return m_headVo;
}

/**
 * �˴����뷽��˵���� �������ڣ�(2004-3-12 10:55:39)
 * 
 * @return boolean
 */
public boolean isBillLock() {
	return m_isBillLock;
}

/**
 * �Ƿ��͹������� Ĭ�ϲ����й��������� �������ڣ�(2003-7-6 12:30:36)
 * 
 * @return java.lang.Boolean
 */
public java.lang.Boolean isSendMessage() {
	return m_isSendMessage;
}

/**
 * ���õ��ݷ����������� �������ڣ�(2002-10-16 13:39:32)
 * 
 * @param icheckState
 *            int
 */
public void setCheckMan(java.lang.String approveid) {
	getParentVO().setAttributeValue(
			BillField.getInstance().getField_CheckMan(), approveid);
}

/**
 * �������� �������ڣ�(2002-10-16 13:40:13)
 * 
 * @param strCheckNote
 *            java.lang.String
 */
public void setCheckNote(java.lang.String strCheckNote) {
	getParentVO().setAttributeValue(
			BillField.getInstance().getField_CheckNote(), strCheckNote);
}

/**
 * ��������״̬ �������ڣ�(2002-10-16 13:39:32)
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
 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:36:56)
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
 * �˴����뷽��˵���� �������ڣ�(2004-3-12 10:55:39)
 * 
 * @param newIsBillLock
 *            boolean
 */
public void setIsBillLock(boolean newIsBillLock) {
	m_isBillLock = newIsBillLock;
}

/**
 * �˴����뷽��˵���� �������ڣ�(2003-7-4 14:37:37)
 * 
 * @param msg
 *            java.lang.String
 */
public void setMessage(java.lang.String msg) {
	m_hintMessage = msg;
}

/**
 * �˴����뷽��˵���� �������ڣ�(01-3-20 17:32:28)
 * 
 * @return nc.vo.pub.ValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {
	m_headVo = (CircularlyAccessibleValueObject) parent;
}

/**
 * �����Ƿ�����Ϣ��־�� �������ڣ�(2003-7-6 15:04:37)
 * 
 * @param param
 *            java.lang.Boolean
 */
public void setSendMessage(java.lang.Boolean param) {
	m_isSendMessage = param;
}
}
}
