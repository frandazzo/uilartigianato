package applica.feneal.domain.validation;

import applica.feneal.domain.model.setting.EmailLayout;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by angelo on 15/06/2017.
 */
@Component
public class EmailLayoutValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(EmailLayout.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        if (StringUtils.isEmpty(((EmailLayout) o).getName()))
            errors.rejectValue("name", null, "Il nome è obbligatorio");

        if (StringUtils.isEmpty(((EmailLayout) o).getType()))
            errors.rejectValue("type", null, "Il tipo è obbligatorio");
        else {
            if (StringUtils.isEmpty(((EmailLayout) o).getImagePath1()))
                errors.rejectValue("imagePath1", null, "L'immagine è obbligatoria");
            if (EmailLayout.TIPO_2.equals(((EmailLayout) o).getType()) && StringUtils.isEmpty(((EmailLayout) o).getImagePath2()))
                errors.rejectValue("imagePath2", null, "L'immagine è obbligatoria");
        }
    }

}
