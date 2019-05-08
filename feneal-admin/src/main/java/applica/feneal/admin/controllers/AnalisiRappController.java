package applica.feneal.admin.controllers;


import applica.feneal.domain.model.User;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;

import applica.feneal.services.StatisticsDelegheAbstractService;
import applica.feneal.services.StatisticsIscrittiService;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.library.ui.PartialViewRenderer;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fgran on 13/12/2017.
 */
@Controller
public class AnalisiRappController {
    @Autowired
    private ViewResolver viewResolver;

    @Autowired
    private Security sec;



    @Autowired
    private StatisticsDelegheAbstractService statServ;



    @RequestMapping(value = "/analisi/riepilogoRap",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse riepilogo(HttpServletRequest request) {

        try {

            HashMap<String, Object> model = new HashMap<String, Object>();

            String view = "analisi/riepilogoRap";
            User u = ((User) sec.getLoggedUser());
            if (u.getCompany().getTipoConfederazione() == 1){ //regionale
                view = "analisi/riepilogoRapRegionale";
                model.put("regione", u.getRegion().getDescription().toUpperCase());
            }else if (u.getCompany().getTipoConfederazione() == 3){
                view = "analisi/riepilogoRapCategoria";
                model.put("categoria", u.getCategory().getDescription());
                model.put("categoriaAlias", u.getCategory().getDescription());
            }

            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, view, model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/analisi/pivotrap",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse pivot(HttpServletRequest request) {

        try {

            //String urlAnalisiIscritti = optMan.get("data.analisiiscritti.url");

            HashMap<String, Object> model = new HashMap<String, Object>();
            //  model.put("datasource", urlAnalisiIscritti);

            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "analisi/pivotrap", model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/analisi/pivotrap/data",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse pivotData(HttpServletRequest request) {
        try {
            User u = ((User) sec.getLoggedUser());

            List<PivotanalisysData> data = null;

            if (u.getCompany().getTipoConfederazione() == 1){
                //sono un regionale
                data =  statServ.getPivotAnalisysData(u.getCompany().getDescription(), "", "fenealweb_delega");
            }else if (u.getCompany().getTipoConfederazione() == 2){
                //sono un nazionale
                data =  statServ.getPivotAnalisysData("", "","fenealweb_delega");
            }else{
                data = statServ.getPivotAnalisysData("", u.getCategory().getDescription(),"fenealweb_delega");
            }


            return new ValueResponse(data);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value="/analisi/riepilogoRap/categoria", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse getIscrittiPerCategoria(@RequestParam(value="year", required=true) int year,
                                                                @RequestParam(value="region", required=false) String region
    ) {




        try {
            if (region != null)
                if (region.equals("noregion"))
                    region = "";
            return new ValueResponse(statServ.getIscrittiPerCategoria(year, region,"fenealweb_delega"));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value="/analisi/riepilogoRap/areageografica", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse getIscrittiPerAreaGeografica(@RequestParam(value="year", required=true) int year,
                                                                     @RequestParam(value="region", required=false) String region) {

        try {
            if (region != null)
                if (region.equals("noregion"))
                    region = "";
            return new ValueResponse(statServ.getIscrittiPerAreaGeografica(year, region,"fenealweb_delega"));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value="/analisi/riepilogoRap/anni", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse getAnniIscrizioni() {

        try {

            return new ValueResponse(statServ.getAnniIscrizioni("fenealweb_delega"));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

}
