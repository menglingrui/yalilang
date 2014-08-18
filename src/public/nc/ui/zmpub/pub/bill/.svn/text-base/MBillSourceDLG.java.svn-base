package nc.ui.zmpub.pub.bill ;
import java.awt.Container;
import java.util.List;

import nc.bs.logging.Logger;
import nc.itf.zmpub.pub.ISonVO;
import nc.itf.zmpub.pub.ISonVOH;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;
import nc.vo.zmpub.pub.tool.ZmPubTool;
/**
 * mlr
 *支持表头表体多选
支持权限数据过滤
支持查询按表头表体过滤数据
支持分担

 */
public abstract class MBillSourceDLG extends MutiBillSourceDLG {
	private static final long serialVersionUID = -6568321766139263187L;
	//返回分担后的vo数组
	protected AggregatedValueObject[] spiltBillVos = null;
	//分担维度 表头
	protected String[] spiltFields=null;
	//分担维度 表体
	protected String[] spiltFields1=null;
	public String[] getSpiltFields() {
		return spiltFields;
	}
	public void setSpiltFields(String[] spiltFields) {
		this.spiltFields = spiltFields;
	}
	public AggregatedValueObject[] getSpiltBillVos() {
		return spiltBillVos;
	}
	public void setSpiltBillVos(AggregatedValueObject[] spiltBillVos) {
		this.spiltBillVos = spiltBillVos;
	}

	public String[] getSpiltFields1() {
		return spiltFields1;
	}
	public void setSpiltFields1(String[] spiltFields1) {
		this.spiltFields1 = spiltFields1;
	}
	public SuperVO[] queryHeadAndBodyVOs(String strWhere,
			String pk_invbasdocName, String pk_invmandocName) throws Exception {
		if(getUIController()==null || getUIController().getBillVoName()==null ||
				getUIController().getBillVoName().length==0){
			SourBillInfor infor=getSourBillInfor();
			if(infor ==null){
				return null;
			}
			String sql = getQuerySql(infor, strWhere,
					pk_invbasdocName, pk_invmandocName); 
			//add jay
			SuperVO [] vos=null;
			Class[] ParameterTypes = new Class[] { String.class, String.class };
			Object[] ParameterValues = new Object[] { "nc.vo.ct.pub.ManageHeaderVO", sql };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.bill.ZmPubBO", null,
					"queryByHeadAndBodyVOs", ParameterTypes, ParameterValues);
			if (o != null) {
				vos = (SuperVO[]) o;			
			}	
			return vos;
			
		} else {
			String headVoName = getUIController().getBillVoName()[1];
			String bodyVoName = getUIController().getBillVoName()[2];
			SuperVO headVo = (SuperVO) Class.forName(headVoName).newInstance();
			SuperVO bodyVo = (SuperVO) Class.forName(bodyVoName).newInstance();
			String sql = getQuerySql(headVo, bodyVo, strWhere,
					pk_invbasdocName, pk_invmandocName);

			SuperVO[] vos = null;
			Class[] ParameterTypes = new Class[] { String.class, String.class };
			Object[] ParameterValues = new Object[] { headVoName, sql };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.bill.ZmPubBO", null,
					"queryByHeadAndBodyVOs", ParameterTypes, ParameterValues);
			if (o != null) {
				vos = (SuperVO[]) o;
				
			}
			return vos;
		}
		
	}
		
	
	
	public String getQuerySql(SourBillInfor infor, String strWhere,
			String pk_invbasdocName, String pk_invmandocName) {
		
		String sql = " select " + infor.getHeadtable() + ".* from "
				+ infor.getHeadtable() + " join " + infor.getBodytables()[0]
				+ " on " + infor.getHeadtable()  + "."
				+ infor.getHeadpk() + " = " + infor.getBodytables()[0] + "."
				+ infor.getHeadpk();
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_invmandocName) != null
				&& PuPubVO.getString_TrimZeroLenAsNull(pk_invbasdocName) != null) {
			sql = sql + " join bd_invbasdoc inv on inv.pk_invbasdoc= "
					+ infor.getBodytables()[0] + "." + pk_invbasdocName
					+ " join bd_invcl cl on cl.pk_invcl =inv.pk_invcl ";
		}
		sql = sql + " where " + " isnull(" + infor.getHeadtable()
				+ ".dr,0)=0 and isnull(" + infor.getBodytables()[0] + ".dr,0)=0 ";
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_invmandocName) != null
				&& PuPubVO.getString_TrimZeroLenAsNull(pk_invbasdocName) != null) {
			sql = sql + " and isnull(inv.dr,0)=0" + " and isnull(cl.dr,0)=0 ";
		}
		if (strWhere != null && strWhere.length() != 0)
			sql = sql + " and " + strWhere;
		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();
		sql = sql + " and " + infor.getHeadtable()+ ".pk_corp ='" + pk_corp
				+ "'";
		return sql;
}
	/**
	 * 获取支持表体查询的sql 支持按存货分类 和 物料档案 进行用户资源权限过滤
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2012-1-11下午03:12:31
	 * @param headVo
	 * @param bodyVo
	 * @return
	 */
	public String getQuerySql(SuperVO headVo, SuperVO bodyVo, String strWhere,
			String pk_invbasdocName, String pk_invmandocName) {
		String sql = " select "+ headVo.getTableName()+".* from " + headVo.getTableName() + " join "
				+ bodyVo.getTableName() + " on " + headVo.getTableName() + "."
				+ headVo.getPKFieldName() + " = " + bodyVo.getTableName() + "."
				+ bodyVo.getParentPKFieldName();
				if(PuPubVO.getString_TrimZeroLenAsNull(pk_invmandocName)!=null &&
						PuPubVO.getString_TrimZeroLenAsNull(pk_invbasdocName)!=null){
				sql=sql+ " join bd_invbasdoc inv on inv.pk_invbasdoc= "
				+ bodyVo.getTableName() + "." + pk_invbasdocName
				+ " join bd_invcl cl on cl.pk_invcl =inv.pk_invcl " ;
				}
				sql=sql+" where "
				+ " isnull(" + headVo.getTableName() + ".dr,0)=0 and isnull("
				+ bodyVo.getTableName() + ".dr,0)=0 ";
				if(PuPubVO.getString_TrimZeroLenAsNull(pk_invmandocName)!=null &&
						PuPubVO.getString_TrimZeroLenAsNull(pk_invbasdocName)!=null){
					sql=sql+ " and isnull(inv.dr,0)=0" + " and isnull(cl.dr,0)=0 ";
				}
		if (strWhere != null && strWhere.length() != 0)
			sql = sql + " and " + strWhere;
		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();
		sql=sql+" and "+headVo.getTableName()+".pk_corp ='"+pk_corp+"'";
		return sql;
	}
	
	public abstract  IControllerBase getUIController();
	
	
	public  SourBillInfor getSourBillInfor(){
		return null;
	}
    
	/**
	 * 根据类名称，where语句构造参照界面
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param parent
	 */
	public MBillSourceDLG(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}

	/**
	 * 根据类名称，where语句构造参照界面
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 */
	public MBillSourceDLG(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, nodeKey, userObj, parent);
		m_whereStr = getQueryWhere();
		initialize();
	}
	public void loadHeadData() {
		try {
			//利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and (" + getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			CircularlyAccessibleValueObject[] tmpHeadVo=this.queryHeadAndBodyVOs(tmpWhere, getPk_invbasdocName(), getPk_invmandocName());
//			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.queryHeadAllData(getBillType(),
//					businessType, tmpWhere);

			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000490")/*@res "数据加载失败！"*/);
		}
	}


	public abstract String getPk_invbasdocName();

	public abstract String getPk_invmandocName() ;
	/**
	 * "确定"按钮的响应，从界面获取被选单据VO
	 */
	public void onOk() {
		if (getbillListPanel().getHeadTable().getSelectedRowCount()>0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
					m_billHeadVo, m_billBodyVo);
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
		//	retBillVos=spilt(selectedBillVOs);
			setSonDatas(retBillVos);
			this.closeOK();
		}			
	}
	/**
	 * 设置孙表信息
	 * @param retBillVos
	 */
    public void setSonDatas(AggregatedValueObject[] retBillVos) {
		if(retBillVos==null || retBillVos.length==0)
			return;
		try {	
		for(int i=0;i<retBillVos.length;i++){
			HYBillVO billvo=(HYBillVO) retBillVos[i];
			if(billvo instanceof ISonVOH){
				String sonclassname=((ISonVOH)billvo).getSonClass();
			
				Class  sonclass=Class.forName(sonclassname);
			    
				SuperVO  sonvo=(SuperVO) sonclass.newInstance();
				
				ISonVO[] bvos=(ISonVO[]) billvo.getChildrenVO();
				HYChildSuperVO[] sbvos=(HYChildSuperVO[]) billvo.getChildrenVO();
				if(sbvos!=null&& sbvos.length>0){
					for(int j=0;j<sbvos.length;j++){
						SuperVO[] svos=HYPubBO_Client.queryByCondition(sonclass,
								" isnull(dr,0)=0 " +
								" and "+sbvos[j].getPKFieldName()+"='"+sbvos[j].getPrimaryKey()+"'");
						List list=null;
						if(svos!=null&&svos.length>0){
							list=ZmPubTool.getList(svos);						
					    }
						bvos[j].setSonVOS(list);
				   }
			}
		 }
		}
		}
	  catch (Exception e) {		
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, "对象框", e.getMessage());
		}
	}
	/**
     * 进行分担
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-6-25下午01:27:29
     * @param selectedBillVOs
     * @return
     */
	public  AggregatedValueObject[] spilt(
			AggregatedValueObject[] selectedBillVOs) {
		if(selectedBillVOs==null || selectedBillVOs.length==0)
			return selectedBillVOs;
		spiltBillVos=SplitBillVOs.getSplitVOs(
				getUIController().getBillVoName()[0],
				getUIController().getBillVoName()[1], 
				getUIController().getBillVoName()[2], 
				selectedBillVOs, 
				spiltFields,
				spiltFields1);	
		return spiltBillVos;
	}

	@Override
	public String getHeadTableCode() {
	
		return null;
	}


	/**
	 * 根据主表获取子表数据
	 * 
	 * @param row
	 *            选中的表头行
	 */
	public void loadBodyData(int row) {
		try {
			// 获得主表ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row,
					getpkField()).toString();
			// 查询子表VO数组
//			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client
//					.queryBodyAllData(getBillType(), id, getBodyCondition());
		   
			IControllerBase ctrl=getUIController();
			if(ctrl==null){
				return;
			}
			String sqlWhere="";
					
			sqlWhere= sqlWhere +getpkField()+ " = '"+id+"' and isnull(dr,0)=0 ";
			if(getBodyCondition()!=null){
				sqlWhere=sqlWhere+" and "+getBodyCondition();
			}
			Class c=Class.forName(ctrl.getBillVoName()[2])	;
			//查询表体数据
			CircularlyAccessibleValueObject[] tmpBodyVo=HYPubBO_Client.queryByCondition(c, sqlWhere);
			
			// 表头界面数据加载前业务扩展
		//	tmpBodyVo = beforeLoadBodyData(row, tmpBodyVo);

			getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

}
