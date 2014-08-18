package nc.ui.pub.workflownote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.wfengine.engine.ActivityInstance;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.DapCall;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIEditorPane;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.workflowqry.FlowQryTableCellRenderer;
import nc.ui.wfengine.designer.ProcessGraph;
import nc.ui.wfengine.flowchart.FlowChart;
import nc.ui.wfengine.flowchart.UfWGraphModel;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.CurrencyInfo;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.uap.pf.WorkitemPrintDataOfBill;
import nc.vo.uap.wfmonitor.ProcessAdminInfo;
import nc.vo.uap.wfmonitor.ProcessRouteRes;
import nc.vo.wfengine.core.XpdlPackage;
import nc.vo.wfengine.core.parser.UfXPDLParser;
import nc.vo.wfengine.core.parser.XPDLNames;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.serializer.UfXPDLSerializer;
import nc.vo.wfengine.core.workflow.BasicWorkflowProcess;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.definition.IApproveflowConst;
import nc.vo.wfengine.pub.ActivityInstanceStatus;

/**
 * ���̹���Panel
 * 
 * @author leijun 2006-7-18
 * @modifier leijun 2007-3-13 �����а����п��Զ�����
 * @modifier leijun 2007-9-12 ����ͼ����XPDLԴ���л���ʾ
 * @modifier leijun 2008-7 ��������ͼ���ء���ʾ�л�����
 * @modifier leijun 2008-11 ���ݵ��������ж��Ƿ���Ҫ��ʾ�����
 * @modifier  yangtao 2013-10-23 ���ӷ�����pk
 */
public class FlowAdminPanel extends UIPanel implements ActionListener {
	/** �������� */
	public String m_strBillType = null;

	/** ҵ������ */
	public String m_strBusiType = null;

	/** ����ID */
	public String m_strBillID = null;

	/** �������� */
	public int m_iWorkflowtype;

	public String m_strBillNo;

	private UILabel ivjLabelTitle = null;

	/** ������״̬ */
	private int m_iMainFlowStatus = -1;

	/** ���������� */
	private WorkflownoteVO[] m_noteVOs = null;

	private UILabel labBillStatus;

	private UIComboBox comboBillStatus;

	private UISplitPane spCenter = null;

	// lj+
	private UIButton btnPrint = null;

	private UIPanel ivjPnTitle = null;

	private UITablePane ivjTablePnState = null;

	private Component m_container;

	private UIMenuItem m_miXpdl = null;

	private UIMenuItem m_miGraph = null;

	/**
	 * ����ͼҳǩ������ʾ��ʽ��ӳ���
	 * ��ʾ��ʽ��0��ʾ������ʾXPDL;1��ʾ������ʾ����ͼ
	 */
	private HashMap m_hmIndexToStyle = new HashMap();

	/**
	 * ����ͼҳǩ����ͼ�ε�ӳ���
	 */
	private HashMap m_hmIndexToGraph = new HashMap();

	/**
	 * ����ͼҳǩ����XPDL��ӳ���
	 */
	private HashMap m_hmIndexToXpdl = new HashMap();

	private UITabbedPane m_tabbedPane;

	private UICheckBox chbHideGraph;

	private ProcessAdminInfo m_pai;

	public FlowAdminPanel(ToftPanel container) {
		super();
		m_container = container;
	}

	public FlowAdminPanel(FlowStateDlg container) {
		super();
		m_container = container;
	}

	private UIMenuItem getMiXpdl() {
		if (m_miXpdl == null) {
			m_miXpdl = new UIMenuItem("Xpdl");
			m_miXpdl.addActionListener(this);
		}
		return m_miXpdl;
	}

	private UIMenuItem getMiGraph() {
		if (m_miGraph == null) {
			m_miGraph = new UIMenuItem("Graph");
			m_miGraph.addActionListener(this);
		}
		return m_miGraph;
	}

	/**
	 * ����õ��ݵ�����������ʷ�켣ͼ
	 * 
	 * @return
	 */
	private UITabbedPane constructGraph() {
		if (m_tabbedPane == null) {
			m_tabbedPane = new UITabbedPane();

			// �ҵ�WFTask���������̶��弰�丸���̶���
			ProcessRouteRes processRoute = null;
			try {
				processRoute = m_pai.getProcessRoute();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000237")/* @res "����" */, NCLangRes.getInstance().getStrByID(
						"pfworkflow", "UPPpfworkflow-000494")/*��ѯ���ݵ�����ͼ�����쳣��*/
						+ e.getMessage());
			}

			if (processRoute == null || processRoute.getXpdlString() == null)
				// WARN::˵���õ��ݾ�û������ʵ��
				return m_tabbedPane;

			// ��ȡ������״̬
			m_iMainFlowStatus = processRoute.getProcStatus();

			// ����һ����ʱ��
			XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
			pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

			// ��������ͼҳǩ
			constructGraphTab(processRoute, m_tabbedPane, pkg);

			addTabListener();
		}
		return m_tabbedPane;
	}

	private void addTabListener() {
		constructGraph().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//XXX:����ͼ��XPDL֮���л�
				if (((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) && e.isControlDown()) {
					// ������Ҽ����,�򵯳��˵�
					UIPopupMenu menu = new UIPopupMenu();

					int index = constructGraph().getSelectedIndex();
					Object obj = m_hmIndexToStyle.get(index);

					if (obj == null || ((Integer) obj) == 1)
						menu.add(getMiXpdl());
					else
						menu.add(getMiGraph());
					menu.show(constructGraph(), e.getX(), e.getY());
				}
			}
		});
	}

	/**
	 * ��������ͼҳǩ
	 * 
	 * @param lhm
	 * @return
	 */
	private void constructGraphTab(ProcessRouteRes processRoute, UITabbedPane tabPane, XpdlPackage pkg) {
		String def_xpdl = null;
		ProcessRouteRes currentRoute = processRoute;
		if (currentRoute.getXpdlString() != null)
			def_xpdl = currentRoute.getXpdlString().toString();

		WorkflowProcess wp = null;
		try {
			// ǰ̨����XML��Ϊ����
			wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
		} catch (XPDLParserException e) {
			Logger.error(e.getMessage(), e);
			return;
		}
		wp.setPackage(pkg);

		// ��ʼ��Graph
		FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		// ���ù�����ʾ
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		// auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);
		// auditChart.setBorder(BorderFactory.createEtchedBorder());
		ActivityInstance[] allActInstances = currentRoute.getActivityInstance();
		String[] startedActivityDefIds = new String[allActInstances.length];

		//��ǰ�����еĻ
		HashSet hsRunningActs = new HashSet();
		for (int i = 0; i < allActInstances.length; i++) {
			startedActivityDefIds[i] = allActInstances[i].getActivityID();
			if (allActInstances[i].getStatus() == ActivityInstanceStatus.Started.getIntValue())
				hsRunningActs.add(startedActivityDefIds[i]);
		}
		auditChart.setActivityRouteHighView(hsRunningActs, startedActivityDefIds, currentRoute
				.getActivityRelations(), Color.RED, Color.BLUE);

		UIScrollPane graphScrollPane = new UIScrollPane(auditChart);
		JViewport vport = graphScrollPane.getViewport();
		vport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

		String title = auditChart.getWorkflowProcess().getName() + " "
				+ ((BasicWorkflowProcess) auditChart.getWorkflowProcess()).getVersion();
		// graphScrollPane.setBorder(BorderFactory.createEtchedBorder());
		tabPane.addTab(title, graphScrollPane);
		m_hmIndexToGraph.put(tabPane.getTabCount() - 1, auditChart);

		ProcessRouteRes[] subRoutes = processRoute.getSubProcessRoute();
		for (int i = 0; i < (subRoutes == null ? 0 : subRoutes.length); i++) {
			currentRoute = subRoutes[i]; // ȡ�����̣�����ѭ��
			constructGraphTab(currentRoute, tabPane, pkg);
		}
	}

	private UILabel getLabBillStatus() {
		if (labBillStatus == null) {
			labBillStatus = new UILabel(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000023")/*����״̬*/);
		}
		return labBillStatus;
	}

	private UIComboBox getComboApproveStatus() {
		if (comboBillStatus == null) {
			comboBillStatus = new UIComboBox();
			//XXX:ȡֵӦ��IWorkFlowStatus�еĳ�������һ��
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000273")/* @res "����ͨ��" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000502")/* @res "����������" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000503")/* @res "�ύ̬" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000274")/* @res "������ͨ��" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000504")/* @res "��������" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000504")/* @res "��������" */);

			comboBillStatus.setEditable(false);
			comboBillStatus.setEnabled(false);
		}
		return comboBillStatus;
	}

	private UIComboBox getComboWorkflowStatus() {
		if (comboBillStatus == null) {
			comboBillStatus = new UIComboBox();
			//XXX:ȡֵӦ��IWorkFlowStatus�еĳ�������һ��
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000017")/*����������*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000018")/*������������*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000019")/*������������*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000274")/* @res "������ͨ��" */); //XXX:��������û�����״̬
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000020")/*�޹�����*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000020")/*�޹�����*/);

			comboBillStatus.setEditable(false);
			comboBillStatus.setEnabled(false);
		}
		return comboBillStatus;
	}

	private FlowLayout getPnTitleFlowLayout() {
		FlowLayout ivjPnTitleFlowLayout = null;

		ivjPnTitleFlowLayout = new FlowLayout();
		ivjPnTitleFlowLayout.setVgap(8);

		return ivjPnTitleFlowLayout;
	}

	private UITablePane getTablePnState() {
		if (ivjTablePnState == null) {
			ivjTablePnState = new UITablePane();
			ivjTablePnState.setName("TablePnState");
			// ���ñ���е�ѡģʽ
			ivjTablePnState.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// �����е���ģʽ
			ivjTablePnState.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return ivjTablePnState;
	}

	/**
	 * ��ñ��
	 */
	public UITable getTableState() {
		return getTablePnState().getTable();

	}

	public UISplitPane getSpCenter() {
		if (spCenter == null) {
			spCenter = new UISplitPane(UISplitPane.VERTICAL_SPLIT);
			spCenter.add(constructGraph(), UISplitPane.TOP);
			spCenter.add(getTablePnState(), UISplitPane.BOTTOM);
			spCenter.setDividerLocation(300);
		}
		return spCenter;
	}

	private UIButton getBtnPrint() {
		if (btnPrint == null) {
			btnPrint = new UIButton(NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/* @res "��ӡ" */);
			btnPrint.setPreferredSize(new Dimension(60, 20));
			btnPrint.addActionListener(this);
		}
		return btnPrint;
	}

	private UIPanel getPnTitle() {
		if (ivjPnTitle == null) {
			ivjPnTitle = new UIPanel();
			ivjPnTitle.setName("PnTitle");
			ivjPnTitle.setPreferredSize(new Dimension(10, 36));
			ivjPnTitle.setLayout(getPnTitleFlowLayout());
			ivjPnTitle.add(getLabelTitle());

			ivjPnTitle.add(getLabBillStatus());
			boolean isWorkflow = m_iWorkflowtype == IApproveflowConst.WORKFLOW_TYPE_WORKFLOW;
			ivjPnTitle.add(isWorkflow ? getComboWorkflowStatus() : getComboApproveStatus());

			ivjPnTitle.add(getBtnPrint());
			ivjPnTitle.add(getChbHideGraph());
		}
		return ivjPnTitle;
	}

	public UICheckBox getChbHideGraph() {
		if (chbHideGraph == null) {
			chbHideGraph = new UICheckBox(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000138")/*����ͼ*/);
			chbHideGraph.setSelected(true);
			chbHideGraph.addActionListener(this);
		}
		return chbHideGraph;
	}

	public void actionPerformed(ActionEvent e) {
		Object evtSource = e.getSource();
		if (evtSource == btnPrint) {
			// System.out.println("Print");
			if (m_noteVOs == null || m_noteVOs.length == 0) { return; }

			// onPrintDirect();
			onPrintTemplate();

			// �رնԻ��� leijun 2004-12-10
			if (m_container instanceof FlowStateDlg)
				((FlowStateDlg) m_container).dispose();
		} else if (evtSource == getMiXpdl()) {
			int iSelectedIndex = constructGraph().getSelectedIndex();
			//XXX:��ʾ��ǰ����ͼ��XPDL����
			m_hmIndexToStyle.put(iSelectedIndex, 0);
			UIScrollPane graphScrollPane = (UIScrollPane) constructGraph().getSelectedComponent();
			if (m_hmIndexToXpdl.get(iSelectedIndex) == null) {
				FlowChart flowChart = (FlowChart) graphScrollPane.getViewport().getView();
				String strXpdl = "Error occured,please see log!";
				try {
					strXpdl = UfXPDLSerializer.getInstance().serialize(flowChart.getWorkflowProcess());
				} catch (Exception ex) {
					Logger.error(ex.getMessage(), ex);
					strXpdl += "----------------------------\n" + ex.getMessage();
				}
				UIEditorPane ep = new UIEditorPane();
				ep.setEditable(false);
				ep.setText(strXpdl);
				m_hmIndexToXpdl.put(iSelectedIndex, ep);
			}
			graphScrollPane.setViewportView((UIEditorPane) m_hmIndexToXpdl.get(iSelectedIndex));
		} else if (evtSource == getMiGraph()) {
			int iSelectedIndex = constructGraph().getSelectedIndex();
			//XXX:��ʾ��ǰXPDL���������ͼ
			m_hmIndexToStyle.put(iSelectedIndex, 1);
			UIScrollPane graphScrollPane = (UIScrollPane) constructGraph().getSelectedComponent();
			graphScrollPane.setViewportView((FlowChart) m_hmIndexToGraph.get(iSelectedIndex));
		} else if (evtSource == getChbHideGraph()) {
			boolean isShow = getChbHideGraph().isSelected();
			if (isShow) {
				getSpCenter().add(constructGraph(), UISplitPane.TOP);
				getSpCenter().setDividerLocation(300);
			} else
				getSpCenter().remove(constructGraph());
		}
	}

	/**
	 * ʹ�ô�ӡģ���ӡ
	 * 
	 * @since NC5.0
	 */
	private void onPrintTemplate() {
		if (m_noteVOs == null || m_noteVOs.length < 1)
			return;
		// ��ӡԤ��
		PrintEntry pe = new PrintEntry(this, generateDataSource());
		// ���ô�ӡģ��ID�Ĳ�ѯ����
		pe.setTemplateID(DapCall.getPkcorp(), "101206", DapCall.getOperator(), null);
		// ��������˶����ӡģ�壬��ѡ��һ��ģ��
		int iResult = pe.selectTemplate();
		if (iResult == 1) {
			// ��ʼ��ӡ
			// pe.print();
			// Ҳ������Ԥ��������Ԥ�������ӡ
			pe.preview();
		}
	}

	/**
	 * ����������Ϣ����е���������ӡ����Դ
	 * @return
	 */
	private IDataSource generateDataSource() {
		HashMap<String, String[]> hmDatas = new HashMap<String, String[]>();

		VOTableModel ftm = (VOTableModel) getTableState().getModel();

		int rowCount = ftm.getRowCount();

		// ���������� - �ӻ����л�ȡ
		BilltypeVO billtypeVO = PfUIDataCache.getBillTypeInfo(m_strBillType);
		String billtypeName = Pfi18nTools.i18nBilltypeName(billtypeVO.getPk_billtypecode(), billtypeVO
				.getBilltypename());
		String[] colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = billtypeName;
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLTYPE, colValues);

		// ���ݺ�
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = m_noteVOs[0].getBillno();
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLNO, colValues);

		// ҵ�������� - �ӻ����л�ȡ
		SQLParameter para = new SQLParameter();
		para.addParam(m_strBusiType);
		Object obj = DBCacheFacade.runQuery("select businame from bd_busitype where pk_busitype=?",
				para, new ColumnProcessor(1));
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = String.valueOf(obj == null ? "" : obj);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BUSITYPE, colValues);

		// ��˾���� -  
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			String strCorp = m_noteVOs[j].getPk_corp();
			if (StringUtil.isEmptyWithTrim(strCorp))
				colValues[j] = "";
			else {
				try {
					colValues[j] = DapCall.getCorpInfoByPK(strCorp)[1];
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CORP, colValues);
		
	
		

		// �������� - 
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			String strCurr = m_noteVOs[j].getCurrency();
			if (StringUtil.isEmptyWithTrim(strCurr))
				colValues[j] = "";
			else {
				CurrtypeVO currVo = PfUIDataCache.getCurrType(strCurr);
				colValues[j] = currVo.getCurrtypename();
			}
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CURRTYPE, colValues);

		// ����ʱ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 1);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDDATE, colValues);

		// ����������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 0);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDMAN, colValues);

		// ����ʱ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 3);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DEALDATE, colValues);

		// ��ʱ
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 4);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DURATION, colValues);

		// ����������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 2);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CHECKMAN, colValues);

		// ������PK
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = m_noteVOs[j].getCheckman();
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_PK_CHECKMAN, colValues);
		
		// ������pk    yangtao
		colValues = new String[rowCount];
		for (int j=0; j<rowCount; j++){
			Object value = m_noteVOs[j].getSenderman();
			colValues[j] = String.valueOf(value==null? "":value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_PK_SENDCKMAN, colValues);

		// ��������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 7);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_NOTE, colValues);

		// ԭ�ҽ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 9);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_MONEY, colValues);

		// ���ҽ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 10);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_LOCALMONEY, colValues);

		// ���ҽ��
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 11);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_ASSMONEY, colValues);

		// �������
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 6);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_APPROVERESULT, colValues);

		return new WorkitemPrintDataOfBill(hmDatas);
	}

	/**
	 * @param strBillType
	 * @param strBusitype
	 * @param strBillID
	 * @param strBillNo
	 * @param iWorkflowtype �������ͣ�1=������ 3=������
	 */
	public void initAdminPanel(String strBillType, String strBusitype, String strBillID,
			String strBillNo, int iWorkflowtype) {

		m_strBusiType = strBusitype;
		m_strBillType = strBillType;
		m_strBillID = strBillID;
		m_strBillNo = strBillNo;
		m_iWorkflowtype = iWorkflowtype;

		//XXX:leijun+2009-8 ���̼��ר�õġ��ϲ����Զ�̵��õļ����Ϣ
		try {
			m_pai = NCLocator.getInstance().lookup(IPFWorkflowQry.class).queryFlowInfo4Admin(m_strBillID,
					m_strBillType, m_strBusiType, m_iWorkflowtype);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/* @res "����" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000505")/*��ѯ�õ��ݵ�������ʷ��Ϣ�����쳣��*/
					+ e.getMessage());
			return;
		}

		this.setLayout(new BorderLayout());
		this.add(getPnTitle(), BorderLayout.NORTH);
		this.add(getSpCenter(), BorderLayout.CENTER);

		if (IApproveflowConst.WORKFLOW_TYPE_APPROVE == m_iWorkflowtype)
			initByApproveflow();
		else
			initByWorkflow();

		// ���������е���Ⱦ��
		TableColumn noteColumn = getTableState().getColumn(
				NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000205")/* @res "����" */);
		noteColumn.setCellRenderer(new FlowQryTableCellRenderer());

	}

	/**
	 * ��ʼ�� �������������
	 */
	private void initByWorkflow() {
		WorkflowItemTableModel fsTM = new WorkflowItemTableModel(WorkflownoteVO.class);
		getTableState().setModel(fsTM);
		// �����п�
		getTableState().setColumnWidth(fsTM.getColumnWidth());
		//��ѯ������������
		queryWorkflowItems();
		//��䵽��ģ��
		populateWithWorkitems(fsTM);

		// ��ȡ�õ��ݵĹ�����״̬
		setBillWorkflowStatus();

		// ���ñ���
		BilltypeVO billType = PfUIDataCache.getBillTypeInfo(m_strBillType);
		String strBilltypeName = Pfi18nTools.i18nBilltypeName(billType.getPk_billtypecode(), billType
				.getBilltypename());
		getLabelTitle()
				.setText(
						strBilltypeName
								+ NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000021")/*������״��*/);

	}

	/**
	 * ��ʼ�� �������������
	 */
	private void initByApproveflow() {
		ApproveItemTableModel fsTM = new ApproveItemTableModel(WorkflownoteVO.class);
		getTableState().setModel(fsTM);
		// �����п�
		getTableState().setColumnWidth(fsTM.getColumnWidth());

		//��ѯ����������
		queryApproveItems();
		//��䵽��ģ��
		populateWithWorkitems(fsTM);

		// ��ȡ�õ��ݵ�����״̬
		setBillApproveStatus();

		// ���ñ���
		BilltypeVO billType = PfUIDataCache.getBillTypeInfo(m_strBillType);
		String strBilltypeName = Pfi18nTools.i18nBilltypeName(billType.getPk_billtypecode(), billType
				.getBilltypename());
		getLabelTitle()
				.setText(
						strBilltypeName
								+ NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000191")/* @res "����״��" */);
	}

	private void queryWorkflowItems() {
		// ��ѯ�õ��ݵ���ʷ���������Ϣ
		// �����Ѿ�������ɵ� ����δ����Ĺ�����
		try {
			m_noteVOs = m_pai.getNoteVOs();

			// ����ϵͳ������������������þ���
			adjustMoneyScale(m_noteVOs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/* @res "����" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000505")/*��ѯ�õ��ݵ�������ʷ��Ϣ�����쳣��*/
					+ e.getMessage());
		}
	}

	private void queryApproveItems() {
		// ��ѯ�õ��ݵ���ʷ������Ϣ
		// �����Ѿ�������ɵ� ����δ�����Ĺ�����
		try {
			m_noteVOs = m_pai.getNoteVOs();

			// ����ϵͳ������������������þ���
			adjustMoneyScale(m_noteVOs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/* @res "����" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000022")/*��ѯ�õ��ݵ���ʷ������������Ϣ�����쳣��*/
					+ e.getMessage());
		}
	}

	private UILabel getLabelTitle() {
		if (ivjLabelTitle == null) {
			ivjLabelTitle = new UILabel();
			ivjLabelTitle.setName("LabelTitle");
			ivjLabelTitle.setText("Title");
			ivjLabelTitle.setForeground(Color.black);
			ivjLabelTitle.setILabelType(UILabel.STYLE_TITLE_REPORT);
			ivjLabelTitle.setPreferredSize(new Dimension(400, 22));
			ivjLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);

		}
		return ivjLabelTitle;
	}

	/**
	 * ��ȡ�õ��ݵĹ�����״̬
	 */
	private void setBillWorkflowStatus() {
		try {
			// XXX::��<code>IWorkFlowStatus</code>����
			int iBillStatus = m_pai.getFlowStatus();

			getComboWorkflowStatus().setSelectedIndex(iBillStatus);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ��ȡ�õ��ݵ�������״̬
	 */
	private void setBillApproveStatus() {
		try {
			// XXX::��<code>IWorkFlowStatus</code>����
			int iBillStatus = m_pai.getFlowStatus();
			getComboApproveStatus().setSelectedIndex(iBillStatus);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * ����������䵽��ģ��
	 */
	private void populateWithWorkitems(VOTableModel fsTM) {
		
		// �����ģ��
		fsTM.clearTable();
		

		// �ж��Ƿ���Ҫ��ʾ����� leijun+2008-11
		boolean isShowMoney = isShowMoney(m_strBillType);
		if (isShowMoney) {
			boolean isAss = false;
			try {
				isAss = SysInitBO_Client.getParaBoolean(DapCall.getPkcorp(), "BD302").booleanValue();
				if (!isAss) {
					// ���ظ�����
					hideColumn(WorkitemColumnInfo.ASSMONEY.toString());
					hideColumn(WorkitemColumnInfo.ASSMONEY_BEFORE.toString());
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		} else {
			//���ؽ����
			hideColumn(WorkitemColumnInfo.MONEY.toString());
			hideColumn(WorkitemColumnInfo.MONEY_BEFORE.toString());
			hideColumn(WorkitemColumnInfo.LOCALMONEY.toString());
			hideColumn(WorkitemColumnInfo.LOCALMONEY_BEFORE.toString());
			hideColumn(WorkitemColumnInfo.ASSMONEY.toString());
			hideColumn(WorkitemColumnInfo.ASSMONEY_BEFORE.toString());
		}
		
		if (m_noteVOs == null || m_noteVOs.length < 1)
			return;
		
		//��VO������ӵ���ģ��
		for (int i = 0; i < m_noteVOs.length; i++) {
			fsTM.addVO(m_noteVOs[i]);
			
		}
	}

	/**
	 * ���ر���ĳ��
	 * @param strIdentifier
	 */
	private void hideColumn(String strIdentifier) {
		TableColumnModel tcm = getTableState().getColumnModel();
		int iColIndexAssMoney = PfUtilUITools.getColumnIndex(strIdentifier, tcm);
		if (iColIndexAssMoney > -1)
			getTableState().removeColumn(tcm.getColumn(iColIndexAssMoney));
	}

	/**
	 * �ж�ĳ���������Ƿ���Ҫ��ʾ�����Ϣ
	 * <li>�������Ƿ�ʵ��������ƽ̨����ȡ�ӿ�
	 * 
	 * @param billType
	 * @return
	 */
	private boolean isShowMoney(String billType) {
		try {
			AggregatedValueObject billvo = PfUtilBaseTools.pfInitVoClass(billType, 0);

			CurrencyInfo cinfo = new CurrencyInfo();
			return PfUtilBaseTools.fetchMoneyInfo(billvo, cinfo, billType);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return false;
	}

	private void adjustMoneyScale(WorkflownoteVO[] notes) {
		// ��ø���(BD303)�����ұ���(BD301),�Ƿ�������(BD302)
		String strLocalCurr = null; // ���ұ���
		try {
			strLocalCurr = SysInitBO_Client.getPkValue(DapCall.getPkcorp(), "BD301");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		String strAssCurr = null;
		try {
			strAssCurr = SysInitBO_Client.getPkValue(DapCall.getPkcorp(), "BD303");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		nc.vo.bd.b20.CurrtypeVO currVo = null; // ��ǰ����
		int digest = 2;
		for (int i = 0; i < notes.length; i++) {
			WorkflownoteVO noteVO = notes[i];

			if (noteVO.getCurrency() != null && noteVO.getCurrency().trim().length() != 0) {
				currVo = PfDataCache.getCurrType(noteVO.getCurrency());
			} else {
				currVo = null;
			}
			if (currVo != null) {
				digest = currVo.getCurrdigit().intValue();
			} else {
				digest = 2;
			}
			if (noteVO.getMoney() != null) {
				noteVO.setMoney(noteVO.getMoney().setScale(-digest, UFDouble.ROUND_HALF_UP));
			}
			if (noteVO.getPremoney() != null) {
				noteVO.setPremoney(noteVO.getPremoney().setScale(-digest, UFDouble.ROUND_HALF_UP));
			}
			if (strLocalCurr != null && strLocalCurr.trim().length() != 0) {
				currVo = PfDataCache.getCurrType(strLocalCurr);
			} else {
				currVo = null;
			}
			if (currVo != null) {
				digest = currVo.getCurrdigit().intValue();
			} else {
				digest = 2;
			}
			if (noteVO.getLocalMoney() != null) {
				noteVO.setLocalMoney(noteVO.getLocalMoney().setScale(-digest, UFDouble.ROUND_HALF_UP));
			}
			if (noteVO.getPrelocalMoney() != null) {
				noteVO
						.setPrelocalMoney(noteVO.getPrelocalMoney().setScale(-digest, UFDouble.ROUND_HALF_UP));
			}
			if (strAssCurr != null && strAssCurr.trim().length() != 0) {
				currVo = PfDataCache.getCurrType(strAssCurr);
			} else {
				currVo = null;
			}
			if (currVo != null) {
				digest = currVo.getCurrdigit().intValue();
			} else {
				digest = 2;
			}
			if (noteVO.getAssMoney() != null) {
				noteVO.setAssMoney(noteVO.getAssMoney().setScale(-digest, UFDouble.ROUND_HALF_UP));
			}
			if (noteVO.getPreassMoney() != null) {
				noteVO.setPreassMoney(noteVO.getPreassMoney().setScale(-digest, UFDouble.ROUND_HALF_UP));
			}
		}
	}

	public int getMainFlowStatus() {
		return m_iMainFlowStatus;
	}

}