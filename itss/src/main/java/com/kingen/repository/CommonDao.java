package com.kingen.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;
import com.kingen.util.Page;
import com.kingen.util.Reflections;


/**
 * 
 * @author wj
 * @param <T>  entity type
 * @date  2016-11-24
 */
//会不会生成多个不同泛型的commondao
//因为有泛型的存在，不会直接实例化，只有userDao extends CommonDao<User> 传入类型了，才会实例化CommonDao,并在构造器里持有类型
//不能是抽象，会实例化不了
@Repository(value="commonDao")
public   class CommonDao<T,PK  extends Serializable>  {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<T> entityClass;
	
	
	
	public Class<T> getEntityClass() {
		return entityClass;
	}



	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}



	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public CommonDao() {//构造器 在spring初始化的时候执行，创建各个BEAN
		entityClass = Reflections.getClassGenricType(getClass());
	}
	
	public void flushAndClear(){
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	/**
	 * 获得当前事物的session
	 * 
	 * @return org.hibernate.Session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	 
	public void save(T o) {
		if (o != null) {
			 getCurrentSession().saveOrUpdate(o);
		}
	}
	
	public Serializable saveWithReturnId(T o) {
		return getCurrentSession().save(o);
	}
	public Serializable saveObjWithReturnId(Object o) {
		return getCurrentSession().save(o);
	}

	 
	public T getById(Class<T> c, Serializable id) {
		return (T) getCurrentSession().get(c, id);
	}
	
	/**
	 * 由HQL保证返回唯一对象
	 * @param hql
	 * @return
	 */
	public T one(String hql) {
		return (T) createQuery(hql).uniqueResult();
	}
	
	/**
	 * 由HQL保证返回唯一对象
	 * @param hql
	 * @return
	 */
	public T one(String hql, Map<String, Object> params) {
		return (T) createQuery(hql,params).uniqueResult();
	}
	/**
	 * 由参数保证返回唯一对象
	 * 
	 * 
	 * 要求params MAP的key 必须是entity的属性
	 * 根据entityName,params 拼装hql，并返回单个对象（由hql保证返回单对象）
	 * @param entityName
	 * @param params
	 * @return
	 */
	@Deprecated
	public T oneByEntity(String entityName, Map<String, Object> params) {
		
		String hql = queryHqlSetter(entityName, params);
		Query q = createQuery(hql, params);
		return (T)q.uniqueResult();
	}
	
	/**
	 * 查询指定的唯一实体
	 * @param entityName  实体名
	 * @param id  主键
	 * @param idName  主键字段名  可以为id.userId这种
	 * @return
	 */
	public <X> X uniqueEntity(String entityName,String idName,PK id) {
		
		String hql = idHqlSetter(entityName,idName, id);
		Map<String,Object> params = Maps.newHashMapWithExpectedSize(1);
		params.put("p1", id);
		Query q = createQuery(hql,params);
		return (X)q.uniqueResult();
	}
	
	
	private String idHqlSetter(String entityName, String idName, PK id) {
		
		Assert.hasText(entityName,"entityName不应为空");
		Assert.hasText(idName,"idName不应为空");
		Validate.isTrue(id!=null,"id不应为空");
		String hql = "from "+entityName +" where "+idName+"  = :p1"; //无法保证id的类型，只能用setParameter方法 传参
		
		
		return hql;
	}



	/**
	 * 返回<T>
	 * @return
	 */
	public T unique(Serializable id) {
		return getById(entityClass, id);
	}

	
	@Deprecated
	public T getByHql(String hql) {
		Query q = createQuery(hql);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Deprecated
	public T getByHql(String hql, Map<String, Object> params) {
		Query q = createQuery(hql,params);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	 
	public void delete(T o) {
		if (o != null) {
			getCurrentSession().delete(o);
		}
	}

	 
	public void update(T o) {
		if (o != null) {
			getCurrentSession().update(o);
		}
	}

	 
	public void saveOrUpdate(T o) {
		if (o != null) {
			getCurrentSession().saveOrUpdate(o);
		}
	}

	 
	public List<T> find(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return q.list();
	}
	
	/**
	 * 返回<T> 集合
	 * @return
	 */
	public List<T> findCriteria() {
		DetachedCriteria  detachedCriteria =  DetachedCriteria.forClass(entityClass);
		return find(detachedCriteria);
	}
	/**
	 * 返回<T> 集合
	 * @return
	 */
	public List<T> find() {
		String hql = "from "+ entityClass.getSimpleName();
		Query q = createQuery(hql);
		return q.list();
	}
	
	/**
	 * QL 分页查询,返回<T> 集合
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public  Page<T> find(Page<T> page){
    	
    	//如果类没有提供泛型，则用page的泛型  wj
    	//TODO test
    	Class<T> entityClass = this.entityClass;
    	if(Object.class.equals(entityClass)){
    		entityClass = Reflections.getClassGenricType(page.getClass());
    	}
    	
    	String  qlString = "from "+ entityClass.getSimpleName();
		// get count
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
	        Query query = createQuery(countQlString);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setTotal(Integer.valueOf(list.get(0).toString()));
	        }else{
	        	page.setTotal(list.size());
	        }
			if (page.getTotal() < 1) {
				return page;
			}
			
			
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query1 = createQuery(ql); 
    	// set page
        query1.setFirstResult(page.getFirstResult());
        query1.setMaxResults(page.getLimit()); 
        page.setDataList(query1.list());
		return page;
    }
    /**
     * 返回当前对象的分页集合
     * @param page
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public  Page<T> find(Page<T> page,Map<String, Object> params){
    	
    	String  qlString =	queryHqlSetter(entityClass.getSimpleName(), params);
    	
    	// get count
    	String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
    	Query query = createQuery(countQlString,params);
    	List<Object> list = query.list();
    	if (list.size() > 0){
    		page.setTotal(Integer.valueOf(list.get(0).toString()));
    	}else{
    		page.setTotal(list.size());
    	}
    	if (page.getTotal() < 1) {
    		return page;
    	}
    	
    	
    	// order by
    	String ql = qlString;
    	if (StringUtils.isNotBlank(page.getOrderBy())){
    		ql += " order by " + page.getOrderBy();
    	}
    	Query query1 = createQuery(ql,params); 
    	// set page
    	query1.setFirstResult(page.getFirstResult());
    	query1.setMaxResults(page.getLimit()); 
    	page.setDataList(query1.list());
    	return page;
    }
    
    /**
     * 返回  @param entityName 对象的分页集合
     * @param page
     * @param entityName
     * @param params
     * @return
     */
    
    @SuppressWarnings("unchecked")
    public <X> Page<X> findByEntity(Page<X> page ,String entityName, Map<String, Object> params){
    	String  qlString =	queryHqlSetter(entityName, params);
    	
    	// get count
    	String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
    	Query query = createQuery(countQlString, params);
    	List<Object> list = query.list();
    	if (list.size() > 0){
    		page.setTotal(Integer.valueOf(list.get(0).toString()));
    	}else{
    		page.setTotal(list.size());
    	}
    	if (page.getTotal() < 1) {
    		return page;
    	}
    	
    	
    	// order by
    	String ql = qlString;
    	if (StringUtils.isNotBlank(page.getOrderBy())){
    		ql += " order by " + page.getOrderBy();
    	}
    	Query query1 = createQuery(ql, params); 
    	// set page
    	query1.setFirstResult(page.getFirstResult());
    	query1.setMaxResults(page.getLimit()); 
    	page.setDataList(query1.list());
    	return page;
    }

    
    
    
	
	public List<T> find(String hql, Class<T> resultClass) {
		Query q = getCurrentSession().createQuery(hql);
		q.setResultTransformer(Transformers.aliasToBean(resultClass));
		
		return q.list();
	}

	 
	public List<T> find(String hql, Map<String, Object> params) {
		Query q = createQuery(hql, params);
		return q.list();
	}
	
	/**
	 * 查询指定对象的列表
	 * 
	 * 要求params MAP的key 必须是entity的属性
	 * 根据entityName,params 拼装hql，并返回集合
	 * @author wj
	 * @param entityName
	 * @param params
	 * @return
	 */
	public <X> List<X> findByEntity(String entityName, Map<String, Object> params) {
		
		String hql = queryHqlSetter(entityName, params);
		Query q = createQuery(hql, params);
		return q.list();
	}
	
	/**
	 * 查询指定对象的列表
	 * @param x  对象
	 * @param fields  要过滤的属性
	 * @return
	 */
	public <X> List<X> findByEntity(X x,String... fields) {
		
		String hql = queryHqlSetter(x.getClass().getSimpleName(), fields);
		
		Map<String, Object> params =  Maps.newHashMapWithExpectedSize(fields.length);
		for(String field : fields){
			params.put(field, Reflections.getFieldValue(x, field));
		}
		Query q = createQuery(hql, params);
		return q.list();
	}
	
	
	/**
	 * 查询当前对象的列表
	 * @param params 参数
	 * @return
	 */
	public  List<T> find(Map<String, Object> params) {
		String hql = queryHqlSetter(entityClass.getSimpleName(), params);
		Query q = createQuery(hql, params);
		return q.list();
	}
	/**
	 * 查询当前对象的列表
	 * @param t  当前对象
	 * @param fields 要过滤的参数
	 * @return
	 */
	public  List<T> find(T t ,String... fields) {
		String hql = queryHqlSetter(entityClass.getSimpleName(), fields);
		
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(fields.length);
		for(String field : fields){
			params.put(field, Reflections.getFieldValue(t, field));
		}
		Query q = createQuery(hql, params);
		return q.list();
	}
	 
	
	
	/**
	 * 
	 * 根据entityName,params 拼装hql，要求params MAP的key 必须是entity的属性
	 * @author wj
	 * @param entityName
	 * @param params
	 * @return
	 */
	private String queryHqlSetter(String entityName, Map<String, Object> params){
		Assert.hasText(entityName,"entityName不应为空");
		String hql = "from "+entityName +" where 1=1 ";
		if (params != null) {
            Set<String> keySet = params.keySet();
            for (String string : keySet) {
            	hql += "and "+string+" =:"+string;
            }
        }
		
		return hql;
		
	}
	
	/**
	 * 根据给定对象的属性 自动拼接hql
	 * @author wj
	 * @param entityName
	 * @param fieldNames
	 * @return
	 */
	private String queryHqlSetter(String entityName , String... fieldNames){
		Assert.hasText(entityName,"entityName不应为空");
		String hql = "from "+entityName +" where 1=1 ";
		if ( !ArrayUtils.isEmpty(fieldNames)) {
			
			for (String field : fieldNames) {
				hql += "and "+field+" =:"+field;
			}
		}
		return hql;
	}
	
	/**
	 * 返回任意类型的实体，区别于<T>
	 * @param hql
	 * @param params
	 * @return
	 */
	public <E> List<E> findme(String hql, Map<String, Object> params) {
		Query q = createQuery(hql, params);
		return q.list();
	}

	 /**
	  * 
	  * @param hql
	  * @param params
	  * @param page  当前页
	  * @param rows   每页数量
	  * @return
	  */
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = createQuery(hql, params);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	 
	public List<T> find(String hql, int page, int rows) {
		Query q = createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	
	/**
	 * 已经解析好的页码，hibernate直接用的
	 * @param hql
	 * @param first
	 * @param max
	 * @return
	 */
	public List<T> find1(String hql, int first, int max) {
		Query q = createQuery(hql);
		return q.setFirstResult(first).setMaxResults(max).list();
	}
	
	
	  /** 
     * 去除hql的orderBy子句。 
     * @param qlString
     * @return 
     */  
    private String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    } 
	
	
    /** 
     * 去除qlString的select子句。 
     * @param qlString
     * @return 
     */  
    private String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    }  
    
    
    /**
	 * 创建 QL 查询对象  并set值
	 * @param qlString  hql语句
	 * @param parameter  参数
	 * @return
	 */
	public Query createQuery(String qlString,  Map<String,Object> parameter){
		Query query = getCurrentSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString){
		Query query = getCurrentSession().createQuery(qlString);
		return query;
	}
	
	//Criteria 动态参数绑定就比较麻烦了
    /**
	 * 设置查询参数
	 * @param query  已经带有参数的hql了
	 * @param parameter
	 */
	private void setParameter(Query query, Map<String,Object> parameter){
		if (parameter != null) {
            Set<String> keySet = parameter.keySet();
            for (String string : keySet) {
                Object value = parameter.get(string);
                //这里考虑传入的参数是什么类型，不同类型使用的方法不同  
                if(value instanceof Collection<?>){
                    query.setParameterList(string, (Collection<?>)value);
                }else if(value instanceof Object[]){
                    query.setParameterList(string, (Object[])value);
                }else{
                    query.setParameter(string, value);
                }
            }
        }
	}
    
	
	
	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public  Page<T> find(Page<T> page, String qlString, Map<String,Object> parameter){
		// get count
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
	        Query query = createQuery(countQlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setTotal(Integer.valueOf(list.get(0).toString()));
	        }else{
	        	page.setTotal(list.size());
	        }
			if (page.getTotal() < 1) {
				return page;
			}
			
			
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query1 = createQuery(ql, parameter); 
    	// set page
        query1.setFirstResult(page.getFirstResult());
        query1.setMaxResults(page.getLimit()); 
        page.setDataList(query1.list());
		return page;
    }
    
    
    
   

	 
	public Long count(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return (Long) q.uniqueResult();
	}

	 
	public Long count(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return (Long) q.uniqueResult();
	}

	 
	public int executeHql(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}

	 
	public int executeHql(String hql, Map<String, Object> params) {
		Query q = createQuery(hql, params);
		return q.executeUpdate();
	}

	 
	public List<Map> findBySql(String sql) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	 
	public List<Map> findBySql(String sql, int page, int rows) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	 
	public List<Map> findBySql(String sql, Map<String, Object> params) {
//		SQLQuery q = getCurrentSession().createSQLQuery(sql);
//		if (params != null && !params.isEmpty()) {
//			for (String key : params.keySet()) {
//				q.setParameter(key, params.get(key));
//			}
//		}
		SQLQuery q =createSqlQuery(sql, params);
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	 
	public List<Map> findBySql(String sql, Map<String, Object> params, Integer page, Integer rows) {
		SQLQuery q = createSqlQuery(sql, params);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	 
	public int executeSql(String sql) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.executeUpdate();
	}

	 
	public int executeSql(String sql, Map<String, Object> params) {
		SQLQuery q = createSqlQuery(sql, params);
		return q.executeUpdate();
	}

	 
	public BigInteger countBySql(String sql) {
		SQLQuery q =createSqlQuery(sql);
		return (BigInteger) q.uniqueResult();
	}

	 
	public BigInteger countBySql(String sql, Map<String, Object> params) {
		SQLQuery q =createSqlQuery(sql);
		return (BigInteger) q.uniqueResult();
	}

	 
	public List<T> findListBySql(String sql,T t) {
		
		SQLQuery q = getCurrentSession().createSQLQuery(sql).addEntity( t.toString(),t.getClass());
		return q.list();
		
		
	}
	
	 
	public List<T> findListBySql(String sql, int page, int rows,T t) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql).addEntity( t.toString(),t.getClass());
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	 
	public void saveme(Object o) {
		if (o != null) {
			 getCurrentSession().saveOrUpdate(o);
		}
	
	}
	
	 
	public void updateme(Object o) {
		if (o != null) {
			getCurrentSession().update(o);
		}
	}
	
	
	
	
	public List<T> findAll(){
		return getCurrentSession().createCriteria(entityClass).list();
	}
	
	
	

	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		page.setTotal(Integer.valueOf(count(detachedCriteria)+""));
		if (page.getTotal() < 1) {
			return page;
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
	        criteria.setFirstResult(page.getFirstResult());
	        criteria.setMaxResults(page.getLimit()); 
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())){ //column asc,column desc
			for (String order : StringUtils.split(page.getOrderBy(), ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					criteria.addOrder(Order.asc(o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						criteria.addOrder(Order.desc(o[0]));
					}else{
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		page.setDataList(criteria.list());
		return page;
	}

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list(); 
	}
	
	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}
	
	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}
	
	
	/**
	 * 物理删除当前对象
	 * @param id
	 * @return
	 */
	public int delete(Serializable id){
		String hql = "delete from " +entityClass.getSimpleName()+" where id=:p1";
		return getCurrentSession().createQuery(hql).setParameter("p1", id).executeUpdate();
	}

	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString,  Map<String, Object> parameter, Class<?> resultClass){
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}
	/**
	 * 创建 SQL 查询对象  并set值
	 * @param sqlString  sql 语句
	 * @param parameter  参数
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString, Map<String, Object>  parameter){
		SQLQuery query = getCurrentSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}
	/**
	 * 创建 SQL 查询对象  并set值
	 * @param sqlString  sql 语句
	 * @param parameter  参数
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString){
		SQLQuery query = getCurrentSession().createSQLQuery(sqlString);
		setParameter(query, null);
		return query;
	}
	
	/**
	 * 设置查询结果类型
	 * @param query
	 * @param resultClass
	 */
	private void setResultTransformer(SQLQuery query, Class<?> resultClass){
		if (resultClass != null){
			if (resultClass == Map.class){
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (resultClass == List.class){
				query.setResultTransformer(Transformers.TO_LIST);
			}else{
				query.addEntity(resultClass);
			}
		}
	}
	private void setResultTransformer(Query query, Class<?> resultClass){
		if (resultClass != null){
			if (resultClass == Map.class){
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (resultClass == List.class){
				query.setResultTransformer(Transformers.TO_LIST);
			}else{
				query.setResultTransformer(Transformers.aliasToBean(resultClass));
			}
		}
	}
}
