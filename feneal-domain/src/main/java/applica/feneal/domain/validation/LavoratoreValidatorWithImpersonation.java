package applica.feneal.domain.validation;

import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.framework.AEntity;
import applica.framework.LoadRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LavoratoreValidatorWithImpersonation implements DomainClassValidator {

    @Autowired
    private LavoratoriRepository lavRep;



    @Override
    public String validate(AEntity elem) throws Exception {

        Lavoratore l = ((Lavoratore) elem);




        if (l == null)
            throw new Exception("Impossibile validare. Elemento da validare nullo o non dello stesso tipo del validatore");

        StringBuilder b = new StringBuilder();


        if (StringUtils.isEmpty(l.getName()))
            b.append("Nome lavoratore nullo");
        if (StringUtils.isEmpty((l.getSurname())))
            b.append("Cognome lavoratore nullo");
        if (StringUtils.isEmpty((l.getFiscalcode())))
            b.append("Codice fiscale lavoratore nullo");

//        if (l.isPrivacy() == false)
//            b.append("Consenso al trattamento dei dati obbligatorio");

        //verifico la data di nascita
        if (l.getBirthDate() == null)
            b.append("Data di nascita nn specificata");

        //adesso verifico il codice fiscale
        //esso deve essere univoco per il territorio

        //adesso devo verificare che if cod fisc. sia unica per tutto il territorio
        //il territorio lo prendo dal lavoratore stesso . questo validatore è usato solo nelle importazioni massive e
        //in questo caso il lavoratore stessa vede il campo companyId già valorizzato correttamente
        //ricordati che solo gli utetne con company regionale usano la SecuredEntity

        Lavoratore l1 = lavRep.find(LoadRequest.build().filter("companyId", l.getCompanyId()).filter("fiscalcode", l.getFiscalcode())).findFirst().orElse(null);
        //se sto creando una nuov anagrafica allora se l1 esiste cè un errore
        if (l.getLid() == 0){
            if (l1 != null)
                b.append("Codice fiscale gia esistente");
        }else{
            if (l1 !=null)
                if (l1.getLid() != l.getLid()){
                    b.append("Codice fiscale gia esistente");
                }
        }


        String error = b.toString();


        //se l'errore è nbullo imposto le proietà di supporto alla ricerca
        if (StringUtils.isEmpty(error)){
            l.setNamesurname(String.format("%s %s", l.getName(), l.getSurname()));
            l.setSurnamename(String.format("%s %s", l.getSurname(),l.getName()));
        }



        return error;
    }


}
