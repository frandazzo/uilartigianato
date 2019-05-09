package applica.feneal.services;

import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.deleghe.UiDelegheReportSearchParams;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;

import java.util.List;

/**
 * Created by angelo on 17/05/2016.
 */
public interface ReportDelegheService {

    List<Delega> retrieveDeleghe(UiDelegheReportSearchParams params);

    List<DelegaBilateralita> retrieveDelegheBilateralita(UiDelegheReportSearchParams params);
    List<DelegaUnc> retrieveDelegheUnc(UiDelegheReportSearchParams params);



}
