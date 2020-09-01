package com.pushi.pushi08.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Admin
 * @version 1.0
 * @date 2020/7/11 23:50
 */
public class listResult {
    private Map<Integer, Object> content = new HashMap<>();

    public Map<Integer, Object> getContent() {
        return content;
    }

    public void setContent(Map<Integer, Object> content) {
        this.content = content;
    }
}
