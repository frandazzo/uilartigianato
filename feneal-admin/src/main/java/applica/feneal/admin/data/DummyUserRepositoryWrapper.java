package applica.feneal.admin.data;

import applica.feneal.domain.model.DummyUser;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.Repository;
import applica.framework.Sort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/19/15
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
@org.springframework.stereotype.Repository
public class DummyUserRepositoryWrapper implements Repository<DummyUser> {

    @Autowired
    private UsersRepositoryWrapper wrp;

    @Override
    public Optional<DummyUser> get(Object id) {
        User entity = wrp.get(id).orElse(null);
        if (entity == null)
            return Optional.empty();
        DummyUser u = new DummyUser();


        u.setActive(entity.isActive());
        u.setCompany(entity.getCompany());
        u.setDefaultProvince(entity.getDefaultProvince());
        u.setName(entity.getName());
        u.setSurname(entity.getSurname());
        u.setId(entity.getId());
        u.setPassword(entity.getPassword());
        u.setUsername(entity.getUsername());
        u.setActivationCode(entity.getActivationCode());
        u.setImage(entity.getImage());
        u.setMail(entity.getMail());
        u.setRoles((java.util.List<Role>) entity.getRoles());
        u.setRegistrationDate(entity.getRegistrationDate());
        u.setDecPass(entity.getDecPass());
        u.setCategory(entity.getCategory());


        Optional<DummyUser> result = Optional.of(u);
        return result;
    }

    @Override
    public LoadResponse<DummyUser> find(LoadRequest request) {

        request.setRowsPerPage(0);
        request.setPage(0);

        LoadResponse<User> u = wrp.find(request);







        List<DummyUser> l = new ArrayList<>();
        List<User> data = u.getRows();

        for (User entity : data) {
            DummyUser uu = new DummyUser();

            uu.setActive(entity.isActive());
            uu.setCompany(entity.getCompany());
            uu.setDefaultProvince(entity.getDefaultProvince());
            uu.setName(entity.getName());
            uu.setSurname(entity.getSurname());
            uu.setId(entity.getId());
            uu.setPassword(entity.getPassword());
            uu.setUsername(entity.getUsername());
            uu.setActivationCode(entity.getActivationCode());
            uu.setImage(entity.getImage());
            uu.setMail(entity.getMail());
            uu.setRoles((java.util.List<Role>) entity.getRoles());
            uu.setRegistrationDate(entity.getRegistrationDate());
            uu.setDecPass(entity.getDecPass());
            uu.setCategory(entity.getCategory());
            l.add(uu);
        }



        if (l.size() > 0) {
            Sort  s = request.getSorts().stream().filter(c -> c.getProperty().equals("roles")).findFirst().orElse(null);
            if (s != null){
                //ordino per ruolo
                if (s.isDescending()){
                    Collections.sort(l, new Comparator<User>() {
                        @Override
                        public int compare(final User object1, final User object2) {
                            return object1.getRoles().get(0).getRole().compareTo(object2.getRoles().get(0).getRole());
                        }
                    });
                }else{
                    Collections.sort(l, new Comparator<User>() {
                        @Override
                        public int compare(final User object1, final User object2) {
                            return object2.getRoles().get(0).getRole().compareTo(object1.getRoles().get(0).getRole());
                        }
                    });
                }

            }


            Sort  s1 = request.getSorts().stream().filter(c -> c.getProperty().equals("category")).findFirst().orElse(null);
            if (s1 != null){
                //ordino per ruolo

                Collections.sort(l, new Comparator<User>() {
                    @Override
                    public int compare(final User object1, final User object2) {
                        if (object1.getCategory() == null)
                            return 1;
                        if (object2.getCategory() == null)
                            return -1;
                        return object1.getCategory().getDescription().compareTo(object2.getCategory().getDescription());
                    }
                });
            }


        }

        LoadResponse<DummyUser> res = new LoadResponse<>();
        res.setRows(l);
        res.setTotalRows(u.getTotalRows());



        return res;
    }

    @Override
    public void save(DummyUser entity) {

        User u = new User();

        u.setActive(entity.isActive());
        u.setCompany(entity.getCompany());
        u.setDefaultProvince(entity.getDefaultProvince());
        u.setName(entity.getName());
        u.setSurname(entity.getSurname());
        u.setId(entity.getId());
        u.setPassword(entity.getPassword());
        u.setUsername(entity.getUsername());
        u.setActivationCode(entity.getActivationCode());
        u.setImage(entity.getImage());
        u.setMail(entity.getMail());
        u.setRoles((java.util.List<Role>) entity.getRoles());
        u.setRegistrationDate(entity.getRegistrationDate());
        u.setDecPass(entity.getDecPass());
        u.setCategory(entity.getCategory());
        wrp.save(u);
    }

    @Override
    public void delete(Object id) {
        wrp.delete(id);
    }

    @Override
    public Class<DummyUser> getEntityType() {
        return DummyUser.class;
    }
}