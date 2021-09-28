//import data.*;
//import entities.DBPerson;
//import operations.PersonOperations;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import java.util.Optional;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class PersonCRUDTests {
//    private Coordinates coord;
//    private Location location;
//    private Person person;
//
//    private PersonOperations po;
//
//
//    @BeforeAll
//    void setUp(){
//        coord = new Coordinates(1L,4.0);
//        location = new Location(1,2F,3F);
//        person = new Person("Хибернет-козел", coord, 125F, Color.ORANGE, Color.GREEN, Country.JAPAN, location);
//
//        po = new PersonOperations();
//    }
//
//    @Test
//    void addPerson() {
//        long id = po.createPerson(person.toDBPerson());
//
//        Assertions.assertNotNull(id);
//    }
//
//    @Test
//    void getPerson() {
//        // протестить запрос несуществующего человека
//        DBPerson expectedPerson = person.toDBPerson();
//        long id = po.createPerson(expectedPerson);
//        expectedPerson.setId(id);
//
//        Optional<DBPerson> dbPerson = po.getPerson(id);
//        Assertions.assertTrue(dbPerson.isPresent());
//        DBPerson actualPerson = dbPerson.get();
//
//        Assertions.assertTrue(expectedPerson.equals(actualPerson));
//    }
//
//    @Test
//    void deletePerson() {
//        // протестить удаление несуществующего человека
//        // проверить, реально ли такого человека больше нет в бд
//        long id = po.createPerson(person.toDBPerson());
//
//        Assertions.assertTrue(po.deletePerson(id));
//    }
//}
