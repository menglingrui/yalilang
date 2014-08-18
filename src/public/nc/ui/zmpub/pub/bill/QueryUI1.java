package nc.ui.zmpub.pub.bill;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.logging.Logger;
import nc.itf.zmpub.pub.ISonVOH;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;
import nc.vo.zmpub.pub.tool.ZmPubTool;
/**
 * �����ϸ�鿴����
 * @author zpm
 */
public class QueryUI1 extends nc.ui.pub.beans.UIDialog implements
		ActionListener, ListSelectionListener, BillEditListener,
		BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SonDefBillManageUI myClientUI;

	private JPanel ivjUIDialogContentPane = null;

	protected BillListPanel ivjbillListPanel = null;

	private String m_pkcorp = null;

	private String m_operator = null;

	private String m_billType = null;

	private UIPanel ivjPanlCmd = null;

	private UIButton ivjbtnOk = null;

	private UIButton ivjbtnCancel = null;

	private UIButton btn_addline = null;

	private UIButton btn_deline = null;
	
//	zhf add �����������̵İ�ʵ�����̹��ܰ�ť   ��
	private UIButton ivjbtnLock = null;

	private Map<String, List<SuperVO>> map = null;

	private boolean isEdit = true;
	
	private String pk_ware = null;//�ֿ�
	
	private boolean isSign = false;//�Ƿ�ǩ��ͨ��

	public QueryUI1(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, SonDefBillManageUI myClientUI,
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
		setTitle("�鿴������ϸ");
		setContentPane(getUIDialogContentPane());
		// ���ñ༩״̬
		setEdit();
		//
//		getbtnOk().addActionListener(this);
//		getbtnLock().addActionListener(this);
//		getbtnCancel().addActionListener(this);
//		getAddLine().addActionListener(this);
//		getDeline().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addBodyEditListener(this);
		getbillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
//		getbillListPanel().getBodyScrollPane("xew_proaccept_bb")
//				.addEditListener2(this);
		// ���ر�ͷ����
		loadHeadData();
	}

	public void setEdit() {
		getbillListPanel().setEnabled(isEdit);
		getbtnCancel().setEnabled(true);
		getbtnCancel().setEnabled(isEdit);
		getbtnOk().setEnabled(isEdit);
		getAddLine().setEnabled(isEdit);
		getDeline().setEnabled(isEdit);
		
//		getbtnLock().setEnabled(isEdit||!isSign);
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
				
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	// ���У����Ĭ��ֵ
	public void setBodyDefaultValue(int row) {}
	

	public Map<String, List<SuperVO>> getBufferData() {
		if (map == null) {
			map = cloneBufferData();
		}
		return map;
	}

	public Map<String, List<SuperVO>> cloneBufferData() {
		Map<String, Map<String,SuperVO>> map1 = myClientUI.getHl1();
		Map<String, List<SuperVO>> map2 = new HashMap<String, List<SuperVO>>();
		if (map1.size() > 0) {
			Iterator<String> it = map1.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<SuperVO> list = new ArrayList<SuperVO>();
				Map<String,SuperVO> bmap=map1.get(key);
				if(bmap!=null&&bmap.size()>0){
					for(String bkey:bmap.keySet()){
						list.add(bmap.get(bkey));
					}
				}
				map2.put(key, cloneBBVO(list));
			}
		}
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

	protected SuperVO getHeadBVO(int row) {
		SuperVO vo = (SuperVO) getbillListPanel().getHeadBillModel()
				.getBodyValueRowVO(row,myClientUI.getUIControl().getBillVoName()[2]);
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

	// ����
	protected void onLineAdd() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		getbillListPanel().getBodyBillModel().addLine();
		ISonVOH sh=(ISonVOH) Class.forName(myClientUI.getUIControl().getBillVoName()[0]).newInstance();
		Class.forName(sh.getSonClass()).getName();
		SuperVO[] bvos=(SuperVO[]) getbillListPanel().getBodyBillModel().getBodyValueVOs(Class.forName(sh.getSonClass()).getName());
		int rowno=ZmPubTool.getMaxRowNo((HYChildSuperVO[])bvos);
		int newno=rowno+10;
		getbillListPanel().getBodyBillModel().setValueAt(rowno+10+"", getbillListPanel().getBodyBillModel().getRowCount()-1, "crowno");
		int hrow=getbillListPanel().getHeadTable().getSelectedRow();
		SuperVO bvo=(SuperVO) Class.forName(myClientUI.getUIControl().getBillVoName()[2]).newInstance();
		String pk=(String) getbillListPanel().getHeadBillModel().getValueAt(hrow, bvo.getPKFieldName());
		getbillListPanel().getBodyBillModel().setValueAt(pk, getbillListPanel().getBodyBillModel().getRowCount()-1, "crowno")	;	
	}



	// ɾ��
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
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// ��ѡ
				ivjbillListPanel.getBodyTable().setSelectionMode(
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// ��ѡ
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
			ivjPanlCmd.add(getAddLine(), getAddLine().getName());
			ivjPanlCmd.add(getDeline(), getDeline().getName());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			//ivjPanlCmd.add(getbtnLock(),getbtnLock().getName());
		}
		return ivjPanlCmd;
	}

	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText("ȷ��");
		}
		return ivjbtnOk;
	}

	private UIButton getAddLine() {
		if (btn_addline == null) {
			btn_addline = new UIButton();
			btn_addline.setName("addline");
			btn_addline.setText("����");
		}
		return btn_addline;
	}

	private UIButton getDeline() {
		if (btn_deline == null) {
			btn_deline = new UIButton();
			btn_deline.setName("deline");
			btn_deline.setText("ɾ��");
		}
		return btn_deline;
	}

	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText("ȡ��");
		}
		return ivjbtnCancel;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnOk())) {
			try {
				saveCurrentData(getHeadCurrentRow());
				check();
				closeOK();
				onboSave();
			} catch (Exception e1) {
				MessageDialog.showErrorDlg(this, "����", e1.getMessage());
			}
		} else if (e.getSource().equals(getbtnCancel())) {
			closeCancel();
		} else if (e.getSource().equals(getAddLine())) {
			try {
				onLineAdd();
			} catch (Exception e1) {
				e1.printStackTrace();
				MessageDialog.showErrorDlg(this, "����", e1.getMessage());
			}
		} else if (e.getSource().equals(getDeline())) {
			onLineDel();
		}
	}
	public  void onboSave() throws InstantiationException, IllegalAccessException, ClassNotFoundException, UifException {
		Map<String, List<SuperVO>> map= getBufferData();
		if(map!=null&& map.size()>0){
			List list=new ArrayList();
		    for(String key:map.keySet()){
		    	if(map.get(key)!=null&& map.get(key).size()>0){
		    		list.addAll(map.get(key));
		    	}
		    }	
			ISonVOH sh=(ISonVOH) Class.forName(myClientUI.getUIControl().getBillVoName()[0]).newInstance();;
		    SuperVO[] vos=(SuperVO[]) list.toArray((SuperVO[]) java.lang.reflect.Array.newInstance(Class.forName(sh.getSonClass()), 0));
		    if(vos!=null &&vos.length>0){
		    	HYPubBO_Client.updateAry(vos);
		    }
		}
	}
	public void saveCurrentData(int row) {
		if (row < 0) {
			return;
		}
		HYChildSuperVO bvo = (HYChildSuperVO) getHeadBVO(row);
		String key = bvo.getCrowno();
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




	// �����к���VO
	public SuperVO getGenBVO(String crowno) {
		SuperVO bvo = null;
		int row = getbillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < row; i++) {
			Object o = getbillListPanel().getHeadBillModel().getValueAt(i,
					"crowno");
			if (o.equals(crowno)) {
				bvo = getHeadBVO(i);
			}
		}
		return bvo;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == getbillListPanel().getHeadTable()
				.getSelectionModel()) {
			// ��������
			// ���¼��ر�������
		}
	}

	// ȡ��������ݻ�
	public void calcMaxTray() {
		// Ŀǰͨ����ʽ�Զ���ѯ����
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
			// ��������
			int oldrow = e.getOldRow();
			if (oldrow >= 0) {
				saveCurrentData(oldrow);
			}
			// ��ձ�������
			getbillListPanel().getBodyBillModel().clearBodyData();
			// ���¼��ر�������
			int row = e.getRow();
			HYChildSuperVO newbvo = (HYChildSuperVO) getHeadBVO(row);
			String key2 = newbvo.getCrowno();
            String pk=newbvo.getPrimaryKey();
			ArrayList<SuperVO> list = (ArrayList<SuperVO>)getBufferData().get(key2+pk);
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
	
	private String getkey(int row) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		ISonVOH hbill=(ISonVOH) Class.forName(myClientUI.getUIControl().getBillVoName()[0]).newInstance();
		SuperVO son= (SuperVO) Class.forName(hbill.getSonClass()).newInstance();
		return PuPubVO.getString_TrimZeroLenAsNull(getBodyValue(row, son.getPKFieldName()))+","+
		PuPubVO.getString_TrimZeroLenAsNull(getBodyValue(row, "crowno"));
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
