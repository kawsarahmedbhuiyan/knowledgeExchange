package com.kawsar.knowledgeExchange.service;

import com.kawsar.knowledgeExchange.domain.Credential;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.NotFoundException;
import com.kawsar.knowledgeExchange.utils.HashGenerationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.Objects.isNull;
import static com.kawsar.knowledgeExchange.utils.Constant.PERSISTENCE_UNIT;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

    public User findById(int id) {
        User user = em.find(User.class, id);

        if (isNull(user)) {
            String exceptionMessage = "User Not Found for ID=" + id;

            logger.error(exceptionMessage);

            throw new NotFoundException(exceptionMessage);
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