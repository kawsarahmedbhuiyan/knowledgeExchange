package net.therap.knowledgeExchange.dao;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Repository
public class UserDao {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public User findById(int id) {
        return em.find(User.class, id);
    }

    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> findAll() {
        return em.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public boolean isDuplicateByUsername(User user) {
        return user.isNew() &&
                em.createNamedQuery("User.findByUsername", User.class)
                        .setParameter("username", user.getUsername())
                        .getResultList().size() == 1;
    }

    public boolean isValidCredential(Credential credential) {
        return em.createNamedQuery("User.find", User.class)
                .setParameter("username", credential.getUsername())
                .setParameter("password", credential.getPassword())
                .getResultList().size() == 1;
    }

    @Transactional
    public User saveOrUpdate(User user) {
        if (user.isNew()) {
            em.persist(user);
        } else {
            user = em.merge(user);
        }

        return user;
    }

    @Transactional
    public void delete(User user) {
        em.remove(user);
    }
}