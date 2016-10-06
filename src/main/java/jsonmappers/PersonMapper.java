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
    
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private List<PhoneMapper> phones;
    private AddressMapper address;
    private List<HobbyMapper> hobbies;
    
    public PersonMapper(Person p) {
        id = p.getId();
        firstname = p.getFirstName();
        lastname = p.getLastName();
        email = p.getMail();
        phones = phonesToStringList(p.getPhone());
        address = new AddressMapper(p.getAddress());
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<HobbyMapper> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyMapper> hobbies) {
        this.hobbies = hobbies;
    }

    public AddressMapper getAddress() {
        return address;
    }

    public void setAddress(AddressMapper address) {
        this.address = address;
    }
}
