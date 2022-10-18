package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.Status;
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

    public List<Post> findAll() {
        return em.createNamedQuery("Post.findAll", Post.class).getResultList();
    }

    public List<Post> findAllByStatus(Status status) {
        return em.createNamedQuery("Post.findAllByStatus", Post.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Post> findAllByManagerAndStatus(User manager, Status status) {
        return em.createNamedQuery("Post.findAllByManagerAndStatus", Post.class)
                .setParameter("manager", manager)
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
}