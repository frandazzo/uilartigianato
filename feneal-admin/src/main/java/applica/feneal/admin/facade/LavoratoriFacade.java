package applica.feneal.admin.facade;


import applica.feneal.admin.viewmodel.lavoratori.*;
import applica.feneal.admin.viewmodel.reports.UiIscrizione;
import applica.feneal.domain.data.core.*;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.lavoratori.FiscalData;
import applica.feneal.domain.model.core.lavoratori.IscrittoAnnoInCorso;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.search.LavoratoreSearchParams;
import applica.feneal.domain.model.dbnazionale.Iscrizione;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Country;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.feneal.domain.model.setting.Fondo;
import applica.feneal.services.ComunicazioniService;
import applica.feneal.services.GeoService;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.ReportIscrittiService;
import applica.framework.security.Security;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fgran on 06/04/2016.
 */
@Component
public class LavoratoriFacade {

    @Autowired
    private LavoratoreService svc;





    @Autowired
    private GeoService geosvc;

    @Autowired
    private CategoriaRepository categoryRepository;

    @Autowired
    private ComunicazioniService comSvc;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private Attribuzione1Repository attr1Rep;

    @Autowired
    private Attribuzione2Repository attr2Rep;

    @Autowired
    private Attribuzione3Repository attr3Rep;

    @Autowired
    private Security security;

    @Autowired
    private CategoriaRepository catRep;

    @Autowired
    private CompanyRepository companyRep;


    @Autowired
    private ReportIscrittiService rptIscrServ;



    public UiCompleteLavoratoreSummary getLavoratoreSummaryById(long id) throws Exception {
        UiCompleteLavoratoreSummary s = new UiCompleteLavoratoreSummary();

        Lavoratore l = getLavoratoreById(id);
        if (l == null)
            return null;

        UiLavoratoreAnagraficaSummary a = convertLavoratoreToUILavoratoreAnagSummary(l);
        s.setData(a);

        // Presenza iscrizione anno in corso
        IscrittoAnnoInCorso iscritto = svc.checkIfIscrittoAnnoInCorso(id);
        UiIscrittoAnnoInCorso i = convertIscrittoDataToUIIscrittoAnnoInCorso(iscritto);
        s.setIscrittoData(i);


        return s;
    }

    public Lavoratore getLavoratoreById(long id) {
        return svc.getLavoratoreById(((User) security.getLoggedUser()).getLid(), id);
    }

    private UiLavoratoreAnagraficaSummary convertLavoratoreToUILavoratoreAnagSummary(Lavoratore l) {
        UiLavoratoreAnagraficaSummary s = new UiLavoratoreAnagraficaSummary();
        s.setId(l.getLid());
        s.setName(l.getName());
        s.setSurname(l.getSurname());
        s.setImage(l.getImage());
        s.setNationality(l.getNationality());
        s.setBirthProvince(l.getBirthProvince());
        s.setBirthPlace(l.getBirthPlace());
        s.setBirthDate(l.getBirthDate());
        s.setLivingPlace(l.getLivingCity());
        s.setLivingProvince(l.getLivingProvince());
        s.setAddress(l.getAddress());
        s.setAddressco(l.getAddressco());
        s.setCap(l.getCap());
        s.setFiscalcode(l.getFiscalcode());
        s.setPhone(l.getPhone());
        s.setCellphone(l.getCellphone());
        s.setMail(l.getMail());
        s.setPrivacy(l.isPrivacy());
        s.setCompanyId(l.getCompanyId());
//        s.setCe(l.getCe());
//        s.setEc(l.getEc());

        if (l.getAttribuzione1() == null)
            s.setAttribuzione1("");
        else
            s.setAttribuzione1(l.getAttribuzione1().getDescription());

        if (l.getAttribuzione2() == null)
            s.setAttribuzione2("");
        else
            s.setAttribuzione2(l.getAttribuzione2().getDescription());


        if (l.getFund() == null)
            s.setFund("");
        else
            s.setFund(l.getFund().getDescription());



        if (l.getAttribuzione3() == null)
            s.setAttribuzione3("");
        else
            s.setAttribuzione3(l.getAttribuzione3().getDescription());




        if (l.getFund() == null)
            s.setFund("");
        else
            s.setFund(l.getFund().getDescription());

        return s;
    }

    public long saveAnagrafica(UiAnagrafica anag) throws Exception {

        Lavoratore l = convertUiAnagraficaToLavoratore(anag);

        svc.saveOrUpdate(((User) security.getLoggedUser()).getLid(),l);

        return l.getLid();
    }

    private Lavoratore convertUiAnagraficaToLavoratore(UiAnagrafica anag) {
        Lavoratore l = new Lavoratore();

        l.setId(anag.getId());
        l.setName(anag.getName());
        l.setSurname(anag.getSurname());
        l.setSex(anag.getSex());
        l.setFiscalcode(anag.getFiscalcode());
        l.setImage(anag.getImage());
        l.setPrivacy(anag.isPrivacy());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            l.setBirthDate(df.parse(anag.getBirthDate()));
        } catch (ParseException e) {
            l.setBirthDate(null);
        }

        l.setAddress(anag.getAddress());
        l.setAddressco(anag.getAddressco());
        l.setCap(anag.getCap());
        l.setPhone(anag.getPhone());
        l.setCellphone(anag.getCellphone());
        l.setMail(anag.getMail());
//        l.setCe(anag.getCe());
//        l.setEc(anag.getEc());


        l.setAttribuzione1(retrieveAttribuzione1(anag.getAttribuzione1()));
        l.setAttribuzione2(retrieveAttribuzione2(anag.getAttribuzione2()));
        l.setAttribuzione3(retrieveAttribuzione3(anag.getAttribuzione4()));

        l.setFund(retrieveFund(anag.getFund()));
        l.setNotes(anag.getNotes());

        //per la nazionalità se il valore inviato dal client è un numero >0 allora rappresenta l'id della nazione
        try {
            Integer idNationality = Integer.parseInt(anag.getNationality());
            if (idNationality != null)
                if (idNationality > 0){
                    Country c = geosvc.getCountryById(idNationality);
                    if (c!= null)
                        l.setNationality(c.getDescription());
                }

        } catch(NumberFormatException e){

            try{
                Country p = geosvc.getCountryByName(anag.getNationality());
                if (p!= null)
                    l.setNationality(p.getDescription());

            }catch(Exception e1){
                l.setNationality(null);
            }

        }

        //per la provincia se il valore inviato dal client è un numero >0 allora rappresenta l'id della provincia
        try {
            Integer idProvince = Integer.parseInt(anag.getBirthProvince());
            if (idProvince != null)
                if (idProvince > 0){
                    Province p = geosvc.getProvinceById(idProvince);
                    if (p!= null)
                        l.setBirthProvince(p.getDescription());
                }

        } catch(NumberFormatException e){



            try{
                Province p = geosvc.getProvinceByName(anag.getBirthProvince());
                if (p!= null)
                    l.setBirthProvince(p.getDescription());

            }catch(Exception e1){
                l.setBirthProvince(null);
            }



        }

        //per la città se il valore inviato dal client è un numero >0 allora rappresenta l'id della città
        try {
            Integer idBirthPlace = Integer.parseInt(anag.getBirthPlace());
            if (idBirthPlace != null)
                if (idBirthPlace > 0){
                    City c = geosvc.getCityById(idBirthPlace);
                    if (c!= null)
                        l.setBirthPlace(c.getDescription());
                }
        } catch(NumberFormatException e){


            try{
                City c = geosvc.getCityByName(anag.getBirthPlace());
                if (c!= null)
                    l.setBirthPlace(c.getDescription());

            }catch(Exception e1){
                l.setBirthPlace(null);
            }





        }

        //per la provincia di residenza se il valore inviato dal client è un numero >0 allora rappresenta l'id della provincia
        try {
            Integer idLivingProvince = Integer.parseInt(anag.getLivingProvince());
            if (idLivingProvince != null)
                if (idLivingProvince > 0){
                    Province p = geosvc.getProvinceById(idLivingProvince);
                    if (p!= null)
                        l.setLivingProvince(p.getDescription());
                }

        } catch(NumberFormatException e){

            try{
                Province p = geosvc.getProvinceByName(anag.getLivingProvince());
                if (p!= null)
                    l.setLivingProvince(p.getDescription());

            }catch(Exception e1){
                l.setLivingProvince(null);
            }



        }

        //per la città di residenza se il valore inviato dal client è un numero >0 allora rappresenta l'id della città
        try {
            Integer idLivingCity = Integer.parseInt(anag.getLivingCity());
            if (idLivingCity != null)
                if (idLivingCity > 0){
                    City c = geosvc.getCityById(idLivingCity);
                    if (c!= null)
                        l.setLivingCity(c.getDescription());
                }
        } catch(NumberFormatException e){



            try{
                City c = geosvc.getCityByName(anag.getLivingCity());
                if (c!= null)
                    l.setLivingCity(c.getDescription());

            }catch(Exception e1){
                l.setLivingCity(null);
            }


        }


        return l;
    }


    private UiIscrittoAnnoInCorso convertIscrittoDataToUIIscrittoAnnoInCorso(IscrittoAnnoInCorso iscritto) {

        UiIscrittoAnnoInCorso uiIscrittoAnnoInCorso = new UiIscrittoAnnoInCorso();

        uiIscrittoAnnoInCorso.setAzienda(iscritto.getAzienda());
        uiIscrittoAnnoInCorso.setEnte(iscritto.getEnte());
        uiIscrittoAnnoInCorso.setIscritto(iscritto.iscritto());
        uiIscrittoAnnoInCorso.setPeriodo(iscritto.getPeriodo());
        uiIscrittoAnnoInCorso.setSettore(iscritto.getSettore());
        uiIscrittoAnnoInCorso.setCategoria(iscritto.getCategoria());
        uiIscrittoAnnoInCorso.setDelega(iscritto.isDelega());
        return uiIscrittoAnnoInCorso;
    }



    public void deleteLavoratore(long id) throws Exception {
        svc.delete(((User) security.getLoggedUser()).getLid(), id);
    }

    public List<Lavoratore> findLocalLavoratori(LavoratoreSearchParams s) {
        return svc.findLocalLavoratori(((User) security.getLoggedUser()).getLid(), s);
    }





    public boolean existSmsCredentials() {
        return comSvc.existSmsCredentials();
    }

    private Fondo retrieveFund(String fund) {
        if (StringUtils.isEmpty(fund))
            return null;

        return fundRepository.get(Long.parseLong(fund)).orElse(null);
    }


    private Attribuzione1 retrieveAttribuzione1(String fund) {
        if (StringUtils.isEmpty(fund))
            return null;

        return attr1Rep.get(Long.parseLong(fund)).orElse(null);
    }

    private Attribuzione2 retrieveAttribuzione2(String fund) {
        if (StringUtils.isEmpty(fund))
            return null;

        return attr2Rep.get(Long.parseLong(fund)).orElse(null);
    }

    private Attribuzione3 retrieveAttribuzione3(String fund) {
        if (StringUtils.isEmpty(fund))
            return null;

        return attr3Rep.get(Long.parseLong(fund)).orElse(null);
    }


    public long getIdLavoratoreByFiscalCode(String fiscalCode, long companyId) throws Exception {

        Lavoratore ll = svc.findLavoratoreByFiscalCode(fiscalCode, companyId);
        if (ll == null)
            return -1;

        return ll.getLid();
    }



    public UiWorkerIscrizioniChart getIscrizioniChart(long id) throws Exception {
        //ottengo il lavoratore
        Lavoratore l = svc.getLavoratoreById(((User) security.getLoggedUser()).getLid(), id);
        if (l == null)
            throw new Exception("Lavoratore non trovato");

        //adesso recupero tutte le iscrizioni per il lavoartore
        List<Iscrizione> list = rptIscrServ.findIscrizioniByWorkerId(id);



        //adesso devo convertire tutti i dati delle iscrizioni un una classe uiworkeriscrizionichart
        return converToUiIscrizioniChart(l, list);

    }

    public FiscalData getFiscalData(String fiscalcode) throws RemoteException {
        return geosvc.getFiscalData(fiscalcode);
    }








    private UiWorkerIscrizioniChart converToUiIscrizioniChart(Lavoratore l, List<Iscrizione> list) {
        UiWorkerIscrizioniChart result = new UiWorkerIscrizioniChart();
        result.setWorkerName(l.getNamesurname());

        if (list.size() == 0)
            return result;


        //recupero la lista degli anni e delle province legate alle iscrizioni
        result.setAnni(retrieveAnniFromIscrizioni(list));
        result.setProvinces(retrieveProvincesesFromIscrizioni(list));
        //recupero la lista degli id delle province delle iscrizioni
        result.setProvincesIds(retrieveCategoryIds(result.getProvinces()));

        result.setLoggedUserProvinceIds(retrieveProvicnesForLavoratoreCompany(l));

        result.setChartElements(convertIscrizioniToUiChartElements(list, result.getAnni(), result.getProvinces(), result.getProvincesIds()));
        return result;
    }

    private List<UiWorkerChartElement> convertIscrizioniToUiChartElements(List<Iscrizione> iscrizioni, Integer[] anni, String[] province, Integer[] provinceIds) {
        List<UiWorkerChartElement> result = new ArrayList<>();

        for (int i = 0; i < anni.length; i++) {
            Integer anno = anni[i];

            for (int j = 0; j < province.length; j++) {


                UiWorkerChartElement elem = new UiWorkerChartElement();
                elem.setX(i);
                elem.setY(j);
                elem.setValue(provinceIds[j]);

                //recupero la lista dei settori
                elem.setSettori(retrieveSettori(anno, province[j], iscrizioni));

                result.add(elem);
            }

        }

        return result;
    }

    private List<String> retrieveSettori(Integer anno, String provinciaName, List<Iscrizione> list) {
        HashMap<String, Object> l = new HashMap<>();

        for (Iscrizione iscrizione : list) {
            if (iscrizione.getNomeProvincia().equals(provinciaName) && iscrizione.getAnno() == anno )
                if (!l.containsKey(iscrizione.getSettore()))
                    l.put(iscrizione.getSettore(), "");
        }

        String[] a = l.keySet().toArray(new String[l.keySet().size()]);
        return new ArrayList<String>(Arrays.asList(a));
    }

    private Integer[] retrieveCategoryIds( String[] provicneNames) {
//        HashMap<Integer, Object> l = new HashMap<>();
//
//        for (Iscrizione iscrizione : list) {
//            if (!l.containsKey(iscrizione.getId_Provincia()))
//                l.put(iscrizione.getId_Provincia(), "");
//        }
//
//        Integer[] a = l.keySet().toArray(new Integer[l.keySet().size()]);
//        return a;

        if (provicneNames == null)
            return new Integer[]{};
        if (provicneNames.length == 0)
            return new Integer[]{};

        Integer[] a = new Integer[provicneNames.length];
        int i = 0;
        for (String cat : provicneNames) {

            a[i] = geosvc.getProvinceByName(cat).getIid();
            i++;
        }
        return a;

    }

    private Integer[] retrieveProvicnesForLavoratoreCompany(Lavoratore l) {
        List<Integer> a =  new ArrayList<Integer>();
        Company cc = companyRep.get(l.getCompanyId()).orElse(null);

        for (Province ca : cc.getProvinces()) {
            a.add(ca.getIid());
        }
        return a.toArray(new Integer[a.size()]);
    }


    private String[] retrieveProvincesesFromIscrizioni(List<Iscrizione> list) {
        HashMap<String, Object> l = new HashMap<>();

        for (Iscrizione iscrizione : list) {
            if (!l.containsKey(iscrizione.getNomeProvincia()))
                l.put(iscrizione.getNomeProvincia(), "");
        }

        String[] a = l.keySet().toArray(new String[l.keySet().size()]);
        return a;

    }

    private Integer[] retrieveAnniFromIscrizioni(List<Iscrizione> list) {

        HashMap<Integer, Object> l = new HashMap<>();

        for (Iscrizione iscrizione : list) {
            if (!l.containsKey(iscrizione.getAnno()))
                l.put(iscrizione.getAnno(), "");
        }

        Integer[] a = l.keySet().toArray(new Integer[l.keySet().size()]);
        return a;




    }

    public List<UiIscrizione> getIscrizioniDetails(long id) {
        Lavoratore l = svc.getLavoratoreById(((User) security.getLoggedUser()).getLid(), id);
        if (l == null)
            return new ArrayList<>();

        List<Iscrizione> iscriziones = rptIscrServ.findIscrizioniByWorkerId(id);

        return convertIscrizioniToUiiscrizioni(iscriziones);

    }


    private List<UiIscrizione> convertIscrizioniToUiiscrizioni(List<Iscrizione> iscrizioni) {
        List<UiIscrizione> result = new ArrayList<>();

//        UiIscrizione ee = new UiIscrizione();
//        ee.setSettore("Edile");
//        ee.setNomeProvincia("Potenza");
//        ee.setAnno(2015);
//        ee.setNomeRegione("Basilicata");

        for (Iscrizione iscrizione : iscrizioni) {
            result.add(convertIscrizioneToUiIscrizione(iscrizione));
        }


        return result;
    }

    private UiIscrizione convertIscrizioneToUiIscrizione(Iscrizione iscrizione) {
        UiIscrizione i = new UiIscrizione();

        i.setOperatoreDelega(iscrizione.getOperatoreDelega());
        i.setAnno(iscrizione.getAnno());
        i.setAzienda(iscrizione.getAzienda());
        i.setContratto(iscrizione.getContratto());
        i.setLivello(iscrizione.getLivello());
        i.setNomeProvincia(iscrizione.getNomeProvincia());
        i.setNomeRegione(iscrizione.getNomeRegione());
        i.setPiva(iscrizione.getPiva());
        i.setQuota(iscrizione.getQuota());
        i.setSettore(iscrizione.getSettore());
        i.setCompanyId(iscrizione.getCompanyId());

        return i;
    }












}
