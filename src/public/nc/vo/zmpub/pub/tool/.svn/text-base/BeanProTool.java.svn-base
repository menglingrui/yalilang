package nc.vo.zmpub.pub.tool;
import java.lang.reflect.Field;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * VO属性完全复制工具类
 * @author mlr
 */
public class BeanProTool {
   public static void  copyBeanParm(Object[] srcs,Object[] poss)throws Exception{
	   if(srcs==null || srcs.length==0 || poss==null || poss.length==0){
			  return;
	    } 
	   if(srcs.length!=poss.length){
		   throw new Exception("来源对象数组长度 和 目的对象数组长度  不一致");
	   }
	   for(int i=0;i<srcs.length;i++){
		   copyBeanParm(srcs[i],poss[i]);  
	   }
   }
	/**
	 * 将 sour的属性复制到dest中 
	 * @param sour
	 * @param dest
	 * @param sid //追加标示符
	 */
	public void BeanAttrCopy(CircularlyAccessibleValueObject sour, CircularlyAccessibleValueObject dest,String sid) {
		if(sour==null || dest==null)
			return;
		String[] attrnames=sour.getAttributeNames();
		if(attrnames==null || attrnames.length==0)
			return;
		for(int i=0;i<attrnames.length;i++){
			if(sid==null || sid.length()==0){
			    dest.setAttributeValue(attrnames[i],sour.getAttributeValue(attrnames[i]));	
			}else{
				dest.setAttributeValue(attrnames[i]+sid,sour.getAttributeValue(attrnames[i]));	
			}
		}
	}
  /**
   * 将  src 对象中的属性值 赋值到  pos中	
   * @param src 原对象  
   * @param pos 目的对象
   * @throws Exception
   */
  public static void  copyBeanParm(Object src,Object pos)throws BusinessException{
	  Class sc=src.getClass();
	  Class ps=pos.getClass();
	  Field[] sfields=sc.getFields();
	  Field[] pfields=ps.getFields();
	  if(sfields==null || sfields.length==0 || pfields==null || pfields.length==0){
		  return;
	  } 
	  for(int i=0;i<sfields.length;i++){
		  String name=sfields[i].getName();
		  for(int j=0;j<pfields.length;j++){
			  if(name.equalsIgnoreCase(pfields[j].getName())){
				  Object value;
				try {
					value = sfields[i].get(src);
					 pfields[j].set(pos,value);
				} catch (Exception e) {
					e.printStackTrace();
					throw new BusinessException(e.getMessage());
				}
				  break;
				}
		  }
	  }	   
   }
}
