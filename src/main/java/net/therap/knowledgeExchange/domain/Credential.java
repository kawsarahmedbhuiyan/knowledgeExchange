package net.therap.knowledgeExchange.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
public class Credential {

    @NotNull
    @Size(min = 2, max = 20)
    private String username;

    @NotNull
    @Size(min = 2, max = 100)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}