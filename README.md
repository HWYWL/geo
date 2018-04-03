# geo
地区与该地区编码的相互转换

## 特性

- 简洁的转换API
- 支持地区名称转换为该地区的编码
- 支持地区编码转换为该地区的名称
- 支持获得该地区的上级所属地区
- 支持通过两个地区的的地址获得其之间的距离

## 使用

*通过maven工程直接引入**


## 举个栗子🌰

```java

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

    /**
     * 通过ip获得位置信息
     */
    @Test
    public void testPlaceName8(){
        String s = IP2Location.ip2Location("119.130.230.20");

        System.out.println(s);
    }

    /**
     * 通过两个ip获得位置大概的距离
     */
    @Test
    public void testPlaceName9(){
        String ip1 = "119.130.230.20";
        String ip2 = "119.120.230.20";
        double distance = IP2Location.ip2Location(ip1, ip2);

        System.out.println(IP2Location.ip2Location(ip1) + " 距离 " + IP2Location.ip2Location(ip2) + ": " + distance / 1000.0 + "千米");
    }
```

### 执行结果
![](https://i.imgur.com/HctxHqu.jpg)

### 性能
![](https://i.imgur.com/8ypQ38s.jpg)

- 第一次执行时会比较慢，在初始化之后访问都在4ms以下

## 问题建议

- 联系我的邮箱：ilovey_hwy@163.com

