//package dev.psulej.dataintegrationprojectbackend.security;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final UserDetailsService userDetailsService;
//    private final TokenProvider tokenProvider;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain) throws ServletException, IOException {
//        try {
//            getJwtFromRequest(request)
//                    .flatMap(tokenProvider::validateTokenAndGetJws)
//                    .ifPresent(jws -> {
//                        String username = jws.getBody().getSubject();
//                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContext context = SecurityContextHolder.getContext();
//                        context.setAuthentication(authentication);
//                    });
//        } catch (Exception e) {
//            log.error("Cannot set user authentication", e);
//        }
//        chain.doFilter(request, response);
//    }
//
//    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
//        String tokenHeader = request.getHeader(TOKEN_HEADER);
//        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
//            return Optional.of(tokenHeader.replace(TOKEN_PREFIX, ""));
//        }
//        return Optional.empty();
//    }
//
//    public static final String TOKEN_HEADER = "Authorization";
//    public static final String TOKEN_PREFIX = "Bearer ";
//}