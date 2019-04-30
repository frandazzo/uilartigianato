package applica.feneal.services.impl.setup;


import applica.feneal.domain.data.core.ContractRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.configuration.Contract;
import applica.framework.LoadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 05/04/2017.
 */
@Component
public class SettoriSetup implements AppSetup{

    @Autowired
    private CategoriaRepository parRep;

    @Autowired
    private ContractRepository par1Rep;


    @Autowired
    private CategoriaRepository catRep;

    @Override
    public void setup() {


        createCategoria(Categoria.feneal);
        createCategoria(Categoria.uilm);
        createCategoria(Categoria.uiltec);
        createCategoria(Categoria.uila);
        createCategoria(Categoria.uilcom);
        createCategoria(Categoria.uilt);
        createCategoria(Categoria.uiltucs);
        createCategoria(Categoria.uiltemp);


        createContratto("AREA MECCANICA");
        createContratto("AREA TESSILE - MODA");
        createContratto("AREA CHIMICA");
        createContratto("AREA AGROALIMENTARE");
        createContratto("AREA LEGNO - LAPIDEI");
        createContratto("AREA COMUNICAZIONE");
        createContratto("AREA SERVIZI");
        createContratto("AREA COSTRUZIONI");
        createContratto("AREA AUTOTRASPORTO");
        createContratto("ALTRO");








    }

    private void createCategoria(String description) {

        Categoria c = findCategoria(description);
        if (c == null)
        {
            c = new Categoria();
            c.setDescription(description);

            parRep.save(c);
        }

    }

    private void createContratto(String description) {

        Contract c = findContratto(description);
        if (c == null)
        {
             c = new Contract();
            c.setDescription(description);

            par1Rep.save(c);
        }

    }

    private Categoria findCategoria(String description) {
        LoadRequest req = LoadRequest.build().filter("description",description);

        return catRep.find(req).findFirst().orElse(null);
    }

    private Contract findContratto(String description) {
        LoadRequest req = LoadRequest.build().filter("description",description);

        return par1Rep.find(req).findFirst().orElse(null);
    }



    private Categoria findSettore(String cat) {
        LoadRequest req = LoadRequest.build().filter("description",cat);

        return parRep.find(req).findFirst().orElse(null);
    }
}
