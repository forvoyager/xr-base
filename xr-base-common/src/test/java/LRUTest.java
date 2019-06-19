import com.xr.base.core.cache.LRUCache;
import org.junit.Test;

/**
 * @Time: 2019-03-28 13:33
 * @Author: forvoyager@outlook.com
 * @Description:
 */
public class LRUTest {

  @Test
  public void test_lru(){
    final LRUCache<String, Object> cache = new LRUCache<>(5);

    for(int i = 0;i<30; i++){
      cache.put(String.valueOf(i), i);
      System.out.println(cache);
    }

  }

}
