package nc.bs.zmpub.sourcebill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.trade.billsource.DefaultDataFinder;
import nc.bs.trade.billsource.IBillFlow;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uif.pub.exception.UifRuntimeException;
import nc.vo.bill.pub.MiscUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.datatype.DataTypeConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.trade.billsource.LightBillVO;
/**
 * ֧������ĵ��ݣ��ڵ������͹������ע�����
 * ��Ҫ�����׼��Ʒ������ʱͬһ�ŵ��ݣ��ظ���ʾ������
 * @author mlr
 */
public class ZMDefaultDatafinder extends DefaultDataFinder {

  @Override
  protected String createSQL(String billType) {
		IBillFlow billflow = getBillFlow(billType);
		if (billflow == null) {
			System.out.println("��������:" + billType + "û���ҵ�����������Ϣ��");
			return null;
		}	

		if(billflow.getSubTableName() == null)
			return null;
		
		String bTable = billflow.getSubTableName();
		String bFkField = billflow.getSubTableForeignKeyFiled();
		String bTableSourceTypeField = billflow.getSourceTypeField();
		String bTableSourceIDField = billflow.getSourceIDField();
		
		if (bTableSourceTypeField==null || bTableSourceIDField==null)
			return null;
		
		StringBuffer sb = new StringBuffer("SELECT DISTINCT ");
		sb.append(bTableSourceTypeField).append(",")
				.append(bTableSourceIDField);
		sb.append(" FROM ").append(bTable);

		sb.append(" WHERE");
		sb.append(" ");
		sb.append(bFkField);

		sb.append("=?");
		sb.append(" and dr=0 ");
	//	sb.append(" and ");

		return sb.toString();
	}

  protected Vector createSQLMy(String billType) {


    nc.vo.pf.changeui02.VotableVO headAttrVo = PfDataCache.getBillTypeToVO(
        billType, true);
    nc.vo.pf.changeui02.VotableVO itemAttrVo = PfDataCache.getBillTypeToVO(
        billType, false);
    if (headAttrVo == null || itemAttrVo == null) {
      SCMEnv.out("��������:" + billType + "û��ע�ᵥ��VO���ձ�");
      return null;
    }
    UFBoolean ifContainCurType = DataTypeConst.UFBOOLEAN_N;
    
    String hTable = headAttrVo.getVotable();// billflow.getMainTableName();
    String hPkField = headAttrVo.getPkfield();// billflow.getMainTablePrimaryKeyFiled();
    String hTableCodeField = headAttrVo.getBillno();// billflow.getBillNOField();
    String bTable = itemAttrVo.getVotable();// billflow.getSubTableName();
    String bFkField = itemAttrVo.getPkfield();// billflow.getSubTableForeignKeyFiled();
    String bTableSourceTypeField = itemAttrVo.getDef2();// billflow.getSourceTypeField();
    String bTableSourceIDField = itemAttrVo.getDef3();// billflow.getSourceIDField();
    String hTableBillTypeField = itemAttrVo.getDef1();
    if(null == hTableBillTypeField || "".equals(hTableBillTypeField.trim())){
      SCMEnv.out("��������:" + billType + "û��ע��DEF1���������ֶ�����");
    }
      

    // �������û����Դ���������ֶ�,�򷵻ؿ�.����������͵���û�б�ʶ��Դ����
    // ����,���޷���λ���Ƿ��Ǻ󵥾�.
    // ͨ���������:����ĳ�̶ֹ����͵��ݵĺ�������.
    if (bTableSourceIDField == null)
      return null;

    // �ڸ����͵ĵ����в���ĳ�����͵��ݵĺ�������
    StringBuffer sb = new StringBuffer("SELECT DISTINCT");
    sb.append(" ");
    sb.append(hTable + "." + hPkField);
    sb.append(", ");
    sb.append(hTable + "." + hTableCodeField);
    sb.append(" ");
    sb.append("FROM");
    sb.append(" ");
    sb.append(hTable);
    sb.append(", ");
    if (hTable.equalsIgnoreCase(bTable)) {
      sb.append(bTable + " B");
    }
    else {
      sb.append(bTable);
    }
    sb.append(" ");
    sb.append("WHERE");
    sb.append(" ");
    sb.append(hTable + "." + hPkField);
    sb.append("=");

    if (hTable.equalsIgnoreCase(bTable)) {
      sb.append("B" + "." + bFkField);
    }
    else {
      sb.append(bTable + "." + bFkField);
    }

    sb.append(" and ");
    if (hTable.equalsIgnoreCase(bTable)) {
      sb.append("B" + "." + bTableSourceIDField);
    }
    else {
      sb.append(bTable + "." + bTableSourceIDField);
    }
    sb.append("=?");

//    if (null != bTableSourceTypeField
//        && !"".equals(bTableSourceTypeField.trim())) {
//      sb.append(" and ");
//      sb.append(bTable + "." + bTableSourceTypeField);
//      sb.append(" = ? ");
//    }

    if (hTableBillTypeField != null) {
    	ifContainCurType = DataTypeConst.UFBOOLEAN_Y;
      sb.append(" and ");
      sb.append(hTable + "." + hTableBillTypeField);
      sb.append("='").append(billType.trim()).append("'");
    }

    sb.append(" and ");
    sb.append(hTable + ".dr =0");
    sb.append(" and ");

    if (hTable.equalsIgnoreCase(bTable)) {
      sb.append("B" + ".dr =0");
    }
    else {
      sb.append(bTable + ".dr =0");
    }
    sb.append(" and "+hTable+".pk_billtype= '"+billType.trim()+"'") ; 
    Vector vSql = new Vector();
    vSql.add(sb.toString());
    vSql.add(ifContainCurType);
    return vSql;

  }

  @Override
  public IBillFlow getBillFlow(String billType) {
    // TODO �Զ����ɷ������
    return super.getBillFlow(billType);
  }

  @Override
  public LightBillVO[] getForwardBills(String srcBillType, String srcBillID,
      final String curBillType) {
	  boolean isContainCurtype = false;
	  String sql = "";
	   //������������ �����빺��
	    if(curBillType.equals(ScmConst.PO_Pray) && srcBillType.equals(ScmConst.PO_MaterialAppBill)){
	    	 sql = " SELECT DISTINCT csourceid2,vsourcecode2 FROM po_requireapp_b " +
	    	 		"WHERE dr = 0 AND crequireappid = ?  AND csourcetypecode2 = '"+ScmConst.PO_Pray+"'";
	    }else if(curBillType.equals(ScmConst.m_sBillDBDD) && srcBillType.equals(ScmConst.PO_MaterialAppBill)){
	    	 sql = " SELECT DISTINCT cfirstid2,vfirstcode2 FROM po_requireapp_b " +
 	 		"WHERE dr = 0 AND crequireappid = ?  AND cfirsttypecode2 in ('"+ScmConst.m_sBillZZJDBDD+"','"+ScmConst.m_sBillGSJDBDD+"')";
	    }else{
	    	Vector vtemp = createSQLMy(curBillType);
	    	if(vtemp != null && vtemp.size() >= 2){
		    	sql = (String)vtemp.get(0);
		    	isContainCurtype = ((UFBoolean)vtemp.get(1)).booleanValue();
	    	}else {
	    		return null;
	    	}
	    }

    if (sql == null || sql.trim().length() == 0)
      return null;

    PersistenceManager sessionManager = null;
    try {
      sessionManager = PersistenceManager.getInstance();
      JdbcSession session = sessionManager.getJdbcSession();
      SQLParameter para = new SQLParameter();
      if(curBillType.equals(ScmConst.PO_Pray) && srcBillType.equals(ScmConst.PO_MaterialAppBill)
    		  || (curBillType.equals(ScmConst.m_sBillDBDD) && srcBillType.equals(ScmConst.PO_MaterialAppBill))){
    	  para.addParam(srcBillID);
      }else{
	      para.addParam(srcBillID);
//	      para.addParam(srcBillType);
//	      if (curBillType != null && isContainCurtype)
//	        para.addParam(curBillType);
      }

      ResultSetProcessor p = new ResultSetProcessor() {
        public ArrayList handleResultSet(ResultSet rs) throws SQLException {
          ArrayList al = new ArrayList();
          while (rs.next()) {
            String id = rs.getString(1);
            String code = rs.getString(2);

            if (id != null) {
              LightBillVO svo = new LightBillVO();
              svo.setID(id);
              svo.setCode(code);
              svo.setType(curBillType);
              al.add(svo);
            }
          }
          return al;
        }
      };
      ArrayList result = (ArrayList) session.executeQuery(sql, para, p);
      if (result.size() == 0){
        return null;
      }
      int size = result.size();
      LightBillVO[] vos = new LightBillVO[size];
      vos = (LightBillVO[]) result.toArray( vos );
      //��乫˾��Ϣ����ֹ��ǰ�ĵ�������ʱû�а�ť
      for(LightBillVO vo:vos){
        this.setCorp(vo);
      }
      return vos;
    }
    catch (DbException e) {
      Logger.error(e.getMessage(), e);
      throw new UifRuntimeException("getForwardBills error");
    }
    finally {
      sessionManager.release();
    }

  }
  
  public void setCorp(LightBillVO vo){
    if( vo.getCorp() != null && vo.getCorp().trim().length() >0 ){
      return;
    }
    List<String> codeAndCorp=getBillCodeAndCorp(vo.getType(), vo.getID());
    if(codeAndCorp!=null)
    vo.setCorp( codeAndCorp.get(1) );
  }

  @Override
  public String[] getForwardBillTypes(LightBillVO vo) throws BusinessException {

    // from
    // nc.bs.trade.billsource.BillTypeSetDataFinder.getForwardBillTypes(LightBillVO)

    BilltypeVO type = PfDataCache.getBillType(vo.getType());

    if (type == null)
      return null;

    if (type.getForwardbilltype() == null)
      return null;

    String[] forwordtypes = MiscUtil.getStringTokens(type.getForwardbilltype(),
        ",");

    ArrayList<String> types = new ArrayList<String>();
    for (int i = 0; i < forwordtypes.length; i++) {
      if (PfDataCache.getBillType(forwordtypes[i]) != null)
        types.add(forwordtypes[i]);
    }

    forwordtypes = new String[types.size()];

    types.toArray(forwordtypes);

    return forwordtypes;

  }

  @Override
  public LightBillVO[] getSourceBills(String curBillType, String curBillID) {
	  ArrayList<LightBillVO> result = new ArrayList<LightBillVO>(); 
    if( curBillType.equals(ScmConst.PO_Pray) //�빺��
    		|| curBillType.equals(ScmConst.m_sBillZZJDBDD ) //��������
    		|| curBillType.equals(ScmConst.m_sBillGSJDBDD)
    		|| curBillType.equals(ScmConst.m_sBillDBDD )){
    	String sql = "";
    	if(curBillType.equals(ScmConst.PO_Pray)){
	    	sql = " SELECT DISTINCT head.vbillcode, head.crequireappid, head.cbilltypecode ,head.pk_corp "
				+ " FROM po_requireapp head "
				+ " INNER JOIN po_requireapp_b body ON body.crequireappid = head.crequireappid "
				+ " WHERE body.dr = 0 AND head.dr = 0 AND body.csourceid2 = ? " +
						"AND body.csourcetypecode2 = '"+ScmConst.PO_Pray+"' ";
    	}else if(curBillType.equals(ScmConst.m_sBillZZJDBDD ) //��������
        		|| curBillType.equals(ScmConst.m_sBillGSJDBDD)
        		|| curBillType.equals(ScmConst.m_sBillDBDD )){
    	  //�����ж���Ӧ�ĵ��������Ƿ�����
    	  if(null == PfDataCache.getBillTypeInfo(ScmConst.PO_MaterialAppBill))
    	      return result.toArray(new LightBillVO[0]);
    		sql = " SELECT DISTINCT head.vbillcode, head.crequireappid, head.cbilltypecode ,head.pk_corp "
				+ " FROM po_requireapp head "
				+ " INNER JOIN po_requireapp_b body ON body.crequireappid = head.crequireappid "
				+ " WHERE body.dr = 0 AND head.dr = 0 AND body.cfirstid2 = ? " +
						"AND body.cfirsttypecode2 in ('"+ScmConst.m_sBillZZJDBDD+"','"+ScmConst.m_sBillGSJDBDD+"') ";
    	}
		
		PersistenceManager sessionManager = null;
		try {
			sessionManager = PersistenceManager.getInstance();
			JdbcSession session = sessionManager.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(curBillID);
			ResultSetProcessor p = new ResultSetProcessor() {
				@SuppressWarnings("unchecked")
				public Object handleResultSet(ResultSet rs) throws SQLException {
					ArrayList al = new ArrayList();
					while (rs.next()) {
						String code = rs.getString(1);
						String id = rs.getString(2);
						String type = rs.getString(3);
						String corp = rs.getString(4);
						if (type != null && id != null
								&& type.trim().length() > 0
								&& id.trim().length() > 0) {
							LightBillVO svo = new LightBillVO();
							svo.setType(type);
							svo.setID(id);
							svo.setCode(code);
							svo.setCorp(corp);
							al.add(svo);
						}
					}
					return al;
				}
			};
			result = (ArrayList<LightBillVO>) session.executeQuery(sql, para, p);
			if (result.size() != 0){
				// �������ε��ݺ�
				for (LightBillVO vo : result) {
					if(vo.getCode()!= null &&vo.getCode().trim().length() != 0){
						continue;
					}
	        List<String> codeAndCorp=getBillCodeAndCorp(vo.getType(), vo.getID());
	        vo.setCode(codeAndCorp.get(0));
	        vo.setCorp(codeAndCorp.get(1));
				}
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new UifRuntimeException(e.getMessage());
		} finally {
			sessionManager.release();
		}
    }
    LightBillVO[] supervos =  super.getSourceBills(curBillType, curBillID);
    if(supervos != null && supervos.length != 0)
	    for (LightBillVO lightBillVO : supervos) {
	    	lightBillVO.setType(lightBillVO.getType().trim());
	    	result.add(lightBillVO);
		}
    return result.toArray(new LightBillVO[result.size()]);
  }
	
}
