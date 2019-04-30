package applica.feneal.admin.controllers;


import applica.feneal.admin.viewmodel.trace.UiTraceLoginView;
import applica.feneal.domain.model.TraceLogin;
import applica.feneal.services.TraceService;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.library.ui.PartialViewRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angelo on 18/11/2017.
 */
@Controller
public class TraceController {

    @Autowired
    private ViewResolver viewResolver;



    @Autowired
    private TraceService traceServ;


    @RequestMapping(value = "/trace/logins", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse viewTraceLogins(HttpServletRequest request) {

        try {

            UiTraceLoginView traceLoginView = new UiTraceLoginView();
            List<TraceLogin> traceLogins = traceServ.retrieveTraceLogins();

            HashMap<String, Object> model = new HashMap<String, Object>();

            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "trace/traceLogins", model, LocaleContextHolder.getLocale(), request);
            traceLoginView.setTraceLogins(traceLogins);
            traceLoginView.setContent(content);

            return new ValueResponse(traceLoginView);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }










}


