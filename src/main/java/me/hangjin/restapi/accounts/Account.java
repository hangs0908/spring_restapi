package me.hangjin.restapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true) //이메일 하나만 저장시키기 위
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;



}
