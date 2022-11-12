package ar.com.auth.model;

import ar.com.auth.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private Roles roleName;
    @Column(name = "role_description")
    private String roleDescription;
    @ManyToMany(mappedBy = "userRoles")
    List<User> roleUsers;
}
