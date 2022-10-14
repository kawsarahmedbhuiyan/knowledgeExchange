package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.dao.UserDao;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.exception.NotFoundException;
import net.therap.knowledgeExchange.utils.HashGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User findById(int id) {
        User user = userDao.findById(id);

        if (Objects.isNull(user)) {
            throw new NotFoundException("User Not Found for ID=" + id);
        }

        return user;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public boolean isDuplicateByUsername(User user) {
        return userDao.isDuplicateByUsername(user);
    }

    public void saveOrUpdate(User user) {
        if (user.isNew()) {
            setHashedPassword(user);
        }

        userDao.saveOrUpdate(user);
    }

    public void delete(User user) {
        userDao.delete(user);
    }

    private void setHashedPassword(User user) {
        user.setPassword(HashGenerationUtil.getHashedValue(user.getPassword()));
    }
}