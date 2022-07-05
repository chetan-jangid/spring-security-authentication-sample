package com.security.authentication.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_account_role")
@NamedQueries(
        @NamedQuery(name = "UserAccountRole.getRolesByUserAccountId", query = """
        select r.roleKey as roleKey
        from UserAccountRole uar
        inner join AccountRole r on uar.roleKey = r.roleKey
        where uar.userAccount.id = :userAccountId
        order by r.roleKey
        """)
)
public class UserAccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "role_key", nullable = false)
    private String roleKey;
}
