package applica.feneal.services.impl.setup;

import applica.feneal.domain.data.core.WidgetRepository;
import applica.feneal.domain.model.core.Widget;
import applica.framework.LoadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 13/06/2016.
 */
@Component
public class WidgetSetup implements  AppSetup {

    @Autowired
    private WidgetRepository wRep;


    @Override
    public void setup() {


        //widget per la categoria
        //************************************************
        //************************************************
        //contatore iscritti per settore per anno
        Widget w1 = findWidgetByUrl("widget/real/contatoreIscrittiPerSettore/");
        if (w1 == null){
            w1 = new Widget();
            w1.setDescription("ContatoreIscritti");
            w1.setType("locale");
            w1.setContext("dashboard");
            w1.setDefaultPosition(1);
            w1.setUrl("widget/real/contatoreIscrittiPerSettore/");
            wRep.save(w1);

        }
//        //andamento iscritti per settore
        Widget wa11 = findWidgetByUrl("widget/real/andamentoIscrittiPerSettore/");
        if (wa11 == null){
            wa11 = new Widget();
            wa11.setDescription("AndamentoIscritti");
            wa11.setType("locale");
            wa11.setContext("dashboard");
            wa11.setDefaultPosition(2);
            wa11.setUrl("widget/real/andamentoIscrittiPerSettore/");

            wRep.save(wa11);

        }
//
//        //contatore prestazioni per mese e anno
        Widget w1pr = findWidgetByUrl("widget/real/contatorePrestazioni/");
        if (w1pr == null){
            w1pr = new Widget();
            w1pr.setDescription("ContatorePrestazioni");
            w1pr.setType("all");
            w1pr.setContext("dashboard");
            w1pr.setDefaultPosition(1);
            w1pr.setUrl("widget/real/contatorePrestazioni/");
            wRep.save(w1pr);

        }
//        //andamento per un singolo anno (se nullo, tutto)
        Widget wa11pr = findWidgetByUrl("widget/real/andamentoPrestazioni/");
        if (wa11pr == null){
            wa11pr = new Widget();
            wa11pr.setDescription("AndamentoPrestazioni");
            wa11pr.setType("all");
            wa11pr.setContext("dashboard");
            wa11pr.setDefaultPosition(1);
            wa11pr.setUrl("widget/real/andamentoPrestazioni/");

            wRep.save(wa11pr);

        }
//
//
//        //contatore segnalazioni ricevute per mese e anno
        Widget w1pr1 = findWidgetByUrl("widget/real/contatoreSegnalazioni/");
        if (w1pr1 == null){
            w1pr1 = new Widget();
            w1pr1.setDescription("ContatoreSegnalazioni");
            w1pr1.setType("all");
            w1pr1.setContext("dashboard");
            w1pr1.setDefaultPosition(1);
            w1pr1.setUrl("widget/real/contatoreSegnalazioni/");
            wRep.save(w1pr1);

        }
//        //andamento per un singolo anno (se nullo, tutto)
        Widget wa11pr2 = findWidgetByUrl("widget/real/andamentoSegnalazioni/");
        if (wa11pr2 == null){
            wa11pr2 = new Widget();
            wa11pr2.setDescription("AndamentoSegnalazioni");
            wa11pr2.setType("all");
            wa11pr2.setContext("dashboard");
            wa11pr2.setDefaultPosition(1);
            wa11pr2.setUrl("widget/real/andamentoSegnalazioni/");

            wRep.save(wa11pr2);

        }
//
//
//
//        //contatore deleghe ricevute per mese e anno
//        Widget wx = findWidgetByUrl("widget/real/contatoreDeleghe/");
//        if (wx == null){
//            wx = new Widget();
//            wx.setDescription("ContatoreDeleghe");
//            wx.setType("all");
//            wx.setContext("dashboard");
//            wx.setDefaultPosition(1);
//            wx.setUrl("widget/real/contatoreDeleghe/");
//            wRep.save(wx);
//
//        }
        //andamento per un singolo anno (se nullo, tutto)
        Widget wy = findWidgetByUrl("widget/real/andamentoDeleghe/");
        if (wy == null){
            wy = new Widget();
            wy.setDescription("AndamentoDeleghe");
            wy.setType("locale");
            wy.setContext("dashboard");
            wy.setDefaultPosition(1);
            wy.setUrl("widget/real/andamentoDeleghe/");

            wRep.save(wy);

        }
//
//        //************************************************
//        //************************************************
//
//        //widget per ital e caf
//        //sono già inclusi con i tipi all (segnalazioni e prestazioni)
//        //i dettagli dipenderanno dall'utente loggato.....
//
//
//        //per quanto riguarda la uil bisogna aggiungere il grafico per gli iscritti totali....
        Widget w1a = findWidgetByUrl("widget/real/contatoreIscrittiPerCategoria/");
        if (w1a == null){
            w1a = new Widget();
            w1a.setDescription("ContatoreIscrittiPerCategoria");
            w1a.setType("uil");
            w1a.setContext("dashboard");
            w1a.setDefaultPosition(2);
            w1a.setUrl("widget/real/contatoreIscrittiPerCategoria/");
            wRep.save(w1a);

        }
//        //andamento iscritti per settore
        Widget wa11a = findWidgetByUrl("widget/real/andamentoIscrittiPerCategoria/");
        if (wa11a == null){
            wa11a = new Widget();
            wa11a.setDescription("AndamentoIscrittiPerCategoria");
            wa11a.setType("uil");
            wa11a.setContext("dashboard");
            wa11a.setDefaultPosition(1);
            wa11a.setUrl("widget/real/andamentoIscrittiPerCategoria/");

            wRep.save(wa11a);

        }

//VECCHI WIDJETS
//        Widget w1 = findWidgetByUrl("widget/real/contatoreIscritti/");
//        if (w1 == null){
//            w1 = new Widget();
//            w1.setDescription("ContatoreIscritti");
//            w1.setType("locale");
//            w1.setContext("dashboard");
//            w1.setDefaultPosition(1);
//            w1.setUrl("widget/real/contatoreIscritti/");
//            wRep.save(w1);
//
//        }
//        //andamento iscritti per settore
//        Widget wa11 = findWidgetByUrl("widget/real/andamentoIscrittiPerProvincia/");
//        if (wa11 == null){
//            wa11 = new Widget();
//            wa11.setDescription("AndamentoIscrittiProvincia");
//            wa11.setType("locale");
//            wa11.setContext("dashboard");
//            wa11.setDefaultPosition(2);
//            wa11.setUrl("widget/real/andamentoIscrittiPerProvincia/");
//
//            wRep.save(wa11);
//
//        }
//
//
//
//        Widget w0 = findWidgetByUrl("widget/real/sindacalizzazione/");
//        if (w0 == null){
//            w0 = new Widget();
//            w0.setDescription("Rappresentanza");
//            w0.setType("locale");
//            w0.setContext("dashboard");
//            w0.setDefaultPosition(1);
//            w0.setUrl("widget/real/sindacalizzazione/");
//
//            wRep.save(w0);
//
//        }
//
//
//        Widget w11 = findWidgetByUrl("widget/real/contatoreIscrittiEdile/");
//        if (w11 == null){
//            w11 = new Widget();
//            w11.setDescription("ContatoreIscrittiEdile");
//            w11.setType("locale");
//            w11.setContext("dashboard");
//            w11.setDefaultPosition(3);
//            w11.setUrl("widget/real/contatoreIscrittiEdile/");
//
//            wRep.save(w11);
//
//        }
//
//        Widget wa = findWidgetByUrl("widget/real/contatoreIscrittiTerritorioAccorpato/");
//        if (wa == null){
//            wa = new Widget();
//            wa.setDescription("ContatoreIscrittiTerritorioAccorpato");
//            wa.setType("locale");
//            wa.setContext("dashboard");
//            wa.setDefaultPosition(1);
//            wa.setUrl("widget/real/contatoreIscrittiTerritorioAccorpato/");
//
//            wRep.save(wa);
//
//        }
//
//
//        Widget wa1 = findWidgetByUrl("widget/real/andamentoIscrittiPerTerritorioAccorpato/");
//        if (wa1 == null){
//            wa1 = new Widget();
//            wa1.setDescription("AndamentoIscrittiTerritorioAccorpato");
//            wa1.setType("locale");
//            wa1.setContext("dashboard");
//            wa1.setDefaultPosition(1);
//            wa1.setUrl("widget/real/andamentoIscrittiPerTerritorioAccorpato/");
//
//            wRep.save(wa1);
//
//        }
//
//
//
//
//
//        Widget wa1w1 = findWidgetByUrl("widget/real/andamentoIscrittiPerSettoreEdile/");
//        if (wa1w1 == null){
//            wa1w1 = new Widget();
//            wa1w1.setDescription("AndamentoIscrittiPerSettoreEdile");
//            wa1w1.setType("locale");
//            wa1w1.setContext("dashboard");
//            wa1w1.setDefaultPosition(3);
//            wa1w1.setUrl("widget/real/andamentoIscrittiPerSettoreEdile/");
//
//            wRep.save(wa1w1);
//
//        }
//
//
//        Widget wa1w11 = findWidgetByUrl("widget/real/noniscritticlassifica/");
//        if (wa1w11 == null){
//            wa1w11 = new Widget();
//            wa1w11.setDescription("ClassificaNonIscritti");
//            wa1w11.setType("locale");
//            wa1w11.setContext("dashboard");
//            wa1w11.setDefaultPosition(2);
//            wa1w11.setUrl("widget/real/noniscritticlassifica/");
//
//            wRep.save(wa1w11);
//
//        }




    }

    private Widget findWidgetByUrl(String s) {
        LoadRequest req = LoadRequest.build().filter("url", s);
        return wRep.find(req).findFirst().orElse(null);
    }


}
