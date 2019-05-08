package applica.feneal.domain.model.analisi;

import java.util.ArrayList;
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

        for (IscrittiDescriptorItem iscrittiDescriptorItem : iscritti) {
            IscrittiDescriptorItem r = iscrittiDescriptorItem.add(delegheBil.iscritti);
            if (r != null)
                result.getIscritti().add(r);
        }

        return result;
    }
}
