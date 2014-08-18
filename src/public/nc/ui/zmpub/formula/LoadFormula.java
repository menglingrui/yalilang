package nc.ui.zmpub.formula;

import java.awt.Container;

import nc.ui.pub.beans.MessageDialog;

/**
 * ��ʽ���ء��༭
 * @author zpm
 *
 */
public class LoadFormula {
	
	private Container parent = null;
	
	private String title = null;
	
	private String formuDesc = null;
	
	private String formuCode = null;
	
	private boolean flag = true;
	
	public LoadFormula(Container parent,String title,String formuDesc, String formuCode){
		initData(parent,title,formuDesc, formuCode);
	}
	
	/**
	 * ��ʼ����ʽ����
	 */
	public void  initData(Container parent,String title,String formuDesc, String formuCode){
		this.parent = parent;
		this.title = title;
		this.formuDesc = formuDesc;
		this.formuCode = formuCode;
	}

	
	public boolean showModal(){
		try{
			loadFormula();
		}catch(Exception e){
			MessageDialog.showErrorDlg(parent, "����", e.getMessage());
			flag = false;
		}
		return flag;
	}
	/**
	 * ���ع�ʽ 
	 */
	public  void loadFormula() throws Exception{
		FormulaDlg dlg = new FormulaDlg(parent);
		dlg.initData(ReadXML.getFunctionvos(),ReadXML.getFormulaDefvo(),ReadXML.getPZDefvo(),title,formuDesc,formuCode);
		returnParentUI(dlg);
	}
	
	public void returnParentUI(FormulaDlg dlg){
		int iRes = dlg.showModal();
		if (iRes == MessageDialog.ID_OK) {
			formuDesc = dlg.getFormulaName();
			formuCode = dlg.getFormulaCode();
		}
	}

	public String getFormuDesc() {
		return formuDesc;
	}

	public void setFormuDesc(String formuDesc) {
		this.formuDesc = formuDesc;
	}

	public String getFormuCode() {
		return formuCode;
	}

	public void setFormuCode(String formuCode) {
		this.formuCode = formuCode;
	}
}
