package io.cess.demo.controller;

import java.util.List;

import javax.inject.Inject;

import io.cess.core.spring.Prefix;
import io.cess.demo.entity.TestEntity;
import io.cess.demo.services.TestService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/test")
@Controller
public class TestController {

	
//	@InitBinder("entity")
//	public void initBinder(WebDataBinder binder){
//		binder.setFieldDefaultPrefix("entity.");
//	}
	
	@RequestMapping("/savePathVariable/{id}")
	@ResponseBody
	public String savePathVariable(@PathVariable String id){
		return id;
	}
	
	@RequestMapping("/saveEntity")
	@ResponseBody
	public TestEntity saveEntity(/*@Prefix()*/@Prefix("entity")TestEntity entity){
		return entity;
	}
	

	@RequestMapping("/saveStringList")
	@ResponseBody
//	public TestEntity test(@Prefix("entity")java.util.Date date,TestEntity entity,String name,@PathVariable String id){
//	public TestEntity test(@ModelAttribute("entity")TestEntity entity){
//	public List<String> saveStringList(@Prefix()List<String> ids){
//	public List<String> saveStringList(@Prefix List<String> ids){
	public String[] saveStringList(@Prefix String[] ids){
//		TestEntity entity = new TestEntity();
//		service.save(entity);
		return ids;
	}
	
	@RequestMapping("/saveLongList")
	@ResponseBody
//	public TestEntity test(@Prefix("entity")java.util.Date date,TestEntity entity,String name,@PathVariable String id){
//	public TestEntity test(@ModelAttribute("entity")TestEntity entity){
//	public List<Long> saveLongList(@Prefix()List<Long> ids){
//	public List<Long> saveLongList(@RequestBody List<Long> ids){
	public List<Long> saveLongList(@Prefix List<Long> ids){
		
//		TestEntity entity = new TestEntity();
//		service.save(entity);
		return ids;
	}
	
	@RequestMapping("/saveMap")
	@ResponseBody
//	public TestEntity test(@Prefix("entity")java.util.Date date,TestEntity entity,String name,@PathVariable String id){
//	public TestEntity test(@ModelAttribute("entity")TestEntity entity){
	public TestEntity saveMap(@PathVariable String id,@Prefix()TestEntity entity,@Prefix()List<Long> ids){
//		TestEntity entity = new TestEntity();
//		service.save(entity);
		return entity;
	}
	
}
