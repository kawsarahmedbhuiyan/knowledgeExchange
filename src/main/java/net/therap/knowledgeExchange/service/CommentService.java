package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.domain.Comment;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

import static net.therap.knowledgeExchange.common.Status.DELETED;

/**
 * @author kawsar.bhuiyan
 * @since 10/19/22
 */
@Service
public class CommentService {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public Comment findById(int id) {
        Comment comment = em.find(Comment.class, id);

        if (Objects.isNull(comment)) {
            throw new NotFoundException("Comment Not Found for ID=" + id);
        }

        return comment;
    }
    
    @Transactional
    public void saveOrUpdate(Comment comment) {
        if (comment.isNew()) {
            em.persist(comment);
        } else {
            em.merge(comment);
        }
    }
    
    @Transactional
    public void delete(Comment comment) {
        comment.setStatus(DELETED);

        em.merge(comment);
    }
}