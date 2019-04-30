package applica.feneal.admin.customSecurity;

import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.framework.*;
import applica.framework.data.security.LoggedUserIdOwnerProvider;
import applica.framework.data.security.OwnerProvider;
import applica.framework.data.security.SecureEntity;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Created by fgran on 12/02/2018.
 */
public class CustomSecureCrudstrategy  extends ChainedCrudStrategy {
    private OwnerProvider ownerProvider;
    private String ownerPropertyName = "ownerId";

    @Autowired
    private Security sec;


    public CustomSecureCrudstrategy() {
    }

    public String getOwnerPropertyName() {
        return this.ownerPropertyName;
    }

    public void setOwnerPropertyName(String ownerPropertyName) {
        this.ownerPropertyName = ownerPropertyName;
    }

    public OwnerProvider getOwnerProvider() {
        return this.ownerProvider;
    }

    public void setOwnerProvider(OwnerProvider ownerProvider) {
        this.ownerProvider = ownerProvider;
    }

    private Object getOwnerId() {
        if(this.ownerProvider == null) {
            this.ownerProvider = new LoggedUserIdOwnerProvider();
        }

        return this.ownerProvider.provide();
    }

    private void checkAttributes() {
        Assert.notNull(this.getParent(), "Parent strategy not found. Check application configuration file");
        Assert.notNull(this.ownerPropertyName, "Parent strategy not found. Check application configuration file");
    }

    private boolean canRemoveSecuredFilters(){
        if (sec.getLoggedUser() == null)
            return false;
        Role r = (Role)(((User) sec.getLoggedUser()).getRoles().get(0));
        if (r.getLid() == 3 || r.getLid() == 4)
            return false;
        return true;
    }

    public <T extends Entity> T get(Object id, Repository<T> repository) {
        this.checkAttributes();

        if (canRemoveSecuredFilters())
            return super.get(id, repository);

        if(SecureEntity.class.isAssignableFrom(repository.getEntityType())) {
            Optional entity = super.find(LoadRequest.build().eq(this.getOwnerPropertyName(), this.getOwnerId()).id(id), repository).findFirst();
            return (T)entity.orElse((Object)null);
        } else {
            return super.get(id, repository);
        }
    }

    public <T extends Entity> LoadResponse<T> find(LoadRequest loadRequest, Repository<T> repository) {
        this.checkAttributes();

        if (!canRemoveSecuredFilters())
        {
            if(SecureEntity.class.isAssignableFrom(repository.getEntityType())) {
                Filter filter = new Filter(this.getOwnerPropertyName(), this.getOwnerId());
                loadRequest.getFilters().add(filter);
            }
        }

        return super.find(loadRequest, repository);
    }

    public <T extends Entity> void save(T entity, Repository<T> repository) {
        this.checkAttributes();
        if(SecureEntity.class.isAssignableFrom(repository.getEntityType())) {
            SecureEntity se = (SecureEntity)entity;
            if(se.getOwnerId() == null) {
                se.setOwnerId(this.getOwnerId());
            }
        }

        super.save(entity, repository);
    }

    public <T extends Entity> void delete(Object id, Repository<T> repository) {
        this.checkAttributes();
        super.delete(id, repository);
    }
}

