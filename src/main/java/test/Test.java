/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author TimmosQuadros
 */
public class Test {

    public static void main(String[] args) {
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_CA2-1.1_war_1.0-SNAPSHOTPU");
        HashMap<String, Object> puproperties = new HashMap();
        
        puproperties.put("javax.persistence.sql-load-script-source", "scripts/populateZip.sql");
        Persistence.generateSchema("com.mycompany_CA2-1.1_war_1.0-SNAPSHOTPU", puproperties);
        //EntityManager em = emf.createEntityManager();
    }
}
