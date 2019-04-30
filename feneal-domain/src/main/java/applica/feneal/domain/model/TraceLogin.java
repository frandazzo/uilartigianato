package applica.feneal.domain.model;

import applica.framework.AEntity;

/**
 * Created by angelo on 14/11/2017.
 */
public class TraceLogin extends AEntity {

    private int counterWebsite;
    private int year;
    private String month;
    private String company;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getCounterWebsite() {
        return counterWebsite;
    }

    public void setCounterWebsite(int counterWebsite) {
        this.counterWebsite = counterWebsite;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
