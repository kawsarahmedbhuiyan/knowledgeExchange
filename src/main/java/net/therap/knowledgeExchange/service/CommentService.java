package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.domain.Comment;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.util.Objects.isNull;
import static net.therap.knowledgeExchange.common.Status.DELETED;
import static net.therap.knowledgeExchange.utils.Constant.PERSISTENCE_UNIT;

/**
 * @author kawsar.bhuiyan
 * @since 10/19/22
 */
@Service
public class CommentService {

    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

    public Comment findById(int id) {
        Comment comment = em.find(Comment.class, id);

        if (isNull(comment)) {
            String exceptionMessage = "Comment Not Found for ID=" + id;

            logger.error(exceptionMessage);

            throw new NotFoundException(exceptionMessage);
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