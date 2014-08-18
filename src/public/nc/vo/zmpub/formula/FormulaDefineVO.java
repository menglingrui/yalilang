package nc.vo.zmpub.formula;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.BeanHelper;

/**
 * ��ʽ����VO
 * @author zpm
 *
 */
public class FormulaDefineVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code = null;//����
	
	private String name = null;//����
	
	private String classname = null;//ʵ����
	
	private String parentname = null;//��ʾ����

	private List<FormulaDefineVO> list = null;//����
	
	private boolean isFinalevel = false;//�Ƿ����ռ���
	
	private String showcode = null;//�Ƿ���ʾ�������
	
	private String summaryType = null;//���� ��ʽ
	
	private String attriname;//ָ��Ҫ���ص��ֶ�
	private String csoureType;//���ݵ�������--������Դ���ݻ�����Ϣ��ѯ��ʱ����Ҫ
	private String iqushutype;//ȡ������(1=�տ2=Ӧ�պ�����ϸ,3=��Ʒ����,4=��������,5=Ӧ�յ�)
	private String invcode = null;//�������

	public String getIqushutype() {
		return iqushutype;
	}

	public void setIqushutype(String iqushutype) {
		this.iqushutype = iqushutype;
	}

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public String getShowcode() {
		return showcode;
	}

	public void setShowcode(String showcode) {
		this.showcode = showcode;
	}

	public boolean isFinalevel() {
		if(list == null || list.size() == 0){
			isFinalevel = true;
		}
		return isFinalevel;
	}

	public void setFinalevel(boolean isFinalevel) {
		this.isFinalevel = isFinalevel;
	}

	public void setAttributeValue(String attributeName, Object value) {
		BeanHelper.setProperty(this, attributeName, value);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public List<FormulaDefineVO> getFormualdefvo() {
		return list;
	}

	public void setFormualdefvo(List<FormulaDefineVO> list) {
		this.list = list;
	}
	
	public void addFormulaVO(FormulaDefineVO vo){
		if(list == null){
			list = new ArrayList<FormulaDefineVO>();
		}
		list.add(vo);
	}
	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}
	@Override
	public String toString() {
		if("Y".equals(getShowcode())){
			return invcode+name;
		}else {
			return name;
		}
	}

	
	public List<FormulaDefineVO> getList() {
		return list;
	}

	public void setList(List<FormulaDefineVO> list) {
		this.list = list;
	}

	public String getAttriname() {
		return attriname;
	}

	public void setAttriname(String attriname) {
		this.attriname = attriname;
	}

	public String getCsoureType() {
		return csoureType;
	}

	public void setCsoureType(String csoureType) {
		this.csoureType = csoureType;
	}
}
