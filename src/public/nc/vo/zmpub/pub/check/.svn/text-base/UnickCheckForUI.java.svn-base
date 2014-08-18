package nc.vo.zmpub.pub.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
/**
 * ui端的自动唯一性校验
 * 主要用于档案删除时，校验是否已经应用于业务单据，如果被引用则不允许删除
 * 支持动态的注册参与校验的业务单据
 * @author mlr
 */
//  create table zm_unique(
//	unique varchar(1000),
//	iscontain char(20),
//  checkclass char(1000),
//  checkmodule char(20),
//	checktable char(20),
//	hcontrol varchar(1000),
//	bcontrol varchar(1000),
//    hvo varchar(1000),
//    bvo varchar(1000)
//  )
public class UnickCheckForUI {
	public static Map<String, UinqueVO> map = new HashMap<String, UinqueVO>();

	public static boolean check(String checktable, SuperVO[] vos,
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
			SuperVO[][] voss = (SuperVO[][]) SplitBillVOs.getSplitVOs(vos,
					uniques);
			if (voss == null || voss.length == 0)
				return false;
			for (int i = 0; i < voss.length; i++) {
				if (vos[i] == null) {

				} else if (voss[i].length > 1) {
					for (int j = 0; j < uniques.length; j++) {
						list.add((String) voss[i][0]
								.getAttributeValue(uniques[j]));
						throw new BusinessException("重复数据");
					}
					return false;
				}
			}
			if (uvo.getIscontain() != null && uvo.getIscontain().length() > 0) {
				List<String> uns = new ArrayList<String>();
				for (int i = 0; i < uniques.length; i++) {
					if (uniques[i].equals(uvo.getIscontain())) {

					} else {
						uns.add(uniques[i]);
					}
				}
				SuperVO[][] voss1 = (SuperVO[][]) SplitBillVOs.getSplitVOs(vos,
						uns.toArray(new String[0]));
				if (voss1 == null || voss1.length == 0)
					return false;
				for (int i = 0; i < voss1.length; i++) {
					SuperVO[] vos1 = voss1[i];
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
							String cons1 = (String) dvo.getAttributeValue(uvo
									.getIscontain());
							String checkclass = uvo.getCheckclass();
							if (checkclass == null || checkclass.length() == 0)
								return false;
							IUnickCheck check = null;
							try {
								check = getCheckInstanse(uvo.getCheckclass(),
										uvo.getCheckmodule());
							} catch (InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (check.isCrossClient(cons, cons1)) {
								for (int l = 0; l < uniques.length; l++) {
									list.add((String) vo
											.getAttributeValue(uniques[j]));
								}
								for (int l = 0; l < uniques.length; l++) {
									list.add((String) dvo
											.getAttributeValue(uniques[j]));
								}
								return true;
							}

						}

					}
				}
			}
		}
		return false;
	}

	public static IUnickCheck getCheckInstanse(String checkclass,
			String checkmodule) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		if (checkclass != null && checkclass.length() > 0) {
			Object o = Class.forName(checkclass).newInstance();
			if (o instanceof IUnickCheck) {
				return (IUnickCheck) o;
			}
		}
		return null;
	}

	private static UinqueVO queryUnVO(String checktable) {
		SuperVO[] vos=null;	
			try {
				 vos=  HYPubBO_Client.queryByCondition(UinqueVO.class,
						" and isnull(dr,0)=0 and checktable='"+checktable+"'");
			} catch (UifException e) {
				e.printStackTrace();
			}
	
		if(vos==null|| vos.length==0)
			return null;
		
		return (UinqueVO) vos[0];
	}

}
