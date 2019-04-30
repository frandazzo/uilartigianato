package applica.feneal.services.utils;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.utils.Box;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 18/05/2017.
 */
@Component
public class UilStatisticsUtils {

    @Autowired
    private DettaglioQuoteAssociativeRepository dettRep;

    private Integer[] retrieveIntegerArray(String queryString) {

        final Box box = new Box();

        dettRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = dettRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    SQLQuery query = s.createSQLQuery(queryString);


                    List<Object[]> objects = query.list();
                    box.setValue(objects);

                    tx.commit();

                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();

                }
                finally{
                    s.close();

                }
            }
        });

        List<Object[]> result = (List<Object[]>)box.getValue();
        List<Integer> res = new ArrayList<>();
        for (Object s : result) {
            res.add(Integer.parseInt(s.toString()));
        }
        return res.toArray(new Integer[res.size()]);
    }

    private int retrieveInteger(String queryString) {

        final Box box = new Box();

        dettRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = dettRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    SQLQuery query = s.createSQLQuery(queryString);


                    box.setValue(((BigInteger)query.uniqueResult()).intValue());

                    tx.commit();

                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();

                }
                finally{
                    s.close();

                }
            }
        });

        return Integer.parseInt(box.getValue().toString());
    }


    public int retrieveIscrittiPerSettore(Categoria settore, Integer anno, long companyId, long catyegoryId) {

        String annoFine = String.format("%s-12-31", anno);
        String annoInizio = String.format("%s-01-01", anno);

        //dataInizio <= '2017-12-31'
        String dataInizioClause = String.format(" dataInizio <= '%s'", annoFine);
        //dataFine >= '2017-01-01'
        String dataFineClause = String.format(" dataFine >= '%s'", annoInizio);
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", catyegoryId);

        //settore = 'COMMERCIO'
        String settoreClause = String.format(" settore = '%s'", settore.getDescription().replace("'","\\'"));


        String queryStart =
                "SELECT count(distinct idLavoratore) as num FROM fenealweb_dettaglioquote ";


        String queryString = String.format("%s where %s and %s and %s and %s and %s",
                queryStart, companyIdClause, categoryIdClause, settoreClause, dataInizioClause, dataFineClause );

        return retrieveInteger(queryString);
    }


    public Integer[] retrieveAnniIscrizioniPerCategoria(long companyId, long categoryId) {

        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);


        String queryStartForDataFine = "select distinct(year(dataFine)) as years from fenealweb_dettaglioquote ";

        String queryStartForDataInizio = "select distinct(year(dataInizio)) as years from fenealweb_dettaglioquote ";


        String firstQueryString = String.format(" %s where %s and %s ", queryStartForDataFine, companyIdClause, categoryIdClause);
        String secondQueryString = String.format(" %s where %s and %s ", queryStartForDataInizio, companyIdClause, categoryIdClause);

        String queryString = String.format("%s UNION %s ", firstQueryString, secondQueryString);



        return retrieveIntegerArray(queryString);
    }

    public int retrieveIscrittiPerCategoria(Integer anno, long companyId, long categoryId) {
        String annoFine = String.format("%s-12-31", anno);
        String annoInizio = String.format("%s-01-01", anno);

        //dataInizio <= '2017-12-31'
        String dataInizioClause = String.format(" dataInizio <= '%s'", annoFine);
        //dataFine >= '2017-01-01'
        String dataFineClause = String.format(" dataFine >= '%s'", annoInizio);
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);



        String queryStart =
                "SELECT count(distinct idLavoratore) as num FROM fenealweb_dettaglioquote ";


        String queryString = String.format("%s where %s and %s and  %s and %s",
                queryStart, companyIdClause, categoryIdClause,  dataInizioClause, dataFineClause );

        return retrieveInteger(queryString);
    }

    public Integer[] retrieveAnniIscrizioni(long companyId) {
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);



        String queryStartForDataFine = "select distinct(year(dataFine)) as years from fenealweb_dettaglioquote ";

        String queryStartForDataInizio = "select distinct(year(dataInizio)) as years from fenealweb_dettaglioquote ";


        String firstQueryString = String.format(" %s where %s  ", queryStartForDataFine, companyIdClause );
        String secondQueryString = String.format(" %s where %s  ", queryStartForDataInizio, companyIdClause);

        String queryString = String.format("%s UNION %s ", firstQueryString, secondQueryString);



        return retrieveIntegerArray(queryString);
    }

    public int retrievePrestazioniPerCategoria(Integer year, Integer month, long companyId, long categoryId, long tipoDocId) {


        String annoFine = String.format("LAST_DAY('%s-12-01')", year);
        String annoInizio = String.format("%s-01-01", year);



        if (month != null){
            String monthString = (month + 1 < 10)? "0" + String.valueOf(month + 1): String.valueOf(month +1);

            annoFine = String.format("LAST_DAY('%s-%s-01')", year, monthString);
            annoInizio = String.format("%s-%s-01", year, monthString);
        }
//tipoId <= 4
        String tipoDocClause = String.format(" tipoId =  %s", tipoDocId);

        //dataInizio <= '2017-12-31'
        String dataInizioClause = String.format(" data <= %s", annoFine);
        //dataFine >= '2017-01-01'
        String dataFineClause = String.format(" data >= '%s'", annoInizio);
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);



        String queryStart =
                "SELECT count(id) as num FROM fenealweb_documento ";


        String queryString = String.format("%s where %s and %s and %s  and %s and %s",
                queryStart, companyIdClause, categoryIdClause,  dataInizioClause, dataFineClause, tipoDocClause );

        return retrieveInteger(queryString);




    }

    public Integer[] retrieveAnniPrestazioniPerCategoria(long companyId, long categoryId) {
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);


        String queryStartForData = "select distinct(year(data)) as years from fenealweb_documento ";



        String queryString = String.format(" %s where %s and %s ", queryStartForData, companyIdClause, categoryIdClause);



        return retrieveIntegerArray(queryString);
    }

    public Integer[] retrieveAnniDeleghePerCategoria(long companyId, long categoryId, Categoria settore) {
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);


        String settoreClause = "";
        if (settore != null)
            settoreClause = String.format(" and sectorId = '%s'", settore.getLid());

        String queryStartForData = "select distinct(year(documentDate)) as years from fenealweb_delega ";



        String queryString = String.format(" %s where %s and %s %s", queryStartForData, companyIdClause, categoryIdClause, settoreClause);



        return retrieveIntegerArray(queryString);
    }

    public int retrieveDelegheNuovePerCategoria(Integer year, Integer month, long companyId, long categoryId, Categoria settore) {

        String annoFine = String.format("LAST_DAY('%s-12-01')", year);
        String annoInizio = String.format("%s-01-01", year);



        if (month != null){
            String monthString = (month + 1 < 10)? "0" + String.valueOf(month + 1): String.valueOf(month +1);

            annoFine = String.format("LAST_DAY('%s-%s-01')", year, monthString);
            annoInizio = String.format("%s-%s-01", year, monthString);
        }


        //dataInizio <= '2017-12-31'
        String dataInizioClause = String.format(" documentDate <= %s", annoFine);
        //dataFine >= '2017-01-01'
        String dataFineClause = String.format(" documentDate >= '%s'", annoInizio);
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);

        String settoreClause = "";
        if (settore != null)
            settoreClause = String.format(" and sectorId = '%s'", settore.getLid());

        String queryStart =
                "SELECT count(id) as num FROM fenealweb_delega ";


        String queryString = String.format("%s where %s and %s and %s  and %s %s",
                queryStart, companyIdClause, categoryIdClause,  dataInizioClause, dataFineClause, settoreClause );

        return retrieveInteger(queryString);

    }

    public int retrieveDeleghePerCategoria(Integer year, Integer month, long companyId, long categoryId, Integer state, Categoria settore) {

        String annoFine = String.format("LAST_DAY('%s-12-01')", year);
        String annoInizio = String.format("%s-01-01", year);



        if (month != null){
            String monthString = (month + 1 < 10)? "0" + String.valueOf(month + 1): String.valueOf(month +1);

            annoFine = String.format("LAST_DAY('%s-%s-01')", year, monthString);
            annoInizio = String.format("%s-%s-01", year, monthString);
        }

        String dateField = "documentDate";
        switch (state){
            case 1:
                dateField = "documentDate";
            case 2:
                dateField = "sendDate";
            case 3:
                dateField = "acceptDate";
            case 4:
                dateField = "activationDate";
            case 5:
                dateField = "cancelDate";
            case 6:
                dateField = "revokeDate";

            default:
                break;
        }


        //dataInizio <= '2017-12-31'
        String dataInizioClause = String.format(" %s <= %s", dateField, annoFine);
        //dataFine >= '2017-01-01'
        String dataFineClause = String.format(" %s >= '%s'", dateField,annoInizio);
        //companyId = 1
        String companyIdClause = String.format(" companyId = %s", companyId);
        //categoryId = 1
        String categoryIdClause = String.format(" categoryId = %s", categoryId);

        String settoreClause = "";
        if (settore != null)
            settoreClause = String.format(" and sectorId = '%s'", settore.getLid());


        //state = 1
        String stateIdClause = String.format(" state = %s", state);


        String queryStart =
                "SELECT count(id) as num FROM fenealweb_delega ";


        String queryString = String.format("%s where %s and %s and %s  and %s and %s %s",
                queryStart, companyIdClause, categoryIdClause,  dataInizioClause, dataFineClause , stateIdClause, settoreClause);

        return retrieveInteger(queryString);

    }

    public Integer[] retrieveAnniSegnalazioni(boolean categoryType, long categoryId, long companyId) throws Exception {
      throw new Exception("method to remove");
//        String companyIdClause = String.format(" companyId = %s", companyId);
//
//
//
//        String categoryIdClause = "";
//
//        if (categoryType)
//            categoryIdClause = String.format(" recipientCategoryId = %s", categoryId);
//        else
//            categoryIdClause = String.format(" senderCategoryId = %s", categoryId);
//
//
//        String queryStartForData = "select distinct(year(date)) as years from fenealweb_notifications ";
//
//
//
//        String queryString = String.format(" %s where %s and %s ", queryStartForData, companyIdClause, categoryIdClause);
//
//
//
//        return retrieveIntegerArray(queryString);
    }

    public int retrieveSegnalazioniRicevutePerCategoria(Integer year, Integer month, long companyId, long recipientCategoryId, long senderCategoryId) throws Exception {
        throw new Exception("method to remove");

//        String annoFine = String.format("LAST_DAY('%s-12-01')", year);
//        String annoInizio = String.format("%s-01-01", year);
//
//
//
//        if (month != null){
//            String monthString = (month + 1 < 10)? "0" + String.valueOf(month + 1): String.valueOf(month +1);
//
//            annoFine = String.format("LAST_DAY('%s-%s-01')", year, monthString);
//            annoInizio = String.format("%s-%s-01", year, monthString);
//        }
////recipientCategoryId <= 4
//        String recipientCategoryClause = String.format(" recipientCategoryId =  %s", recipientCategoryId);
//
//        //dataInizio <= '2017-12-31'
//        String dataInizioClause = String.format(" date <= %s", annoFine);
//        //dataFine >= '2017-01-01'
//        String dataFineClause = String.format(" date >= '%s'", annoInizio);
//        //companyId = 1
//        String companyIdClause = String.format(" companyId = %s", companyId);
//        //senderCategoryIdClause = 1
//        String senderCategoryIdClause = String.format(" senderCategoryId = %s", senderCategoryId);
//
//
//
//        String queryStart =
//                "SELECT count(id) as num FROM fenealweb_notifications ";
//
//
//        String queryString = String.format("%s where %s and %s and %s  and %s and %s",
//                queryStart, companyIdClause, senderCategoryIdClause,  dataInizioClause, dataFineClause, recipientCategoryClause );
//
//        return retrieveInteger(queryString);


    }

    public int retrieveSegnalazioniEffettuatePerCategoria(Integer year, Integer month, long companyId, long senderCategoryId, long recipientCategoryId) throws Exception {

        throw new Exception("method to remove");

//        String annoFine = String.format("LAST_DAY('%s-12-01')", year);
//        String annoInizio = String.format("%s-01-01", year);
//
//
//
//        if (month != null){
//            String monthString = (month + 1 < 10)? "0" + String.valueOf(month + 1): String.valueOf(month +1);
//
//            annoFine = String.format("LAST_DAY('%s-%s-01')", year, monthString);
//            annoInizio = String.format("%s-%s-01", year, monthString);
//        }
////recipientCategoryId <= 4
//        String recipientCategoryClause = String.format(" recipientCategoryId =  %s", recipientCategoryId);
//
//        //dataInizio <= '2017-12-31'
//        String dataInizioClause = String.format(" date <= %s", annoFine);
//        //dataFine >= '2017-01-01'
//        String dataFineClause = String.format(" date >= '%s'", annoInizio);
//        //companyId = 1
//        String companyIdClause = String.format(" companyId = %s", companyId);
//        //senderCategoryIdClause = 1
//        String senderCategoryIdClause = String.format(" senderCategoryId = %s", senderCategoryId);
//
//
//
//        String queryStart =
//                "SELECT count(id) as num FROM fenealweb_notifications ";
//
//
//        String queryString = String.format("%s where %s and %s and %s  and %s and %s",
//                queryStart, companyIdClause, senderCategoryIdClause,  dataInizioClause, dataFineClause, recipientCategoryClause );
//
//        return retrieveInteger(queryString);

    }
}
