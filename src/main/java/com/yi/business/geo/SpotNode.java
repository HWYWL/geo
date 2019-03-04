package com.yi.business.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 地点节点
 * @author YI
 * @date 2019-2-28 21:03:49
 */
public class SpotNode {
    private int is_spot = 0;
    private HashMap<Character, SpotNode> child_list = new HashMap<Character, SpotNode>();
    private List<SpotUnit> info = new ArrayList<SpotUnit>();

    public SpotNode() {
        super();
    }

    public int getIs_spot() {
        return is_spot;
    }

    public void setIs_spot(int is_spot) {
        this.is_spot = is_spot;
    }

    public HashMap<Character, SpotNode> getChild_list() {
        return child_list;
    }

    public void setChild_list(HashMap<Character, SpotNode> child_list) {
        this.child_list = child_list;
    }

    public List<SpotUnit> getInfo() {
        return info;
    }

    public void setInfo(List<SpotUnit> info) {
        this.info = info;
    }

}
