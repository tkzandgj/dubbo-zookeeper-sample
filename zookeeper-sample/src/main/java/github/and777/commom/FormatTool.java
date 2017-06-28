package github.and777.commom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author edliao on 2017/6/28.
 * @description 序列化
 */
public class FormatTool {

  public static Map<String, String> formatArray2Map(String[] titles, String[] datas) {
    return formatArray2Map(Arrays.asList(titles),Arrays.asList(datas));
  }

  public static Map<String, String> formatArray2Map(List<String> titles, List<String> datas) {
    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < (titles.size() < datas.size() ? titles.size() : datas.size()); i++) {
      map.put(titles.get(i), datas.get(i));
    }
    return map;
  }

}
