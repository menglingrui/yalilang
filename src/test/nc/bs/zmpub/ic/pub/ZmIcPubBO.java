//package nc.bs.zmpub.ic.pub;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import nc.bs.dao.BaseDAO;
//import nc.bs.framework.common.NCLocator;
//import nc.bs.ic.ic2a1.PickBillBO;
//import nc.itf.ic.pub.IGeneralBill;
//import nc.itf.uap.pf.IPFBusiAction;
//import nc.jdbc.framework.processor.ColumnProcessor;
//import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
//import nc.vo.ic.pub.bill.GeneralBillItemVO;
//import nc.vo.ic.pub.bill.GeneralBillVO;
//import nc.vo.ic.pub.bill.QryConditionVO;
//import nc.vo.pub.AggregatedValueObject;
//import nc.vo.pub.BusinessException;
//import nc.vo.pub.ValidationException;
//import nc.vo.pub.compiler.PfParameterVO;
//import nc.vo.pub.lang.UFBoolean;
//import nc.vo.pub.lang.UFDate;
//import nc.vo.pub.lang.UFDouble;
//import nc.vo.scm.constant.ScmConst;
//import nc.vo.scm.pu.PuPubVO;
//import nc.vo.zmpub.pub.consts.ZmpubBtnConst;
//import nc.vo.zmpub.pub.tool.ResultSetProcessorTool;
//import nc.vo.zmpub.pub.tool.ZmPubTool;
///**
// *  其它出库签字动作(ic模块)
// */
//public class ZmIcPubBO  {
//	private BaseDAO dao = null;
//	private BaseDAO getDao(){
//		if(dao == null){
//			dao = new BaseDAO();
//		}
//		return dao;
//	}	
//	private ChangeToICBill change = null;
//	private ChangeToICBill getChange(){
//		if(change == null){
//		}
//		change = new ChangeToICBill();
//		return change;
//	}
//	public ZmIcPubBO(){
//		super();
//	}
//	public ZmIcPubBO(BaseDAO dao){
//		super();
//		this.dao = dao;
//	}	
//	public ZmIcPubBO(ChangeToICBill change){
//		super();
//		this.change = change;
//	}
//	public ZmIcPubBO(BaseDAO dao ,ChangeToICBill change){
//		super();
//		this.dao = dao;
//		this.change = change;
//	}
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）推事单据执行指定动作
//	 * 2011-9-8下午02:24:52
//	 * @param date 登录日期
//	 * @param billvos 数据
//	 * @param actionname 待执行的动作
//	 * @throws BusinessException
//	 */
//	public void pushBillDoActions(String date, AggregatedValueObject[] billvos,String actionname) throws BusinessException {
//
//		if(billvos == null || billvos.length == 0)
//			return;
//		String s_billtype = PuPubVO.getString_TrimZeroLenAsNull(billvos[0].getParentVO().getAttributeValue("cbilltypecode"));
//
//		IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
//		for(AggregatedValueObject billvo:billvos){
//			bsBusiAction.processAction(actionname, s_billtype,date,null,billvo, null,null);
//		}
//	}
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）推事单据执行指定动作
//	 * 2011-9-8下午02:24:52
//	 * @param date 登录日期
//	 * @param billvos 数据
//	 * @param actionname 待执行的动作
//	 * @throws BusinessException
//	 */
//	public void pushBillDoAction(String date, AggregatedValueObject billvo,String actionname) throws BusinessException {
//
//		if(billvo == null)
//			return;
//		String s_billtype = PuPubVO.getString_TrimZeroLenAsNull(billvo.getParentVO().getAttributeValue("cbilltypecode"));
//
//		IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
//		//		for(AggregatedValueObject billvo:billvos){
//		bsBusiAction.processAction(actionname, s_billtype,date,null,billvo, null,null);
//		//		}
//	}
////	//取消签字
////	public void canelPushSign46(String date, AggregatedValueObject[] billvo) throws Exception {
////
////		if(billvo == null || billvo.length == 0)
////			return;
////
////		for(AggregatedValueObject bill:billvo){
////			if(PuPubVO.getInteger_NullAs(bill.getParentVO().getAttributeValue("fbillflag"), 2)!=2){
////				throw new BusinessException("存在下游已签字确认的入库单据");
////			}
////		}
////		String s_billtype = PuPubVO.getString_TrimZeroLenAsNull(billvo[0].getParentVO().getAttributeValue("cbilltypecode"));
////
////		//		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
////		IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
////		BaseDAO dao = new BaseDAO();
////		for(int i = 0 ;i < billvo.length;i++){
////			bsBusiAction.processAction("DELETE", s_billtype,date,null,billvo[i], null,null);//执行删除
////			//			清空累计入库数量
////			String sql = "update ic_general_b set ncorrespondnum = 0.0 where isnull(dr,0) = 0 and cgeneralhid = '"+((GeneralBillVO)billvo[i]).getHeaderVO().getPrimaryKey()+"'";
////			int num = dao.executeUpdate(sql);
////			//			if(num <= 0)
////			//				throw new BusinessException("数据处理异常");	
////		}
////
////
////
////		//			afterAction(billvo,false);
////		//		}
////	}
//
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）校验是否存在下游库存单据
//	 * 2011-9-9上午08:40:30
//	 * @param ID 当前业务单据头ID
//	 * @param isFirst 是否考虑源头信息关联
//	 * @throws ValidationException
//	 */
//	public void checkExitNextBills(String ID,boolean isFirst) throws BusinessException{
//		if(PuPubVO.getString_TrimZeroLenAsNull(ID)==null)
//			return;
//		String sql = " select count(0) from ic_general_b b inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid " +
//		" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
//		" and b.csourcebillhid = '"+ID+"' and h.fbillflag = "+ZmpubBtnConst.ic_sign_billstatus;
//		int flag = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0);
//		if(flag>0)
//			throw new ValidationException("存在已签字的下游出库单据");
//		if(!isFirst)
//			return;
//		sql = " select count(0) from ic_general_b b inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid " +
//		" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
//		" and b.cfirstbillhid = '"+ID+"' and h.fbillflag = "+ZmpubBtnConst.ic_sign_billstatus;
//		flag = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0);
//		if(flag>0)
//			throw new ValidationException("存在已签字的下游入库单据");
//	}
//
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）移拨单弃审时删除下游库存单据
//	 * 2011-9-9上午09:35:59
//	 * @param ID 业务单据ID
//	 *  @param date 当前日期
//	 * @throws BusinessException
//	 */
//	public void deleteNextBillsForMove(String ID,String date,String cuser) throws BusinessException{
//		if(PuPubVO.getString_TrimZeroLenAsNull(ID)==null)
//			return;
//		deleteNextBillByFirstID(ID, ScmConst.m_otherIn, date,cuser);
//		deleteNextBillBySourceID(ID, ScmConst.m_otherOut, date,cuser);
//	}
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）关联源头单据信息删除库存单据
//	 * 2011-9-9上午09:36:42
//	 * @param ID
//	 * @throws BusinessException
//	 */
//	public void deleteNextBillByFirstID(String ID,String icbilltype,String date,String cuser) throws BusinessException{
//
//		//		先查询 调用脚本删除
//		GeneralBillVO[] bills = queryNextBillsByFirstID(ID, icbilltype);
//		if(bills == null || bills.length == 0)
//			return;
//		for(GeneralBillVO bill:bills){
//			bill.getHeaderVO().setCoperatoridnow(cuser);
//		}
//		pushBillDoActions(date, bills, "DELETE");
//	}
//
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）关联上游单据信息删除库存单据
//	 * 2011-9-9上午09:37:08
//	 * @param ID
//	 * @throws BusinessException
//	 */
//	public void deleteNextBillBySourceID(String ID,String icbilltype,String date,String cuser) throws BusinessException{
//		//     先查询调用脚本删除
//		GeneralBillVO[] bills = queryNextBillsBySourceID(ID, icbilltype);
//		if(bills == null || bills.length == 0)
//			return;
//		for(GeneralBillVO bill:bills){
//			bill.getHeaderVO().setCoperatoridnow(cuser);
//		}
//		pushBillDoActions(date, bills, "DELETE");
//	}
//	
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）根据源头ID查询 库存单据
//	 * 2011-9-9上午09:50:40
//	 * @param ID
//	 * @return
//	 * @throws BusinessException
//	 */
//	public GeneralBillVO[] queryNextBillsByFirstID(String ID,String icbilltype) throws BusinessException{
//		GeneralBillVO[] bills = null;
//		String where  = " cfirstbillhid = '"+ID+"' and cbilltypecode = '"+icbilltype+"'";
//		QryConditionVO voCond = new QryConditionVO(where);
//	    ArrayList alListData = (ArrayList)queryBills(icbilltype, voCond);
//	    if(alListData == null || alListData.size() == 0)
//	    	return null;
//	    bills = new GeneralBillVO[alListData.size()];
//	    alListData.toArray(bills);
//		return bills;
//	}
//	
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）根据来源ID 查询库存单据
//	 * 2011-9-9上午09:50:45
//	 * @param ID
//	 * @return
//	 * @throws BusinessException
//	 */
//	public GeneralBillVO[] queryNextBillsBySourceID(String ID,String icbilltype) throws BusinessException{
//		GeneralBillVO[] bills = null;
//		String where  = " csourcebillhid = '"+ID+"' and cbilltypecode = '"+icbilltype+"'";
//		QryConditionVO voCond = new QryConditionVO(where);
//	    ArrayList alListData = (ArrayList)queryBills(icbilltype, voCond);
//	    if(alListData == null || alListData.size() == 0)
//	    	return null;
//	    bills = new GeneralBillVO[alListData.size()];
//	    alListData.toArray(bills);
//		return bills;
//	}
//
//	private  String beanName = IGeneralBill.class.getName(); 
//
//	public  ArrayList queryBills(String arg0 ,QryConditionVO arg1 ) throws BusinessException{
//		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
//		ArrayList o =  bo.queryBills(arg0 ,arg1 );					
//		return o;
//	}
//	
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）自动生成库存单据  暂不支持一张单据 分单生成多张库存单据的情况 如有需要联系zhf
//	 * 2011-9-10上午08:51:28
//	 * @param bill 待生成库存单据的业务数据
//	 * @param paraVo 平台参量  （内部包含来源单据类型、日期）
//	 * @param icbilltype  生成的库存单据类型
//	 * @param classes	待转换单据vo类型数组
//	 * @param isAutoPick  出库单据时是否自动拣货出库
//	 * @param isAutoSign  是否自动签字
//	 * @param isRet 是否返回新生成的库存单据信息
//	 * @return
//	 * @throws BusinessException
//	 */
//	public GeneralBillVO autoGenIcBill(
//			AggregatedValueObject bill,
//			PfParameterVO paraVo,
//			String icbilltype,
//			Class[] classes,
//			boolean isAutoPick,
//			boolean isAutoSign,
//			boolean isRet) throws BusinessException{
//		
//		AggregatedValueObject[] genvos = getChange().transAndGenBillVO(bill, classes[0],
//				classes[1], 
//				classes[2], null, null, 
//				icbilltype, paraVo);
//		
//		if(genvos == null || genvos.length == 0 || genvos.length>1)
//			throw new BusinessException("数据转换异常");
//
////		自动拣货
//		GeneralBillVO newout = (GeneralBillVO)genvos[0];
//		setPk_invmandoc(newout);
//		
//		//存放数量
//		Map<String,UFDouble> map = new HashMap<String, UFDouble>();
//		if (null == newout || null == newout.getItemVOs() || 0 == newout.getItemVOs().length)
//			return newout;
//		//如果是退库   退库业务    数量为负    生成出库时不能自动捡货  先把数量变为0.1  捡货完成后在还原数量
//		// 此处为变通处理 若有不妥之处  请修改
//		if(PuPubVO.getUFBoolean_NullAs(bill.getParentVO().getAttributeValue("ureserve1"),UFBoolean.FALSE).booleanValue()){
//			for(GeneralBillItemVO item:newout.getItemVOs()){
//				String billid = item.getPrimaryKey();
//				UFDouble num = item.getNshouldoutnum();
//				item.setNshouldoutnum(num.multiply(-1));//退库数量取正
//				map.put(billid, num);
//			}
//			//add by yf 退库出库仓库与入库仓库反转
//			GeneralBillHeaderVO hvo = newout.getHeaderVO();
//			String cwarehouseid = hvo.getCwarehouseid();
//			String cotherwarehouseid = (String) hvo.getAttributeValue("cotherwhid");
//			hvo.setAttributeValue("cotherwhid", cwarehouseid);
//			hvo.setCwarehouseid(cotherwarehouseid);
//			//end
//		}
//		
//		if(isAutoPick){
//			
//			
//			nc.bs.ic.ic2a1.PickBillBO pick = new PickBillBO();
//			
//			//自动检获
//			newout = pick.pickAutoForPushSave(newout, new UFDate(paraVo.m_currentDate));
//			
//			GeneralBillItemVO[] items = newout.getItemVOs();
//			//add by yf 退库捡货完成出库仓库与入库仓库回转
//			if(PuPubVO.getUFBoolean_NullAs(bill.getParentVO().getAttributeValue("ureserve1"),UFBoolean.FALSE).booleanValue()){
//				GeneralBillHeaderVO hvo = newout.getHeaderVO();
//				String cwarehouseid = hvo.getCwarehouseid();
//				String cotherwarehouseid = (String) hvo.getAttributeValue("cotherwhid");
//				hvo.setAttributeValue("cotherwhid", cwarehouseid);
//				hvo.setCwarehouseid(cotherwarehouseid);
//			}
//			//end
//			String error = null;
//			for(GeneralBillItemVO item:items){
//				if(PuPubVO.getUFBoolean_NullAs(bill.getParentVO().getAttributeValue("ureserve1"),UFBoolean.FALSE).booleanValue()){
//					UFDouble num = map.get(item.getPrimaryKey());
//					item.setNshouldoutnum(item.getNshouldoutnum().multiply(-1));
//					item.setNoutnum(item.getNoutnum().multiply(-1));
//					item.setNmny(item.getNmny().multiply(-1));
//					item.setNplannedmny(item.getNplannedmny().multiply(-1));
//				}
//				error = PuPubVO.getString_TrimZeroLenAsNull(item.getVnotebody());
//				if(error!=null){
//					throw new BusinessException("行号"+ZmPubTool.getString_NullAsTrimZeroLen(item.getCrowno())+"存货"+
//							ZmPubTool.getString_NullAsTrimZeroLen(item.getInv().getCinventorycode())+error);
////					if("PN03".equals(item.getCfirsttype())){
////						throw new BusinessException("行号"+ZmPubTool.getString_NullAsTrimZeroLen(item.getCrowno())+"存货"+
////								ZmPubTool.getString_NullAsTrimZeroLen(ZmPubTool.getInvCodeByInvBasid(item.getCinvbasid()))+error);
////					}
//				}
//			}
//		}
//		
//		
//		newout = doSaveAndSignICBill(newout, paraVo.m_currentDate, isAutoSign,isRet);
//		if(isRet)
//			return newout;
//		return null;
//	}
//	
//	/**
//	 * mlr 跨公司出库 设置存货信息
//	 * @param newout
//	 * @throws BusinessException 
//	 */
//	private void setPk_invmandoc(GeneralBillVO newout) throws BusinessException {
//		if(newout==null ||newout.getParentVO()==null || newout.getChildrenVO()==null || newout.getChildrenVO().length==0){
//			return;
//		}
//		String pk_corp=PuPubVO.getString_TrimZeroLenAsNull(newout.getParentVO().getAttributeValue("pk_corp"));
//		GeneralBillItemVO[] items=newout.getItemVOs();
//		for(int i=0;i<items.length;i++){
//			items[i].setCinventoryid(getPk_invmandoc(items[i],pk_corp));
//		}
//		newout.setChildrenVO(items);
//	}
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
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鸡西矿业）自动保存库存单据  外系统推式保存
//	 * 2011-9-9下午05:48:28
//	 * @param bill 库存单据
//	 * @param date 当前日期
//	 * @param isAutoSign 是否自动签字
//	 * @param isRet 是否返回保存后的单据
//	 * @return
//	 * @throws BusinessException
//	 */
//	public GeneralBillVO doSaveAndSignICBill(
//			GeneralBillVO bill,
//			String date,
//			boolean isAutoSign,
//			boolean isRet) 
//	throws BusinessException{
//		
//		ZmPubTool.dealIcGenBillVO(bill);
//		
//		String s_billtype = PuPubVO.getString_TrimZeroLenAsNull(
//				bill.getParentVO().getAttributeValue("cbilltypecode"));
//		
//		pushBillDoAction(date, bill, ZmPubTool.getIcBillSaveActionName(s_billtype));
//		
//		GeneralBillVO[] bills = null;
//		
//		String clastbillid = ZmPubTool.getString_NullAsTrimZeroLen(
//				bill.getChildrenVO()[0].getAttributeValue("csourcebillhid"));
//		String clastbilltyp = ZmPubTool.getString_NullAsTrimZeroLen(
//				bill.getChildrenVO()[0].getAttributeValue("csourcetype"));//add by yf 2012-07-05修改原因：平衡调剂单需要推一组退库出入库单，和一组出入库单
//		if(isRet){
//			//add by yf 2012-07-05修改原因：平衡调剂单需要推一组退库出入库单，和一组出入库单
//			if(clastbilltyp.equalsIgnoreCase("PN04")){
//				String billid = ZmPubTool.getString_NullAsTrimZeroLen(
//						bill.getChildrenVO()[0].getAttributeValue("cgeneralhid"));
//				bills = queryNextBillsByID(billid, s_billtype);
//			}else{
//				bills = queryNextBillsBySourceID(clastbillid, s_billtype);
//			}
//			//add end
//		}
//		if(isAutoSign){
//			//			重新查询出 其他出库单	
//	        if(!isRet){
//	        	bills = queryNextBillsBySourceID(clastbillid, s_billtype);
//	        }
//			if(bills == null ||bills.length == 0 || bills.length>1){
//				throw new BusinessException("数据处理异常");
//			}
//			for(GeneralBillVO bill2:bills){
//				bill2.getParentVO().setAttributeValue("coperatoridnow", bill.getParentVO().getAttributeValue("coperatoridnow"));
//			}
//			pushBillDoActions(date, bills, "SIGN");
//		}
//		if(isRet)
//		return bills[0];
//		return null;
//	}
//	
//	/**
//	 * 
//	 * @author yf
//	 * @说明：（鸡西矿业）根据单据ID查询 库存单据
//	 * 2012-7-5上午09:50:40
//	 * @param ID
//	 * @return
//	 * @throws BusinessException
//	 */
//	public GeneralBillVO[] queryNextBillsByID(String ID,String icbilltype) throws BusinessException{
//		GeneralBillVO[] bills = null;
//		String where  = " head.cgeneralhid = '"+ID+"' and cbilltypecode = '"+icbilltype+"'";
//		QryConditionVO voCond = new QryConditionVO(where);
//	    ArrayList alListData = (ArrayList)queryBills(icbilltype, voCond);
//	    if(alListData == null || alListData.size() == 0)
//	    	return null;
//	    bills = new GeneralBillVO[alListData.size()];
//	    alListData.toArray(bills);
//		return bills;
//	}
//}