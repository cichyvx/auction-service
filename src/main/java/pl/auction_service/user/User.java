package pl.auction_service.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.auction_service.auction.Auction;
import pl.auction_service.wallet.Wallet;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
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

    @JsonManagedReference
    @OneToMany(targetEntity = Auction.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Auction> auctions;

    @OneToOne(targetEntity = Wallet.class, mappedBy = "owner")
    private Wallet userWallet;

    public Long getId() {
        return id;
    }

    public List<Auction> getAuctions() {
        return auctions.stream().toList();
    }

    public Wallet getWallet() {
        return userWallet;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = Collections.emptyList();
        switch (roles){
            case 1 -> list.add(new Role("USER"));
            case 2 -> list.add(new Role("ADMIN"));
        }
        return list;
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

    private final class Role implements GrantedAuthority{

        private final String name;

        private Role(String name){
            this.name = name;
        }

        @Override
        public String getAuthority() {
            return name;
        }
    }

}
