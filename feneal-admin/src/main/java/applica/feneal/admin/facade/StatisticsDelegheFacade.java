package applica.feneal.admin.facade;

import applica.feneal.domain.model.analisi.IscrittiDescriptor;
import applica.feneal.domain.model.analisi.PivotanalisysData;
import applica.feneal.domain.utils.MergeRemoveDupSort;
import applica.feneal.services.StatisticsDelegheAbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StatisticsDelegheFacade {

    @Autowired
    private StatisticsDelegheAbstractService statServ;


    public List<PivotanalisysData> getPivotAnalisysData(String regionId, String categoria) {
        List<PivotanalisysData> result = new ArrayList<>();

        List<PivotanalisysData> deleghe = statServ.getPivotAnalisysData(regionId,categoria,"fenealweb_delega");
        List<PivotanalisysData> delegheBil = statServ.getPivotAnalisysData(regionId,categoria,"fenealweb_delega_bilateralita");
        List<PivotanalisysData> delegheUnc = statServ.getPivotAnalisysData(regionId,categoria,"fenealweb_delega_unc");

        deleghe.stream().forEach(a -> a.setNazionalita("delega"));
        delegheBil.stream().forEach(a ->a.setNazionalita("bilateralita"));
        delegheUnc.stream().forEach(a ->a.setNazionalita("unc"));

        result.addAll(deleghe);
        result.addAll(delegheBil);
        result.addAll(delegheUnc);

        return result;
    }

    public IscrittiDescriptor getIscrittiPerCategoria(int year, String region) throws Exception {


        IscrittiDescriptor deleghe = statServ.getIscrittiPerCategoria(year, region,"fenealweb_delega");
        IscrittiDescriptor delegheBil = statServ.getIscrittiPerCategoria(year, region,"fenealweb_delega_bilateralita");
        IscrittiDescriptor delegheUnc = statServ.getIscrittiPerCategoria(year, region,"fenealweb_delega_unc");


        IscrittiDescriptor result = deleghe.add(delegheBil).add(delegheUnc);


        return result;
    }

    public IscrittiDescriptor getIscrittiPerAreaGeografica(int year, String region) throws Exception {
        IscrittiDescriptor deleghe = statServ.getIscrittiPerAreaGeografica(year, region,"fenealweb_delega");
        IscrittiDescriptor delegheBil = statServ.getIscrittiPerAreaGeografica(year, region,"fenealweb_delega_bilateralita");
        IscrittiDescriptor delegheUnc = statServ.getIscrittiPerAreaGeografica(year, region,"fenealweb_delega_unc");
        IscrittiDescriptor result =deleghe.add(delegheBil).add(delegheUnc);

        return result;
    }

    public List<Integer> getAnniIscrizioni() throws Exception {


        Integer[] deleghe = statServ.getAnniIscrizioni("fenealweb_delega");
        Integer[] delegheBil = statServ.getAnniIscrizioni("fenealweb_delega_bilateralita");
        Integer[] delegheUnc = statServ.getAnniIscrizioni("fenealweb_delega_unc");

        MergeRemoveDupSort d = new MergeRemoveDupSort();
       Integer[] delegheresult = d.mergeRemoveDupSortIt(deleghe, delegheBil);

       List<Integer> l = Arrays.stream(d.mergeRemoveDupSortIt(delegheresult, delegheUnc)).distinct().collect(Collectors.toList());

        Collections.sort(l, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2)*-1;
            }
        });
        return l;


    }
}
