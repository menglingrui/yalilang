package nc.vo.zmpub.formula;

import java.io.Serializable;

import nc.vo.pub.BeanHelper;

/**
 * ����vo����
 * @author zpm
 *
 */
public class SystemfunctionVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String name  = "";//ϵͳ��������
	
	private String code = "";//ϵͳ��������
	
	private String readme = "";//ϵͳ���麬��˵��

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}
	
	@Override
	public String toString() {
		return code;
	}
	public void setAttributeValue(String attributeName, Object value) {
		BeanHelper.setProperty(this, attributeName, value);
	}
}
