package com.yi.business.geo;

public class GeoCodeInfo {
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

    private String province_code = "";
    private String city_code = "";
    private String county_code = "";
    private String town_code = "";

    private Coordinates coordinates = new Coordinates();

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public GeoCodeInfo() {
        super();
    }

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

    public GeoCodeInfo(String province, String city, String county, String town) {
        super();
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
    }

    public GeoCodeInfo(String province, String city, String county, String town, String provinceName, String cityName, String countyName, String townName, String province_code, String city_code,
                       String county_code, String town_code, Coordinates coordinates) {
        super();
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.countyName = countyName;
        this.townName = townName;
        this.province_code = province_code;
        this.city_code = city_code;
        this.county_code = county_code;
        this.town_code = town_code;
        this.coordinates = coordinates;
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
        return "省：" + province + "code:" + province_code + "\t市：" + city + "code:" + city_code + "\t县：" + county + "code:" + county_code + "\t镇：" + town + "code:" + town_code;
    }

    public String toStringCaluateSimilar() {
        return province + city + county + town;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCounty_code() {
        return county_code;
    }

    public void setCounty_code(String county_code) {
        this.county_code = county_code;
    }

    public String getTown_code() {
        return town_code;
    }

    public void setTown_code(String town_code) {
        this.town_code = town_code;
    }

}
