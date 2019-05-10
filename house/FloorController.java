package com.house;


import com.eova.common.base.BaseController;
import com.house.model.Floor;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年6月24日  
*/
public class FloorController  extends BaseController {

	
	/**
	 * 获得层数
	 * 
	 */
	public void getfloorNum() {
		
		renderJson(Floor.dao.find("select floor_no as ID , CONCAT(floor_no,'层') as CN  from h_floor where house_no =? order by floor_no  ",getParaToInt(0)));
	}


	

}
