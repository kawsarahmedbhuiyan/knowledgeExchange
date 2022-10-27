package com.kawsar.knowledgeExchange.service;

import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.Post;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.Objects.isNull;
import static com.kawsar.knowledgeExchange.common.Status.*;
import static com.kawsar.knowledgeExchange.utils.Constant.PERSISTENCE_UNIT;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Service
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

    public Post findById(int id) {
        Post post = em.find(Post.class, id);

        if (isNull(post)) {
            String exceptionMessage = "Post Not Found for ID=" + id;

            logger.error(exceptionMessage);

            throw new NotFoundException(exceptionMessage);
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