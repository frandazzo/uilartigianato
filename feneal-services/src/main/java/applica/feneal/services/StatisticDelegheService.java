package applica.feneal.services;

import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;

import java.util.List;

public interface StatisticDelegheService {
    Integer[] getAnniIscrizioni();
    IscrittiDescriptor getIscrittiPerCategoria(int anno, String region);
    IscrittiDescriptor getIscrittiPerAreaGeografica(int anno, String region);

    List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria);
}
