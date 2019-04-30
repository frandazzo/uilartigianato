package applica.feneal.domain.model.setting;

import applica.feneal.domain.model.utils.SecuredDomainEntity;

/**
 * Created by fgran on 06/05/2017.
 */
public class Attribuzione1 extends SecuredDomainEntity {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

