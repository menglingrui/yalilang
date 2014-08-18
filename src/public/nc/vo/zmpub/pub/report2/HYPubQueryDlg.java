package nc.vo.zmpub.pub.report2;

import java.awt.Component;
import java.util.Vector;

import javax.swing.table.TableCellEditor;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.field.BillField;

/**
 * 公共查询对话框。
 */
public class HYPubQueryDlg extends QueryDLG {

	private static final long serialVersionUID = 5562392331477957115L;

	public HYPubQueryDlg() {
		super();
		init();
	}

	public HYPubQueryDlg(java.awt.Container parent) {
		super(parent);
		init();
	}

	public HYPubQueryDlg(java.awt.Container parent, String title) {
		super(parent, title);
		init();
	}

	public HYPubQueryDlg(java.awt.Frame parent) {
		super(parent);
		init();
	}

	public HYPubQueryDlg(java.awt.Frame parent, String title) {
		super(parent, title);
		init();
	}

	public HYPubQueryDlg(boolean isFixedSet) {
		super(isFixedSet);
		init();
	}
	 public void setTempletID(String pkCorp, String funCode, String pkUser, String pkBusiType, String nodeKey, String orgtype) {
	    super.setTempletID(pkCorp, funCode, pkUser, pkBusiType, nodeKey, orgtype);
	    init();
	}
	 
	    /**
	     * 
	     * get conditionvo by fieldname
	     * @return nc.vo.pub.query.QueryConditionVO[]
	     * @param fieldcode
	     *            java.lang.String
	     */
	    public ConditionVO[] getConditionVOsByFieldName(String fieldname) {
	        ConditionVO[] conditions = getConditionVO();
	        Vector items = new Vector(1);
	        for (int i = 0; i < conditions.length; i++) {
	            if (conditions[i].getFieldName().equals(fieldname))
	                items.addElement(conditions[i]);
	        }
	        ConditionVO[] results = new ConditionVO[items.size()];
	        items.copyInto(results);
	        return results;
	    }
	 
	protected void afterEdit(TableCellEditor editor, int row, int col) {
		super.afterEdit(editor, row, col);
//		String fieldcode = getFieldCodeByRow(row);
//		if (PuPubVO.getString_TrimZeroLenAsNull(fieldcode) == null)
//			return;
//		if (fieldcode.startsWith("sc")) {
//			ConditionVO[] cons = getConditionVOsByFieldCode(fieldcode);
//			if (cons == null || cons.length == 0)
//				return;
//			String value = PuPubVO.getString_TrimZeroLenAsNull(cons[0]
//					.getRefResult().getRefPK());
//			if (PuPubVO.getString_TrimZeroLenAsNull(value) == null)
//				return;
//			Object o = getValueRefObjectByFieldCode("ys_ccyssq_h.pk_cave");
//			if (o == null)
//				return;
//			if (o instanceof UIRefPane) {
//				UIRefPane ref = (UIRefPane) o;
//				ref.getRefModel().addWherePart(
//						" and xew_hole.pk_minearea= '" + value + "'");
//			}
//		}
	}
	protected Component getComponent(String filedcode) {
		Object o = getValueRefObjectByFieldCode(filedcode);
		Component jb = null;
		if (o instanceof UIRefCellEditor) {
			jb = ((UIRefCellEditor) o).getComponent();// getUITabInput().getCellEditor(1,
														// 4);
		} else {
			jb = (Component) o;
		}

		return jb;
	}
	private void init() {
		String nodecode = getCurFunCode();
		String value = PuPubVO.getString_TrimZeroLenAsNull(nodecode);
		if (PuPubVO.getString_TrimZeroLenAsNull(value) == null)
			return;
		Object o = getValueRefObjectByFieldCode("sc");
		if (o == null)
			return;
		if (o instanceof UIRefPane) {
			UIRefPane ref = (UIRefPane) o;
			if(ref.getRefModel()==null){
				return ;
			}
			ref.getRefModel().addWherePart(
					" and zm_config.nodecode= '" + value + "'");
		}
	}
	/**
	 * 在查询模板中出现的主表名（或别名）。
	 * @return
	 * twh (2006-6-14 上午10:58:55)<br>
	 */
	protected String getHeadTable() {
		return null;
	}

	/**
	 * 单据状态列名
	 * @return
	 * twh (2006-6-14 上午11:01:28)<br>
	 */
	protected String getBillStatusFieldName() {
		return BillField.getInstance().getField_BillStatus();
	}

	/**
	 * 获得由ConditionVO[]组合成的where子句
	 */
	public String getWhereSQL(ConditionVO[] conditions) {
		return super.getWhereSQL(conditions);
	}

	protected String getPkCorp() {
		return ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}

	/**
	 * 根据fieldcode返回取值处的参照或固定选项对象(不包括选择的结果).
	 */
	public Object getValueRefObjectByFieldCode(String fieldcode) {
		Object o = super.getValueRefObjectByFieldCode(fieldcode);
		if (o == null) {
			return new UIRefPane();
		}
		return o;
	}
}
