package net.therap.knowledgeExchange.dao;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Repository
public class ForumDao {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public Forum findById(int id) {
        return em.find(Forum.class, id);
    }

    public List<Forum> findAll() {
        return em.createNamedQuery("Forum.findAll", Forum.class).getResultList();
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
}