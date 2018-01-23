package io.cess.auth.repository;

import io.cess.auth.entity.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface ClientDetailRepository extends Repository<ClientDetail, String> {


    ClientDetail findByClientId(String id);
}
