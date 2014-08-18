package nc.ui.zmpub.pub.bill;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.base.AbstractBillUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * @author mlr
 */
public class ZMEditDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, ListSelectionListener, BillEditListener,
		BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel ivjUIDialogContentPane = null;
	
	private AbstractBillUI myClientUI=null;

	protected BillListPanel ivjbillListPanel = null;

	private String m_pkcorp = null;

	private String m_operator = null;

	private String m_billType = null;

	private UIPanel ivjPanlCmd = null;

	private UIButton ivjbtnOk = null;

	private UIButton ivjbtnCancel = null;

	private UIButton btn_addline = null;

	private UIButton btn_deline = null;
	
//	zhf add 增加虚拟托盘的绑定实际托盘功能按钮   绑定
	private UIButton ivjbtnLock = null;

	private Map<String, List<SuperVO>> map = null;

	private boolean isEdit = true;
	
	private String pk_ware = null;//仓库
	
	private boolean isSign = false;//是否签字通过
	    
    private int oldrow=-1;

	public ZMEditDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, AbstractBillUI myClientUI,
			boolean isEdit) {
		super(myClientUI);
		this.myClientUI = myClientUI;
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.isEdit = isEdit;
		init();
	}

	private void init() {
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle("tile");
		setContentPane(getUIDialogContentPane());
		// 设置编缉状态
		setEdit();
		//
		getDeleteBtn().addActionListener(this);
		getAddBtn().addActionListener(this);
		getbtnOk().addActionListener(this);
		getbtnLock().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getAddLine().addActionListener(this);
		getDeline().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addBodyEditListener(this);
		getbillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
		getbillListPanel().getBodyScrollPane(getbillListPanel().getChildListPanel().getTableCode())
				.addEditListener2(this);
		// 加载表头数据
		loadHeadData();
	}

	public void setEdit() {
		getbillListPanel().setEnabled(isEdit);
		getbtnCancel().setEnabled(true);
		getbtnCancel().setEnabled(isEdit);
		getbtnOk().setEnabled(isEdit);
		getAddLine().setEnabled(isEdit);
		getDeline().setEnabled(isEdit);
		
		getbtnLock().setEnabled(isEdit||!isSign);
	}

	public void loadHeadData() {
		try {
			HYBillVO billvo = null;
			if(isEdit){
				billvo = (HYBillVO) myClientUI.getVOFromUI();
			}else{
				billvo = (HYBillVO)myClientUI.getBufferData().getCurrentVO();
			}
			
			if (billvo != null) {
				getbillListPanel().setHeaderValueVO(billvo.getChildrenVO());
				getbillListPanel().getHeadBillModel().execLoadFormula();
//				pk_ware = PuPubVO.getString_TrimZeroLenAsNull(billvo.getParentVO().getAttributeValue("geh_cwarehouseid"));
//				isSign = PuPubVO.getInteger_NullAs(billvo.getParentVO().getAttributeValue("pwb_fbillflag"),IBillStatus.FREE)==IBillStatus.CHECKPASS;
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	// 增行，孙表默认值
	public void setBodyDefaultValue(int row) {
		
		
	}

	public Map<String, List<SuperVO>> getBufferData() {
		if (map == null) {
			map = cloneBufferData();
		}
		return map;
	}

	public Map<String, List<SuperVO>> cloneBufferData() {
	//	Map<String, List<SuperVO>> map1 = myClientUI.getTrayInfor();
		Map<String, List<SuperVO>> map2 = new HashMap<String, List<SuperVO>>();
//		if (map1.size() > 0) {
//			Iterator<String> it = map1.keySet().iterator();
//			while (it.hasNext()) {
//				String key = it.next();
//				List<SuperVO> list = map1.get(key);
//				map2.put(key, cloneBBVO(list));
//			}
//		}
		return map2;
	}

	public List<SuperVO> cloneBBVO(List<SuperVO> list) {
		List<SuperVO> list1 = new ArrayList<SuperVO>();
		if (list != null && list.size() > 0) {
			for (SuperVO b : list) {
				list1.add((SuperVO) b.clone());
			}
		}
		return list1;
	}



	protected SuperVO getBodyVO(int row) {
		SuperVO vo = (SuperVO) getbillListPanel()
				.getBodyBillModel().getBodyValueRowVO(row,
						SuperVO.class.getName());
		return vo;
	}

	protected int getBodyCurrentRow() {
		int row = getbillListPanel().getBodyTable().getRowCount() - 1;
		return row;
	}

	protected int getHeadCurrentRow() {
		int row = getbillListPanel().getHeadTable().getSelectedRow();
		return row;
	}

	// 增行
	protected void onLineAdd() {
		getbillListPanel().getBodyBillModel().addLine();
		setBodyDefaultValue(getBodyCurrentRow());
	}


	// 删行
	protected void onLineDel() {
		int[] rows = getbillListPanel().getBodyTable().getSelectedRows();
		getbillListPanel().getBodyBillModel().delLine(rows);
	}

	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet(m_billType, null, m_operator,
						m_pkcorp);
				ivjbillListPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// 单选
				ivjbillListPanel.getBodyTable().setSelectionMode(
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// 单选
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
				ivjbillListPanel.setEnabled(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getAddBtn(), getAddBtn().getName());
			ivjPanlCmd.add(getDeleteBtn(), getDeleteBtn().getName());
			ivjPanlCmd.add(getAddLine(), getAddLine().getName());
			ivjPanlCmd.add(getDeline(), getDeline().getName());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			ivjPanlCmd.add(getbtnLock(),getbtnLock().getName());
		}
		return ivjPanlCmd;
	}
	private UIButton ivjbtnOk1 = null;
	private UIButton getAddBtn() {
		if (ivjbtnOk1 == null) {
			ivjbtnOk1 = new UIButton();
			ivjbtnOk1.setName("btnOk");
			ivjbtnOk1.setText("增加");
		}
		return ivjbtnOk1;
	}
	private UIButton ivjbtnOk2 = null;

	
	private UIButton getDeleteBtn() {
		if (ivjbtnOk2 == null) {
			ivjbtnOk2 = new UIButton();
			ivjbtnOk2.setName("btnOk");
			ivjbtnOk2.setText("删除");
		}
		return ivjbtnOk2;
	}
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText("确定");
		}
		return ivjbtnOk;
	}

	private UIButton getAddLine() {
		if (btn_addline == null) {
			btn_addline = new UIButton();
			btn_addline.setName("addline");
			btn_addline.setText("增行");
		}
		return btn_addline;
	}

	private UIButton getDeline() {
		if (btn_deline == null) {
			btn_deline = new UIButton();
			btn_deline.setName("deline");
			btn_deline.setText("删行");
		}
		return btn_deline;
	}

	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText("取消");
		}
		return ivjbtnCancel;
	}
	
	// 添加绑定按钮
	private UIButton getbtnLock() {
		if (ivjbtnLock == null) {
			ivjbtnLock = new UIButton();
			ivjbtnLock.setName("ivjbtnLock");
			ivjbtnLock.setText("绑定");
		}
		return ivjbtnLock;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnOk())) {
			try {
				saveCurrentData(getHeadCurrentRow());
				check();
				closeOK();
			} catch (Exception e1) {
				MessageDialog.showErrorDlg(this, "警告", e1.getMessage());
			}
		} else if (e.getSource().equals(getbtnCancel())) {
			closeCancel();
		} else if (e.getSource().equals(getAddLine())) {
			onLineAdd();
		} else if (e.getSource().equals(getDeline())) {
			onLineDel();
		}else if(e.getSource().equals(getAddBtn())){
		    onAdd();
		}else if(e.getSource().equals(getDeleteBtn())){
			onDelete();
		}
	}

	public void onDelete() {
		
		
	}

	public void onAdd() {
		getbillListPanel().getHeadBillModel().addLine();
		int selrow=getbillListPanel().getHeadBillModel().getRowCount()-1;
		
		BillEditEvent event =new BillEditEvent(getbillListPanel().getParentListPanel().getTable(), oldrow, selrow);
		
		
		bodyRowChange(event);
		
	}

	public void saveCurrentData(int row) {
		if (row < 0) {
			return;
		}
		String key = (String) getbillListPanel().getHeadBillModel().getValueAt(row, "crowno");
		SuperVO[] bvos = (SuperVO[]) getbillListPanel()
				.getBodyBillModel().getBodyValueVOs(
						SuperVO.class.getName());
		if (bvos != null && bvos.length > 0) {
			getBufferData().put(key, arrayToList(bvos));
		} else {
			getBufferData().remove(key);
		}
	}

	public void check() throws BusinessException {

	}






	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == getbillListPanel().getHeadTable()
				.getSelectionModel()) {
			// 备份数据
			// 重新加载表体数据
		}
	}

	// 取托盘最大容积
	public void calcMaxTray() {
		// 目前通过公式自动查询出来
	}

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		return true;
	}

	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		saveCurrentData(getHeadCurrentRow());
	}

	public ArrayList<SuperVO> arrayToList(SuperVO[] o) {
		if (o == null || o.length == 0)
			return null;
		ArrayList<SuperVO> list = new ArrayList<SuperVO>();
		for (SuperVO s : o) {
			list.add(s);
		}
		return list;
	}

	public void bodyRowChange(BillEditEvent e) {
		if (e.getSource() == getbillListPanel().getParentListPanel().getTable()) {
			// 备份数据
			int oldrow = e.getOldRow();
			if (oldrow >= 0) {
				saveCurrentData(oldrow);
			}
			// 清空表体数据
			getbillListPanel().getBodyBillModel().clearBodyData();
			// 重新加载表体数据
			int row = e.getRow();
			this.oldrow=e.getRow();
			String key2 =(String) getbillListPanel().getHeadBillModel().getValueAt(row, "crowno");

			ArrayList<SuperVO> list = (ArrayList<SuperVO>)getBufferData().get(key2);
			if(list !=null && list.size() > 0){
				getbillListPanel().getBodyBillModel().setBodyDataVO(list.toArray(new SuperVO[0]));			

				getbillListPanel().getBodyBillModel().execLoadFormula();	
		}
		}
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	
	private Object getBodyValue(int row,String fieldname){
		return getbillListPanel().getBodyBillModel().getValueAt(row, fieldname);
	}
	
	
	private Object getHeadValue(String fieldname){
		if(PuPubVO.getString_TrimZeroLenAsNull(fieldname)==null)
			return null;
		int row = getbillListPanel().getHeadTable().getSelectedRow();
		if(row < 0)
			return null;
		return getbillListPanel().getHeadBillModel().getValueAt(row, fieldname);
	}	

}
