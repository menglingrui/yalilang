package nc.ui.zmpub.formula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.formulaset.function.PostfixMathCommandI;
import nc.vo.zmpub.formula.BusinessFunction;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.SemPubVO;
import nc.vo.zmpub.formula.SystemfunctionVO;

/**
 * 公式对话框
 * @author mlr
 */
@SuppressWarnings("all")
public class FormulaDlg extends UIDialog implements java.awt.event.ActionListener {
	
	private static final long serialVersionUID = 1L;

	private JPanel ivjUIDialogContentPane = null;

	private Panel ivjPanel1 = null;

	private UIButton ivjUIBn0 = null;

	private UIButton ivjUIBn00 = null;

	private UIButton ivjUIBn1 = null;

	private UIButton ivjUIBn2 = null;

	private UIButton ivjUIBn3 = null;

	private UIButton ivjUIBn4 = null;

	private UIButton ivjUIBn5 = null;

	private UIButton ivjUIBn6 = null;

	private UIButton ivjUIBn7 = null;

	private UIButton ivjUIBn8 = null;

	private UIButton ivjUIBn9 = null;

	private UIButton ivjUIBnAdd = null;
	
	//lyf add
	private UIButton ivjUIBnEqe = null;
	private UIButton ivjUIBnSplit = null;
//	private UIButton ivjUIBnTemp = null;

	//lyf add
	
	private UIButton ivjUIBnAnd = null;

	private UIButton ivjUIBnCancel = null;

	private UIButton ivjUIBnDiv = null;

	private UIButton ivjUIBnDot = null;

	private UIButton ivjUIBnElse = null;

	private UIButton ivjUIBnEq = null;

	private UIButton ivjUIBnGuide = null;

	private UIButton ivjUIBnIf = null;

	private UIButton ivjUIBnLeft = null;

	private UIButton ivjUIBnLess = null;

	private UIButton ivjUIBnLessEq = null;

	private UIButton ivjUIBnMore = null;

	private UIButton ivjUIBnMoreEq = null;

	private UIButton ivjUIBnMul = null;

	private UIButton ivjUIBnNotEq = null;

	private UIButton ivjUIBnOk = null;

	private UIButton ivjUIBnOr = null;

	private UIButton ivjUIBnRight = null;

	private UIButton ivjUIBnSub = null;

	private UIButton ivjUIBnTest = null;

	private UIButton ivjUIBnThen = null;

	private UILabel ivjUILabel1 = null;

	private UILabel ivjUILabelContent = null;

	private UILabel ivjUILabelContent1 = null;

	private UILabel ivjUILabelFormu = null;

	private UIList ivjUIListContent = null;

	private UIList ivjUIListFormuRef = null;

	private UIList ivjUIListService = null;

	private UIPanel ivjUIPanel10 = null;

	private UIPanel ivjUIPanel11 = null;

	private UIPanel ivjUIPanel12 = null;

	private UIPanel ivjUIPanel13 = null;

	private UIPanel ivjUIPanel14 = null;

	private UIPanel ivjUIPanel15 = null;

	private UIPanel ivjUIPanel16 = null;

	private UIPanel ivjUIPanel2 = null;

	private UIPanel ivjUIPanel4 = null;

	private UIPanel ivjUIPanel5 = null;

	private UIPanel ivjUIPanel6 = null;

	private UIPanel ivjUIPanel7 = null;

	private UIPanel ivjUIPanel71 = null;

	private UIPanel ivjUIPanel8 = null;

	private UIPanel ivjUIPanel9 = null;

	private UIPanel ivjUIPanelButton = null;

	private UIPanel ivjUIPanelDigit = null;

	private UIPanel ivjUIPanelOprate = null;

	private UIScrollPane ivjUIScrollPane1 = null;

	private UIScrollPane ivjUIScrollPane2 = null;

	private UIScrollPane ivjUIScrollPane3 = null;

	private UIScrollPane ivjUIScrollPane31 = null;

	private UIScrollPane ivjUIScrollPane4 = null;

	private UITextArea ivjUITextAreaFormu = null;

	private String formulaCode = null;// 公式code表达式

	private String formulaDesc = null;// 公式描述

	private boolean allowNullFormula = false;// 公式校验时是否允许公式为空
	
	private ArrayList<FormulaDefineVO> formulaDefvo = null;//运费公式定义
	
	private ArrayList<FormulaDefineVO> pzformuladefvo = null; //品种设置定义VO
	
	// 系统函数定义
	/** mlr 修改 2008/07/05 start * */
//	String[] item={"abs","exp","sqrt","power","log","ln","max","min","round","int","sgn","iif","sin","cos","tg","ctg","asin","acos","atg","getColValue","getColNmV","cvs","cvn","toNumber","toString","charAt","left","right","mid","endsWith","startsWith","equalsIgnoreCase","indexOf","lastIndexOf","length","toLowerCase","toUpperCase","date","year","mon","day","days",
//	 "yearOf","monOf","dayOf"};
	String[] item = { "ABS", "MAX", "MIN", "IIF" };
	
	private ArrayList<SystemfunctionVO> functionvos = null; //系统函数定义

	private JTree tree1 = null;
	
	private JTree tree2 = null;//品种类型
	
	private UITabbedPane tabpanel = null;//页签切换面板 
	
	private java.awt.Container parent;
	
	//<key--描述>
	//<value--具体操作公式定义VO>
	private Hashtable<String, FormulaDefineVO> colKeyValue = new Hashtable<String, FormulaDefineVO>();
	//
	//
//	private Hashtable<String, String> rowKeyValue = new Hashtable<String, String>();

	String m_sLineSep = System.getProperty("line.separator"); // 换行符

	String m_asOperationStr[] = { "=", "!=", "<>", "<", "<=", ">", ">=", ","}; // 操作符
	String m_splitStr = "->";
	// 定义特殊字符串
	String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<",
			">", "&", "&&", "|", "||", " ", "+", "-", "*", "/", "(", ")", ",",m_splitStr,
			//lyf 增加分割符号
			";",
			//lyf增加分割符号
			m_sLineSep };

	// 定义特殊字符
	String m_sSpecialChar = ",-+()*/=<>& " + m_sLineSep;

	private boolean okFlag = false;// 确定按钮按下标志

	// 公式验证加入向上取整函数
	private FormulaParse forParse = null;

	private BusinessFunction[] funVos = null; // 二次开发注册函数

	/**
	 * DiaTest 构造子注解。
	 */
	@SuppressWarnings("deprecation")
	public FormulaDlg() {
		super();
		initialize();
	}

	/**
	 * DiaTest 构造子注解。
	 * 
	 */
	public FormulaDlg(java.awt.Container parent) {
		super(parent);
		this.parent = parent;
		initialize();
	}

	/**
	 * DiaTest 构造子注解。
	 * 
	 */
	public FormulaDlg(java.awt.Frame owner) {
		super(owner);
		initialize();
	}
	/**
	 * @功能:初始化方法<加载初始值>
	 */
	private void initialize() {
		try {
			// 读取所有的系统函数
			setName("DiaTest");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(774, 469);
			// 添加向上取整函数
			forParse = new FormulaParse();
			// 查询二次开发业务函数
			funVos = (BusinessFunction[]) HYPubBO_Client.queryByCondition(BusinessFunction.class, " dr = 0 ");
			setContentPane(getUIDialogContentPane());
			//设置按钮状态
			setBtnEditEnabled();
			// 加入公式容器进行解析
			if (funVos != null && funVos.length > 0) {
				ArrayList list = new ArrayList();
				for (int i = 0; i < funVos.length; i++) {
					PostfixMathCommandI classs = (PostfixMathCommandI) Class
							.forName(funVos[i].getItemClass()).newInstance();
					forParse.addFunction(funVos[i].getVitemname(), classs);
					list.add(funVos[i].getVitemname());
				}
				// 加入到特殊函数里面，为校验方便
				for (int s = 0; s < m_asSpecialStr.length; s++) {
					list.add(m_asSpecialStr[s]);
				}
				m_asSpecialStr = new String[list.size()];
				list.toArray(m_asSpecialStr);
			}
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		initConnections();
	}
	/**
	 * 初始化数据
	 */
	public void initData(ArrayList<SystemfunctionVO> functions,
			ArrayList<FormulaDefineVO> formulaDefvo, ArrayList<FormulaDefineVO> pzformuladefvo, 
			String title,String formuDesc, String formuCode) {
		// 清除内容界面的缓存数据
		getUIListContent().setListData(new Vector());
		// 清除TextArea的缓存数据
		getUITextAreaFormu().setText(null);
		getUIBnGuide().setEnabled(false);
		setTitle(title);
		this.formulaDefvo = formulaDefvo;
		this.pzformuladefvo = pzformuladefvo;
		this.functionvos = functions;
		//
		setFormula(formuDesc, formuCode);
		setItems();
	}
	/**
	 * 
	 */
	public void actionPerformed(java.awt.event.ActionEvent ev) {
		if (((UIButton) ev.getSource()) == getUIBnOk()){// 确定
			onBnOk();
			return;
		} else if (((UIButton) ev.getSource()) == getUIBnCancel()){// 取消
			onBnCancel();
			return;
		} else if (((UIButton) ev.getSource()) == getUIBnTest()){// 验证
			onBnTest();
			return;
		} else if (((UIButton) ev.getSource()) == getUIBnGuide()){// 公式向导
			return;
		} else if(((UIButton) ev.getSource()) == getUIBnAnd()){//并且
			updateTextAree("  &&  ");
			return;
		} else if(((UIButton) ev.getSource()) == getUIBnOr()){//或
			updateTextAree("  ||  ");
			return;
		}
		//lyf 
		 else if(((UIButton) ev.getSource()) == getUIBnSplit()){//临时变量
				updateTextAree("  ; ");
				return;
			}
		 else if(((UIButton) ev.getSource()) == getUIBnEqe()){//临时变量
				updateTextAree(" -> ");
				return;
			}
		//lyf
		String st = ((UIButton) ev.getSource()).getText().trim();
		if (st.equals("0") || st.equals("00") || st.equals("1")
				|| st.equals("2") || st.equals("3") || st.equals("4")
				|| st.equals("5") || st.equals("6") || st.equals("7")
				|| st.equals("8") || st.equals("9") || st.equals(".")) {
			updateTextAree(st);
		} else {
			updateTextAree("  " + st + "  ");
		}
	}

	/**
	 * 返回 Panel1 特性值。
	 * 
	 * @return Panel
	 */
	private Panel getPanel1() {
		if (ivjPanel1 == null) {
			try {
				ivjPanel1 = new Panel();
				ivjPanel1.setName("Panel1");
				ivjPanel1.setLayout(new BorderLayout());
				ivjPanel1.setBounds(0, 0, 774, 419);
				getPanel1().add(getUIPanel13(), "North");
				getPanel1().add(getUIPanel14(), "Center");
				//zpm
				getPanel1().add(getUILabelDescript(), "South");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPanel1;
	}
	//函数解释
	//zpm
	private UILabel labelDescript = null;	
	private UILabel getUILabelDescript() {
		if (labelDescript == null) {
			try {
				labelDescript = new UILabel();
				labelDescript.setName("labelDescript");
				labelDescript.setText("");
				labelDescript.setPreferredSize(new Dimension(720, 12));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return labelDescript;
	}
	/**
	 * 返回 UIBn0 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn0() {
		if (ivjUIBn0 == null) {
			try {
				ivjUIBn0 = new UIButton();
				ivjUIBn0.setName("UIBn0");
				ivjUIBn0.setFont(new Font("dialog", 0, 12));
				ivjUIBn0.setText("0");
				ivjUIBn0.setActionCommand("0");
				ivjUIBn0.addActionListener(this);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIBn0;
	}

	/**
	 * 返回 UIBn00 特性值。
	 * @return UIButton
	 */	
	private UIButton getUIBn00() {
		if (ivjUIBn00 == null) {
			try {
				ivjUIBn00 = new UIButton();
				ivjUIBn00.setName("UIBn00");
				ivjUIBn00.setFont(new Font("dialog", 0, 12));
				ivjUIBn00.setText("00");
				ivjUIBn00.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBn00.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn00;
	}

	/**
	 * 返回 UIBn1 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn1() {
		if (ivjUIBn1 == null) {
			try {
				ivjUIBn1 = new UIButton();
				ivjUIBn1.setName("UIBn1");
				ivjUIBn1.setFont(new Font("dialog", 0, 12));
				ivjUIBn1.setText("1");

				ivjUIBn1.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn1;
	}

	/**
	 * 返回 UIBn2 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn2() {
		if (ivjUIBn2 == null) {
			try {
				ivjUIBn2 = new UIButton();
				ivjUIBn2.setName("UIBn2");
				ivjUIBn2.setFont(new Font("dialog", 0, 12));
				ivjUIBn2.setText("2");

				ivjUIBn2.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn2;
	}

	/**
	 * 返回 UIBn3 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn3() {
		if (ivjUIBn3 == null) {
			try {
				ivjUIBn3 = new UIButton();
				ivjUIBn3.setName("UIBn3");
				ivjUIBn3.setFont(new Font("dialog", 0, 12));
				ivjUIBn3.setText("3");

				ivjUIBn3.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn3;
	}

	/**
	 * 返回 UIBn4 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn4() {
		if (ivjUIBn4 == null) {
			try {
				ivjUIBn4 = new UIButton();
				ivjUIBn4.setName("UIBn4");
				ivjUIBn4.setFont(new Font("dialog", 0, 12));
				ivjUIBn4.setText("4");
				ivjUIBn4.setActionCommand("UIBn2");

				ivjUIBn4.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn4;
	}

	/**
	 * 返回 UIBn5 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn5() {
		if (ivjUIBn5 == null) {
			try {
				ivjUIBn5 = new UIButton();
				ivjUIBn5.setName("UIBn5");
				ivjUIBn5.setFont(new Font("dialog", 0, 12));
				ivjUIBn5.setText("5");
				ivjUIBn5.setActionCommand("UIBn5");

				ivjUIBn5.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn5;
	}

	/**
	 * 返回 UIBn6 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn6() {
		if (ivjUIBn6 == null) {
			try {
				ivjUIBn6 = new UIButton();
				ivjUIBn6.setName("UIBn6");
				ivjUIBn6.setFont(new Font("dialog", 0, 12));
				ivjUIBn6.setText("6");

				ivjUIBn6.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn6;
	}

	/**
	 * 返回 UIBn7 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn7() {
		if (ivjUIBn7 == null) {
			try {
				ivjUIBn7 = new UIButton();
				ivjUIBn7.setName("UIBn7");
				ivjUIBn7.setFont(new Font("dialog", 0, 12));
				ivjUIBn7.setText("7");
				ivjUIBn7.setActionCommand("UIBn1");

				ivjUIBn7.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn7;
	}

	/**
	 * 返回 UIBn8 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn8() {
		if (ivjUIBn8 == null) {
			try {
				ivjUIBn8 = new UIButton();
				ivjUIBn8.setName("UIBn8");
				ivjUIBn8.setFont(new Font("dialog", 0, 12));
				ivjUIBn8.setText("8");
				ivjUIBn8.setActionCommand("UIBn4");

				ivjUIBn8.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn8;
	}

	/**
	 * 返回 UIBn9 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBn9() {
		if (ivjUIBn9 == null) {
			try {
				ivjUIBn9 = new UIButton();
				ivjUIBn9.setName("UIBn9");
				ivjUIBn9.setFont(new Font("dialog", 0, 12));
				ivjUIBn9.setText("9");

				ivjUIBn9.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBn9;
	}
	/**
	 * @author lyf 
	 * 返回 getUIBnEqe  特性值。
	 * @return UIButton
	 */
	private UIButton getUIBnEqe() {
		if (ivjUIBnEqe == null) {
			try {
				ivjUIBnEqe = new UIButton();
				ivjUIBnEqe.setName("ivjUIBnEqe");
				ivjUIBnEqe.setFont(new Font("dialog", 0, 14));
				ivjUIBnEqe.setText(m_splitStr);
				ivjUIBnEqe.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnEqe.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnEqe;
	}
	/**
	 * @author lyf 
	 * 返回 getUIBnSplit  特性值。
	 * @return UIButton
	 */
	private UIButton getUIBnSplit() {
		if (ivjUIBnSplit == null) {
			try {
				ivjUIBnSplit = new UIButton();
				ivjUIBnSplit.setName("ivjUIBnEqe");
				ivjUIBnSplit.setFont(new Font("dialog", 0, 14));
				ivjUIBnSplit.setText(";");
				ivjUIBnSplit.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnSplit.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnSplit;
	}

	/**
	 * 返回 UIBnAdd 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBnAdd() {
		if (ivjUIBnAdd == null) {
			try {
				ivjUIBnAdd = new UIButton();
				ivjUIBnAdd.setName("UIBnAdd");
				ivjUIBnAdd.setFont(new Font("dialog", 0, 14));
				ivjUIBnAdd.setText(" + ");
				ivjUIBnAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnAdd.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnAdd;
	}

	/**
	 * 返回 UIBnAnd 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBnAnd() {
		if (ivjUIBnAnd == null) {
			try {
				ivjUIBnAnd = new UIButton();
				ivjUIBnAnd.setName("UIBnAnd");
				ivjUIBnAnd.setPreferredSize(new Dimension(50, 22));
				ivjUIBnAnd.setFont(new Font("dialog", 0, 12));
				ivjUIBnAnd
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000023")/* @res " 并且 " */);
				ivjUIBnAnd.setBounds(178, 9, 50, 22);
				ivjUIBnAnd.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnAnd.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnAnd;
	}

	/**
	 * 返回 UIBnCancel 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBnCancel() {
		if (ivjUIBnCancel == null) {
			try {
				ivjUIBnCancel = new UIButton();
				ivjUIBnCancel.setName("UIBnCancel");
				ivjUIBnCancel.setFont(new Font("dialog", 0, 12));
				ivjUIBnCancel.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC001-0000008")/* @res "取消" */);
				ivjUIBnCancel.setBounds(515, 9, 50, 22);
				ivjUIBnCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnCancel.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnCancel;
	}

	/**
	 * 返回 UIBnDiv 特性值。
	 * @return UIButton
	 */
	private UIButton getUIBnDiv() {
		if (ivjUIBnDiv == null) {
			try {
				ivjUIBnDiv = new UIButton();
				ivjUIBnDiv.setName("UIBnDiv");
				ivjUIBnDiv.setFont(new Font("dialog", 0, 14));
				ivjUIBnDiv.setText(" / ");
				ivjUIBnDiv.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnDiv.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnDiv;
	}

	/**
	 * 返回 UIBnDot 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnDot() {
		if (ivjUIBnDot == null) {
			try {
				ivjUIBnDot = new UIButton();
				ivjUIBnDot.setName("UIBnDot");
				ivjUIBnDot.setText(".");

				ivjUIBnDot.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnDot;
	}

	/**
	 * 返回 UIBnElse 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnElse() {
		if (ivjUIBnElse == null) {
			try {
				ivjUIBnElse = new UIButton();
				ivjUIBnElse.setName("UIBnElse");
				ivjUIBnElse.setFont(new Font("dialog", 0, 12));
				ivjUIBnElse
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000024")/* @res " 否则 " */);
				ivjUIBnElse.setBounds(64, 9, 50, 22);
				ivjUIBnElse.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnElse.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnElse;
	}

	/**
	 * 返回 UIBnEq 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnEq() {
		if (ivjUIBnEq == null) {
			try {
				ivjUIBnEq = new UIButton();
				ivjUIBnEq.setName("UIBnEq");
				ivjUIBnEq.setFont(new Font("dialog", 0, 12));
				ivjUIBnEq.setText(" = ");
				ivjUIBnEq.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnEq.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnEq;
	}

	/**
	 * 返回 UIBnGuide 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnGuide() {
		if (ivjUIBnGuide == null) {
			try {
				ivjUIBnGuide = new UIButton();
				ivjUIBnGuide.setName("UIBnGuide");
				ivjUIBnGuide.setFont(new Font("dialog", 0, 12));
				ivjUIBnGuide
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000025")/* @res "公式向导" */);
				ivjUIBnGuide.setBounds(292, 9, 76, 22);
				ivjUIBnGuide.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnGuide.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnGuide;
	}

	/**
	 * 返回 UIBnIf 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnIf() {
		if (ivjUIBnIf == null) {
			try {
				ivjUIBnIf = new UIButton();
				ivjUIBnIf.setName("UIBnIf");
				ivjUIBnIf.setFont(new Font("dialog", 0, 12));
				ivjUIBnIf
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000026")/* @res " 如果 " */);
				ivjUIBnIf.setBounds(7, 9, 50, 22);
				ivjUIBnIf.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnIf.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnIf;
	}

	/**
	 * 返回 UIBnLeft 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnLeft() {
		if (ivjUIBnLeft == null) {
			try {
				ivjUIBnLeft = new UIButton();
				ivjUIBnLeft.setName("UIBnLeft");
				ivjUIBnLeft.setFont(new Font("dialog", 0, 12));
				ivjUIBnLeft.setText(" ( ");

				ivjUIBnLeft.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnLeft;
	}

	/**
	 * 返回 UIBnLess 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnLess() {
		if (ivjUIBnLess == null) {
			try {
				ivjUIBnLess = new UIButton();
				ivjUIBnLess.setName("UIBnLess");
				ivjUIBnLess.setFont(new Font("dialog", 0, 14));
				ivjUIBnLess.setText(" < ");
				ivjUIBnLess.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnLess.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnLess;
	}

	/**
	 * 返回 UIBnLessEq 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnLessEq() {
		if (ivjUIBnLessEq == null) {
			try {
				ivjUIBnLessEq = new UIButton();
				ivjUIBnLessEq.setName("UIBnLessEq");
				ivjUIBnLessEq.setFont(new Font("dialog", 0, 14));
				ivjUIBnLessEq.setText(" <= ");
				ivjUIBnLessEq.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnLessEq.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnLessEq;
	}

	/**
	 * 返回 UIBnMore 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnMore() {
		if (ivjUIBnMore == null) {
			try {
				ivjUIBnMore = new UIButton();
				ivjUIBnMore.setName("UIBnMore");
				ivjUIBnMore.setFont(new Font("dialog", 0, 14));
				ivjUIBnMore.setText(" > ");
				ivjUIBnMore.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnMore.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnMore;
	}

	/**
	 * 返回 UIBnMoreEq 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnMoreEq() {
		if (ivjUIBnMoreEq == null) {
			try {
				ivjUIBnMoreEq = new UIButton();
				ivjUIBnMoreEq.setName("UIBnMoreEq");
				ivjUIBnMoreEq.setFont(new Font("dialog", 0, 14));
				ivjUIBnMoreEq.setText(" >= ");
				ivjUIBnMoreEq.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnMoreEq.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnMoreEq;
	}

	/**
	 * 返回 UIBnMul 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnMul() {
		if (ivjUIBnMul == null) {
			try {
				ivjUIBnMul = new UIButton();
				ivjUIBnMul.setName("UIBnMul");
				ivjUIBnMul.setFont(new Font("dialog", 0, 24));
				ivjUIBnMul.setText(" * ");
				ivjUIBnMul.setMargin(new java.awt.Insets(15, 0, 2, 0));
				ivjUIBnMul.addActionListener(this);
			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnMul;
	}

	/**
	 * 返回 UIBnNotEq 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnNotEq() {
		if (ivjUIBnNotEq == null) {
			try {
				ivjUIBnNotEq = new UIButton();
				ivjUIBnNotEq.setName("UIBnNotEq");
				ivjUIBnNotEq.setFont(new Font("dialog", 0, 14));
				ivjUIBnNotEq.setText(" <> ");
				ivjUIBnNotEq.setMargin(new java.awt.Insets(2, 0, 2, 0));
				ivjUIBnNotEq.addActionListener(this);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIBnNotEq;
	}

	/**
	 * 返回 UIBnOk 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnOk() {
		if (ivjUIBnOk == null) {
			try {
				ivjUIBnOk = new UIButton();
				ivjUIBnOk.setName("UIBnOk");
				ivjUIBnOk.setFont(new Font("dialog", 0, 12));
				ivjUIBnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC001-0000044")/* @res "确定" */);
				ivjUIBnOk.setBounds(458, 9, 50, 22);
				ivjUIBnOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
				ivjUIBnOk.addActionListener(this);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIBnOk;
	}

	/**
	 * 返回 UIBnOr 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnOr() {
		if (ivjUIBnOr == null) {
			try {
				ivjUIBnOr = new UIButton();
				ivjUIBnOr.setName("UIBnOr");
				ivjUIBnOr.setPreferredSize(new Dimension(50, 22));
				ivjUIBnOr.setFont(new Font("dialog", 0, 12));
				ivjUIBnOr
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000027")/* @res " 或者 " */);
				ivjUIBnOr.setBounds(235, 9, 50, 22);
				ivjUIBnOr.setMargin(new java.awt.Insets(2, 0, 2, 0));
				ivjUIBnOr.addActionListener(this);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIBnOr;
	}

	/**
	 * 返回 UIBnRight 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnRight() {
		if (ivjUIBnRight == null) {
			try {
				ivjUIBnRight = new UIButton();
				ivjUIBnRight.setName("UIBnRight");
				ivjUIBnRight.setFont(new Font("dialog", 0, 12));
				ivjUIBnRight.setText(" ) ");

				ivjUIBnRight.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnRight;
	}

	/**
	 * 返回 UIBnSub 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnSub() {
		if (ivjUIBnSub == null) {
			try {
				ivjUIBnSub = new UIButton();
				ivjUIBnSub.setName("UIBnSub");
				ivjUIBnSub.setFont(new Font("dialog", 0, 18));
				ivjUIBnSub.setText(" - ");
				ivjUIBnSub.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnSub.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnSub;
	}

	/**
	 * 返回 UIBnTest 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnTest() {
		if (ivjUIBnTest == null) {
			try {
				ivjUIBnTest = new UIButton();
				ivjUIBnTest.setName("UIBnTest");
				ivjUIBnTest.setFont(new Font("dialog", 0, 12));
				ivjUIBnTest
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000028")/* @res "公式验证" */);
				ivjUIBnTest.setBounds(375, 9, 76, 22);
				ivjUIBnTest.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnTest.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnTest;
	}

	/**
	 * 返回 UIBnThen 特性值。
	 * 
	 * @return UIButton
	 */
	private UIButton getUIBnThen() {
		if (ivjUIBnThen == null) {
			try {
				ivjUIBnThen = new UIButton();
				ivjUIBnThen.setName("UIBnThen");
				ivjUIBnThen
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000029")/* @res " 则 " */);
				ivjUIBnThen.setBounds(121, 9, 50, 22);
				ivjUIBnThen.setMargin(new java.awt.Insets(2, 0, 2, 0));

				ivjUIBnThen.addActionListener(this);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIBnThen;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(null);				
				getUIDialogContentPane()
						.add(getPanel1(), getPanel1().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 返回 UILabel1 特性值。
	 * 
	 * @return UILabel
	 */
	private UILabel getUILabel1() {
		if (ivjUILabel1 == null) {
			try {
				ivjUILabel1 = new UILabel();
				ivjUILabel1.setName("UILabel1");
				ivjUILabel1
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000030")/* @res "项 目" */);
				ivjUILabel1.setBounds(80, 3, 44, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabel1;
	}

	/**
	 * 返回 UILabelContent 特性值。
	 * 
	 * @return UILabel
	 */
	private UILabel getUILabelContent() {
		if (ivjUILabelContent == null) {
			try {
				ivjUILabelContent = new UILabel();
				ivjUILabelContent.setName("UILabelContent");
				ivjUILabelContent
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000031")/* @res "内 容" */);
				ivjUILabelContent.setBounds(80, 3, 44, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelContent;
	}

	/**
	 * 返回 UILabelContent1 特性值。
	 * 
	 * @return UILabel
	 */
	private UILabel getUILabelContent1() {
		if (ivjUILabelContent1 == null) {
			try {
				ivjUILabelContent1 = new UILabel();
				ivjUILabelContent1.setName("UILabelContent1");
				ivjUILabelContent1
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000032")/* @res "业务函数" */);
				ivjUILabelContent1.setBounds(68, 3, 69, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelContent1;
	}

	/**
	 * 返回 UILabelFormu 特性值。
	 * 
	 * @return UILabel
	 */
	private UILabel getUILabelFormu() {
		if (ivjUILabelFormu == null) {
			try {
				ivjUILabelFormu = new UILabel();
				ivjUILabelFormu.setName("UILabelFormu");
				ivjUILabelFormu
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"formulaparse", "UPPformulaparse-000033")/* @res "系统函数" */);
				ivjUILabelFormu.setBounds(74, 3, 63, 22);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUILabelFormu;
	}

	/**
	 * 公式内容框
	 * 
	 * @return UIList
	 */
	private UIList getUIListContent() {
		if (ivjUIListContent == null) {
			try {
				ivjUIListContent = new UIList();
				ivjUIListContent.setName("UIListContent");
				ivjUIListContent
						.setBackground(new java.awt.Color(255, 255, 255));
				ivjUIListContent.setBounds(0, 0, 160, 120);
				ivjUIListContent
						.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIListContent;
	}

	/**
	 * 系统函数框
	 * 
	 * @return UIList
	 */
	private UIList getUIListFormuRef() {
		if (ivjUIListFormuRef == null) {
			try {
				ivjUIListFormuRef = new UIList();
				ivjUIListFormuRef.setName("UIListFormuRef");
				ivjUIListFormuRef.setBackground(new java.awt.Color(255, 255,
						255));
				ivjUIListFormuRef.setBounds(0, 0, 157, 177);
				ivjUIListFormuRef
						.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				//zpm换个地方
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIListFormuRef;
	}

	private JTree getUItree1() {
		if (tree1 == null) {
			tree1 = new JTree(new Vector());
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(new ImageIcon("1.gif"));
			renderer.setClosedIcon(new ImageIcon("2.gif"));
			renderer.setOpenIcon(new ImageIcon("3.gif"));
			renderer.setBorderSelectionColor(Color.blue);
			tree1.setCellRenderer(renderer);
			tree1.setRootVisible(false);
		}
		return tree1;
	}
	
	private JTree getUItree2() {
		if (tree2 == null) {
			tree2 = new JTree(new Vector());
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(new ImageIcon("1.gif"));
			renderer.setClosedIcon(new ImageIcon("2.gif"));
			renderer.setOpenIcon(new ImageIcon("3.gif"));
			renderer.setBorderSelectionColor(Color.blue);
			tree2.setCellRenderer(renderer);
			tree2.setRootVisible(false);
		}
		return tree2;
	}
	//项目切换
	private UITabbedPane  getUITabbedPanel(){
		if(tabpanel == null){
			tabpanel = new nc.ui.pub.beans.UITabbedPane(JTabbedPane.BOTTOM);
			tabpanel.setName("UITabPane");
			tabpanel.insertTab("项目", null,getUItree1(), null, 0);
			tabpanel.insertTab("品种", null,getUItree2(), null, 1);
		}
		return tabpanel;
	}
		

	/**
	 * 业务函数框
	 * 
	 * @return UIList
	 */
	private UIList getUIListService() {
		if (ivjUIListService == null) {
			try {
				ivjUIListService = new UIList();
				ivjUIListService.setName("UIListService");
				ivjUIListService
						.setBackground(new java.awt.Color(255, 255, 255));
				ivjUIListService.setBounds(0, 0, 160, 120);
				ivjUIListService
						.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				// 业务函数增加“向上取整函数”.这里使用汉字
				if (funVos != null && funVos.length > 0) {
					ivjUIListService.setListData(funVos);
				}
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIListService;
	}

	/**
	 * 返回 UIPanel10 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel10() {
		if (ivjUIPanel10 == null) {
			try {
				ivjUIPanel10 = new UIPanel();
				ivjUIPanel10.setName("UIPanel10");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel10;
	}

	/**
	 * 返回 UIPanel11 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel11() {
		if (ivjUIPanel11 == null) {
			try {
				ivjUIPanel11 = new UIPanel();
				ivjUIPanel11.setName("UIPanel11");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel11;
	}

	/**
	 * 返回 UIPanel12 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel12() {
		if (ivjUIPanel12 == null) {
			try {
				ivjUIPanel12 = new UIPanel();
				ivjUIPanel12.setName("UIPanel12");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel12;
	}

	/**
	 * 返回 UIPanel13 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel13() {
		if (ivjUIPanel13 == null) {
			try {
				ivjUIPanel13 = new UIPanel();
				ivjUIPanel13.setName("UIPanel13");
				ivjUIPanel13.setPreferredSize(new Dimension(10, 250));
				ivjUIPanel13.setLayout(new BorderLayout());
				getUIPanel13().add(getUIPanel15(), "East");
				getUIPanel13().add(getUIPanel16(), "Center");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel13;
	}

	/**
	 * 返回 UIPanel14 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel14() {
		if (ivjUIPanel14 == null) {
			try {
				ivjUIPanel14 = new UIPanel();
				ivjUIPanel14.setName("UIPanel14");
				ivjUIPanel14.setLayout(getUIPanel14GridLayout());
				getUIPanel14().add(getUIPanel6(), getUIPanel6().getName());
				getUIPanel14().add(getUIPanel71(), getUIPanel71().getName());
				getUIPanel14().add(getUIPanel8(), getUIPanel8().getName());
				getUIPanel14().add(getUIPanel7(), getUIPanel7().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIPanel14;
	}

	/**
	 * 返回 UIPanel14GridLayout 特性值。
	 * 
	 * @return GridLayout
	 */
	private GridLayout getUIPanel14GridLayout() {
		GridLayout ivjUIPanel14GridLayout = null;
		try {
			/* 创建部件 */
			ivjUIPanel14GridLayout = new GridLayout();
			ivjUIPanel14GridLayout.setColumns(4);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjUIPanel14GridLayout;
	}

	/**
	 * 返回 UIPanel15 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel15() {
		if (ivjUIPanel15 == null) {
			try {
				ivjUIPanel15 = new UIPanel();
				ivjUIPanel15.setName("UIPanel15");
				ivjUIPanel15.setPreferredSize(new Dimension(200, 0));
				ivjUIPanel15.setLayout(new BorderLayout());
				getUIPanel15().add(getUIPanel2(), "Center");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel15;
	}

	/**
	 * 返回 UIPanel16 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel16() {
		if (ivjUIPanel16 == null) {
			try {
				ivjUIPanel16 = new UIPanel();
				ivjUIPanel16.setName("UIPanel16");
				ivjUIPanel16.setLayout(new BorderLayout());
				getUIPanel16().add(getUIPanel4(), "Center");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel16;
	}

	/**
	 * 返回 UIPanel2 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel2() {
		if (ivjUIPanel2 == null) {
			try {
				ivjUIPanel2 = new UIPanel();
				ivjUIPanel2.setName("UIPanel2");
				ivjUIPanel2.setPreferredSize(new Dimension(200, 0));
				ivjUIPanel2.setLayout(null);
				getUIPanel2().add(getUIPanelDigit(),
						getUIPanelDigit().getName());
				getUIPanel2().add(getUIPanelOprate(),
						getUIPanelOprate().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel2;
	}

	/**
	 * 返回 UIPanel4 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel4() {
		if (ivjUIPanel4 == null) {
			try {
				ivjUIPanel4 = new UIPanel();
				ivjUIPanel4.setName("UIPanel4");
				ivjUIPanel4.setPreferredSize(new Dimension(0, 250));
				ivjUIPanel4.setBorder(new EtchedBorder());
				ivjUIPanel4.setLayout(new BorderLayout());
				getUIPanel4().add(getUIPanelButton(), "South");
				getUIPanel4().add(getUIPanel5(), "Center");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel4;
	}

	/**
	 * 返回 UIPanel5 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel5() {
		if (ivjUIPanel5 == null) {
			try {
				ivjUIPanel5 = new UIPanel();
				ivjUIPanel5.setName("UIPanel5");
				ivjUIPanel5.setBorder(new EtchedBorder());
				ivjUIPanel5.setLayout(new BorderLayout());
				getUIPanel5().add(getUIScrollPane4(), "Center");
				getUIPanel5().add(getUIPanel9(), "North");
				getUIPanel5().add(getUIPanel10(), "South");
				getUIPanel5().add(getUIPanel11(), "East");
				getUIPanel5().add(getUIPanel12(), "West");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel5;
	}

	/**
	 * 返回 UIPanel6 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel6() {
		if (ivjUIPanel6 == null) {
			try {
				ivjUIPanel6 = new UIPanel();
				ivjUIPanel6.setName("UIPanel6");
				ivjUIPanel6.setPreferredSize(new Dimension(194, 10));
				ivjUIPanel6.setBorder(new EtchedBorder());
				ivjUIPanel6.setLayout(null);
				getUIPanel6().add(getUILabelFormu(),
						getUILabelFormu().getName());
				getUIPanel6().add(getUIScrollPane1(),
						getUIScrollPane1().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel6;
	}

	/**
	 * 返回 UIPanel7 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel7() {
		if (ivjUIPanel7 == null) {
			try {
				ivjUIPanel7 = new UIPanel();
				ivjUIPanel7.setName("UIPanel7");
				ivjUIPanel7.setPreferredSize(new Dimension(194, 10));
				ivjUIPanel7.setBorder(new EtchedBorder());
				ivjUIPanel7.setLayout(null);
				getUIPanel7().add(getUILabelContent(),
						getUILabelContent().getName());
				getUIPanel7().add(getUIScrollPane3(),
						getUIScrollPane3().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel7;
	}

	/**
	 * 返回 UIPanel71 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel71() {
		if (ivjUIPanel71 == null) {
			try {
				ivjUIPanel71 = new UIPanel();
				ivjUIPanel71.setName("UIPanel71");
				ivjUIPanel71.setPreferredSize(new Dimension(194, 10));
				ivjUIPanel71.setBorder(new EtchedBorder());
				ivjUIPanel71.setLayout(null);
				getUIPanel71().add(getUILabelContent1(),
						getUILabelContent1().getName());
				getUIPanel71().add(getUIScrollPane31(),
						getUIScrollPane31().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel71;
	}

	/**
	 * 返回 UIPanel8 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel8() {
		if (ivjUIPanel8 == null) {
			try {
				ivjUIPanel8 = new UIPanel();
				ivjUIPanel8.setName("UIPanel8");
				ivjUIPanel8.setBorder(new EtchedBorder());
				ivjUIPanel8.setLayout(null);
				getUIPanel8().add(getUILabel1(), getUILabel1().getName());
				getUIPanel8().add(getUIScrollPane2(),
						getUIScrollPane2().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel8;
	}

	/**
	 * 返回 UIPanel9 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanel9() {
		if (ivjUIPanel9 == null) {
			try {
				ivjUIPanel9 = new UIPanel();
				ivjUIPanel9.setName("UIPanel9");

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanel9;
	}

	/**
	 * 返回 UIPanelButton 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanelButton() {
		if (ivjUIPanelButton == null) {
			try {
				ivjUIPanelButton = new UIPanel();
				ivjUIPanelButton.setName("UIPanelButton");
				ivjUIPanelButton
						.setAlignmentY(java.awt.Component.BOTTOM_ALIGNMENT);
				ivjUIPanelButton.setLayout(null);
				ivjUIPanelButton
						.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
				ivjUIPanelButton.setPreferredSize(new Dimension(10, 40));
				ivjUIPanelButton
						.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT);
				getUIPanelButton().add(getUIBnIf(), getUIBnIf().getName());
				getUIPanelButton().add(getUIBnElse(), getUIBnElse().getName());
				getUIPanelButton().add(getUIBnThen(), getUIBnThen().getName());
				getUIPanelButton().add(getUIBnTest(), getUIBnTest().getName());
				getUIPanelButton().add(getUIBnAnd(), getUIBnAnd().getName());
				getUIPanelButton().add(getUIBnOr(), getUIBnOr().getName());
				getUIPanelButton()
						.add(getUIBnGuide(), getUIBnGuide().getName());
				getUIPanelButton().add(getUIBnOk(), getUIBnOk().getName());
				getUIPanelButton().add(getUIBnCancel(),
						getUIBnCancel().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanelButton;
	}

	/**
	 * 返回 UIPanelDigit 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanelDigit() {
		if (ivjUIPanelDigit == null) {
			try {
				ivjUIPanelDigit = new UIPanel();
				ivjUIPanelDigit.setName("UIPanelDigit");
				ivjUIPanelDigit.setPreferredSize(new Dimension(10, 200));
				ivjUIPanelDigit.setBorder(new EtchedBorder());
				ivjUIPanelDigit.setLayout(getUIPanelDigitGridLayout());
				ivjUIPanelDigit.setBounds(6, 19, 186, 75);
				getUIPanelDigit().add(getUIBn7(), getUIBn7().getName());
				getUIPanelDigit().add(getUIBn8(), getUIBn8().getName());
				getUIPanelDigit().add(getUIBn9(), getUIBn9().getName());
				getUIPanelDigit().add(getUIBn0(), getUIBn0().getName());
				getUIPanelDigit().add(getUIBn4(), getUIBn4().getName());
				getUIPanelDigit().add(getUIBn5(), getUIBn5().getName());
				getUIPanelDigit().add(getUIBn6(), getUIBn6().getName());
				getUIPanelDigit().add(getUIBnDot(), getUIBnDot().getName());
				getUIPanelDigit().add(getUIBn1(), getUIBn1().getName());
				getUIPanelDigit().add(getUIBn2(), getUIBn2().getName());
				getUIPanelDigit().add(getUIBn3(), getUIBn3().getName());
				getUIPanelDigit().add(getUIBn00(), getUIBn00().getName());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanelDigit;
	}

	/**
	 * 返回 UIPanelDigitGridLayout 特性值。
	 * 
	 * @return GridLayout
	 */
	private GridLayout getUIPanelDigitGridLayout() {
		GridLayout ivjUIPanelDigitGridLayout = null;
		try {
			/* 创建部件 */
			ivjUIPanelDigitGridLayout = new GridLayout(3, 4);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjUIPanelDigitGridLayout;
	}

	/**
	 * 返回 UIPanelOprate 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getUIPanelOprate() {
		if (ivjUIPanelOprate == null) {
			try {
				ivjUIPanelOprate = new UIPanel();
				ivjUIPanelOprate.setName("UIPanelOprate");
				ivjUIPanelOprate.setPreferredSize(new Dimension(10, 200));
				ivjUIPanelOprate.setBorder(new EtchedBorder());
				ivjUIPanelOprate.setLayout(getUIPanelOprateGridLayout());
				ivjUIPanelOprate.setBounds(6, 123, 186, 75);
				getUIPanelOprate().add(getUIBnAdd(), getUIBnAdd().getName());
				getUIPanelOprate().add(getUIBnSub(), getUIBnSub().getName());
				getUIPanelOprate().add(getUIBnMul(), getUIBnMul().getName());
				getUIPanelOprate().add(getUIBnDiv(), getUIBnDiv().getName());
				getUIPanelOprate().add(getUIBnEq(), getUIBnEq().getName());
				getUIPanelOprate()
						.add(getUIBnNotEq(), getUIBnNotEq().getName());
				getUIPanelOprate().add(getUIBnLeft(), getUIBnLeft().getName());
				getUIPanelOprate()
						.add(getUIBnRight(), getUIBnRight().getName());
				getUIPanelOprate().add(getUIBnLess(), getUIBnLess().getName());
				getUIPanelOprate().add(getUIBnMore(), getUIBnMore().getName());
				getUIPanelOprate().add(getUIBnLessEq(),
						getUIBnLessEq().getName());
				getUIPanelOprate().add(getUIBnMoreEq(),
						getUIBnMoreEq().getName());
				//lyf
				getUIPanelOprate().add(getUIBnEqe(), getUIBnEqe().getName());
				getUIPanelOprate().add(getUIBnSplit(), getUIBnSplit().getName());

				//lyf

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIPanelOprate;
	}

	/**
	 * 返回 UIPanelOprateGridLayout 特性值。
	 * 
	 * @return GridLayout
	 */
	private GridLayout getUIPanelOprateGridLayout() {
		GridLayout ivjUIPanelOprateGridLayout = null;
		try {
			/* 创建部件 */
			ivjUIPanelOprateGridLayout = new GridLayout(3, 4);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjUIPanelOprateGridLayout;
	}

	/**
	 * 返回 UIScrollPane1 特性值。
	 * 
	 * @return UIScrollPane
	 */
	private UIScrollPane getUIScrollPane1() {
		if (ivjUIScrollPane1 == null) {
			try {
				ivjUIScrollPane1 = new UIScrollPane();
				ivjUIScrollPane1.setName("UIScrollPane1");
				ivjUIScrollPane1.setBounds(17, 22, 160, 134);
				getUIScrollPane1().setViewportView(getUIListFormuRef());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane1;
	}

	/**
	 * 返回 UIScrollPane2 特性值。
	 * 
	 * @return UIScrollPane
	 */
	private UIScrollPane getUIScrollPane2() {
		if (ivjUIScrollPane2 == null) {
			try {
				ivjUIScrollPane2 = new UIScrollPane();
				ivjUIScrollPane2.setName("UIScrollPane2");
				ivjUIScrollPane2.setBounds(17, 22, 160, 134);
				// getUIScrollPane2().setViewportView(getUIListItem());
				getUIScrollPane2().setViewportView(getUITabbedPanel());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane2;
	}

	/**
	 * 返回 UIScrollPane3 特性值。
	 * 
	 * @return UIScrollPane
	 */
	private UIScrollPane getUIScrollPane3() {
		if (ivjUIScrollPane3 == null) {
			try {
				ivjUIScrollPane3 = new UIScrollPane();
				ivjUIScrollPane3.setName("UIScrollPane3");
				ivjUIScrollPane3.setBounds(17, 22, 160, 134);
				getUIScrollPane3().setViewportView(getUIListContent());

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane3;
	}

	/**
	 * 返回 UIScrollPane31 特性值。
	 * 
	 * @return UIScrollPane
	 */
	private UIScrollPane getUIScrollPane31() {
		if (ivjUIScrollPane31 == null) {
			try {
				ivjUIScrollPane31 = new UIScrollPane();
				ivjUIScrollPane31.setName("UIScrollPane31");
				ivjUIScrollPane31.setBounds(17, 22, 160, 134);
				getUIScrollPane31().setViewportView(getUIListService());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane31;
	}

	/**
	 * 返回 UIScrollPane4 特性值。
	 * @return UIScrollPane
	 */
	private UIScrollPane getUIScrollPane4() {
		if (ivjUIScrollPane4 == null) {
			try {
				ivjUIScrollPane4 = new UIScrollPane();
				ivjUIScrollPane4.setName("UIScrollPane4");
				ivjUIScrollPane4.setBorder(new EtchedBorder());
				getUIScrollPane4().setViewportView(getUITextAreaFormu());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane4;
	}

	/**
	 * 返回 UITextAreaFormu 特性值。
	 */
	private UITextArea getUITextAreaFormu() {
		if (ivjUITextAreaFormu == null) {
			try {
				ivjUITextAreaFormu = new UITextArea();
				ivjUITextAreaFormu.setName("UITextAreaFormu");
				ivjUITextAreaFormu.setLineWrap(true);
				ivjUITextAreaFormu.setBounds(0, 0, 546, 182);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUITextAreaFormu;
	}

	/**
	 * 每当部件抛出异常时被调用
	 */
	public void handleException(Throwable exception) {
		exception.printStackTrace();
		MessageDialog.showErrorDlg(parent, "错误", exception.getMessage());
	}

	/**
	 * 初始化类。
	 */
	private void initConnections() {
		java.awt.event.MouseAdapter aMA = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent ev) {
				if ((UIList) ev.getSource() == getUIListFormuRef()) {//系统函数框
					onListFormuRef(ev);
					return;
				}else if ((UIList) ev.getSource() == getUIListContent()) {//公式内容框
					onListContent(ev);
					return;
				}else if ((UIList) ev.getSource() == getUIListService()) {//业务函数框
					onListService(ev);
					return;
				}
			}
		};
		//系统函数框
		getUIListFormuRef().addMouseListener(aMA);
		//内容框
		getUIListContent().addMouseListener(aMA);
		//业务函数框
		getUIListService().addMouseListener(aMA);

		java.awt.event.KeyListener aKL = new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent ev) {
			}
			public void keyTyped(java.awt.event.KeyEvent ev) {
			}
			public void keyReleased(java.awt.event.KeyEvent ev) {
				if (ev.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {//回车键
					if ((UIList) ev.getSource() == getUIListFormuRef()) {
						onListFormuRef(ev);
						return;
					} else if ((UIList) ev.getSource() == getUIListContent()) {
						onListContent(ev);
						return;
					} else if ((UIList) ev.getSource() == getUIListService()) {
						onListService(ev);
						return;
					}
				}
			}
		};
		//系统函数框
		getUIListFormuRef().addKeyListener(aKL);
		//内容框
		getUIListContent().addKeyListener(aKL);
		//加入业务函数的监听
		getUIListService().addKeyListener(aKL);
		//项目框
		getUItree1().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getUItree1()
						.getLastSelectedPathComponent();
				onListItem(node);

			}
		});
		
		//品种名称
		getUItree2().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getUItree2()
						.getLastSelectedPathComponent();
				onListItem(node);
			}
		});
	}
	/**
	 * 
	 */
	public boolean isAllowNullFormula() {
		return allowNullFormula;
	}

	/**
	 * 
	 */
	public boolean isIn(String st, String[] stArray) {
		if (st != null && stArray != null) {
			for (int i = 0; i < stArray.length; i++) {
				if (st.equals(stArray[i]))
					return true;
			}
		}
		return false;
	}

	//zpm
	public String[] getStringArray(){
		String[] arrays = null;
		if(functionvos != null && functionvos.size()>0){
			arrays = new String[functionvos.size()];
			for(int i = 0 ;i < functionvos.size();i++){
				arrays[i] = functionvos.get(i).getCode();
			}
		}
		return arrays;
	}
	
	/**
	 * zpm
	 */
	public boolean isSpecialChar(String st) {
		if (isIn(st, getStringArray()))
			return true;

		if (isIn(st, m_asOperationStr))
			return true;

		if (isIn(st, m_asSpecialStr))
			return true;

		return false;
	}

	/**
	 * 
	 */
	public void onBnCancel() {
		closeCancel();
	}

	/**
	 * 
	 */
	public void onBnOk() {
		okFlag = true;
		if (onBnTest()) {
			closeOK();
		}
		okFlag = false;
	}

	public ArrayList<String> getTempFieds(String[] formuPart){
		 ArrayList<String> temp = new ArrayList<String>();
		 if(formuPart != null && formuPart.length >0){
			 for(int i=0;i<formuPart.length;i++){
				 String formu = SemPubVO.getString_TrimZeroLenAsNull(formuPart[i]);
				 formu  = formu.replace(" ", "");
				 if(formu != null && "->".equals(formu) && i>0){
					 temp.add(formuPart[i-1]);
				 }
			 }
		 }
		 return temp;
	}
	/**
	 * @功能:方法->公式验证
	 */
	public boolean onBnTest() {
		try {
			String formulaDescNew = getUITextAreaFormu().getText();
			if (formulaDescNew == null || formulaDescNew.trim().length() <= 0) {
				setFormulaName("");
				setFormulaCode("");
				return true;
			}
//			// 清空单据界面价格项页签是否被引用的行标记
//			getQuotedRowSet().clear();
			//
			String[] formuPart = parseString(formulaDescNew);
			//lyf
			 ArrayList<String> temp = getTempFieds(formuPart);
			 //lyf
			// 转换成编码,并且验证
			String formuCode = transToCode(formuPart,temp);

			if (formuCode == null || formuCode.trim().length() <= 0) {
				MessageDialog
						.showErrorDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000034")/* @res "公式设置错误" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000036")/* @res "公式设置错误！" */);
				return false;
			}
			if (testCode(formuCode)) {
				setFormulaName(formulaDescNew);
				setFormulaCode(formuCode);
			} else {
				MessageDialog
						.showErrorDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000034")/* @res "公式设置错误" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000036")/* @res "公式设置错误！" */);
				return false;
			}
			// }
			if (!okFlag) {
				MessageDialog
						.showHintDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000037")/* @res "公式设置正确" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000038")/* @res "公式设置正确！" */);

			}
			return true;
		} catch (Exception ex) {
			MessageDialog.showErrorDlg(parent, "错误", ex.getMessage());
			return false;
		}
	}
	/**
	 * 项目编辑
	 */
	private void onListItem(DefaultMutableTreeNode node) {
		if (node != null) {
			DefaultMutableTreeNode p = (DefaultMutableTreeNode) node.getParent();
			if (p != null && p.isRoot()) {//根节点
				getUIListContent().setListData(new Vector());
			} else {
				FormulaDefineVO definevo = (FormulaDefineVO)node.getUserObject();
				List<FormulaDefineVO> list = definevo.getFormualdefvo();
				if(list!=null && list.size() > 0){
					Vector vector = new Vector();
					for (int i = 0; i < list.size(); i++) {
						vector.add(list.get(i));
					}
					getUIListContent().setListData(vector);
				}
			}
		}
	}
	/**
	 * 项目内容
	 */
	private void onListContent(java.awt.event.KeyEvent ev) {
		int index = getUIListContent().getSelectedIndex();
		onListContent(index);
	}

	/**
	 * 项目内容
	 */
	private void onListContent(java.awt.event.MouseEvent ev) {
		if (ev.getClickCount() != 2) {
			return;
		}
		int index = getUIListContent().locationToIndex(ev.getPoint());
		onListContent(index);
	}
	/**
	 * 项目内容
	 */
	private void onListContent(int index) {
		if (index >= 0) {// 选中
			FormulaDefineVO formulaitemvo = (FormulaDefineVO) 
					getUIListContent().getModel().getElementAt(index);
			//
			String desc = formulaitemvo.getName();
			String parent = formulaitemvo.getParentname();
			String key = reCombinKey(parent, desc);
			updateTextAree(key);
		}
	}
	/**
	 * 业务函数
	 */
	private void onListService(java.awt.event.MouseEvent ev) {
		int index = getUIListService().locationToIndex(ev.getPoint());
		if (ev.getClickCount() != 2) {
			//zpm
			showBusinessDescription(index);
			return;
		}
		onListService(index);
	}
	/**
	 * 业务函数
	 */
	private void onListService(java.awt.event.KeyEvent ev) {
		int index = getUIListService().getSelectedIndex();
		onListService(index);
	}
	/**
	 * 业务函数
	 */
	private void onListService(int index) {
		String methodName = getUIListService().getModel().getElementAt(index)
				.toString();
		if (funVos != null && funVos.length > 0) {
			for (int i = 0; i < funVos.length; i++) {
				String name = funVos[i].getVitemname();
				int ii = funVos[i].getParamsCount();
				if (methodName.equalsIgnoreCase(name)) {
					String count = " ";
					if (ii > 0) {
						for (int s = 1; s < ii; s++) {
							count = count + ", ";
						}
					}
					methodName = name + "(" + count + ")";
				}
			}
		}
		methodName = " " + methodName;
		updateTextAree(methodName);
	}
	/**
	 *  业务函数
	 * zpm
	 */
	protected void showBusinessDescription(int index){
		if(index < 0 ){
			return;
		}
		BusinessFunction vo = (BusinessFunction)getUIListService().getModel().getElementAt(index);
		getUILabelDescript().setText(vo.getReadme());
	}
	/**
	 * 系统函数
	 */
	private void onListFormuRef(java.awt.event.KeyEvent ev) {
		int index = getUIListFormuRef().getSelectedIndex();
		onListFormuRef(index);
	}

	/**
	 * 系统函数
	 */
	private void onListFormuRef(java.awt.event.MouseEvent ev) {
		int index = getUIListFormuRef().locationToIndex(ev.getPoint());
		if (ev.getClickCount() != 2) {
			//zpm
			showSystemDescription(index);
			return;
		}
		onListFormuRef(index);
	}
	/**
	 * 系统函数
	 */
	private void onListFormuRef(int index) {
		String methodName = getUIListFormuRef().getModel().getElementAt(index).toString();
		if (methodName.equalsIgnoreCase("getColValue")
				|| methodName.equalsIgnoreCase("getColNmV")
				|| methodName.equalsIgnoreCase("cvs")
				|| methodName.equalsIgnoreCase("cvn")) {
			methodName += "( , , , ) ";
		} else if (methodName.equalsIgnoreCase("iif")
				|| methodName.equalsIgnoreCase("mid")) {
			methodName += "( , , ) ";
		} else if (methodName.equalsIgnoreCase("max")
				|| methodName.equalsIgnoreCase("min")
				|| methodName.equalsIgnoreCase("power")
				|| methodName.equalsIgnoreCase("charAt")
				|| methodName.equalsIgnoreCase("endsWith")
				|| methodName.equalsIgnoreCase("startsWith")
				|| methodName.equalsIgnoreCase("equalsIgnoreCase")
				|| methodName.equalsIgnoreCase("indexOf")
				|| methodName.equalsIgnoreCase("lastIndexOf")
				|| methodName.equalsIgnoreCase("left")
				|| methodName.equalsIgnoreCase("right")
				|| methodName.equalsIgnoreCase("round")) {
			methodName += "( , ) ";
		} else {
			methodName += "( ) ";
		}
		methodName = " " + methodName;
		updateTextAree(methodName);
	}
	/**
	 * 系统函数
	 * zpm
	 */
	protected void showSystemDescription(int index){
		if(index < 0 ){
			return;
		}
		SystemfunctionVO vo = (SystemfunctionVO)getUIListFormuRef().getModel().getElementAt(index);
		getUILabelDescript().setText(vo.getReadme());
	}
	
	
	
	
	/**
	 * @功能：对公式描述进行拆解分析,得到每一个有意义的字符
	 * @param formulateDesc
	 * @return
	 */
	protected String[] parseString(String formulateDesc) {
		// 若输入的formulateDesc为空，则返回空
		if (formulateDesc == null || formulateDesc.trim().length() == 0)
			return null;
		// 初始单词序列
		String asKeyWords[] = null;
		// 初始哈西表
		java.util.Hashtable table = new java.util.Hashtable();
		// 初始记数器
		int iCount = 0;
		int iOffSet = 0;
		iOffSet += getIndex(formulateDesc);
		// 找到第一个单词
		String sWord = parseWord(formulateDesc.substring(iOffSet));

		// 若单词长度大于0，则开始寻找其余单词
		while (sWord.length() > 0) {
			// 计数加上单词的长度
			iOffSet += sWord.length();
			String subString = formulateDesc.substring(iOffSet);
			iOffSet += getIndex(subString);
		
			// 去掉单词内的空格
			sWord = sWord.trim();
			// 若单词长度大于0
			if (sWord.length() > 0) {
				// 存入新单词
				String s = sWord;
				// 存入哈西表
				table.put(new Integer(iCount), s);
				// 计数加1
				iCount++;
			}
			// 查找下一个单词
			sWord = parseWord(formulateDesc.substring(iOffSet));

			// 若单词中仅含有空格则结束
			String s = sWord.trim();
			if (s.length() == 0) {
				sWord = s;
			}
		}
		// 初始字符串数组
		asKeyWords = new String[iCount];
		// 从哈西表中提取记录
		for (int i = 0; i < iCount; i++) {
			asKeyWords[i] = (String) table.get(new Integer(i));
		}
		// 返回字符串组
		return asKeyWords;
	}

	public int getIndex(String formulateDesc){
		int iOffSet = 0;
		while (// 若计数小于输入字串的长度，且输入字串在计数位的字符串为空格
				(iOffSet < formulateDesc.length() && formulateDesc.charAt(iOffSet) == ' ') // 若计数小于输入字串的长度，且输入字串在计数位的字符串为“Tab”
						|| (iOffSet < formulateDesc.length() && formulateDesc.charAt(iOffSet) == '\t') // 若计数小于输入字串的长度，且输入字串在计数位的字符串包含于换行符之内
						|| (iOffSet < formulateDesc.length() && m_sLineSep.indexOf(formulateDesc
								.charAt(iOffSet)) >= 0)) {
					// 计数器加1
					iOffSet++;
				}
		return iOffSet;
	}
	/**
	 * 从sql语句 s 中提取第一个有意义的单词 word = (|)|*|=|,|?| |<|>|!=|<>|<=|>=|其他
	 */
	protected String parseWord(String s) {
		// 注意此处不可用 s=s.trim();语句，否则会出错

		// 若输入单词长度为0，则返回""
		if (s.length() == 0) {
			return "";
		}
		// 标志:是否在''内,是否在""内,是否找到单词
		boolean bInSingle = false;
		boolean bInDouble = false;
		boolean bFound = false;
		// 初始计数器
		int iOffSet = 0;
		// 初始字符缓存
		char c;

		// 过滤掉空格,'\t',与回车换行符。计数器保存着除去特定字符的开始位置,即第一个有效字符的位置
		while (// 若计数小于输入字串的长度，且输入字串在计数位的字符串为空格
		(iOffSet < s.length() && s.charAt(iOffSet) == ' ') // 若计数小于输入字串的长度，且输入字串在计数位的字符串为“Tab”
				|| (iOffSet < s.length() && s.charAt(iOffSet) == '\t') // 若计数小于输入字串的长度，且输入字串在计数位的字符串包含于换行符之内
				|| (iOffSet < s.length() && m_sLineSep.indexOf(s
						.charAt(iOffSet)) >= 0)) {
			// 计数器加1
			iOffSet++;
			// 若计数大于输入字符串长度，则返回""
			if (iOffSet > s.length()) {
				return "";
			}
		}
		// 若计数大于输入字符串长度，则返回""
		if (iOffSet >= s.length()) {
			return "";
		}
		// 取得输入字符串在计数位置的字符
		c = s.charAt(iOffSet); // 第一个有效字符

		/*
		 * //过滤掉() if (c == '(' && s.length() >= 2 && s.charAt(1) == ')') { s =
		 * s.substring(2, s.length()); if (s.length() == 0) return ""; }
		 */

		// 返回特殊字符串
		// 计数器加1
		iOffSet++;
		// 若计数小于输入字符串长度
		if (iOffSet < s.length()) {
			// 取得输入字符串在计数位置2位的字符串
			char nowChar = s.charAt(iOffSet);
			String ss = "" + c + nowChar;
			if("->".equals(ss)){
				return ss;
			}
			// 依次比较是否为特殊字符串
			for (int i = 0; i < m_asSpecialStr.length; i++) {
				if (ss.equals(m_asSpecialStr[i])) {
					// 返回特殊字符串
					return s.substring(0, iOffSet + 1);
				}
			}
		
		}
		// 计数器减1
		iOffSet--;

		// 查找并返回特殊字符
		for (int i = 0; i < m_sSpecialChar.length(); i++) {
			if (c == m_sSpecialChar.charAt(i)) {
				return s.substring(0, iOffSet + 1);
			}
		}

		// 若计数小于输入字符串的长度
		while (iOffSet < s.length()) {
			// 取得输入字符串在计数位置的字符
			c = s.charAt(iOffSet);
			// 若为单引号
			if (c == '\'') {
				// 若不在双引号内
				if (!bInDouble) {
					// 若在单引号内
					if (bInSingle) {
						// 解析''
						// 若计数加1小于输入字符串的长度，且输入字符串在计数加1位置的字符为单引号
						if ((iOffSet + 1) < s.length()
								&& s.charAt(iOffSet + 1) == '\'') {
							// 计数加1
							iOffSet++;
						} else {
							// 否则，计数加1，跳出循环
							iOffSet++;
							break;
						}
					}
					// 是否在单引号中设为真
					bInSingle = true;
				}
			}

			// 若为双引号
			if (c == '"') {
				// 若不在单引号中
				if (!bInSingle) {
					// 若在双引号中
					if (bInDouble) {
						// 计数加1，跳出循环
						iOffSet++;
						break;
					}
					// 是否在双引号中设为真
					bInDouble = true;
				}
			}

			// 不在单引号内且不在双引号内
			if ((!bInDouble) && (!bInSingle)) {

				// 计数加1
				iOffSet++;
				// 若计数小于输入字符串的长度
				if (iOffSet < s.length()) {
					// 取得输入字符串在计数位置2位的字符串
					String ss = "" + c + s.charAt(iOffSet);
					// 循环比较是否为特殊字符串
					for (int i = 0; i < m_asSpecialStr.length; i++) {
						// 若找到，则跳出循环
						if (ss.equals(m_asSpecialStr[i])) {
							bFound = true;
							break;
						}
					}
				}
				// 计数减1
				iOffSet--;

				// 循环查找是否为特殊字符
				for (int i = 0; i < m_sSpecialChar.length(); i++) {
					if (c == m_sSpecialChar.charAt(i)) {
						// 若找到，则跳出循环
						bFound = true;
						break;
					}
				}
				// 若找到，则跳出循环
				if (bFound) {
					break;
				}
			}
			// 计数加1
			iOffSet++;
		}

		// 将输入字符串从0到计数位进行返回
		return s.substring(0, iOffSet);
	}

	public void setAllowNullFormula(boolean flag) {
		allowNullFormula = flag;
	}

	/**
	 * @功能:设置公式变量
	 * @return
	 */
	public void setFormula(String desc, String code) {

		setFormulaName(desc);

		setFormulaCode(code);

		getUITextAreaFormu().setText(desc);

	}

	/**
	 * @功能:给公式描述进行赋值
	 * @return java.lang.String
	 */
	public void setFormulaName(String desc) {
		this.formulaDesc = desc;
	}

	/**
	 * @return java.lang.String
	 */
	public void setFormulaCode(String code) {
		formulaCode = code;
	}

	/**
	 * @功能:设置公式的非编辑状态
	 * @since ：V1.00
	 * @param enabled
	 */
	public void setBtnEditEnabled() {
		getUIBnIf().setEnabled(false);
		getUIBnElse().setEnabled(false);
		getUIBnThen().setEnabled(false);
		getUIBnAnd().setEnabled(true);
		getUIBnOr().setEnabled(true);
		getUIBnEq().setEnabled(true);
		getUIBnNotEq().setEnabled(true);
		getUIBnLess().setEnabled(true);
		getUIBnLessEq().setEnabled(true);
		getUIBnMore().setEnabled(true);
		getUIBnMoreEq().setEnabled(true);
	}

	/**
	 * 
	 */
	public void setItems() {
		if(formulaDefvo !=null){//公式项目、内容定义
			DefaultMutableTreeNode rot = new DefaultMutableTreeNode("root");
			createTreeNode(rot,formulaDefvo);
			getUItree1().setModel(new DefaultTreeModel(rot));
		} else {
			getUItree1().setModel(null);
		}
		if(pzformuladefvo != null){//品种设置定义
//			DefaultMutableTreeNode rot = new DefaultMutableTreeNode("root");
//			createTreeNode(rot,pzformuladefvo);
//			getUItree2().setModel(new DefaultTreeModel(rot));
		}else{
			getUItree2().setModel(null);
		}		
		if(functionvos != null){//系统函数定义
			getUIListFormuRef().setListData(functionvos.toArray(new SystemfunctionVO[0]));	
		}else{
			getUIListFormuRef().setListData(item);
		}
	}
	
	protected void createTreeNode(DefaultMutableTreeNode rot,List<FormulaDefineVO> list){
		if(list == null || list.get(0).isFinalevel()){
			setColKeyValue(list);
			return;
		}
		for(int i = 0 ; list != null && i < list.size();i++){
			FormulaDefineVO vo = list.get(i);
			List<FormulaDefineVO> zlist = vo.getFormualdefvo();
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(vo);
			createTreeNode(root,zlist);
			rot.add(root);
		}
	}

	public void setColKeyValue(List<FormulaDefineVO> list){
		if(list != null && list.get(0).isFinalevel()){
			for(int i = 0 ;i<list.size();i++){
				FormulaDefineVO vo = list.get(i);
				String parent = vo.getParentname();
				String desc = vo.getName();
				String key = reCombinKey(parent, desc);
				colKeyValue.put(key, vo);
			}
		}
	}
	
//	/**
//	 * 按照KeyValue中的key值进行排序. key--->为Integer类型
//	 * 
//	 * @param list-->待排序集合
//	 * @return ArrayList<KeyValue> -->排序后的集合
//	 */
//	private ArrayList<KeyValue> onSort(ArrayList<KeyValue> list) {
//		if (list.size() > 0) {
//			ListSort sort = new ListSort();
//			Collections.sort(list, sort);
//		}
//		return list;
//	}
//
//	class ListSort implements Comparator<KeyValue> {
//		public int compare(KeyValue keyvalue1, KeyValue keyvalue2) {
//			if ((Integer) keyvalue1.getKey() > (Integer) keyvalue2.getKey()) {
//				return 1;
//			}
//			else {
//				return 0;
//			}
//		}
//	}

	/**
	 */
	public boolean testCode(String formuCode) throws Exception {
		return forParse.checkExpress(formuCode);
	}

//	/**
//	 */
//	private String transRow(String rowCode) {
//		String rowName = null;
//		if (rowKeyValue != null) {
//			rowName = (String) rowKeyValue.get(rowCode);
//		}
//		return rowName;
//	}

	/**
	 * @功能:翻译编码
	 * @param colDesc
	 * @return
	 */
	public String transCol(String colDesc) {
		String colCode = null;
		Object obj = colKeyValue.get(colDesc);
		if (obj != null) {
			FormulaDefineVO vo = (FormulaDefineVO) obj;
			colCode = vo.getCode();
		}
		if (colCode != null) {
			colCode = colCode.trim();
		}
		return colCode;
	}

	/**
	 * 功能：将公式的描述性表达式转换成相应的code表达式，同时进行公式验证 创建日期
	 * 
	 */
	public String transToCode(String[] formuPart,ArrayList<String> temp) {
		if (formuPart == null || formuPart.length <= 0) {
			return null;
		}
		Vector<String> vec = new Vector<String>();
		int ifCount = 0;// 如果数目
		int thenCount = 0;// 则数目
		int elseCount = 0; // else数目
		int leftCount = 0;// 左括号数目
		int rightCount = 0;// 右括号数目
		int defaultElseCount = 0; // 缺省数目

		for (int i = 0; i < formuPart.length; i++) {
			String st = formuPart[i];
			if (st.equals(getUIBnIf().getText().trim()) || st.equals("IIF")) {// "如果"按钮
				if (i > 0
						&& (!formuPart[i - 1].equals(getUIBnElse().getText()
								.trim()))) {
					vec.addElement(",");
				}
				vec.addElement("iif");
				ifCount++;
			} else if (st.equals(getUIBnElse().getText().trim())) {// "否则"按钮
				vec.addElement(",");
				elseCount++;
				if (i < formuPart.length - 1
						&& !formuPart[i + 1].equals(getUIBnIf().getText()
								.trim())) {
					defaultElseCount = elseCount;
				}
			} else if (st.equals(getUIBnThen().getText().trim())) {// "则"按钮
				vec.addElement(",");
				thenCount++;
			} else if (st.equals(getUIBnAnd().getText().trim())) {// "并且"按钮
				vec.addElement("&");
			} else if (st.equals(getUIBnOr().getText().trim())) {// "或者"按钮
				vec.addElement("|");
			}else if (st.equals(getUIBnOr().getText().trim())) {// "或者"按钮
				vec.addElement("|");
			} else if (st.startsWith("[")) {// 设置内容项<zpm>
				// 判断中括号[]是否配对
				if ( !isMatchedBracket( st ) ) {
					// 中括号不匹配，返回null
					return null;
				}
				st = transCol(st);
				if (st != null && !"".equals(st)) {
					vec.addElement(st);
				}
				else {
					return null;
				}
			} 
//			else if (st.startsWith("DM")) {// 设置
//				st = transRow(st);
//				if (st != null && !"".equals(st)) {
//					vec.addElement(st);
//				}
//			} 
			  else {
				if (isSpecialChar(st)) {
					vec.addElement(st);
				}else if("->".equals(st)){
					vec.addElement(st);
				}else if(temp.contains(st)){//lyf增加临时字段赋值
					  vec.addElement(st);

				}else {
					try {
//						Double.valueOf(st);
						vec.addElement(st);
					} catch (Exception ex) {
						return null;
					}
				}
			}
			// 开始
			if (getUIBnIf().getText().trim().equals(st) || "IIF".equals(st)
					|| getUIBnElse().getText().trim().equals(st)
					|| getUIBnThen().getText().trim().equals(st)
					|| getUIBnAnd().getText().trim().equals(st)
					|| getUIBnOr().getText().trim().equals(st)
					|| "+".equals(st) || "-".equals(st) || "*".equals(st)
					|| "/".equals(st)) {
				if ((i + 1) >= formuPart.length) {
					return null;
				}

			}

			if ("(".equals(st)) {
				leftCount++;
			}
			if (")".equals(st)) {
				rightCount++;
			}
		}

//		if (ifCount != thenCount) {
//			return null;
//		}
//		if (ifCount > 0
//				&& (elseCount == 0 || (elseCount > 0 && defaultElseCount != elseCount))) {
//			return null;
//		}
		if (leftCount != rightCount) {
			return null;
		}

//		for (int i = 0; i < ifCount; i++) {
//			vec.addElement(")");
//		}

		if (vec.size() > 0) {
			String reCode = "";
			for (int i = 0; i < vec.size(); i++) {
				reCode += vec.elementAt(i).toString() + "";
			}
			return reCode;
		}
		return null;
	}
	
	/**
	 * 判断中括号字符串中的中括号是否匹配
	 * @param bracketStr 中括号括起来的字符串 
	 * @return 匹配返回 true; 不匹配返回 false;
	 */
	private boolean isMatchedBracket( String bracketStr ) {
		if ( !bracketStr.startsWith( "[") ) {
			return false;
		}
		else if ( !bracketStr.endsWith( "]") ) {
			return false;
		}
		
		// 取第一个中括号 [ 和最后一个中括号 ] 之间的字符串
		String unbracketStr = bracketStr.substring( 1, bracketStr.length() - 1 );
		if ( unbracketStr.indexOf( "[" ) > 0 || 
			 unbracketStr.indexOf( "]" ) > 0	) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * @功能: 更新面板的值
	 */
	public void updateTextAree(String st) {

		String sel = getUITextAreaFormu().getSelectedText();

		if (sel != null) {
			getUITextAreaFormu().replaceSelection(st);
		} else {
			int pos = getUITextAreaFormu().getCaretPosition();

			if (pos >= 0) {
				getUITextAreaFormu().insert(st, pos);
			} else {
				getUITextAreaFormu().append(st);
			}
		}
		getUITextAreaFormu().requestFocus();
	}

	/**
	 * @功能:获得公式名称
	 */
	public String getFormulaName() {
		return formulaDesc;
	}

	/**
	 * @功能:获得公式编码
	 */
	public String getFormulaCode() {
		return formulaCode;
	}

//	/**
//	 * @功能:获得价格项是否被费用项引用 list<String>类型。String具体组成：<范围,价格项X> 比如说：<0,9> 或 <1,10>
//	 *                   或 <2,11>等等
//	 * @return
//	 */
//	public Integer[] getQuotedRowsNum() {
//		return getQuotedRowSet().toArray( new Integer[ getQuotedRowSet().size() ] );
//	}

	/**
	 * @功能:得到重新组装的key
	 * @return
	 */
	private String reCombinKey(String iscope, String desc) {
		String key = null;
		if (iscope != null && desc != null) {
			key = "[" + iscope + "." + desc + "]";
		}
		return key;
	}

	/**
	 * 过滤价格项，展示所要求的价格项
	 */
//	private FormulaItemVO[] getFliterItemvos(Map map1, Map map2,
//			HashMap<Integer, KeyValue[]> priceMap) {
//		FormulaItemVO[] curr_Itemvos = itemvos.clone();
//		ArrayList<FormulaItemVO> list = new ArrayList<FormulaItemVO>();
//		if (curr_Itemvos != null && curr_Itemvos.length > 0) {
//			for (int i = 0; i < curr_Itemvos.length; i++) {
//				int o1 = curr_Itemvos[i].getIcalculatescope();// 范围
//				int o2 = curr_Itemvos[i].getIclass();// 分类
//				if (map1.containsKey(o1) && map2.containsKey(o2)) {
//					list.add(curr_Itemvos[i]);
//					continue;
//				}
//				if (o2 == 4 && priceMap != null && priceMap.size() > 0) {// 分类为运输单价---->为4
//					Iterator it = priceMap.keySet().iterator();
//					while (it.hasNext()) {
//						int count = (Integer) it.next();// 取范围
//						KeyValue[] keyvalues = priceMap.get(count);
//						if (o1 == count) {
//							if (curr_Itemvos[i].getPriceX() > 0
//									&& keyvalues != null
//									&& keyvalues.length > 0) {
//								for (int z = 0; z < keyvalues.length; z++) {
//									int ii = (Integer) keyvalues[z].getKey();// 价格项几(其中的value是名称)
//									if (curr_Itemvos[i].getPriceX() == ii) {
//										list.add(curr_Itemvos[i]);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		if (list.size() > 0) {
//			curr_Itemvos = new FormulaItemVO[list.size()];
//			list.toArray(curr_Itemvos);
//		} else {
//			curr_Itemvos = null;
//		}
//		return curr_Itemvos;
//	}

	/**
	 * 从缓存更新数据
	 */
//	public KeyValue[] updateNamebyCache(String[] codes,
//			HashMap<Integer, KeyValue[]> priceMap) {
//		KeyValue[] valuse = null;
//		// 更新缓存
//		if (codes != null && codes.length > 0) {
//			// 第一次的话，codes 为空
//			if (itemvos != null && itemvos.length > 0) {
//				for (int i = 0; i < itemvos.length; i++) {
//					KeyValue o = new KeyValue();
//					int devprice = itemvos[i].getIclass();// 分类
//					int iscope = itemvos[i].getIcalculatescope();// 范围编码
//					String iScope = (String) getScopeTable().get(iscope);// 范围名称
//					// /这里重新给价格项重新赋值
//					if (devprice == 4 && priceMap != null
//							&& priceMap.size() > 0) {// 分类为运输单价---->为4
//						Iterator it = priceMap.keySet().iterator();
//						while (it.hasNext()) {
//							int count = (Integer) it.next();// 取范围
//							KeyValue[] keyvalues = priceMap.get(count);
//							if (iscope == count) {
//								if (itemvos[i].getPriceX() > 0
//										&& keyvalues != null
//										&& keyvalues.length > 0) {
//									for (int z = 0; z < keyvalues.length; z++) {
//										int ii = (Integer) keyvalues[z]
//												.getKey();// 显示的价格项
//										// x,1,2,3,4,5等
//										String name = (String) keyvalues[z]
//												.getValue();// 显示名称
//										if (ii == itemvos[i].getPriceX()) {
//											o.setOldPriceX(ii);
//											itemvos[i].setPriceX(ii);
//											itemvos[i].setVitemname(name);
//											break;
//										}
//									}
//								}
//							}
//						}
//					}
//					// /
//					String desc = Changeresid(itemvos[i]);// 描述
//					// String desc = itemvos[i].getVitemname();// 描述
//					String resid = itemvos[i].getResid().substring(11);
//					int id = Integer.parseInt(resid);
//					if(id>=44&&id<=66){
//						desc = itemvos[i].getVitemname();
//					}
//					String code = itemvos[i].getVitemcode();// 描述编码
//					String key = getCombinKey(iScope, desc);
//
//					o.setParent(iscope);
//					o.setKey(code);
//					o.setValue(devprice);
//					colKeyValue.put(key, o);
//					rowKeyValue.put(code, key);
//				}
//			}
//			//
//			valuse = new KeyValue[codes.length];
//			for (int i = 0; i < codes.length; i++) {
//				String[] formuPart = parseString(codes[i]);
//				// 转换成编码,并且验证
//				String formuDesc = transToCode(formuPart);
//				valuse[i] = new KeyValue();
//				valuse[i].setKey(codes[i]);
//				valuse[i].setValue(formuDesc);
//			}
//		}
//		return valuse;
//	}

//	public ArrayList<Integer> getQuotedkey(String[] codes) {
//		ArrayList<Integer> its = new ArrayList<Integer>();
//		try {
//			FormulaItemVO[] r_items = FormulaBillHelper
//					.getFormulateItemvos(" 1=1 ");
//			HashMap<String, Integer> map = new HashMap<String, Integer>();
//			if (r_items != null && r_items.length > 0) {
//				for (int i = 0; i < r_items.length; i++) {
//					String vitemcode = r_items[i].getVitemcode();
//					String vitemname = r_items[i].getVitemname();
//					if (vitemname.startsWith("价格项")) {
//						String price = vitemname.substring(3);
//						map.put(vitemcode, Integer.valueOf(price));
//					} else {
//						map.put(vitemcode, -1);
//					}
//				}
//			}
//			if (codes != null && codes.length > 0) {
//				for (int i = 0; i < codes.length; i++) {
//					String[] formuPart = parseString(codes[i]);
//					if (formuPart != null && formuPart.length > 0) {
//						for (int j = 0; j < formuPart.length; j++) {
//							if (formuPart[j].startsWith("DM")) {
//								Integer int1 = map.get(formuPart[j]);
//								if (int1 != null && int1 > 0) {
//									its.add(int1);
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (BusinessException e) {
//			ExceptionUtils.getInstance().wrappException(e);
//		}
//		return its;
//	}
	
//	private Set<Integer> getQuotedRowSet() {
//		if ( quotedRowSet == null ) {
//			quotedRowSet = new HashSet<Integer>();
//		}
//		
//		return quotedRowSet;
//	}
}