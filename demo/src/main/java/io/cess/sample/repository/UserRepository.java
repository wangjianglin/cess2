package io.cess.sample.repository;

import io.cess.sample.User;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User.class,idClass = long.class)
public interface UserRepository {
    User save(User user);
}
