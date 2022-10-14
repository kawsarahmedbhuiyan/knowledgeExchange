package net.therap.knowledgeExchange.dao;

import net.therap.knowledgeExchange.common.RoleType;
import net.therap.knowledgeExchange.domain.Role;
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
public class RoleDao {

    @PersistenceContext(unitName = "knowledge-exchange-persistence-unit")
    private EntityManager em;

    public Role findById(int id) {
        return em.find(Role.class, id);
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
    public Role saveOrUpdate(Role role) {
        if (role.isNew()) {
            em.persist(role);
        } else {
            role = em.merge(role);
        }

        return role;
    }
}