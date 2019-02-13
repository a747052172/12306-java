package com.mxc.util; 
import com.mxc.constant.AppConstant;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class YdmUtil
{
	// 下载云打码DLL http://yundama.com/apidoc/YDM_SDK.html#DLL
	// yundamaAPI 32位, yundamaAPI-x64 64位

	public interface YDM extends Library
	{
		YDM	INSTANCE	= (YDM) Native.loadLibrary(AppConstant.YDM_DLL_PATH, YDM.class);		

		public void YDM_SetBaseAPI(String lpBaseAPI);
		public void YDM_SetAppInfo(int nAppId, String lpAppKey);
		public int YDM_Login(String lpUserName, String lpPassWord);
		public int YDM_DecodeByPath(String lpFilePath, int nCodeType, byte[] pCodeResult);
		public int YDM_UploadByPath(String lpFilePath, int nCodeType);
		public int YDM_EasyDecodeByPath(String lpUserName, String lpPassWord, int nAppId, String lpAppKey, String lpFilePath, int nCodeType, int nTimeOut, byte[] pCodeResult);
		public int YDM_DecodeByBytes(byte[] lpBuffer, int nNumberOfBytesToRead, int nCodeType, byte[] pCodeResult);
		public int YDM_UploadByBytes(byte[] lpBuffer, int nNumberOfBytesToRead, int nCodeType);
		public int YDM_EasyDecodeByBytes(String lpUserName, String lpPassWord, int nAppId, String lpAppKey, byte[] lpBuffer, int nNumberOfBytesToRead, int nCodeType, int nTimeOut, byte[] pCodeResult);
		public int YDM_GetResult(int nCaptchaId, byte[] pCodeResult);
		public int YDM_Report(int nCaptchaId, boolean bCorrect);
		public int YDM_EasyReport(String lpUserName, String lpPassWord, int nAppId, String lpAppKey, int nCaptchaId, boolean bCorrect);
		public int YDM_GetBalance(String lpUserName, String lpPassWord);
		public int YDM_EasyGetBalance(String lpUserName, String lpPassWord, int nAppId, String lpAppKey);
		public int YDM_SetTimeOut(int nTimeOut);
		public int YDM_Reg(String lpUserName, String lpPassWord, String lpEmail, String lpMobile, String lpQQUin);
		public int YDM_EasyReg(int nAppId, String lpAppKey, String lpUserName, String lpPassWord, String lpEmail, String lpMobile, String lpQQUin);
		public int YDM_Pay(String lpUserName, String lpPassWord, String lpCard);
		public int YDM_EasyPay(String lpUserName, String lpPassWord, long nAppId, String lpAppKey, String lpCard);
	}
	
	
	public static String decode(String imagepath, int codeType) throws Exception
	{
		// 注意这里是普通会员账号，不是开发者账号，注册地址 http://www.yundama.com/index/reg/user
		// 开发者可以联系客服领取免费调试题分
		String username = AppConstant.YDM_USERNAME;
		String password	= AppConstant.YDM_PASSWORD;

		// 测试时可直接使用默认的软件ID密钥，但要享受开发者分成必须使用自己的软件ID和密钥
		// 1. http://www.yundama.com/index/reg/developer 注册开发者账号
		// 2. http://www.yundama.com/developer/myapp 添加新软件
		// 3. 使用添加的软件ID和密钥进行开发，享受丰厚分成
		int appid = AppConstant.YDM_APPID;									
		String 	appkey	= AppConstant.YDM_APPKEY;
		
		// 图片路径
//		String	imagepath	= "img\\test.png";
		

		//  例：1004表示4位字母数字，不同类型收费不同。请准确填写，否则影响识别率。在此查询所有类型 http://www.yundama.com/price.html
//		int codetype = 1004;''
		
		// 只需要在初始的时候登陆一次
		int uid = 0;
		YDM.INSTANCE.YDM_SetAppInfo(appid, appkey);			// 设置软件ID和密钥
		uid = YDM.INSTANCE.YDM_Login(username, password);	// 登陆到云打码

		if(uid > 0){
			System.out.println("登陆成功,正在提交识别...");
			
			byte[] byteResult = new byte[30];
			int cid = YDM.INSTANCE.YDM_DecodeByPath(imagepath, codeType, byteResult);
			String strResult = new String(byteResult, "UTF-8").trim();
			
			// 返回其他错误代码请查询 http://www.yundama.com/apidoc/YDM_ErrorCode.html
			System.out.println("识别返回代码:" + cid);
			System.out.println("识别返回结果:" + strResult); 
			return strResult;

		}else{
			System.out.println("登录失败，错误代码为：" + uid);
		}
		return null;  
	}
}
