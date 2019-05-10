package com.house.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.house.model.Floor;
import com.house.model.House;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;



/**
 * package com.house.map;
 * 
 * @author 孙傲
 * @date 2018年6月11日
 */
public class MapController extends BaseController {

	/**
	 * 测试地图渲染描边
	 * 
	 */
	public void index() {
		int i  = 0;
		if(!xx.isEmpty(getParaToInt("a"))) {
			i = getParaToInt("a");
		}

		setAttr("saMap", "厂区地图测试浏览");

		if(i==0) {
			render("/house/houseMap.html");
		}else {
			render("/house/houseMapLine"+i+".html");
		}
	}
	/**
	 * 地图预览
	 */
	public void mapHomeView() {
		
		String  jsonStr = JsonKit.toJson(Db.use(xx.DS_MAIN).find("select a.*,case when b.name is null then '无描述' else b.name end as name from h_location a left join h_house b on a.id = b.house_no ")).toString();
		Record r = Db.use(xx.DS_MAIN).findFirst("select * from v_status_all");
		//地图参数
		setAttr("mapinit", jsonStr);
		
		setAttr("bie", r);
		render("/house/mapHomeView.html");
		
	}
	
	/**
	 * 楼内情况预览
	 * 
	 */
	public void mapFloorView() {
		//楼信息
		
		setAttr("houses", House.dao.findFirst(" select * from h_house where house_no = ? ",getParaToInt("house_no")));
		//楼层信息
		setAttr("floors", Floor.dao.find(" select * from h_floor where house_no = ?  order by floor_no ",getParaToInt("house_no")));
		//统计
		setAttr("status", House.dao.findFirst(" select * from v_status where house_no = ? ",getParaToInt("house_no")));
		render("/house/mapFloorView.html");
	}
	
	
	/**
	 * 编辑地图坐标
	 */
	public void mapHome() {
		
		String  jsonStr = JsonKit.toJson(Db.use(xx.DS_MAIN).find("select * from h_location")).toString();
		setAttr("mapinit", jsonStr);
		render("/house/mapHome.html");
		
	}

	/**
	 * 获得地图坐标
	 */
	public void getMap() {
		//1.所有楼层

		renderJson(Db.use(xx.DS_MAIN).find("select a.*,b.name as name from h_location a,h_house b where a.id = b.house_no "));
	}
	
	/***
	 * 保存地图坐标
	 */
	public void saveMap(){
		/**
		 * 提交json字符串可以解析
		 */
		String jsonString = HttpKit.readData(getRequest());
		System.err.println("saveMap："+jsonString);
		@SuppressWarnings("rawtypes")
		List rl = FastJson.getJson().parse(jsonString, List.class);
		/**
		 * 
		 */
		@SuppressWarnings("rawtypes")
		Map map = null;
		List<Record> rMap = new ArrayList<>();
		
		List<Record> rlHouse = new ArrayList<>();
		Record r = null;
		Record rH = null;
		for (Object o : rl) {
			map =new HashMap<Object, Object>();
			r = new Record();
			rH = new Record();
			map = FastJson.getJson().parse(o.toString(), Map.class);
			int lh = Integer.parseInt(map.get("id").toString());
			r.set("id", lh);
			r.set("house_name", map.get("text").toString());
			r.set("pagex", map.get("pageX").toString());
			r.set("pagey", map.get("pageY").toString());
			r.set("house_color", map.get("color").toString());
			r.set("width", map.get("width").toString());
			r.set("height", map.get("height").toString());
			rMap.add(r);
			if(House.dao.getlhNum(lh)) {
				rH.set("house_no", Integer.parseInt(map.get("id").toString()));
				rH.set("name", map.get("text").toString());
				rH.set("edittime", new Date());
				rlHouse.add(rH);
			}

		}
		Record rMsg = new Record();
		if(House.dao.saveMap(rMap,rlHouse)){
			
			rMsg.set("flag", true);
		}else{
			rMsg.set("flag", false);
		}
		
		renderJson(rMsg);
	}
	
}
