package com.orelit.springcore.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@Table(name = "orel_user")
public class OrelUser extends BaseEntity {

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy ="orelUser")
    private Department department;;


    public OrelUser(Long id) {
        super(id);
    }
}
