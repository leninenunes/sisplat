/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import facade.exceptions.IllegalOrphanException;
import facade.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Rt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import model.RtHasProfissional;
import model.Status;
import model.Status_;

/**
 *
 * @author lenine.nunes
 */
public class StatusJpaController implements Serializable {

    public StatusJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Status status) {
        if (status.getRtCollection() == null) {
            status.setRtCollection(new ArrayList<Rt>());
        }
        if (status.getRtHasProfissionalCollection() == null) {
            status.setRtHasProfissionalCollection(new ArrayList<RtHasProfissional>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rt> attachedRtCollection = new ArrayList<Rt>();
            for (Rt rtCollectionRtToAttach : status.getRtCollection()) {
                rtCollectionRtToAttach = em.getReference(rtCollectionRtToAttach.getClass(), rtCollectionRtToAttach.getId());
                attachedRtCollection.add(rtCollectionRtToAttach);
            }
            status.setRtCollection(attachedRtCollection);
            Collection<RtHasProfissional> attachedRtHasProfissionalCollection = new ArrayList<RtHasProfissional>();
            for (RtHasProfissional rtHasProfissionalCollectionRtHasProfissionalToAttach : status.getRtHasProfissionalCollection()) {
                rtHasProfissionalCollectionRtHasProfissionalToAttach = em.getReference(rtHasProfissionalCollectionRtHasProfissionalToAttach.getClass(), rtHasProfissionalCollectionRtHasProfissionalToAttach.getRtHasProfissionalPK());
                attachedRtHasProfissionalCollection.add(rtHasProfissionalCollectionRtHasProfissionalToAttach);
            }
            status.setRtHasProfissionalCollection(attachedRtHasProfissionalCollection);
            em.persist(status);
            for (Rt rtCollectionRt : status.getRtCollection()) {
                Status oldStatusIdOfRtCollectionRt = rtCollectionRt.getStatusId();
                rtCollectionRt.setStatusId(status);
                rtCollectionRt = em.merge(rtCollectionRt);
                if (oldStatusIdOfRtCollectionRt != null) {
                    oldStatusIdOfRtCollectionRt.getRtCollection().remove(rtCollectionRt);
                    oldStatusIdOfRtCollectionRt = em.merge(oldStatusIdOfRtCollectionRt);
                }
            }
            for (RtHasProfissional rtHasProfissionalCollectionRtHasProfissional : status.getRtHasProfissionalCollection()) {
                Status oldStatusIdOfRtHasProfissionalCollectionRtHasProfissional = rtHasProfissionalCollectionRtHasProfissional.getStatusId();
                rtHasProfissionalCollectionRtHasProfissional.setStatusId(status);
                rtHasProfissionalCollectionRtHasProfissional = em.merge(rtHasProfissionalCollectionRtHasProfissional);
                if (oldStatusIdOfRtHasProfissionalCollectionRtHasProfissional != null) {
                    oldStatusIdOfRtHasProfissionalCollectionRtHasProfissional.getRtHasProfissionalCollection().remove(rtHasProfissionalCollectionRtHasProfissional);
                    oldStatusIdOfRtHasProfissionalCollectionRtHasProfissional = em.merge(oldStatusIdOfRtHasProfissionalCollectionRtHasProfissional);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Status status) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Status persistentStatus = em.find(Status.class, status.getId());
            Collection<Rt> rtCollectionOld = persistentStatus.getRtCollection();
            Collection<Rt> rtCollectionNew = status.getRtCollection();
            Collection<RtHasProfissional> rtHasProfissionalCollectionOld = persistentStatus.getRtHasProfissionalCollection();
            Collection<RtHasProfissional> rtHasProfissionalCollectionNew = status.getRtHasProfissionalCollection();
            List<String> illegalOrphanMessages = null;
            for (Rt rtCollectionOldRt : rtCollectionOld) {
                if (!rtCollectionNew.contains(rtCollectionOldRt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rt " + rtCollectionOldRt + " since its statusId field is not nullable.");
                }
            }
            for (RtHasProfissional rtHasProfissionalCollectionOldRtHasProfissional : rtHasProfissionalCollectionOld) {
                if (!rtHasProfissionalCollectionNew.contains(rtHasProfissionalCollectionOldRtHasProfissional)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RtHasProfissional " + rtHasProfissionalCollectionOldRtHasProfissional + " since its statusId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Rt> attachedRtCollectionNew = new ArrayList<Rt>();
            for (Rt rtCollectionNewRtToAttach : rtCollectionNew) {
                rtCollectionNewRtToAttach = em.getReference(rtCollectionNewRtToAttach.getClass(), rtCollectionNewRtToAttach.getId());
                attachedRtCollectionNew.add(rtCollectionNewRtToAttach);
            }
            rtCollectionNew = attachedRtCollectionNew;
            status.setRtCollection(rtCollectionNew);
            Collection<RtHasProfissional> attachedRtHasProfissionalCollectionNew = new ArrayList<RtHasProfissional>();
            for (RtHasProfissional rtHasProfissionalCollectionNewRtHasProfissionalToAttach : rtHasProfissionalCollectionNew) {
                rtHasProfissionalCollectionNewRtHasProfissionalToAttach = em.getReference(rtHasProfissionalCollectionNewRtHasProfissionalToAttach.getClass(), rtHasProfissionalCollectionNewRtHasProfissionalToAttach.getRtHasProfissionalPK());
                attachedRtHasProfissionalCollectionNew.add(rtHasProfissionalCollectionNewRtHasProfissionalToAttach);
            }
            rtHasProfissionalCollectionNew = attachedRtHasProfissionalCollectionNew;
            status.setRtHasProfissionalCollection(rtHasProfissionalCollectionNew);
            status = em.merge(status);
            for (Rt rtCollectionNewRt : rtCollectionNew) {
                if (!rtCollectionOld.contains(rtCollectionNewRt)) {
                    Status oldStatusIdOfRtCollectionNewRt = rtCollectionNewRt.getStatusId();
                    rtCollectionNewRt.setStatusId(status);
                    rtCollectionNewRt = em.merge(rtCollectionNewRt);
                    if (oldStatusIdOfRtCollectionNewRt != null && !oldStatusIdOfRtCollectionNewRt.equals(status)) {
                        oldStatusIdOfRtCollectionNewRt.getRtCollection().remove(rtCollectionNewRt);
                        oldStatusIdOfRtCollectionNewRt = em.merge(oldStatusIdOfRtCollectionNewRt);
                    }
                }
            }
            for (RtHasProfissional rtHasProfissionalCollectionNewRtHasProfissional : rtHasProfissionalCollectionNew) {
                if (!rtHasProfissionalCollectionOld.contains(rtHasProfissionalCollectionNewRtHasProfissional)) {
                    Status oldStatusIdOfRtHasProfissionalCollectionNewRtHasProfissional = rtHasProfissionalCollectionNewRtHasProfissional.getStatusId();
                    rtHasProfissionalCollectionNewRtHasProfissional.setStatusId(status);
                    rtHasProfissionalCollectionNewRtHasProfissional = em.merge(rtHasProfissionalCollectionNewRtHasProfissional);
                    if (oldStatusIdOfRtHasProfissionalCollectionNewRtHasProfissional != null && !oldStatusIdOfRtHasProfissionalCollectionNewRtHasProfissional.equals(status)) {
                        oldStatusIdOfRtHasProfissionalCollectionNewRtHasProfissional.getRtHasProfissionalCollection().remove(rtHasProfissionalCollectionNewRtHasProfissional);
                        oldStatusIdOfRtHasProfissionalCollectionNewRtHasProfissional = em.merge(oldStatusIdOfRtHasProfissionalCollectionNewRtHasProfissional);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = status.getId();
                if (findStatus(id) == null) {
                    throw new NonexistentEntityException("The status with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Status status;
            try {
                status = em.getReference(Status.class, id);
                status.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The status with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rt> rtCollectionOrphanCheck = status.getRtCollection();
            for (Rt rtCollectionOrphanCheckRt : rtCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Status (" + status + ") cannot be destroyed since the Rt " + rtCollectionOrphanCheckRt + " in its rtCollection field has a non-nullable statusId field.");
            }
            Collection<RtHasProfissional> rtHasProfissionalCollectionOrphanCheck = status.getRtHasProfissionalCollection();
            for (RtHasProfissional rtHasProfissionalCollectionOrphanCheckRtHasProfissional : rtHasProfissionalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Status (" + status + ") cannot be destroyed since the RtHasProfissional " + rtHasProfissionalCollectionOrphanCheckRtHasProfissional + " in its rtHasProfissionalCollection field has a non-nullable statusId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(status);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Status> findStatusEntities() {
        return findStatusEntities(true, -1, -1);
    }

    public List<Status> findStatusEntities(int maxResults, int firstResult) {
        return findStatusEntities(false, maxResults, firstResult);
    }

    private List<Status> findStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Status.class));
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
    
    public List<Status> findStatusFilter(Status status, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Status> rt = cq.from(Status.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(status.getId() != null && status.getId() != 0){
                predicates.add(cb.equal(rt.get(Status_.id), status.getId()));
            }
            if(status.getNome() != null && !status.getNome().equals("")){
                predicates.add(cb.like(rt.get(Status_.nome), "%" + status.getNome() + "%"));
            }
            cq.where(predicates.toArray(new Predicate[] {}));
            Query q = em.createQuery(cq);
            
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Status findStatus(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Status.class, id);
        } finally {
            em.close();
        }
    }

    public int getStatusCount(Status status) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Status> rt = cq.from(Status.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(status.getId() != null && status.getId() != 0){
                predicates.add(cb.equal(rt.get(Status_.id), status.getId()));
            }
            if(status.getNome() != null && !status.getNome().equals("")){
                predicates.add(cb.like(rt.get(Status_.nome), "%" + status.getNome() + "%"));
            }
            cq.where(predicates.toArray(new Predicate[] {}));
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
