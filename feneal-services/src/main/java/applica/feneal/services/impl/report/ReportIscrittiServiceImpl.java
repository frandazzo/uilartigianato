package applica.feneal.services.impl.report;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.Attribuzione1Repository;
import applica.feneal.domain.data.core.Attribuzione2Repository;
import applica.feneal.domain.data.core.Attribuzione3Repository;
import applica.feneal.domain.data.core.FundRepository;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.servizi.search.UiIscrittoReportSearchParams;
import applica.feneal.domain.model.dbnazionale.Iscrizione;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.feneal.domain.model.setting.Fondo;
import applica.feneal.domain.utils.Box;
import applica.feneal.services.DelegheService;
import applica.feneal.services.GeoService;
import applica.feneal.services.ReportIscrittiService;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by angelo on 29/05/2016.
 */
@Service
public class ReportIscrittiServiceImpl implements ReportIscrittiService {

    @Autowired
    private DettaglioQuoteAssociativeRepository dettRep;

    @Autowired
    private AziendeRepository azRep;

    @Autowired
    private DelegheService delServ;

    @Autowired
    private GeoService geoSvc;

    @Autowired
    private CategoriaRepository settoreRepository;

    @Autowired
    private LavoratoriRepository lavRep;

    @Autowired
    private Attribuzione1Repository attr1Rep;

    @Autowired
    private Attribuzione2Repository attr2Rep;

    @Autowired
    private Attribuzione3Repository attr3Rep;

    @Autowired
    private FundRepository fundRep;


    @Override
    public List<Iscritto> retrieveFastPerformaceQuoteIscritti(UiIscrittoReportSearchParams params) throws Exception {
        final Box box = new Box();


        List<Attribuzione1> attr1s = attr1Rep.find(null).getRows();
        List<Attribuzione2> attr2s = attr2Rep.find(null).getRows();
        List<Attribuzione3> attr3s = attr3Rep.find(null).getRows();
        List<Fondo> funds = fundRep.find(null).getRows();


        lavRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session s = lavRep.getSession();
                Transaction tx = null;

                try{

                    tx = s.beginTransaction();

                    String query = createQuery(params);
                    List<Object[]> objects = s.createSQLQuery(query)
                            //.addScalar("ID")
                            .addScalar("iscrittoProvincia")
                            .addScalar("iscrittoDataRegistrazione")
                            .addScalar("iscrittoDataInizio")
                            .addScalar("iscrittoDataFine")
                            .addScalar("iscrittoTipo")
                            .addScalar("iscrittoSettore")
                            .addScalar("iscrittoOperatoreDelega")
                            .addScalar("iscrittoQuota")
                            .addScalar("iscrittoLivello")
                            .addScalar("iscrittoNote")
                            .addScalar("iscrittoContratto")
                            .addScalar("idAzienda")
                            .addScalar("idLavoratore")
                            .addScalar("iscrittoRegione")

                            .addScalar("lavoratoreNome")
                            .addScalar("lavoratoreCognome")
                            .addScalar("lavoratoreSesso")
                            .addScalar("lavoratoreCodiceFiscale")
                            .addScalar("lavoratoreDataNascita")
                            .addScalar("lavoratoreNazionalita")
                            .addScalar("lavoratoreProvinciaNascita")
                            .addScalar("lavoratoreLuogoNascita")
                            .addScalar("lavoratoreProvinciaResidenza")
                            .addScalar("lavoratoreCittaResidenza")
                            .addScalar("lavoratoreIndirizzo")
                            .addScalar("lavoratoreCap")
                            .addScalar("lavoratoreTelefono")
                            .addScalar("lavoratoreCellulare")
                            .addScalar("lavoratorMail")
                            .addScalar("lavoratoreFondo")
                            .addScalar("lavoratoreAttribuzione1")
                            .addScalar("lavoratoreAttribuzione2")
                            .addScalar("lavoratoreAttribuzione3")
                            .addScalar("aziendaRagioneSociale")
                            .addScalar("iscrittoRipresaDati")

                            .addScalar("delegaMansione")
                            .addScalar("delegaLuogoLavoro")
                            .addScalar("delegaNote")
                            .addScalar("delegaCollaboratore")
                    .list();

                    tx.commit();



                    List<Iscritto> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        Iscritto v = new Iscritto();

                        v.setIscrittoProvincia((String)object[0]);

                        Date dataRegistrazione = (Date)object[1];
                        v.setIscrittoDataRegistrazione(dataRegistrazione);

                        Date dataInizio = (Date)object[2];
                        Date dataFine = (Date)object[3];
                        SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");

                        v.setIscrittoDataFine(dataFine);
                        v.setIscrittoDataInizio(dataInizio);

                        v.setIscrittoCompetenza(String.format("%s - %s", f1.format(dataInizio), f1.format(dataFine)));
                        v.setTipo((String)object[4]);
                        v.setIscrittoSettore((String)object[5]);

                        v.setIscrittoOperatoreDelega((String)object[6]);
                        v.setIscrittoQuota((Double)object[7]);
                        v.setIscrittoLivello((String)object[8]);

                        v.setIscrittoNote((String)object[9]);
                        v.setIscrittoContratto((String)object[10]);
                        if (object[11] != null)
                            v.setAziendaId(((BigInteger)object[11]).longValue());
                        v.setLavoratoreId(((BigInteger)object[12]).longValue());
                        v.setIscrittoRegione((String)object[13]);

                        v.setLavoratoreNome((String)object[14]);
                        v.setLavoratoreCognome((String)object[15]);
                        v.setLavoratoreSesso((String)object[16]);
                        v.setLavoratoreCodiceFiscale((String)object[17]);
                        v.setLavoratoreDataNascita((Date)object[18]);
                        v.setLavoratoreNazionalita((String)object[19]);
                        v.setLavoratoreProvinciaNascita((String)object[20]);
                        v.setLavoratoreLuogoNascita((String)object[21]);
                        v.setLavoratoreProvinciaResidenza((String)object[22]);
                        v.setLavoratoreCittaResidenza((String)object[23]);
                        v.setLavoratoreIndirizzo((String)object[24]);
                        v.setLavoratoreCap((String)object[25]);
                        v.setLavoratoreTelefono((String)object[26]);
                        v.setLavoratoreCellulare((String)object[27]);
                        v.setLavoratorMail((String)object[28]);
                        if (object[29] != null){
                            Fondo f = funds.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[29]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreFondo(f.getDescription());
                        }

                        if (object[30] != null){
                            Attribuzione1 f = attr1s.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[30]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreAttribuzione1(f.getDescription());
                        }

                        if (object[31] != null)
                        {
                            Attribuzione2 f = attr2s.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[31]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreAttribuzione2(f.getDescription());
                        }
                        if (object[32] != null){
                            Attribuzione3 f = attr3s.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[32]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreAttribuzione3(f.getDescription());
                        }

                        v.setAziendaRagioneSociale((String)object[33]);
                        v.setIscrittoRipresaDati((boolean)object[34]);

                        v.setDelegaMansione((String)object[35]);
                        v.setDelegaLuogoLavoro((String)object[36]);
                        v.setDelegaNote((String)object[37]);
                        v.setDelegaCollaboratore((String)object[38]);
                        v.setCompanyId(Long.parseLong(params.getCompany()));
                        //ComprensoriTrentino comprensori = new ComprensoriTrentino();
                        //v.setLavoratoreComprensorio(comprensori.getComprensorio(v.getLavoratoreCittaResidenza()));

                        a.add(v);
                    }


                    box.setValue(a);


                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                }
                finally{
                    s.close();

                }
            }

            private String createQuery(UiIscrittoReportSearchParams params) {

                Date dateFrom = createDate(1, params.getDatefromMonthReport(), params.getDatefromYearReport());
                Date dateTo = createDate(0, params.getDatetoMonthReport(), params.getDatetoYearReport());

                if (!dateFrom.before(dateTo)){
                    Date dd = dateTo;
                    dateTo = dateFrom;
                    dateFrom = dd;
                }

                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                String dateFromString = f.format(dateFrom);
                String dateToString = f.format(dateTo);

                String province = params.getProvince();

                String azienda = params.getFirm();

                String query =  String.format("select " +
                        "t.Provincia as iscrittoProvincia," +
                        "t.DataRegistrazione as iscrittoDataRegistrazione," +
                        "t.TipoDocumento as iscrittoTipo," +
                        "t.Settore as iscrittoSettore," +
                        "t.OperatoreDelega as iscrittoOperatoreDelega," +
                        "t.DataInizio as iscrittoDataInizio," +
                        "t.DataFine as iscrittoDataFine," +
                        "t.Quota as iscrittoQuota," +
                        "t.Livello as iscrittoLivello," +
                        "t.Note as iscrittoNote," +
                        "t.Contratto as iscrittoContratto, " +
                        "t.idAzienda," +
                        "t.idLavoratore," +
                        "t.Regione as iscrittoRegione, " +

                                "l.name as lavoratoreNome," +
                                "l.surname as lavoratoreCognome, " +
                                "l.sex as lavoratoreSesso," +
                                "l.birthDate as lavoratoreDataNascita," +
                                "l.fiscalCode as lavoratoreCodiceFiscale, " +
                                "l.nationality as lavoratoreNazionalita," +
                                "l.birthProvince as lavoratoreProvinciaNascita, " +
                                "l.birthPlace as lavoratoreLuogoNascita," +
                                "l.livingProvince as lavoratoreProvinciaResidenza," +
                                "l.livingCity as lavoratoreCittaResidenza, " +
                                "l.address as lavoratoreIndirizzo," +
                                "l.cap as lavoratoreCap, " +
                                "l.phone as lavoratoreTelefono," +
                                "l.cellPhone as lavoratoreCellulare," +
                                "l.mail as lavoratorMail, " +
                                "l.fundId as lavoratoreFondo," +
                                "l.attribuzione1Id as lavoratoreAttribuzione1, " +
                                "l.attribuzione2Id as lavoratoreAttribuzione2," +
                                "l.attribuzione3Id as lavoratoreAttribuzione3," +

                                "a.description as aziendaRagioneSociale," +

                            "t.ripresaDati as iscrittoRipresaDati, " +
                            "t.delegaMansione as delegaMansione," +
                            "t.delegaLuogoLavoro as delegaLuogoLavoro," +
                            "t.delegaNote as delegaNote," +
                            "t.delegaCollaboratore as delegaCollaboratore " +





                        " from fenealweb_dettaglioquote t  " +
                        "inner join fenealweb_lavoratore l on t.idLavoratore = l.id " +
                        "left outer join fenealweb_azienda a on t.idAzienda = a.id " +
                                                    "where t.companyId = %s " +
                                                    "and t.dataInizio <= '%s' " +
                                                    "and t.dataFine >= '%s'",
                                                    Long.parseLong(params.getCompany()),
                                                    dateToString,
                                                    dateFromString);


                if (!StringUtils.isEmpty(province)){
                    Province p = geoSvc.getProvinceById(Integer.parseInt(province));
                    query = query + String.format(" and t.provincia like '%s'", p.getDescription().replace("'","''"));
                }

                if (!StringUtils.isEmpty(params.getSector())){
                    Categoria r = settoreRepository.get(params.getSector()).orElse(null);
                    if (r != null){
                        query = query + String.format(" and t.settore = '%s'", r.getDescription());
                    }
                }


                if (!StringUtils.isEmpty(azienda)) {
                    query = query + String.format(" and t.idAzienda = %s", azienda);
                }


                return query;

            }
        });



        List<Iscritto> quoteIscritti = (List<Iscritto>)box.getValue();

        List<Iscritto> quoteIscrittiSenzaDuplicati = new ArrayList<>();

        // faccio visualizzare una sola volta il record relativo ad un iscritto
        for (Iscritto quota : quoteIscritti) {
            if (quoteIscrittiSenzaDuplicati.stream().filter(q -> q.getLavoratoreId() == quota.getLavoratoreId()).count() == 0)
                quoteIscrittiSenzaDuplicati.add(quota);
        }



        //a questo punto posso verificare eventuialmente l'esistenza della delega attiva o accettata
        if (!params.isDelegationActiveExist())
            return quoteIscrittiSenzaDuplicati;
        //se non sto cercando quote associative....


        List<Iscritto> ll = new ArrayList<>();
        for (Iscritto dettaglioQuotaAssociativa : quoteIscrittiSenzaDuplicati) {

            String az = "";
            //if (!s.containsEntiBilaterali() && StringUtils.isEmpty(azienda)){
            //recupero l'azienda dall'identificativo della quota
            Azienda a = azRep.get(dettaglioQuotaAssociativa.getAziendaId()).orElse(null);
            if (a != null)
                az = a.getDescription();
            //}

            String province1 = dettaglioQuotaAssociativa.getIscrittoProvincia();

            if (!StringUtils.isEmpty(params.getProvince())){
                province1 =  params.getProvince();
            }

            if (delServ.hasWorkerDelegaAttivaOAccettata(dettaglioQuotaAssociativa.getLavoratoreId(),  az, province1)){
                ll.add(dettaglioQuotaAssociativa);
            }
        }

        return ll;
    }

    @Override
    public List<Iscritto> retrieveFastPerformaceQuoteIscritti(long firmId) throws Exception {
        final Box box = new Box();

        List<Attribuzione1> attr1s = attr1Rep.find(null).getRows();
        List<Attribuzione2> attr2s = attr2Rep.find(null).getRows();
        List<Attribuzione3> attr3s = attr3Rep.find(null).getRows();
        List<Fondo> funds = fundRep.find(null).getRows();

         lavRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session s = lavRep.getSession();
                Transaction tx = null;

                try{

                    tx = s.beginTransaction();

                    String query = createQuery(firmId);
                    List<Object[]> objects = s.createSQLQuery(query)
                            //.addScalar("ID")
                            .addScalar("iscrittoProvincia")
                            .addScalar("iscrittoDataRegistrazione")
                            .addScalar("iscrittoDataInizio")
                            .addScalar("iscrittoDataFine")
                            .addScalar("iscrittoTipo")
                            .addScalar("iscrittoSettore")



                            .addScalar("iscrittoQuota")
                            .addScalar("iscrittoLivello")
                            .addScalar("iscrittoNote")
                            .addScalar("iscrittoContratto")
                            .addScalar("idAzienda")
                            .addScalar("idLavoratore")



                            .addScalar("lavoratoreNome")
                            .addScalar("lavoratoreCognome")
                            .addScalar("lavoratoreSesso")
                            .addScalar("lavoratoreDataNascita")
                            .addScalar("lavoratoreCodiceFiscale")

                            .addScalar("lavoratoreNazionalita")
                            .addScalar("lavoratoreProvinciaNascita")
                            .addScalar("lavoratoreLuogoNascita")
                            .addScalar("lavoratoreProvinciaResidenza")
                            .addScalar("lavoratoreCittaResidenza")
                            .addScalar("lavoratoreIndirizzo")
                            .addScalar("lavoratoreCap")
                            .addScalar("lavoratoreTelefono")
                            .addScalar("lavoratoreCellulare")
                            .addScalar("lavoratorMail")
                            .addScalar("lavoratoreFondo")
                            .addScalar("lavoratoreAttribuzione1")
                            .addScalar("lavoratoreAttribuzione2")
                            .addScalar("lavoratoreAttribuzione3")
                            .addScalar("aziendaRagioneSociale")
                            .addScalar("companyId")
                            .addScalar("iscrittoOperatoreDelega")
                            .list();



                            tx.commit();

                    List<Iscritto> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        Iscritto v = new Iscritto();

                        v.setIscrittoProvincia((String)object[0]);

                        Date dataRegistrazione = (Date)object[1];
                        v.setIscrittoDataRegistrazione(dataRegistrazione);

                        Date dataInizio = (Date)object[2];
                        Date dataFine = (Date)object[3];
                        SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");

                        v.setIscrittoDataFine(dataFine);
                        v.setIscrittoDataInizio(dataInizio);

                        v.setIscrittoCompetenza(String.format("%s - %s", f1.format(dataInizio), f1.format(dataFine)));
                        v.setTipo((String)object[4]);
                        v.setIscrittoSettore((String)object[5]);


                        v.setIscrittoQuota((Double)object[6]);
                        v.setIscrittoLivello((String)object[7]);

                        v.setIscrittoNote((String)object[8]);
                        v.setIscrittoContratto((String)object[9]);
                        if (object[11] != null)
                            v.setAziendaId(((BigInteger)object[10]).longValue());
                        v.setLavoratoreId(((BigInteger)object[11]).longValue());


                        v.setLavoratoreNome((String)object[12]);
                        v.setLavoratoreCognome((String)object[13]);
                        v.setLavoratoreSesso((String)object[14]);
                        v.setLavoratoreDataNascita((Date)object[15]);
                        v.setLavoratoreCodiceFiscale((String)object[16]);

                        v.setLavoratoreNazionalita((String)object[17]);
                        v.setLavoratoreProvinciaNascita((String)object[18]);
                        v.setLavoratoreLuogoNascita((String)object[19]);
                        v.setLavoratoreProvinciaResidenza((String)object[20]);
                        v.setLavoratoreCittaResidenza((String)object[21]);
                        v.setLavoratoreIndirizzo((String)object[22]);
                        v.setLavoratoreCap((String)object[23]);
                        v.setLavoratoreTelefono((String)object[24]);
                        v.setLavoratoreCellulare((String)object[25]);
                        v.setLavoratorMail((String)object[26]);
                        if (object[27] != null){
                            Fondo f = funds.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[28]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreFondo(f.getDescription());
                        }

                        if (object[28] != null){
                            Attribuzione1 f = attr1s.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[28]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreAttribuzione1(f.getDescription());
                        }

                        if (object[29] != null)
                        {
                            Attribuzione2 f = attr2s.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[29]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreAttribuzione2(f.getDescription());
                        }
                        if (object[30] != null){
                            Attribuzione3 f = attr3s.stream().filter(a1 -> a1.getLid() == ((BigInteger)object[30]).longValue()).findFirst().orElse(null);
                            if (f != null)
                                v.setLavoratoreAttribuzione3(f.getDescription());
                        }

                        v.setAziendaRagioneSociale((String)object[31]);
                        v.setCompanyId(((BigInteger)object[32]).longValue());
                        v.setIscrittoOperatoreDelega((String)object[33]);



                        a.add(v);
                    }


                    box.setValue(a);


                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                }
                finally{
                    s.close();

                }
            }

            private String createQuery(long firmId) {

                String query =  String.format("select " +
                                                    "t.Provincia as iscrittoProvincia," +
                            "t.DataRegistrazione as iscrittoDataRegistrazione," +
                            "t.DataInizio as iscrittoDataInizio," +
                            "t.DataFine as iscrittoDataFine," +
                            "t.TipoDocumento as iscrittoTipo," +
                            "t.Settore as iscrittoSettore," +
                            "t.Quota as iscrittoQuota," +
                            "t.Livello as iscrittoLivello," +
                            "t.Note as iscrittoNote," +
                            "t.Contratto as iscrittoContratto, " +
                            "t.idAzienda," +
                            "t.idLavoratore," +
                            "l.name as lavoratoreNome," +
                            "l.surname as lavoratoreCognome, " +
                            "l.sex as lavoratoreSesso," +
                            "l.birthDate as lavoratoreDataNascita," +
                            "l.fiscalCode as lavoratoreCodiceFiscale, " +
                            "l.nationality as lavoratoreNazionalita," +
                            "l.birthProvince as lavoratoreProvinciaNascita, " +
                            "l.birthPlace as lavoratoreLuogoNascita," +
                            "l.livingProvince as lavoratoreProvinciaResidenza," +
                            "l.livingCity as lavoratoreCittaResidenza, " +
                            "l.address as lavoratoreIndirizzo," +
                            "l.cap as lavoratoreCap, " +
                            "l.phone as lavoratoreTelefono," +
                            "l.cellPhone as lavoratoreCellulare," +
                            "l.mail as lavoratorMail, " +
                            "l.fundId as lavoratoreFondo," +
                            "l.attribuzione1Id as lavoratoreAttribuzione1, " +
                            "l.attribuzione2Id as lavoratoreAttribuzione2," +
                            "l.attribuzione3Id as lavoratoreAttribuzione3," +
                            "a.description as aziendaRagioneSociale," +
                            "t.companyId, " +
                            "t.OperatoreDelega as iscrittoOperatoreDelega" +

                                " from fenealweb_dettaglioquote t  " +
                                "inner join fenealweb_lavoratore l on t.idLavoratore = l.id " +
                                "inner join fenealweb_azienda a on t.idAzienda = a.id " +
                                "where t.idAzienda = %s " ,


                        firmId);


                return query;

            }
        });


        List<Iscritto> result = (List<Iscritto>)box.getValue();

        if (result != null)
            return result;

        return new ArrayList<>();

    }

//    @Override
//    public List<DettaglioQuotaAssociativa> retrieveQuoteIscritti(UiIscrittoReportSearchParams params) throws Exception {
//
//        Date dateFrom = createDate(1, params.getDatefromMonthReport(), params.getDatefromYearReport());
//        Date dateTo = createDate(0, params.getDatetoMonthReport(), params.getDatetoYearReport());
//        String province = params.getProvince();
//
//        String settore =  params.getSector();
//        String ente =  null;
//        String azienda = params.getFirm();
//
//
//
//        LoadRequest req = LoadRequest.build();
//
//        if (!dateFrom.before(dateTo)){
//            Date dd = dateTo;
//            dateTo = dateFrom;
//            dateFrom = dd;
//        }
//
//        req.getFilters().add(new Filter("dataInizio", dateTo, Filter.LTE));
//        req.getFilters().add(new Filter("dataFine", dateFrom, Filter.GTE));
//
//
//        User u = ((User) sec.getLoggedUser());
//        Role r = u.retrieveUserRole();
//
//        if (r.getLid() == 10 || r.getLid() == 11){
//                //non metto filtri sulla categoria
//        }else{
//            Filter fr = new Filter("categoryId" , ((User) sec.getLoggedUser()).getLid());
//            req.getFilters().add(fr);
//        }
//
//
//
//        if (!StringUtils.isEmpty(province)){
//            Filter f3 = new Filter("provincia", province, Filter.LIKE);
//            req.getFilters().add(f3);
//        }
//
//        Categoria s = null;
//        if (!StringUtils.isEmpty(settore)){
//
//            s = settoreRepository.find(LoadRequest.build().filter("description", settore)).findFirst().orElse(null);
//            if (s == null)
//                throw new Exception("Settore nullo");
//
//
//            Filter f7 = new Filter("settore", settore, Filter.EQ);
//            req.getFilters().add(f7);
//
//
//            if (!StringUtils.isEmpty(azienda)) {
//                Filter f9 = new Filter("idAzienda", azienda, Filter.EQ);
//                req.getFilters().add(f9);
//            }
//
//
//        }
//
//
//
//
//        List<DettaglioQuotaAssociativa> quoteIscritti = dettRep.find(req).getRows();
//
//        List<DettaglioQuotaAssociativa> quoteIscrittiSenzaDuplicati = new ArrayList<>();
//
//        // Eventualmente faccio visualizzare una sola volta il record relativo ad un iscritto
//
//        for (DettaglioQuotaAssociativa quota : quoteIscritti) {
//            if (quoteIscrittiSenzaDuplicati.stream().filter(q -> q.getIdLavoratore() == quota.getIdLavoratore()).count() == 0)
//                quoteIscrittiSenzaDuplicati.add(quota);
//        }
//
//
//
//
//
//
//
//        //a questo punto posso verificare eventuialmente l'esistenza della delega attiva o accettata
//        if (!params.isDelegationActiveExist())
//            return quoteIscrittiSenzaDuplicati;
//        //se non sto cercando quote associative....
//
//
//
//        //per ogni quota verifico l'esistenza di una delega attiva o accettata
//
//        if (s == null)
//            return quoteIscrittiSenzaDuplicati;
//
//        List<DettaglioQuotaAssociativa> ll = new ArrayList<>();
//        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quoteIscrittiSenzaDuplicati) {
//
//
//            String az = "";
//            //if (!s.containsEntiBilaterali() && StringUtils.isEmpty(azienda)){
//                //recupero l'azienda dall'identificativo della quota
//            Azienda a = azRep.get(dettaglioQuotaAssociativa.getIdAzienda()).orElse(null);
//            if (a != null)
//                az = a.getDescription();
//            //}
//
//            String province1 = dettaglioQuotaAssociativa.getProvincia();
//
//            if (!StringUtils.isEmpty(province)){
//                province1 =  params.getProvince();
//
//            }
//
//            if (delServ.hasWorkerDelegaAttivaOAccettata(dettaglioQuotaAssociativa.getIdLavoratore(),  az, province1)){
//                ll.add(dettaglioQuotaAssociativa);
//            }
//
//        }
//
//        return ll;
//    }



    private List<DettaglioQuotaAssociativa> findQuoteByWorkerId(long id){
        LoadRequest req = LoadRequest.build();

        Filter f9 = new Filter("idLavoratore", id, Filter.EQ);
        req.getFilters().add(f9);




        List<DettaglioQuotaAssociativa> quoteIscritti = dettRep.find(req).getRows();

        List<DettaglioQuotaAssociativa> normalizedquoteIscritti = new ArrayList<>();
        //in questa lista metto tutto in maniera normalizzata: ossia clono tutto il dettaglio quota
        //in base al numero di anni della competenza (guarda il metodo statico DettaglioQuotaAssociativa.cloneOnEveryCompetenceYear
        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quoteIscritti) {
            normalizedquoteIscritti.addAll(dettaglioQuotaAssociativa.cloneOnEveryCompetenceYear());
        }


        List<DettaglioQuotaAssociativa> quoteIscrittiSenzaDuplicati = new ArrayList<>();

        // Eventualmente faccio visualizzare una sola volta il record relativo ad un iscritto

            for (DettaglioQuotaAssociativa quota : normalizedquoteIscritti) {
                if (quoteIscrittiSenzaDuplicati.stream().filter(
                        q -> q.getIdLavoratore() == quota.getIdLavoratore()
                        && q.getIdAzienda() == quota.getIdAzienda()
                        && q.getProvincia().equals(quota.getProvincia())
                        && q.getYear() == quota.getYear()).count() == 0)
                    quoteIscrittiSenzaDuplicati.add(quota);
            }


        return quoteIscrittiSenzaDuplicati;

    }


    @Override
    public List<Iscrizione> findIscrizioniByWorkerId(long id) {

        List<DettaglioQuotaAssociativa> dett = findQuoteByWorkerId(id);

        List<Iscrizione> i = new ArrayList<>();

        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : dett) {

            //calcolo il nome dell''azienda

            Azienda az = retrieveAzienda(dettaglioQuotaAssociativa);
            Lavoratore lav = retrieveLavoratore(dettaglioQuotaAssociativa);

            Iscrizione q = Iscrizione.constructIscrizione(dettaglioQuotaAssociativa, az, lav);
            i.add(q);
        }

        return i;
    }

    private Lavoratore retrieveLavoratore(DettaglioQuotaAssociativa dettaglioQuotaAssociativa) {
        return lavRep.get(dettaglioQuotaAssociativa.getIdLavoratore()).orElse(null);
    }

    private Azienda retrieveAzienda(DettaglioQuotaAssociativa dettaglioQuotaAssociativa) {
        Azienda a = null;

        if (dettaglioQuotaAssociativa.getIdAzienda() > 0){
            a = azRep.get(dettaglioQuotaAssociativa.getIdAzienda()).orElse(null);

        }
        return a;
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
