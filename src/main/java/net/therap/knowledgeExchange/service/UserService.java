package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.exception.NotFoundException;
import net.therap.knowledgeExchange.utils.HashGenerationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Service
public class UserService {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public User findById(int id) {
        User user = em.find(User.class, id);

        if (Objects.isNull(user)) {
            throw new NotFoundException("User Not Found for ID=" + id);
        }

        return user;
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
    public void saveOrUpdate(User user) {
        if (user.isNew()) {
            setHashedPassword(user);
            em.persist(user);
        } else {
            user = em.merge(user);
        }
    }

    private void setHashedPassword(User user) {
        user.setPassword(HashGenerationUtil.getHashedValue(user.getPassword()));
    }
}