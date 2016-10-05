/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Hobby;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Person;
import facade.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class HobbyJpaController implements Serializable {

    public HobbyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hobby hobby) {
        if (hobby.getPersons() == null) {
            hobby.setPersons(new ArrayList<Person>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Person> attachedPersons = new ArrayList<Person>();
            for (Person personsPersonToAttach : hobby.getPersons()) {
                personsPersonToAttach = em.getReference(personsPersonToAttach.getClass(), personsPersonToAttach.getId());
                attachedPersons.add(personsPersonToAttach);
            }
            hobby.setPersons(attachedPersons);
            em.persist(hobby);
            for (Person personsPerson : hobby.getPersons()) {
                personsPerson.getHobies().add(hobby);
                personsPerson = em.merge(personsPerson);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hobby hobby) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hobby persistentHobby = em.find(Hobby.class, hobby.getId());
            List<Person> personsOld = persistentHobby.getPersons();
            List<Person> personsNew = hobby.getPersons();
            List<Person> attachedPersonsNew = new ArrayList<Person>();
            for (Person personsNewPersonToAttach : personsNew) {
                personsNewPersonToAttach = em.getReference(personsNewPersonToAttach.getClass(), personsNewPersonToAttach.getId());
                attachedPersonsNew.add(personsNewPersonToAttach);
            }
            personsNew = attachedPersonsNew;
            hobby.setPersons(personsNew);
            hobby = em.merge(hobby);
            for (Person personsOldPerson : personsOld) {
                if (!personsNew.contains(personsOldPerson)) {
                    personsOldPerson.getHobies().remove(hobby);
                    personsOldPerson = em.merge(personsOldPerson);
                }
            }
            for (Person personsNewPerson : personsNew) {
                if (!personsOld.contains(personsNewPerson)) {
                    personsNewPerson.getHobies().add(hobby);
                    personsNewPerson = em.merge(personsNewPerson);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = hobby.getId();
                if (findHobby(id) == null) {
                    throw new NonexistentEntityException("The hobby with id " + id + " no longer exists.");
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
            Hobby hobby;
            try {
                hobby = em.getReference(Hobby.class, id);
                hobby.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hobby with id " + id + " no longer exists.", enfe);
            }
            List<Person> persons = hobby.getPersons();
            for (Person personsPerson : persons) {
                personsPerson.getHobies().remove(hobby);
                personsPerson = em.merge(personsPerson);
            }
            em.remove(hobby);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hobby> findHobbyEntities() {
        return findHobbyEntities(true, -1, -1);
    }

    public List<Hobby> findHobbyEntities(int maxResults, int firstResult) {
        return findHobbyEntities(false, maxResults, firstResult);
    }

    private List<Hobby> findHobbyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hobby.class));
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

    public Hobby findHobby(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hobby.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Person> findPersonsByHobby(String hobbyName) {
        EntityManager em = getEntityManager();
        
        try
        {
            em.getTransaction().begin();
            Long result = (Long)em.createQuery("SELECT h.id from Hobby h where h.name='"+hobbyName+"'").getSingleResult();
            em.getTransaction().commit();
            return findHobby(result).getPersons();
        }
        finally
        {
            em.close();
        }
    }

    public int getHobbyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hobby> rt = cq.from(Hobby.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
