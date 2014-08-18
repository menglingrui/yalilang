package nc.ui.zmpub.pub.bill;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.SuperVO;
/**
 * 孙表对话框类
 * @author mlr
 */
public class LogNumRefUFPanel extends UIRefPane {

	private static final long serialVersionUID = 3786378049066318161L;
    private SuperVO[] vos=null;
	private boolean m_bisClicked;
	private LotNumbDlg m_dlgLotNumb = null;
	private String classname=null;
    private String billtype;
	private String tile;
    
    public String getTile() {
		return tile;
	}
	public void setTile(String tile) {
		this.tile = tile;
	}
	public String getBilltype() {
		return billtype;
	}
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	public LogNumRefUFPanel() {
    	super();
    	setReturnCode(true);
    	getUITextField().setMaxLength(30);
    	this.setIsBatchData(false); 	
    }
    public LogNumRefUFPanel(String sonclassname,String billtype,String tile) {
    	super();
    	setReturnCode(true);
    	classname=sonclassname;
        this.billtype=billtype;
        this.tile=tile;
    	getUITextField().setMaxLength(30);
    	this.setIsBatchData(false); 	
    }
	public SuperVO[] getVos() {
		return vos;
	}

	public void setVos(SuperVO[] vos) {
		this.vos = vos;
	}

	/**
	 * 
	 */
	public void onButtonClicked() {
		try {
			getLotNumbDlg().setData(vos);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("Can not read data from server!");
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		if (getLotNumbDlg().showModal() == UIDialog.ID_OK) {
			if(getLotNumbDlg().getLis().size()>0){
			   getUITextField().setText("Y");
			   m_bisClicked = true;
			} else {
			   getLotNumbDlg().destroy();
			   m_bisClicked = false;
			   getUITextField().setText("N");
			}
		}
		getUITextField().setRequestFocusEnabled(true);
		getUITextField().grabFocus();
		return;	
	}
	public LotNumbDlg getLotNumbDlg() {
		 if (m_dlgLotNumb == null) {
				 m_dlgLotNumb = new LotNumbDlg(this.getParent(),true,classname,billtype,tile);
		 }
		return m_dlgLotNumb;

	}

}
