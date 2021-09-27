package com.sicredi.assembleia.errorhandling.validators;

import com.sicredi.assembleia.entities.Associado;
import com.sicredi.assembleia.errorhandling.ErrorMessages;
import com.sicredi.assembleia.helpers.Utils;
import com.sicredi.assembleia.repositories.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AssociadoValidator implements Validator {

    @Autowired
    private AssociadoRepository repository;

    private final Utils utils = Utils.getInstance();

    @Override
    public boolean supports(Class<?> clazz) {
        return Associado.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Associado associado = (Associado) obj;
        if (utils.isNullOrEmpty(associado.getNome())) {
            errors.rejectValue("nome", "required", ErrorMessages.REQUIRED);
        }
        if (utils.isNullOrEmpty(associado.getCpf())) {
            errors.rejectValue("cpf", "required", ErrorMessages.REQUIRED);
        } else {
            Associado found = repository.findByCpf(associado.getCpf());
            if (found != null && !found.getId().equals(associado.getId())){
                errors.rejectValue("cpf", "unique", String.format(ErrorMessages.UNIQUE, "cpf"));
            }
        }
    }
}