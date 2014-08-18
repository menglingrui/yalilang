package nc.vo.zmpub.pub.tool;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.dap.pub.IDapSendMessage;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.xew.pub.Xewpubconst;
import nc.vo.zmpub.pub.bill.HYChildSuperVO;
import nc.vo.zmpub.pub.bill.HYHeadSuperVO;
/**
 * 单据发送会计平台处理类
 * @author mlr
 *
 */
public class ZmAccountDapTool {
    private static String[] nbooks=null;
	/**
	 * 传会计平台 默认单据影响因素 从vo可以取到账簿
	 * @param billvo
	 */
	public static void sendMessage(AggregatedValueObject billvo,PfParameterVO pfvo,String nmyName) throws ComponentException, BusinessException{
		if(billvo==null || billvo.getParentVO()==null || billvo.getChildrenVO()==null || billvo.getChildrenVO().length==0){
					
		}else{
			HYHeadSuperVO hvo=(HYHeadSuperVO) billvo.getParentVO();
			HYChildSuperVO[] bvos=(HYChildSuperVO[]) billvo.getChildrenVO();
			String  approvedate=pfvo.m_currentDate;
			DapMsgVO accountVo=getAccountVO(hvo,bvos,nmyName,approvedate);
			accountVo.setMsgType(DapMsgVO.ADDMSG);
			accountVo.setRequestNewTranscation(false);	
			getIDapSendMessage().sendMessage(accountVo, billvo);	
		}
	}
	/**
	 * 传会计平台 默认单据影响因素 从vo可以取到账簿
	 * @param billvo
	 */
	public static void sendMessage_del(AggregatedValueObject billvo,PfParameterVO pfvo,String nmyName) throws ComponentException, BusinessException{
		if(billvo==null || billvo.getParentVO()==null || billvo.getChildrenVO()==null || billvo.getChildrenVO().length==0){
					
		}else{
			HYHeadSuperVO hvo=(HYHeadSuperVO) billvo.getParentVO();
			HYChildSuperVO[] bvos=(HYChildSuperVO[]) billvo.getChildrenVO();
			String  approvedate=pfvo.m_currentDate;
			DapMsgVO accountVo=getAccountVO(hvo,bvos,nmyName,approvedate);
			accountVo.setMsgType(DapMsgVO.DELMSG);
			accountVo.setRequestNewTranscation(false);	
			getIDapSendMessage().sendMessage(accountVo, billvo);	
		}
	}
	/**
	 * 传会计平台 默认单据影响因素 从vo可以取不到账簿
	 * @param billvo
	 */
	public static void sendMessage_dels(AggregatedValueObject billvo,PfParameterVO pfvo,String nmyName,String pk_accountName) throws ComponentException, BusinessException{
		if(billvo==null || billvo.getParentVO()==null || billvo.getChildrenVO()==null || billvo.getChildrenVO().length==0){
					
		}else{
			HYHeadSuperVO hvo=(HYHeadSuperVO) billvo.getParentVO();
			String[] books=getAccountPk(hvo.getPk_corp());
			if(books==null || books==null){
				throw new BusinessException("当前公司没有可用的核算账簿");
			}
			for(int i=0;i<books.length;i++){
			HYChildSuperVO[] bvos=(HYChildSuperVO[]) billvo.getChildrenVO();
			billvo.getParentVO().setAttributeValue(pk_accountName, books[i]);
			new BaseDAO().updateVO((SuperVO) billvo.getParentVO());
			String  approvedate=pfvo.m_currentDate;
			DapMsgVO accountVo=getAccountVO(hvo,bvos,nmyName,approvedate);
			accountVo.setMsgType(DapMsgVO.DELMSG);
			accountVo.setRequestNewTranscation(false);	
			getIDapSendMessage().sendMessage(accountVo, billvo);	
			}
		}
	}
	/**
	 * 传会计平台 默认单据影响因素 从vo可以取不到账簿
	 * @param billvo
	 */
	public static void sendMessages(AggregatedValueObject billvo,PfParameterVO pfvo,String nmyName,String pk_accountName) throws ComponentException, BusinessException{
		if(billvo==null || billvo.getParentVO()==null || billvo.getChildrenVO()==null || billvo.getChildrenVO().length==0){
					
		}else{
			
			HYHeadSuperVO hvo=(HYHeadSuperVO) billvo.getParentVO();
			String[] books=getAccountPk(hvo.getPk_corp());
			if(books==null || books==null){
				throw new BusinessException("当前公司没有可用的核算账簿");
			}
			for(int i=0;i<books.length;i++){
				HYChildSuperVO[] bvos=(HYChildSuperVO[]) billvo.getChildrenVO();
				billvo.getParentVO().setAttributeValue(pk_accountName, books[i]);
				String  approvedate=pfvo.m_currentDate;
				DapMsgVO accountVo=getAccountVO(hvo,bvos,nmyName,approvedate);
				accountVo.setMsgType(DapMsgVO.ADDMSG);
				accountVo.setRequestNewTranscation(false);	
				getIDapSendMessage().sendMessage(accountVo, billvo);
			}			
		}
	}
    /**
     * 获得平台接口
     * @return
     * @throws ComponentException
     */
	public static IDapSendMessage getIDapSendMessage() throws ComponentException {
		return ((IDapSendMessage) NCLocator.getInstance().lookup(IDapSendMessage.class.getName()));
	}
	/**
	 * 根据 获得会计平台vo
	 * @param head
	 * @param items
	 * @param approvedate
	 * @return
	 */
	private static DapMsgVO getAccountVO(HYHeadSuperVO head, HYChildSuperVO[] items,String nmyName,String approvedate) {
        nc.vo.dap.out.DapMsgVO PfStateVO = new nc.vo.dap.out.DapMsgVO();
		PfStateVO.setCorp(head.getPk_corp()); //公司主键2
		PfStateVO.setSys("JX"); //系统号PK3
		PfStateVO.setProc(head.getPk_billtype()); //单据类型及业务处理PK4
		PfStateVO.setBusiType(head.getPk_busitype());
		//业务类型PK5
		PfStateVO.setBusiName(null);
		PfStateVO.setProcMsg(head.getPrimaryKey()); //处理信息7,根据此消息查询平台所用的信息
		PfStateVO.setBillCode(head.getVbillno()); //单据编码8
	//	PfStateVO.set
		PfStateVO.setBusiDate(new UFDate(approvedate)); //业务日期9
		if (items != null) {
		    PfStateVO.setOperator(InvocationInfoProxy.getInstance().getUserCode()); //业务员10PK
			PfStateVO.setCurrency(Xewpubconst.pk_currency); //币种11PK
		}
		//取出总金额
		UFDouble znmy=new UFDouble(0.0);
		for(int i=0;i<items.length;i++){
			znmy=znmy.add(PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(nmyName)));
		}
		PfStateVO.setMoney(znmy); //金额12
		PfStateVO.setComment(head.getVmemo()); //说明13
		PfStateVO.setChecker(head.getVapproveid()); //审核人14PK
		return PfStateVO;
    }
	/**
	 * 得到当前公司 可用的核算账簿
	 * @param pk_corp
	 * @return
	 * @throws DAOException 
	 */
	public static String[] getAccountPk(String pk_corp) throws DAOException{
		if(nbooks==null){
		String sql=" select distinct bd_glbook.pk_glbook " +
				" from bd_glbook " +
				" left outer join bd_subjscheme on bd_glbook.pk_subjscheme =bd_subjscheme.pk_subjscheme " +
				" left outer join  bd_accperiodscheme on  bd_glbook.pk_accperiodscheme=bd_accperiodscheme.pk_accperiodscheme " +
				" left outer join bd_currtype  main on bd_glbook.pk_main_currtype=main.pk_currtype " +
				" left outer join bd_currtype  ass on bd_glbook.pk_ass_currtype=ass.pk_currtype " +
				" left outer join bd_glorgbook on bd_glbook.pk_glbook=bd_glorgbook.pk_glbook " +
				"  where (ISNULL(bd_glbook.dr, 0) = 0 ) " +
				"  and bd_glbook.pk_glbook in (select b.pk_glbook from  bd_glorg h join bd_glorgbook b on h.pk_glorg=b.pk_glorg  where nvl(h.dr,0)=0" +
				"  and nvl(b.dr,0)=0 and h.pk_entityorg='"+pk_corp+"')";
		List<Object[]> list= (List<Object[]>) new BaseDAO().executeQuery(sql, new ArrayListProcessor());
		if(list==null || list.size()==0)
			return null;
		String[] books=new String[list.size()];
		for(int i=0;i<books.length;i++){
			books[i]=(String)list.get(i)[0];
		}
		nbooks=books;
		return books;
	  }	else{
		 return nbooks; 
	  }
	}
}
