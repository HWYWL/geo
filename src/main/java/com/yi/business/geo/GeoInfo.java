package com.yi.business.geo;

public class GeoInfo {
    // 这里是简称
    private String province = "";
    private String city = "";
    private String county = "";
    private String town = "";

    // 这里是全称
    private String provinceName = "";
    private String cityName = "";
    private String countyName = "";
    private String townName = "";

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public GeoInfo() {
        super();
    }

    public GeoInfo(String province, String city, String county, String town) {
        super();
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
    }

    public GeoInfo(String province, String city, String county) {
        super();
        this.province = province;
        this.city = city;
        this.county = county;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String toString() {
        return "省：" + province + "\t市：" + city + "\t区：" + county + "\t镇：" + town;
    }

    public String toStringCaluateSimilar() {
        return province + city + county + town;
    }

}
