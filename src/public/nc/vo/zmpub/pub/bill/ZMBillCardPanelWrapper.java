package nc.vo.zmpub.pub.bill;

import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.ICardController;
import nc.ui.zmpub.pub.bill.BillRowNo;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * 支持自动生成行号的单据卡片Panel
 * @author Administrator
 *
 */
public class ZMBillCardPanelWrapper extends BillCardPanelWrapper{
	private Class m_BodyVOClass;
	// 自定义项的引用
	private ArrayList m_billDef = null;
	private boolean m_isShowWhenDefIsUsed;
	
	public ZMBillCardPanelWrapper(
			ClientEnvironment ce,
			ICardController ctl,
			String pk_busiType,
			String nodeKey,
			ArrayList defAry,
			String moduleCode
			)
			throws Exception {
			super(ce, ctl, pk_busiType, nodeKey);
			m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
			m_ctl = ctl;
			m_billDef = defAry;
			setModuleCode(moduleCode);
			initCardPanel();
		}
	public ZMBillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, ArrayList defAry) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billDef = defAry;
		initCardPanel();
	}
	private BillData m_billData = null;
	public ZMBillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, BillData billData) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billData = billData;
		initCardPanel();
	}

	public ZMBillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, BillData billData, ArrayList defList) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billData = billData;
		m_billDef = defList;
		initCardPanel();
	}

	public ZMBillCardPanelWrapper(ClientEnvironment ce, ICardController ctl, String pk_busiType, String nodeKey, BillData billData, ArrayList defList, boolean isShowWhenDefIsUsed) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		m_BodyVOClass = Class.forName(ctl.getBillVoName()[2]);
		m_ctl = ctl;
		m_billData = billData;
		m_billDef = defList;
		this.m_isShowWhenDefIsUsed = isShowWhenDefIsUsed;
		initCardPanel();

	}
	public ZMBillCardPanelWrapper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 此处插入方法说明。 创建日期：(2001-3-27 11:09:34)
	 * 
	 * @param e
	 *            java.awt.event.ActionEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e) {
		try {
			BillScrollPane bsp = getBillCardPanel().getBodyPanel();

			UIMenuItem item = (UIMenuItem) e.getSource();

			if (item == bsp.getMiInsertLine()) {
				 insertLine();		 
			} else if (item == bsp.getMiAddLine()) {
				addLine();
			} else if (item == bsp.getMiDelLine()) {
				deleteSelectedLines();
			} else if (item == bsp.getMiCopyLine()) {
				copySelectedLines();
			} else if (item == bsp.getMiPasteLine()) {
				pasteLines();
			} else if (item == bsp.getMiPasteLineToTail()) {
				pasteLinesToTail();
			}
		} catch (Exception ex) {
			System.out.println("line error!!!!");
		}
	}
	/**
	 * 增行
	 */
	public void addLine() throws Exception {
		getBillCardPanel().addLine();
		try {
			BillRowNo.addLineRowNo(getBillCardPanel(), getBillCardPanel().getBillType(), "crowno");
		} catch (Exception ex) {
			System.out.println("行号设置错误");
		}
	}
	/**
	* 增行
	*/
	public void insertLine() throws Exception {
		getBillCardPanel().stopEditing();
		getBillCardPanel().insertLine();
		BillRowNo.addLineRowNo(getBillCardPanel(), getBillCardPanel().getBillType(), "crowno");
		int row=getBillCardPanel().getBillTable().getSelectedRow();
		if(row>=0){
		  BillRowNo.insertLineRowNos(getBillCardPanel(), getBillCardPanel().getBillType(), "crowno", row+1, 1);
		}
	}
	
	public void pasteLinesToTail() {
		if (getCopyedBodyVOs() != null)
			for (int i = 0; i < getCopyedBodyVOs().length; i++) {
				getBillCardPanel().stopEditing();
				getBillCardPanel().addLine();
				int lastrow = getBillCardPanel().getBillTable().getRowCount()-1;
				getBillCardPanel().getBillModel().setBodyRowVO(getCopyedBodyVOs()[i], lastrow);
				execLoadBodyRowFormula(lastrow);
				try {
					BillRowNo.addLineRowNo(getBillCardPanel(), getBillCardPanel().getBillType(), "crowno");
					
				} catch (Exception ex) {
					System.out.println("行号设置错误");
				}
			}
		
	}
	/**
	 * 执行表体公式加载数据。 创建日期：(2004-2-18 22:55:25)
	 * 
	 * @param intRow
	 *            int
	 */
	private void execLoadBodyRowFormula(int intRow) {
		try {
			if (getBillCardPanel().getBillModel() == null)
				return;

			BillItem[] items = getBillCardPanel().getBillModel().getBodyItems();

			String[] formulas = getExecLoadFormula(items);

			if (formulas != null)
				getBillCardPanel().getBillModel().execFormula(intRow, formulas);
		} catch (Exception ex) {
			System.out.println("BillListWrapper:执行行公式加载数据错误");
			ex.printStackTrace();
		}
	}
	
	/**
	 * 粘贴当前所选择的行
	 */
	public void pasteLines() {
		if (getCopyedBodyVOs() != null)
			for (int i = 0; i < getCopyedBodyVOs().length; i++) {
				getBillCardPanel().stopEditing();
				getBillCardPanel().insertLine();
				int selectedRow = getBillCardPanel().getBillTable().getSelectedRow();
				getBillCardPanel().getBillModel().setBodyRowVO(getCopyedBodyVOs()[i], selectedRow);
				execLoadBodyRowFormula(selectedRow);				
				BillRowNo.addLineRowNo(getBillCardPanel(), getBillCardPanel().getBillType(), "crowno");
				if(selectedRow>=0){
				  BillRowNo.insertLineRowNos(getBillCardPanel(), getBillCardPanel().getBillType(), "crowno", selectedRow+1, 1);
				}
			}
	}
	
	/**
	 * 复制当前所选择的行
	 */
	public void copySelectedLines() {
		int selectedRow = getBillCardPanel().getBillTable().getSelectedRow();
		if (selectedRow != -1) {
			CircularlyAccessibleValueObject[] vos = getSelectedBodyVOs();
			// getBillCardPanel().getBillData().getBillModel().getBodySelectedVOs(
			// m_BodyVOClass.getName());
			// modify mlr 复制行清空主键
			if(vos!=null&&vos.length>0){
				for(int i=0;i<vos.length;i++){
					try {
						vos[i].setPrimaryKey(null);
						vos[i].setAttributeValue("crowno", null);
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
			}
			setCopyedBodyVOs(vos);

		}
	}
}
