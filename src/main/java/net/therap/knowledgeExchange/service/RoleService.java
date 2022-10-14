package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.RoleType;
import net.therap.knowledgeExchange.dao.RoleDao;
import net.therap.knowledgeExchange.domain.Role;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role findById(int id) {
        Role role = roleDao.findById(id);

        if (Objects.isNull(role)) {
            throw new NotFoundException("Role Not Found for ID=" + id);
        }

        return role;
    }

    public Role findByType(RoleType type) {
        return roleDao.findByType(type);
    }

    public List<Role> findAll() {
        return roleDao.findAll();
    }

    public void saveOrUpdate(Role role) {
        roleDao.saveOrUpdate(role);
    }
}