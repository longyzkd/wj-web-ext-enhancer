package com.kingen.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kingen.repository.CommonDao;
import com.kingen.util.Page;
import com.kingen.web.workflow.PaginationThreadUtils;

/**
 * service的简单封装
 * @author wj
 * @date 2016-11-24
 * @param <T>
 */
//@Transactional 继承commonservice，这里事务配置不会起作用，自己模块的service已经保证事务。除非不继承它，直接注入，可以开启事务
@Service
@Transactional
public class CommonService<T> {
	
	private static Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	
	//@Qualifier(value="commonDao") //要配合@Autowired , // 用@Qualifier会导致泛型消失，不会具体为真正的子类dao
	@Autowired
	private CommonDao<T> dao;
	
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
	
	public List<T> list(String entityName) {
		
		String hql = "from "+entityName;
		List<T> list 	= dao.find(hql);
		if( !CollectionUtils.isEmpty(list)  ){
			return list;
		}else{
			return Collections.emptyList();
		}
	}
	
	public List<T> list(String entityName,Map<String, Object> params) {
		
		List<T> list 	= dao.findByEntity(entityName, params);
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
	 * 由参数保证唯一性
	 * @param entityName
	 * @param params
	 * @return
	 */
	public T unique(String entityName,Map<String, Object> params) {
		return dao.uniqueByEntity(entityName, params);
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
	 * 当前对象的带参数分页集合
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
	public Page<T> find(Page<T> page ,String entityName, Map<String, Object> params) {
		return dao.findByEntity(page, entityName, params);
		
		
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

	
	public void update(T bean) throws Exception{
		dao.update(bean);
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
