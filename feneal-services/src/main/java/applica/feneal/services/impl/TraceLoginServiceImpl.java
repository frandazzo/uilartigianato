package applica.feneal.services.impl;

import applica.feneal.domain.data.TraceLoginRepository;
import applica.feneal.domain.model.TraceLogin;
import applica.feneal.services.TraceService;
import applica.framework.LoadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraceLoginServiceImpl implements TraceService {

    @Autowired
    private TraceLoginRepository traceLoginRepository;




    @Override
    public List<TraceLogin> retrieveTraceLogins() {
        return traceLoginRepository.find(LoadRequest.build()).getRows();
    }



    @Override
    public void traceLogin( int year, String month, String company, String username) {

        TraceLogin tracelogin = traceLoginRepository.find(LoadRequest.build()
                .filter("year", year)
                .filter("month", month)
                .filter("company", company)
                .filter("username", username)).findFirst().orElse(null);

        if (tracelogin == null) {
            tracelogin = new TraceLogin();
            tracelogin.setYear(year);
            tracelogin.setMonth(month);
            tracelogin.setCompany(company);
            tracelogin.setUsername(username);
        }


        tracelogin.setCounterWebsite(tracelogin.getCounterWebsite() + 1);

        traceLoginRepository.save(tracelogin);
    }


}

