/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Person;
import entity.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TimmosQuadros
 */
public class PersonMapper1 {
    
    private long id;
    private String name;
    private String email;
    private List<PhoneMapper> phones;
    
    public PersonMapper1(Person p) {
        id = p.getId();
        name = p.getFirstName()+" "+p.getLastName();
        email = p.getMail();
        phones = phonesToStringList(p.getPhone());
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
}
