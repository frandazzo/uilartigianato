package applica.feneal.services.utils;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.IscrittiDescriptorItem;
import applica.feneal.domain.model.analisi.LegendaFactory;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.geo.Region;
import applica.feneal.domain.utils.Box;
import applica.feneal.services.GeoService;
import applica.framework.LoadRequest;
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

@Component
public class StatisticsDelegheUtils {
    @Autowired
    private DettaglioQuoteAssociativeRepository iscRep;


    @Autowired
    private Security sec;

    @Autowired
    private GeoService geoSvc;

    @Autowired
    private CategoriaRepository catRep;

    private Categoria findCategoriaByName(String categoryName){
        LoadRequest req = LoadRequest.build().filter("description", categoryName);
        return catRep.find(req).findFirst().orElse(null);
    }
    private Region findRegionByName(String regionName){
        return geoSvc.getREgionByName(regionName);
    }



    public List<Integer> getListaAnniIscrizioni(String regione, String categoria){

        Region region = findRegionByName(regione);
        Categoria category = findCategoriaByName(categoria);

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
                        regionWhere = " and r.ID = " + String.valueOf(region.getIid());

                    else if (!StringUtils.isEmpty(categoria))
                        categoryWhere = " and c.Id = " + String.valueOf(category.getLid());

                    String sqlQuery = "select Anno from \n" +
                            "(select  distinct Year( documentDate) as Anno from fenealweb_delega d \n" +
                            "inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state < 5" + categoryWhere + regionWhere + " )\n" +
                            "as a order by Anno  desc";
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
                        regionWhere = " and r.DESCRIZIONE like '" + regione.replace("'", "''") + "' ";

                    else if (!StringUtils.isEmpty(categoria))
                        categoryWhere = " and c.description like '" + categoria + "' ";


                    String sqlQuery = "select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5 " + regionWhere + categoryWhere;


                    SQLQuery query = null;
                    query= s.createSQLQuery(sqlQuery
                    )
                            .addScalar("Regione")
                            .addScalar("Provincia")
                            .addScalar("Anno")
                            .addScalar("Settore")
                            .addScalar("Nazionalita")
                            .addScalar("Id_Delega");





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
                        regionWhere = " and a.Regione like '" + regionId.replace("'", "''") + "' ";


                    String sqlQueryProvince = "select a.Settore as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno + regionWhere + "\n" +
                            "group by a.Settore";




                    SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");


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




                    String sqlQueryProvince = "select a.Settore as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno + regionWhere + "\n" +
                            "group by a.Settore";





                    SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");


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



                    String sqlQueryProvince = "select a.Provincia as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno + regionWhere + "\n" +
                            "group by a.Provincia";

                    String sqlQueryRegion = "select a.Regione as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno  + "\n" +
                            "group by a.Regione";


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









                    String sqlQueryProvince = "select a.Provincia as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno + regionWhere + "\n" +
                            "group by a.Provincia";;


                    SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");

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
                        regionWhere = " and r.DESCRIZIONE like '" + regionId.replace("'", "''") + "' ";

                    else if (!StringUtils.isEmpty(categoryId))
                        categoryWhere = " and c.description like '" + categoryId + "' ";


                    String sqlQueryProvince = "select a.Provincia as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno + categoryWhere + regionWhere  + "\n" +
                            "group by a.Provincia";

                    String sqlQueryRegion = "select a.Regione as label, Count(a.Id_Delega) as total  from \n" +
                            "(select \n" +
                            "r.DESCRIZIONE as Regione, \n" +
                            "p.DESCRIZIONE as Provincia, \n" +
                            "Year(d.documentDate) as Anno, \n" +
                            "c.description as Settore, \n" +
                            "l.nationality as Nazionalita, \n" +
                            "d.Id as Id_Delega\n" +
                            "   from fenealweb_delega d \n" +
                            "   inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "   inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "   inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "   inner join tb_regioni r on r.ID = p.ID_TB_REGIONI where d.state <5) as a\n" +
                            "where anno = " +  anno + categoryWhere   + "\n" +
                            "group by a.Regione";






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

