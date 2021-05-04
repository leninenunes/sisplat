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
import model.Profissional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import model.Funcao;
import model.Funcao_;

/**
 *
 * @author lenine.nunes
 */
public class FuncaoJpaController implements Serializable {

    public FuncaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcao funcao) {
        if (funcao.getProfissionalCollection() == null) {
            funcao.setProfissionalCollection(new ArrayList<Profissional>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profissional> attachedProfissionalCollection = new ArrayList<Profissional>();
            for (Profissional profissionalCollectionProfissionalToAttach : funcao.getProfissionalCollection()) {
                profissionalCollectionProfissionalToAttach = em.getReference(profissionalCollectionProfissionalToAttach.getClass(), profissionalCollectionProfissionalToAttach.getId());
                attachedProfissionalCollection.add(profissionalCollectionProfissionalToAttach);
            }
            funcao.setProfissionalCollection(attachedProfissionalCollection);
            em.persist(funcao);
            for (Profissional profissionalCollectionProfissional : funcao.getProfissionalCollection()) {
                Funcao oldFuncaoIdOfProfissionalCollectionProfissional = profissionalCollectionProfissional.getFuncaoId();
                profissionalCollectionProfissional.setFuncaoId(funcao);
                profissionalCollectionProfissional = em.merge(profissionalCollectionProfissional);
                if (oldFuncaoIdOfProfissionalCollectionProfissional != null) {
                    oldFuncaoIdOfProfissionalCollectionProfissional.getProfissionalCollection().remove(profissionalCollectionProfissional);
                    oldFuncaoIdOfProfissionalCollectionProfissional = em.merge(oldFuncaoIdOfProfissionalCollectionProfissional);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcao funcao) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcao persistentFuncao = em.find(Funcao.class, funcao.getId());
            Collection<Profissional> profissionalCollectionOld = persistentFuncao.getProfissionalCollection();
            Collection<Profissional> profissionalCollectionNew = funcao.getProfissionalCollection();
            List<String> illegalOrphanMessages = null;
            for (Profissional profissionalCollectionOldProfissional : profissionalCollectionOld) {
                if (!profissionalCollectionNew.contains(profissionalCollectionOldProfissional)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Profissional " + profissionalCollectionOldProfissional + " since its funcaoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Profissional> attachedProfissionalCollectionNew = new ArrayList<Profissional>();
            for (Profissional profissionalCollectionNewProfissionalToAttach : profissionalCollectionNew) {
                profissionalCollectionNewProfissionalToAttach = em.getReference(profissionalCollectionNewProfissionalToAttach.getClass(), profissionalCollectionNewProfissionalToAttach.getId());
                attachedProfissionalCollectionNew.add(profissionalCollectionNewProfissionalToAttach);
            }
            profissionalCollectionNew = attachedProfissionalCollectionNew;
            funcao.setProfissionalCollection(profissionalCollectionNew);
            funcao = em.merge(funcao);
            for (Profissional profissionalCollectionNewProfissional : profissionalCollectionNew) {
                if (!profissionalCollectionOld.contains(profissionalCollectionNewProfissional)) {
                    Funcao oldFuncaoIdOfProfissionalCollectionNewProfissional = profissionalCollectionNewProfissional.getFuncaoId();
                    profissionalCollectionNewProfissional.setFuncaoId(funcao);
                    profissionalCollectionNewProfissional = em.merge(profissionalCollectionNewProfissional);
                    if (oldFuncaoIdOfProfissionalCollectionNewProfissional != null && !oldFuncaoIdOfProfissionalCollectionNewProfissional.equals(funcao)) {
                        oldFuncaoIdOfProfissionalCollectionNewProfissional.getProfissionalCollection().remove(profissionalCollectionNewProfissional);
                        oldFuncaoIdOfProfissionalCollectionNewProfissional = em.merge(oldFuncaoIdOfProfissionalCollectionNewProfissional);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = funcao.getId();
                if (findFuncao(id) == null) {
                    throw new NonexistentEntityException("The funcao with id " + id + " no longer exists.");
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
            Funcao funcao;
            try {
                funcao = em.getReference(Funcao.class, id);
                funcao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcao with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Profissional> profissionalCollectionOrphanCheck = funcao.getProfissionalCollection();
            for (Profissional profissionalCollectionOrphanCheckProfissional : profissionalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funcao (" + funcao + ") cannot be destroyed since the Profissional " + profissionalCollectionOrphanCheckProfissional + " in its profissionalCollection field has a non-nullable funcaoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(funcao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funcao> findFuncaoEntities() {
        return findFuncaoEntities(true, -1, -1);
    }

    public List<Funcao> findFuncaoEntities(int maxResults, int firstResult) {
        return findFuncaoEntities(false, maxResults, firstResult);
    }

    private List<Funcao> findFuncaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcao.class));
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
    
    public List<Funcao> findFuncaoFilter(Funcao funcao, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Funcao> rt = cq.from(Funcao.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(funcao.getId() != null && funcao.getId() != 0){
                predicates.add(cb.equal(rt.get(Funcao_.id), funcao.getId()));
            }
            if(funcao.getNome() != null && !funcao.getNome().equals("")){
                predicates.add(cb.like(rt.get(Funcao_.nome), "%" + funcao.getNome() + "%"));
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

    public Funcao findFuncao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcao.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncaoCount(Funcao funcao) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcao> rt = cq.from(Funcao.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(funcao.getId() != null && funcao.getId() != 0){
                predicates.add(cb.equal(rt.get(Funcao_.id), funcao.getId()));
            }
            if(funcao.getNome() != null && !funcao.getNome().equals("")){
                predicates.add(cb.like(rt.get(Funcao_.nome), "%" + funcao.getNome() + "%"));
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
