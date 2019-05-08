package applica.feneal.services;

import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;

import java.util.List;

public interface StatisticsDelegheAbstractService {

    Integer[] getAnniIscrizioni(String tipoEntita) throws Exception;
    IscrittiDescriptor getIscrittiPerCategoria(int anno, String region, String tipoEntita) throws Exception;
    IscrittiDescriptor getIscrittiPerAreaGeografica(int anno, String region, String tipoEntita) throws Exception;

    List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria,  String tipoEntita);

}
