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
import entity.Address;
import entity.InfoEntity;
import entity.Phone;
import facade.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class InfoEntityJpaController implements Serializable {

    public InfoEntityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public InfoEntity create(InfoEntity infoEntity) {
        if (infoEntity.getPhone() == null) {
            infoEntity.setPhone(new ArrayList<Phone>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address = infoEntity.getAddress();
            if (address != null) {
                address = em.getReference(address.getClass(), address.getId());
                infoEntity.setAddress(address);
            }
            List<Phone> attachedPhone = new ArrayList<Phone>();
            for (Phone phonePhoneToAttach : infoEntity.getPhone()) {
                phonePhoneToAttach = em.getReference(phonePhoneToAttach.getClass(), phonePhoneToAttach.getId());
                attachedPhone.add(phonePhoneToAttach);
            }
            infoEntity.setPhone(attachedPhone);
            em.persist(infoEntity);
            if (address != null) {
                address.getInfoentities().add(infoEntity);
                address = em.merge(address);
            }
            for (Phone phonePhone : infoEntity.getPhone()) {
                InfoEntity oldInfoEntityOfPhonePhone = phonePhone.getInfoEntity();
                phonePhone.setInfoEntity(infoEntity);
                phonePhone = em.merge(phonePhone);
                if (oldInfoEntityOfPhonePhone != null) {
                    oldInfoEntityOfPhonePhone.getPhone().remove(phonePhone);
                    oldInfoEntityOfPhonePhone = em.merge(oldInfoEntityOfPhonePhone);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return infoEntity;
    }

    public void edit(InfoEntity infoEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InfoEntity persistentInfoEntity = em.find(InfoEntity.class, infoEntity.getId());
            Address addressOld = persistentInfoEntity.getAddress();
            Address addressNew = infoEntity.getAddress();
            List<Phone> phoneOld = persistentInfoEntity.getPhone();
            List<Phone> phoneNew = infoEntity.getPhone();
            if (addressNew != null) {
                addressNew = em.getReference(addressNew.getClass(), addressNew.getId());
                infoEntity.setAddress(addressNew);
            }
            List<Phone> attachedPhoneNew = new ArrayList<Phone>();
            for (Phone phoneNewPhoneToAttach : phoneNew) {
                phoneNewPhoneToAttach = em.getReference(phoneNewPhoneToAttach.getClass(), phoneNewPhoneToAttach.getId());
                attachedPhoneNew.add(phoneNewPhoneToAttach);
            }
            phoneNew = attachedPhoneNew;
            infoEntity.setPhone(phoneNew);
            infoEntity = em.merge(infoEntity);
            if (addressOld != null && !addressOld.equals(addressNew)) {
                addressOld.getInfoentities().remove(infoEntity);
                addressOld = em.merge(addressOld);
            }
            if (addressNew != null && !addressNew.equals(addressOld)) {
                addressNew.getInfoentities().add(infoEntity);
                addressNew = em.merge(addressNew);
            }
            for (Phone phoneOldPhone : phoneOld) {
                if (!phoneNew.contains(phoneOldPhone)) {
                    phoneOldPhone.setInfoEntity(null);
                    phoneOldPhone = em.merge(phoneOldPhone);
                }
            }
            for (Phone phoneNewPhone : phoneNew) {
                if (!phoneOld.contains(phoneNewPhone)) {
                    InfoEntity oldInfoEntityOfPhoneNewPhone = phoneNewPhone.getInfoEntity();
                    phoneNewPhone.setInfoEntity(infoEntity);
                    phoneNewPhone = em.merge(phoneNewPhone);
                    if (oldInfoEntityOfPhoneNewPhone != null && !oldInfoEntityOfPhoneNewPhone.equals(infoEntity)) {
                        oldInfoEntityOfPhoneNewPhone.getPhone().remove(phoneNewPhone);
                        oldInfoEntityOfPhoneNewPhone = em.merge(oldInfoEntityOfPhoneNewPhone);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = infoEntity.getId();
                if (findInfoEntity(id) == null) {
                    throw new NonexistentEntityException("The infoEntity with id " + id + " no longer exists.");
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
            InfoEntity infoEntity;
            try {
                infoEntity = em.getReference(InfoEntity.class, id);
                infoEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The infoEntity with id " + id + " no longer exists.", enfe);
            }
            Address address = infoEntity.getAddress();
            if (address != null) {
                address.getInfoentities().remove(infoEntity);
                address = em.merge(address);
            }
            List<Phone> phone = infoEntity.getPhone();
            for (Phone phonePhone : phone) {
                phonePhone.setInfoEntity(null);
                phonePhone = em.merge(phonePhone);
            }
            em.remove(infoEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InfoEntity> findInfoEntityEntities() {
        return findInfoEntityEntities(true, -1, -1);
    }

    public List<InfoEntity> findInfoEntityEntities(int maxResults, int firstResult) {
        return findInfoEntityEntities(false, maxResults, firstResult);
    }

    private List<InfoEntity> findInfoEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InfoEntity.class));
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

    public InfoEntity findInfoEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InfoEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getInfoEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InfoEntity> rt = cq.from(InfoEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
