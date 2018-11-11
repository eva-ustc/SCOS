package es.source.code.scos;

import java.util.Map;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos
 * @date 2018/10/28 15:24
 * @description God Bless,No Bug!
 */
public class EventMessage {
    private int content;
    private Map<String,Object> foodInfo;

    public Map<String, Object> getFoodInfo() {
        return foodInfo;
    }

    public void setFoodInfo(Map<String, Object> foodInfo) {
        this.foodInfo = foodInfo;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
