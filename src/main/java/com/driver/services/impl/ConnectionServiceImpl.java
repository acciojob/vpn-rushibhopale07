package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user= userRepository2.findById(userId).get();
        if(!user.isConnected())
            throw new Exception("Already connected");
        if(user.getCountry().toString() == countryName)
            return user;

        for (ServiceProvider serviceProvider : user.getServiceProviderList())
        {
            List<Country> countryList = serviceProvider.getCountryList();

            if(countryList.contains(countryName)) {
                user.setMaskedIp(CountryName.valueOf(countryName).toString() + "." + serviceProvider.getId() + "." + userId);
                user.setConnected(true);
                userRepository2.save(user);
                return user;
            }
        }
         throw new Exception("Unable to connect");
    }
    @Override
    public User disconnect(int userId) throws Exception {

        User user= userRepository2.findById(userId).get();
        if(user.isConnected()== false)
            throw new Exception("Already disconnected");
        else
            user.setMaskedIp(null);
            user.setConnected(false);
            userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        User userSender= userRepository2.findById(senderId).get();
        User userReceiver = userRepository2.findById(receiverId).get();

        if(userReceiver ==null || userSender== null)
            throw new Exception("Cannot establish communication");

        if(userSender.getCountry() != userReceiver.getCountry())
        {
            connect(senderId,userSender.getCountry().toString());
        }
        return  userSender;
    }
}
