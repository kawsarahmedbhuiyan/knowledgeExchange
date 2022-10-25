package net.therap.knowledgeExchange.domain;

import net.therap.knowledgeExchange.common.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.hash;
import static net.therap.knowledgeExchange.common.Status.PENDING;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Forum.findAll", query = "FROM Forum f ORDER BY f.name"),
        @NamedQuery(name = "Forum.findAllByStatus", query = "FROM Forum  f WHERE f.status = :status ORDER BY f.name"),
        @NamedQuery(name = "Forum.findAllByManagerAndStatus", query = "FROM Forum  f WHERE " +
                "f.manager = :manager AND f.status = :status ORDER BY f.name")
})
@Table(name = "forum")
public class Forum extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @NotNull
    @Size(min = 300, max = 3000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private Set<Post> posts;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public Forum() {
        this.status = PENDING;
        this.enrollments = new HashSet<>();
        this.posts = new HashSet<>();
    }

    public Forum(User manager) {
        this();
        this.manager = manager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> entries) {
        this.enrollments = entries;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
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

    public boolean isManagedByUser(User user) {
        return manager.equals(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Forum)) {
            return false;
        }

        Forum forum = (Forum) o;

        return getId() == forum.getId();
    }

    @Override
    public int hashCode() {
        return hash(getId());
    }
}