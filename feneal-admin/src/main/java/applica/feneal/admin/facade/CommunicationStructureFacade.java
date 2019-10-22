package applica.feneal.admin.facade;

import applica.feneal.admin.viewmodel.UiCommunicationStructure;
import applica.feneal.domain.model.core.CommunicationStructure;
import applica.feneal.services.CommunicationStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommunicationStructureFacade {

    @Autowired
    private CommunicationStructureService commStrRep;

    public List<UiCommunicationStructure> retriveCommStructureList() {
        List<CommunicationStructure> list = commStrRep.retriveLastCommunicationStructure();
        return convertListToUiCommunicationSrtucture(list);
    }

    private List<UiCommunicationStructure> convertListToUiCommunicationSrtucture(List<CommunicationStructure> list) {

        SimpleDateFormat dF = new SimpleDateFormat("dd/MM/yyyy");

        List<UiCommunicationStructure> l = new ArrayList<>();
        for (CommunicationStructure comm : list) {
            UiCommunicationStructure c = new UiCommunicationStructure();
            c.setTitle(comm.getBriefDescription());
            c.setDescription(comm.getDescription());
            c.setDate(dF.format(comm.getDate()));
            if(comm.getNomeattached1() != "") {
                c.setNameAtt1(comm.getNomeattached1());
                c.setAtt1(comm.getAttached1());
            }else{
                c.setNameAtt1("");
                c.setAtt1("");
            }
            if(comm.getNomeattached2() != "") {
                c.setNameAtt2(comm.getNomeattached2());
                c.setAtt2(comm.getAttached2());
            }else{
                c.setNameAtt2("");
                c.setAtt2("");
            }
            if(comm.getNomeattached3() != "") {
                c.setNameAtt3(comm.getNomeattached3());
                c.setAtt3(comm.getAttached3());
            }else{
                c.setNameAtt3("");
                c.setAtt3("");
            }
            l.add(c);
        }

        return l;
    }
}
