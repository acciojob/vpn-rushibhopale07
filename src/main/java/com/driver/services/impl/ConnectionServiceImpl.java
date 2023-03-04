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

        if(user.getConnected())
            throw new Exception("Already connected");

        if(countryName.equalsIgnoreCase(user.getOriginalCountry().toString()))
            return user;


        ServiceProvider serviceProvider = null;
        Country country = null;
        int minProviderId= Integer.MAX_VALUE;

        for(ServiceProvider serviceProvider1 : user.getServiceProviderList())
        {
            //we take one by one provider where have the country as we want
            // as  well as they have minimum id numbeer

            for(Country country1: serviceProvider1.getCountryList())
            {
                if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && serviceProvider1.getId() < minProviderId )
                {
                    country= country1;
                    serviceProvider=serviceProvider1;
                    minProviderId =serviceProvider1.getId();
                }
            }
        }
        // when we get a specific serviceprovider with a as you want countryname
        if(serviceProvider != null)
        {
            Connection connection = new Connection();
            connection.setUser(user);
            connection.setServiceProvider(serviceProvider);

            //to set the maskID we want to get countrycode
            //updatedCountryCode.serviceProviderId.userId

            String updatedCountryCode= country.getCode();
            user.setMaskedIp(updatedCountryCode+"."+serviceProvider.getId()+"."+userId);
            user.setConnected(true);

            List<Connection> connectionList =user.getConnectionList();
            connectionList.add(connection);

            user.setConnectionList(connectionList);
            serviceProvider.setConnectionList(connectionList);

            userRepository2.save(user);
            serviceProviderRepository2.save(serviceProvider);
            connectionRepository2.save(connection);

            return user;
        }
        else
            throw new Exception("Unable to connect");



    }
    @Override
    public User disconnect(int userId) throws Exception {

        User user= userRepository2.findById(userId).get();
        if(user.getConnected()== false)
            throw new Exception("Already disconnected");

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

        if(userSender.getOriginalCountry() != userReceiver.getOriginalCountry())
        {
            connect(senderId, userSender.getOriginalCountry().getCountryName().toString());
        }
        return  userSender;
    }
}
