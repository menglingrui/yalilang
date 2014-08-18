package nc.vo.pub.workflownote;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * 审批流任务项VO对象,与pub_workflownote表对应
 * 
 * @author 樊冠军 2002-6-19
 * @modifier 雷军 2005-3-25 NC31该表增加了几个字段
 * @modifier 雷军 2005-10-20 转换为SuperVO
 */
public class WorkflownoteVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 工作项类型-审批
	 */
	public static final String WORKITEM_TYPE_APPROVE = "Z";

	/**
	 * 工作项类型-制单
	 */
	public static final String WORKITEM_TYPE_MAKEBILL = "MAKEBILL";

	/**
	 * 工作项类型-业务消息
	 */
	public static final String WORKITEM_TYPE_BIZ = "BIZ";

	/**
	 * 工作项改派后缀标记
	 */
	public static final String WORKITEM_APPOINT_SUFFIX = "_D";

	/**
	 * 业务消息使用pk_billtype表示节点号的前缀标记
	 */
	public static final String BIZ_NODE_PREFIX = "[F]";

	/** 主键 */
	public String pk_checkflow;

	/** 公司主键 */
	public String pk_corp;
	 

	/** 动作代码，扩展代码 */
	public String actiontype;

	/** 单据主键 */
	public String billid;

	/** 单据号 */
	public String billno;

	/** 单据类型主键 */
	public String pk_billtype;

	/** 业务类型主键 */
	public String pk_businesstype;

	/** 批语 */
	public String checknote;

	/** 发送时间 */
	public UFDateTime senddate;

	/** 处理时间 */
	public UFDateTime dealdate;

	/** 是否审批 */
	public String ischeck;

	/** 消息内容 */
	public String messagenote;

	/** 接收删除标记 */
	public UFBoolean receivedeleteflag;

	/** 发送人PK */
	public String senderman;

	/** 审核人PK */
	public String checkman;
	

	/** 金额信息 */
	public String currency = null;

	public UFDouble localMoney;

	public UFDouble assMoney;

	public UFDouble money;

	public UFDouble prelocalMoney;

	public UFDouble preassMoney;

	public UFDouble premoney;

	/** 任务项的状态结果信息 lj+2005-4-4 */
	public String approveresult = null;

	public Integer approvestatus;

	public Integer priority = new Integer(0); // 消息优先级

	public String pk_wf_task;

	public UFDateTime ts;

	public Integer dr;

	//用户对象
	private String userobject;

	//工作流类型
	private String workflow_type;

	// //////////非数据库字段////////////////
	/** 发送人名称 */
	public String sendername = null;

	/** 审核人名称 */
	public String checkname = null;
	
	/**
	 * 发送人部门 yangtao
	 *@date 2013年10月23日下午3:26:28
	 */
	public String sendDept;
	/**
	 * 审核人部门   yangtao
	 *@date 2013年10月23日下午3:28:12
	 */
	public String chenckDept;
	
	
	
	public WorkflownoteVO() {
	}

	public WorkflownoteVO(String newPk_checkflow) {
		// 为主键字段赋值:
		pk_checkflow = newPk_checkflow;
	}

	public String getWorkflow_type() {
		return workflow_type;
	}

	public void setWorkflow_type(String workflow_type) {
		this.workflow_type = workflow_type;
	}

	public String getUserobject() {
		return userobject;
	}

	public void setUserobject(String userobject) {
		this.userobject = userobject;
	}

	public String getPk_wf_task() {
		return pk_wf_task;
	}

	public void setPk_wf_task(String pk_wf_task) {
		this.pk_wf_task = pk_wf_task;
	}

	public UFDouble getPreassMoney() {
		return preassMoney;
	}

	public void setPreassMoney(UFDouble preassMoney) {
		this.preassMoney = preassMoney;
	}

	public UFDouble getPrelocalMoney() {
		return prelocalMoney;
	}

	public void setPrelocalMoney(UFDouble prelocalMoney) {
		this.prelocalMoney = prelocalMoney;
	}

	public UFDouble getPremoney() {
		return premoney;
	}

	public void setPremoney(UFDouble premoney) {
		this.premoney = premoney;
	}

	/**
	 * 根类Object的方法,克隆这个VO对象。 创建日期：(2002-6-19)
	 */
	public Object clone() {

		// 复制基类内容并创建新的VO对象：
		Object o = null;
		try {
			o = super.clone();
		} catch (Exception e) {
		}
		WorkflownoteVO workflownote = (WorkflownoteVO) o;

		// 你在下面复制本VO对象的所有属性：
		
		return workflownote;
	}

	public String getApproveresult() {
		return approveresult;
	}

	public void setApproveresult(String approveresult) {
		this.approveresult = approveresult;
	}

	public Integer getApprovestatus() {
		return approvestatus;
	}

	public void setApprovestatus(Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 属性m_actiontype的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getActiontype() {
		return actiontype;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-17 15:58:16)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getAssMoney() {
		return assMoney;
	}

	/**
	 * 属性m_billid的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getBillid() {
		return billid;
	}

	/**
	 * 属性m_billno的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getBillno() {
		return billno;
	}

	/**
	 * 属性m_checkman的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getCheckman() {
		return checkman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:23:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCheckname() {
		return checkname;
	}

	/**
	 * 属性m_checknote的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getChecknote() {
		return checknote;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-18 11:05:42)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCurrency() {
		return currency;
	}

	/**
	 * 属性m_dealdate的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getDealdate() {
		return dealdate;
	}

	/**
	 * 属性m_dr的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 返回数值对象的显示名称。 创建日期：(2002-6-19)
	 * 
	 * @return java.lang.String 返回数值对象的显示名称。
	 */
	public String getEntityName() {

		return "Workflownote";
	}

	/**
	 * 属性m_ischeck的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getIscheck() {
		return ischeck;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-17 15:57:55)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocalMoney() {
		return localMoney;
	}

	/**
	 * 属性m_messagenote的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getMessagenote() {
		return messagenote;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-17 15:57:29)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getMoney() {
		return money;
	}

	/**
	 * 属性m_pk_billtype的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * 属性m_pk_businesstype的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_businesstype() {
		return pk_businesstype;
	}

	/**
	 * 属性m_pk_checkflow的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_checkflow() {
		return pk_checkflow;
	}

	/**
	 * 属性m_pk_corp的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 返回对象标识，用来唯一定位对象。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_checkflow;
	}

	/**
	 * 属性m_receivedeleteflag的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReceivedeleteflag() {
		return receivedeleteflag;
	}

	/**
	 * 属性m_senddate的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getSenddate() {
		return senddate;
	}

	/**
	 * 属性m_senderman的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getSenderman() {
		return senderman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:22:49)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSendername() {
		return sendername;
	}

	/**
	 * 属性m_ts的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性m_actiontype的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_actiontype String
	 */
	public void setActiontype(String newActiontype) {

		actiontype = newActiontype;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-17 15:58:16)
	 * 
	 * @param newAssMoney nc.vo.pub.lang.UFDouble
	 */
	public void setAssMoney(nc.vo.pub.lang.UFDouble newAssMoney) {
		assMoney = newAssMoney;
	}

	/**
	 * 属性m_billid的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_billid String
	 */
	public void setBillid(String newBillid) {

		billid = newBillid;
	}

	/**
	 * 属性m_billno的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_billno String
	 */
	public void setBillno(String newBillno) {

		billno = newBillno;
	}

	/**
	 * 属性m_checkman的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_checkman String
	 */
	public void setCheckman(String newCheckman) {

		checkman = newCheckman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:23:02)
	 * 
	 * @param newCheckName java.lang.String
	 */
	public void setCheckname(java.lang.String newCheckName) {
		checkname = newCheckName;
	}

	/**
	 * 属性m_checknote的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_checknote String
	 */
	public void setChecknote(String newChecknote) {

		checknote = newChecknote;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-18 11:05:42)
	 * 
	 * @param newCurrency java.lang.String
	 */
	public void setCurrency(java.lang.String newCurrency) {
		currency = newCurrency;
	}

	/**
	 * 属性m_dealdate的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_dealdate UFDateTime
	 */
	public void setDealdate(UFDateTime newDealdate) {

		dealdate = newDealdate;
	}

	/**
	 * 属性m_dr的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_dr Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性m_ischeck的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_ischeck String
	 */
	public void setIscheck(String newIscheck) {

		ischeck = newIscheck;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-17 15:57:55)
	 * 
	 * @param newLocalMoney nc.vo.pub.lang.UFDouble
	 */
	public void setLocalMoney(nc.vo.pub.lang.UFDouble newLocalMoney) {
		localMoney = newLocalMoney;
	}

	/**
	 * 属性m_messagenote的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_messagenote String
	 */
	public void setMessagenote(String newMessagenote) {

		messagenote = newMessagenote;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-17 15:57:29)
	 * 
	 * @param newMoney nc.vo.pub.lang.UFDouble
	 */
	public void setMoney(nc.vo.pub.lang.UFDouble newMoney) {
		money = newMoney;
	}

	/**
	 * 属性m_pk_billtype的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_billtype String
	 */
	public void setPk_billtype(String newPk_billtype) {

		pk_billtype = newPk_billtype;
	}

	/**
	 * 属性m_pk_businesstype的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_businesstype String
	 */
	public void setPk_businesstype(String newPk_businesstype) {

		pk_businesstype = newPk_businesstype;
	}

	/**
	 * 属性m_pk_checkflow的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_checkflow String
	 */
	public void setPk_checkflow(String newPk_checkflow) {

		pk_checkflow = newPk_checkflow;
	}

	/**
	 * 属性m_pk_corp的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_corp String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * 设置对象标识，用来唯一定位对象。 创建日期：(2002-6-19)
	 * 
	 * @param pk_checkflow String
	 */
	public void setPrimaryKey(String newPk_checkflow) {

		pk_checkflow = newPk_checkflow;
	}

	/**
	 * 属性m_receivedeleteflag的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_receivedeleteflag UFBoolean
	 */
	public void setReceivedeleteflag(UFBoolean newReceivedeleteflag) {

		receivedeleteflag = newReceivedeleteflag;
	}

	/**
	 * 属性m_senddate的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_senddate UFDateTime
	 */
	public void setSenddate(UFDateTime newSenddate) {

		senddate = newSenddate;
	}

	/**
	 * 属性m_senderman的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_senderman String
	 */
	public void setSenderman(String newSenderman) {

		senderman = newSenderman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:22:49)
	 * 
	 * @param newSenderName java.lang.String
	 */
	public void setSendername(java.lang.String newSenderName) {
		sendername = newSenderName;
	}

	/**
	 * 属性m_ts的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_ts UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	public String getParentPKFieldName() {
		return null;
	}

	public String getPKFieldName() {
		return "pk_checkflow";
	}

	public String getTableName() {
		return "pub_workflownote";
	}

	public String getSendDept() {
		return sendDept;
	}

	public void setSendDept(String sendDept) {
		this.sendDept = sendDept;
	}

	public String getChenckDept() {
		return chenckDept;
	}

	public void setChenckDept(String chenckDept) {
		this.chenckDept = chenckDept;
	}
	
}