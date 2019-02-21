package ir.tiroon.notes.service

import ir.tiroon.fanavard.q2.monolith.model.Role
import ir.tiroon.fanavard.q2.monolith.model.State
import ir.tiroon.fanavard.q2.monolith.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("customUserDetailsService")
class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserServices userServices

    @Transactional(readOnly = true)
    UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userServices.get(phoneNumber)
        if (user == null) {
            throw new UsernameNotFoundException("Username not found")
        }

        def userDetails = new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                user.getState() == State.Active,
                true,
                true,
                true,
                getGrantedAuthorities(user)
        )

        userDetails

    }


    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()

        for (Role r : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getRoleName()))
        }
        authorities
    }

}
