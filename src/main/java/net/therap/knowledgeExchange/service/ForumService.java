package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.Objects.isNull;
import static net.therap.knowledgeExchange.common.Status.*;
import static net.therap.knowledgeExchange.utils.Constant.PERSISTENCE_UNIT;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Service
public class ForumService {

    private final Logger logger = LoggerFactory.getLogger(ForumService.class);

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

    public Forum findById(int id) {
        Forum forum = em.find(Forum.class, id);

        if (isNull(forum)) {
            String exceptionMessage = "Forum Not Found for ID=" + id;

            logger.error(exceptionMessage);

            throw new NotFoundException(exceptionMessage);
        }

        return forum;
    }

    public List<Forum> findAll() {
        return em.createNamedQuery("Forum.findAll", Forum.class).getResultList();
    }

    public List<Forum> findAllByStatus(Status status) {
        return em.createNamedQuery("Forum.findAllByStatus", Forum.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Forum> findAllByManagerAndStatus(User manager, Status status) {
        return em.createNamedQuery("Forum.findAllByManagerAndStatus", Forum.class)
                .setParameter("manager", manager)
                .setParameter("status", status)
                .getResultList();
    }

    @Transactional
    public void saveOrUpdate(Forum forum) {
        if (forum.isNew()) {
            em.persist(forum);
        } else {
            em.merge(forum);
        }
    }

    @Transactional
    public void approve(Forum forum) {
        forum.setStatus(APPROVED);

        em.merge(forum);
    }

    @Transactional
    public void decline(Forum forum) {
        forum.setStatus(DECLINED);

        em.merge(forum);
    }

    @Transactional
    public void delete(Forum forum) {
        forum.setStatus(DELETED);

        em.merge(forum);
    }
}