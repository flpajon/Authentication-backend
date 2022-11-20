package ar.com.auth.repositories;

import ar.com.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserNameAndIsEnabledTrue(String userName);
    Boolean existsUserByUserName(String userName);
    User findUserByUserName(String userName);
}
