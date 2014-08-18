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
 * ֧�����ı��湤����,������ʱ�������Ĺ�ϵʱ����̨����Ķ����ű����Ե���ʹ�ø��࣬
 * ��ʵ�������Զ����档
 * @author mlr
 */
public class SonBillSave extends BillSave{
	private HYSuperDMO dmo = null;
	/**
	 * ������ݲٿ��������
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
	 * ������ݲٿ��������
	 * @return
	 */
	public BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}
	/**
	 * �����ӱ����ݡ�
	 * �������ڣ�(2004-2-27 20:59:29)
	 * @param vo nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception �쳣˵����
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
	 * ����VO����,�������ĵ��ݱ���ʱ�����Ե����������ʵ�����ı��档
	 * �������ڣ�(2004-2-27 11:15:29)
	 * @return ArrayList
	 * @param vo nc.vo.pub.AggregatedValueObject

	 */
	public ISonVOH ioh;
	public java.util.ArrayList saveBill1(nc.vo.pub.AggregatedValueObject billVo) throws Exception{
		if(billVo==null)
			throw new Exception("��������Ϊ��");
		if(billVo.getParentVO()==null ){
			throw new Exception("��ͷ����Ϊ��");
		}
		if(billVo.getChildrenVO()==null || billVo.getChildrenVO().length==0)
			throw new Exception("���岻��Ϊ��");
		if(!(billVo instanceof ISonVOH)){
			throw new Exception("�ۺ�voû��ʵ��ISonVOH�ӿ�");
		}
		
		if(!(billVo.getChildrenVO()[0] instanceof ISonVO)){
			throw new Exception("�ӱ�voû��ʵ��ISonVO�ӿ�");
		}
		ioh=(ISonVOH)billVo;
	    HYBillVO  oldbillvo=(HYBillVO) ObjectUtils.serializableClone(billVo);
	    HYBillVO billvov=(HYBillVO)billVo;
	    java.util.ArrayList retAry = super.saveBill(billvov);
		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("����ʧ��");
		}
		HYBillVO newBillVo = (HYBillVO)retAry.get(1);
		if(oldbillvo.getParentVO().getPrimaryKey()==null){
			//���������ֶ����������Ϣ 
			//��Ϊ��������Ļ�  
			//����� ���صľۺ�vo �ǲ���������Ϣ��
			setChildData(newBillVo,getSuperDMO());			
		}				
		insertBody_Pk(newBillVo,oldbillvo);		
		if(oldbillvo.getParentVO().getPrimaryKey()==null){
			//�������� 
			//�������������Ļ���
			// ����Ҫ  ��billvo ����¡һ��  ��Ϊ ������Ϊ  oldbillvo
			// ����billvo  ��÷��ؽ��   Ȼ��ȡ�� billvo�ı��� ����  �����к���Ϣ  
			// ����������  ͬ���� oldbillvo�������
			// Ȼ�� ���������Ϣ
		     //������������			
			saveXiHa(oldbillvo);			
		}else{
			//����� �޸ı���Ļ�   
			// ����Ҫ  ��billvo ����¡һ��  ��Ϊ ������Ϊ  oldbillvo
			// ����billvo  ��÷��ؽ��   Ȼ��ȡ�� billvo�ı��� ����  �����к���Ϣ  
			// ����������  ͬ���� oldbillvo�������
			
			//Ȼ�� �ӽڵ�  oldbillvo �и���vo״̬ �ҳ�  ���� ɾ�� �޸ĵı�����Ϣ
			//������  ���������Ϣ
			//�޸ĵ�  ��ɾ��ԭ���������Ϣ  Ȼ�󱣴����µ������Ϣ
			//ɾ����  ɾ�������Ϣ		
		   //�����޸ı���
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
	 * �����������ݵ�newBillVo
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
		  			throw new Exception("�ۺ�voû��ʵ��ISonVO�ӿ�");	
		  		}		  		
		  	}else{
		  		throw new Exception("�ۺ�voû��ʵ��ISonVOH�ӿ�");
		  	}
		}
		if(newBillVo instanceof IExAggVO){
			IExAggVO bill=(IExAggVO) newBillVo;
			 bill.setTableVO(bill.getTableCodes()[0], bvos);
		}
	}
	/**
	 * ����������������������һ��ʵ����ѯ�������
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
	 * ֱ�ӽ�������ݴ������ݿ�
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
	 * ��newBillVo����ʱ���ɵı�ͷ����������Ϣ��ͬ����oldbillvo��
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-10-12����06:07:16
	 * @param newBillVo
	 * @param oldbillvo
	 * @throws Exception 
	 */
	public void insertBody_Pk(HYBillVO newBillVo,HYBillVO oldbillvo) throws Exception {
		//���� �к� ͬ����������    ���Ǵ��ڱ����޸�ɾ����     �ֽ��������л�����к��ظ�������		
		//�������ӱ�  �Ѿ�ȥ����ɾ����  ��û��ȥ�������Ϣ  newChilds
	    //����ǰ���ӱ�  û��ȥ��ɾ���� ������vo״̬�� ��������ϢoldChilds		
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
				throw new Exception("�кŲ���Ϊ��");
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
	 * �����ӱ�����ɾ�������Ϣ
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-10-12����05:33:54
	 * @param pk_ca_makesuc_b1 �ӱ�����
	 * @throws Exception
	 */
	public void deleteXiHa(String pk)throws Exception{
		SQLParameter para=new SQLParameter();
		para.addParam(pk.trim());
	    String sql=getSql();	
		getDao().executeUpdate(sql, para);	
	}
	/**
	 * �õ������ӱ�����ɾ������sql
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-11-16����05:02:34
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
	 * ֱ�ӽ�������ݴ浽���ݿ�
	 * ����vo״̬����������״̬����VOStatus.NEW
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-10-12����05:40:30
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
	 * ����vo״̬�������
	 * VOStatus.UPDATED �����
	 * VOStatus.DELETED ��ɾ��
	 * VOStatus.NEW ������
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-10-12����05:40:30
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
	 * ����vo״̬,�������vo����
	 * ��Ϊ ���� �޸�  ɾ��
	 * ����һ��map  
	 * keyΪ add Ϊ����
	 * keyΪ edit Ϊ�޸�
	 * keyΪ dete Ϊɾ��
	 * keyΪ unchg Ϊû�б仯 ����������б仯 ���Ա��뽫���ȫɾȫ��
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-10-12����05:45:51
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
