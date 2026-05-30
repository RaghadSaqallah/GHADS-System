package dao;

import confiq.JPAUtil;
import models.Organization;
import javax.persistence.EntityManager;
import java.util.List;

public class OrganizationDAO {

    public boolean addOrganization(Organization org) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(org);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // 2. تحديث بيانات منظمة
    public boolean updateOrganization(Organization org) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(org);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    // 3. حذف منظمة
public boolean deleteOrganization(int orgId) {
    EntityManager em = null;
    try {
        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Organization org = em.find(Organization.class, orgId);
        if (org == null) return false;

        em.createQuery(
            "DELETE FROM AidDistribution a WHERE a.organization.orgId = :oid")
            .setParameter("oid", orgId)
            .executeUpdate();

        em.createQuery(
            "DELETE FROM User u WHERE u.organization.orgId = :oid")
            .setParameter("oid", orgId)
            .executeUpdate();

        em.remove(org);
        em.getTransaction().commit();
        return true;

    } catch (Exception e) {
        return false;
    } finally {
        em.close();
    }
}

    public List<Organization> getAllOrganizations() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT o FROM Organization o", Organization.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Organization getOrganizationById(int orgId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Organization.class, orgId);
        } finally {
            em.close();
        }
    }
}
