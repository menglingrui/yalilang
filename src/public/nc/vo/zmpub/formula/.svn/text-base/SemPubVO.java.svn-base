package nc.vo.zmpub.formula;

//VO
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pupl.pub.VariableConst;

/**
 * 类：用于VO保存前的必需检查，如所有浮点型的位数检查
 * 作者：王印芬
 * 功能：
 * 日期：(2002-7-11 15:10:00)
 * 修改日期，修改人，修改原因，注释标志：
 * 2004-04-06,晁志平，效率优化，增加创建临时表的最小ID数
 */
@SuppressWarnings("all")
public class SemPubVO {
  //================异常参数
  //一些参数
  //public  final static  int   SYSTEM_ERROR = 0 ;//系统故障
  //public  final static  int   SUCCESS = 1 ;//成功
  //public  final static  int   CONCURRENT = 2 ;//并发
  //提示框提示信息
  public  static  String  MESSAGE_SYSTEM_ERROR ;//= "系统故障！" ;
  public  static  String  MESSAGE_CONCURRENT ;//= "存在并发操作，请刷新！" ;
  static{
      MESSAGE_SYSTEM_ERROR = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON","UPPSCMCommon-000428")/*@res "系统故障！"*/;
      MESSAGE_CONCURRENT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON","UPPSCMCommon-000058")/*@res "存在并发操作，请刷新！"*/;
  }

  public  static  boolean DEBUG = true ;

  /*
  * 采购管理采用创建临时表思路处理主键条件查询的最小ID数
  * 2004-05-13,统一走公共DMO类
  */
  //public  final   static  int  _MINNUM_CRT_TMP_TBL_ = 10;
  
  /**
   * <p>作者：晁志平
   * <p>功能：根据一个对象的值得到UFDate的值，如果为空，返回空
   * <p>参数：Object value     对象值
   * <p>返回：UFDateTime       UFDateTime的值
   * <p>例外：无
   * <p>日期：(2005-05-17 10:39:21)
   * <p>修改日期，修改人，修改原因，注释标志：
   */
  public static UFDateTime getUFDateTime(Object value) {
    if ( value == null || value.toString().trim().equals("") ){
      return  null ;
    }else if(value  instanceof  UFDate){
      return  (UFDateTime)value ;
    }else{
      return  new UFDateTime(value.toString().trim());
    }
  } 
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-7-11 15:25:46)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 */
public SemPubVO() {}
/**
 * 作者：王印芬
 * 功能：检查是否VO中的浮点型存在超出12＋8的可能
 * 参数：AggregatedValueObject   voCheck   需检查的VO
 *    String[]      saHeadCheckItem   需检查的VO中的头需检查的对象
 *    String[]      saBodyCheckItem   需检查的VO中的体需检查的对象
 * 返回：
 * 例外：如果存在超出标准的数，则抛出异常，使用者需捕捉异常
 * 日期：(2002-7-11 8:55:48)
 * 修改日期，修改人，修改原因，注释标志：
 */
public  static  void checkDigit_12_8_BeforeSave(
  AggregatedValueObject   voCheck,
  String[]      saHeadCheckItem,
  String[]      saBodyCheckItem)
throws  BusinessException
{
  //参数正确性检查

  //
  CircularlyAccessibleValueObject   voHead = voCheck.getParentVO() ;
  CircularlyAccessibleValueObject[] voaBody = voCheck.getChildrenVO() ;

  //头
  int   iLen = 0 ;
  if (saHeadCheckItem!=null) {
    iLen = saHeadCheckItem.length ;
    for (int i = 0; i < iLen; i++){
      Object  oValue = voHead.getAttributeValue( saHeadCheckItem[i] ) ;
      if ( oValue!=null &&
         !oValue.toString().trim().equals("") ) {
        UFDouble  dValue = new  UFDouble(oValue.toString()) ;
        if ( dValue.compareTo(VariableConst.MIN_13DIGIT_NUM)>0 ){
          throw new BusinessException("数量、单价、金额等存在超出小数点前12位的数据（包括原币、本币及辅币），请检查！") ;
        }
      }
    }
  }

  //体
  if (saBodyCheckItem!=null) {
    iLen = saBodyCheckItem.length ;
    int   iBodyLen = voaBody.length ;
    for (int i = 0; i < iBodyLen; i++){
      for (int  j=0;j<iLen;j++) {
        Object  oValue = voaBody[i].getAttributeValue( saBodyCheckItem[j] ) ;
        if ( oValue!=null &&
           !oValue.toString().trim().equals("") ) {
          UFDouble  dValue = new  UFDouble(oValue.toString()) ;
          if ( dValue.compareTo(VariableConst.MIN_13DIGIT_NUM)>0 ){
            throw new BusinessException("数量、单价、金额等存在超出小数点前12位的数据（包括原币、本币及辅币），请检查！") ;
          }
        }
      }
    }
  }
}
/**
 *  功能说明：根据精度小数位数返回精度值：如 0.01、0.001 等
 *
 *  创建日期：2004-03-18
 *
 *  创建者：晁志平
 */
public static UFDouble getDigitVal(int iDigitNum) throws java.rmi.RemoteException {
  UFDouble dRet = new UFDouble(1.0);
  UFDouble dMul = new UFDouble(0.1);
  //
  for (int i = 0; i < iDigitNum; i++) {
    dRet = dRet.multiply(dMul, iDigitNum);
  }
  //
  return dRet;
}
/**
 * 作者：WYF
 * 功能：从一组值中得到不重复的值数组，一般用于如对于ID数组的过滤，滤掉重复值
 * 参数：String[]  saOrgValue      用于过滤的值数组
 * 返回：String[]            过滤后的数组
 *                    其中所有元素均不为空
 * 例外：无
 * 日期：(2003-11-24 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static String[] getDistinctArray(String[]  saOrgValue) {
  if ( saOrgValue==null  ) {
    return  null ;
  }

  HashMap mapRet = new  HashMap() ;
  int   iLen = saOrgValue.length ;
  for (int i = 0; i < iLen; i++){
    if (saOrgValue[i]!=null) {
      mapRet.put(saOrgValue[i],"") ;
    }
  }

  if (mapRet.size()==0) {
    return  null ;
  }

  return  (String[])mapRet.keySet().toArray(new String[mapRet.size()]) ;
  
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFDouble的值，如果为空，返回零
 * 参数：Object  value     对象值
 * 返回：Integer       Integer的值
 * 例外：无
 * 日期：(2003-09-02 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static Integer getInteger_NullAs(Object  value,Integer iDefaultValue) {
  if ( value == null || value.toString().trim().equals("") ){
    if (iDefaultValue.equals(VariableConst.ZERO_INTEGER)) {
      return  VariableConst.ZERO_INTEGER ;
    }else if (iDefaultValue.equals(VariableConst.ONE_INTEGER)) {
      return  VariableConst.ONE_INTEGER ;
    }else{
      return  iDefaultValue;
    }
  }else if(value  instanceof  Integer){
    return  (Integer)value ;
  }else{
    return  new Integer(value.toString().trim());
  }
}
/**
 * 作者：王印芬
 * 功能：得到一个哈希MAP中不存在的KEY
 *      主要用于LOAD中查询前的KEY过滤
 * 参数：HashMap     mapCheck  用于检查的HashMap
 *    String[]    saKey     用于检查的KEY数组
 * 返回：无
 * 例外：无
 * 日期：(2003-11-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static String[] getNotExistedKeys(
  HashMap     mapCheck,
  String[]    saKey) {
    
  //参数正确性检查
  if (mapCheck== null || saKey==null){
    return  null;
  }

  int   iLen = saKey.length ;
  HashMap mapQueryId = new  HashMap() ;
  for (int i = 0; i < iLen; i++){
    if ( SemPubVO.getString_TrimZeroLenAsNull(saKey[i])!=null &&
       !mapCheck.containsKey(saKey[i]) ) {
      mapQueryId.put(saKey[i],"") ;
    }
  }

  int   iQueryLen = mapQueryId.size() ;
  if (iQueryLen==0) {
    return  null ;
  }
  return  (String[])mapQueryId.keySet().toArray(new String[iQueryLen]) ;}
/**
 * 作者：王印芬
 * 功能：得到一个哈希MAP中不存在的KEY
 *      主要用于LOAD中查询前的KEY过滤
 * 参数：HashMap     htCheck 用于检查的HashMap
 *    String[]    saKey     用于检查的KEY数组
 * 返回：无
 * 例外：无
 * 日期：(2003-11-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static String[] getNotExistedKeys(
  Hashtable   htCheck,
  String[]    saKey) {
    
  //参数正确性检查
  if (htCheck== null || saKey==null){
    return  null;
  }

  int   iLen = saKey.length ;
  HashMap mapQueryId = new  HashMap() ;
  for (int i = 0; i < iLen; i++){
    if ( SemPubVO.getString_TrimZeroLenAsNull(saKey[i])!=null &&
       !htCheck.containsKey(saKey[i]) ) {
      mapQueryId.put(saKey[i],"") ;
    }
  }

  int   iQueryLen = mapQueryId.size() ;
  if (iQueryLen==0) {
    return  null ;
  }
  return  (String[])mapQueryId.keySet().toArray(new String[iQueryLen]) ;}
/**
 * 作者：王印芬
 * 功能：给定一个域及其值数组，返回一个WHERE串，如给定“po_order.corderid”{"1","2","3"}
 *    则返回 (po_order.corderid='1' OR po_order.corderid='2' OR po_order.corderid='3')
 *    主要用于查询中拼SQL条件
 * 参数：String    sFieldName      域名称
 *    Object[]  oaValue       域的值数组
 * 返回：String      拼好的字符串
 * 例外：无
 * 日期：(2003-03-27 8:55:48)
 * 修改日期，修改人，修改原因，注释标志：
 */
public  static  String getORWhereByValues(
  String    sFieldName,
  Object[]  oaValue
  ){
  //参数正确性检查
  if (sFieldName==null || oaValue==null) {
    return  null ;
  }
  //支持权限查询
  if(oaValue.length == 1 && oaValue[0].toString().toLowerCase().indexOf("select") >= 0){
      return sFieldName + " in " + oaValue[0] +  " ";
  }
  StringBuffer  sbufOR = new  StringBuffer() ;
  boolean     bString = ( oaValue[0] instanceof String ) ? true : false;
  String      sOR = " OR " ;
  sbufOR.append("(") ;
  
  int   iLen = oaValue.length ;
  for (int i = 0; i < iLen; i++){
    sbufOR.append(sFieldName) ;
    sbufOR.append("=") ;
    if ( bString ) {
      sbufOR.append("'") ;
    }
    sbufOR.append(oaValue[i]) ;
    if ( bString ) {
      sbufOR.append("'") ;
    }
    sbufOR.append(sOR) ;
  }
  sbufOR.delete( sbufOR.length()-sOR.length(),sbufOR.length()-1 ) ;
  sbufOR.append(")") ;

  return  sbufOR.toString() ;
}

/**
 * 作者：王印芬
 * 功能：给定一个域及其值数组，返回一个WHERE串，如给定“po_order.corderid”{"1","2","3"}
 *    则返回 (po_order.corderid='1' OR po_order.corderid='2' OR po_order.corderid='3')
 *    主要用于查询中拼SQL条件
 * 参数：String    sFieldName      域名称
 *    Object[]  oaValue       域的值数组
 * 返回：String      拼好的字符串
 * 例外：无
 * 日期：(2003-03-27 8:55:48)
 * 修改日期，修改人，修改原因，注释标志：
 */
public  static  String getORWhereByLikeValues(
  String    sFieldName,
  Object[]  oaValue
  ){
  //参数正确性检查
  if (sFieldName==null || oaValue==null) {
    return  null ;
  }
  //支持权限查询
  if(oaValue.length == 1 && oaValue[0].toString().toLowerCase().indexOf("select") >= 0){
      return sFieldName + " in " + oaValue[0] +  " ";
  }
  StringBuffer  sbufOR = new  StringBuffer() ;
  boolean     bString = ( oaValue[0] instanceof String ) ? true : false;
  String      sOR = " OR " ;
  sbufOR.append("(") ;
  
  int   iLen = oaValue.length ;
  for (int i = 0; i < iLen; i++){
    sbufOR.append(sFieldName) ;
    sbufOR.append(" like") ;
    if ( bString ) {
      sbufOR.append("'") ;
    }
    sbufOR.append(oaValue[i]) ;
    if ( bString ) {
      sbufOR.append("'") ;
    }
    sbufOR.append(sOR) ;
  }
  sbufOR.delete( sbufOR.length()-sOR.length(),sbufOR.length()-1 ) ;
  sbufOR.append(")") ;

  return  sbufOR.toString() ;
}

/**
 * 作者：王印芬
 * 功能：给定一个字段数组，返回一个相应的字符串，如给定{"1","2","3"}
 *    则返回 "1,2,3"
 *    主要用于查询中拼SELECT条件
 * 参数：String[]  saField       域数组
 * 返回：String      拼好的字符串
 * 例外：无
 * 日期：(2003-05-30 8:55:48)
 * 修改日期，修改人，修改原因，注释标志：
 */
public  static  String getSelectStringByFields(String[] saField){
  
  return  getSelectStringByFields(null,saField) ;
}
/**
 * 给定一个int数组，返回一个相应的字符串，如给定{1,2,3}  则返回 "1,2,3"
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param saField
 * @return  如果传入参数为空或长度为零，则返回 ""
 * <p>
 * @author czp
 * @time 2007-2-12 下午04:12:28
 */
public  static  String getSelectStringByFields(int[] iaField){
  String strRet = "";
  if(iaField == null || iaField.length == 0){
    return strRet;
  }
  int iLen = iaField.length;
  strRet += iaField[0]; 
  for (int i = 1; i < iLen; i++) {
    strRet += ",";
    strRet += iaField[i];
  }
  return strRet;
}
/**
 * 作者：王印芬
 * 功能：给定一个字段数组，返回一个相应的字符串，如给定{"a"},{"1","2","3"}
 *    则返回 "a.1,a.2,a.3"
 *    主要用于查询中拼SELECT条件
 * 参数：String    sPrefix   前缀，应为表名
 *     String[] saField       域数组
 * 返回：String      拼好的字符串
 * 例外：无
 * 日期：(2003-05-30 8:55:48)
 * 修改日期，修改人，修改原因，注释标志：
 */
public  static  String getSelectStringByFields(
  String    sPrefix, 
  String[]  saField){
  //参数正确性检查
  if (saField==null) {
    return  null ;
  }

  StringBuffer  sbufOR = new  StringBuffer() ;
  
  int   iLen = saField.length ;
  for (int i = 0; i < iLen; i++){
    if (sPrefix!=null) {
      sbufOR.append(sPrefix) ;
      sbufOR.append(".") ;
    }
    sbufOR.append(saField[i]) ;
    sbufOR.append(",") ;
  }
  sbufOR.delete( sbufOR.length()-1,sbufOR.length() ) ;

  return  sbufOR.toString() ;
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到String的值，如果为空串，返回空
 *    该方法主要可用于setAttrobuteValue()
 * 参数：Object  value     对象值
 * 返回：String        String的值
 * 例外：无
 * 日期：(2003-06-05 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static String getString_TrimZeroLenAsNull(Object value) {
  if ( value==null || value.toString().trim().length()==0 ) {
    return  null ;
  }
  return  value.toString() ;
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFBoolean的值，如果为空，返回用户指定的FALSE或TRUE
 * 参数：Object  value     对象值
 * 返回：UFBoolean       UFBoolean的值
 * 例外：无
 * 日期：(2003-09-02 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static UFBoolean getUFBoolean_NullAs(Object  value,UFBoolean bDefaultValue) {
  if ( value == null || value.toString().trim().equals("") ){
    return  bDefaultValue ;
  }else if(value  instanceof  UFBoolean){
    return  (UFBoolean)value ;
  }else{
    return  new UFBoolean(value.toString().trim());
  }
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFDate的值，如果为空，返回空
 * 参数：Object  value     对象值
 * 返回：UFDate        UFDate的值
 * 例外：无
 * 日期：(2003-04-02 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * since v51, 增加是否解析参数方法后重构， by Chaozp on 2007-02-13
 * 
 */
public static UFDate getUFDate(Object value) {
  return getUFDate(value, true);
}
/**
 * 获取日期，支持“是否解析”参量。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param value
 * @param isParse
 * @return
 * <p>
 * @author czp
 * @time 2007-2-13 上午09:12:42
 */
public static UFDate getUFDate(Object value, boolean isParse) {
  if ( value == null || value.toString().trim().equals("") ){
    return  null ;
  }else if(value  instanceof  UFDate){
    return  (UFDate)value ;
  }else{
    return  new UFDate(value.toString().trim(),isParse);
  }
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFDouble的值，如果为空，返回零
 * 参数：Object  value     对象值
 * 返回：UFDouble        UFDouble的值
 * 例外：无
 * 日期：(2003-03-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static UFDouble getUFDouble_NullAsZero(Object  value) {
  if ( value == null || value.toString().trim().equals("") ){
    return  UFDouble.ZERO_DBL ;
  }else if(value  instanceof  UFDouble){
    return  (UFDouble)value ;
  }else if(value  instanceof  BigDecimal){
    return  new UFDouble((BigDecimal)value) ;
  }else{
    return  new UFDouble(value.toString().trim());
  }
}
/**
 * 作者：WYF
 * 功能：根据一个double得到UFDouble的值
 * 参数：double  dValue      值
 * 返回：UFDouble        UFDouble的值
 * 例外：无
 * 日期：(2003-03-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static UFDouble getUFDouble_ValueAsValue(double  dValue) {
  if ( dValue==0 ){
    return  UFDouble.ZERO_DBL ;
  }else{
    return  new UFDouble(dValue) ;
  }
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFDouble的值，空即返回空，零即返回零
 * 参数：Object  value     对象值
 * 返回：UFDouble        UFDouble的值
 * 例外：无
 * 日期：(2003-03-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static UFDouble getUFDouble_ValueAsValue(Object  value) {
  if ( value == null || value.toString().trim().equals("") ){
    return  null ;
  }else if(value  instanceof  UFDouble){
    return  (UFDouble)value ;
  }else if(value  instanceof  BigDecimal){
    return  new UFDouble((BigDecimal)value) ;
  }else{
    return  new UFDouble(value.toString().trim());
  }

}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFDouble的值，如果为空，返回零
 * 参数：Object  value     对象值
 * 返回：UFDouble        UFDouble的值
 * 例外：无
 * 日期：(2003-03-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static UFDouble getUFDouble_ZeroAsNull(double  dValue) {
  if ( dValue==0 ){
    return  null ;
  }else{
    return  new UFDouble(dValue) ;
  }
}
/**
 * 作者：WYF
 * 功能：根据一个对象的值得到UFDouble的值，如果为零，返回空
 * 参数：Object  value     对象值
 * 返回：UFDouble        UFDouble的值
 * 例外：无
 * 日期：(2003-03-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public static UFDouble getUFDouble_ZeroAsNull(Object  value) {
  UFDouble  dValue = getUFDouble_NullAsZero(value) ;
  if ( dValue.compareTo(UFDouble.ZERO_DBL)==0 ) {
    return  null ;
  }
  return  dValue ;
}
/**
 * 作者：王印芬
 * 功能：判断两个字符串是否相等，均为NULL　或　TRIM＝NULL　认为相等
 * 参数：String    sValue1   串1
 *    String    sValue2   串2
 * 返回：boolean       是否相等
 * 例外：无
 * 日期：(2004-04-20 8:55:48)
 * 修改日期，修改人，修改原因，注释标志：
 */
public  static  boolean isEqual(
  String    sValue1,
  String    sValue2
  ){

  String  sTemp1 = getString_TrimZeroLenAsNull(sValue1) == null ? "": sValue1 ;
  String  sTemp2 = getString_TrimZeroLenAsNull(sValue2) == null ? "": sValue2 ;

  return  sTemp1.equals(sTemp2) ;
}


/**
 * 方法处理 SQL 语句中的IN()问题, 只能对于fieldname 在数据库中定义为 char or vchar 类型的字段!
 * 参  数	String fieldname IN 的数据库字段名, ArrayList fieldvalue IN 中的取值
 * 返回值	拼接的SQL 字串.
 * 创建日期：(2003-01-16 11:06:46)
 */

}
