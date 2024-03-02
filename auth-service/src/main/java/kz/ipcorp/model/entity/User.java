package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import kz.ipcorp.model.enumuration.Role;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserStatus> userStatuses = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @ElementCollection
    @CollectionTable(name = "favorite_products", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "product_id")
    private List<UUID> favoriteProducts = new ArrayList<>();
    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void removeFavoriteProduct(UUID productId) {
        favoriteProducts.remove(productId);
    }
    public boolean isFavoriteProduct(UUID productId) {
        return favoriteProducts.contains(productId);
    }
}
