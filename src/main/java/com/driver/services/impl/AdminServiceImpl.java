package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin = adminRepository1.findById(adminId).get();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);
        serviceProvider.setAdmin(admin);

        List<ServiceProvider>serviceProviders;

        if(admin.getServiceProviders()== null)
            serviceProviders= new ArrayList<>();
        else
            serviceProviders= admin.getServiceProviders();

        serviceProviders.add(serviceProvider);

        adminRepository1.save(admin);
        serviceProviderRepository1.save(serviceProvider);

        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        ServiceProvider serviceProvider= serviceProviderRepository1.findById(serviceProviderId).get();

        Country country = new Country();

        CountryName countryName1 = CountryName.valueOf(countryName);
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());
        country.setServiceProvider(serviceProvider);
        countryRepository1.save(country);

        List<Country> countryList= serviceProvider.getCountryList();
        countryList.add(country);

        serviceProviderRepository1.save(serviceProvider);

        return serviceProvider;
    }
}
