package nc.ui.pub.workflownote;

import java.util.HashMap;

import nc.ui.ml.NCLangRes;

/**
 * 工作流、审批流的工作项列信息
 * 
 * @author leijun 2008-9
 * @since 5.5
 * 修改      杨涛  增加发送人部门和审批人部门
 * 修改日期:2013-10-23
 * 
 */
public class WorkitemColumnInfo {
	public static final int PK_INT = 0;

	public static final int BILLID_INT = 1;

	public static final int BILLTYPE_INT = 2;

	public static final int BUSITYPE_INT = 3;

	public static final int CORP_INT = 4;
	
	public static final int SENDDEPT_INT=30;
	
	public static final int CHENCKDEPT_INT=31;

	public static final int CURRTYPE_INT = 5;

	public static final int SENDER_INT = 6;

	public static final int SENDDATE_INT = 7;

	public static final int CHECKER_INT = 8;

	public static final int CHECKDATE_INT = 9;

	public static final int DURATION_INT = 10;

	public static final int APPROVESTATUS_INT = 11;

	public static final int APPROVERESULT_INT = 12;

	public static final int APPROVENOTE_INT = 13;

	public static final int TITLE_INT = 14;

	public static final int MONEY_INT = 15;

	public static final int LOCALMONEY_INT = 16;

	public static final int ASSMONEY_INT = 17;

	public static final int MONEY_BEFORE_INT = 18;

	public static final int LOCALMONEY_BEFORE_INT = 19;

	public static final int ASSMONEY_BEFORE_INT = 20;

	public static final int BILLNO_INT = 21;

	public static final int DEALMAN_INT = 22;

	public static final int DEALDATE_INT = 23;

	public static final int DEALSTATUS_INT = 24;

	public static final WorkitemColumnInfo PK = new WorkitemColumnInfo(PK_INT);

	public static final WorkitemColumnInfo BILLID = new WorkitemColumnInfo(BILLID_INT);

	public static final WorkitemColumnInfo BILLTYPE = new WorkitemColumnInfo(BILLTYPE_INT);

	public static final WorkitemColumnInfo BUSITYPE = new WorkitemColumnInfo(BUSITYPE_INT);

	public static final WorkitemColumnInfo CORP = new WorkitemColumnInfo(CORP_INT);//公司
	
	public static final WorkitemColumnInfo SENDDEPT= new WorkitemColumnInfo(SENDDEPT_INT);
	
	public static final WorkitemColumnInfo CHENCKDEPT= new WorkitemColumnInfo(CHENCKDEPT_INT);

	public static final WorkitemColumnInfo CURRTYPE = new WorkitemColumnInfo(CURRTYPE_INT);

	public static final WorkitemColumnInfo SENDER = new WorkitemColumnInfo(SENDER_INT);

	public static final WorkitemColumnInfo SENDDATE = new WorkitemColumnInfo(SENDDATE_INT);

	public static final WorkitemColumnInfo CHECKER = new WorkitemColumnInfo(CHECKER_INT);

	public static final WorkitemColumnInfo CHECKDATE = new WorkitemColumnInfo(CHECKDATE_INT);

	public static final WorkitemColumnInfo DURATION = new WorkitemColumnInfo(DURATION_INT);

	public static final WorkitemColumnInfo APPROVESTATUS = new WorkitemColumnInfo(APPROVESTATUS_INT);

	public static final WorkitemColumnInfo APPROVERESULT = new WorkitemColumnInfo(APPROVERESULT_INT);

	public static final WorkitemColumnInfo APPROVENOTE = new WorkitemColumnInfo(APPROVENOTE_INT);

	public static final WorkitemColumnInfo TITLE = new WorkitemColumnInfo(TITLE_INT);

	public static final WorkitemColumnInfo MONEY = new WorkitemColumnInfo(MONEY_INT);

	public static final WorkitemColumnInfo LOCALMONEY = new WorkitemColumnInfo(LOCALMONEY_INT);

	public static final WorkitemColumnInfo ASSMONEY = new WorkitemColumnInfo(ASSMONEY_INT);

	public static final WorkitemColumnInfo MONEY_BEFORE = new WorkitemColumnInfo(MONEY_BEFORE_INT);

	public static final WorkitemColumnInfo LOCALMONEY_BEFORE = new WorkitemColumnInfo(
			LOCALMONEY_BEFORE_INT);

	public static final WorkitemColumnInfo ASSMONEY_BEFORE = new WorkitemColumnInfo(
			ASSMONEY_BEFORE_INT);

	public static final WorkitemColumnInfo BILLNO = new WorkitemColumnInfo(BILLNO_INT);

	public static final WorkitemColumnInfo DEALMAN = new WorkitemColumnInfo(DEALMAN_INT);

	public static final WorkitemColumnInfo DEALDATE = new WorkitemColumnInfo(DEALDATE_INT);

	public static final WorkitemColumnInfo DEALSTATUS = new WorkitemColumnInfo(DEALSTATUS_INT);

	public static final String[] TAGS = { "pk_checkflow", "billid", "pk_billtype", "pk_businesstype",
			"pk_corp", "currency", "senderman", "senddate", "checkman", "dealdate", "duration",
			"approvestatus", "approveresult", "checknote", "messagenote", "money", "localmoney",
			"assmoney", "premoney", "prelocalmoney", "preassmoney", "billno", "dealman", "dealdate",
			"dealstatus" ,"senddept","chenckdept"};

	public static final WorkitemColumnInfo[] VALUES = { PK, BILLID, BILLTYPE, BUSITYPE, CORP,
			CURRTYPE, SENDER, SENDDATE, CHECKER, CHECKDATE, DURATION, APPROVESTATUS, APPROVERESULT,
			APPROVENOTE, TITLE, MONEY, LOCALMONEY, ASSMONEY, MONEY_BEFORE, LOCALMONEY_BEFORE,
			ASSMONEY_BEFORE, BILLNO, DEALMAN, DEALDATE, DEALSTATUS ,SENDDEPT,CHENCKDEPT};

	private static final HashMap<String, WorkitemColumnInfo> tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	/**
	 * 构造对象
	 * @param value The value
	 */
	private WorkitemColumnInfo(int value) {
		_value = value;
	}

	/**
	 * 根据字串来构造对象，字串必取值于TAGS数组中的某值。
	 *
	 * @param tag The String
	 * @return The WorkitemColumnInfo object
	 */
	public static WorkitemColumnInfo fromString(String tag) {
		WorkitemColumnInfo race = (WorkitemColumnInfo) tagMap.get(tag);
		if (race == null && tag != null)
			throw new IllegalArgumentException(tag);
		return race;
	}

	public String toString() {
		switch (_value) {
			case PK_INT:
				return "工作项主键";
			case BILLTYPE_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0000807")/*@res "单据类型"*/;
			case BUSITYPE_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC001-0000003")/*@res "业务类型"*/;
			case CORP_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0000404")/*@res "公司"*/;
			case CURRTYPE_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0001755")/*@res "币种"*/;
			case SENDER_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000202")/* "发送人" */;
			case SENDDATE_INT:
				return NCLangRes.getInstance().getStrByID("101206", "UPP101206-000019")/*@res "发送日期"*/;
			case CHECKER_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000203")/*@res "审批人"*/;
			case CHECKDATE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000204")/*@res "审批日期"*/;
			case DURATION_INT:
				return NCLangRes.getInstance().getStrByID("101206", "UPP101206-000046") /*@res "历时"*/;
			case APPROVESTATUS_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000191")/*@res "审批状况"*/;
			case APPROVERESULT_INT:
				return NCLangRes.getInstance().getStrByID("102220", "UPP102220-000194")/*@res "审批意见"*/;
			case APPROVENOTE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000205")/*@res "批语"*/;
			case TITLE_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0003660")/*@res "说明"*/;
			case MONEY_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0000926")/*@res "原币金额"*/;
			case LOCALMONEY_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0002615")/*@res "本币金额"*/;
			case ASSMONEY_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0003969") /*@res "辅币金额"*/;
			case MONEY_BEFORE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000632")/*@res "送审原币金额"*/;
			case LOCALMONEY_BEFORE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000633")/*@res "送审本币金额"*/;
			case ASSMONEY_BEFORE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000634") /*@res "送审辅币金额"*/;
			case BILLNO_INT:
				return NCLangRes.getInstance().getStrByID("101206", "UPP101206-000005")/*@res "单据编号"*/;
			case DEALMAN_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000007")/*处理人*/;
			case DEALDATE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000430")/*处理日期*/;
			case DEALSTATUS_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000010")/*处理状况*/;
			case SENDDEPT_INT:
				return  "发送部门";
			case CHENCKDEPT_INT:
				return  "审核部门";
			 
			
			default:
				return "ERROR";
		}
	}

	/**
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value];
	}
}
