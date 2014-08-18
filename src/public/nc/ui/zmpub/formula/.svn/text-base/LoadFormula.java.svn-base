package nc.ui.zmpub.formula;

import java.awt.Container;

import nc.ui.pub.beans.MessageDialog;

/**
 * 公式加载、编辑
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
	 * 初始化公式数据
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
			MessageDialog.showErrorDlg(parent, "错误", e.getMessage());
			flag = false;
		}
		return flag;
	}
	/**
	 * 加载公式 
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
