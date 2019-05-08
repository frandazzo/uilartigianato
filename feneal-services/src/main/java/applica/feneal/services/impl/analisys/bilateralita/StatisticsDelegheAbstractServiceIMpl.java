package applica.feneal.services.impl.analisys.bilateralita;

import applica.feneal.domain.model.User;
import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.services.StatisticsDelegheAbstractService;
import applica.feneal.services.StatisticsDelegheAbstractUtils;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsDelegheAbstractServiceIMpl implements StatisticsDelegheAbstractService {

    @Autowired
    private Security sec;


    @Autowired
    private StatisticsDelegheAbstractUtils utils;

    @Override
    public Integer[] getAnniIscrizioni(String tipoEntita) throws Exception {

        List<Integer> a = null;
        User u = ((User) sec.getLoggedUser());



        if (u.getCompany().getTipoConfederazione() == 1){
            //sono un regionale
            a= utils.getListaAnniIscrizioni(u.getCompany().getDescription(), "",tipoEntita);

        }else if (u.getCompany().getTipoConfederazione() == 2){
            //sono un nazionale
            a= utils.getListaAnniIscrizioni("","",tipoEntita);

        }else{
            a= utils.getListaAnniIscrizioni("", u.getCategory().getDescription(),tipoEntita);

        }

        Integer[] a1 = new Integer[a.size()];

        a1 = a.toArray(a1);


        return a1;





    }

    @Override
    public IscrittiDescriptor getIscrittiPerCategoria(int anno, String region, String tipoEntita) throws Exception {
        //devo verificare se si tratta di una utenza nazionale oppure regionale
        //la categoria non puo fare questa query (è interessata solo all'area geografica)
        User u = ((User) sec.getLoggedUser());
        if (u.getCompany().getTipoConfederazione() == 1){
            //utente regionale
            return utils.getIscrittiPerCategoria_UtenteRegionale(anno,u.getCompany().getDescription(), tipoEntita);
        }

        if (u.getCompany().getTipoConfederazione() == 2){
            //utente nazionale

            if (!StringUtils.isEmpty(region)){
                return utils.getIscrittiPerCategoria_UtenteNazionale(anno, region, tipoEntita);
            }else{
                return utils.getIscrittiPerCategoria_UtenteNazionale(anno, "", tipoEntita);
            }
        }


        throw new RuntimeException("Operazione non permessa");
    }

    @Override
    public IscrittiDescriptor getIscrittiPerAreaGeografica(int anno, String region, String tipoEntita) throws Exception {

        //devo verificare se si tratta di una utenza nazionale oppure regionale
        //la categoria non puo fare questa query (è interessata solo all'area geografica)
        User u = ((User) sec.getLoggedUser());
        if (u.getCompany().getTipoConfederazione() == 1){
            //utente regionale
            return utils.getIscrittiPerAreaGeografica_UtenteRegionale(anno,u.getCompany().getDescription(), tipoEntita);
        }





        if (u.getCompany().getTipoConfederazione() == 2){
            //utente nazionale

            if (!StringUtils.isEmpty(region)){
                return utils.getIscrittiPerAreaGeografica_UtenteNazionale(anno, region, tipoEntita);
            }else{
                return utils.getIscrittiPerAreaGeografica_UtenteNazionale(anno, "", tipoEntita);
            }
        }

        //categoria
        if (!StringUtils.isEmpty(region)){
            return utils.getIscrittiPerAreaGeografica_UtenteCategoria(anno, region, u.getCategory().getDescription(), tipoEntita);
        }


        return utils.getIscrittiPerAreaGeografica_UtenteCategoria(anno, "", u.getCategory().getDescription(),tipoEntita);


    }

    @Override
    public List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria, String tipoEntita) {
        return utils.getPivotAnalisysData(regione, categoria, tipoEntita);
    }
}
