package applica.feneal.domain.validation;

import applica.feneal.domain.data.core.servizi.DocumentiAziendaRepository;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.servizi.DocumentoAzienda;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by fgran on 04/05/2017.
 */
@Component
public class DocumentAziendaValidator implements Validator {

    @Autowired
    private DocumentiAziendaRepository docRep;

    @Autowired
    private Security sec;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(DocumentoAzienda.class);
    }

    @Override
    public void validate(Object o, Errors errors) {


        User u = ((User) sec.getLoggedUser());
        Role r = ((Role) u.getRoles().get(0));

        if (r.getLid() != 3 && r.getLid() != 4)
            errors.rejectValue("userCompleteName", null, "Il documento è stato creato da un altro utente");



        if (((DocumentoAzienda) o).getData() == null)
            errors.rejectValue("data", null, "La data è obbligatoria");

        if (((DocumentoAzienda) o).getTipo() == null)
            errors.rejectValue("tipo", null, "Il tipo è obbligatorio");

        DocumentoAzienda toVerifyDoc = (DocumentoAzienda) o;
        if (toVerifyDoc.getLid() > 0){
            DocumentoAzienda dbData = docRep.get(toVerifyDoc.getLid()).orElse(null);

            if (dbData.getUserId() != (u.getLid())){
              //  if (r.getLid() != 3) //regionale
                    errors.rejectValue("userCompleteName", null, "Il documento è stata creata da un'altro utente");

            }
        }


    }

}
