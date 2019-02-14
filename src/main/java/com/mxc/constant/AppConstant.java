package com.mxc.constant;
/**
 * 功能描述:常量
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年1月24日 下午6:35:39
 */
public interface AppConstant {
	/**
	 * 12306账号
	 */
	public String TICKET_USERNAME = "";
	
	/**
	 * 12306密码
	 */
	public String TICKET_PASSWORD = "";
	/**
	 * 云打码用户名
	 */
	public String YDM_USERNAME = "";
	/**
	 * 云打码密码
	 */
	public String YDM_PASSWORD = "";
	/**
	 * // yundamaAPI 32位, yundamaAPI-x64 64位
	 */
	public String YDM_DLL_PATH = "";
	/**
	 * yundama的 appid
	 */
	public Integer YDM_APPID = 6735;
	/**
	 * yundama的 appkey
	 */
	public String YDM_APPKEY = "";
	/**
	 * 第几位乘客 从0开始 normalPassenger_0 代表第一位   normalPassenger_1 代表第二位
	 */
	public String NORMALPASSENGER = "normalPassenger_0";
}
