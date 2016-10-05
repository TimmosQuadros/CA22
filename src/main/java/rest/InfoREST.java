/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Company;
import entity.Hobby;
import entity.Person;
import entity.Phone;
import facade.Facade;
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
        JsonArray datasetsHobies = new JsonArray();
        JsonArray datasetsPhones = new JsonArray();
        JsonArray datasetsAddress = new JsonArray();

        JsonObject JSON = new JsonObject();

        List<Person> persons = facade.getPersons();

        Person person = facade.getPersonByPhone(phone);

        List<Phone> phones = person.getPhone();

        for (Phone phone1 : phones) {

            if (phone1.getNumber() == phone) {

                List<Hobby> hobbies = person.getHobies();

                for (Hobby hobby : hobbies) {
                    JsonObject nameHobby = new JsonObject();
                    JsonObject descriptionHobby = new JsonObject();

                    nameHobby.addProperty("name", hobby.getName());
                    descriptionHobby.addProperty("description", hobby.getDescription());

                    datasetsHobies.add(nameHobby);
                    datasetsHobies.add(descriptionHobby);
                }

                for (Phone phonePerson : phones) {
                    JsonObject phoneNumber = new JsonObject();
                    JsonObject phoneDescription = new JsonObject();

                    phoneNumber.addProperty("number", phonePerson.getNumber());
                    phoneDescription.addProperty("description", phonePerson.getDescription());

                    datasetsPhones.add(phoneNumber);
                    datasetsPhones.add(phoneDescription);
                }
                JsonObject street = new JsonObject();
                JsonObject city = new JsonObject();
                JsonObject zip = new JsonObject();
                JsonObject additionalInfo = new JsonObject();

                street.addProperty("street", person.getAddress().getStreet());
                city.addProperty("city", person.getAddress().getCityInfo().getCity());
                zip.addProperty("zip", person.getAddress().getCityInfo().getZip());
                additionalInfo.addProperty("additional info", person.getAddress().getAdditionalInfo());

                datasetsAddress.add(street);
                datasetsAddress.add(city);
                datasetsAddress.add(zip);
                datasetsAddress.add(additionalInfo);

                JSON.addProperty("firstName", person.getFirstName());
                JSON.addProperty("lastName", person.getLastName());
                JSON.addProperty("mail", person.getMail());

            }
        }

        JSON.add("hobbies", datasetsHobies);
        JSON.add("phone", datasetsPhones);
        JSON.add("address", datasetsAddress);

        return JSON.toString();
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
        JsonArray datasetsHobies = new JsonArray();
        JsonArray datasetsPhones = new JsonArray();
        JsonArray datasetsAddress = new JsonArray();
        JsonArray datasetsContainer = new JsonArray();

        JsonObject JSON = new JsonObject();

        List<Person> persons = facade.getPersonsFromHobby(hobby);

        for (Person person : persons) {

            List<Phone> phones = person.getPhone();

            for (Phone phone1 : phones) {

                List<Hobby> hobbies = person.getHobies();

                for (Hobby hobbyE : hobbies) {
                    JsonObject nameHobby = new JsonObject();
                    JsonObject descriptionHobby = new JsonObject();

                    nameHobby.addProperty("name", hobbyE.getName());
                    descriptionHobby.addProperty("description", hobbyE.getDescription());

                    datasetsHobies.add(nameHobby);
                    datasetsHobies.add(descriptionHobby);
                }

                for (Phone phonePerson : phones) {
                    JsonObject phoneNumber = new JsonObject();
                    JsonObject phoneDescription = new JsonObject();

                    phoneNumber.addProperty("number", phonePerson.getNumber());
                    phoneDescription.addProperty("description", phonePerson.getDescription());

                    datasetsPhones.add(phoneNumber);
                    datasetsPhones.add(phoneDescription);
                }
                JsonObject street = new JsonObject();
                JsonObject city = new JsonObject();
                JsonObject zip = new JsonObject();
                JsonObject additionalInfo = new JsonObject();

                street.addProperty("street", person.getAddress().getStreet());
                city.addProperty("city", person.getAddress().getCityInfo().getCity());
                zip.addProperty("zip", person.getAddress().getCityInfo().getZip());
                additionalInfo.addProperty("additional info", person.getAddress().getAdditionalInfo());

                datasetsAddress.add(street);
                datasetsAddress.add(city);
                datasetsAddress.add(zip);
                datasetsAddress.add(additionalInfo);
                
                JsonObject firstName = new JsonObject();
                JsonObject lastName = new JsonObject();
                JsonObject mail = new JsonObject();

                firstName.addProperty("firstName", person.getFirstName());
                lastName.addProperty("lastName", person.getLastName());
                mail.addProperty("mail", person.getMail());
                
                datasetsContainer.add(firstName);
                datasetsContainer.add(lastName);
                datasetsContainer.add(mail);
                
                JsonObject hobbiesJsonObject = new JsonObject();
                JsonObject phonesJsonObject = new JsonObject();
                JsonObject addressesJsonObject = new JsonObject();
                
                hobbiesJsonObject.add("hobbies", datasetsHobies);
                phonesJsonObject.add("phone", datasetsPhones);
                addressesJsonObject.add("address", datasetsAddress);
                
                datasetsContainer.add(hobbiesJsonObject);
                datasetsContainer.add(phonesJsonObject);
                datasetsContainer.add(addressesJsonObject);
            }
            
            JSON.add(""+person.getFirstName()+"",datasetsContainer);
        }

        return JSON.toString();
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
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void postJson(String content) {
        
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
}
