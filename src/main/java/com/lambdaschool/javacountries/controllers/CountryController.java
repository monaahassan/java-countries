package com.lambdaschool.javacountries.controllers;

import com.lambdaschool.javacountries.models.Country;
import com.lambdaschool.javacountries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController {
    @Autowired
    CountryRepository countryrepo;

    private List<Country> findCountry (List<Country> myList, CheckCountry tester){
        List<Country> temp = new ArrayList<>();

        for (Country country : myList) {
            if (tester.test(country)){
                temp.add(country);
            }
        }
        return temp;
    }

    //    http://localhost:2019/countries/names/all
    @GetMapping(value = "names/all", produces = "application/json")
    public ResponseEntity<?> listAllCountries(){
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }
    //    http://localhost:2019/countries/names/start/all
    @GetMapping(value = "/names/start/{letter}", produces = "application/json")
    public ResponseEntity<?> countryByLetter(@PathVariable char letter){
        List<Country> myList = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(myList::add);
        List<Country> rtnList = findCountry(myList, c -> c.getName().charAt(0) == letter);
       return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    @GetMapping(value = "/population/total", produces = "application/json")
    public ResponseEntity<?> totalPopulation(){
        List<Country> countries = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(countries::add);

        long total = 0;

        for (Country c : countries){
            total+=c.getPopulation();
        }

        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @GetMapping(value = "/population/min", produces = "application/json")
    public ResponseEntity<?> minPopulation(){
        List<Country> countries = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(countries::add);

//        long min = countries.get(0).getPopulation();
        Country toReturn = countries.get(0);

        for (Country c : countries) {
            if (toReturn.getPopulation() > c.getPopulation()){
                toReturn = c;
            }
        }
        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }
    @GetMapping(value = "/population/max", produces = "application/json")
    public ResponseEntity<?> maxPopulation(){

        List<Country> countries = new ArrayList<>();
        countryrepo.findAll().iterator().forEachRemaining(countries::add);

        Country toReturn = countries.get(0);

        for (Country c : countries) {
            if (toReturn.getPopulation() < c.getPopulation()){
                toReturn = c;
            }
        }

        return new ResponseEntity<>(toReturn, HttpStatus.OK);
    }
}