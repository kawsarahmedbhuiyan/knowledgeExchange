package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
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
 * @since 10/15/22
 */
@Service
public class ForumService {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public Forum findById(int id) {
        Forum forum = em.find(Forum.class, id);

        if (Objects.isNull(forum)) {
            throw new NotFoundException("Forum Not Found for ID=" + id);
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

    public void approve(Forum forum) {
        forum.setStatus(APPROVED);

        saveOrUpdate(forum);
    }

    public void decline(Forum forum) {
        forum.setStatus(DECLINED);

        saveOrUpdate(forum);
    }

    public void delete(Forum forum) {
        forum.setStatus(DELETED);

        saveOrUpdate(forum);
    }
}