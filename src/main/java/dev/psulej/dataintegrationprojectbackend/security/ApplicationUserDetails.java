//package dev.psulej.dataintegrationprojectbackend.security;
//
//import lombok.Builder;
//import lombok.Getter;
//
//@Builder
//@Getter
//public class ApplicationUserDetails implements UserDetails {
//
//    private UUID id;
//    private String login;
//    private String password;
//    private String name;
//    private String email;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    @Override
//    public String getUsername() {
//        return login;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
