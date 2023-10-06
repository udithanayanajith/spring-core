package com.orelit.springcore.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Department
 * Author: udithan
 * Date: 21-Sep-23
 */

/**
 * Create department table
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dep_name")
    private String dep_name;

    @Column(name = "dep_contact_no")
    private String dep_contact_no;

    @Column(name = "dep_email")
    private String dep_email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private OrelUser orelUser;

}
