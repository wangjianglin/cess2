package lin
/**
  * Created by lin on 4/3/16.
  */
object Test {

//  def main(args: Array[String]): Unit ={
//    println("ok");
//  }
}
//package me.ours.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class IndexController {

  @RequestMapping(Array { "/" })
  def index(): String = "index"


  @RequestMapping(Array {"/test"})
  def test(): ModelAndView ={
    return new ModelAndView("index","message","Scala!!")
  }
}