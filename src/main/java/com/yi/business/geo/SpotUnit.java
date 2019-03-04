package com.yi.business.geo;

import java.util.ArrayList;
import java.util.List;

public class SpotUnit {
    private String code = "";
    private String name = "";
    private String sname = "";
    private String suffix = "";
    private List<String> aliasList = new ArrayList<String>();
    private List<String> parent_unit = new ArrayList<String>();
    private String level = "";
    private int is_alias_flag = 0;
    private List<SpotUnit> parent_index = new ArrayList<SpotUnit>();
    private Coordinates coordinates = new Coordinates();

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getIs_alias_flag() {
        return is_alias_flag;
    }

    public SpotUnit() {

    }

    public SpotUnit(String code, String name, String sname, String suffix, List<String> aliasList, List<String> parent_unit, String level, int is_alias_flag) {
        super();
        this.code = code;
        this.name = name;
        this.sname = sname;
        this.suffix = suffix;
        this.aliasList = aliasList;
        this.parent_unit = parent_unit;
        this.level = level;
        this.is_alias_flag = is_alias_flag;
    }

    public SpotUnit(String code, String name, String sname, String suffix, List<String> aliasList, List<String> parent_unit, String level, int is_alias_flag, Coordinates coordinates) {
        super();
        this.code = code;
        this.name = name;
        this.sname = sname;
        this.suffix = suffix;
        this.aliasList = aliasList;
        this.parent_unit = parent_unit;
        this.level = level;
        this.is_alias_flag = is_alias_flag;
        this.coordinates = coordinates;
    }

    public SpotUnit(String code, String name, String sname, String suffix, List<String> aliasList, List<String> parent_unit, String level, int is_alias_flag, List<SpotUnit> parent_index,
                    Coordinates coordinates) {
        super();
        this.code = code;
        this.name = name;
        this.sname = sname;
        this.suffix = suffix;
        this.aliasList = aliasList;
        this.parent_unit = parent_unit;
        this.level = level;
        this.is_alias_flag = is_alias_flag;
        this.parent_index = parent_index;
        this.coordinates = coordinates;
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

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<String> getAliasList() {
        return aliasList;
    }

    public void setAliasList(List<String> aliasList) {
        this.aliasList = aliasList;
    }

    public List<String> getParent_unit() {
        return parent_unit;
    }

    public void setParent_unit(List<String> parent_unit) {
        this.parent_unit = parent_unit;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int isIs_alias_flag() {
        return is_alias_flag;
    }

    public void setIs_alias_flag(int is_alias_flag) {
        this.is_alias_flag = is_alias_flag;
    }

    public List<SpotUnit> getParent_index() {
        return parent_index;
    }

    public void setParent_index(List<SpotUnit> parent_index) {
        this.parent_index = parent_index;
    }

}
