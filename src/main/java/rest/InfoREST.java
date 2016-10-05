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
import entity.Company;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jsonmappers.CompanyMapper;
import jsonmappers.PersonMapper;
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

        return gson.toJson(personMapperList);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("company/{phone}")
    public String getCompany(@PathParam("phone") int phone) {
        Company company = facade.getCompanyByPhone(phone);

        return gson.toJson(new CompanyMapper(company));
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void postJson(String content) {
//        Person p = new Gson().fromJson(content, Person.class);
//    }
    /**
     * PUT method for updating or creating an instance of InfoREST
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
