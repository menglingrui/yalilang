package nc.vo.zmpub.formula;

import nc.vo.pub.BusinessException;
import nc.vo.zmpub.pub.report.ReportBaseVO;

public class AllSummary implements IAllSummary {

	public Object calculateInvRow(ReportBaseVO bivo, String attrName)
			throws BusinessException {
		if(bivo == null){
			return null;
		}
		return bivo.getAttributeValue(attrName);
	}

}
