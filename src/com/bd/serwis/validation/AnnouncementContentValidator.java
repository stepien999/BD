package com.bd.serwis.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.AnnouncementContentValidator")
public class AnnouncementContentValidator implements Validator {

	public AnnouncementContentValidator() {
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (value.toString().length() > 1500) {
			FacesMessage msg = new FacesMessage(
					"Treœæ og³oszenia mo¿e mieæ maksymalnie 1500 znaków");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}
	}
}
