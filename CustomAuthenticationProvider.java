@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    @Qualifier("ldapAuthProvider")
    private LdapAuthenticationProvider ldapAuthProvider;

    @Autowired
    @Qualifier("daoAuthProvider")
    private DaoAuthenticationProvider daoAuthProvider;

    @Autowired
    private UserService userService;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        UserEntity userEntity = userService.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        AuthTypeEnum authType = userEntity.getAuthType();
        switch (authType) {
        case SIMPLE:
            return daoAuthProvider.authenticate(authentication);
        case LDAP:
            return ldapAuthProvider.authenticate(authentication);
        default:
            throw new IllegalArgumentException(authType + " is not yet implemented!");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
