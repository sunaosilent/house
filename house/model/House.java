package com.house.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.catalina.authenticator.SavedRequest;

import com.eova.common.base.BaseModel;
import com.eova.common.utils.xx;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * package com.house.model;
 * 
 * @author 孙傲
 * @date 2018年6月22日
 */
public class House extends BaseModel<House> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6663614734417648997L;

	public static final House dao = new House();

	/**
	 * 保存地图坐标
	 * 
	 * @param rl
	 *            提交修改的地图坐标和楼号
	 * @return
	 */
	@Before(Tx.class)
	public boolean saveMap(List<Record> rl, List<Record> rlHouse) {

		System.err.println("rl"+rl+"  rlHouse:"+rlHouse);
		int delFlag = Db.use(xx.DS_MAIN).update(" delete from h_location ");

		int addFlagNum = Db.use(xx.DS_MAIN).batchSave("h_location", rl, rl.size()).length;

		int addhouseNum = Db.use(xx.DS_MAIN).batchSave("h_house", rlHouse, rlHouse.size()).length;

		if (addFlagNum == rl.size() && delFlag > 0 && addhouseNum == rlHouse.size()) {
			return true;
		}
		return false;
		/*
		 * List<Record> addList = new ArrayList<>(); List<Record> updateList = new
		 * ArrayList<>(); List<Record> saveList =
		 * Db.use(xx.DS_MAIN).find(" select id from h_location "); for (Record rS :
		 * saveList) { for (Record r : rl) { if (rS.getInt("id") == r.getInt("id")) {
		 * updateList.add(r);
		 * 
		 * } } } rl.removeAll(updateList);
		 * 
		 * addList.addAll(rl); int updateFlagNum = updateList.size(); int addFlagNum =
		 * addList.size(); System.err.println("updateFlagNum" + updateFlagNum);
		 * System.err.println("addFlagNum" + addFlagNum); if (addFlagNum > 0) {
		 * 
		 * if (updateFlagNum == Db.use(xx.DS_MAIN).batchUpdate("h_location", updateList,
		 * updateFlagNum).length && addFlagNum ==
		 * Db.use(xx.DS_MAIN).batchSave("h_location", addList, addFlagNum).length) {
		 * return true; } return false; } else { if (updateFlagNum ==
		 * Db.use(xx.DS_MAIN).batchUpdate("h_location", updateList,
		 * updateFlagNum).length) { return true; } return false;
		 * 
		 * }
		 */
	}

	/**
	 * 根据楼号查询是否存在
	 * 
	 * @param lh
	 * @return
	 */
	public boolean getlhNum(int lh) {

		return xx.isEmpty(this.findFirst(" select house_no from h_house where house_no = ?", lh));
	}

	/***
	 * 获得实景图
	 * 
	 * @param hosue_no
	 *            楼号
	 * @return
	 */
	public House getHouseImage(int hosue_no) {

		return this.findFirst("select house_no,image_path,image2_path from h_house where house_no = ? ", hosue_no);
	}

}
