package io.cess.sample;

import io.cess.sample.repository.UserRepository;
import io.cess.sample.repository2.UserRepository2;
import io.cess.sample.repository.UserRepository;
import io.cess.sample.repository2.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * Created by lin on 26/10/2016.
 */
//@Transactional
@Component
public class ServiceComponent {

    //primaryDS
    @PersistenceContext(unitName = "primaryPersistenceUnit")
//    @Qualifier("entityManagerPrimary")
    @Autowired
    private EntityManager jpa;

    @PersistenceContext(unitName="secondaryPersistenceUnit")
    @Qualifier("entityManagerSecondary")
    @Autowired
    private EntityManager jpa2;

    @Autowired
    private UserRepository respository;

    @Autowired
    private UserRepository2 respository2;

//    @Transactional
    public User addUser(){
        User user = new User();
        user.setName("name");
        user.setEmail("email");
//        jpa.persist(user);
        user = respository.save(user);
        user.setEmail("email2");
//        jpa.flush();
        return user;
    }

//    @Transactional("transactionManagerSecondary")
    public User addUser2(){
        User user = new User();
        user.setName("name2");
        user.setEmail("email2");
        respository2.save(user);
//        jpa2.persist(user);
        user.setEmail("email22");
//        jpa2.flush();
        return user;
    }
}
