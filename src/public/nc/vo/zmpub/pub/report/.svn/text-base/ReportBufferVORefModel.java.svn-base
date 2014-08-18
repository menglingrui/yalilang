package nc.vo.zmpub.pub.report;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 报表参照
 * @author ddk
 *
 */
public class ReportBufferVORefModel extends AbstractRefModel{

	// 参照标题
	private String m_sRefTitle = "报表参照";

	private String tablename = "zm_config";

	private String[] fieldcode = { "nodecode", "detail" };

	private String[] fieldname = { "功能节点号", "描述" };

	private String strWhere = " isnull(dr,0)=0 ";

	private String[] hidecode = { "pk_config"};

	private int defaultFieldCount = 2;

	/**
	 * RouteRefModel 构造子注解。
	 */
	public ReportBufferVORefModel() {
		super();
	}

	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return defaultFieldCount;
	}

	/**
	 * 显示字段列表 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return fieldcode;
	}

	/**
	 * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return fieldname;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return hidecode;
	}

	/**
	 * 主键字段名
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_config";
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}

	@Override
	public String getWherePart() {

		return strWhere;
	}

	/**
	 * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {

		return tablename;
	}

	@Override
	public boolean isCacheEnabled() {

		return false;
	}

}
