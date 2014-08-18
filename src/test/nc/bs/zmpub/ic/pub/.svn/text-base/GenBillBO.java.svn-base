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
// * 推式生成库存单据（其他出入库单）(必须在ic模块下用 不然自动拣货回失败)
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
//	 * @说明：（鸡西矿业）自动进入库存转移
//	 * 2011-9-9上午10:07:05
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
//		//*********************读取系统参数*************************
//
////		出库是否自动拣货
//		boolean ispick = true;
////			PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic05"),UFBoolean.TRUE)
////		.booleanValue();
////		出库是否自动签字
//		boolean isoutsign = false;
////		if(ispick){
////			isoutsign =  PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic01"),UFBoolean.FALSE)
////			.booleanValue();
////		}
////		是否自动入库
//		boolean isautoin = true;
////			PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic02"),UFBoolean.FALSE)
////		.booleanValue();
////		入库是否自动签字
//		boolean isinsign = false;
////		if(isautoin)
////			isinsign =  PuPubVO.getUFBoolean_NullAs(srvInitQry.getParaBoolean(paraVo.m_coId, "jxic03"),UFBoolean.TRUE)
////			.booleanValue();
//		//***********************************************
//
//		//		生成其他出库单
//		Class[] classes = getClasses();
//		GeneralBillVO newOutBill = getIcBO().
//			autoGenIcBill(bill, paraVo, ScmConst.m_otherOut,classes, ispick, isoutsign, true);
//
//		if(isautoin && newOutBill == null)
//			throw new BusinessException("数据处理异常");
//
//		//		生成入库方入库单	
//		
//		genInBill(bill, newOutBill, paraVo,isautoin,isinsign);
//	}
//	/**
//	 * 设置聚合vo,head,body的Class
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
////		生成其他入库单     暂不支持表体  调出仓库 和调入仓库 的不一致  如果后续要支持 该处逻辑修改下
//		if(ingenvos == null || ingenvos.length == 0|| ingenvos.length>1){
//			throw new BusinessException("数据转换异常");
//		}
//		
//		paraVo.m_billType = ScmConst.m_otherOut;
//		AggregatedValueObject[] ingenvos2 = getChange().transAndGenBillVO(newOutBill, GeneralBillVO.class,
//				GeneralBillHeaderVO.class, 
//				GeneralBillItemVO.class, null, null, 
//				ScmConst.m_otherIn, paraVo);
//
//		if(ingenvos2 == null || ingenvos2.length == 0|| ingenvos2.length>1){
//			throw new BusinessException("数据转换异常");
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
//				//for add mlr  跨公司处理存货管理id
//				item.setCinventoryid(getPk_invmandoc(item,pk_corp));
//				//item.setCinvmanid(getPk_invmandoc(item,pk_corp));
//			}
//		}
//		invo.setChildrenVO(newitems);
////		保存其他入库单
//		getIcBO().doSaveAndSignICBill(invo, paraVo.m_currentDate, issign, false);
//	}
//	
//	/**
//	 * mlr 获得公司管理id
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
//	 * @说明：（鸡西矿业）移拨单弃审时删除下游库存单据
//	 * 2011-9-9上午10:05:56
//	 * @param bill
//	 * @param date
//	 * @throws BusinessException
//	 */
//	public void dealOnUnApprove(AggregatedValueObject bill,String date,String cuser) throws BusinessException{
//		if(bill == null)
//			return;
////		检验是否下游单据已签字  出库单+入库单
//		String headid = bill.getParentVO().getPrimaryKey();
//		if(PuPubVO.getString_TrimZeroLenAsNull(headid)==null)
//			throw new BusinessException("数据异常");
//		getIcBO().checkExitNextBills(headid,true);
////		自动删除 出库单  入库单
//		getIcBO().deleteNextBillByFirstID(bill.getParentVO().getPrimaryKey(), ScmConst.m_otherIn, date,cuser);
//		getIcBO().deleteNextBillBySourceID(bill.getParentVO().getPrimaryKey(), ScmConst.m_otherOut, date,cuser);
//	}
//}
//
