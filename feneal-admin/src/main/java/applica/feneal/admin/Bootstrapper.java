package applica.feneal.admin;

import applica.feneal.admin.crudconfig.CrudConfSetup;
import applica.feneal.domain.data.UsersRepository;
import applica.feneal.services.SetupService;
import applica.framework.AEntity;
import applica.framework.library.options.OptionsManager;
import applica.framework.licensing.LicenseManager;
import applica.framework.widgets.CrudFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Applica (www.applica.guru)
 * User: bimbobruno
 * Date: 2/21/13
 * Time: 3:37 PM
 */
public class Bootstrapper {

    @Autowired
    UsersRepository rep;

    static {
        AEntity.strategy = AEntity.IdStrategy.Long;
    }

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private OptionsManager options;

    @Autowired
    private CrudFactory crudFactory;

    @Autowired
    private SetupService setupService;

    @Autowired
    private CrudConfSetup crudSetup;


    public void init() {
        LicenseManager.instance().setUser(options.get("applica.framework.licensing.user"));
        LicenseManager.instance().mustBeValid();

        setupService.setup();
        crudSetup.setup();
    }


}
