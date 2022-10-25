package net.therap.knowledgeExchange.domain;

import net.therap.knowledgeExchange.common.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static java.util.Objects.hash;
import static net.therap.knowledgeExchange.common.Status.NEW;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Entity
@Table(name = "comment")
public class Comment extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 2, max = 2000)
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public Comment() {
        this.status = NEW;
    }

    public Comment(Post post, User user) {
        this();
        this.post = post;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isNew() {
        return this.id == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Comment)) {
            return false;
        }

        Comment comment = (Comment) o;

        return getId() == comment.getId();
    }

    @Override
    public int hashCode() {
        return hash(getId());
    }
}