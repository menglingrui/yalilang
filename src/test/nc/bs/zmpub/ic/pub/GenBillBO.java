//package nc.bs.zmpub.ic.pub;
//
//import nc.bs.dao.BaseDAO;
//import nc.jdbc.framework.processor.ColumnProcessor;
//import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
//import nc.vo.ic.pub.bill.GeneralBillItemVO;
//import nc.vo.ic.pub.bill.GeneralBillVO;
//import nc.vo.pub.AggregatedValueObject;
//import nc.vo.pub.BusinessException;
//import nc.vo.pub.compiler.PfParameterVO;
//import nc.vo.scm.constant.ScmConst;
//import nc.vo.scm.pu.PuPubVO;
///**
// * ��ʽ���ɿ�浥�ݣ���������ⵥ��(������icģ������ ��Ȼ�Զ������ʧ��)
// * @author zhf mlr 
// *
// */
//public abstract class GenBillBO {
//	
//	private ChangeToICBill change = null;
//	protected ChangeToICBill getChange(){
//		if(change == null){
//			change = new ChangeToICBill();
//		}
//		return change;
//	}
//	
//	private ZmIcPubBO icbo = null;
//	protected ZmIcPubBO getIcBO(){
//		if(icbo == null)
//			icbo = new ZmIcPubBO(getDao(),getChange());
//		return icbo;
//	}
//	
//	private BaseDAO dao = null;
//	protected BaseDAO getDao(){
//		if(dao == null){
//			dao = new BaseDAO();
//		}
//		return dao;
//	}
//	
//	
//	/**
//	 * 
//	 * @author zhf
//	 * @˵������������ҵ���Զ�������ת��
//	 * 2011-9-9����10:07:05
//	 * @param bill
//	 * @param paraVo
//	 * @throws BusinessException
//	 */
//	
//	public void autoGenBill(AggregatedValueObject bill,PfParameterVO paraVo) throws BusinessException{
//		if(bill == null)
//			return;
//		
////		ISysInitQry srvInitQry = (ISysInitQry) NCLocator.getInstance().lookup(
////				ISysInitQry.class.getName());
//		//*********************��ȡϵͳ����*************************
//
////		�����Ƿ��Զ����
//		boolean ispick = true;
////			PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic05"),UFBoolean.TRUE)
////		.booleanValue();
////		�����Ƿ��Զ�ǩ��
//		boolean isoutsign = false;
////		if(ispick){
////			isoutsign =  PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic01"),UFBoolean.FALSE)
////			.booleanValue();
////		}
////		�Ƿ��Զ����
//		boolean isautoin = true;
////			PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic02"),UFBoolean.FALSE)
////		.booleanValue();
////		����Ƿ��Զ�ǩ��
//		boolean isinsign = false;
////		if(isautoin)
////			isinsign =  PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic03"),UFBoolean.TRUE)
////			.booleanValue();
//		//***********************************************
//
//		//		�����������ⵥ
//		Class[] classes = getClasses();
//		GeneralBillVO newOutBill = getIcBO().
//			autoGenIcBill(bill, paraVo, ScmConst.m_otherOut,classes, ispick, isoutsign, true);
//
//		if(isautoin && newOutBill == null)
//			throw new BusinessException("���ݴ����쳣");
//
//		//		������ⷽ��ⵥ	
//		
//		genInBill(bill, newOutBill, paraVo,isautoin,isinsign);
//	}
//	/**
//	 * ���þۺ�vo,head,body��Class
//	 * @return Class[]
//	 */
//	protected abstract Class[] getClasses();
//
//	protected void genInBill(AggregatedValueObject bill,GeneralBillVO newOutBill,PfParameterVO paraVo,boolean isin,boolean issign) throws BusinessException{
//
//		AggregatedValueObject[] ingenvos = getChange().transAndGenBillVO(bill,
//				getClasses()[0],
//				getClasses()[1], 
//				getClasses()[2],
//				null, null, 
//				ScmConst.m_otherIn, paraVo);
//		
////		����������ⵥ     �ݲ�֧�ֱ���  �����ֿ� �͵���ֿ� �Ĳ�һ��  �������Ҫ֧�� �ô��߼��޸���
//		if(ingenvos == null || ingenvos.length == 0|| ingenvos.length>1){
//			throw new BusinessException("����ת���쳣");
//		}
//		
//		paraVo.m_billType = ScmConst.m_otherOut;
//		AggregatedValueObject[] ingenvos2 = getChange().transAndGenBillVO(newOutBill, GeneralBillVO.class,
//				GeneralBillHeaderVO.class, 
//				GeneralBillItemVO.class, null, null, 
//				ScmConst.m_otherIn, paraVo);
//
//		if(ingenvos2 == null || ingenvos2.length == 0|| ingenvos2.length>1){
//			throw new BusinessException("����ת���쳣");
//		}
//		
//		GeneralBillVO invo1 = (GeneralBillVO)ingenvos2[0];
//		GeneralBillVO invo = (GeneralBillVO)ingenvos[0];
//		
//		GeneralBillItemVO[] newitems = (GeneralBillItemVO[])invo1.getChildrenVO();
//		String pk_corp=PuPubVO.getString_TrimZeroLenAsNull(invo.getParentVO().getAttributeValue("pk_corp"));
//		if(isin){
//			for(GeneralBillItemVO item:newitems){
//				item.setNinnum(item.getNshouldinnum());
//				item.setNinassistnum(item.getNneedinassistnum());
//				//for add mlr  �繫˾����������id
//				item.setCinventoryid(getPk_invmandoc(item,pk_corp));
//				//item.setCinvmanid(getPk_invmandoc(item,pk_corp));
//			}
//		}
//		invo.setChildrenVO(newitems);
////		����������ⵥ
//		getIcBO().doSaveAndSignICBill(invo, paraVo.m_currentDate, issign, false);
//	}
//	
//	/**
//	 * mlr ��ù�˾����id
//	 * @param item
//	 * @param pk_corp 
//	 * @return
//	 * @throws BusinessException 
//	 */
//	private String getPk_invmandoc(GeneralBillItemVO item, String pk_corp) throws BusinessException {
//		if(pk_corp==null)
//			return null;
//		String pk_invbasdoc=item.getCinvbasid();	
//		String sql=" select b.pk_invmandoc from bd_invbasdoc h join bd_invmandoc b on h.pk_invbasdoc=b.pk_invbasdoc where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and " +
//				" h.pk_invbasdoc='"+pk_invbasdoc+"' and b.pk_corp='"+pk_corp+"'";
//        String pk_invmandoc=PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, new ColumnProcessor()));	
//		return pk_invmandoc;
//	}
//
//
//	/**
//	 * 
//	 * @author zhf
//	 * @˵������������ҵ���Ʋ�������ʱɾ�����ο�浥��
//	 * 2011-9-9����10:05:56
//	 * @param bill
//	 * @param date
//	 * @throws BusinessException
//	 */
//	public void dealOnUnApprove(AggregatedValueObject bill,String date,String cuser) throws BusinessException{
//		if(bill == null)
//			return;
////		�����Ƿ����ε�����ǩ��  ���ⵥ+��ⵥ
//		String headid = bill.getParentVO().getPrimaryKey();
//		if(PuPubVO.getString_TrimZeroLenAsNull(headid)==null)
//			throw new BusinessException("�����쳣");
//		getIcBO().checkExitNextBills(headid,true);
////		�Զ�ɾ�� ���ⵥ  ��ⵥ
//		getIcBO().deleteNextBillByFirstID(bill.getParentVO().getPrimaryKey(), ScmConst.m_otherIn, date,cuser);
//		getIcBO().deleteNextBillBySourceID(bill.getParentVO().getPrimaryKey(), ScmConst.m_otherOut, date,cuser);
//	}
//}
//
