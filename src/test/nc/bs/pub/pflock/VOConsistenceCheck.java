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
 * VO一致性交验类
 * @author fangj 2004-5-13
 * @modifier leijun 2008-12 查询主表VO时，只取TS字段，提高效率
 * @modifier leijun 2008-12 增加单据类型的构造函数 
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
	 * 检查要操作的AggregatedVO是否在数据库中存在,或者是否和当前使用的版本一致(ts),
	 * 如果不存在,throw BusinessException "该单据已经被他人删除，请刷新界面";
	 * 如果ts不一致,throw BusinessException "该单据已经被他人修改，请刷新界面，重做业务"
	 * @throws BusinessException
	 */
	public void checkConsistence() throws BusinessException {
		try {
			if (this.m_vo.getParentVO().getAttributeValue("ts") == null)
				return;

			//从数据库中获取主VO（XXX:只取ts字段）
			String[] tableNameAndPKField = getTableNameAndPKField(m_vo.getParentVO());
			String tableName = tableNameAndPKField[0]; //主表表名和主键字段名
			String pkField = tableNameAndPKField[1];
			String sqlTs = "select ts from " + tableName + " where " + pkField + "='"
					+ m_vo.getParentVO().getPrimaryKey() + "'";
			String tsInDB = queryTs(sqlTs);

			if (tsInDB == null)
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000603")/*@res "该单据已经被他人删除，请刷新界面"*/);
			if (!tsInDB.equals(this.m_vo.getParentVO().getAttributeValue("ts").toString()))
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000604")/*@res "该单据已经被他人修改，请刷新界面，重做业务"*/);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 返回 tableName 和 getPKFieldName
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
				//根据元数据模型信息来获取
				IBean bean = PfMetadataTools.queryMetaOfBilltype(m_billtype);
				resultArray[0] = bean.getTable().getName();
				resultArray[1] = bean.getTable().getPrimaryKeyName();
			} else {
				//从单据VO对照信息中获取主表表名和主键字段名
				VotableVO tableVo = PfDataCache.getBillTypeToVO(m_billtype, true);
				if (tableVo == null)
					throw new PFBusinessException("流程平台：该单据类型" + m_billtype + "没有注册VO对照信息");
				resultArray[0] = tableVo.getVotable();
				resultArray[1] = tableVo.getPkfield();
			}
		}
		return resultArray;
	}

	/**
	 * 检查要操作的AggregatedVO是否在数据库中存在，或者是否和当前使用的版本一致(ts)
	 * 如果不存在，throw BusinessException "该单据已经被他人删除，请刷新界面"
	 * 如果ts不一致，throw BusinessException "该单据已经被他人修改，请刷新界面，重做业务"
	 */
	protected void checkConsistenceAry(CircularlyAccessibleValueObject[] itemVos)
			throws BusinessException {
		if (itemVos == null || itemVos.length == 0)
			return;
		//修改标准产品 ts 校验bug 
		//如果传入的vo对象类型不一样，导致从数据库查询ts会为空 
		//因为默认按对象数组中的第一个对象的类型取表名和pk信息
		//导致从数据库查询ts时查错表  mlr
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
	 * 检查要操作的AggregatedVO是否在数据库中存在，或者是否和当前使用的版本一致(ts)
	 * 如果不存在，throw BusinessException "该单据已经被他人删除，请刷新界面"
	 * 如果ts不一致，throw BusinessException "该单据已经被他人修改，请刷新界面，重做业务"
	 * mlr modify
	 * 
	 * 这个方法必须要求实体对象是一致的
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
			//查询数据库中的时间戳
			String[] tableNameAndPKField = getTableNameAndPKField(itemVos[0]);
			String[] strTemp = getTsSqlStr(pkAry, tableNameAndPKField[0], tableNameAndPKField[1]);
			for (int i = 0; i < strTemp.length; i++) {
				List lResult = getTsChanged(strTemp[i]);
				for (Iterator iter = lResult.iterator(); iter.hasNext();) {
					Object[] objs = (Object[]) iter.next();
					newTsMap.put(objs[0], objs[1]);
				}
			}
			//进行比较
			Iterator e = oldTsMap.keySet().iterator();
			while (e.hasNext()) {
				Object key = e.next();
				String strOldTs = String.valueOf(oldTsMap.get(key));
				String strNewTs = (String) newTsMap.get(key);

				if (!strOldTs.equals(strNewTs))
					throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000604")/*@res "该单据已经被他人修改，请刷新界面，重做业务"*/);
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
	 * 获得时间戳的SQL语句。
	 * @return java.lang.String[]
	 */
	private static String[] getSplitSqlIn(ArrayList pkAry) {
		/**-------------------增加了拆分功能-----------------*/
		int PACKAGESIZE = 400;
		int fixedPKLength = pkAry.size();
		int numofPackage = fixedPKLength / PACKAGESIZE + (fixedPKLength % PACKAGESIZE > 0 ? 1 : 0);
		/**-------------------------------------------------*/

		/**根据包的个数组成SQL数组*/
		String[] strTemp = new String[numofPackage];

		/**根据包的个数循环构建numofPackage个SQL数组*/
		for (int s = 0; s < numofPackage; s++) { //for1
			String strArys = "";
			/**确定起始点和中止点*/
			int beginIndex = s * PACKAGESIZE;
			int endindex = beginIndex + PACKAGESIZE;
			/**长度超出后进行微调*/
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
	 * 获得时间戳的SQL语句。
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