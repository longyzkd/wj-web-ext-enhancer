/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.service.account;

import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.kingen.aop.ServiceLogAnnotation;
import com.kingen.bean.Menu;
import com.kingen.bean.SysOrgMenu;
import com.kingen.bean.User;
import com.kingen.repository.account.MenuDao;
import com.kingen.repository.account.UserDao;
import com.kingen.service.CommonService;
import com.kingen.util.BeanUtils;
import com.kingen.util.Constants;
import com.kingen.util.Digests;
import com.kingen.util.Encodes;
import com.kingen.util.Global;
import com.kingen.util.Page;

/**
 * 用户管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService extends CommonService<User,String> {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private MenuDao menuDao;

	
	@Transactional(readOnly=true)
	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}


	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		return user.getUsername();
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		
			
			byte[] salt = Digests.generateSalt(SALT_SIZE);
			user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}


	/**
	 * 保存
	 * @param user
	 */
	public void create(User user) {
		entryptPassword(user);
		userDao.save(user);
		
	}
	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.update(user);
		
	}

	public void del(User u) {
			userDao.delete(u.getUserId());
		
	}
	public void del(List<User> beanList) {
			if(!CollectionUtils.isEmpty(beanList)){
				for(User user:beanList){
					del(user);
				}
			}
			
		
	}

	public void updatePwd(User user) {
		entryptPassword(user);
		userDao.update(user);
		
	}


	public static void main(String[] a){
		
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		//logger.info("salt"+Encodes.encodeHex(salt));
		
		byte[] hashPassword = Digests.sha1("321".getBytes(),  Encodes.decodeHex("848c7f1e3c58ca09"), HASH_INTERATIONS);
		
//		String  hashPassword = new SimpleHash("SHA-1", "321", ByteSource.Util.bytes( Encodes.decodeHex("848c7f1e3c58ca09")), HASH_INTERATIONS).toString();
		logger.info("pwd   "+ Digests.encryptPassword("123", "8449a736b311440a") );
//		logger.info("pwd"+hashPassword);
	}
	
	/**
	 * 启用ehcache缓存
	 * @return
	 */
	@Cacheable(value="sysCache" )
	public List<Menu> findAllMenus() {
		return menuDao.find();
	}


	public Page<User> getUsers(Page<User> page, User user) {
		Page<User> p = userDao.findUsers(page,user);
		return p;
	}


	public List<SysOrgMenu> findMenusBy(String orgId) {
		String hql = "from SysOrgMenu where id.orgId=:orgId";
		Map<String, Object> paramter = Maps.newHashMap();
		paramter.put("orgId", orgId);
		return menuDao.findme(hql, paramter);
	}
	
	@ServiceLogAnnotation(action="保存用户")
	public void saveUser(User data,String[] funidvalue,Boolean synToActiviti) throws Exception{
		if (data != null) {
			User u = userDao.findByUserid(data.getUserId());//查询出的用户会回滚？
			
			if (u != null) {
				throw new Exception("新建用户失败，用户名已存在！");
			} else {
				entryptPassword(data);
				data.setStatus(Constants.UserStatusEnum.Active.getIndex());//默认激活状态
				userDao.save(data);
				
				
				 // 同步数据到Activiti Identify模块
		        if (synToActiviti) {
		        	UserQuery userQuery = identityService.createUserQuery();
		            List<org.activiti.engine.identity.User> activitiUsers = userQuery.userId(data.getUserId()).list();
		 
		            saveActivitiUser(data);
		        }
		        
		        
			}
			
		}
	}
	
	@ServiceLogAnnotation(action="修改用户")
	public void  updateUser(User data, String[] funidvalue,Boolean synToActiviti){
		User t = userDao.findByUserid(data.getUserId());
		BeanUtils.copyNotNullProperties(data, t,new String[]{"password"});//密码不更新，密码单独更新
		
		update(t);
		

		 // 同步数据到Activiti Identify模块
       if (synToActiviti) {

           saveActivitiUser(t);
       }
		
		
	}


	public User one(User user) {
		
		return userDao.findByUserid(user.getUserId());
	}


	public List<Menu> findMenuByuserId(String userIdString) {
		List<Menu>  menus =	menuDao.findMenusBy(userIdString);
		return menus;
	}

	/**
	 * 改变用户状态
	 * @param userIds
	 * @param status
	 */
	@ServiceLogAnnotation(action="修改用户状态")
	public void updateUser(List<String> userIds,String status) {
		Assert.notEmpty(userIds,"用户ID不应为空");
		for(String id :userIds ){
			User t = userDao.findByUserid(id);
			t.setStatus(status);
			update(t);
		}
	
		
	}

	@ServiceLogAnnotation(action="重置用户密码")
	public void updateUsersPwd(List<String> userIds) {
		Assert.notEmpty(userIds,"用户ID不应为空");
		for(String id :userIds ){
			User t = userDao.findByUserid(id);
			t.setPassword(Global.getConfig("initialPwd"));//初始化密码
			updatePwd(t);
		}
	
		
		
	}


	public void delThem(List<String> userIds,Boolean synToActiviti) {
		Assert.notEmpty(userIds,"用户ID不应为空");
		for(String id :userIds ){
			userDao.delete(id);
			
		     if (synToActiviti) {
		            // 同步删除Activiti User,会自动删除membership对应的信息
		            identityService.deleteUser(id);
		        }
		}
		
	}


	public void testException() throws Exception {
		throw new Exception("test");
		
	}
	
	
	
	
	//工作流 
	  @Autowired
	  protected IdentityService identityService;

	
    /**
     * 添加/修改 一个用户到Activiti {@link org.activiti.engine.identity.User}
     * @param user  用户对象, {@link User}
     */
    //private 不影响事务传播
    private void saveActivitiUser(User user) {
    	 org.activiti.engine.identity.User activitiUser   = identityService.createUserQuery().userId(user.getUserId()).singleResult();
         if (activitiUser == null) {
        	 activitiUser = identityService.newUser(user.getUserId());
         }
         activitiUser.setFirstName(user.getUsername());
         activitiUser.setLastName(user.getUsername());
         activitiUser.setEmail("");
    	 activitiUser.setPassword(user.getPassword());
         identityService.saveUser(activitiUser);
        logger.info("add activiti user: {}"+ToStringBuilder.reflectionToString(activitiUser));
    }
   
    
	
}
