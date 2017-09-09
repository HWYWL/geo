package com.yi.test;

import com.yi.business.geo.*;
import org.junit.Test;

import java.util.List;

/**
 * TestPlace on geo
 * @author hwy
 * @date 2017年9月9日21:18:10
 */
public class TestPlace {

    /**
     *将地址转为该地区编码
     */
    @Test
    public void testPlaceName1(){
        GeoCodeInfo geoCode = TermRelationTreeCoordinate.completeGeoCode("海南海口龙华区海外大厦");
        if (geoCode != null) {
            System.out.println(geoCode.toString());
        }else{
            System.out.println("无数据");
        }
    }

    /**
     *将地区编码转为该地址
     */
    @Test
    public void testPlaceName2(){
        String name = TermRelationTreeCoordinate.geoCodeComplete("1191700000");
        if(name !=null){
            System.out.println(name);
        }else {
            System.out.println("暂无数据");
        }
    }

    /**
     *将长地址拆分为短地址
     */
    @Test
    public void testPlaceName3(){
        GeoInfo geoInfo = TermRelationTreeCoordinate.completeGeo("广东广州天河区顺盈商业大厦");
        if (geoInfo != null) {
            System.out.println(geoInfo.toString());
        }else{
            System.out.println("不能补全");
        }
    }

    /**
     *根据地址获得短地址的地区编码和该地区所对应的经纬度
     */
    @Test
    public void testPlaceName4(){
        GeoCodeInfo geoCode = TermRelationTreeCoordinate.completeGeoCode("广东省韶关市翁源县官渡镇");
        if(geoCode !=null){
            System.out.println(geoCode.toString());
            System.out.println("经度："+geoCode.getCoordinates().getCoordinateA().getLongitude()+"\t纬度："+geoCode.getCoordinates().getCoordinateA().getLatitude());
            System.out.println("经度："+geoCode.getCoordinates().getCoordinateB().getLongitude()+"\t纬度："+geoCode.getCoordinates().getCoordinateB().getLatitude());
            System.out.println("经度："+geoCode.getCoordinates().getCoordinateC().getLongitude()+"\t纬度："+geoCode.getCoordinates().getCoordinateC().getLatitude());
            System.out.println("经度："+geoCode.getCoordinates().getCoordinateD().getLongitude()+"\t纬度："+geoCode.getCoordinates().getCoordinateD().getLatitude());
        }
    }

    /**
     * 根据短地址获得其所属上级的所有地址和编码
     */
    @Test
    public void testPlaceName5(){
        List<SpotItem> ugroup = TermRelationTreeCoordinate.tree.collectSpot("翁源县", false);
        List<SpotUnit> levelAndParent = TermRelationTreeCoordinate.getLevelAndParent(ugroup);
        if (levelAndParent != null){
            for (SpotUnit spotUnit:levelAndParent) {
                System.out.println("地区代码：" + spotUnit.getCode() + "\t地名：" + spotUnit.getName() + "\t所属区域：" + spotUnit.getParent_index().get(0).getName());
            }
        }
    }

    /**
     * 根据短地址获得其所属上级的所有地址(不包含国家)
     */
    @Test
    public void testPlaceName6(){
        GeoInfo geoInfo = TermRelationTreeCoordinate.completeGeo("翁源县");
        System.out.println(geoInfo);
        System.out.println("省：" + geoInfo.getProvinceName() + "\t市：" + geoInfo.getCityName() + "\t区/县：" + geoInfo.getCountyName() + "\t镇：" + geoInfo.getTownName());
    }

    /**
     * 根据两个地址计算距离(粗略计算)
     */
    @Test
    public void testPlaceName7(){
        String placeStart = "翁源县";
        String placeEnd = "广东广州天河区顺盈商业大厦";
        double distance = TermRelationTreeCoordinate.GetDistance(placeStart, placeEnd);
        if (distance != -1){
            System.out.println(placeStart + " 距离 " + placeEnd + distance / 1000.0 + "千米");
        }else {
            System.out.println("无此数据");
        }

    }
}
