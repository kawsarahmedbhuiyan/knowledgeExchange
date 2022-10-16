package net.therap.knowledgeExchange.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static net.therap.knowledgeExchange.common.RoleType.ADMIN;
import static net.therap.knowledgeExchange.common.Status.ADDED;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "FROM User u ORDER BY u.name"),
        @NamedQuery(name = "User.findByUsername", query = "FROM User WHERE username = :username"),
        @NamedQuery(name = "User.find", query = "FROM User WHERE username = :username AND password = :password")
})
@Table(name = "user")
public class User extends Persistent {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 2, max = 20)
    private String username;

    @NotNull
    @Size(min = 2, max = 100)
    private String password;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Entry> entries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "likers")
    private Set<Post> likedPosts;

    public User() {
        status = ADDED;
        roles = new HashSet<>();
        entries = new HashSet<>();
        posts = new HashSet<>();
        comments = new HashSet<>();
        likedPosts = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(Set<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public boolean isAdmin() {
        return roles.stream().anyMatch(role -> ADMIN.equals(role.getType()));
    }
}