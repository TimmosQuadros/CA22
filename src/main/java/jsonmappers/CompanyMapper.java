/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Company;
import entity.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TimmosQuadros
 */
public class CompanyMapper {
    
    private long id;
    private String name;
    private String description;
    private int cvr;
    private AddressMapper address;
    private List<PhoneMapper> phones;
    private String mail;
    private int numEmployees;
    private double marketValue;
    
    

    public CompanyMapper(Company company) {
        id = company.getId();
        name = company.getName();
        description = company.getDescription();
        cvr = company.getCvr();
        address = new AddressMapper(company.getAddress());
        phones = phonesToStringList(company.getPhone());
        mail = company.getMail();
        numEmployees = company.getNumEmployees();
        marketValue = company.getMarketValue();
        
    }
    
    /**********************************************************************************
    Because there are bidirectional many to one relationship between phone and infoentity 
    we need to extract data we can't just return a list of phones or else we get
    infinite recursive calls resulting in stackoverflow.
    ***********************************************************************************/
    private List<PhoneMapper> phonesToStringList(List<Phone> phones){
        List<PhoneMapper> list = new ArrayList<>();
        for (Phone phone : phones) {
            list.add(new PhoneMapper(phone));
        }
        return list;
    }

    public List<PhoneMapper> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneMapper> phones) {
        this.phones = phones;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCvr() {
        return cvr;
    }

    public void setCvr(int cvr) {
        this.cvr = cvr;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(int numEmployees) {
        this.numEmployees = numEmployees;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public AddressMapper getAddress() {
        return address;
    }

    public void setAddress(AddressMapper address) {
        this.address = address;
    }
}
