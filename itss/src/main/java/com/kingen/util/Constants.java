package com.kingen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Constants {
	private static Logger logger = LoggerFactory.getLogger(Constants.class);
	
	public static String DEFAULT_PWD ="123456";
	
	public static final String STATUS="status";	
	
	
	public enum UserStatusEnum	{  
		/**
		 * 1 激活
		 * 0 屏蔽
		 */
		Active("1"), Shield("0");
		
		private String index; 

		UserStatusEnum(String idx) { 
	        this.index = idx; 
	    } 
	    public String getIndex() { 
	        return index; 
	    } 
	}
	
	public enum StatusEnum	{  
		/**
		 * 1 激活
		 * 0 屏蔽
		 */
		success("success"), fail("fail");
		
		private String index; 
		
		StatusEnum(String idx) { 
			this.index = idx; 
		} 
		public String getIndex() { 
			return index; 
		} 
	}
	
	public enum ActivitiStatusEnum	{  
		/**
		 * 启用
		 */
		work("work"), 
		/**
		 * 未启用
		 */
		notWork("notWork");
		
		private String index; 
		
		ActivitiStatusEnum(String idx) { 
			this.index = idx; 
		} 
		public String getIndex() { 
			return index; 
		} 
	}
	
	
	
	public static void main(String[] as){
		
		logger.info(UserStatusEnum.Active.getIndex());
	}
	
	

}
