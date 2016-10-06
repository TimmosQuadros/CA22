/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TimmosQuadros
 */
public class person {
    
    public person() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

//    @Test
//    public void testGetPerson() {
//        RestAssured.when().get("http://localhost:8080/CA2-1.1/api/person/21234721").then().statusCode(404);
//    }
    
//    @Test
//    public void testGetPersonWorks() {
//        
//        RestAssured.when().get("http://localhost:8080/CA2-1.1/api/person/complete/1").then().statusCode(200);
//    }
}
