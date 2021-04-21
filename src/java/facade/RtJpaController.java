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
import model.Local;
import model.Status;
import model.RtHasProfissional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import model.Rt;
import model.Rt_;

/**
 *
 * @author lenine.nunes
 */
public class RtJpaController implements Serializable {

    public RtJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rt rt) {
        if (rt.getRtHasProfissionalCollection() == null) {
            rt.setRtHasProfissionalCollection(new ArrayList<RtHasProfissional>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Local localEmbarqueId = rt.getLocalEmbarqueId();
            if (localEmbarqueId != null) {
                localEmbarqueId = em.getReference(localEmbarqueId.getClass(), localEmbarqueId.getId());
                rt.setLocalEmbarqueId(localEmbarqueId);
            }
            Local localDesembarqueId = rt.getLocalDesembarqueId();
            if (localDesembarqueId != null) {
                localDesembarqueId = em.getReference(localDesembarqueId.getClass(), localDesembarqueId.getId());
                rt.setLocalDesembarqueId(localDesembarqueId);
            }
            Status statusId = rt.getStatusId();
            if (statusId != null) {
                statusId = em.getReference(statusId.getClass(), statusId.getId());
                rt.setStatusId(statusId);
            }
            Collection<RtHasProfissional> attachedRtHasProfissionalCollection = new ArrayList<RtHasProfissional>();
            for (RtHasProfissional rtHasProfissionalCollectionRtHasProfissionalToAttach : rt.getRtHasProfissionalCollection()) {
                rtHasProfissionalCollectionRtHasProfissionalToAttach = em.getReference(rtHasProfissionalCollectionRtHasProfissionalToAttach.getClass(), rtHasProfissionalCollectionRtHasProfissionalToAttach.getRtHasProfissionalPK());
                attachedRtHasProfissionalCollection.add(rtHasProfissionalCollectionRtHasProfissionalToAttach);
            }
            rt.setRtHasProfissionalCollection(attachedRtHasProfissionalCollection);
            em.persist(rt);
            if (localEmbarqueId != null) {
                localEmbarqueId.getRtCollection().add(rt);
                localEmbarqueId = em.merge(localEmbarqueId);
            }
            if (localDesembarqueId != null) {
                localDesembarqueId.getRtCollection().add(rt);
                localDesembarqueId = em.merge(localDesembarqueId);
            }
            if (statusId != null) {
                statusId.getRtCollection().add(rt);
                statusId = em.merge(statusId);
            }
            for (RtHasProfissional rtHasProfissionalCollectionRtHasProfissional : rt.getRtHasProfissionalCollection()) {
                Rt oldRtOfRtHasProfissionalCollectionRtHasProfissional = rtHasProfissionalCollectionRtHasProfissional.getRt();
                rtHasProfissionalCollectionRtHasProfissional.setRt(rt);
                rtHasProfissionalCollectionRtHasProfissional = em.merge(rtHasProfissionalCollectionRtHasProfissional);
                if (oldRtOfRtHasProfissionalCollectionRtHasProfissional != null) {
                    oldRtOfRtHasProfissionalCollectionRtHasProfissional.getRtHasProfissionalCollection().remove(rtHasProfissionalCollectionRtHasProfissional);
                    oldRtOfRtHasProfissionalCollectionRtHasProfissional = em.merge(oldRtOfRtHasProfissionalCollectionRtHasProfissional);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rt rt) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rt persistentRt = em.find(Rt.class, rt.getId());
            Local localEmbarqueIdOld = persistentRt.getLocalEmbarqueId();
            Local localEmbarqueIdNew = rt.getLocalEmbarqueId();
            Local localDesembarqueIdOld = persistentRt.getLocalDesembarqueId();
            Local localDesembarqueIdNew = rt.getLocalDesembarqueId();
            Status statusIdOld = persistentRt.getStatusId();
            Status statusIdNew = rt.getStatusId();
            Collection<RtHasProfissional> rtHasProfissionalCollectionOld = persistentRt.getRtHasProfissionalCollection();
            Collection<RtHasProfissional> rtHasProfissionalCollectionNew = rt.getRtHasProfissionalCollection();
            List<String> illegalOrphanMessages = null;
            for (RtHasProfissional rtHasProfissionalCollectionOldRtHasProfissional : rtHasProfissionalCollectionOld) {
                if (!rtHasProfissionalCollectionNew.contains(rtHasProfissionalCollectionOldRtHasProfissional)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RtHasProfissional " + rtHasProfissionalCollectionOldRtHasProfissional + " since its rt field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (localEmbarqueIdNew != null) {
                localEmbarqueIdNew = em.getReference(localEmbarqueIdNew.getClass(), localEmbarqueIdNew.getId());
                rt.setLocalEmbarqueId(localEmbarqueIdNew);
            }
            if (localDesembarqueIdNew != null) {
                localDesembarqueIdNew = em.getReference(localDesembarqueIdNew.getClass(), localDesembarqueIdNew.getId());
                rt.setLocalDesembarqueId(localDesembarqueIdNew);
            }
            if (statusIdNew != null) {
                statusIdNew = em.getReference(statusIdNew.getClass(), statusIdNew.getId());
                rt.setStatusId(statusIdNew);
            }
            Collection<RtHasProfissional> attachedRtHasProfissionalCollectionNew = new ArrayList<RtHasProfissional>();
            for (RtHasProfissional rtHasProfissionalCollectionNewRtHasProfissionalToAttach : rtHasProfissionalCollectionNew) {
                rtHasProfissionalCollectionNewRtHasProfissionalToAttach = em.getReference(rtHasProfissionalCollectionNewRtHasProfissionalToAttach.getClass(), rtHasProfissionalCollectionNewRtHasProfissionalToAttach.getRtHasProfissionalPK());
                attachedRtHasProfissionalCollectionNew.add(rtHasProfissionalCollectionNewRtHasProfissionalToAttach);
            }
            rtHasProfissionalCollectionNew = attachedRtHasProfissionalCollectionNew;
            rt.setRtHasProfissionalCollection(rtHasProfissionalCollectionNew);
            rt = em.merge(rt);
            if (localEmbarqueIdOld != null && !localEmbarqueIdOld.equals(localEmbarqueIdNew)) {
                localEmbarqueIdOld.getRtCollection().remove(rt);
                localEmbarqueIdOld = em.merge(localEmbarqueIdOld);
            }
            if (localEmbarqueIdNew != null && !localEmbarqueIdNew.equals(localEmbarqueIdOld)) {
                localEmbarqueIdNew.getRtCollection().add(rt);
                localEmbarqueIdNew = em.merge(localEmbarqueIdNew);
            }
            if (localDesembarqueIdOld != null && !localDesembarqueIdOld.equals(localDesembarqueIdNew)) {
                localDesembarqueIdOld.getRtCollection().remove(rt);
                localDesembarqueIdOld = em.merge(localDesembarqueIdOld);
            }
            if (localDesembarqueIdNew != null && !localDesembarqueIdNew.equals(localDesembarqueIdOld)) {
                localDesembarqueIdNew.getRtCollection().add(rt);
                localDesembarqueIdNew = em.merge(localDesembarqueIdNew);
            }
            if (statusIdOld != null && !statusIdOld.equals(statusIdNew)) {
                statusIdOld.getRtCollection().remove(rt);
                statusIdOld = em.merge(statusIdOld);
            }
            if (statusIdNew != null && !statusIdNew.equals(statusIdOld)) {
                statusIdNew.getRtCollection().add(rt);
                statusIdNew = em.merge(statusIdNew);
            }
            for (RtHasProfissional rtHasProfissionalCollectionNewRtHasProfissional : rtHasProfissionalCollectionNew) {
                if (!rtHasProfissionalCollectionOld.contains(rtHasProfissionalCollectionNewRtHasProfissional)) {
                    Rt oldRtOfRtHasProfissionalCollectionNewRtHasProfissional = rtHasProfissionalCollectionNewRtHasProfissional.getRt();
                    rtHasProfissionalCollectionNewRtHasProfissional.setRt(rt);
                    rtHasProfissionalCollectionNewRtHasProfissional = em.merge(rtHasProfissionalCollectionNewRtHasProfissional);
                    if (oldRtOfRtHasProfissionalCollectionNewRtHasProfissional != null && !oldRtOfRtHasProfissionalCollectionNewRtHasProfissional.equals(rt)) {
                        oldRtOfRtHasProfissionalCollectionNewRtHasProfissional.getRtHasProfissionalCollection().remove(rtHasProfissionalCollectionNewRtHasProfissional);
                        oldRtOfRtHasProfissionalCollectionNewRtHasProfissional = em.merge(oldRtOfRtHasProfissionalCollectionNewRtHasProfissional);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rt.getId();
                if (findRt(id) == null) {
                    throw new NonexistentEntityException("The rt with id " + id + " no longer exists.");
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
            Rt rt;
            try {
                rt = em.getReference(Rt.class, id);
                rt.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rt with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RtHasProfissional> rtHasProfissionalCollectionOrphanCheck = rt.getRtHasProfissionalCollection();
            for (RtHasProfissional rtHasProfissionalCollectionOrphanCheckRtHasProfissional : rtHasProfissionalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rt (" + rt + ") cannot be destroyed since the RtHasProfissional " + rtHasProfissionalCollectionOrphanCheckRtHasProfissional + " in its rtHasProfissionalCollection field has a non-nullable rt field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Local localEmbarqueId = rt.getLocalEmbarqueId();
            if (localEmbarqueId != null) {
                localEmbarqueId.getRtCollection().remove(rt);
                localEmbarqueId = em.merge(localEmbarqueId);
            }
            Local localDesembarqueId = rt.getLocalDesembarqueId();
            if (localDesembarqueId != null) {
                localDesembarqueId.getRtCollection().remove(rt);
                localDesembarqueId = em.merge(localDesembarqueId);
            }
            Status statusId = rt.getStatusId();
            if (statusId != null) {
                statusId.getRtCollection().remove(rt);
                statusId = em.merge(statusId);
            }
            em.remove(rt);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rt> findRtEntities() {
        return findRtEntities(true, -1, -1);
    }

    public List<Rt> findRtEntities(int maxResults, int firstResult) {
        return findRtEntities(false, maxResults, firstResult);
    }

    private List<Rt> findRtEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rt.class));
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
    
    public List<Rt> findRtFilter(Rt rt, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Rt> root = cq.from(Rt.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(rt.getId() != null && rt.getId() != 0){
                predicates.add(cb.equal(root.get(Rt_.id), rt.getId()));
            }
            if(rt.getLocalEmbarqueId() != null){
                predicates.add(cb.equal(root.get(Rt_.localEmbarqueId), rt.getLocalEmbarqueId()));
            }
            if(rt.getLocalDesembarqueId() != null){
                predicates.add(cb.equal(root.get(Rt_.localDesembarqueId), rt.getLocalDesembarqueId()));
            }
            if(rt.getStatusId() != null){
                predicates.add(cb.equal(root.get(Rt_.statusId), rt.getStatusId()));
            }
            if(rt.getTipo() != null && rt.getTipo() != 0){
                predicates.add(cb.equal(root.get(Rt_.tipo), rt.getTipo()));
            }
            if(rt.getDataViagem() != null){
                predicates.add(cb.equal(root.get(Rt_.dataViagem), rt.getDataViagem()));
            }
            if(rt.getHoraViagem() != null){
                predicates.add(cb.equal(root.get(Rt_.horaViagem), rt.getHoraViagem()));
            }
            if(rt.getComentario() != null && !rt.getComentario().equals("")){
                predicates.add(cb.like(root.get(Rt_.comentario), "%" + rt.getComentario() + "%"));
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

    public Rt findRt(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rt.class, id);
        } finally {
            em.close();
        }
    }

    public int getRtCount(Rt rt) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rt> root = cq.from(Rt.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(rt.getId() != null && rt.getId() != 0){
                predicates.add(cb.equal(root.get(Rt_.id), rt.getId()));
            }
            if(rt.getLocalEmbarqueId() != null){
                predicates.add(cb.equal(root.get(Rt_.localEmbarqueId), rt.getLocalEmbarqueId()));
            }
            if(rt.getLocalDesembarqueId() != null){
                predicates.add(cb.equal(root.get(Rt_.localDesembarqueId), rt.getLocalDesembarqueId()));
            }
            if(rt.getStatusId() != null){
                predicates.add(cb.equal(root.get(Rt_.statusId), rt.getStatusId()));
            }
            if(rt.getTipo() != null && rt.getTipo() != 0){
                predicates.add(cb.equal(root.get(Rt_.tipo), rt.getTipo()));
            }
            if(rt.getDataViagem() != null){
                predicates.add(cb.equal(root.get(Rt_.dataViagem), rt.getDataViagem()));
            }
            if(rt.getHoraViagem() != null){
                predicates.add(cb.equal(root.get(Rt_.horaViagem), rt.getHoraViagem()));
            }
            if(rt.getComentario() != null && !rt.getComentario().equals("")){
                predicates.add(cb.like(root.get(Rt_.comentario), "%" + rt.getComentario() + "%"));
            }
            cq.where(predicates.toArray(new Predicate[] {}));
            cq.select(em.getCriteriaBuilder().count(root));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
