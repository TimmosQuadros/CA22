/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Address;
import entity.CityInfo;
import entity.InfoEntity;
import entity.Person;
import entity.Phone;
import java.util.List;
import javax.persistence.Persistence;
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
public class FacadeTest {

    Facade fp;

    public FacadeTest() {
        fp = new Facade(Persistence.createEntityManagerFactory("com.mycompany_CA2-1.1_war_1.0-SNAPSHOTPU"));
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
//    public void getPerson() {
//        Person p = fp.getPerson(1);
//        
//        assertEquals(p.getFirstName(), "Per");
//    }
//    
//    @Test
//    public void getPersons() {
//        List<Person> persons = fp.getPersons();
//        
//        assertTrue(persons.size()>0);
//        assertTrue(persons.get(0).getFirstName()!=null);
//    }
//    
//    @Test
//    public void getAddress() {
//        Person p = fp.getPerson(1);
//        String test = p.getAddress().getStreet();
//        
//        assertEquals(test, "Parken 14");
//    }
//    
//    @Test
//    public void getHobbies(){
//        Person p = fp.getPerson(1);
//        String test = p.getHobies().get(0).getName();
//        assertEquals(test, "Tennis");
//    }
//    
//    @Test
//    public void getPhone(){
//        Person p = fp.getPerson(1);
//        int test = p.getPhone().get(0).getNumber();
//        assertEquals(test,21232344);
//    }
    
//    @Test
//    public void getPersonFromPhone(){
//        Person p = fp.getPersonByPhone(21232344);
//        assertEquals(p.getFirstName(), "Per");
//    }
    
//    @Test
//    public void getPersonsFromHobby(){
//        List<Person> p = fp.getPersonsFromHobby("Tennis");
//        assertEquals(p.get(0).getFirstName(),"Per");
//    }
    
//    @Test
//    public void postPerson(){
//        CityInfo cityinfo = fp.getCityInfoByCity("Hellerup");
//        assertEquals(cityinfo.getZip(), "2900");
//        Address adr = new Address("Mariegade 26", "Rich city", fp.getCityInfoByCity("Hellerup"));
//        InfoEntity infoE = new InfoEntity("newPerson@gmail.com", adr);
//        
//    }
    
    
}
