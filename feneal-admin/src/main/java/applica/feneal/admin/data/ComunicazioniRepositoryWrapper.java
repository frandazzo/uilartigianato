package applica.feneal.admin.data;

import applica.feneal.domain.data.core.servizi.ComunicazioniRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.servizi.Comunicazione;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.Repository;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by fgran on 04/05/2017.
 */
@org.springframework.stereotype.Repository
public class ComunicazioniRepositoryWrapper implements Repository<Comunicazione> {

    @Autowired
    private ComunicazioniRepository docRep;

    @Autowired
    private Security secure;


    @Override
    public Optional<Comunicazione> get(Object o) {
        return docRep.get(o);
    }

    @Override
    public LoadResponse<Comunicazione> find(LoadRequest loadRequest) {
        return docRep.find(loadRequest);
    }

    @Override
    public void save(Comunicazione documento) {
        //quando salvo devo inserire i valori della categoria
        //se la categoria non è quella dell'utente loggato la validazione
        //andrà in errore....
        documento.setUserId(((User) secure.getLoggedUser()).getLid());
        documento.setUserCompleteName(((User) secure.getLoggedUser()).getCompleteName());

        docRep.save(documento);
    }

    @Override
    public void delete(Object o) {

        //per eliminare devo essere delle stessa categoria
        Comunicazione d = docRep.get(o).orElse(null);
        if (d == null)
            return;
        User u = (User) secure.getLoggedUser();


        if (u.getLid() == d.getUserId() ){
            docRep.delete(o);
            return;
        }

//        if (u.getLid() != d.getUserId() ) {
//            if (u.retrieveUserRole().getLid() == 3) {
//                docRep.delete(o);
//                return;
//            }
//        }
    }

    @Override
    public Class<Comunicazione> getEntityType() {
        return Comunicazione.class;
    }
}
