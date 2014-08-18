package nc.bs.zmpub.pub.report;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 报表查询处理类，封装了对数据库的操作
 * @author mlr
 */
public class ReportDMO  extends DataManageObject{

    public ReportDMO() throws javax.naming.NamingException, SystemException {
        super();
    }

    public ReportDMO(String dbName) throws javax.naming.NamingException,
            SystemException {
        super(dbName);
    }
   /**
    * 报表批量查询类
    * @作者：mlr
    * @说明：完达山物流项目       
    * @时间：2011-7-12下午12:48:26
    * @param sqls
    * @return
    * @throws SQLException
    */
    public List<ReportBaseVO[]> queryVOBySql(String[] sqls)throws SQLException{
    	List<ReportBaseVO[]> list=new ArrayList<ReportBaseVO[]>();
    	if(sqls==null ||sqls.length==0){
    		throw new SQLException("查询语句不能为空");
    	}
    	int size=sqls.length;
    	for(int i=0;i<size;i++){
    		ReportBaseVO[] vos=queryVOBySql(sqls[i]);
    		list.add(vos);
    	}
		return list; 	
    }
    /**
     * 根据查询语句获得动态报表vo
     * @param sql
     * @return
     * @throws SQLException
     */
    public ReportBaseVO[] queryVOBySql(String sql) throws SQLException {
   // 	sql=" select vbillstatus,vbillno,nreserve3 from xew_proaccept ";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector vResult = new Vector();
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            ReportBaseVO voTmp = null;
            while (rs.next()) {
                voTmp = new ReportBaseVO();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    Object value = null;
                    if (rsmd.getColumnType(i) == Types.NUMERIC
                            || rsmd.getColumnType(i) == Types.DECIMAL
                            || rsmd.getColumnType(i) == Types.DOUBLE
                            || rsmd.getColumnType(i) == Types.FLOAT) {
                        Object o = rs.getObject(i);
                        value = (o == null ? new UFDouble("0") : new UFDouble(o.toString()));
                    } else if (rsmd.getColumnType(i) == Types.INTEGER
                            || rsmd.getColumnType(i) == Types.SMALLINT) {
                        value = new Integer(rs.getInt(i));
                    } else if (rsmd.getColumnType(i) == Types.DATE) {
                        value = new UFDate(rs.getDate(i));
                    } else
                        value = rs.getString(i);
                    voTmp.setAttributeValue(rsmd.getColumnName(i), value);
                }
                vResult.add(voTmp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs = null;
            } catch (SQLException ex) {
                throw ex;
            }
        }
        ReportBaseVO[] vos = new ReportBaseVO[vResult.size()];
        vResult.copyInto(vos);
        return vos;
    }
    /**
     * 根据查询语句获得动态报表vo
     * 与queryVOBySql的区别是，当查询的值为零时，动态vo设置的值为0
     * 而queryVOBySql，当查询的值为零时，动态vo设置的值为0.00000000
     * @param sql
     * @return
     * @throws SQLException
     */
    public ReportBaseVO[] queryVOBySql1(String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector vResult = new Vector();
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            ReportBaseVO voTmp = null;
            while (rs.next()) {
                voTmp = new ReportBaseVO();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    Object value = null;
                    if (rsmd.getColumnType(i) == Types.NUMERIC
                            || rsmd.getColumnType(i) == Types.DECIMAL
                            || rsmd.getColumnType(i) == Types.DOUBLE
                            || rsmd.getColumnType(i) == Types.FLOAT) {
                        Object o = rs.getObject(i);
                        //如果是Inger类型直接返回零
                        if(o instanceof Integer){
                        	value=o;
                        }else
                        value = (o == null ? new UFDouble("0") : new UFDouble(o.toString()));
                    } else if (rsmd.getColumnType(i) == Types.INTEGER
                            || rsmd.getColumnType(i) == Types.SMALLINT) {
                        value = new Integer(rs.getInt(i));
                    } else if (rsmd.getColumnType(i) == Types.DATE) {
                        value = new UFDate(rs.getDate(i));
                    } else
                        value = rs.getString(i);
                    voTmp.setAttributeValue(rsmd.getColumnName(i), value);
                }
                vResult.add(voTmp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs = null;
            } catch (SQLException ex) {
                throw ex;
            }
        }
        ReportBaseVO[] vos = new ReportBaseVO[vResult.size()];
        vResult.copyInto(vos);
        return vos;
    }
}
