/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

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
import jsonmappers.PersonMapper;
import utility.JSONConverter;

/**
 * REST Web Service
 *
 * @author TimmosQuadros
 */
@Path("info")
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
    public String getPerson(@PathParam("phone") int phone) {
//        JsonArray datasetsHobies = new JsonArray();
//        JsonArray datasetsPhones = new JsonArray();
//        JsonArray datasetsAddress = new JsonArray();
//
//        JsonObject JSON = new JsonObject();
//
//        List<Person> persons = facade.getPersons();
//
        Person person = facade.getPersonByPhone(phone);
//
//        List<Phone> phones = person.getPhone();
//
//        for (Phone phone1 : phones) {
//
//            if (phone1.getNumber() == phone) {
//
//                List<Hobby> hobbies = person.getHobies();
//
//                for (Hobby hobby : hobbies) {
//                    JsonObject nameHobby = new JsonObject();
//                    JsonObject descriptionHobby = new JsonObject();
//
//                    nameHobby.addProperty("name", hobby.getName());
//                    descriptionHobby.addProperty("description", hobby.getDescription());
//
//                    datasetsHobies.add(nameHobby);
//                    datasetsHobies.add(descriptionHobby);
//                }
//
//                for (Phone phonePerson : phones) {
//                    JsonObject phoneNumber = new JsonObject();
//                    JsonObject phoneDescription = new JsonObject();
//
//                    phoneNumber.addProperty("number", phonePerson.getNumber());
//                    phoneDescription.addProperty("description", phonePerson.getDescription());
//
//                    datasetsPhones.add(phoneNumber);
//                    datasetsPhones.add(phoneDescription);
//                }
//                JsonObject street = new JsonObject();
//                JsonObject city = new JsonObject();
//                JsonObject zip = new JsonObject();
//                JsonObject additionalInfo = new JsonObject();
//
//                street.addProperty("street", person.getAddress().getStreet());
//                city.addProperty("city", person.getAddress().getCityInfo().getCity());
//                zip.addProperty("zip", person.getAddress().getCityInfo().getZip());
//                additionalInfo.addProperty("additional info", person.getAddress().getAdditionalInfo());
//
//                datasetsAddress.add(street);
//                datasetsAddress.add(city);
//                datasetsAddress.add(zip);
//                datasetsAddress.add(additionalInfo);
//
//                JSON.addProperty("firstName", person.getFirstName());
//                JSON.addProperty("lastName", person.getLastName());
//                JSON.addProperty("mail", person.getMail());
//
//            }
//        }
//
//        JSON.add("hobbies", datasetsHobies);
//        JSON.add("phone", datasetsPhones);
//        JSON.add("address", datasetsAddress);

        return gson.toJson(new PersonMapper(person));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persona/{id}")
    public String getPersonFromId(@PathParam("id") int id) {

        

        Person person = facade.getPerson(id);
        
        PersonMapper pm = new PersonMapper(person);
        
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
    public String getPersons(@PathParam("hobby") String hobby) {
        List<Person> persons = facade.getPersonsFromHobby(hobby);
        List<PersonMapper> personMapperList = new ArrayList<>();
        for (Person person : persons) {
            personMapperList.add(new PersonMapper(person));
        }

        return gson.toJson(personMapperList);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("company/{phone}")
    public String getCompany(@PathParam("phone") int phone) {
        JsonArray datasetsPhones = new JsonArray();
        JsonObject JSON = new JsonObject();

        Company company = facade.getCompanyByPhone(phone);

        List<Phone> phones = company.getPhone();

        for (Phone phonePerson : phones) {
            JsonObject phoneNumber = new JsonObject();
            JsonObject phoneDescription = new JsonObject();

            phoneNumber.addProperty("number", phonePerson.getNumber());
            phoneDescription.addProperty("description", phonePerson.getDescription());

            datasetsPhones.add(phoneNumber);
            datasetsPhones.add(phoneDescription);
        }

        JSON.addProperty("cvr", company.getCvr());
        JSON.addProperty("description", company.getDescription());
        JSON.addProperty("marketvalue", company.getMarketValue());
        JSON.addProperty("name", company.getName());
        JSON.addProperty("numEmployees", company.getNumEmployees());
        JSON.add("phones", datasetsPhones);
        JSON.addProperty("email", company.getMail());
        JSON.addProperty("street", company.getAddress().getStreet());
        JSON.addProperty("city", company.getAddress().getCityInfo().getCity());
        JSON.addProperty("zip", company.getAddress().getCityInfo().getZip());
        JSON.addProperty("addressInfo", company.getAddress().getAdditionalInfo());

        return JSON.toString();
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
