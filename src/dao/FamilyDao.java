package dao;

import confiq.JPAUtil;
import models.Family;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class FamilyDao {

    public boolean addFamily(Family family) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(family);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public boolean updateFamily(Family family) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(family);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

 public boolean deleteFamily(int familyId) {
    EntityManager em = null;
    try {
        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        
        Family family = em.find(Family.class, familyId);
        if (family == null) return false;

        em.createQuery(
            "DELETE FROM AidDistribution a WHERE a.family.familyId = :fid")
            .setParameter("fid", familyId)
            .executeUpdate();

        em.remove(family);
        em.getTransaction().commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        em.close();
    }
}

    public List<Family> getAllFamilies() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT f FROM Family f", Family.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Family getFamilyByNationalId(String nationalId) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT f FROM Family f WHERE f.nationalId = :nid", Family.class)
                    .setParameter("nid", nationalId).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    //30 يوم
    public boolean canReceiveAid(int familyId) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            Family family = em.find(Family.class, familyId);

            if (family == null) {
                return false;
            }

            if (family.getVulnerabilityLevel().equalsIgnoreCase("HIGH")) {
                return true;
            }

            //غير هيك لازم تكون ما اخدت اخر 30 يوم
            // يعني مر 30 يوم او اكثر على اخر مساعدة
            if (family.getLastAidDate() != null) {
                LocalDate thirtyDays = LocalDate.now().minusDays(30);  // 5/30 4/30

                return family.getLastAidDate().isBefore(thirtyDays)
                        || family.getLastAidDate().isEqual(thirtyDays);
            }

            return true; // ما استلم نهائي قبل هيك

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Family> getFamiliesSortedByVulnerability() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery(
                    "SELECT f FROM Family f ORDER BY( CASE f.vulnerabilityLevel WHEN 'HIGH' THEN 1 "
                    + "WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 ELSE 4 END)",
                    Family.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Family> getFamiliesNotServed() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery(
                    "SELECT f FROM Family f WHERE f.lastAidDate IS NULL",
                    Family.class).getResultList();
        } finally {
            em.close();
        }
    }

}
