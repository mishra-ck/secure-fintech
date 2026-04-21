package secure.fintech.service;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import secure.fintech.domain.entity.user.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        this.authorities = buildAuthorities(user) ;
    }
    private Set<GrantedAuthority> buildAuthorities(User user){
        Set<GrantedAuthority> auths = new HashSet<>();
        user.getRoles().forEach( role -> {
                // Add ROLE_ prefix for role checks
                auths.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));

                // Add permissions for authority checks
                role.getPermissions().forEach( perm ->{
                    auths.add(new SimpleGrantedAuthority(perm.getName()));
                });
            }
        );
        return auths;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() { return user.isAccountExpired(); }
    @Override
    public boolean isAccountNonLocked() { return user.isAccountLocked(); }
    @Override
    public boolean isCredentialsNonExpired() { return user.isCredentialExpired(); }
    @Override
    public boolean isEnabled() { return user.isEnabled(); }
    public UUID getUserId() { return user.getId(); }
    public String getEntityId() { return user.getEntityId(); }
    /*TODO*/

}
