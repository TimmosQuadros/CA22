/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Address;

/**
 *
 * @author TimmosQuadros
 */
public class AddressMapper {
    
    private long id;
    private String street;
    private String additionalInfo;
    private String city;
    private String zip;

    public AddressMapper(Address address) {
        id = address.getId();
        street = address.getStreet();
        additionalInfo = address.getAdditionalInfo();
        city = address.getCityInfo().getCity();
        zip = address.getCityInfo().getZip();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
