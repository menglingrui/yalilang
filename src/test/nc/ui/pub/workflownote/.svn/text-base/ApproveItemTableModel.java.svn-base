package nc.ui.pub.workflownote;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.table.VOTableModel;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.wfengine.core.util.DurationUnit;
import nc.vo.wfengine.pub.WFTask;
import nc.vo.wfengine.pub.WfTaskStatus;
import nc.vo.zmpub.pub.tool.ZmPubTool;

/**
 * 审批状况表模型
 * @author leijun 2006-6-8
 * @since NC50
 * 修改：yangtao
 * 修改日期：2013-10-22
 * 列名增加了发送人部门和审批人部门
 */
public class ApproveItemTableModel extends VOTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 表格变化后不能频繁获取服务器时间，这里缓存一个类级变量
	 */
	private UFDateTime m_serverTime = null;

	/**
	 * 列名
	 */
	private String[] strColNames = null;
	
	/**
	 * 设置列
	 * @param cls
	 *修改日期： 2013年10月23日下午1:52:43
	 */

	public ApproveItemTableModel(java.lang.Class cls) {
		super(cls);

		//多语初始化
		strColNames = new String[] { WorkitemColumnInfo.SENDER.toString(),
				WorkitemColumnInfo.SENDDATE.toString(), WorkitemColumnInfo.CHECKER.toString(),
				WorkitemColumnInfo.CHECKDATE.toString(), WorkitemColumnInfo.DURATION.toString(),
				WorkitemColumnInfo.APPROVESTATUS.toString(), WorkitemColumnInfo.APPROVERESULT.toString(),
				WorkitemColumnInfo.APPROVENOTE.toString(), WorkitemColumnInfo.TITLE.toString(),
				WorkitemColumnInfo.MONEY.toString(), WorkitemColumnInfo.LOCALMONEY.toString(),
				WorkitemColumnInfo.ASSMONEY.toString(), WorkitemColumnInfo.MONEY_BEFORE.toString(),
				WorkitemColumnInfo.LOCALMONEY_BEFORE.toString(),
				WorkitemColumnInfo.ASSMONEY_BEFORE.toString(),
				//增加下面两个列 yangtao
		       "发送人部门",
		       "审批人部门"  
		};

	}

	private UFDateTime getCurrServerTime() {
		if (m_serverTime == null) {
			m_serverTime = ClientEnvironment.getServerTime();
		}
		return m_serverTime;
	}

	/**
	 * 列宽
	 * @return
	 * @author yangtao
	 * @date 2013年10月23日 下午1:50:37
	 */
	public int[] getColumnWidth() {
		int[] iWidths = new int[] { 60, 120, 60, 120, 95, 60, 60, 120, 260, 100, 100, 100, 100, 100,
				100 ,100,100};
		return iWidths;
	}

	public Class getColumnClass(int columnIndex) {
		if (columnIndex < 9) {
			return String.class;
		} else {
			return UFDouble.class;
		}
	}

	public int getColumnCount() {
		return strColNames.length;
	}

	public String getColumnName(int column) {
		return strColNames[column];
	}

	public Object getValueAt(int row, int col) {
		Object obj = null;
		try {
			WorkflownoteVO flownote = (WorkflownoteVO) getVO(row);
			//检查该工作项是否为修单
			boolean isMakebill = false;
			if (WorkflownoteVO.WORKITEM_TYPE_MAKEBILL.equalsIgnoreCase(flownote.getActiontype()))
				isMakebill = true;

			switch (col) {
				case 0:
					obj = flownote.getSendername(); //发送人
					break;
				case 1:
					obj = flownote.getSenddate();
					break;
				case 2:
					obj = flownote.getCheckname();//审核人
					break;
				case 3:
					obj = flownote.getDealdate();
					break;
				case 4:
					UFDateTime beginTime = flownote.getSenddate();
					UFDateTime endTime = flownote.getDealdate();
					if (endTime == null) {
						int iStatus = flownote.getApprovestatus().intValue();
						if (iStatus == WfTaskStatus.Inefficient.getIntValue())
							obj = "";
						else
							obj = DurationUnit.getElapsedTime(new UFDateTime(String.valueOf(beginTime)),
									getCurrServerTime());
					} else {
						obj = DurationUnit.getElapsedTime(new UFDateTime(String.valueOf(beginTime)),
								new UFDateTime(String.valueOf(endTime)));
					}
					break;
				case 5:
					//状态
					int status = flownote.getApprovestatus().intValue();
					obj = WFTask.resolveApproveStatus(status, isMakebill);
					break;
				case 6:
					//审核意见
					Object result = flownote.getApproveresult();
					obj = WFTask.resolveApproveResult(isMakebill ? null : result);
					break;
				case 7:
					obj = flownote.getChecknote();
					break;
				case 8:
					//解析多语化的消息
					obj = MessageVO.getMessageNoteAfterI18N(flownote.getMessagenote());
					break;
				case 9:
					obj = flownote.getMoney();
					break;
				case 10:
					obj = flownote.getLocalMoney();
					break;
				case 11:
					obj = flownote.getAssMoney();
					break;
				case 12:
					obj = flownote.getPremoney();
					break;
				case 13:
					obj = flownote.getPrelocalMoney();
					break;
				case 14:
					obj = flownote.getPreassMoney();
					break;
				case 15:
//					obj ="这个问题立刻解决";
					obj =ZmPubTool.getDepNameByPk(ZmPubTool.getDeptidByPsnmanbasid(ZmPubTool.getSaleridByOperatorid(flownote.getSenderman())));
					break;
				case 16:
					String caozuoyuan=ZmPubTool.getSaleridByOperatorid(flownote.getCheckman());
					String chenckdeptPK=
							ZmPubTool.getDeptidByPsnmanbasid(caozuoyuan);
					String dept=ZmPubTool.getDepNameByPk(chenckdeptPK);
					obj=dept;
					break;

			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return obj;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Object obj, int row, int col) {
		//Noop!
	}
}