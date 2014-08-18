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
 *֧�ֱ�ͷ�����ѡ
֧��Ȩ�����ݹ���
֧�ֲ�ѯ����ͷ�����������
֧�ֵַ�

 */
public abstract class MBillSourceDLG extends MutiBillSourceDLG {
	private static final long serialVersionUID = -6568321766139263187L;
	//���طֵ����vo����
	protected AggregatedValueObject[] spiltBillVos = null;
	//�ֵ�ά�� ��ͷ
	protected String[] spiltFields=null;
	//�ֵ�ά�� ����
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
					"���ڲ�ѯ...", 1, "nc.bs.zmpub.pub.bill.ZmPubBO", null,
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
					"���ڲ�ѯ...", 1, "nc.bs.zmpub.pub.bill.ZmPubBO", null,
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
	 * ��ȡ֧�ֱ����ѯ��sql ֧�ְ�������� �� ���ϵ��� �����û���ԴȨ�޹���
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2012-1-11����03:12:31
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
	 * ���������ƣ�where��乹����ս���
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
	 * ���������ƣ�where��乹����ս���
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
			//���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
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
					"UPPpfworkflow-000237")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"pfworkflow", "UPPpfworkflow-000490")/*@res "���ݼ���ʧ�ܣ�"*/);
		}
	}


	public abstract String getPk_invbasdocName();

	public abstract String getPk_invmandocName() ;
	/**
	 * "ȷ��"��ť����Ӧ���ӽ����ȡ��ѡ����VO
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
	 * ���������Ϣ
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
			MessageDialog.showErrorDlg(this, "�����", e.getMessage());
		}
	}
	/**
     * ���зֵ�
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-6-25����01:27:29
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
	 * ���������ȡ�ӱ�����
	 * 
	 * @param row
	 *            ѡ�еı�ͷ��
	 */
	public void loadBodyData(int row) {
		try {
			// �������ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row,
					getpkField()).toString();
			// ��ѯ�ӱ�VO����
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
			//��ѯ��������
			CircularlyAccessibleValueObject[] tmpBodyVo=HYPubBO_Client.queryByCondition(c, sqlWhere);
			
			// ��ͷ�������ݼ���ǰҵ����չ
		//	tmpBodyVo = beforeLoadBodyData(row, tmpBodyVo);

			getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

}
