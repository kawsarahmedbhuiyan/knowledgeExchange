package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Enrollment;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static net.therap.knowledgeExchange.common.Status.*;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Service
public class EnrollmentService {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public Enrollment findById(int id) {
        return em.find(Enrollment.class, id);
    }

    public List<Enrollment> findByUserAndStatus(User user, Status status) {
        return em.createNamedQuery("Enrollment.findByUserAndStatus", Enrollment.class)
                .setParameter("user", user)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Enrollment> findByForumAndStatus(Forum forum, Status status) {
        return em.createNamedQuery("Enrollment.findByForumAndStatus", Enrollment.class)
                .setParameter("forum", forum)
                .setParameter("status", status)
                .getResultList();
    }

    public Enrollment findByForumAndUser(Forum forum, User user) {
        try {
            return em.createNamedQuery("Enrollment.findByForumAndUser", Enrollment.class)
                    .setParameter("forum", forum)
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public void saveOrUpdate(Enrollment enrollment) {
        if (enrollment.isNew()) {
            em.persist(enrollment);
        } else {
            em.merge(enrollment);
        }
    }

    public void approve(Enrollment enrollment) {
        enrollment.setStatus(APPROVED);

        saveOrUpdate(enrollment);
    }

    public void decline(Enrollment enrollment) {
        enrollment.setStatus(DECLINED);

        saveOrUpdate(enrollment);
    }

    public void delete(Enrollment enrollment) {
        enrollment.setStatus(DELETED);

        saveOrUpdate(enrollment);
    }
}