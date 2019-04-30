package applica.feneal.domain.model.analisi;

import java.util.ArrayList;

/**
 * Created by fgran on 03/01/2018.
 */
public class LegendaFactory {

    public static Legenda constructLegenda(){

        Legenda l = new Legenda();
        l.setItems(new ArrayList<>());

        LegendaItem i1 = new LegendaItem();
        i1.setLabel("Da 0 a 50 Iscritti");
        i1.setValMax(50);
        i1.setValMin(0);
        l.getItems().add(i1);

        LegendaItem i2 = new LegendaItem();
        i2.setLabel("Da 50 a 100 Iscritti");
        i2.setValMax(100);
        i2.setValMin(51);
        l.getItems().add(i2);


        LegendaItem i3 = new LegendaItem();
        i3.setLabel("Da 100 a 200 Iscritti");
        i3.setValMax(200);
        i3.setValMin(101);
        l.getItems().add(i3);


        LegendaItem i4 = new LegendaItem();
        i4.setLabel("Da 200 a 500 Iscritti");
        i4.setValMax(500);
        i4.setValMin(201);
        l.getItems().add(i4);

        LegendaItem i5 = new LegendaItem();
        i5.setLabel("Da 500 a 1.000 Iscritti");
        i5.setValMax(1000);
        i5.setValMin(501);
        l.getItems().add(i5);

        LegendaItem i6 = new LegendaItem();
        i6.setLabel("Da 1.000 a 2.000 Iscritti");
        i6.setValMax(2000);
        i6.setValMin(1001);
        l.getItems().add(i6);

        LegendaItem i7 = new LegendaItem();
        i7.setLabel("Oltre 2.000 Iscritti");
        i7.setValMax(1000000);
        i7.setValMin(2001);
        l.getItems().add(i7);


        return  l;
    }
}
