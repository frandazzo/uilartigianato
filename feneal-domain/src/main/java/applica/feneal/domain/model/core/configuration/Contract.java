package applica.feneal.domain.model.core.configuration;

import applica.framework.AEntity;

/**
 * Created by angelo on 31/05/2016.
 */
public class Contract extends AEntity {

    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
