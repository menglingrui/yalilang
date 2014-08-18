package nc.ui.zmpub.pub.bill;

import nc.vo.pub.BusinessException;

/**
 * 孙表编辑处理接口
 * @author mlr
 *
 */
public interface ISonEenentHandler {
	/**
	 * 按钮点击前事件
	 * @param btg BtnOK增加    BtnAdd增加    BtnDel删除  BtnCancel取消
	 * @throws BusinessException
	 */
  public void SonbeforeButtonClick(String btg)throws BusinessException;
  /**
   * 按钮点击后事件 	   
   * @param btg   BtnOK增加    BtnAdd增加    BtnDel删除  BtnCancel取消 
   * @throws BusinessException
   */
  public void SonafterButtonClick(String btg)throws BusinessException;
}
