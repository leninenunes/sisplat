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
import model.RtHasProfissional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import model.Apropriacao;
import model.Profissional;
import model.Profissional_;

/**
 *
 * @author lenine.nunes
 */
public class ProfissionalJpaController implements Serializable {

    public ProfissionalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profissional profissional) {
        if (profissional.getRtHasProfissionalCollection() == null) {
            profissional.setRtHasProfissionalCollection(new ArrayList<RtHasProfissional>());
        }
        if (profissional.getApropriacaoCollection() == null) {
            profissional.setApropriacaoCollection(new ArrayList<Apropriacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<RtHasProfissional> attachedRtHasProfissionalCollection = new ArrayList<RtHasProfissional>();
            for (RtHasProfissional rtHasProfissionalCollectionRtHasProfissionalToAttach : profissional.getRtHasProfissionalCollection()) {
                rtHasProfissionalCollectionRtHasProfissionalToAttach = em.getReference(rtHasProfissionalCollectionRtHasProfissionalToAttach.getClass(), rtHasProfissionalCollectionRtHasProfissionalToAttach.getRtHasProfissionalPK());
                attachedRtHasProfissionalCollection.add(rtHasProfissionalCollectionRtHasProfissionalToAttach);
            }
            profissional.setRtHasProfissionalCollection(attachedRtHasProfissionalCollection);
            Collection<Apropriacao> attachedApropriacaoCollection = new ArrayList<Apropriacao>();
            for (Apropriacao apropriacaoCollectionApropriacaoToAttach : profissional.getApropriacaoCollection()) {
                apropriacaoCollectionApropriacaoToAttach = em.getReference(apropriacaoCollectionApropriacaoToAttach.getClass(), apropriacaoCollectionApropriacaoToAttach.getId());
                attachedApropriacaoCollection.add(apropriacaoCollectionApropriacaoToAttach);
            }
            profissional.setApropriacaoCollection(attachedApropriacaoCollection);
            em.persist(profissional);
            for (RtHasProfissional rtHasProfissionalCollectionRtHasProfissional : profissional.getRtHasProfissionalCollection()) {
                Profissional oldProfissionalOfRtHasProfissionalCollectionRtHasProfissional = rtHasProfissionalCollectionRtHasProfissional.getProfissional();
                rtHasProfissionalCollectionRtHasProfissional.setProfissional(profissional);
                rtHasProfissionalCollectionRtHasProfissional = em.merge(rtHasProfissionalCollectionRtHasProfissional);
                if (oldProfissionalOfRtHasProfissionalCollectionRtHasProfissional != null) {
                    oldProfissionalOfRtHasProfissionalCollectionRtHasProfissional.getRtHasProfissionalCollection().remove(rtHasProfissionalCollectionRtHasProfissional);
                    oldProfissionalOfRtHasProfissionalCollectionRtHasProfissional = em.merge(oldProfissionalOfRtHasProfissionalCollectionRtHasProfissional);
                }
            }
            for (Apropriacao apropriacaoCollectionApropriacao : profissional.getApropriacaoCollection()) {
                Profissional oldProfissionalIdOfApropriacaoCollectionApropriacao = apropriacaoCollectionApropriacao.getProfissionalId();
                apropriacaoCollectionApropriacao.setProfissionalId(profissional);
                apropriacaoCollectionApropriacao = em.merge(apropriacaoCollectionApropriacao);
                if (oldProfissionalIdOfApropriacaoCollectionApropriacao != null) {
                    oldProfissionalIdOfApropriacaoCollectionApropriacao.getApropriacaoCollection().remove(apropriacaoCollectionApropriacao);
                    oldProfissionalIdOfApropriacaoCollectionApropriacao = em.merge(oldProfissionalIdOfApropriacaoCollectionApropriacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profissional profissional) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profissional persistentProfissional = em.find(Profissional.class, profissional.getId());
            Collection<RtHasProfissional> rtHasProfissionalCollectionOld = persistentProfissional.getRtHasProfissionalCollection();
            Collection<RtHasProfissional> rtHasProfissionalCollectionNew = profissional.getRtHasProfissionalCollection();
            Collection<Apropriacao> apropriacaoCollectionOld = persistentProfissional.getApropriacaoCollection();
            Collection<Apropriacao> apropriacaoCollectionNew = profissional.getApropriacaoCollection();
            List<String> illegalOrphanMessages = null;
            for (RtHasProfissional rtHasProfissionalCollectionOldRtHasProfissional : rtHasProfissionalCollectionOld) {
                if (!rtHasProfissionalCollectionNew.contains(rtHasProfissionalCollectionOldRtHasProfissional)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RtHasProfissional " + rtHasProfissionalCollectionOldRtHasProfissional + " since its profissional field is not nullable.");
                }
            }
            for (Apropriacao apropriacaoCollectionOldApropriacao : apropriacaoCollectionOld) {
                if (!apropriacaoCollectionNew.contains(apropriacaoCollectionOldApropriacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Apropriacao " + apropriacaoCollectionOldApropriacao + " since its profissionalId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<RtHasProfissional> attachedRtHasProfissionalCollectionNew = new ArrayList<RtHasProfissional>();
            for (RtHasProfissional rtHasProfissionalCollectionNewRtHasProfissionalToAttach : rtHasProfissionalCollectionNew) {
                rtHasProfissionalCollectionNewRtHasProfissionalToAttach = em.getReference(rtHasProfissionalCollectionNewRtHasProfissionalToAttach.getClass(), rtHasProfissionalCollectionNewRtHasProfissionalToAttach.getRtHasProfissionalPK());
                attachedRtHasProfissionalCollectionNew.add(rtHasProfissionalCollectionNewRtHasProfissionalToAttach);
            }
            rtHasProfissionalCollectionNew = attachedRtHasProfissionalCollectionNew;
            profissional.setRtHasProfissionalCollection(rtHasProfissionalCollectionNew);
            Collection<Apropriacao> attachedApropriacaoCollectionNew = new ArrayList<Apropriacao>();
            for (Apropriacao apropriacaoCollectionNewApropriacaoToAttach : apropriacaoCollectionNew) {
                apropriacaoCollectionNewApropriacaoToAttach = em.getReference(apropriacaoCollectionNewApropriacaoToAttach.getClass(), apropriacaoCollectionNewApropriacaoToAttach.getId());
                attachedApropriacaoCollectionNew.add(apropriacaoCollectionNewApropriacaoToAttach);
            }
            apropriacaoCollectionNew = attachedApropriacaoCollectionNew;
            profissional.setApropriacaoCollection(apropriacaoCollectionNew);
            profissional = em.merge(profissional);
            for (RtHasProfissional rtHasProfissionalCollectionNewRtHasProfissional : rtHasProfissionalCollectionNew) {
                if (!rtHasProfissionalCollectionOld.contains(rtHasProfissionalCollectionNewRtHasProfissional)) {
                    Profissional oldProfissionalOfRtHasProfissionalCollectionNewRtHasProfissional = rtHasProfissionalCollectionNewRtHasProfissional.getProfissional();
                    rtHasProfissionalCollectionNewRtHasProfissional.setProfissional(profissional);
                    rtHasProfissionalCollectionNewRtHasProfissional = em.merge(rtHasProfissionalCollectionNewRtHasProfissional);
                    if (oldProfissionalOfRtHasProfissionalCollectionNewRtHasProfissional != null && !oldProfissionalOfRtHasProfissionalCollectionNewRtHasProfissional.equals(profissional)) {
                        oldProfissionalOfRtHasProfissionalCollectionNewRtHasProfissional.getRtHasProfissionalCollection().remove(rtHasProfissionalCollectionNewRtHasProfissional);
                        oldProfissionalOfRtHasProfissionalCollectionNewRtHasProfissional = em.merge(oldProfissionalOfRtHasProfissionalCollectionNewRtHasProfissional);
                    }
                }
            }
            for (Apropriacao apropriacaoCollectionNewApropriacao : apropriacaoCollectionNew) {
                if (!apropriacaoCollectionOld.contains(apropriacaoCollectionNewApropriacao)) {
                    Profissional oldProfissionalIdOfApropriacaoCollectionNewApropriacao = apropriacaoCollectionNewApropriacao.getProfissionalId();
                    apropriacaoCollectionNewApropriacao.setProfissionalId(profissional);
                    apropriacaoCollectionNewApropriacao = em.merge(apropriacaoCollectionNewApropriacao);
                    if (oldProfissionalIdOfApropriacaoCollectionNewApropriacao != null && !oldProfissionalIdOfApropriacaoCollectionNewApropriacao.equals(profissional)) {
                        oldProfissionalIdOfApropriacaoCollectionNewApropriacao.getApropriacaoCollection().remove(apropriacaoCollectionNewApropriacao);
                        oldProfissionalIdOfApropriacaoCollectionNewApropriacao = em.merge(oldProfissionalIdOfApropriacaoCollectionNewApropriacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profissional.getId();
                if (findProfissional(id) == null) {
                    throw new NonexistentEntityException("The profissional with id " + id + " no longer exists.");
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
            Profissional profissional;
            try {
                profissional = em.getReference(Profissional.class, id);
                profissional.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profissional with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RtHasProfissional> rtHasProfissionalCollectionOrphanCheck = profissional.getRtHasProfissionalCollection();
            for (RtHasProfissional rtHasProfissionalCollectionOrphanCheckRtHasProfissional : rtHasProfissionalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profissional (" + profissional + ") cannot be destroyed since the RtHasProfissional " + rtHasProfissionalCollectionOrphanCheckRtHasProfissional + " in its rtHasProfissionalCollection field has a non-nullable profissional field.");
            }
            Collection<Apropriacao> apropriacaoCollectionOrphanCheck = profissional.getApropriacaoCollection();
            for (Apropriacao apropriacaoCollectionOrphanCheckApropriacao : apropriacaoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profissional (" + profissional + ") cannot be destroyed since the Apropriacao " + apropriacaoCollectionOrphanCheckApropriacao + " in its apropriacaoCollection field has a non-nullable profissionalId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(profissional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profissional> findProfissionalEntities() {
        return findProfissionalEntities(true, -1, -1);
    }

    public List<Profissional> findProfissionalEntities(int maxResults, int firstResult) {
        return findProfissionalEntities(false, maxResults, firstResult);
    }

    private List<Profissional> findProfissionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profissional.class));
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
    
    public List<Profissional> findProfissionalFilter(Profissional profissional, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Profissional> rt = cq.from(Profissional.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(profissional.getId() != null && profissional.getId() != 0){
                predicates.add(cb.equal(rt.get(Profissional_.id), profissional.getId()));
            }
            if(profissional.getNome() != null && !profissional.getNome().equals("")){
                predicates.add(cb.like(rt.get(Profissional_.nome), "%" + profissional.getNome() + "%"));
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

    public Profissional findProfissional(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profissional.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfissionalCount(Profissional profissional) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profissional> rt = cq.from(Profissional.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(profissional.getId() != null && profissional.getId() != 0){
                predicates.add(cb.equal(rt.get(Profissional_.id), profissional.getId()));
            }
            if(profissional.getNome() != null && !profissional.getNome().equals("")){
                predicates.add(cb.like(rt.get(Profissional_.nome), "%" + profissional.getNome() + "%"));
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
