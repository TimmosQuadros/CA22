package utility;

import com.google.gson.Gson;
import java.util.List;
import entity.Person;

/**
 *
 * @author Peter
 */
public class JSONConverter {
    
    public static Person getPersonFromJSON(String js) {
        return new Gson().fromJson(js, Person.class);
    }
    public static String getJSONfromPerson(Person p) {
        return new Gson().toJson(p, Person.class);
    }
    public static String getJSONfromPerson(List<Person> persons) {
        return new Gson().toJson(persons);
    }
    
}
