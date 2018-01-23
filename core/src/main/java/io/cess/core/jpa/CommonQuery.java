package io.cess.core.jpa;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import io.cess.CessException;
import io.cess.util.IDGener;

/**
 * 
 * @author 王江林
 * @date 2011-7-6
 * 提供公共数据查询功能
 */
public class CommonQuery {

	private static Map<Object,Map<String,String>> nameMap = new HashMap<Object, Map<String,String>>();
	
	/**
	 * 
	 * @param jpa
	 */
	private static void initNameMap(EntityManager jpa){
		Set<EntityType<?>> ets = jpa.getMetamodel().getEntities();
		if(ets == null){
			nameMap.put(jpa.getEntityManagerFactory(), new HashMap<String, String>());
		}else{
			Map<String,String> tmp = new HashMap<String, String>();
			nameMap.put(jpa.getEntityManagerFactory(), tmp);
			Class<?> t = null;
			javax.persistence.NamedQuery a = null;
			javax.persistence.NamedQueries as = null;
			for(EntityType<?> et:ets){
				t = et.getJavaType();
				if(t == null){
					continue;
				}
				a = t.getAnnotation(javax.persistence.NamedQuery.class);
				if(a!=null){
					tmp.put(a.name(),a.query());
				}
				as = t.getAnnotation(javax.persistence.NamedQueries.class);
				if(as != null && as.value() != null){
					for(javax.persistence.NamedQuery tmpa:as.value()){
						tmp.put(tmpa.name(), tmpa.query());
					}
				}
			}
		}
	}
	
	/**
	 * 根据 命名查询 进行查询
	 * @param jpa
	 * @param queryParams
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> nameQuery(EntityManager jpa,CommonQueryParams<T> queryParams){
		Long exceptionCode = queryParams.getExceptionCode();
		String exceptionMessage = queryParams.getExceptionMessage();
		//判断异常码为负数则抛出异常
		if(exceptionCode != null && exceptionCode < 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Collection<T>) nameQuery(jpa,queryParams.getQuery(),queryParams.getParams());
				}else{
					return (Collection<T>) nameQuery(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		//判断异常码为正数，则抛出警告
		//nameQuery
		if(exceptionCode != null && exceptionCode > 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Collection<T>) nameQuery(jpa,queryParams.getQuery(),queryParams.getParams());
				}else{
					return (Collection<T>) nameQuery(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		if(queryParams.getNames() == null || queryParams.getNames().length == 0){
			return (Collection<T>) nameQuery(jpa,queryParams.getQuery(),queryParams.getParams());
		}else{
			return (Collection<T>) nameQuery(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
		}
	}
	
	
	public static Collection<?> nameQuery(EntityManager jpa,String query,Object...params){
		Page<?> page = nameQuery(jpa,query,null,null,params);
		if(page == null){
			return null;
		}
		return page.getList();
	}
	
	@Deprecated
	public static Collection<?> nameQuery(EntityManager jpa,String query,String[] names,Object...params){
		Page<?> page = nameQuery(jpa,query,null,null,names,params);
		if(page == null){
			return null;
		}
		return page.getList();
	}
	
	
	//支持分页查询
	@SuppressWarnings("unchecked")
	public static <T> Page<T> nameQuery(EntityManager jpa,CommonQueryParams<T> queryParams,Integer pageNo,Integer pageSize){
		Long exceptionCode = queryParams.getExceptionCode();
		String exceptionMessage = queryParams.getExceptionMessage();
		if(exceptionCode != null && exceptionCode < 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Page<T>) nameQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
				}else{
					return (Page<T>) nameQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		//nameQuery
		if(exceptionCode != null && exceptionCode > 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Page<T>) nameQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
				}else{
					return (Page<T>) nameQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		if(queryParams.getNames() == null || queryParams.getNames().length == 0){
			return (Page<T>) nameQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
		}else{
			return (Page<T>) nameQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
		}
	}
	/**
	 * 
	 * @param jpa
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Page<?> nameQuery(EntityManager jpa,String query,Integer pageNo,Integer pageSize,Object...params){
		if(jpa == null || query == null || query.equals("")){
			return null;
		}
		if(pageSize != null){
			if(nameMap.containsKey(jpa.getEntityManagerFactory()) == false){
				initNameMap(jpa);
			}
			return query(jpa,nameMap.get(jpa.getEntityManagerFactory()).get(query),pageNo,pageSize,params);
		}
		Query q = jpa.createNamedQuery(query);
		if(params!=null&& params.length>0){
			for(int n=0;n<params.length;n++){
				q.setParameter(n+1,params[n]);
			}
		}
		List<?> tmp = q.getResultList();
		@SuppressWarnings("rawtypes")
		Page page = new Page();
		page.setList(tmp);
		if(tmp!=null){
			page.setTotal(tmp.size());
		}else{
			page.setTotal(0);
		}
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Page<?> nameQuery(EntityManager jpa,String query,Integer pageno,Integer pagesize,String[] names,Object...params){
		if(jpa == null || query == null || query.equals("")){
			return null;
		}
		if(pagesize != null){
			if(nameMap.containsKey(jpa.getEntityManagerFactory()) == false){
				initNameMap(jpa);
			}
			return query(jpa,nameMap.get(jpa.getEntityManagerFactory()).get(query),pageno,pagesize,names,params);
		}
		Query q = jpa.createNamedQuery(query);
		if(params!=null&& params.length>0){
			if(params!=null&& params.length>0){
				for(int n=0;n<params.length&&n<names.length;n++){
					q.setParameter(names[n],params[n]);
				}
			}else{
				for(int n=0;n<params.length;n++){
					q.setParameter(n+1,params[n]);
				}
			}
		}
		
		List<?> tmp = q.getResultList();
		@SuppressWarnings("rawtypes")
		Page page = new Page();
		page.setList(tmp);
		if(tmp!=null){
			page.setTotal(tmp.size());
		}else{
			page.setTotal(0);
		}
		return page;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> query(EntityManager jpa,CommonQueryParams<T> queryParams){
		Long exceptionCode = queryParams.getExceptionCode();
		String exceptionMessage = queryParams.getExceptionMessage();
		if(exceptionCode != null && exceptionCode < 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Collection<T>) query(jpa,queryParams.getQuery(),queryParams.getParams());
				}else{
					return (Collection<T>) query(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		//nameQuery
		if(exceptionCode != null && exceptionCode > 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Collection<T>) query(jpa,queryParams.getQuery(),queryParams.getParams());
				}else{
					return (Collection<T>) query(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		if(queryParams.getNames() == null || queryParams.getNames().length == 0){
			return (Collection<T>) query(jpa,queryParams.getQuery(),queryParams.getParams());
		}else{
			return (Collection<T>) query(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
		}
	}
	
	public static Collection<?> query(EntityManager jpa,String query,Object...params){
		Page<?> page = query(jpa,query,null,null,params);
		if(page == null){
			return null;
		}
		return page.getList();
	}
	
	@Deprecated
	public static Collection<?> query(EntityManager jpa,String query,String[] names,Object...params){
		Page<?> page = query(jpa,query,null,null,names,params);
		if(page == null){
			return null;
		}
		return page.getList();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Page<T> query(EntityManager jpa,CommonQueryParams<T> queryParams,Integer pageNo,Integer pageSize){
		Long exceptionCode = queryParams.getExceptionCode();
		String exceptionMessage = queryParams.getExceptionMessage();
		if(exceptionCode != null && exceptionCode < 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Page<T>) query(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
				}else{
					return (Page<T>) query(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		//nameQuery
		if(exceptionCode != null && exceptionCode > 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Page<T>) query(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
				}else{
					return (Page<T>) query(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		if(queryParams.getNames() == null || queryParams.getNames().length == 0){
			return (Page<T>) query(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
		}else{
			return (Page<T>) query(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
		}
	}
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Page<?> query(EntityManager jpa,String query,Integer pageno,Integer pagesize,Object...params){
		if(jpa == null || query == null || query.equals("")){
			return null;
		}
		Query q = jpa.createQuery(query);
		//行判断是否需要求total
		boolean isTotal = false;
		if(pageno!=null && pagesize != null){
			q.setFirstResult(pageno*pagesize);
			q.setMaxResults(pagesize);
			isTotal = true;
		}else if(pagesize != null){
			q.setFirstResult(0);
			q.setMaxResults(pagesize);
			isTotal = true;
		}
		if(params!=null&& params.length>0){
			for(int n=0;n<params.length;n++){
				q.setParameter(n+1,params[n]);
			}
		}
		if(isTotal){
			//Query q2 = jpa.createQuery("select count(*) from ("+query+")");
			int index = query.toLowerCase().indexOf(" from ");
			//要去掉不影响数量的语句如order by
			String tmpQuery = "select count(*) "+query.substring(index);
			Query q2 = jpa.createQuery(tmpQuery);
			if(params!=null&& params.length>0){
				for(int n=0;n<params.length;n++){
					q2.setParameter(n+1,params[n]);
				}
			}
			List<?> tmpList = q2.getResultList();
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			long tmpTotal = (Long) tmpList.get(0);
			page.setTotal((int) tmpTotal);
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			return page;
		}else{
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			if(page.getList()!=null){
				page.setTotal(page.getList().size());
			}else{
				page.setTotal(0);
			}
			return page;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Page<?> query(EntityManager jpa,String query,Integer pageno,Integer pagesize,String[] names,Object...params){
		if(jpa == null || query == null || query.equals("")){
			return null;
		}
		Query q = jpa.createQuery(query);
		//行判断是否需要求total
		boolean isTotal = false;
		if(pageno!=null && pagesize != null){
			q.setFirstResult(pageno*pagesize);
			q.setMaxResults(pagesize);
			isTotal = true;
		}else if(pagesize != null){
			q.setFirstResult(0);
			q.setMaxResults(pagesize);
			isTotal = true;
		}
		if(params!=null&& params.length>0){
			if(params!=null&& params.length>0){
				for(int n=0;n<params.length&&n<names.length;n++){
					q.setParameter(names[n],params[n]);
				}
			}else{
				for(int n=0;n<params.length;n++){
					q.setParameter(n+1,params[n]);
				}
			}
		}
		if(isTotal){
			//Query q2 = jpa.createQuery("select count(*) from ("+query+")");
			int index = query.toLowerCase().indexOf(" from ");
			//要去掉不影响数量的语句如order by
			String tmpQuery = "select count(*) "+query.substring(index);
			Query q2 = jpa.createQuery(tmpQuery);
			if(params!=null&& params.length>0 && names!=null && names.length>0){
				for(int n=0;n<params.length&&n<names.length;n++){
					q2.setParameter(names[n],params[n]);
				}
			}
			List<?> tmpList = q2.getResultList();
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			long tmpTotal = (Long) tmpList.get(0);
			page.setTotal((int) tmpTotal);
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			return page;
		}else{
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			if(page.getList()!=null){
				page.setTotal(page.getList().size());
			}else{
				page.setTotal(0);
			}
			return page;
		}
	}
	
	

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> nativeQuery(EntityManager jpa,CommonQueryParams<T> queryParams){
		Long exceptionCode = queryParams.getExceptionCode();
		String exceptionMessage = queryParams.getExceptionMessage();
		if(exceptionCode != null && exceptionCode < 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Collection<T>) nativeQuery(jpa,queryParams.getQuery(),queryParams.getParams());
				}else{
					return (Collection<T>) nativeQuery(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		//nameQuery
		if(exceptionCode != null && exceptionCode > 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Collection<T>) nativeQuery(jpa,queryParams.getQuery(),queryParams.getParams());
				}else{
					return (Collection<T>) nativeQuery(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		if(queryParams.getNames() == null || queryParams.getNames().length == 0){
			return (Collection<T>) nativeQuery(jpa,queryParams.getQuery(),queryParams.getParams());
		}else{
			return (Collection<T>) nativeQuery(jpa,queryParams.getQuery(),queryParams.getNames(),queryParams.getParams());
		}
	}
	
	public static Collection<?> nativeQuery(EntityManager jpa,String query,Object...params){
		Page<?> page = query(jpa,query,null,null,params);
		if(page == null){
			return null;
		}
		return page.getList();
	}
	
	@Deprecated
	public static Collection<?> nativeQuery(EntityManager jpa,String query,String[] names,Object...params){
		Page<?> page = query(jpa,query,null,null,names,params);
		if(page == null){
			return null;
		}
		return page.getList();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Page<T> nativeQuery(EntityManager jpa,CommonQueryParams<T> queryParams,Integer pageNo,Integer pageSize){
		Long exceptionCode = queryParams.getExceptionCode();
		String exceptionMessage = queryParams.getExceptionMessage();
		if(exceptionCode != null && exceptionCode < 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Page<T>) nativeQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
				}else{
					return (Page<T>) nativeQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		//nameQuery
		if(exceptionCode != null && exceptionCode > 0){
			try{
				if(queryParams.getNames() == null || queryParams.getNames().length == 0){
					return (Page<T>) nativeQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
				}else{
					return (Page<T>) nativeQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
				}
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		if(queryParams.getNames() == null || queryParams.getNames().length == 0){
			return (Page<T>) nativeQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getParams());
		}else{
			return (Page<T>) nativeQuery(jpa,queryParams.getQuery(),pageNo,pageSize,queryParams.getNames(),queryParams.getParams());
		}
	}
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Page<?> nativeQuery(EntityManager jpa,String query,Integer pageno,Integer pagesize,Object...params){
		if(jpa == null || query == null || query.equals("")){
			return null;
		}
		Query q = jpa.createNativeQuery(query);
		//行判断是否需要求total
		boolean isTotal = false;
		if(pageno!=null && pagesize != null){
			q.setFirstResult(pageno*pagesize);
			q.setMaxResults(pagesize);
			isTotal = true;
		}else if(pagesize != null){
			q.setFirstResult(0);
			q.setMaxResults(pagesize);
			isTotal = true;
		}
		if(params!=null&& params.length>0){
			for(int n=0;n<params.length;n++){
				q.setParameter(n+1,params[n]);
			}
		}
		if(isTotal){
			//Query q2 = jpa.createQuery("select count(*) from ("+query+")");
			int index = query.toLowerCase().indexOf(" from ");
			//要去掉不影响数量的语句如order by
			String tmpQuery = "select count(*) "+query.substring(index);
			Query q2 = jpa.createNativeQuery(tmpQuery);
			if(params!=null&& params.length>0){
				for(int n=0;n<params.length;n++){
					q2.setParameter(n+1,params[n]);
				}
			}
			List<?> tmpList = q2.getResultList();
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			long tmpTotal = ((Number) tmpList.get(0)).longValue();
			page.setTotal((int) tmpTotal);
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			return page;
		}else{
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			if(page.getList()!=null){
				page.setTotal(page.getList().size());
			}else{
				page.setTotal(0);
			}
			return page;
		}
	}
	@SuppressWarnings("unchecked")
	@Deprecated	
	public static Page<?> nativeQuery(EntityManager jpa,String query,Integer pageno,Integer pagesize,String[] names,Object...params){
		if(jpa == null || query == null || query.equals("")){
			return null;
		}
		Query q = jpa.createNativeQuery(query);
		//行判断是否需要求total
		boolean isTotal = false;
		if(pageno!=null && pagesize != null){
			q.setFirstResult(pageno*pagesize);
			q.setMaxResults(pagesize);
			isTotal = true;
		}else if(pagesize != null){
			q.setFirstResult(0);
			q.setMaxResults(pagesize);
			isTotal = true;
		}
		if(params!=null&& params.length>0){
			if(params!=null&& params.length>0){
				for(int n=0;n<params.length&&n<names.length;n++){
					q.setParameter(names[n],params[n]);
				}
			}else{
				for(int n=0;n<params.length;n++){
					q.setParameter(n+1,params[n]);
				}
			}
		}
		if(isTotal){
			//Query q2 = jpa.createQuery("select count(*) from ("+query+")");
			int index = query.toLowerCase().indexOf(" from ");
			//要去掉不影响数量的语句如order by
			String tmpQuery = "select count(*) "+query.substring(index);
			Query q2 = jpa.createNativeQuery(tmpQuery);
			if(params!=null&& params.length>0 && names!=null && names.length>0){
				for(int n=0;n<params.length&&n<names.length;n++){
					q2.setParameter(names[n],params[n]);
				}
			}
			List<?> tmpList = q2.getResultList();
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			long tmpTotal = ((Number) tmpList.get(0)).longValue();
			page.setTotal((int) tmpTotal);
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			return page;
		}else{
			@SuppressWarnings("rawtypes")
			Page page = new Page();
			page.setPageSize(q.getMaxResults());
			if(pageno != null){
				page.setPageNo(pageno);
			}else{
				page.setPageNo(0);
			}
			page.setList(q.getResultList());
			if(page.getList()!=null){
				page.setTotal(page.getList().size());
			}else{
				page.setTotal(0);
			}
			return page;
		}
	}
	
	/**
	 * 暂不处理
	 * @param jpa
	 * @param cq
	 * @param pageno
	 * @param pagesize
	 * @param params
	 * @return
	 */
//	public static Page query(EntityManager jpa,CriteriaQuery cq,Integer pageno,Integer pagesize,Object...params){
//		if(cq == null || jpa == null){
//			return null;
//		}
//		Query q = jpa.createQuery(cq);
//		//行判断是否需要求total
//		boolean isTotal = false;
//		if(pageno!=null && pagesize != null){
//			q.setFirstResult(pageno*pagesize);
//			q.setMaxResults(pagesize);
//			isTotal = true;
//		}else if(pagesize != null){
//			q.setFirstResult(0);
//			q.setMaxResults(pagesize);
//			isTotal = true;
//		}
//		if(params!=null&& params.length>0){
//			for(int n=0;n<params.length;n++){
//				q.setParameter(n+1,params[n]);
//			}
//		}
//		if(isTotal){
//			//求总数
//			CriteriaBuilder cb = jpa.getCriteriaBuilder();
//			Root r = cq.from(lin.contract.entity.Contract.class);
//			 Iterator it = cq.getRoots().iterator();
//			 while(it.hasNext()){
//				 Object tmp = it.next();
//				 System.out.println(tmp instanceof Expression<?>);
//			 }
//			//cq.multiselect(cb.function("count", String.class,r));
//			 cq.select(cb.function("count", String.class,(Expression<?>[])null));
//			 //r.
//			Query q2 = jpa.createQuery(cq);
//			List tmpList = q2.getResultList();
//			Page page = new Page();
//			page.setPageSize(q.getMaxResults());
//			if(pageno != null){
//				page.setPageNo(pageno);
//			}else{
//				page.setPageNo(0);
//			}
//			page.setList(q.getResultList());
//			return page;
//		}else{
//			Page page = new Page();
//			page.setPageSize(q.getMaxResults());
//			if(pageno != null){
//				page.setPageNo(pageno);
//			}else{
//				page.setPageNo(0);
//			}
//			page.setList(q.getResultList());
//			if(page.getList()!=null){
//				page.setTotal(page.getList().size());
//			}else{
//				page.setTotal(0);
//			}
//			return page;
//		}
//	}
	/**
	 * 根据参数位置(1、2、3...)更新数据库对象 (lisy)
	 * 此处的query参数是sql语句
	 * @param jpa
	 * @param query
	 * @param params
	 * @return
	 */
	public static int nameUpdate(EntityManager jpa,String query,Object...params){
		Query q = jpa.createNamedQuery(query);
		if(params!=null&& params.length>0){
			for(int n=0;n<params.length;n++){
				q.setParameter(n+1,params[n]);
			}
		}
		return q.executeUpdate();
	}
	
	/**
	 * 根据参数名称更新数据库对象	(lisy)
	 * 此处的query参数是sql语句
	 * @param jpa
	 * @param query
	 * @param names
	 * @param params
	 * @return
	 */
	public static int nameUpdate(EntityManager jpa,String query,String[] names,Object...params){
		Query q = jpa.createNamedQuery(query);
		if(params!=null&& params.length>0 && names!=null && names.length>0){
			for(int n=0;n<params.length&&n<names.length;n++){
				q.setParameter(names[n],params[n]);
			}
		}
		return q.executeUpdate();
	}
	
	/**
	 * 根据参数名称更新数据库对象	(lisy)
	 * 此处的query参数是eql语句
	 * @param jpa
	 * @param query
	 * @param params
	 * @return
	 */
	public static int nativeUpdate(EntityManager jpa,String query,Object...params){
		Query q = jpa.createNativeQuery(query);
		if(params!=null&& params.length>0){
			for(int n=0;n<params.length;n++){
				q.setParameter(n+1,params[n]);
			}
		}
		return q.executeUpdate();
	}
	/**
	 * 根据参数位置(1、2、3...)更新数据库对象  (lisy)
	 * 此处的query参数是eql语句
	 * @param jpa
	 * @param query
	 * @param params
	 * @return
	 */
	public static int update(EntityManager jpa,String query,Object...params){
		Query q = jpa.createQuery(query);
		if(params!=null&& params.length>0){
			for(int n=0;n<params.length;n++){
				q.setParameter(n+1,params[n]);
			}
		}
		return q.executeUpdate();
	}
	
	
	
	public static int update(EntityManager jpa,String query,String[] names,Object...params){
		Query q = jpa.createQuery(query);
		if(params!=null&& params.length>0 && names!=null && names.length>0){
			for(int n=0;n<params.length&&n<names.length;n++){
				q.setParameter(names[n],params[n]);
			}
		}
		return q.executeUpdate();
	}
	
	/**
	 * 
	 * @param jpa
	 * @param query
	 * @param names
	 * @param params
	 * @return
	 */
//	public static int nameUpdate(EntityManager jpa,String query,String[] names,Object[] params){
//		Query q = jpa.createNamedQuery(query);
//		if(params!=null&& params.length>0 && names!=null && names.length>0){
//			for(int n=0;n<params.length&&n<names.length;n++){
//				q.setParameter(names[n],params[n]);
//			}
//		}
//		return q.executeUpdate();
//	}

	
	/**
	 * @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long id;
		主键自动生成 	(lisy)
	 * @param <T>
	 * @param jpa
	 * @param entity
	 * @return
	 */
	public static <T> T update(EntityManager jpa,T entity){
		if(entity == null){
			return null;
		}
		Class<?> tmp = entity.getClass();
		//先对二级对象进行处理，
		//暂不处理这样情况，一、这种情况出现的比较少；二、对性能有影响，不划算,由业务自己处理
		//Method[] methods = tmp.getMethods();
		//
		Field[] fields = tmp.getFields();
		if(fields != null){
			for (Field field : fields) {
				if (field.getAnnotation(Id.class) != null) {
					try {
						Object tmpValue = field.get(entity);// field.getType()
						if (field.getAnnotation(GeneratedValue.class) == null) {
							if (field.getType().equals(Integer.class)
									&& (tmpValue == null || tmpValue.equals(0))) {
								field.set(entity, (int) IDGener.next());
							} else if (field.getType().equals(Long.class)
									&& (tmpValue == null || tmpValue.equals(0l))) {
								field.set(entity, IDGener.next());
							} else if (tmpValue != null) {
								entity = jpa.merge(entity);
							} else {
								// break;
							}
						} else {
							if (field.getType().toString().equals("class java.lang.Long")) {
								if(tmpValue.equals(0l)){
									field.set(entity, null);
								}else{
									entity = jpa.merge(entity);
								}
							}else if (field.getType().toString().equals("class java.lang.Integer")) {
								if(tmpValue.equals(0)){
									field.set(entity, null);
								}else{
									entity = jpa.merge(entity);
								}
							}else{
								entity = jpa.merge(entity);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		//让实体一定处于受管状态
		entity = jpa.merge(entity);
		jpa.persist(entity);
		return entity;
	}
	
//	/**
//	 * 先将要删除的实体变成受控制的状态再进行删除  
//	 * @param jpa
//	 * @param entity
//	 */
//	public static void remove(EntityManager jpa,Object entity){
//		entity = jpa.merge(entity);
//		jpa.remove(entity);
//	}
	
	/**
	 * 先查找到实体再进行删除 
	 * @param jpa
	 * @param c
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T remove(EntityManager jpa,CommonQueryRemoveParams<T> params){
		
		Long exceptionCode = params.getExceptionCode();
		String exceptionMessage = params.getExceptionMessage();
		
		if(exceptionCode != null && exceptionCode < 0){
			try{
				return (T) removeImpl(jpa,params.getType(),params.getId());
			}catch(Throwable e){
				throw new CessException(exceptionCode,exceptionMessage,e);
			}
		}
		
		if(exceptionCode != null && exceptionCode > 0){
			try{
				return (T) removeImpl(jpa,params.getType(),params.getId());
			}catch(Throwable e){
				CessException.add(new CessException(exceptionCode,exceptionMessage,e));
				return null;
			}
		}
		
		return (T) removeImpl(jpa,params.getType(),params.getId());
	}
	
	private static Object removeImpl(EntityManager jpa,Class<?> c,long id){
		
		Object entity = jpa.find(c,id);
		if(entity == null){
			return null;
		}
		jpa.remove(entity);
		return entity;
	}
	
}
