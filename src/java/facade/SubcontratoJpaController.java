/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import facade.exceptions.NonexistentEntityException;
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
import model.Subcontrato;
import model.Subcontrato_;

/**
 *
 * @author lenine.nunes
 */
public class SubcontratoJpaController implements Serializable {

    public SubcontratoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subcontrato subcontrato) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(subcontrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subcontrato subcontrato) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            subcontrato = em.merge(subcontrato);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subcontrato.getId();
                if (findSubcontrato(id) == null) {
                    throw new NonexistentEntityException("The subcontrato with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subcontrato subcontrato;
            try {
                subcontrato = em.getReference(Subcontrato.class, id);
                subcontrato.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subcontrato with id " + id + " no longer exists.", enfe);
            }
            em.remove(subcontrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subcontrato> findSubcontratoEntities() {
        return findSubcontratoEntities(true, -1, -1);
    }

    public List<Subcontrato> findSubcontratoEntities(int maxResults, int firstResult) {
        return findSubcontratoEntities(false, maxResults, firstResult);
    }

    private List<Subcontrato> findSubcontratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subcontrato.class));
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
    
    public List<Subcontrato> findSubcontratoFilter(Subcontrato subcontrato, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Subcontrato> rt = cq.from(Subcontrato.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(subcontrato.getId() != null && subcontrato.getId() != 0){
                predicates.add(cb.equal(rt.get(Subcontrato_.id), subcontrato.getId()));
            }
            if(subcontrato.getDescricao() != null && !subcontrato.getDescricao().equals("")){
                predicates.add(cb.like(rt.get(Subcontrato_.descricao), "%" + subcontrato.getDescricao() + "%"));
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

    public Subcontrato findSubcontrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subcontrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubcontratoCount(Subcontrato subcontrato) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subcontrato> rt = cq.from(Subcontrato.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(subcontrato.getId() != null && subcontrato.getId() != 0){
                predicates.add(cb.equal(rt.get(Subcontrato_.id), subcontrato.getId()));
            }
            if(subcontrato.getDescricao() != null && !subcontrato.getDescricao().equals("")){
                predicates.add(cb.like(rt.get(Subcontrato_.descricao), "%" + subcontrato.getDescricao() + "%"));
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
