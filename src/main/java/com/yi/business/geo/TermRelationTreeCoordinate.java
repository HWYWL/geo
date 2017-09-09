package com.yi.business.geo;

import com.yi.tools.FileUtil;
import com.yi.tools.SimilarityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;


public class TermRelationTreeCoordinate {
	public static String[] shenglevel = { "省", "直辖市", "自治区", "特别行政区" };
	public static String COUNTRYLEVEL = "国家";
	public static List<String> SHENGLEVEL = Arrays.asList(shenglevel);
	public static String CITYLEVEL = "市";
	public static String COUNTYLEVEL = "县";
	public static String TOWNLEVEL = "镇";
    private static final  double EARTH_RADIUS = 6378137;    //赤道半径
	public static Log log = LogFactory.getLog(TermRelationTreeCoordinate.class);
	private ReentrantLock lock = new ReentrantLock();
	private boolean runnable = false;
	private static HashMap<String, String[]> code_map = new HashMap<String, String[]>();
	private SpotNode termRelationTreeHead = null;
	private static Map<String, SpotUnit> codeRelationTreeHead = new HashMap<>();;
	private boolean initTreeCompleted = false;
	private HashMap<String, SpotUnit> code_spot_map = new HashMap<String, SpotUnit>();
	public static TermRelationTreeCoordinate tree = new TermRelationTreeCoordinate();

	public boolean isRunnable() {
		return runnable;
	}

	public SpotNode getTermRelationTreeHead() {
		return termRelationTreeHead;
	}

    public boolean isInitTreeCompleted() {
		return initTreeCompleted;
	}

	public void setInitTreeCompleted(boolean initTreeCompleted) {
		this.initTreeCompleted = initTreeCompleted;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public TermRelationTreeCoordinate() {
		super();
		lock.lock();
		read_spot_info();
		lock.unlock();
		termRelationTreeHead = null;
		if (runnable == true) {
			initialTree();
		}

	}

	// 从文件中读入地点信息
	private void read_spot_info() {
		// 从配置文件中读出loc_info的路径
		InputStream in = TermRelationTreeCoordinate.class.getClassLoader().getResourceAsStream("loc_info_coordinate.txt");

		// 把loc_info的信息存入到HashMap里，以code为key值，以这行的信息为value值
		List<String> locInfoList = null;
		try {
			locInfoList = FileUtil.readFileFromInputStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i = 0;
		// HashMap<String, String[]> code_map = new HashMap<String, String[]>();
		for (String temp : locInfoList) {
			i++;
			String loc_info = temp.trim();
			String[] loc_infos = loc_info.split("\t");
			if (loc_infos[0].equals("#")) {
				log.info("loc_info文件里，第" + i + "行是注释");
				continue;
			}
			if (loc_infos.length < 7) {
				log.error("loc_info文件里，第" + i + "行的长度 小于7");
				continue;
			}
			String code = loc_infos[0];
			String name = loc_infos[1];
			String sname = loc_infos[2];
			String suffix = loc_infos[3];
			String alias = loc_infos[4];
			String pcode = loc_infos[5];
			String level = loc_infos[6];
			String coordinate1 = "";
			String coordinate2 = "";
			String coordinate3 = "";
			String coordinate4 = "";
			if (loc_infos.length == 8) {
				coordinate1 = loc_infos[7];
			} else if (loc_infos.length == 9) {
				coordinate1 = loc_infos[7];
				coordinate2 = loc_infos[8];
			} else if (loc_infos.length == 10) {
				coordinate1 = loc_infos[7];
				coordinate2 = loc_infos[8];
				coordinate3 = loc_infos[9];
			} else if (loc_infos.length == 11) {
				coordinate1 = loc_infos[7];
				coordinate2 = loc_infos[8];
				coordinate3 = loc_infos[9];
				coordinate4 = loc_infos[10];
			}

			String[] value = { name, sname, suffix, alias, pcode, level, coordinate1, coordinate2, coordinate3, coordinate4 };
			code_map.put(code, value);
		}

		Iterator<Entry<String, String[]>> iter = code_map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			String code = entry.getKey();
			String[] loc_infos = entry.getValue();
			String name = loc_infos[0];
			String sname = loc_infos[1];
			String suffix = loc_infos[2];
			String alias = loc_infos[3];
			String pcode = loc_infos[4];
			String level = loc_infos[5];
			String coordinate1 = loc_infos[6];
			String coordinate2 = loc_infos[7];
			String coordinate3 = loc_infos[8];
			String coordinate4 = loc_infos[9];
			Coordinates coordinates = new Coordinates(coordinate1, coordinate2, coordinate3, coordinate4);
			List<String> aliasList = new ArrayList<String>();
			for (String temp : alias.split(" ")) {
				aliasList.add(temp);
			}
			// 遍历code_map，为每个元素都添加完整的parent_codes
			List<String> parent_codes = new ArrayList<String>();
			parent_codes.add(pcode);
			// 国家-省，直辖市，自治区-市-区-镇。最多5级的结构
			if (!pcode.equals("1000000000") && !pcode.equals("0")) {
				String p2code = code_map.get(pcode)[4];
				parent_codes.add(p2code);
				if (!p2code.equals("1000000000") && !p2code.equals("0")) {
					String p3code = code_map.get(p2code)[4];
					parent_codes.add(p3code);
					if (!p3code.equals("1000000000") && !p3code.equals("0")) {
						String p4code = code_map.get(p3code)[4];
						parent_codes.add(p4code);
						if (!p4code.equals("1000000000") && !p4code.equals("0")) {
							String p5code = code_map.get(p4code)[4];
							parent_codes.add(p5code);
						}
					}
				}
			}
			int is_alias_flag = 0;
			SpotUnit spotUnit = new SpotUnit(code, name, sname, suffix, aliasList, parent_codes, level, is_alias_flag, coordinates);
			code_spot_map.put(code, spotUnit);
		}
		if (code_spot_map.size() < 1) {
			runnable = false;
		} else {
			runnable = true;
		}
	}

	private boolean initialTree() {
		if (code_spot_map == null || code_spot_map.size() < 1) {
			log.error("code_spot_map是空或size小于1");
			return false;
		}
		termRelationTreeHead = new SpotNode();
		SpotNode treeHeadp = termRelationTreeHead;

		Iterator<Entry<String, SpotUnit>> iterator = code_spot_map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, SpotUnit> entry = iterator.next();
			SpotUnit spotUnit = entry.getValue();
			SpotNode p = treeHeadp;
			String name = spotUnit.getName();
			int name_len = name.length();
			if (name_len == 1) {
				continue;
			}

			//简历地区编码索引
            codeRelationTreeHead.put(spotUnit.getCode(), spotUnit);

			// 建立地名的索引
			for (int i = 0; i < name_len; i++) {
				char c = name.charAt(i);
				HashMap<Character, SpotNode> map = p.getChild_list();
				SpotNode child = map.get(c);
				if (child == null) {
					map.put(c, new SpotNode());
					p.setChild_list(map);
					p = p.getChild_list().get(c);
				} else {
					p = child;
				}
			}

			p.setIs_spot(1);
			for (String pcode : spotUnit.getParent_unit()) {
				SpotUnit pUnit = code_spot_map.get(pcode);
				if (pUnit != null) {
					List<SpotUnit> parent_index = spotUnit.getParent_index();
					parent_index.add(pUnit);
					spotUnit.setParent_index(parent_index);
				}
			}
			List<SpotUnit> info = p.getInfo();
			info.add(spotUnit);
			p.setInfo(info);

            // 不填补“TOWNLEVEL”镇级别和简称是空的
			if (spotUnit.getLevel().equals(TOWNLEVEL) || spotUnit.getSname().equals("")) {
				continue;
			}
			int bk_flag = 0;
			for (String pcode : spotUnit.getParent_unit()) {
				SpotUnit xpu = code_spot_map.get(pcode);
				if (xpu == null) {
					continue;
				}
				// 如果低级行政单位的简称和高级行政单位的简称相同，那么不对低级行政单位的简称做索引。
				// 例如1070000000 吉林省 吉林 省 1000000000 省
				// 1070800000 吉林市 吉林 市 1070000000 市
				// 不会对“吉林市”的简称“吉林”做索引，也就是说，用“吉林”去检索，只能找到“吉林省”
				if (xpu.getSname().equals(spotUnit.getSname())) {
					bk_flag = 1;
					break;
				}
			}
			if (bk_flag == 1) {
				continue;
			}
			// 建立地名简称的索引
			SpotNode sp = treeHeadp;
			String sname = spotUnit.getSname();
			int sname_len = sname.length();
			if (sname_len == 1) {
				continue;
			}
			for (int i = 0; i < sname_len; i++) {
				char c = sname.charAt(i);
				HashMap<Character, SpotNode> map = sp.getChild_list();
				SpotNode child = map.get(c);
				if (child == null) {
					map.put(c, new SpotNode());
					sp.setChild_list(map);
					sp = sp.getChild_list().get(c);
				} else {
					sp = child;
				}
			}
//			num_spots += 1;
			sp.setIs_spot(1);
			for (String pcode : spotUnit.getParent_unit()) {
				SpotUnit pUnit = code_spot_map.get(pcode);
				if (pUnit != null) {
					List<SpotUnit> parent_index = spotUnit.getParent_index();
					parent_index.add(pUnit);
					spotUnit.setParent_index(parent_index);
				}
			}
			List<SpotUnit> sinfo = sp.getInfo();
			sinfo.add(spotUnit);
			sp.setInfo(sinfo);
		}
		initTreeCompleted = true;
		return true;
	}

	public List<SpotItem> collectSpot(String srcStr, boolean overlap) {
		// overlap=true 表示全遍历全切分---找到一个词后从该词的第二个字再开始查找
		// overlap=false 表示非全遍历全切分---找到一个词后从该词词尾开始查找
		if (initTreeCompleted == false || termRelationTreeHead == null) {
			log.error("初始化失败");
			return null;
		}
		List<SpotItem> ugroup = new ArrayList<SpotItem>();
		int i = 0, b_flag = 0, len_srcStr = srcStr.length();
		// B match表示匹配到最后，A match未匹配到最后
		while (i < len_srcStr) {
			b_flag = 0;
			SpotNode curNode = termRelationTreeHead;
			int j = i, k = i;
			while (j < len_srcStr) {
				if (curNode.getIs_spot() == 1) {
					b_flag = 1;
					for (SpotUnit spotUnit : curNode.getInfo()) {
						// System.out.println("A match destination:名称是：" +
						// spotUnit.getName());
						SpotItem spotItem = new SpotItem(srcStr, spotUnit, i, j - 1);
						ugroup.add(spotItem);
					}
					k = j;
				}
				SpotNode nextNode = curNode.getChild_list().get(srcStr.charAt(j));
				if (nextNode == null) {
					break;
				} else {
					curNode = nextNode;
				}
				j += 1;
			}
			if (j >= len_srcStr) {
				if (curNode.getIs_spot() == 1) {
					b_flag = 1;
					for (SpotUnit spotUnit : curNode.getInfo()) {
						// System.out.println("B match destination:名称是：" +
						// spotUnit.getName());
						SpotItem spotItem = new SpotItem(srcStr, spotUnit, i, j - 1);
						ugroup.add(spotItem);
					}
					k = j;
				}
			}
			if (b_flag == 1) {
				if (overlap == true) {
					i += 1;
				} else {
					i = k;
				}
			} else {
				i += 1;
			}

		}
		return ugroup;
	}

	// 获取 uterm_group 关联的上级及上上级term 及所属level
	public static List<SpotUnit> getLevelAndParent(List<SpotItem> ugroup) {
		if (ugroup == null || ugroup.size() < 1) {
			log.info("getLevelAndParent处的参数是空或者size小于1");
			return null;
		}
		List<SpotUnit> parentSpot = new ArrayList<SpotUnit>();
		HashSet<String> pcodeSet = new HashSet<String>();
		HashSet<String> codeSet = new HashSet<String>();
		@SuppressWarnings("rawtypes")
		HashMap<String, ArrayList> snameMap = new HashMap<String, ArrayList>();
		for (SpotItem spotItem : ugroup) {
			String sname = spotItem.getSpotUnit().getSname();
			if (snameMap.containsKey(sname)) {
				snameMap.get(sname).set(0, ((Integer) snameMap.get(sname).get(0) + 1));
				List<SpotItem> a = (List<SpotItem>) snameMap.get(sname).get(1);
				a.add(spotItem);
				snameMap.get(sname).set(1, a);
			} else {
				ArrayList<Object> tList = new ArrayList<Object>();
				List<SpotItem> spotList = new ArrayList<SpotItem>();
				spotList.add(spotItem);
				tList.add((Integer) 1);
				tList.add(spotList);
				snameMap.put(sname, tList);
			}
			String code = spotItem.getSpotUnit().getCode();
			if (!codeSet.contains(code)) {
				codeSet.add(code);
			}
		}
		List<SpotItem> valide_group = new ArrayList<SpotItem>();
		@SuppressWarnings("rawtypes")
		Iterator<Entry<String, ArrayList>> iter = snameMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Entry<String, ArrayList> entry = iter.next();
			Integer num = (Integer) entry.getValue().get(0);
			List<SpotItem> valueSpotItems = (List<SpotItem>) entry.getValue().get(1);
			if (num < 2) {
				SpotItem item = valueSpotItems.get(0);
				valide_group.add(item);
				for (SpotUnit spotUnit : item.getSpotUnit().getParent_index()) {
					String code = spotUnit.getCode();
					if (!pcodeSet.contains(code)) {
						pcodeSet.add(code);
					}
				}

			} else {
				int all_valide = 1;
				for (SpotItem spotItem : valueSpotItems) {
					List<String> pcodes = spotItem.getSpotUnit().getParent_unit();
					String nowCode = spotItem.getSpotUnit().getCode();
					if (pcodeSet.contains(nowCode)) {
						valide_group.add(spotItem);
						all_valide = 0;
						break;
					}
					for (String pcode : pcodes) {
						if (codeSet.contains(pcode)) {
							valide_group.add(spotItem);
							all_valide = 0;
							break;
						}

					}
				}
				if (all_valide == 1) {
					valide_group.addAll(valueSpotItems);
				}

			}

		}
		for (SpotItem spotItem : valide_group) {
			String level = spotItem.getSpotUnit().getLevel();
			// if (level.equals(COUNTYLEVEL) || level.equals(CITYLEVEL)||
			// level.equals(TOWNLEVEL)){//对市，县，镇的级别做补充
			if (level.equals(COUNTYLEVEL) || level.equals(CITYLEVEL)) {// 对市，县级别的做补充
				List<SpotUnit> pList = spotItem.getSpotUnit().getParent_index();
				if (pList.size() > 0) {
					for (SpotUnit spotUnit : pList) {
						if (!spotUnit.getLevel().equals(COUNTRYLEVEL)) {
							parentSpot.add(spotUnit);
						}

					}
				}
			}
			parentSpot.add(spotItem.getSpotUnit());
		}
		// 对parentSpot去重
		List<SpotUnit> parentSpot_qc = new ArrayList<SpotUnit>();
		for (SpotUnit spotUnit : parentSpot) {
			if (!parentSpot_qc.contains(spotUnit)) {
				parentSpot_qc.add(spotUnit);
			}
		}

		return parentSpot_qc;
	}

	public static GeoInfo getGeoInfo(List<SpotUnit> spotUnits, String target) {
		if (spotUnits != null && spotUnits.size() > 0) {
			String province = "";
			String city = "";
			String county = "";
			String town = "";
			HashMap<String, GeoInfo> prvGeoInfo = new HashMap<String, GeoInfo>();
			for (SpotUnit spotUnit : spotUnits) {
				if (SHENGLEVEL.contains(spotUnit.getLevel())) {
					province = spotUnit.getSname();
					String code = spotUnit.getCode();
					if (prvGeoInfo.containsKey(code)) {
						GeoInfo geoInfo = prvGeoInfo.get(code);
						geoInfo.setProvince(province);
						geoInfo.setProvinceName(spotUnit.getName());
					} else {
						GeoInfo geoInfo = new GeoInfo();
						geoInfo.setProvince(province);
						geoInfo.setProvinceName(spotUnit.getName());
						prvGeoInfo.put(code, geoInfo);
					}
				}
				if (CITYLEVEL.equals(spotUnit.getLevel())) {
					city = spotUnit.getSname();
					String pcode = code_map.get(spotUnit.getCode())[4];
					if (prvGeoInfo.containsKey(pcode)) {
						GeoInfo geoInfo = prvGeoInfo.get(pcode);
						geoInfo.setCity(city);
						geoInfo.setCityName(spotUnit.getName());
					} else {
						GeoInfo geoInfo = new GeoInfo();
						geoInfo.setCity(city);
						geoInfo.setCityName(spotUnit.getName());
						prvGeoInfo.put(pcode, geoInfo);
					}
					// System.out.println("city:"+city);
				}
				if (COUNTYLEVEL.equals(spotUnit.getLevel())) {
					county = spotUnit.getSname();
					String pcode = code_map.get(spotUnit.getCode())[4];
					String p2code = code_map.get(pcode)[4];
					if (prvGeoInfo.containsKey(p2code)) {
						GeoInfo geoInfo = prvGeoInfo.get(p2code);
						geoInfo.setCounty(county);
						geoInfo.setCountyName(spotUnit.getName());
					} else {
						GeoInfo geoInfo = new GeoInfo();
						geoInfo.setCounty(county);
						geoInfo.setCountyName(spotUnit.getName());
						prvGeoInfo.put(p2code, geoInfo);
					}
				}
				if (TOWNLEVEL.equals(spotUnit.getLevel())) {
					town = spotUnit.getSname();
					String pcode = code_map.get(spotUnit.getCode())[4];
					String p3code = code_map.get(code_map.get(pcode)[4])[4];
					if (prvGeoInfo.containsKey(p3code)) {
						GeoInfo geoInfo = prvGeoInfo.get(p3code);
						geoInfo.setTown(town);
						geoInfo.setTownName(spotUnit.getName());
					} else {
						GeoInfo geoInfo = new GeoInfo();
						geoInfo.setTown(town);
						geoInfo.setTownName(spotUnit.getName());
						prvGeoInfo.put(p3code, geoInfo);
					}
					// System.out.println("town:"+town);
				}
			}
			double maxSimilar = 0.0;
			GeoInfo maxSimilarGeoInfo = new GeoInfo();
			for (String prv : prvGeoInfo.keySet()) {
				GeoInfo geoInfo = prvGeoInfo.get(prv);
				double similar = SimilarityUtil.sim(target, geoInfo.toStringCaluateSimilar());
				// System.out.println(similar+target+"\t"+geoCodeInfo.toStringCaluateSimilar());
				if (similar > maxSimilar) {
					maxSimilar = similar;
					maxSimilarGeoInfo = geoInfo;
				}
			}
			return maxSimilarGeoInfo;
		} else {
			return null;
		}
	}

	public static GeoCodeInfo getGeoCodeInfo(List<SpotUnit> spotUnits, String target) {
		if (spotUnits != null && spotUnits.size() > 0) {
			String province = "";
			String city = "";
			String county = "";
			String town = "";
			String province_code = "";
			String city_code = "";
			String county_code = "";
			String town_code = "";
			int flag = 0;// 用来标记经纬度来自哪一个地方,第一位代表省，第二位代表市，第三位代表区，第四位代表镇
			HashMap<String, GeoCodeInfo> prvGeoCodeInfo = new HashMap<String, GeoCodeInfo>();
			for (SpotUnit spotUnit : spotUnits) {
				if (SHENGLEVEL.contains(spotUnit.getLevel())) {
					// province = spotUnit.getName();
					province = spotUnit.getSname();
					province_code = spotUnit.getCode();
					if (prvGeoCodeInfo.containsKey(province_code)) {
						GeoCodeInfo geoCodeInfo = prvGeoCodeInfo.get(province_code);
						geoCodeInfo.setProvince(province);
						geoCodeInfo.setProvinceName(spotUnit.getName());
						geoCodeInfo.setProvince_code(province_code);
						geoCodeInfo.getCoordinates().setCoordinateA(spotUnit.getCoordinates().getCoordinateA());
					} else {
						GeoCodeInfo geoCodeInfo = new GeoCodeInfo();
						geoCodeInfo.setProvince(province);
						geoCodeInfo.setProvinceName(spotUnit.getName());
						geoCodeInfo.setProvince_code(province_code);
						geoCodeInfo.getCoordinates().setCoordinateA(spotUnit.getCoordinates().getCoordinateA());
						prvGeoCodeInfo.put(province_code, geoCodeInfo);
					}
					// System.out.println("province:"+province);
				} else if (CITYLEVEL.equals(spotUnit.getLevel())) {
					city = spotUnit.getSname();
					city_code = spotUnit.getCode();
					String pcode = code_map.get(city_code)[4];
					if (prvGeoCodeInfo.containsKey(pcode)) {
						GeoCodeInfo geoCodeInfo = prvGeoCodeInfo.get(pcode);
						geoCodeInfo.setCity(city);
						geoCodeInfo.setCityName(spotUnit.getName());
						geoCodeInfo.setCity_code(city_code);
						geoCodeInfo.getCoordinates().setCoordinateB(spotUnit.getCoordinates().getCoordinateA());
					} else {
						GeoCodeInfo geoCodeInfo = new GeoCodeInfo();
						geoCodeInfo.setCity(city);
						geoCodeInfo.setCityName(spotUnit.getName());
						geoCodeInfo.setCity_code(city_code);
						geoCodeInfo.getCoordinates().setCoordinateB(spotUnit.getCoordinates().getCoordinateA());
						prvGeoCodeInfo.put(pcode, geoCodeInfo);
					}
				} else if (COUNTYLEVEL.equals(spotUnit.getLevel())) {
					county = spotUnit.getSname();
					county_code = spotUnit.getCode();
					String p2code = code_map.get(code_map.get(county_code)[4])[4];
					if (prvGeoCodeInfo.containsKey(p2code)) {
						GeoCodeInfo geoCodeInfo = prvGeoCodeInfo.get(p2code);
						geoCodeInfo.setCounty(county);
						geoCodeInfo.setCountyName(spotUnit.getName());
						geoCodeInfo.setCounty_code(county_code);
						geoCodeInfo.getCoordinates().setCoordinateC(spotUnit.getCoordinates().getCoordinateA());
					} else {
						GeoCodeInfo geoCodeInfo = new GeoCodeInfo();
						geoCodeInfo.setCounty(county);
						geoCodeInfo.setCountyName(spotUnit.getName());
						geoCodeInfo.setCounty_code(county_code);
						geoCodeInfo.getCoordinates().setCoordinateC(spotUnit.getCoordinates().getCoordinateA());
						prvGeoCodeInfo.put(p2code, geoCodeInfo);
					}
				} else if (TOWNLEVEL.equals(spotUnit.getLevel())) {
					town = spotUnit.getSname();
					town_code = spotUnit.getCode();
					String p2code = code_map.get(code_map.get(town_code)[4])[4];
					String p3code = code_map.get(p2code)[4];
					if (prvGeoCodeInfo.containsKey(p3code)) {
						GeoCodeInfo geoCodeInfo = prvGeoCodeInfo.get(p3code);
						geoCodeInfo.setTown(town);
						geoCodeInfo.setTownName(spotUnit.getName());
						geoCodeInfo.setTown_code(town_code);
						geoCodeInfo.getCoordinates().setCoordinateD(spotUnit.getCoordinates().getCoordinateA());
					} else {
						GeoCodeInfo geoCodeInfo = new GeoCodeInfo();
						geoCodeInfo.setTown(town);
						geoCodeInfo.setTownName(spotUnit.getName());
						geoCodeInfo.setTown_code(town_code);
						geoCodeInfo.getCoordinates().setCoordinateD(spotUnit.getCoordinates().getCoordinateA());
						prvGeoCodeInfo.put(p3code, geoCodeInfo);
					}
					// System.out.println("town:"+town);
				}
			}
			double maxSimilar = 0.0;
			GeoCodeInfo maxSimilarGeoCodeInfo = new GeoCodeInfo();
			for (String prv : prvGeoCodeInfo.keySet()) {
				GeoCodeInfo geoCodeInfo = prvGeoCodeInfo.get(prv);
				double similar = SimilarityUtil.sim(target, geoCodeInfo.toStringCaluateSimilar());
				// System.out.println(similar+target+"\t"+geoCodeInfo.toStringCaluateSimilar());
				if (similar > maxSimilar) {
					maxSimilar = similar;
					maxSimilarGeoCodeInfo = geoCodeInfo;
				}
			}
			return maxSimilarGeoCodeInfo;
		} else {
			return null;
		}
	}

    public List<SpotItem> collectCode(String codeStr) {
        if (initTreeCompleted == false || termRelationTreeHead == null) {
            log.error("初始化失败");
            return null;
        }
        System.out.println(termRelationTreeHead);
        List<SpotItem> ugroup = new ArrayList<SpotItem>();

        return ugroup;
    }

	public static GeoInfo completeGeo(String placeStr) {
		List<SpotUnit> termList = new ArrayList<SpotUnit>();
		List<SpotItem> ugroup = tree.collectSpot(placeStr, false);
		termList = TermRelationTreeCoordinate.getLevelAndParent(ugroup);
		GeoInfo geo = TermRelationTreeCoordinate.getGeoInfo(termList, placeStr);
		return geo;
	}

	public static GeoCodeInfo completeGeoCode(String placeStr) {
		List<SpotUnit> termList = new ArrayList<SpotUnit>();
		List<SpotItem> ugroup = tree.collectSpot(placeStr, false);
		termList = TermRelationTreeCoordinate.getLevelAndParent(ugroup);
		GeoCodeInfo geoCode = tree.getGeoCodeInfo(termList, placeStr);
		return geoCode;
	}

	public static String geoCodeComplete(String codeStr) {
        SpotUnit spotUnit = codeRelationTreeHead.get(codeStr);

        return spotUnit.getName();
	}

    /**
     * 计算两个地区之间的距离此计算方法来自 拒绝飞的鸟
     * @return
     */
    public static double GetDistance(String placeStart, String placeEnd) {
        double lon1, lat1, lon2, lat2;
        GeoCodeInfo placeStartGeoCodeInfo = completeGeoCode(placeStart);
        GeoCodeInfo placeEndGeoCodeInfo = completeGeoCode(placeEnd);
        if (placeStartGeoCodeInfo == null || placeEndGeoCodeInfo == null){
            return -1;
        }
        List<Double> placeStartList = longitudeLatitude(placeStartGeoCodeInfo);
        List<Double> placeEndList = longitudeLatitude(placeEndGeoCodeInfo);

        lat1 = placeStartList.get(0);
        lon1 = placeStartList.get(1);
        lat2 = placeEndList.get(0);
        lon2 = placeEndList.get(1);

        if (lat1 == 0.0 || lon1 == 0.0 || lat2 == 0.0 || lon2 == 0.0){
            return -1;
        }

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        return s;//单位米
    }

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }

    private static List<Double> longitudeLatitude (GeoCodeInfo geoCodeInfo){
        List<Double> list = new ArrayList<>();
        String provinceCode = geoCodeInfo.getProvince_code();
        String cityCode = geoCodeInfo.getCity_code();
        String countyCode = geoCodeInfo.getCounty_code();
        String townCode = geoCodeInfo.getTown_code();
        Coordinates coordinates = geoCodeInfo.getCoordinates();

        if (!townCode.isEmpty()){
            list.add(Double.valueOf(coordinates.getCoordinateD().getLatitude()));
            list.add(Double.valueOf(coordinates.getCoordinateD().getLongitude()));
        }else if (!countyCode.isEmpty()){
            list.add(Double.valueOf(coordinates.getCoordinateC().getLatitude()));
            list.add(Double.valueOf(coordinates.getCoordinateC().getLongitude()));
        }else if (!cityCode.isEmpty()){
            list.add(Double.valueOf(coordinates.getCoordinateB().getLatitude()));
            list.add(Double.valueOf(coordinates.getCoordinateB().getLongitude()));
        }else if (!provinceCode.isEmpty()){
            list.add(Double.valueOf(coordinates.getCoordinateA().getLatitude()));
            list.add(Double.valueOf(coordinates.getCoordinateA().getLongitude()));
        }else {
            list.add(0.0);
            list.add(0.0);
        }

        return list;
    }
}
