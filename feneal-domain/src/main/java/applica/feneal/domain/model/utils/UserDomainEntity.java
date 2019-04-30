package applica.feneal.domain.model.utils;

import applica.framework.data.hibernate.annotations.IgnoreMapping;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by fgran on 03/05/2017.
 */
@IgnoreMapping
@JsonIgnoreProperties({ "iid", "sid", "lid" })
public class UserDomainEntity extends SecuredDomainEntity {

    private String userCompleteName;
    private long userId;


    public String getUserCompleteName() {
        return userCompleteName;
    }

    public void setUserCompleteName(String userCompleteName) {
        this.userCompleteName = userCompleteName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
