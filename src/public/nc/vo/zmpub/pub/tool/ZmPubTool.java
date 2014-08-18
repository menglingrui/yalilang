package nc.vo.zmpub.pub.tool;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.zmpub.pub.ISonVO;
import nc.itf.zmpub.pub.ISonVOH;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtobill.BilltobillreferVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;
/** 
 * �ù��������ȴӻ�����ȡ��,���������û�вŲ�ѯ���ݿ�  
 * ��������˲�ѯЧ��
 * ���õĵ��ݲ����͵����������Լ�һЩ�����Բ����ڱ�������ʵ��
 * @author zhf
 */
public class ZmPubTool {
	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // ������
	private static nc.bs.pub.formulaparse.FormulaParse fp = new nc.bs.pub.formulaparse.FormulaParse();
	static PfUtilBO  pf=null;
	public static PfUtilBO getPfBO(){
		if(pf==null){
			pf=new PfUtilBO();
		}
		return pf;
	}
	private static BaseDAO dao=null;
	public static BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * ���ں�̨
	 * @param fomular ִ�еĹ�ʽ ���ѯ�ֿ����Ĺ�ʽ��storcode->getColValue(bd_strodoc,storcode,pk_stordoc,storid)   
	 * @param names   ����ֵ������  �磺new String[]{"storid"}
	 * @param values  ����ֵ ��:new String[]{"0001AE10000000018ES9"} 
	 * @return
	 * @throws BusinessException
	 */
	public static final Object execFomular(String fomular, String[] names,
			String[] values) throws BusinessException {
		fp.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("��������쳣");
		}
		int index = 0;
		for (String name : names) {
			fp.addVariable(name, values[index]);
			index++;
		}
		return fp.getValue();
	}
	
	public static String getSql(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'����'")) {
			sql = sql.replace("'����'", " 8");
		}
		if (sql.contains("'����ͨ��'")) {
			sql = sql.replace("'����ͨ��'", " 1");
		}
		return sql;
	}
	/**
	 * �����������ںʹ������id������ʧЧ���ڣ����ڿͻ���
	 * @param scri
	 * @param pk_invmandoc
	 * @return
	 * @throws BusinessException
	 */
	public static UFDate calQualityDate(UFDate scri,String pk_invmandoc) throws BusinessException{
		if(scri==null||scri.toString()==null ||scri.toString().length()==0)
			return null;
	    boolean  isQualityDateMag=isQualityDateMag(pk_invmandoc);
		if(!isQualityDateMag)
			return null;
		int qualityperiodunit=getQualityperiodunit(pk_invmandoc);
		int qualitydaynum=getQualitydaynum(pk_invmandoc);	
		return calcQualityDate(scri, qualityperiodunit, qualitydaynum);
	}
    
	private static int getQualitydaynum(String pk_invmandoc) throws BusinessException {
		String formal="ismag->getColValue(bd_invmandoc,qualitydaynum,pk_invmandoc,pk_invmandoc)";
		return 	PuPubVO.getInteger_NullAs((execFomularClient(formal, new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc})), 0);
	}
	private static int getQualityperiodunit(String pk_invmandoc) throws BusinessException {
		String formal="ismag->getColValue(bd_invmandoc,qualityperiodunit,pk_invmandoc,pk_invmandoc)";
		return 	PuPubVO.getInteger_NullAs((execFomularClient(formal, new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc})), 0);
 
	}
	public static String getSubSql(String[] saID) {
		String sID = null;
		StringBuffer sbSql = new StringBuffer("(");
		for (int i = 0; i < saID.length; i++) {
			if (i > 0) {
				sbSql.append(",");
			}
			sbSql.append("'");
			sID = saID[i];
			if (sID == null) {
				sID = "";
			}
			sbSql.append(sID);
			sbSql.append("'");
		}
		sbSql = sbSql.append(")");
		return sbSql.toString();
	}
	/**
	 * ������Ŀ����id�����Ŀ���� ���ڷ����
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getProCodeByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("jobcode->getColValue(bd_jobbasfil,jobcode,pk_jobbasfil,pk_jobbasfil)", 
				new String[]{"pk_jobbasfil"}, new String[]{pk_basid}));
	}
	/**
	 * �÷��������ã����Ӧ��ֻ��������ά��Ŀ
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getProSerMonthCodeByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("jobcode->getColValue(bd_jobbasfil,def10,pk_jobbasfil,pk_jobbasfil)", 
				new String[]{"pk_jobbasfil"}, new String[]{pk_basid}));
	}
	/**
	 * ǰ̨����
	 */
	private static Map<String,String> ClientBdCahche=new HashMap<String,String>();
	/**
	 * ��̨����
	 */
	private static Map<String,String> bsBdCahceh=new HashMap<String, String>();
	/**
	 * ���ݵ������ƻ��bd_bdinfo ����
	 * @param bdname
	 * @return
	 * @throws BusinessException
	 */
	public static String getBdinforPkByTname(String bdname) throws BusinessException {		
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			if(PuPubVO.getString_TrimZeroLenAsNull(bsBdCahceh.get(bdname))!=null){
				return bsBdCahceh.get(bdname);
			}else{
				String pk =PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("pk_bdinfo->getColValue2(bd_bdinfo,pk_bdinfo,bdname,bdname,pk_corp,pk_corp)", 
						new String[]{"bdname","pk_corp"}, new String[]{bdname,"0001"}));
				bsBdCahceh.put(bdname, pk);
				return bsBdCahceh.get(bdname);
			}
		} else {
			if(PuPubVO.getString_TrimZeroLenAsNull(ClientBdCahche.get(bdname))!=null){
				return ClientBdCahche.get(bdname);
			}else{
				String pk =PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient("pk_bdinfo->getColValue2(bd_bdinfo,pk_bdinfo,bdname,bdname,pk_corp,pk_corp)", 
						new String[]{"bdname","pk_corp"}, new String[]{bdname,"0001"}));
				ClientBdCahche.put(bdname, pk);
				return ClientBdCahche.get(bdname);
			}
		}
		
	}

	/**
	 * ���ݹ�˾������ù�˾���ƣ����ڷ����
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getCorpNameByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("corpname->getColValue(bd_corp,unitname,pk_corp,pk_corp)", 
				new String[]{"pk_corp"}, new String[]{pk_basid}));
	}
	/**
	 * ���ݹ�˾������ù�˾���룬���ڷ����
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getCorpCodeByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("corpcode->getColValue(bd_corp,unitcode,pk_corp,pk_corp)", 
				new String[]{"pk_corp"}, new String[]{pk_basid}));
	}
	 /**
	  * �����Զ�����������ö��������ƣ����ڷ����
	  * @param pk_basid
	  * @return
	  * @throws BusinessException
	  */
	public static String getMinNameByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("minareaname->getColValue(bd_defdoc, docname, pk_defdoc,pk_minarea )", 
				new String[]{"pk_minarea"}, new String[]{pk_basid}));
	}
	 /**
	  * �����Զ�����������ö�������룬���ڷ����
	  * @param pk_basid
	  * @return
	  * @throws BusinessException
	  */
	public static String getMinCodeByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("minareacode->getColValue(bd_defdoc, doccode, pk_defdoc,pk_minarea )", 
				new String[]{"pk_minarea"}, new String[]{pk_basid}));
	}
	/**
	 * ���ݲ���������ò������ƣ����ڷ����
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getDepNameByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("depname->getColValue(bd_deptdoc,deptname , pk_deptdoc, pk_deptdoc)", 
				new String[]{"pk_deptdoc"}, new String[]{pk_basid}));
	}
	/**
	 * ���ݲ���������ñ��룬���ڷ����
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getDepCodeByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("depcode->getColValue(bd_deptdoc,deptcode , pk_deptdoc, pk_deptdoc)", 
				new String[]{"pk_deptdoc"}, new String[]{pk_basid}));
	}
	
	
	/**
	 * �����ã����Ӧ��ֻ������xew��Ŀ
	 * @param pk_basid
	 * @return
	 * @throws BusinessException
	 */
	public static String getProSerMonthCodeByPkClient(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient("jobcode->getColValue(bd_jobbasfil,def10,pk_jobbasfil,pk_jobbasfil)", 
				new String[]{"pk_jobbasfil"}, new String[]{pk_basid}));
	}
	
	/**
	 * ������Ŀ��������Ŀ���������ڷ����
	 * @param jobcode
	 * @return
	 * @throws BusinessException
	 */
	public static String getProPkByCode(String jobcode) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("jobcode->getColValue(bd_jobbasfil,pk_jobbasfil,jobcode,jobcode)", 
				new String[]{"jobcode"}, new String[]{jobcode}));
	}
	
	/**
	 * ������Ŀ���������Ŀ���ƣ����ڷ����
	 * @param jobcode
	 * @return
	 * @throws BusinessException
	 */
	public static String getProNameByPk(String pk_basid) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("jobname->getColValue(bd_jobbasfil,jobname,pk_jobbasfil,pk_jobbasfil)", 
				new String[]{"pk_jobbasfil"}, new String[]{pk_basid}));
	}
	
	/**
	 * ���浥��
	 * @throws Exception
	 */
	public static void saveBillVO(String pk_billtype,String busidate,AggregatedValueObject[] abillvos)throws Exception{
		//����ɾ���ű�
		if (abillvos == null || abillvos.length == 0) {
			return;
		}       
		PfUtilBO pfbo = getPfBO();
		for (AggregatedValueObject bill : abillvos) {	
			if(bill==null || bill.getParentVO()==null || bill.getChildrenVO()==null || bill.getChildrenVO().length==0){
				continue;
			}				
//			if(PuPubVO.getInteger_NullAs(bill.getParentVO().getAttributeValue("vbillstatus"), -1)!=IBillStatus.FREE){
//				String billno=PuPubVO.getString_TrimZeroLenAsNull(bill.getParentVO().getAttributeValue("vbillno"));
//				throw new BusinessException(" ���ݺ�Ϊ["+billno+"] �����Ѿ��ύ������ ����ɾ��");
//			}
			pfbo.processAction("WRITE",pk_billtype,busidate,null,bill,null);
		}		
	}
	/**
	 * �鿴����Ƿ����ڹ���
	 * @param pk_invmandoc
	 * @return
	 * @throws BusinessException 
	 */
	private static boolean isQualityDateMagClient(String pk_invmandoc) throws BusinessException {
		String formal="ismag->getColValue(bd_invmandoc,qualitymanflag,pk_invmandoc,pk_invmandoc)";		
		return PuPubVO.getUFBoolean_NullAs(execFomularClient(formal, new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}), UFBoolean.FALSE).booleanValue();
	}
	
	/**
	 * �鿴����Ƿ����ڹ���
	 * @param pk_invmandoc
	 * @return
	 * @throws BusinessException 
	 */
	private static boolean isQualityDateMag(String pk_invmandoc) throws BusinessException {
		String formal="ismag->getColValue(bd_invmandoc,qualitymanflag,pk_invmandoc,pk_invmandoc)";		
		return PuPubVO.getUFBoolean_NullAs(execFomular(formal, new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}), UFBoolean.FALSE).booleanValue();
	}
	/**
	 * ����ʧЧ����
	 * @param d
	 * @param qualityperiodunit
	 * @param qualitydaynum
	 * @return
	 */
	
	public static UFDate calcQualityDate(UFDate d,Integer qualityperiodunit,Integer qualitydaynum) {
		  if(d==null || qualityperiodunit==null || qualitydaynum==null)
		    return null;
		  if(qualitydaynum.intValue()==0)
		    return d;
		  if(InvVO.quality_day==qualityperiodunit.intValue()){
		    return d.getDateAfter(qualitydaynum.intValue());
		  }else if(InvVO.quality_month==qualityperiodunit.intValue()){
		    int[] yearday = getAfterMonth(d.getYear(),d.getMonth(),qualitydaynum.intValue());
		    if(yearday==null)
		      return null;
		    return getUFDate(yearday[0],yearday[1],d.getDay());
		  }else if(InvVO.quality_year==qualityperiodunit.intValue()){
		    return getUFDate(d.getYear()+qualitydaynum.intValue(),d.getMonth(),d.getDay());
		  }
		  return null;
		}
	/**
	 * �õ�֮����N�µ�����
	 * @param year
	 * @param month
	 * @param interval
	 * @return
	 * 
	 * test case:
	 * 2005-02     + 12     
	 * 2006-02 
	 * 
	 * 2005 02     + 14     
	 * 2006 02+2 
	 * 
	 * 2005-09   + 19 
	 * 2006 09+7
	 * 2007 7+9-12
	 * 2007 4 
	 */
	public static UFDate getUFDate(int year, int month, int day){
	    if(year<=0 || month<=0 || day<=0)
	      return null;
	    String syear =  year+"";
	    String smonth = month+"";
	    if(month<10)
	      smonth="0"+month;
	    int days = UFDate.getDaysMonth(year, month);
	    if(day>days)
	      day = days;
	    String sday = day+"";
	    if(day<10)
	      sday="0"+day;
	    return new UFDate(syear+"-"+smonth+"-"+sday);
	}
	/**
	 * �õ�֮����N�µ�����
	 * @param year
	 * @param month
	 * @param interval
	 * @return
	 * 
	 * test case:
	 * 2005-02     + 12     
	 * 2006-02 
	 * 
	 * 2005 02     + 14     
	 * 2006 02+2 
	 * 
	 * 2005-09   + 19 
	 * 2006 09+7
	 * 2007 7+9-12
	 * 2007 4 
	 */
	public static int[] getAfterMonth(int year, int month, int interval){
	    if (interval<=0) return null;
	    int m = interval/12;
	    int n = interval%12;
	    
	    int yearX = year+m;
	    int monthX = month+n;
	    if (monthX>12){
	        yearX = yearX+1;
	        monthX = monthX-12;
	    }
	    
	    int[] iyearmonth = new int[2];
	    iyearmonth[0] = yearX;
	    iyearmonth[1] = monthX;
	    
	    return iyearmonth;
	}
	
	/**
	 * �õ�֮����N�µ�����
	 * @author yangtao
	 * @param startDate ����һ�������ַ�����ʽΪ��yyyy-MM-dd��
	 * @param n   �����ֵ
	 * @throws ParseException 
	 */

	 public static String getEndDate(String startDate,int n) throws ParseException{
		 Calendar cal = getCalendarOfstr(startDate);
		 cal.add(Calendar.MONTH, n);
		 String endDate = getstringOfcal(cal);
		 return endDate;
	 }
	/**
	 * ��Stringת����Calendar.
	 * ���ո�ʽΪ"yyyy-MM-dd"���ַ���
	 * @throws ParseException 
	 * @author yangtao
	 * @�������ڣ�2013-09-28
	 * */
	public static Calendar getCalendarOfstr(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d= sdf.parse(date);//���ַ���ת����date
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		return cal;
	}

	/**
	 * Calendar ת���� String.
	 * ��Calendar����ת����"yyyy-MM-dd"��ʽ���ַ���
	 * @author yangtao
	 * @return string
	 * @throws ParseException
	 */
	public static String getstringOfcal(Calendar cal) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(cal.getTime());
		return str;
	}

	/**
	 * ��dateת����"yyyy-MM-dd"������ʽ
	 * @param d 
	 * @return
	 * @author yangtao
	 * @�������ڣ�2013-09-28
	 */
	public static String getstringOfdate(Date d){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(d);
		
		return str;
	}
	
	/**
	 *��String��ʽ���ַ�ת����date
	 * @throws ParseException 
	 * 
	 */
	
	public static Date getdateOfstr(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d= sdf.parse(date);
		return d;
	}
	
	/**
	 * ����ĳ��������֮���ж��ٸ���.
	        ˵�������յ����ڸ�ʽ����Ϊ ����4λ����2λ��<br/>
	 * �� 2013-01-01��2013-12-01����ζ�Ŵ�2013��1�µ�2013��12�µõ��Ľ����12.
	 * 
	 * @param start ��ʼ����    ��    ��ʽΪ��yyyy-MM-dd�����ַ���
	 * @param end   ��������    ��    ��ʽΪ��yyyy-MM-dd�����ַ���
	 * @return
	 * @author yangtao
	 * @throws ParseException 
	 * @date 2013-10-9 ����10:20:09
	 */
	public static int countMonth(String start,String end) throws ParseException{
		
		int i =1;
	    while (true){
	    	Calendar s=getCalendarOfstr(start);
	    	s.add(Calendar.MONTH,  i);
	    	if(getstringOfcal(s).substring(0, 7).equals(end.substring(0, 7))){
	    		break;
	    	}
	    	i++;
	    }
		
		return i+1;
	}
	
/**
 * �õ���ǰ���ڵ��ܿ�ʼ���ڣ������죩
 * ���ա�YYYY-MM-DD�������ڸ�ʽ
 * @param date
 * @throws ParseException
 * @author yangtao
 * @return ���ص�ǰ���ڵ��ܿ�ʼ���ڣ������죩
 * @date 2014-6-5 ����05:10:29
 */
	public static String getSartdateOfWeek(String date)
			throws BusinessException {
		String startdate = null;
		try {
			Calendar ad = getCalendarOfstr(date);
			int a = ad.get(Calendar.DAY_OF_WEEK);
			ad.add(Calendar.DATE, -a + 1);
			startdate = getstringOfcal(ad);

		} catch (ParseException e) {

			e.printStackTrace();
			new BusinessException();
		}
		return startdate;

	}
	/**
	 * �õ���ǰ���ڵ���ĩ(����6)����
	 * ���ա�YYYY-MM-DD�������ڸ�ʽ
	 * @param date
	 * @throws ParseException
	 * @author yangtao
	 * @return ���ص�ǰ���ڵ���ĩ(����6)����
	 * @date 2014-6-5 ����05:10:29
	 */
	public static String getEnddateOfweek(String date) throws BusinessException {
		String enddate = null;
		try {
			String start = getSartdateOfWeek(date);
			Calendar ad = getCalendarOfstr(start);
			ad.add(Calendar.DATE, 6);
			enddate = getstringOfcal(ad);
		} catch (ParseException e) {

			e.printStackTrace();
			new BusinessException();
		}
		return enddate;
	}
	
	/**
	 * �������ù�˾
	 * @param items
	 * @param pk_corp
	 * @param invmname
	 * @param invbasname
	 * @throws BusinessException
	 */
	public static void setPk_invmandocForClient(CircularlyAccessibleValueObject[] items, String pk_corp,
			String invmname, String invbasname) throws BusinessException {
	   if(items==null || items.length==0)
		   return;
	   for(int i=0;i<items.length;i++){
		 String pk_invbasdoc=PuPubVO.getString_TrimZeroLenAsNull(items[i].getAttributeValue(invbasname));
		   
		 String pk_invmandoc=PuPubVO.getString_TrimZeroLenAsNull(
				ZmPubTool.execFomularClient("pk_invmandoc->getColValue2(bd_invmandoc,pk_invmandoc,pk_corp,pk_corp,pk_invbasdoc,pk_invbasdoc)",
				new String[]{"pk_corp","pk_invbasdoc"}, new String[]{pk_corp,pk_invbasdoc})) ;		 
		 items[i].setAttributeValue(invmname, pk_invmandoc);
	   }
		
	}

	private static nc.ui.pub.formulaparse.FormulaParse fpClient = new nc.ui.pub.formulaparse.FormulaParse();
	/**
	 * ����ǰ̨ ִ�й�ʽ
	 * @param fomular ִ�еĹ�ʽ ���ѯ�ֿ����Ĺ�ʽ��storcode->getColValue(bd_strodoc,storcode,pk_stordoc,storid)   
	 * @param names   ����ֵ������  �磺new String[]{"storid"}
	 * @param values  ����ֵ ��:new String[]{"0001AE10000000018ES9"} 
	 * @return
	 * @throws BusinessException
	 */
	public static final Object execFomularClient(String fomular,
			String[] names, String[] values) throws BusinessException {
		fpClient.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("��������쳣");
		}
		int index = 0;
		for (String name : names) {
			fpClient.addVariable(name, values[index]);
			index++;
		}
		return fpClient.getValue();
	}
	public static String getString_NullAsTrimZeroLen(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString().trim();
	}
	// ��ʱʹ�����·�ʽ���� ����
	public static final int STEP_VALUE = 10;
	public static final int START_VALUE = 10;

	/**
	 * ��vo�����к�����
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��011-1-26����03:34:51
	 * @param voaCA
	 * @param sBillType
	 * @param sRowNOKey
	 */
	public static void setVOsRowNoByRule(
			CircularlyAccessibleValueObject[] voaCA, String sRowNOKey) {

		if (voaCA == null)
			return;
		int index = START_VALUE;
		for (CircularlyAccessibleValueObject vo : voaCA) {
			vo.setAttributeValue(sRowNOKey, String.valueOf(index));
			index = index + STEP_VALUE;
		}

	}
	/**
	 * �жϿ�浥��vo,�Ƿ�֧�����ι���
	 * @param bill
	 * @throws BusinessException
	 */
	public static void dealIcGenBillVO(GeneralBillVO bill)
	throws BusinessException {
		if (bill == null)
			return;
		GeneralBillItemVO[] items = null;

		bill.getParentVO().setStatus(VOStatus.NEW);
		items = bill.getItemVOs();
		setVOsRowNoByRule(items, "crowno");
		if (items == null || items.length == 0)
			return;
		String inv_fomu = "wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,invman)";
		String[] names = new String[] { "invman" };
		String[] values = new String[1];
		for (GeneralBillItemVO item : items) {
			item.setStatus(VOStatus.NEW);
			// �����Ƿ����ι���
			values[0] = item.getCinventoryid();
			if (isLotMgtForInv(item.getCinventoryid(), inv_fomu, names, values))
				item.setAttributeValue("isLotMgt", 1);// ���ι���
			// �����Ƿ񸨼�������
		}
		// }
}
	
	/**
	 * �鿴��� �Ƿ����ι��������ڷ����
	 * zhf
	 * 
	 * @param invmanid
	 * @param fomular
	 * @param names
	 * @param values
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isLotMgtForInv(String invmanid) throws BusinessException {
		String inv_fomu = "wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,invman)";
		String[] names = new String[] { "invman" };
		String[] values = new String[]{invmanid};
		return PuPubVO.getUFBoolean_NullAs(execFomular(inv_fomu, names, values),
				UFBoolean.FALSE).booleanValue();
	}
	/**
	 * �鿴����Ƿ����ι���,���ڷ����
	 * zhf
	 * 
	 * @param invmanid
	 * @param fomular
	 * @param names
	 * @param values
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isLotMgtForInv(String invmanid, String fomular,
			String[] names, String[] values) throws BusinessException {
		return PuPubVO.getUFBoolean_NullAs(execFomular(fomular, names, values),
				UFBoolean.FALSE).booleanValue();
	}
	/**
	 * zhf
	 * ��ÿ�浥�ݶ�������
	 * @param icbilltype
	 * @return
	 */
	public static String getIcBillSaveActionName(String icbilltype) {
		if (icbilltype.equalsIgnoreCase(ScmConst.m_otherIn)
				|| icbilltype.equalsIgnoreCase(ScmConst.m_otherOut))
			return "WRITE";
		else
			return "PUSHSAVE";
	}
	/**
	 * ��ѯ���εĵ�������
	 * @throws DAOException 
	 */
	public static BilltobillreferVO[] queryNextBillType(String srctype) throws DAOException{
		String wsql=" isnull(dr,0)=0 and sourcebilltype ='"+srctype+"'";
		List<BilltobillreferVO> list =(List<BilltobillreferVO>) new BaseDAO().retrieveByClause(BilltobillreferVO.class, wsql);
		if(list==null || list.size()==0)
			return null;
		
		return list.toArray(new BilltobillreferVO[0]);
	}
	/**
	 * ���ɾ����������ʱУ��У�����Ƿ�������ݵ�sql
	 * @throws DAOException 
	 */
	public static  String getNextBillCheckSql(String nextbilltype,String sourceid) throws BusinessException{
		String wsql=" isnull(dr,0)=0 and pk_billtype='"+nextbilltype+"' and  coalesce(headbodyflag,'N')='N'";
		
		List<VotableVO> tables=(List<VotableVO>) new BaseDAO().retrieveByClause(VotableVO.class, wsql);
		if(tables==null || tables.size()==0 )
		   throw new BusinessException("û���ҵ�vo���ձ�,��������Ϊ:"+nextbilltype);
		
		VotableVO table=tables.get(0);
		if(table.getDef3()==null || table.getDef3().length()==0)
			 throw new BusinessException("��������Ϊ:"+nextbilltype +" ������Դ����idû������");
		String sql=" select count(0) from "+table.getVotable()+" h where h."+table.getDef3()+
		           " = '"+sourceid+"' and isnull(h.dr,0)=0";
		return sql;		
	}
	/**
	 * ���ӵ���id�͵������ͣ�У���Ƿ������������
	 * @param curbype
	 * @param curbillid
	 * @throws BusinessException
	 */
	public static void checkExitNextBill(String curbype,String curbillid) throws BusinessException{
		BilltobillreferVO[] bvos=queryNextBillType(curbype);
		if(bvos==null|| bvos.length==0){
			return;
		}
		for(int i=0;i<bvos.length;i++){
			String nexttype=bvos[i].getBilltype();
			String sql=getNextBillCheckSql(nexttype, curbillid);
			Integer count=PuPubVO.getInteger_NullAs(new BaseDAO().executeQuery(sql, new ColumnProcessor()), 0);
			if(count>0){
				throw new BusinessException("�������ε��� �޷�ִ�иò���");
			}
		}		
	}
	/**
	 * ���ݵ�ǰ�������ͺ͵�ǰ����id�����ε������ͣ�У���Ƿ������������
	 * @param curbype
	 * @param curbillid
	 * @param nexttype
	 * @throws BusinessException
	 */
	public static void checkExitNextBill(String curbype,String curbillid,String nexttype) throws BusinessException{			
			String sql=getNextBillCheckSql(nexttype, curbillid);
			Integer count=PuPubVO.getInteger_NullAs(new BaseDAO().executeQuery(sql, new ColumnProcessor()), 0);
			if(count>0){
				throw new BusinessException("�������ε��� �޷�ִ�иò���");
		}
		
		
	}
	/**
	 * �������ε������ͺ���Դ����id,�������ξۺ�vo
	 * @return
	 * @throws BusinessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public  static AggregatedValueObject[] getBillVOByTypeAndCsouid(String nextbilltype,String sourceid) throws BusinessException, ClassNotFoundException, InstantiationException, IllegalAccessException{	
		String wsql=" isnull(dr,0)=0 and pk_billtype='"+nextbilltype+"' and  coalesce(headbodyflag,'N')='N'";	
		List<VotableVO> tables=(List<VotableVO>) new BaseDAO().retrieveByClause(VotableVO.class, wsql);
		if(tables==null || tables.size()==0)
		   throw new BusinessException("û���ҵ�vo���ձ�,��������Ϊ:"+nextbilltype);		
		VotableVO table=tables.get(0);
		if(table.getDef3()==null || table.getDef3().length()==0)
			 throw new BusinessException("��������Ϊ:"+nextbilltype +" ������Դ����idû������");		
		String bodyclass=table.getHeaditemvo();		
		if(bodyclass==null || bodyclass.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û�����ñ���vo��Ӧ��");		
		String pkfield=table.getPkfield();		
		if(pkfield==null || pkfield.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û������  ����id");		
		String sql=" select * from "+table.getVotable()+" h where h."+table.getDef3()+
		           " = '"+sourceid+"' and isnull(h.dr,0)=0";
        Class bclass= Class.forName(bodyclass); 		
		List bodys=(List) new BaseDAO().executeQuery(sql, new BeanListProcessor(bclass));
		if(bodys==null)
			return null;		   
		SuperVO[] costbvos=(SuperVO[]) bodys.toArray((SuperVO[])java.lang.reflect.Array.newInstance(bclass, 0));		
		String hwsql=" isnull(dr,0)=0 and pk_billtype='"+nextbilltype+"' and  coalesce(headbodyflag,'N')='Y'";	
		List<VotableVO> htables=(List<VotableVO>) new BaseDAO().retrieveByClause(VotableVO.class, wsql);
		if(tables==null || tables.size()==0 )
		   throw new BusinessException("û���ҵ�vo���ձ�,��������Ϊ:"+nextbilltype);		
		VotableVO htable=htables.get(0);		
		String headclass=htable.getHeaditemvo();		
		if(headclass==null || headclass.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û�����ñ�ͷvo��Ӧ��");	
		Class hclass=Class.forName(headclass);		
		String billvoclass=htable.getBillvo();		
		if(billvoclass==null || billvoclass.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û�����þۺ�vo��Ӧ��");	    
		Class billclass=Class.forName(billvoclass);		
		   //�������ֵ�
		SuperVO[][] costbvoss=(SuperVO[][]) SplitBillVOs.getSplitVOs(costbvos, new String[]{pkfield});
		   //����ֱ�ӳɱ����㵥�ľۺ�vo
		AggregatedValueObject[] abillvos=(AggregatedValueObject[]) java.lang.reflect.Array.newInstance(billclass, costbvoss.length);
		   for(int i=0;i<costbvoss.length;i++){
			   SuperVO[] abvos=costbvoss[i];
			   if(abvos==null || abvos.length==0){
				   continue;
			   }
			   String pk_costaccount=abvos[0].getPrimaryKey();
			   List li=(List) new BaseDAO().retrieveByClause(hclass, " isnull(dr,0)=0 and "+pkfield+"='"+pk_costaccount+"'");
			   if(li==null || li.size()==0)
				   continue;
			   SuperVO ahvo=(SuperVO) li.get(0);
			   abillvos[i]=(AggregatedValueObject) Class.forName(billvoclass).newInstance();
			   abillvos[i].setParentVO(ahvo);
			   abillvos[i].setChildrenVO(abvos);
		   }
		return abillvos;		
	}
	/**
	 * ���ݵ������ͺ͵���id��ѯ�ۺ�vo
	 * @return
	 * @throws BusinessException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static AggregatedValueObject[] getBillVOByTypeAndPk(String nextbilltype,String pk) throws BusinessException, ClassNotFoundException, InstantiationException, IllegalAccessException{	
		String wsql=" isnull(dr,0)=0 and pk_billtype='"+nextbilltype+"' and  coalesce(headbodyflag,'N')='N'";	
		List<VotableVO> tables=(List<VotableVO>) new BaseDAO().retrieveByClause(VotableVO.class, wsql);
		if(tables==null || tables.size()==0)
		   throw new BusinessException("û���ҵ�vo���ձ�,��������Ϊ:"+nextbilltype);		
		VotableVO table=tables.get(0);
		if(table.getDef3()==null || table.getDef3().length()==0)
			 throw new BusinessException("��������Ϊ:"+nextbilltype +" ������Դ����idû������");		
		String bodyclass=table.getHeaditemvo();		
		if(bodyclass==null || bodyclass.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û�����ñ���vo��Ӧ��");		
		String pkfield=table.getPkfield();		
		if(pkfield==null || pkfield.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û������  ����id");		
		String sql=" select * from "+table.getVotable()+" h where h."+pkfield+
		           " = '"+pk+"' and isnull(h.dr,0)=0";
        Class bclass= Class.forName(bodyclass); 		
		List bodys=(List) new BaseDAO().executeQuery(sql, new BeanListProcessor(bclass));
		if(bodys==null)
			return null;		   
		SuperVO[] costbvos=(SuperVO[]) bodys.toArray((SuperVO[])java.lang.reflect.Array.newInstance(bclass, 0));		
		String hwsql=" isnull(dr,0)=0 and pk_billtype='"+nextbilltype+"' and  coalesce(headbodyflag,'N')='Y'";	
		List<VotableVO> htables=(List<VotableVO>) new BaseDAO().retrieveByClause(VotableVO.class, hwsql);
		if(tables==null || tables.size()==0 )
		   throw new BusinessException("û���ҵ�vo���ձ�,��������Ϊ:"+nextbilltype);		
		VotableVO htable=htables.get(0);		
		String headclass=htable.getHeaditemvo();		
		if(headclass==null || headclass.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û�����ñ�ͷvo��Ӧ��");	
		Class hclass=Class.forName(headclass);		
		String billvoclass=htable.getBillvo();		
		if(billvoclass==null || billvoclass.length()==0)
			throw new BusinessException("��������Ϊ:"+nextbilltype +" û�����þۺ�vo��Ӧ��");	    
		Class billclass=Class.forName(billvoclass);		
		   //�������ֵ�
		SuperVO[][] costbvoss=(SuperVO[][]) SplitBillVOs.getSplitVOs(costbvos, new String[]{pkfield});
		if(costbvoss==null ||costbvoss.length==0){
			//������ڿ� ��˵��û�б���
			   List li=(List) new BaseDAO().retrieveByClause(hclass, " isnull(dr,0)=0 and "+pkfield+"='"+pk+"'");
			   if(li==null || li.size()==0)
				   return null;
			   SuperVO ahvo=(SuperVO) li.get(0);
			   AggregatedValueObject billvo=(AggregatedValueObject) Class.forName(billvoclass).newInstance();
			   billvo.setParentVO(ahvo);
			   return new AggregatedValueObject[]{billvo};
		}
		   //����ֱ�ӳɱ����㵥�ľۺ�vo
		AggregatedValueObject[] abillvos=(AggregatedValueObject[]) java.lang.reflect.Array.newInstance(billclass, costbvoss.length);
		   for(int i=0;i<costbvoss.length;i++){
			   SuperVO[] abvos=costbvoss[i];
			   if(abvos==null || abvos.length==0){
				   continue;
			   }
			   String pk_costaccount=(String) abvos[0].getAttributeValue(abvos[0].getParentPKFieldName());
			   List li=(List) new BaseDAO().retrieveByClause(hclass, " isnull(dr,0)=0 and "+pkfield+"='"+pk_costaccount+"'");
			   if(li==null || li.size()==0)
				   continue;
			   SuperVO ahvo=(SuperVO) li.get(0);
			   abillvos[i]=(AggregatedValueObject) Class.forName(billvoclass).newInstance();
			   abillvos[i].setParentVO(ahvo);
				 //�����������������
			    SuperVO[] bodyvos=abvos;
				if(bodyvos!=null && bodyvos.length>0){
				 	HYBillVO sbill=(HYBillVO) Class.forName(htable.getBillvo()).newInstance();		
	    			if(sbill instanceof ISonVOH){
	    				ISonVOH sh=(ISonVOH) sbill;
	    			  String sonclass=sh.getSonClass();
	    			  Class sclass=Class.forName(sonclass);
	    			  SuperVO vo=(SuperVO) sclass.newInstance();
	            for(int k=0;k<bodyvos.length;k++){           			  
	    			  List slist=(List) getDao().retrieveByClause(sclass, " isnull(dr,0)=0 and "+bodyvos[k].getPKFieldName()+"='"+bodyvos[k].getPrimaryKey()+"'");
	    			  ((ISonVO)bodyvos[k]).setSonVOS(slist);
	             }
	    		}
				}
			   abillvos[i].setChildrenVO(abvos);
		   }
		return abillvos;		
	}
	/**
	 * ɾ������
	 * @throws Exception
	 */
	public static void deleteNextBillVO(String nextbilltype,String sourceid,String busidate)throws Exception{
		AggregatedValueObject[] abillvos=getBillVOByTypeAndCsouid(nextbilltype,sourceid);
		//����ɾ���ű�
		if (abillvos == null || abillvos.length == 0) {
			return;
		}       
		PfUtilBO pfbo = getPfBO();
		for (AggregatedValueObject bill : abillvos) {	
			if(bill==null || bill.getParentVO()==null || bill.getChildrenVO()==null || bill.getChildrenVO().length==0){
				continue;
			}				
			if(PuPubVO.getInteger_NullAs(bill.getParentVO().getAttributeValue("vbillstatus"), -1)!=IBillStatus.FREE){
				String billno=PuPubVO.getString_TrimZeroLenAsNull(bill.getParentVO().getAttributeValue("vbillno"));
				throw new BusinessException(" ���ݺ�Ϊ["+billno+"] �����Ѿ��ύ������ ����ɾ��");
			}
			pfbo.processAction("DELETE",nextbilltype,busidate,null,bill,null);
		}		
	}
	/**
	 * ɾ������
	 * @throws Exception
	 */
	public static void deleteBillVO(String pk_billtype,String busidate,AggregatedValueObject[] abillvos)throws Exception{
		//����ɾ���ű�
		if (abillvos == null || abillvos.length == 0) {
			return;
		}       
		PfUtilBO pfbo = getPfBO();
		for (AggregatedValueObject bill : abillvos) {	
			if(bill==null || bill.getParentVO()==null || bill.getChildrenVO()==null || bill.getChildrenVO().length==0){
				continue;
			}				
			if(PuPubVO.getInteger_NullAs(bill.getParentVO().getAttributeValue("vbillstatus"), -1)!=IBillStatus.FREE){
				String billno=PuPubVO.getString_TrimZeroLenAsNull(bill.getParentVO().getAttributeValue("vbillno"));
				throw new BusinessException(" ���ݺ�Ϊ["+billno+"] �����Ѿ��ύ������ ����ɾ��");
			}
			pfbo.processAction("DELETE",pk_billtype,busidate,null,bill,null);
		}		
	}
	/**
	 * ���ݵ������ͻ��VotableVO
	 * @param pk_billtype
	 * @param isMain �Ƿ�����
	 * @return
	 * @throws DAOException
	 */
	public static VotableVO getVotableVO(String pk_billtype, boolean isMain) throws DAOException {
	  	String ismain="N";
    	if(isMain){
    		ismain="Y";
    	}
    	String wsql=" isnull(dr,0)=0 and pk_billtype='"+pk_billtype+"' and  coalesce(headbodyflag,'N')='"+ismain+"'";	
		List<VotableVO> tables=(List<VotableVO>) new BaseDAO().retrieveByClause(VotableVO.class, wsql);
		if(tables==null || tables.size()==0)
			return null;
		return tables.get(0);
	}
	/**����ά��������ά������
	 * 
	 * @param vo
	 * @param persions
	 * @return
	 */
	public static String getPersionsByVo(SuperVO vo,String[] persions){
		if(vo==null || persions==null || persions.length==0)
			return null;
		String pr="";
			for(int i=0;i<persions.length;i++){
				pr=pr+vo.getAttributeValue(persions[i]);
			}
		return pr;
	}
	
	
	
	/**
	 * У���ַ����Ƿ������
	 * @param str
	 * @throws Exception
	 */
	public static void regexYear(String str) throws Exception {
		boolean flag=true;
		Pattern pattern=null;
		if(str==null){
			throw new BusinessException("��Ȳ���Ϊ��");	
		}
		if(str.length()!=4){
			throw new BusinessException("��ȱ�����4Ϊ");	
		}			
		pattern = Pattern.compile("[2][0][1][0-9]{1}");				
		Matcher matcher = pattern.matcher(str);	
		if(!matcher.find()){
			throw new BusinessException("�������ݴ���...��ȸ�ʽ����Ϊ:2010��2019");
		}
	}
	/**
	 * У���ַ����Ƿ����·�
	 * @param str
	 * @throws Exception
	 */
	public static void regexMonth(String str) throws Exception {
		boolean flag=true;
		Pattern pattern=null;
		if(str==null ){
			throw new BusinessException("������·ݲ���Ϊ��");
		}
		if(str.length()!=2){
			throw new BusinessException("������·ݱ�������λ");
		}
		pattern = Pattern.compile("0?[0][1-9]|1[0-2]");
		Matcher matcher = pattern.matcher(str);

		if(!matcher.find()) {
		   throw new BusinessException("������·ݴ���... �·ݸ�ʽ����Ϊ: 01��09����10��12");
		}
		}
	

	/**
	 /* �鿴 pk_invmandoc ����������  �Ƿ����� pk_invcl ���������,���ڷ����
	 * @param pk_invcl11
	 * @param pk_invmandoc1
	 * @return
	 * @throws BusinessException 
	 */
	public static boolean isInvmanContain(String pk_invcl, String pk_invmandoc) throws BusinessException {
		if(pk_invcl==null || pk_invcl.length()==0 || pk_invmandoc==null || pk_invmandoc.length()==0)
			return false;
		String invclcode=getInvclCodeByPk(pk_invcl);
		String sql=" select h.pk_invmandoc from bd_invmandoc h " +
		" join bd_invbasdoc b " +
		" on h.pk_invbasdoc = b.pk_invbasdoc " +
		" join bd_invcl c  " +
		" on b.pk_invcl = c.pk_invcl " +
		" where isnull(h.dr, 0) = 0  " +
		" and isnull(b.dr, 0) = 0 " +
		" and isnull(c.dr, 0) = 0 " +
		" and c.invclasscode like '"+invclcode+"%' ";
		List list=(List) getDao().executeQuery(sql, new ArrayListProcessor());
		if(list==null || list.size()==0)
			return false;
		for(int i=0;i<list.size();i++){
			Object[] o=(Object[]) list.get(i);
			if(o==null || o.length==0)
				continue;
			String pk_b=PuPubVO.getString_TrimZeroLenAsNull(o[0]);
			if(pk_invmandoc.equals(pk_b)){
				return true;
			}
		}		
		return false;
	}
	/**
	   �鿴������ƿ�Ŀ�Ƿ���ڽ���,���ڷ����
	 * @param pk_accsubj1
	 * @param pk_accsubj2
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isAccsubCross(String pk_accsubj1, String pk_accsubj2) throws BusinessException {
		if(pk_accsubj1==null || pk_accsubj1.length()==0 || pk_accsubj2==null || pk_accsubj2.length()==0){
			return false;
		}
		String invclcode=getAccountSubCodeByPk(pk_accsubj1);
		String invclcode1=getAccountSubCodeByPk(pk_accsubj2);
		if(invclcode.startsWith(invclcode1)){
			return true;
		}
		if(invclcode1.startsWith(invclcode)){
		    return true;	
		}
		return false;
	}
    /**
     * �鿴������ҵ����Ƿ���ڽ���
     * @param pk_invcl1
     * @param pk_invcl12
     * @return
     * @throws BusinessException 
     */
	public static boolean isInvclCross(String pk_invcl1, String pk_invcl2) throws BusinessException {
		if(pk_invcl1==null || pk_invcl1.length()==0 || pk_invcl2==null || pk_invcl2.length()==0){
			return false;
		}
		
		String invclcode=getInvclCodeByPk(pk_invcl1);
		String invclcode1=getInvclCodeByPk(pk_invcl2);
		if(invclcode.startsWith(invclcode1)){
			return true;
		}
		if(invclcode1.startsWith(invclcode)){
		    return true;	
		}
		return false;
	}
	
	
	
	/**
	 * ���ݹ�˾��ѯ �����˲�
	 * @param pk_corp
	 * @return
	 * @throws DAOException 
	 */
	public static List<String> getAccountBookByCorp(String pk_corp) throws DAOException{
	   String sql=" select pk_glbook from bd_glbook  where  bd_glbook.pk_glbook in (select b.pk_glbook from " +
								" bd_glorg h join bd_glorgbook b on h.pk_glorg=b.pk_glorg " +
								" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.pk_entityorg='"+pk_corp+"')";
	   List list=(List<String>) new BaseDAO().executeQuery(sql, new ArrayListProcessor());
	   return list;
	}
	/**
	 * ���ݴ������������ȡ���������룬�����
	 * @param pk_invcl
	 * @return
	 * @throws BusinessException
	 */
	public static String getInvclCodeByPk(String pk_invcl) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("invclasscode->getColValue(bd_invcl,invclasscode,pk_invcl,pk_invcl)",
				new String[]{"pk_invcl"}, new String[]{pk_invcl}));
	}
	/**
	 * ���ݴ������������ȡ����������ƣ������
	 * @param pk_invcl
	 * @return
	 * @throws BusinessException
	 */
	public static String getInvclNameByPk(String pk_invcl) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("invclassname->getColValue(bd_invcl,invclassname,pk_invcl,pk_invcl)",
				new String[]{"pk_invcl"}, new String[]{pk_invcl}));
	}
	/**
	 * ���ӻ�ƿ�Ŀ������ȡ��Ŀ���룬�����
	 * @param pk_accsubj
	 * @return
	 * @throws BusinessException
	 */
	public static String getAccountSubCodeByPk(String pk_accsubj) throws BusinessException {        
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("subjcode->getColValue(bd_accsubj,subjcode ,pk_accsubj ,pk_accsubj )",
				new String[]{"pk_accsubj"}, new String[]{pk_accsubj}));
	}
	 /** ���ݴ������������óɱ�����,�����
	 * @param pk_invmandoc
	 * @return
	 * @throws BusinessException 
	 */
	public static String getInvPriceBypPk(String pk_invmandoc) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("price->getColValue(bd_invmandoc,costprice, pk_invmandoc, pk_invmandoc )", 
				new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}));
	}
	/**
	 * ���ݴ��������������ȡ������룬�����
	 * @param pk_invbasdoc
	 * @return
	 * @throws BusinessException
	 */
	public static String getInvcodeByPk(String pk_invbasdoc) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("invcode->getColValue(bd_invbasdoc,invcode, pk_invbasdoc , pk_invbasdoc  )", 
				new String[]{"pk_invbasdoc "}, new String[]{pk_invbasdoc}));
	}
	/**
	 * ���ݴ������������ȡ�����������
	 * @param pk_invmandoc
	 * @return
	 * @throws BusinessException
	 */
	public static String getInvPkByManPk(String pk_invmandoc) throws BusinessException {
		if (RuntimeEnv.getInstance().isRunningInServer()){
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("pk_invbasdoc->getColValue(bd_invmandoc,pk_invbasdoc, pk_invmandoc , pk_invmandoc  )", 
					new String[]{"pk_invmandoc "}, new String[]{pk_invmandoc}));
		}else{
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient("pk_invbasdoc->getColValue(bd_invmandoc,pk_invbasdoc, pk_invmandoc , pk_invmandoc  )", 
					new String[]{"pk_invmandoc "}, new String[]{pk_invmandoc}));
		}
	
	}
	/**
	 * ���ݲ���Ա���ҵ��Ա
	 * @param operatorid
	 * @return
	 * @throws BusinessException
	 */
	public static String getSaleridByOperatorid(String operatorid)throws BusinessException{
		String fomuler = "pk_psndoc ->getColValue(sm_userandclerk,pk_psndoc,userid,loguser)";
		String[] names = new String[]{"loguser"};
		String[] values = new String[]{operatorid};
		if (RuntimeEnv.getInstance().isRunningInServer()){
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular(fomuler, names, values));
		}else{
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient(fomuler, names, values));
		}
	} 

	/**
	 * ����ҵ��Ա��ò���
	 * @param operatorid
	 * @return
	 * @throws BusinessException
	 */
	public static String getDeptidByPsnmanbasid(String saleid)throws BusinessException{
		String fomuler = "pk_deptdoc ->getColValue(bd_psndoc,pk_deptdoc,pk_psnbasdoc,saleid)";
		String[] names = new String[]{"saleid"};
		String[] values = new String[]{saleid};
		if (RuntimeEnv.getInstance().isRunningInServer()){
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular(fomuler, names, values));
		}else{
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient(fomuler, names, values));
		}
	} 
	/**
	 * ���ݴ�������������˰��
	 * @param bd_invmandoc
	 * @return ˰��
	 * @throws BusinessException 
	 * @author yangtao
	 */
	public static String getTaxratioByinvmanID(String pk_invmandoc)
			throws BusinessException {
		// ��ù�������

		String taxratio = "taxratio ->getColValue(bd_taxitems,taxratio,pk_taxitems,"
				+ "getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,"
				+ "getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,invmanid)))";
		String[] names = new String[] { "invmanid" };
		String[] values = new String[] { pk_invmandoc };
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular(
					taxratio, names, values));
		} else {
			return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool
					.execFomularClient(taxratio, names, values));
		}
	}
	
	
	/**
	 * ���ݴ������������ȡ������ƣ������
	 * @param pk_invbasdoc
	 * @return
	 * @throws BusinessException
	 */
	public static String getInvNameByPk(String pk_invbasdoc) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("invname->getColValue(bd_invbasdoc,invname, pk_invbasdoc , pk_invbasdoc  )", 
				new String[]{"pk_invbasdoc "}, new String[]{pk_invbasdoc}));
	}
	/**
	 * �����˲�������ȡ�˲����룬�����
	 * @param pk_accountbook
	 * @return
	 * @throws BusinessException
	 */
	public static String getBookCodeByPk(String pk_accountbook) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("code->getColValue(bd_glbook,code,pk_glbook,pk_glbook)", 
				new String[]{"pk_glbook "}, new String[]{pk_accountbook}));
	}
	/**
	 * �����˲�������ȡ�˲����ƣ������
	 * @param pk_accountbook
	 * @return
	 * @throws BusinessException
	 */
	public static String getBookNameByPk(String pk_accountbook) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("name->getColValue(bd_glbook,name,pk_glbook,pk_glbook)", 
				new String[]{"pk_glbook "}, new String[]{pk_accountbook}));
	}
    /**
     * ���ݴ������id��ȡ�������id�������
     * @param pk_invmandoc
     * @return
     * @throws BusinessException
     */
	public static String getPkinvclBypPk(String pk_invmandoc) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("pk_invcl->getColValue(bd_invbasdoc,pk_invcl, pk_invbasdoc, pk_invmandoc )", 
				new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}));
	}
	/**
	 * ������Ŀ���ͱ�������Ŀ��������,�����
	 * @param protype
	 * @return
	 * @throws BusinessException 
	 */
	public static String getpk_protypeByCode(String protype) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("pk_jobtype->getColValue(bd_jobtype,pk_jobtype,jobtypecode ,jobtypecode )", 
				new String[]{"jobtypecode"}, new String[]{protype}));
	}
	/**
	 * ������Ŀ���ͱ�������Ŀ��������,�ͻ���
	 * @param protype
	 * @return
	 * @throws BusinessException 
	 */
	public static String getpk_protypeByCodeClient(String protype) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient("pk_jobtype->getColValue(bd_jobtype,pk_jobtype,jobtypecode ,jobtypecode )", 
				new String[]{"jobtypecode"}, new String[]{protype}));
	}
	
	/**
	 * ������Ŀ���ͱ�������Ŀ���ͱ��룬�����
	 * @param protype
	 * @return
	 * @throws BusinessException 
	 */
	public static String getjobtypenameByCode(String protype) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("pk_jobtype->getColValue(bd_jobtype,jobtypename,jobtypecode ,jobtypecode )", 
				new String[]{"jobtypecode"}, new String[]{protype}));
	}
	/**
	 * ������Ŀ���ͱ�������Ŀ���ͱ�����򣬷����
	 * @param protype
	 * @return
	 * @throws BusinessException 
	 */
	public static String getjobtypeCdgzByCode(String protype) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("jobclclass ->getColValue(bd_jobtype,jobclclass,jobtypecode ,jobtypecode )", 
				new String[]{"jobtypecode"}, new String[]{protype}));
	}
	/**
	 * ������Ŀ���ͱ�������Ŀ���ͱ�����򣬿ͻ���
	 * @param protype
	 * @return
	 * @throws BusinessException 
	 */
	public static String getjobtypeCdgzByCodeClient(String protype) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient("jobclclass ->getColValue(bd_jobtype,jobclclass,jobtypecode ,jobtypecode )", 
				new String[]{"jobtypecode"}, new String[]{protype}));
	}
	/**
	 * ������Ŀ���ͱ�������Ŀ���ͱ��룬�ͻ���
	 * @param protype
	 * @return
	 * @throws BusinessException 
	 */
	public static String getjobtypenameByCodeClient(String protype) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomularClient("pk_jobtype->getColValue(bd_jobtype,jobtypename,jobtypecode ,jobtypecode )", 
				new String[]{"jobtypecode"}, new String[]{protype}));
	}
	/**
	 * ����vo����ȡ�������к�
	 * @param vos
	 * @return
	 */
	public static int getMaxRowNo(HYChildSuperVO[] vos) {
		if(vos==null || vos.length==0){
			return 10;
		}
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<vos.length;i++){
			list.add(PuPubVO.getInteger_NullAs(vos[i].getCrowno(),0));
		}
		Integer[] rows=list.toArray(new Integer[0]); 
		Arrays.sort(rows);	
		if(rows==null || rows.length==0)
			return 10;
		return rows[rows.length-1];
	}
	/**
	 * ��յ������� �������Ƿ�����
	 * @param nbill
	 * @param b
	 * @throws BusinessException
	 */
	public static void clearPrimaryKey(HYBillVO nbill, boolean isNew) throws BusinessException {
	       if(nbill.getChildrenVO()!=null && nbill.getChildrenVO().length>0){
	    	   for(int k=0;k<nbill.getChildrenVO().length;k++){
	    		   nbill.getChildrenVO()[k].setPrimaryKey(null);
	    		   nbill.getChildrenVO()[k].setAttributeValue(((SuperVO)nbill.getChildrenVO()[k]).getParentPKFieldName(), null);
	    		   if(isNew){
	    			   nbill.getChildrenVO()[k].setStatus(VOStatus.NEW);
	    		   }
	    		   if(nbill instanceof ISonVOH){
	    			   if(nbill.getChildrenVO()[k] instanceof ISonVO){
	    				   ISonVO bvo=(ISonVO) nbill.getChildrenVO()[k];
	    				   List list=bvo.getSonVOS();
	    				   if(list!=null && list.size()>0){
	    					   for(int n=0;n<list.size();n++){
	    						   SuperVO son= (SuperVO) list.get(n);
	    						   son.setPrimaryKey(null);
	    						   son.setAttributeValue(son.getParentPKFieldName(), null);
	    						   if(isNew){
	    						   son.setStatus(VOStatus.NEW);
	    						   }
	    					   }
	    				   }
	    			   }
	    		   }  
	    	   }
	       }		
	}
	/**
	 * ��vo�ŵ�list��
	 * @param vos
	 * @return
	 */
	public static List getList(SuperVO[] vos){
		if(vos==null || vos.length==0)
			return null;
		List list=new ArrayList();
		for(int i=0;i<vos.length;i++){
			list.add(vos[i]);
		}		
		return list;
	}
	/**
	 * ��list�ŵ�vo��
	 * @param list
	 * @return
	 */
	public static SuperVO[] getList(List list){
		if(list==null || list.size()==0)
			return null;
		SuperVO vo=(SuperVO) list.get(0);
		SuperVO[] vos=(SuperVO[]) java.lang.reflect.Array.newInstance(vo.getClass(), list.size());
		for(int i=0;i<list.size();i++){
			vos[i]=(SuperVO) list.get(i);
		}		
		return vos;
	}
	   /**
	 * ��ȡָ�����������ܵ���һ
	 * @Methods Name getMonday
	 * @return Date
	 * @author Jay
     * @throws ParseException 
	 */
	public static String getMonday(String currentdate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d= sdf.parse(currentdate);//���ַ���ת����date
		Calendar cDay = Calendar.getInstance();   
        cDay.setTime(d);   
        cDay.set(Calendar.DAY_OF_WEEK, 2);//���⽫���ն�λ��һ�죬��һȡ�ڶ���
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;   
	}
	/**
	 * ϣ����ѡ������ʹ��
	 * ��ȡָ��������������
	 * @Methods Name getSunday
	 * @return Date
	 * @author Jay
	 * @throws ParseException 
	 */
	public static String getSunday(String  currentdate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
		Calendar cDay = Calendar.getInstance();   
		SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        cDay.setTime(date);
        if(Calendar.DAY_OF_WEEK==cDay.getFirstDayOfWeek()){	//����պ������գ�ֱ�ӷ���
            String str = fsdf.format(cDay.getTime());
        	return str;
        }else{//����������գ���һ�ܼ���
        	cDay.add(Calendar.DAY_OF_YEAR, 7);
        	cDay.set(Calendar.DAY_OF_WEEK, 1);
        	String str = fsdf.format(cDay.getTime());
        	return str;
        }  
	}
	/**
	 * ϣ����ѡ������ʹ��
     * �õ����µ�һ�������
     * @Methods Name getFirstDayOfMonth
     * @return Date
     * @author Jay
	 * @throws ParseException 
     */
	public static String getFirstDayOfMonth(String currentdate) throws ParseException   {  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
        Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
                  String str = fsdf.format(cDay.getTime());
        return str;   
	}   
	/**
	 * ϣ����ѡ������ʹ��
     * �õ��������һ�������
     * @Methods Name getLastDayOfMonth
     * @return Date
     * @author Jay
	 * @throws ParseException 
     */
	public static String getLastDayOfMonth(String currentdate) throws ParseException   {  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
    	Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;    
	}
	/**
	 * ϣ����ѡ������ʹ��
     * �õ������ȵ�һ�������
     * @Methods Name getFirstDayOfQuarter
     * @return Date
     * @author Jay
	 * @throws ParseException 
     */
	public static String getFirstDayOfQuarter(String currentdate) throws ParseException   {   
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
    	Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){  
        	cDay.set(Calendar.MONTH, Calendar.JANUARY);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){  
        	cDay.set(Calendar.MONTH, Calendar.APRIL);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {  
        	cDay.set(Calendar.MONTH, Calendar.JULY);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {  
        	cDay.set(Calendar.MONTH, Calendar.OCTOBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;     
	}
	/**
	 * ϣ����ѡ������ʹ��
     * �õ����������һ�������
     * @Methods Name getLastDayOfQuarter
     * @return Date
     * @author Jay
	 * @throws ParseException 
     */
	public static String getLastDayOfQuarter(String currentdate) throws ParseException   { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
    	Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){  
        	cDay.set(Calendar.MONTH, Calendar.MARCH);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){  
        	cDay.set(Calendar.MONTH, Calendar.JUNE);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {  
        	cDay.set(Calendar.MONTH, Calendar.SEPTEMBER);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {  
        	cDay.set(Calendar.MONTH, Calendar.DECEMBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;    
	}
	/**
	 * ϣ����ѡ������ʹ��
	 * ��ȡһ���еĵ�һ��
	 * @param currentdate
	 * @return
	 * @author Jay
	 * @throws ParseException
	 */
	public static String getFirstDayOfYear(String currentdate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
    	Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;  
	}
	/**
	 * ϣ����ѡ������ʹ��
	 * ��ȡһ���е����һ��
	 * @param currentdate
	 * @return
	 * @author Jay
	 * @throws ParseException
	 */
	public static String getLastDayOfYear(String currentdate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
    	Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_YEAR, cDay.getActualMaximum(Calendar.DAY_OF_YEAR));
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;  
	}
	/**
	 * ϣ����ѡ������ʹ��
	 * ��ȡ��ǰ����֮�����֮ǰn���µ�����
	 * ������Ĳ���nΪ����ʱ��Ϊ֮�󡣷�֮Ϊ֮ǰ
	 * @param currentdate
	 * @param n
	 * @return
	 * @author Jay
	 * @throws ParseException
	 */
	public  static String getDateOfMontth(String currentdate,int n) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date= sdf.parse(currentdate);//���ַ���ת����date
    	Calendar cDay = Calendar.getInstance();   
        cDay.setTime(date);
        cDay.add(Calendar.MONTH, n);
        SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = fsdf.format(cDay.getTime());
        return str;  
		
	}
	/**
	 * ���ݴ������������ô����������
	 * @param pk_invmandoc
	 * @author Jay
	 * @return
	 * @throws BusinessException
	 */
	public static String getInvbaspkBymanPk(String pk_invmandoc) throws BusinessException {
		return PuPubVO.getString_TrimZeroLenAsNull(ZmPubTool.execFomular("invname->getColValue(bd_invmandoc,pk_invbasdoc, pk_invmandoc , pk_invmandoc  )", 
				new String[]{"pk_invmandoc "}, new String[]{pk_invmandoc}));
	}
}