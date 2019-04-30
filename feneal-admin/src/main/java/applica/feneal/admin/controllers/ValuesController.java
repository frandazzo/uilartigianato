package applica.feneal.admin.controllers;

import applica.feneal.domain.data.RolesRepository;
import applica.feneal.domain.data.UsersRepository;
import applica.feneal.domain.data.core.CollaboratorRepository;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.data.core.SignupDelegationReasonRepository;
import applica.feneal.domain.data.core.VisibleFunctionRepository;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.lavoratori.ListaLavoroRepository;
import applica.feneal.domain.data.geo.CitiesRepository;
import applica.feneal.domain.data.geo.CountriesRepository;
import applica.feneal.domain.data.geo.ProvinceRepository;
import applica.feneal.domain.model.Filters;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.VisibleFunction;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.lavoratori.search.LavoratoreSearchParams;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Country;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.model.setting.CausaleIscrizioneDelega;
import applica.feneal.domain.model.setting.Collaboratore;
import applica.feneal.services.LavoratoreService;
import applica.framework.Disjunction;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.Sort;
import applica.framework.library.SimpleItem;
import applica.framework.library.responses.ValueResponse;
import applica.framework.security.Security;
import applica.framework.security.authorization.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica (www.applica.guru)
 * User: bimbobruno
 * Date: 3/3/13
 * Time: 11:11 PM
 */
@Controller
@RequestMapping("/values")
public class ValuesController {

    @Autowired
    private SignupDelegationReasonRepository causaleIscrizioneDelegaRepository;



    @Autowired
    private Security security;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private LavoratoreService lavoratoreService;

    @Autowired
    private CompanyRepository compRep;


    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    CitiesRepository citRepository;

    @Autowired
    AziendeRepository aziendeRepository;

    @Autowired
    CountriesRepository countryRepository;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    VisibleFunctionRepository visibleFunctionRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ListaLavoroRepository listaLavoroRepository;

    @RequestMapping("/allcountries")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse states1() {



        List<Country> c = countryRepository.find(
                LoadRequest.build()
                       .sort("description", false)
        ).getRows();

        return new ValueResponse(SimpleItem.createList(c, "description", "id"));
    }

    @RequestMapping("/allprovinces")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse provinces1() {

        List<Province> provinces = provinceRepository.find(
                LoadRequest.build().sort("description", false)).getRows();

        return new ValueResponse(SimpleItem.createList(provinces, "description", "id"));
    }

    @RequestMapping("/countries")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse states(@RequestParam(required = false, defaultValue = "") String keyword) {

        if (keyword.equals(""))
            return new ValueResponse(new ArrayList<SimpleItem>());

        List<Country> c = countryRepository.find(
                LoadRequest.build()
                        .eq("continent", keyword).sort("description", false)
        ).getRows();

        return new ValueResponse(SimpleItem.createList(c, "description", "id"));
    }

    @RequestMapping("/cities")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse cities(@RequestParam(required = false, defaultValue = "") String keyword) {
        if (keyword.equals(""))
            return new ValueResponse(new ArrayList<SimpleItem>());

        if (!StringUtils.isNumeric(keyword)){
            //allora sto inviando il testgo di una provincia
            //pertanto recupero la provincia per nome...
            Province pp = provinceRepository.find(LoadRequest.build().filter("description", keyword, Filter.EQ)).findFirst().orElse(null);
            if (pp == null)
                return new ValueResponse(new ArrayList<SimpleItem>());

            keyword = String.valueOf(pp.getIid());
        }


        List<City> cities = citRepository.find(
                LoadRequest.build()
                        .eq("idProvince", keyword)
        ).getRows();

        return new ValueResponse(SimpleItem.createList(cities, "description", "id"));
    }


    @RequestMapping("/capforcity")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse capforcity(@RequestParam(required = false, defaultValue = "") String keyword) {
        if (keyword.equals(""))
            return new ValueResponse("");
        try{
            Integer i = Integer.parseInt(keyword);
            City citis = citRepository.get(i).orElse(null);
            if (citis != null)
                return new ValueResponse(citis.getCap());
        }catch(Exception e){
            e.printStackTrace();
        }






        return new ValueResponse("");
    }



    @RequestMapping("/companies")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse territori(String keyword) {
        List<Company> roles = compRep.find(
                LoadRequest.build()
                        .eq("tipoConfederazione", 1)
        ).getRows();

        return new ValueResponse(SimpleItem.createList(roles, "description", "id"));
    }


    @RequestMapping("/roles")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse roles(String keyword) {
        List<Role> roles = rolesRepository.find(
                LoadRequest.build()
                        .like("role", keyword)
        ).getRows();

        return new ValueResponse(SimpleItem.createList(roles, "role", "id"));
    }

    @RequestMapping("/workers")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse workers(String keyword) {

        if (keyword.length() < 3)
            return new ValueResponse(new ArrayList<SimpleItem>());

        LavoratoreSearchParams params = new LavoratoreSearchParams();
        params.setNamesurname(keyword);

        List<Lavoratore> workers = lavoratoreService.findLocalLavoratori(((User) security.getLoggedUser()).getLid(),params );

        return new ValueResponse(SimpleItem.createList(workers, "surnamename", "id"));
    }


    @RequestMapping("/workersbyregion")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse workers(String keyword, String category) {

        if (keyword.length() < 3)
            return new ValueResponse(new ArrayList<SimpleItem>());

        LavoratoreSearchParams params = new LavoratoreSearchParams();
        params.setNamesurname(keyword);
        params.setCompanyId(Long.parseLong(category));
        List<Lavoratore> workers = lavoratoreService.findLocalLavoratori(((User) security.getLoggedUser()).getLid(),params );

        return new ValueResponse(SimpleItem.createList(workers, "surnamename", "id"));
    }



    @RequestMapping("/rolesforadmin")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse rolesforadmin(String keyword) {

        //lamministratore puo creare solo un altro amministratore oppure l'amministratore di  una società
        List<Role> roles =  new ArrayList<Role>();

        Role r = ((Role)security.getLoggedUser().getRoles().get(0));
        if (r.getLid() == 1){ //se sono supre amministratore posso solo creare altri amministratori
            roles.add(rolesRepository.get(new Long(1)).get());
            roles.add(rolesRepository.get(new Long(2)).get());
        }else{ //altrimenti sono un ammiistratore della società che crea utenti per quella società
            roles.add(rolesRepository.get(new Long(3)).get());
            roles.add(rolesRepository.get(new Long(4)).get());
            roles.add(rolesRepository.get(new Long(5)).get());
            roles.add(rolesRepository.get(new Long(6)).get());


//            roles.add(rolesRepository.get(new Long(6)).get());
//            roles.add(rolesRepository.get(new Long(7)).get());

        }
        return new ValueResponse(SimpleItem.createList(roles, "role", "id"));
    }

    @RequestMapping("/permissions")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse permissions(String keyword) {
        return new ValueResponse(
                SimpleItem.createList(Permissions.instance().allPermissions(), (p) -> p, (p) -> p)
        );
    }

    @RequestMapping("/provinces")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse provinces(String keyword) {

        List<Province> provinces = provinceRepository.find(
                LoadRequest.build().sort("description", false)).getRows();

        return new ValueResponse(SimpleItem.createList(provinces, "description", "id"));
    }

    @RequestMapping("/provincesForCompany")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse provincesForCompany(String keyword) {

        if (StringUtils.isEmpty(keyword))
            return new ValueResponse();

        Company c  = compRep.get(Long.parseLong(keyword)).orElse(null);

        if (c == null)
            return new ValueResponse();

        List<Province> provinces = c.getProvinces();

        return new ValueResponse(SimpleItem.createList(provinces, "description", "id"));
    }



    @RequestMapping("/aziendebyregion")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse companiesbyregion(String keyword, String category) {

        if (keyword.length() < 2)
            return new ValueResponse(new ArrayList<SimpleItem>());

        Disjunction d = new Disjunction();
        List<Filter> ff = new ArrayList<>();

        Filter f = new Filter();
        f.setType(Filter.LIKE);
        f.setProperty("description");
        f.setValue(keyword);

        ff.add(f);

        Filter f1 = new Filter();
        f1.setType(Filter.LIKE);
        f1.setProperty("alternativeDescription");
        f1.setValue(keyword);

        ff.add(f1);

        d.setChildren(ff);


        Sort s = new Sort();
        s.setDescending(false);
        s.setProperty("description");

        LoadRequest req = LoadRequest.build();
        req.getFilters().add(d);


        Filter company = new Filter();
        company.setProperty("companyId");
        company.setType(Filter.EQ);
        company.setValue(Long.parseLong(category));
        req.getFilters().add(company);


        req.getSorts().add(s);






        List<Azienda> aziende = aziendeRepository.find(req).getRows();



        return new ValueResponse(SimpleItem.createList(aziende, "completeDescription", "id"));
    }

    @RequestMapping("/aziende")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse companies(String keyword) {

        if (keyword.length() < 2)
            return new ValueResponse(new ArrayList<SimpleItem>());

        Disjunction d = new Disjunction();
        List<Filter> ff = new ArrayList<>();

        Filter f = new Filter();
        f.setType(Filter.LIKE);
        f.setProperty("description");
        f.setValue(keyword);

        ff.add(f);

        Filter f1 = new Filter();
        f1.setType(Filter.LIKE);
        f1.setProperty("alternativeDescription");
        f1.setValue(keyword);

        ff.add(f1);

        d.setChildren(ff);


        Sort s = new Sort();
        s.setDescending(false);
        s.setProperty("description");

        LoadRequest req = LoadRequest.build();
        req.getFilters().add(d);
        req.getSorts().add(s);

        List<Azienda> aziende = aziendeRepository.find(req).getRows();



        return new ValueResponse(SimpleItem.createList(aziende, "completeDescription", "id"));
    }


    @RequestMapping("/visiblefunctions")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse visibleFunctions(String keyword) {

        List<VisibleFunction> functions = visibleFunctionRepository.find(
                LoadRequest.build()).getRows();

        return new ValueResponse(SimpleItem.createList(functions, "description", "id"));
    }

    @RequestMapping("/collaborators")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse collaborators(String keyword) {

        List<Collaboratore> functions = collaboratorRepository.find(
                LoadRequest.build().like(Filters.DESCRIPTION, keyword)).getRows();

        return new ValueResponse(SimpleItem.createList(functions, "description", "id"));
    }


    @RequestMapping("/causalideleghe")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse causali() {

        List<CausaleIscrizioneDelega> functions = causaleIscrizioneDelegaRepository.find(LoadRequest.build().sort(Filters.DESCRIPTION, false)).getRows();

        return new ValueResponse(SimpleItem.createList(functions, "description", "id"));
    }

    @RequestMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse users(String keyword) {

        List<User> users = usersRepository.find(
                LoadRequest.build()).getRows();

        return new ValueResponse(SimpleItem.createList(
                users,
                (u) -> String.format("%s %s", u.getName(), u.getSurname()),
                (u) -> String.valueOf(u.getId())));
    }

    @RequestMapping("/listelavoro")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody ValueResponse listeLavoro(String keyword) {


       // l.setUserId(((User) sec.getLoggedUser()).getLid());

        List<ListaLavoro> listeLavoro = listaLavoroRepository.find(
                LoadRequest.build().filter("companyId", ((User) security.getLoggedUser()).getCompany().getLid())).getRows();

        return new ValueResponse(SimpleItem.createList(listeLavoro, "description", "id"));
    }

}
