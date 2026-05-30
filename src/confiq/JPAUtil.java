package confiq;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory emf;

    private JPAUtil() {
    }

    
    
    private static EntityManagerFactory getEMF() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("GHADSPU");
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEMF().createEntityManager();
    }

    public static void closeEMF() {
        if (emf != null) {
            emf.close();
        }
    }
}
