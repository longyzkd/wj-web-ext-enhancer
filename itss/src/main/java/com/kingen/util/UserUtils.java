package com.kingen.util;

import org.apache.shiro.SecurityUtils;

import com.kingen.bean.User;

public class UserUtils {

	

	/**
	 * current session's user
	 * @return
	 */
	public static User getCurrentUser() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
}
