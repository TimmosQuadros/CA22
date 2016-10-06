/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Hobby;
import entity.Person;
import entity.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TimmosQuadros
 */
public class PersonMapper {
    
    private String firstname;
    private String lastname;
    private String email;
    private List<PhoneMapper> phones;
    private String street;
    private String additionalInfo;
    private String zipcode;
    private String city;
    private List<HobbyMapper> hobbies;
    
    public PersonMapper(Person p) {
        firstname = p.getFirstName();
        lastname = p.getLastName();
        email = p.getMail();
        phones = phonesToStringList(p.getPhone());
        street = p.getAddress().getStreet();
        additionalInfo = p.getAddress().getAdditionalInfo();
        zipcode = p.getAddress().getCityInfo().getZip();
        city = p.getAddress().getCityInfo().getCity();
        hobbies = hobbiesToStringList(p.getHobies());
    }
    
    /**********************************************************************************
    Because there are bidirectional many to one relationship between phone and infoentity 
    we need to extract data we can't just return a list of phones or else we get
    infinite recursive calls resulting in stackoverflow.
    **********************************************************************************/
    private List<PhoneMapper> phonesToStringList(List<Phone> phones){
        List<PhoneMapper> list = new ArrayList<>();
        for (Phone phone : phones) {
            list.add(new PhoneMapper(phone));
        }
        return list;
    }
    
    private List<HobbyMapper> hobbiesToStringList(List<Hobby> hobbies){
        List<HobbyMapper> list = new ArrayList<>();
        for (Hobby hobby : hobbies) {
            list.add(new HobbyMapper(hobby));
        }
        return list;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhoneMapper> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneMapper> phones) {
        this.phones = phones;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<HobbyMapper> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyMapper> hobbies) {
        this.hobbies = hobbies;
    }
    
    
}
