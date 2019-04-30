package applica.feneal.admin.data;

import applica.feneal.domain.data.core.servizi.DocumentiAziendaRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.servizi.DocumentoAzienda;
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
public class DocumentiAziendaRepositoryWrapper implements Repository<DocumentoAzienda> {

    @Autowired
    private DocumentiAziendaRepository docRep;

    @Autowired
    private Security secure;


    @Override
    public Optional<DocumentoAzienda> get(Object o) {
        return docRep.get(o);
    }

    @Override
    public LoadResponse<DocumentoAzienda> find(LoadRequest loadRequest) {
        return docRep.find(loadRequest);
    }

    @Override
    public void save(DocumentoAzienda documento) {
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
        DocumentoAzienda d = docRep.get(o).orElse(null);

        User u = (User) secure.getLoggedUser();
        if (d == null)
            return;

        if (u.getLid() == d.getUserId() ){
            docRep.delete(o);
            return;
        }

//        if (u.getLid() != d.getUserId() ){
//            if (u.retrieveUserRole().getLid() == 3){
//                docRep.delete(o);
//                return;
//            }
//
//
//
//        }
    }

    @Override
    public Class<DocumentoAzienda> getEntityType() {
        return DocumentoAzienda.class;
    }
}
