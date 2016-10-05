/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Person;

/**
 *
 * @author TimmosQuadros
 */
public class PersonMapper {
    
    private long id;
    private String firstname;
    private String lastname;
    private String street;
    

    public PersonMapper(Person p) {
        id = p.getId();
        firstname = p.getFirstName();
        lastname = p.getLastName();
        street = p.getAddress().getStreet();
    }
    
    
    
}
