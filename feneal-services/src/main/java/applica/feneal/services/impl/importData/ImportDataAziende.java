package applica.feneal.services.impl.importData;

import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.importData.ImportDelegaSummary;
import applica.feneal.domain.model.geo.City;
import applica.feneal.services.AziendaService;
import applica.feneal.services.GeoService;
import applica.framework.management.csv.RowData;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 09/05/2017.
 */
@Component
public class ImportDataAziende {

    @Autowired
    private Security sec;

    @Autowired
    private AziendaService azSvc;

    @Autowired
    private GeoService geo;

    public  Azienda constructAzienda(ImportDelegaSummary summary, ImportData data) throws Exception {
        Azienda l = azSvc.getAziendaByDescription(summary.getAzienda(), Long.parseLong(data.getCompany()));
        if (l == null){


            //creo il lavoratore
            l = new Azienda();
            l.setCompanyId(Long.parseLong(data.getCompany()));

            l.setDescription(summary.getAzienda());
            l.setAlternativeDescription(summary.getAziendaDescrAlternativa());
            if (!StringUtils.isEmpty(summary.getAziendaComune())){

                City cc1 = geo.getCityByName(summary.getAziendaComune());
                if (cc1!= null){
                    l.setCity(cc1.getDescription());
                    l.setProvince(geo.getProvinceById(cc1.getIdProvince()).getDescription());
                }
            }

            l.setAddress(summary.getAziendaIndirizzo());
            l.setCap(summary.getAziendaCap());
            l.setPhone(summary.getAziendaTelefono());
            l.setNotes(summary.getAziendaNote());

            azSvc.saveOrUpdateWithImpersonation(((User) sec.getLoggedUser()).getLid(),l);

        }
        else{
            //devo verificare se aggiornare i dati dell'azienda
            if (data.getUpdateAzienda() != null)
                if (data.getUpdateAzienda() == 1){

                    l.setAlternativeDescription(summary.getAziendaDescrAlternativa());

                    if (!StringUtils.isEmpty(summary.getAziendaComune())){

                        City cc1 = geo.getCityByName(summary.getAziendaComune());
                        if (cc1!= null){
                            l.setCity(cc1.getDescription());
                            l.setProvince(geo.getProvinceById(cc1.getIdProvince()).getDescription());
                        }
                    }
                    l.setAddress(summary.getAziendaIndirizzo());
                    l.setCap(summary.getAziendaCap());
                    l.setPhone(summary.getAziendaTelefono());
                    l.setNotes(summary.getAziendaNote());
                }

            azSvc.saveOrUpdateWithImpersonation(((User) sec.getLoggedUser()).getLid(),l);


        }
        return l;
    }


    public Azienda constructAzienda(RowData rowData,  ImportData importData) throws Exception {

        if (StringUtils.isEmpty(String.valueOf(rowData.getData().get("AZIENDA"))))
            return null;

        Azienda l = azSvc.getAziendaByDescription(rowData.getData().get("AZIENDA"), Long.parseLong(importData.getCompany()));
        if (l == null){


            //creo il lavoratore
            l = new Azienda();
            l.setCompanyId(Long.parseLong(importData.getCompany()));

            l.setDescription(rowData.getData().get("AZIENDA"));
            l.setAlternativeDescription(rowData.getData().get("DESCR_ALTERNATIVA"));
            if (!StringUtils.isEmpty(String.valueOf(rowData.getData().get("COMUNE_AZIENDA")))){

                City cc1 = geo.getCityByName(rowData.getData().get("COMUNE_AZIENDA"));
                if (cc1!= null){
                    l.setCity(cc1.getDescription());
                    l.setProvince(geo.getProvinceById(cc1.getIdProvince()).getDescription());
                }
            }
            l.setPiva(rowData.getData().get("PARTITA_IVA"));
            l.setAddress(rowData.getData().get("INDIRIZZO_AZIENDA"));
            l.setCap(rowData.getData().get("CAP_AZIENDA"));
            l.setPhone(rowData.getData().get("TELEFONO_AZIENDA"));
            l.setNotes(rowData.getData().get("NOTE_AZIENDA"));

            azSvc.saveOrUpdateWithImpersonation(((User) sec.getLoggedUser()).getLid(),l);

        }
        else{
            //devo verificare se aggiornare i dati dell'azienda
            if (importData.getUpdateAzienda() != null)
                if (importData.getUpdateAzienda() == 1){

                    if (!StringUtils.isEmpty(rowData.getData().get("COMUNE_AZIENDA"))){

                        City cc1 = geo.getCityByName(rowData.getData().get("COMUNE_AZIENDA"));
                        if (cc1!= null){
                            l.setCity(cc1.getDescription());
                            l.setProvince(geo.getProvinceById(cc1.getIdProvince()).getDescription());
                        }
                    }
                    l.setAlternativeDescription(rowData.getData().get("DESCR_ALTERNATIVA"));
                    l.setPiva(rowData.getData().get("PARTITA_IVA"));
                    l.setAddress(rowData.getData().get("INDIRIZZO_AZIENDA"));
                    l.setCap(rowData.getData().get("CAP_AZIENDA"));
                    l.setPhone(rowData.getData().get("TELEFONO_AZIENDA"));
                    l.setNotes(rowData.getData().get("NOTE_AZIENDA"));
                }

            azSvc.saveOrUpdateWithImpersonation(((User) sec.getLoggedUser()).getLid(),l);


        }
        return l;
    }
}
