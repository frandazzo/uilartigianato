package applica.feneal.services.impl.analisys.iscritti;


import applica.feneal.domain.model.User;
import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.services.GeoService;
import applica.feneal.services.StatisticsIscrittiService;
import applica.feneal.services.StatisticsIscrittiUtils;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fgran on 01/05/2016.
 */
@Service
public class StatisticsIscrittiServiceImpl implements StatisticsIscrittiService {

    @Autowired
    private Security sec;

    @Autowired
    private StatisticsIscrittiUtils utils;

    @Autowired
    private GeoService geo;



    @Override
    public Integer[] getAnniIscrizioni() {

        List<Integer> a = null;
        User u = ((User) sec.getLoggedUser());



        if (u.getCompany().getTipoConfederazione() == 1){
            //sono un regionale
            a= utils.getListaAnniIscrizioni(u.getCompany().getDescription(), "");

        }else if (u.getCompany().getTipoConfederazione() == 2){
            //sono un nazionale
            a= utils.getListaAnniIscrizioni("","");

        }else{
            a= utils.getListaAnniIscrizioni("", u.getCategory().getDescription());

        }

        Integer[] a1 = new Integer[a.size()];

        a1 = a.toArray(a1);


        return a1;
    }

    @Override
    public IscrittiDescriptor getIscrittiPerCategoria(int anno, String region) {

        //devo verificare se si tratta di una utenza nazionale oppure regionale
        //la categoria non puo fare questa query (è interessata solo all'area geografica)
        User u = ((User) sec.getLoggedUser());
        if (u.getCompany().getTipoConfederazione() == 1){
            //utente regionale
            return utils.getIscrittiPerCategoria_UtenteRegionale(anno,u.getCompany().getDescription());
        }

        if (u.getCompany().getTipoConfederazione() == 2){
            //utente nazionale

            if (!StringUtils.isEmpty(region)){
                return utils.getIscrittiPerCategoria_UtenteNazionale(anno, region);
            }else{
                return utils.getIscrittiPerCategoria_UtenteNazionale(anno, "");
            }
        }


        throw new RuntimeException("Operazione non permessa");
    }

    @Override
    public IscrittiDescriptor getIscrittiPerAreaGeografica(int anno, String region) {


        //devo verificare se si tratta di una utenza nazionale oppure regionale
        //la categoria non puo fare questa query (è interessata solo all'area geografica)
        User u = ((User) sec.getLoggedUser());
        if (u.getCompany().getTipoConfederazione() == 1){
            //utente regionale
            return utils.getIscrittiPerAreaGeografica_UtenteRegionale(anno,u.getCompany().getDescription());
        }





        if (u.getCompany().getTipoConfederazione() == 2){
            //utente nazionale

            if (!StringUtils.isEmpty(region)){
                return utils.getIscrittiPerAreaGeografica_UtenteNazionale(anno, region);
            }else{
                return utils.getIscrittiPerAreaGeografica_UtenteNazionale(anno, "");
            }
        }

        //categoria
        if (!StringUtils.isEmpty(region)){
            return utils.getIscrittiPerAreaGeografica_UtenteCategoria(anno, region, u.getCategory().getDescription());
        }


        return utils.getIscrittiPerAreaGeografica_UtenteCategoria(anno, "", u.getCategory().getDescription());


    }

    @Override
    public List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria) {

        return utils.getPivotAnalisysData(regione, categoria);
    }


}
