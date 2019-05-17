package applica.feneal.services.impl.analisys.bilateralita;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.IscrittiDescriptorItem;
import applica.feneal.domain.model.analisi.LegendaFactory;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.geo.Region;
import applica.feneal.domain.utils.Box;
import applica.feneal.services.GeoService;
import applica.feneal.services.StatisticsDelegheAbstractUtils;
import applica.framework.LoadRequest;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

@Component
public class StatisticsDelegheAbstractUtilsImpl implements StatisticsDelegheAbstractUtils {

    @Autowired
    private GeoService geoSvc;

    @Autowired
    private CategoriaRepository catRep;



    @Override
    public List<Integer> getListaAnniIscrizioni(String regione, String categoria, String tipoEntita) throws Exception {
        //questa è la lista degli anni da testare per verificare la presenza di una delega bilateralità
        Integer[] all = new Integer[]{2010,2011,2012,2013,2014,2015,2016,2017,2018,2019,2020, 2021,2022,2023,2024,2025};


        List<Integer> result = new ArrayList<>();

        for (Integer anno : all) {
            if (existRecord(regione, categoria,tipoEntita,anno))
                result.add(anno);
        }

        Collections.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2)*-1;
            }
        });

        return result;



    }

    @Override
    public IscrittiDescriptor getIscrittiPerCategoria_UtenteRegionale(int anno, String regionId, String tipoEntita) throws Exception {
        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());

        catRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = catRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    executeQueryAndMaterializeData(s, regionId, tipoEntita, anno, box);

                    tx.commit();
                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                    box.setException(e);
                }
                finally{
                    s.close();
                }
            }
        });

        if (box.getException() != null)
            throw  box.getException();
        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());
        return result;

    }

    private void executeQueryAndMaterializeData(Session s, String regionId, String tipoEntita, int anno, Box box) {
        String regionWhere = StringUtils.isEmpty(regionId) ? "" : " and r.DESCRIZIONE like '" + regionId.replace("'", "''") + "' ";

        String sqlQueryProvince = "        select a.categoria as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,\n" +
                "                p.DESCRIZIONE as provincia,\n" +
                "                c.description as categoria,\n" +
                "                r.DESCRIZIONE as regione\n" +
                "                from "+ tipoEntita +" d\n" +
                "\n" +
                "                inner join tb_provincie p on d.provinceId = p.ID\n" +
                "                inner join uilweb_categorias c on d.sectorId = c.id\n" +
                "                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
                //"                where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,CURDATE())) "+ regionWhere +") a\n" +


            "                   where \n" +
                "                ((d.documentDate <= MAKEDATE("+ String.valueOf(anno + 1) +",1) and COALESCE(d.cancelDate) is null)\n" +
                "                or\n" +
                "                (d.documentDate <= MAKEDATE("+ String.valueOf(anno + 1) +",1) and COALESCE(d.cancelDate) >=  MAKEDATE("+ String.valueOf(anno) +",1)   )) \n" +
                "                \n" + regionWhere +
                "                )a" +



                "        group by a.categoria;";



        if (tipoEntita.equals("fenealweb_delega")){
            sqlQueryProvince = "        select a.categoria as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,\n" +
                    "                p.DESCRIZIONE as provincia,\n" +
                    "                c.description as categoria,\n" +
                    "                r.DESCRIZIONE as regione\n" +
                    "                from fenealweb_delega d\n" +
                    "\n" +
                    "                inner join tb_provincie p on d.provinceId = p.ID\n" +
                    "                inner join uilweb_categorias c on d.sectorId = c.id\n" +
                    "                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
                  //  "                where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,d.revokeDate, CURDATE())) "+ regionWhere +") a\n" +



                    "                   where \n" +
                    "                ((d.documentDate <= MAKEDATE("+ String.valueOf(anno + 1) +",1) and COALESCE(d.cancelDate,d.revokeDate) is null)\n" +
                    "                or\n" +
                    "                (d.documentDate <= MAKEDATE("+ String.valueOf(anno + 1) +",1) and COALESCE(d.cancelDate,d.revokeDate) >=  MAKEDATE("+ String.valueOf(anno) +",1)   )) \n" +
                    "                \n" + regionWhere +
                    "                )a" +



                    "        group by a.categoria;";
        }



        SQLQuery query = s.createSQLQuery(sqlQueryProvince).addScalar("label").addScalar("total");
        extractDescriptios(query, box);
    }


    private void executeQueryAndMaterializeData_QueryAreaGeografica(Session s, String regionId, String tipoEntita, int anno, Box box) {
        String regionWhere = StringUtils.isEmpty(regionId) ? "" : " and r.DESCRIZIONE like '" + regionId.replace("'", "''") + "' ";
        String revoke = tipoEntita.equals("fenealweb_delega")? " ,d.revokeDate ": "";

        String sqlQueryProvince = "        select a.provincia as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,\n" +
                "                p.DESCRIZIONE as provincia,\n" +
                "                c.description as categoria,\n" +
                "                r.DESCRIZIONE as regione\n" +
                "                from "+ tipoEntita +" d\n" +
                "\n" +
                "                inner join tb_provincie p on d.provinceId = p.ID\n" +
                "                inner join uilweb_categorias c on d.sectorId = c.id\n" +
                "                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
               // "                where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,"+revoke+"CURDATE())) "+ regionWhere +") a\n" +


                "                   where \n" +
                "                ((d.documentDate <= MAKEDATE("+ String.valueOf(anno +1) +",1) and COALESCE(d.cancelDate"+revoke+") is null)\n" +
                "                or\n" +
                "                (d.documentDate <= MAKEDATE("+ String.valueOf(anno +1) +",1) and COALESCE(d.cancelDate"+revoke+") >=  MAKEDATE("+ String.valueOf(anno) +",1)     )) \n" +
                "                \n" + regionWhere +
                "                )a" +

                "        group by a.provincia;";



        String sqlQueryRegion = "        select a.regione as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,\n" +
                "                p.DESCRIZIONE as provincia,\n" +
                "                c.description as categoria,\n" +
                "                r.DESCRIZIONE as regione\n" +
                "                from "+ tipoEntita +" d\n" +
                "\n" +
                "                inner join tb_provincie p on d.provinceId = p.ID\n" +
                "                inner join uilweb_categorias c on d.sectorId = c.id\n" +
                "                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
               // "                where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,"+revoke+"CURDATE())) ) a\n" +

                "                   where \n" +
                "                ((d.documentDate <= MAKEDATE("+ String.valueOf(anno+1) +",1) and COALESCE(d.cancelDate"+revoke+") is null)\n" +
                "                or\n" +
                "                (d.documentDate <= MAKEDATE("+ String.valueOf(anno +1) +",1) and COALESCE(d.cancelDate"+revoke+")  >=  MAKEDATE("+ String.valueOf(anno) +",1)     )) \n" +
                "                )a" +

                "        group by a.regione;";

        executeAndMaterialize(s, regionId, box, sqlQueryProvince, sqlQueryRegion);
    }

    private void executeAndMaterialize(Session s, String regionId, Box box, String sqlQueryProvince, String sqlQueryRegion) {
        String querySql = StringUtils.isEmpty(regionId) ? sqlQueryRegion : sqlQueryProvince;

        SQLQuery query = s.createSQLQuery(querySql).addScalar("label").addScalar("total");
        extractDescriptios(query, box);
    }


    private void executeQueryAndMaterializeData_QueryCategoria(Session s, String regionId, String tipoEntita, int anno, Box box, String categoria) {
        String regionWhere = StringUtils.isEmpty(regionId) ? "" : " and r.DESCRIZIONE like '" + regionId.replace("'", "''") + "' ";
        String categoriaWhere = StringUtils.isEmpty(categoria)? "": " and c.description like '" + categoria + "' ";


        String revoke = tipoEntita.equals("fenealweb_delega")? " ,d.revokeDate ": "";


        String sqlQueryProvince = "select a.provincia as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,\n" +
                "                p.DESCRIZIONE as provincia,\n" +
                "                c.description as categoria,\n" +
                "                r.DESCRIZIONE as regione\n" +
                "                from "+ tipoEntita +" d\n" +
                "\n" +
                "                inner join tb_provincie p on d.provinceId = p.ID\n" +
                "                inner join uilweb_categorias c on d.sectorId = c.id\n" +
                "                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
             //   "                where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,"+ revoke+"CURDATE())) "+ regionWhere + categoriaWhere +") a\n" +
                "                   where \n" +
                "                ((d.documentDate <= MAKEDATE("+ String.valueOf(anno+1) +",1) and COALESCE(d.cancelDate"+revoke+") is null)\n" +
                "                or\n" +
                "                (d.documentDate <= MAKEDATE("+ String.valueOf(anno+1) +",1) and COALESCE(d.cancelDate"+revoke+")     >=  MAKEDATE("+ String.valueOf(anno) +",1)    ) ) \n" +
                "                \n" + regionWhere + categoriaWhere +
                "                )a" +


                "        group by a.provincia;";



        String sqlQueryRegion = "select a.regione as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,\n" +
                "                p.DESCRIZIONE as provincia,\n" +
                "                c.description as categoria,\n" +
                "                r.DESCRIZIONE as regione\n" +
                "                from "+ tipoEntita +" d\n" +
                "\n" +
                "                inner join tb_provincie p on d.provinceId = p.ID\n" +
                "                inner join uilweb_categorias c on d.sectorId = c.id\n" +
                "                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
               // "                where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,"+revoke +"CURDATE())) "+ categoriaWhere +") a\n" +

                "                   where \n" +
                "                ((d.documentDate <= MAKEDATE("+ String.valueOf(anno+1) +",1) and COALESCE(d.cancelDate"+revoke+") is null)\n" +
                "                or\n" +
                "                (d.documentDate <= MAKEDATE("+ String.valueOf(anno+1) +",1) and COALESCE(d.cancelDate"+revoke+")  >=  MAKEDATE("+ String.valueOf(anno) +",1)     )) \n" +
                "                \n" +  categoriaWhere +
                "                )a" +



                "        group by a.regione;";

        executeAndMaterialize(s, regionId, box, sqlQueryProvince, sqlQueryRegion);
    }


    @Override
    public IscrittiDescriptor getIscrittiPerCategoria_UtenteNazionale(int anno, String regionId, String tipoEntita) throws Exception {


//        select a.categoria as label, count(a.id) as total from (select d.id as id,  r.id as idRegion,
//                p.DESCRIZIONE as provincia,
//                c.description as categoria,
//                r.DESCRIZIONE as regione
//                from fenealweb_delega_bilateralita d
//
//                inner join tb_provincie p on d.provinceId = p.ID
//                inner join uilweb_categorias c on d.sectorId = c.id
//                inner join tb_regioni r on r.ID = p.ID_TB_REGIONI
//                where (MAKEDATE(2017,1) between d.documentDate and COALESCE(d.cancelDate,CURDATE())) and r.id = 10) a
//        group by a.categoria;

        return getIscrittiPerCategoria_UtenteRegionale(anno,regionId,tipoEntita);

    }

    @Override
    public IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteRegionale(int anno, String regionId, String tipoEntita) throws Exception {



        return getIscrittiPerAreaGeografica_UtenteNazionale(anno,regionId, tipoEntita);


    }

    @Override
    public IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteNazionale(int anno, String regionId, String tipoEntita) throws Exception {


        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());

        catRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = catRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    executeQueryAndMaterializeData_QueryAreaGeografica(s, regionId, tipoEntita, anno, box);

                    tx.commit();
                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                    box.setException(e);
                }
                finally{
                    s.close();
                }
            }
        });

        if (box.getException() != null)
            throw  box.getException();
        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());
        return result;





    }

    @Override
    public IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteCategoria(int anno, String regionId, String categoria, String tipoEntita) throws Exception {


        final Box box = new Box();
        final IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(LegendaFactory.constructLegenda().getItems());

        catRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = catRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();

                    executeQueryAndMaterializeData_QueryCategoria(s, regionId, tipoEntita, anno, box, categoria);

                    tx.commit();
                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                    box.setException(e);
                }
                finally{
                    s.close();
                }
            }
        });

        if (box.getException() != null)
            throw  box.getException();
        result.setIscritti((List<IscrittiDescriptorItem>)box.getValue());
        return result;




    }

    @Override
    public List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria, String tipoEntita) {
        final Box box = new Box();
        final List<PivotanalisysData> result = new ArrayList<>();



        catRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = catRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();
                    String categoryWhere = "";
                    String regionWhere = "";


                    if (!StringUtils.isEmpty(regione))
                        regionWhere = " r.DESCRIZIONE like '" + regione.replace("'", "''") + "' ";

                    else if (!StringUtils.isEmpty(categoria))
                        categoryWhere = " c.description like '" + categoria + "' ";

                    String operatoreAnd = !StringUtils.isEmpty(regione) && !StringUtils.isEmpty(categoria)? " and " : "";
                    String where = !StringUtils.isEmpty(regione) || !StringUtils.isEmpty(categoria)? " where " : "";

                    String revoke = tipoEntita.equals("fenealweb_delega")? " d.revokeDate, ": "";
                    String sqlQuery = "select r.DESCRIZIONE as Regione, p.DESCRIZIONE as Provincia, y.Anno as Anno,\n" +
                            "c.description as Settore, d.id as Id_Delega\n" +
                            " from "+ tipoEntita +" d\n" +
                            " INNER JOIN\n" +
                            " (\n" +
                            " select (t*2010+u+1)  Anno from\n" +
                            "(select 1 t ) A,\n" +
                            "(select 0 u union select 1 union select 2 union select 3 union select 4 union\n" +
                            "select 5 union select 6 union select 7 union select 8 union select 9 \n" +
                            "union select 10 union select 11 union select 12 union select 13 union select 14) B\n" +
                            "order by Anno\n" +
                            ") y\n" +
                            "ON y.anno BETWEEN year(d.documentDate) AND COALESCE(year(d.cancelDate),"+revoke+"year(CURDATE()))\n" +
                            "inner join fenealweb_lavoratore l on d.workerId = l.id\n" +
                            "inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "inner join uilweb_categorias c on d.sectorId = c.id \n" +
                            "inner join tb_regioni r on r.ID = p.ID_TB_REGIONI " + where + regionWhere + operatoreAnd + categoryWhere;


                    SQLQuery query = null;
                    query= s.createSQLQuery(sqlQuery
                    )
                            .addScalar("Regione")
                            .addScalar("Provincia")
                            .addScalar("Anno")
                            .addScalar("Settore")
                            .addScalar("Id_Delega");

                    List<Object[]> objects = query.list();
                    List<PivotanalisysData> a = new ArrayList<>();
                    for (Object[] object : objects) {

                        PivotanalisysData v = new PivotanalisysData();
                        v.setRegione((String)object[0]);
                        v.setProvincia((String)object[1]);
                        v.setAnno(((BigInteger)object[2]).intValue());
                        v.setSettore((String)object[3]);
                        v.setNazionalita("");
                        v.setId_Lavoratore(((BigInteger)object[4]).longValue());

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

    private void extractDescriptios(SQLQuery query, Box box) {
        List<Object[]> objects = query.list();
        List<IscrittiDescriptorItem> a = new ArrayList<>();
        for (Object[] object : objects) {

            IscrittiDescriptorItem v = new IscrittiDescriptorItem();

            v.setLabel((String)object[0]);
            v.setTotal(((BigInteger)object[1]).intValue());


            a.add(v);
        }
        box.setValue(a);
    }


    private  boolean existRecord(String regione, String categoria, String tipoEntita, int anno) throws Exception {

        Region region = geoSvc.getREgionByName(regione);
        Categoria category = catRep.find(LoadRequest.build()
                .filter("description", categoria))
                .findFirst().orElse(null);

        final Box box = new Box();




        catRep.executeCommand(new Command() {
            @Override
            public void execute() {

                Session s = catRep.getSession();
                Transaction tx = null;
                try{

                    tx = s.beginTransaction();




//                    select d.*,
//                    p.DESCRIZIONE as provincia,
//                            c.description as categoria,
//                    r.DESCRIZIONE as regione
//                    from fenealweb_delega_bilateralita d
//
//                    inner join tb_provincie p on d.provinceId = p.ID
//                    inner join uilweb_categorias c on d.sectorId = c.id
//                    inner join tb_regioni r on r.ID = p.ID_TB_REGIONI
//                    where (MAKEDATE(2016,1) between d.documentDate and COALESCE(d.cancelDate,CURDATE())) and p.id = 2;



                    String regionWhere = StringUtils.isEmpty(regione) ? "":" and r.ID = " + String.valueOf(region.getIid());
                    String categoryWhere = StringUtils.isEmpty(categoria) ? "": " and c.Id = " + String.valueOf(category.getLid());




                    String sqlQuery = "select count(*) as count from "+ tipoEntita + " d\n" +
                            "                    inner join tb_provincie p on d.provinceId = p.ID\n" +
                            "                    inner join uilweb_categorias c on d.sectorId = c.id\n" +
                            "                    inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
                            "                    where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,CURDATE())) " +
                            regionWhere + categoryWhere;

                    if (tipoEntita.equals("fenealweb_delega")){
                        sqlQuery = "select count(*) as count from fenealweb_delega d\n" +
                                "                    inner join tb_provincie p on d.provinceId = p.ID\n" +
                                "                    inner join uilweb_categorias c on d.sectorId = c.id\n" +
                                "                    inner join tb_regioni r on r.ID = p.ID_TB_REGIONI\n" +
                                "                    where (MAKEDATE("+ String.valueOf(anno) +",1) between d.documentDate and COALESCE(d.cancelDate,d.revokeDate,CURDATE())) " +
                                regionWhere + categoryWhere;
                    }


                    SQLQuery query = query= s.createSQLQuery(sqlQuery).addScalar("count");



                    Object objects = query.uniqueResult();
                    box.setValue(objects);

                    tx.commit();

                }
                catch(Exception e){
                    e.printStackTrace();
                    tx.rollback();
                    box.setException(e);
                }
                finally{
                    s.close();

                }



            }
        });

        if (box.getException()!= null)
            throw  box.getException();
        return ((BigInteger) box.getValue()).longValue()  > 0 ? true : false;

    }



}
