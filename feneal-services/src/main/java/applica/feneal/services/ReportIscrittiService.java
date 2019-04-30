package applica.feneal.services;

import applica.feneal.domain.model.core.servizi.search.UiIscrittoReportSearchParams;
import applica.feneal.domain.model.dbnazionale.Iscrizione;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;

import java.util.List;

/**
 * Created by angelo on 29/05/2016.
 */
public interface ReportIscrittiService {

    List<Iscritto> retrieveFastPerformaceQuoteIscritti(UiIscrittoReportSearchParams params) throws Exception;

    List<Iscritto> retrieveFastPerformaceQuoteIscritti(long firmId) throws Exception;

    //List<DettaglioQuotaAssociativa> retrieveQuoteIscritti(UiIscrittoReportSearchParams params) throws Exception;

    List<Iscrizione> findIscrizioniByWorkerId(long id);

}
