package nc.vo.zmpub.formula;
import nc.vo.pub.BusinessException;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * mlr 工程验收单明细
 * @author mlr
 */
public interface IAllSummary {
	
	public Object calculateInvRow(ReportBaseVO detail,String attriName) throws BusinessException;


}
