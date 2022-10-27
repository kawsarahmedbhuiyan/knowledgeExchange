package com.kawsar.knowledgeExchange.service;

import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.domain.Enrollment;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.Objects.isNull;
import static com.kawsar.knowledgeExchange.common.Status.*;
import static com.kawsar.knowledgeExchange.utils.Constant.PERSISTENCE_UNIT;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Service
public class EnrollmentService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

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

    @Transactional
    public void enroll(Forum forum, User user) {
        Enrollment enrollment = findByForumAndUser(forum, user);

        if (isNull(enrollment)) {
            enrollment = new Enrollment(forum, user);
            em.persist(enrollment);
        } else {
            enrollment.setStatus(PENDING);
            em.merge(enrollment);
        }
    }

    @Transactional
    public void approve(Enrollment enrollment) {
        enrollment.setStatus(APPROVED);

        em.merge(enrollment);
    }

    @Transactional
    public void decline(Enrollment enrollment) {
        enrollment.setStatus(DECLINED);

        em.merge(enrollment);
    }

    @Transactional
    public void delete(Enrollment enrollment) {
        enrollment.setStatus(DELETED);

        em.merge(enrollment);
    }
}