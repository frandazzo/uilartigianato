package applica.feneal.domain.model.analisi;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by fgran on 03/01/2018.
 */
public class IscrittiDescriptor {

    private List<LegendaItem> legenda;
    private List<IscrittiDescriptorItem> iscritti;


    public List<LegendaItem> getLegenda() {
        return legenda;
    }

    public void setLegenda(List<LegendaItem> legenda) {
        this.legenda = legenda;
    }

    public List<IscrittiDescriptorItem> getIscritti() {
        return iscritti;
    }

    public void setIscritti(List<IscrittiDescriptorItem> iscritti) {
        this.iscritti = iscritti;
    }

    public IscrittiDescriptor add(IscrittiDescriptor delegheBil) {
        IscrittiDescriptor result = new IscrittiDescriptor();
        result.setLegenda(legenda);

        result.setIscritti(new ArrayList<>());


        //metto in una hash ttute l'unione di tutte le voci della lista di partenza e quella di arrivo
        Hashtable<String, Integer> v = new Hashtable<>();
        for (IscrittiDescriptorItem iscrittiDescriptorItem : iscritti) {
            v.put(iscrittiDescriptorItem.getLabel(), iscrittiDescriptorItem.getTotal());
        }

        for (IscrittiDescriptorItem iscrittiDescriptorItem : delegheBil.getIscritti()) {
            if (!v.containsKey(iscrittiDescriptorItem.getLabel()))
                v.put(iscrittiDescriptorItem.getLabel(), iscrittiDescriptorItem.getTotal());
            else{
                int oldvalue = v.get(iscrittiDescriptorItem.getLabel());
                int valueToAdd = iscrittiDescriptorItem.getTotal();
                v.put(iscrittiDescriptorItem.getLabel(), oldvalue + valueToAdd);

            }
        }

        //ciclo su tute le key della hashmap e creo un itemDescriptr....
        for (String label : v.keySet()) {
            IscrittiDescriptorItem item = new IscrittiDescriptorItem();
            item.setLabel(label);
            item.setTotal(v.get(label));
            result.getIscritti().add(item);
        }



        return result;
    }
}
