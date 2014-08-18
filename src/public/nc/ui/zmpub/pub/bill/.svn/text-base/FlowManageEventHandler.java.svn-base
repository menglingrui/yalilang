package nc.ui.zmpub.pub.bill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.zmpub.pub.ISonVO;
import nc.itf.zmpub.pub.ISonVOH;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.version55.print.output.excel.core.ExcelException;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;
import nc.vo.zmpub.pub.consts.ZmpubBtnConst;
import nc.vo.zmpub.pub.tool.PowerGetTool;
/**
 * 审批流基类 支持按表体条件查询 for add mlr 支持用户资源权限过滤 目前支持 存货分类 和 物料档案
 * 支持主子孙表的处理
 * @author zhf
 */
public  class FlowManageEventHandler extends ManageEventHandler {
	private SourceBillFlowDlg soureDlg = null;
	private BillItem[] backitems=null;	
	public BillItem[] getBackitems() {
		return backitems;
	}

	public void setBackitems(BillItem[] backitems) {
		this.backitems = backitems;
	}

	public FlowManageEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected BillListPanel getBillListPanel() {
		return getBillListPanelWrapper().getBillListPanel();
	}

	protected BillCardPanel getBillCardPanel() {
		return getBillCardPanelWrapper().getBillCardPanel();
	}

	protected BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	protected BillListPanelWrapper getBillListPanelWrapper() {
		return getBillManageUI().getBillListWrapper();
	}
	/**
	 * Button的事件响应处理。 创建日期：(2004-1-6 17:20:57)
	 * 
	 * @param bo
	 *            nc.ui.pub.ButtonObject 异常说明。
	 */
	public void onButton(ButtonObject bo) {
           super.onButton(bo);
           onButtonAfter(bo);
	}
	
	public void onButtonAfter(ButtonObject bo) {
		
		
	}
	
	public void onLineChangeAfter(nc.ui.pub.bill.BillEditEvent e){
     
	}

	/**
	 * 复制当前所选择的行
	 * @throws BusinessException 
	 */
	public void copySelectedLines() throws BusinessException {
		CircularlyAccessibleValueObject[] vos= getBillCardPanelWrapper().getCopyedBodyVOs();
			if(vos!=null&&vos.length==0){
				for(int i=0;i<vos.length;i++){
					vos[i].setPrimaryKey(null);
					String name=getUIController().getPkField();
					vos[i].setAttributeValue(name, null);
				}
			}
	}
	protected void onBoCopy() throws Exception {
		getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);

		// 获得数据
		AggregatedValueObject copyVo = getBufferData().getCurrentVOClone();
		// 进行主键清空处理
		copyVo.getParentVO().setPrimaryKey(null);
		if (copyVo instanceof IExAggVO) {
			clearChildPk(((IExAggVO) copyVo).getAllChildrenVO());
		} else {
			clearChildPk(copyVo.getChildrenVO());
		}
		// 设置为新增处理
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		// 设置单据号
		String noField = getBillUI().getBillField().getField_BillNo();
		BillItem noitem = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem(noField);
		if (noitem != null)
			copyVo.getParentVO().setAttributeValue(noField,
					noitem.getValueObject());
		//如果存在孙表进行孙表数据的复制
		copySonDatas();
		
		// 设置界面数据
		getBillUI().setCardUIData(copyVo);
		getBillUI().setDefaultData();
	}
	/**
	 * 复制孙表数据
	 * @throws Exception 
	 */
	public void copySonDatas() throws Exception {
		if(getBillManageUI() instanceof SonDefBillManageUI){
			SonDefBillManageUI ui=(SonDefBillManageUI) getBillManageUI();		    
			Map<String, Map<String,SuperVO>> map=(Map<String, Map<String, SuperVO>>) ObjectUtils.serializableClone(ui.getHl1());
			Map<String, Map<String,SuperVO>> cmap=new HashMap<String, Map<String,SuperVO>> ();
			if(map==null||map.size()==0)
				return;
			for(String key:map.keySet()){				
				String crowno=key.substring(0,key.length()-20);
				String nkey=crowno+"null";
				Map<String,SuperVO> cnmap=new HashMap<String, SuperVO>();
				cmap.put(nkey, cnmap);
				
				Map<String,SuperVO> smap=map.get(key);
				if(smap!=null&&smap.size()>0){
					for(String skey:smap.keySet()){
						String srowno=skey.substring(20, skey.length());
						String nskey="null"+srowno;
						HYChildSuperVO svo=(HYChildSuperVO) smap.get(skey);
						svo.setAttributeValue(svo.getParentPKFieldName(), null);
						svo.setStatus(VOStatus.NEW);
						svo.setAttributeValue(svo.getPKFieldName(), null);
						cnmap.put(nskey,svo);
					}
				}	
			}	
			ui.setHl1(cmap);
		}
	}

	private void clearChildPk(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		if (vos == null || vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
			vos[i].setPrimaryKey(null);
			if(vos[i] instanceof HYChildSuperVO){
				HYChildSuperVO vo=(HYChildSuperVO) vos[i];
				vo.setCsourcebillcode(null);
				vo.setVsourcebillid(null);
				vo.setVsourcebillrowid(null);
				vo.setVsourcebilltype(null);
				vo.setVlastbillcode(null);
				vo.setVlastbillid(null);
				vo.setVlastbillrowid(null);
				vo.setVlastbilltype(null);
			}
		}
	}
	@Override
	protected void onBoSave() throws Exception {
			dataNotNullValidate();	
			   //支持验证公式mlr
		    if(!getBillCardPanel().getBillData().execValidateFormulas())
		             return ;
		super.onBoSave();
	}
	
	/**
	 * 审批动作,支持批审
	 */
	public void onBoAudit() throws Exception {
		AggregatedValueObject[] billvos = (AggregatedValueObject[]) getBillListPanel().getMultiSelectedVOs(getUIController().getBillVoName()[0], 
				getUIController().getBillVoName()[1],
				getUIController().getBillVoName()[2]);
		if(billvos!=null&&billvos.length!=0 &&getBillManageUI().isListPanelSelected()){
			onBoAuditBanth(billvos);
			onQuery(strwhere);
		}else{
		    super.onBoAudit();
		}
		onBoRefresh();
	}

	public void onQuery(String strwhere2) throws Exception {

		// 支持按表体条件查询
		SuperVO[] queryVos = null;
		try {
			queryVos = queryHeadVOs(strwhere2);
		} catch (Exception e) {
			queryVos = queryHeadAndBodyVOs(strwhere2);
		}
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		strwhere=strwhere2;
	}
	/**
	 * 批审
	 * @param billvos
	 * @throws Exception 
	 */
	private void onBoAuditBanth(AggregatedValueObject[] billvos) throws Exception {
		if(billvos==null || billvos.length==0)
			return ;
		
		
		for(int i=0;i<billvos.length;i++){
			// 获得数据
			AggregatedValueObject modelVo = billvos[i];
			
			setTsAndDr(modelVo);
			
			
			
			setChildVOs(modelVo);
			setCheckManAndDate(modelVo);
			// 如果状态一致则退出
			if (checkVOStatus(modelVo, new int[] { IBillStatus.CHECKPASS })) {
				System.out.println("无效的鼠标处理机制");
				return;
			}
			beforeOnBoAction(IBillButton.Audit, modelVo);
			// *******************
			AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction()
					.approve(modelVo, getUIController().getBillType(),
							getBillUI()._getDate().toString(),
							getBillUI().getUserObject());

			if (PfUtilClient.isSuccess()) {
				afterOnBoAction(IBillButton.Audit, retVo);
				CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
				if (childVos == null)
					modelVo.setParentVO(retVo.getParentVO());
				else
					modelVo = retVo;
//				// 更新列表
//				getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
//				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}

		}			
	}
	
	public void setTsAndDr(AggregatedValueObject modelVo) throws BusinessException {
		BillUIBuffer buffer=getBufferData();
		ArrayList list=(ArrayList) buffer.getRelaSortObject();
		if(list==null || list.size()==0 || modelVo==null){
			return;
		}
		for(int i=0;i<list.size();i++){
			AggregatedValueObject bufVO=(AggregatedValueObject) list.get(i);
			if(bufVO.getParentVO().getPrimaryKey().equals(modelVo.getParentVO().getPrimaryKey())){
				modelVo.getParentVO().setAttributeValue("ts", bufVO.getParentVO().getAttributeValue("ts"));
			}					
		}
	}

	/**
	 * 设置表体数据
	 * @param modelVo
	 * @throws BusinessException 
	 * @throws ClassNotFoundException 
	 */
	private void setChildVOs(AggregatedValueObject modelVo) throws BusinessException, ClassNotFoundException {
	     if(modelVo==null){
	    	 return;
	     }
	     String pk_h=modelVo.getParentVO().getPrimaryKey();
	     CircularlyAccessibleValueObject[] b1vos = (CircularlyAccessibleValueObject[]) HYPubBO_Client.queryAllBodyData(	    		 
	    		 getUIController().getBillType(),
	    		 Class.forName(getUIController().getBillVoName()[2]), pk_h,null);	
	     modelVo.setChildrenVO(b1vos);
	}

	/**
	 * 获得子表数据。 创建日期：(2004-3-11 17:44:14)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject[] childVos = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}
	private void setCheckManAndDate(AggregatedValueObject vo) throws Exception {
		// 放入审批日期、审批人
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(),
				getBillUI()._getDate());
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(),
				getBillUI()._getOperator());
	}
	protected void dataNotNullValidate() throws ValidationException {
		StringBuffer message = null;
		BillItem[] headtailitems = getBillCardPanelWrapper().getBillCardPanel()
				.getBillData().getHeadTailItems();
		if (headtailitems != null) {
			for (int i = 0; i < headtailitems.length; i++) {
				if (headtailitems[i].isNull())
					if (isNULL(headtailitems[i].getValueObject())
							&& headtailitems[i].isShow()) {
						if (message == null)
							message = new StringBuffer();
						message.append("[");
						message.append(headtailitems[i].getName());
						message.append("]");
						message.append(",");
					}
			}
		}
		if (message != null) {
			message.deleteCharAt(message.length() - 1);
			throw new NullFieldException(message.toString());
		}

		// 增加多子表的循环
		String[] tableCodes = getBillCardPanelWrapper().getBillCardPanel()
				.getBillData().getTableCodes(BillData.BODY);
		if (tableCodes != null) {
			for (int t = 0; t < tableCodes.length; t++) {
				String tablecode = tableCodes[t];
				for (int i = 0; i < getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel(tablecode)
						.getRowCount(); i++) {
					StringBuffer rowmessage = new StringBuffer();

					rowmessage.append(" ");
					if (tableCodes.length > 1) {
						rowmessage.append(getBillCardPanelWrapper()
								.getBillCardPanel().getBillData().getTableName(
										BillData.BODY, tablecode));
						rowmessage.append("(");
						// "页签"
						rowmessage.append(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("_Bill", "UPP_Bill-000003"));
						rowmessage.append(") ");
					}
					rowmessage.append(i + 1);
					rowmessage.append("(");
					// "行"
					rowmessage.append(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("_Bill", "UPP_Bill-000002"));
					rowmessage.append(") ");

					StringBuffer errormessage = null;
					BillItem[] items = getBillCardPanelWrapper()
							.getBillCardPanel().getBillData()
							.getBodyItemsForTable(tablecode);
					for (int j = 0; j < items.length; j++) {
						BillItem item = items[j];
						if (item.isShow() && item.isNull()) {// 如果卡片显示，并且为空，才非空校验
							Object aValue = getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(tablecode)
									.getValueAt(i, item.getKey());
							if (isNULL(aValue)) {
								errormessage = new StringBuffer();
								errormessage.append("[");
								errormessage.append(item.getName());
								errormessage.append("]");
								errormessage.append(",");
							}
						}
					}
					if (errormessage != null) {

						errormessage.deleteCharAt(errormessage.length() - 1);
						rowmessage.append(errormessage);
						if (message == null)
							message = new StringBuffer(rowmessage);
						else
							message.append(rowmessage);
						break;
					}
				}
				if (message != null)
					break;
			}
		}
		if (message != null) {
			throw new NullFieldException(message.toString());
		}

	}
	/**
	 * 按钮m_boEdit点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoEdit() throws Exception {
		// 界面没有数据或者有数据但是没有选中任何行
		if (getBufferData().getCurrentVO() == null)
			return;
		SuperVO hvo=(SuperVO) getBufferData().getCurrentVO().getParentVO();
		Integer vbillstatus=PuPubVO.getInteger_NullAs(hvo.getAttributeValue("vbillstatus"), -10);
		if(vbillstatus ==IBillStatus.FREE || vbillstatus==IBillStatus.COMMIT)
		checkMaker1();
		if (getBillManageUI().isListPanelSelected()) {
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBufferData().updateView();
		}
		super.onBoEdit();
	}
	
	protected void onBoEdit1() throws Exception {
		if (getBillManageUI().isListPanelSelected()) {
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBufferData().updateView();
		}
//		//出去右键监听
//		getBillCardPanelWrapper().getBillCardPanel().removeBodyMenuListener();
		super.onBoEdit();
	}
	/**
	 * 校验 只能自己修改自己的单据
	 * @throws BusinessException
	 */
	public void checkMaker() throws BusinessException {
		SuperVO hvo=(SuperVO) getBufferData().getCurrentVO().getParentVO();
		String pk_make=PuPubVO.getString_TrimZeroLenAsNull(hvo.getAttributeValue("voperatorid"));
		String operid=ClientEnvironment.getInstance().getUser().getPrimaryKey();
		if(!pk_make.equals(operid)){
			throw new BusinessException("不能删除他人填制单据");
		}		
	}
	
	/**
	 * 校验 只能自己修改自己的单据
	 * @throws BusinessException
	 */
	public void checkMaker1() throws BusinessException {
		SuperVO hvo=(SuperVO) getBufferData().getCurrentVO().getParentVO();
		String pk_make=PuPubVO.getString_TrimZeroLenAsNull(hvo.getAttributeValue("voperatorid"));
		String operid=ClientEnvironment.getInstance().getUser().getPrimaryKey();
		if(!pk_make.equals(operid)){
			throw new BusinessException("不能修改他人填制单据");
		}		
	}

	private boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
	/*
	 * 增加数据到BillUIBuffer 如果增加到缓存的数据需要处理，则覆盖该方法
	 */
	public void addDataToBuffer(SuperVO[] queryVos) throws Exception {
		if (queryVos == null) {
			getBufferData().clear();
			return;
		}
		for (int i = 0; i < queryVos.length; i++) {
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(queryVos[i]);
			getBufferData().addVOToBuffer(aVo);
		}
	}
	/**
	 * 用指定的VO数组 <I>resultVOs </I>去更新BillUIBuffer.这个操作会先把Buffer中原有的数据清空。
	 * 如果指定resultVOs为空Buffer将被情况，且CurrentRow被设置为-1 否则CurrentRow设置为第0行
	 * 
	 * @throws Exception
	 */
	public void updateBuffer1() throws Exception // 暂时改成final,确保目前还没有人继承过它
	{

		if (getBufferData().getVOBufferSize() != 0) {

			getBillUI().setListHeadData(
					getBufferData().getAllHeadVOsFromBuffer());
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBufferData().setCurrentRow(0);
		} else {
			getBillUI().setListHeadData(null);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000066")/* @res "没有查到任何满足条件的数据!" */);
		}
	}

	String strwhere=null;
	/**
	 * 支持按表体条件查询 for add mlr isPowerUser 是否权限过滤
	 */
	public void onBoQuery() throws Exception {
		
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		// 支持按表体条件查询
		SuperVO[] queryVos = null;
		try {
			queryVos = queryHeadVOs(strWhere.toString());
		} catch (Exception e) {
			queryVos = queryHeadAndBodyVOs(strWhere.toString());
		}
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);
		updateBuffer();
		strwhere=strWhere.toString();
	}

	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();
		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;
		
		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length() == 0)
			strWhere = "1=1";
	
		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") ";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}

	/**
	 * 支持按表体条件查询 for add mlr isPowerUser 是否权限过滤
	 */
	public void onBoQuery(String pk_invbasdocName, String pk_invmandocName)
			throws Exception {

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		// 支持按表体条件查询
		SuperVO[] queryVos = null;
		
		queryVos = queryHeadAndBodyVOs(strWhere.toString(), pk_invbasdocName,
				pk_invmandocName);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	public SuperVO[] queryHeadAndBodyVOs(String strWhere) throws Exception {
		String headVoName = getUIController().getBillVoName()[1];
		String bodyVoName = getUIController().getBillVoName()[2];
		SuperVO headVo = (SuperVO) Class.forName(headVoName).newInstance();
		SuperVO bodyVo = (SuperVO) Class.forName(bodyVoName).newInstance();
		String sql = getQuerySql(headVo, bodyVo, strWhere);
		SuperVO[] vos = null;
		Class[] ParameterTypes = new Class[] { String.class, String.class };
		Object[] ParameterValues = new Object[] { headVoName, sql };
		Object o = LongTimeTask.calllongTimeService("zmpub", this.getBillUI(),
				"正在查询...", 1, "nc.bs.zmpub.pub.bill.ZmPubBO", null,
				"queryByHeadAndBodyVOs", ParameterTypes, ParameterValues);
		if (o != null) {
			vos = (SuperVO[]) o;
		}
		return vos;
	}

	public SuperVO[] queryHeadAndBodyVOs(String strWhere,
			String pk_invbasdocName, String pk_invmandocName) throws Exception {
		String headVoName = getUIController().getBillVoName()[1];
		String bodyVoName = getUIController().getBillVoName()[2];
		SuperVO headVo = (SuperVO) Class.forName(headVoName).newInstance();
		SuperVO bodyVo = (SuperVO) Class.forName(bodyVoName).newInstance();
		String sql = getQuerySql(headVo, bodyVo, strWhere, pk_invbasdocName,
				pk_invmandocName);
		SuperVO[] vos = null;
		Class[] ParameterTypes = new Class[] { String.class, String.class };
		Object[] ParameterValues = new Object[] { headVoName, sql };
		Object o = LongTimeTask.calllongTimeService("zmpub", this.getBillUI(),
				"正在查询...", 1, "nc.bs.zmpub.pub.bill.ZmPubBO", null,
				"queryByHeadAndBodyVOs", ParameterTypes, ParameterValues);
		if (o != null) {
			vos = (SuperVO[]) o;
		}
		return vos;
	}

	/**
	 * 获取支持表体查询的sql
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2012-1-11下午03:12:31
	 * @param headVo
	 * @param bodyVo
	 * @return
	 */
	private String getQuerySql(SuperVO headVo, SuperVO bodyVo, String strWhere) {
		String sql = " select "+ headVo.getTableName()+".* from " + headVo.getTableName() + " join "
				+ bodyVo.getTableName() + " on " + headVo.getTableName() + "."
				+ headVo.getPKFieldName() + " = " + bodyVo.getTableName() + "."
				+ bodyVo.getParentPKFieldName() + " where " + " isnull("
				+ headVo.getTableName() + ".dr,0)=0 and isnull("
				+ bodyVo.getTableName() + ".dr,0)=0 ";
		if (strWhere != null && strWhere.length() != 0)
			sql = sql + " and " + strWhere;
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
				+ bodyVo.getParentPKFieldName()
				+ " join bd_invbasdoc inv on inv.pk_invbasdoc= "
				+ bodyVo.getTableName() + "." + pk_invbasdocName
				+ " join bd_invcl cl on cl.pk_invcl =inv.pk_invcl " + " where "
				+ " isnull(" + headVo.getTableName() + ".dr,0)=0 and isnull("
				+ bodyVo.getTableName() + ".dr,0)=0 "
				+ " and isnull(inv.dr,0)=0" + " and isnull(cl.dr,0)=0 ";
		if (strWhere != null && strWhere.length() != 0)
			sql = sql + " and " + strWhere;
		
		// 用户角色存货分类权限过滤
		String pk_corp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();
		sql=sql+" and "+headVo.getTableName()+".pk_corp ='"+pk_corp+"'";
		
		String cuserid = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		String powersql = PowerGetTool.queryClassPowerSql("bd_invcl", pk_corp,
				cuserid);
		String powersql1 = PowerGetTool.queryClassPowerSql("bd_invmandoc",
				pk_corp, cuserid);
		if (PuPubVO.getString_TrimZeroLenAsNull(powersql) != null)
			sql = sql + " and cl.pk_invcl in (" + powersql + ")";
		if (PuPubVO.getString_TrimZeroLenAsNull(powersql1) != null)
			sql = sql + " and inv." + pk_invmandocName + " in (" + powersql1
					+ ")";
		return sql;
	}

	@Override
	protected String getHeadCondition() {
		String hvoname = getUIController().getBillVoName()[1];
		try {
			SuperVO headvo = (SuperVO) Class.forName(hvoname).newInstance();
			return " " + headvo.getTableName() + ".pk_corp = '"
					+ _getCorp().getPrimaryKey() + "' ";
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return " pk_corp = '" + _getCorp().getPrimaryKey() + "' ";
	}

	protected void onBoCancel() throws Exception {
		String billcode = null;
		String pk_billtype = null;
		String pk_corp = null;
		if (isAdding()) {
			billcode = (String) getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("vbillno").getValueObject();
			pk_billtype = getBillManageUI().getUIControl().getBillType();
			pk_corp = _getCorp().getPrimaryKey();
		}
		
		if (getBufferData().isVOBufferEmpty()) {
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			if(getBillUI() instanceof SonDefBillManageUI){
				SonDefBillManageUI ui=(SonDefBillManageUI) getBillUI();
				if(ui.getHl()!=null)
				ui.getHl().clear();
				if(ui.getHl1()!=null)
				ui.getHl1().clear();
			}
		} else {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			
            if(getBufferData().getCurrentRow()==-1){
               getBufferData().setCurrentRow(0);
            }else{
               getBufferData().setCurrentRow(getBufferData().getCurrentRow());
            }			
			
		}

		if (billcode != null && !"".equals(billcode)) {
			returnBillNo(billcode, pk_billtype, pk_corp);
		}
	}

	/**
	 * 动作脚本平台,作废，回收单据号
	 */
	@Override
	protected void onBoDel() throws Exception {
		//
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showYesNoDlg(getBillUI(), "作废", "是否确认作废当前单据?") != UIDialog.ID_YES) {
			return;
		}
		String billcode = (String) getBufferData().getCurrentVO().getParentVO()
				.getAttributeValue("vbillno");
		String pk_billtype = getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		checkMaker();
		super.onBoDel();
		//
		returnBillNo(billcode, pk_billtype, pk_corp);
	}

	@Override
	protected void onBoDelete() throws Exception {
		//
		if (getBufferData().getCurrentVO() == null)
			return;
		String billcode = (String) getBufferData().getCurrentVO().getParentVO()
				.getAttributeValue("vbillno");
		String pk_billtype = getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		checkMaker();
		super.onBoDelete();
		//
		returnBillNo(billcode, pk_billtype, pk_corp);
	}

	/**
	 * 取消、删除单据时退回单据号
	 */
	protected void returnBillNo(String billcode, String pk_billtype,
			String pk_corp) {
		if (billcode != null && billcode.trim().length() > 0) {// 单据号不为空，才进行回退
			try {
				nc.vo.pub.billcodemanage.BillCodeObjValueVO bcoVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
				bcoVO.setAttributeValue("公司", pk_corp);
				nc.ui.pub.billcodemanage.BillcodeRuleBO_Client
						.returnBillCodeOnDelete(pk_corp, pk_billtype, billcode,
								bcoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void updateListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			getBillListPanelWrapper().updateListVo(vo,
					getBufferData().getCurrentRow());
		}
	}

	@Override
	public void onBillRef() throws Exception {
		// 参照前将表头表体的本币金额，原币金额，本币汇率的精度重置为8，防止参照后币种精度的丢失
		setMaxDigitBeforeRef();
		try {
			super.onBillRef();
			if (PfUtilClient.isCloseOK()) {
				getBillUI().setBillOperate(IBillOperate.OP_REFADD);
				getBillUI().setDefaultData();
				setRowNum();
				setSonDatas();
			}
		} catch (Exception e) {
			throw new ExcelException("数据不存在或加载失败");
		} finally {
			// 参照完成之后还原参照按钮tag
			ButtonObject btn = getButtonManager()
					.getButton(IBillButton.Refbill);
			btn.setTag(String.valueOf(IBillButton.Refbill));
			// if (PfUtilClient.isCloseOK()) {
			// getBillUI().setBillOperate(IBillOperate.OP_REFADD);
			// }
		}
	}
	/**
	 * 参照生成设置行号
	 */
    public void setRowNum() {
    	int count=getBillCardPanel().getRowCount();
		BillRowNo.addLineRowNos(getBillCardPanel(), getUIController().getBillType(),
				"crowno", count);	
	}

	/**
     * 数据交换设置孙表信息
     * @throws Exception 
     */
	public void setSonDatas() throws Exception {
		   //PfUtilClient.getRetOldVos() 必须重新设置孙表，原因是因为以下这个方法
		//m_tmpRetVos 已经进行了交换赋值
		//	public static AggregatedValueObject[] getRetVos() {
//		if (!m_isRetChangeVo)
//			// 需要进行VO交换
//			m_tmpRetVos = changeVos(m_tmpRetVos);
//
//		return m_tmpRetVos;
//	}
	    HYBillVO sbill=(HYBillVO) PfUtilClient.getRetOldVo();
	    HYBillVO tbill=(HYBillVO) getBillManageUI().getVOFromUI();
	    Map<String, Map<String,SuperVO>> map=new HashMap<String, Map<String,SuperVO>>();
	    if(sbill instanceof ISonVOH && tbill instanceof ISonVOH ){
	    	if(sbill.getChildrenVO()!=null&&sbill.getChildrenVO().length>0){
	    		for(int i=0;i<sbill.getChildrenVO().length;i++){
	    			ISonVO bvo=(ISonVO) sbill.getChildrenVO()[i];
	    			List list=bvo.getSonVOS();
	    			List dlist=changeSon(list);
	    			String crowno=(String) tbill.getChildrenVO()[i].getAttributeValue("crowno");
	    			String pk=null;
	    			String key=crowno+pk;
	    			Map<String,SuperVO> smap=new HashMap<String,SuperVO>();
	    			if(dlist!=null&&dlist.size()>0){
	    				for(int j=0;j<dlist.size();j++){
	    					HYChildSuperVO sbvo=(HYChildSuperVO) dlist.get(j);
	    					sbvo.setPrimaryKey(null);
	    					sbvo.setAttributeValue(sbvo.getParentPKFieldName(), null);
	    					sbvo.setStatus(VOStatus.NEW);
	    					String skey=sbvo.getPrimaryKey()+sbvo.getCrowno();
	    					smap.put(skey, sbvo);
	    				}
	    			}
	    			map.put(key, smap);
	    		}
	    		if(getBillManageUI() instanceof SonDefBillManageUI){
	    			((SonDefBillManageUI)getBillManageUI()).setHl1(map);
	    		}
	    	}
	    }
	}
	/**
	 * 将来也孙表信息设置到目的孙表信息 子类实现
	 * @param list
	 * @return
	 */
	public List changeSon(List list) {
		
		return list;
	}

	/**
	 * 参照前将表头表体的本币金额，原币金额，本币汇率的精度重置为8，防止参照后币种精度的丢失
	 */
	protected void setMaxDigitBeforeRef() {
		try {
			String MAX_DIGIT = "8";
			String[] headshowitems = null;
			String[][] headshowmaxdigitnums = null;
			String[] bodyshowitems = null;
			String[][] bodyshowmaxdigitnums = null;

			String[][] headshownums = null;
			String[][] bodyshownums = null;

			if (getBillUI() instanceof BillManageUI) {
				BillManageUI clientui = (BillManageUI) getBillUI();
				headshownums = clientui.getHeadShowNum();
				bodyshownums = clientui.getItemShowNum();
			} else if (getBillUI() instanceof MultiChildBillManageUI) {
				MultiChildBillManageUI clientui = (MultiChildBillManageUI) getBillUI();
				headshownums = clientui.getHeadShowNum();
				bodyshownums = clientui.getItemShowNum();
			}

			if (headshownums != null && headshownums.length > 0) {
				headshowitems = headshownums[0];
				if (headshownums[0] != null && headshownums[0].length > 0) {
					String[] headshowdigits = new String[headshowitems.length];
					for (int i = 0; i < headshowdigits.length; i++) {
						headshowdigits[i] = MAX_DIGIT;
					}
					headshowmaxdigitnums = new String[][] { headshowitems,
							headshowdigits };
				}
			}

			if (bodyshownums != null && bodyshownums.length > 0) {
				String[] tablecodes = getBillCardPanelWrapper()
						.getBillCardPanel().getBillData().getBodyTableCodes();
				if (tablecodes != null && tablecodes.length > 0) {
					bodyshowmaxdigitnums = new String[tablecodes.length * 2][];
					for (int t = 0; t < tablecodes.length; t++) {
						bodyshowitems = bodyshownums[2 * t];
						if (bodyshowitems != null && bodyshowitems.length > 0) {
							String[] bodyshowdigits = new String[bodyshowitems.length];
							for (int i = 0; i < bodyshowdigits.length; i++) {
								bodyshowdigits[i] = MAX_DIGIT;
							}
							bodyshowmaxdigitnums[2 * t] = bodyshowitems;
							bodyshowmaxdigitnums[2 * t + 1] = bodyshowdigits;
						}

					}

				}
			}

			// 更新金额精度
			getBillCardPanelWrapper().setCardDecimalDigits(
					headshowmaxdigitnums, bodyshowmaxdigitnums);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.debug("setMaxDigitBeforeRef:UI参照时初始化单据金额精度为8，出现异常."
					+ e.getMessage());
		}

	}

	/**
	 * 联查
	 */
	public void onJoinQuery() throws BusinessException {
		getBillManageUI().showHintMessage("联查");
		if (getBufferData().getCurrentVO() == null) {
			return;
		}

		getSourceDlg().showModal();
	}

	/**
	 * 联查对话框
	 */
	public SourceBillFlowDlg getSourceDlg() throws BusinessException {
		try {
			soureDlg = new SourceBillFlowDlg(getBillManageUI(),
					getUIController().getBillType(),/* 当前单据类型 */
					getBufferData().getCurrentVO().getParentVO()
							.getPrimaryKey(), /* 当前单据ID */
					null, /* 当前业务类型 */
					_getOperator(), /* 当前用户ID */
					(String) getBufferData().getCurrentVO().getParentVO()
							.getAttributeValue("vbillno") /* 单据号 */);
			soureDlg
					.setBillFinderClassname("nc.bs.scm.sourcebill.SCMBillFinder");
		} catch (BusinessException e) {
			Logger.error(e);
			throw new BusinessException("获取联查对话框出错! ");
		}
		return soureDlg;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		DefBillManageUI ui = null;
		if (getBillUI() instanceof DefBillManageUI)
			ui = ((DefBillManageUI) getBillUI());
		if (ui != null && intBtn == ui.getLinkButtonNo()) {// 联查
			onJoinQuery();
		} else {
			MutiDefBillManageUI ui1 = null;
			if (getBillUI() instanceof MutiDefBillManageUI)
				ui1 = ((MutiDefBillManageUI) getBillUI());
			if (ui1 != null && intBtn == ui1.getLinkButtonNo()) {// 联查
				onJoinQuery();
			}								
		}
		if(getBillUI() instanceof SonDefBillManageUI){
			 if(ui != null && intBtn == ZmpubBtnConst.clmx){
				 quotaQuery();
			 }
		}
		super.onBoElse(intBtn);
	}
	/**
	 * 查看孙表明细
	 * @throws Exception
	 */
	protected void quotaQuery() throws Exception{
		ClientLink cl = new ClientLink(ClientEnvironment.getInstance());
		String corp = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_corp"));
		SonDefBillManageUI ui = (SonDefBillManageUI) getBillUI();		
		QueryUI qui=new QueryUI(ui.getSonQueryBillType(), cl.getUser(), corp, null, ui, false,getSonQueryDigTile());
		qui.showModal();
	}
	/**
	 * 孙表查看明细对话框标题
	 * @return
	 */
	public  String getSonQueryDigTile(){
	  return "明细查看";	
	}


	protected void onBoCancelAudit() throws Exception {
		AggregatedValueObject[] billvos = (AggregatedValueObject[]) getBillListPanel().getMultiSelectedVOs(getUIController().getBillVoName()[0], 
				getUIController().getBillVoName()[1],
				getUIController().getBillVoName()[2]);
		if(billvos!=null&&billvos.length!=0&&getBillManageUI().isListPanelSelected()){
			onBoCancelAuditBanth(billvos);
			onQuery(strwhere);
		}else{
			super.onBoCancelAudit();
		}
		
	}

	private void onBoCancelAuditBanth(AggregatedValueObject[] billvos) throws Exception {
		if(billvos==null || billvos.length==0)
			return;
		for(int i=0;i<billvos.length;i++){
		// 获得数据
		AggregatedValueObject modelVo = billvos[i];
		setChildVOs(modelVo);
		// 放入反审批日期、审批人
		setCheckManAndDate(modelVo);
		// 如果状态一致则退出
		if (checkVOStatus(modelVo, new int[] { IBillStatus.FREE })) {
			System.out.println("无效的鼠标处理机制");
			return;
		}
		beforeOnBoAction(IBillButton.CancelAudit, modelVo);
		// *******************
		AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction()
				.unapprove(modelVo, getUIController().getBillType(),
						getBillUI()._getDate().toString(),
						getBillUI().getUserObject());

		if (PfUtilClient.isSuccess()) {
			afterOnBoAction(IBillButton.CancelAudit, retVo);
			CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
			if (childVos == null)
				modelVo.setParentVO(retVo.getParentVO());
			else
				modelVo = retVo;

			Integer intState = (Integer) modelVo.getParentVO()
					.getAttributeValue(getBillField().getField_BillStatus());
			if (intState.intValue() == IBillStatus.FREE) {
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_CheckMan(), null);
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_CheckDate(), null);
			}
			
//			// 更新列表数据
//			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
//			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	}
  }
}
	protected void onBoCommit() throws Exception {
		AggregatedValueObject[] billvos = (AggregatedValueObject[]) getBillListPanel().getMultiSelectedVOs(getUIController().getBillVoName()[0], 
				getUIController().getBillVoName()[1],
				getUIController().getBillVoName()[2]);
		if(billvos!=null&&billvos.length!=0&&billvos!=null&&getBillManageUI().isListPanelSelected()){
			onBoCommitBanth(billvos);
			onQuery(strwhere);
		}else{
			// 获得数据
			AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
			//校验制单人和提交人是否一致
			if(!modelVo.getParentVO().getAttributeValue(getBillField().getField_Operator()).equals(getBillUI()._getOperator())){
			    getBillUI().showErrorMessage("制单人和提交人必须一致");
				return;
			}
			// 临时性设置制单人为当前操作人，
			modelVo.getParentVO().setAttributeValue(
					getBillField().getField_Operator(), getBillUI()._getOperator());
			beforeOnBoAction(IBillButton.Commit, modelVo);
			String strTime = ClientEnvironment.getServerTime()
					.toString();
			ArrayList retList = getBusinessAction().commit(modelVo,
					getUIController().getBillType(),
					getBillUI()._getDate().toString() + strTime.substring(10),
					getBillUI().getUserObject());

			if (PfUtilClient.isSuccess()) {
				Object o = retList.get(1);
				if (o instanceof AggregatedValueObject) {
					AggregatedValueObject retVo = (AggregatedValueObject) o;
					afterOnBoAction(IBillButton.Commit, retVo);
					CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
					if (childVos == null)
						modelVo.setParentVO(retVo.getParentVO());
					else
						modelVo = retVo;
				}
				getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}
		
		}
		updateListVo();
	}


	private void onBoCommitBanth(AggregatedValueObject[] billvos) throws Exception {
		if(billvos==null || billvos.length==0)
			return;
		for(int i=0;i<billvos.length;i++){
			// 获得数据
			AggregatedValueObject modelVo = billvos[i];
			
			setTsAndDr(modelVo);
			//校验制单人和提交人是否一致
			if(!modelVo.getParentVO().getAttributeValue(getBillField().getField_Operator()).equals(getBillUI()._getOperator())){
			    getBillUI().showErrorMessage("制单人和提交人必须一致");
				return;
			}
			// 临时性设置制单人为当前操作人，
			modelVo.getParentVO().setAttributeValue(
					getBillField().getField_Operator(), getBillUI()._getOperator());
		
			
			beforeOnBoAction(IBillButton.Commit, modelVo);
			String strTime = ClientEnvironment.getServerTime()
					.toString();
			ArrayList retList = getBusinessAction().commit(modelVo,
					getUIController().getBillType(),
					getBillUI()._getDate().toString() + strTime.substring(10),
					getBillUI().getUserObject());

			if (PfUtilClient.isSuccess()) {
				Object o = retList.get(1);
				if (o instanceof AggregatedValueObject) {
					AggregatedValueObject retVo = (AggregatedValueObject) o;
					afterOnBoAction(IBillButton.Commit, retVo);
					CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
					if (childVos == null)
						modelVo.setParentVO(retVo.getParentVO());
					else
						modelVo = retVo;
				}
//				getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
//				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}
		}	
	}
	
	
	
    /**
     * ui界面更新后调用该方法 缓存更新 
     * 
     * 如按钮事件，行切换事件触发后，更新界面，最后调用该方法
     */
	public void afterUpdate() {
		//ObjectUtils.serializableClone(getBillCardPanel().getBillModel().getBodyItems());
		
	}

}
