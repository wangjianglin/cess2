package io.cess.auth.service.impl;

import io.cess.auth.entity.ClientDetail;
//import io.cess.auth.mapper.ClientDetailMapper;
//import io.cess.auth.repository.ClientDetailRepository;
//import io.cess.auth.repository.ClientDetailRepository;
import io.cess.auth.mapper.ClientDetailMapper;
import io.cess.auth.repository.ClientDetailRepository;
import io.cess.auth.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDetailMapper clientDetailMapper;

    @Autowired
    private ClientDetailRepository clientDetailRepository;

    @Override
    public ClientDetail loadClientByClientId(String clientId) {

        ClientDetail clientDetail = clientDetailMapper.findById(clientId);

        clientDetail = clientDetailRepository.findByClientId(clientId);
//
        return clientDetail;
//        return null;

//        ClientDetail clientDetial = new ClientDetail();
//
//        clientDetial.setClientId("web_app");
//        clientDetial.setClientSecret("{noop}123456");
//
//        clientDetial.setAuthorities("FOO_READ,FOO_WRITE");
//        clientDetial.setScope("FOO,FOO2");
//        clientDetial.setAutoapprove("FOO,FOO2");
//        clientDetial.setGrantTypes("implicit,refresh_token,password,authorization_code,client_credentials");
//
//
//        return clientDetial;
    }
}
