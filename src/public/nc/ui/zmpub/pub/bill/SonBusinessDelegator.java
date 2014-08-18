package nc.ui.zmpub.pub.bill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.itf.zmpub.pub.ISonVO;
import nc.itf.zmpub.pub.ISonVOH;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
/**
 *孙表业务处理类
 *@author mlr
 */
public class SonBusinessDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		if (billType == null || billType.trim().length() == 0)
			return null;
		else{
			VotableVO table=PfUIDataCache.getBillTypeToVO(billType, true);
			String headclassname=table.getHeaditemvo();
			SuperVO hvo=(SuperVO) Class.forName(headclassname).newInstance();
			ISonVOH ihvo=null;
			if(hvo instanceof ISonVOH){
				ihvo=(ISonVOH) hvo;
			}else{
				throw new BusinessException("表头vo 没有实现ISonVOH接口");
			}
			// 根据主表主键，取得子表的数据
			CircularlyAccessibleValueObject[] b1vos = (CircularlyAccessibleValueObject[]) HYPubBO_Client.queryAllBodyData(billType, voClass, key,strWhere);		
			if(b1vos!=null && b1vos.length!=0){
				for(int i=0;i<b1vos.length;i++){
				String key1=b1vos[i].getPrimaryKey();
				SuperVO bvo=(SuperVO) b1vos[i];
				 // 获得孙表数据
				SuperVO[] b2vos = (SuperVO[]) this.queryByCondition(Class.forName(ihvo.getSonClass()),bvo.getPKFieldName()+"='" + key1 + "' and isnull(dr,0)=0");
				 if(b2vos==null || b2vos.length==0)
					 continue;
				 List list=new ArrayList();
				 list.addAll(Arrays.asList(b2vos));
				 if(!(b1vos[i] instanceof ISonVO) ){
					 throw new BusinessException("子表没有实现ISonVO接口 ");
				 }
				 ISonVO son= (ISonVO) b1vos[i];
				 son.setSonVOS(list);
				}			
			}
			return b1vos;
		}
	}	
}
