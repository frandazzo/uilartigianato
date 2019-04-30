package applica.feneal.admin.facade;


import applica.feneal.admin.viewmodel.aziende.UiAnagraficaAzienda;
import applica.feneal.admin.viewmodel.aziende.UiAziendaAnagraficaSummary;
import applica.feneal.admin.viewmodel.aziende.UiCompleteAziendaSummary;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.services.AziendaService;
import applica.feneal.services.GeoService;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fgran on 07/04/2016.
 */
@Component
public class AziendeFacade {

    @Autowired
    private Security security;

    @Autowired
    private AziendeRepository azRep;

    @Autowired
    private AziendaService svc;

    @Autowired
    private GeoService geosvc;


    @Autowired
    private CompanyRepository compRep;






    public UiCompleteAziendaSummary getFirmById(long aziendaId) {

        Azienda a =  azRep.get(aziendaId).orElse(null);

        UiCompleteAziendaSummary s = new UiCompleteAziendaSummary();

        UiAziendaAnagraficaSummary anag = convertAziendaToUiAzienda(a);

        s.setData(anag);

        return s;

    }

    private UiAziendaAnagraficaSummary convertAziendaToUiAzienda(Azienda a) {
        UiAziendaAnagraficaSummary s = new UiAziendaAnagraficaSummary();

        s.setId(a.getLid());
        s.setDescription(a.getDescription());
        s.setAlternativeDescription(a.getAlternativeDescription());
        s.setCity(a.getCity());
        s.setProvince(a.getProvince());
        s.setCap(a.getCap());
        s.setAddress(a.getAddress());
        s.setNotes(a.getNotes());
        s.setPhone(a.getPhone());
        s.setPiva(a.getPiva());

        return s;
    }

    public long saveAnagrafica(UiAnagraficaAzienda anag) throws Exception {
        Azienda l = convertUiAnagraficaToAzienda(anag);

        svc.saveOrUpdate(((User) security.getLoggedUser()).getLid(),l);

        return l.getLid();
    }

    private Azienda convertUiAnagraficaToAzienda(UiAnagraficaAzienda anag) {
        Azienda az = new Azienda();

        az.setId(anag.getId());
        az.setDescription(anag.getDescription());
        az.setAlternativeDescription(anag.getAlternativeDescription());
        az.setCap(anag.getCap());
        az.setAddress(anag.getAddress());
        az.setNotes(anag.getNotes());
        az.setPhone(anag.getPhone());
        az.setPiva(anag.getPiva());

        //per la provincia se il valore inviato dal client è un numero >0 allora rappresenta l'id della provincia
        try {
            Integer idProvince = Integer.parseInt(anag.getProvince());
            if (idProvince != null)
                if (idProvince > 0){
                    Province p = geosvc.getProvinceById(idProvince);
                    if (p!= null)
                        az.setProvince(p.getDescription());
                }

        } catch(NumberFormatException e){

            //se arrivo qui puo' essere che sto inviando la descrixione
            try{
                Province p = geosvc.getProvinceByName(anag.getProvince());
                if (p!= null)
                    az.setProvince(p.getDescription());

            }catch(Exception e1){
                az.setProvince(null);
            }





        }

        //per la città se il valore inviato dal client è un numero >0 allora rappresenta l'id della città
        try {
            Integer idCity = Integer.parseInt(anag.getCity());
            if (idCity != null)
                if (idCity > 0){
                    City c = geosvc.getCityById(idCity);
                    if (c!= null)
                        az.setCity(c.getDescription());
                }
        } catch(NumberFormatException e){


            //se arrivo qui puo' essere che sto inviando la descrixione
            try{
                City c = geosvc.getCityByName(anag.getCity());
                if (c!= null)
                    az.setCity(c.getDescription());

            }catch(Exception e1){
                az.setCity(null);
            }

        }

        return az;
    }

    public void deleteAzienda(long id) {
        svc.delete(((User) security.getLoggedUser()).getLid(), id);
    }


    public List<UiAnagraficaAzienda> searchAziende(String description) {

        List<UiAnagraficaAzienda> result = new ArrayList<>();
        //prendo tutti i territori
        List<Company> companies = compRep.find(null).getRows();
        for (Company company : companies) {
            List<Azienda> a = searchAziende(description,String.valueOf(company.getLid()));

            result.addAll(convertToUi(a, company));
        }

        return result;

    }

    private Collection<? extends UiAnagraficaAzienda> convertToUi(List<Azienda> a, Company companyany) {
        List<UiAnagraficaAzienda> anag = new ArrayList<>();

        for (Azienda azienda : a) {
            UiAnagraficaAzienda s = new UiAnagraficaAzienda();
            s.setId(azienda.getLid());
            s.setDescription(azienda.getDescription());
            s.setAlternativeDescription(companyany.getDescription());
            anag.add(s);
        }

        return anag;
    }

    public List<Azienda> searchAziende(String description, String company) {
        LoadRequest req = createLoadRequest(description, company);
        return  azRep.find(req).getRows();
    }

    private LoadRequest createLoadRequest(@RequestParam(value = "description", required = false) String description, @RequestParam(value = "company", required = false) String company) {
        LoadRequest req = LoadRequest.build();

        if (!StringUtils.isEmpty(description)){
            Filter f = new Filter("description", description);
            f.setType(Filter.LIKE);
            req.getFilters().add(f);
        }

        if (!StringUtils.isEmpty(company)){
            Filter f = new Filter("companyId", Long.parseLong(company));
            f.setType(Filter.EQ);
            req.getFilters().add(f);

        }

        req.setPage(1);
        req.setRowsPerPage(100);
        return req;
    }
}
