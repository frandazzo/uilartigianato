package applica.feneal.domain.validation;

import applica.feneal.domain.data.core.servizi.ComunicazioniRepository;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.servizi.Comunicazione;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by angelo on 28/04/2016.
 */
@Component
public class CommunicationValidator implements Validator {

    @Autowired
    private ComunicazioniRepository docRep;

    @Autowired
    private Security sec;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Comunicazione.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        User u = ((User) sec.getLoggedUser());
        Role r = ((Role) u.getRoles().get(0));

        if (r.getLid() != 3 && r.getLid() != 4)
            errors.rejectValue("userCompleteName", null, "Il documento è stato creato da un altro utente");



        if (((Comunicazione) o).getData() == null)
            errors.rejectValue("data", null, "La data è obbligatoria");

        if (((Comunicazione) o).getTipo() == null)
            errors.rejectValue("tipo", null, "Il tipo è obbligatorio");

        if (((Comunicazione) o).getCausale() == null)
            errors.rejectValue("causale", null, "La causale è obbligatoria");

        if (StringUtils.isEmpty(((Comunicazione) o).getOggetto()))
            errors.rejectValue("oggetto", null, "L'oggetto è obbligatorio");




        Comunicazione toVerifyDoc = (Comunicazione) o;
        if (toVerifyDoc.getLid() > 0){
            Comunicazione dbData = docRep.get(toVerifyDoc.getLid()).orElse(null);

            if (dbData.getUserId() != (u.getLid())){
                //if (r.getLid() != 3) //regionale
                    errors.rejectValue("userCompleteName", null, "La comunicazione è stata creata da un'altro utente");

            }


        }


    }

}
