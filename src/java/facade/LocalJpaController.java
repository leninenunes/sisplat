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
import model.Local;
import model.Local_;

/**
 *
 * @author lenine.nunes
 */
public class LocalJpaController implements Serializable {

    public LocalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Local local) {
        if (local.getRtCollection() == null) {
            local.setRtCollection(new ArrayList<Rt>());
        }
        if (local.getRtCollection1() == null) {
            local.setRtCollection1(new ArrayList<Rt>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rt> attachedRtCollection = new ArrayList<Rt>();
            for (Rt rtCollectionRtToAttach : local.getRtCollection()) {
                rtCollectionRtToAttach = em.getReference(rtCollectionRtToAttach.getClass(), rtCollectionRtToAttach.getId());
                attachedRtCollection.add(rtCollectionRtToAttach);
            }
            local.setRtCollection(attachedRtCollection);
            Collection<Rt> attachedRtCollection1 = new ArrayList<Rt>();
            for (Rt rtCollection1RtToAttach : local.getRtCollection1()) {
                rtCollection1RtToAttach = em.getReference(rtCollection1RtToAttach.getClass(), rtCollection1RtToAttach.getId());
                attachedRtCollection1.add(rtCollection1RtToAttach);
            }
            local.setRtCollection1(attachedRtCollection1);
            em.persist(local);
            for (Rt rtCollectionRt : local.getRtCollection()) {
                Local oldLocalEmbarqueIdOfRtCollectionRt = rtCollectionRt.getLocalEmbarqueId();
                rtCollectionRt.setLocalEmbarqueId(local);
                rtCollectionRt = em.merge(rtCollectionRt);
                if (oldLocalEmbarqueIdOfRtCollectionRt != null) {
                    oldLocalEmbarqueIdOfRtCollectionRt.getRtCollection().remove(rtCollectionRt);
                    oldLocalEmbarqueIdOfRtCollectionRt = em.merge(oldLocalEmbarqueIdOfRtCollectionRt);
                }
            }
            for (Rt rtCollection1Rt : local.getRtCollection1()) {
                Local oldLocalDesembarqueIdOfRtCollection1Rt = rtCollection1Rt.getLocalDesembarqueId();
                rtCollection1Rt.setLocalDesembarqueId(local);
                rtCollection1Rt = em.merge(rtCollection1Rt);
                if (oldLocalDesembarqueIdOfRtCollection1Rt != null) {
                    oldLocalDesembarqueIdOfRtCollection1Rt.getRtCollection1().remove(rtCollection1Rt);
                    oldLocalDesembarqueIdOfRtCollection1Rt = em.merge(oldLocalDesembarqueIdOfRtCollection1Rt);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Local local) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Local persistentLocal = em.find(Local.class, local.getId());
            Collection<Rt> rtCollectionOld = persistentLocal.getRtCollection();
            Collection<Rt> rtCollectionNew = local.getRtCollection();
            Collection<Rt> rtCollection1Old = persistentLocal.getRtCollection1();
            Collection<Rt> rtCollection1New = local.getRtCollection1();
            List<String> illegalOrphanMessages = null;
            for (Rt rtCollectionOldRt : rtCollectionOld) {
                if (!rtCollectionNew.contains(rtCollectionOldRt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rt " + rtCollectionOldRt + " since its localEmbarqueId field is not nullable.");
                }
            }
            for (Rt rtCollection1OldRt : rtCollection1Old) {
                if (!rtCollection1New.contains(rtCollection1OldRt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rt " + rtCollection1OldRt + " since its localDesembarqueId field is not nullable.");
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
            local.setRtCollection(rtCollectionNew);
            Collection<Rt> attachedRtCollection1New = new ArrayList<Rt>();
            for (Rt rtCollection1NewRtToAttach : rtCollection1New) {
                rtCollection1NewRtToAttach = em.getReference(rtCollection1NewRtToAttach.getClass(), rtCollection1NewRtToAttach.getId());
                attachedRtCollection1New.add(rtCollection1NewRtToAttach);
            }
            rtCollection1New = attachedRtCollection1New;
            local.setRtCollection1(rtCollection1New);
            local = em.merge(local);
            for (Rt rtCollectionNewRt : rtCollectionNew) {
                if (!rtCollectionOld.contains(rtCollectionNewRt)) {
                    Local oldLocalEmbarqueIdOfRtCollectionNewRt = rtCollectionNewRt.getLocalEmbarqueId();
                    rtCollectionNewRt.setLocalEmbarqueId(local);
                    rtCollectionNewRt = em.merge(rtCollectionNewRt);
                    if (oldLocalEmbarqueIdOfRtCollectionNewRt != null && !oldLocalEmbarqueIdOfRtCollectionNewRt.equals(local)) {
                        oldLocalEmbarqueIdOfRtCollectionNewRt.getRtCollection().remove(rtCollectionNewRt);
                        oldLocalEmbarqueIdOfRtCollectionNewRt = em.merge(oldLocalEmbarqueIdOfRtCollectionNewRt);
                    }
                }
            }
            for (Rt rtCollection1NewRt : rtCollection1New) {
                if (!rtCollection1Old.contains(rtCollection1NewRt)) {
                    Local oldLocalDesembarqueIdOfRtCollection1NewRt = rtCollection1NewRt.getLocalDesembarqueId();
                    rtCollection1NewRt.setLocalDesembarqueId(local);
                    rtCollection1NewRt = em.merge(rtCollection1NewRt);
                    if (oldLocalDesembarqueIdOfRtCollection1NewRt != null && !oldLocalDesembarqueIdOfRtCollection1NewRt.equals(local)) {
                        oldLocalDesembarqueIdOfRtCollection1NewRt.getRtCollection1().remove(rtCollection1NewRt);
                        oldLocalDesembarqueIdOfRtCollection1NewRt = em.merge(oldLocalDesembarqueIdOfRtCollection1NewRt);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = local.getId();
                if (findLocal(id) == null) {
                    throw new NonexistentEntityException("The local with id " + id + " no longer exists.");
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
            Local local;
            try {
                local = em.getReference(Local.class, id);
                local.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The local with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rt> rtCollectionOrphanCheck = local.getRtCollection();
            for (Rt rtCollectionOrphanCheckRt : rtCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Local (" + local + ") cannot be destroyed since the Rt " + rtCollectionOrphanCheckRt + " in its rtCollection field has a non-nullable localEmbarqueId field.");
            }
            Collection<Rt> rtCollection1OrphanCheck = local.getRtCollection1();
            for (Rt rtCollection1OrphanCheckRt : rtCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Local (" + local + ") cannot be destroyed since the Rt " + rtCollection1OrphanCheckRt + " in its rtCollection1 field has a non-nullable localDesembarqueId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(local);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Local> findLocalEntities() {
        return findLocalEntities(true, -1, -1);
    }

    public List<Local> findLocalEntities(int maxResults, int firstResult) {
        return findLocalEntities(false, maxResults, firstResult);
    }

    private List<Local> findLocalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Local.class));
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
    
    public List<Local> findLocalFilter(Local local, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Local> rt = cq.from(Local.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(local.getId() != null && local.getId() != 0){
                predicates.add(cb.equal(rt.get(Local_.id), local.getId()));
            }
            if(local.getTipo() != null && local.getTipo() != 0){
                predicates.add(cb.equal(rt.get(Local_.tipo), local.getTipo()));
            }
            if(local.getNome() != null && !local.getNome().equals("")){
                predicates.add(cb.like(rt.get(Local_.nome), "%" + local.getNome() + "%"));
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

    public Local findLocal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Local.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalCount(Local local) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Local> rt = cq.from(Local.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(local.getId() != null && local.getId() != 0){
                predicates.add(cb.equal(rt.get(Local_.id), local.getId()));
            }
            if(local.getTipo() != null && local.getTipo() != 0){
                predicates.add(cb.equal(rt.get(Local_.tipo), local.getTipo()));
            }
            if(local.getNome() != null && !local.getNome().equals("")){
                predicates.add(cb.like(rt.get(Local_.nome), "%" + local.getNome() + "%"));
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
