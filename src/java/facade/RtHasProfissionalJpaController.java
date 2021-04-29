/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import facade.exceptions.NonexistentEntityException;
import facade.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.Profissional;
import model.Rt;
import model.RtHasProfissional;
import model.RtHasProfissionalPK;
import model.RtHasProfissional_;
import model.Status;

/**
 *
 * @author lenine.nunes
 */
public class RtHasProfissionalJpaController implements Serializable {

    public RtHasProfissionalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RtHasProfissional rtHasProfissional) throws PreexistingEntityException, Exception {
        if (rtHasProfissional.getRtHasProfissionalPK() == null) {
            rtHasProfissional.setRtHasProfissionalPK(new RtHasProfissionalPK());
        }
        rtHasProfissional.getRtHasProfissionalPK().setRtId(rtHasProfissional.getRt().getId());
        rtHasProfissional.getRtHasProfissionalPK().setProfissionalId(rtHasProfissional.getProfissional().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profissional profissional = rtHasProfissional.getProfissional();
            if (profissional != null) {
                profissional = em.getReference(profissional.getClass(), profissional.getId());
                rtHasProfissional.setProfissional(profissional);
            }
            Rt rt = rtHasProfissional.getRt();
            if (rt != null) {
                rt = em.getReference(rt.getClass(), rt.getId());
                rtHasProfissional.setRt(rt);
            }
            Status statusId = rtHasProfissional.getStatusId();
            if (statusId != null) {
                statusId = em.getReference(statusId.getClass(), statusId.getId());
                rtHasProfissional.setStatusId(statusId);
            }
            em.persist(rtHasProfissional);
            if (profissional != null) {
                profissional.getRtHasProfissionalCollection().add(rtHasProfissional);
                profissional = em.merge(profissional);
            }
            if (rt != null) {
                rt.getRtHasProfissionalCollection().add(rtHasProfissional);
                rt = em.merge(rt);
            }
            if (statusId != null) {
                statusId.getRtHasProfissionalCollection().add(rtHasProfissional);
                statusId = em.merge(statusId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRtHasProfissional(rtHasProfissional.getRtHasProfissionalPK()) != null) {
                throw new PreexistingEntityException("RtHasProfissional " + rtHasProfissional + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RtHasProfissional rtHasProfissional) throws NonexistentEntityException, Exception {
        rtHasProfissional.getRtHasProfissionalPK().setRtId(rtHasProfissional.getRt().getId());
        rtHasProfissional.getRtHasProfissionalPK().setProfissionalId(rtHasProfissional.getProfissional().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RtHasProfissional persistentRtHasProfissional = em.find(RtHasProfissional.class, rtHasProfissional.getRtHasProfissionalPK());
            Profissional profissionalOld = persistentRtHasProfissional.getProfissional();
            Profissional profissionalNew = rtHasProfissional.getProfissional();
            Rt rtOld = persistentRtHasProfissional.getRt();
            Rt rtNew = rtHasProfissional.getRt();
            Status statusIdOld = persistentRtHasProfissional.getStatusId();
            Status statusIdNew = rtHasProfissional.getStatusId();
            if (profissionalNew != null) {
                profissionalNew = em.getReference(profissionalNew.getClass(), profissionalNew.getId());
                rtHasProfissional.setProfissional(profissionalNew);
            }
            if (rtNew != null) {
                rtNew = em.getReference(rtNew.getClass(), rtNew.getId());
                rtHasProfissional.setRt(rtNew);
            }
            if (statusIdNew != null) {
                statusIdNew = em.getReference(statusIdNew.getClass(), statusIdNew.getId());
                rtHasProfissional.setStatusId(statusIdNew);
            }
            rtHasProfissional = em.merge(rtHasProfissional);
            if (profissionalOld != null && !profissionalOld.equals(profissionalNew)) {
                profissionalOld.getRtHasProfissionalCollection().remove(rtHasProfissional);
                profissionalOld = em.merge(profissionalOld);
            }
            if (profissionalNew != null && !profissionalNew.equals(profissionalOld)) {
                profissionalNew.getRtHasProfissionalCollection().add(rtHasProfissional);
                profissionalNew = em.merge(profissionalNew);
            }
            if (rtOld != null && !rtOld.equals(rtNew)) {
                rtOld.getRtHasProfissionalCollection().remove(rtHasProfissional);
                rtOld = em.merge(rtOld);
            }
            if (rtNew != null && !rtNew.equals(rtOld)) {
                rtNew.getRtHasProfissionalCollection().add(rtHasProfissional);
                rtNew = em.merge(rtNew);
            }
            if (statusIdOld != null && !statusIdOld.equals(statusIdNew)) {
                statusIdOld.getRtHasProfissionalCollection().remove(rtHasProfissional);
                statusIdOld = em.merge(statusIdOld);
            }
            if (statusIdNew != null && !statusIdNew.equals(statusIdOld)) {
                statusIdNew.getRtHasProfissionalCollection().add(rtHasProfissional);
                statusIdNew = em.merge(statusIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RtHasProfissionalPK id = rtHasProfissional.getRtHasProfissionalPK();
                if (findRtHasProfissional(id) == null) {
                    throw new NonexistentEntityException("The rtHasProfissional with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RtHasProfissionalPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RtHasProfissional rtHasProfissional;
            try {
                rtHasProfissional = em.getReference(RtHasProfissional.class, id);
                rtHasProfissional.getRtHasProfissionalPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rtHasProfissional with id " + id + " no longer exists.", enfe);
            }
            Profissional profissional = rtHasProfissional.getProfissional();
            if (profissional != null) {
                profissional.getRtHasProfissionalCollection().remove(rtHasProfissional);
                profissional = em.merge(profissional);
            }
            Rt rt = rtHasProfissional.getRt();
            if (rt != null) {
                rt.getRtHasProfissionalCollection().remove(rtHasProfissional);
                rt = em.merge(rt);
            }
            Status statusId = rtHasProfissional.getStatusId();
            if (statusId != null) {
                statusId.getRtHasProfissionalCollection().remove(rtHasProfissional);
                statusId = em.merge(statusId);
            }
            em.remove(rtHasProfissional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RtHasProfissional> findRtHasProfissionalEntities() {
        return findRtHasProfissionalEntities(true, -1, -1);
    }

    public List<RtHasProfissional> findRtHasProfissionalEntities(int maxResults, int firstResult) {
        return findRtHasProfissionalEntities(false, maxResults, firstResult);
    }

    private List<RtHasProfissional> findRtHasProfissionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RtHasProfissional.class));
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
    
    public List<RtHasProfissional> findRtHasProfissionalFilter(RtHasProfissional rtHasProfissional, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<RtHasProfissional> rt = cq.from(RtHasProfissional.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
//            if(rtHasProfissional.getRt() != null && rtHasProfissional.getRt().getId() != 0){
//                predicates.add(cb.equal(rt.get(RtHasProfissional_.rt.getName()), rtHasProfissional.getRt().getId()));
//            }
//            if(rtHasProfissional.getProfissional().getId() != null && !rtHasProfissional.getProfissional().getNome().equals("")){
//                predicates.add(cb.like(rt.get(RtHasProfissional_.profissional.getName()), "%" + rtHasProfissional.getProfissional().getNome() + "%"));
//            }
            cq.where(predicates.toArray(new Predicate[] {}));
            Query q = em.createQuery(cq);
            
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RtHasProfissional findRtHasProfissional(RtHasProfissionalPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RtHasProfissional.class, id);
        } finally {
            em.close();
        }
    }

    public int getRtHasProfissionalCount(RtHasProfissional rtHasProfissional) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RtHasProfissional> rt = cq.from(RtHasProfissional.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
//            if(rtHasProfissional.getRt().getId() != null && rtHasProfissional.getRt().getId() != 0){
//                predicates.add(cb.equal(rt.get(RtHasProfissional_.rt.getName()), rtHasProfissional.getRt().getId()));
//            }
//            if(rtHasProfissional.getProfissional().getId() != null && !rtHasProfissional.getProfissional().getNome().equals("")){
//                predicates.add(cb.like(rt.get(RtHasProfissional_.profissional.getName()), "%" + rtHasProfissional.getProfissional().getNome() + "%"));
//            }
            cq.where(predicates.toArray(new Predicate[] {}));
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
