package nc.bs.pf.change;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.logging.Logger;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.type.IType;
import nc.md.util.MDUtil;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 抽象VO交换类，前后台都可调用
 * @author leijun 2006-8-1
 * @modifier leijun 2007-5-24 缓存目的单据属性的数据类型，提高效率
 * @modifier guowl 2008-6-12 根据新的交换规则执行交换
 * @modifier leijun 2009-8 V56对于单据项目到单据项目的，采取公式批量交换
 */
public abstract class AbstractConversion implements IchangeVO {

	//由平台提供的一些环境变量
	protected String m_strDate = null;

	protected String m_strOperator = null;

	protected String m_strAccountYear;

	protected String m_strCorp;

	protected String m_strNowTime;

	//lj+2006-1-24 源和目的单据类型PK
	protected String m_sourceBilltype = null;

	protected String m_destBilltype;

	//guowl+2008-8-19 目的业务流程
	protected String m_destBusitype;

	//字段映射
	protected String[][] sFields = null;

	//赋值映射
	protected String[][] sAssigns = null;

	/**
	 * 返回交换类型枚举ConversionEnum，默认为单据项目-单据项目
	 * @return
	 * @since 5.5
	 */
	public ConversionPolicyEnum getConversionPolicy() {
		return ConversionPolicyEnum.BILLITEM_BILLITEM;
	}

	public String getSourceBilltype() {
		return m_sourceBilltype;
	}

	public void setSourceBilltype(String sourceBilltype) {
		this.m_sourceBilltype = sourceBilltype;
	}

	public String getDestBilltype() {
		return m_destBilltype;
	}

	public void setDestBilltype(String billtype) {
		m_destBilltype = billtype;
	}

	public String getDestBusitype() {
		return m_destBusitype;
	}

	public void setDestBusitype(String busitype) {
		m_destBusitype = busitype;
	}

	public void setSysCorp(String corpCode) {
		m_strCorp = corpCode;
	}

	public void setSysTime(String time) {
		m_strNowTime = time;
	}

	public void setSysAccountYear(String accountYear) {
		m_strAccountYear = accountYear;
	}

	/**
	 * 设置系统日期
	 * 
	 * @param sysDate String
	 */
	public void setSysDate(String sysDate) {
		m_strDate = sysDate;
	}

	/**
	 * 设置系统操作人
	 * 
	 * @param sysDate String
	 */
	public void setSysOperator(String sysOperator) {
		m_strOperator = sysOperator;
	}

	/**
	 * 检测 
	 * 
	 * @return boolean
	 */
	public boolean isCheck(CircularlyAccessibleValueObject souce,
			CircularlyAccessibleValueObject target) {
		target.setStatus(souce.getStatus());
		return true;
	}

	/**
	 * 所有目的属性 在目的VO中的数据类型
	 * @param targetVO
	 * @return
	 */
	private HashMap getAllFieldToTypeMap(AggregatedValueObject targetVO) {
		ArrayList<String> alDestAttr = new ArrayList<String>();
		if (sAssigns != null && sAssigns.length > 0) {
			//获得赋值规则中所有需要赋值的目的属性
			for (int i = 0; i < (sAssigns == null ? 0 : sAssigns.length); i++)
				alDestAttr.add(sAssigns[i][0]);
		}
		if (sFields != null && sFields.length > 0) {
			// 获得映射规则中所有需要赋值的目的属性
			for (int i = 0; i < (sFields == null ? 0 : sFields.length); i++)
				alDestAttr.add(sFields[i][0]);
		}
		if (getFormulas() != null && getFormulas().length > 0) {
			// 获得公式交换中所有需要赋值的目的属性
			getFormulaParse().setExpressArray(getFormulas());
			VarryVO[] varrys = getFormulaParse().getVarryArray();
			for (int i = 0; i < varrys.length; i++)
				alDestAttr.add(varrys[i].getFormulaName());
		}

		return getFieldToTypeMap(targetVO, alDestAttr);
	}

	/**
	 * 缓存目的属性在目的单据VO中的数据类型
	 * @param targetVO
	 * @param alDestAttr
	 * @return
	 */
	private HashMap getFieldToTypeMap(AggregatedValueObject targetVO, ArrayList alDestAttr) {
		CircularlyAccessibleValueObject headVO = targetVO.getParentVO();
		CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();

		HashMap<String, Class> hmFieldToType = new HashMap<String, Class>();
		for (Iterator iter = alDestAttr.iterator(); iter.hasNext();) {
			String destAttr = (String) iter.next();
			String destField = getFieldName(destAttr);
			if (isParentField(destAttr)) {
				// 获得目的单据主表VO中该字段的数据类型
				Class cl = getFieldType(headVO, destField);
				hmFieldToType.put(destAttr, cl);
			} else if (isChildField(destAttr)) {
				if (bodyVOs != null && bodyVOs.length > 0) {
					// 获得目的单据子表VO中该字段的数据类型
					Class cl = getFieldType(bodyVOs[0], destField);;
					hmFieldToType.put(destAttr, cl);
				}
			}
		}
		return hmFieldToType;
	}

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO) throws BusinessException {
		//
		switch (getConversionPolicy()) {
			case BILLITEM_BILLITEM: //单据项目->单据项目
				return billitemToBillitem(sourceVO, targetVO);
			case METADATA_BILLITEM: //元数据->单据项目
				return billitemToMetadata(sourceVO, targetVO);
			case BILLITEM_METADATA: //单据项目->元数据
				return metadataToBillitem(sourceVO, targetVO);
			case METADATA_METADATA: //元数据->元数据
				return metadataToMetadata(sourceVO, targetVO);
			default:
				break;
		}
		return null;
	}

	private AggregatedValueObject metadataToMetadata(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO) throws BusinessException {
		//1.赋值
		execAssignsForMetadata(targetVO);

		//2.映射规则
		execFieldsMetaToMeta(sourceVO, targetVO);

		//3.公式
		execFormulasMetaToMeta(sourceVO, targetVO);

		// 交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			targetVO = afterClassImpl.retChangeBusiVO(sourceVO, targetVO);
		return targetVO;
	}

	private void execFormulasMetaToMeta(AggregatedValueObject sourceVO, AggregatedValueObject targetVO)
			throws BusinessException {
		String[] formulas = getFormulas();
		if (formulas == null || formulas.length == 0)
			return;

		//执行公式交换
		for (int i = 0; i < formulas.length; i++) {
			Logger.debug("**执行公式交换:" + formulas[i]);
			// 设置表达式
			getFormulaParse().setExpress(formulas[i]);
			// 获得变量
			VarryVO varry = getFormulaParse().getVarry();
			//leijun 2006-3-6 add this break
			if (varry == null)
				continue;

			execFormulaMetaToMeta2(sourceVO, targetVO, varry);
		}
	}

	private void execFormulaMetaToMeta2(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry) throws BusinessException {
		parseMetaFormulaVar(sourceVO, varry);

		String destPath = varry.getFormulaName();
		NCObject ncObj = NCObject.newInstance(targetVO);
		IType iType = ncObj.getRelatedBean().getAttributeByPath(destPath).getDataType();
		Object realValue = null;
		if (isCollectionOfPath(destPath, ncObj.getRelatedBean())) {
			//XXX:取得公式值
			Object[] valueObjs = getFormulaParse().getValueO();
			for (int i = 0; i < valueObjs.length; i++) {
				valueObjs[i] = getValueByIType(valueObjs[i], iType);
			}
			realValue = valueObjs;
		} else {
			//XXX:取得公式值
			Object valueObj = getFormulaParse().getValueAsObject();
			realValue = getValueByIType(valueObj, iType);
		}

		//如果出错，则抛出异常
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);

		// 给目的元数据中的属性赋值
		ncObj.setAttributeValue(destPath, realValue);
	}

	private void parseMetaFormulaVar(AggregatedValueObject sourceVO, VarryVO varry) {
		Logger.debug("**开始执行元数据公式");
		for (int j = 0; j < (varry.getVarry() == null ? 0 : varry.getVarry().length); j++) {
			String var = varry.getVarry()[j];
			Object value = getVarValueFromMetadata(sourceVO, var);

			//设置变量值到公式解析器
			getFormulaParse().addVariable(var, value);
			Logger.debug("**变量var=" + var + ";赋值value=" + value);
		}
	}

	private void execFieldsMetaToMeta(AggregatedValueObject sourceVO, AggregatedValueObject targetVO) {
		if (sFields == null || sFields.length == 0)
			return;
		NCObject ncObj = NCObject.newInstance(targetVO);
		Object value = null;
		for (int i = 0; i < sFields.length; i++) {
			Logger.debug("执行字段交换:" + sFields[i][0] + "->" + sFields[i][1]);
			//取得目的属性的类型
			IType iType = ncObj.getRelatedBean().getAttributeByPath(sFields[i][0]).getDataType();
			//取得来源属性的值
			Object valueObj = getVarValueFromMetadata(sourceVO, sFields[i][1]);
			if (valueObj == null)
				continue;

			if (PfMetadataTools.isObjectArray(valueObj)) {
				//如果来源属性值为数组，即来源属性为子表属性
				int len = Array.getLength(valueObj);
				Object[] objArray = new Object[len];
				for (int j = 0; j < len; j++) {
					//对数组中的每一个元素进行类型转换，转换成目的属性的类型
					objArray[j] = getValueByIType(Array.get(valueObj, j), iType);
				}
				value = objArray;
				if (isCollectionOfPath(sFields[i][0], ncObj.getRelatedBean()))
					//如果目的属性也是集合类型，则将数组赋给它
					ncObj.setAttributeValue(sFields[i][0], value);
				else
					//如果目的属性不是集合类型，则取数组的第一个元素给它赋值
					ncObj.setAttributeValue(sFields[i][0], Array.get(value, 0));

			} else {
				//来源属性值不是数组，则直接把值赋给目的属性
				value = getValueByIType(valueObj, iType);
				ncObj.setAttributeValue(sFields[i][0], value);
			}

		}
	}

	private void execAssignsForMetadata(AggregatedValueObject targetVO) {
		if (sAssigns == null || sAssigns.length == 0)
			return;
		NCObject ncObj = NCObject.newInstance(targetVO);
		for (int i = 0; i < sAssigns.length; i++) {
			Logger.debug(" 执行赋值：" + sAssigns[i][0] + "->" + sAssigns[i][1]);
			Object valueObj = sAssigns[i][1];
			//如果是系统变量
			if (PfUtilBaseTools.isSystemEnvField(sAssigns[i][1])) {
				valueObj = getEnvParamValue(sAssigns[i][1]);
			}

			//获得目的Path对应的元数据属性，并根据其数据类型给其赋值
			IAttribute attr = ncObj.getRelatedBean().getAttributeByPath(sAssigns[i][0]);
			ncObj.setAttributeValue(sAssigns[i][0], getValueByIType(valueObj, attr.getDataType()));
		}
	}

	/**
	 * 将元数据的属性值转化为指定的类型
	 * @param valueObj
	 * @param iType
	 * @return
	 */
	private Object getValueByIType(Object valueObj, IType iType) {
		if (valueObj == null)
			return null;
		Object objTarget = valueObj;
		objTarget = iType.valueOf(valueObj);
		return objTarget;
	}

	/**
	 * 来源：元数据；目的：单据项目
	 * @param sourceVO
	 * @param targetVO
	 * @return
	 * @throws BusinessException
	 */
	private AggregatedValueObject metadataToBillitem(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO) throws BusinessException {
		// 所有目的属性 在目的VO中的数据类型
		HashMap hmFieldToType = getAllFieldToTypeMap(targetVO);

		//为单据项目赋值
		execAssignsForBillitem(targetVO, hmFieldToType);

		// 执行字段值交换
		execFieldsMetaToBill(sourceVO, targetVO, hmFieldToType);

		// 执行公式交换
		execFormulasMetaToBill(sourceVO, targetVO, hmFieldToType);

		// 交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			targetVO = afterClassImpl.retChangeBusiVO(sourceVO, targetVO);
		return targetVO;
	}

	private void execFormulasMetaToBill(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, HashMap hmFieldToType) throws BusinessException {
		String[] formulas = getFormulas();
		if (formulas == null || formulas.length == 0)
			return;

		//执行公式交换
		for (int i = 0; i < formulas.length; i++) {
			Logger.debug("**执行公式交换:" + formulas[i]);
			// 设置表达式
			getFormulaParse().setExpress(formulas[i]);
			// 获得变量
			VarryVO varry = getFormulaParse().getVarry();
			//leijun 2006-3-6 add this break
			if (varry == null)
				continue;

			if (isParentField(varry.getFormulaName()))

				execHeadFormulaMetaToBill(sourceVO, targetVO, varry, (Class) hmFieldToType.get(varry
						.getFormulaName()));
			else

				execBodyFormulaMetaToBill(sourceVO, targetVO, varry, (Class) hmFieldToType.get(varry
						.getFormulaName()));
		}

	}

	/**
	 * 来源是元数据，目的是单据的表头
	 * @param sourceVO
	 * @param targetVO
	 * @param varry
	 * @param class1
	 * @throws BusinessException 
	 */
	private void execHeadFormulaMetaToBill(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry, Class destAttrType) throws BusinessException {

		Object valueObj = getFormulaResultSingleForMeta(sourceVO, varry);

		// 给目的单据中的表头属性赋值
		String destField = getFieldName(varry.getFormulaName());
		setValueToVo(valueObj, targetVO.getParentVO(), destField, destAttrType);

	}

	private Object getFormulaResultSingleForMeta(AggregatedValueObject sourceVO, VarryVO varry)
			throws PFBusinessException {
		parseMetaFormulaVar(sourceVO, varry);

		//XXX:取得公式值，如果出错，则抛出异常
		Object valueObj = getFormulaParse().getValueAsObject();
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);
		return valueObj;
	}

	/**
	 * 为单据项目赋值
	 * @param targetVO
	 * @param hmFieldToType
	 */
	private void execAssignsForBillitem(AggregatedValueObject targetVO, HashMap hmFieldToType) {
		if (sAssigns == null || sAssigns.length == 0)
			return;

		if (hmFieldToType == null)
			hmFieldToType = getAllFieldToTypeMap(targetVO);

		// 执行赋值
		for (int i = 0; i < sAssigns.length; i++) {
			Logger.debug("执行赋值:" + sAssigns[i][0] + "->" + sAssigns[i][1]);

			Object valueObj = sAssigns[i][1];
			String destField = getFieldName(sAssigns[i][0]);
			//如果是系统变量
			if (PfUtilBaseTools.isSystemEnvField(sAssigns[i][1])) {
				valueObj = getEnvParamValue(sAssigns[i][1]);
			}

			if (isParentField(sAssigns[i][0])) {
				//目的是表头
				CircularlyAccessibleValueObject headVO = targetVO.getParentVO();
				setValueToVo(valueObj, headVO, destField, (Class) hmFieldToType.get(sAssigns[i][0]));
			} else {
				//目的是表体
				CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();
				for (int j = 0; j < (bodyVOs == null ? 0 : bodyVOs.length); j++)
					setValueToVo(valueObj, bodyVOs[j], destField, (Class) hmFieldToType.get(sAssigns[i][0]));
			}
		}
	}

	private void execFieldsMetaToBill(AggregatedValueObject sourceVO, AggregatedValueObject targetVO,
			HashMap hmFieldToType) {
		if (sFields == null || sFields.length == 0)
			return;

		if (hmFieldToType == null)
			hmFieldToType = getAllFieldToTypeMap(targetVO);

		// 执行字段间的转换
		for (int i = 0; i < sFields.length; i++) {
			Logger.debug("执行字段交换:" + sFields[i][0] + "->" + sFields[i][1]);

			Object valueObj = null;
			String destField = getFieldName(sFields[i][0]);

			valueObj = getVarValueFromMetadata(sourceVO, sFields[i][1]);
			if (valueObj == null)
				continue;

			//判定值是否为数组类型
			boolean isArray = valueObj == null ? false : PfMetadataTools.isObjectArray(valueObj);

			if (isParentField(sFields[i][0])) {
				CircularlyAccessibleValueObject headVO = targetVO.getParentVO();
				setValueToVo(isArray ? Array.get(valueObj, 0) : valueObj, headVO, destField,
						(Class) hmFieldToType.get(sFields[i][0]));
			} else {
				CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();
				for (int j = 0; j < (bodyVOs == null ? 0 : bodyVOs.length); j++) {
					Object realValue = valueObj;
					if (isArray) {
						int iLen = Array.getLength(valueObj);
						if (j < iLen)
							realValue = Array.get(valueObj, j);
						else
							realValue = Array.get(valueObj, iLen - 1);
					}
					setValueToVo(realValue, bodyVOs[j], destField, (Class) hmFieldToType.get(sFields[i][0]));
				}
			}
		}

	}

	private AggregatedValueObject billitemToMetadata(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO) throws BusinessException {
		// 赋值
		execAssignsForMetadata(targetVO);

		// 执行映射交换
		execFieldsBillToMeta(sourceVO, targetVO);

		// 执行公式交换
		execFormulaBillToMeta(sourceVO, targetVO);

		// 交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			targetVO = afterClassImpl.retChangeBusiVO(sourceVO, targetVO);
		return targetVO;
	}

	/**
	 * 执行公式交换，来源是单据项目，目的是元数据
	 * @param sourceVO
	 * @param targetVO
	 * @throws BusinessException
	 */
	private void execFormulaBillToMeta(AggregatedValueObject sourceVO, AggregatedValueObject targetVO)
			throws BusinessException {

		String[] formulas = getFormulas();
		if (formulas == null || formulas.length == 0)
			return;

		//执行公式交换
		for (int i = 0; i < formulas.length; i++) {
			Logger.debug("**执行公式交换:" + formulas[i]);
			// 设置表达式
			getFormulaParse().setExpress(formulas[i]);
			// 获得变量
			VarryVO varry = getFormulaParse().getVarry();
			//leijun 2006-3-6 add this break
			if (varry == null)
				continue;

			String destPath = varry.getFormulaName();
			NCObject ncObj = NCObject.newInstance(targetVO);
			IType iType = ncObj.getRelatedBean().getAttributeByPath(destPath).getDataType();
			if (isCollectionOfPath(destPath, ncObj.getRelatedBean())) {
				Object[] objValues = getFormulaResultCollectionForBill(sourceVO, targetVO, varry);
				//将公式值进行类型转换
				Object[] values = new Object[objValues.length];
				for (int j = 0; j < objValues.length; j++) {
					values[j] = getValueByIType(objValues[j], iType);
				}
				// 给目的元数据中的属性赋值
				ncObj.setAttributeValue(destPath, values);
			} else {
				//XXX:取得公式值，如果出错，则抛出异常
				Object valueObj = getFormulaResultSingleForBill(sourceVO, targetVO, varry);
				// 给目的元数据中的属性赋值
				ncObj.setAttributeValue(destPath, getValueByIType(valueObj, iType));
			}
		}
	}

	/**
	 * 来源是元数据；目的是单据表体
	 * @param sourceVO
	 * @param targetVO
	 * @param varry
	 * @param class1
	 * @throws BusinessException
	 */
	private void execBodyFormulaMetaToBill(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry, Class destAttrType) throws BusinessException {

		Object[] objValues = getFormulaResultAryForMeta(sourceVO, varry);

		// 给目的单据中的表体属性赋值
		String destField = getFieldName(varry.getFormulaName());
		CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();
		if (objValues != null && objValues.length > 0) {
			for (int i = 0; i < bodyVOs.length; i++)
				if (i >= objValues.length)
					setValueToVo(objValues[objValues.length - 1], bodyVOs[i], destField, destAttrType);
				else
					setValueToVo(objValues[i], bodyVOs[i], destField, destAttrType);
		} else {
			for (int i = 0; i < bodyVOs.length; i++)
				bodyVOs[i].setAttributeValue(destField, null);
		}

	}

	private Object[] getFormulaResultAryForMeta(AggregatedValueObject sourceVO, VarryVO varry)
			throws PFBusinessException {
		parseMetaFormulaVar(sourceVO, varry);

		//XXX:取得公式值，如果出错，则抛出异常
		Object[] objValues = getFormulaParse().getValueO();
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);
		return objValues;
	}

	/**
	 * 根据单据项目-单据项目规则样式，来转换VO对象
	 * @param sourceVO
	 * @param targetVO
	 * @return
	 * @throws BusinessException
	 */
	private AggregatedValueObject billitemToBillitem(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO) throws BusinessException {
		// 所有目的属性 在目的VO中的数据类型
		HashMap hmFieldToType = getAllFieldToTypeMap(targetVO);

		//赋值
		execAssignsForBillitem(targetVO, hmFieldToType);

		// 执行字段值交换
		execFieldBillToBill(sourceVO, targetVO, hmFieldToType);

		// 执行公式交换
		execFormulasBillToBill2(sourceVO, targetVO, hmFieldToType);

		// 交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			targetVO = afterClassImpl.retChangeBusiVO(sourceVO, targetVO);
		return targetVO;
	}

	public AggregatedValueObject[] retChangeBusiVOs(AggregatedValueObject[] sorceVOs,
			AggregatedValueObject[] tagVOs) throws BusinessException {
		if (sorceVOs == null)
			return null;

		switch (getConversionPolicy()) {
			case BILLITEM_BILLITEM:
				return billitemToBillitemBatch(sorceVOs, tagVOs);
			case BILLITEM_METADATA:
				return metadataToBillitemBatch(sorceVOs, tagVOs);
			case METADATA_BILLITEM:
				return billitemToMetadataBatch(sorceVOs, tagVOs);
			case METADATA_METADATA:
				return metadataToMetadataBatch(sorceVOs, tagVOs);
			default:
				break;
		}

		return null;
	}

	private AggregatedValueObject[] metadataToMetadataBatch(AggregatedValueObject[] sorceVOs,
			AggregatedValueObject[] tagVOs) throws BusinessException {
		for (int i = 0; i < sorceVOs.length; i++) {
			AggregatedValueObject sourceVO = sorceVOs[i];
			AggregatedValueObject targetVO = tagVOs[i];
			//1.赋值
			execAssignsForMetadata(targetVO);

			//2.映射规则
			execFieldsMetaToMeta(sourceVO, targetVO);

			//3.公式
			execFormulasMetaToMeta(sourceVO, targetVO);
		}

		//3.交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			tagVOs = afterClassImpl.retChangeBusiVOs(sorceVOs, tagVOs);

		return tagVOs;
	}

	private AggregatedValueObject[] metadataToBillitemBatch(AggregatedValueObject[] sorceVOs,
			AggregatedValueObject[] tagVOs) throws BusinessException {
		// 所有目的属性 在目的VO中的数据类型
		HashMap hmFieldToType = getAllFieldToTypeMap(tagVOs[0]);

		for (int i = 0; i < sorceVOs.length; i++) {
			AggregatedValueObject sourceVO = sorceVOs[i];
			AggregatedValueObject targetVO = tagVOs[i];
			//为单据项目赋值
			execAssignsForBillitem(targetVO, hmFieldToType);

			// 执行字段值交换
			execFieldsMetaToBill(sourceVO, targetVO, hmFieldToType);

			// 执行公式交换
			execFormulasMetaToBill(sourceVO, targetVO, hmFieldToType);
		}

		//3.交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			tagVOs = afterClassImpl.retChangeBusiVOs(sorceVOs, tagVOs);

		return tagVOs;
	}

	private AggregatedValueObject[] billitemToMetadataBatch(AggregatedValueObject[] sorceVOs,
			AggregatedValueObject[] tagVOs) throws BusinessException {

		for (int i = 0; i < sorceVOs.length; i++) {
			AggregatedValueObject sourceVO = sorceVOs[i];
			AggregatedValueObject targetVO = tagVOs[i];
			// 赋值
			execAssignsForMetadata(targetVO);

			// 执行映射交换
			execFieldsBillToMeta(sourceVO, targetVO);

			// 执行公式交换
			execFormulaBillToMeta(sourceVO, targetVO);
		}

		//3.交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			tagVOs = afterClassImpl.retChangeBusiVOs(sorceVOs, tagVOs);

		return tagVOs;
	}

	/**
	 * 根据单据项目-单据项目规则样式，来批量转换VO对象
	 * @param sourceVO
	 * @param targetVO
	 * @return
	 * @throws BusinessException
	 */
	private AggregatedValueObject[] billitemToBillitemBatch(AggregatedValueObject[] sorceVOs,
			AggregatedValueObject[] tagVOs) throws BusinessException {
		// 所有目的属性 在目的VO中的数据类型
		HashMap hmFieldToType = getAllFieldToTypeMap(tagVOs[0]);

		for (int i = 0; i < sorceVOs.length; i++) {
			AggregatedValueObject sorceVO = sorceVOs[i];
			AggregatedValueObject tagVO = tagVOs[i];
			//执行赋值
			execAssignsForBillitem(tagVO, hmFieldToType);
			// 1.执行字段值交换
			execFieldBillToBill(sorceVO, tagVO, hmFieldToType);
			// 2.执行公式
			execFormulasBillToBill2(sorceVO, tagVO, hmFieldToType);
		}

		//3.交换后继类 的额外处理
		IchangeVO afterClassImpl = getAfterClass();
		if (afterClassImpl != null)
			tagVOs = afterClassImpl.retChangeBusiVOs(sorceVOs, tagVOs);

		return tagVOs;
	}

	/**
	 * 判断是否主表项目
	 * <li>如果以"H_"开头，则表示主表；否则
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isParentField(String key) {
		if (key == null)
			return false;
		if (key.startsWith("H_"))
			return true;
		return false;
	}

	/**
	 * 判断是否子表项目，added by guowl
	 * <li>如果以"B_"开头，则表示子表；否则
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isChildField(String key) {
		if (key == null)
			return false;
		if (key.startsWith("B_"))
			return true;
		return false;
	}

	/**
	 * 交换主表字段
	 * 
	 * @param sourceVO 来源单据聚合VO
	 * @param valueVO 目的单据聚合VO
	 * @param destAttr 目的属性
	 * @param srcAttr 来源属性
	 * @param destAttrType 目的属性的数据类型
	 */
	protected void exchangeHeadField(AggregatedValueObject sourceVO, AggregatedValueObject valueVO,
			String destAttr, String srcAttr, Class destAttrType) {

		CircularlyAccessibleValueObject headVO = valueVO.getParentVO();
		CircularlyAccessibleValueObject[] bodyVOs = valueVO.getChildrenVO();
		CircularlyAccessibleValueObject srcheadVO = sourceVO.getParentVO();
		CircularlyAccessibleValueObject[] srcbodyVOs = sourceVO.getChildrenVO();

		String destField = getFieldName(destAttr);
		String srcField = getFieldName(srcAttr);

		//如果来源值为一些系统变量，则平台赋值
		Object valueObj = null;
		if (PfUtilBaseTools.isSystemEnvField(srcField)) {
			valueObj = getEnvParamValue(srcField);
		} else {
			if (isParentField(srcAttr))
				valueObj = srcheadVO.getAttributeValue(srcField);
			else if (bodyVOs != null && srcbodyVOs.length > 0)
				//XXX::取子表的第一个
				valueObj = srcbodyVOs[0].getAttributeValue(srcField);
		}

		setValueToVo(valueObj, headVO, destField, destAttrType);
	}

	/**
	 * 交换子表数据
	 * 
	 * @param sourceVO 来源VO
	 * @param targetVO 目的VO
	 * @param destAttr 目的属性
	 * @param srcAttr 取值
	 * @param destAttrType 目的属性的数据类型
	 */
	protected void exchangeBodyField(AggregatedValueObject sourceVO, AggregatedValueObject targetVO,
			String destAttr, String srcAttr, Class destAttrType) {

		CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();
		CircularlyAccessibleValueObject srcheadVO = sourceVO.getParentVO();
		CircularlyAccessibleValueObject[] srcbodyVOs = sourceVO.getChildrenVO();

		String destField = getFieldName(destAttr);
		String srcField = getFieldName(srcAttr);

		for (int i = 0; i < (bodyVOs == null ? 0 : bodyVOs.length); i++)
			if (isCheck(srcbodyVOs[i], bodyVOs[i])) {
				Object valueObj = null;
				if (PfUtilBaseTools.isSystemEnvField(srcField)) {
					valueObj = getEnvParamValue(srcField);
				} else if (isParentField(srcAttr)) {
					valueObj = srcheadVO.getAttributeValue(srcField);
				} else {
					valueObj = srcbodyVOs[i].getAttributeValue(srcField);
				}

				setValueToVo(valueObj, bodyVOs[i], destField, destAttrType);
			}
	}

	/**
	 * 执行字段属性的转换
	 * <li>比如：B_vproducenum->B_vbatchcode
	 * <li>比如：H_coperator->SYSOPERATOR
	 * 
	 * @param sourceVO 来源单据聚合VO
	 * @param targetVO 目的单据聚合VO
	 * @param hmFieldToType 属性类型映射
	 */
	public void execFieldBillToBill(AggregatedValueObject sourceVO, AggregatedValueObject targetVO,
			HashMap hmFieldToType) {
		if (sFields == null || sFields.length == 0)
			return;

		if (hmFieldToType == null)
			hmFieldToType = getAllFieldToTypeMap(targetVO);

		// 执行字段间的转换
		for (int i = 0; i < sFields.length; i++) {
			Logger.debug("执行字段交换:" + sFields[i][0] + "->" + sFields[i][1]);
			if (isParentField(sFields[i][0]))
				exchangeHeadField(sourceVO, targetVO, sFields[i][0], sFields[i][1], (Class) hmFieldToType
						.get(sFields[i][0]));
			else
				exchangeBodyField(sourceVO, targetVO, sFields[i][0], sFields[i][1], (Class) hmFieldToType
						.get(sFields[i][0]));
		}
	}

	/**
	 * 来源：单据项目；目的：元数据
	 * @param sourceVO
	 * @param targetVO
	 */
	private void execFieldsBillToMeta(AggregatedValueObject sourceVO, AggregatedValueObject targetVO) {
		if (sFields == null || sFields.length == 0)
			return;

		CircularlyAccessibleValueObject srcheadVO = sourceVO.getParentVO();
		CircularlyAccessibleValueObject[] srcbodyVOs = sourceVO.getChildrenVO();

		NCObject ncObj = NCObject.newInstance(targetVO);
		// 执行字段间的转换
		for (int i = 0; i < sFields.length; i++) {
			Logger.debug("执行字段交换:" + sFields[i][0] + "->" + sFields[i][1]);

			String srcField = getFieldName(sFields[i][1]);
			IAttribute attr = ncObj.getRelatedBean().getAttributeByPath(sFields[i][0]);
			IType iType = attr.getDataType();

			Object valueObj = null;
			if (isParentField(sFields[i][1])) {
				valueObj = getValueByIType(srcheadVO.getAttributeValue(srcField), iType);
			} else if (srcbodyVOs != null && srcbodyVOs.length > 0) {
				if (isCollectionOfPath(sFields[i][0], ncObj.getRelatedBean())) {
					//对于集合类型，需遍历来源子表VO数组获取值
					Object[] valueObj2 = new Object[srcbodyVOs.length];
					for (int k = 0; k < srcbodyVOs.length; k++)
						valueObj2[k] = getValueByIType(srcbodyVOs[k].getAttributeValue(srcField), iType);
					valueObj = valueObj2;
				} else {
					//XXX::取子表的第一个
					valueObj = getValueByIType(srcbodyVOs[0].getAttributeValue(srcField), iType);
				}
			}

			ncObj.setAttributeValue(sFields[i][0], valueObj);
		}
	}

	/**
	 * 判断某path对应的属性链中 是否存在集合类型
	 * @param strPath 比如a.b, details.c等
	 * @param bean
	 * @return
	 */
	private boolean isCollectionOfPath(String strPath, IBean bean) {
		IAttribute attr = null;
		int npos = strPath.indexOf('.');
		if (npos < 0) {
			attr = bean.getAttributeByName(strPath);
			return MDUtil.isCollectionType(attr.getDataType());
		} else {
			String[] paths = strPath.split("\\.");
			StringBuffer newPath = new StringBuffer();
			for (int j = 0; j < paths.length; j++) {
				newPath.append(paths[j]);
				attr = bean.getAttributeByPath(newPath.toString());
				if (MDUtil.isCollectionType(attr.getDataType()))
					return true;
				newPath.append(".");
			}
		}
		return false;
	}

	/**
	 * 源是billitem，目的是metadata
	 * @param sourceVO
	 * @param targetVO
	 * @param destAttr
	 * @param srcAttr
	 * @param type
	 */
	protected void exchangeMetadata(AggregatedValueObject sourceVO, AggregatedValueObject targetVO,
			String destAttr, String srcAttr, IType type) {

		CircularlyAccessibleValueObject srcheadVO = sourceVO.getParentVO();
		CircularlyAccessibleValueObject[] srcbodyVOs = sourceVO.getChildrenVO();

		String srcField = getFieldName(srcAttr);

		Object valueObj = null;

		if (isParentField(srcAttr))
			valueObj = srcheadVO.getAttributeValue(srcField);
		else if (srcbodyVOs.length > 0)
			//XXX::取子表的第一个
			valueObj = srcbodyVOs[0].getAttributeValue(srcField);

		NCObject ncObj = NCObject.newInstance(targetVO);
		IAttribute attr = ncObj.getRelatedBean().getAttributeByPath(destAttr);
		ncObj.setAttributeValue(destAttr, getValueByIType(valueObj, attr.getDataType()));

	}

	/**
	 * 从单据聚合VO中获取变量的值，以做为主表公式的变量值
	 * <li>例如：H_bcalculatedinvcost->iif(H_iscalculatedinvcost == null, \"N\",B_iscalculatedinvcost)
	 * 
	 * @param valueVO 单据聚合VO
	 * @param srcAttr 变量名，可能为主VO字段，也可能为子VO字段
	 * @return
	 */
	protected Object getVarValueForHeadFormula(AggregatedValueObject valueVO, String srcAttr) {
		CircularlyAccessibleValueObject headVO = valueVO.getParentVO();
		CircularlyAccessibleValueObject[] bodyVOs = valueVO.getChildrenVO();

		String srcField = getFieldName(srcAttr);

		//如果取值为一些系统变量，则平台赋值
		Object valueObj = null;
		if (PfUtilBaseTools.isSystemEnvField(srcField)) {
			valueObj = getEnvParamValue(srcField);
		} else {
			if (isParentField(srcAttr)) {
				valueObj = headVO.getAttributeValue(srcField);
			} else {
				//如果取子表VO属性，则取第一个表体VO的值
				if (bodyVOs != null && bodyVOs.length > 0)
					valueObj = bodyVOs[0].getAttributeValue(srcField);
			}
		}

		if (valueObj != null && !valueObj.equals("")) {
			if (valueObj instanceof String || valueObj instanceof UFDate)
				valueObj = valueObj.toString();
		}

		return valueObj;
	}

	private Object getEnvParamValue(String srcField) {
		Object valueObj = null;
		if (srcField.equals(PfUtilBaseTools.ENV_LOGINDATE)) {
			valueObj = m_strDate;
		} else if (srcField.equals(PfUtilBaseTools.ENV_LOGINUSER)) {
			valueObj = m_strOperator;
		} else if (srcField.equals(PfUtilBaseTools.ENV_ACCOUNTYEAR)) {
			valueObj = m_strAccountYear;
		} else if (srcField.equals(PfUtilBaseTools.ENV_LOGINCORP)) {
			valueObj = m_strCorp;
		} else if (srcField.equals(PfUtilBaseTools.ENV_NOWTIME)) {
			valueObj = m_strNowTime;
		} else if (srcField.equals(PfUtilBaseTools.ENV_DEST_BILLTYPE)) {
			valueObj = m_destBilltype;
		} else if (srcField.equals(PfUtilBaseTools.ENV_DEST_BUSITYPE)) {
			valueObj = m_destBusitype;
		}
		return valueObj;
	}

	/**
	 * 从单据聚合VO中获取变量的值，以做为子表公式的变量值
	 * <li>例如：B_bcalculatedinvcost->iif(H_iscalculatedinvcost == null, \"N\",B_iscalculatedinvcost)
	 * 
	 * @param sourceVO 来源单据聚合VO
	 * @param targetVO 目的单据聚合VO
	 * @param srcAttr 变量名，可能为主VO字段，也可能为子VO字段
	 * @return
	 */
	protected Object[] getVarValueForBodyFormula(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, String srcAttr) {

		CircularlyAccessibleValueObject srcheadVO = sourceVO.getParentVO();
		CircularlyAccessibleValueObject[] srcbodyVOs = sourceVO.getChildrenVO();

		CircularlyAccessibleValueObject destHeadVO = targetVO.getParentVO();
		CircularlyAccessibleValueObject[] destBodyVOs = targetVO.getChildrenVO();

		if (srcbodyVOs == null)
			return null;

		//变量值个数=来源单据表体数
		Object[] values = new Object[srcbodyVOs.length];

		String srcField = getFieldName(srcAttr);

		//如果取值为一些系统变量，则平台赋值
		Object valueObj = null;
		if (PfUtilBaseTools.isSystemEnvField(srcField)) {
			valueObj = getEnvParamValue(srcField);
			for (int i = 0; i < srcbodyVOs.length; i++)
				values[i] = valueObj.toString();
		} else {
			if (isParentField(srcAttr)) { //主表字段
				//从来源单据表头VO中取值
				valueObj = srcheadVO.getAttributeValue(srcField);
				if (valueObj == null)
					//从目的单据表头VO中取值
					valueObj = destHeadVO.getAttributeValue(srcField);
				if (valueObj != null && !valueObj.equals("")) {
					if (valueObj instanceof UFDate)
						valueObj = valueObj.toString();
					// }else{
					// value = "\"\"";
				}

				//返回值赋值
				for (int i = 0; i < srcbodyVOs.length; i++)
					values[i] = valueObj;
			} else { //子表字段
				for (int i = 0; i < srcbodyVOs.length; i++) {
					//从来源单据表体VO中取值
					valueObj = srcbodyVOs[i].getAttributeValue(srcField);
					if (valueObj == null)
						//从目的单据表体VO中取值
						valueObj = destBodyVOs[i].getAttributeValue(srcField);

					if (valueObj != null && !valueObj.equals("")) {
						if (valueObj instanceof UFDate) {
							values[i] = valueObj.toString();
						} else
							values[i] = valueObj;
						// }else{
						// values[i] = "\"\"";
					}
				}
			}
		}

		return values;
	}

	private Object getVarValueFromMetadata(AggregatedValueObject sourceVO, String srcPath) {
		Object valueObj = null;
		//如果是系统变量
		if (PfUtilBaseTools.isSystemEnvField(srcPath))
			valueObj = getEnvParamValue(srcPath);
		else {
			NCObject ncObj = NCObject.newInstance(sourceVO);
			valueObj = ncObj.getAttributeValue(srcPath);
			if (valueObj != null && !valueObj.equals("")) {
				if (valueObj instanceof String || valueObj instanceof UFDate)
					valueObj = valueObj.toString();
			}
		}
		return valueObj;
	}

	/**
	 * 执行表头公式
	 * @param sourceVO 来源单据聚合VO
	 * @param targetVO 目的单据聚合VO
	 * @param varry 公式变量
	 * @param destAttrType 目的属性数据类型
	 * @throws PFBusinessException 
	 */
	protected void setHeadFormulaResult(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry, Class destAttrType) throws BusinessException {

		Object valueObj = getFormulaResultSingleForBill(sourceVO, targetVO, varry);

		// 给目的单据中的表头属性赋值
		String destField = getFieldName(varry.getFormulaName());
		setValueToVo(valueObj, targetVO.getParentVO(), destField, destAttrType);
	}

	private Object getFormulaResultSingleForBill(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry) throws PFBusinessException {
		Logger.debug("**开始执行表头公式");
		String[] strVars = varry.getVarry();
		for (int j = 0; j < (strVars == null ? 0 : strVars.length); j++) {
			String strVar = strVars[j];
			//从来源单据VO中取得变量值
			Object value = getVarValueForHeadFormula(sourceVO, strVar);

			//从目的单据VO中取得变量值
			if (value == null)
				value = getVarValueForHeadFormula(targetVO, strVar);

			//设置变量值到公式解析器
			getFormulaParse().addVariable(strVar, value);
			Logger.debug("**变量var=" + strVar + ";赋值value=" + value);
		}

		//XXX:取得公式值，如果出错，则抛出异常
		Object valueObj = getFormulaParse().getValueAsObject();
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);
		return valueObj;
	}

	private Object[] getFormulaResultCollectionForBill(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry) throws PFBusinessException {
		Logger.debug("**开始执行表体公式");
		CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();
		if (bodyVOs == null || bodyVOs.length == 0)
			return null;
		String[] strVars = varry.getVarry();
		for (int j = 0; j < (strVars == null ? 0 : strVars.length); j++) {
			String strVar = strVars[j];
			//从来源或目的VO中取得变量值，变量值个数=来源单据表体数
			Object[] values = getVarValueForBodyFormula(sourceVO, targetVO, strVar);
			getFormulaParse().addVariable(strVar, values);
		}

		//XXX:取得公式值，如果出错，则抛出异常
		Object[] objValues = getFormulaParse().getValueO();
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);
		return objValues;
	}

	/**
	 * 执行表体公式  
	 *  
	 * @param sourceVO 来源单据聚合VO
	 * @param targetVO 目的单据聚合VO
	 * @param varry 公式变量
	 * @param destAttrType 目的属性数据类型
	 * @throws BusinessException 
	 */
	protected void setBodyFormulaResult(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, VarryVO varry, Class destAttrType) throws BusinessException {
		CircularlyAccessibleValueObject[] srcbodyVOs = sourceVO.getChildrenVO();
		CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();

		// 给目的单据中的表体属性赋值
		if (bodyVOs == null || bodyVOs.length == 0)
			return;

		Object[] objValues = getFormulaResultCollectionForBill(sourceVO, targetVO, varry);
		String destField = getFieldName(varry.getFormulaName());
		if (objValues != null && objValues.length > 0) {
			for (int i = 0; i < bodyVOs.length; i++)
				if (isCheck(srcbodyVOs[i], bodyVOs[i])) {
					if (i >= objValues.length)
						setValueToVo(objValues[objValues.length - 1], bodyVOs[i], destField, destAttrType);
					else
						setValueToVo(objValues[i], bodyVOs[i], destField, destAttrType);
				}
		} else {
			for (int i = 0; i < bodyVOs.length; i++)
				if (isCheck(srcbodyVOs[i], bodyVOs[i])) {
					bodyVOs[i].setAttributeValue(destField, null);
				}
		}
	}

	/**
	 * 初始化公式解析器。
	 * @modifier 雷军 2005-3-10 打印出用户注册的函数
	 */
	protected void initFormulaParse(UserDefineFunction[] funVos) {
		Logger.debug(">>>AbstractConversion.initFormulaParse() called");
		if (funVos == null)
			return;
		for (int i = 0; i < funVos.length; i++) {
			String className = funVos[i].getClassName();
			String methodName = funVos[i].getMethodName();
			Class returnType = funVos[i].getReturnType();
			Logger.debug(">>>className=" + className);
			Logger.debug(">>>methodName=" + methodName);
			Logger.debug(">>>returnType=" + returnType);
			// 把自定义函数设置到公式解析器中
			getFormulaParse().setSelfMethod(className, methodName, returnType, funVos[i].getArgTypes(),
					false);
		}
	}

	/**
	 * 初始化
	 */
	public void init() {
		initFormulaParse(getUserDefineFunction());
		initField();
		initAssign();
	}

	/**
	 * 执行带有公式的转换 
	 * <li>比如：B_cqpbaseschemeid->iif((H_pk_corp == SYSCORP),B_cqpbaseschemeid,null)
	 * 
	 * @param sourceVO 来源单据聚合VO
	 * @param targetVO 目的单据聚合VO
	 * @param hmFieldToType 属性类型映射
	 * @throws BusinessException 
	 * @deprecated 5.6 对于单据项目到单据项目的，采取公式批量交换
	 */
	public void execFormulasBillToBill(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, HashMap hmFieldToType) throws BusinessException {
		String[] formulas = getFormulas();
		if (formulas == null || formulas.length == 0)
			return;

		if (hmFieldToType == null)
			hmFieldToType = getAllFieldToTypeMap(targetVO);

		//执行公式交换
		for (int i = 0; i < formulas.length; i++) {
			Logger.debug("**执行公式交换:" + formulas[i]);
			// 设置表达式
			getFormulaParse().setExpress(formulas[i]);
			// 获得变量
			VarryVO varry = getFormulaParse().getVarry();
			//leijun 2006-3-6 add this break
			if (varry == null)
				continue;

			if (isParentField(varry.getFormulaName()))
				setHeadFormulaResult(sourceVO, targetVO, varry, (Class) hmFieldToType.get(varry
						.getFormulaName()));
			else
				setBodyFormulaResult(sourceVO, targetVO, varry, (Class) hmFieldToType.get(varry
						.getFormulaName()));
		}
	}

	/**
	 * 执行带有公式的转换 
	 * <li>比如：B_cqpbaseschemeid->iif((H_pk_corp == SYSCORP),B_cqpbaseschemeid,null)
	 * 
	 * @param sourceVO 来源单据聚合VO
	 * @param targetVO 目的单据聚合VO
	 * @param hmFieldToType 属性类型映射
	 * @throws BusinessException 
	 * @modifier leijun 2009-8 采取公式批量交换，避免前台交换时可能导致的多次远程调用
	 */
	public void execFormulasBillToBill2(AggregatedValueObject sourceVO,
			AggregatedValueObject targetVO, HashMap hmFieldToType) throws BusinessException {
		String[] formulas = getFormulas();
		if (formulas == null || formulas.length == 0)
			return;

		if (hmFieldToType == null)
			hmFieldToType = getAllFieldToTypeMap(targetVO);

		//执行公式交换
		//1.分别收集表头公式和表体公式
		ArrayList<String> alHeadFormula = new ArrayList<String>();
		ArrayList<String> alBodyFormula = new ArrayList<String>();
		getFormulaParse().setExpressArray(formulas);
		VarryVO[] varrys = getFormulaParse().getVarryArray();
		for (int i = 0; i < varrys.length; i++) {
			String formulaName = varrys[i].getFormulaName();
			if (isParentField(formulaName)) {
				alHeadFormula.add(formulas[i]);
			} else {
				alBodyFormula.add(formulas[i]);
			}
		}
		Logger.debug(">>收集到表头公式=" + alHeadFormula);
		Logger.debug(">>收集到表体公式=" + alBodyFormula);

		//2.批量执行表头公式
		getFormulaParse().setExpressArray(alHeadFormula.toArray(new String[0]));
		VarryVO[] hVarrys = getFormulaParse().getVarryArray();
		ArrayList<String> alHeadFormulaName = new ArrayList<String>();
		for (VarryVO varryVO : hVarrys) {
			alHeadFormulaName.add(varryVO.getFormulaName());
			String[] strVars = varryVO.getVarry();
			//表头公式，比如H_b->H_c或H_b->B_c
			for (int j = 0; j < (strVars == null ? 0 : strVars.length); j++) {
				String strVar = strVars[j];
				//从来源单据VO中取得变量值
				Object value = getVarValueForHeadFormula(sourceVO, strVar);

				//从目的单据VO中取得变量值
				if (value == null)
					value = getVarValueForHeadFormula(targetVO, strVar);

				//设置变量值到公式解析器
				getFormulaParse().addVariable(strVar, value);
				Logger.debug("**变量var=" + strVar + ";赋值value=" + value);
			}
		}
		//公式执行结果为[n][1]
		Object[][] objValues = getFormulaParse().getValueOArray();
		String errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);
		//给目的单据中的表头属性赋值
		for (int i = 0; i < alHeadFormulaName.size(); i++) {
			String hFormulaName = alHeadFormulaName.get(i);
			String destHeadField = getFieldName(hFormulaName);
			setValueToVo(objValues[i][0], targetVO.getParentVO(), destHeadField, (Class) hmFieldToType
					.get(hFormulaName));
		}

		//3.批量执行表体公式
		CircularlyAccessibleValueObject[] bodyVOs = targetVO.getChildrenVO();
		if (bodyVOs == null || bodyVOs.length == 0)
			return;
		getFormulaParse().setExpressArray(alBodyFormula.toArray(new String[0]));
		VarryVO[] bVarrys = getFormulaParse().getVarryArray();
		ArrayList<String> alBodyFormulaName = new ArrayList<String>();
		for (VarryVO varryVO : bVarrys) {
			alBodyFormulaName.add(varryVO.getFormulaName());
			String[] strVars = varryVO.getVarry();
			//表体公式，比如B_c->H_d或B_c->B_e
			for (int j = 0; j < (strVars == null ? 0 : strVars.length); j++) {
				String strVar = strVars[j];
				//从来源或目的VO中取得变量值，变量值个数=来源单据表体数
				Object[] values = getVarValueForBodyFormula(sourceVO, targetVO, strVar);
				getFormulaParse().addVariable(strVar, values);
			}
		}

		//公式执行结果为[n][m]，n为公式个数；m为实参数组元素的个数(即表体行数)
		objValues = getFormulaParse().getValueOArray();
		errStr = getFormulaParse().getError();
		if (!StringUtil.isEmptyWithTrim(errStr))
			throw new PFBusinessException(errStr);
		// 给目的单据中的表体属性赋值
		for (int j = 0; j < alBodyFormulaName.size(); j++) {
			String bFormulaName = alBodyFormulaName.get(j);
			String destBodyField = getFieldName(bFormulaName);

			Object[] bodyValues = objValues[j];
			if (bodyValues != null && bodyValues.length > 0) {
				for (int i = 0; i < bodyVOs.length; i++)
					if (i >= bodyValues.length)
						setValueToVo(bodyValues[bodyValues.length - 1], bodyVOs[i], destBodyField,
								(Class) hmFieldToType.get(bFormulaName));
					else
						setValueToVo(bodyValues[i], bodyVOs[i], destBodyField, (Class) hmFieldToType
								.get(bFormulaName));
			} else {
				for (int i = 0; i < bodyVOs.length; i++)
					bodyVOs[i].setAttributeValue(destBodyField, null);
			}
		}
	}

	/**
	 * 获得公式解析器
	 * @return
	 */
	public abstract FormulaParseFather getFormulaParse();

	/**
	 * 获得映射类型的交换规则
	 * <li>由子类 实现
	 * @return
	 */
	public String[] getField() {
		return null;
	}

	/**
	 * 获得赋值类型的交换规则 
	 * <li>由子类 实现
	 * @return
	 * @since 5.5
	 */
	public String[] getAssign() {
		return null;
	}

	/**
	 * 获得实际属性名
	 * 
	 * @param key
	 * @return
	 */
	public static String getFieldName(String key) {
		String fieldname = null;

		if (key.startsWith("H_") || key.startsWith("B_")) {
			fieldname = key.substring(2);
		} else
			fieldname = key;

		return fieldname;
	}

	/**
	 * 获取到待转换的字段 对  
	 */
	protected void initField() {
		String tmpStr = "";
		int intIndex = 0;
		String[] field = getField();
		if (field == null)
			return;
		sFields = new String[field.length][2];
		for (int i = 0; i < field.length; i++) {
			tmpStr = field[i];
			intIndex = tmpStr.indexOf("->");
			//左值 - 目的
			sFields[i][0] = tmpStr.substring(0, intIndex);
			//右值 - 来源
			sFields[i][1] = tmpStr.substring(intIndex + 2);
		}
	}

	/**
	 * 获取到待转换的赋值 对  
	 */
	protected void initAssign() {
		String tmpStr = "";
		int intIndex = 0;
		if (getAssign() == null)
			return;
		sAssigns = new String[getAssign().length][2];
		for (int i = 0; i < getAssign().length; i++) {
			tmpStr = getAssign()[i];
			intIndex = tmpStr.indexOf("->");
			//左值 - 目的
			sAssigns[i][0] = tmpStr.substring(0, intIndex);
			//右值 - 来源
			sAssigns[i][1] = tmpStr.substring(intIndex + 2);
		}
	}

	/**
	 * 返回用户自定义函数
	 */
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}

	/**
	 * 获得公式类型的交换规则
	 * <li>由子类 实现
	 * 
	 * @return String[]
	 */
	public String[] getFormulas() {
		return null;
	}

	/**
	 * 返回另外一个交换后续处理类名
	 * <li>由子类 实现
	 * 
	 * @return String
	 */
	public String getOtherClassName() {
		return null;
	}

	/**
	 * 获得后继处理类实例
	 * @return
	 */
	protected abstract IchangeVO getAfterClass();

	/**
	 * 获得后续类 
	 * <li>由子类 实现
	 * 
	 * @return  
	 */
	public String getAfterClassName() {
		return null;
	}

	/**
	 * 为VO的某个属性赋值
	 *  
	 * @param valueObj 待赋的值
	 * @param billvo 单据主子VO
	 * @param fieldName 属性名
	 * @param fieldType 属性的数据类型
	 */
	protected void setValueToVo(Object valueObj, CircularlyAccessibleValueObject billvo,
			String fieldName, Class fieldType) {
		if (valueObj == null)
			return;

		if (fieldType == null)
			return;

		Object oTarget = valueObj;
		if (fieldType.equals(String.class)) {
			String strTarget = valueObj.toString();
			oTarget = strTarget;
		}
		if (fieldType.equals(UFDouble.class)) {
			if (valueObj instanceof UFDouble)
				oTarget = (UFDouble) valueObj;
			else {
				UFDouble dblTarget = new UFDouble(valueObj.toString());
				oTarget = dblTarget;
			}
		}
		if (fieldType.equals(Integer.class)) {
			if (valueObj instanceof Integer)
				oTarget = (Integer) valueObj;
			else {
				Integer dblTarget = new Integer(valueObj.toString());
				oTarget = dblTarget;
			}
		}
		if (fieldType.equals(UFBoolean.class)) {
			UFBoolean dblTarget = UFBoolean.valueOf(valueObj.toString());
			oTarget = dblTarget;
		}
		if (fieldType.equals(UFDate.class)) {
			if (valueObj instanceof UFDate)
				oTarget = (UFDate) valueObj;
			else {
				UFDate dblTarget = new UFDate(valueObj.toString());
				oTarget = dblTarget;
			}
		}

		//赋值到目的单据VO（主VO或子VO）中
		billvo.setAttributeValue(fieldName, oTarget);
	}

	/**
	 * 通过反射 获得VO中某字段的数据类型
	 * @param billvo
	 * @param fieldName
	 * @return
	 */
	protected Class getFieldType(CircularlyAccessibleValueObject billvo, String fieldName) {
		Class cl = null;//zhf mofify
		try {
			cl = billvo.getClass().getDeclaredField("m_" + fieldName).getType();
		} catch (java.lang.NoSuchFieldException e) {
			try {
				cl = billvo.getClass().getDeclaredField(fieldName).getType();
			} catch (java.lang.NoSuchFieldException ex) {
				try {
					cl = billvo.getClass().getField(fieldName).getType();
				} catch (java.lang.NoSuchFieldException e1) {
					//通过方法反射查找属性类型  for add mlr
					String s=fieldName.substring(1);
					String s1=fieldName.substring(0,1);
					String methodName="get"+s1.toUpperCase()+s;
					try {
						cl=billvo.getClass().getMethod(methodName).getReturnType();
					} catch (SecurityException e2) {
						cl = String.class;
						Logger.warn(ex.getMessage(), ex);
			
						
					} catch (NoSuchMethodException e2) {
						cl = String.class;
						Logger.warn(ex.getMessage(), ex);					
					}					
				}				
			} catch (Exception ex) {
				
			}
		} catch (Exception ex) {
			Logger.warn(ex.getMessage(), ex);
		}
		return cl;
	}
}
