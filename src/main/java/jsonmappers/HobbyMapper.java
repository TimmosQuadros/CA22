/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Hobby;

/**
 *
 * @author TimmosQuadros
 */
public class HobbyMapper {
    private String name;
    private String description;

    public HobbyMapper(Hobby hobby) {
        name = hobby.getName();
        description = hobby.getDescription();
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
    
}
