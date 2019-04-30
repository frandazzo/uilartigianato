package applica.feneal.services.impl;

import applica.feneal.services.SetupService;
import applica.feneal.services.impl.setup.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/9/15
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SetupServiceImpl implements SetupService {




    @Autowired
    private AdminSetup adminSetup;


    @Autowired
    private GeoSetup geoSetup;



    @Autowired
    private RolesSetup rolesSetup;




    @Autowired
    private WidgetSetup wSetup;

    @Autowired
    private SettoriSetup settSetup;





    @Override
    public void setup() {



        //inizializzo in ordine e tenedo conto di eventuali dipendenze...
        geoSetup.setup();
        rolesSetup.setup();
        adminSetup.setup();



        settSetup.setup();
        wSetup.setup();






    }



}
