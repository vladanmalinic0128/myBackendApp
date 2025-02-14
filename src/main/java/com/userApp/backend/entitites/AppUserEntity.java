package com.userApp.backend.entitites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "app_user", schema = "ipdatabase", catalog = "")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserEntity implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "firstname")
    private String firstname;

    @Basic
    @Column(name = "lastname")
    private String lastname;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "mail")
    private String mail;

    @Basic
    @Column(name = "locked")
    private Boolean locked;

    @Basic
    @Column(name = "enabled")
    private Boolean enabled;

    @Basic
    @Column(name = "isActive")
    private Boolean isActive;

    @Basic
    @Column(name = "avatar")
    private String avatar;

    @OneToOne(mappedBy = "appUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private AdministratorEntity administrator;

    @OneToOne(mappedBy = "appUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private AdvisorEntity advisor;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<MessageEntity> messages;

    @OneToOne(mappedBy = "appUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private UserEntity user;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = null;
        if(advisor != null && advisor.getId() != null)
            authority = new SimpleGrantedAuthority("advisor");
        else if(administrator != null && administrator.getId() != null)
            authority = new SimpleGrantedAuthority("administrator");
        else if(user != null && user.getId() != null)
            authority = new SimpleGrantedAuthority("user");

        if(authority != null)
            return Collections.singletonList(authority);
        else
            return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public AppUserEntity(String firstname, String lastname, String username, String password, String mail, Boolean locked, Boolean enabled, Boolean isActive, String avatar, AdministratorEntity administrator, AdvisorEntity advisor, Collection<MessageEntity> messages, UserEntity user) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.locked = locked;
        this.enabled = enabled;
        this.isActive = isActive;
        this.avatar = avatar;
        this.administrator = administrator;
        this.advisor = advisor;
        this.messages = messages;
        this.user = user;
    }
}
