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
import model.Contrato;
import model.UnidadeMedida;
import model.Apropriacao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import model.Escopo;
import model.Escopo_;

/**
 *
 * @author lenine.nunes
 */
public class EscopoJpaController implements Serializable {

    public EscopoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Escopo escopo) {
        if (escopo.getApropriacaoCollection() == null) {
            escopo.setApropriacaoCollection(new ArrayList<Apropriacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato contratoId = escopo.getContratoId();
            if (contratoId != null) {
                contratoId = em.getReference(contratoId.getClass(), contratoId.getId());
                escopo.setContratoId(contratoId);
            }
            UnidadeMedida unidadeMedidaId = escopo.getUnidadeMedidaId();
            if (unidadeMedidaId != null) {
                unidadeMedidaId = em.getReference(unidadeMedidaId.getClass(), unidadeMedidaId.getId());
                escopo.setUnidadeMedidaId(unidadeMedidaId);
            }
            Collection<Apropriacao> attachedApropriacaoCollection = new ArrayList<Apropriacao>();
            for (Apropriacao apropriacaoCollectionApropriacaoToAttach : escopo.getApropriacaoCollection()) {
                apropriacaoCollectionApropriacaoToAttach = em.getReference(apropriacaoCollectionApropriacaoToAttach.getClass(), apropriacaoCollectionApropriacaoToAttach.getId());
                attachedApropriacaoCollection.add(apropriacaoCollectionApropriacaoToAttach);
            }
            escopo.setApropriacaoCollection(attachedApropriacaoCollection);
            em.persist(escopo);
            if (contratoId != null) {
                contratoId.getEscopoCollection().add(escopo);
                contratoId = em.merge(contratoId);
            }
            if (unidadeMedidaId != null) {
                unidadeMedidaId.getEscopoCollection().add(escopo);
                unidadeMedidaId = em.merge(unidadeMedidaId);
            }
            for (Apropriacao apropriacaoCollectionApropriacao : escopo.getApropriacaoCollection()) {
                Escopo oldEscopoIdOfApropriacaoCollectionApropriacao = apropriacaoCollectionApropriacao.getEscopoId();
                apropriacaoCollectionApropriacao.setEscopoId(escopo);
                apropriacaoCollectionApropriacao = em.merge(apropriacaoCollectionApropriacao);
                if (oldEscopoIdOfApropriacaoCollectionApropriacao != null) {
                    oldEscopoIdOfApropriacaoCollectionApropriacao.getApropriacaoCollection().remove(apropriacaoCollectionApropriacao);
                    oldEscopoIdOfApropriacaoCollectionApropriacao = em.merge(oldEscopoIdOfApropriacaoCollectionApropriacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Escopo escopo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escopo persistentEscopo = em.find(Escopo.class, escopo.getId());
            Contrato contratoIdOld = persistentEscopo.getContratoId();
            Contrato contratoIdNew = escopo.getContratoId();
            UnidadeMedida unidadeMedidaIdOld = persistentEscopo.getUnidadeMedidaId();
            UnidadeMedida unidadeMedidaIdNew = escopo.getUnidadeMedidaId();
            Collection<Apropriacao> apropriacaoCollectionOld = persistentEscopo.getApropriacaoCollection();
            Collection<Apropriacao> apropriacaoCollectionNew = escopo.getApropriacaoCollection();
            List<String> illegalOrphanMessages = null;
            for (Apropriacao apropriacaoCollectionOldApropriacao : apropriacaoCollectionOld) {
                if (!apropriacaoCollectionNew.contains(apropriacaoCollectionOldApropriacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Apropriacao " + apropriacaoCollectionOldApropriacao + " since its escopoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (contratoIdNew != null) {
                contratoIdNew = em.getReference(contratoIdNew.getClass(), contratoIdNew.getId());
                escopo.setContratoId(contratoIdNew);
            }
            if (unidadeMedidaIdNew != null) {
                unidadeMedidaIdNew = em.getReference(unidadeMedidaIdNew.getClass(), unidadeMedidaIdNew.getId());
                escopo.setUnidadeMedidaId(unidadeMedidaIdNew);
            }
            Collection<Apropriacao> attachedApropriacaoCollectionNew = new ArrayList<Apropriacao>();
            for (Apropriacao apropriacaoCollectionNewApropriacaoToAttach : apropriacaoCollectionNew) {
                apropriacaoCollectionNewApropriacaoToAttach = em.getReference(apropriacaoCollectionNewApropriacaoToAttach.getClass(), apropriacaoCollectionNewApropriacaoToAttach.getId());
                attachedApropriacaoCollectionNew.add(apropriacaoCollectionNewApropriacaoToAttach);
            }
            apropriacaoCollectionNew = attachedApropriacaoCollectionNew;
            escopo.setApropriacaoCollection(apropriacaoCollectionNew);
            escopo = em.merge(escopo);
            if (contratoIdOld != null && !contratoIdOld.equals(contratoIdNew)) {
                contratoIdOld.getEscopoCollection().remove(escopo);
                contratoIdOld = em.merge(contratoIdOld);
            }
            if (contratoIdNew != null && !contratoIdNew.equals(contratoIdOld)) {
                contratoIdNew.getEscopoCollection().add(escopo);
                contratoIdNew = em.merge(contratoIdNew);
            }
            if (unidadeMedidaIdOld != null && !unidadeMedidaIdOld.equals(unidadeMedidaIdNew)) {
                unidadeMedidaIdOld.getEscopoCollection().remove(escopo);
                unidadeMedidaIdOld = em.merge(unidadeMedidaIdOld);
            }
            if (unidadeMedidaIdNew != null && !unidadeMedidaIdNew.equals(unidadeMedidaIdOld)) {
                unidadeMedidaIdNew.getEscopoCollection().add(escopo);
                unidadeMedidaIdNew = em.merge(unidadeMedidaIdNew);
            }
            for (Apropriacao apropriacaoCollectionNewApropriacao : apropriacaoCollectionNew) {
                if (!apropriacaoCollectionOld.contains(apropriacaoCollectionNewApropriacao)) {
                    Escopo oldEscopoIdOfApropriacaoCollectionNewApropriacao = apropriacaoCollectionNewApropriacao.getEscopoId();
                    apropriacaoCollectionNewApropriacao.setEscopoId(escopo);
                    apropriacaoCollectionNewApropriacao = em.merge(apropriacaoCollectionNewApropriacao);
                    if (oldEscopoIdOfApropriacaoCollectionNewApropriacao != null && !oldEscopoIdOfApropriacaoCollectionNewApropriacao.equals(escopo)) {
                        oldEscopoIdOfApropriacaoCollectionNewApropriacao.getApropriacaoCollection().remove(apropriacaoCollectionNewApropriacao);
                        oldEscopoIdOfApropriacaoCollectionNewApropriacao = em.merge(oldEscopoIdOfApropriacaoCollectionNewApropriacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = escopo.getId();
                if (findEscopo(id) == null) {
                    throw new NonexistentEntityException("The escopo with id " + id + " no longer exists.");
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
            Escopo escopo;
            try {
                escopo = em.getReference(Escopo.class, id);
                escopo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The escopo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Apropriacao> apropriacaoCollectionOrphanCheck = escopo.getApropriacaoCollection();
            for (Apropriacao apropriacaoCollectionOrphanCheckApropriacao : apropriacaoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Escopo (" + escopo + ") cannot be destroyed since the Apropriacao " + apropriacaoCollectionOrphanCheckApropriacao + " in its apropriacaoCollection field has a non-nullable escopoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Contrato contratoId = escopo.getContratoId();
            if (contratoId != null) {
                contratoId.getEscopoCollection().remove(escopo);
                contratoId = em.merge(contratoId);
            }
            UnidadeMedida unidadeMedidaId = escopo.getUnidadeMedidaId();
            if (unidadeMedidaId != null) {
                unidadeMedidaId.getEscopoCollection().remove(escopo);
                unidadeMedidaId = em.merge(unidadeMedidaId);
            }
            em.remove(escopo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Escopo> findEscopoEntities() {
        return findEscopoEntities(true, -1, -1);
    }

    public List<Escopo> findEscopoEntities(int maxResults, int firstResult) {
        return findEscopoEntities(false, maxResults, firstResult);
    }

    private List<Escopo> findEscopoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Escopo.class));
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
    
    public List<Escopo> findEscopoFilter(Escopo escopo, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Escopo> rt = cq.from(Escopo.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(escopo.getId() != null && escopo.getId() != 0){
                predicates.add(cb.equal(rt.get(Escopo_.id), escopo.getId()));
            }
            if(escopo.getDescricao() != null && !escopo.getDescricao().equals("")){
                predicates.add(cb.like(rt.get(Escopo_.descricao), "%" + escopo.getDescricao() + "%"));
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

    public Escopo findEscopo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Escopo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEscopoCount(Escopo escopo) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Escopo> rt = cq.from(Escopo.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(escopo.getId() != null && escopo.getId() != 0){
                predicates.add(cb.equal(rt.get(Escopo_.id), escopo.getId()));
            }
            if(escopo.getDescricao() != null && !escopo.getDescricao().equals("")){
                predicates.add(cb.like(rt.get(Escopo_.descricao), "%" + escopo.getDescricao() + "%"));
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
