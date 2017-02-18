package com.kingen.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.kingen.bean.User;
import com.kingen.repository.CommonDao;
import com.kingen.util.BeanUtils;
import com.kingen.util.Page;
import com.kingen.util.Reflections;
import com.kingen.web.workflow.PaginationThreadUtils;

/**
 * service的简单封装
 *  但是一个service经常会包含多种实体类型的dao，所以这里的泛型有局限性
 * @author wj
 * @date 2016-11-24
 * @param <T>
 */
//@Transactional 继承commonservice，这里事务配置不会起作用，自己模块的service已经保证事务。除非不继承它，直接注入，可以开启事务
@Service
@Transactional
//@Lazy(value=true)
public class   CommonService<T,PK  extends Serializable> {
	
	private static Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	
	//@Qualifier(value="commonDao") //要配合@Autowired , 用@Qualifier会导致泛型消失，不会具体为真正的子类dao
	//注入真正的实体类型的子类dao,根据T类型
	@Autowired
	private CommonDao<T,PK> dao;   
	
	/*
	//解决 直接用CommonDao，泛型消失。
	//可以直接使用CommonDao，手动给CommonDao传入类型
	
	//不可直接使用CommonDao，还是需要他的子类
	@PostConstruct
	public void initEntityClass(){//不用构造器，以免dao为空
		
		Class<T> entityClass = Reflections.getClassGenricType(getClass());
		dao.setEntityClass(entityClass); //直接用CommonDao，泛型消失//会覆盖之前的类型
	}
	*/
	
	/**
	 * 返回当前对象集合
	 * @return
	 */
	public List<T> list() {
		
		List<T> list 	= dao.find();
		
        if( !CollectionUtils.isEmpty(list)  ){
      	   return list;
         }else{
      	   return Collections.emptyList();
         }
	}
	
	public <X> List<X> list(String entityName) {
		
//		String hql = "from "+entityName;
		List<X> list 	= dao.findByEntity(entityName);
		if( !CollectionUtils.isEmpty(list)  ){
			return list;
		}else{
			return Collections.emptyList();
		}
	}
	public <X> List<X> list(Class<X> clazz) {
		
//		String hql = "from "+entityName;
		List<X> list 	= dao.findByEntity(clazz.getSimpleName());
		if( !CollectionUtils.isEmpty(list)  ){
			return list;
		}else{
			return Collections.emptyList();
		}
	}
	
	public <X> List<X> list(String entityName,Map<String, Object> params) {
		
		List<X> list 	= dao.findByEntity(entityName, params);
		if( !CollectionUtils.isEmpty(list)  ){
			return list;
		}else{
			return Collections.emptyList();
		}
	}
	
	public <X> List<X> list(Class<X> clazz,Map<String, Object> params) {
		
		List<X> list 	= dao.findByEntity(clazz.getSimpleName(), params);
		if( !CollectionUtils.isEmpty(list)  ){
			return list;
		}else{
			return Collections.emptyList();
		}
	}
	
	/**
	 * 返回当前唯一对象
	 * @return
	 */
	public T unique(Serializable id) {
		
		 return dao.unique(id);  
	}
	
	/**
	 * 查询唯一指定对象

	 * @param entityName 实体名
	 * @param idName ID名
	 * @param id  id值
	 * @return
	 */
	public <X> X uniqueEntity(String entityName,String idName,PK id) {
		return dao.uniqueEntity(entityName,idName, id);
	}
	public <X> X uniqueEntity(Class<X> clazz,String idName,PK id) {
		return dao.uniqueEntity(clazz.getSimpleName(),idName, id);
	}
	
	/**
	 * 由参数保证唯一性
	 * @param entityName
	 * @param params
	 * @return
	 */
	@Deprecated
	public T one(String entityName,Map<String, Object> params) {
		return dao.oneByEntity(entityName, params);
	}
	
	
	/**
	 * 当前对象的分页集合
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page) {
		return dao.find(page);
	}
	 
	/**
	 * 当前对象的带参数分页集合. 要求params MAP的key 必须是entity的属性
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page, Map<String, Object> params) {
		return dao.find(page, params);
		
		
	}
	/**
	 * entityName对象的分页集合 ，带参数的
	 * @param page
	 * @return
	 */
	public  <X> Page<X> find(Page<X> page ,String entityName, Map<String, Object> params) {
		return dao.findByEntity(page, entityName, params);
		
		
	}
	/**
	 * entityName对象的分页集合 ，带参数的
	 * @param page
	 * @return
	 */
	public <X> Page<X> find(Page<X> page ,String entityName) {
		return dao.findByEntity(page, entityName, null);
		
		
	}
	public <X> Page<X> find(Page<X> page ,Class<X> clazz) {
		return dao.findByEntity(page, clazz.getSimpleName(), null);
		
		
	}
	
	
	/**
     * 根据是否 {新增/修改} 查询实体 唯一性
     * @param beanClazz
     * @param property
     * @param val
     * @param rawValue
     * @param action
     * @return
     */
	public <E> List<E> getEntityBy(String beanClazz,String property, Object val,Object rawValue,String action) {
	
		if(StringUtils.isEmpty(action) || "insert".equals(action)){ //新增
			Map<String,Object> params = Maps.newHashMapWithExpectedSize(1);
			params.put(property, val);
			return dao.findByEntity(beanClazz, params);
		}else if("edit".equals(action)  || "update".equals(action)){
			return dao.findExcept(beanClazz,property,val,rawValue);
		}
		return null;
		
	}
	
	
	
	
	//----------------old methods
	
	public List<T> getAllList(String tableSimpleName) throws Exception{
		StringBuilder sff = new StringBuilder();  
        sff.append("select a from ").append(tableSimpleName).append(" a ");  
        List<T> list = dao.createQuery(sff.toString()).list();  
        if( list.size()>0 ){
      	   return list;
         }else{
      	   return Collections.emptyList();
         }
	}

	
	public T getUnique(String tableSimpleName, String[] columns, String[] values) throws Exception{
		StringBuilder sb = new StringBuilder();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
           }  
           T entity = dao.unique(sb.toString());  
           return entity; 
        }else{  
           logger.error("columns.length != values.length");
           return null;  
        } 
	}
	
	public List<T> findByWhere(String tableSimpleName, String[] columns,
			String[] values) throws Exception{
		StringBuilder sb = new StringBuilder();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
           }  
           List<T> list = dao.createQuery(sb.toString()).list();  
//         最好用JDK提供的Collections.emptyList()来返回一个空的集合对象 而不是 null
//         Collections.EMPTY_LIST 是返回一个空集合对象，emptyList()是对EMPTY_LIST做了一个泛型支持，具体看源码
//         Collections.EMPTY_LIST的返回值是一个不可变的空List，不能进行例如add的各种操作！
           if( list.size()>0 ){
        	   return list;
           }else{
        	   return Collections.emptyList();
           }
        }else{  
        	return Collections.emptyList();  
        } 
	}

	
	public List<T> getCount(String tableSimpleName) throws Exception{
    	String hql = "select count(*) from " + tableSimpleName;
    	List<T> list = dao.createQuery(hql).list();
    	return list;
	}

	
	public Serializable add(T bean) throws Exception{
		return dao.saveWithReturnId(bean);
	}
	public Serializable addObj(Object bean) throws Exception{
		return dao.saveObjWithReturnId(bean);
	}

	
	public void saveOrUpdate(T bean) throws Exception{
		dao.saveOrUpdate(bean);
	}

	
	public void delete(T bean) throws Exception{
		dao.delete(bean);
	}
	
	/**
	 * 物理删除当前对象 ,要求主键是名是ID
	 * @param ids
	 */
	public void delThem(List<String> ids) {
		Assert.notEmpty(ids,"ID不应为空");
		for(String id :ids ){
			dao.delete(id);
		}
		
	}
	/**
	 * 物理删除当前对象 
	 * @param ids
	 */
	public void delThem(List<PK> ids,String idName) {
		Assert.notEmpty(ids,"ID不应为空");
		for(PK id :ids ){
			dao.delete(id,idName);
		}
		
	}
	/**
	 * 物理删除指定对象 
	 * @param ids
	 */
	public void delThemEntity(String entityName,String idName,List<PK> ids) {
		Assert.hasText(entityName,"entityName不应为空");
		Assert.hasText(idName,"idName不应为空");
		Assert.notEmpty(ids,"ID不应为空");
		for(PK id :ids ){
			dao.deleteEntity(entityName,idName,id);
		}
		
	}
	
	public void update(T bean) {
		dao.update(bean);
	}
	/**
	 * 忽视那些 存在于数据库，而页面上没有的 属性。
	 * 一般用于页面编辑，并且有些字段没有在页面上显示出来
	 * @param pk
	 * @param bean
	 * @param ignoreProperties 忽视的属性
	 * @throws Exception
	 */
	public void updateNotNull(Serializable pk,T bean,String[] ignoreProperties) throws Exception{
		
		T t = dao.unique(pk);
		BeanUtils.copyNotNullProperties(bean, t,ignoreProperties);
		dao.update(t);
	}
	
	public void updateNotNull(Serializable pk,T bean) throws Exception{
		
		updateNotNull(pk,bean);
	}

	
	public T getBean(Class<T> obj, Serializable id) throws Exception{
		return dao.getById(obj, id);
	}

	

	/**
	 * 采用 threadlocal 的分页
	 * @param tableSimpleName
	 * @param columns
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public List<T> findByPage(String tableSimpleName, String[] columns, String[] values) throws Exception{
		Integer totalSum = 0;
		List<T> list = new ArrayList<T>();
		if(columns.length <= 0 && values.length <= 0){
			list = getAllList(tableSimpleName);
		}else{
			list = findByWhere(tableSimpleName, columns, values);
		}
		if(!CollectionUtils.isEmpty(list) ){
			totalSum = list.size();
		}
		int[] pageParams = PaginationThreadUtils.setPage(totalSum);
		
		StringBuilder sb = new StringBuilder();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
           } 
	       String hql = sb.toString();
	       if(hql.endsWith("where ")){
	    	   hql = hql.substring(0, hql.length()-6);
	       }
	       logger.info("findByPage: HQL: "+hql);
	       list = dao.find1(hql, pageParams[0], pageParams[1]); 
//	       list = dao.findByPage(hql, pageParams[0], pageParams[1]); 
	       if( list.size()>0 ){
        	   return list;
           }else{
        	   return Collections.emptyList();
           }
        }else{
        	logger.info("findByPage: columns.length != values.length");
        	return Collections.emptyList();
        }
	}
	

}
