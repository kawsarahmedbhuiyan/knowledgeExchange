CREATE TABLE user
(
    id       int AUTO_INCREMENT,
    username varchar(20)  NOT NULL,
    password varchar(100) NOT NULL,
    name     varchar(45)  NOT NULL,
    status   varchar(10)  NOT NULL,
    created  DATETIME     NOT NULL,
    updated  DATETIME     NOT NULL,
    version  int          NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_username UNIQUE (username)
);

CREATE TABLE role
(
    id      int AUTO_INCREMENT,
    type    varchar(10) NOT NULL,
    status  varchar(10) NOT NULL,
    created DATETIME    NOT NULL,
    updated DATETIME    NOT NULL,
    version int         NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id),
    CONSTRAINT uq_role_type UNIQUE (type)
);

CREATE TABLE user_role
(
    user_id int NOT NULL,
    role_id int NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE forum
(
    id         int AUTO_INCREMENT,
    name       varchar(45) NOT NULL,
    manager_id int         NOT NULL,
    status     varchar(10) NOT NULL,
    created    DATETIME    NOT NULL,
    updated    DATETIME    NOT NULL,
    version    int         NOT NULL,
    CONSTRAINT pk_forum PRIMARY KEY (id),
    CONSTRAINT uq_forum_name UNIQUE (name),
    CONSTRAINT fk_forum_manager_id FOREIGN KEY (manager_id) REFERENCES user (id)
);

CREATE TABLE enrollment
(
    id       int AUTO_INCREMENT,
    forum_id int         NOT NULL,
    user_id  int         NOT NULL,
    status   varchar(10) NOT NULL,
    created  DATETIME    NOT NULL,
    updated  DATETIME    NOT NULL,
    version  int         NOT NULL,
    CONSTRAINT pk_enrollment PRIMARY KEY (id),
    CONSTRAINT uq_enrollment_forum_id_user_id UNIQUE (forum_id, user_id),
    CONSTRAINT fk_enrollment_forum_id FOREIGN KEY (forum_id) REFERENCES forum (id),
    CONSTRAINT fk_enrollment_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE post
(
    id       int AUTO_INCREMENT,
    heading  varchar(300)  NOT NULL,
    body     varchar(3000) NOT NULL,
    user_id  int           NOT NULL,
    forum_id int           NOT NULL,
    status   varchar(10)   NOT NULL,
    created  DATETIME      NOT NULL,
    updated  DATETIME      NOT NULL,
    version  int           NOT NULL,
    CONSTRAINT pk_post PRIMARY KEY (id),
    CONSTRAINT fk_post_user_id FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_post_forum_id FOREIGN KEY (forum_id) REFERENCES forum (id)
);

CREATE TABLE post_user_like
(
    user_id int NOT NULL,
    post_id int NOT NULL,
    CONSTRAINT pk_post_user_like PRIMARY KEY (user_id, post_id),
    CONSTRAINT fk_post_user_like_user_id FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_post_user_like_post_id FOREIGN KEY (post_id) REFERENCES post (id)
);

CREATE TABLE comment
(
    id      int AUTO_INCREMENT,
    body    varchar(2000) NOT NULL,
    user_id int           NOT NULL,
    post_id int           NOT NULL,
    status  varchar(10)   NOT NULL,
    created DATETIME      NOT NULL,
    updated DATETIME      NOT NULL,
    version int           NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_comment_post_id FOREIGN KEY (post_id) REFERENCES post (id)
);