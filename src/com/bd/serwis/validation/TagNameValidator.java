package com.bd.serwis.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.TagNameValidator")
public class TagNameValidator implements Validator {

	final private Pattern pattern;
	private Matcher matcher;

	public TagNameValidator() {
		pattern = Pattern.compile("^([A-Za-z])+([,]([A-Za-z])+)*$");
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		matcher = pattern.matcher(value.toString());
		
		if (!matcher.matches()) {

			FacesMessage msg = new FacesMessage("Niepoprawny format. Poprawny format: slowo1,slowo2,..");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}

		String[] checks = value.toString().split(",");
		for (String str : checks) {
			str.replaceAll("\\s+", "");
			if (str.length() > 45) {
				FacesMessage msg = new FacesMessage(
						"D³ugoœæ jednego s³owa kluczowego nie mo¿e przekraczaæ 45 znaków");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}
}