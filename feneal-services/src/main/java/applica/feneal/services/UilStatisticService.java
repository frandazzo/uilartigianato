package applica.feneal.services;

import applica.feneal.domain.model.core.report.RiepilogoAndamento;
import applica.feneal.domain.model.core.report.RiepilogoIscrittiPerSettore;

import java.util.List;

/**
 * Created by fgran on 22/03/2017.
 */
public interface UilStatisticService {
    RiepilogoIscrittiPerSettore getContatoreIscrittiPerSettore(Long loggedUser, Integer anno);

    List<RiepilogoAndamento> getAndamentoIscrittiPerSettore();

    List<String> getListaMesi();
    List<String> getListaMesiWithoutNull();

    Integer getMonthByMonthName(String mese);

    RiepilogoIscrittiPerSettore getContatoreIscrittiPerCategoria(Integer anno, Integer mese);

    List<RiepilogoAndamento> getAndamentoIscrittiPerCategoria();

    List<RiepilogoAndamento> getAndamentoDeleghe(Integer year, String settore);

    List<RiepilogoAndamento> getAndamentoSegnalazioni(Integer year) throws Exception;

    List<RiepilogoAndamento> getAndamentoPrestazioni(Integer year);

    RiepilogoIscrittiPerSettore getContatorePrestazioni(Integer yeam, Integer month) throws Exception;

    RiepilogoIscrittiPerSettore getContatoreSegnalazioni(Integer year, Integer month) throws Exception;
}
