package nc.bs.zmpub.ic.pub;

import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;

/**
 * 转换类(ic模块)
 */

public class ChangeToICBill {
	
	
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鸡西矿业）vo转换生成库存单据vo
	 * 2011-9-8下午01:59:40
	 * @param billVO  待转换的单据vo
	 * @param sVoName 待转换单据vo类型
	 * @param sHeadVoName 待转换单据vo表头类型
	 * @param sBodyVoName 待转换单据vo表体类型
	 * @param saHeadKey 待转换单据vo 分单表头依据字段
	 * @param saBodyKey 待转换单据vo 分单表体依据字段
	 * @param ctarbilltype 目标单据类型
	 * @param paraVo 参量vo
	 * @return
	 * @throws Exception
	 */
	public AggregatedValueObject[] transAndGenBillVO(AggregatedValueObject billVO,
			Class sVoClass,
			Class sHeadVoClass, 
			Class sBodyVoClass,
			String[] saHeadKey,
			String[] saBodyKey,
			String ctarbilltype,
			PfParameterVO paraVo) throws BusinessException {
		
		if(billVO==null){
			return null;
		}
		
		if(PuPubVO.getString_TrimZeroLenAsNull(ctarbilltype)==null)
			throw new BusinessException("目标单据类型为空");
		
		String csoucebilltype = paraVo.m_billType;
		if(PuPubVO.getString_TrimZeroLenAsNull(csoucebilltype)==null)
			throw new BusinessException("待转换单据【单据类型】为空");

		//分单
		AggregatedValueObject[] vos = null;
		if(saBodyKey!=null&&saBodyKey!=null)
		vos = SplitBillVOs.getSplitVO(
				sVoClass.getName(), sHeadVoClass.getName(),sBodyVoClass.getName(),				
				billVO, saHeadKey, saBodyKey);
		else{
			vos = (AggregatedValueObject[])java.lang.reflect.Array.newInstance(sVoClass, 1);
			vos[0] = billVO;
		}
			
		
		//				
		AggregatedValueObject[] vo = PfUtilTools.runChangeDataAry(csoucebilltype,ctarbilltype,vos,paraVo); 
		return vo;
	}
	
	/**
	 * @功能：取消签字动作入口
	 */
//	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject value,String coperator,String date) throws Exception {
//		if(value == null ){
//			return null;
//		}
//		GeneralBillVO[] billvo = null;
//		//成产品入库
//		GeneralBillHeaderVO outhvo = (GeneralBillHeaderVO) value.getParentVO();
//		
////		String rklx = PuPubVO.getString_TrimZeroLenAsNull(outhvo.getAttributeValue(IcSnPubConst.sn_rklx));
////		if(rklx == null)
////			throw new BusinessException("入库类型为空");
//		String s_billtype = ScmConst.m_otherIn;
////	    if(rklx.equalsIgnoreCase(IcSnPubConst.sn_rklx_otherin))
////	    	s_billtype = ScmConst.m_otherIn;
//		
//		String where  = " csourcebillhid = '"+outhvo.getPrimaryKey()+"' and cbilltypecode = '"+s_billtype+"'";
//		QryConditionVO voCond = new QryConditionVO(where);
//	    ArrayList alListData = (ArrayList)queryBills(s_billtype, voCond);
//		if(alListData!=null && alListData.size()>0){
//			for(int i = 0 ;i<alListData.size();i++){
//				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
//				gvo.getHeaderVO().setCoperatoridnow(coperator);//增加当前操作员，业务员PK加锁
//			}
//			billvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
//		}
//		return billvo;
//	}
//	/**
//	 * @功能:其它出库单分单
//	 */
//	public AggregatedValueObject[] splitGeneralBillVO(AggregatedValueObject billVO)throws BusinessException{
//		check(billVO);
////		AggregatedValueObject[] sourceVO = {billVO};
//		AggregatedValueObject[]  vos = SplitBillVOs.getSplitVO(GeneralBillVO.class.getName(),
//				GeneralBillHeaderVO.class.getName(),
//				GeneralBillItemVO.class.getName(),billVO,null,IcSnPubConst.sn_outtoin_split);
//		return vos;
//	}
//	/**
//	 * @功能 :表体验证[其它出库单] 入库库存组织、入库仓库
//	 */
//	public void check(AggregatedValueObject voSource) throws BusinessException{
//		if(voSource == null || voSource.getChildrenVO() == null || voSource.getChildrenVO().length == 0){
//			throw new BusinessException("签字单据为空或者表体为空，请指定单据");
//		}
//		if(! (voSource  instanceof GeneralBillVO)){
//			throw new BusinessException("签字单据类型错误");
//		}
//		GeneralBillItemVO[] items = ((GeneralBillVO)voSource).getItemVOs();
//		for(GeneralBillItemVO item : items){
//			Object o1 =  item.getAttributeValue("pk_defdoc"+IcSnPubConst.sn_rk_warehouse_index);//入库仓库
//			Object o2 = item.getAttributeValue("pk_defdoc"+IcSnPubConst.sn_rk_calbody_index);//入库库存组织
//			if(o1 == null || o2 == null){
//				throw new BusinessException("表体未指定入库库存组织，入库仓库");
//			}
//		}
//	}
//	
	
	
//	/**
//	 * 其他出库转换为其他入库
//	 * @param vos
//	 * @param coperator
//	 * @param date
//	 * @return
//	 * @throws BusinessException
//	 */
//	private GeneralBillVO[] getOtherInBill(GeneralBillVO[] vos,String coperator,UFDate date) throws BusinessException{
//		if(vos == null||vos.length ==0)
//			return null;
//		GeneralBillVO[] invos = new GeneralBillVO[vos.length];
//		int index = 0;
//		GeneralBillVO invo = null;
//		GeneralBillItemVO[] items = null;
//		for(GeneralBillVO vo:vos){
//			invo = (GeneralBillVO)vo.clone();
//			invo.getHeaderVO().setCbilltypecode(ScmConst.m_otherIn);
//			invo.getHeaderVO().setVbillcode(null);
////			调整库存组织和仓库
////			invo.getHeaderVO().setPk_calbody();
//			items = invo.getItemVOs();
//			if(items == null || items.length ==0)
//				throw new BusinessException("表体数据为空");
//			invo.getHeaderVO().setPk_calbody(PuPubVO.getString_TrimZeroLenAsNull(items[0].getAttributeValue("pk_defdoc"+IcSnPubConst.sn_rk_calbody_index)));
//			invo.getHeaderVO().setCwarehouseid(PuPubVO.getString_TrimZeroLenAsNull(items[0].getAttributeValue("pk_defdoc"+IcSnPubConst.sn_rk_warehouse_index)));
//			invo.getHeaderVO().setAttributeValue(IcSnPubConst.sn_rklx, null);
//			invo.getHeaderVO().setAttributeValue(IcSnPubConst.sn_rklx2, null);
//			
//			for(GeneralBillItemVO item:items){
//				item.setNinnum(item.getNoutnum());
//				item.setNshouldinnum(item.getNshouldoutnum());
//				item.setNinassistnum(item.getNoutassistnum());
//				item.setNneedinassistnum(item.getNshouldoutassistnum());
//				item.setCsourcetype(ScmConst.m_otherOut);
//				item.setCsourcebillbid(item.getPrimaryKey());
//				item.setCsourcebillhid(item.getCgeneralhid());
//				item.setNoutnum(null);
//				item.setNoutassistnum(null);
//				item.setNshouldoutassistnum(null);
//				item.setNshouldoutnum(null);
//				item.setAttributeValue("vuserdef"+IcSnPubConst.sn_rk_calbody_index, null);
//				item.setAttributeValue("vuserdef"+IcSnPubConst.sn_rk_warehouse_index, null);
//			}
//			Sn2PubTool.dealBillVO(invo, coperator, date);
//			
//			invos[index] = invo;
//			index ++;
//		}
//		
//		return invos;
//	}
	
//	public void setSpcGenBillVO(AggregatedValueObject[] billVO,String coperator,String date){
//		if(billVO == null || billVO.length == 0){
//			return;
//		}
//		for(AggregatedValueObject b : billVO){
//			GeneralBillVO bill = (GeneralBillVO)b;
////			bill.setGetPlanPriceAtBs(false);//不需要查询计划价
//////			bill.getHeaderVO().setCoperatorid(coperator);//制单人
////			bill.getHeaderVO().setCoperatoridnow(coperator);//当前操作人///业务加锁，锁定当前操作人员
////			bill.getHeaderVO().setDbilldate(new UFDate(date));//单据日期
////			bill.getHeaderVO().setStatus(VOStatus.NEW);//单据新增状态
////			if(bill.getItemVOs()==null || bill.getItemVOs().length == 0){
////				continue;
////			}
////			for(int i = 0 ;i<bill.getItemVOs().length;i++){
////				bill.getItemVOs()[i].setCrowno(String.valueOf((i+1)*10));//行号
////				bill.getItemVOs()[i].setStatus(VOStatus.NEW);//单据新增状态
////				if(bill.getItemVOs()[i].getDbizdate() == null){
////					bill.getItemVOs()[i].setDbizdate(new UFDate(date));//业务日期
////				}
////			}
//			
//			Sn2PubTool.dealBillVO(bill, coperator, new UFDate(date));
//		}
//	}
//	//回写其他出库单
//	public void reWrite(AggregatedValueObject[] billVO,boolean flag)throws BusinessException{
//		if(billVO == null || billVO.length == 0){
//			return;
//		}
//		ArrayList<String> list = new ArrayList<String>();
//		String sql = "";
//		for(AggregatedValueObject b : billVO){
//			GeneralBillVO bill = (GeneralBillVO)b;
//			if(bill.getItemVOs()==null || bill.getItemVOs().length == 0){
//				continue;
//			}
//			for(int i = 0 ;i<bill.getItemVOs().length;i++){
//				GeneralBillItemVO item = bill.getItemVOs()[i];
//				String bid = item.getCsourcebillbid();
//				UFDouble  ninassistnum = item.getNinassistnum()==null ? UFDouble.ZERO_DBL:item.getNinassistnum();//实入辅数量
//				UFDouble  ninnum  = item.getNinnum()==null ? UFDouble.ZERO_DBL:item.getNinnum();//实入数量
//				if(flag){//签字  ncorrespondastnum  累计出库辅数量  ncorrespondnum  累计出库数量
//					 sql = " update ic_general_b set ncorrespondastnum = nvl(ncorrespondastnum,0)+ "+ninassistnum+",ncorrespondnum  = nvl(ncorrespondnum,0)+ "+ninnum+" where cgeneralbid = '"+bid+"'  ";
//				}else{//取消签字  ncorrespondastnum  累计出库辅数量  ncorrespondnum  累计出库数量
//					 sql = " update ic_general_b set ncorrespondastnum = nvl(ncorrespondastnum,0)- "+ninassistnum+",ncorrespondnum  = nvl(ncorrespondnum,0)- "+ninnum+"  where cgeneralbid = '"+bid+"'  ";
//				}
//				list.add(sql);
//			}
//		}
//		try{
//			for(String s: list){
//				dao.executeUpdate(s);
//			}
//		}catch(Exception e){
//			throw new BusinessException("回写其它出库单累计出库数量、累计出库辅数量错误");
//		}
//	}
}
