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
 * ��ʽ�Ի���
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

	private String formulaCode = null;// ��ʽcode���ʽ

	private String formulaDesc = null;// ��ʽ����

	private boolean allowNullFormula = false;// ��ʽУ��ʱ�Ƿ�����ʽΪ��
	
	private ArrayList<FormulaDefineVO> formulaDefvo = null;//�˷ѹ�ʽ����
	
	private ArrayList<FormulaDefineVO> pzformuladefvo = null; //Ʒ�����ö���VO
	
	// ϵͳ��������
	/** mlr �޸� 2008/07/05 start * */
//	String[] item={"abs","exp","sqrt","power","log","ln","max","min","round","int","sgn","iif","sin","cos","tg","ctg","asin","acos","atg","getColValue","getColNmV","cvs","cvn","toNumber","toString","charAt","left","right","mid","endsWith","startsWith","equalsIgnoreCase","indexOf","lastIndexOf","length","toLowerCase","toUpperCase","date","year","mon","day","days",
//	 "yearOf","monOf","dayOf"};
	String[] item = { "ABS", "MAX", "MIN", "IIF" };
	
	private ArrayList<SystemfunctionVO> functionvos = null; //ϵͳ��������

	private JTree tree1 = null;
	
	private JTree tree2 = null;//Ʒ������
	
	private UITabbedPane tabpanel = null;//ҳǩ�л���� 
	
	private java.awt.Container parent;
	
	//<key--����>
	//<value--���������ʽ����VO>
	private Hashtable<String, FormulaDefineVO> colKeyValue = new Hashtable<String, FormulaDefineVO>();
	//
	//
//	private Hashtable<String, String> rowKeyValue = new Hashtable<String, String>();

	String m_sLineSep = System.getProperty("line.separator"); // ���з�

	String m_asOperationStr[] = { "=", "!=", "<>", "<", "<=", ">", ">=", ","}; // ������
	String m_splitStr = "->";
	// ���������ַ���
	String m_asSpecialStr[] = { "!=", "!>", "!<", "<>", "<=", ">=", "=", "<",
			">", "&", "&&", "|", "||", " ", "+", "-", "*", "/", "(", ")", ",",m_splitStr,
			//lyf ���ӷָ����
			";",
			//lyf���ӷָ����
			m_sLineSep };

	// ���������ַ�
	String m_sSpecialChar = ",-+()*/=<>& " + m_sLineSep;

	private boolean okFlag = false;// ȷ����ť���±�־

	// ��ʽ��֤��������ȡ������
	private FormulaParse forParse = null;

	private BusinessFunction[] funVos = null; // ���ο���ע�ắ��

	/**
	 * DiaTest ������ע�⡣
	 */
	@SuppressWarnings("deprecation")
	public FormulaDlg() {
		super();
		initialize();
	}

	/**
	 * DiaTest ������ע�⡣
	 * 
	 */
	public FormulaDlg(java.awt.Container parent) {
		super(parent);
		this.parent = parent;
		initialize();
	}

	/**
	 * DiaTest ������ע�⡣
	 * 
	 */
	public FormulaDlg(java.awt.Frame owner) {
		super(owner);
		initialize();
	}
	/**
	 * @����:��ʼ������<���س�ʼֵ>
	 */
	private void initialize() {
		try {
			// ��ȡ���е�ϵͳ����
			setName("DiaTest");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(774, 469);
			// �������ȡ������
			forParse = new FormulaParse();
			// ��ѯ���ο���ҵ����
			funVos = (BusinessFunction[]) HYPubBO_Client.queryByCondition(BusinessFunction.class, " dr = 0 ");
			setContentPane(getUIDialogContentPane());
			//���ð�ť״̬
			setBtnEditEnabled();
			// ���빫ʽ�������н���
			if (funVos != null && funVos.length > 0) {
				ArrayList list = new ArrayList();
				for (int i = 0; i < funVos.length; i++) {
					PostfixMathCommandI classs = (PostfixMathCommandI) Class
							.forName(funVos[i].getItemClass()).newInstance();
					forParse.addFunction(funVos[i].getVitemname(), classs);
					list.add(funVos[i].getVitemname());
				}
				// ���뵽���⺯�����棬ΪУ�鷽��
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
	 * ��ʼ������
	 */
	public void initData(ArrayList<SystemfunctionVO> functions,
			ArrayList<FormulaDefineVO> formulaDefvo, ArrayList<FormulaDefineVO> pzformuladefvo, 
			String title,String formuDesc, String formuCode) {
		// ������ݽ���Ļ�������
		getUIListContent().setListData(new Vector());
		// ���TextArea�Ļ�������
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
		if (((UIButton) ev.getSource()) == getUIBnOk()){// ȷ��
			onBnOk();
			return;
		} else if (((UIButton) ev.getSource()) == getUIBnCancel()){// ȡ��
			onBnCancel();
			return;
		} else if (((UIButton) ev.getSource()) == getUIBnTest()){// ��֤
			onBnTest();
			return;
		} else if (((UIButton) ev.getSource()) == getUIBnGuide()){// ��ʽ��
			return;
		} else if(((UIButton) ev.getSource()) == getUIBnAnd()){//����
			updateTextAree("  &&  ");
			return;
		} else if(((UIButton) ev.getSource()) == getUIBnOr()){//��
			updateTextAree("  ||  ");
			return;
		}
		//lyf 
		 else if(((UIButton) ev.getSource()) == getUIBnSplit()){//��ʱ����
				updateTextAree("  ; ");
				return;
			}
		 else if(((UIButton) ev.getSource()) == getUIBnEqe()){//��ʱ����
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
	 * ���� Panel1 ����ֵ��
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
	//��������
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
	 * ���� UIBn0 ����ֵ��
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
	 * ���� UIBn00 ����ֵ��
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
	 * ���� UIBn1 ����ֵ��
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
	 * ���� UIBn2 ����ֵ��
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
	 * ���� UIBn3 ����ֵ��
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
	 * ���� UIBn4 ����ֵ��
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
	 * ���� UIBn5 ����ֵ��
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
	 * ���� UIBn6 ����ֵ��
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
	 * ���� UIBn7 ����ֵ��
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
	 * ���� UIBn8 ����ֵ��
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
	 * ���� UIBn9 ����ֵ��
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
	 * ���� getUIBnEqe  ����ֵ��
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
	 * ���� getUIBnSplit  ����ֵ��
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
	 * ���� UIBnAdd ����ֵ��
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
	 * ���� UIBnAnd ����ֵ��
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
								"formulaparse", "UPPformulaparse-000023")/* @res " ���� " */);
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
	 * ���� UIBnCancel ����ֵ��
	 * @return UIButton
	 */
	private UIButton getUIBnCancel() {
		if (ivjUIBnCancel == null) {
			try {
				ivjUIBnCancel = new UIButton();
				ivjUIBnCancel.setName("UIBnCancel");
				ivjUIBnCancel.setFont(new Font("dialog", 0, 12));
				ivjUIBnCancel.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC001-0000008")/* @res "ȡ��" */);
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
	 * ���� UIBnDiv ����ֵ��
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
	 * ���� UIBnDot ����ֵ��
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
	 * ���� UIBnElse ����ֵ��
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
								"formulaparse", "UPPformulaparse-000024")/* @res " ���� " */);
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
	 * ���� UIBnEq ����ֵ��
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
	 * ���� UIBnGuide ����ֵ��
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
								"formulaparse", "UPPformulaparse-000025")/* @res "��ʽ��" */);
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
	 * ���� UIBnIf ����ֵ��
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
								"formulaparse", "UPPformulaparse-000026")/* @res " ��� " */);
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
	 * ���� UIBnLeft ����ֵ��
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
	 * ���� UIBnLess ����ֵ��
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
	 * ���� UIBnLessEq ����ֵ��
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
	 * ���� UIBnMore ����ֵ��
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
	 * ���� UIBnMoreEq ����ֵ��
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
	 * ���� UIBnMul ����ֵ��
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
	 * ���� UIBnNotEq ����ֵ��
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
	 * ���� UIBnOk ����ֵ��
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
						"common", "UC001-0000044")/* @res "ȷ��" */);
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
	 * ���� UIBnOr ����ֵ��
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
								"formulaparse", "UPPformulaparse-000027")/* @res " ���� " */);
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
	 * ���� UIBnRight ����ֵ��
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
	 * ���� UIBnSub ����ֵ��
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
	 * ���� UIBnTest ����ֵ��
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
								"formulaparse", "UPPformulaparse-000028")/* @res "��ʽ��֤" */);
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
	 * ���� UIBnThen ����ֵ��
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
								"formulaparse", "UPPformulaparse-000029")/* @res " �� " */);
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
	 * ���� UIDialogContentPane ����ֵ��
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
	 * ���� UILabel1 ����ֵ��
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
								"formulaparse", "UPPformulaparse-000030")/* @res "�� Ŀ" */);
				ivjUILabel1.setBounds(80, 3, 44, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabel1;
	}

	/**
	 * ���� UILabelContent ����ֵ��
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
								"formulaparse", "UPPformulaparse-000031")/* @res "�� ��" */);
				ivjUILabelContent.setBounds(80, 3, 44, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelContent;
	}

	/**
	 * ���� UILabelContent1 ����ֵ��
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
								"formulaparse", "UPPformulaparse-000032")/* @res "ҵ����" */);
				ivjUILabelContent1.setBounds(68, 3, 69, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelContent1;
	}

	/**
	 * ���� UILabelFormu ����ֵ��
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
								"formulaparse", "UPPformulaparse-000033")/* @res "ϵͳ����" */);
				ivjUILabelFormu.setBounds(74, 3, 63, 22);

			} catch (Throwable ivjExc) {

				handleException(ivjExc);
			}
		}
		return ivjUILabelFormu;
	}

	/**
	 * ��ʽ���ݿ�
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
	 * ϵͳ������
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
				//zpm�����ط�
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
	//��Ŀ�л�
	private UITabbedPane  getUITabbedPanel(){
		if(tabpanel == null){
			tabpanel = new nc.ui.pub.beans.UITabbedPane(JTabbedPane.BOTTOM);
			tabpanel.setName("UITabPane");
			tabpanel.insertTab("��Ŀ", null,getUItree1(), null, 0);
			tabpanel.insertTab("Ʒ��", null,getUItree2(), null, 1);
		}
		return tabpanel;
	}
		

	/**
	 * ҵ������
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
				// ҵ�������ӡ�����ȡ��������.����ʹ�ú���
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
	 * ���� UIPanel10 ����ֵ��
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
	 * ���� UIPanel11 ����ֵ��
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
	 * ���� UIPanel12 ����ֵ��
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
	 * ���� UIPanel13 ����ֵ��
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
	 * ���� UIPanel14 ����ֵ��
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
	 * ���� UIPanel14GridLayout ����ֵ��
	 * 
	 * @return GridLayout
	 */
	private GridLayout getUIPanel14GridLayout() {
		GridLayout ivjUIPanel14GridLayout = null;
		try {
			/* �������� */
			ivjUIPanel14GridLayout = new GridLayout();
			ivjUIPanel14GridLayout.setColumns(4);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjUIPanel14GridLayout;
	}

	/**
	 * ���� UIPanel15 ����ֵ��
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
	 * ���� UIPanel16 ����ֵ��
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
	 * ���� UIPanel2 ����ֵ��
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
	 * ���� UIPanel4 ����ֵ��
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
	 * ���� UIPanel5 ����ֵ��
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
	 * ���� UIPanel6 ����ֵ��
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
	 * ���� UIPanel7 ����ֵ��
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
	 * ���� UIPanel71 ����ֵ��
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
	 * ���� UIPanel8 ����ֵ��
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
	 * ���� UIPanel9 ����ֵ��
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
	 * ���� UIPanelButton ����ֵ��
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
	 * ���� UIPanelDigit ����ֵ��
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
	 * ���� UIPanelDigitGridLayout ����ֵ��
	 * 
	 * @return GridLayout
	 */
	private GridLayout getUIPanelDigitGridLayout() {
		GridLayout ivjUIPanelDigitGridLayout = null;
		try {
			/* �������� */
			ivjUIPanelDigitGridLayout = new GridLayout(3, 4);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjUIPanelDigitGridLayout;
	}

	/**
	 * ���� UIPanelOprate ����ֵ��
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
	 * ���� UIPanelOprateGridLayout ����ֵ��
	 * 
	 * @return GridLayout
	 */
	private GridLayout getUIPanelOprateGridLayout() {
		GridLayout ivjUIPanelOprateGridLayout = null;
		try {
			/* �������� */
			ivjUIPanelOprateGridLayout = new GridLayout(3, 4);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjUIPanelOprateGridLayout;
	}

	/**
	 * ���� UIScrollPane1 ����ֵ��
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
	 * ���� UIScrollPane2 ����ֵ��
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
	 * ���� UIScrollPane3 ����ֵ��
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
	 * ���� UIScrollPane31 ����ֵ��
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
	 * ���� UIScrollPane4 ����ֵ��
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
	 * ���� UITextAreaFormu ����ֵ��
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
	 * ÿ�������׳��쳣ʱ������
	 */
	public void handleException(Throwable exception) {
		exception.printStackTrace();
		MessageDialog.showErrorDlg(parent, "����", exception.getMessage());
	}

	/**
	 * ��ʼ���ࡣ
	 */
	private void initConnections() {
		java.awt.event.MouseAdapter aMA = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent ev) {
				if ((UIList) ev.getSource() == getUIListFormuRef()) {//ϵͳ������
					onListFormuRef(ev);
					return;
				}else if ((UIList) ev.getSource() == getUIListContent()) {//��ʽ���ݿ�
					onListContent(ev);
					return;
				}else if ((UIList) ev.getSource() == getUIListService()) {//ҵ������
					onListService(ev);
					return;
				}
			}
		};
		//ϵͳ������
		getUIListFormuRef().addMouseListener(aMA);
		//���ݿ�
		getUIListContent().addMouseListener(aMA);
		//ҵ������
		getUIListService().addMouseListener(aMA);

		java.awt.event.KeyListener aKL = new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent ev) {
			}
			public void keyTyped(java.awt.event.KeyEvent ev) {
			}
			public void keyReleased(java.awt.event.KeyEvent ev) {
				if (ev.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {//�س���
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
		//ϵͳ������
		getUIListFormuRef().addKeyListener(aKL);
		//���ݿ�
		getUIListContent().addKeyListener(aKL);
		//����ҵ�����ļ���
		getUIListService().addKeyListener(aKL);
		//��Ŀ��
		getUItree1().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) getUItree1()
						.getLastSelectedPathComponent();
				onListItem(node);

			}
		});
		
		//Ʒ������
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
	 * @����:����->��ʽ��֤
	 */
	public boolean onBnTest() {
		try {
			String formulaDescNew = getUITextAreaFormu().getText();
			if (formulaDescNew == null || formulaDescNew.trim().length() <= 0) {
				setFormulaName("");
				setFormulaCode("");
				return true;
			}
//			// ��յ��ݽ���۸���ҳǩ�Ƿ����õ��б��
//			getQuotedRowSet().clear();
			//
			String[] formuPart = parseString(formulaDescNew);
			//lyf
			 ArrayList<String> temp = getTempFieds(formuPart);
			 //lyf
			// ת���ɱ���,������֤
			String formuCode = transToCode(formuPart,temp);

			if (formuCode == null || formuCode.trim().length() <= 0) {
				MessageDialog
						.showErrorDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000034")/* @res "��ʽ���ô���" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000036")/* @res "��ʽ���ô���" */);
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
										"UPPformulaparse-000034")/* @res "��ʽ���ô���" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000036")/* @res "��ʽ���ô���" */);
				return false;
			}
			// }
			if (!okFlag) {
				MessageDialog
						.showHintDlg(
								this,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000037")/* @res "��ʽ������ȷ" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"formulaparse",
										"UPPformulaparse-000038")/* @res "��ʽ������ȷ��" */);

			}
			return true;
		} catch (Exception ex) {
			MessageDialog.showErrorDlg(parent, "����", ex.getMessage());
			return false;
		}
	}
	/**
	 * ��Ŀ�༭
	 */
	private void onListItem(DefaultMutableTreeNode node) {
		if (node != null) {
			DefaultMutableTreeNode p = (DefaultMutableTreeNode) node.getParent();
			if (p != null && p.isRoot()) {//���ڵ�
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
	 * ��Ŀ����
	 */
	private void onListContent(java.awt.event.KeyEvent ev) {
		int index = getUIListContent().getSelectedIndex();
		onListContent(index);
	}

	/**
	 * ��Ŀ����
	 */
	private void onListContent(java.awt.event.MouseEvent ev) {
		if (ev.getClickCount() != 2) {
			return;
		}
		int index = getUIListContent().locationToIndex(ev.getPoint());
		onListContent(index);
	}
	/**
	 * ��Ŀ����
	 */
	private void onListContent(int index) {
		if (index >= 0) {// ѡ��
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
	 * ҵ����
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
	 * ҵ����
	 */
	private void onListService(java.awt.event.KeyEvent ev) {
		int index = getUIListService().getSelectedIndex();
		onListService(index);
	}
	/**
	 * ҵ����
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
	 *  ҵ����
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
	 * ϵͳ����
	 */
	private void onListFormuRef(java.awt.event.KeyEvent ev) {
		int index = getUIListFormuRef().getSelectedIndex();
		onListFormuRef(index);
	}

	/**
	 * ϵͳ����
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
	 * ϵͳ����
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
	 * ϵͳ����
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
	 * @���ܣ��Թ�ʽ�������в�����,�õ�ÿһ����������ַ�
	 * @param formulateDesc
	 * @return
	 */
	protected String[] parseString(String formulateDesc) {
		// �������formulateDescΪ�գ��򷵻ؿ�
		if (formulateDesc == null || formulateDesc.trim().length() == 0)
			return null;
		// ��ʼ��������
		String asKeyWords[] = null;
		// ��ʼ������
		java.util.Hashtable table = new java.util.Hashtable();
		// ��ʼ������
		int iCount = 0;
		int iOffSet = 0;
		iOffSet += getIndex(formulateDesc);
		// �ҵ���һ������
		String sWord = parseWord(formulateDesc.substring(iOffSet));

		// �����ʳ��ȴ���0����ʼѰ�����൥��
		while (sWord.length() > 0) {
			// �������ϵ��ʵĳ���
			iOffSet += sWord.length();
			String subString = formulateDesc.substring(iOffSet);
			iOffSet += getIndex(subString);
		
			// ȥ�������ڵĿո�
			sWord = sWord.trim();
			// �����ʳ��ȴ���0
			if (sWord.length() > 0) {
				// �����µ���
				String s = sWord;
				// ���������
				table.put(new Integer(iCount), s);
				// ������1
				iCount++;
			}
			// ������һ������
			sWord = parseWord(formulateDesc.substring(iOffSet));

			// �������н����пո������
			String s = sWord.trim();
			if (s.length() == 0) {
				sWord = s;
			}
		}
		// ��ʼ�ַ�������
		asKeyWords = new String[iCount];
		// �ӹ���������ȡ��¼
		for (int i = 0; i < iCount; i++) {
			asKeyWords[i] = (String) table.get(new Integer(i));
		}
		// �����ַ�����
		return asKeyWords;
	}

	public int getIndex(String formulateDesc){
		int iOffSet = 0;
		while (// ������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ���Ϊ�ո�
				(iOffSet < formulateDesc.length() && formulateDesc.charAt(iOffSet) == ' ') // ������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ���Ϊ��Tab��
						|| (iOffSet < formulateDesc.length() && formulateDesc.charAt(iOffSet) == '\t') // ������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ��������ڻ��з�֮��
						|| (iOffSet < formulateDesc.length() && m_sLineSep.indexOf(formulateDesc
								.charAt(iOffSet)) >= 0)) {
					// ��������1
					iOffSet++;
				}
		return iOffSet;
	}
	/**
	 * ��sql��� s ����ȡ��һ��������ĵ��� word = (|)|*|=|,|?| |<|>|!=|<>|<=|>=|����
	 */
	protected String parseWord(String s) {
		// ע��˴������� s=s.trim();��䣬��������

		// �����뵥�ʳ���Ϊ0���򷵻�""
		if (s.length() == 0) {
			return "";
		}
		// ��־:�Ƿ���''��,�Ƿ���""��,�Ƿ��ҵ�����
		boolean bInSingle = false;
		boolean bInDouble = false;
		boolean bFound = false;
		// ��ʼ������
		int iOffSet = 0;
		// ��ʼ�ַ�����
		char c;

		// ���˵��ո�,'\t',��س����з��������������ų�ȥ�ض��ַ��Ŀ�ʼλ��,����һ����Ч�ַ���λ��
		while (// ������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ���Ϊ�ո�
		(iOffSet < s.length() && s.charAt(iOffSet) == ' ') // ������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ���Ϊ��Tab��
				|| (iOffSet < s.length() && s.charAt(iOffSet) == '\t') // ������С�������ִ��ĳ��ȣ��������ִ��ڼ���λ���ַ��������ڻ��з�֮��
				|| (iOffSet < s.length() && m_sLineSep.indexOf(s
						.charAt(iOffSet)) >= 0)) {
			// ��������1
			iOffSet++;
			// ���������������ַ������ȣ��򷵻�""
			if (iOffSet > s.length()) {
				return "";
			}
		}
		// ���������������ַ������ȣ��򷵻�""
		if (iOffSet >= s.length()) {
			return "";
		}
		// ȡ�������ַ����ڼ���λ�õ��ַ�
		c = s.charAt(iOffSet); // ��һ����Ч�ַ�

		/*
		 * //���˵�() if (c == '(' && s.length() >= 2 && s.charAt(1) == ')') { s =
		 * s.substring(2, s.length()); if (s.length() == 0) return ""; }
		 */

		// ���������ַ���
		// ��������1
		iOffSet++;
		// ������С�������ַ�������
		if (iOffSet < s.length()) {
			// ȡ�������ַ����ڼ���λ��2λ���ַ���
			char nowChar = s.charAt(iOffSet);
			String ss = "" + c + nowChar;
			if("->".equals(ss)){
				return ss;
			}
			// ���αȽ��Ƿ�Ϊ�����ַ���
			for (int i = 0; i < m_asSpecialStr.length; i++) {
				if (ss.equals(m_asSpecialStr[i])) {
					// ���������ַ���
					return s.substring(0, iOffSet + 1);
				}
			}
		
		}
		// ��������1
		iOffSet--;

		// ���Ҳ����������ַ�
		for (int i = 0; i < m_sSpecialChar.length(); i++) {
			if (c == m_sSpecialChar.charAt(i)) {
				return s.substring(0, iOffSet + 1);
			}
		}

		// ������С�������ַ����ĳ���
		while (iOffSet < s.length()) {
			// ȡ�������ַ����ڼ���λ�õ��ַ�
			c = s.charAt(iOffSet);
			// ��Ϊ������
			if (c == '\'') {
				// ������˫������
				if (!bInDouble) {
					// ���ڵ�������
					if (bInSingle) {
						// ����''
						// ��������1С�������ַ����ĳ��ȣ��������ַ����ڼ�����1λ�õ��ַ�Ϊ������
						if ((iOffSet + 1) < s.length()
								&& s.charAt(iOffSet + 1) == '\'') {
							// ������1
							iOffSet++;
						} else {
							// ���򣬼�����1������ѭ��
							iOffSet++;
							break;
						}
					}
					// �Ƿ��ڵ���������Ϊ��
					bInSingle = true;
				}
			}

			// ��Ϊ˫����
			if (c == '"') {
				// �����ڵ�������
				if (!bInSingle) {
					// ����˫������
					if (bInDouble) {
						// ������1������ѭ��
						iOffSet++;
						break;
					}
					// �Ƿ���˫��������Ϊ��
					bInDouble = true;
				}
			}

			// ���ڵ��������Ҳ���˫������
			if ((!bInDouble) && (!bInSingle)) {

				// ������1
				iOffSet++;
				// ������С�������ַ����ĳ���
				if (iOffSet < s.length()) {
					// ȡ�������ַ����ڼ���λ��2λ���ַ���
					String ss = "" + c + s.charAt(iOffSet);
					// ѭ���Ƚ��Ƿ�Ϊ�����ַ���
					for (int i = 0; i < m_asSpecialStr.length; i++) {
						// ���ҵ���������ѭ��
						if (ss.equals(m_asSpecialStr[i])) {
							bFound = true;
							break;
						}
					}
				}
				// ������1
				iOffSet--;

				// ѭ�������Ƿ�Ϊ�����ַ�
				for (int i = 0; i < m_sSpecialChar.length(); i++) {
					if (c == m_sSpecialChar.charAt(i)) {
						// ���ҵ���������ѭ��
						bFound = true;
						break;
					}
				}
				// ���ҵ���������ѭ��
				if (bFound) {
					break;
				}
			}
			// ������1
			iOffSet++;
		}

		// �������ַ�����0������λ���з���
		return s.substring(0, iOffSet);
	}

	public void setAllowNullFormula(boolean flag) {
		allowNullFormula = flag;
	}

	/**
	 * @����:���ù�ʽ����
	 * @return
	 */
	public void setFormula(String desc, String code) {

		setFormulaName(desc);

		setFormulaCode(code);

		getUITextAreaFormu().setText(desc);

	}

	/**
	 * @����:����ʽ�������и�ֵ
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
	 * @����:���ù�ʽ�ķǱ༭״̬
	 * @since ��V1.00
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
		if(formulaDefvo !=null){//��ʽ��Ŀ�����ݶ���
			DefaultMutableTreeNode rot = new DefaultMutableTreeNode("root");
			createTreeNode(rot,formulaDefvo);
			getUItree1().setModel(new DefaultTreeModel(rot));
		} else {
			getUItree1().setModel(null);
		}
		if(pzformuladefvo != null){//Ʒ�����ö���
//			DefaultMutableTreeNode rot = new DefaultMutableTreeNode("root");
//			createTreeNode(rot,pzformuladefvo);
//			getUItree2().setModel(new DefaultTreeModel(rot));
		}else{
			getUItree2().setModel(null);
		}		
		if(functionvos != null){//ϵͳ��������
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
//	 * ����KeyValue�е�keyֵ��������. key--->ΪInteger����
//	 * 
//	 * @param list-->�����򼯺�
//	 * @return ArrayList<KeyValue> -->�����ļ���
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
	 * @����:�������
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
	 * ���ܣ�����ʽ�������Ա��ʽת������Ӧ��code���ʽ��ͬʱ���й�ʽ��֤ ��������
	 * 
	 */
	public String transToCode(String[] formuPart,ArrayList<String> temp) {
		if (formuPart == null || formuPart.length <= 0) {
			return null;
		}
		Vector<String> vec = new Vector<String>();
		int ifCount = 0;// �����Ŀ
		int thenCount = 0;// ����Ŀ
		int elseCount = 0; // else��Ŀ
		int leftCount = 0;// ��������Ŀ
		int rightCount = 0;// ��������Ŀ
		int defaultElseCount = 0; // ȱʡ��Ŀ

		for (int i = 0; i < formuPart.length; i++) {
			String st = formuPart[i];
			if (st.equals(getUIBnIf().getText().trim()) || st.equals("IIF")) {// "���"��ť
				if (i > 0
						&& (!formuPart[i - 1].equals(getUIBnElse().getText()
								.trim()))) {
					vec.addElement(",");
				}
				vec.addElement("iif");
				ifCount++;
			} else if (st.equals(getUIBnElse().getText().trim())) {// "����"��ť
				vec.addElement(",");
				elseCount++;
				if (i < formuPart.length - 1
						&& !formuPart[i + 1].equals(getUIBnIf().getText()
								.trim())) {
					defaultElseCount = elseCount;
				}
			} else if (st.equals(getUIBnThen().getText().trim())) {// "��"��ť
				vec.addElement(",");
				thenCount++;
			} else if (st.equals(getUIBnAnd().getText().trim())) {// "����"��ť
				vec.addElement("&");
			} else if (st.equals(getUIBnOr().getText().trim())) {// "����"��ť
				vec.addElement("|");
			}else if (st.equals(getUIBnOr().getText().trim())) {// "����"��ť
				vec.addElement("|");
			} else if (st.startsWith("[")) {// ����������<zpm>
				// �ж�������[]�Ƿ����
				if ( !isMatchedBracket( st ) ) {
					// �����Ų�ƥ�䣬����null
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
//			else if (st.startsWith("DM")) {// ����
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
				}else if(temp.contains(st)){//lyf������ʱ�ֶθ�ֵ
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
			// ��ʼ
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
	 * �ж��������ַ����е��������Ƿ�ƥ��
	 * @param bracketStr ���������������ַ��� 
	 * @return ƥ�䷵�� true; ��ƥ�䷵�� false;
	 */
	private boolean isMatchedBracket( String bracketStr ) {
		if ( !bracketStr.startsWith( "[") ) {
			return false;
		}
		else if ( !bracketStr.endsWith( "]") ) {
			return false;
		}
		
		// ȡ��һ�������� [ �����һ�������� ] ֮����ַ���
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
	 * @����: ��������ֵ
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
	 * @����:��ù�ʽ����
	 */
	public String getFormulaName() {
		return formulaDesc;
	}

	/**
	 * @����:��ù�ʽ����
	 */
	public String getFormulaCode() {
		return formulaCode;
	}

//	/**
//	 * @����:��ü۸����Ƿ񱻷��������� list<String>���͡�String������ɣ�<��Χ,�۸���X> ����˵��<0,9> �� <1,10>
//	 *                   �� <2,11>�ȵ�
//	 * @return
//	 */
//	public Integer[] getQuotedRowsNum() {
//		return getQuotedRowSet().toArray( new Integer[ getQuotedRowSet().size() ] );
//	}

	/**
	 * @����:�õ�������װ��key
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
	 * ���˼۸��չʾ��Ҫ��ļ۸���
	 */
//	private FormulaItemVO[] getFliterItemvos(Map map1, Map map2,
//			HashMap<Integer, KeyValue[]> priceMap) {
//		FormulaItemVO[] curr_Itemvos = itemvos.clone();
//		ArrayList<FormulaItemVO> list = new ArrayList<FormulaItemVO>();
//		if (curr_Itemvos != null && curr_Itemvos.length > 0) {
//			for (int i = 0; i < curr_Itemvos.length; i++) {
//				int o1 = curr_Itemvos[i].getIcalculatescope();// ��Χ
//				int o2 = curr_Itemvos[i].getIclass();// ����
//				if (map1.containsKey(o1) && map2.containsKey(o2)) {
//					list.add(curr_Itemvos[i]);
//					continue;
//				}
//				if (o2 == 4 && priceMap != null && priceMap.size() > 0) {// ����Ϊ���䵥��---->Ϊ4
//					Iterator it = priceMap.keySet().iterator();
//					while (it.hasNext()) {
//						int count = (Integer) it.next();// ȡ��Χ
//						KeyValue[] keyvalues = priceMap.get(count);
//						if (o1 == count) {
//							if (curr_Itemvos[i].getPriceX() > 0
//									&& keyvalues != null
//									&& keyvalues.length > 0) {
//								for (int z = 0; z < keyvalues.length; z++) {
//									int ii = (Integer) keyvalues[z].getKey();// �۸��(���е�value������)
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
	 * �ӻ����������
	 */
//	public KeyValue[] updateNamebyCache(String[] codes,
//			HashMap<Integer, KeyValue[]> priceMap) {
//		KeyValue[] valuse = null;
//		// ���»���
//		if (codes != null && codes.length > 0) {
//			// ��һ�εĻ���codes Ϊ��
//			if (itemvos != null && itemvos.length > 0) {
//				for (int i = 0; i < itemvos.length; i++) {
//					KeyValue o = new KeyValue();
//					int devprice = itemvos[i].getIclass();// ����
//					int iscope = itemvos[i].getIcalculatescope();// ��Χ����
//					String iScope = (String) getScopeTable().get(iscope);// ��Χ����
//					// /�������¸��۸������¸�ֵ
//					if (devprice == 4 && priceMap != null
//							&& priceMap.size() > 0) {// ����Ϊ���䵥��---->Ϊ4
//						Iterator it = priceMap.keySet().iterator();
//						while (it.hasNext()) {
//							int count = (Integer) it.next();// ȡ��Χ
//							KeyValue[] keyvalues = priceMap.get(count);
//							if (iscope == count) {
//								if (itemvos[i].getPriceX() > 0
//										&& keyvalues != null
//										&& keyvalues.length > 0) {
//									for (int z = 0; z < keyvalues.length; z++) {
//										int ii = (Integer) keyvalues[z]
//												.getKey();// ��ʾ�ļ۸���
//										// x,1,2,3,4,5��
//										String name = (String) keyvalues[z]
//												.getValue();// ��ʾ����
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
//					String desc = Changeresid(itemvos[i]);// ����
//					// String desc = itemvos[i].getVitemname();// ����
//					String resid = itemvos[i].getResid().substring(11);
//					int id = Integer.parseInt(resid);
//					if(id>=44&&id<=66){
//						desc = itemvos[i].getVitemname();
//					}
//					String code = itemvos[i].getVitemcode();// ��������
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
//				// ת���ɱ���,������֤
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
//					if (vitemname.startsWith("�۸���")) {
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