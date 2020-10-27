package applica.feneal.admin.data;

import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.services.GeoService;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Created by applica on 17/03/15.
 */
@org.springframework.stereotype.Repository
public class AziendeRepositoryWrapper implements Repository<Azienda> {

    @Autowired
    private AziendeRepository aziendeRepository;

    @Autowired
    private GeoService geoSvc;


    @Override
    public Optional<Azienda> get(Object id) {
        return aziendeRepository.get(id);
    }

    @Override
    public LoadResponse<Azienda> find(LoadRequest request) {


        final LoadRequest finalRequest = request;

        request.popFilter("alternativeDescription").ifPresent(filter -> {
            if (StringUtils.hasLength(String.valueOf(filter.getValue())))
                finalRequest.getFilters().add(new Filter("alternativeDescription", filter.getValue(),Filter.LIKE));
        });


        request.popFilter("province").ifPresent(filter -> {
            if (StringUtils.hasLength(String.valueOf(filter.getValue())))
                finalRequest.getFilters().add(new Filter("province", geoSvc.getProvinceById(Integer.valueOf((String)filter.getValue())),Filter.EQ));
        });

        request.popFilter("city").ifPresent(filter -> {
            if (StringUtils.hasLength(String.valueOf(filter.getValue())))
                finalRequest.getFilters().add(new Filter("city", geoSvc.getCityById(Integer.valueOf((String)filter.getValue())),Filter.EQ));
        });



        return aziendeRepository.find(request);

    }

    @Override
    public void save(Azienda entity) {
        aziendeRepository.save(entity);
    }

    @Override
    public void delete(Object id) {
        aziendeRepository.delete(id);
    }

    @Override
    public Class<Azienda> getEntityType() {
        return aziendeRepository.getEntityType();
    }
}
