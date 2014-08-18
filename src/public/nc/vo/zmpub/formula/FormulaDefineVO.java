package nc.vo.zmpub.formula;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.BeanHelper;

/**
 * 公式定义VO
 * @author zpm
 *
 */
public class FormulaDefineVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code = null;//编码
	
	private String name = null;//名称
	
	private String classname = null;//实现类
	
	private String parentname = null;//显示父类

	private List<FormulaDefineVO> list = null;//子类
	
	private boolean isFinalevel = false;//是否最终级别
	
	private String showcode = null;//是否显示存货编码
	
	private String summaryType = null;//汇总 方式
	
	private String attriname;//指定要返回的字段
	private String csoureType;//依据单据类型--按照来源单据汇总信息查询的时候需要
	private String iqushutype;//取数类型(1=收款，2=应收核销明细,3=商品流向,4=返货处理,5=应收单)
	private String invcode = null;//存货编码

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
