package nc.ui.zmpub.pub.bill ;
import java.awt.Container;
import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.BillSourceDLG2;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 
 * 支持单表模式参照框
 * @author mlr

 */
public abstract class MBillSourceDLG2 extends BillSourceDLG2 {
	private static final long serialVersionUID = -2082621589873712775L;
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
	public ReportBaseVO[] queryHeadAndBodyVOs(String strWhere,
			String pk_invbasdocName, String pk_invmandocName) throws Exception {
		String headVoName = getUIController().getBillVoName()[1];
		String bodyVoName = getUIController().getBillVoName()[2];
		SuperVO headVo = (SuperVO) Class.forName(headVoName).newInstance();
		SuperVO bodyVo = (SuperVO) Class.forName(bodyVoName).newInstance();
		String sql = getQuerySql(headVo, bodyVo, strWhere, pk_invbasdocName,
				pk_invmandocName);
		ReportBaseVO[] vos = null;
		Class[] ParameterTypes = new Class[] { String.class, };
		Object[] ParameterValues = new Object[] {sql };
		Object o = LongTimeTask.calllongTimeService("zmpub", this,
				"正在查询...", 1, "nc.bs.zmpub.pub.report.ReportDMO", null,
				"queryVOBySql1", ParameterTypes, ParameterValues);
		if (o != null) {
			vos = (ReportBaseVO[]) o;
		}
		return vos;
	}

	/**
	 * 获得表头和表体的区分标示符
	 * @return
	 */
	public  abstract String getHeadSpitChar();
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
		String sql=" select ";
		for(int i=0;i<headVo.getAttributeNames().length;i++){
			if(PuPubVO.getString_TrimZeroLenAsNull(getHeadSpitChar())!=null){
			sql=sql+headVo.getTableName()+"."+headVo.getAttributeNames()[i]+" "+headVo.getAttributeNames()[i]+getHeadSpitChar()+",";
			}else{
				sql=sql+headVo.getTableName()+"."+headVo.getAttributeNames()[i]+" "+headVo.getAttributeNames()[i]+",";
			}
		}
		  sql = sql+"  "+ bodyVo.getTableName()+".* from " + headVo.getTableName() + " join "
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
	UIMenuItem menuSelect = new UIMenuItem("整单选取");//整单选取  Select

	UIMenuItem menuUnselect = new UIMenuItem("整单取消");//整单取消 ,,Unselect"

	UIMenuItem menuSperator = new UIMenuItem("---------");
	/**
	 * @return
	 */
	protected BillListPanel getBillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();

				ivjbillListPanel.setName("billListPanel");

				//装载模板
				String nodekey = getNodeKey() == null ? DEFAULT_NODEKEY : getNodeKey();//加载单据模板的nodekey

				nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(getRefBillType(), null,
				/*getBusinessType(),*/getOperator(), getPkCorp(), nodekey);

				BillListData billDataVo = new BillListData(vo);

				//更改主表的显示位数
				String[][] tmpAry = getHeadShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				//更改子表的显示位数
				tmpAry = getBodyShowNum();
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				//进行主子隐藏列的判断
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				//隐藏表体
				ivjbillListPanel.getBodyUIPanel().setVisible(false);

				//增加下拉菜单  
				UIMenuItem[] miBody = { menuSelect, menuUnselect, menuSperator,
						getListPanel().getMiAllSelctRow(), getListPanel().getMiCancelAllSelctRow() };
				getListPanel().setMiBody(miBody);
				getListPanel().setBBodyMenuShow(true);

				getListPanel().addTableBodyMenu();

				getListPanel().setTotalRowShow(true);

				ivjbillListPanel.setMultiSelect(true);

			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}
	
	public abstract String getRefBillType() ;
	public abstract  IControllerBase getUIController();

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
	public MBillSourceDLG2(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, parent);
		m_whereStr = getQueryWhere();
		//initialize();
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
	public MBillSourceDLG2(String pkField, String pkCorp, String operator, String funNode,
			String queryWhere, String billType, String businessType, String templateId,
			String currentBillType, String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
				currentBillType, nodeKey, userObj, parent);
		m_whereStr = getQueryWhere();
	//	initialize();
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
			
			getBillListPanel().setHeaderValueVO(tmpHeadVo);
			getBillListPanel().getHeadBillModel().execLoadFormula();

			//lj+ 2005-4-5
			//selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000490")/*@res "数据加载失败！"*/);
		}
	}

	/**
	 * "确定"按钮的响应，从界面获取被选单据VO
	 */
	public void onOk() {
		if (getBillListPanel().getHeadBillModel().getRowCount() > 0) {
			ReportBaseVO[] selectedvos = (ReportBaseVO[]) getSelectVOs();
			if (selectedvos.length == 0)
				return;

			try {
//				//XXX:public类直接实例化
//				IQueryDataConvertor qryDataConverotr = (IQueryDataConvertor) Class.forName(
//						m_expandvoClzName).newInstance();

				AggregatedValueObject[] selectedAggVOs = convertFlatVOs(selectedvos);
				retBillVo = selectedAggVOs.length > 0 ? selectedAggVOs[0] : null;
				retBillVos = selectedAggVOs;

			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}

		}
		this.closeOK();
	}
	/**
	 * 将 单表vo 转化为 聚合vo
	 * @param selectedvos
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AggregatedValueObject[] convertFlatVOs(
			ReportBaseVO[] selectedvos) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	   if(selectedvos==null|| selectedvos.length==0)
		   return null;	
		String billvo=getUIController().getBillVoName()[0];
		String headVoName = getUIController().getBillVoName()[1];
		String bodyVoName = getUIController().getBillVoName()[2];
		SuperVO headVo1 = (SuperVO) Class.forName(headVoName).newInstance();
		String[] hspilts=new String[headVo1.getAttributeNames().length];
		for(int i=0;i<hspilts.length;i++){
			if(PuPubVO.getString_TrimZeroLenAsNull(getHeadSpitChar())!=null){
				hspilts[i]=headVo1.getAttributeNames()[i]+getHeadSpitChar();
			}else{
				hspilts[i]=headVo1.getAttributeNames()[i];
			}
		}		
		ReportBaseVO[][] reptss= (ReportBaseVO[][]) SplitBillVOs.getSplitVOs(selectedvos, hspilts);
		if(reptss==null || reptss.length==0)
			return null;	
		AggregatedValueObject[]  billvos=new AggregatedValueObject[reptss.length];
		for(int i=0;i<reptss.length;i++){
			billvos[i]=(AggregatedValueObject) Class.forName(billvo).newInstance();
			ReportBaseVO hvo=reptss[i][0];
			SuperVO headVo = (SuperVO) Class.forName(headVoName).newInstance();
			copyReportBaseVOtoSuperVO(hvo,headVo,getHeadSpitChar());
			billvos[i].setParentVO(headVo);
			SuperVO[] bvos=(SuperVO[]) java.lang.reflect.Array.newInstance(Class.forName(bodyVoName), reptss[i].length);			
			for(int j=0;j<reptss[i].length;j++){
				ReportBaseVO bvo=reptss[i][j];
				bvos[j]=(SuperVO) Class.forName(bodyVoName).newInstance();				
				copyReportBaseVOtoSuperVO(bvo,bvos[j],null);				
			}
			billvos[i].setChildrenVO(bvos);
		}			
		return spilt(billvos);
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
		if( (spiltFields==null || spiltFields.length==0) && (spiltFields1==null || spiltFields1.length==0)){
			return selectedBillVOs;
		}
		spiltBillVos=SplitBillVOs.getSplitVOs(
				getUIController().getBillVoName()[0],
				getUIController().getBillVoName()[1], 
				getUIController().getBillVoName()[2], 
				selectedBillVOs, 
				spiltFields,
				spiltFields1);	
		return spiltBillVos;
	}
	/**
	 * 
	 * @param hvo
	 * @param headVo
	 * @param charid ReportBaseVO的字段扩展字符
	 */
	public void copyReportBaseVOtoSuperVO(ReportBaseVO hvo, SuperVO headVo,String charid) {
		if(hvo==null || headVo==null )
			return;
		for(int i=0;i<headVo.getAttributeNames().length;i++){
			Object obj=null;
			if(PuPubVO.getString_TrimZeroLenAsNull(charid)==null){
				obj=hvo.getAttributeValue(headVo.getAttributeNames()[i]);
			}else{
				obj=hvo.getAttributeValue(headVo.getAttributeNames()[i]+charid);
			}
			headVo.setAttributeValue(headVo.getAttributeNames()[i], obj);
		}
	}
	public CircularlyAccessibleValueObject[] getSelectVOs() {
		ReportBaseVO[] bodyvos = (ReportBaseVO[]) getBillListPanel().getBillListData()
				.getHeadBillModel().getBodyValueVOs(ReportBaseVO.class.getName());
		bodyvos[0].getAttributeValue("vbillstatusx");
		ArrayList<ReportBaseVO> resultvos = new ArrayList<ReportBaseVO>();
		for (int i = 0; i < bodyvos.length; i++) {
			if (getListPanel().getTableModel().getRowState(i) == BillModel.SELECTED) {
				resultvos.add(bodyvos[i]);
			}
		}
		return (ReportBaseVO[]) resultvos
				.toArray(new ReportBaseVO[0]);
	}

	public abstract String getPk_invbasdocName();

	public abstract String getPk_invmandocName() ;
//    /**
//     * 进行分担
//     * @作者：mlr
//     * @说明：完达山物流项目 
//     * @时间：2012-6-25下午01:27:29
//     * @param selectedBillVOs
//     * @return
//     */
//	public  AggregatedValueObject[] spilt(
//			AggregatedValueObject[] selectedBillVOs) {
//		if(selectedBillVOs==null || selectedBillVOs.length==0)
//			return selectedBillVOs;
//		spiltBillVos=SplitBillVOs.getSplitVOs(
//				getUIController().getBillVoName()[0],
//				getUIController().getBillVoName()[1], 
//				getUIController().getBillVoName()[2], 
//				selectedBillVOs, 
//				spiltFields,
//				spiltFields1);	
//		return spiltBillVos;
//	}
}
