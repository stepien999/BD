package com.bd.serwis.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.AnnouncementTitleValidator")
public class AnnouncementTitleValidator implements Validator {

	public AnnouncementTitleValidator() {
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (value.toString().length() > 150) {
			FacesMessage msg = new FacesMessage(
					"Tytu� og�oszenia mo�e mie� maksymalnie 150 znak�w");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}
	}
}
