/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Phone;

/**
 *
 * @author TimmosQuadros
 */
public class PhoneMapper {
    
    private int number;
    private String description;

    public PhoneMapper(Phone p) {
        number = p.getNumber();
        description = p.getDescription();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
