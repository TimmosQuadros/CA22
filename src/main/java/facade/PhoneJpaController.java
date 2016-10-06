/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.InfoEntity;
import entity.Phone;
import facade.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class PhoneJpaController implements Serializable {

    public PhoneJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Phone create(Phone phone) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InfoEntity infoEntity = phone.getInfoEntity();
            if (infoEntity != null) {
                infoEntity = em.getReference(infoEntity.getClass(), infoEntity.getId());
                phone.setInfoEntity(infoEntity);
            }
            em.persist(phone);
            if (infoEntity != null) {
                infoEntity.getPhone().add(phone);
                infoEntity = em.merge(infoEntity);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return phone;
    }

    public void edit(Phone phone) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Phone persistentPhone = em.find(Phone.class, phone.getId());
            InfoEntity infoEntityOld = persistentPhone.getInfoEntity();
            InfoEntity infoEntityNew = phone.getInfoEntity();
            if (infoEntityNew != null) {
                infoEntityNew = em.getReference(infoEntityNew.getClass(), infoEntityNew.getId());
                phone.setInfoEntity(infoEntityNew);
            }
            phone = em.merge(phone);
            if (infoEntityOld != null && !infoEntityOld.equals(infoEntityNew)) {
                infoEntityOld.getPhone().remove(phone);
                infoEntityOld = em.merge(infoEntityOld);
            }
            if (infoEntityNew != null && !infoEntityNew.equals(infoEntityOld)) {
                infoEntityNew.getPhone().add(phone);
                infoEntityNew = em.merge(infoEntityNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = phone.getId();
                if (findPhone(id) == null) {
                    throw new NonexistentEntityException("The phone with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Phone phone;
            try {
                phone = em.getReference(Phone.class, id);
                phone.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The phone with id " + id + " no longer exists.", enfe);
            }
            InfoEntity infoEntity = phone.getInfoEntity();
            if (infoEntity != null) {
                infoEntity.getPhone().remove(phone);
                infoEntity = em.merge(infoEntity);
            }
            em.remove(phone);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Phone> findPhoneEntities() {
        return findPhoneEntities(true, -1, -1);
    }

    public List<Phone> findPhoneEntities(int maxResults, int firstResult) {
        return findPhoneEntities(false, maxResults, firstResult);
    }

    private List<Phone> findPhoneEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Phone.class));
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

    public Phone findPhone(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Phone.class, id);
        } finally {
            em.close();
        }
    }
    
    public Phone findPhoneByPhoneNumber(int phonenumber) {
        EntityManager em = getEntityManager();
        
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT p.id FROM Phone p WHERE p.number="+phonenumber).getSingleResult();
            em.getTransaction().commit();
            return findPhone(result);
        }
        finally
        {
            em.close();
        }
    }

    public int getPhoneCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Phone> rt = cq.from(Phone.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
