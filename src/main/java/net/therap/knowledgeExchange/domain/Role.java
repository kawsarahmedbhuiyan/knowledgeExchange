package net.therap.knowledgeExchange.domain;

import net.therap.knowledgeExchange.common.RoleType;
import net.therap.knowledgeExchange.common.Status;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public Role() {
        this.users = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isNew() {
        return getId() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Role)) {
            return false;
        }

        return id != 0 && id == ((Role) o).getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}