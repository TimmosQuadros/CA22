/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author TimmosQuadros
 */
@Entity
@XmlRootElement
public class Person extends InfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String firstName;
    private String lastName;
    
    @ManyToMany
    private List<Hobby> hobies = new ArrayList<>();

    public Person() {
        super(null, null);
    }
    
    public Person(String mail, Address address) {
        super(mail, address);
    }

    public Person(String firstName, String lastName, String mail, Address address) {
        super(mail, address);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlTransient
    public List<Hobby> getHobies() {
        return hobies;
    }

    public void setHobies(List<Hobby> hobies) {
        this.hobies = hobies;
    }
    
}
