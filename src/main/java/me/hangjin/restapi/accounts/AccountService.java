package me.hangjin.restapi.accounts;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    public Account saveAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new AccountAdapter(account);
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_"+ r.name()))
                 .collect(Collectors.toSet());
    }
}
