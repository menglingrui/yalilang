package nc.bs.zmpub.pub.tool.mod;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
/**
 * �����Ϣע����
 * @author mlr
 */
public abstract class AccountBalanceData {
	   /**
	    * ��ȡ�½� ������ȫ·��
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����10:53:54
	    * @return
	    * @throws Exception
	    */
	   public abstract String getMonthAccountClass()throws Exception;
	   /**
	    * ע���½�  ���� �����id���ֶ�����
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����11:03:55
	    * @return
	    * @throws Exception
	    */
	   public abstract String getAcMonID() throws Exception;
	   /**
	    * ע���½�  ���Ż������
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����11:03:55
	    * @return
	    * @throws Exception
	    */
	   public abstract String getDyearmonthName() throws Exception;
	   /**
	    * ע���½�  ���� ����¿�ʼ��������
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����11:03:55
	    * @return
	    * @throws Exception
	    */
	   public abstract String getBegindateName() throws Exception;
	   /**
	    * ע���½�  ���� ����½�����������
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����11:03:55
	    * @return
	    * @throws Exception
	    */
	   public abstract String getEnddateName() throws Exception;
	   /**
	    * �׸ڿ�ҵ�����ز�ѯ���Ľ����� ����������� id
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����11:11:15
	    * @return
	    * @throws Exception
	    */
	   public abstract String getLastAccountMonthQuerySql(String corp) throws Exception;
	   /**
	    * ��ȡ�½��--->�ִ����ĵ�������
	    * @author mlr
	    * @˵�������׸ڿ�ҵ��
	    * 2011-10-31����02:05:26
	    * @return
	    * @throws Exception
	    */
	   public abstract String getChangeClass()throws Exception;
	   
	   
//	   /**
//	    * �Ƿ�����ڳ�����
//	    * @param pk_corp
//	    * @return
//	    * @throws BusinessException
//	    */
//	   public abstract boolean isloadPeridData(String pk_corp)throws BusinessException;
	  /**
	   * �����ڳ�����
	   * ��ʲô����¼����ڳ������أ�
<<<<<<< .mine
	   * �·ݽ���ʱ���һ�·��Ѿ��� һ�·ݵĽ��˱��п϶��Ѿ��������ڳ�����  
	   * ���һ�·��Ѿ����˵�����½���̨���޸��Ͳ�������ڳ�������
	   * ��Ϊ����ʱ:�Ǵӽ�����ȡ�ѽ��˵�����ת�����ִ���,�������һ������������Ĵ�ҵ�񵥾��ϻ�����������
	   * ���һ�·�δ������ô�ڳ��������޸���ʱ��,�ͼ��ز����ִ��������Ա����ֶ�����
=======
	   * 
	   * �·ݽ���ʱ ���һ�·� �Ѿ����� һ�·ݵĽ��˱��� �϶��Ѿ��������ڳ�����  
	   * 
	   * ���һ�·��Ѿ����˵�����½���̨���޸�  �Ͳ�������ڳ�������
	   * ��Ϊ ����ʱ  �� �Ǵӽ�����ȡ�ѽ��˵����� ת�����ִ���  ����  ���һ������������Ĵ�ҵ��
	   * �����ϻ�����������
	   * 
	   * ���һ�·�δ���� ��ô�ڳ����� ���޸���ʱ�� �ͼ��ز����ִ��������Ա����ֶ�����
>>>>>>> .r1004
	   * @author mlr
	   * @param  whereSql4 
	   * @˵�������׸ڿ�ҵ��
	   * 2011-11-1����12:47:10
	   * @return
	   * @throws Exception
	   */
	   public abstract SuperVO[]  loadPeridData(String whereSql4)throws Exception;	
}
