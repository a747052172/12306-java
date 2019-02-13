package com.mxc.entity;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8264314619685235982L;
	/**
	 * linktypeid 不知道什么意思  dc
	 */
	private String linktypeid;
	/**
	 * 始发地参数（取12306上面的参数值）
	 */
	private String fs;
	/**
	 * 目的地参数（取12306上面的参数值）
	 */
	private String ts;
	/**
	 * flag参数
	 */
	private String flag;
	
	/**
	 * 出发日期
	 */
	private String date;
	
	/**
	 * 发车时间
	 */
	private List<String> listStartTime;
	
	/**
	 * 参数从构造方法中传进来
	 * @param linktypeid
	 * @param fs
	 * @param ts
	 * @param flag
	 * @param date
	 */
	public Ticket(String linktypeid, String fs, String ts, String flag, String date, List<String> listStartTime) {
		super();
		this.linktypeid = linktypeid;
		this.fs = fs;
		this.ts = ts;
		this.flag = flag;
		this.date = date;
		this.listStartTime = listStartTime;
	}
	
	public List<String> getListStartTime() {
		return listStartTime;
	}


	public void setListStartTime(List<String> listStartTime) {
		this.listStartTime = listStartTime;
	}



	public String getLinktypeid() {
		return linktypeid;
	}

	public void setLinktypeid(String linktypeid) {
		this.linktypeid = linktypeid;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	
}
