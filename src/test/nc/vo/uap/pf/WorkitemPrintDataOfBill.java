package nc.vo.uap.pf;

import java.util.HashMap;

import nc.ui.pub.print.IDataSource;

/**
 * ����������Ϣ�Ĵ�ӡ����Դ
 * 
 * @author leijun 2007-8-23
 * @modifier leijun 2008-8 ����һ������Դ����pk_checkman
 * @modifier yangtao 2013-10-23 ������һ������Դ���� pk_sendman
 * 
 */
public class WorkitemPrintDataOfBill implements IDataSource {

	/**
	 * @date 2013��10��23������2:28:15
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��ӡ����Դ����
	 */
	public static final String DATAITEM_BILLTYPE = "billtype";

	public static final String DATAITEM_BUSITYPE = "busitype";

	public static final String DATAITEM_BILLNO = "billno";

	public static final String DATAITEM_CORP = "corp";

	public static final String DATAITEM_CURRTYPE = "currtype";

	public static final String DATAITEM_SENDDATE = "senddate";

	public static final String DATAITEM_SENDMAN = "sendman";

	public static final String DATAITEM_DEALDATE = "dealdate";

	public static final String DATAITEM_CHECKMAN = "checkman";
	
	public static final String DATAITEM_PK_CHECKMAN = "pk_checkman";
	/**
	 * ����������  yangtao
	 */
	public static final String DATAITEM_PK_SENDCKMAN ="pk_sendman";

	public static final String DATAITEM_NOTE = "note";

	public static final String DATAITEM_MONEY = "money";

	public static final String DATAITEM_LOCALMONEY = "localmoney";

	public static final String DATAITEM_ASSMONEY = "assmoney";

	public static final String DATAITEM_APPROVERESULT = "approveresult";
	
	public static final String DATAITEM_DURATION = "duration";
	
	public static  final String DATAITEM_SENDEPT="senddept";
	
	public  static final String DARAITEM_CHENCKDETE="chenckdept";

	/**
	 * ��ӡ���ݣ�����-����ֵ
	 */
	HashMap<String, String[]> m_hmDatas = new HashMap<String, String[]>();

	public WorkitemPrintDataOfBill(HashMap<String, String[]> hmDatas) {
		super();
		m_hmDatas = hmDatas;
	}

	public String[] getAllDataItemExpress() {
		return new String[] { DATAITEM_BILLTYPE, DATAITEM_BUSITYPE, DATAITEM_BILLNO, DATAITEM_CORP,
				DATAITEM_CURRTYPE, DATAITEM_SENDDATE, DATAITEM_SENDMAN, DATAITEM_DEALDATE,
				DATAITEM_CHECKMAN, DATAITEM_NOTE, DATAITEM_MONEY, DATAITEM_LOCALMONEY, DATAITEM_ASSMONEY,
				DATAITEM_APPROVERESULT, DATAITEM_DURATION ,DATAITEM_SENDEPT,DARAITEM_CHENCKDETE,DATAITEM_PK_SENDCKMAN};
	}

	public String[] getAllDataItemNames() {
		return getAllDataItemExpress();
	}

	public String[] getDependentItemExpressByExpress(String itemExpress) {
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		return m_hmDatas.get(itemExpress);
	}

	public String getModuleName() {
		return null;
	}

	public boolean isNumber(String itemExpress) {
		if (itemExpress.equals("money"))
			return true;
		if (itemExpress.equals("localmoney"))
			return true;
		if (itemExpress.equals("assmoney"))
			return true;
		return false;
	}

}
