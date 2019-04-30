package applica.feneal.domain.model.setting;

import applica.feneal.domain.model.utils.SecuredDomainEntity;

/**
 * Created by angelo on 08/06/2017.
 */
public class EmailLayout extends SecuredDomainEntity {

    public static final String TIPO_1 = "TIPO1";
    public static final String TIPO_2 = "TIPO2";
    public static final String TIPO_3 = "TIPO3";

    private String name;
    private String type;
    private String imagePath1;
    private String imagePath2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }
}

