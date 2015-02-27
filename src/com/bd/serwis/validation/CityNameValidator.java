package com.bd.serwis.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.CityNameValidator")
public class CityNameValidator implements Validator {

	public CityNameValidator() {

	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (value.toString().length() > 100) {
			FacesMessage msg = new FacesMessage(
					"Nazwa miasta musi mieæ mniej ni¿ 100 znaków");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}