package com.kawsar.knowledgeExchange.domain;

import com.kawsar.knowledgeExchange.common.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.hash;
import static com.kawsar.knowledgeExchange.common.Status.DELETED;
import static com.kawsar.knowledgeExchange.common.Status.PENDING;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Post.findAllByForumAndStatus", query = "FROM Post p WHERE " +
                "p.forum = :forum AND p.status = :status ORDER BY p.created DESC"),
        @NamedQuery(name = "Post.findAllByForumAndUserAndStatus", query = "FROM Post p WHERE " +
                "p.forum = :forum AND p.user = :user AND p.status = :status ORDER BY p.created DESC")
})
@Table(name = "post")
public class Post extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 10, max = 300)
    private String title;

    @NotNull
    @Size(min = 300, max = 3000)
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @ManyToMany
    @JoinTable(
            name = "post_user_like",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public Post() {
        this.status = PENDING;
        this.likers = new HashSet<>();
        this.comments = new HashSet<>();
    }

    public Post(User user, Forum forum) {
        this();
        this.user = user;
        this.forum = forum;
    }

    public Post(User user, Forum forum, Status status) {
        this();
        this.user = user;
        this.forum = forum;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String heading) {
        this.title = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Set<User> getLikers() {
        return likers;
    }

    public void setLikers(Set<User> likers) {
        this.likers = likers;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTotalLikes() {
        return likers.size();
    }

    public int getTotalComments() {
        return (int) comments.stream().filter(comment -> comment.getStatus() != DELETED).count();
    }

    public boolean isNew() {
        return this.id == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Post)) {
            return false;
        }

        Post post = (Post) o;

        return getId() == post.getId();
    }

    @Override
    public int hashCode() {
        return hash(getId());
    }
}