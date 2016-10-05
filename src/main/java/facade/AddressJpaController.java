/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Address;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.CityInfo;
import entity.InfoEntity;
import facade.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class AddressJpaController implements Serializable {

    public AddressJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) {
        if (address.getInfoentities() == null) {
            address.setInfoentities(new ArrayList<InfoEntity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CityInfo cityInfo = address.getCityInfo();
            if (cityInfo != null) {
                cityInfo = em.getReference(cityInfo.getClass(), cityInfo.getId());
                address.setCityInfo(cityInfo);
            }
            List<InfoEntity> attachedInfoentities = new ArrayList<InfoEntity>();
            for (InfoEntity infoentitiesInfoEntityToAttach : address.getInfoentities()) {
                infoentitiesInfoEntityToAttach = em.getReference(infoentitiesInfoEntityToAttach.getClass(), infoentitiesInfoEntityToAttach.getId());
                attachedInfoentities.add(infoentitiesInfoEntityToAttach);
            }
            address.setInfoentities(attachedInfoentities);
            em.persist(address);
            if (cityInfo != null) {
                cityInfo.getAddresses().add(address);
                cityInfo = em.merge(cityInfo);
            }
            for (InfoEntity infoentitiesInfoEntity : address.getInfoentities()) {
                Address oldAddressOfInfoentitiesInfoEntity = infoentitiesInfoEntity.getAddress();
                infoentitiesInfoEntity.setAddress(address);
                infoentitiesInfoEntity = em.merge(infoentitiesInfoEntity);
                if (oldAddressOfInfoentitiesInfoEntity != null) {
                    oldAddressOfInfoentitiesInfoEntity.getInfoentities().remove(infoentitiesInfoEntity);
                    oldAddressOfInfoentitiesInfoEntity = em.merge(oldAddressOfInfoentitiesInfoEntity);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Address address) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address persistentAddress = em.find(Address.class, address.getId());
            CityInfo cityInfoOld = persistentAddress.getCityInfo();
            CityInfo cityInfoNew = address.getCityInfo();
            List<InfoEntity> infoentitiesOld = persistentAddress.getInfoentities();
            List<InfoEntity> infoentitiesNew = address.getInfoentities();
            if (cityInfoNew != null) {
                cityInfoNew = em.getReference(cityInfoNew.getClass(), cityInfoNew.getId());
                address.setCityInfo(cityInfoNew);
            }
            List<InfoEntity> attachedInfoentitiesNew = new ArrayList<InfoEntity>();
            for (InfoEntity infoentitiesNewInfoEntityToAttach : infoentitiesNew) {
                infoentitiesNewInfoEntityToAttach = em.getReference(infoentitiesNewInfoEntityToAttach.getClass(), infoentitiesNewInfoEntityToAttach.getId());
                attachedInfoentitiesNew.add(infoentitiesNewInfoEntityToAttach);
            }
            infoentitiesNew = attachedInfoentitiesNew;
            address.setInfoentities(infoentitiesNew);
            address = em.merge(address);
            if (cityInfoOld != null && !cityInfoOld.equals(cityInfoNew)) {
                cityInfoOld.getAddresses().remove(address);
                cityInfoOld = em.merge(cityInfoOld);
            }
            if (cityInfoNew != null && !cityInfoNew.equals(cityInfoOld)) {
                cityInfoNew.getAddresses().add(address);
                cityInfoNew = em.merge(cityInfoNew);
            }
            for (InfoEntity infoentitiesOldInfoEntity : infoentitiesOld) {
                if (!infoentitiesNew.contains(infoentitiesOldInfoEntity)) {
                    infoentitiesOldInfoEntity.setAddress(null);
                    infoentitiesOldInfoEntity = em.merge(infoentitiesOldInfoEntity);
                }
            }
            for (InfoEntity infoentitiesNewInfoEntity : infoentitiesNew) {
                if (!infoentitiesOld.contains(infoentitiesNewInfoEntity)) {
                    Address oldAddressOfInfoentitiesNewInfoEntity = infoentitiesNewInfoEntity.getAddress();
                    infoentitiesNewInfoEntity.setAddress(address);
                    infoentitiesNewInfoEntity = em.merge(infoentitiesNewInfoEntity);
                    if (oldAddressOfInfoentitiesNewInfoEntity != null && !oldAddressOfInfoentitiesNewInfoEntity.equals(address)) {
                        oldAddressOfInfoentitiesNewInfoEntity.getInfoentities().remove(infoentitiesNewInfoEntity);
                        oldAddressOfInfoentitiesNewInfoEntity = em.merge(oldAddressOfInfoentitiesNewInfoEntity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = address.getId();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
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
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            CityInfo cityInfo = address.getCityInfo();
            if (cityInfo != null) {
                cityInfo.getAddresses().remove(address);
                cityInfo = em.merge(cityInfo);
            }
            List<InfoEntity> infoentities = address.getInfoentities();
            for (InfoEntity infoentitiesInfoEntity : infoentities) {
                infoentitiesInfoEntity.setAddress(null);
                infoentitiesInfoEntity = em.merge(infoentitiesInfoEntity);
            }
            em.remove(address);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
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

    public Address findAddress(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }
    
    public Address findAddressByAddress(Address adr) {
        EntityManager em = getEntityManager();
        String city = adr.getCityInfo().getCity();
        String zip = adr.getCityInfo().getZip();
        String street = adr.getStreet();
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT a.id FROM Address a WHERE a.cityInfo.city='"+city+"'"+" and a.cityInfo.zip='"+zip+"'" +" and a.street='"+street+"'").getSingleResult();
            em.getTransaction().commit();
            return findAddress(result);
        }
        finally
        {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
