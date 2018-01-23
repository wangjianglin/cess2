package io.cess.sample.repository2;

import io.cess.sample.User;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User.class,idClass = long.class)
public interface UserRepository2
{
    User save(User user);
}
