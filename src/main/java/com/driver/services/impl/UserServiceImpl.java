package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        if(countryName.equalsIgnoreCase("ind") || countryName.equalsIgnoreCase("aus") || countryName.equalsIgnoreCase("usa") || countryName.equalsIgnoreCase("chi") || countryName.equalsIgnoreCase("jpn")) {


            // to register user we cretae user and their country
            User user = new User();

            user.setUsername(username);
            user.setPassword(password);
            userRepository3.save(user);


            Country country = new Country();
            if (countryName.equalsIgnoreCase("ind")) {
                country.setCountryName(CountryName.IND);
                country.setCode(CountryName.IND.toCode());
            } else if (countryName.equalsIgnoreCase("aus")) {
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            } else if (countryName.equalsIgnoreCase("usa")) {
                country.setCountryName(CountryName.USA);
                country.setCode(CountryName.USA.toCode());
            } else if (countryName.equalsIgnoreCase("chi")) {
                country.setCountryName(CountryName.CHI);
                country.setCode(CountryName.CHI.toCode());
            } else {
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }


            user.setOriginalCountry(country);
            user.setOriginalIp(country.getCode() + "." + user.getId());
            user.setConnected(false);
            user.setMaskedIp(null);

            country.setUser(user);

            userRepository3.save(user);

            return user;
        }
        else
            throw  new Exception("Country not found");
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user= userRepository3.findById(userId).get();
       ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

       List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
       serviceProviderList.add(serviceProvider);

       user.setServiceProviderList(serviceProviderList);

       userRepository3.save(user);
       serviceProviderRepository3.save(serviceProvider);

       return  user;
    }
}
