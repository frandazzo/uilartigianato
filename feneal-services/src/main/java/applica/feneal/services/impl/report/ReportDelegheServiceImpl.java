package applica.feneal.services.impl.report;

import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.deleghe.DelegheRepository;
import applica.feneal.domain.data.core.rappresentanza.DelegaBilateralitaRepository;
import applica.feneal.domain.data.core.rappresentanza.DelegaUncRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.deleghe.UiDelegheReportSearchParams;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;
import applica.feneal.services.GeoService;
import applica.feneal.services.ReportDelegheService;
import applica.framework.Disjunction;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by angelo on 17/05/2016.
 */
@Service
public class ReportDelegheServiceImpl implements ReportDelegheService {

    @Autowired
    private DelegheRepository delRep;

    @Autowired
    private CategoriaRepository secRep;


   @Autowired
   private DelegaBilateralitaRepository delBilRep;

   @Autowired
   private DelegaUncRepository delUncRep;




    @Override
    public List<Delega> retrieveDeleghe(UiDelegheReportSearchParams params) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String region = params.getCompany();
        String province = params.getProvince();
        String sector = params.getSector();

        String firm = params.getFirm();
        String collaborator = params.getCollaborator();
        String causaleIscr = params.getCausaleIscrizione();


        Disjunction d = new Disjunction();
        List<Filter> orFilters = new ArrayList<>();
        if (params.isSubscribed())
        {
            Filter f = new Filter();
            f.setProperty("state");
            f.setType(Filter.EQ);
            f.setValue(Delega.state_subscribe);
            orFilters.add(f);
        }

        if (params.isSent())
        {
            Filter f = new Filter();
            f.setProperty("state");
            f.setType(Filter.EQ);
            f.setValue(Delega.state_sent);
            orFilters.add(f);
        }

        if (params.isAccepted())
        {
            Filter f = new Filter();
            f.setProperty("state");
            f.setType(Filter.EQ);
            f.setValue(Delega.state_accepted);
            orFilters.add(f);
        }

        if (params.isActivated())
        {
            Filter f = new Filter();
            f.setProperty("state");
            f.setType(Filter.EQ);
            f.setValue(Delega.state_activated);
            orFilters.add(f);
        }

        if (params.isCancelled())
        {
            Filter f = new Filter();
            f.setProperty("state");
            f.setType(Filter.EQ);
            f.setValue(Delega.state_cancelled);
            orFilters.add(f);
        }

        if (params.isRevoked())
        {
            Filter f = new Filter();
            f.setProperty("state");
            f.setType(Filter.EQ);
            f.setValue(Delega.state_revoked);
            orFilters.add(f);
        }

        d.setChildren(orFilters);



        Date subscribedFromDate = null;
        if (!StringUtils.isEmpty(params.getSubscribedFromDate())){
            try{
                subscribedFromDate = df.parse(params.getSubscribedFromDate());
            }catch(Exception e){
                subscribedFromDate = null;
            }
        }


        Date subscribedToDate = null;
        if (!StringUtils.isEmpty(params.getSubscribedToDate()))
            try {
                subscribedToDate = df.parse(params.getSubscribedToDate());
            } catch (ParseException e) {
                subscribedToDate = null;
            }

        Date sentFromDate = null;
        if (!StringUtils.isEmpty(params.getSentFromDate()))
            try {
                sentFromDate = df.parse(params.getSentFromDate());
            } catch (ParseException e) {
                sentFromDate = null;
            }

        Date sentToDate = null;
        if (!StringUtils.isEmpty(params.getSentToDate()))
            try {
                sentToDate = df.parse(params.getSentToDate());
            } catch (ParseException e) {
                sentToDate = null;
            }

        Date acceptedFromDate = null;
        if (!StringUtils.isEmpty(params.getAcceptedFromDate()))
            try {
                acceptedFromDate = df.parse(params.getAcceptedFromDate());
            } catch (ParseException e) {
                acceptedFromDate = null;
            }

        Date acceptedToDate = null;
        if (!StringUtils.isEmpty(params.getAcceptedToDate()))
            try {
                acceptedToDate = df.parse(params.getAcceptedToDate());
            } catch (ParseException e) {
                acceptedToDate = null;
            }

        Date activatedFromDate = null;
        if (!StringUtils.isEmpty(params.getActivatedFromDate()))
            try {
                activatedFromDate = df.parse(params.getActivatedFromDate());
            } catch (ParseException e) {
                activatedFromDate = null;
            }

        Date activatedToDate = null;
        if (!StringUtils.isEmpty(params.getActivatedToDate()))
            try {
                activatedToDate = df.parse(params.getActivatedToDate());
            } catch (ParseException e) {
                activatedToDate = null;
            }

        Date cancelledFromDate = null;
        if (!StringUtils.isEmpty(params.getCancelledFromDate()))
            try {
                cancelledFromDate = df.parse(params.getCancelledFromDate());
            } catch (ParseException e) {
                cancelledFromDate = null;
            }

        Date cancelledToDate = null;
        if (!StringUtils.isEmpty(params.getCancelledToDate()))
            try {
                cancelledToDate = df.parse(params.getCancelledToDate());
            } catch (ParseException e) {
                cancelledToDate = null;
            }

        Date revokedFromDate = null;
        if (!StringUtils.isEmpty(params.getRevokedFromDate()))
            try {
                revokedFromDate = df.parse(params.getRevokedFromDate());
            } catch (ParseException e) {
                revokedFromDate = null;
            }

        Date revokedToDate = null;
        if (!StringUtils.isEmpty(params.getRevokedToDate()))
            try {
                revokedToDate = df.parse(params.getRevokedToDate());
            } catch (ParseException e) {
                revokedToDate = null;
            }


        //posso adesso fare la query

        LoadRequest req = LoadRequest.build();

        if (orFilters.size() > 0)
            req.getFilters().add(d);


        if (!StringUtils.isEmpty(region)){

            Filter fc = new Filter("companyId", Long.parseLong(region), Filter.EQ);
            req.getFilters().add(fc);
        }


        if (!StringUtils.isEmpty(province)){
            Integer proId = Integer.parseInt(province);
            Filter f1 = new Filter("province.id", proId, Filter.EQ);
            req.getFilters().add(f1);
        }

        if (!StringUtils.isEmpty(sector)){
            Categoria s = secRep.get(params.getSector()).orElse(null);

            if (s != null){
                Filter f2 = new Filter("sector", s.getId(), Filter.EQ);
                req.getFilters().add(f2);
            }
        }

        if (!StringUtils.isEmpty(firm)){
            Filter f4 = new Filter("workerCompany", Long.parseLong(firm), Filter.EQ);
            req.getFilters().add(f4);
        }

        if (!StringUtils.isEmpty(collaborator)){
            Filter f5 = new Filter("collaborator", Long.parseLong(collaborator), Filter.EQ);
            req.getFilters().add(f5);
        }

        if (!StringUtils.isEmpty(causaleIscr)){
            Filter f6 = new Filter("subscribeReason", Long.parseLong(causaleIscr), Filter.EQ);
            req.getFilters().add(f6);
        }

        if (subscribedFromDate != null) {
            Filter f7 = new Filter("documentDate", subscribedFromDate, Filter.GTE);
            req.getFilters().add(f7);
        }

        if (subscribedToDate != null) {
            Filter f8 = new Filter("documentDate", subscribedToDate, Filter.LTE);
            req.getFilters().add(f8);
        }

        if (sentFromDate != null) {
            Filter f9 = new Filter("sendDate", sentFromDate, Filter.GTE);
            req.getFilters().add(f9);
        }

        if (sentToDate != null) {
            Filter f10 = new Filter("sendDate", sentToDate, Filter.LTE);
            req.getFilters().add(f10);
        }

        if (acceptedFromDate != null) {
            Filter f11 = new Filter("acceptDate", acceptedFromDate, Filter.GTE);
            req.getFilters().add(f11);
        }

        if (acceptedToDate != null) {
            Filter f12 = new Filter("acceptDate", acceptedToDate, Filter.LTE);
            req.getFilters().add(f12);
        }

        if (activatedFromDate != null) {
            Filter f13 = new Filter("activationDate", activatedFromDate, Filter.GTE);
            req.getFilters().add(f13);
        }

        if (activatedToDate != null) {
            Filter f14 = new Filter("activationDate", activatedToDate, Filter.LTE);
            req.getFilters().add(f14);
        }

        if (cancelledFromDate != null) {
            Filter f15 = new Filter("cancelDate", cancelledFromDate, Filter.GTE);
            req.getFilters().add(f15);
        }

        if (cancelledToDate != null) {
            Filter f16 = new Filter("cancelDate", cancelledToDate, Filter.LTE);
            req.getFilters().add(f16);
        }

        if (revokedFromDate != null) {
            Filter f17 = new Filter("revokeDate", revokedFromDate, Filter.GTE);
            req.getFilters().add(f17);
        }

        if (revokedToDate != null) {
            Filter f18 = new Filter("revokeDate", revokedToDate, Filter.LTE);
            req.getFilters().add(f18);
        }


        List<Delega> del = delRep.find(req).getRows();
        return del;

    }

    @Override
    public List<DelegaBilateralita> retrieveDelegheBilateralita(UiDelegheReportSearchParams params) {
        String region = params.getCompany();
        String province = params.getProvince();
        String sector = params.getSector();

        LoadRequest req = LoadRequest.build();
        if (!StringUtils.isEmpty(region)){
            Filter fc = new Filter("companyId", Long.parseLong(region), Filter.EQ);
            req.getFilters().add(fc);
        }
        if (!StringUtils.isEmpty(province)){
            Integer proId = Integer.parseInt(province);
            Filter f1 = new Filter("province.id", proId, Filter.EQ);
            req.getFilters().add(f1);
        }

        if (!StringUtils.isEmpty(sector)){
            Categoria s = secRep.get(params.getSector()).orElse(null);

            if (s != null){
                Filter f2 = new Filter("sector", s.getId(), Filter.EQ);
                req.getFilters().add(f2);
            }
        }

        List<DelegaBilateralita> del = delBilRep.find(req).getRows();
        return del;

    }

    @Override
    public List<DelegaUnc> retrieveDelegheUnc(UiDelegheReportSearchParams params) {
        String region = params.getCompany();
        String province = params.getProvince();
        String sector = params.getSector();
        String firm = params.getFirm();
        LoadRequest req = LoadRequest.build();
        if (!StringUtils.isEmpty(region)){
            Filter fc = new Filter("companyId", Long.parseLong(region), Filter.EQ);
            req.getFilters().add(fc);
        }
        if (!StringUtils.isEmpty(province)){
            Integer proId = Integer.parseInt(province);
            Filter f1 = new Filter("province.id", proId, Filter.EQ);
            req.getFilters().add(f1);
        }

        if (!StringUtils.isEmpty(sector)){
            Categoria s = secRep.get(params.getSector()).orElse(null);

            if (s != null){
                Filter f2 = new Filter("sector", s.getId(), Filter.EQ);
                req.getFilters().add(f2);
            }
        }

        if (!StringUtils.isEmpty(firm)){
            Filter f4 = new Filter("workerCompany", Long.parseLong(firm), Filter.EQ);
            req.getFilters().add(f4);
        }

        List<DelegaUnc> del = delUncRep.find(req).getRows();
        return del;
    }


    /* Crea oggetto java Date dato il mese e l'anno */
    private Date createDate(int day, String month, String year) {

        if (month == null || year == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        if (day == 0)
            calendar.set(Calendar.MONTH, Integer.valueOf(month));
        else
            calendar.set(Calendar.MONTH, Integer.valueOf(month) - 1);

        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        Date date = calendar.getTime();

        return date;
    }
}
