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
 * ̨���޸�������
 * �Ƕ� AccountModBOTool2����չ
 * ֧�ִ� �½�����ȡ�ѽ���µ��ִ������� +�޸�û�н��Ĵ�ҵ�񵥾ݻ��ܵ�����
 * �޸�Ŀ��: ����ִ��� �޸���Ч��  
 * ��Ϊͨ��ע�����������ע��
 * @author mlr
 */
public abstract class AccountModBOTool4 extends AccountModBOTool2{
	//���󷽷�����չ
	/**
	 * ��ȡ���ע����Ϣ
	 */
	public abstract AccountBalanceData getAccountBalanceData()throws Exception;	
   //ҵ���߼�ʵ�巽������չ     ��ʼ   for add mlr
	/**
		 * @author mlr
		 * @˵�������׸ڿ�ҵ���޸���̨��
		 * �ִ����޸������
		 * ֧�ִӰ��½�����ȡ����̨���޸�
		 * @param whereSql1 ���ݲ�ѯwhereSql    ��Ӧҵ�񵥾�  ҵ�񵥾ݱ���֧��һ��wheresql ���ҵ�񵥾ݶ�Ӧ��ͬ�ı�
		 *                                     ��֧��  �˷�����Ҫ�Ľ�
		 * @param whereSql2 ����ѯwhereSql    ��Ӧ����
		 * @param whereSql3 ���̨�����ݵ� whereSql  ��Ӧ�ִ�����
		 * @param whereSql4 �����ڳ�����whereSql   ��Ӧ����  һ��� whereSql2 һ��  ���� ������ڳ����ǲ�ͬ��
		 * @return
		 * @throws Exception 
	*/
	public void modifyAccounts(String whereSql1,String whereSql2,String whereSql3,String whereSql4 ,String pk_corp) throws Exception{
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null )
			throw new Exception("û��ע������Ϣ");
		AccountData ad=getAccountData();
		if(ad==null )
			throw new Exception("û��ע���ִ�����Ϣ");
		
		clearNum(ad.getClearSql(whereSql3));//����ִ���
		AccperiodmonthVO am = getLastAccountMonth(pk_corp);//��ȡ���һ��������
		SuperVO[] preparas = null;
		if(am != null){//��ȡ�����
			preparas = getAccountsForOneMonth(am.getPk_accperiodmonth(),whereSql2);
		}
//		if(am==null){
//			//û�н�����   �����ڳ����
//			preparas=abd.loadPeridData(whereSql4);
//			preparas=combinAccounts(preparas);
//			
//		}
		PubBillData pb=getPubBillData();
		if(pb==null )
			throw new Exception(" û��ע�ᵥ�ݹ�����Ϣ");
		StringBuffer whereBuffer = new StringBuffer();
        if(pb.getBillDateName()==null || pb.getBillDateName().length()==0)
        	throw new Exception("û��ע��ҵ�񵥾�����");
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
			throw new BusinessException("û��ע�ᵥ����ϢMAP");
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
		if(paras != null && paras.length > 0){//����û��ҵ������ ���Ϊ��
//			for(SuperVO para:paras){
//				if(PuPubVO.getUFDouble_NullAsZero(para.getAttributeValue("nnum")).doubleValue()<0)
//					throw new BusinessException("�����쳣,�ִ������ڸ����");
//				if(PuPubVO.getUFDouble_NullAsZero(para.getAttributeValue("nallnum")).doubleValue()<0)
//					throw new BusinessException("�����쳣,�ִ������ڸ����");
//			}
		}
	
	    String classn=abd.getChangeClass();//��ȡ��� -->�ִ��������ݽ�����
	    if(classn==null || classn.length()==0)
	    	throw new Exception(" û��ע���������ִ����Ľ�����");
	    String clssnum=ad.getNumClass();//��ȡ�ִ��� ����ȫ·��
	    if(clssnum==null || clssnum.length()==0)
	    	throw new Exception(" û��ע���ִ�����ȫ·��");
	    Class cl=Class.forName(clssnum); 
	    SuperVO[] newpreparas1=null;
	    if(preparas!=null && preparas.length!=0) 
	      newpreparas1= (SuperVO[])SingleVOChangeDataBsTool.runChangeVOAry(preparas, cl, classn);//���ݽ�������ִ���vo	    
		SuperVO[] newpreparas=combinAccounts(newpreparas1);//����Сά�Ƚ������ݺϲ�
	    SuperVO[] newparas=combinAccounts(paras);//	����Сά�Ƚ������ݺϲ�	
		String[] strs= ad.getSetNumFields();//��ñ仯�������ֶ�
		int[] types=ad.getSetNumFieldsType();//��ñ仯����������
		if(strs==null || strs.length==0)
			throw new Exception("û��ע��仯���ֶ�");
		if(types==null || types.length==0)
			throw new Exception("û��ע��仯���ֶε�����");
		if(ad.getUnpk()==null || ad.getUnpk().length==0)
			throw new Exception("û�����ִ�����Ϣ��ע���ִ�����Сά��");
        //�ϲ����  ���ڳ���� ׷�ӵ����½��
		SuperVO[] newalls=(SuperVO[]) CombinVO.combinVoByFields(newparas,newpreparas,ad.getUnpk(),types,strs);		
        //�����޸���̨������
		if(newalls==null || newalls.length==0)
			return;
		//��������
		lock(newalls);
	    getDao().insertVOArray(newalls);
	}
    /**
     * mlr
       ����ǰ���µ��ִ�������
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
				throw new BusinessException("�ִ������³��ֲ��������Ժ����");
			}
		}
		
	}
	 /**
     * mlr
       �ִ�����Сά����������
     * @param inv
     * @return
	 * @throws Exception 
     */
	public String getNumKey(SuperVO inv) throws Exception {
		if(inv==null){
			return null;
		}
		if(getAccountData()==null){
			throw new BusinessException("û��ע���ִ�����Ϣ��");
		}
		if(getAccountData().getUnpk()==null || getAccountData().getUnpk().length==0){
			throw new BusinessException("�ִ�����Ϣ��û��ע���ִ���ά��");
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
	 * @˵�������׸ڿ�ҵ������ϵͳ���Ľ�����  ���ϵͳ�����ڽ����� ����null
	 * 2011-10-25����03:27:29
	 * @return
	 * @throws Exception 
	 */
	public AccperiodmonthVO getLastAccountMonth(String pk_corp) throws Exception{
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null )
			throw new Exception("û��ע������Ϣ");		
		String sql = abd.getLastAccountMonthQuerySql(pk_corp);
		if(sql==null || sql.length()==0)
			throw new Exception(" û��ע���ѯ�������� ��Ӧ�Ļ���µĲ�ѯ���");
		String cmonthid = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, new ColumnProcessor()));
		if(cmonthid == null)
			return null;
		return AccountCalendar.getInstanceByAccperiodMonth(cmonthid).getMonthVO();
	}	
	/**
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�����½���ȡָ���µ��½�����
	 * 2011-10-22����04:00:12
	 * @param cmonthid   appWhereSql������Χ
	 * @return
	 * @throws Exception 
	 */
	public SuperVO[] getAccountsForOneMonth(String cmonthid,String appWhereSql) throws Exception{
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null )
			throw new Exception("û��ע������Ϣ");		
		String moclass=abd.getMonthAccountClass();
		String monid=abd.getAcMonID();		
		if(moclass==null || moclass.length()==0)
			throw new Exception("û��ע�������");
		Class moc=Class.forName(moclass);
		if(monid==null || monid.length()==0)
			throw new Exception("û��ע������Ż��������������");
		if(PuPubVO.getString_TrimZeroLenAsNull(cmonthid)==null)
			throw new BusinessException("�������[�·�]Ϊ��");
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
	//ҵ���߼�ʵ�巽������չ     ����   for add mlr
	/**
	 * ���н��˴���
	 * @param body
	 * @param lmon
	 * @param pk_corp
	 * @return 
	 * @throws BusinessException
	 */
	public SuperVO[] doAccount(String pk_accperiodmonth,String pk_corp) throws Exception{		
		AccountBalanceData abd=getAccountBalanceData();
		if(abd==null){
			throw new BusinessException("û��ע������Ϣ��");
		}
			
		SuperVO mon  = null;
		SuperVO[] paras = null;
		SuperVO[] preparas = null;
		AccountData ad=getAccountData();
		if(ad==null){
			throw new BusinessException("û��ע���ִ�����Ϣ��");
		}
		
		if(ad.getNumClass()==null || ad.getNumClass().length()==0){
			throw new BusinessException("�ִ�����Ϣ����û��ע���ִ���ʵ����");
		}
		SuperVO curnumvo=(SuperVO) Class.forName(ad.getNumClass()).newInstance();
		String[] names =curnumvo.getAttributeNames();	
		if(abd.getAcMonID()==null || abd.getAcMonID().length()==0){
			throw new BusinessException("�����Ϣ����û��ע��������������");
		}
		
		AccountCalendar ac = AccountCalendar.getInstanceByAccperiodMonth(pk_accperiodmonth);		
        //��ȡ���½����
		 //�����һ����
		AccperiodmonthVO  preMoth=AccperiodmonthAccessor.getInstance().queryFormerAccperiodmonthVO(
				ac.getMonthVO().getPk_accperiodscheme(),
				ac.getYearVO().getPeriodyear(),
				ac.getMonthVO().getMonth());
		 //�ӽ����ȡ
		preparas = getAccountsForOneMonth(preMoth.getPk_accperiodmonth(),null);		
		if(preparas!=null && preparas.length!=0)
			preparas=combinAccounts(preparas);		
       //�����ڳ����ݿ�ʼ mlr
//	    if(abd.getMonthAccountClass()==null || abd.getMonthAccountClass().length()==0){
//	    	throw new BusinessException("�����Ϣ��û��ע����ʵ����");
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
        //�����ڳ����ݽ���mlr	     	   
        //��ȡ��ǰ�½��----��ҵ�񵥾��ϻ��ܵ���
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
		if(paras != null && paras.length > 0){//����û��ҵ������ ���Ϊ��
		//���ݺϲ�  Ĭ��Ϊ�ϲ��õ�����  �ô����Բ��ٺϲ�			
		paras=combinAccounts(paras);			
		if(ad.getUnpk()==null&&ad.getUnpk().length==0){
			throw new BusinessException("�ִ�����Ϣ��û��ע������ά����Ϣ");
		}
		if(ad.getSetNumFields()==null|| ad.getSetNumFields().length==0){
			throw new BusinessException("�ִ�����Ϣ��û��ע������ֶ�");
		}
        //����У��
//			for(SuperVO para:paras){
//				 //У���ִ�����Сά�Ȳ���Ϊ��
//				 int index=0;
//				 for(String name:ad.getUnpk()){
//					 if(PuPubVO.getString_TrimZeroLenAsNull(para.getAttributeValue(name))==null){
//						 throw new BusinessException(ad.getUnpkName()[index]+"Ϊ��");
//					 }
//					 index++;
//				 }
//				for(int i=0;i<ad.getSetNumFields().length;i++){
//					if(PuPubVO.getUFDouble_NullAsZero(para.getAttributeValue(ad.getSetNumFields()[i])).doubleValue()<0)
//						throw new BusinessException("�����쳣,���������ڸ����;�����Ϊ��"+ac.getMonthVO().getMonth());
//				    }			
//			}
		}		
        //�ϲ���� ���ڳ����׷�ӵ����½��
		paras = combo(preparas,paras);		
		if(paras == null || paras.length == 0)
			return null;
		//ת���������
		if(abd.getDyearmonthName()==null || abd.getDyearmonthName().length()==0){
			throw new BusinessException("�����Ϣ��û��ע�����-�·��ֶ�");
		}
		if(abd.getBegindateName()==null ||abd.getBegindateName().length()==0){
			throw new BusinessException("�����Ϣ��û��ע�Ὺʼ�����ֶ�");
		}
		if(abd.getEnddateName()==null || abd.getEnddateName().length()==0){
			throw new BusinessException("�����Ϣ��û��ע����������ֶ�");
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
	 * �ϲ��ִ���
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
