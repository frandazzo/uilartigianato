package applica.feneal.services;


import applica.feneal.domain.model.core.CommunicationStructure;

import java.util.List;

public interface CommunicationStructureService {

    List<CommunicationStructure> retriveLastCommunicationStructure();
}
