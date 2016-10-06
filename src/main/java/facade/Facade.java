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
    public Address createAddress(Address address) throws Exception {
        return ajpa.create(address);
    }

    public void createCityInfo(CityInfo cityinfo) throws Exception {
        cijpa.create(cityinfo);
    }

    public Company createCompany(Company company) throws Exception {
        return cojpa.create(company);
    }

    public void createHobby(Hobby hobby) throws Exception {
        hjpa.create(hobby);
    }

    public InfoEntity createInfoEntity(InfoEntity infoentity) throws Exception {
        return ijpa.create(infoentity);
    }

    public Person createPerson(Person person) throws Exception {
        return pejpa.create(person);
    }

    public Phone createPhone(Phone phone) throws Exception {
        return phjpa.create(phone);
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
    public Person getPersonByPhone(int phonenumber) {
        return pejpa.findPersonByPhone(phonenumber);
    }
    
    public Phone getPhoneByPhoneNumber(int phonenumber){
        return phjpa.findPhoneByPhoneNumber(phonenumber);
    }

    public List<Person> getPersonsFromHobby(String hobbyName) {
        return hjpa.findPersonsByHobby(hobbyName);
    }
    
    public Hobby getHobbyFromName(String hobbyName){
        return hjpa.findHobbyByName(hobbyName);
    }

    public Company getCompanyByPhone(int phonenumber) {
        return cojpa.findCompanyByPhone(phonenumber);
    }

    public CityInfo getCityInfoByCity(String city) {
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
    public void editAddress(Address address) throws Exception {
        ajpa.edit(address);
    }

    public void editCityInfos(CityInfo cityinfo) throws Exception {
        cijpa.edit(cityinfo);
    }

    public void editCompany(Company company) throws Exception {
        cojpa.edit(company);
    }

    public void editHobby(Hobby hobby) throws Exception {
        hjpa.edit(hobby);
    }

    public void editInfoEntity(InfoEntity infoentity) throws Exception {
        ijpa.edit(infoentity);
    }

    public void editPerson(Person person) {
        pejpa.edit(person);
    }

    public void editPhone(Phone phone) throws Exception {
        phjpa.edit(phone);
    }

    //Delete
    public void deleteAddress(long id) throws IllegalOrphanException, NonexistentEntityException {
        ajpa.destroy(id);
    }

    public void deleteCityInfo(long id) throws NonexistentEntityException {
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
