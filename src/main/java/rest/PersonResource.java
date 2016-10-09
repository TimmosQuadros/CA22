/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import RESTException.PersonNotFoundException;
import com.google.gson.Gson;
import entity.Address;
import entity.CityInfo;
import entity.Hobby;
import entity.InfoEntity;
import entity.Person;
import entity.Phone;
import facade.Facade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jsonmappers.AddressMapper;
import jsonmappers.HobbyMapper;
import jsonmappers.PersonMapper;
import jsonmappers.PersonMapper1;
import jsonmappers.PhoneMapper;

/**
 * REST Web Service
 *
 * @author TimmosQuadros
 */
@Path("person")
public class PersonResource {

    @Context
    private UriInfo context;

    private static Gson gson = new Gson();
    private EntityManagerFactory emf;
    private Facade facade;

    /**
     * Creates a new instance of PersonResource
     */
    public PersonResource() {
        emf = Persistence.createEntityManagerFactory("com.mycompany_CA2-1.1_war_1.0-SNAPSHOTPU");
        facade = new Facade(emf);
    }
 
    /**
     * Get method to retrieve the person with the given phonenumber. If the
     * phonenumber does not exist, it will throw a PersonNotFoundException.
     * @param phone
     * @return
     * @throws PersonNotFoundException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/{phone}")
    public String getPerson(@PathParam("phone") int phone) throws PersonNotFoundException {
        Person person = null;
        try {
            person = facade.getPersonByPhone(phone);
        } catch (Exception e) {
        }

        if (person == null) {
            throw new PersonNotFoundException("No person exists with that phone number");
        }

        return gson.toJson(new PersonMapper(person));
    }

    /**
     * Get method to retrieve the person with the given id. If the given it does
     * not exist, it will throw a PersonNotFoundException.
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("complete/{id}")
    public String getPersonFromId(@PathParam("id") int id) throws PersonNotFoundException {
        PersonMapper pm = null;
        Person person = null;
        try {
            person = facade.getPerson(id);
            pm = new PersonMapper(person);
        } catch (Exception e) {
        }

        if (person == null) {
            throw new PersonNotFoundException("No person exists with that id");
        }

        return gson.toJson(pm);
    }

    /**
     * Get method to retrieve all persons.
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("complete")
    public String getAllPersons() {
        List<PersonMapper> personMapperList = new ArrayList<>();
        List<Person> persons = facade.getPersons();
        for (Person person : persons) {
            personMapperList.add(new PersonMapper(person));
        }
        return "{\"persons\":" + gson.toJson(personMapperList) + "}";
    }

    /**
     * Get method to retrieve all people with the given contactinfo. If no people
     * exist, it will throw a PersonNotFoundException.
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("contactinfo")
    public String getAllPersonsContactinfo() {
        List<PersonMapper1> personMapper1List = new ArrayList<>();
        List<Person> persons = facade.getPersons();
        for (Person person : persons) {
            personMapper1List.add(new PersonMapper1(person));
        }
        return "{\"persons\":" + gson.toJson(personMapper1List) + "}";
    }

    /**
     * Get method to retrieve a person with the given contactinfo. If no person
     * exists with the given contactinfo, it will throw a PersonNotFoundException
     * @param id
     * @return
     * @throws PersonNotFoundException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("contactinfo/{id}")
    public String getPersonContactinfo(@PathParam("id") int id) throws PersonNotFoundException {
        PersonMapper1 pm1 = null;
        Person person = null;
        try {
            person = facade.getPerson(id);
            pm1 = new PersonMapper1(person);
        } catch (Exception e) {
        }

        if (person == null) {
            throw new PersonNotFoundException("No person exists with that id");
        }

        return gson.toJson(pm1);
    }

    /**
     * Get method to retrieve all people with the given hobby. If no people exists
     * with the given hobby, it will throw a PersonNotFoundException.
     *
     * @param hobby
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persons/{hobby}")
    public String getPersons(@PathParam("hobby") String hobby) throws PersonNotFoundException {
        List<Person> persons = new ArrayList<>();
        List<PersonMapper> personMapperList = new ArrayList<>();
        try {
            persons = facade.getPersonsFromHobby(hobby);
            personMapperList = new ArrayList<>();
            for (Person person : persons) {
                personMapperList.add(new PersonMapper(person));
            }

        } catch (Exception e) {
        }

        if (persons.isEmpty()) {
            throw new PersonNotFoundException("No person exists with that hobby");
        }
        return "{" + gson.toJson(personMapperList) + "}";
    }

    /**
     * Post method to insert a person in the database. It will auto-increment an
     * ID for the newly created person.
     *
     * @param content
     * @return
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String content) throws Exception {
        PersonMapper pm = gson.fromJson(content, PersonMapper.class);
        AddressMapper tmpAddress = pm.getAddress();
        Address address = new Address(tmpAddress.getStreet(), tmpAddress.getAdditionalInfo(), facade.getCityInfoByCity(tmpAddress.getCity()));
        Address JPACreatedAddress = facade.createAddress(address);

        InfoEntity infoentity = new InfoEntity(pm.getEmail(), JPACreatedAddress);

        Person p = new Person(pm.getFirstname(), pm.getLastname(), pm.getEmail(), JPACreatedAddress);
        Person JPACreatedPerson = facade.createPerson(p);

        addPhone(pm.getPhones(), JPACreatedPerson);
        addHobby(pm.getHobbies(), JPACreatedPerson);

        facade.editPerson(JPACreatedPerson);

        Person consistentPerson = facade.getPerson(JPACreatedPerson.getId());

        return gson.toJson(new PersonMapper(consistentPerson));
    }

    /**
     * PUT method for updating/edit the person with the given ID. If the ID does
     * not exist in the database, it will throw a PersonNotFoundException.
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String putPerson(String content) throws PersonNotFoundException {

        PersonMapper pm = gson.fromJson(content, PersonMapper.class);

        Person JPAPerson;
        JPAPerson = facade.getPerson(pm.getId());
        if (JPAPerson == null) {
            throw new PersonNotFoundException("The person with the given id doesn't exists and therefore can't be edited");
        }

        if (pm.getFirstname() != null) {
            JPAPerson.setFirstName(pm.getFirstname());
            facade.editPerson(JPAPerson);
        }
        if (pm.getLastname() != null) {
            JPAPerson.setLastName(pm.getLastname());
            facade.editPerson(JPAPerson);
        }
        if (pm.getEmail() != null) {
            JPAPerson.setMail(pm.getEmail());
        }
        if (pm.getPhones() != null) {
            List<Phone> phones = toPhoneList(pm.getPhones());
            JPAPerson.setPhone(phones);
        }
        if (pm.getAddress() != null) {
            Address adr = toAddress(pm.getAddress());
            try {
                facade.editAddress(adr);
            } catch (Exception ex) {

            }
        }
        if (pm.getHobbies() != null) {
            JPAPerson.setHobies(toHobbyList(pm.getHobbies()));
        }

        facade.editPerson(JPAPerson);

        Person consistentPerson = facade.getPerson(JPAPerson.getId());

        return gson.toJson(new PersonMapper(consistentPerson));
    }

    /**
     * This method deletes the person with the given id. If the ID does not
     * exist in the database, it will throw a PersonNotFoundException.
     *
     * @param id
     * @return
     * @throws PersonNotFoundException
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{id}")
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        Person p = facade.getPerson(id);
        if (p == null) {
            throw new PersonNotFoundException("Cannot delete, person doesn't exists");
        }

        try {
            facade.deleteAddress(p.getAddress().getId());
            List<Phone> phones = p.getPhone();
            if (phones != null) {
                for (Phone phone : phones) {
                    facade.deletePhone(phone.getId());
                }
            }
            facade.deletePerson(id);
        } catch (Exception ex) {

        }
        return gson.toJson(new PersonMapper(p));
    }

    /**
     * Method to add hobby. If the hobby does not exist, it creates the hobby
     * and applies it to a person. If the hobby does exist, it will update the
     * list of hobbies on a person and add the hobby to the person.
     * @param hobbyMappers
     * @param person
     * @throws Exception 
     */
    private void addHobby(List<HobbyMapper> hobbyMappers, Person person) throws Exception {
        Hobby hobbyAlreadyExists = null;
        for (HobbyMapper hobbyMapper : hobbyMappers) {
            try {
                hobbyAlreadyExists = facade.getHobbyFromName(hobbyMapper.getName());
            } catch (Exception e) {

            }
            if (hobbyAlreadyExists == null) { //That is the hobby doesn't already exists
                Hobby hobby = new Hobby(hobbyMapper.getName(), hobbyMapper.getDescription(), new ArrayList<Person>()); //the list of persons belonging to this hobby is empty since it is a new hobby...
                facade.createHobby(hobby);
                person.addHobby(hobby);
                hobby.addPerson(person);
                facade.editHobby(hobby);
            } else {
                person.addHobby(hobbyAlreadyExists); //Update the list of hobbies on the person edit person when return
                hobbyAlreadyExists.addPerson(person); //Update the list of persons attached to hobby
                facade.editHobby(hobbyAlreadyExists);   //
                hobbyAlreadyExists = null;
            }
        }
    }
/**
 * Method to add a phone. If the phone does not exist, it creates the phone
 * and applies it to a person. If the phone does exist, it will update the
 * list of phones on a person, and add the phone to the person.
 * @param phoneMappers
 * @param infoEntity
 * @return
 * @throws PersonNotFoundException 
 */
    private List<Phone> addPhone(List<PhoneMapper> phoneMappers, InfoEntity infoEntity) throws PersonNotFoundException {
        List<Phone> phones = new ArrayList<>();
        for (PhoneMapper phoneMapper : phoneMappers) {
            try {
                Phone JPACreatedPhone = facade.createPhone(new Phone(phoneMapper.getNumber(), phoneMapper.getDescription(), infoEntity));
                infoEntity.addPhone(JPACreatedPhone);
            } catch (Exception ex) {
                throw new PersonNotFoundException("couldn't create phone");
            }
        }
        return phones;
    }
/**
 * Method to return the list of phones from a person. 
 * @param phoneM
 * @return 
 */
    public List<Phone> toPhoneList(List<PhoneMapper> phoneM) {
        List<Phone> phones = new ArrayList<>();
        for (PhoneMapper phoneMapper : phoneM) {
            Phone phone = facade.getPhoneByPhoneNumber(phoneMapper.getNumber());
            phones.add(phone);
        }
        return phones;
    }

    /**
     * Method to return the list of hobbies from a person.
     * @param hobbyM
     * @return 
     */
    public List<Hobby> toHobbyList(List<HobbyMapper> hobbyM) {
        List<Hobby> hobies = new ArrayList<>();
        for (HobbyMapper hobbyMapper : hobbyM) {
            Hobby hobby = facade.getHobbyFromName(hobbyMapper.getName());
            hobies.add(hobby);
        }
        return hobies;
    }

    /**
     * Method to set the address. It checks if the address with the
     * given ID is null. If it is not, it will fill the address object with
     * information about street, cityinfo etc. and return the object.
     * @param addressM
     * @return 
     */
    public Address toAddress(AddressMapper addressM) {
        Address addressFromJPA = facade.getAddress(addressM.getId());
        if (addressM.getStreet() != null) {
            addressFromJPA.setStreet(addressM.getStreet());
        }
        if (addressM.getAdditionalInfo() != null) {
            addressFromJPA.setAdditionalInfo(addressM.getAdditionalInfo());
        }
        if (addressM.getCity() != null) {
            CityInfo cityInfo = null;
            try {
                cityInfo = facade.getCityInfoByCity(addressM.getCity());
            } catch (Exception e) {
            }
            if (cityInfo != null) {
                addressFromJPA.setCityInfo(cityInfo);
            }
        }
        return addressFromJPA;
    }
}
