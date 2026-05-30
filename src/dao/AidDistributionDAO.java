package dao;

import confiq.JPAUtil;
import models.AidDistribution;
import models.Family;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class AidDistributionDAO {

    public boolean addDistribution(AidDistribution record) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(record); // save the record
            Family family = record.getFamily(); // update last aid date for that family
            family.setLastAidDate(LocalDate.now());
            em.merge(family);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public List<AidDistribution> getAllDistributionRecords() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT a FROM AidDistribution a", AidDistribution.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<AidDistribution> getRecordsByOrganization(int orgId) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT a FROM AidDistribution a WHERE a.organization.orgId = :oid", AidDistribution.class)
                    .setParameter("oid", orgId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long getServedCountByOrganization(int orgId) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT COUNT(DISTINCT a.family.familyId) FROM AidDistribution a WHERE a.organization.orgId = :oid", Long.class)
                    .setParameter("oid", orgId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public boolean deleteDistribution(int distributionId) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            AidDistribution record = em.find(AidDistribution.class, distributionId);
            if (record != null) { // فحص منطقي (Logical Check)
                em.remove(record);
                em.getTransaction().commit();
                return true;
            } else {
                // السجل غير موجود أصلاً
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public long getNotServedFamiliesCount() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT COUNT(f) FROM Family f WHERE f.lastAidDate IS NULL", Long.class).getSingleResult();

        } catch (Exception e) {
            return 0;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public AidDistribution getLastDistributionByFamily(int familyId) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery(
                    "SELECT a FROM AidDistribution a "
                    + "WHERE a.family.familyId = :fid "
                    + "ORDER BY a.distributionDate DESC",
                    AidDistribution.class)
                    .setParameter("fid", familyId)
                    .setMaxResults(1) // أول نتيجة بس  
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
