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
 * 流程管理Panel
 * 
 * @author leijun 2006-7-18
 * @modifier leijun 2007-3-13 批语列按照列宽自动换行
 * @modifier leijun 2007-9-12 流程图增加XPDL源码切换显示
 * @modifier leijun 2008-7 增加流程图隐藏、显示切换功能
 * @modifier leijun 2008-11 根据单据类型判定是否需要显示金额列
 * @modifier  yangtao 2013-10-23 增加发送人pk
 */
public class FlowAdminPanel extends UIPanel implements ActionListener {
	/** 单据类型 */
	public String m_strBillType = null;

	/** 业务类型 */
	public String m_strBusiType = null;

	/** 单据ID */
	public String m_strBillID = null;

	/** 流程类型 */
	public int m_iWorkflowtype;

	public String m_strBillNo;

	private UILabel ivjLabelTitle = null;

	/** 主流程状态 */
	private int m_iMainFlowStatus = -1;

	/** 审批任务项 */
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
	 * 流程图页签与其显示样式的映射表。
	 * 显示样式：0表示正在显示XPDL;1表示正在显示流程图
	 */
	private HashMap m_hmIndexToStyle = new HashMap();

	/**
	 * 流程图页签与其图形的映射表。
	 */
	private HashMap m_hmIndexToGraph = new HashMap();

	/**
	 * 流程图页签与其XPDL的映射表。
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
	 * 构造该单据的审批流程历史轨迹图
	 * 
	 * @return
	 */
	private UITabbedPane constructGraph() {
		if (m_tabbedPane == null) {
			m_tabbedPane = new UITabbedPane();

			// 找到WFTask所属的流程定义及其父流程定义
			ProcessRouteRes processRoute = null;
			try {
				processRoute = m_pai.getProcessRoute();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000237")/* @res "错误" */, NCLangRes.getInstance().getStrByID(
						"pfworkflow", "UPPpfworkflow-000494")/*查询单据的流程图出现异常：*/
						+ e.getMessage());
			}

			if (processRoute == null || processRoute.getXpdlString() == null)
				// WARN::说明该单据就没有流程实例
				return m_tabbedPane;

			// 获取主流程状态
			m_iMainFlowStatus = processRoute.getProcStatus();

			// 构造一个临时包
			XpdlPackage pkg = new XpdlPackage("unknown", "unknown", null);
			pkg.getExtendedAttributes().put(XPDLNames.MADE_BY, "UFW");

			// 构造流程图页签
			constructGraphTab(processRoute, m_tabbedPane, pkg);

			addTabListener();
		}
		return m_tabbedPane;
	}

	private void addTabListener() {
		constructGraph().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//XXX:流程图和XPDL之间切换
				if (((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) && e.isControlDown()) {
					// 如果是右键点击,则弹出菜单
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
	 * 构造流程图页签
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
			// 前台解析XML串为对象
			wp = UfXPDLParser.getInstance().parseProcess(def_xpdl);
		} catch (XPDLParserException e) {
			Logger.error(e.getMessage(), e);
			return;
		}
		wp.setPackage(pkg);

		// 初始化Graph
		FlowChart auditChart = new ProcessGraph(new UfWGraphModel());
		// 启用工具提示
		ToolTipManager.sharedInstance().registerComponent(auditChart);
		// auditChart.setEnabled(false);
		auditChart.populateByWorkflowProcess(wp, false);
		// auditChart.setBorder(BorderFactory.createEtchedBorder());
		ActivityInstance[] allActInstances = currentRoute.getActivityInstance();
		String[] startedActivityDefIds = new String[allActInstances.length];

		//当前正运行的活动
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
			currentRoute = subRoutes[i]; // 取子流程，继续循环
			constructGraphTab(currentRoute, tabPane, pkg);
		}
	}

	private UILabel getLabBillStatus() {
		if (labBillStatus == null) {
			labBillStatus = new UILabel(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000023")/*流程状态*/);
		}
		return labBillStatus;
	}

	private UIComboBox getComboApproveStatus() {
		if (comboBillStatus == null) {
			comboBillStatus = new UIComboBox();
			//XXX:取值应与IWorkFlowStatus中的常量保持一致
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000273")/* @res "审批通过" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000502")/* @res "审批进行中" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000503")/* @res "提交态" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000274")/* @res "审批不通过" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000504")/* @res "无审批流" */);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000504")/* @res "无审批流" */);

			comboBillStatus.setEditable(false);
			comboBillStatus.setEnabled(false);
		}
		return comboBillStatus;
	}

	private UIComboBox getComboWorkflowStatus() {
		if (comboBillStatus == null) {
			comboBillStatus = new UIComboBox();
			//XXX:取值应与IWorkFlowStatus中的常量保持一致
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000017")/*工作流结束*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000018")/*工作流进行中*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000019")/*工作流已启动*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000274")/* @res "审批不通过" */); //XXX:工作流中没有这个状态
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000020")/*无工作流*/);
			comboBillStatus.addItem(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000020")/*无工作流*/);

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
			// 设置表格行单选模式
			ivjTablePnState.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// 设置列调整模式
			ivjTablePnState.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return ivjTablePnState;
	}

	/**
	 * 获得表格
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
			btnPrint = new UIButton(NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/* @res "打印" */);
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
					"UPPpfworkflow-000138")/*流程图*/);
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

			// 关闭对话框 leijun 2004-12-10
			if (m_container instanceof FlowStateDlg)
				((FlowStateDlg) m_container).dispose();
		} else if (evtSource == getMiXpdl()) {
			int iSelectedIndex = constructGraph().getSelectedIndex();
			//XXX:显示当前流程图的XPDL定义
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
			//XXX:显示当前XPDL定义的流程图
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
	 * 使用打印模板打印
	 * 
	 * @since NC5.0
	 */
	private void onPrintTemplate() {
		if (m_noteVOs == null || m_noteVOs.length < 1)
			return;
		// 打印预览
		PrintEntry pe = new PrintEntry(this, generateDataSource());
		// 设置打印模板ID的查询条件
		pe.setTemplateID(DapCall.getPkcorp(), "101206", DapCall.getOperator(), null);
		// 如果分配了多个打印模板，可选择一个模板
		int iResult = pe.selectTemplate();
		if (iResult == 1) {
			// 开始打印
			// pe.print();
			// 也可以先预览，再在预览界面打印
			pe.preview();
		}
	}

	/**
	 * 根据审批信息表格中的数据填充打印数据源
	 * @return
	 */
	private IDataSource generateDataSource() {
		HashMap<String, String[]> hmDatas = new HashMap<String, String[]>();

		VOTableModel ftm = (VOTableModel) getTableState().getModel();

		int rowCount = ftm.getRowCount();

		// 单据类型名 - 从缓存中获取
		BilltypeVO billtypeVO = PfUIDataCache.getBillTypeInfo(m_strBillType);
		String billtypeName = Pfi18nTools.i18nBilltypeName(billtypeVO.getPk_billtypecode(), billtypeVO
				.getBilltypename());
		String[] colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = billtypeName;
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLTYPE, colValues);

		// 单据号
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = m_noteVOs[0].getBillno();
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BILLNO, colValues);

		// 业务类型名 - 从缓存中获取
		SQLParameter para = new SQLParameter();
		para.addParam(m_strBusiType);
		Object obj = DBCacheFacade.runQuery("select businame from bd_busitype where pk_busitype=?",
				para, new ColumnProcessor(1));
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			colValues[j] = String.valueOf(obj == null ? "" : obj);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_BUSITYPE, colValues);

		// 公司名称 -  
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
		
	
		

		// 币种名称 - 
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

		// 发送时间
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 1);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDDATE, colValues);

		// 发送人名称
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 0);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_SENDMAN, colValues);

		// 处理时间
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 3);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DEALDATE, colValues);

		// 历时
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 4);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_DURATION, colValues);

		// 审批人名称
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 2);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_CHECKMAN, colValues);

		// 审批人PK
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = m_noteVOs[j].getCheckman();
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_PK_CHECKMAN, colValues);
		
		// 发送人pk    yangtao
		colValues = new String[rowCount];
		for (int j=0; j<rowCount; j++){
			Object value = m_noteVOs[j].getSenderman();
			colValues[j] = String.valueOf(value==null? "":value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_PK_SENDCKMAN, colValues);

		// 审批批语
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 7);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_NOTE, colValues);

		// 原币金额
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 9);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_MONEY, colValues);

		// 本币金额
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 10);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_LOCALMONEY, colValues);

		// 辅币金额
		colValues = new String[rowCount];
		for (int j = 0; j < rowCount; j++) {
			Object value = ftm.getValueAt(j, 11);
			colValues[j] = String.valueOf(value == null ? "" : value);
		}
		hmDatas.put(WorkitemPrintDataOfBill.DATAITEM_ASSMONEY, colValues);

		// 审批意见
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
	 * @param iWorkflowtype 流程类型：1=审批流 3=工作流
	 */
	public void initAdminPanel(String strBillType, String strBusitype, String strBillID,
			String strBillNo, int iWorkflowtype) {

		m_strBusiType = strBusitype;
		m_strBillType = strBillType;
		m_strBillID = strBillID;
		m_strBillNo = strBillNo;
		m_iWorkflowtype = iWorkflowtype;

		//XXX:leijun+2009-8 流程监控专用的、合并多个远程调用的监控信息
		try {
			m_pai = NCLocator.getInstance().lookup(IPFWorkflowQry.class).queryFlowInfo4Admin(m_strBillID,
					m_strBillType, m_strBusiType, m_iWorkflowtype);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/* @res "错误" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000505")/*查询该单据的审批历史信息出现异常：*/
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

		// 设置批语列的渲染器
		TableColumn noteColumn = getTableState().getColumn(
				NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000205")/* @res "批语" */);
		noteColumn.setCellRenderer(new FlowQryTableCellRenderer());

	}

	/**
	 * 初始化 工作流处理情况
	 */
	private void initByWorkflow() {
		WorkflowItemTableModel fsTM = new WorkflowItemTableModel(WorkflownoteVO.class);
		getTableState().setModel(fsTM);
		// 调整列宽
		getTableState().setColumnWidth(fsTM.getColumnWidth());
		//查询工作流工作项
		queryWorkflowItems();
		//填充到表模型
		populateWithWorkitems(fsTM);

		// 获取该单据的工作流状态
		setBillWorkflowStatus();

		// 设置标题
		BilltypeVO billType = PfUIDataCache.getBillTypeInfo(m_strBillType);
		String strBilltypeName = Pfi18nTools.i18nBilltypeName(billType.getPk_billtypecode(), billType
				.getBilltypename());
		getLabelTitle()
				.setText(
						strBilltypeName
								+ NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000021")/*工作流状况*/);

	}

	/**
	 * 初始化 审批流处理情况
	 */
	private void initByApproveflow() {
		ApproveItemTableModel fsTM = new ApproveItemTableModel(WorkflownoteVO.class);
		getTableState().setModel(fsTM);
		// 调整列宽
		getTableState().setColumnWidth(fsTM.getColumnWidth());

		//查询审批工作项
		queryApproveItems();
		//填充到表模型
		populateWithWorkitems(fsTM);

		// 获取该单据的审批状态
		setBillApproveStatus();

		// 设置标题
		BilltypeVO billType = PfUIDataCache.getBillTypeInfo(m_strBillType);
		String strBilltypeName = Pfi18nTools.i18nBilltypeName(billType.getPk_billtypecode(), billType
				.getBilltypename());
		getLabelTitle()
				.setText(
						strBilltypeName
								+ NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000191")/* @res "审批状况" */);
	}

	private void queryWorkflowItems() {
		// 查询该单据的历史工作项处理信息
		// 包括已经处理完成的 或尚未处理的工作项
		try {
			m_noteVOs = m_pai.getNoteVOs();

			// 根据系统环境，将金额数据设置精度
			adjustMoneyScale(m_noteVOs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/* @res "错误" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000505")/*查询该单据的审批历史信息出现异常：*/
					+ e.getMessage());
		}
	}

	private void queryApproveItems() {
		// 查询该单据的历史审批信息
		// 包括已经审批完成的 或尚未审批的工作项
		try {
			m_noteVOs = m_pai.getNoteVOs();

			// 根据系统环境，将金额数据设置精度
			adjustMoneyScale(m_noteVOs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000237")/* @res "错误" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000022")/*查询该单据的历史工作流处理信息出现异常：*/
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
	 * 获取该单据的工作流状态
	 */
	private void setBillWorkflowStatus() {
		try {
			// XXX::见<code>IWorkFlowStatus</code>常量
			int iBillStatus = m_pai.getFlowStatus();

			getComboWorkflowStatus().setSelectedIndex(iBillStatus);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取该单据的审批流状态
	 */
	private void setBillApproveStatus() {
		try {
			// XXX::见<code>IWorkFlowStatus</code>常量
			int iBillStatus = m_pai.getFlowStatus();
			getComboApproveStatus().setSelectedIndex(iBillStatus);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 将工作项填充到表模型
	 */
	private void populateWithWorkitems(VOTableModel fsTM) {
		
		// 清除表模型
		fsTM.clearTable();
		

		// 判定是否需要显示金额列 leijun+2008-11
		boolean isShowMoney = isShowMoney(m_strBillType);
		if (isShowMoney) {
			boolean isAss = false;
			try {
				isAss = SysInitBO_Client.getParaBoolean(DapCall.getPkcorp(), "BD302").booleanValue();
				if (!isAss) {
					// 隐藏辅币列
					hideColumn(WorkitemColumnInfo.ASSMONEY.toString());
					hideColumn(WorkitemColumnInfo.ASSMONEY_BEFORE.toString());
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		} else {
			//隐藏金额列
			hideColumn(WorkitemColumnInfo.MONEY.toString());
			hideColumn(WorkitemColumnInfo.MONEY_BEFORE.toString());
			hideColumn(WorkitemColumnInfo.LOCALMONEY.toString());
			hideColumn(WorkitemColumnInfo.LOCALMONEY_BEFORE.toString());
			hideColumn(WorkitemColumnInfo.ASSMONEY.toString());
			hideColumn(WorkitemColumnInfo.ASSMONEY_BEFORE.toString());
		}
		
		if (m_noteVOs == null || m_noteVOs.length < 1)
			return;
		
		//将VO数组添加到表模型
		for (int i = 0; i < m_noteVOs.length; i++) {
			fsTM.addVO(m_noteVOs[i]);
			
		}
	}

	/**
	 * 隐藏表格的某列
	 * @param strIdentifier
	 */
	private void hideColumn(String strIdentifier) {
		TableColumnModel tcm = getTableState().getColumnModel();
		int iColIndexAssMoney = PfUtilUITools.getColumnIndex(strIdentifier, tcm);
		if (iColIndexAssMoney > -1)
			getTableState().removeColumn(tcm.getColumn(iColIndexAssMoney));
	}

	/**
	 * 判定某单据类型是否需要显示金额信息
	 * <li>即单据是否实现了流程平台金额获取接口
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
		// 获得辅币(BD303)、本币币种(BD301),是否主辅币(BD302)
		String strLocalCurr = null; // 本币币种
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

		nc.vo.bd.b20.CurrtypeVO currVo = null; // 当前币种
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