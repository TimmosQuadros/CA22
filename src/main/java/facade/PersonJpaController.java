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
import entity.Hobby;
import entity.InfoEntity;
import entity.Person;
import java.util.ArrayList;
import java.util.List;
import entity.Phone;
import facade.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class PersonJpaController implements Serializable {
    
    public PersonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Person create(Person person) {
        if (person.getHobies() == null) {
            person.setHobies(new ArrayList<Hobby>());
        }
        if (person.getPhone() == null) {
            person.setPhone(new ArrayList<Phone>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address = person.getAddress();
            if (address != null) {
                address = em.getReference(address.getClass(), address.getId());
                person.setAddress(address);
            }
            List<Hobby> attachedHobies = new ArrayList<Hobby>();
            for (Hobby hobiesHobbyToAttach : person.getHobies()) {
                hobiesHobbyToAttach = em.getReference(hobiesHobbyToAttach.getClass(), hobiesHobbyToAttach.getId());
                attachedHobies.add(hobiesHobbyToAttach);
            }
            person.setHobies(attachedHobies);
            List<Phone> attachedPhone = new ArrayList<Phone>();
            for (Phone phonePhoneToAttach : person.getPhone()) {
                phonePhoneToAttach = em.getReference(phonePhoneToAttach.getClass(), phonePhoneToAttach.getId());
                attachedPhone.add(phonePhoneToAttach);
            }
            person.setPhone(attachedPhone);
            em.persist(person);
            if (address != null) {
                address.getInfoentities().add(person);
                address = em.merge(address);
            }
            for (Hobby hobiesHobby : person.getHobies()) {
                hobiesHobby.getPersons().add(person);
                hobiesHobby = em.merge(hobiesHobby);
            }
            for (Phone phonePhone : person.getPhone()) {
                entity.InfoEntity oldInfoEntityOfPhonePhone = phonePhone.getInfoEntity();
                phonePhone.setInfoEntity(person);
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
        return person;
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            Address address = person.getAddress();
            if (address != null) {
                address.getInfoentities().remove(person);
                address = em.merge(address);
            }
            List<Hobby> hobies = person.getHobies();
            for (Hobby hobiesHobby : hobies) {
                hobiesHobby.getPersons().remove(person);
                hobiesHobby = em.merge(hobiesHobby);
            }
            List<Phone> phone = person.getPhone();
            for (Phone phonePhone : phone) {
                phonePhone.setInfoEntity(null);
                phonePhone = em.merge(phonePhone);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
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
    
    

    public Person findPerson(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }
    
    public Person findPersonByPhone(int phonenumber) {
        EntityManager em = getEntityManager();
        
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT p.infoEntity.id FROM Phone p WHERE p.number="+phonenumber).getSingleResult();
            em.getTransaction().commit();
            return findPerson(result);
        }
        finally
        {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
