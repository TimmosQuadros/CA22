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
import entity.CityInfo;
import facade.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class CityInfoJpaController implements Serializable {

    public CityInfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CityInfo cityInfo) {
        if (cityInfo.getAddresses() == null) {
            cityInfo.setAddresses(new ArrayList<Address>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Address> attachedAddresses = new ArrayList<Address>();
            for (Address addressesAddressToAttach : cityInfo.getAddresses()) {
                addressesAddressToAttach = em.getReference(addressesAddressToAttach.getClass(), addressesAddressToAttach.getId());
                attachedAddresses.add(addressesAddressToAttach);
            }
            cityInfo.setAddresses(attachedAddresses);
            em.persist(cityInfo);
            for (Address addressesAddress : cityInfo.getAddresses()) {
                CityInfo oldCityInfoOfAddressesAddress = addressesAddress.getCityInfo();
                addressesAddress.setCityInfo(cityInfo);
                addressesAddress = em.merge(addressesAddress);
                if (oldCityInfoOfAddressesAddress != null) {
                    oldCityInfoOfAddressesAddress.getAddresses().remove(addressesAddress);
                    oldCityInfoOfAddressesAddress = em.merge(oldCityInfoOfAddressesAddress);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CityInfo cityInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CityInfo persistentCityInfo = em.find(CityInfo.class, cityInfo.getId());
            List<Address> addressesOld = persistentCityInfo.getAddresses();
            List<Address> addressesNew = cityInfo.getAddresses();
            List<Address> attachedAddressesNew = new ArrayList<Address>();
            for (Address addressesNewAddressToAttach : addressesNew) {
                addressesNewAddressToAttach = em.getReference(addressesNewAddressToAttach.getClass(), addressesNewAddressToAttach.getId());
                attachedAddressesNew.add(addressesNewAddressToAttach);
            }
            addressesNew = attachedAddressesNew;
            cityInfo.setAddresses(addressesNew);
            cityInfo = em.merge(cityInfo);
            for (Address addressesOldAddress : addressesOld) {
                if (!addressesNew.contains(addressesOldAddress)) {
                    addressesOldAddress.setCityInfo(null);
                    addressesOldAddress = em.merge(addressesOldAddress);
                }
            }
            for (Address addressesNewAddress : addressesNew) {
                if (!addressesOld.contains(addressesNewAddress)) {
                    CityInfo oldCityInfoOfAddressesNewAddress = addressesNewAddress.getCityInfo();
                    addressesNewAddress.setCityInfo(cityInfo);
                    addressesNewAddress = em.merge(addressesNewAddress);
                    if (oldCityInfoOfAddressesNewAddress != null && !oldCityInfoOfAddressesNewAddress.equals(cityInfo)) {
                        oldCityInfoOfAddressesNewAddress.getAddresses().remove(addressesNewAddress);
                        oldCityInfoOfAddressesNewAddress = em.merge(oldCityInfoOfAddressesNewAddress);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cityInfo.getId();
                if (findCityInfo(id) == null) {
                    throw new NonexistentEntityException("The cityInfo with id " + id + " no longer exists.");
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
            CityInfo cityInfo;
            try {
                cityInfo = em.getReference(CityInfo.class, id);
                cityInfo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cityInfo with id " + id + " no longer exists.", enfe);
            }
            List<Address> addresses = cityInfo.getAddresses();
            for (Address addressesAddress : addresses) {
                addressesAddress.setCityInfo(null);
                addressesAddress = em.merge(addressesAddress);
            }
            em.remove(cityInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CityInfo> findCityInfoEntities() {
        return findCityInfoEntities(true, -1, -1);
    }

    public List<CityInfo> findCityInfoEntities(int maxResults, int firstResult) {
        return findCityInfoEntities(false, maxResults, firstResult);
    }

    private List<CityInfo> findCityInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CityInfo.class));
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

    public CityInfo findCityInfo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CityInfo.class, id);
        } finally {
            em.close();
        }
    }
    
    public CityInfo findCityInfo(String city) {
        EntityManager em = getEntityManager();
        
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT c.id FROM CityInfo c WHERE c.city='"+city+"'").getSingleResult();
            em.getTransaction().commit();
            return findCityInfo(result);
        }
        finally
        {
            em.close();
        }
    }

    public int getCityInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CityInfo> rt = cq.from(CityInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
