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

        User user = new User();

        Country country = new Country();
        CountryName countryName1 = CountryName.valueOf(countryName);
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());
        country.setUser(user);

        user.setUsername(username);
        user.setPassword(password);
        user.setOriginalIp(country.getCode()+"."+user.getId());
        user.setConnected(false);
        user.setMaskedIp(null);

        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user= userRepository3.findById(userId).get();
       ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

       List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
       serviceProviderList.add(serviceProvider);
       user.setServiceProviderList(serviceProviderList);

       userRepository3.save(user);

       return  user;
    }
}
