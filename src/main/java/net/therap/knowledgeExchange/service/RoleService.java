package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.RoleType;
import net.therap.knowledgeExchange.domain.Role;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.Objects.isNull;
import static net.therap.knowledgeExchange.utils.Constant.PERSISTENCE_UNIT;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Service
public class RoleService {

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

    public Role findById(int id) {
        Role role = em.find(Role.class, id);

        if (isNull(role)) {
            String exceptionMessage = "Role Not Found for ID=" + id;

            logger.error(exceptionMessage);

            throw new NotFoundException(exceptionMessage);
        }

        return role;
    }

    public Role findByType(RoleType type) {
        try {
            return em.createNamedQuery("Role.findByType", Role.class)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Role> findAll() {
        return em.createNamedQuery("Role.findAll", Role.class).getResultList();
    }

    @Transactional
    public void saveOrUpdate(Role role) {
        if (role.isNew()) {
            em.persist(role);
        } else {
            role = em.merge(role);
        }
    }
}