package si.fri.rso.badmintonapporganizations.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rso.badmintonapporganizations.lib.Organization;
import si.fri.rso.badmintonapporganizations.models.converters.OrganizationConverter;
import si.fri.rso.badmintonapporganizations.models.entities.OrganizationEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class OrganizationBean {

    private Logger log = Logger.getLogger(OrganizationBean.class.getName());

    @Inject
    private EntityManager em;


    @Inject
    @Metric(name = "organization_name_histogram")
    Histogram histogram;

    @Inject
    @Metric(name = "relative_new_organizations")
    private ConcurrentGauge relative_new_organization;

    @Inject
    @Metric(name = "change_organization_counter")
    private Counter counter_change;

    @Timed(name = "get_all_organizations_timed")
    public List<Organization> getOrganizations(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, OrganizationEntity.class, queryParameters).stream()
                .map(OrganizationConverter::toDto).collect(Collectors.toList());
    }



    public Organization getOrganization(Integer id) {

        OrganizationEntity organizationEntity = em.find(OrganizationEntity.class, id);

        if (organizationEntity == null) {
            throw new NotFoundException();
        }

        Organization organization = OrganizationConverter.toDto(organizationEntity);

        return organization;
    }

    public Organization createOrganization(Organization org) {

        OrganizationEntity organizationEntity = OrganizationConverter.toEntity(org);

        try {
            beginTx();
            em.persist(organizationEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (organizationEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }
        relative_new_organization.inc();
        histogram.update(organizationEntity.getName().length());

        return OrganizationConverter.toDto(organizationEntity);
    }

    public boolean deleteOrganization(Integer id) {

        OrganizationEntity org = em.find(OrganizationEntity.class, id);

        if (org != null) {
            try {
                beginTx();
                em.remove(org);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }
        relative_new_organization.dec();
        return true;
    }

    @Counted(name = "organization_delete_counter")
    public Organization putOrganization(Integer id, Organization org) {

        OrganizationEntity c = em.find(OrganizationEntity.class, id);

        if (c == null) {
            return null;
        }

        OrganizationEntity updatedOrganizationEntity = OrganizationConverter.toEntity(org);

        try {
            beginTx();
            updatedOrganizationEntity.setId(c.getId());
            updatedOrganizationEntity = em.merge(updatedOrganizationEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }
        counter_change.inc(1);
        return OrganizationConverter.toDto(updatedOrganizationEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

}
