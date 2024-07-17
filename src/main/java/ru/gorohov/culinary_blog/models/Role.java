package ru.gorohov.culinary_blog.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ru.gorohov.culinary_blog.models.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(EnumSet.noneOf(Permission.class)),
    ADMIN(EnumSet.of(
            ADMIN_READ,
            ADMIN_BAN,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            ADMIN_CREATE
    ));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Stream.concat(permissions
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getAction())),
                Stream.of(new SimpleGrantedAuthority("ROLE_" + this.name()))
        ).toList();
    }
}
