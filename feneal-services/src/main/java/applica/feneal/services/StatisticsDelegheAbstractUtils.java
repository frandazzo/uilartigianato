package applica.feneal.services;

import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;

import java.util.List;

public interface StatisticsDelegheAbstractUtils {


    List<Integer> getListaAnniIscrizioni(String regione, String categoria, String tipoEntita) throws Exception;

    IscrittiDescriptor getIscrittiPerCategoria_UtenteRegionale(int anno, String description, String tipoEntita) throws Exception;

    IscrittiDescriptor getIscrittiPerCategoria_UtenteNazionale(int anno, String region, String tipoEntita) throws Exception;

    IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteRegionale(int anno, String regionId, String tipoEntita) throws Exception;

    IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteNazionale(int anno, String regionId, String tipoEntita) throws Exception;

    IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteCategoria(int anno, String regionId, String categoria, String tipoEntita) throws Exception;

    List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria,String tipoEntita);
}
