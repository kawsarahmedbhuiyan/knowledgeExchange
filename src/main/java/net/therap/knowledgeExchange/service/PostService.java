package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

import static net.therap.knowledgeExchange.common.Status.*;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Service
public class PostService {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public Post findById(int id) {
        Post post = em.find(Post.class, id);

        if (Objects.isNull(post)) {
            throw new NotFoundException("Post Not Found for ID=" + id);
        }

        return post;
    }

    public List<Post> findAllByForumAndStatus(Forum forum, Status status) {
        return em.createNamedQuery("Post.findAllByForumAndStatus", Post.class)
                .setParameter("forum", forum)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Post> findAllByForumAndUserAndStatus(Forum forum, User user, Status status) {
        return em.createNamedQuery("Post.findAllByForumAndUserAndStatus", Post.class)
                .setParameter("forum", forum)
                .setParameter("user", user)
                .setParameter("status", status)
                .getResultList();
    }

    @Transactional
    public void saveOrUpdate(Post post) {
        if (post.isNew()) {
            em.persist(post);
        } else {
            em.merge(post);
        }
    }

    @Transactional
    public void approve(Post post) {
        post.setStatus(APPROVED);

        em.merge(post);
    }

    @Transactional
    public void decline(Post post) {
        post.setStatus(DECLINED);

        em.merge(post);
    }

    @Transactional
    public void delete(Post post) {
        post.setStatus(DELETED);

        em.merge(post);
    }

    @Transactional
    public void addOrRemoveLike(Post post, User user) {
        if (isLikedByUser(post, user)) {
            post.getLikers().remove(user);
        } else {
            post.getLikers().add(user);
        }

        em.merge(post);
    }

    public boolean isLikedByUser(Post post, User user) {
        return post.getLikers().contains(user);
    }
}