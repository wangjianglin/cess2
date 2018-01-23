package io.cess.auth.service;

import io.cess.auth.entity.ClientDetail;

public interface ClientService {

    ClientDetail loadClientByClientId(String clientId);
}
