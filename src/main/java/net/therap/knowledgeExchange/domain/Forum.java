package net.therap.knowledgeExchange.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@Table(name = "forum")
public class Forum extends Persistent {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToMany(mappedBy = "forums")
    private Set<User> users;

    public Forum() {
        users = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}