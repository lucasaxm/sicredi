package com.sicredi.assembleia.errorhandling.validators;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.repositories.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateAssociadoValidator")
public class AssociadoValidator implements Validator {

    @Autowired
    AssociadoRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Associado.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Associado associado = (Associado) obj;
        if (checkInputString(associado.getNome())) {
            errors.rejectValue("nome", "required", ValidationErrors.REQUIRED);
        }
        if (checkInputString(associado.getCpf())) {
            errors.rejectValue("cpf", "required", ValidationErrors.REQUIRED);
        } else if (!repository.findByCpf(associado.getCpf()).isEmpty()){
            errors.rejectValue("cpf", "unique", ValidationErrors.UNIQUE);
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}