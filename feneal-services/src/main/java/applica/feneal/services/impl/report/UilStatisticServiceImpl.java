package applica.feneal.services.impl.report;

import applica.feneal.domain.data.core.DocumentTypeRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.report.IscrittiPerContesto;
import applica.feneal.domain.model.core.report.RiepilogoAndamento;
import applica.feneal.domain.model.core.report.RiepilogoIscrittiPerSettore;
import applica.feneal.domain.model.setting.TipoDocumento;
import applica.feneal.services.UilStatisticService;
import applica.feneal.services.utils.UilStatisticsUtils;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by fgran on 22/03/2017.
 */
@Service
public class UilStatisticServiceImpl implements UilStatisticService {

    @Autowired
    private Security sec;

    @Autowired
    private UilStatisticsUtils statisticsUtils;

    @Autowired
    private CategoriaRepository catRep;

    @Autowired
    private DocumentTypeRepository tipoDocRep;

    @Autowired
    private CategoriaRepository secRep;

    @Override
    public RiepilogoIscrittiPerSettore getContatoreIscrittiPerSettore(Long loggedUser, Integer anno) {
        User u = ((User) sec.getLoggedUser());
        //ottengo la lista dei settori per cui devo trovere il num di iscritti
        List<Categoria> settori = secRep.find(null).getRows();

        //se il paramtero è nullo imposto il paramtero di default
        if (anno == null){

            anno = Calendar.getInstance().get(Calendar.YEAR);
        }

        List<IscrittiPerContesto> t = new ArrayList<>();
        for (Categoria settore : settori) {
            IscrittiPerContesto c = new IscrittiPerContesto();
            c.setContesto(settore.getDescription());
            c.setNumIscritti(statisticsUtils.retrieveIscrittiPerSettore(settore, anno, u.getCompany().getLid(), u.getLid() ));
            t.add(c);
        }


        RiepilogoIscrittiPerSettore r = new RiepilogoIscrittiPerSettore();
        r.setIscrittiPerContesto(t);

        return r;

    }

    @Override
    public List<RiepilogoAndamento> getAndamentoIscrittiPerSettore() {

        List<RiepilogoAndamento> riepilogoAndamentos = new ArrayList<>();

        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
        long categoryId = u.getLid();
        Integer[] anni = statisticsUtils.retrieveAnniIscrizioniPerCategoria(companyId, categoryId);
        List<Categoria> settori = secRep.find(null).getRows();



        //per ogni anno recupero gli iscritti in gtutti i settori
        for (Integer anno : anni) {

            for (Categoria settore : settori) {
                RiepilogoAndamento r = new RiepilogoAndamento();

                r.setAnno(anno);
                r.setContesto(settore.getDescription());
                r.setValore(statisticsUtils.retrieveIscrittiPerSettore(settore, anno,companyId, categoryId));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "EDILE"));
                r.setTerritorio("");

                riepilogoAndamentos.add(r);
            }





        }


        return riepilogoAndamentos;

    }

    @Override
    public List<String> getListaMesi() {
        return Arrays.asList("", "Gennaio", "Febbraio", "Marzo",
                "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre",
                "Dicembre"
        );
    }

    @Override
    public List<String> getListaMesiWithoutNull() {
        return Arrays.asList( "Gennaio", "Febbraio", "Marzo",
                "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre",
                "Dicembre"
        );
    }

    @Override
    public Integer getMonthByMonthName(String mese) {
        if (StringUtils.isEmpty(mese))
            return null;


        if (mese.toLowerCase().equals("gennaio"))
            return 0;
        if (mese.toLowerCase().equals("febbraio"))
            return 1;
        if (mese.toLowerCase().equals("marzo"))
            return 2;
        if (mese.toLowerCase().equals("aprile"))
            return 3;
        if (mese.toLowerCase().equals("maggio"))
            return 4;
        if (mese.toLowerCase().equals("giugno"))
            return 5;
        if (mese.toLowerCase().equals("luglio"))
            return 6;
        if (mese.toLowerCase().equals("agosto"))
            return 7;
        if (mese.toLowerCase().equals("settembre"))
            return 8;
        if (mese.toLowerCase().equals("ottobre"))
            return 9;
        if (mese.toLowerCase().equals("novembre"))
            return 10;
        return 11;
    }

    @Override
    public RiepilogoIscrittiPerSettore getContatoreIscrittiPerCategoria(Integer anno, Integer mese) {

        //ottengo la lista delle categorie
        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();


        List<Categoria> categories = catRep.find(LoadRequest.build().filter("categoryType",1)).getRows();

        if (anno == null){

            anno = Calendar.getInstance().get(Calendar.YEAR);
            //mese = Calendar.getInstance().get(Calendar.MONTH);
        }



        List<IscrittiPerContesto> t = new ArrayList<>();
        for (Categoria category : categories) {
            IscrittiPerContesto c = new IscrittiPerContesto();
            c.setContesto(category.getDescription());
            c.setNumIscritti(statisticsUtils.retrieveIscrittiPerCategoria(anno, companyId, category.getLid() ));
            t.add(c);
        }

        RiepilogoIscrittiPerSettore r = new RiepilogoIscrittiPerSettore();
        r.setIscrittiPerContesto(t);




        return r;
    }

    @Override
    public List<RiepilogoAndamento> getAndamentoIscrittiPerCategoria() {
        List<RiepilogoAndamento> riepilogoAndamentos = new ArrayList<>();

        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
        Integer[] anni  = statisticsUtils.retrieveAnniIscrizioni(companyId);
        List<Categoria> categories = catRep.find(LoadRequest.build().filter("categoryType",1)).getRows();

        //per ogni anno recupero gli iscritti in gtutti i settori
        for (Integer anno : anni) {
            for (Categoria category : categories) {
                RiepilogoAndamento r = new RiepilogoAndamento();

                r.setAnno(anno);
                r.setContesto(category.getDescription());
                r.setValore(statisticsUtils.retrieveIscrittiPerCategoria(anno,companyId, category.getLid()));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "EDILE"));
                r.setTerritorio("");

                riepilogoAndamentos.add(r);
            }

//            RiepilogoAndamento r = new RiepilogoAndamento();
//
//            r.setAnno(anno);
//            r.setContesto("FENEAL");
//            r.setValore(anno);//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "EDILE"));
//            r.setTerritorio("");
//
//            riepilogoAndamentos.add(r);
//
//
//            RiepilogoAndamento r1 = new RiepilogoAndamento();
//
//            r1.setAnno(anno);
//            r1.setContesto("UILA");
//            r1.setValore(anno);//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "IMPIANTI FISSI"));
//            r1.setTerritorio("");
//
//            riepilogoAndamentos.add(r1);
//
//            RiepilogoAndamento r11 = new RiepilogoAndamento();
//
//            r11.setAnno(anno);
//            r11.setContesto("UILTUCS");
//            r11.setValore(anno);//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "INPS"));
//            r11.setTerritorio("");
//
//            riepilogoAndamentos.add(r11);

        }


        return riepilogoAndamentos;
    }

    @Override
    public List<RiepilogoAndamento> getAndamentoDeleghe(Integer year,  String settore) {

        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
        long categoryId = u.getLid();

        List<RiepilogoAndamento> riepilogoAndamentos = new ArrayList<>();

        Categoria s11 = null;
        if (!StringUtils.isEmpty(settore)){
            s11 = secRep.find(LoadRequest.build().filter("description", settore)).findFirst().orElse(null);
        }



        if (year == null){
            Integer[] anni = statisticsUtils.retrieveAnniDeleghePerCategoria(companyId, categoryId, s11);


            //per ogni anno recupero gli iscritti in gtutti i settori
            for (Integer anno : anni) {

                RiepilogoAndamento r = new RiepilogoAndamento();

                r.setAnno(anno);
                r.setContesto("Nuove");
                r.setValore(statisticsUtils.retrieveDelegheNuovePerCategoria(anno,null, companyId, categoryId, s11));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "EDILE"));
                r.setTerritorio("");

                riepilogoAndamentos.add(r);


                RiepilogoAndamento r1 = new RiepilogoAndamento();

                r1.setAnno(anno);
                r1.setContesto("Revoche");
                r1.setValore(statisticsUtils.retrieveDeleghePerCategoria(anno,null, companyId, categoryId, 6, s11));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "IMPIANTI FISSI"));
                r1.setTerritorio("");

                riepilogoAndamentos.add(r1);

                RiepilogoAndamento r11 = new RiepilogoAndamento();

                r11.setAnno(anno);
                r11.setContesto("Annullate");
                r11.setValore(statisticsUtils.retrieveDeleghePerCategoria(anno,null, companyId, categoryId, 5, s11));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "INPS"));
                r11.setTerritorio("");

                riepilogoAndamentos.add(r11);

            }


            return riepilogoAndamentos;
        }

        //altrimenti devo valuate i mesi dell'anno...

        List<String> mesi = getListaMesi();
        int i = 0;
        for (String s : mesi) {
            RiepilogoAndamento r = new RiepilogoAndamento();

            r.setAnno(year);
            r.setContesto("Nuove");
            r.setContestoSecondario(s);
            r.setValore(statisticsUtils.retrieveDelegheNuovePerCategoria(year,i, companyId, categoryId, s11));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "EDILE"));
            r.setTerritorio("");

            riepilogoAndamentos.add(r);


            RiepilogoAndamento r1 = new RiepilogoAndamento();

            r1.setAnno(year);
            r1.setContesto("Revoche");
            r1.setContestoSecondario(s);
            r1.setValore(statisticsUtils.retrieveDeleghePerCategoria(year,i, companyId, categoryId, 6, s11));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "IMPIANTI FISSI"));
            r1.setTerritorio("");

            riepilogoAndamentos.add(r1);

            RiepilogoAndamento r11 = new RiepilogoAndamento();

            r11.setAnno(year);
            r11.setContesto("Annullate");
            r11.setContestoSecondario(s);
            r11.setValore(statisticsUtils.retrieveDeleghePerCategoria(year,i, companyId, categoryId, 5, s11));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "INPS"));
            r11.setTerritorio("");

            riepilogoAndamentos.add(r11);
            i++;
        }
        return riepilogoAndamentos;


    }

    @Override
    public List<RiepilogoAndamento> getAndamentoSegnalazioni(Integer year) throws Exception {
        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
        //Categoria category = u.getCategoria().getCategoria();

        List<RiepilogoAndamento> riepilogoAndamentos = new ArrayList<>();
        List<Categoria> tipiCategoria = catRep.find(LoadRequest.build()).getRows();
        //questo flag è necessario al servizio delle notifiche per cercare l
        //le notifiche ricevute o inviate
        //se appartengo ad una categoria allora vorrei vedere tutte le
        //selegnalazioni ricevute
        boolean categoryType = false;
        //if (category.getCategoryType() == TipoCategoriaEnum.Categoria)
            categoryType = true;

        if (year == null){
            Integer[] anni = statisticsUtils.retrieveAnniSegnalazioni(categoryType, u.getLid(),companyId);

            //per ogni anno recupero gli iscritti in gtutti i settori
            for (Integer anno : anni) {

                for (Categoria cat : tipiCategoria) {

                    int numSegnalazioni;
                    if (categoryType)
                        numSegnalazioni = statisticsUtils.retrieveSegnalazioniRicevutePerCategoria(anno, null, companyId,u.getLid(),cat.getLid() );
                    else
                        numSegnalazioni = statisticsUtils.retrieveSegnalazioniEffettuatePerCategoria(anno, null, companyId,u.getLid(),cat.getLid() );

                    if (numSegnalazioni > 0){

                        RiepilogoAndamento r = new RiepilogoAndamento();

                        r.setAnno(anno);
                        r.setContesto(cat.getDescription());
                        r.setValore(numSegnalazioni);
                        r.setTerritorio("");

                        riepilogoAndamentos.add(r);

                    }
                }
            }


            return riepilogoAndamentos;
        }

        //altrimenti devo valuate i mesi dell'anno...

        List<String> mesi = getListaMesiWithoutNull();
        int i = 0;
        for (String s : mesi) {

            for (Categoria cat : tipiCategoria) {

                int numSegnalazioni;
                if (categoryType)
                    numSegnalazioni = statisticsUtils.retrieveSegnalazioniRicevutePerCategoria(year, i, companyId,u.getLid(),cat.getLid() );
                else
                    numSegnalazioni = statisticsUtils.retrieveSegnalazioniEffettuatePerCategoria(year, i, companyId,u.getLid(),cat.getLid() );



                    RiepilogoAndamento r = new RiepilogoAndamento();

                    r.setAnno(year);
                    r.setContesto(cat.getDescription());
                    r.setContestoSecondario(s);
                    r.setValore(numSegnalazioni);
                    r.setTerritorio("");

                    riepilogoAndamentos.add(r);


            }
            i++;
        }
        return riepilogoAndamentos;
    }

    @Override
    public List<RiepilogoAndamento> getAndamentoPrestazioni(Integer year) {

        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
        long categoryId = u.getLid();

        List<RiepilogoAndamento> riepilogoAndamentos = new ArrayList<>();
        List<TipoDocumento> tipiDocumento = tipoDocRep.find(LoadRequest.build()).getRows();

        if (year == null){
            Integer[] anni = statisticsUtils.retrieveAnniPrestazioniPerCategoria(companyId, categoryId);

            //per ogni anno recupero gli iscritti in gtutti i settori
            for (Integer anno : anni) {


                for (TipoDocumento tipoDocumento : tipiDocumento) {
                    RiepilogoAndamento r = new RiepilogoAndamento();

                    r.setAnno(anno);
                    r.setContesto(tipoDocumento.getDescription());
                    r.setValore(statisticsUtils.retrievePrestazioniPerCategoria(anno, null, companyId,categoryId ,  tipoDocumento.getLid()));//utils.getNumIscrittiPerSettoriPerAnnoETerritorio(anno, territorio, "EDILE"));
                    r.setTerritorio("");

                    riepilogoAndamentos.add(r);
                }
            }


            return riepilogoAndamentos;
        }

        //altrimenti devo valuate i mesi dell'anno...

        List<String> mesi = getListaMesiWithoutNull();
        int i = 0;
        for (String s : mesi) {
            for (TipoDocumento tipoDocumento : tipiDocumento) {
                RiepilogoAndamento r = new RiepilogoAndamento();

                r.setAnno(year);
                r.setContesto(tipoDocumento.getDescription());
                r.setContestoSecondario(s);
                r.setValore(statisticsUtils.retrievePrestazioniPerCategoria(year, i, companyId,categoryId ,  tipoDocumento.getLid()));
                r.setTerritorio("");

                riepilogoAndamentos.add(r);

            }
            i++;
        }
        return riepilogoAndamentos;
    }

    @Override
    public RiepilogoIscrittiPerSettore getContatorePrestazioni(Integer year, Integer month) throws Exception {
        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
        long categoryId = u.getLid();
        //ottengo la lista dei settori per cui devo trovere il num di iscritti
        List<TipoDocumento> tipiDocumento = tipoDocRep.find(LoadRequest.build()).getRows();


        //se il paramtero è nullo
        if (year == null){

            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
        }

        List<IscrittiPerContesto> t = new ArrayList<>();
        for (TipoDocumento tipo : tipiDocumento) {
            IscrittiPerContesto c = new IscrittiPerContesto();
            c.setContesto(tipo.getDescription());
            c.setNumIscritti(statisticsUtils.retrieveSegnalazioniEffettuatePerCategoria(year, month, companyId,categoryId ,  tipo.getLid()));
            t.add(c);
        }

        RiepilogoIscrittiPerSettore r = new RiepilogoIscrittiPerSettore();
        r.setIscrittiPerContesto(t);

        return r;
    }

    @Override
    public RiepilogoIscrittiPerSettore getContatoreSegnalazioni(Integer year, Integer month) throws Exception {

        User u = ((User) sec.getLoggedUser());
        long companyId = u.getCompany().getLid();
       // Categoria category = u.getCategoria().getCategoria();
        List<Categoria> tipiCategoria = catRep.find(LoadRequest.build()).getRows();

        boolean categoryType = false;
       // if (category.getCategoryType() == TipoCategoriaEnum.Categoria)
            categoryType = true;

        //se il paramtero è nullo imposto il paramtero di default
        if (year == null){

            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
        }




        List<IscrittiPerContesto> t = new ArrayList<>();
        for (Categoria cat : tipiCategoria) {

            int numSegnalazioni;
            if (categoryType)
                numSegnalazioni = statisticsUtils.retrieveSegnalazioniRicevutePerCategoria(year, month, companyId,u.getLid(),cat.getLid() );
            else
                numSegnalazioni = statisticsUtils.retrieveSegnalazioniEffettuatePerCategoria(year, month, companyId,u.getLid(),cat.getLid() );

            if (numSegnalazioni> 0){
                IscrittiPerContesto c = new IscrittiPerContesto();
                c.setContesto(cat.getDescription());
                c.setNumIscritti(numSegnalazioni);
                t.add(c);
            }

        }

        RiepilogoIscrittiPerSettore r = new RiepilogoIscrittiPerSettore();
        r.setIscrittiPerContesto(t);




        return r;
    }


}
