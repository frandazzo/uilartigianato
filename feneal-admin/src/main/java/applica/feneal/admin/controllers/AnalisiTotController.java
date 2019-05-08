package applica.feneal.admin.controllers;


import applica.feneal.admin.facade.StatisticsDelegheFacade;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;

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
public class AnalisiTotController {
    @Autowired
    private ViewResolver viewResolver;

    @Autowired
    private Security sec;



    @Autowired
    private StatisticsDelegheFacade statServ;




    @RequestMapping(value = "/analisi/riepilogoTot",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse riepilogo(HttpServletRequest request) {

        try {

            HashMap<String, Object> model = new HashMap<String, Object>();

            String view = "analisi/riepilogoTot";
            User u = ((User) sec.getLoggedUser());
            if (u.getCompany().getTipoConfederazione() == 1){ //regionale
                view = "analisi/riepilogoTotRegionale";
                model.put("regione", u.getRegion().getDescription().toUpperCase());
            }else if (u.getCompany().getTipoConfederazione() == 3){
                view = "analisi/riepilogoTotCategoria";
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


    @RequestMapping(value = "/analisi/pivottot",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse pivot(HttpServletRequest request) {

        try {

            //String urlAnalisiIscritti = optMan.get("data.analisiiscritti.url");

            HashMap<String, Object> model = new HashMap<String, Object>();
            //  model.put("datasource", urlAnalisiIscritti);

            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "analisi/pivottot", model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/analisi/pivottot/data",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse pivotData(HttpServletRequest request) {
        try {
            User u = ((User) sec.getLoggedUser());

            List<PivotanalisysData> data = null;

            if (u.getCompany().getTipoConfederazione() == 1){
                //sono un regionale
                data =  statServ.getPivotAnalisysData(u.getCompany().getDescription(), "");
            }else if (u.getCompany().getTipoConfederazione() == 2){
                //sono un nazionale
                data =  statServ.getPivotAnalisysData("", "");
            }else{
                data = statServ.getPivotAnalisysData("", u.getCategory().getDescription());
            }


            return new ValueResponse(data);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value="/analisi/riepilogoTot/categoria", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse getIscrittiPerCategoria(@RequestParam(value="year", required=true) int year,
                                                                @RequestParam(value="region", required=false) String region
    ) {




        try {
            if (region != null)
                if (region.equals("noregion"))
                    region = "";
            return new ValueResponse(statServ.getIscrittiPerCategoria(year, region));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value="/analisi/riepilogoTot/areageografica", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse getIscrittiPerAreaGeografica(@RequestParam(value="year", required=true) int year,
                                                                     @RequestParam(value="region", required=false) String region) {

        try {
            if (region != null)
                if (region.equals("noregion"))
                    region = "";
            return new ValueResponse(statServ.getIscrittiPerAreaGeografica(year, region));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value="/analisi/riepilogoTot/anni", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse getAnniIscrizioni() {

        try {

            return new ValueResponse(statServ.getAnniIscrizioni());
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

}
