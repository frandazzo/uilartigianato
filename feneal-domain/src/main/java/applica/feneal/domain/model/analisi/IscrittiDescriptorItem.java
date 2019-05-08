package applica.feneal.domain.model.analisi;

import java.util.List;

/**
 * Created by fgran on 03/01/2018.
 */
public class IscrittiDescriptorItem {
    private String label;
    private int total;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public IscrittiDescriptorItem add(List<IscrittiDescriptorItem> iscritti) {
        IscrittiDescriptorItem result = new IscrittiDescriptorItem();
        for (IscrittiDescriptorItem iscrittiDescriptorItem : iscritti) {
            if (label.equals(iscrittiDescriptorItem.getLabel())){
                result.setLabel(label);
                result.setTotal(total + iscrittiDescriptorItem.getTotal());
                return result;
            }

        }
        return null;
    }
}
