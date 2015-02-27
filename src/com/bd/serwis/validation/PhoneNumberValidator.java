package com.bd.serwis.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.PhoneNumberValidator")
public class PhoneNumberValidator implements Validator {
	
	private static final String PHONE_NUMBER_PATTERN = "^([0-9]{9})|(([0-9]{3}-){2}[0-9]{3})$";

	final private Pattern pattern;
	private Matcher matcher;

	/**
	     *
	     */
	public PhoneNumberValidator() {
		pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		matcher = pattern.matcher(value.toString());
		if (!matcher.matches()) {

			FacesMessage msg = new FacesMessage(
					"Niepoprawny numer telefonu. Poprawny format: 000-000-000 lub 000000000");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}
	}
}