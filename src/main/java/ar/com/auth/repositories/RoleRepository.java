package ar.com.auth.repositories;

import ar.com.auth.enums.Roles;
import ar.com.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByRoleName(Roles RoleName);
}
