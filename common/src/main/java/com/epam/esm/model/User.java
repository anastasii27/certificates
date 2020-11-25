package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy="user")
    @NotAudited
    private Set<Order> orders;
}
