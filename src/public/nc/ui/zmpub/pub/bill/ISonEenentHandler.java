package nc.ui.zmpub.pub.bill;

import nc.vo.pub.BusinessException;

/**
 * ���༭����ӿ�
 * @author mlr
 *
 */
public interface ISonEenentHandler {
	/**
	 * ��ť���ǰ�¼�
	 * @param btg BtnOK����    BtnAdd����    BtnDelɾ��  BtnCancelȡ��
	 * @throws BusinessException
	 */
  public void SonbeforeButtonClick(String btg)throws BusinessException;
  /**
   * ��ť������¼� 	   
   * @param btg   BtnOK����    BtnAdd����    BtnDelɾ��  BtnCancelȡ�� 
   * @throws BusinessException
   */
  public void SonafterButtonClick(String btg)throws BusinessException;
}
