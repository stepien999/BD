package com.bd.serwis.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.PasswordValidator")
public class PasswordValidator implements Validator {

    public PasswordValidator() {
        
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

        
        if (value.toString().length() < 6 || value.toString().length() > 150) {
            FacesMessage msg
                    = new FacesMessage("Has³o musi mieæ wiêcej ni¿ 6 znaków i maksymalnie 150 znaków");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}