package applica.feneal.domain.model.core.deleghe.states;

import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.deleghe.DelegaState;
import applica.feneal.domain.model.setting.CausaleRevoca;
import applica.feneal.domain.model.setting.option.ApplicationOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 05/04/2016.
 */
public class CancelledState implements DelegaState {

    @Override
    public List<Integer> getSupportedNextStates(Delega del, ApplicationOptions opt) {
        if (del.isTombstoneDelega() == true)
            return new ArrayList<>();
        return Arrays.asList(Delega.ACTION_BACK);
    }

    @Override
    public void subscribeDelega(Delega del) {
    }

    @Override
    public void sendDelega(Date date, Delega del) {

    }

    @Override
    public void acceptDelega(Date date, Delega del) {

    }

    @Override
    public void cancelDelega(Date date, Delega del, CausaleRevoca reason, Delega acceptedDelega) {

    }

    @Override
    public void revokeDelega(Date date, Delega del, CausaleRevoca reason) {

    }

    @Override
    public void activateDelega(Delega del) {

    }

    @Override
    public void goBack(Delega delega) {
        if (delega.isTombstoneDelega() )
            return;

        if (delega.getPreecedingState().equals(Delega.state_accepted))
        {
            delega.setCancelReason(null);
            delega.setCancelDate(null);
            delega.setState(Delega.state_accepted);
            delega.setPreecedingState(Delega.state_sent);
        }
        else if (delega.getPreecedingState().equals(Delega.state_activated))
        {
            delega.setCancelReason(null);
            delega.setCancelDate(null);
            delega.setState(Delega.state_activated);
            delega.setPreecedingState(Delega.state_accepted);
        }
        else if (delega.getPreecedingState().equals(Delega.state_cancelled))
        {

        }
        else if (delega.getPreecedingState().equals(Delega.state_revoked))
        {

        }
        else if (delega.getPreecedingState().equals(Delega.state_sent)){
            delega.setCancelReason(null);
            delega.setCancelDate(null);
            delega.setState(Delega.state_sent);
            delega.setPreecedingState(Delega.state_subscribe);
        }

        else { //subscribed state
            delega.setCancelReason(null);
            delega.setCancelDate(null);
            delega.setState(Delega.state_subscribe);
            delega.setPreecedingState(null);
        }
    }

    @Override
    public void updateDelega(Delega delegaToUpdate, Delega data) {
        delegaToUpdate.setContract(data.getContract());
        delegaToUpdate.setCollaborator(data.getCollaborator());
        delegaToUpdate.setNotes(data.getNotes());
        delegaToUpdate.setMansione(data.getMansione());
        delegaToUpdate.setLuogoLavoro(data.getLuogoLavoro());
    }
}

