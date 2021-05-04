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
import model.Apropriacao;
import model.Apropriacao_;
import model.Escopo;
import model.Profissional;

/**
 *
 * @author lenine.nunes
 */
public class ApropriacaoJpaController implements Serializable {

    public ApropriacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Apropriacao apropriacao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escopo escopoId = apropriacao.getEscopoId();
            if (escopoId != null) {
                escopoId = em.getReference(escopoId.getClass(), escopoId.getId());
                apropriacao.setEscopoId(escopoId);
            }
            Profissional profissionalId = apropriacao.getProfissionalId();
            if (profissionalId != null) {
                profissionalId = em.getReference(profissionalId.getClass(), profissionalId.getId());
                apropriacao.setProfissionalId(profissionalId);
            }
            em.persist(apropriacao);
            if (escopoId != null) {
                escopoId.getApropriacaoCollection().add(apropriacao);
                escopoId = em.merge(escopoId);
            }
            if (profissionalId != null) {
                profissionalId.getApropriacaoCollection().add(apropriacao);
                profissionalId = em.merge(profissionalId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Apropriacao apropriacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Apropriacao persistentApropriacao = em.find(Apropriacao.class, apropriacao.getId());
            Escopo escopoIdOld = persistentApropriacao.getEscopoId();
            Escopo escopoIdNew = apropriacao.getEscopoId();
            Profissional profissionalIdOld = persistentApropriacao.getProfissionalId();
            Profissional profissionalIdNew = apropriacao.getProfissionalId();
            if (escopoIdNew != null) {
                escopoIdNew = em.getReference(escopoIdNew.getClass(), escopoIdNew.getId());
                apropriacao.setEscopoId(escopoIdNew);
            }
            if (profissionalIdNew != null) {
                profissionalIdNew = em.getReference(profissionalIdNew.getClass(), profissionalIdNew.getId());
                apropriacao.setProfissionalId(profissionalIdNew);
            }
            apropriacao = em.merge(apropriacao);
            if (escopoIdOld != null && !escopoIdOld.equals(escopoIdNew)) {
                escopoIdOld.getApropriacaoCollection().remove(apropriacao);
                escopoIdOld = em.merge(escopoIdOld);
            }
            if (escopoIdNew != null && !escopoIdNew.equals(escopoIdOld)) {
                escopoIdNew.getApropriacaoCollection().add(apropriacao);
                escopoIdNew = em.merge(escopoIdNew);
            }
            if (profissionalIdOld != null && !profissionalIdOld.equals(profissionalIdNew)) {
                profissionalIdOld.getApropriacaoCollection().remove(apropriacao);
                profissionalIdOld = em.merge(profissionalIdOld);
            }
            if (profissionalIdNew != null && !profissionalIdNew.equals(profissionalIdOld)) {
                profissionalIdNew.getApropriacaoCollection().add(apropriacao);
                profissionalIdNew = em.merge(profissionalIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = apropriacao.getId();
                if (findApropriacao(id) == null) {
                    throw new NonexistentEntityException("The apropriacao with id " + id + " no longer exists.");
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
            Apropriacao apropriacao;
            try {
                apropriacao = em.getReference(Apropriacao.class, id);
                apropriacao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apropriacao with id " + id + " no longer exists.", enfe);
            }
            Escopo escopoId = apropriacao.getEscopoId();
            if (escopoId != null) {
                escopoId.getApropriacaoCollection().remove(apropriacao);
                escopoId = em.merge(escopoId);
            }
            Profissional profissionalId = apropriacao.getProfissionalId();
            if (profissionalId != null) {
                profissionalId.getApropriacaoCollection().remove(apropriacao);
                profissionalId = em.merge(profissionalId);
            }
            em.remove(apropriacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Apropriacao> findApropriacaoEntities() {
        return findApropriacaoEntities(true, -1, -1);
    }

    public List<Apropriacao> findApropriacaoEntities(int maxResults, int firstResult) {
        return findApropriacaoEntities(false, maxResults, firstResult);
    }

    private List<Apropriacao> findApropriacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Apropriacao.class));
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
    
    public List<Apropriacao> findApropriacaoFilter(Apropriacao apropriacao, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Apropriacao> rt = cq.from(Apropriacao.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(apropriacao.getId() != null && apropriacao.getId() != 0){
                predicates.add(cb.equal(rt.get(Apropriacao_.id), apropriacao.getId()));
            }
            if(apropriacao.getData() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.data), apropriacao.getData()));
            }
            if(apropriacao.getInicio() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.inicio), apropriacao.getInicio()));
            }
            if(apropriacao.getTermino() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.termino), apropriacao.getTermino()));
            }
            if(apropriacao.getIntervalo() > 0){
                predicates.add(cb.equal(rt.get(Apropriacao_.intervalo), apropriacao.getIntervalo()));
            }
            if(apropriacao.getTag() != null && !apropriacao.getTag().equals("")){
                predicates.add(cb.like(rt.get(Apropriacao_.tag), "%" + apropriacao.getTag() + "%"));
            }
            if(apropriacao.getEscopoId() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.escopoId), apropriacao.getEscopoId()));
            }
            if(apropriacao.getProfissionalId() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.profissionalId), apropriacao.getProfissionalId()));
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

    public Apropriacao findApropriacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Apropriacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getApropriacaoCount(Apropriacao apropriacao) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Apropriacao> rt = cq.from(Apropriacao.class);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<Predicate>();
            if(apropriacao.getId() != null && apropriacao.getId() != 0){
                predicates.add(cb.equal(rt.get(Apropriacao_.id), apropriacao.getId()));
            }
            if(apropriacao.getData() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.data), apropriacao.getData()));
            }
            if(apropriacao.getInicio() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.inicio), apropriacao.getInicio()));
            }
            if(apropriacao.getTermino() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.termino), apropriacao.getTermino()));
            }
            if(apropriacao.getIntervalo() > 0){
                predicates.add(cb.equal(rt.get(Apropriacao_.intervalo), apropriacao.getIntervalo()));
            }
            if(apropriacao.getTag() != null && !apropriacao.getTag().equals("")){
                predicates.add(cb.like(rt.get(Apropriacao_.tag), "%" + apropriacao.getTag() + "%"));
            }
            if(apropriacao.getEscopoId() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.escopoId), apropriacao.getEscopoId()));
            }
            if(apropriacao.getProfissionalId() != null){
                predicates.add(cb.equal(rt.get(Apropriacao_.profissionalId), apropriacao.getProfissionalId()));
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
