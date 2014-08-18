package nc.bs.zmpub.pub.dao;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ResultSetProcessor;
/**
 * 数据库访问帮助类封装了常用的持久层访问操作
 * 支持预编译批量查询
 * @author mlr
 *
 */
public class ZmPubDAO extends ZmBaseDAO{
	private static ZmPubDAO dao=null;
	
	public static ZmPubDAO getDAO(){
		if(dao==null){
			dao=new ZmPubDAO();
		}
		return dao;
	}
	/**
	 * 根据指定SQL 执行有参数的数据库查询,并返回ResultSetProcessor处理后的对象
	 * 支持预编译批量查询
	 * 此查询目前主要用于西尔维预算接口模块的批量查询处理
	 * @param sql
	 *            查询的SQL
	 * @param parameter
	 *            查询参数
	 * @param processor
	 *            结果集处理器
	 */
	public Object[] executeQuery(String sql, SQLParameter[] parameters, ResultSetProcessor processor,boolean isDebug) throws DAOException {
		PersistenceManager manager = null;
		Object[] values = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			JdbcSession session = manager.getJdbcSession();
			
			if(parameters==null || parameters.length==0){
				throw new DAOException("参数个数不能为空");
			}
			for(int n=0;n<parameters.length;n++){
				if(parameters[n]==null){
					throw new DAOException("查询参数数组[parameters] 索引为["+n+"]的参数为空" );
				}
				if(parameters[n].getParameters()==null || parameters[n].getParameters().size()==0){
					throw new DAOException("查询参数数组[parameters] 索引为["+n+"]的参数中list集合的值为空" );
				}
			}	
			if(sql==null || sql.length()==0){
				throw new DAOException("查询sql语句为空" );
			}
			if(processor==null){
				throw new DAOException("结果集处理对象为空" );
			}
			for(int i=0;i<parameters.length;i++){
				if(session==null){
					throw new DAOException("session为空");
				}
				if(session.getConnection()==null){
					throw new DAOException("Connection是空的");
				}
				if(session.getConnection().isClosed()==true){
					throw new DAOException("Connection关闭");
				}
				values[i] = session.executeQuery(sql, parameters[i], processor);
			}	
			if(isDebug==true){
				exeDebug(sql, parameters);
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage()+parameters[0].getCountParams());}
		 catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage()+parameters[0].getCountParams());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}
	/**
	 * 根据指定SQL 执行有参数的数据库查询,并返回ResultSetProcessor处理后的对象
	 * 
	 * @param sql
	 *            查询的SQL
	 * @param parameter
	 *            查询参数
	 * @param processor
	 *            结果集处理器
	 */
	public Object executeQuery(String sql, SQLParameter parameter, ResultSetProcessor processor,boolean isDebug) throws DAOException {
		if(isDebug){
			exeDebug(sql, parameter);
		}
		return super.executeQuery(sql, processor);
	}
	/**
	 * 给sql语句赋值便于调试
	 * @param sql
	 * @param s
	 */
	public void exeDebug(String sql, SQLParameter[] parameters) {
	    for(int i=0;i<parameters.length;i++){
	    	exeDebug(sql, parameters[i]); 	
	    }
	}
	/**
	 * 给sql语句赋值便于调试
	 * @param sql
	 * @param s
	 */
	public String exeDebug(String sql, SQLParameter parameter) {
		String sqldug=sql;
		List list=parameter.getParameters();
		for(int i=0;i<list.size();i++){
			Object value=list.get(i);
			sqldug=replace(sqldug,value);
		}
		System.out.println(sqldug);
		return sqldug;		
	}
	/**
	 * 将sql语句中的？用value替代
	 * @param sqldug
	 * @param value
	 * @return
	 */
	public String replace(String sqldug, Object value) {
		Object realvalue=null;
		if(isChar(value)){
		   realvalue="'" + value+"'";
		}else{
			realvalue=value;	
		}
		sqldug.replaceFirst("\\?", realvalue+"");		
		return sqldug;
	}
	/**
	 * 判断值是否是字符
	 * @param value
	 * @return
	 */
	public boolean isChar(Object value) {
		if(value ==null){
			return false;
		}	
		if(value instanceof String){
			return true;
		}
		return false;
	}
		
	/**
	 * 根据指定SQL 执行有参数的数据库更新操作
	 * 支持批量执行sql语句
	 * @param sql
	 *            更新的sql
	 * @param parameter
	 *            更新参数
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql, SQLParameter[] parameters,boolean isDebug) throws DAOException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			if(parameters==null || parameters.length==0){
				throw new DAOException("参数不能 为空");
			}
            for(int i=0;i<parameters.length;i++){
            	session.addBatch(sql,parameters[i]);
            }
            if(isDebug){
            	exeDebug(sql, parameters);
            }
            value=session.executeBatch();
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}
		
}
