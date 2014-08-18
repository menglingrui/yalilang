package nc.bs.pub.pflock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pf.pub.PfDataCache;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.model.IBean;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * VOһ���Խ�����
 * @author fangj 2004-5-13
 * @modifier leijun 2008-12 ��ѯ����VOʱ��ֻȡTS�ֶΣ����Ч��
 * @modifier leijun 2008-12 ���ӵ������͵Ĺ��캯�� 
 */
public class VOConsistenceCheck implements IConsistenceCheck {
	protected AggregatedValueObject m_vo = null;

	protected String m_billtype = null;

	public VOConsistenceCheck(AggregatedValueObject vo) {
		super();
		this.m_vo = vo;
	}

	public VOConsistenceCheck(AggregatedValueObject vo, String billtype) {
		super();
		this.m_vo = vo;
		this.m_billtype = billtype;
	}

	/**
	 * ���Ҫ������AggregatedVO�Ƿ������ݿ��д���,�����Ƿ�͵�ǰʹ�õİ汾һ��(ts),
	 * ���������,throw BusinessException "�õ����Ѿ�������ɾ������ˢ�½���";
	 * ���ts��һ��,throw BusinessException "�õ����Ѿ��������޸ģ���ˢ�½��棬����ҵ��"
	 * @throws BusinessException
	 */
	public void checkConsistence() throws BusinessException {
		try {
			if (this.m_vo.getParentVO().getAttributeValue("ts") == null)
				return;

			//�����ݿ��л�ȡ��VO��XXX:ֻȡts�ֶΣ�
			String[] tableNameAndPKField = getTableNameAndPKField(m_vo.getParentVO());
			String tableName = tableNameAndPKField[0]; //��������������ֶ���
			String pkField = tableNameAndPKField[1];
			String sqlTs = "select ts from " + tableName + " where " + pkField + "='"
					+ m_vo.getParentVO().getPrimaryKey() + "'";
			String tsInDB = queryTs(sqlTs);

			if (tsInDB == null)
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000603")/*@res "�õ����Ѿ�������ɾ������ˢ�½���"*/);
			if (!tsInDB.equals(this.m_vo.getParentVO().getAttributeValue("ts").toString()))
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000604")/*@res "�õ����Ѿ��������޸ģ���ˢ�½��棬����ҵ��"*/);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * ���� tableName �� getPKFieldName
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	private String[] getTableNameAndPKField(CircularlyAccessibleValueObject vo) throws BusinessException {

		String[] resultArray = new String[2];
		if (vo instanceof SuperVO) {
			resultArray[0] = ((SuperVO) vo).getTableName();
			resultArray[1] = ((SuperVO) vo).getPKFieldName();
		} else {
			if (PfMetadataTools.isBilltypeRelatedMeta(m_billtype)) {
				//����Ԫ����ģ����Ϣ����ȡ
				IBean bean = PfMetadataTools.queryMetaOfBilltype(m_billtype);
				resultArray[0] = bean.getTable().getName();
				resultArray[1] = bean.getTable().getPrimaryKeyName();
			} else {
				//�ӵ���VO������Ϣ�л�ȡ��������������ֶ���
				VotableVO tableVo = PfDataCache.getBillTypeToVO(m_billtype, true);
				if (tableVo == null)
					throw new PFBusinessException("����ƽ̨���õ�������" + m_billtype + "û��ע��VO������Ϣ");
				resultArray[0] = tableVo.getVotable();
				resultArray[1] = tableVo.getPkfield();
			}
		}
		return resultArray;
	}

	/**
	 * ���Ҫ������AggregatedVO�Ƿ������ݿ��д��ڣ������Ƿ�͵�ǰʹ�õİ汾һ��(ts)
	 * ��������ڣ�throw BusinessException "�õ����Ѿ�������ɾ������ˢ�½���"
	 * ���ts��һ�£�throw BusinessException "�õ����Ѿ��������޸ģ���ˢ�½��棬����ҵ��"
	 */
	protected void checkConsistenceAry(CircularlyAccessibleValueObject[] itemVos)
			throws BusinessException {
		if (itemVos == null || itemVos.length == 0)
			return;
		//�޸ı�׼��Ʒ ts У��bug 
		//��������vo�������Ͳ�һ�������´����ݿ��ѯts��Ϊ�� 
		//��ΪĬ�ϰ����������еĵ�һ�����������ȡ������pk��Ϣ
		//���´����ݿ��ѯtsʱ����  mlr
		Map<String,List<CircularlyAccessibleValueObject>> map=new HashMap<String,List<CircularlyAccessibleValueObject>>();
		for(int i=0;i<itemVos.length;i++){
			String classname=itemVos[i].getClass().getName();
			if(map.get(classname)==null){
				List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
				list.add(itemVos[i]);
				map.put(classname, list);
			}else{
				map.get(classname).add(itemVos[i]);
			}
		}
		for(String classname:map.keySet()){
			List<CircularlyAccessibleValueObject> list=map.get(classname);
			if(list!=null && list.size()>0){
				CircularlyAccessibleValueObject[] vos=list.toArray((CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), list.size()));
				checkConsistenceAry1(vos);
			}
		}	
	}
	/**
	 * ���Ҫ������AggregatedVO�Ƿ������ݿ��д��ڣ������Ƿ�͵�ǰʹ�õİ汾һ��(ts)
	 * ��������ڣ�throw BusinessException "�õ����Ѿ�������ɾ������ˢ�½���"
	 * ���ts��һ�£�throw BusinessException "�õ����Ѿ��������޸ģ���ˢ�½��棬����ҵ��"
	 * mlr modify
	 * 
	 * �����������Ҫ��ʵ�������һ�µ�
	 */
	private void checkConsistenceAry1(CircularlyAccessibleValueObject[] itemVos)
			throws BusinessException {
		if (itemVos == null || itemVos.length == 0)
			return;
		try {
			ArrayList pkAry = new ArrayList();
			HashMap oldTsMap = new HashMap();
			HashMap newTsMap = new HashMap();
			for (int i = 0; i < itemVos.length; i++) {
				CircularlyAccessibleValueObject itemVo = itemVos[i];
				Object tsValue = itemVo.getAttributeValue("ts");
				if (tsValue != null && !StringUtil.isEmptyWithTrim(itemVo.getPrimaryKey())) {
					pkAry.add(itemVo.getPrimaryKey());
					oldTsMap.put(itemVo.getPrimaryKey(), tsValue);
				}
			}
			//��ѯ���ݿ��е�ʱ���
			String[] tableNameAndPKField = getTableNameAndPKField(itemVos[0]);
			String[] strTemp = getTsSqlStr(pkAry, tableNameAndPKField[0], tableNameAndPKField[1]);
			for (int i = 0; i < strTemp.length; i++) {
				List lResult = getTsChanged(strTemp[i]);
				for (Iterator iter = lResult.iterator(); iter.hasNext();) {
					Object[] objs = (Object[]) iter.next();
					newTsMap.put(objs[0], objs[1]);
				}
			}
			//���бȽ�
			Iterator e = oldTsMap.keySet().iterator();
			while (e.hasNext()) {
				Object key = e.next();
				String strOldTs = String.valueOf(oldTsMap.get(key));
				String strNewTs = (String) newTsMap.get(key);

				if (!strOldTs.equals(strNewTs))
					throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000604")/*@res "�õ����Ѿ��������޸ģ���ˢ�½��棬����ҵ��"*/);
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	private String queryTs(String strTsSql) throws DbException {
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			Object objTs = jdbc.executeQuery(strTsSql, new ColumnProcessor());
			return objTs == null ? null : objTs.toString();
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * @param strSql
	 * @return
	 * @throws DbException
	 */
	private List getTsChanged(String strSql) throws DbException {
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			List lRet = (List) jdbc.executeQuery(strSql, new ArrayListProcessor());
			return lRet;
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ���ʱ�����SQL��䡣
	 * @return java.lang.String[]
	 */
	private static String[] getSplitSqlIn(ArrayList pkAry) {
		/**-------------------�����˲�ֹ���-----------------*/
		int PACKAGESIZE = 400;
		int fixedPKLength = pkAry.size();
		int numofPackage = fixedPKLength / PACKAGESIZE + (fixedPKLength % PACKAGESIZE > 0 ? 1 : 0);
		/**-------------------------------------------------*/

		/**���ݰ��ĸ������SQL����*/
		String[] strTemp = new String[numofPackage];

		/**���ݰ��ĸ���ѭ������numofPackage��SQL����*/
		for (int s = 0; s < numofPackage; s++) { //for1
			String strArys = "";
			/**ȷ����ʼ�����ֹ��*/
			int beginIndex = s * PACKAGESIZE;
			int endindex = beginIndex + PACKAGESIZE;
			/**���ȳ��������΢��*/
			if (endindex > fixedPKLength)
				endindex = fixedPKLength;

			for (int i = beginIndex; i < endindex; i++) { //for2
				strArys += "'" + pkAry.get(i) + "',";
			} //for2

			strArys = "(" + strArys.substring(0, strArys.length() - 1) + ")";
			strTemp[s] = strArys;

		} //for1
		return strTemp;
	}

	/**
	 * ���ʱ�����SQL��䡣
	 * @return java.lang.String[]
	 */
	private String[] getTsSqlStr(ArrayList pkAry, String tableName, String fieldName) {
		String[] strTemp = getSplitSqlIn(pkAry);

		for (int i = 0; i < strTemp.length; i++) {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select ");
			sqlBuffer.append(fieldName);
			sqlBuffer.append(",ts from ");
			sqlBuffer.append(tableName);
			sqlBuffer.append(" where ");
			sqlBuffer.append(fieldName);
			sqlBuffer.append(" in ");
			sqlBuffer.append(strTemp[i]);
			strTemp[i] = sqlBuffer.toString();
		}
		return strTemp;
	}
}