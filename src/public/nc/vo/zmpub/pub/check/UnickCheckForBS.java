package nc.vo.zmpub.pub.check;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.zmpub.pub.check.BsUniqueCheck;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
//create table zm_unique(
//unique varchar(1000),
//iscontain char(20),
//checkclass char(1000),
//checkmodule char(20),
//checktable char(20),
//hcontrol varchar(1000),
//bcontrol varchar(1000),
//hvo varchar(1000),
//bvo varchar(1000)
//)

/**
 *  bs端的自动唯一性校验
 * 主要用于档案删除时，校验是否已经应用于业务单据，如果被引用则不允许删除
 * 支持动态的注册参与校验的业务单据
 */
public class UnickCheckForBS {
	public static Map<String, UinqueVO> map = new HashMap<String, UinqueVO>();

	public static boolean check(String checktable, SuperVO[] vos, SuperVO hvo,
			List<String> list) throws BusinessException {
		if (vos == null || vos.length == 0)
			return false;
		if (map.get(checktable) == null) {
			UinqueVO uvo = queryUnVO(checktable);
			if (uvo == null) {
				return false;
			} else {
				map.put(checktable, uvo);
			}
		}
		if (map.get(checktable) == null) {
			return false;
		}
		UinqueVO uvo = map.get(checktable);
		String unique = uvo.getUnique();
		if (unique == null || unique.length() == 0) {
			return false;
		} else {
			String[] uniques = unique.split(":");
			String ctable = uvo.getChecktable();
			if (ctable == null || ctable.length() == 0) {
				return false;
			}
            //处理交叉校验
			return dealCheckValuate(uvo, hvo, vos, list);
		}
		}
	

	public static boolean  dealCheckValuate(UinqueVO uvo,SuperVO hvo, SuperVO[] vos,List<String> list ) throws BusinessException {
		String unique = uvo.getUnique();
		if (unique == null || unique.length() == 0) {
			return false;
		} else {
			String[] uniques = unique.split(":");
	
		String spiltwhesql = hvo.getPKFieldName() + "  in ( select "
		+ hvo.getPKFieldName() + " from " + hvo.getTableName()
		+ " where isnull(dr,0)=0 ";
		if (uvo.getHcontrol() != null && uvo.getHcontrol().length() > 0) {
				String[] sps = uvo.getHcontrol().split(":");
				for (int i = 0; i < sps.length; i++) {
					spiltwhesql = spiltwhesql + " and " + sps[i] + "='"
							+ hvo.getAttributeValue(sps[i]) + "'";
				}
		}
		spiltwhesql=spiltwhesql+" )";				
		// 处理交叉校验 
		List<String> uns = new ArrayList<String>();
		for (int i = 0; i < uniques.length; i++) {
			if (uniques[i].equals(uvo.getIscontain())) {

			} else {
				uns.add(uniques[i]);
			}
		}
		for (int i = 0; i < vos.length; i++) {
			try {
				BsUniqueCheck.FieldUniqueChecks(vos[i], uniques,
						spiltwhesql, "");
			} catch (Exception e) {
				e.printStackTrace();

			}
			// 处理交叉校验
			List list1 = null;
			try {
				list1 = queryByCheckFields(vos[i], (String[]) uns
						.toArray(), spiltwhesql);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (list1 == null || list1.size() == 0)
				continue;
	
				SuperVO[] vos1 = (SuperVO[]) list1.toArray();
				if (vos1 == null || vos1.length == 0)
					continue;
				for (int j = 0; j < vos1.length; j++) {
					SuperVO vo = vos1[j];
					for (int k = 0; k < vos1.length; k++) {
						SuperVO dvo = vos1[k];
						if (j == k)
							continue;
						String cons = (String) vo.getAttributeValue(uvo
								.getIscontain());
						String cons1 = (String) dvo
								.getAttributeValue(uvo.getIscontain());
						String checkclass = uvo.getCheckclass();
						if (checkclass == null
								|| checkclass.length() == 0)
							return false;
						IUnickCheck check = null;
						try {
							check = getCheckInstanse(uvo
									.getCheckclass(), uvo
									.getCheckmodule());
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						if (check.isCrossServer(cons, cons1)) {
						    list.add(" 存在交叉数据 ");
							return true;
						}

					}

				
			}
		
	}
		return true;
	}
	}
	private static List queryByCheckFields(SuperVO vo, String[] checkFields,
			String conditon) throws Exception {
		StringBuffer cond = new StringBuffer();
		for (int i = 0; i < checkFields.length; i++) {
			String sign = " = ";
			if (BsUniqueCheck.isEmpty(vo.getAttributeValue(checkFields[i]))) {
				if (!(vo.getAttributeValue(checkFields[i]) instanceof String)
						&& !"".equalsIgnoreCase((String) vo
								.getAttributeValue(checkFields[i]))) {
					sign = " is ";
				}
			}
			if (BsUniqueCheck.isChar(vo.getAttributeValue(checkFields[i]))
					&& sign.equalsIgnoreCase(" = ")) {
				cond.append(" " + checkFields[i] + sign + "'"
						+ vo.getAttributeValue(checkFields[i]) + "'" + " and");
			} else {
				cond.append(" " + checkFields[i] + sign
						+ vo.getAttributeValue(checkFields[i]) + " and");
			}
		}
		int length = cond.toString().length();
		String condition = cond.toString().substring(0, length - 4)
				+ " and  isNull(" + vo.getEntityName() + ".dr,0)=0 " + conditon;
		List list = (List) BsUniqueCheck.getDao().retrieveByClause(
				vo.getClass(), condition);
		return list;
	}

	public static IUnickCheck getCheckInstanse(String checkclass,
			String checkmodule) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (checkmodule != null) {
			Object o = NewObjectService.newInstance(checkmodule, checkclass);
			if (o instanceof IUnickCheck) {
				return (IUnickCheck) o;
			}
		} else {
			Object o = NewObjectService.newInstance(checkclass);
			if (o instanceof IUnickCheck) {
				return (IUnickCheck) o;
			}
		}

		return null;
	}

	private static UinqueVO queryUnVO(String checktable) {
		List list=null;
		try {
			list = (List) BsUniqueCheck.getDao().retrieveByClause(UinqueVO.class,
					" and isnull(dr,0)=0 and checktable='"+checktable+"'");
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if(list==null|| list.size()==0)
			return null;
		
		return (UinqueVO) list.get(0);
	}

}
