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
import model.Escopo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import model.UnidadeMedida;
import model.UnidadeMedida_;

/**
 *
 * @author lenine.nunes
 */
public class UnidadeMedidaJpaController implements Serializable {

    public UnidadeMedidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UnidadeMedida unidadeMedida) {
        if (unidadeMedida.getEscopoCollection() == null) {
            unidadeMedida.setEscopoCollection(new ArrayList<Escopo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Escopo> attachedEscopoCollection = new ArrayList<Escopo>();
            for (Escopo escopoCollectionEscopoToAttach : unidadeMedida.getEscopoCollection()) {
                escopoCollectionEscopoToAttach = em.getReference(escopoCollectionEscopoToAttach.getClass(), escopoCollectionEscopoToAttach.getId());
                attachedEscopoCollection.add(escopoCollectionEscopoToAttach);
            }
            unidadeMedida.setEscopoCollection(attachedEscopoCollection);
            em.persist(unidadeMedida);
            for (Escopo escopoCollectionEscopo : unidadeMedida.getEscopoCollection()) {
                UnidadeMedida oldUnidadeMedidaIdOfEscopoCollectionEscopo = escopoCollectionEscopo.getUnidadeMedidaId();
                escopoCollectionEscopo.setUnidadeMedidaId(unidadeMedida);
                escopoCollectionEscopo = em.merge(escopoCollectionEscopo);
                if (oldUnidadeMedidaIdOfEscopoCollectionEscopo != null) {
                    oldUnidadeMedidaIdOfEscopoCollectionEscopo.getEscopoCollection().remove(escopoCollectionEscopo);
                    oldUnidadeMedidaIdOfEscopoCollectionEscopo = em.merge(oldUnidadeMedidaIdOfEscopoCollectionEscopo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UnidadeMedida unidadeMedida) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UnidadeMedida persistentUnidadeMedida = em.find(UnidadeMedida.class, unidadeMedida.getId());
            Collection<Escopo> escopoCollectionOld = persistentUnidadeMedida.getEscopoCollection();
            Collection<Escopo> escopoCollectionNew = unidadeMedida.getEscopoCollection();
            List<String> illegalOrphanMessages = null;
            for (Escopo escopoCollectionOldEscopo : escopoCollectionOld) {
                if (!escopoCollectionNew.contains(escopoCollectionOldEscopo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Escopo " + escopoCollectionOldEscopo + " since its unidadeMedidaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Escopo> attachedEscopoCollectionNew = new ArrayList<Escopo>();
            for (Escopo escopoCollectionNewEscopoToAttach : escopoCollectionNew) {
                escopoCollectionNewEscopoToAttach = em.getReference(escopoCollectionNewEscopoToAttach.getClass(), escopoCollectionNewEscopoToAttach.getId());
                attachedEscopoCollectionNew.add(escopoCollectionNewEscopoToAttach);
            }
            escopoCollectionNew = attachedEscopoCollectionNew;
            unidadeMedida.setEscopoCollection(escopoCollectionNew);
            unidadeMedida = em.merge(unidadeMedida);
            for (Escopo escopoCollectionNewEscopo : escopoCollectionNew) {
                if (!escopoCollectionOld.contains(escopoCollectionNewEscopo)) {
                    UnidadeMedida oldUnidadeMedidaIdOfEscopoCollectionNewEscopo = escopoCollectionNewEscopo.getUnidadeMedidaId();
                    escopoCollectionNewEscopo.setUnidadeMedidaId(unidadeMedida);
                    escopoCollectionNewEscopo = em.merge(escopoCollectionNewEscopo);
                    if (oldUnidadeMedidaIdOfEscopoCollectionNewEscopo != null && !oldUnidadeMedidaIdOfEscopoCollectionNewEscopo.equals(unidadeMedida)) {
                        oldUnidadeMedidaIdOfEscopoCollectionNewEscopo.getEscopoCollection().remove(escopoCollectionNewEscopo);
                        oldUnidadeMedidaIdOfEscopoCollectionNewEscopo = em.merge(oldUnidadeMedidaIdOfEscopoCollectionNewEscopo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = unidadeMedida.getId();
                if (findUnidadeMedida(id) == null) {
                    throw new NonexistentEntityException("The unidadeMedida with id " + id + " no longer exists.");
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
            UnidadeMedida unidadeMedida;
            try {
                unidadeMedida = em.getReference(UnidadeMedida.class, id);
                unidadeMedida.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The unidadeMedida with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Escopo> escopoCollectionOrphanCheck = unidadeMedida.getEscopoCollection();
            for (Escopo escopoCollectionOrphanCheckEscopo : escopoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UnidadeMedida (" + unidadeMedida + ") cannot be destroyed since the Escopo " + escopoCollectionOrphanCheckEscopo + " in its escopoCollection field has a non-nullable unidadeMedidaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(unidadeMedida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UnidadeMedida> findUnidadeMedidaEntities() {
        return findUnidadeMedidaEntities(true, -1, -1);
    }

    public List<UnidadeMedida> findUnidadeMedidaEntities(int maxResults, int firstResult) {
        return findUnidadeMedidaEntities(false, maxResults, firstResult);
    }

    private List<UnidadeMedida> findUnidadeMedidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UnidadeMedida.class));
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
    
    public List<UnidadeMedida> findUnidadeMedidaFilter(UnidadeMedida unidadeMedida, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<UnidadeMedida> rt = cq.from(UnidadeMedida.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(unidadeMedida.getId() != null && unidadeMedida.getId() != 0){
                predicates.add(cb.equal(rt.get(UnidadeMedida_.id), unidadeMedida.getId()));
            }
            if(unidadeMedida.getNome() != null && !unidadeMedida.getNome().equals("")){
                predicates.add(cb.like(rt.get(UnidadeMedida_.nome), "%" + unidadeMedida.getNome() + "%"));
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

    public UnidadeMedida findUnidadeMedida(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UnidadeMedida.class, id);
        } finally {
            em.close();
        }
    }

    public int getUnidadeMedidaCount(UnidadeMedida unidadeMedida) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UnidadeMedida> rt = cq.from(UnidadeMedida.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(unidadeMedida.getId() != null && unidadeMedida.getId() != 0){
                predicates.add(cb.equal(rt.get(UnidadeMedida_.id), unidadeMedida.getId()));
            }
            if(unidadeMedida.getNome() != null && !unidadeMedida.getNome().equals("")){
                predicates.add(cb.like(rt.get(UnidadeMedida_.nome), "%" + unidadeMedida.getNome() + "%"));
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
