package isn.geiger.server.database.controllers;

import isn.geiger.server.database.controllers.exceptions.NonexistentEntityException;
import isn.geiger.server.database.controllers.exceptions.PreexistingEntityException;
import isn.geiger.server.database.entities.Hitlog;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author LoadLow
 */
public class HitlogJpaController implements Serializable {

    public HitlogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hitlog hitlog) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(hitlog);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHitlog(hitlog.getTime()) != null) {
                throw new PreexistingEntityException("Hitlog " + hitlog + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hitlog hitlog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            hitlog = em.merge(hitlog);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Date id = hitlog.getTime();
                if (findHitlog(id) == null) {
                    throw new NonexistentEntityException("The hitlog with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Date id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hitlog hitlog;
            try {
                hitlog = em.getReference(Hitlog.class, id);
                hitlog.getTime();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hitlog with id " + id + " no longer exists.", enfe);
            }
            em.remove(hitlog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hitlog> findHitlogEntities() {
        return findHitlogEntities(true, -1, -1);
    }

    public List<Hitlog> findHitlogEntities(int maxResults, int firstResult) {
        return findHitlogEntities(false, maxResults, firstResult);
    }

    private List<Hitlog> findHitlogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hitlog.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Hitlog findHitlog(Date id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hitlog.class, id);
        } finally {
            em.close();
        }
    }

    public int getHitlogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hitlog> rt = cq.from(Hitlog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
