package nc.itf.zmpub.pub;
import java.util.List;
/**
 * ʵ�������
 * �ӱ�vo����ʵ�ָýӿ�
 * @author mlr
 */
public interface ISonVO {
  /**
   * ���������Ϣ
   * @return
   */
  public abstract List getSonVOS();
  /**
   * �����к�����
   * @return
   */
  public abstract String getRowNumName(); 
  /**
   * ���������Ϣ
   * @param list
   */
  public abstract void  setSonVOS(List list);
}
