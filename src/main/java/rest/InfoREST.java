/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import RESTException.PersonNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Address;
import entity.Company;
import entity.Hobby;
import entity.InfoEntity;
import entity.Person;
import entity.Phone;
import facade.Facade;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jsonmappers.CompanyMapper;
import jsonmappers.HobbyMapper;
import jsonmappers.PersonMapper;
import jsonmappers.PhoneMapper;
import utility.JSONConverter;

/**
 * REST Web Service
 *
 * @author TimmosQuadros
 */
@Path("person")
public class InfoREST {

    @Context
    private UriInfo context;
    private static Gson gson = new Gson();
    private EntityManagerFactory emf;
    private Facade facade;

    /**
     * Creates a new instance of InfoREST
     */
    public InfoREST() {
        emf = Persistence.createEntityManagerFactory("com.mycompany_CA2-1.1_war_1.0-SNAPSHOTPU");
        facade = new Facade(emf);
    }

    /**
     * Retrieves representation of an instance of rest.InfoREST
     *
     * @param phone
     * @return an instance of java.lang.String
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
        return "{"+gson.toJson(personMapperList)+"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("company/{phone}")
    public String getCompany(@PathParam("phone") int phone) {
        Company company = facade.getCompanyByPhone(phone);

        return gson.toJson(new CompanyMapper(company));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String content) throws Exception {
        PersonMapper pm = gson.fromJson(content, PersonMapper.class);
        Address address = new Address(pm.getStreet(),pm.getAdditionalInfo(),facade.getCityInfoByCity(pm.getCity()));
        Address JPACreatedAddress = facade.createAddress(address);
        
        InfoEntity infoentity = new InfoEntity(pm.getEmail(), JPACreatedAddress);
        //InfoEntity JPACreatedInfoEntity = facade.createInfoEntity(infoentity);
        
        
        Person p = new Person(pm.getFirstname(), pm.getLastname(), pm.getEmail(), JPACreatedAddress);
        Person JPACreatedPerson = facade.createPerson(p);
        
        
        
        addPhone(pm.getPhones(), JPACreatedPerson);
        addHobby(pm.getHobbies(), JPACreatedPerson);
        
        facade.editPerson(JPACreatedPerson);
        
        Person consistentPerson = facade.getPerson(JPACreatedPerson.getId());
        
        return gson.toJson(new PersonMapper(consistentPerson));
    }
    /**
     * PUT method for updating or creating an instance of InfoREST
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    
    private void addHobby(List<HobbyMapper> hobbyMappers,Person person) throws PersonNotFoundException, Exception{
        Hobby hobbyAlreadyExists=null;
        for (HobbyMapper hobbyMapper : hobbyMappers) {
            try {
                hobbyAlreadyExists=facade.getHobbyFromName(hobbyMapper.getName());
            } catch (Exception e) {
                
            }
            if(hobbyAlreadyExists==null){ //That is the hobby doesn't already exists
                Hobby hobby = new Hobby(hobbyMapper.getName(), hobbyMapper.getDescription(), new ArrayList<Person>()); //the list of persons belonging to this hobby is empty since it is a new hobby...
                facade.createHobby(hobby);
                person.addHobby(hobby);
                hobby.addPerson(person);
                facade.editHobby(hobby);
            }else{
                person.addHobby(hobbyAlreadyExists); //Update the list of hobbies on the person edit person when return
                hobbyAlreadyExists.addPerson(person); //Update the list of persons attached to hobby
                facade.editHobby(hobbyAlreadyExists);   //
                hobbyAlreadyExists=null;
            }
        }
    }
    
    private List<Phone> addPhone(List<PhoneMapper> phoneMappers,InfoEntity infoEntity) throws PersonNotFoundException{
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
}
