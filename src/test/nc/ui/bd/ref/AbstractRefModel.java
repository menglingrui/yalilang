package nc.ui.bd.ref;

/**
 * ����գ��������ջ��ࡣ �������ڣ�(2001-8-23 20:26:54) ģ����δ������Ŀ
 * 
 * @author������
 *  
 */

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.jdbc.framework.misc.LRUMap;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.bd.mmpub.DataDictionaryReader;
import nc.ui.uap.bd.def.DefFieldInfo;
import nc.ui.uap.bd.def.UFRefDefTanslateUtil;
import nc.vo.bd.access.BdinfoVO;
import nc.vo.bd.ref.DocSealWherePartMng;
import nc.vo.bd.ref.IFilterStrategy;
import nc.vo.bd.ref.RefColumnDispConvertVO;
import nc.vo.bd.ref.RefQueryResultVO;
import nc.vo.bd.ref.RefQueryVO;
import nc.vo.bd.ref.RefVO_mlang;
import nc.vo.bd.ref.RefcolumnVO;
import nc.vo.bd.ref.ReftableVO;
import nc.vo.bd.refdatatemp.RefdatatempVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.logging.Debug;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sm.nodepower.OrgnizeTypeVO;
/**
 * ֧�ֹ����ظ�����
 * @author mlr
 *
 */
public abstract class AbstractRefModel implements IRefModel {

	private String para1 = null;

	private String para2 = null;

	private String para3 = null;

	protected IRefModelHandler modelHandler;

	private ArrayList hiddenFieldList = new ArrayList();

	// V55�����Ƿ��Сд����
	private boolean isCaseSensitve = false;

	//
	private Vector vecAllColumnNames = null;

	private RefQueryVO queryVO = new RefQueryVO();

	/**
	 * AbstractRefModel ������ע�⡣
	 */
	public AbstractRefModel() {
		super();
		try {
			Class c;
			// ���ط������ࡣ
			if (RuntimeEnv.getInstance().isRunningInServer())
				c = Class.forName("nc.ui.bd.ref.impl.RefModelHandlerForServer");
			// ���ؿͻ�����
			else {
				// Server server = (Server)
				// NCLocator.getInstance().lookup(Server.class.getName());
				// Container container = server.getContainer("uap");
				// if(container == null)
				// throw new ServletException("�޷��ҵ�ģ��uap");
				// ClassLoader cl = container.getClassLoader();
				c = Class.forName("nc.ui.bd.ref.RefModelHandlerForClient");
			}

			Constructor cs = c
					.getConstructor(new Class[] { AbstractRefModel.class });
			modelHandler = (IRefModelHandler) cs.newInstance(this);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private String[] m_strFieldCode = null; // ҵ�����ֶ���

	private String[] m_strFieldName = null; // ҵ�����ֶ�������

	// ҵ����ֶ�����
	private int[] m_intFieldType = null;

	private String m_strPkFieldCode = null;

	private String m_strTableName = null; // ҵ�����

	private java.lang.String m_strBlurValue = null;

	private String m_strRefTitle = null;

	protected Vector m_vecData = null;

	private int m_iDefaultFieldCount = 2;

	private String m_strOrderPart = null;

	protected java.lang.String m_strPk_corp = null;

	protected String m_strRefCodeField = null;

	protected String m_strRefNameField = null;

	protected Vector m_vecSelectedData = null;

	private String m_strStrPatch = "";

	private String m_strWherePart = null;

	private boolean m_bCacheEnabled = true;

	private Hashtable m_htCodeIndex = null;

	private String m_orgTypeCode = OrgnizeTypeVO.COMPANY_TYPE;

	// ��ǰ�������ڽ���Ĺ��ܽڵ��
	private String m_fun_code = null;

	// �������Ӱ�ť�򿪽ڵ�Ĺ��ܽڵ����
	private String m_fun_code_DocOpen = null;

	// �����˲�����
	private String m_pk_glOrgBook = null;

	// ���Ӵ���ʱ��ȡ���ķ�ʽ 2004-06-10
	private boolean isFromTempTable = false;

	// �Ƿ����ģ��ƥ��Sql
	private boolean isIncludeBlurPart = true;

	// sxj ����ֵ�Ƿ�����¼��ڵ�
	private boolean isIncludeSub = false;

	private boolean isPKMatch = false;

	// ��������Ͳ��տ��Դ�һ�����Ͳ��գ�����ƥ��������ȡ�
	private AbstractRefModel m_defGridmodel = null;

	// sxj 2003-10-31
	// ƥ������ʱ�����ƥ�䲻�ϣ��Ƿ�ˢ��������ƥ��һ��
	private boolean m_isRefreshMatch = true;

	// sxj 2004-03-17
	// �Ƿ���ʾ�������
	private boolean m_isSealedDataShow = false;

	// �����û������Զ���setPkƥ����С� 2004-09-23
	private String m_matchField = null;

	// ����ƥ��
	private HashMap m_matchHM = new HashMap();

	// ֧��������2003-02-19
	// ֧���������� 2004-01-12
	private String[] m_mnecodes = null;

	// sxj 2003-11-14
	private String querySql = null;

	// �߼���������
	private String refFilterDlgClaseName = null;

	private String refQueryDlgClaseName = null;

	// sxj
	private Object userPara = null;

	protected static final String LARGE_NODE_NAME = "���̻�������:���̵���:�ͻ�����:��Ӧ�̵���:���̸�������:�ͻ���������:��Ӧ�̸�������:���̵�����������:�ͻ�������������:��Ӧ�̵�����������:�����������:�������:�����������:���ϵ���";

	// ����setpkƥ������ʱ���Ƿ��������WherePart�Ŀ���
	private boolean isMatchPkWithWherePart = false;

	// ���յı༭״̬,��״̬�ͽ���Ŀɱ༭��һ��
	private boolean m_isRefEnable = true;

	// ���նԹ�ʽ������֧��
	// ��ʽ���� formula[0][0] ="�ֶ�";formula[0][1]="��ʽ����"
	private String[][] formulas = null;

	// ������ղ�ѯ�Ƿ�����(���Ǹ߼���ѯ)
	boolean isLocQueryEnable = true;

	// �Ƿ�����ѡ���ĩ��
	private boolean m_bNotLeafSelectedEnabled = true;

	// isUseDataPower 2002-09-09
	private boolean m_bUserDataPower = true;

	// ��̬�нӿ�����
	private String m_dynamicColClassName = null;

	// sxj 2003-04-10
	// �ṩ��������Pks���й���

	private int m_filterStrategy = IFilterStrategy.INSECTION;

	private HashMap m_hmFilterPks = new HashMap();

	private String[] m_filterPks = null;

	// htDispConvert2002-08-30
	private Hashtable m_htDispConvertor = null;

	// sxj 2003-10-28 ��̬�����ÿ���
	private boolean m_isDynamicCol = false;

	private int[] m_iShownColumns = null;

	// ֧�������������¼��ƥ��2003-02-21
	private boolean m_mnecodeInput = false;

	private String[] m_strBlurFields = null;

	private java.lang.String m_strDataSource = null;

	// ��̬������
	private String[] m_strDynamicFieldNames = null;

	private java.lang.String m_strGroupPart = null;

	private String[] m_strHiddenFieldCode = null;

	private java.lang.String m_strOriginWherePart;

	// ��������ID(����������)
	protected String m_strRefNodeName = null;

	// �û����ö�̬�нӿ�ʱ��Ҫʹ�õ��Ĳ�����
	private Object m_userParameter = null;

	//
	// private Vector m_vecUnMatchData = null;

	private String refTempDataWherePart = null;

	// ģ��ƥ��ʱ��Ĭ�ϰ�code+�������У����飩��Ϊƥ����С��ò��������Ƿ�ֻ������������ƥ��
	private boolean isOnlyMneFields = false;

	// ������wherePart
	private String m_addWherePart = null;

	// ������������ʾת��
	private Object[][] m_columnDispConverter = null;

	// �û�
	private String m_pk_user = null;

	FormulaHandler formulaHandler = null;

	DefFieldsHandler defFieldHandler = null;

	private EventListenerList eListenerList = new EventListenerList();

	private Map eventListMap = new HashMap();

	private transient ChangeEvent changeEvent = null;

	// �Ƿ�ֻ��ʾ��������
	private boolean isGroupAssignData = false;

	// �������ݻ���Keyǰ׺�ַ���
	private String refDataCachekeyPreStr = "REFDATACACHE";

	private DDReaderVO ddReaderVO = null;

	// �����Զ����������Ȩ�޵�֧��.����Ȩ�����Ȱ���ԴID��ѯ��
	private int resourceID = -1;

	private String sealedWherePart = null;

	// ����������ݻ��������仯��ͬʱ��setpkʱ����������
	private String envWherePart = null;

	private BdinfoVO bdinfoVO = null;

	// �Ƿ��ѳ�ʼ��BDInfoVO
	private boolean isInitBDinfoVO = false;

	// �Ƿ�Ҫ�ı�������˳��Ϊ���棩
	private boolean isChangeTableSeq = true;

	// ά����ť�Ƿ����
	private boolean isMaintainButtonEnabled = true;

	// ����Ȩ����Դ�ֶ�
	private String dataPowerResourceColumn = null;

	// ��¼��ϸ��Ϣ��ʾ���
	private String refCardInfoComponentImplClass = null;

	// ������ʾ�ֶ�
	private String refShowNameField = null;

	// �Զ������ֶ�,�����Ǳ���.�ֶ���
	private String[] defFields = null;

	// ���뷽ʽMap
	private Map<String, Integer> alignMap = null;

	// ��������Զ����������,��ʵ�������̳�JComponent���Ұ���AbstractRefMOdel�Ĺ�����
	private String uiControlComponentClassName = null;

	// ����ά���ӿ��࣬since v5.5 ֧���Զ��嵵��ά������
	private String refDocEditClassName = null;

	private class DDReaderVO {
		String tableName = null;

		String[] fieldNames = null;

		/**
		 * @return ���� fieldNames��
		 */
		public String[] getFieldNames() {
			return fieldNames;
		}

		/**
		 * @param fieldNames
		 *            Ҫ���õ� fieldNames��
		 */
		public void setFieldNames(String[] fieldNames) {
			this.fieldNames = fieldNames;
		}

		/**
		 * @return ���� tableName��
		 */
		public String getTableName() {
			return tableName;
		}

		/**
		 * @param tableName
		 *            Ҫ���õ� tableName��
		 */
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
	}

	/**
	 * @return ���� m_columnDispConverterClass��
	 */
	public Object[][] getColumnDispConverter() {
		return m_columnDispConverter;
	}

	/**
	 * @param dispConverterClass
	 *            Ҫ���õ� m_columnDispConverterClass��
	 */
	public void setColumnDispConverter(Object[][] dispConverter) {
		m_columnDispConverter = dispConverter;
	}

	/**
	 * ����where�Ӿ� �������ڣ�(2001-8-16 12:42:02)
	 * 
	 * @param newWherePart
	 *            java.lang.String
	 */
	public void addWherePart(String newWherePart) {
		// ��ԭʼWhere����
		// m_strWherePart = getOriginWherePart();
		// if (m_strWherePart == null || m_strWherePart.trim().length() == 0) {
		// m_strWherePart = " ( 11 = 11 )";
		// }
		// if (newWherePart != null && newWherePart.trim().length() > 0) {
		// m_strWherePart = "( " + m_strWherePart + " )";
		// m_strWherePart += " " + newWherePart + " ";
		// }

		// setAddWherePart(newWherePart);

		m_addWherePart = newWherePart;

		// clearData();
		resetSelectedData_WhenDataChanged();
	}

	/**
	 * �������ڣ�(2004-9-23 9:03:51)
	 * 
	 * @param newM_matchField
	 *            java.lang.String �Զ�����С���SetPk�������ֵ�ڸ�����ƥ�䡣 ������ʹ�á�
	 */
	public void setMatchField(java.lang.String newM_matchField) {
		m_matchField = newM_matchField;
	}

	/**
	 * �˴����뷽��˵����
	 * 
	 * /** ������ݼ����档 �������ڣ�(2001-8-23 21:01:00)
	 */
	public void clearData() {

		clearModelData();
		clearCacheData();

	}

	/**
	 * ɾ�����ջ�������
	 */
	public void clearCacheData() {
		modelHandler.clearCacheData();
	}

	protected void clearModelData() {
		m_vecData = null;
		setSelectedData(null);

	}

	protected void clearDataPowerCache() {
		modelHandler.clearDataPowerCache();
	}

	/**
	 * ��һ������ת��ΪVO,�粻ʹ��VO������ʵ�֡� �������ڣ�(2001-8-13 16:34:11)
	 * 
	 * @return nc.vo.pub.ValueObject
	 * @param vData
	 *            java.util.Vector
	 */
	public nc.vo.pub.ValueObject convertToVO(java.util.Vector vData) {
		return null;
	}

	/**
	 * ����������ת��ΪVO���顣 �������ڣ�(2001-8-13 16:34:11)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 * @param vData
	 *            java.util.Vector
	 */
	public nc.vo.pub.ValueObject[] convertToVOs(java.util.Vector vData) {
		if (vData == null || vData.size() == 0)
			return null;
		nc.vo.pub.ValueObject[] vos = new nc.vo.pub.ValueObject[vData.size()];
		for (int i = 0; i < vData.size(); i++) {
			Vector vRecord = (Vector) vData.elementAt(i);
			vos[i] = convertToVO(vRecord);
		}
		return vos;
	}

	/**
	 * ģ���ֶ�ֵ�� �������ڣ�(2001-8-17 11:17:42)
	 * 
	 * @return java.lang.String
	 */
	public String getBlurValue() {
		return m_strBlurValue;
	}

	/**
	 * ��ȡ��������ݿ���еĲ������ݣ�����άVector��
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-7-12</strong>
	 * <p>
	 * 
	 * @param
	 * @return Vector
	 * @exception BusinessException
	 * @since NC5.0
	 */
	public final Vector getRefData() {
		return modelHandler.getRefData();
	}

	/**
	 * ��ȡ�������ݣ�����άVector�� �Զ�����տ��Ը���
	 * 
	 * @return java.util.Vector
	 */

	public java.util.Vector getData() {

		String sql = getRefSql();

		Vector v = null;
		if (isCacheEnabled()) {
			/** �ӻ�������� */
			v = modelHandler.getFromCache(getRefDataCacheKey(),
					getRefCacheSqlKey());
		}

		if (v == null) {

			// �����ݿ��--Ҳ�����ڴ˶�������

			try {

				if (isFromTempTable()) {
					v = modelHandler.queryRefDataFromTemplateTable(sql);
				} else {
					// ͬʱ��ȡ������Ŀ����
					v = getQueryResultVO();
				}

			} catch (Exception e) {
				Debug.debug(e.getMessage(), e);
			}

		}

		putToCache(v);

		m_vecData = v;
		return m_vecData;
	}

	public void putToCache(Vector v) {
		if (v != null && isCacheEnabled()) {
			/** ���뵽������ */
			modelHandler.putToCache(getRefDataCacheKey(), getRefCacheSqlKey(),
					v);
		}
	}

	public Vector getConvertedData1(boolean isDataFromCache, Vector v,
			boolean isDefConverted) {
		return getConvertedData(isDataFromCache, v, isDefConverted);
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-7-12</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	protected Vector getConvertedData(boolean isDataFromCache, Vector v,
			boolean isDefConverted) {
		if (v == null) {
			return v;
		}
		Vector vecData = v;

		// ������Э�̵Ľ��,������Pks����
		if (vecData != null && vecData.size() > 0) {
			vecData = getFilterPKsVector(vecData, getFilterStrategy());

		}
		// �����Դ���
		// �����е������Ѿ��������.
		if (!isDataFromCache) {
			setMlangValues(vecData, getRefVO_mlang());
			if (getFormulas() != null) {
				getFormulaHandler().setFormulaValues(vecData, getFormulas());
			}
			// ͳ�����Զ�����pk-->name
			if (isDefConverted) {
				getDefFieldHandler().setDefFieldValues(vecData);
			}

		}
		// ���⴦�����
		// try to convert
		handleDispConvertor(vecData);
		// end converts
		// ����������ʾ�С�
		if (getColumnDispConverter() != null) {
			setColDispValues(vecData, getColumnDispConverter());
		}
		return vecData;
	}

	/**
	 * ö��������ʾֵ��ת��
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-3-9</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception
	 * @since NC5.0
	 */
	private void handleDispConvertor(Vector v) {
		if (getDispConvertor() != null) {
			Hashtable convert = getDispConvertor();
			Enumeration e = convert.keys();
			while (e.hasMoreElements()) {
				String convertColumn = (String) e.nextElement();
				Hashtable conv = (Hashtable) convert.get(convertColumn);
				Integer Idx = (Integer) getHtCodeIndex().get(convertColumn);
				if (Idx == null || Idx.intValue() < 0)
					continue;
				int idx = Idx.intValue();
				if (v != null) {
					int rows = v.size();
					for (int i = 0; i < rows; i++) {
						Vector vRow = (Vector) v.elementAt(i);
						if (vRow != null) {
							Object oldValue = vRow.elementAt(idx);
							if (oldValue == null)
								continue;
							Object newValue = conv.get(oldValue.toString());
							if (newValue != null) {
								vRow.setElementAt(newValue, idx);
								// ������󣬿���ת��Ϊԭֵ��Ŀǰ�ȱ��ַ�����ֵ
								// Object obj =
								// getRefValueVO(vRow.elementAt(idx),
								// newValue);
								// vRow.setElementAt(obj, idx);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
	 */
	public int getDefaultFieldCount() {
		return m_iDefaultFieldCount;
	}

	/**
	 * �õ�һ���ֶ��������ֶ��е��±ꡣ �������ڣ�(2001-8-16 15:39:23)
	 * 
	 * @return int
	 * @param fieldList
	 *            java.lang.String[]
	 * @param field
	 *            java.lang.String
	 */
	public int getFieldIndex(String field) {

		if (field == null || field.trim().length() == 0
				|| getHtCodeIndex() == null || getHtCodeIndex().size() == 0)
			return -1;
		Object o = getHtCodeIndex().get(field.trim());
		if (o == null) {
			// ���붯̬��
			int index = m_htCodeIndex.size();
			if (isDynamicCol() && getDynamicFieldNames() != null) {
				for (int i = 0; i < getDynamicFieldNames().length; i++) {
					m_htCodeIndex.put(getDynamicFieldNames()[i].trim(),
							new Integer(index + i));
				}
			}
			o = getHtCodeIndex().get(field.trim());
		}

		return (o == null) ? -1 : ((Integer) o).intValue();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-6-17 18:35:14)
	 * 
	 * @return java.util.Hashtable
	 */
	public Hashtable getHtCodeIndex() {
		if (m_htCodeIndex == null || m_htCodeIndex.size() == 0) {
			m_htCodeIndex = new Hashtable();
			if (getFieldCode() != null)
				for (int i = 0; i < getFieldCode().length; i++) {
					m_htCodeIndex.put(getFieldCode()[i].trim(), new Integer(i));
				}

			if (getHiddenFieldCode() != null) {
				int index = 0;
				if (getFieldCode() != null) {
					index = getFieldCode().length;
				}
				for (int i = 0; i < getHiddenFieldCode().length; i++) {
					if (getHiddenFieldCode()[i] != null) {
						m_htCodeIndex.put(getHiddenFieldCode()[i].trim(),
								new Integer(index + i));
					} else {
						System.out
								.println("Waring: The RefModel has some errors.");
					}
				}
			}

		}
		return m_htCodeIndex;
	}

	/**
	 * Order�Ӿ䡣
	 * 
	 * @return java.lang.String
	 */
	public String getOrderPart() {
		if (m_strOrderPart == null && getFieldCode() != null
				&& getFieldCode().length > 0)
			m_strOrderPart = getFieldCode()[0];
		return m_strOrderPart;
	}

	/**
	 * �õ���˾��������Ĭ�ϲ���ʹ�á� �������ڣ�(2001-8-17 11:17:03)
	 * 
	 * @return java.lang.String
	 */
	public String getPk_corp() {
		if (m_strPk_corp == null) {
			m_strPk_corp = modelHandler.getPk_corp();
		}
		return m_strPk_corp;
	}

	/**
	 * ����ֵ���������ֶ�
	 * 
	 * @return java.lang.String
	 */
	public String getPkValue() {
		String strValue = null;
		Object objValue = getValue(getPkFieldCode());
		if (objValue != null) {
			strValue = objValue.toString();
		}

		return strValue;
	}

	/**
	 * ����ֵ���飭�������ֶ�
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getPkValues() {

		Object[] oDatas = getValues(getPkFieldCode());
		String[] sDatas = objs2Strs(oDatas);
		return sDatas;
	}

	/**
	 * ���ձ����ֶΡ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.String
	 */
	public String getRefCodeField() {
		if (m_strRefCodeField == null && getFieldCode() != null
				&& getFieldCode().length > 0)
			m_strRefCodeField = getFieldCode()[0];
		return m_strRefCodeField;
	}

	/**
	 * ����ֵ���������ֶ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.String
	 */
	public String getRefCodeValue() {

		String strValue = null;
		String[] strValues = getRefCodeValues();
		if (strValues != null && strValues.length > 0) {
			strValue = strValues[0];
		}
		return strValue;
	}

	/**
	 * ����ֵ���飭�������ֶ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getRefCodeValues() {
		Object[] oDatas = getValues(getRefCodeField());
		String[] sDatas = objs2Strs(oDatas);
		return sDatas;
	}

	protected String[] objs2Strs(Object[] oDatas) {
		if (oDatas == null)
			return null;
		String[] sDatas = new String[oDatas.length];
		for (int i = 0; i < oDatas.length; i++) {
			if (oDatas[i] == null)
				sDatas[i] = null;
			else
				sDatas[i] = oDatas[i].toString().trim();
		}
		return sDatas;
	}

	/**
	 * ���������ֶΡ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.String
	 */
	public String getRefNameField() {
		if (m_strRefNameField == null && getFieldCode() != null
				&& getFieldCode().length > 1)
			m_strRefNameField = getFieldCode()[1];
		return m_strRefNameField;
	}

	/**
	 * ����ֵ�������ֶ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRefNameValue() {

		String strValue = null;
		String[] strValues = getRefNameValues();
		if (strValues != null && strValues.length > 0) {
			strValue = strValues[0];
		}
		return strValue;
	}

	/**
	 * ����ֵ�������ֶ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getRefNameValues() {
		// �����У�ȡ������ֵ
		Object[] oDatas = getValues(getRefNameField(), getSelectedData(), false);

		String[] sDatas = objs2Strs(oDatas);

		return sDatas;
	}

	public java.lang.String[] getRefShowNameValues() {
		// �����У�ȡ������ֵ
		Object[] oDatas = getValues(getRefShowNameField(), getSelectedData(),
				false);

		return objs2Strs(oDatas);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-15 17:30:17)
	 * 
	 * @return java.lang.String
	 */
	public String getRefSql() {

		return getSql(getStrPatch(), getFieldCode(), getHiddenFieldCode(),
				getTableName(), getWherePart(), getGroupPart(), getOrderPart());

	}

	/**
	 * ����ѡ�����ݣ�����ά���顣 �������ڣ�(2001-8-23 19:10:29)
	 * 
	 * @return java.util.Vector
	 */
	public java.util.Vector getSelectedData() {
		return m_vecSelectedData;
	}

	/**
	 * DISTINCT�Ӿ�
	 * 
	 * @return java.lang.String
	 */
	public String getStrPatch() {
		return m_strStrPatch;
	}

	/**
	 * ����ֵ�����ݲ����ֶ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.Object
	 */
	public Object getValue(String field) {
		Object[] values = getValues(field);

		Object value = null;

		if (values != null && values.length > 0) {
			value = values[0];
		}

		return value;
	}

	/**
	 * ����ֵ���飭���ݲ����ֶ� �������ڣ�(2001-8-13 16:19:24)
	 * 
	 * @return java.lang.Object[]
	 */
	public java.lang.Object[] getValues(String field) {

		return getValues(field, true);
	}

	/**
	 * ȡ����ѡ�м�¼�����е�ֵ
	 * 
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-11-16</strong>
	 * <p>
	 * 
	 * @param field
	 *            ��
	 * @param �Ƿ���ԭֵ���п����ѱ����루���綨���˹�ʽ�������˶��﷭�룬������ö�����͵Ķ�Ӧֵ��
	 * 
	 * @return java.lang.Object[]
	 * @exception BusinessException
	 * @since NC5.0
	 */
	public java.lang.Object[] getValues(String field, boolean isOriginValue) {

		return getValues(field, getSelectedData(), isOriginValue);
	}

	/**
	 * 
	 */
	public java.lang.Object[] getValues(String field, Vector datas) {

		return getValues(field, datas, true);
	}

	protected java.lang.Object[] getValues(String field, Vector datas,
			boolean isOriginValue) {
		int col = getFieldIndex(field);
		if (datas == null || datas.size() == 0)
			return null;
		if (col < 0) {
			throw new RuntimeException("������û�и�����");
		}
		Vector vRecord = null;
		Object[] oData = new Object[datas.size()];
		for (int i = 0; i < datas.size(); i++) {
			vRecord = (Vector) datas.elementAt(i);
			if (vRecord == null || vRecord.size() == 0 || col >= vRecord.size())
				oData[i] = null;
			else {
				oData[i] = getValueOfRefvalueVO(vRecord.elementAt(col),
						isOriginValue);
			}
		}
		return oData;
	}

	private Object getValueOfRefvalueVO(Object obj, boolean isOriginValue) {
		Object value = obj;
		if (obj instanceof RefValueVO && isOriginValue) {
			value = ((RefValueVO) obj).getOriginValue();
		}
		/**
		 * V55 ���ݿ��в����ԭʼ����ΪBigDecimal��ΪNC�����㣬���ն���ͳһת��ΪUFDouble
		 * 
		 */

		value = RefPubUtil.bigDecimal2UFDouble(value);

		return value;

	}

	/**
	 * ��ȡ�Ѿ������Ĳ������ݣ�����άVector�� �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector
	 */
	public java.util.Vector getVecData() {
		return m_vecData;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-25 12:06:11)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public nc.vo.pub.ValueObject getVO() {
		if (getSelectedData() != null && getSelectedData().size() > 0)
			return convertToVO(((Vector) getSelectedData().elementAt(0)));
		else
			return null;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-25 12:06:11)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public nc.vo.pub.ValueObject[] getVOs() {
		return convertToVOs(getSelectedData());
	}

	/**
	 * getWherePart ����ע�⡣
	 */
	public String getWherePart() {
		return m_strWherePart;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-23 21:03:06)
	 * 
	 * @return boolean
	 */
	public boolean isCacheEnabled() {
		return m_bCacheEnabled;
	}

	/**
	 * ƥ������������ݣ���ѡ������Ҳ�ı��ˡ� �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector
	 */
	public Vector matchBlurData(String strBlurValue) {
		Vector v = null;
		if (strBlurValue != null && strBlurValue.trim().length() > 0) {
			v = matchData(getBlurFields(), new String[] { strBlurValue });
			// �����ģ����ѯ�Ľ�������Ǳ���ȶ���ƥ�䣬����Ҷ�ӽڵ��Ƿ����ѡ��
			// �������β���ͨ��ģ��ƥ���ɱ��ͣ����˵���Ҷ�ӽڵ�

			if (!isNotLeafSelectedEnabled()
					&& (!isNull(strBlurValue) || isMnecodeInput())) {

				v = filterNotLeafNode(v);
			}

		}
		// �����ѡ������
		setSelectedData(v);
		return v;

	}

	private boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * ��ȡƥ������ ƥ��ɹ� ���ݣ�>m_vecSelectedData ƥ�䲻�ɹ� null��>m_vecSelectedData
	 * �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector sxj �޸����value= null or value.length()=0
	 *         ����ƥ��,���Ч�ʣ���ֹ�ظ�reloadData()
	 */
	public Vector matchData(String field, String value) {
		return matchData(new String[] { field }, new String[] { value });
	}

	/**
	 * ƥ���������ݣ���ѡ������Ҳ�ı��ˡ� �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector sxj 2004-06-08 �������Pk ����ƥ�䣬��������ڽ���ƥ�䡣 ����
	 *         getMatchField()�ṩ���н���ƥ��
	 */
	public Vector matchPkData(String strPkValue) {

		if (strPkValue == null || strPkValue.trim().length() == 0) {
			setSelectedData(null);
			return null;

		}
		return matchPkData(new String[] { strPkValue });
	}

	/**
	 * �����������ݡ� 1.ʹ�û����ֶ�������ˢ�°�ť�� �������ڣ�(2001-8-23 21:14:19)
	 * 
	 * @return java.util.Vector
	 */
	public java.util.Vector reloadData() {

		reloadData1();

		return getRefData();
	}

	// ��������ǰ�Ĵ���
	public void reloadData1() {
		// ˢ��ǰ��ѡ�������
		String[] selectedPKs = getPkValues();

		clearData();
		// ������Ȩ�޻���
		clearDataPowerCache();

		modelHandler.getLRUMap().clear();

		// ˢ�º�����ƥ�����ݡ�
		if (selectedPKs != null && selectedPKs.length > 0) {
			matchPkData(selectedPKs);
		}
		modelHandler.fireDBCache();
	}

	/**
	 * ����ģ��ֵ�� �������ڣ�(2001-8-17 12:57:37)
	 */
	public void setBlurValue(String strBlurValue) {
		m_strBlurValue = strBlurValue;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-23 21:03:06)
	 */
	public void setCacheEnabled(boolean cacheEnabled) {
		m_bCacheEnabled = cacheEnabled;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-24 10:32:33)
	 * 
	 * @param vData
	 *            java.util.Vector
	 */
	public void setData(Vector vData) {
		m_vecData = vData;
		// Ĭ�ϲ���֧���Զ�������
		if (isCacheEnabled()) {
			String sql = getRefSql();
			modelHandler.putToCache(getRefDataCacheKey(), sql, vData);
		}
	}

	/**
	 * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
	 */
	public void setDefaultFieldCount(int iDefaultFieldCount) {
		m_iDefaultFieldCount = iDefaultFieldCount;
	}

	/**
	 * Order�Ӿ䡣
	 */
	public void setOrderPart(String strOrderPart) {
		m_strOrderPart = strOrderPart;
	}

	/**
	 * ���ù�˾��������Ĭ�ϲ���ʹ�á� �Զ�����ղ�֧�ֶ�̬�ı价��������̬�ı�WherePart
	 * 
	 * @return java.lang.String
	 */
	public void setPk_corp(String strPk_corp) {
		if (strPk_corp == null) {
			return;
		}
		// �л���˾�������˲�����Ҳ��ı䣨���ڻ�ƿ�Ŀ���յ������˲���
		m_pk_glOrgBook = null;
		// �Զ�����գ����ַ����滻�㷨����̬�ı�Wherepart,����ϵͳĬ�ϲ���
		setPk_corpForDefRef(strPk_corp, m_strPk_corp);
		m_strPk_corp = strPk_corp;
		// ϵͳĬ�ϲ���
		// ����WherePart,Ҳ������������ϢҲҪ�ı䣬
		resetWherePart();
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-6-16</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private void resetWherePart() {
		setRefNodeName(getRefNodeName());
		// ������ҵ������ӵ�WherePart
		String wherePart = getWherePart();
		// ���WherePart�в�����ҵ������ӵ�WherePart���ٸ������
		if (getAddWherePart() != null && wherePart != null
				&& wherePart.indexOf(getAddWherePart()) < 0) {
			addWherePart(getAddWherePart());
		}
	}

	/**
	 * ���ù�˾����ͬʱ���ò���Ĭ����������
	 * 
	 * @deprecated
	 */
	public void setPk_corpAndRefreshRefNodeName(String strPk_corp) {
		setPk_corp(strPk_corp);

	}

	/*
	 * �Զ�����ն�̬�ı乫˾������Ҫ��������WherePart
	 */
	private void setPk_corpForDefRef(String pk_corp, String pk_corpOld) {

		if (pk_corpOld != null && pk_corp != null
				&& (!pk_corpOld.equals(pk_corp))) {
			String wherePartOld = getWherePart();
			String wherePart = "";
			if (wherePartOld != null) {
				int index = wherePartOld.indexOf("'" + pk_corpOld + "'");
				if (index > 0) {
					wherePart = wherePartOld.replaceFirst("'" + pk_corpOld
							+ "'", "'" + pk_corp + "'");
					setWherePart(wherePart);
				}
			}
		}
	}

	/**
	 * ���ò��ձ����ֶΡ� �������ڣ�(2001-8-13 16:19:24)
	 */
	public void setRefCodeField(String strRefCodeField) {
		m_strRefCodeField = strRefCodeField;
	}

	/**
	 * ���ò��������ֶΡ� �������ڣ�(2001-8-13 16:19:24)
	 */
	public void setRefNameField(String strRefNameField) {
		m_strRefNameField = strRefNameField;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-25 18:58:00)
	 * 
	 * @param vecSelectedData
	 *            java.util.Vector
	 */
	public void setSelectedData(Vector vecSelectedData) {
		m_vecSelectedData = vecSelectedData;
	}

	public void setSelectedDataAndConvertData(Vector vecSelectedData) {
		if (vecSelectedData == null) {
			setSelectedData(vecSelectedData);
			return;
		}
		Vector matchVec = getConvertedData(false, vecSelectedData, false);
		setSelectedData(matchVec);
		putLruMap(matchVec);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-6-19 21:15:22)
	 * 
	 * @param newStrPatch
	 *            java.lang.String
	 */
	public void setStrPatch(java.lang.String newStrPatch) {
		m_strStrPatch = newStrPatch;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-16 12:42:02)
	 * 
	 * @param newWherePart
	 *            java.lang.String
	 */
	public void setWherePart(String newWherePart) {

		if (newWherePart != null) {
			newWherePart = newWherePart.trim();
			int index = newWherePart.toLowerCase().indexOf("where ");
			if (index == 0)
				newWherePart = newWherePart.substring(index + 5, newWherePart
						.length());
		}
		m_strWherePart = newWherePart;
		// ͬʱ�޸�ԭʼwhere
		m_strOriginWherePart = m_strWherePart;

		resetSelectedData_WhenDataChanged();

	}

	/**
	 * ��Where �����ı�����m_vecData������ƥ���Ѿ����õ�pks.
	 */
	private void resetSelectedData_WhenDataChanged() {

		String[] selectedPKs = getPkValues();
		// �����ݡ�
		clearModelData();

		if (selectedPKs != null && selectedPKs.length > 0) {
			matchPkData(selectedPKs);
		}

		Vector selectedData = getSelectedData();

		setSelectedData(selectedData);
		// ֪ͨUIRefPane,ˢ�½���
		fireChange();
	}

	/**
	 * java.util.Vector ���˷�ĩ���ڵ�
	 */
	protected Vector filterNotLeafNode(Vector vec) {
		return vec;
	}

	/**
	 * ģ���ֶ����� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String[] getBlurFields() {

		if (m_strBlurFields == null || m_strBlurFields.length == 0) {

			if (isOnlyMneFields()) {
				return m_strBlurFields = getMnecodes();
			} else if (getFieldCode() != null) {
				ArrayList al = new ArrayList();
				int len = getFieldCode().length;

				al.add(getFieldCode()[0]);
				if (len >= 2) {
					al.add(getFieldCode()[1]);
				}

				if (getMnecodes() != null) {
					for (int i = 0; i < getMnecodes().length; i++) {
						if (al.contains(getMnecodes()[i])) {
							continue;
						}
						al.add(getMnecodes()[i]);
					}
				}
				m_strBlurFields = new String[al.size()];
				al.toArray(m_strBlurFields);

			}

		}

		return m_strBlurFields;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-9 8:54:00)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDataSource() {
		return m_strDataSource;
	}

	/**
	 * ���ڲ�������ת����Ӱ��� ��attrib1����1���Ϻ� 2���Ͼ�3������ Hashtable conv=new Hashtable();
	 * Hashtable contents=new Hashtable(); contents.put("1","�Ϻ�");
	 * contents.put("2","�Ͼ�"); contents.put("3","����");
	 * conv.put("attrib1",contents); return conv; ͯ־��2002-08-30
	 */
	public java.util.Hashtable getDispConvertor() {
		if (getDdReaderVO() != null && m_htDispConvertor == null) {
			m_htDispConvertor = getDDReaderMap(getDdReaderVO().getTableName(),
					getDdReaderVO().getFieldNames());
		}
		return m_htDispConvertor;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 19:40:50)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDynamicColClassName() {
		return m_dynamicColClassName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-30 8:34:51)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getDynamicFieldNames() {
		return m_strDynamicFieldNames;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-4-3 20:08:40)
	 * 
	 * @return java.util.Hashtable
	 * @param tableName
	 *            java.lang.String
	 */
	public Map getFieldCNName() {
		return modelHandler.getFieldCNName();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-4-10 18:42:56) ȡ���˺�����ݼ�. vecData ��ǰ�����
	 */
	public Vector getFilterPKsVector(Vector vecData, int filterStrategy) {
		Vector filterVector = null;
		int pkIndex = getFieldIndex(getPkFieldCode());
		if (vecData != null) {
			// û�й��˵�PKs
			if (getFilterPks() == null) {
				return vecData;
			}
			filterVector = new Vector();
			for (int i = 0; i < vecData.size(); i++) {
				Object vecpk = ((Vector) vecData.elementAt(i))
						.elementAt(pkIndex);

				switch (filterStrategy) {

				case IFilterStrategy.INSECTION: {
					if (m_hmFilterPks.size() == 0) {
						return null;
					}
					if (m_hmFilterPks.get(vecpk.toString()) != null) {
						filterVector.add(vecData.elementAt(i));
					}
					break;
				}

				case IFilterStrategy.REFDATACOLLECT_MINUS_INSECTION: {
					if (m_hmFilterPks.size() == 0) {
						return vecData;
					}
					if (m_hmFilterPks.get(vecpk.toString()) == null) {
						filterVector.add(vecData.elementAt(i));
					}
					break;
				}
				default:

				}
			}
		}
		return filterVector;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-8 13:31:41)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getGroupPart() {
		return m_strGroupPart;
	}

	/**
	 * ����ʾ�ֶ��б�
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getHiddenFieldCode() {
		return m_strHiddenFieldCode;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-9 9:38:40)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOriginWherePart() {
		return m_strOriginWherePart;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-8 10:36:45)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRefNodeName() {
		return m_strRefNodeName == null ? getClass().getName()
				: m_strRefNodeName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-29 10:08:50)
	 * 
	 * @return int[]
	 */
	public int[] getShownColumns() {
		if ((m_iShownColumns == null || m_iShownColumns.length == 0)
				&& getDefaultFieldCount() > 0) {
			m_iShownColumns = new int[getDefaultFieldCount()];
			for (int i = 0; i < getDefaultFieldCount(); i++) {
				m_iShownColumns[i] = i;
			}
		}
		return m_iShownColumns;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(02-7-4 18:40:10)
	 * 
	 * @return java.util.Vector
	 */
	// public java.util.Vector getUnMatchData() {
	// if (m_vecUnMatchData == null)
	// m_vecUnMatchData = new Vector();
	// return m_vecUnMatchData;
	// }
	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 19:54:29)
	 * 
	 * @return java.lang.Object
	 */
	public java.lang.Object getUserParameter() {
		return m_userParameter;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 19:38:34)
	 * 
	 * @return boolean
	 */
	public boolean isDynamicCol() {
		return m_isDynamicCol;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-2-21 16:27:33)
	 * 
	 * @return boolean
	 */
	public boolean isMnecodeInput() {
		return m_mnecodeInput;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-4-10 20:39:56)
	 * 
	 * @return boolean
	 */
	public boolean isNotLeafSelectedEnabled() {
		return m_bNotLeafSelectedEnabled;
	}

	/**
	 * �Ƿ�����ʹ������Ȩ��
	 * 
	 * @return boolean
	 */
	public boolean isUseDataPower() {
		return m_bUserDataPower;
	}

	/**
	 * ����ģ���ֶΡ� �������ڣ�(2001-8-17 12:57:37)
	 */
	public void setBlurFields(String[] strBlurFields) {
		m_strBlurFields = strBlurFields;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-9 8:54:00)
	 * 
	 * @param newDataSource
	 *            java.lang.String
	 */
	public void setDataSource(java.lang.String newDataSource) {
		m_strDataSource = newDataSource;
	}

	/**
	 * ���ڲ�������ת����Ӱ��� ��attrib1����1���Ϻ� 2���Ͼ�3������ Hashtable conv=new Hashtable();
	 * Hashtable contents=new Hashtable(); contents.put("1","�Ϻ�");
	 * contents.put("2","�Ͼ�"); contents.put("3","����");
	 * conv.put("attrib1",contents); return conv; ͯ־��2002-08-30
	 */
	public void setDispConvertor(java.util.Hashtable newDispConvertor) {
		m_htDispConvertor = newDispConvertor;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 19:40:50)
	 * 
	 * @param newColClassName
	 *            java.lang.String
	 */
	public void setDynamicColClassName(java.lang.String newColClassName) {
		m_dynamicColClassName = newColClassName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-30 8:34:51)
	 * 
	 * @param newDynamicFieldNames
	 *            java.lang.String[]
	 */
	public void setDynamicFieldNames(java.lang.String[] newDynamicFieldNames) {
		m_strDynamicFieldNames = newDynamicFieldNames;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-4-10 18:32:43)
	 * 
	 * @param newM_filterPks
	 *            java.lang.String[]
	 * 
	 * ���ù��˵�Pks
	 */
	public void setFilterPks(java.lang.String[] newM_filterPks) {
		setFilterPks(newM_filterPks, IFilterStrategy.INSECTION);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2005-9-02 18:32:43)
	 * 
	 * @param newM_filterPks
	 *            java.lang.String[]
	 * 
	 * 
	 * ���ù��˵�Pks
	 */
	public void setFilterPks(java.lang.String[] newM_filterPks,
			int filterStrategy) {
		m_filterPks = newM_filterPks;
		// ���˲���
		m_filterStrategy = filterStrategy;
		m_hmFilterPks.clear();
		if (m_filterPks != null && m_filterPks.length > 0) {

			for (int i = 0; i < m_filterPks.length; i++) {
				m_hmFilterPks.put(m_filterPks[i], m_filterPks[i]);
			}
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-8 13:31:41)
	 * 
	 * @param newGroupPart
	 *            java.lang.String
	 */
	public void setGroupPart(java.lang.String newGroupPart) {
		m_strGroupPart = newGroupPart;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-6-25 10:53:54)
	 * 
	 * @param newHiddenFieldCode
	 *            java.lang.String[]
	 */
	public void setHiddenFieldCode(java.lang.String[] newHiddenFieldCode) {
		getHtCodeIndex().clear();
		hiddenFieldList.clear();
		hiddenFieldList.addAll(Arrays.asList(newHiddenFieldCode));
		m_strHiddenFieldCode = newHiddenFieldCode;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-6-17 18:35:14)
	 * 
	 * @return java.util.Hashtable
	 */
	public void setHtCodeIndex(Hashtable ht) {
		m_htCodeIndex = ht;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 19:38:34)
	 * 
	 * @param newDynamicCol
	 *            boolean
	 */
	public void setIsDynamicCol(boolean newDynamicCol) {
		m_isDynamicCol = newDynamicCol;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-2-21 16:27:33)
	 * 
	 * @param newM_mnecodeInput
	 *            boolean
	 */
	public void setMnecodeInput(boolean newM_mnecodeInput) {
		m_mnecodeInput = newM_mnecodeInput;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-4-10 20:39:56)
	 * 
	 * @param newM_bNotLeafSelectedEnabled
	 *            boolean
	 */
	public void setNotLeafSelectedEnabled(boolean newM_bNotLeafSelectedEnabled) {
		m_bNotLeafSelectedEnabled = newM_bNotLeafSelectedEnabled;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-8 10:36:45)
	 * 
	 * @param newRefNodeName
	 *            java.lang.String
	 */
	public void setRefNodeName(java.lang.String newRefNodeName) {
		m_strRefNodeName = newRefNodeName;
	}

	/**
	 * v5 ������ʹ�� �滻��ʽ����ֲ����� 1��setRefNodeName(refnodename); 2��setPk_corp(pk_corp);
	 * 
	 * @deprecated
	 */
	public void setRefNodeName(java.lang.String newRefNodeName, String pk_corp) {
		m_strPk_corp = pk_corp;
		m_strRefNodeName = newRefNodeName;
	}

	/**
	 * Ϊ����������DefaultModel��ӵķ�������DefaultModel�ﱻ���� �����Զ���model����ֱ�ӵ��ø÷������븲����
	 */
	public void setRefTitle(java.lang.String newRefTitle) {
		m_strRefTitle = newRefTitle;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-29 10:08:50)
	 * 
	 * @return int[]
	 */
	public void setShownColumns(int[] iShownColumns) {
		m_iShownColumns = iShownColumns;
		return;
	}

	/**
	 * Ϊ����������DefaultModel��ӵķ�������DefaultModel�ﱻ���� �����Զ���model����ֱ�ӵ��ø÷������븲����
	 */
	public void setTableName(java.lang.String newTableName) {
		m_strTableName = newTableName;
	}

	/**
	 * �����Ƿ�ʹ������Ȩ�ޡ� �������ڣ�(2001-8-23 21:03:06)
	 */
	public void setUseDataPower(boolean useDataPower) {
		m_bUserDataPower = useDataPower;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 19:54:29)
	 * 
	 * @param newParameter
	 *            java.lang.Object
	 */
	public void setUserParameter(java.lang.Object newParameter) {
		m_userParameter = newParameter;
	}

	/**
	 * ���ݼ�¼λ�ã������Ʋ�����ֵ
	 */
	public void setValue(int recordIndex, String field, Object value) {
		Vector vecData = getSelectedData();
		if (vecData == null || recordIndex >= vecData.size()) {
			return;
		}
		Vector vRecord = (Vector) vecData.get(recordIndex);
		int col = getFieldIndex(field);

		//
		if (isDynamicCol()) {
			if (vRecord == null || vRecord.size() == 0 || col < 0)
				return;

			if (value != null) {

				if (col >= vRecord.size()) {
					vRecord.add(value);
				} else {
					vRecord.setElementAt(value, col);
				}
				return;

			}
		}

		if (vRecord == null || vRecord.size() == 0 || col < 0
				|| col >= vRecord.size())
			return;

		if (value != null) {
			vRecord.setElementAt(value, col);

		}

	}

	/**
	 * �Զ�����գ�������ӷ������
	 */
	public void addSealedWherePart(String refNodeName, String sealedWherePart) {

		DocSealWherePartMng.addSealedWherePart(refNodeName, sealedWherePart);
	}

	/**
	 * sxj 2004-5-26 ���� �ı�WherePartʱ�Ƿ��������
	 */
	public void addWherePart(String newWherePart, boolean isRrefreshData) {
		addWherePart(newWherePart);
		if (isRrefreshData) {
			clearModelData();
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-1 10:21:59)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFilterDlgClaseName() {
		return refFilterDlgClaseName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-9-23 9:03:51)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMatchField() {
		if (m_matchField == null) {
			return getPkFieldCode();
		}
		return m_matchField;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-11-10 10:31:47)
	 * 
	 * @return java.util.HashMap
	 */
	private java.util.HashMap getMatchHM() {
		return m_matchHM;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-2-19 9:24:17)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getMnecodes() {
		return m_mnecodes;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-16 10:31:17)
	 * 
	 * @return java.lang.Object
	 */
	public java.lang.Object getPara() {
		return userPara;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-14 15:42:52)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getQuerySql() {
		return querySql;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-15 10:51:44)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRefQueryDlgClaseName() {
		return refQueryDlgClaseName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-14 16:01:25)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTempDataWherePart() {
		return refTempDataWherePart;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-10 12:09:05)
	 * 
	 * @return boolean
	 */
	public boolean isFromTempTable() {
		return isFromTempTable;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-14 16:13:36)
	 * 
	 * @return boolean
	 */
	public boolean isIncludeSub() {
		return isIncludeSub;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-31 15:57:17)
	 * 
	 * @return boolean
	 */
	public boolean isRefreshMatch() {
		return m_isRefreshMatch;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-17 12:53:23)
	 * 
	 * @return boolean
	 */
	public boolean isSealedDataShow() {
		return m_isSealedDataShow;
	}

	/**
	 * ��ȡƥ������ ƥ��ɹ� ���ݣ�>m_vecSelectedData ƥ�䲻�ɹ� null��>m_vecSelectedData
	 * �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector sxj �޸����value= null or value.length()=0
	 *         ����ƥ��,���Ч�ʣ���ֹ�ظ�reloadData() sxj 2004-11-11 add
	 */
	public Vector matchData(String field, String[] values) {
		return matchData(new String[] { field }, values);
	}

	/**
	 * ƥ���������ݣ���ѡ������Ҳ�ı��ˡ� �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector sxj 2004-06-08 �������Pk ����ƥ�䣬��������ڽ���ƥ�䡣 ����
	 *         getMatchField()�ṩ���н���ƥ��
	 */
	public Vector matchPkData(String[] strPkValues) {

		if (strPkValues == null) {
			return null;
		}

		Vector v = null;
		String matchsql = null;

		matchsql = getMatchSql(strPkValues);

		Vector matchVec = getMatchVectorFromLRU(strPkValues);

		if (matchVec != null) {
			// setSelectedDataAndConvertData(matchVec);
			setSelectedData(matchVec);

			return matchVec;
		}

		try {
			if (isNonSqlMatch(matchsql)) {
				v = matchData(getMatchField(), strPkValues);

			} else {

				if (isFromTempTable()) {
					v = modelHandler.queryRefDataFromTemplateTable(matchsql);
				} else {
					v = modelHandler.matchPK(getDataSource(), matchsql);
				}
			}

		} catch (Exception e) {
			Debug.error(e.getMessage(), e);
		}

		// putLruMap(strPkValues, v);

		setSelectedDataAndConvertData(v);

		return getSelectedData();
	}

	public Vector getMatchVectorFromLRU(String[] strPkValues) {
		if (isCacheEnabled()) {
			LRUMap<String, Vector> lruMap = modelHandler.getLRUMap();

			String lruKey = getLRUKey(strPkValues);

			Vector matchVec = lruMap.get(lruKey);
			return matchVec;
		}
		return null;
	}

	private void putLruMap(Vector vecData) {
		if (isCacheEnabled()) {
			String[] pkValues = getPkValues();
			if (pkValues == null) {
				return;
			}
			LRUMap<String, Vector> lruMap = modelHandler.getLRUMap();
			String lruKey = getLRUKey(pkValues);
			lruMap.put(lruKey, vecData);
		}
	}

	private String getLRUKey(String[] strPkValues) {
		StringBuffer sb = new StringBuffer();
		String dsName = getDataSource();
		if (dsName == null) {
			dsName = InvocationInfoProxy.getInstance().getUserDataSource();
		}
		sb.append(dsName).append(getRefNodeName()).append(
				getMatchSql(strPkValues));

		if (strPkValues != null && strPkValues.length > 0) {
			for (int i = 0; i < strPkValues.length; i++) {
				sb.append(strPkValues[i]);
			}

		}
		return sb.toString();
	}

	/**
	 * 
	 */
	public boolean isNonSqlMatch(String matchsql) {
		return modelHandler.isNonSqlMatch(matchsql);
	}

	public String getMatchSql(String[] strPkValues) {
		setPKMatch(true);
		boolean originSealedDataShow = isSealedDataShow();
		boolean originUseDataPower = isUseDataPower();

		if (!isRefEnable()) {
			// ������ղ��ɱ༭������������Ȩ�޿���;����������� ---��V5������
			setUseDataPower(false);
			setSealedDataShow(true);
		}

		String matchsql;
		// pkƥ��ʱ����distinct,��ֹ����������ظ����ݵ����⡣
		String strPatch = IRefConst.DISTINCT;
		String strWherePart = null;

		String[] fieldCodes = getFieldCode();

		if (fieldCodes != null && fieldCodes.length > 0) {
			if (fieldCodes[0] != null
					&& strPatch.indexOf(fieldCodes[0].toLowerCase()) >= 0) {
				strPatch = "";
			}
		}
		if (isMatchPkWithWherePart()) {
			strWherePart = getWherePart();
		}

		if (isLargeDataRef() && canUseDB())
			matchsql = getSpecialMatchSql(new String[] { getMatchField() },
					strPkValues, strPatch, getFieldCode(),
					getHiddenFieldCode(), getTableName(), strWherePart,
					getOrderPart());
		// ������Sql
		else
			matchsql = getSql_Match(new String[] { getMatchField() },
					strPkValues, strPatch, getFieldCode(),
					getHiddenFieldCode(), getTableName(), strWherePart,
					getOrderPart());// ������Sql

		setPKMatch(false);

		setSealedDataShow(originSealedDataShow);
		setUseDataPower(originUseDataPower);

		return matchsql;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-1 10:21:59)
	 * 
	 * @param newFilterDlgClaseName
	 *            java.lang.String
	 */
	public void setFilterDlgClaseName(java.lang.String newFilterDlgClaseName) {
		refFilterDlgClaseName = newFilterDlgClaseName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-10 12:09:05)
	 * 
	 * @param newFromTempTable
	 *            boolean
	 */
	public void setFromTempTable(boolean newFromTempTable) {
		isFromTempTable = newFromTempTable;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-14 16:13:36)
	 * 
	 * @param newIncludeSub
	 *            boolean
	 */
	public void setIncludeSub(boolean newIncludeSub) {
		isIncludeSub = newIncludeSub;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-31 15:57:17)
	 * 
	 * @param newRefreshMatch
	 *            boolean
	 */
	public void setIsRefreshMatch(boolean newRefreshMatch) {
		m_isRefreshMatch = newRefreshMatch;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-2-19 9:24:17)
	 * 
	 * @param newM_mnecode
	 *            java.lang.String
	 */
	public void setMnecode(java.lang.String[] newM_mnecodes) {
		m_mnecodes = newM_mnecodes;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-16 10:31:17)
	 * 
	 * @param newPara
	 *            java.lang.Object
	 */
	public void setPara(java.lang.Object newPara) {
		userPara = newPara;
	}

	/**
	 * sxj 2004-5-26 ���� �ı�WherePartʱ�Ƿ��������
	 */
	public void setPara(java.lang.Object newPara, boolean isRrefreshData) {
		setPara(newPara);
		if (isRrefreshData) {
			clearModelData();
		}
	}

	/**
	 * sxj 2004-5-26 ���� �ı�WherePartʱ�Ƿ��������
	 */
	public void setPk_corp(String strPk_corp, boolean isRrefreshData) {
		setPk_corp(strPk_corp);
		if (isRrefreshData) {
			clearModelData();
		}
	}

	/**
	 * ֻ�޸ı�����ֵ����ͬ��WherePart�е�ֵ��
	 */
	public void setPk_corpValue(String strPk_corp) {
		m_strPk_corp = strPk_corp;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-14 15:42:52)
	 * 
	 * @param newQuerySql
	 *            java.lang.String
	 */
	public void setQuerySql(java.lang.String newQuerySql) {
		querySql = newQuerySql;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-15 10:51:44)
	 * 
	 * @param newRefQueryDlgClaseName
	 *            java.lang.String
	 */
	public void setRefQueryDlgClaseName(java.lang.String newRefQueryDlgClaseName) {
		refQueryDlgClaseName = newRefQueryDlgClaseName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-3-17 12:53:23)
	 * 
	 * @param newSealedDataShow
	 *            boolean
	 */
	public void setSealedDataShow(boolean newSealedDataShow) {
		m_isSealedDataShow = newSealedDataShow;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-14 16:01:25)
	 * 
	 * @param newTempDataWherePart
	 *            java.lang.String
	 */
	public void setTempDataWherePart(java.lang.String newTempDataWherePart) {
		refTempDataWherePart = newTempDataWherePart;
	}

	/**
	 * sxj 2004-5-26 ���� �ı�WherePartʱ�Ƿ��������
	 */
	public void setWherePart(String newWherePart, boolean isRrefreshData) {
		setWherePart(newWherePart);
		if (isRrefreshData) {
			clearModelData();
		}
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-6-3 16:56:59)
	 * 
	 * @author��ëʿ��
	 * @return nc.ui.bd.ref.AbstractRefModel
	 * @deprecated since v5.0
	 */
	public AbstractRefModel getM_defGridmodel() {
		return m_defGridmodel;
	}

	/**
	 * @return ���� m_pk_org��
	 */
	public String getPk_GlOrgBook() {
		return m_pk_glOrgBook;
	}

	/**
	 * @return ���� m_orgTypeCode��
	 */
	public String getOrgTypeCode() {
		return m_orgTypeCode;
	}

	/**
	 * ��������֧���Զ�����գ���̬�ı������˲���WherePart�Ķ�̬�ı䡣
	 * 
	 * @param orgTypeCode
	 *            Ҫ���õ� m_orgTypeCode��
	 * @param pk_org
	 *            Ҫ���õ� m_pk_org�� ���˲��������������ýӿ� ��Ҫ���ܣ�
	 *            �ṩ�����˰������˲���ʽ���ã�����ҵ������ÿ��ܻ�������⡣
	 */
	public void setPk_GlOrgBook(String orgTypeCode, String pk_GlOrgBook) {
		// ��������������
		String pk = pk_GlOrgBook;
		String type = orgTypeCode;
		BdinfoVO infoVO = getBdinfoVO();
		if (infoVO != null) {
			pk = BDGLOrgBookAccessor.getAccuratePK(infoVO.getOrgtypecode(),
					orgTypeCode, pk_GlOrgBook);
			type = infoVO.getOrgtypecode();
		}
		setOrgTypeCodeAndPK(type, pk);

		// ������������.
		resetWherePart();

	}

	/*
	 * ���õ������͵����ԡ�
	 */
	public void setOrgTypeCodeAndPK(String orgTypeCode, String pk) {
		// ����ǹ�˾����
		if (OrgnizeTypeVO.COMPANY_TYPE.equals(orgTypeCode)) {
			setPk_corp(pk);
			m_orgTypeCode = null;
			m_pk_glOrgBook = null;

		} else if (OrgnizeTypeVO.ZHUZHANG_TYPE.equals(orgTypeCode)) {
			m_orgTypeCode = orgTypeCode;
			m_pk_glOrgBook = pk;
		}

	}

	/**
	 * @param newM_defGridmodel
	 *            nc.ui.bd.ref.AbstractRefModel
	 * @deprecated since v5.0
	 */
	public void setM_defGridmodel(AbstractRefModel newM_defGridmodel) {
		m_defGridmodel = newM_defGridmodel;
	}

	/*
	 * 
	 */
	public String getDataPowerSubSql(String strTableName,
			String strTableShowName) {
		String tableName = strTableName;
		if (strTableName != null) {
			tableName = strTableName.trim();
		}
		String powerSql = modelHandler.getDataPowerSubSql(tableName,
				strTableShowName, this);
		return powerSql;
	}

	protected String getDataPowerSqlKey(String strTableName,
			String strTableShowName) {
		String pk_org = null;
		if (getPk_GlOrgBook() != null) {
			pk_org = getPk_GlOrgBook();
		} else {
			pk_org = getPk_corp();
		}
		String tableName = strTableName;
		if (strTableName != null) {
			tableName = strTableName.trim();
		}
		String dataPowerKey = tableName + "_" + strTableShowName + pk_org;

		if (getResourceID() != -1) {
			dataPowerKey += getResourceID();
		}

		return dataPowerKey;
	}

	/**
	 * @return ���� m_fun_code��
	 */
	public String getFun_code() {
		return m_fun_code;
	}

	/**
	 * @param m_fun_code
	 *            Ҫ���õ� m_fun_code��
	 */
	public void setFun_code(String m_fun_code) {

		boolean isEqual = modelHandler.equals(this.m_fun_code, m_fun_code);

		this.m_fun_code = m_fun_code;

		// ͬʱҪ���³�ʼ�����յ�����
		if (!isEqual) {
			setRefNodeName(getRefNodeName());
		}
	}

	/**
	 * Ϊ��������Ӵ˷���, �븲�Ǵ˷�������Ҫ�����ֶ����顣
	 */
	protected RefVO_mlang[] getRefVO_mlang() {

		return null;

	}

	/**
	 * * Ϊ��������Ӵ˷���, ���ṩĬ��ʵ��.
	 * 
	 * vecData�������е����ݣ�ȫ���������ò��������vecData,��Ҫ��getData()�����õ����������ѭ����
	 * vecData�е�����Ϊÿһ��Record��ÿһ��RecordҲ��һ��Vector.
	 * 
	 * ����ֵΪVector����������Ϊ������Object[],ע�����Object[]��˳��һ����refVO_mlangs��˳��һ�¡�
	 * 
	 * �������ڣ�(2005-3-30 16:19:24)
	 * 
	 * @return Vector
	 */
	private Vector getValues_mlang(Vector vecData, RefVO_mlang[] refVO_mlangs) {
		// Ҫ�������

		Vector datas = new Vector();

		if (vecData != null && vecData.size() > 0) {
			int recordAccout = vecData.size(); // �м�¼����
			if (refVO_mlangs != null) {
				for (int i = 0; i < refVO_mlangs.length; i++) {
					RefVO_mlang refVO_mlang = refVO_mlangs[i];
					String resid = "";
					String resPath = refVO_mlang.getDirName();
					String[] resIDFieldNames = refVO_mlangs[i]
							.getResIDFieldNames();

					Object[] oData_mlang = new Object[recordAccout];

					for (int j = 0; j < recordAccout; j++) {

						Vector record = (Vector) vecData.get(j); // �м�¼
						resid = "";
						if (resIDFieldNames != null) {
							resid += refVO_mlang.getPreStr();
							for (int k = 0; k < resIDFieldNames.length; k++) {
								Object oValue = record
										.get(getFieldIndex(resIDFieldNames[k]));
								if (oValue != null) {
									resid += oValue.toString();
								}

							}
						}
						String dirFieldName = refVO_mlang.getDirFieldName();
						if (dirFieldName != null) {
							int dirFieldNanemIndex = getFieldIndex(dirFieldName);
							if (dirFieldNanemIndex != -1) {
								Object oValue = record.get(dirFieldNanemIndex);
								resPath = oValue == null ? resPath : oValue
										.toString();
							}

						}

						// ������ԴIDȡ����������
						String str_multiLang = NCLangRes4VoTransl
								.getNCLangRes().getStrByID(resPath, resid);
						// ������Դû���ҵ�,����ԭ����ֵ.
						if (resid.equals(str_multiLang)) {
							str_multiLang = record.get(
									getFieldIndex(refVO_mlang.getFieldName()))
									.toString();
						}
						oData_mlang[j] = str_multiLang;

					}
					datas.add(oData_mlang);
				}

			}
		}

		return datas;

	}

	/**
	 * Ϊ��������Ӵ˷���, ���÷�����ֵ������������. �����ض��е�ֵ �������ڣ�(2005-3-30 16:19:24)
	 * 
	 */
	private void setMlangValues(Vector vecData, RefVO_mlang[] refVO_mlangs) {

		if (vecData == null || vecData.size() == 0 || refVO_mlangs == null) {
			return;
		}
		Vector vec_malng = getValues_mlang(vecData, refVO_mlangs);
		if (vec_malng == null || vec_malng.size() != refVO_mlangs.length) {
			return;
		}
		String[] fieldNames = new String[refVO_mlangs.length];
		for (int i = 0; i < refVO_mlangs.length; i++) {
			fieldNames[i] = refVO_mlangs[i].getFieldName();
		}
		setValuesByFieldName(vecData, vec_malng, fieldNames);

	}

	class DefFieldsHandler {

		/**
		 * ���й�ʽ�Ĳ��գ�����ʽȡֵ�� �������ڣ�(2005-3-30 16:19:24)
		 * 
		 */
		void setDefFieldValues(Vector vecData) {

			String[] defFields = getShowDefFields();

			if (vecData == null || vecData.size() == 0 || defFields == null
					|| defFields.length == 0) {
				return;
			}
			Vector<Object[]> rawData = new Vector<Object[]>();
			Vector convertedData = null;
			DefFieldInfo defFieldInfo = new DefFieldInfo(defFields);

			for (int i = 0; i < defFields.length; i++) {
				rawData.add(getValues(defFields[i], vecData));
			}
			try {
				convertedData = UFRefDefTanslateUtil.getDefValues(defFieldInfo,
						rawData, Integer.valueOf(getOrgTypeCode()).intValue(),
						getPk_corp());
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}

			if (convertedData == null) {
				return;
			}

			setValuesByFieldName(vecData, convertedData, defFields);

		}
	}

	class FormulaHandler {

		private FormulaParseFather parse = null;

		/**
		 * ���й�ʽ�Ĳ��գ�����ʽȡֵ�� �������ڣ�(2005-3-30 16:19:24)
		 * 
		 */
		void setFormulaValues(Vector vecData, String[][] formulas) {

			if (vecData == null || vecData.size() == 0 || formulas == null) {
				return;
			}
			String[][] showFormulas = getShowFieldFormulas(formulas);
			if (showFormulas == null) {
				return;
			}
			Object[][] datas = getValues_formulas(vecData, showFormulas);

			if (datas == null || datas.length != showFormulas.length) {
				return;
			}
			Vector v = new Vector();
			String[] fieldNames = new String[datas.length];
			for (int i = 0; i < datas.length; i++) {
				v.add(datas[i]);
				fieldNames[i] = (String) showFormulas[i][0];
			}
			setValuesByFieldName(vecData, v, fieldNames);

		}

		private String[][] getShowFieldFormulas(String[][] formulas) {
			List<String[]> list = new ArrayList<String[]>();
			for (int i = 0; i < formulas.length; i++) {
				String formulaField = formulas[i][0];
				if (isShowField(formulaField)) {
					list.add(formulas[i]);
				}
			}
			String[][] showFormulas = null;
			if (list.size() > 0) {
				showFormulas = new String[list.size()][];
				list.toArray(showFormulas);
			}
			return showFormulas;
		}

		private Object[][] getValues_formulas(Vector vecData,
				String[][] formulas) {
			// Ҫ�������

			Object[][] datas = null;
			int flen = formulas.length;
			if (vecData != null && vecData.size() > 0 && flen > 0) {

				int recordAccout = vecData.size(); // �м�¼����
				datas = new Object[flen][recordAccout];
				ArrayList list = new ArrayList();
				String formulaField = null;
				String strFormula = null;
				for (int i = 0; i < flen; i++) {
					list.clear();
					formulaField = formulas[i][0];
					strFormula = formulas[i][1];

					for (int j = 0; j < recordAccout; j++) {
						Vector record = (Vector) vecData.get(j);
						list.add(getFormulaValue(record, formulaField));
					}
					datas[i] = getFormulaValues(formulaField, strFormula, list);
				}
			}

			return datas;

		}

		/**
		 * <p>
		 * <strong>����޸��ˣ�sxj</strong>
		 * <p>
		 * <strong>����޸����ڣ�2006-9-19</strong>
		 * <p>
		 * 
		 * @param
		 * @return String
		 * @exception BusinessException
		 * @since NC5.0
		 */
		private String getFormulaValue(Vector record, String formulaField) {
			int fieldIndex = getFieldIndex(formulaField);
			if (fieldIndex == -1) {
				return null;
			}
			Object obj = record.get(fieldIndex);

			String value = null;

			if (obj instanceof RefValueVO && obj != null) {

				Object originValue = ((RefValueVO) obj).getOriginValue();

				value = originValue == null ? null : originValue.toString();

			} else {
				if (obj != null) {
					value = obj.toString();
				}
			}
			return value;
		}

		private Object[] getFormulaValues(String fieldName, String formulas,
				List givenvalues) {
			FormulaParseFather parse = getParse();
			// String express = formula;
			String[] expresses = StringUtil.toArray(formulas, ";");
			parse.setExpressArray(expresses);
			parse.addVariable(fieldName, givenvalues);
			Object[][] values = parse.getValueOArray();
			return values[values.length - 1];
		}

		/**
		 * @return ���� parse��
		 */
		private FormulaParseFather getParse() {
			if (parse == null) {
				parse = new nc.ui.pub.formulaparse.FormulaParse();
			}
			return parse;
		}

	}

	/*
	 * _ add by hetian 2005-04-11 ______________________________________
	 */
	/**
	 * �жϸò����Ƿ��Ǵ����ݱ�
	 * 
	 * @return
	 */
	public boolean isLargeDataRef() {
		if (m_strRefNodeName == null || m_strRefNodeName.equals(""))
			return false;
		if (LARGE_NODE_NAME.indexOf(m_strRefNodeName) >= 0
				&& isChangeTableSeq()) {
			return true;
		}
		return false;
	}

	/**
	 * �ı�����
	 * 
	 * @param table
	 * @return
	 */
	public String changeBaseTable(String table) {

		if (table == null || table.indexOf("join") < 0)
			return table;
		StringTokenizer token = new StringTokenizer(table);
		String firstTable = "";
		String secondTable = "";
		String lastElement = "";
		String joinStr = "";
		String onStr = "";
		boolean isJoin = false;
		boolean isOn = false;
		int index = 0;
		while (token.hasMoreTokens()) {
			String element = token.nextToken();
			if (lastElement.equalsIgnoreCase("join")) {
				secondTable = element;
				isJoin = false;
			}
			if (element.equalsIgnoreCase("on")) {
				isOn = true;
			}
			if (isJoin) {
				joinStr += new StringBuffer().append(" ").append(element)
						.append(" ").toString();
			}
			if (isOn) {
				onStr += new StringBuffer().append(" ").append(element).append(
						" ").toString();
			}
			if (index == 0) {
				firstTable = new StringBuffer().append(element).append(" ")
						.toString();
				isJoin = true;
			}

			lastElement = element;
			index++;
		}

		return secondTable + joinStr + firstTable + onStr;
	}

	/**
	 * �õ�ƥ�����
	 * 
	 * @return
	 */
	public String getSpecialMatchSql(String[] fieldNames, String values[],
			String strPatch, String[] strFieldCode, String[] hiddenFields,
			String strTableName, String strWherePart, String strOrderField) {
		if (strTableName == null || strTableName.trim().length() == 0)
			return null;
		if (!isPKMatch()) {
			strTableName = changeBaseTable(strTableName);
		}
		if (strOrderField != null) {
			strOrderField = filterColumn(strOrderField);
		}
		return getSql_Match(fieldNames, values, strPatch, strFieldCode,
				hiddenFields, strTableName, strWherePart, strOrderField);

	}

	/**
	 * ������� SQL
	 */
	public String buildBaseSql(String patch, String[] columns,
			String[] hiddenColumns, String tableName, String whereCondition) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer sql = new StringBuffer("select ").append(patch)
				.append(" ");
		//�����ظ����� mlr
	//	sql.append(" distinct ");
		//end
		int columnCount = columns == null ? 0 : columns.length;
		addQueryColumn(columnCount, sql, columns, hiddenColumns);
		// ����FROM�Ӿ�
		sql.append(" from ").append(tableName);
		// ����WHERE�Ӿ�
		if (whereCondition != null && whereCondition.trim().length() != 0) {
			whereClause.append(" where (").append(whereCondition).append(" )");
		} else
			whereClause.append(" where 11=11 ");

		appendAddWherePartCondition(whereClause);

		addDataPowerCondition(getTableName(), whereClause);
		addSealCondition(whereClause);
		addEnvWherePart(whereClause);
		sql.append(" ").append(whereClause.toString());

		return sql.toString();
	}

	protected void addDataPowerCondition(String tableName,
			StringBuffer whereClause) {
		if (isUseDataPower()) {

			String powerSql = getDataPowerSubSql(tableName, getDataPowerName());

			if (powerSql != null)

				whereClause.append(" and (").append(
						getDataPowerResourceColumn()).append(" in (").append(
						powerSql).append("))");

		}

	}

	public String getDataPowerResourceColumn() {
		if (dataPowerResourceColumn == null) {
			return getPkFieldCode();
		}
		return dataPowerResourceColumn;
	}

    public void appendAddWherePartCondition(StringBuffer whereClause) {

		if (getAddWherePart() == null) {
			return;
		}

		if (isPKMatch() && !isMatchPkWithWherePart()) {

			return;

		}
		whereClause.append(" ").append(getAddWherePart());

	}

	/**
	 * ������Ȩ���ض���Ļ��ᣬ���า�ǡ� ���������ʹ�ã������ƿ�Ŀ��汾
	 */
	protected String getDataPowerName() {
		return getRefNodeName();
	}

	/**
	 * ���������
	 * 
	 * @param iSelectFieldCount
	 * @param strSql
	 * @param strFieldCode
	 * @param hiddenFields
	 */
	public void addQueryColumn(int iSelectFieldCount, StringBuffer strSql,
			String[] strFieldCode, String[] hiddenFields) {
		for (int i = 0; i < iSelectFieldCount; i++) {
			strSql.append(strFieldCode[i]);
			if (i < iSelectFieldCount - 1)
				strSql.append(",");
		}
		// ���������ֶ�
		if (hiddenFields != null && hiddenFields.length > 0) {
			for (int k = 0; k < hiddenFields.length; k++) {
				if (hiddenFields[k] != null
						&& hiddenFields[k].trim().length() > 0) {
					strSql.append(",");
					strSql.append(hiddenFields[k]);
				}
			}
		}
	}

	/**
	 * ��ӷ������
	 * 
	 * @param whereClause
	 */
	public void addSealCondition(StringBuffer whereClause) {
		// ������ݲ���ʾ�����Where����
		if (!isSealedDataShow()) {

			String wherePart = getSealedWherePart();
			if (wherePart != null) {
				whereClause.append(" and (").append(wherePart).append(") ");

			}
		}
	}

	/**
	 * ��ѯ���շ��������
	 * 
	 * @param refNodeName
	 * @return refNodeName��Ӧ���յĹ��˷�����ݵ�SQL where����
	 */
	public String getSealedWherePart() {
		if (sealedWherePart != null) {
			return sealedWherePart;
		}

		sealedWherePart = DocSealWherePartMng
				.getSealedWherePart(getRefNodeName());
		if (sealedWherePart == null) {
			sealedWherePart = DocSealWherePartMng
					.getSealedWherePart(getTableName());
		}

		return sealedWherePart;
	}

	/**
	 * ���÷������
	 * 
	 * @param sealedWherePart
	 *            Ҫ���õ� sealedWherePart��
	 */
	public void setSealedWherePart(String sealedWherePart) {
		this.sealedWherePart = sealedWherePart;
	}

	/**
	 * ���˱���
	 * 
	 * @param column
	 * @return
	 */
	public String filterColumn(String column) {
		return column.substring(column.indexOf(".") + 1, column.length());
	}

	/**
	 * ����SQL���
	 * 
	 * @author ���� �����޸�
	 */
	protected String getSql(String strPatch, String[] strFieldCode,
			String[] hiddenFields, String strTableName, String strWherePart,
			String strGroupField, String strOrderField) {
		if (strTableName == null || strTableName.trim().length() == 0) {
			return null;
		}

		String basSQL = buildBaseSql(strPatch, strFieldCode, hiddenFields,
				strTableName, strWherePart);
		StringBuffer sqlBuffer = new StringBuffer(basSQL);
		if (getQuerySql() != null) {
			addQueryCondition(sqlBuffer);
		}
		if (getBlurValue() != null && isIncludeBlurPart()) {
			String blurSql = addBlurWherePart();
			sqlBuffer.append(blurSql);
		}

		//
		// ����Group�Ӿ�
		if (strGroupField != null) {
			sqlBuffer.append(" group by ").append(strGroupField).toString();
		}
		// ����ORDER�Ӿ�
		if (strOrderField != null && strOrderField.trim().length() != 0) {
			sqlBuffer.append(" order by ").append(strOrderField).toString();
		}
		return sqlBuffer.toString();
	}

	protected void addQueryCondition(StringBuffer sqlBuffer) {
		sqlBuffer.append(" and (").append(getPkFieldCode()).append(" in (")
				.append(getQuerySql()).append("))").toString();
	}

	/**
	 * @param sqlBuffer
	 */
	public String addBlurWherePart() {
		return modelHandler.addBlurWherePart();
	}

	/**
	 */
	protected String getSql_Match(String[] fieldNames, String[] values,
			String strPatch, String[] strFieldCode, String[] hiddenFields,
			String strTableName, String strWherePart, String strOrderField) {
		if (strTableName == null || strTableName.trim().length() == 0)
			return null;

		String basSQL = null;

		basSQL = buildBaseSql(strPatch, strFieldCode, hiddenFields,
				strTableName, strWherePart);

		StringBuffer buffer = new StringBuffer(basSQL);

		buffer.append(" and (");
		if (isPKMatch()) {
			buffer.append(getWherePartByFieldsAndValues(fieldNames, values));
		} else {
			String[] toLowCasefieldNames = new String[fieldNames.length];
			String[] toLowCaseValues = new String[values.length];
			for (int i = 0; i < toLowCasefieldNames.length; i++) {
				toLowCasefieldNames[i] = RefPubUtil.toLowerDBFunctionWrapper(
						this, fieldNames[i]);
			}
			for (int i = 0; i < toLowCaseValues.length; i++) {
				toLowCaseValues[i] = RefPubUtil.toLowerCaseStr(this, values[i]);
			}

			buffer.append(getWherePartByFieldsAndValues(toLowCasefieldNames,
					toLowCaseValues));
		}
		buffer.append(") ");
		// ����ORDER�Ӿ�
		if (strOrderField != null) {
			buffer.append(" order by ").append(strOrderField).toString();
		}
		return buffer.toString();
	}

	/*
	 * 
	 * ��java.util.Hashtable getDispConvertor()�г�ʼ��
	 * 
	 */
	public Hashtable getConvertHT(String tableName, String[] fieldNames) {
		// ֻ����һ����ʶ
		setDdReaderVO(tableName, fieldNames);
		// ��ȡֵ��
		return null;
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-7-20</strong>
	 * <p>
	 * 
	 * @param
	 * @return Hashtable
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private Hashtable getDDReaderMap(String tableName, String[] fieldNames) {
		DataDictionaryReader ddReader = new DataDictionaryReader(tableName);
		Hashtable convert = new Hashtable();

		for (int i = 0; i < fieldNames.length; i++) {
			// ��������Ѿ���������,ȥ������.ֵ����û�б���
			String newfieldName = fieldNames[i];
			if (newfieldName.indexOf(".") > 0) {
				newfieldName = newfieldName.substring(
						newfieldName.indexOf(".") + 1, newfieldName.length());
			}
			String[] keys = ddReader.getQzsm(newfieldName);
			Hashtable contents = new Hashtable();
			for (int j = 0; j < keys.length; j++) {
				if (keys[j] != null) {
					contents.put(ddReader
							.getNumberByQzsm(newfieldName, keys[j]).toString(),
							keys[j]);
				}
			}

			convert.put(fieldNames[i], contents);
		}
		return convert;
	}

	/**
	 * @return ���� isMatchPkWithWherePart��
	 * 
	 */
	public boolean isMatchPkWithWherePart() {
		return isMatchPkWithWherePart;
	}

	/**
	 * @param isMatchPkWithWherePart
	 *            Ҫ���õ� isMatchPkWithWherePart��
	 */
	public void setMatchPkWithWherePart(boolean isMatchPkWithWherePart) {
		this.isMatchPkWithWherePart = isMatchPkWithWherePart;
	}

	/**
	 * @return ���� m_isRefEnable��
	 */
	public boolean isRefEnable() {
		return m_isRefEnable;
	}

	/**
	 * �����Ƿ�ɱ༭ ���� 1��������տ��Ա༭ ��matchPKʱ����������Ȩ�޺Ͳ������������ 2��������ղ��ܱ༭
	 * ��matchPKʱ������������Ȩ�޺Ͱ����������
	 * 3�����ڹ���1�����isUseDataPower()=false,�Ͳ���������Ȩ�ޣ����isSealedDataShow=true,�Ͱ����������
	 */
	public void setisRefEnable(boolean refEnable) {
		m_isRefEnable = refEnable;

	}

	/**
	 * @return ���� formulas��
	 */
	public String[][] getFormulas() {
		return formulas;
	}

	/**
	 * @param formulas
	 *            Ҫ���õ� formulas��
	 */
	public void setFormulas(String[][] formulas) {
		this.formulas = formulas;
	}

	/**
	 * @return ���� isLocQueryEnable��
	 */
	public boolean isLocQueryEnable() {
		return isLocQueryEnable;
	}

	/**
	 * @param isLocQueryEnable
	 *            Ҫ���õ� isLocQueryEnable��
	 */
	public void setLocQueryEnable(boolean isLocQueryEnable) {
		this.isLocQueryEnable = isLocQueryEnable;
	}

	/**
	 * @return ���� m_hmFilterPks��
	 */
	public HashMap getHmFilterPks() {
		return m_hmFilterPks;
	}

	/**
	 * @return ���� m_filterStrategy��
	 */
	public int getFilterStrategy() {
		return m_filterStrategy;
	}

	/**
	 * @return ���� m_filterPks��
	 */
	public String[] getFilterPks() {
		return m_filterPks;
	}

	/**
	 * ��ȡƥ������ ƥ��ɹ� ���ݣ�>m_vecSelectedData ƥ�䲻�ɹ� null��>m_vecSelectedData
	 * �������ڣ�(2001-8-23 18:39:24)
	 * 
	 * @return java.util.Vector sxj �޸����value= null or value.length()=0
	 *         ����ƥ��,���Ч�ʣ���ֹ�ظ�reloadData() sxj 2004-11-11 add
	 */
	public Vector matchData(String[] fields, String[] values) {

		Vector vMatchData = getMatchedRecords(fields, values);

		// ʹ�û��棬����ƥ�䲻�����ݣ�ˢ��������ִ��һ��

		if (isCacheEnabled() && vMatchData == null && isRefreshMatch()) {
			clearData();
			vMatchData = getMatchedRecords(fields, values);
		}
		// boolean isDefConverted = true;
		// if (vMatchData == null || vMatchData.size() == 1) {
		// isDefConverted = false;
		// }
		// if (vMatchData != null) {
		// vMatchData = getConvertedData(false, vMatchData, isDefConverted);
		// }
		setSelectedData(vMatchData);

		return vMatchData;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2005-10-21 14:43:00)
	 * 
	 * @param fieldNames
	 *            java.lang.String[]
	 * @param values
	 *            java.lang.String
	 */
	protected Vector getMatchedRecords(String[] fieldNames, String[] values) {

		Vector vMatchedRecords = null;
		// sxj
		if (values == null || values.length == 0) {
			return vMatchedRecords;
		}
		//
		if (fieldNames == null || fieldNames.length == 0) {

			return vMatchedRecords;
		}
		// Ҫ�Ƚϵ�ֵ�ŵ�HashMap
		getMatchHM().clear();
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				getMatchHM().put(RefPubUtil.toLowerCaseStr(this, values[i]),
						RefPubUtil.toLowerCaseStr(this, values[i]));
			}
		}
		vMatchedRecords = new Vector();
		// ��ȫ����ƥ������
		setIncludeBlurPart(false);
		// ������������
		Vector data = null;

		data = getRefData();

		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				Vector vRecord = (Vector) data.elementAt(i);
				if (vRecord != null)

					for (int j = 0; j < fieldNames.length; j++) {
						int col = getFieldIndex(fieldNames[j]);
						if (col < 0 || col >= vRecord.size()) {
							continue;
						}
						if (vRecord.elementAt(col) != null
								&& getMatchHM().get(
										RefPubUtil.toLowerCaseStr(this, vRecord
												.elementAt(col).toString()
												.trim())) != null) {
							vMatchedRecords.addElement(vRecord);
							break;
						}
					}

			}
		}

		if (vMatchedRecords != null && vMatchedRecords.size() == 0)
			vMatchedRecords = null;
		setIncludeBlurPart(true); // �ָ�ԭ״̬

		return vMatchedRecords;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2005-10-21 17:12:01)
	 * 
	 * @return boolean
	 */
	public boolean isOnlyMneFields() {
		return isOnlyMneFields;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2005-10-21 17:12:01)
	 * 
	 * @param newIsOnlyMneFields
	 *            boolean
	 */
	public void setOnlyMneFields(boolean newIsOnlyMneFields) {
		isOnlyMneFields = newIsOnlyMneFields;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-2-19 9:24:17)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getWherePartByFieldsAndValues(String[] fields,
			String[] values) {
		String wherePart = "";
		StringBuffer sb = new StringBuffer();
		if (fields != null && values != null) {
			int length = fields.length;
			for (int i = 0; i < length; i++) {
				if (values.length == 1) {
					// =
					sb.append(fields[i]).append("='").append(values[0]).append(
							"' ");

				} else {
					// in

					sb.append(RefPubUtil.getInSubSql(fields[i], values));

				}

				if (i == length - 1) {
					continue;
				}
				sb.append(" or ");

			}
		} else {
			return null;
		}

		wherePart = sb.toString();
		return wherePart;

	}

	/**
	 * @return Returns the m_addWherePart.
	 */
	public String getAddWherePart() {
		return m_addWherePart;
	}

	public void saveTempData(Object[] retPKs) {

		// ����Pk ����ʱ��

		if (retPKs == null || retPKs.length == 0) {
			setTempDataWherePart(null);
			return;
		}

		String pk_ts = null;

		RefdatatempVO[] vos = new RefdatatempVO[retPKs.length];
		for (int i = 0; i < retPKs.length; i++) {
			vos[i] = new RefdatatempVO();
			vos[i].setPk_selecteddata(retPKs[i].toString());
			vos[i].setPk_corp(getPk_corp());
			vos[i].setCuserid(getPk_user());
			vos[i].setRefnodename(getRefNodeName());
			vos[i].setPk_ts("temp");

		}
		try {
			pk_ts = modelHandler.saveRefDataTempVOs(vos);

		} catch (Exception e) {
			Debug.error(e.getMessage(), e);
		}

		String selSql = "select pk_selecteddata from bd_refdatatemp ";
		// ������ʱ��ȡ��WherePart
		String wherePart = selSql + " where pk_corp='" + getPk_corp()
				+ "' and cuserid = '" + getPk_user() + "' "
				+ " and refnodename='" + getRefNodeName() + "' and pk_ts = '"
				+ pk_ts + "'";
		setTempDataWherePart(wherePart);

	}

	/**
	 * ���й�ʽ�Ĳ��գ�����ʽȡֵ�� �������ڣ�(2005-3-30 16:19:24)
	 * 
	 */
	public void setColDispValues(Vector vecData, Object[][] dispConverter) {

		if (vecData == null || vecData.size() == 0 || dispConverter == null) {
			return;
		}

		// ����ʾת����ѭ��
		for (int i = 0; i < dispConverter.length; i++) {

			// ����ʾת�����ã֣�
			RefColumnDispConvertVO convertVO = (RefColumnDispConvertVO) dispConverter[i][0];
			if (convertVO == null) {
				continue;
			}
			// ʵ��������
			String className = dispConverter[i][1].toString();

			// ������ʾת�����ã֣�������������ֵ
			// convertVO.setPks(pks);
			convertVO.setRefData(vecData);
			// ��������ʾת��
			m_vecData = modelHandler.setColDispValue(vecData, convertVO,
					className);
		}
	}

	/*
	 * 
	 */
	public ReftableVO getRefTableVO(String pk_corp) {

		return modelHandler.getRefTableVO(pk_corp);
	}

	/*
	 * ��Ŀ��Ϣ���ݷŵ�����.
	 */
	public void setReftableVO2Cache(ReftableVO vo, String pk_org) {

		modelHandler.setReftableVO2Cache(vo, pk_org);
	}

	public String getReftableVOCacheKey(String pk_org) {
		String refNodeName = getRefNodeName();

		// if (OrgnizeTypeVO.ZHUZHANG_TYPE.equals(getOrgTypeCode())){
		// pk_org = getPk_GlOrgBook();
		// }else{
		// pk_org = getPk_corp();
		// }
		// Ŀǰ���ǰ���ǰ��¼��˾��������Ŀ��Ϣ,�Ժ���������Լ���������˲���֧��

		String key = refNodeName + pk_org;
		return key;
	}

	/*
	 * ��ѯ���������Լ�������Ŀ��Ϣ���� 1����Ŀ����ֱ�ӷ��뻺��. 2���������ݷ���
	 */
	public Vector getQueryResultVO() {
		boolean isQueryRefColumn = !modelHandler
				.isReftableVOCached(getPk_corp());

		RefQueryVO queryVO = getRefQueryVO();

		// Ŀǰ���ǰ���ǰ��¼��˾��������Ŀ��Ϣ,�Ժ���������Լ���������˲���֧��
		queryVO.setPk_org(getPk_corp());
		RefQueryResultVO resultVO = null;

		resultVO = modelHandler.queryRefdataByQueryVO(queryVO);

		if (resultVO == null) {
			return null;
		}
		Vector v = resultVO.getRefData();
		if (isQueryRefColumn) {
			setReftableVO2Cache(resultVO.getRefTableVO(), getPk_corp());
		}

		return v;
	}

	protected RefQueryVO getRefQueryVO() {
		boolean isQueryRefColumn = !modelHandler
				.isReftableVOCached(getPk_corp());

		queryVO.setDataSourceName(getDataSource());
		queryVO.setRefNodeName(getRefNodeName());
		queryVO.setQuerySql(getRefSql());
		queryVO.setObj(getPara());
		queryVO.setQueryRefColumn(isQueryRefColumn);
		return queryVO;
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		if (m_strRefTitle != null) {
			return m_strRefTitle;
		}

		m_strRefTitle = modelHandler.getRefNodeName_mLang(getRefNodeName());

		if (m_strRefTitle == null) {
			m_strRefTitle = getRefNodeName();

		}
		return m_strRefTitle;
	}

	/**
	 * @param m_pk_user
	 *            Ҫ���õ� m_pk_user��
	 */
	public void setPk_user(String m_pk_user) {
		this.m_pk_user = m_pk_user;
	}

	/*
	 * �������ֶθ�ֵ
	 */
	public void resetFieldName() {
		if (getFieldCode() != null && getTableName() != null
				&& (getFieldName() == null || getFieldName().length == 0)) {

			String[] name = new String[getFieldCode().length];

			for (int i = 0; i < getFieldCode().length; i++) {

				String fieldCode = modelHandler
						.getFieldCodeWithTableName(getFieldCode()[i]);

				String resid = modelHandler.getResID(fieldCode);

				name[i] = modelHandler.getRefMultiLangStr(resid, fieldCode);

			}
			setFieldName(name);
		}
	}

	/**
	 * @return ���� formulaHandler��
	 */
	private FormulaHandler getFormulaHandler() {
		if (formulaHandler == null) {
			formulaHandler = new FormulaHandler();
		}
		return formulaHandler;
	}

	private DefFieldsHandler getDefFieldHandler() {
		if (defFieldHandler == null) {
			defFieldHandler = new DefFieldsHandler();
		}
		return defFieldHandler;
	}

	/**
	 * �����Զ������ݵ�ָ��vecData�е�field�С� �������ڣ�(2005-3-30 16:19:24)
	 * 
	 * @return java.lang.Object[]
	 */
	private void setValuesByFieldName(Vector vecData, Vector newVecData,
			String[] fieldNames) {

		int col = -1;
		for (int i = 0; i < fieldNames.length; i++) {

			col = getFieldIndex(fieldNames[i]);
			Object[] values = (Object[]) newVecData.get(i);
			if (values == null) {
				continue;
			}
			for (int j = 0; j < values.length; j++) {
				Vector vRecord = (Vector) vecData.get(j);
				if (vRecord == null || vRecord.size() == 0 || col < 0
						|| col >= vRecord.size()) {
					continue;
				}

				Object obj = getRefValueVO(vRecord.elementAt(col), values[j]);

				vRecord.setElementAt(obj, col);

			}
		}
	}

	/**
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-5-17</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	private RefValueVO getRefValueVO(Object originValue, Object newValue) {
		if (originValue instanceof RefValueVO) {
			return (RefValueVO) originValue;
		}
		RefValueVO valueVO = new RefValueVO();
		valueVO.setOriginValue(originValue);
		valueVO.setNewValue(newValue);
		return valueVO;
	}

	/**
	 * @return ���� m_pk_user��
	 */
	public String getPk_user() {
		if (m_pk_user == null) {
			m_pk_user = modelHandler.getPk_user();
		}
		return m_pk_user;
	}

	/**
	 * ֻ��Ӳ��ظ��ļ����ࡣ
	 * <p>
	 * <strong>����޸��ˣ�sxj</strong>
	 * <p>
	 * <strong>����޸����ڣ�2006-8-10</strong>
	 * <p>
	 * 
	 * @param
	 * @return void
	 * @exception BusinessException
	 * @since NC5.0
	 */
	public void addChangeListener(ChangeListener l) {

		if (eventListMap.get(l) == null) {
			eListenerList.add(ChangeListener.class, l);
			eventListMap.put(l, l);
		}
	}

	public void removeChangeListener(ChangeListener l) {
		eListenerList.add(ChangeListener.class, l);
		eventListMap.remove(l);
	}

	public ChangeListener[] getChangeListeners() {
		return (ChangeListener[]) eListenerList
				.getListeners(ChangeListener.class);
	}

	public void fireChange() {
		Object[] listeners = eListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	/**
	 * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_strFieldCode;
	}

	/**
	 * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_strFieldName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-31 18:59:51)
	 * 
	 * @return java.lang.String
	 */
	public String getGroupCode() {
		return IRefConst.GROUPCORP;
	}

	/**
	 * �����ֶ���
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_strPkFieldCode;
	}

	/**
	 * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */

	/**
	 * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_strTableName;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-22 13:22:29)
	 * 
	 * @param newFieldCode
	 *            java.lang.String[]
	 */
	public void setFieldCode(java.lang.String[] newFieldCode) {
		getHtCodeIndex().clear();
		m_strFieldCode = newFieldCode;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(01-3-22 13:22:29)
	 * 
	 * @param newFieldName
	 *            java.lang.String[]
	 */
	public void setFieldName(java.lang.String[] newFieldName) {
		m_strFieldName = newFieldName;
	}

	/**
	 * �����ֶ���
	 */
	public void setPkFieldCode(String strPkFieldCode) {
		m_strPkFieldCode = strPkFieldCode;
	}

	/**
	 * @return ���� m_fun_code_DocOpen��
	 */
	public String getFun_code_DocOpen() {
		return m_fun_code_DocOpen;
	}

	/**
	 * @param docOpen
	 *            Ҫ���õ� m_fun_code_DocOpen��
	 */
	public void setFun_code_DocOpen(String docOpen) {
		m_fun_code_DocOpen = docOpen;
	}

	/**
	 * @return ���� m_intFieldType��
	 */
	public int[] getIntFieldType() {
		return m_intFieldType;
	}

	/**
	 * @param fieldType
	 *            Ҫ���õ� m_intFieldType��
	 */
	public void setIntFieldType(int[] fieldType) {
		m_intFieldType = fieldType;
	}

	/**
	 * @return ���� isGroupAssignData��
	 */
	public boolean isGroupAssignData() {
		return isGroupAssignData;
	}

	/**
	 * @param isGroupAssignData
	 *            Ҫ���õ� isGroupAssignData��
	 */
	public void setGroupAssignData(boolean isGroupAssignData) {
		this.isGroupAssignData = isGroupAssignData;
	}

	public String getRefDataCacheKey() {
		String key = refDataCachekeyPreStr
				+ getDataPowerSqlKey(getTableName(), getRefNodeName());

		return key;
	}

	public String getFieldShowName(String fieldCode) {
		int index = getFieldIndex(fieldCode);
		if (index < 0 || index > getFieldName().length - 1) {
			return null;
		} else {
			return getFieldName()[index];
		}
	}

	/**
	 * @return ���� ddReaderVO��
	 */
	private DDReaderVO getDdReaderVO() {
		return ddReaderVO;
	}

	/**
	 * @param ddReaderVO
	 *            Ҫ���õ� ddReaderVO��
	 */
	public void setDdReaderVO(String tableName, String[] fieldnames) {
		if (ddReaderVO == null) {
			ddReaderVO = new DDReaderVO();
		}
		ddReaderVO.setTableName(tableName);
		ddReaderVO.setFieldNames(fieldnames);
	}

	/**
	 * @return ���� resourceID��
	 */
	public int getResourceID() {
		return resourceID;
	}

	/**
	 * @param resourceID
	 *            Ҫ���õ� resourceID��
	 */
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	/**
	 * @return ���� isPKMatch��
	 */
	public boolean isPKMatch() {
		return isPKMatch;
	}

	/**
	 * @param isPKMatch
	 *            Ҫ���õ� isPKMatch��
	 */
	public void setPKMatch(boolean isPKMatch) {
		this.isPKMatch = isPKMatch;
	}

	/**
	 * @return ���� isIncludeBlurPart��
	 */
	public boolean isIncludeBlurPart() {
		return isIncludeBlurPart;
	}

	/**
	 * @param isIncludeBlurPart
	 *            Ҫ���õ� isIncludeBlurPart��
	 */
	public void setIncludeBlurPart(boolean isIncludeBlurPart) {
		this.isIncludeBlurPart = isIncludeBlurPart;
	}

	/**
	 * @return ���� envWherePart��
	 */
	private String getEnvWherePart() {
		return envWherePart;
	}

	/**
	 * ��������ʱ��Ҫ���ò��յĻ��������ķ���ȥƴ�����������޷����ݻ��������ı仯���仯�� ���磺
	 * 
	 * @param envWherePart
	 *            Ҫ���õ� envWherePart��
	 */
	public void setEnvWherePart(String envWherePart) {
		this.envWherePart = envWherePart;
	}

	/**
	 * 
	 */
	public void addEnvWherePart(StringBuffer whereClause) {
		// setpk ,������������
		if (isPKMatch()) {
			return;
		}
		String wherePart = getEnvWherePart();
		if (wherePart != null) {

			whereClause.append(" and (").append(wherePart).append(") ");

		}

	}

	/**
	 * @return ���� bdinfoVO��
	 */
	public BdinfoVO getBdinfoVO() {
		if (bdinfoVO == null && !isInitBDinfoVO) {
			bdinfoVO = modelHandler.getBdInfoVObyName(getRefNodeName());
			isInitBDinfoVO = true;
		}
		return bdinfoVO;
	}

	/**
	 * @param bdinfoVO
	 *            Ҫ���õ� bdinfoVO��
	 */
	public void setBdinfoVO(BdinfoVO bdinfoVO) {
		this.bdinfoVO = bdinfoVO;
		isInitBDinfoVO = true;
	}

	/**
	 * @return Returns the isChangeTableSeq.
	 */
	public boolean isChangeTableSeq() {
		return isChangeTableSeq;
	}

	/**
	 * @param isChangeTableSeq
	 *            The isChangeTableSeq to set.
	 */
	public void setChangeTableSeq(boolean isChangeTableSeq) {
		this.isChangeTableSeq = isChangeTableSeq;
	}

	/**
	 * @return ���� isMaintainButtonEnabled��
	 */
	public boolean isMaintainButtonEnabled() {
		return isMaintainButtonEnabled;
	}

	/**
	 * @param isMaintainButtonEnabled
	 *            Ҫ���õ� isMaintainButtonEnabled��
	 */
	public void setMaintainButtonEnabled(boolean isMaintainButtonEnabled) {
		this.isMaintainButtonEnabled = isMaintainButtonEnabled;
	}

	/**
	 * @param dataPowerPK_column
	 *            Ҫ���õ� dataPowerPK_column��
	 */
	public void setDataPowerResourceColumn(String dataPowerResourceColumn) {
		this.dataPowerResourceColumn = dataPowerResourceColumn;
	}

	/**
	 * @return ���� refCardInfoComponentImplClass��
	 */
	public String getRefCardInfoComponentImplClass() {
		return refCardInfoComponentImplClass;
	}

	/**
	 * @param refCardInfoComponentImplClass
	 *            Ҫ���õ� refCardInfoComponentImplClass��
	 */
	public void setRefCardInfoComponentImplClass(
			String refCardInfoComponentImplClass) {
		this.refCardInfoComponentImplClass = refCardInfoComponentImplClass;
	}

	public String getRefShowNameField() {
		return refShowNameField == null ? getRefNameField() : refShowNameField;
	}

	public void setRefShowNameField(String refShowNameField) {
		this.refShowNameField = refShowNameField;
	}

	// ��ʼ���Զ������ֶ�
	// defFields.length==0��ʾ��ʼ�����ˡ�
	public String[] getDefFields() {
		return defFields;
	}

	/**
	 * ��Ŀ��ʾ���Զ�����
	 * 
	 * @return
	 */
	public String[] getShowDefFields() {
		String[] defFieldNames = null;
		if (getDefFields() == null || getDefFields().length == 0) {
			return defFieldNames;
		}

		Map<String, RefcolumnVO> map = modelHandler.getRefColumnVOsMap();

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < getDefFields().length; i++) {
			if (map.get(getDefFields()[i]) != null
					&& map.get(getDefFields()[i]).getIscolumnshow()
							.booleanValue()) {
				list.add(getDefFields()[i]);
			}
		}

		if (list.size() > 0) {
			String[] fieldNames = new String[list.size()];
			list.toArray(fieldNames);
			defFieldNames = fieldNames;
		}

		return defFieldNames;
	}

	public void setDefFields(String[] defFields) {
		this.defFields = defFields;
	}

	public Map<String, Integer> getAlignMap() {
		return alignMap;
	}

	private void setAlignMap(Map<String, Integer> alignMap) {
		this.alignMap = alignMap;
	}

	/**
	 * ��Ŀ���뷽ʽ
	 * 
	 * @param fieldName
	 *            ��Ŀ���� Ҫ������У�����Ҫ��fieldCodes�е�һ�£�
	 * @param alignment
	 *            ���뷽ʽ ������SwingConstants������ SwingConstants.RIGHT
	 */
	public void addFieldAlignment(String fieldName, int alignment) {
		if (getAlignMap() == null) {
			setAlignMap(new HashMap<String, Integer>());
		}
		getAlignMap().put(fieldName, new Integer(alignment));
	}

	public boolean canUseDB() {
		return modelHandler.canUseDB();
	}

	public Vector getCacheValue(String sql) {
		return modelHandler.getCacheValue(sql);
	}

	public Vector queryMain(String dsName, String sql) {
		return modelHandler.queryMain(dsName, sql);
	}

	public void removeCacheValue(String sql) {
		modelHandler.removeCache(sql);
	}

	public void setCacheValue(String sql, Vector value) {
		modelHandler.setCacheValue(sql, value);
	}

	public String getCodeRuleFromPara(String orgTypeCode, String pk_GlOrgBook,
			String codingRule, UFBoolean boolean1) {
		return modelHandler.getCodeRuleFromPara(orgTypeCode, pk_GlOrgBook,
				codingRule, boolean1);
	}

	public String getCodeRuleFromPara(String codingRule) {
		return modelHandler.getCodeRuleFromPara(codingRule);
	}

	public String getNumberCodingRule(String codingRule) {
		return modelHandler.getNumberCodingRule(codingRule);
	}

	public String[] getDefFields(String[] fieldCode) {
		return modelHandler.getDefFields(fieldCode);
	}

	public String getPara1() {
		return para1;
	}

	public void setPara1(String para1) {
		this.para1 = para1;
	}

	public String getPara2() {
		return para2;
	}

	public void setPara2(String para2) {
		this.para2 = para2;
	}

	public String getPara3() {
		return para3;
	}

	public void setPara3(String para3) {
		this.para3 = para3;
	}

	protected IRefModelHandler getModelHandler() {
		return modelHandler;
	}

	public String getUiControlComponentClassName() {
		return uiControlComponentClassName;
	}

	public void setUiControlComponentClassName(
			String uiControlComponentClassName) {
		this.uiControlComponentClassName = uiControlComponentClassName;
	}

	/**
	 * �����������仯ʱ���ø÷�����������ʵ�־���ҵ���߼���
	 * 
	 * @param eventData
	 *            ��ʱ���ط����仯���������
	 */
	public void UiControlClassValueChanged(UIControlClassEvent event) {

	}

	public String getRefDocEditClassName() {
		return refDocEditClassName;
	}

	public void setRefDocEditClassName(String refDocEditClassName) {
		this.refDocEditClassName = refDocEditClassName;
	}

	public boolean isShowField(String fieldName) {
		boolean isShowField = false;
		int[] showColumns = getShownColumns();
		int index = getFieldIndex(fieldName);
		for (int i = 0; i < showColumns.length; i++) {
			if (showColumns[i] == index) {
				isShowField = true;
				break;
			}
		}
		return isShowField;
	}

	public String[] getShowFields() {
		String[] showFields = new String[getShownColumns().length];
		int fieldCodeLength = getFieldCode().length;
		for (int i = 0; i < showFields.length; i++) {
			int index = getShownColumns()[i];
			if (index < fieldCodeLength) {
				showFields[i] = getFieldCode()[index];
			} else {
				Logger.debug("������Ŀ��Ϣ��������������Ŀ�е����ð�ť���ò���Ϊ��" + getRefNodeName());
			}
		}
		return showFields;
	}

	public boolean isHiddenField(String fieldCode) {

		return hiddenFieldList.contains(fieldCode);
	}

	// ����ʱ�Ƿ��Сд����
	public boolean isCaseSensitve() {
		return isCaseSensitve;
	}

	public void setCaseSensive(boolean isCaseSensitve) {
		this.isCaseSensitve = isCaseSensitve;
	}

	public Vector getAllColumnNames() {

		if (vecAllColumnNames != null) {
			return vecAllColumnNames;
		}

		vecAllColumnNames = new Vector();
		if (getFieldCode() != null && getFieldCode().length > 0) {
			if (getFieldName() == null || getFieldName().length == 0) {
				for (int i = 0; i < getFieldCode().length; i++) {
					vecAllColumnNames.addElement(getFieldCode()[i]);
				}
			} else {
				if (getFieldName().length >= getFieldCode().length) {
					for (int i = 0; i < getFieldCode().length; i++) {
						vecAllColumnNames.addElement(getFieldName()[i]);
					}
				} else {
					for (int i = 0; i < getFieldName().length; i++) {
						vecAllColumnNames.addElement(getFieldName()[i]);
					}
					for (int i = getFieldName().length; i < getFieldCode().length; i++) {
						vecAllColumnNames.addElement(getFieldCode()[i]);
					}
				}
			}

		}

		if (getHiddenFieldCode() != null)
			for (int i = 0; i < getHiddenFieldCode().length; i++) {
				vecAllColumnNames.addElement(getHiddenFieldCode()[i]);
			}

		// ���붯̬��
		if (isDynamicCol()) {

			String[] dynamicColNames = getDynamicFieldNames();
			if (getDynamicFieldNames() != null) {

				for (int i = 0; i < dynamicColNames.length; i++) {

					// ���뵽��ʾ��������
					vecAllColumnNames.addElement(dynamicColNames[i]);
				}
			}
		}
		return vecAllColumnNames;
	}

	protected String getRefCacheSqlKey() {

		return getRefSql() == null ? getClass().getName() : getRefSql();
	}
}