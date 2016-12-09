package com.kingen.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页控制器
 *
 * @author HenryYan
 */
@Controller
@RequestMapping("/main")
public class MainController {

  //JS success 重定向到这
  	@RequestMapping(value = "home" ,method = RequestMethod.GET)
  	public ModelAndView  welcome( Model model) {//用这个返回首页 ext请求controller js会加上前缀login 导致404
  		ModelAndView modelAndView = new ModelAndView();  
          modelAndView.addObject("", "");  
          modelAndView.setViewName("home");  
          return modelAndView;  
  		
  	}
}
