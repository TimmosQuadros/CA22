/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmappers;

import entity.Company;

/**
 *
 * @author TimmosQuadros
 */
public class CompanyMapper {
    
    private long id;
    private String name;
    private String description;
    private int cvr;
    private int numEmployees;
    private double marketValue;

    public CompanyMapper(Company company) {
        id = company.getId();
        name = company.getName();
        description = company.getDescription();
        cvr = company.getCvr();
        numEmployees = company.getNumEmployees();
        marketValue = company.getMarketValue();
    }
    
    
}
