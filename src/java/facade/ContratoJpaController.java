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
import model.Contrato;
import model.Contrato_;

/**
 *
 * @author lenine.nunes
 */
public class ContratoJpaController implements Serializable {

    public ContratoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contrato contrato) {
        if (contrato.getEscopoCollection() == null) {
            contrato.setEscopoCollection(new ArrayList<Escopo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Escopo> attachedEscopoCollection = new ArrayList<Escopo>();
            for (Escopo escopoCollectionEscopoToAttach : contrato.getEscopoCollection()) {
                escopoCollectionEscopoToAttach = em.getReference(escopoCollectionEscopoToAttach.getClass(), escopoCollectionEscopoToAttach.getId());
                attachedEscopoCollection.add(escopoCollectionEscopoToAttach);
            }
            contrato.setEscopoCollection(attachedEscopoCollection);
            em.persist(contrato);
            for (Escopo escopoCollectionEscopo : contrato.getEscopoCollection()) {
                Contrato oldContratoIdOfEscopoCollectionEscopo = escopoCollectionEscopo.getContratoId();
                escopoCollectionEscopo.setContratoId(contrato);
                escopoCollectionEscopo = em.merge(escopoCollectionEscopo);
                if (oldContratoIdOfEscopoCollectionEscopo != null) {
                    oldContratoIdOfEscopoCollectionEscopo.getEscopoCollection().remove(escopoCollectionEscopo);
                    oldContratoIdOfEscopoCollectionEscopo = em.merge(oldContratoIdOfEscopoCollectionEscopo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contrato contrato) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getId());
            Collection<Escopo> escopoCollectionOld = persistentContrato.getEscopoCollection();
            Collection<Escopo> escopoCollectionNew = contrato.getEscopoCollection();
            List<String> illegalOrphanMessages = null;
            for (Escopo escopoCollectionOldEscopo : escopoCollectionOld) {
                if (!escopoCollectionNew.contains(escopoCollectionOldEscopo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Escopo " + escopoCollectionOldEscopo + " since its contratoId field is not nullable.");
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
            contrato.setEscopoCollection(escopoCollectionNew);
            contrato = em.merge(contrato);
            for (Escopo escopoCollectionNewEscopo : escopoCollectionNew) {
                if (!escopoCollectionOld.contains(escopoCollectionNewEscopo)) {
                    Contrato oldContratoIdOfEscopoCollectionNewEscopo = escopoCollectionNewEscopo.getContratoId();
                    escopoCollectionNewEscopo.setContratoId(contrato);
                    escopoCollectionNewEscopo = em.merge(escopoCollectionNewEscopo);
                    if (oldContratoIdOfEscopoCollectionNewEscopo != null && !oldContratoIdOfEscopoCollectionNewEscopo.equals(contrato)) {
                        oldContratoIdOfEscopoCollectionNewEscopo.getEscopoCollection().remove(escopoCollectionNewEscopo);
                        oldContratoIdOfEscopoCollectionNewEscopo = em.merge(oldContratoIdOfEscopoCollectionNewEscopo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contrato.getId();
                if (findContrato(id) == null) {
                    throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.");
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
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Escopo> escopoCollectionOrphanCheck = contrato.getEscopoCollection();
            for (Escopo escopoCollectionOrphanCheckEscopo : escopoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the Escopo " + escopoCollectionOrphanCheckEscopo + " in its escopoCollection field has a non-nullable contratoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(contrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contrato> findContratoEntities() {
        return findContratoEntities(true, -1, -1);
    }

    public List<Contrato> findContratoEntities(int maxResults, int firstResult) {
        return findContratoEntities(false, maxResults, firstResult);
    }

    private List<Contrato> findContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contrato.class));
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
    
    public List<Contrato> findContratoFilter(Contrato contrato, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Contrato> rt = cq.from(Contrato.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(contrato.getId() != null && contrato.getId() != 0){
                predicates.add(cb.equal(rt.get(Contrato_.id), contrato.getId()));
            }
            if(contrato.getDescricao() != null && !contrato.getDescricao().equals("")){
                predicates.add(cb.like(rt.get(Contrato_.descricao), "%" + contrato.getDescricao() + "%"));
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

    public Contrato findContrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratoCount(Contrato contrato) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contrato> rt = cq.from(Contrato.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(contrato.getId() != null && contrato.getId() != 0){
                predicates.add(cb.equal(rt.get(Contrato_.id), contrato.getId()));
            }
            if(contrato.getDescricao() != null && !contrato.getDescricao().equals("")){
                predicates.add(cb.like(rt.get(Contrato_.descricao), "%" + contrato.getDescricao() + "%"));
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
