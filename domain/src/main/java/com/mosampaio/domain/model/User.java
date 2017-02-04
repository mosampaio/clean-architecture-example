package com.mosampaio.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static java.util.UUID.randomUUID;
import static org.mindrot.jbcrypt.BCrypt.checkpw;
import static org.mindrot.jbcrypt.BCrypt.gensalt;
import static org.mindrot.jbcrypt.BCrypt.hashpw;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "builderForTest")
public class User {

    @Id
    private String id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    public User(final String username, final String password) {
        this.id = randomUUID().toString();
        this.username = username;
        this.passwordHash = generatePasswordHash(password);
    }

    // Generates bcrypted password hash
    private static String generatePasswordHash(String password) {
        return hashpw(password, gensalt());
    }

    public boolean authenticate(final String password) {
        return checkpw(password, this.passwordHash);
    }

}
