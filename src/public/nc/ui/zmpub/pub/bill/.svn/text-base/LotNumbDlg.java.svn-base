package nc.ui.zmpub.pub.bill;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.vo.pub.BusinessException;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
/**
 * 孙表参照对话框 mlr
 */
public class LotNumbDlg extends UIDialog implements java.awt.event.ActionListener {
	private static final long serialVersionUID = -6435830803487288885L;
	private List  lis = new ArrayList();// 模版增加的数据
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private UIPanel ivjUIPanel1 = null;
	private UIButton ivjBtnCancel = null;
	private UIButton ivjBtnOK = null;
	private UIButton btnAdd = null;
	private UIButton btnDel = null;
	private ClientLink m_cl = null;
	private BillCardPanel m_card = null;	
	private String classname=null;
	private String billtype =null;
	private String tile="孙表信息";
	private ISonEenentHandler handler=null;
	
	public ISonEenentHandler getHandler() {
		return handler;
	}

	public void setHandler(ISonEenentHandler handler) {
		this.handler = handler;
	}

	public String getTile() {
		return tile;
	}

	public void setTile(String tile) {
		this.tile = tile;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * LotNumbDlg 构造子注解。
	 */
	public LotNumbDlg() {
		super();
		initialize();
	}
   
	/**
	 * LotNumbDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LotNumbDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * LotNumbDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public LotNumbDlg(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}

	/**
	 * LotNumbDlg 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public LotNumbDlg(java.awt.Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * LotNumbDlg 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public LotNumbDlg(java.awt.Frame owner, String title) {
		super(owner, title);
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-5-8 20:47:36)
	 * 
	 * @return java.lang.String
	 */
	public String getAssQuant() {
		return null;
	}

	/**
	 * 返回 BtnCancel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			try {
				ivjBtnCancel = new nc.ui.pub.beans.UIButton();
				ivjBtnCancel.setName("BtnCancel");
				ivjBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC001-0000008")/* @res "取消" */);
				ivjBtnCancel.setBounds(560, 321, 70, 21);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBtnCancel;
	}

	/**
	 * 返回 BtnOK 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBtnOK() {
		if (ivjBtnOK == null) {
			try {
				ivjBtnOK = new nc.ui.pub.beans.UIButton();
				ivjBtnOK.setName("BtnOK");
				ivjBtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC001-0000044")/* @res "确定" */);
				ivjBtnOK.setBounds(480, 321, 70, 21);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBtnOK;
	}

	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (btnAdd == null) {
			try {
				btnAdd = new nc.ui.pub.beans.UIButton();
				btnAdd.setName("BtnAdd");
				btnAdd.setText("增加");
				btnAdd.setBounds(480, 321, 70, 21);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return btnAdd;
	}
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (btnDel == null) {
			try {
				btnDel = new nc.ui.pub.beans.UIButton();
				btnDel.setName("BtnDel");
				btnDel.setText("删除");
				btnDel.setBounds(480, 321, 70, 21);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return btnDel;
	}

	// /**
	// * 此处插入方法说明。
	// * 创建者：张欣
	// * 功能：
	// * 参数：
	// * 返回：
	// * 例外：
	// * 日期：(2001-5-16 13:47:22)
	// * 修改日期，修改人，修改原因，注释标志：
	// *
	// * @return java.lang.String
	// */
	// public String getLotNumb() {
	//
	// m_strLotNumb = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getVbatchcode();
	// return m_strLotNumb;
	//
	// }

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				// ivjUIDialogContentPane.setLayout(new BorderLayout());
				// ivjUIDialogContentPane.add(getbillListPanel(), "Center");
				// ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				// getUIDialogContentPane().add(getUIPanel1(),
				// getUIPanel1().getName());

				// getUIDialogContentPane().add(getBtnQueryAll(),
				// getBtnQueryAll().getName());
				getUIDialogContentPane().add(getPanlCmd(), BorderLayout.SOUTH);
			//	getUIDialogContentPane().add(getPanlCmd1(), BorderLayout.NORTH);
				// getUIDialogContentPane().add(getBtnOK(), BorderLayout.SOUTH);
				// getUIDialogContentPane().add(getBtnCancel(),BorderLayout.SOUTH);
				getUIDialogContentPane().add(getBillCardPanel(), "Center");
				// getUIDialogContentPane().add(getBtnRefresh(),
				// getBtnRefresh().getName());
				// getUIDialogContentPane().add(getBtnNew(),
				// getBtnNew().getName());
				// getUIDialogContentPane().add(getBtnEdit(),
				// getBtnEdit().getName());

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	private UIPanel ivjPanlCmd = null;
	private UIPanel ivjPanlCmd1 = null;
   /**
    * 确定 取消按钮
    * @return
    */
	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getBtnAdd(), getBtnAdd().getName());
			ivjPanlCmd.add(getBtnDel(), getBtnDel().getName());
			ivjPanlCmd.add(getBtnOK(), getBtnOK().getName());
			ivjPanlCmd.add(getBtnCancel(), getBtnCancel().getName());
		}
		return ivjPanlCmd;
	}
	/**
	 * 增加  删除按钮
	 * @return
	 */
	private UIPanel getPanlCmd1() {
		if (ivjPanlCmd1 == null) {
			ivjPanlCmd1 = new UIPanel();
			ivjPanlCmd1.setName("PanlCmd1");
			ivjPanlCmd1.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd1.setLayout(new FlowLayout());
			ivjPanlCmd1.add(getBtnAdd(), getBtnAdd().getName());
			ivjPanlCmd1.add(getBtnDel(), getBtnDel().getName());
		}
		return ivjPanlCmd1;
	}

	/**
	 * 返回 UIPanel1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	// private nc.ui.pub.beans.UIPanel getUIPanel1() {
	// if (ivjUIPanel1 == null) {
	// try {
	// ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
	// ivjUIPanel1.setName("UIPanel1");
	// ivjUIPanel1.setLayout(null);
	// ivjUIPanel1.setBounds(10, 0, 800, 80);
	// getUIPanel1().add(getLbWareHouseID(), getLbWareHouseID().getName());
	// getUIPanel1().add(getTfWareHouse(), getTfWareHouse().getName());
	// getUIPanel1().add(getLbUnitName(), getLbUnitName().getName());
	// getUIPanel1().add(getTfUnitName(), getTfUnitName().getName());
	// getUIPanel1().add(getLbFreeItem1(), getLbFreeItem1().getName());
	// getUIPanel1().add(getTfFreeItem1(), getTfFreeItem1().getName());
	// getUIPanel1().add(getLbInventory(), getLbInventory().getName());
	// getUIPanel1().add(getTfInventory(), getTfInventory().getName());
	// getUIPanel1().add(getckIsQueryZeroLot(),
	// getckIsQueryZeroLot().getName());
	// } catch (java.lang.Throwable ivjExc) {
	// handleException(ivjExc);
	// }
	// }
	// return ivjUIPanel1;
	// }
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
		// nc.vo.scm.pub.SCMEnv.error(exception);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			setModal(true);
			setName("LotNumbDlg");
			setSize(700, 450);
			// setResizable(false);
			setContentPane(getUIDialogContentPane());
			 initConnections1();
			// m_cardBodySortCtl = new ClientUISortCtl(this,true,BillItem.BODY);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// setUIEditable(false);
		// getckIsQueryZeroLot().addItemListener(this);
	}
	

	public List getLis() {
		return lis;
	}

	public void setLis(List lis) {
		this.lis = lis;
	}

	private void initConnections1() {
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);	
		getBtnAdd().addActionListener(this);
		getBtnDel().addActionListener(this);
		
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			LotNumbDlg aLotNumbDlg = null;
			aLotNumbDlg = new LotNumbDlg();
			aLotNumbDlg.setModal(true);
			aLotNumbDlg.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			aLotNumbDlg.show();
			java.awt.Insets insets = aLotNumbDlg.getInsets();
			aLotNumbDlg.setSize(aLotNumbDlg.getWidth() + insets.left
					+ insets.right, aLotNumbDlg.getHeight() + insets.top
					+ insets.bottom);
			aLotNumbDlg.setVisible(true);
		} catch (Throwable exception) {
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-5-8 18:27:15)
	 */
	public void onCancel() {
		// this.setVisible(false);
		// aLotNumbDlg.closeCancel();
		closeCancel();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-5-8 18:27:04)
	 * @throws ValidationException 
	 */
	public void onOK() throws ValidationException {
			dataNotNullValidate();	
		List list = new ArrayList();
		SuperVO[]	 bvos=(SuperVO[]) getBillCardPanel().getBillData().getBodyValueChangeVOs(
				classname);
	    if(bvos!=null&&bvos.length>0){
	    	for(int i=0;i<bvos.length;i++)
	    		list.add(bvos[i]);
	    }
		if (list.size() == 0) {
			onCancel();
		} else {
			closeOK();
		}
		setLis(list);
	}

	/**
	 * 在表格中显示数据。 创建日期：(2001-5-8 20:50:59)
	 */
	public void setVOtoBody() {
		// LotNumbRefVO[] voaAllData = null;

		// if (isTrackedBill()) {
		// getUITablePane1().getTable().setModel(getTrackedTableModel());
		// voaAllData = getTrackedTableModel().getAllData();
		//
		// } else {
		// getUITablePane1().getTable().setModel(getNotTrackedTableModel());
		// voaAllData = getNotTrackedTableModel().getAllData();
		// }
		// getUITablePane1().getTable().setModel(getBillCardPanel().getBillModel());

		// test注释
		// voaAllData =
		// (LotNumbRefVO[])getBillCardPanel().getBillModel().getBodyValueVOs(LotNumbRefVO.class.getName());
		//	
		// if (voaAllData != null && voaAllData.length > 0) {
		// getUITablePane1().getTable().setRowSelectionInterval(0, 0);
		// setSelVO(voaAllData[0]);
		// }

	}

	private UILabel ivjLbWareHouseID = null;
	

	/**
	 * 返回 LbWareHouseID 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLbWareHouseID() {
		if (ivjLbWareHouseID == null) {
			try {
				ivjLbWareHouseID = new nc.ui.pub.beans.UILabel();
				ivjLbWareHouseID.setName("LbWareHouseID");
				ivjLbWareHouseID.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC000-0000153")/* @res "仓库" */);
				ivjLbWareHouseID.setBounds(0, 4, 80, 22);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjLbWareHouseID;
	}

	/**
	 * 创建人：刘家清 创建日期：2007-9-26下午02:10:22 创建原因： LotNumbDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LotNumbDlg(java.awt.Container parent, boolean isMutiSel,
			boolean IsBodyMutiSel) {
		super(parent);
		initialize();
	}

	/**
	 * LotNumbDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public LotNumbDlg(java.awt.Container parent, boolean isMutiSel,String classname,String billtype,String tile) {
		super(parent);
		this.classname=classname;
		this.billtype=billtype;
		this.tile=tile;
		initialize();
		initListener();
	}
    /**
     *初始化 监听器
     */
	public  void initListener() {
	   getBillCardPanel().addBodyEditListener2(new BillEditListener2(){
		 public boolean beforeEdit(BillEditEvent e) {
//			 String key=e.getKey();
//			 int row=e.getRow();
//			 UFBoolean isde=PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "reserve14"), UFBoolean.FALSE);
//			 if(isde.booleanValue()==true){
//				 return false;
//			 }
//			 if("invcode".equals(key)){
//					JComponent jf= getBillCardPanel().getBodyItem(key).getComponent();
//					if(jf instanceof UIRefPane){
//						UIRefPane panel=(UIRefPane) jf;
//						//panel.setNotLeafSelectedEnabled(false);
//						panel.getRefModel().addWherePart(" and coalesce(laborflag,'N')='N'");
//					}			
//			 } 
			return true;
		 }		   
	   });
	}
	/**
	 * LotNumbDlg 构造子注解。
	 */
	public LotNumbDlg(boolean isMutiSel) {
		super();
		initialize();
	}



	/**
	 * 此处插入方法说明。 创建者：张欣 功能： 参数： 返回： 例外： 日期：(2001-5-11 11:31:45)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return tile;
	}

	/**
	 * 此处插入方法说明。 创建者：张欣 功能： 参数： 返回： 例外： 日期：(2001-6-12 16:55:10)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void onRefresh() {
		
		setVOtoBody(); // 重新将数据放入表体
	}

	/**
	 * 根据传入的仓库和存货ID从数据库表头、表体中查出相关数据 创建者：张欣 功能： 参数： 返回： 例外： 日期：(2001-5-16
	 * 13:38:16) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */

	public void setData(SuperVO[] vos) {		
		getBillCardPanel().getBillModel().setBodyDataVO(vos);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().updateValue();//去掉行状态变化
		BillModel model = getBillCardPanel().getBillModel();
		if (model.getRowCount() > 0) {
			for (int i = 0; i < model.getRowCount(); i++) {
				try {
					SuperVO bbvo = (SuperVO) Class.forName(classname).newInstance();
					String pkname = bbvo.getPKFieldName();
					String pk = PuPubVO.getString_TrimZeroLenAsNull(model.getValueAt(i, pkname));
					if (pk == null) {
						model.setRowState(i, BillModel.ADD);
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	   /**
     * 出库按 批次由小到大  按货架编码由小到大排序
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-9-13上午11:18:39
     * @param stocks
     * @throws BusinessException 
     */
//	private void sortout(StockInvOnHandVO[] stocks) throws BusinessException {
//       if(stocks==null || stocks.length==0)
//    	   return ;
//       //设置货架编码
//       for(int i=0;i<stocks.length;i++){
//    	   String cdtpk=stocks[i].getPplpt_pk();
//    	   String cdtcode=(String) ZmPubTool.execFomularClient("code->getColValue(bd_cargdoc_tray,cdt_traycode,cdt_pk,cdt_pk)", new String[]{"cdt_pk"}, new String[]{cdtpk});
//    	   stocks[i].setWhs_customize3(cdtcode);
//       }
//       VOUtil.ascSort(stocks,new String[]{"whs_batchcode","whs_customize3"});//按批次号  和货架编码排序	
//	}
	/**
	 * 取得系统信息。
	 */
	private ClientLink getCEnvInfo() {
		if (m_cl == null) {
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			m_cl = new ClientLink(ce);
		}
		return m_cl;

	}
    
	public BillCardPanel getBillCardPanel() {
		if (m_card == null) {
			try {
				m_card = new nc.ui.pub.bill.BillCardPanel();
				m_card.setName("BillCardPanel");
				m_card.setBounds(10, 84, 700, 400);

				BillData bd = new BillData(m_card.getTempletData(billtype,
						null, getCEnvInfo().getUser(), getCEnvInfo().getCorp(),
						null));
				if (bd == null) {
					nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
					return m_card;
				}
				// 自定义项
				BatchCodeDefSetTool.changeBillDataByBCDef(m_cl.getCorp(), bd);

				m_card.setBillData(bd);

				m_card.getBodyPanel().setBBodyMenuShow(false);
				m_card.getBodyPanel().setRowNOShow(false);
				m_card.setEnabled(true);
				m_card.setBodyMenuShow(false);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}

		}
		return m_card;
	}

	public void afterEdit(BillEditEvent arg0) {

	}

	public void bodyRowChange(BillEditEvent arg0) {
		int selrow = getBillCardPanel().getBillTable().getSelectedRow();

	}

	public void mouse_doubleclick(BillMouseEnent arg0) throws ValidationException {
		onOK();

	}


	public javax.swing.JPanel getIvjUIDialogContentPane() {
		return ivjUIDialogContentPane;
	}

	public void setIvjUIDialogContentPane(
			javax.swing.JPanel ivjUIDialogContentPane) {
		this.ivjUIDialogContentPane = ivjUIDialogContentPane;
	}

	public UIPanel getIvjUIPanel1() {
		return ivjUIPanel1;
	}

	public void setIvjUIPanel1(UIPanel ivjUIPanel1) {
		this.ivjUIPanel1 = ivjUIPanel1;
	}

	public UIButton getIvjBtnCancel() {
		return ivjBtnCancel;
	}

	public void setIvjBtnCancel(UIButton ivjBtnCancel) {
		this.ivjBtnCancel = ivjBtnCancel;
	}

	public UIButton getIvjBtnOK() {
		return ivjBtnOK;
	}

	public void setIvjBtnOK(UIButton ivjBtnOK) {
		this.ivjBtnOK = ivjBtnOK;
	}
	
	public ClientLink getM_cl() {
		return m_cl;
	}

	public void setM_cl(ClientLink m_cl) {
		this.m_cl = m_cl;
	}

	public BillCardPanel getM_card() {
		return m_card;
	}

	public void setM_card(BillCardPanel m_card) {
		this.m_card = m_card;
	}
	public UILabel getIvjLbWareHouseID() {
		return ivjLbWareHouseID;
	}

	public void setIvjLbWareHouseID(UILabel ivjLbWareHouseID) {
		this.ivjLbWareHouseID = ivjLbWareHouseID;
	}

	public void actionPerformed(ActionEvent e) {
		if(getHandler()!=null){
			UIButton bttn=(UIButton) e.getSource();
			try {
				getHandler().SonbeforeButtonClick(bttn.getName());
			} catch (BusinessException e1) {
				MessageDialog.showErrorDlg(this, "错误", e1.getMessage());
				return;
			}
		}
		if (e.getSource() == LotNumbDlg.this.getBtnOK())
			try {
			
				onOK();
			} catch (ValidationException e1) {
				MessageDialog.showErrorDlg(this, "错误", e1.getMessage());
			}
		if (e.getSource() == LotNumbDlg.this.getBtnCancel())
			onCancel();	
		if (e.getSource() == LotNumbDlg.this.getBtnAdd())
			onBoAdd();
		if (e.getSource() == LotNumbDlg.this.getBtnDel())
			onBoDel();
		if(getHandler()!=null){
			UIButton bttn=(UIButton) e.getSource();
			try {
				getHandler().SonafterButtonClick(bttn.getName());
			} catch (BusinessException e1) {
				MessageDialog.showErrorDlg(this, "错误", e1.getMessage());
				return;
			}
		}
	}

	private void onBoDel() {
		if (getBillCardPanel().getBillTable().getSelectedRow() > -1) {
			int[] rows=getBillCardPanel().getBillTable().getSelectedRows();
			for(int i=0;i<rows.length;i++){
				 UFBoolean isde=PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(rows[i], "reserve14"), UFBoolean.FALSE);
				 if(isde.booleanValue()==true){
					 return;
				 }
			}
			getBillCardPanel().delLine();
		}		
	}

	private void onBoAdd() {
		getBillCardPanel().getBillModel().addLine();	
		BillRowNo.addLineRowNo(getBillCardPanel(),billtype, "crowno");
	}
	protected void dataNotNullValidate() throws ValidationException {
		StringBuffer message = null;
		BillItem[] headtailitems = getBillCardPanel()
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
		String[] tableCodes = getBillCardPanel()
				.getBillData().getTableCodes(BillData.BODY);
		if (tableCodes != null) {
			for (int t = 0; t < tableCodes.length; t++) {
				String tablecode = tableCodes[t];
				for (int i = 0; i < 
						getBillCardPanel().getBillModel(tablecode)
						.getRowCount(); i++) {
					StringBuffer rowmessage = new StringBuffer();

					rowmessage.append(" ");
					if (tableCodes.length > 1) {
						rowmessage.append(
								getBillCardPanel().getBillData().getTableName(
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
					BillItem[] items = 
							getBillCardPanel().getBillData()
							.getBodyItemsForTable(tablecode);
					for (int j = 0; j < items.length; j++) {
						BillItem item = items[j];
						if (item.isShow() && item.isNull()) {// 如果卡片显示，并且为空，才非空校验
							Object aValue = 
									getBillCardPanel().getBillModel(tablecode)
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
	private boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}

}