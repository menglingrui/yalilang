package nc.bs.zmpub.pub.tool.mod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.AccountCalendar;
import nc.bd.accperiod.AccperiodmonthAccessor;
import nc.bs.uap.lock.PKLock;
import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report2.CombinVO;
import nc.vo.zmpub.pub.tool.ZmPubTool;
/**
 * 台账修复工具类
 * 是对 AccountModBOTool2的扩展
 * 支持从 月结存表中取已结存月的现存量数据 +修复没有结存的从业务单据汇总的数据
 * 修改目的: 提高现存量 修复的效率  
 * 改为通过注册类进行数据注册
 * @author mlr
 */
public abstract class AccountModBOTool4 extends AccountModBOTool2{
	//抽象方法的扩展
	/**
	 * 获取结存注册信息
	 */
	public abstract AccountBalanceData getAccountBalanceData()throws Exception;	
   //业务逻辑实体方法的扩展     开始   for add mlr
	/**
		 * @author mlr
		 * @说明：（鹤岗矿业）修复矿级台账
		 * 现存量修复入口类
		 * 支持从按月结存表中取数的台账修复
		 * @param whereSql1 单据查询whereSql    对应业务单据  业务单据必须支持一个wheresql 如果业务单据对应不同的表
		 *                                     则不支持  此方法需要改进
		 * @param whereSql2 结存查询whereSql    对应结存表
		 * @param whereSql3 清楚台账数据的 whereSql  对应现存量表
		 * @param whereSql4 加载期初库存的whereSql   对应结存表  一般跟 whereSql2 一样  除非 结存表和期初表是不同表
		 * @return
		 * @throws Exception 
	*/
	public void modifyAccounts(String whereSql1,String whereSql2,String whereSql3,String whereSql4 ,String pk_corp) throws Exception{
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null )
			throw new Exception("没有注册结存信息");
		AccountData ad=getAccountData();
		if(ad==null )
			throw new Exception("没有注册现存量信息");
		
		clearNum(ad.getClearSql(whereSql3));//清空现存量
		AccperiodmonthVO am = getLastAccountMonth(pk_corp);//获取最后一个结账月
		SuperVO[] preparas = null;
		if(am != null){//获取结存量
			preparas = getAccountsForOneMonth(am.getPk_accperiodmonth(),whereSql2);
		}
//		if(am==null){
//			//没有结账月   加载期初库存
//			preparas=abd.loadPeridData(whereSql4);
//			preparas=combinAccounts(preparas);
//			
//		}
		PubBillData pb=getPubBillData();
		if(pb==null )
			throw new Exception(" 没有注册单据公共信息");
		StringBuffer whereBuffer = new StringBuffer();
        if(pb.getBillDateName()==null || pb.getBillDateName().length()==0)
        	throw new Exception("没有注册业务单据日期");
		if(am!=null){
			whereBuffer.append(" h."+pb.getBillDateName()+"  > '"+am.getEnddate()+"' ");
		}
		if(whereBuffer.length()!=0){
		  if(PuPubVO.getString_TrimZeroLenAsNull(whereSql1)!=null){			
			whereBuffer.append(" and "+whereSql1);
		  }		
		}
		else{
		  if(PuPubVO.getString_TrimZeroLenAsNull(whereSql1)!=null){			
			 whereBuffer.append(whereSql1);
		  }		
		}
		if(getBillDataMap()==null || getBillDataMap().size()==0){
			throw new BusinessException("没有注册单据信息MAP");
		}
		String[] billtypes=new String[getBillDataMap().size()];
		int index=0;
		for(String key:getBillDataMap().keySet()){
			billtypes[index]=key;
			index=index+1;
		}
		String insql=ZmPubTool.getSubSql(billtypes);
		
		whereBuffer.append(" and h.pk_billtype in "+insql);
		
		SuperVO[] paras = null;
		paras = this.getAccountNum(whereBuffer.toString());			
		if(paras != null && paras.length > 0){//该月没有业务数据 结存为零
//			for(SuperVO para:paras){
//				if(PuPubVO.getUFDouble_NullAsZero(para.getAttributeValue("nnum")).doubleValue()<0)
//					throw new BusinessException("数据异常,现存量存在负结存");
//				if(PuPubVO.getUFDouble_NullAsZero(para.getAttributeValue("nallnum")).doubleValue()<0)
//					throw new BusinessException("数据异常,现存量存在负结存");
//			}
		}
	
	    String classn=abd.getChangeClass();//获取结存 -->现存量的数据交换类
	    if(classn==null || classn.length()==0)
	    	throw new Exception(" 没有注册结存量到现存量的交换类");
	    String clssnum=ad.getNumClass();//获取现存量 的类全路径
	    if(clssnum==null || clssnum.length()==0)
	    	throw new Exception(" 没有注册现存量类全路径");
	    Class cl=Class.forName(clssnum); 
	    SuperVO[] newpreparas1=null;
	    if(preparas!=null && preparas.length!=0) 
	      newpreparas1= (SuperVO[])SingleVOChangeDataBsTool.runChangeVOAry(preparas, cl, classn);//数据交换获得现存量vo	    
		SuperVO[] newpreparas=combinAccounts(newpreparas1);//按最小维度进行数据合并
	    SuperVO[] newparas=combinAccounts(paras);//	按最小维度进行数据合并	
		String[] strs= ad.getSetNumFields();//获得变化量设置字段
		int[] types=ad.getSetNumFieldsType();//获得变化量数据类型
		if(strs==null || strs.length==0)
			throw new Exception("没有注册变化量字段");
		if(types==null || types.length==0)
			throw new Exception("没有注册变化量字段的类型");
		if(ad.getUnpk()==null || ad.getUnpk().length==0)
			throw new Exception("没有在现存量信息中注册现存量最小维度");
        //合并结存  将期初结存 追加到本月结存
		SuperVO[] newalls=(SuperVO[]) CombinVO.combinVoByFields(newparas,newpreparas,ad.getUnpk(),types,strs);		
        //保存修复后台账数据
		if(newalls==null || newalls.length==0)
			return;
		//加锁处理
		lock(newalls);
	    getDao().insertVOArray(newalls);
	}
    /**
     * mlr
       给当前更新的现存量加锁
     * @param accounts
     * @throws Exception 
     */
	public void lock(SuperVO[] accounts) throws Exception {
		if(accounts==null || accounts.length==0){
			return;
		}
		Map<String,String> map=new HashMap<String, String>();
		for(int i=0;i<accounts.length;i++){
			String key=getNumKey(accounts[i]);
			map.put(key, key);
		}
		for(String lock:map.keySet()){
			boolean isLockSucess=PKLock.getInstance().addDynamicLock(lock);
			if(isLockSucess==false){
				throw new BusinessException("现存量更新出现并发，请稍后操作");
			}
		}
		
	}
	 /**
     * mlr
       现存量最小维度联合主键
     * @param inv
     * @return
	 * @throws Exception 
     */
	public String getNumKey(SuperVO inv) throws Exception {
		if(inv==null){
			return null;
		}
		if(getAccountData()==null){
			throw new BusinessException("没有注册现存量信息类");
		}
		if(getAccountData().getUnpk()==null || getAccountData().getUnpk().length==0){
			throw new BusinessException("现存量信息类没有注册现存量维度");
		}
		String key="";
		for(String name:getAccountData().getUnpk()){
			key=key+inv.getAttributeValue(name);
		}
		return key;
	}
	/**
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业）返回系统最后的结账月  如果系统不存在结账月 返回null
	 * 2011-10-25下午03:27:29
	 * @return
	 * @throws Exception 
	 */
	public AccperiodmonthVO getLastAccountMonth(String pk_corp) throws Exception{
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null )
			throw new Exception("没有注册结存信息");		
		String sql = abd.getLastAccountMonthQuerySql(pk_corp);
		if(sql==null || sql.length()==0)
			throw new Exception(" 没有注册查询最后结账月 对应的会计月的查询语句");
		String cmonthid = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, new ColumnProcessor()));
		if(cmonthid == null)
			return null;
		return AccountCalendar.getInstanceByAccperiodMonth(cmonthid).getMonthVO();
	}	
	/**
	 * @author mlr
	 * @说明：（鹤岗矿业）从月结表获取指定月的月结数据
	 * 2011-10-22下午04:00:12
	 * @param cmonthid   appWhereSql条件范围
	 * @return
	 * @throws Exception 
	 */
	public SuperVO[] getAccountsForOneMonth(String cmonthid,String appWhereSql) throws Exception{
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null )
			throw new Exception("没有注册结存信息");		
		String moclass=abd.getMonthAccountClass();
		String monid=abd.getAcMonID();		
		if(moclass==null || moclass.length()==0)
			throw new Exception("没有注册结存表类");
		Class moc=Class.forName(moclass);
		if(monid==null || monid.length()==0)
			throw new Exception("没有注册结存表存放会计月主键的名字");
		if(PuPubVO.getString_TrimZeroLenAsNull(cmonthid)==null)
			throw new BusinessException("输入参数[月份]为空");
		String whereSql = null;
		if(PuPubVO.getString_TrimZeroLenAsNull(appWhereSql)!=null)
			whereSql = "isnull(dr,0) = 0 and " +monid +"= '"+cmonthid+"'"+" and "+appWhereSql;
		else
			whereSql = "isnull(dr,0) = 0 and "+monid+" = '"+cmonthid+"'";
		Collection<SuperVO> c = (Collection<SuperVO>)getDao().retrieveByClause(moc, whereSql);
		if(c == null || c.size() ==0)
			return null;
		SuperVO[] months = new SuperVO[c.size()];
		Iterator<SuperVO> it = c.iterator();
		int index = 0;
		while(it.hasNext()){
			months[index] = it.next();
			index ++;
		}
		
		return months;
	}
	//业务逻辑实体方法的扩展     结束   for add mlr
	/**
	 * 进行结账处理
	 * @param body
	 * @param lmon
	 * @param pk_corp
	 * @return 
	 * @throws BusinessException
	 */
	public SuperVO[] doAccount(String pk_accperiodmonth,String pk_corp) throws Exception{		
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null){
			throw new BusinessException("没有注册结存信息类");
		}
			
		SuperVO mon  = null;
		SuperVO[] paras = null;
		SuperVO[] preparas = null;
		AccountData ad=getAccountData();
		if(ad==null){
			throw new BusinessException("没有注册现存量信息类");
		}
		
		if(ad.getNumClass()==null || ad.getNumClass().length()==0){
			throw new BusinessException("现存量信息类中没有注册现存量实现类");
		}
		SuperVO curnumvo=(SuperVO) Class.forName(ad.getNumClass()).newInstance();
		String[] names =curnumvo.getAttributeNames();	
		if(abd.getAcMonID()==null || abd.getAcMonID().length()==0){
			throw new BusinessException("结存信息类中没有注册结存月主键名称");
		}
		
		AccountCalendar ac = AccountCalendar.getInstanceByAccperiodMonth(pk_accperiodmonth);		
        //获取上月结存量
		 //获得上一个月
		AccperiodmonthVO  preMoth=AccperiodmonthAccessor.getInstance().queryFormerAccperiodmonthVO(
				ac.getMonthVO().getPk_accperiodscheme(),
				ac.getYearVO().getPeriodyear(),
				ac.getMonthVO().getMonth());
		 //从结存中取
		preparas = getAccountsForOneMonth(preMoth.getPk_accperiodmonth(),null);		
		if(preparas!=null && preparas.length!=0)
			preparas=combinAccounts(preparas);		
       //加载期初数据开始 mlr
//	    if(abd.getMonthAccountClass()==null || abd.getMonthAccountClass().length()==0){
//	    	throw new BusinessException("结存信息类没有注册结存实现类");
//	    }
//	    String lastmonthid=PuPubVO.getString_TrimZeroLenAsNull(abd.getLastAccountMonthQuerySql(pk_corp));
//	    
//	   if(lastmonthid==null){
//		
//			String whereSql = "isnull(dr,0) = 0 and dyearmonth = '00-00' and pk_corp='"+pk_corp+"'";				
//			Collection<SuperVO> c = (Collection<SuperVO>)getDao().retrieveByClause(Class.forName(abd.getMonthAccountClass()), whereSql);
//			SuperVO[] months=null;
//		 if( c!=null && c.size()!=0){
//			 months = new SuperVO[c.size()];
//			 Iterator<SuperVO> it = c.iterator();
//			 int index = 0;
//			 while(it.hasNext()){
//				months[index] = it.next();
//				index ++;
//			 }				
//		  }
//		  SuperVO[] mos=combinAccounts(months);
//		  preparas= mos;
//		}	    
        //加载期初数据结束mlr	     	   
        //获取当前月结存----从业务单据上汇总得来
		String whereSql = " h.dbilldate >= '"+ac.getMonthVO().getBegindate()
		+"' and  h.dbilldate <= '"+ac.getMonthVO().getEnddate()+"' and h.pk_corp='"+pk_corp+"'";
		String[] billtypes=new String[getBillDataMap().size()];
		int index1=0;
		for(String key:getBillDataMap().keySet()){
			billtypes[index1]=key;
			index1=index1+1;
		}
	    String insql=ZmPubTool.getSubSql(billtypes);
		
	    whereSql=whereSql+" and h.pk_billtype in "+insql;
	    
		paras =getAccountNum(whereSql);
		if(paras != null && paras.length > 0){//该月没有业务数据 结存为零
		//数据合并  默认为合并好的数据  该处可以不再合并			
		paras=combinAccounts(paras);			
		if(ad.getUnpk()==null&&ad.getUnpk().length==0){
			throw new BusinessException("现存量信息类没有注册最新维度信息");
		}
		if(ad.getSetNumFields()==null|| ad.getSetNumFields().length==0){
			throw new BusinessException("现存量信息类没有注册存量字段");
		}
        //数据校验
//			for(SuperVO para:paras){
//				 //校验现存量最小维度不能为空
//				 int index=0;
//				 for(String name:ad.getUnpk()){
//					 if(PuPubVO.getString_TrimZeroLenAsNull(para.getAttributeValue(name))==null){
//						 throw new BusinessException(ad.getUnpkName()[index]+"为空");
//					 }
//					 index++;
//				 }
//				for(int i=0;i<ad.getSetNumFields().length;i++){
//					if(PuPubVO.getUFDouble_NullAsZero(para.getAttributeValue(ad.getSetNumFields()[i])).doubleValue()<0)
//						throw new BusinessException("数据异常,在用量存在负结存;会计月为："+ac.getMonthVO().getMonth());
//				    }			
//			}
		}		
        //合并结存 将期初结存追加到本月结存
		paras = combo(preparas,paras);		
		if(paras == null || paras.length == 0)
			return null;
		//转换结存数据
		if(abd.getDyearmonthName()==null || abd.getDyearmonthName().length()==0){
			throw new BusinessException("结存信息类没有注册年度-月份字段");
		}
		if(abd.getBegindateName()==null ||abd.getBegindateName().length()==0){
			throw new BusinessException("结存信息类没有注册开始日期字段");
		}
		if(abd.getEnddateName()==null || abd.getEnddateName().length()==0){
			throw new BusinessException("结存信息类没有注册结束日期字段");
		}
		List<SuperVO> lmon =new ArrayList<SuperVO>();
		for(SuperVO para:paras){		
			mon = (SuperVO) Class.forName(abd.getMonthAccountClass()).newInstance();
			for(String name:names){
				mon.setAttributeValue(name, para.getAttributeValue(name));
			}
			mon.setAttributeValue(abd.getAcMonID(),ac.getMonthVO().getPk_accperiodmonth());			
			mon.setAttributeValue(abd.getDyearmonthName(), ac.getYearVO().getPeriodyear()+"-"+ac.getMonthVO().getMonth());
			mon.setAttributeValue(abd.getBegindateName(), ac.getMonthVO().getBegindate());
			mon.setAttributeValue(abd.getEnddateName(), ac.getMonthVO().getEnddate());
		   lmon.add(mon);
		}
		return (SuperVO[])lmon.toArray((SuperVO[])java.lang.reflect.Array.newInstance(paras[0].getClass(), 0));
	}
	/**
	 * 合并现存量
	 * @param preparas
	 * @param paras
	 * @return
	 * @throws Exception 
	 */
	public SuperVO[] combo(SuperVO[] preparas, SuperVO[] paras) throws Exception {
        if(preparas==null || preparas.length==0)
        	return paras;
		if(paras==null || paras.length==0)
			return preparas;
	
        List list=new ArrayList();
		for(int i=0;i<preparas.length;i++){
			list.add(preparas[i]);
		}
		for(int i=0;i<paras.length;i++){
			list.add(paras[i]);
		}	
	    SuperVO[] vos=(SuperVO[]) list.toArray((SuperVO[])java.lang.reflect.Array.newInstance(paras[0].getClass(), 0));
	    if(vos!=null && vos.length>0){
	    	return combinAccounts(vos);
	    }
		return null;
	}		
}
