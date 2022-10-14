package net.therap.knowledgeExchange.domain;

import net.therap.knowledgeExchange.common.RoleType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "FROM Role"),
        @NamedQuery(name = "Role.findByType", query = "FROM Role WHERE type = :type")
})
@Table(name = "role")
public class Role extends Persistent {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
        users = new HashSet<>();
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}