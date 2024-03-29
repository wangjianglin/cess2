package io.cess.demo.services.impl;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import io.cess.demo.entity.TestEntity;
import io.cess.demo.services.TestService;
import org.springframework.transaction.annotation.Transactional;

import io.cess.demo.entity.TestEntity;
import io.cess.demo.services.TestService;
import io.cess.util.IDGener;

@Named
//@Service
@Transactional
public class TestServiceImpl implements TestService {

	//@PersistenceContext(unitName="default2")
//	@PersistenceContext
	private EntityManager jpa;
	
	@Override
	public TestEntity get(long id) {
		return jpa.find(TestEntity.class, id);
	}

	@Override
	public TestEntity save(TestEntity entity) {
		entity.setId(IDGener.next());
		entity.setName("name:"+IDGener.next());
		entity.setValue("value:"+IDGener.next());
		jpa.persist(entity);
		return entity;
	}

	@Override
	public TestEntity delete(long id) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestEntity> list() {
		Query query = jpa.createQuery("select a from lin.demo.entity.TestEntity a");
		return query.getResultList();
//		return null;
	}

}
