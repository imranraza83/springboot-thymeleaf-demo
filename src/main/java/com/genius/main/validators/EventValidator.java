package com.genius.main.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.genius.main.models.Event;

@Component
public class EventValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
	
		return Event.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
//		ValidationUtils.rejectIfEmpty(errors, "startDate", "startDate.empty");
//		ValidationUtils.rejectIfEmpty(errors, "endDate", "endDate.empty");
		Event event = (Event)target;
		Date startDate = null;
		Date endDate=null;
		try {
			if(event.getStartDate().isEmpty())
				throw new Exception("Cannot parse empty date value");
			 startDate = new SimpleDateFormat("dd/MM/yyyy").parse(event.getStartDate()) ;
		} catch (ParseException e1) {
			e1.printStackTrace();
			errors.rejectValue("startDate", "startDate.parseError");
		}catch(Exception ex) {
			ex.printStackTrace();
			errors.rejectValue("startDate", "startDate.parseError");
			return;
		}
		try {
			if(event.getEndDate().isEmpty())
				throw new Exception("Cannot parse empty date value");
			 endDate = new SimpleDateFormat("dd/MM/yyyy").parse(event.getEndDate()) ;
		} catch (ParseException e) {
			e.printStackTrace();
			errors.rejectValue("endDate", "endDate.parseError");
		}catch(Exception ex) {
			ex.printStackTrace();
			errors.rejectValue("endDate", "endDate.parseError");
			return;
		}
		if(startDate.after(endDate)) {
			errors.rejectValue("startDate", "startDate.lessThen.endDate");
		}
	}

}
