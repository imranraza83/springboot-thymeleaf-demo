package com.genius.main.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.genius.main.models.Event;
import com.genius.main.validators.EventValidator;

@Controller
public class WebApplicationController {
	private String applicationEnvironment;
	private EventValidator eventValidator;
	
	@Autowired
	public WebApplicationController(Environment environment, EventValidator eventValidator){
		this.applicationEnvironment = environment.getProperty("application-environment");
		this.eventValidator = eventValidator;
	}
	
	@InitBinder("event")
	protected void initEventBinder(WebDataBinder binder) {
		binder.addValidators(eventValidator);
	}
	
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("startdate", new Date());
		model.addAttribute("enddate", new Date());
		model.addAttribute("applicationEnvironment", applicationEnvironment);
		model.addAttribute("event", new Event());
		return "index";
	}
	
	@RequestMapping("/compareDates")
	public String result(@Valid @ModelAttribute("event") Event event,BindingResult bindingResult, Model model) throws ParseException {
		if (bindingResult.hasErrors()) {
		    return "index";
		}
		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(event.getStartDate()) ;
		Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(event.getEndDate()) ;
		
		long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    model.addAttribute("noofdays", diff);
		return "result";
	}
}
