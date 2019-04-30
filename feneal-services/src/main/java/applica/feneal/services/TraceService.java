package applica.feneal.services;

import applica.feneal.domain.model.TraceLogin;

import java.util.List;

/**
 * Created by angelo on 17/11/2017.
 */
public interface TraceService {

    List<TraceLogin> retrieveTraceLogins();



    void traceLogin(int year, String month, String company, String username);


}
