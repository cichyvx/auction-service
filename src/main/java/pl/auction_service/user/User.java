package pl.auction_service.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.auction_service.auction.Auction;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@Table(name = "users")
@Setter
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String username;
    private String password;
    private int roles;

    //@OneToMany(targetEntity = Auction.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    @OneToMany(targetEntity = Auction.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Auction> auctions;

    public Long getId() {
        return id;
    }

    public List<Auction> getAuctions() {
        return auctions.stream().toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
