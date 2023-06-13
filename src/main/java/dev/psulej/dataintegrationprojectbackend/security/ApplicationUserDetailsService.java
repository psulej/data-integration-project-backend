//package dev.psulej.dataintegrationprojectbackend.security;
//
//@RequiredArgsConstructor
//@Service
//public class ApplicationUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        return userRepository.findByLoginIgnoreCase(username)
//                .map(this::mapToUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
//    }
//
//    private ApplicationUserDetails mapToUserDetails(User user) {
//        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.role().name()));
//        return ApplicationUserDetails.builder()
//                .id(user.id())
//                .email(user.email())
//                .login(user.login())
//                .name(user.name())
//                .password(user.password())
//                .authorities(authorities)
//                .build();
//    }
//}
