package nc.bs.zmpub.pub.bill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.comsave.BillSave;
import nc.itf.zmpub.pub.ISonVO;
import nc.itf.zmpub.pub.ISonVOH;
import nc.jdbc.framework.SQLParameter;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;
/**
 * 支持孙表的保存工具类,当单据时主子孙表的关系时，后台保存的动作脚本可以调用使用该类，
 * 来实现孙表的自动保存。
 * @author mlr
 */
public class SonBillSave extends BillSave{
	private HYSuperDMO dmo = null;
	/**
	 * 获得数据操库操作对象
	 * @return
	 */
	public HYSuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new HYSuperDMO();
		}
		return dmo;
	}
	public BaseDAO dao=null;
	/**
	 * 获得数据操库操作对象
	 * @return
	 */
	public BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}
	/**
	 * 设置子表数据。
	 * 创建日期：(2004-2-27 20:59:29)
	 * @param vo nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception 异常说明。
	 */
	public void setChildData(AggregatedValueObject vo,HYSuperDMO dmo)throws BusinessException {		
		{
			SuperVO[] itemVos = (SuperVO[]) vo.getChildrenVO();
			SuperVO headVo = (SuperVO) vo.getParentVO();
			vo.setChildrenVO(dmo.queryByWhereClause(itemVos[0].getClass(),itemVos[0].getParentPKFieldName()+ "='"
						+ headVo.getPrimaryKey()
						+ "' and isnull(dr,0)=0"));
		}
	}	
	/**
	 * 单据VO保存,主子孙表的单据保存时，可以调用这个方法实现孙表的保存。
	 * 创建日期：(2004-2-27 11:15:29)
	 * @return ArrayList
	 * @param vo nc.vo.pub.AggregatedValueObject

	 */
	public ISonVOH ioh;
	public java.util.ArrayList saveBill1(nc.vo.pub.AggregatedValueObject billVo) throws Exception{
		if(billVo==null)
			throw new Exception("传入数据为空");
		if(billVo.getParentVO()==null ){
			throw new Exception("表头不能为空");
		}
		if(billVo.getChildrenVO()==null || billVo.getChildrenVO().length==0)
			throw new Exception("表体不能为空");
		if(!(billVo instanceof ISonVOH)){
			throw new Exception("聚合vo没有实现ISonVOH接口");
		}
		
		if(!(billVo.getChildrenVO()[0] instanceof ISonVO)){
			throw new Exception("子表vo没有实现ISonVO接口");
		}
		ioh=(ISonVOH)billVo;
	    HYBillVO  oldbillvo=(HYBillVO) ObjectUtils.serializableClone(billVo);
	    HYBillVO billvov=(HYBillVO)billVo;
	    java.util.ArrayList retAry = super.saveBill(billvov);
		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("保存失败");
		}
		HYBillVO newBillVo = (HYBillVO)retAry.get(1);
		if(oldbillvo.getParentVO().getPrimaryKey()==null){
			//新增保存手动查出表体信息 
			//因为新增保存的话  
			//保存后 返回的聚合vo 是不带表体信息的
			setChildData(newBillVo,getSuperDMO());			
		}				
		insertBody_Pk(newBillVo,oldbillvo);		
		if(oldbillvo.getParentVO().getPrimaryKey()==null){
			//新增保存 
			//如果是新增保存的话：
			// 首先要  将billvo 深层克隆一份  作为 旧数据为  oldbillvo
			// 保存billvo  获得返回结果   然后取得 billvo的表体 主键  根据行号信息  
			// 将表体主键  同步到 oldbillvo的孙表中
			// 然后 保存孙表信息
		     //处理新增保存			
			saveXiHa(oldbillvo);			
		}else{
			//如果是 修改保存的话   
			// 首先要  将billvo 深层克隆一份  作为 旧数据为  oldbillvo
			// 保存billvo  获得返回结果   然后取得 billvo的表体 主键  根据行号信息  
			// 将表体主键  同步到 oldbillvo的孙表中
			
			//然后 从节的  oldbillvo 中根据vo状态 找出  新增 删除 修改的表体信息
			//新增的  保存孙表信息
			//修改的  先删除原来的孙表信息  然后保存最新的孙表信息
			//删除的  删除孙表信息		
		   //处理修改保存
		   Map<String,List<SuperVO>>  map=splitVO((SuperVO[])oldbillvo.getChildrenVO());
		   List<SuperVO> adds=map.get("add");
		   List<SuperVO> edits=map.get("edit");
		   List<SuperVO> detes=map.get("dete");
		   List<SuperVO> unchg=map.get("unchg");
		   if(adds!=null && adds.size()!=0){
			  for(int i=0;i<adds.size();i++){
				 saveXiHa(((ISonVO)(adds.get(i))).getSonVOS());
			  } 
		   }
           if(edits!=null && edits.size()!=0){
        	   for(int i=0;i<edits.size();i++){
				 updateXiHa(((ISonVO)(edits.get(i))).getSonVOS());
  			  } 
		   }
           if(detes!=null && detes.size()!=0){
        	   for(int i=0;i<detes.size();i++){
          		 deleteXiHa((detes.get(i)).getPrimaryKey()); 				 
    		  } 
		   }	
           if(unchg!=null && unchg.size()!=0){
        	   for(int i=0;i<unchg.size();i++){
        		   updateXiHa(((ISonVO)(unchg.get(i))).getSonVOS());
      		    } 
		   }	
		}
		ioh=null;
		sql=null;
        return retAry;
	}
	/**
	 * 设置孙表的数据到newBillVo
	 * @param newBillVo
	 * @throws Exception 
	 */
	private void setSonChildDatas(HYBillVO newBillVo) throws Exception {
		SuperVO[] bvos=(SuperVO[]) newBillVo.getChildrenVO();
		if(bvos==null || bvos.length==0)
			return ;
		for(int i=0;i<bvos.length;i++){
		  	String bodypk=bvos[i].getPrimaryKey();
		  	if(newBillVo instanceof ISonVOH ){
		  		ISonVOH billvo=(ISonVOH)newBillVo;
		  		String soncl=billvo.getSonClass();
		  		SuperVO son=(SuperVO) Class.forName(soncl).newInstance();
		  		List list=querySonDatas(bodypk,son);
		  		if(bvos[i] instanceof ISonVO ){
		  			ISonVO bodyvo=(ISonVO)bvos[i];
		  			bodyvo.setSonVOS(list);
		  		}else{
		  			throw new Exception("聚合vo没有实现ISonVO接口");	
		  		}		  		
		  	}else{
		  		throw new Exception("聚合vo没有实现ISonVOH接口");
		  	}
		}
		if(newBillVo instanceof IExAggVO){
			IExAggVO bill=(IExAggVO) newBillVo;
			 bill.setTableVO(bill.getTableCodes()[0], bvos);
		}
	}
	/**
	 * 根据孙表的主表主键和孙表的一个实例查询孙表数据
	 * @param bodypk
	 * @param son
	 * @return
	 * @throws DAOException 
	 */
	public  List querySonDatas(String bodypk, SuperVO son) throws DAOException {
		
		String hpk=son.getParentPKFieldName();
		
	   List	list=(List) getDao().retrieveByClause(son.getClass(), hpk +" ='"+bodypk+"' and isnull(dr,0)=0 ");
		
	   if(list==null ||list.size()==0)
		   return null;
		return list;
	}
	/**
	 * 直接将孙表数据存入数据库
	 * @param oldbillvo
	 * @throws Exception
	 */
	public void saveXiHa(HYBillVO oldbillvo) throws Exception {
		SuperVO[] oldchilds=(SuperVO[]) oldbillvo.getChildrenVO();
		if(oldchilds!=null && oldchilds.length!=0){
			List<SuperVO> list=new ArrayList<SuperVO>();
				for(int i=0;i<oldchilds.length;i++){	
					if(((ISonVO)oldchilds[i]).getSonVOS()!=null&& ((ISonVO)oldchilds[i]).getSonVOS().size()>0){
					  list.addAll(((ISonVO)oldchilds[i]).getSonVOS());
					}
				}
				saveXiHa(list);
		}		
	}
	/**
	 * 将newBillVo保存时生成的表头表体主键信息，同步到oldbillvo中
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-12下午06:07:16
	 * @param newBillVo
	 * @param oldbillvo
	 * @throws Exception 
	 */
	public void insertBody_Pk(HYBillVO newBillVo,HYBillVO oldbillvo) throws Exception {
		//根据 行号 同步孙表的主键    但是存在表体修改删除后     又进行了增行会存在行号重复的问题		
		//保存后的子表  已经去掉了删除行  但没有去掉孙表信息  newChilds
	    //保存前的子表  没有去掉删除行 （存在vo状态） 有孙表的信息oldChilds		
		SuperVO[]  newChilds=(SuperVO[]) newBillVo.getChildrenVO();
		if(newChilds==null || newChilds.length==0 ){
			return;
		}
		SuperVO[] oldChilds=(SuperVO[]) oldbillvo.getChildrenVO();
		for(int i=0;i<newChilds.length;i++){
			String pk=newChilds[i].getPrimaryKey();
	//		String pk_h=PuPubVO.getString_TrimZeroLenAsNull(newChilds[i].getAttributeValue(newChilds[i].getParentPKFieldName()));
            ISonVO newso=(ISonVO)newChilds[i];
			String crowno=PuPubVO.getString_TrimZeroLenAsNull(newChilds[i].getAttributeValue(newso.getRowNumName()));   
			if(crowno==null || crowno.length()==0)
				throw new Exception("行号不能为空");
            for(int j=0;j<oldChilds.length;j++){
               if(oldChilds[j].getStatus()==VOStatus.DELETED){
            	   continue;
               }
               ISonVO oldso=(ISonVO) oldChilds[j];
               if(crowno.equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(oldChilds[j].getAttributeValue(oldso.getRowNumName())))){
            	   oldChilds[j].setPrimaryKey(newChilds[i].getPrimaryKey());
            	   List list= oldso.getSonVOS();
            	   if(list!=null && list.size()!=0){
            	      for(int k=0;k<list.size();k++){
       				  // ((SuperVO)list.get(k)).setPrimaryKey(pk);
       				((SuperVO)list.get(k)).setAttributeValue(((SuperVO)list.get(k)).getParentPKFieldName(), pk);
       			      }
            	   }
               }
           }
		}
	}
	/**
	 * 跟据子表主键删除孙表信息
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-12下午05:33:54
	 * @param pk_ca_makesuc_b1 子表主键
	 * @throws Exception
	 */
	public void deleteXiHa(String pk)throws Exception{
		SQLParameter para=new SQLParameter();
		para.addParam(pk.trim());
	    String sql=getSql();	
		getDao().executeUpdate(sql, para);	
	}
	/**
	 * 得到根据子表主键删除孙表的sql
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-11-16下午05:02:34
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public String sql=null;
	private String getSql() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	  if(sql==null){
		String str=ioh.getSonClass();
		SuperVO ivo=(SuperVO) Class.forName(str).newInstance();
		sql=" update "+ivo.getTableName()+" set dr=1 where "+ivo.getParentPKFieldName()+"=?";
	  }	
	  return sql;
	}
	/**
	 * 直接将孙表数据存到数据库
	 * 单据vo状态必须是新增状态即：VOStatus.NEW
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-12下午05:40:30
	 * @throws Exception
	 */
	public void saveXiHa(List  list) throws Exception{
		if(list==null || list.size()==0)
			return;
		for(int i=0;i<list.size();i++){
		 if(((SuperVO) list.get(i)).getStatus()==VOStatus.NEW)
	      getDao().insertVO((SuperVO) list.get(i));
		}
	}
	/**
	 * 根据vo状态更新孙表
	 * VOStatus.UPDATED 则更新
	 * VOStatus.DELETED 则删除
	 * VOStatus.NEW 则新增
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-12下午05:40:30
	 * @throws Exception
	 */
	public void updateXiHa(List  list) throws Exception{
		if(list==null || list.size()==0)
			return;
		for(int i=0;i<list.size();i++){
			SuperVO vo=(SuperVO) list.get(i);
		  if(vo.getStatus()==VOStatus.UPDATED){
	        getDao().updateVO((SuperVO) list.get(i));
		  }else if(vo.getStatus()==VOStatus.DELETED){
			getDao().deleteVO(vo);
		  }else if(vo.getStatus()==VOStatus.NEW){
			getDao().insertVO(vo);
		  }
		}
	}
	/**
	 * 根据vo状态,将传入的vo数组
	 * 分为 新增 修改  删除
	 * 返回一个map  
	 * key为 add 为新增
	 * key为 edit 为修改
	 * key为 dete 为删除
	 * key为 unchg 为没有变化 但可能孙表有变化 所以必须将孙表全删全存
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-12下午05:45:51
	 * @param vos
	 * @return
	 */
	public Map<String,List<SuperVO>> splitVO(SuperVO[] vos){
		if(vos==null || vos.length==0){
			return new HashMap<String,List<SuperVO>>();
		}
		Map<String,List<SuperVO>>  map=new HashMap<String,List<SuperVO>>();
		for(int i=0;i<vos.length;i++){
			if(vos[i].getStatus()==VOStatus.NEW || PuPubVO.getString_TrimZeroLenAsNull(vos[i].getPrimaryKey())==null){
				if(map.containsKey("add")){
					map.get("add").add(vos[i]);
				}else{
					List<SuperVO> list=new ArrayList<SuperVO>();
					list.add(vos[i]);
					map.put("add", list);
				}
			}
			if(vos[i].getStatus()==VOStatus.DELETED){
				if(map.containsKey("dete")){
					map.get("dete").add(vos[i]);
				}else{
					List<SuperVO> list=new ArrayList<SuperVO>();
					list.add(vos[i]);
					map.put("dete", list);
				}
			}
			if(vos[i].getStatus()==VOStatus.UPDATED){
				if(map.containsKey("edit")){
					map.get("edit").add(vos[i]);
				}else{
					List<SuperVO> list=new ArrayList<SuperVO>();
					list.add(vos[i]);
					map.put("edit", list);
				}
			}
			if(vos[i].getStatus()==VOStatus.UNCHANGED){
				if(map.containsKey("unchg")){
					map.get("unchg").add(vos[i]);
				}else{
					List<SuperVO> list=new ArrayList<SuperVO>();
					list.add(vos[i]);
					map.put("unchg", list);
				}
			}
		}	
		return map;		
	}
}
