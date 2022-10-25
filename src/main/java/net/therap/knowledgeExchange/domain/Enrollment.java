package net.therap.knowledgeExchange.domain;

import net.therap.knowledgeExchange.common.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static java.util.Objects.hash;
import static net.therap.knowledgeExchange.common.Status.PENDING;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Enrollment.findByUserAndStatus", query = "FROM Enrollment WHERE " +
                "user = :user AND status = :status"),
        @NamedQuery(name = "Enrollment.findByForumAndStatus", query = "FROM Enrollment WHERE " +
                "forum = :forum AND status = :status"),
        @NamedQuery(name = "Enrollment.findByForumAndUser", query = "FROM Enrollment WHERE " +
                "forum = :forum AND user = :user")
})
@Table(name = "enrollment")
public class Enrollment extends Persistent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public Enrollment() {
        this.status = PENDING;
    }

    public Enrollment(Forum forum, User user) {
        this();
        this.forum = forum;
        this.user = user;
    }

    public Enrollment(Forum forum, User user, Status status) {
        this.forum = forum;
        this.user = user;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

        if (!(o instanceof Enrollment)) {
            return false;
        }

        Enrollment enrollment = (Enrollment) o;

        return getId() == enrollment.getId();
    }

    @Override
    public int hashCode() {
        return hash(getId());
    }
}