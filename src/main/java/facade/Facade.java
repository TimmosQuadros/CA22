/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Address;
import entity.CityInfo;
import entity.Company;
import entity.Hobby;
import entity.InfoEntity;
import entity.Person;
import entity.Phone;
import facade.exceptions.IllegalOrphanException;
import facade.exceptions.NonexistentEntityException;
import facade.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class Facade {

    private AddressJpaController ajpa;
    private CityInfoJpaController cijpa;
    private CompanyJpaController cojpa;
    private HobbyJpaController hjpa;
    private InfoEntityJpaController ijpa;
    private PersonJpaController pejpa;
    private PhoneJpaController phjpa;

    EntityManagerFactory emf;

    public Facade(EntityManagerFactory emf) {
        ajpa = new AddressJpaController(emf);
        cijpa = new CityInfoJpaController(emf);
        cojpa = new CompanyJpaController(emf);
        hjpa = new HobbyJpaController(emf);
        ijpa = new InfoEntityJpaController(emf);
        pejpa = new PersonJpaController(emf);
        phjpa = new PhoneJpaController(emf);
    }

    //Create
    public void createAddress(Address address) throws Exception {
        ajpa.create(address);
    }

    public void createCityInfo(CityInfo cityinfo) throws Exception {
        cijpa.create(cityinfo);
    }

    public void createCompany(Company company) throws Exception {
        cojpa.create(company);
    }

    public void createHobby(Hobby hobby) throws Exception {
        hjpa.create(hobby);
    }

    public void createInfoEntity(InfoEntity infoentity) throws Exception {
        ijpa.create(infoentity);
    }

    public void createPerson(Person person) throws Exception {
        pejpa.create(person);
    }

    public void createPhone(Phone phone) throws Exception {
        phjpa.create(phone);
    }

    //Retreive
    public Address getAddress(long id) {
        return ajpa.findAddress(id);
    }

    public CityInfo getCityInfo(long id) {
        return cijpa.findCityInfo(id);
    }

    public Company getCompany(long id) {
        return cojpa.findCompany(id);
    }

    public Hobby getHobby(long id) {
        return hjpa.findHobby(id);
    }

    public InfoEntity getInfoEntity(long id) {
        return ijpa.findInfoEntity(id);
    }

    public Person getPerson(long id) {
        return pejpa.findPerson(id);
    }

    public Phone getPhone(long id) {
        return phjpa.findPhone(id);
    }
    
    //Retreive specific
    public Person getPersonByPhone(int phonenumber){
        return pejpa.findPersonByPhone(phonenumber);
    }
    
    public List<Person> getPersonsFromHobby(String hobbyName){
        return hjpa.findPersonsByHobby(hobbyName);
    }
    
    public Company getCompanyByPhone(int phonenumber){
        return cojpa.findCompanyByPhone(phonenumber);
    }
    
    public CityInfo getCityInfoByCity(String city){
        return cijpa.findCityInfo(city);
    }

    //Retreive all
    public List<Address> getAddresses() {
        return ajpa.findAddressEntities();
    }

    public List<CityInfo> getCityInfos() {
        return cijpa.findCityInfoEntities();
    }

    public List<Company> getCompanies() {
        return cojpa.findCompanyEntities();
    }

    public List<Hobby> getHobbies() {
        return hjpa.findHobbyEntities();
    }

    public List<InfoEntity> getInfoEntities() {
        return ijpa.findInfoEntityEntities();
    }

    public List<Person> getPersons() {
        return pejpa.findPersonEntities();
    }

    public List<Phone> getPhones() {
        return phjpa.findPhoneEntities();
    }
    
    //Update
    public void editAddress(Address address) throws Exception{
        ajpa.edit(address);
    }
    
    public void editCityInfos(CityInfo cityinfo) throws Exception{
        cijpa.edit(cityinfo);
    }
    
    public void editCompany(Company company) throws Exception{
        //Not Implemented
    }
    
    public void editHobby(Hobby hobby) throws Exception{
        hjpa.edit(hobby);
    }

    public void editInfoEntity(InfoEntity infoentity) throws Exception{
        ijpa.edit(infoentity);
    }
    
    public void editPerson(Person person){
        //Not Implemented
    }
    
    public void editPhone(Phone phone) throws Exception{
        phjpa.edit(phone);
    }
    
    //Delete
    public void deleteAddress(long id) throws IllegalOrphanException, NonexistentEntityException{
        ajpa.destroy(id);
    }
    
    public void deleteCityInfo(long id) throws NonexistentEntityException{
        cijpa.destroy(id);
    }
    
    public void deleteCompany(long id) throws IllegalOrphanException, NonexistentEntityException {
        cojpa.destroy(id);
    }
    
    public void deleteHobby(long id) throws NonexistentEntityException {
        hjpa.destroy(id);
    }
    
    public void deleteInfoentity(long id) throws IllegalOrphanException, NonexistentEntityException {
        ijpa.destroy(id);
    }
    
    public void deletePerson(long id) throws IllegalOrphanException, NonexistentEntityException {
        pejpa.destroy(id);
    }
    
    public void deletePhone(long id) throws NonexistentEntityException {
        phjpa.destroy(id);
    }
    

}