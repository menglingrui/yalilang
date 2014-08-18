package nc.vo.zmpub.pub.tool;
import java.util.HashMap;
import nc.vo.scm.pu.PuPubVO;
/**
 * 重复性查数据库，缓存处理工具类。
 * 屏蔽重复查询，共享变化性概率小的数据，直接从缓存读取
 * 
 * 缓存作用域：服务端，只要服务器不重启，该缓存就起作用
 * @author mlr
 *
 */
public class MapCacheTool {
	/**
	 * 用map来进行缓存，全局变量，客户端和服务端共用
	 */
    private static HashMap<String, HashMap<String, Object>> map=new HashMap<String,HashMap<String,Object>>();
    /**
     * 重新设置全局缓存
     * @param map
     */
	public static void setMap(HashMap<String, HashMap<String, Object>> map) {
		MapCacheTool.map = map;
	}
    /**
     * 获得全局缓存
     * @return
     */
	public static HashMap<String, HashMap<String, Object>> getMap() {
		return map;
	}
	/**
	 * 根据key值获得对应缓存，如何不存在则，则新建
	 * @param key
	 * @return
	 */
	public static HashMap<String,Object> getMap(String key){
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null ){
		  return null;
		}
		if(map.get(key)==null ){
			HashMap<String,Object> smap=new HashMap<String,Object>();
			map.put(key, smap);
		}	
		return map.get(key);		
	}
	/**
	 * 根据key和skey获得缓存对象值
	 * @param key
	 * @param skey
	 * @return
	 */
	public static Object getMapObject(String key,String skey){
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null ){
		  return null;
		}
		if(map.get(key)==null ){
			HashMap<String,Object> smap=new HashMap<String,Object>();
			map.put(key, smap);
		}	
		return map.get(key).get(skey);		
	}
	/**
	 * 根据key和skey获得缓存对象值
	 * @param key
	 * @param skey
	 * @return
	 */
	public static void pubMapObject(String key,String skey,Object obj){
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null || PuPubVO.getString_TrimZeroLenAsNull(skey)==null ){
		  return ;
		}
		if(map.get(key)==null ){
			HashMap<String,Object> smap=new HashMap<String,Object>();
			
			map.put(key, smap);
		}	
		map.get(key).put(skey, obj);
	}
}
