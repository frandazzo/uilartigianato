package applica.feneal.domain.model.core.quote;

import applica.feneal.domain.model.User;
import applica.framework.AEntity;

import java.util.Date;

public class Bonifico extends AEntity {

    private Date data;
    private String note;
    private User user;


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
