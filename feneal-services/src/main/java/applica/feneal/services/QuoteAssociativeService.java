package applica.feneal.services;

import applica.feneal.domain.model.core.deleghe.DelegaPerCreazioneQuota;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.UpdatableDettaglioQuota;
import applica.feneal.domain.model.core.quote.fenealgestImport.DettaglioQuotaDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.domain.model.core.servizi.search.UiIqaReportSearchParams;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;

import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 20/05/2016.
 */
public interface QuoteAssociativeService {




    String importQuoteAssociativeFromDatiTerritori(int companyId, RiepilogoQuotaDTO dto) throws Exception;


    void deleteQuota(long idRiepilogoQuota);

    List<DettaglioQuotaAssociativa> retrieveQuote(UiIqaReportSearchParams params) throws Exception;

//    List<DettaglioQuotaAssociativa> findCurrentIscrizioniForAzienda(long firmId);


    List<Iscritto> fastfindCurrentIscrizioniForAzienda(long firmId) throws Exception;

    List<DettaglioQuotaAssociativa> getDettagliQuota(long idRiepilogoQuota, Long idWorker);

    //metodo che crea le quote a partire dalle delleghe: "Crea quote per azienda"
    String creaQuotemassive(RiepilogoQuotaDTO dto) throws Exception;

    List<DettaglioQuotaAssociativa> getStoricoVersamenti(long workerId);

    RiepilogoQuoteAssociative getRiepilogoQuotaById(long id);

//    RiepilogoQuoteAssociative creaQuoteManuali(RiepilogoQuotaDTO dto) throws Exception;;
//
//    RiepilogoQuoteAssociative creaQuoteBreviMano(RiepilogoQuotaDTO dto) throws Exception;

    DettaglioQuotaAssociativa addItem(RiepilogoQuoteAssociative riepilogoQuoteAssociative, DettaglioQuotaDTO dettaglioQuotaDTO) throws Exception;

    void deleteItem(long quotaId, long itemId);

    void updateItem(long quotaId, long itemId, UpdatableDettaglioQuota updatedData);

    void duplicaQuota(long quotaId, Date inizio, Date fine);

    //long createQuotaAssociativaPerSettore(Categoria settore, Date inizio, Date fine, String provinciaId) throws Exception;

    void modifyCompetenceQuotaItems(long quotaId, Date inizio, Date fine);

    List<DelegaPerCreazioneQuota> searchAziendeByName(String name);

    String bonificaQuote(String note, long[] quoteIds);
    String deleteBonifico(long idBonifico) throws Exception;


}
