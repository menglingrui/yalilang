package nc.ui.zmpub.pub.bill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ListSelectionModel;

import nc.itf.zmpub.pub.ISonVO;
import nc.itf.zmpub.pub.ISonVOH;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;
import nc.vo.zmpub.pub.consts.ZmpubBtnConst;
/**
 * 支持孙表的ui类,支持自定项设置的ui类
 * @author mlr
 */
public abstract class SonDefBillManageUI extends SonClientUI implements BillCardBeforeEditListener,ISonEenentHandler{
	private static final long serialVersionUID = -5178594056626651498L;
	protected ClientLink cl = new ClientLink(ClientEnvironment.getInstance());;// 客户端环境变量
	private LogNumRefUFPanel ivjLotNumbRefPane = null;
	// 存放孙表使用材料信息 行号+表体主键 作为key值
	private Map<String, List> hl = new HashMap<String, List>();
	//保存前存放孙表数据
	private Map<String, Map<String,SuperVO>> hl1 = new HashMap<String, Map<String,SuperVO>>();
	/**
	 * 孙表的单据类型
	 * @return
	 */
	public abstract String getSonBillType();
	/**
	 * 孙表明细查看单据类型
	 * @return
	 */
	public abstract String getSonQueryBillType();

	protected LogNumRefUFPanel getLotNumbRefPane() {
		if (ivjLotNumbRefPane == null) {
			try {
				SuperVO vo=(SuperVO) Class.forName(getUIControl().getBillVoName()[1]).newInstance();
				String sonclssname=null;
				if(vo instanceof ISonVOH){
					ISonVOH son=(ISonVOH) vo;
					sonclssname=son.getSonClass();
				}else{
					this.showErrorMessage("主表没有实现 ISonVOH接口");
				}
				ivjLotNumbRefPane = new LogNumRefUFPanel(sonclssname,getSonBillType(),getSonDigTile());
				ivjLotNumbRefPane.setName("LotNumbRefPane");
				ivjLotNumbRefPane.setLocation(38, 1);
				initSonListener(ivjLotNumbRefPane);
			
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjLotNumbRefPane;
	}
	/**
	 * 返回卡片模版。 创建日期：(2002-12-23 9:44:25)
	 */
	public  BillCardPanel getSonBillCardPanel() {
		return  getLotNumbRefPane().getLotNumbDlg().getBillCardPanel();
	}

	public abstract String getSonDigTile() ;
	/**
	 * 初始化孙表监听器
	 * @param panel
	 */
	public void initSonListener(LogNumRefUFPanel panel) {
			   
		panel.getLotNumbDlg().getBillCardPanel().addBodyEditListener2(new BillEditListener2(){
				 public boolean beforeEdit(BillEditEvent e) {
					 boolean bb= SonbeforeEdit(e);
					 return bb;
				 }		   
			   });
		   
		panel.getLotNumbDlg().getBillCardPanel().addEditListener(new BillEditListener(){

			public void afterEdit(BillEditEvent e) {
				SonafterEdit(e);				
			}
			public void bodyRowChange(BillEditEvent e) {
				SonbodyRowChange(e);
			}			
		});
		panel.getLotNumbDlg().setHandler(this);
		
	}
	/**
	 * 孙表编辑后事件
	 * @param e
	 */
	public void SonafterEdit(BillEditEvent e) {
		
		
	}
	/**
	 * 孙表编辑后事件
	 * @param e
	 */
	public void SonbodyRowChange(BillEditEvent e) {
		
	}
	
	 /**
	  * 孙表编辑前事件
	  * @param e
	  * @return
	  */
	 public boolean SonbeforeEdit(BillEditEvent e) {
		 return true;
	 }	
	@Override
	public void bodyRowChange(final nc.ui.pub.bill.BillEditEvent e) {
          super.bodyRowChange(e);
          if(getManageEventHandler() instanceof FlowManageEventHandler){
        	  ((FlowManageEventHandler)getManageEventHandler()).onLineChangeAfter(e);
          }
	}
	public SonDefBillManageUI() {
		super();
		init();
	}
	public AggregatedValueObject getChangedVOFromUI() throws java.lang.Exception {
		Map<String, List> nmap=getHl();
		Map<String, Map<String,SuperVO>> map=getHl1();
		for(String key:map.keySet()){
			Map<String,SuperVO> map1=map.get(key);
			List list=new ArrayList();
			for(String key2:map1.keySet()){
				list.add(map1.get(key2));
			}
			nmap.put(key, list);
		}		
		return super.getChangedVOFromUI();
	}	
	@Override
	protected void initSelfData() {
		super.initSelfData();
		//设置多选
		getBillListPanel().getHeadTable().setRowSelectionAllowed(true);	//true那一行内容能够全部选中,false只能选中一个单元格
		getBillListPanel().setParentMultiSelect(true);//设置表头多选,带复选框
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);//往下拖拽，选中表头所有行		//bb.set
		//除去行操作多余按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(IBillButton.CopyLine));
			btnobj.removeChildButton(getButtonManager().getButton(IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(IBillButton.InsLine));
			btnobj.removeChildButton(getButtonManager().getButton(IBillButton.PasteLinetoTail));
		}
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		getBillListPanel().getChildListPanel().setTatolRowShow(true);
	}  

	public Map<String, List> getHl() {
		return hl;
	}

	public void setHl(Map<String, List> hl) {
		this.hl = hl;
	}

	public Map<String, Map<String,SuperVO>> getHl1() {
		return hl1;
	}

	public void setHl1(Map<String, Map<String,SuperVO>> hl1) {
		this.hl1 = hl1;
	}


	protected BusinessDelegator createBusinessDelegator() {
		return new SonBusinessDelegator();
	}

	@Override
	public boolean isLinkQueryEnable() {
		return true;
	}

	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	
	/**
	 * 数据初始化
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-12下午04:55:37
	 */
	private void init() {
		getBufferData().addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				if (!getBufferData().isVOBufferEmpty()) {
					int row = getBufferData().getCurrentRow();
					if (row < 0) {
						return;
					}
					// 更新缓存
					AggregatedValueObject obj = getBufferData().getCurrentVO();
					if (obj != null) {
						HYBillVO billvo = (HYBillVO) getBufferData()
								.getCurrentVO();
						SuperVO[] bvo = (SuperVO[]) billvo
								.getChildrenVO();
						setList(bvo);
					}
				}
			}
		});
	}

	public void setList(SuperVO[] bvo) {
		Map<String, Map<String,SuperVO>> hl1 = new HashMap<String, Map<String,SuperVO>>();
		if (bvo != null && bvo.length > 0) {
			for (SuperVO b : bvo) {
				HYChildSuperVO svo=(HYChildSuperVO) b;
				ISonVO son=(ISonVO) b;
				String crowno =svo.getCrowno();
				String key = b.getPrimaryKey();
				Map<String,SuperVO> map=new HashMap<String,SuperVO>();			
				hl1.put(crowno + key, map);
				List<SuperVO> list=son.getSonVOS();
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
					   SuperVO vo =list.get(i);
						 String pk=vo.getPrimaryKey();
						 String cnum=(String) vo.getAttributeValue("crowno");
						 map.put(pk+cnum, vo);
					   
					}
				}
			}
		}		
		setHl1(hl1);
		setHl(new HashMap<String, List>());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	protected String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(), cl
				.getCorp(), null, null);
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("pk_corp").setValue(cl.getCorp());
		getBillCardPanel().getTailItem("voperatorid").setValue(cl.getUser());
		getBillCardPanel().getTailItem("dmakedate").setValue(cl.getLogonDate());
		getBillCardPanel().getHeadItem("dbilldate").setValue(cl.getLogonDate());
		getBillCardPanel().getHeadItem("vbillstatus")
				.setValue(IBillStatus.FREE);
		getBillCardPanel().getHeadItem("pk_billtype").setValue(
				getUIControl().getBillType());
		getBillCardPanel().getHeadItem("vbillno").setValue(getBillNo());
	
	}
	public abstract String getSonDigName();
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if (e.getPos() == BillItem.BODY) {
			if(getSonDigName()==null || getSonDigName().length()==0){
				this.showErrorMessage(" getSonDigName() 方法没有实现 ,必须返回模版孙表存放字段名称 ");
				return false;
			}
			if (getSonDigName().equalsIgnoreCase(key)) {
				getBillCardPanel().getBillModel().setValueAt("", row, getSonDigName());
				nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(key);
				getLotNumbRefPane().setMaxLength(biCol.getLength());
				getBillCardPanel().getBodyPanel().getTable().getColumn(
						biCol.getName()).setCellEditor(
						new nc.ui.pub.bill.BillCellEditor(getLotNumbRefPane()));
				String crowno = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBillModel().getValueAt(row, "crowno"));
				String bclassname=getUIControl().getBillVoName()[2];
				SuperVO  vo=null;
				try {
					 vo=(SuperVO) Class.forName(bclassname).newInstance();
				} catch (Exception e1) {
			         this.showErrorMessage(e1.getMessage());
			         return false;
				}
				String bodypk = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBillModel().getValueAt(row,
										vo.getPKFieldName()));
				String ckey = crowno + bodypk;
				if (getHl1().get(ckey) != null && getHl1().get(ckey).size() > 0) {
					Map<String,SuperVO> map= getHl1().get(ckey);
					List<SuperVO> nlist=new ArrayList<SuperVO>();
					if(map!=null && map.size()>0){
					    for(String pk:map.keySet()){
					    	if(map.get(pk).getStatus()!=VOStatus.DELETED){
					    		nlist.add(map.get(pk));
					    }	
					}
					if(nlist.size()!=0){    
				
					getLotNumbRefPane().setVos(
							(SuperVO[]) (nlist.toArray(new SuperVO[0])));
					}else{
					nlist=beforeEditSetSonDatas(nlist,row);
					}
				    } 
				}else {
					List<SuperVO> nlist=beforeEditSetSonDatas(null,row);
					if(nlist!=null)
					getLotNumbRefPane().setVos(nlist.toArray(new SuperVO[0]));
					
				}			  
			}
			return true;	
	 }  
		return true;
	}
    /**
     * 表体编辑前,设置孙表数据前处理
     * @param nlist
     * @param row
     * @return
     */
	public List<SuperVO> beforeEditSetSonDatas(List<SuperVO> nlist, int row) {
		
		return nlist;
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		try {
			String key = e.getKey();
			int row = e.getRow();
			Object value = e.getValue();
			if (e.getPos() == BillItem.HEAD) {

				getBillCardPanel().execHeadEditFormulas();

			} else {
				if (getSonDigName().equalsIgnoreCase(key)) {
					// 支持批次号 多选拆行 for add mlr
					List<SuperVO> vos = getLotNumbRefPane()
							.getLotNumbDlg().getLis();
					String crowno = PuPubVO
							.getString_TrimZeroLenAsNull(getBillCardPanel()
									.getBillModel().getValueAt(row, "crowno"));
					vos=afterEditDealSonDatas(vos,row);
					String bclassname=getUIControl().getBillVoName()[2];
					SuperVO  vo=null;
					try {
						 vo=(SuperVO) Class.forName(bclassname).newInstance();
					} catch (Exception e1) {
				         this.showErrorMessage(e1.getMessage());
				         return ;
					}
					String bodypk = PuPubVO
							.getString_TrimZeroLenAsNull(getBillCardPanel()
									.getBillModel().getValueAt(row,
											vo.getPKFieldName()));
					String ckey = crowno + bodypk;
					if(vos!=null ||vos.size()>0){
						 Map<String,SuperVO> omap=hl1.get(ckey);
						 for(int i=0;i<vos.size();i++){
							 String pk=vos.get(i).getPrimaryKey();
							 String cnum=(String) vos.get(i).getAttributeValue("crowno");
							 if(omap==null|| omap.size()==0){
								 omap=new HashMap<String,SuperVO>();
								 hl1.put(ckey, omap);
							 }		 
							 omap.put(pk+cnum, vos.get(i));
						 }		
						}
					  setIsExistSonChar(hl1.get(ckey),e);
					}				
					getLotNumbRefPane().getLotNumbDlg().setLis(null);
			super.afterEdit(e);
			}

		} catch (Exception e1) {
            this.showErrorMessage(e1.getMessage());
		}
	}	
	/**
	 * 孙表编辑后,设置到Map前,处理孙表变化的VO
	 * @param vos
	 * @param row
	 * @return
	 */
	public List<SuperVO> afterEditDealSonDatas(List<SuperVO> vos, int row) {

		
		
		return vos;
	}
	/**
	 * 编辑后设置材料是否存在的标记
	 * @param map
	 * @param e 
	 */
	public void setIsExistSonChar(Map<String, SuperVO> map, BillEditEvent e) {
	   boolean isexit=false;
	   if(map!=null&&map.size()>0){
		   for(String key:map.keySet()){
			   SuperVO vo=map.get(key);
			   if(vo!=null){
				   if(vo.getStatus()==VOStatus.DELETED){
					     
				   }else{
					   isexit=true;
					   break;
				   }
			   }
		   }
	   }
	   if(isexit==true){
		   getBillCardPanel().getBillModel().setValueAt("Y", e.getRow(), getSonDigName()) ;
	   }else{
		   getBillCardPanel().getBillModel().setValueAt("N", e.getRow(), getSonDigName()) ; 
	   }		
	}
	/**
	 * 编辑后设置材料是否存在的标记
	 * @param map
	 * @param e 
	 */
	public void setIsExistSonChar(Map<String, SuperVO> map, int  row) {
	   boolean isexit=false;
	   if(map!=null&&map.size()>0){
		   for(String key:map.keySet()){
			   SuperVO vo=map.get(key);
			   if(vo!=null){
				   if(vo.getStatus()==VOStatus.DELETED){
					     
				   }else{
					   isexit=true;
					   break;
				   }
			   }
		   }
	   }
	   if(isexit==true){
		   getBillCardPanel().getBillModel().setValueAt("Y", row, getSonDigName()) ;
	   }else{
		   getBillCardPanel().getBillModel().setValueAt("N", row, getSonDigName()) ; 
	   }		
	}
	/**
	 * 编辑后设置材料是否存在的标记
	 * @param map
	 * @param e 
	 */
	public void setIsExistSonChar1(Map<String, SuperVO> map, int  row) {
	   boolean isexit=false;
	   if(map!=null&&map.size()>0){
		   for(String key:map.keySet()){
			   SuperVO vo=map.get(key);
			   if(vo!=null){
				   if(vo.getStatus()==VOStatus.DELETED){
					     
				   }else{
					   isexit=true;
					   break;
				   }
			   }
		   }
	   }
	   if(isexit==true){
		   getBillListPanel().getBodyBillModel().setValueAt("Y", row, getSonDigName()) ;
	   }else{
		   getBillListPanel().getBodyBillModel().setValueAt("N", row, getSonDigName()) ; 
	   }		
	}
	@Override
	public Map<String, List> getSonMap() {
		return hl;
	}
	protected void initPrivateButton() {
		// 打印管理
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(ZmpubBtnConst.ASSPRINT);
		btnvo6.setBtnName("打印管理");
		btnvo6.setBtnChinaName("打印管理");
		btnvo6.setBtnCode(null);// code最好设置为空
		btnvo6.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo6.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo6);
		

		
		if(!isLinkQueryEnable())
			return;
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(getLinkButtonNo());
		btnvo.setBtnName("联查");
		btnvo.setBtnChinaName("联查");
		btnvo.setBtnCode(null);// code最好设置为空
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo);	
		
		
		ButtonVO btnvo61 = new ButtonVO();
		btnvo61.setBtnNo(ZmpubBtnConst.clmx);
		btnvo61.setBtnName(getQueryDetailBtnName());
		btnvo61.setBtnChinaName(getQueryDetailBtnName());
		btnvo61.setBtnCode(null);// code最好设置为空
//		btnvo61.setBusinessStatus(new int[]{IBillStatus.FREE});
		btnvo61.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo61);
		// 辅助查询
		ButtonVO btnvo5 = new ButtonVO();
		btnvo5.setBtnNo(ZmpubBtnConst.ASSQUERY);
		btnvo5.setBtnName("辅助查询");
		btnvo5.setBtnChinaName("辅助查询");
		btnvo5.setBtnCode(null);// code最好设置为空
		btnvo5.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo5.setChildAry(new int[] {ZmpubBtnConst.LINKQUERY,IBillButton.ApproveInfo,ZmpubBtnConst.clmx});
		addPrivateButton(btnvo5);
	

		super.initPrivateButton();
	}
    public abstract String getQueryDetailBtnName() ;

	/**
     * ui界面更新后调用该方法 缓存更新 
     * 
     * 如按钮事件，行切换事件触发后，更新界面，最后调用该方法
     */
	public void afterUpdate() {
		super.afterUpdate();
		try {
			setIsExistSonChar(this);
		} catch (BusinessException e) {
			e.printStackTrace();
			this.showErrorMessage(e.getMessage());
		}
		   if(getManageEventHandler() instanceof FlowManageEventHandler){
	        	  ((FlowManageEventHandler)getManageEventHandler()).afterUpdate();
	         }
	}
	/**
	 * 设置孙表标志位
	 * @param ui
	 * @throws BusinessException
	 */
	public  void setIsExistSonChar(BillManageUI ui) throws BusinessException {
		try {
		if(ui.isListPanelSelected()){
			SonDefBillManageUI sonui=null;
			BillListPanel  panel=ui.getBillListPanel();
			if(ui instanceof SonDefBillManageUI ){
				sonui=(SonDefBillManageUI) ui;
			}else{
				return;
			}
			Map<String, Map<String,SuperVO>> map=sonui.getHl1();
			int count=panel.getBodyBillModel().getRowCount();
			Class bodyclass = Class.forName(ui.getUIControl().getBillVoName()[2]);		
			SuperVO bodyvo=(SuperVO) bodyclass.newInstance();			
			for(int i=0;i<count;i++){
				String pk=(String) panel.getBodyBillModel().getValueAt(i, bodyvo.getPKFieldName());
				String crowno=(String) panel.getBodyBillModel().getValueAt(i,"crowno");
				String key=crowno+pk;				
			    setIsExistSonChar1(map.get(key), i);			
			}
		}else{
			SonDefBillManageUI sonui=null;
			BillCardPanel  panel=ui.getBillCardPanel();
			if(ui instanceof SonDefBillManageUI ){
				sonui=(SonDefBillManageUI) ui;
			}else{
				return;
			}
			Map<String, Map<String,SuperVO>> map=sonui.getHl1();
			int count=panel.getBillModel().getRowCount();
			Class bodyclass = Class.forName(ui.getUIControl().getBillVoName()[2]);		
			SuperVO bodyvo=(SuperVO) bodyclass.newInstance();			
			for(int i=0;i<count;i++){
				String pk=(String) panel.getBillModel().getValueAt(i, bodyvo.getPKFieldName());
				String crowno=(String) panel.getBillModel().getValueAt(i,"crowno");
				String key=crowno+pk;
				setIsExistSonChar(map.get(key), i);				
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}
}
