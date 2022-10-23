package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.domain.*;
import net.therap.knowledgeExchange.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;


/**
 * @author kawsar.bhuiyan
 * @since 10/1/22
 */
@Service
public class AccessCheckerService {

    public void checkAdminAccess(HttpServletRequest request) {
        User sessionUser = getSessionUser(request);

        if (!sessionUser.isAdmin()) {
            throw new UnauthorizedException("You do not have admin access");
        }
    }

    public void checkForumDeleteAccess(HttpServletRequest request, Forum forum) {
        User sessionUser = getSessionUser(request);

        if (!(sessionUser.isAdmin() || forum.isManagedByUser(sessionUser))) {
            throw new UnauthorizedException("You are not authorized to delete this forum");
        }
    }

    public void checkPostViewAccess(HttpServletRequest request, Post post) {
        User sessionUser = getSessionUser(request);

        if (post.getForum().getEnrollments().stream()
                .noneMatch(enrollment -> sessionUser.equals(enrollment.getUser()))) {
            throw new UnauthorizedException("You must join this forum to read the full post");
        }
    }

    public void checkPostSaveAccess(HttpServletRequest request, Forum forum) {
        User sessionUser = getSessionUser(request);

        if (forum.getEnrollments().stream().noneMatch(enrollment -> sessionUser.equals(enrollment.getUser()))) {
            throw new UnauthorizedException("You must join the forum to add a new post");
        }
    }

    public void checkPostUpdateAccess(HttpServletRequest request, Post post) {
        User sessionUser = getSessionUser(request);

        if (!sessionUser.equals(post.getUser())) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }
    }

    public void checkPostDeleteAccess(HttpServletRequest request, Post post) {
        User sessionUser = getSessionUser(request);

        if (!(sessionUser.equals(post.getForum().getManager()) || sessionUser.equals(post.getUser()))) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }
    }

    public void checkManagerAccess(HttpServletRequest request, Forum forum) {
        User sessionUser = getSessionUser(request);

        if (!sessionUser.equals(forum.getManager())) {
            throw new UnauthorizedException("You do not have manager access for this forum");
        }
    }

    public void checkPostLikeAccess(HttpServletRequest request, Post post) {
        User sessionUser = getSessionUser(request);

        if (post.getForum().getEnrollments().stream()
                .noneMatch(enrollment -> sessionUser.equals(enrollment.getUser()))) {
            throw new UnauthorizedException("You must join the forum to like this post");
        }
    }

    public void checkCommentSaveAccess(HttpServletRequest request, Comment comment) {
        User sessionUser = getSessionUser(request);

        if (comment.getPost().getForum().getEnrollments()
                .stream().noneMatch(enrollment -> sessionUser.equals(enrollment.getUser()))) {
            throw new UnauthorizedException("You must join the forum to comment on this post");
        }
    }

    public void checkCommentUpdateAccess(HttpServletRequest request, Comment comment) {
        User sessionUser = getSessionUser(request);

        if (!sessionUser.equals(comment.getUser())) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }
    }

    public void checkCommentDeleteAccess(HttpServletRequest request, Comment comment) {
        User sessionUser = getSessionUser(request);

        if (!(sessionUser.equals(comment.getPost().getForum().getManager())
                || sessionUser.equals(comment.getUser()))) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
    }

    public void checkUserUpdateAccess(HttpServletRequest request, User user) {
        User sessionUser = getSessionUser(request);

        if (!(sessionUser.isAdmin() || sessionUser.equals(user))) {
            throw new UnauthorizedException("You are not authorized to update this user");
        }
    }

    public void checkEnrollmentDeleteAccess(HttpServletRequest request, Enrollment enrollment) {
        User sessionUser = getSessionUser(request);

        if(!(sessionUser.equals(enrollment.getForum().getManager()) || sessionUser.equals(enrollment.getUser()))) {
            throw new UnauthorizedException("You are not authorized to delete this enrollment");
        }
    }
}