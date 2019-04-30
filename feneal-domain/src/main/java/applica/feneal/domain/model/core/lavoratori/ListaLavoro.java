package applica.feneal.domain.model.core.lavoratori;

import applica.feneal.domain.model.utils.UserDomainEntity;
import applica.framework.annotations.ManyToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 04/06/2016.
 */
public class ListaLavoro extends UserDomainEntity {
    private String description;
    @ManyToMany
    private List<Lavoratore> lavoratori;

    public ListaLavoro clone(){

        ListaLavoro l = new ListaLavoro();
        l.setDescription(description);


        List<Lavoratore> l1 = new ArrayList<>();
        for (Lavoratore lavoratore : lavoratori) {
            l1.add(lavoratore);
        }
        l.setLavoratori(l1);
        l.setUserId(getUserId());
        l.setUserCompleteName(getUserCompleteName());
        return l;

    }

    public ListaLavoro intersects(ListaLavoro l , String description){

        List<Lavoratore> reusult  = new ArrayList<>();

        for (Lavoratore lavoratore : lavoratori) {
            if (l.containsWorker(lavoratore.getFiscalcode()))
                reusult.add(lavoratore);
        }

        ListaLavoro ll = new ListaLavoro();
        ll.setLavoratori(reusult);
        ll.setDescription(description);
        ll.setUserId(l.getUserId());
        ll.setUserCompleteName(l.getUserCompleteName());


        return ll;
    }




    public boolean containsWorker(String fiscalCode){
        for (Lavoratore lavoratore : lavoratori) {
            if (lavoratore.getFiscalcode().toLowerCase().equals(fiscalCode.toLowerCase()))
                return true;
        }
        return false;
    }

    private void removeWorker(Lavoratore lav){

        if (!containsWorker(lav.getFiscalcode()))
            return;

        int indexToRemove = -1;
        for (int i = 0; i < lavoratori.size(); i++) {
           Lavoratore l  = lavoratori.get(i);
            if (l.getFiscalcode().toLowerCase().equals(lav.getFiscalcode().toLowerCase())){
                indexToRemove = i;
                break;
            }

        }
        if (indexToRemove > -1 )
            lavoratori.remove(indexToRemove);

    }

//    public ListaLavoro  excludeLavoratoriOfOtherList(ListaLavoro b, String description ){
//
//        List<Lavoratore> l = new ArrayList<>();
//
//        for (Lavoratore lavoratore : lavoratori) {
//            l.add(lavoratore);
//        }
//
//
//        for (Lavoratore lavoratore : b.getLavoratori()) {
//            Lavoratore ll = lavoratori.stream().filter(a -> a.getFiscalcode().equals(lavoratore.getFiscalcode())).findFirst().orElse(null);
//            if (ll != null)
//                l.add(ll);
//        }
//
//
//        ListaLavoro ll = new ListaLavoro();
//        ll.setDescription(description);
//        ll.setLavoratori(l);
//        ll.setUserId(b.getUserId());
//        ll.setUserCompleteName(b.getUserCompleteName());
//        return ll;
//
//    }















    private void removeWorker1(Lavoratore lav){

        if (!containsWorker(lav.getFiscalcode()))
            return;

        int indexToRemove = -1;
        for (int i = 0; i < lavoratori.size(); i++) {
            Lavoratore l  = lavoratori.get(i);
            if (l.getFiscalcode().toLowerCase().equals(lav.getFiscalcode().toLowerCase())){
                indexToRemove = i;
                break;
            }

        }
        if (indexToRemove > -1 )
            lavoratori.remove(indexToRemove);

    }

    public ListaLavoro  excludeLavoratoriOfOtherList(ListaLavoro b, String description ){
        for (Lavoratore lavoratore : b.getLavoratori()) {
            removeWorker1(lavoratore);
        }


        ListaLavoro ll = new ListaLavoro();
        ll.setDescription(description);
        ll.setLavoratori(lavoratori);
        ll.setUserId(b.getUserId());
        ll.setUserCompleteName(b.getUserCompleteName());
        return ll;

    }






















    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lavoratore> getLavoratori() {
        return lavoratori;
    }

    public void setLavoratori(List<Lavoratore> lavoratori) {
        this.lavoratori = lavoratori;
    }
}
