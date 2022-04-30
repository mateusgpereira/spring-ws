package com.springws.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class PasswordResetTokenEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String token;

    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

}
