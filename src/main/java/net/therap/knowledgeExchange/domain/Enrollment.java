package net.therap.knowledgeExchange.domain;

import javax.persistence.*;

import static net.therap.knowledgeExchange.common.Status.PENDING;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Enrollment.findByForumAndUser", query = "FROM Enrollment WHERE " +
                "forum = :forum AND user = :user")
})
@Table(name = "enrollment")
public class Enrollment extends Persistent {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Enrollment() {
        status = PENDING;
    }

    public Enrollment(Forum forum, User user) {
        status = PENDING;
        this.forum = forum;
        this.user = user;
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
}