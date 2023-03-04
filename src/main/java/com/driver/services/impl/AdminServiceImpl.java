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
        admin.setUsername(username);
        admin.setPassword(password);
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

        serviceProviderRepository1.save(serviceProvider);

        adminRepository1.save(admin);


        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
// ind, aus, usa, chi, jpn.
        if(countryName.equalsIgnoreCase("ind") || countryName.equalsIgnoreCase("aus") || countryName.equalsIgnoreCase("usa") || countryName.equalsIgnoreCase("chi") || countryName.equalsIgnoreCase("jpn")) {


            ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
            Country country = new Country();

            if(countryName.equalsIgnoreCase("ind")) {
                country.setCountryName(CountryName.IND);
                country.setCode(CountryName.IND.toCode());
            } else if (countryName.equalsIgnoreCase("aus")) {
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            }
            else if (countryName.equalsIgnoreCase("usa")) {
                country.setCountryName(CountryName.USA);
                country.setCode(CountryName.USA.toCode());
            }
            else if (countryName.equalsIgnoreCase("chi")) {
                country.setCountryName(CountryName.CHI);
                country.setCode(CountryName.CHI.toCode());
            }
            else {
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }

            country.setServiceProvider(serviceProvider);
            List<Country> countryList = serviceProvider.getCountryList();
            countryList.add(country);

            serviceProvider.setCountryList(countryList);

            return serviceProvider;
        }
        else
            throw new Exception("Country not found");
    }
}
