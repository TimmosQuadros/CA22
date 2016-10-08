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
import entity.Company;
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
public class CompanyJpaController implements Serializable {

    public CompanyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * This method creates a company with JPA in the db.
     * @param company
     * @return 
     */
    public Company create(Company company) {
        if (company.getPhone() == null) {
            company.setPhone(new ArrayList<Phone>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address = company.getAddress();
            if (address != null) {
                address = em.getReference(address.getClass(), address.getId());
                company.setAddress(address);
            }
            List<Phone> attachedPhone = new ArrayList<Phone>();
            for (Phone phonePhoneToAttach : company.getPhone()) {
                phonePhoneToAttach = em.getReference(phonePhoneToAttach.getClass(), phonePhoneToAttach.getId());
                attachedPhone.add(phonePhoneToAttach);
            }
            company.setPhone(attachedPhone);
            em.persist(company);
            if (address != null) {
                address.getInfoentities().add(company);
                address = em.merge(address);
            }
            for (Phone phonePhone : company.getPhone()) {
                entity.InfoEntity oldInfoEntityOfPhonePhone = phonePhone.getInfoEntity();
                phonePhone.setInfoEntity(company);
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
        return company;
    }
    
    // PeterBoss
    public void edit(Company company) {
        EntityManager em = getEntityManager();
            em.getTransaction().begin();
            em.merge(company);
            em.getTransaction().commit();
            em.close();
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The company with id " + id + " no longer exists.", enfe);
            }
            Address address = company.getAddress();
            if (address != null) {
                address.getInfoentities().remove(company);
                address = em.merge(address);
            }
            List<Phone> phone = company.getPhone();
            for (Phone phonePhone : phone) {
                phonePhone.setInfoEntity(null);
                phonePhone = em.merge(phonePhone);
            }
            em.remove(company);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Company> findCompanyEntities() {
        return findCompanyEntities(true, -1, -1);
    }

    public List<Company> findCompanyEntities(int maxResults, int firstResult) {
        return findCompanyEntities(false, maxResults, firstResult);
    }

    private List<Company> findCompanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Company.class));
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

    public Company findCompany(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Company.class, id);
        } finally {
            em.close();
        }
    }
    
    public Company findCompanyByPhone(int phonenumber) {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT p.infoEntity.id FROM Phone p WHERE p.number="+phonenumber).getSingleResult();
            em.getTransaction().commit();
            return findCompany(result);
        }
        finally
        {
            em.close();
        }
    }
    
    public Company findCompanyByCVR(int cvr) {
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT c.id FROM Company c WHERE c.cvr="+cvr).getSingleResult();
            em.getTransaction().commit();
            return findCompany(result);
        }
        finally
        {
            em.close();
        }
    }
    
    

    public int getCompanyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Company> rt = cq.from(Company.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
