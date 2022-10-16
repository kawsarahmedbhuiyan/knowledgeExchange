package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.dao.ForumDao;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Service
public class ForumService {

    @Autowired
    private ForumDao forumDao;

    public Forum findById(int id) {
        Forum forum = forumDao.findById(id);

        if (Objects.isNull(forum)) {
            throw new NotFoundException("Forum Not Found for ID=" + id);
        }

        return forum;
    }

    public List<Forum> findAll() {
        return forumDao.findAll();
    }

    public List<Forum> findAllByManagerAndStatus(User manager, Status status) {
        return forumDao.findAllByManagerAndStatus(manager, status);
    }

    public void saveOrUpdate(Forum forum) {
        forumDao.saveOrUpdate(forum);
    }
}