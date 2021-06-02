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
import model.Empresa;
import model.Empresa_;

/**
 *
 * @author lenine.nunes
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getProfissionalCollection() == null) {
            empresa.setProfissionalCollection(new ArrayList<Profissional>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profissional> attachedProfissionalCollection = new ArrayList<Profissional>();
            for (Profissional profissionalCollectionProfissionalToAttach : empresa.getProfissionalCollection()) {
                profissionalCollectionProfissionalToAttach = em.getReference(profissionalCollectionProfissionalToAttach.getClass(), profissionalCollectionProfissionalToAttach.getId());
                attachedProfissionalCollection.add(profissionalCollectionProfissionalToAttach);
            }
            empresa.setProfissionalCollection(attachedProfissionalCollection);
            em.persist(empresa);
            for (Profissional profissionalCollectionProfissional : empresa.getProfissionalCollection()) {
                Empresa oldEmpresaIdOfProfissionalCollectionProfissional = profissionalCollectionProfissional.getEmpresaId();
                profissionalCollectionProfissional.setEmpresaId(empresa);
                profissionalCollectionProfissional = em.merge(profissionalCollectionProfissional);
                if (oldEmpresaIdOfProfissionalCollectionProfissional != null) {
                    oldEmpresaIdOfProfissionalCollectionProfissional.getProfissionalCollection().remove(profissionalCollectionProfissional);
                    oldEmpresaIdOfProfissionalCollectionProfissional = em.merge(oldEmpresaIdOfProfissionalCollectionProfissional);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getId());
            Collection<Profissional> profissionalCollectionOld = persistentEmpresa.getProfissionalCollection();
            Collection<Profissional> profissionalCollectionNew = empresa.getProfissionalCollection();
            List<String> illegalOrphanMessages = null;
            for (Profissional profissionalCollectionOldProfissional : profissionalCollectionOld) {
                if (!profissionalCollectionNew.contains(profissionalCollectionOldProfissional)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Profissional " + profissionalCollectionOldProfissional + " since its empresaId field is not nullable.");
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
            empresa.setProfissionalCollection(profissionalCollectionNew);
            empresa = em.merge(empresa);
            for (Profissional profissionalCollectionNewProfissional : profissionalCollectionNew) {
                if (!profissionalCollectionOld.contains(profissionalCollectionNewProfissional)) {
                    Empresa oldEmpresaIdOfProfissionalCollectionNewProfissional = profissionalCollectionNewProfissional.getEmpresaId();
                    profissionalCollectionNewProfissional.setEmpresaId(empresa);
                    profissionalCollectionNewProfissional = em.merge(profissionalCollectionNewProfissional);
                    if (oldEmpresaIdOfProfissionalCollectionNewProfissional != null && !oldEmpresaIdOfProfissionalCollectionNewProfissional.equals(empresa)) {
                        oldEmpresaIdOfProfissionalCollectionNewProfissional.getProfissionalCollection().remove(profissionalCollectionNewProfissional);
                        oldEmpresaIdOfProfissionalCollectionNewProfissional = em.merge(oldEmpresaIdOfProfissionalCollectionNewProfissional);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getId();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Profissional> profissionalCollectionOrphanCheck = empresa.getProfissionalCollection();
            for (Profissional profissionalCollectionOrphanCheckProfissional : profissionalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Profissional " + profissionalCollectionOrphanCheckProfissional + " in its profissionalCollection field has a non-nullable empresaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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
    
    public List<Empresa> findEmpresaFilter(Empresa empresa, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Empresa> rt = cq.from(Empresa.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(empresa.getId() != null && empresa.getId() != 0){
                predicates.add(cb.equal(rt.get(Empresa_.id), empresa.getId()));
            }
            if(empresa.getNome() != null && !empresa.getNome().equals("")){
                predicates.add(cb.like(rt.get(Empresa_.nome), "%" + empresa.getNome() + "%"));
            }
            if(empresa.getCnpj() != null && !empresa.getCnpj().equals("")){
                predicates.add(cb.like(rt.get(Empresa_.cnpj), "%" + empresa.getCnpj() + "%"));
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

    public Empresa findEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount(Empresa empresa) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(empresa.getId() != null && empresa.getId() != 0){
                predicates.add(cb.equal(rt.get(Empresa_.id), empresa.getId()));
            }
            if(empresa.getNome() != null && !empresa.getNome().equals("")){
                predicates.add(cb.like(rt.get(Empresa_.nome), "%" + empresa.getNome() + "%"));
            }
            if(empresa.getCnpj() != null && !empresa.getCnpj().equals("")){
                predicates.add(cb.like(rt.get(Empresa_.cnpj), "%" + empresa.getCnpj() + "%"));
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
