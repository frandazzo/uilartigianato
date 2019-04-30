package applica.feneal.domain.validation;

import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.framework.AEntity;
import applica.framework.LoadRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AziendaValidatorWithImpersonation implements DomainClassValidator {

    @Autowired
    private AziendeRepository azRep;

    @Override
    public String validate(AEntity elem) throws Exception {
        Azienda l = ((Azienda) elem);
        if (l == null)
            throw new Exception("Impossibile validare. Elemento da validare nullo o non dello stesso tipo del validatore");

        StringBuilder b = new StringBuilder();


        if (StringUtils.isEmpty(l.getDescription())){
            b.append("Ragione sociale nulla");
            return b.toString();
        }

        //adesso devo verificare che la ragione sociale dell'azienda sia unica per tutto il territorio
        //il territorio lo prendo dalla aziedna stessa . questo validatore è usato solo nelle importazioni massive e
        //in questo caso l'aziedna stessa vede il campo companyId già valorizzato correttamente
        //ricordati che solo gli utetne con company regionale usano la SecuredEntity
        Azienda a = azRep.find(LoadRequest.build().filter("companyId", l.getCompanyId()).filter("description", l.getDescription())).findFirst().orElse(null);
        if (l.getLid() == 0){
            if (a != null)
                b.append("Ragione sociale già esistente");

        }else{
            if (a != null){
                if (l.getLid() != a.getLid())
                    b.append("Ragione sociale già esistente");
            }

        }


        String error = b.toString();


        return error;
    }
}
