package nc.vo.zmpub.listener;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.event.MouseInputListener;

import nc.ui.pub.bill.BillListPanel;
/**
 * 鼠标监听事件,该事件主要用于支持单据表头多选操作
 * @author mlr
 */
public class MouseClick implements MouseInputListener{
	BillListPanel panel=null;
	public MouseClick(BillListPanel blp) {
		panel=blp;
		panel.getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent em) {
       if(em.isShiftDown()){
    	   if(panel!=null){
    		   int[] rows=panel.getHeadTable().getSelectedRows();
    		   if(rows!=null&&rows.length>0){
    			   panel.getParentListPanel().selectTableRow(rows[0], rows[rows.length-1], false);
    		   }
    	   }
       } 
       if(em.isControlDown()){
    	   if(panel!=null){
    		   int[] rows=panel.getHeadTable().getSelectedRows();
    		   if(rows!=null&&rows.length>0){
    			   panel.getParentListPanel().selectTableRow(rows[0], rows[rows.length-1], true);
    		   }
    	   }  	   
       }
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}
	
}
