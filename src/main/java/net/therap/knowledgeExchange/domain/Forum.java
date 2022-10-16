package net.therap.knowledgeExchange.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static net.therap.knowledgeExchange.common.Status.PENDING;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Forum.findAll", query = "FROM Forum f ORDER BY f.name"),
        @NamedQuery(name = "Forum.findAllByManagerAndStatus", query = "FROM Forum  f WHERE " +
                "f.manager = :manager AND f.status = :status ORDER BY f.name")
})
@Table(name = "forum")
public class Forum extends Persistent {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private Set<Entry> entries;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private Set<Post> posts;

    public Forum() {
        status = PENDING;
        entries = new HashSet<>();
        posts = new HashSet<>();
    }

    public Forum(User manager) {
        this.manager = manager;
        status = PENDING;
        entries = new HashSet<>();
        posts = new HashSet<>();
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

    public Set<Entry> getEntries() {
        return entries;
    }

    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}