/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import RESTException.CompanyNotFoundException;
import RESTException.PersonNotFoundException;
import com.google.gson.Gson;
import entity.Address;
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
import jsonmappers.AddressMapper;
import jsonmappers.CompanyMapper;
import jsonmappers.HobbyMapper;
import jsonmappers.PersonMapper;
import jsonmappers.PhoneMapper;

/**
 * REST Web Service
 *
 * @author TimmosQuadros
 */
@Path("company")
public class CompanyResource {

    @Context
    private UriInfo context;
    
    private static Gson gson = new Gson();
    private EntityManagerFactory emf;
    private Facade facade;

    /**
     * Creates a new instance of CompanyResource
     */
    public CompanyResource() {
        emf = Persistence.createEntityManagerFactory("com.mycompany_CA2-1.1_war_1.0-SNAPSHOTPU");
        facade = new Facade(emf);
    }

    /**
     * Retrieves representation of an instance of rest.CompanyResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String content) throws Exception {
        CompanyMapper cm = gson.fromJson(content, CompanyMapper.class);
        AddressMapper tmpAddress = cm.getAddress();
        Address address = new Address(tmpAddress.getStreet(), tmpAddress.getAdditionalInfo(), facade.getCityInfoByCity(tmpAddress.getCity()));
        Address JPACreatedAddress = facade.createAddress(address);

        InfoEntity infoentity = new InfoEntity(cm.getMail(), JPACreatedAddress);

        Company company = new Company(content, address);
        Company JPACreatedCompany = facade.createCompany(company);

        addPhone(cm.getPhones(), JPACreatedCompany);

        facade.editCompany(JPACreatedCompany);

        Company consistentCompany = facade.getCompany(JPACreatedCompany.getId());

        return gson.toJson(new CompanyMapper(JPACreatedCompany));
    }

    /**
     * PUT method for updating or creating an instance of CompanyResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String putJson(String content) throws CompanyNotFoundException, Exception {
        CompanyMapper cm = gson.fromJson(content, CompanyMapper.class);
        
        Company JPAcompany = facade.getCompany(cm.getId());
        
        if(JPAcompany==null){
            throw new CompanyNotFoundException("No company with given id exists, can't edit try a different id.");
        }
        
        if(cm.getCvr()!=0 && cm.getCvr()>9999999){
            JPAcompany.setCvr(cm.getCvr());
        }
        
        if(cm.getDescription()!=null){
            JPAcompany.setDescription(cm.getDescription());
        }
        
        if(cm.getMarketValue()!=0.0 && cm.getMarketValue()>0.0){
            JPAcompany.setMarketValue(cm.getMarketValue());
        }
        
        if(cm.getName()!=null){
            JPAcompany.setName(cm.getName());
        }
        
        if(cm.getNumEmployees()!=0 && cm.getNumEmployees()>0){
            JPAcompany.setNumEmployees(cm.getNumEmployees());
        }
        
        facade.editCompany(JPAcompany);

        Company consistentCompany = facade.getCompany(JPAcompany.getId());

        return gson.toJson(new CompanyMapper(consistentCompany));
        
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{phone}")
    public String getCompany(@PathParam("phone") int phone) {
        Company company = facade.getCompanyByPhone(phone);

        return gson.toJson(new CompanyMapper(company));
    }
    
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
    
    
}
