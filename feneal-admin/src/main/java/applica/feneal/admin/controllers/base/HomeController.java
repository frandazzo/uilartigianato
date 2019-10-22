package applica.feneal.admin.controllers.base;

import applica.feneal.admin.facade.CommunicationStructureFacade;
import applica.feneal.admin.viewmodel.UiCommunicationStructure;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.CommunicationStructure;
import applica.feneal.domain.model.core.Company;
import applica.feneal.services.CommunicationStructureService;
import applica.feneal.services.impl.importData.ImportCausaliService;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Applica (www.applica.guru)
 * User: bimbobruno
 * Date: 2/22/13
 * Time: 3:18 PM
 */
@Controller
public class HomeController {

    @Autowired
    private ImportCausaliService causService;
    @Autowired
    private Security sec;


    @Autowired
    private CommunicationStructureFacade commStrFacade;





    @RequestMapping("/")
    @PreAuthorize("isAuthenticated()")
    public String index(Model model) {

        //imposto il ruolo dell'utente per deciderne le funzioni di menu
        User u = ((User) sec.getLoggedUser());
        model.addAttribute("roleid", u.retrieveUserRole().getLid());
        model.addAttribute("user", ((User) sec.getLoggedUser()).getCompleteName());
        model.addAttribute("rolename", u.retrieveUserRole().getRole());
        model.addAttribute("userInitials", String.format("%s%s",((User) sec.getLoggedUser()).getName().charAt(0), ((User) sec.getLoggedUser()).getSurname().charAt(0)));

        String categoryId = "";
        String categoryName = "";
        int categoryType = -1;
        if (u.retrieveUserRole().getLid() > 2)
        {
            categoryId = String.valueOf(u.getLid());
            //categoryName = u.getCompleteName();
            Company c = u.getCompany();
            if (c == null)
            {
                categoryType = 0;
                categoryName = "Nessuno";
            }
            else{
                categoryType = c.getTipoConfederazione();
                categoryName = c.getDescription();

            }



        }
        if (u.getCategory() != null){
            model.addAttribute("category", u.getCategory().getDescription());
        }else{
             model.addAttribute("category","");
        }

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("categoryType", categoryType);

        if (((User) sec.getLoggedUser()).getCompany() != null)
            model.addAttribute("provinces", ((User) sec.getLoggedUser()).getCompany().getProvinces()
                .stream().map(p -> p.getDescription().toLowerCase()).collect(Collectors.joining(",")));
        else
            model.addAttribute("provinces", "");




       // inizializzo le causali di base dell'applicativo se l'utente loggato ha un contesto regionale
        if (u.retrieveUserRole().getLid() == 3 || u.retrieveUserRole().getLid() == 4){
            causService.setup();
        }

        if(u.retrieveUserRole().getLid() != 1){



            List<UiCommunicationStructure> commStructList = commStrFacade.retriveCommStructureList();

            if(commStructList.size() > 0) {
                model.addAttribute("commFlag", true);
            }else {
                model.addAttribute("commFlag", false);
            }
            model.addAttribute("commStructList", commStructList);
        }




        return "index";
    }

}
