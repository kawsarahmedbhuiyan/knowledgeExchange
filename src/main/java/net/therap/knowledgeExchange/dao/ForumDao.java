package net.therap.knowledgeExchange.dao;

import net.therap.knowledgeExchange.domain.Forum;
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

    @Transactional
    public void saveOrUpdate(Forum forum) {
        if (forum.isNew()) {
            em.persist(forum);
        } else {
            em.merge(forum);
        }
    }
}