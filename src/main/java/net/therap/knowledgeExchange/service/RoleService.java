package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.RoleType;
import net.therap.knowledgeExchange.domain.Role;
import net.therap.knowledgeExchange.exception.NotFoundException;
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

    @PersistenceContext(unitName = PERSISTENCE_UNIT)
    private EntityManager em;

    public Role findById(int id) {
        Role role = em.find(Role.class, id);

        if (isNull(role)) {
            throw new NotFoundException("Role Not Found for ID=" + id);
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