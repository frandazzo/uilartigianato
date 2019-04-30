package applica.feneal.services.utils;


import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.IscrittiDescriptorItem;
import applica.feneal.domain.model.analisi.LegendaFactory;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.domain.utils.Box;
import applica.framework.security.Security;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 11/06/2016.
 */
@Component
public class StatisticsUtils {
    @Autowired
    private DettaglioQuoteAssociativeRepository iscRep;


    @Autowired
    private Security sec;




    public  List<Integer> getListaAnniIscrizioni(String regione, String categoria){

        final Box box = new Box();

        final User u = ((User) sec.getLoggedUser());


        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    String categoryWhere = "";
                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regione))
                        regionWhere = " where Regione like '" + regione.replace("'", "''") + "' ";

                    else if (!StringUtils.isEmpty(categoria))
                        categoryWhere = " where Settore like '" + categoria + "' ";



                    String sqlQuery = "select Anno from (select  distinct Year(dataFine) as Anno from fenealweb_dettaglioquote\n" +
                            categoryWhere + regionWhere + "\n" +
                            "union\n" +

                            "select  distinct Year(dataInizio) as Anno from fenealweb_dettaglioquote " +
                            categoryWhere + regionWhere +") as a order by Anno desc\n" ;


                    SQLQuery query = query= s.createSQLQuery(sqlQuery).addScalar("Anno");



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
            res.add(Integer.parseInt(String.valueOf(s)));
        }
        return res;


    }

    public List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria) {



        final Box box = new Box();
        final List<PivotanalisysData> result = new ArrayList<>();



        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();
                    String categoryWhere = "";
                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regione))
                        regionWhere = " where Regione like '" + regione.replace("'", "''") + "' ";

                    else if (!StringUtils.isEmpty(categoria))
                        categoryWhere = " where Settore like '" + categoria + "' ";


                    String sqlQuery = "select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            categoryWhere + regionWhere +
                            " group by d.companyId, Anno, provincia, idLavoratore " +      "\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia, Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            categoryWhere + regionWhere +
                            " group by d.companyId, Anno, provincia, idLavoratore  " +  "\n" ;


                    SQLQuery query = null;
                    query= s.createSQLQuery(sqlQuery
                    )
                            .addScalar("Regione")
                            .addScalar("Provincia")
                            .addScalar("Anno")
                            .addScalar("Settore")
                            .addScalar("Nazionalita")
                            .addScalar("Id_Lavoratore");





                    List<Object[]> objects = query.list();
                    List<PivotanalisysData> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        PivotanalisysData v = new PivotanalisysData();

                        v.setRegione((String)object[0]);
                        v.setProvincia((String)object[1]);

                        v.setAnno(((BigInteger)object[2]).intValue());
                        v.setSettore((String)object[3]);

                        v.setNazionalita((String)object[4]);
                        v.setId_Lavoratore(((BigInteger)object[5]).longValue());

                        a.add(v);
                    }
                    box.setValue(a);

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

        return ((List<PivotanalisysData>)box.getValue());

    }




    public IscrittiDescriptor getIscrittiPerCategoria_UtenteNazionale(int anno, String regionId){

        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());


        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();



                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regionId))
                        regionWhere = " and Regione like '" + regionId.replace("'", "''") + "' ";


                    String sqlQueryProvince = "select a.Settore as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            "where Anno = " + anno +  regionWhere +
                            " group by Settore;";




                    SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");





//                    SQLQuery query = null;
//                    if (!StringUtils.isEmpty((regionId))){
//                        query= s.createSQLQuery(
//                                "select nomeCategoria as label, count(Id_Lavoratore) as total from Iscrizioni where anno=" + anno +" and  Id_Regione=" + regionId +
//                                        " GROUP BY nomeCategoria").addScalar("label").addScalar("total");
//
//                    }
//                    else{
//                        query= s.createSQLQuery(
//                                "select nomeCategoria as label, count(Id_Lavoratore) as total from Iscrizioni where anno=" + anno   +
//                                        " GROUP BY nomeCategoria").addScalar("label").addScalar("total");
//                    }



                    List<Object[]> objects = query.list();
                    List<IscrittiDescriptorItem> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        IscrittiDescriptorItem v = new IscrittiDescriptorItem();

                        v.setLabel((String)object[0]);
                        v.setTotal(((BigInteger)object[1]).intValue());


                        a.add(v);
                    }
                    box.setValue(a);

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

        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());

        return result;

    }

    public IscrittiDescriptor getIscrittiPerCategoria_UtenteRegionale(int anno, String regionId){

        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());


        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();


                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regionId))
                        regionWhere = " and Regione like '" + regionId.replace("'", "''") + "' ";




                    String sqlQueryProvince = "select a.Settore as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            "where Anno = " + anno + regionWhere +
                            " group by Settore;";


                    SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");





//
//                    SQLQuery query = s.createSQLQuery(
//                            "select nomeCategoria as label, count(Id_Lavoratore) as total from Iscrizioni where anno=" + anno +" and  Id_Regione=" + regionId +
//                                    " GROUP BY nomeCategoria").addScalar("label").addScalar("total");


                    List<Object[]> objects = query.list();
                    List<IscrittiDescriptorItem> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        IscrittiDescriptorItem v = new IscrittiDescriptorItem();

                        v.setLabel((String)object[0]);
                        v.setTotal(((BigInteger)object[1]).intValue());



                        a.add(v);
                    }
                    box.setValue(a);

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

        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());

        return result;

    }

    public IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteNazionale(int anno, String regionId){

        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());


        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regionId))
                        regionWhere = " and Regione like '" + regionId.replace("'", "''") + "' ";


                    String sqlQueryProvince = "select a.Provincia as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            "where Anno = " + anno +  regionWhere +
                            " group by Provincia;";

                    String sqlQueryRegion = "select a.Regione as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            "where Anno = " + anno +
                            " group by Regione;";


                    SQLQuery query = null;
                    if (StringUtils.isEmpty(regionId))
                        query = s.createSQLQuery(sqlQueryRegion).addScalar("label").addScalar("total");
                    else
                        query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");



//                    SQLQuery query = null;
//                    if (!StringUtils.isEmpty(regionId)){
//                        query= s.createSQLQuery(
//                                "select nomeProvincia as label, count(Id_Lavoratore) as total from Iscrizioni where anno=" + anno +" and  Id_Regione=" + regionId +
//                                        " GROUP BY nomeProvincia").addScalar("label").addScalar("total");
//
//                    }
//                    else{
//                        query= s.createSQLQuery(
//                                "select nomeRegione as label, count(Id_Lavoratore) as total from Iscrizioni where anno=" + anno   +
//                                        " GROUP BY nomeRegione").addScalar("label").addScalar("total");
//                    }



                    List<Object[]> objects = query.list();
                    List<IscrittiDescriptorItem> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        IscrittiDescriptorItem v = new IscrittiDescriptorItem();

                        v.setLabel((String)object[0]);
                        v.setTotal(((BigInteger)object[1]).intValue());


                        a.add(v);
                    }
                    box.setValue(a);

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

        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());

        return result;

    }

    public IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteRegionale(int anno, String regionId){

        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());


        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();


                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regionId))
                        regionWhere = " and Regione like '" + regionId.replace("'", "''") + "' ";




                    String sqlQueryProvince = "select a.Provincia as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            " where Anno = " + anno + regionWhere +
                            " group by Provincia;";


                    SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");

//                    SQLQuery query = null;
//                    query= s.createSQLQuery(
//                            "select nomeProvincia as label, count(Id_Lavoratore) as total from Iscrizioni where anno=" + anno +" and  Id_Regione=" + regionId +
//                                    " GROUP BY nomeProvincia").addScalar("label").addScalar("total");
//




                    List<Object[]> objects = query.list();
                    List<IscrittiDescriptorItem> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        IscrittiDescriptorItem v = new IscrittiDescriptorItem();

                        v.setLabel((String)object[0]);
                        v.setTotal(((BigInteger)object[1]).intValue());



                        a.add(v);
                    }
                    box.setValue(a);

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

        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());

        return result;

    }


    public IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteCategoria(int anno, String regionId, String categoryId){

        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());


        iscRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = iscRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    String categoryWhere = "";
                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regionId))
                        regionWhere = " and Regione like '" + regionId.replace("'", "''") + "' ";

                    else if (!StringUtils.isEmpty(categoryId))
                        categoryWhere = " and Settore like '" + categoryId + "' ";





                    String sqlQueryProvince = "select a.Provincia as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            "where Anno = " + anno + categoryWhere + regionWhere +
                            "group by Provincia;";

                    String sqlQueryRegion = "select a.Regione as label, Count(a.Id_Lavoratore) as total  from " +
                            "(select  Regione, Provincia, Year(dataFine) as Anno, Settore , l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore\n" +
                            "\n" +
                            "union\n" +
                            "\n" +
                            "select  Regione,  Provincia , Year(dataInizio) as Anno, Settore, l.nationality as Nazionalita, idLavoratore as Id_Lavoratore\n" +
                            "from fenealweb_dettaglioquote d inner join fenealweb_lavoratore l on idLavoratore = l.id\n" +
                            "group by d.companyId, Anno, provincia, idLavoratore) as a " +
                            "where Anno = " + anno + categoryWhere  +
                            "group by Regione;";


                    SQLQuery query = null;
                    if (StringUtils.isEmpty(regionId))
                        query = s.createSQLQuery(sqlQueryRegion).addScalar("label").addScalar("total");
                    else
                        query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");




                    List<Object[]> objects = query.list();
                    List<IscrittiDescriptorItem> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        IscrittiDescriptorItem v = new IscrittiDescriptorItem();

                        v.setLabel((String)object[0]);
                        v.setTotal(((BigInteger)object[1]).intValue());



                        a.add(v);
                    }
                    box.setValue(a);

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

        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());

        return result;

    }


}
