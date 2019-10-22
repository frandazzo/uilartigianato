package applica.feneal.domain.validation;


import applica.feneal.domain.model.core.CommunicationStructure;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CommunicationStructureValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CommunicationStructure.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CommunicationStructure c = ((CommunicationStructure) o);

        if (!StringUtils.isEmpty(c.getAttached1()) && StringUtils.isEmpty(c.getNomeattached1())) {
            errors.rejectValue("nomeattached1", null, "Per caricare l'allegato inserire il nome dell'allegato");
            return;
        }

        if (!StringUtils.isEmpty(c.getAttached2()) && StringUtils.isEmpty(c.getNomeattached2())) {
            errors.rejectValue("nomeattached2", null, "Per caricare l'allegato inserire il nome dell'allegato");
            return;
        }

        if (!StringUtils.isEmpty(c.getAttached3()) && StringUtils.isEmpty(c.getNomeattached3())) {
            errors.rejectValue("nomeattached3", null, "Per caricare l'allegato inserire il nome dell'allegato");
            return;
        }


        if (StringUtils.isEmpty(c.getAttached1()) && !StringUtils.isEmpty(c.getNomeattached1()) ){
            errors.rejectValue("attached1", null, "Non è possibile inserire un allegato vuoto");
            return;
        }

        if (StringUtils.isEmpty(c.getAttached2()) && !StringUtils.isEmpty(c.getNomeattached2()) ){
            errors.rejectValue("attached2", null, "Non è possibile inserire un allegato vuoto");
            return;
        }

        if (StringUtils.isEmpty(c.getAttached3()) && !StringUtils.isEmpty(c.getNomeattached3()) ){
            errors.rejectValue("attached3", null, "Non è possibile inserire un allegato vuoto");
            return;
        }
    }
}
