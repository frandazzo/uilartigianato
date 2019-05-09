package applica.feneal.admin.facade;

import applica.feneal.admin.utils.FenealDateUtils;
import applica.feneal.admin.viewmodel.deleghe.*;
import applica.feneal.admin.viewmodel.reports.UiDelega;
import applica.feneal.domain.data.core.*;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.geo.ProvinceRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.deleghe.UiDelegheReportSearchParams;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;
import applica.feneal.domain.model.setting.CausaleRevoca;
import applica.feneal.services.DelegheService;
import applica.feneal.services.ListaLavoroService;
import applica.feneal.services.ReportDelegheService;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 14/04/2016.
 */
@Component
public class DelegheFacade {

    @Autowired
    private DelegheService delegheService;

    @Autowired
    private RevocationReasonRepository revRep;

    @Autowired
    private CategoriaRepository sectorRepository;

    @Autowired
    private LavoratoriRepository lavoratoriRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ContractRepository paritheticRepository;

    @Autowired
    private AziendeRepository aziendeRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private SignupDelegationReasonRepository subscribeReason;

    @Autowired
    private ReportDelegheService delService;

    @Autowired
    private ListaLavoroService lSrv;

    @Autowired
    private Security security;

    @Autowired
    private LavoratoriRepository lavRep;

    @Autowired
    private ApplicationOptionRepository appRep;

    @Autowired
    private CompanyRepository comRep;

    public List<UIDelega> getAllWorkerDeleghe(long workerId) {
        List<Delega> delegas = delegheService.getAllWorkerDeleghe(workerId);
        List<UIDelega> uiDelegaFormEntities = new ArrayList<>();
        for (Delega delega: delegas) {
            uiDelegaFormEntities.add(getUIDelegaFromModelEntity(delega));
        }
        return uiDelegaFormEntities;
    }

    private UIDelega getUIDelegaFromModelEntity(Delega delega) {
        UIDelega uiDelega = new UIDelega();
        uiDelega.setState(delega.getState());
        uiDelega.setId(String.valueOf(delega.getId()));

        uiDelega.setMansione(delega.getMansione());
        uiDelega.setLuogoLavoro(delega.getLuogoLavoro());

        uiDelega.setSupportedNextStates(delegheService.getDelegaPermittedNextStates(delega, appRep.find(null).getRows()));
        uiDelega.setDocumentDate(FenealDateUtils.getStringFromDate(delega.getDocumentDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER));
        uiDelega.setSendDate(FenealDateUtils.getStringFromDate(delega.getSendDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER));
        uiDelega.setActivationDate(FenealDateUtils.getStringFromDate(delega.getActivationDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER));
        uiDelega.setRevokeDate(FenealDateUtils.getStringFromDate(delega.getRevokeDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER));
        uiDelega.setCancelDate(FenealDateUtils.getStringFromDate(delega.getCancelDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER));
        uiDelega.setAcceptDate(FenealDateUtils.getStringFromDate(delega.getAcceptDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER));
        uiDelega.setSector(delega.getSector() != null ? delega.getSector().toString() : null);
        uiDelega.setContract(delega.getContract() != null? delega.getContract().toString() : null);
        if (delega.getWorker() != null) {
            uiDelega.setWorker(delega.getWorker() != null? delega.getWorker().toString() : null);
            uiDelega.setWorkerId(String.valueOf(delega.getWorker().getLid()));
        }

        uiDelega.setWorkerCompany(delega.getWorkerCompany() != null? delega.getWorkerCompany().toString() : null);
        uiDelega.setProvince(delega.getProvince() != null? delega.getProvince().toString() : null);
        uiDelega.setSubscribeReason(delega.getSubscribeReason() != null? delega.getSubscribeReason().toString() : null);
        uiDelega.setRevokeReason(delega.getRevokeReason() != null? delega.getRevokeReason().toString() : null);
        uiDelega.setNotes(delega.getNotes());
        uiDelega.setCollaborator(delega.getCollaborator() != null? delega.getCollaborator().getDescription():"");
        uiDelega.setUserCompleteName(delega.getUserCompleteName());




        return uiDelega;
    }

    public List<UiDelega> reportDeleghe(UiDelegheReportSearchParams params) {
        List<Delega> del = delService.retrieveDeleghe(params);

        return convertDelegheToUiDeleghe(del);
    }

    public List<UiDelega> reportDelegheBilateralita(UiDelegheReportSearchParams params) {
        List<DelegaBilateralita> del = delService.retrieveDelegheBilateralita(params);

        return convertDelegheToUiDelegheBilateralita(del);
    }

    public List<UiDelega> reportDelegheUnc(UiDelegheReportSearchParams params) {
        List<DelegaUnc> del = delService.retrieveDelegheUnc(params);

        return convertDelegheToUiDelegheUnc(del);
    }

    public List<UiDelega> convertDelegheToUiDelegheUnc(List<DelegaUnc> del) {
        List<UiDelega> result = new ArrayList<>();

        List<Company> companies = comRep.find(null).getRows();

        for (DelegaUnc delega : del) {
            UiDelega d = new UiDelega();
            d.setCompanyId(delega.getCompanyId());
            d.setRegione(companies.stream().filter(a ->a.getLid() == delega.getCompanyId()).findFirst().orElse(null).getDescription());
            d.setDelegaProvincia(delega.getProvince().getDescription());
            d.setDelegaDataDocumento(delega.getDocumentDate());
            d.setDelegaDataAnnullamento(delega.getCancelDate());
            if (delega.getSector() != null)
                d.setDelegaSettore(delega.getSector().getDescription());

            d.setDelegaId(delega.getLid());
            d.setDelegaNote(delega.getNotes());

            populateUiDelegaWorkerData1(delega, d);

            if (delega.getWorkerCompany() != null) {
                d.setAziendaRagioneSociale(delega.getWorkerCompany().getDescription());
                d.setAziendaCitta(delega.getWorkerCompany().getCity());
                d.setAziendaProvincia(delega.getWorkerCompany().getProvince());
                d.setAziendaCap(delega.getWorkerCompany().getCap());
                d.setAziendaIndirizzo(delega.getWorkerCompany().getAddress());
                d.setAziendaNote(delega.getWorkerCompany().getNotes());
                d.setAziendaId(delega.getWorkerCompany().getLid());
            }
            result.add(d);
        }

        return result;
    }

    public List<UiDelega> convertDelegheToUiDelegheBilateralita(List<DelegaBilateralita> del) {
        List<UiDelega> result = new ArrayList<>();

        List<Company> companies = comRep.find(null).getRows();

        for (DelegaBilateralita delega : del) {
            UiDelega d = new UiDelega();
            d.setCompanyId(delega.getCompanyId());
            d.setRegione(companies.stream().filter(a ->a.getLid() == delega.getCompanyId()).findFirst().orElse(null).getDescription());
            d.setDelegaProvincia(delega.getProvince().getDescription());
            d.setDelegaDataDocumento(delega.getDocumentDate());
            d.setDelegaDataAnnullamento(delega.getCancelDate());
            if (delega.getSector() != null)
                d.setDelegaSettore(delega.getSector().getDescription());

            d.setDelegaId(delega.getLid());
            d.setDelegaNote(delega.getNotes());

            populateUiDelegaWorkerData(delega, d);

//            if (delega.getWorkerCompany() != null) {
//                d.setAziendaRagioneSociale(delega.getWorkerCompany().getDescription());
//                d.setAziendaCitta(delega.getWorkerCompany().getCity());
//                d.setAziendaProvincia(delega.getWorkerCompany().getProvince());
//                d.setAziendaCap(delega.getWorkerCompany().getCap());
//                d.setAziendaIndirizzo(delega.getWorkerCompany().getAddress());
//                d.setAziendaNote(delega.getWorkerCompany().getNotes());
//                d.setAziendaId(delega.getWorkerCompany().getLid());
//            }
            result.add(d);
        }

        return result;
    }

    private void populateUiDelegaWorkerData(DelegaBilateralita delega, UiDelega d) {
        d.setLavoratoreId(delega.getWorker().getLid());
        d.setLavoratoreCap(delega.getWorker().getCap());
        d.setLavoratoreCellulare(delega.getWorker().getCellphone());
        d.setLavoratorMail(delega.getWorker().getMail());
        d.setLavoratoreTelefono(delega.getWorker().getPhone());
        d.setLavoratoreCittaResidenza(delega.getWorker().getLivingCity());
        d.setLavoratoreCodiceFiscale(delega.getWorker().getFiscalcode());
        d.setLavoratoreCognome(delega.getWorker().getSurname());

        d.setLavoratoreDataNascita(delega.getWorker().getBirthDate());
        d.setLavoratoreIndirizzo(delega.getWorker().getAddress());
        d.setLavoratoreLuogoNascita(delega.getWorker().getBirthPlace());
        d.setLavoratoreNazionalita(delega.getWorker().getNationality());
        d.setLavoratoreNome(delega.getWorker().getName());
        d.setLavoratoreCittaResidenza(delega.getWorker().getLivingCity());
        d.setLavoratoreProvinciaNascita(delega.getWorker().getBirthProvince());
        d.setLavoratoreProvinciaResidenza(delega.getWorker().getLivingProvince());
        if (delega.getWorker().getSex().equals("M"))
            d.setLavoratoreSesso(Lavoratore.MALE);
        else
            d.setLavoratoreSesso(Lavoratore.FEMALE);

//            d.setLavoratoreCodiceCassaEdile(delega.getWorker().getCe());
//            d.setLavoratoreCodiceEdilcassa(delega.getWorker().getEc());
        if (delega.getWorker().getFund() != null)
            d.setLavoratoreFondo(delega.getWorker().getFund().getDescription());


        if (delega.getWorker().getAttribuzione1() != null)
            d.setLavoratoreAttribuzione1(delega.getWorker().getAttribuzione1().getDescription());

        if (delega.getWorker().getAttribuzione2() != null)
            d.setLavoratoreAttribuzione2(delega.getWorker().getAttribuzione2().getDescription());

        if (delega.getWorker().getAttribuzione3() != null)
            d.setLavoratoreAttribuzione3(delega.getWorker().getAttribuzione3().getDescription());


        d.setLavoratoreNote(delega.getWorker().getNotes());
    }
    private void populateUiDelegaWorkerData1(DelegaUnc delega, UiDelega d) {
        d.setLavoratoreId(delega.getWorker().getLid());
        d.setLavoratoreCap(delega.getWorker().getCap());
        d.setLavoratoreCellulare(delega.getWorker().getCellphone());
        d.setLavoratorMail(delega.getWorker().getMail());
        d.setLavoratoreTelefono(delega.getWorker().getPhone());
        d.setLavoratoreCittaResidenza(delega.getWorker().getLivingCity());
        d.setLavoratoreCodiceFiscale(delega.getWorker().getFiscalcode());
        d.setLavoratoreCognome(delega.getWorker().getSurname());

        d.setLavoratoreDataNascita(delega.getWorker().getBirthDate());
        d.setLavoratoreIndirizzo(delega.getWorker().getAddress());
        d.setLavoratoreLuogoNascita(delega.getWorker().getBirthPlace());
        d.setLavoratoreNazionalita(delega.getWorker().getNationality());
        d.setLavoratoreNome(delega.getWorker().getName());
        d.setLavoratoreCittaResidenza(delega.getWorker().getLivingCity());
        d.setLavoratoreProvinciaNascita(delega.getWorker().getBirthProvince());
        d.setLavoratoreProvinciaResidenza(delega.getWorker().getLivingProvince());
        if (delega.getWorker().getSex().equals("M"))
            d.setLavoratoreSesso(Lavoratore.MALE);
        else
            d.setLavoratoreSesso(Lavoratore.FEMALE);

//            d.setLavoratoreCodiceCassaEdile(delega.getWorker().getCe());
//            d.setLavoratoreCodiceEdilcassa(delega.getWorker().getEc());
        if (delega.getWorker().getFund() != null)
            d.setLavoratoreFondo(delega.getWorker().getFund().getDescription());


        if (delega.getWorker().getAttribuzione1() != null)
            d.setLavoratoreAttribuzione1(delega.getWorker().getAttribuzione1().getDescription());

        if (delega.getWorker().getAttribuzione2() != null)
            d.setLavoratoreAttribuzione2(delega.getWorker().getAttribuzione2().getDescription());

        if (delega.getWorker().getAttribuzione3() != null)
            d.setLavoratoreAttribuzione3(delega.getWorker().getAttribuzione3().getDescription());


        d.setLavoratoreNote(delega.getWorker().getNotes());
    }

    public List<UiDelega> convertDelegheToUiDeleghe(List<Delega> del) {

        List<UiDelega> result = new ArrayList<>();

        List<Company> companies = comRep.find(null).getRows();

        for (Delega delega : del) {
            UiDelega d = new UiDelega();

            d.setCompanyId(delega.getCompanyId());
            if (delega.getBreviMano() == null)
                d.setDelegaBreviMano(false);
            else
                d.setDelegaBreviMano(delega.getBreviMano());

            d.setRegione(companies.stream().filter(a ->a.getLid() == delega.getCompanyId()).findFirst().orElse(null).getDescription());
            d.setDelegaProvincia(delega.getProvince().getDescription());
            if (delega.getSubscribeReason() != null)
                d.setDelegaCausaleSottoscrizione(delega.getSubscribeReason().getDescription());
            if (delega.getRevokeReason() != null)
                d.setDelegaCausaleRevoca(delega.getRevokeReason().getDescription());
            if (delega.getCancelReason() != null)
                d.setDelegaCausaleAnnullamento(delega.getCancelReason().getDescription());

            d.setDelegaOperatore(delega.getUserCompleteName());

            d.setDelegaMansione(delega.getMansione());
            d.setDelegaLuogoLavoro(delega.getLuogoLavoro());

            d.setDelegaDataDocumento(delega.getDocumentDate());

                d.setDelegaDataInoltro(delega.getSendDate());

                d.setDelegaDataAccettazione(delega.getAcceptDate());

                d.setDelegaDataAttivazione(delega.getActivationDate());

                d.setDelegaDataAnnullamento(delega.getCancelDate());

                d.setDelegaDataRevoca(delega.getRevokeDate());


            if (delega.getSector() != null)
                d.setDelegaSettore(delega.getSector().getDescription());
            if (delega.getContract() != null)
                d.setDelegaContract(delega.getContract().getDescription());
            if (delega.getCollaborator()!= null)
                d.setDelegaCollaboratore(delega.getCollaborator().getDescription());

            d.setDelegaStato(getDelegaStateString(delega.getState()));
            d.setDelegaId(delega.getLid());
            d.setDelegaNote(delega.getNotes());




            d.setLavoratoreId(delega.getWorker().getLid());
            d.setLavoratoreCap(delega.getWorker().getCap());
            d.setLavoratoreCellulare(delega.getWorker().getCellphone());
            d.setLavoratorMail(delega.getWorker().getMail());
            d.setLavoratoreTelefono(delega.getWorker().getPhone());
            d.setLavoratoreCittaResidenza(delega.getWorker().getLivingCity());
            d.setLavoratoreCodiceFiscale(delega.getWorker().getFiscalcode());
            d.setLavoratoreCognome(delega.getWorker().getSurname());

            d.setLavoratoreDataNascita(delega.getWorker().getBirthDate());
            d.setLavoratoreIndirizzo(delega.getWorker().getAddress());
            d.setLavoratoreLuogoNascita(delega.getWorker().getBirthPlace());
            d.setLavoratoreNazionalita(delega.getWorker().getNationality());
            d.setLavoratoreNome(delega.getWorker().getName());
            d.setLavoratoreCittaResidenza(delega.getWorker().getLivingCity());
            d.setLavoratoreProvinciaNascita(delega.getWorker().getBirthProvince());
            d.setLavoratoreProvinciaResidenza(delega.getWorker().getLivingProvince());
            if (delega.getWorker().getSex().equals("M"))
                d.setLavoratoreSesso(Lavoratore.MALE);
            else
                d.setLavoratoreSesso(Lavoratore.FEMALE);

//            d.setLavoratoreCodiceCassaEdile(delega.getWorker().getCe());
//            d.setLavoratoreCodiceEdilcassa(delega.getWorker().getEc());
            if (delega.getWorker().getFund() != null)
                d.setLavoratoreFondo(delega.getWorker().getFund().getDescription());


            if (delega.getWorker().getAttribuzione1() != null)
                d.setLavoratoreAttribuzione1(delega.getWorker().getAttribuzione1().getDescription());

            if (delega.getWorker().getAttribuzione2() != null)
                d.setLavoratoreAttribuzione2(delega.getWorker().getAttribuzione2().getDescription());

            if (delega.getWorker().getAttribuzione3() != null)
                d.setLavoratoreAttribuzione3(delega.getWorker().getAttribuzione3().getDescription());


            d.setLavoratoreNote(delega.getWorker().getNotes());

            if (delega.getWorkerCompany() != null) {
                d.setAziendaRagioneSociale(delega.getWorkerCompany().getDescription());
                d.setAziendaCitta(delega.getWorkerCompany().getCity());
                d.setAziendaProvincia(delega.getWorkerCompany().getProvince());
                d.setAziendaCap(delega.getWorkerCompany().getCap());
                d.setAziendaIndirizzo(delega.getWorkerCompany().getAddress());
                d.setAziendaNote(delega.getWorkerCompany().getNotes());
                d.setAziendaId(delega.getWorkerCompany().getLid());
            }



            result.add(d);
        }

        return result;
    }

    private String getDelegaStateString(Integer delegaState) {

        switch (delegaState) {
            case Delega.state_accepted:
                return "Accettata";
            case Delega.state_subscribe:
                return "Sottoscritta";
            case Delega.state_sent:
                return "Inoltrata";
            case Delega.state_activated:
                return "Attivata";
            case Delega.state_revoked:
                return "Revocata";
            case Delega.state_cancelled:
                return "Cancellata";
            default:
                return "";
        }
    }


    public UISaveDelegaResponse saveDelega(UIDelega delega) throws Exception {
        Delega l = convertUIDelegaToModelEntity(delega);

        delegheService.saveOrUpdate(((User) security.getLoggedUser()).getLid(),l);

        //Restituisco l'oggetto delega in quanto per la navigazione è necessario sia l'id della delega
        // che quello del suo lavoratore
        return getSaveDelegaResponse(l);
    }

    private UISaveDelegaResponse getSaveDelegaResponse(Delega l) {
        UISaveDelegaResponse response = new UISaveDelegaResponse();
        response.setId(l.getLid());
        response.setWorkerId(l.getWorker().getLid());
        return response;
    }

    private Delega convertUIDelegaToModelEntity(UIDelega uiDelega) {
        Delega delega = new Delega();
        delega.setId(uiDelega.getId());
        delega.setNotes(uiDelega.getNotes());
        delega.setMansione(uiDelega.getMansione());
        delega.setLuogoLavoro(uiDelega.getLuogoLavoro());
        delega.setBreviMano(uiDelega.getBreviMano());
        try {
            delega.setDocumentDate(StringUtils.hasLength(uiDelega.getDocumentDate()) ? FenealDateUtils.getDateFromString(uiDelega.getDocumentDate(), FenealDateUtils.FORMAT_DATE_DATEPICKER): null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (StringUtils.hasLength(uiDelega.getSector())) {
            try {
                LoadRequest req = LoadRequest.build().filter("description", uiDelega.getSector());
                delega.setSector(sectorRepository.find(req).findFirst().orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.hasLength(uiDelega.getContract())) {
            try {
                long parithethicId = Long.parseLong(uiDelega.getContract());
                delega.setContract(paritheticRepository.get(parithethicId).orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.hasLength(uiDelega.getWorkerCompany())) {
            try {
                long aziendaId = Long.parseLong(uiDelega.getWorkerCompany());
                delega.setWorkerCompany(aziendeRepository.get(aziendaId).orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.hasLength(uiDelega.getCollaborator())) {
            try {
                long collaboratoreId = Long.parseLong(uiDelega.getCollaborator());
                delega.setCollaborator(collaboratorRepository.get(collaboratoreId).orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.hasLength(uiDelega.getWorkerId())) {
            try {
                long lavoratoreId = Long.parseLong(uiDelega.getWorkerId());
                delega.setWorker(lavoratoriRepository.get(lavoratoreId).orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.hasLength(uiDelega.getProvince())) {
            try {
                Integer provinceID = Integer.parseInt(uiDelega.getProvince());
                delega.setProvince(provinceRepository.get(provinceID).orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.hasLength(uiDelega.getSubscribeReason())) {
            try {
                Long subscribeReasonId = Long.parseLong(uiDelega.getSubscribeReason());
                delega.setSubscribeReason(subscribeReason.get(subscribeReasonId).orElse(null));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


        //lo stato non va modificato dal form
        //le date relative agli stati non devono poter essere modificate...


        return delega;
    }

    public void deleteDelega(long delegaId) throws Exception {
        delegheService.deleteDelega(((User) security.getLoggedUser()).getLid(), delegaId);
    }

    public Delega getDelegaById(Long id) {
        return delegheService.getDelegaById(id);
    }


    public void changeState(UiDelegaChangeState state) throws Exception {

        Date d = parseDate(state);



        Delega delega = getDelegaById(Long.parseLong(state.getDelegaId()));
        switch (Integer.parseInt(state.getState())) {
            case Delega.state_accepted:

                delegheService.acceptDelega(d, delega);
                return;
            case Delega.state_subscribe:
                delegheService.subscribeDelega(delega);
                return;
            case Delega.state_sent:
                delegheService.sendDelega(d, delega);
                return;
            case Delega.state_activated:
                delegheService.activateDelega(d, delega);
                return;
            case Delega.state_revoked:
                CausaleRevoca c = retrieveCausale(state.getCausaleId());
                delegheService.revokeDelega(d, delega, c);
                return;
            case Delega.state_cancelled:

                CausaleRevoca c1 = retrieveCausale(state.getCausaleId());
                delegheService.cancelDelega(d, delega, c1);
                return;
            case Delega.ACTION_BACK:
                delegheService.goBack(delega);
                return;
        }
    }

    private CausaleRevoca retrieveCausale(String causaleId) {
        try{
            return revRep.get(Integer.parseInt(causaleId)).orElse(null);
        }catch (Exception e){
            return null;
        }
    }

    private Date parseDate(UiDelegaChangeState state) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = f.parse(state.getDate());
        } catch (Exception e) {
            e.printStackTrace();
            d = new Date();
        }
        return d;
    }

    public void revertState(Long id) throws Exception {
        Delega delega = getDelegaById(id);
        delegheService.goBack(delega);
    }

    public void inoltreDeleghe(UiInoltraDeleghe uiSend) throws Exception {
        List<Delega> deleghe = new ArrayList<>();

        for (UiDelega uiDelega : uiSend.getSelectedDeleghe()) {
            Delega del = delegheService.getDelegaById(uiDelega.getDelegaId());
            if (del != null)
                deleghe.add(del);
        }


        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        try {
            d = f.parse(uiSend.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        delegheService.inoltraDeleghe(deleghe, d);
    }

    public void accettaDeleghe(UiAccettaDeleghe uiSend) throws Exception {
        List<Delega> deleghe = new ArrayList<>();

        for (UiDelega uiDelega : uiSend.getSelectedDeleghe()) {
            Delega del = delegheService.getDelegaById(uiDelega.getDelegaId());
            if (del != null)
                deleghe.add(del);
        }


        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        try {
            d = f.parse(uiSend.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        delegheService.accettaDeleghe(deleghe, d);
    }

    public ListaLavoro createListalavoro(List<UiDelega> deleghe, String description) throws Exception {
        List<Delega> com = convertUiToDeleghe(deleghe);
        return lSrv.createListaFromDeleghe(com, description);
    }

    private List<Delega> convertUiToDeleghe(List<UiDelega> deleghe) {
        List<Delega> a = new ArrayList<>();

        for (UiDelega uiDelega : deleghe) {
            Delega s = new Delega();
            s.setWorker(lavRep.get(uiDelega.getLavoratoreId()).orElse(null));

            //non mi serviranno gli altri campi.... dato che utilizzo il metodo solo per la crerazione
            //della òlista di lavoro

            a.add(s);
        }

        return a;
    }


    public List<UiDelega> reportDelegheTot(UiDelegheReportSearchParams params) {
        List<UiDelega> deleghe = reportDeleghe(params);
        List<UiDelega> delegheBile = reportDelegheBilateralita(params);
        List<UiDelega> delegheUnc = reportDelegheUnc(params);

        deleghe.forEach(a -> a.setTipo("Delega"));
        delegheBile.forEach(a -> a.setTipo("Delega Bilateralita"));
        delegheUnc.forEach(a -> a.setTipo("Delega Unc"));

        List<UiDelega> result = new ArrayList<>();
        result.addAll(deleghe);
        result.addAll(delegheBile);
        result.addAll(delegheUnc);


        return result;

    }
}
