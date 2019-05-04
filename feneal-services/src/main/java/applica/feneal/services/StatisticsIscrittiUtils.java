package applica.feneal.services;

import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;

import java.util.List;

public interface StatisticsIscrittiUtils {
    List<Integer> getListaAnniIscrizioni(String regione, String categoria);

    List<PivotanalisysData> getPivotAnalisysData(String regione, String categoria);

    IscrittiDescriptor getIscrittiPerCategoria_UtenteNazionale(int anno, String regionId);

    IscrittiDescriptor getIscrittiPerCategoria_UtenteRegionale(int anno, String regionId);

    IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteNazionale(int anno, String regionId);

    IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteRegionale(int anno, String regionId);

    IscrittiDescriptor getIscrittiPerAreaGeografica_UtenteCategoria(int anno, String regionId, String categoryId);
}
