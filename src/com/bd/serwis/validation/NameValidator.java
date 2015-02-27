package com.bd.serwis.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.bd.serwis.validation.NameValidator")
public class NameValidator implements Validator {

    public NameValidator() {
        
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

        
        if (value.toString().length() > 60) {
            FacesMessage msg
                    = new FacesMessage("Imiê oraz nazwisko nie mo¿e przekraczaæ 60 znaków");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}