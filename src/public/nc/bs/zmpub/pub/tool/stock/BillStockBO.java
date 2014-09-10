package nc.bs.zmpub.pub.tool.stock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zmpub.pub.report2.CombinVO;
/**
 * 是对现存量更新类的扩展 针对 业务单据更新现存量的操作
 * @author mlr
 */
public abstract class BillStockBO extends StockBO {
	/**
	 * 获得继承类的名字
	 */
	public abstract String getThisClassName();

	/**
	 * 
	 */
	private static final long serialVersionUID = 3404341684522482080L;

	// 抽象方法的扩展开始
	/**
	 * 单据类型 与 数据交换类 对应map
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, String> getTypetoChangeClass() throws Exception;

	/**
	 * 获得各个单据类型 对应的 现存量设置规则
	 * @return Map<String,boolean[]>
	 * @throws Exception
	 */

	public abstract Map<String, UFBoolean[]> getTypetosetnum() throws Exception;

	// 抽象方法的扩展结束
	/**
	 * 业务单据更新现存量 传来的是业务单据的数据 
	 * @param vos
	 *            业务单据数据
	 * @param pk_billtype
	 *            更新现存量单据类型
	 * @throws Exception
	 */

	public void updateStockByBill(SuperVO[] vos, String pk_billtype)
			throws Exception {
		Map<String, String> map = getTypetoChangeClass();
		if (map == null || map.size() == 0)
			throw new Exception("没有注册单据类型->现存量的数据交换类");
		String className = map.get(pk_billtype);
		if (className == null || className.length() == 0)
			throw new Exception(" 单据类型为:" + pk_billtype + " 没有注册交换类");
		String changeClName = getClassName();
		if (changeClName == null || changeClName.length() == 0)
			throw new Exception("没有注册现存量实现类的全路径");
		Class cl = Class.forName(changeClName);
		SuperVO[] numvos = SingleVOChangeDataBsTool.runChangeVOAry(vos, cl,
				className);
		if (numvos == null || numvos.length == 0) {
			return;
		}
		setAccountNumChange(numvos, pk_billtype);
		updateStock(numvos);
	}
	/**
	 * 业务单据更新现存量 传来的是业务单据的数据
	 * @param vos
	 *            业务单据数据
	 * @param pk_billtype
	 *            更新现存量单据类型
	 * @throws Exception
	 */
	public void updateStockByBillForDel(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if(vos!=null&&vos.getChildrenVO()!=null&&vos.getChildrenVO().length>0){
			Map<String, String> map = getTypetoChangeClass();
			if (map == null || map.size() == 0)
				throw new Exception("没有注册单据类型->现存量的数据交换类");
			String className = map.get(pk_billtype);
			if (className == null || className.length() == 0)
				throw new Exception(" 单据类型为:" + pk_billtype + " 没有注册交换类");
			String changeClName = getClassName();
			if (changeClName == null || changeClName.length() == 0)
				throw new Exception("没有注册现存量实现类的全路径");
			// 处理单据修改操 修改行数据 处理单据修改操 删行数据
			dealModUpdate(vos, pk_billtype);
			dealModDelete(vos, pk_billtype);
			Class cl = Class.forName(changeClName);
			// 过滤掉删除行
			AggregatedValueObject billvo = filterDelAndUpdate(vos);
			SuperVO[] numvos = SingleVOChangeDataBsTool.runChangeVOAry(billvo, cl,
					className);
			if (numvos == null || numvos.length == 0) {
				return;
			}
			setAccountNumChange(numvos, pk_billtype);
			updateStock(numvos);
		}	
	}

	/**
	 * 业务单据更新现存量 传来的是业务单据的数据
	 * @param vos
	 *            业务单据数据
	 * @param pk_billtype
	 *            更新现存量单据类型
	 * @throws Exception
	 */
	public void updateStockByBillForSave(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if(vos!=null&&vos.getChildrenVO()!=null&&vos.getChildrenVO().length>0){
			Map<String, String> map = getTypetoChangeClass();
			if (map == null || map.size() == 0)
				throw new Exception("没有注册单据类型->现存量的数据交换类");
			String className = map.get(pk_billtype);
			if (className == null || className.length() == 0)
				throw new Exception(" 单据类型为:" + pk_billtype + " 没有注册交换类");
			String changeClName = getClassName();
			if (changeClName == null || changeClName.length() == 0)
				throw new Exception("没有注册现存量实现类的全路径");
			// 处理单据修改操 修改行数据 处理单据修改操 删行数据
			dealModUpdate(vos, pk_billtype);
			dealModDelete(vos, pk_billtype);
			Class cl = Class.forName(changeClName);
			// 过滤掉删除行
			AggregatedValueObject billvo = filterDelAndUpdate(vos);
			SuperVO[] numvos = SingleVOChangeDataBsTool.runChangeVOAry(billvo, cl,
					className);
			if (numvos == null || numvos.length == 0) {
				return;
			}
			setAccountNumChange(numvos, pk_billtype);
			updateStock(numvos);
		}	
	}
	
	/**
	 * 增加返回现存量提示信息，主要是如果现存量为负数时会返回提示
	 * 业务单据更新现存量 传来的是业务单据的数据  
	 * @param vos
	 *            业务单据数据
	 * @param pk_billtype
	 *            更新现存量单据类型
	 * @throws Exception
	 */
	public String updateStockByBillForSave1(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if(vos!=null&&vos.getChildrenVO()!=null&&vos.getChildrenVO().length>0){
			Map<String, String> map = getTypetoChangeClass();
			if (map == null || map.size() == 0)
				throw new Exception("没有注册单据类型->现存量的数据交换类");
			String className = map.get(pk_billtype);
			if (className == null || className.length() == 0)
				throw new Exception(" 单据类型为:" + pk_billtype + " 没有注册交换类");
			String changeClName = getClassName();
			if (changeClName == null || changeClName.length() == 0)
				throw new Exception("没有注册现存量实现类的全路径");
			// 处理单据修改操 修改行数据 处理单据修改操 删行数据
			String dupstring=dealModUpdate1(vos, pk_billtype);
			String dmpstring=dealModDelete1(vos, pk_billtype);
			Class cl = Class.forName(changeClName);
			// 过滤掉删除行
			AggregatedValueObject billvo = filterDelAndUpdate(vos);
			SuperVO[] numvos = SingleVOChangeDataBsTool.runChangeVOAry(billvo, cl,
					className);
			if (numvos == null || numvos.length == 0) {
				return dupstring;
			}
			setAccountNumChange(numvos, pk_billtype);
			return updateStock1(numvos)+dmpstring;
		}
		return null;
	}
	public String dealModDelete1(AggregatedValueObject vos, String pk_billtype) throws Exception {
		if (vos.getParentVO() == null) {
			throw new Exception("单据表头为空");
		}
		if (vos.getChildrenVO() == null || vos.getChildrenVO().length == 0)
			return null;
		SuperVO[] bodys = (SuperVO[]) ObjectUtils.serializableClone(vos
				.getChildrenVO());
		// 存放修改 删除的vo
		List<SuperVO> list = new ArrayList<SuperVO>();
		for (int i = 0; i < bodys.length; i++) {
			if (bodys[i].getStatus() == VOStatus.DELETED) {
				list.add(bodys[i]);
			}
		}
		if (list == null || list.size() == 0)
			return null;

		Map<String, String> map = getTypetoChangeClass();
		String changeClName = getClassName();
		Class cl = Class.forName(changeClName);
		String className = map.get(pk_billtype);
		SuperVO headVo = (SuperVO) ObjectUtils.serializableClone(vos
				.getParentVO());		
		//add mlr for modifysave start
		AggregatedValueObject nbillvo = new HYBillVO();
		nbillvo.setParentVO(headVo);
		SuperVO[] novos = (SuperVO[]) java.lang.reflect.Array.newInstance(list
				.get(0).getClass(), list.size());
		for(int i=0;i<list.size();i++){
			novos[i]=(SuperVO) ObjectUtils.serializableClone(list.get(i));
		}
		nbillvo.setChildrenVO(novos);	
		SuperVO[] nnumvos = SingleVOChangeDataBsTool.runChangeVOAry(nbillvo, cl,
				className);
		//add mlr for modifysave  end
		
		if (getChangeNums() == null || getChangeNums().length == 0)
			throw new Exception("没有注册现存量变化字段");
		for (int i = 0; i < nnumvos.length; i++) {
			for (int j = 0; j < getChangeNums().length; j++) {
					UFDouble nuf=PuPubVO.getUFDouble_NullAsZero(nnumvos[i]
                        .getAttributeValue(getChangeNums()[j]));
				nnumvos[i].setAttributeValue(getChangeNums()[j], new UFDouble(0)
						.sub(nuf));
			}
		}
		setAccountNumChange(nnumvos, pk_billtype);
		return updateStock1(nnumvos);
	}

	public void dealModDelete(AggregatedValueObject vos, String pk_billtype) throws Exception {
		if (vos.getParentVO() == null) {
			throw new Exception("单据表头为空");
		}
		if (vos.getChildrenVO() == null || vos.getChildrenVO().length == 0)
			return;
		SuperVO[] bodys = (SuperVO[]) ObjectUtils.serializableClone(vos
				.getChildrenVO());
		// 存放修改 删除的vo
		List<SuperVO> list = new ArrayList<SuperVO>();
		for (int i = 0; i < bodys.length; i++) {
			if (bodys[i].getStatus() == VOStatus.DELETED) {
				list.add(bodys[i]);
			}
		}
		if (list == null || list.size() == 0)
			return;

		Map<String, String> map = getTypetoChangeClass();
		String changeClName = getClassName();
		Class cl = Class.forName(changeClName);
		String className = map.get(pk_billtype);
		SuperVO headVo = (SuperVO) ObjectUtils.serializableClone(vos
				.getParentVO());		
		//add mlr for modifysave start
		AggregatedValueObject nbillvo = new HYBillVO();
		nbillvo.setParentVO(headVo);
		SuperVO[] novos = (SuperVO[]) java.lang.reflect.Array.newInstance(list
				.get(0).getClass(), list.size());
		for(int i=0;i<list.size();i++){
			novos[i]=(SuperVO) ObjectUtils.serializableClone(list.get(i));
		}
		nbillvo.setChildrenVO(novos);	
		SuperVO[] nnumvos = SingleVOChangeDataBsTool.runChangeVOAry(nbillvo, cl,
				className);
		//add mlr for modifysave  end
		
		if (getChangeNums() == null || getChangeNums().length == 0)
			throw new Exception("没有注册现存量变化字段");
		for (int i = 0; i < nnumvos.length; i++) {
			for (int j = 0; j < getChangeNums().length; j++) {
					UFDouble nuf=PuPubVO.getUFDouble_NullAsZero(nnumvos[i]
                        .getAttributeValue(getChangeNums()[j]));
				nnumvos[i].setAttributeValue(getChangeNums()[j], new UFDouble(0)
						.sub(nuf));
			}
		}
		setAccountNumChange(nnumvos, pk_billtype);
		updateStock(nnumvos);
	}

	/**
	 * 业务单据更新现存量 传来的是业务单据的数据
	 * @param vos
	 *            业务单据数据
	 * @param pk_billtype
	 *            更新现存量单据类型
	 * @throws Exception
	 */
	public void updateStockByBillForDelete(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if(vos!=null&&vos.getChildrenVO()!=null&&vos.getChildrenVO().length>0){
		     for(int i=0;i<vos.getChildrenVO().length;i++){
		    	 CircularlyAccessibleValueObject vo=  vos.getChildrenVO()[i];
		    	 if(vo!=null){
		    		 vo.setStatus(VOStatus.DELETED);
		    	 }
		     }
		 	updateStockByBillForSave( vos,  pk_billtype);
		}
	}
	
	/**
	 * 业务单据更新现存量 传来的是业务单据的数据
	 * @param vos
	 *            业务单据数据
	 * @param pk_billtype
	 *            更新现存量单据类型
	 * @throws Exception
	 */
	public void updateStockByBillForDelete1(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if(vos!=null&&vos.getChildrenVO()!=null&&vos.getChildrenVO().length>0){
		     for(int i=0;i<vos.getChildrenVO().length;i++){
		    	 CircularlyAccessibleValueObject vo=  vos.getChildrenVO()[i];
		    	 if(vo!=null){
		    		 vo.setStatus(VOStatus.DELETED);
		    	 }
		     }
		 	updateStockByBillForSave1( vos,  pk_billtype);
		}
	}
	
	/**
	 * 过滤掉删除行
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-28下午05:20:24
	 */
	private AggregatedValueObject filterDelAndUpdate(AggregatedValueObject vos)
			throws Exception {
		if (vos == null || vos.getParentVO() == null
				|| vos.getChildrenVO() == null
				|| vos.getChildrenVO().length == 0)
			return vos;
		AggregatedValueObject billvo = (AggregatedValueObject) ObjectUtils
				.serializableClone(vos);
		SuperVO[] bvos = (SuperVO[]) billvo.getChildrenVO();
		List<SuperVO> list = new ArrayList<SuperVO>();
		for (int i = 0; i < bvos.length; i++) {
			if (bvos[i].getStatus() == VOStatus.NEW) {
				list.add(bvos[i]);
			}
		}
		billvo.setChildrenVO(list.toArray((SuperVO[]) java.lang.reflect.Array
				.newInstance(bvos[0].getClass(), 0)));
		return billvo;
	}
	/**
	 * 处理单据修改操作 修改行数据 处理单据修改操作 删行数据
	 * @param pk_billtype
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-28下午12:39:13
	 */
	private String dealModUpdate1(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if (vos.getParentVO() == null) {
			throw new Exception("单据表头为空");
		}
		if (vos.getChildrenVO() == null || vos.getChildrenVO().length == 0)
			return null;
		SuperVO[] bodys = (SuperVO[]) ObjectUtils.serializableClone(vos
				.getChildrenVO());
		// 存放修改 删除的vo
		List<SuperVO> list = new ArrayList<SuperVO>();
		for (int i = 0; i < bodys.length; i++) {
			if (bodys[i].getStatus() == VOStatus.UPDATED) {
				list.add(bodys[i]);
			}
		}
		if (list == null || list.size() == 0)
			return null;
		// 存放修改删除的记录的 数据库对应记录 vo
		SuperVO[] ovos = (SuperVO[]) java.lang.reflect.Array.newInstance(list
				.get(0).getClass(), list.size());
		for (int i = 0; i < list.size(); i++) {
			List li = (List) getDao().retrieveByClause(
					list.get(0).getClass(),
					list.get(0).getPKFieldName() + " = '"
							+ list.get(i).getPrimaryKey() + "'");
			if (li != null && li.size() != 0)
				ovos[i] = (SuperVO) li.get(0);
		}
		Map<String, String> map = getTypetoChangeClass();
		String changeClName = getClassName();
		Class cl = Class.forName(changeClName);
		String className = map.get(pk_billtype);
		SuperVO headVo = (SuperVO) ObjectUtils.serializableClone(vos
				.getParentVO());
		SuperVO[] bodyVos = (SuperVO[]) ObjectUtils.serializableClone(ovos);
		AggregatedValueObject billvo = new HYBillVO();
		billvo.setParentVO(headVo);
		billvo.setChildrenVO(bodyVos);
		
		
		SuperVO[] numvos = SingleVOChangeDataBsTool.runChangeVOAry(billvo, cl,
				className);
		
		//add mlr for modifysave end
		AggregatedValueObject nbillvo = new HYBillVO();
		nbillvo.setParentVO(headVo);
		SuperVO[] novos = (SuperVO[]) java.lang.reflect.Array.newInstance(list
				.get(0).getClass(), list.size());
		for(int i=0;i<list.size();i++){
			novos[i]=(SuperVO) ObjectUtils.serializableClone(list.get(i));
		}
		nbillvo.setChildrenVO(novos);	
		SuperVO[] nnumvos = SingleVOChangeDataBsTool.runChangeVOAry(nbillvo, cl,
				className);
		//add mlr for modifysave  end
		
		if (getChangeNums() == null || getChangeNums().length == 0)
			throw new Exception("没有注册现存量变化字段");
		for (int i = 0; i < numvos.length; i++) {
			for (int j = 0; j < getChangeNums().length; j++) {
				UFDouble uf = PuPubVO.getUFDouble_NullAsZero(numvos[i]
						.getAttributeValue(getChangeNums()[j]));
				UFDouble nuf=PuPubVO.getUFDouble_NullAsZero(nnumvos[i]
                        .getAttributeValue(getChangeNums()[j]));
				numvos[i].setAttributeValue(getChangeNums()[j], nuf
						.sub(uf));
			}
		}
		setAccountNumChange(numvos, pk_billtype);
		return updateStock1(numvos);
	}
	/**
	 * 处理单据修改操作 修改行数据 处理单据修改操作 删行数据
	 * @param pk_billtype
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-28下午12:39:13
	 */
	private void dealModUpdate(AggregatedValueObject vos, String pk_billtype)
			throws Exception {
		if (vos.getParentVO() == null) {
			throw new Exception("单据表头为空");
		}
		if (vos.getChildrenVO() == null || vos.getChildrenVO().length == 0)
			return;
		SuperVO[] bodys = (SuperVO[]) ObjectUtils.serializableClone(vos
				.getChildrenVO());
		// 存放修改 删除的vo
		List<SuperVO> list = new ArrayList<SuperVO>();
		for (int i = 0; i < bodys.length; i++) {
			if (bodys[i].getStatus() == VOStatus.UPDATED) {
				list.add(bodys[i]);
			}
		}
		if (list == null || list.size() == 0)
			return;
		// 存放修改删除的记录的 数据库对应记录 vo
		SuperVO[] ovos = (SuperVO[]) java.lang.reflect.Array.newInstance(list
				.get(0).getClass(), list.size());
		for (int i = 0; i < list.size(); i++) {
			List li = (List) getDao().retrieveByClause(
					list.get(0).getClass(),
					list.get(0).getPKFieldName() + " = '"
							+ list.get(i).getPrimaryKey() + "'");
			if (li != null && li.size() != 0)
				ovos[i] = (SuperVO) li.get(0);
		}
		Map<String, String> map = getTypetoChangeClass();
		String changeClName = getClassName();
		Class cl = Class.forName(changeClName);
		String className = map.get(pk_billtype);
		SuperVO headVo = (SuperVO) ObjectUtils.serializableClone(vos
				.getParentVO());
		SuperVO[] bodyVos = (SuperVO[]) ObjectUtils.serializableClone(ovos);
		AggregatedValueObject billvo = new HYBillVO();
		billvo.setParentVO(headVo);
		billvo.setChildrenVO(bodyVos);
		
		
		SuperVO[] numvos = SingleVOChangeDataBsTool.runChangeVOAry(billvo, cl,
				className);
		
		//add mlr for modifysave end
		AggregatedValueObject nbillvo = new HYBillVO();
		nbillvo.setParentVO(headVo);
		SuperVO[] novos = (SuperVO[]) java.lang.reflect.Array.newInstance(list
				.get(0).getClass(), list.size());
		for(int i=0;i<list.size();i++){
			novos[i]=(SuperVO) ObjectUtils.serializableClone(list.get(i));
		}
		nbillvo.setChildrenVO(novos);	
		SuperVO[] nnumvos = SingleVOChangeDataBsTool.runChangeVOAry(nbillvo, cl,
				className);
		//add mlr for modifysave  end
		
		if (getChangeNums() == null || getChangeNums().length == 0)
			throw new Exception("没有注册现存量变化字段");
		for (int i = 0; i < numvos.length; i++) {
			for (int j = 0; j < getChangeNums().length; j++) {
				UFDouble uf = PuPubVO.getUFDouble_NullAsZero(numvos[i]
						.getAttributeValue(getChangeNums()[j]));
				UFDouble nuf=PuPubVO.getUFDouble_NullAsZero(nnumvos[i]
                        .getAttributeValue(getChangeNums()[j]));
				numvos[i].setAttributeValue(getChangeNums()[j], nuf
						.sub(uf));
			}
		}
		setAccountNumChange(numvos, pk_billtype);
		updateStock(numvos);
	}

	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 ArrayList<SuperVO[]> 存放每个查询维度查询出来的现存量
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:25:52
	 */
	public ArrayList<SuperVO[]> queryStockDetail(SuperVO[] vos)
			throws Exception {
		ArrayList<SuperVO[]> list = new ArrayList<SuperVO[]>();
		if (vos == null || vos.length == 0)
			return null;

		for (int i = 0; i < vos.length; i++) {
			String whereSql = getWheresql(vos[i]);
			if (whereSql == null || whereSql.length() == 0) {
				list.add(null);
			} else {
				list.add(queryStock(whereSql));
			}
		}
		return list;
	}
    /**
     * 跟据vo得到查询现存量的过滤条件
     * @param vos
     * @return
     * @throws Exception
     */
	public String getWheresql(SuperVO vos) throws Exception {
		if (vos == null)
			return null;
		String[] fields = getDef_Fields();
		if (fields == null || fields.length == 0)
			throw new Exception("没有注册现存量维度");
		String wsql = null;
		for (int i = 0; i < fields.length; i++) {
			String value = PuPubVO.getString_TrimZeroLenAsNull(vos
					.getAttributeValue(fields[i]));
			if (value != null) {
				if (wsql == null) {
					wsql = fields[i] + " = '" + value + "'";
				} else {
					wsql = wsql + " and " + fields[i] + " = '" + value + "'";
				}
			}
		}
		if (wsql == null) {
			return null;
		} else {
			wsql = wsql + " and isnull(dr,0)=0 and pk_corp='"
					+ SQLHelper.getCorpPk() + "'";
		}
		return wsql;
	}
	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 SuperVO[] 存放每个查询维度查询出来的现存量(按查询维度合并后)
	 * 
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:27:29
	 * 
	 */
	public SuperVO[] queryStockCombin1(SuperVO[] vos,String whereSql) throws Exception {
		ArrayList<SuperVO[]> list = queryStockDetail1(vos,whereSql);
		if (vos == null || vos.length == 0)
			return null;
		if (list == null || list.size() == 0)
			return null;
		for (int i = 0; i < vos.length; i++) {
			SuperVO[] vss = list.get(i);
			String[] conds = getConminFields(vos[i]);
			if (conds == null || conds.length == 0) {
				continue;
			} else {
				SuperVO[] coms = (SuperVO[]) CombinVO.combinData(vss, conds,
						getChangeNums(), vos[0].getClass());
				String[] filelds = getChangeNums();
				if (filelds == null || filelds.length == 0) {
					continue;
				}
				if(coms==null || coms.length==0)
					continue;
				for (int k = 0; k < filelds.length; k++) {
					vos[i].setAttributeValue(filelds[k], coms[0]
							.getAttributeValue(filelds[k]));
				}
			}
		}
		return vos;
	}
	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 ArrayList<SuperVO[]> 存放每个查询维度查询出来的现存量
	 * 
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:25:52
	 * 
	 */
	public ArrayList<SuperVO[]> queryStockDetail1(SuperVO[] vos,String whereSql1)
			throws Exception {
		ArrayList<SuperVO[]> list = new ArrayList<SuperVO[]>();
		if (vos == null || vos.length == 0)
			return null;

		for (int i = 0; i < vos.length; i++) {
			String whereSql = getWheresql(vos[i]);		
			if (whereSql == null || whereSql.length() == 0) {
				list.add(null);
			} else {
				if(whereSql!=null && whereSql.length()>0 ){
					whereSql=whereSql+" and "+whereSql1;
				}
				list.add(queryStock(whereSql));
			}
		}
		return list;
	}
	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 SuperVO[] 存放每个查询维度查询出来的现存量(按查询维度合并后)
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:27:29
	 * 
	 */
	public SuperVO[] queryStockCombin(SuperVO[] vos) throws Exception {
		ArrayList<SuperVO[]> list = queryStockDetail(vos);
		if (vos == null || vos.length == 0)
			return null;
		if (list == null || list.size() == 0)
			return null;
		for (int i = 0; i < vos.length; i++) {
			SuperVO[] vss = list.get(i);
			String[] conds = getConminFields(vos[i]);
			if (conds == null || conds.length == 0) {
				continue;
			} else {
				SuperVO[] coms = (SuperVO[]) CombinVO.combinData(vss, conds,
						getChangeNums(), vos[0].getClass());
				String[] filelds = getChangeNums();
				if (filelds == null || filelds.length == 0) {
					continue;
				}
				if(coms==null || coms.length==0)
					continue;
				for (int k = 0; k < filelds.length; k++) {
					vos[i].setAttributeValue(filelds[k], coms[0]
							.getAttributeValue(filelds[k]));
				}
			}
		}
		return vos;
	}

	/**
	 * 获得数据合并维度
	 * 
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:52:49
	 * 
	 */
	public String[] getConminFields(SuperVO vo) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		String[] fields = getDef_Fields();
		if (fields == null || fields.length == 0)
			throw new Exception("没有注册现存量维度");
		if (vo == null)
			return null;
		for (int i = 0; i < fields.length; i++) {
			String value = PuPubVO.getString_TrimZeroLenAsNull(vo
					.getAttributeValue(fields[i]));
			if (value != null) {
				list.add(fields[i]);
			}
		}
		if (list == null || list.size() == 0)
			return null;

		return list.toArray(new String[0]);
	}

	/**
	 * 设置现存量数据变化量
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void setAccountNumChange(SuperVO[] vos1, String pk_billtype)
			throws Exception {
		if (vos1 == null || vos1.length == 0)
			return;
		Map<String, UFBoolean[]> nmap = getTypetosetnum();
		if (nmap == null || nmap.size() == 0)
			throw new Exception(" 没有注册单据类型 对应  现存量变化规则");
		String[] fields = getChangeNums();
		if (fields == null || fields.length == 0)
			throw new Exception(" 没有注册现存量变化量字段");
		UFBoolean[] ufs = nmap.get(pk_billtype);
		if (ufs == null || ufs.length == 0 || ufs.length != fields.length)
			throw new Exception("单据类型为" + pk_billtype
					+ " 注册的变化量规则为空 或  注册的规则数组和变化量字段数组长度不一致");
		setAccountNum(vos1, ufs);
	}

	/**
	 * @author mlr
	 * @说明：（鹤岗矿业）将矿级单据数据转换为台账参量 2011-9-14下午03:24:36
	 * @param bill
	 *            矿级单据
	 * @return
	 * @throws Exception
	 */
	public void setAccountNum(SuperVO[] vos, UFBoolean[] isNumCirl)
			throws Exception {
		if (vos == null || vos.length == 0)
			return;
		for (int j = 0; j < vos.length; j++) {
			String[] fields = getChangeNums();
			for (int i = 0; i < fields.length; i++) {
				if (isNumCirl[i] == null) {
					vos[j].setAttributeValue(fields[i], new UFDouble(0.0));
				} else if (isNumCirl[i].booleanValue() == true) {
					UFDouble num = PuPubVO.getUFDouble_NullAsZero(vos[j]
							.getAttributeValue(fields[i]));
					vos[j].setAttributeValue(fields[i], num.multiply(-1));
				}
			}
		}
	}

	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 ArrayList<SuperVO[]> 存放每个查询维度查询出来的现存量
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:25:52
	 */
	public ArrayList<SuperVO[]> queryStockDetailForClient(SuperVO[] vos)
			throws Exception {
		ArrayList<SuperVO[]> list = null;
		try {
			Class[] ParameterTypes = new Class[] { SuperVO[].class };
			Object[] ParameterValues = new Object[] { vos };
			Object o = LongTimeTask.calllongTimeService("zmpub", null,
					"正在查询...", 1, getThisClassName(), null, "queryStockDetail",
					ParameterTypes, ParameterValues);
			if (o != null) {
				list = (ArrayList<SuperVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new Exception(e.getMessage());

		}
		return list;
	}

	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 SuperVO[] 存放每个查询维度查询出来的现存量(按查询维度合并后)
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:27:29 
	 */
	public SuperVO[] queryStockCombinForClient(SuperVO[] vos) throws Exception {
		SuperVO[] nvos = null;
		try {
			Class[] ParameterTypes = new Class[] { SuperVO[].class };
			Object[] ParameterValues = new Object[] { vos };
			Object o = LongTimeTask.calllongTimeService("zmpub", null,
					"正在查询...", 1, getThisClassName(), null, "queryStockCombin",
					ParameterTypes, ParameterValues);
			if (o != null) {
				nvos = (SuperVO[]) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new Exception(e.getMessage());

		}
		return nvos;
	}

	/**
	 * 通过where条件查询现存量 
	 * @param whereSql
	 * @return
	 */
	public SuperVO[] queryStockForClient(String whereSql) throws Exception {
		SuperVO[] nvos = null;
		try {
			Class[] ParameterTypes = new Class[] { String.class };
			Object[] ParameterValues = new Object[] { whereSql };
			Object o = LongTimeTask.calllongTimeService("zmpub", null,
					"正在查询...", 1, getThisClassName(), null, "queryStock",
					ParameterTypes, ParameterValues);
			if (o != null) {
				nvos = (SuperVO[]) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new Exception(e.getMessage());

		}
		return nvos;
	}

}
