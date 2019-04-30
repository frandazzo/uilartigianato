package applica.feneal.services.impl.importData;

import applica.feneal.domain.data.core.*;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.configuration.Contract;
import applica.feneal.domain.model.setting.*;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.LoadRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 09/05/2017.
 */
@Component
public class ImportCausaliService implements AppSetup {

    @Autowired
    private CategoriaRepository sectorRep;

    @Autowired
    private DocumentTypeRepository tipoDocRep;




    @Autowired
    private FundRepository fondoRep;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private SignupDelegationReasonRepository causaleIscrizioneDelegaRep;

    @Autowired
    private RevocationReasonRepository causaleRevocaRep;

    @Autowired
    private Attribuzione1Repository attribuzione1Repository;
    @Autowired
    private Attribuzione2Repository attribuzione2Repository;


    public Contract getContract(String description){
        if (!StringUtils.isEmpty(description)){
            return contractRepository.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    public Contract getContractById(Long id){

            return contractRepository.get(id).orElse(null);

    }

    public Attribuzione1 getAttribuzione1(String description){
        if (!StringUtils.isEmpty(description)){
            return attribuzione1Repository.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }


    public Attribuzione2 getAttribuzione2(String description){
        if (!StringUtils.isEmpty(description)){
            return attribuzione2Repository.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }




    public Fondo getFondo(String description){
        if (!StringUtils.isEmpty(description)){
            return fondoRep.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    public CausaleIscrizioneDelega getCausaleIscrizione(String description){
        if (!StringUtils.isEmpty(description)){
            return causaleIscrizioneDelegaRep.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    public Collaboratore getCollaboratore(String description){
        if (!StringUtils.isEmpty(description)){
            return collaboratorRepository.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    public Contract getContratto(String description){
        if (!StringUtils.isEmpty(description)){
            return contractRepository.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    public TipoDocumento getTipoDocumento(String description){
        if (!StringUtils.isEmpty(description)){
            return tipoDocRep.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }


    public void createIfNotExistCausaleRevocaDelega(String s) {
        CausaleRevoca d = causaleRevocaRep.find(LoadRequest.build().filter("description", s)).findFirst().orElse(null);
        if (d == null){
            d = new CausaleRevoca();
            d.setDescription(s);

            causaleRevocaRep.save(d);
        }
    }


    public void createIfNotExistContratto(String s) {
        Contract d = contractRepository.find(LoadRequest.build().filter("description", s)).findFirst().orElse(null);
        if (d == null){
            d = new Contract();
            d.setDescription(s);

            contractRepository.save(d);
        }
    }

    public void createIfNotExistCausaleIscrizioneDelega(String iscrizione) {
        CausaleIscrizioneDelega d = causaleIscrizioneDelegaRep.find(LoadRequest.build().filter("description", iscrizione)).findFirst().orElse(null);
        if (d == null){
            d = new CausaleIscrizioneDelega();
            d.setDescription(iscrizione);

            causaleIscrizioneDelegaRep.save(d);
        }

    }

    public void createIfNotExistFondo(String fondo) {
        Fondo d = fondoRep.find(LoadRequest.build().filter("description", fondo)).findFirst().orElse(null);
        if (d == null){
            d = new Fondo();
            d.setDescription(fondo);

            fondoRep.save(d);
        }

    }

    public void createIfNotExistTipoDocumento(String description) {
        TipoDocumento d = tipoDocRep.find(LoadRequest.build().filter("description", description)).findFirst().orElse(null);
        if (d == null){
            d = new TipoDocumento();
            d.setDescription(description);

            tipoDocRep.save(d);
        }

    }


    public void createIfNotExistAttribuzione1(String fondo) {
        Attribuzione1 d = attribuzione1Repository.find(LoadRequest.build().filter("description", fondo)).findFirst().orElse(null);
        if (d == null){
            d = new Attribuzione1();
            d.setDescription(fondo);

            attribuzione1Repository.save(d);
        }

    }

    public void createIfNotExistAttribuzione2(String fondo) {
        Attribuzione2 d = attribuzione2Repository.find(LoadRequest.build().filter("description", fondo)).findFirst().orElse(null);
        if (d == null){
            d = new Attribuzione2();
            d.setDescription(fondo);

            attribuzione2Repository.save(d);
        }

    }



    public void createIfNotExistCollaboratore(String collaboratore) {
        Collaboratore d = collaboratorRepository.find(LoadRequest.build().filter("description", collaboratore)).findFirst().orElse(null);
        if (d == null){
            d = new Collaboratore();
            d.setDescription(collaboratore);

            collaboratorRepository.save(d);
        }

    }

    public CausaleRevoca getCausaleRevoca(String description) {
        if (!StringUtils.isEmpty(description)){
            return causaleRevocaRep.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    public Categoria getSettore(String description) {
        if (!StringUtils.isEmpty(description)){
            return sectorRep.find(LoadRequest.build().filter("description", description)).findFirst().get();
        }
        return null;
    }

    @Override
    public void setup() {
        createIfNotExistTipoDocumento("GENERICO");
        createIfNotExistTipoDocumento("SCANSIONE DELEGA");
        createIfNotExistCausaleIscrizioneDelega("NUOVA ISCRIZIONE");
        createIfNotExistCausaleRevocaDelega("ISCRIZIONE AD ALTRO SINDACATO");
    }
}
