package com.kingen.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 封装fastJson的链式编程,比如  JsonResultBuilder.success(true).msg("任务已签收").json();
 * @author wj
 * @date 2016-11-10
 *
 */
public class JsonResultBuilder {

	public static final String SUCCESS = "success";	
	
	public static final String ERRORS = "errors";
	public static final String ERROR = "error";
	public static final String DETAIL = "detail";	
	public static final String MSG = "msg";
	public static final String ERRORCODE = "errorCode";
	public static final String ERRORMSG = "errorMsg";
	
	public static final String DATA = "data";

	JSONObject jsonObject;
	
	
	public String toString() {
		return jsonObject.toString();
	}

	private JsonResultBuilder() {

	}

	public static JsonResultBuilder success(boolean success) {
		JsonResultBuilder jsonResultBuilder = new JsonResultBuilder();
		JSONObject jsonObject = new JSONObject();

		jsonResultBuilder.jsonObject = jsonObject;

		jsonObject.put(SUCCESS, success);

		return jsonResultBuilder;
	}
	
	
	public  JsonResultBuilder data(Object data) {
	
		jsonObject.put(DATA, data);
		
		return this;
	}



	public JsonResultBuilder errorCode(int errorCode) {
		JSONObject errorObject = getErrorJson();

		errorObject.put(ERRORCODE, errorCode);

		return this;
	}

	public JsonResultBuilder errorMsg(String errorMsg) {
		JSONObject errorObject = getErrorJson();

		errorObject.put(ERRORMSG, errorMsg);

		return this;
	}

	public JsonResultBuilder errorDetail(String detail) {
		JSONObject errorObject = getErrorJson();

		errorObject.put(DETAIL, detail);

		return this;
	}

	private JSONObject getErrorJson() {
		JSONObject errorJson = (JSONObject) jsonObject.get(ERROR);
		if (errorJson == null) {
			errorJson = new JSONObject();
			jsonObject.put(ERROR, errorJson);
		}

		return errorJson;
	}

	public JsonResultBuilder msg(String msg) {
		jsonObject.put(MSG, msg);

		return this;
	}

	public JsonResultBuilder msg(String result, String reason, String suggest) {

		String message = String.format("%s，%s，%s。", result, reason, suggest);
		msg(message);

		return this;
	}

	public JsonResultBuilder addError(String name, String msg) {
		JSONObject errorObject = getErrorJson();

		JSONArray json = (JSONArray) jsonObject.get(ERRORS);
		if (json == null) {
			json = new JSONArray();
			errorObject.put(ERRORS, json);
		}

		JSONObject newErrorJson = new JSONObject();
		newErrorJson.put(name, msg);
		json.add(newErrorJson);

		return this;
	}



	public JSONObject json() {
		return jsonObject;
	}

	
}
