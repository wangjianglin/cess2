package io.cess.auth.controller;


import io.cess.auth.entity.ClientDetail;
import io.cess.auth.repository.ClientDetailRepository;
import io.cess.core.spring.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientDetailRepository clientDetailRepository;

    @RequestMapping("/{clientId}")
    @JsonBody
    public ClientDetail getClientDetailById(@PathVariable("clientId") String clientId){

        ClientDetail clientDetail = clientDetailRepository.findByClientId(clientId);

        clientDetail.setClientSecret(null);

        return clientDetail;
    }
}
