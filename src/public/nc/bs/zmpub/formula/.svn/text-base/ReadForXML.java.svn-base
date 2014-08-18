package nc.bs.zmpub.formula;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.gl.pfxx.XmlUtils;
import nc.bs.zmpub.pub.dao.ZmPubDAO;
import nc.vo.pub.BusinessException;
import nc.vo.zmpub.formula.FormulaDefineVO;
import nc.vo.zmpub.formula.SystemfunctionVO;
import nc.vo.zmpub.formula.ZMForlumaPathVO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 读取XML配置文件
 * @author mlr
 */
public class ReadForXML {
	
	private String NCHOME = RuntimeEnv.getInstance().getNCHome();
	
	private String file1=FormulaPath.file1;
	
	private String file2=FormulaPath.file2;
	
	private String file3=FormulaPath.file3;
	
	private String file4=FormulaPath.file4;
	
	public ReadForXML() {
		super();
		switchXmlPath();
	}
    /**
     * 更换xml路径
     */
	public void switchXmlPath() {
	  	try {
	  		/**
	  		 * 这里不需要设置缓存，因为缓存在调用类已经社设置
	  		 */
			List list=  (List) ZmPubDAO.getDAO().retrieveByClause(ZMForlumaPathVO.class, "isnull(dr,0)=0");
			if(list==null ||list.size()==0)
				return;
			HashMap<String, String> map=new HashMap<String, String>();
			for(int i=0;i<list.size();i++){
				 ZMForlumaPathVO vo=(ZMForlumaPathVO) list.get(i);		
			      map.put(vo.getVfilename(),NCHOME+vo.getVfilepath());	
			}
			if(map.get("file1")!=null&&map.get("file1").length()>0){
				file1=map.get("file1");
			}
	        if(map.get("file2")!=null&&map.get("file2").length()>0){
	        	file2=map.get("file2");
			}
	        if(map.get("file3")!=null&&map.get("file3").length()>0){
	        	file3=map.get("file3");
			}
	        if(map.get("file4")!=null&&map.get("file4").length()>0){
	        	file4=map.get("file4");
	        }
		} catch (DAOException e) {
			e.printStackTrace();
		}		
	}

	public ArrayList<FormulaDefineVO> readxml(Element element,FormulaDefineVO vo){
		NodeList rootlist = element.getChildNodes();
		if(rootlist == null) {
			return null;
		}
		ArrayList<FormulaDefineVO> list = new ArrayList<FormulaDefineVO>();
		for(int i = 0;i < rootlist.getLength(); i++) {
			Node node = rootlist.item(i);
			FormulaDefineVO definevo = new FormulaDefineVO();
			if(node.getNodeType()==Node.ELEMENT_NODE){
				NamedNodeMap attributes = node.getAttributes();
				for(int j = 0; attributes !=null && j < attributes.getLength(); j++){
					Node attribute = attributes.item(j);//属性
					String name = attribute.getNodeName();
					String value = attribute.getNodeValue();
					definevo.setAttributeValue(name, value);
				}
				if(vo!=null){
					vo.addFormulaVO(definevo);
				}
				readxml((Element)node,definevo);
				list.add(definevo);
			}
		}
		return list;
	}
	
	/**
	 * 读取zzfl_plugin.xml中的所有信息
	 * @return map
	 */
	public ArrayList<FormulaDefineVO> readInfoXml() throws BusinessException{
		ArrayList<FormulaDefineVO> list = null;
		try {
			
			Document doc = XmlUtils.getDocument(file1);
			Element root = doc.getDocumentElement();//根元素
			list = readxml(root,null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 读取zzfl_refmodel.xml中的所有信息
	 * @return map
	 */
	public ArrayList<FormulaDefineVO> readRefModelXml() throws BusinessException{
		ArrayList<FormulaDefineVO> list = null;
		try {
			Document doc = XmlUtils.getDocument(file3);
			Element root = doc.getDocumentElement();//根元素
			list = readxml(root,null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 读取 zzfl_pingzhong.xml文件
	 * @return map
	 */
	public ArrayList<FormulaDefineVO> readPZXML() throws BusinessException{
		ArrayList<FormulaDefineVO> list = null;
		try {
			Document doc = XmlUtils.getDocument(file4);
			Element root = doc.getDocumentElement();//根元素
			list = readxml(root,null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 读取systemfunction.xml中的所有信息
	 * @return map
	 */
	public ArrayList<SystemfunctionVO> readFunctionXML() throws BusinessException{
		ArrayList<SystemfunctionVO> list = null;
		try {
			Document doc = XmlUtils.getDocument(file2);
			Element root = doc.getDocumentElement();//根元素
			list = readSystemXML(root);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}
	
	public ArrayList<SystemfunctionVO> readSystemXML(Element root)throws Exception{
		//取到第一个元素
		Element child = XmlUtils.getFirstChildElement(root, "function");
		ArrayList<SystemfunctionVO> list = new ArrayList<SystemfunctionVO>();
		while(child != null){
			SystemfunctionVO systemvo = new SystemfunctionVO();
			NodeList rootlist = child.getChildNodes();
			for(int i = 0; rootlist != null && i < rootlist.getLength(); i++) {
				Node node = rootlist.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					String name = node.getNodeName();
					String value = node.getTextContent();
					systemvo.setAttributeValue(name, value);
				}
			}
			list.add(systemvo);
			child = XmlUtils.getNextSiblingElement(child, "function");
		}
		return list;
	}
}