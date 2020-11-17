@Component
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        UserEntity userEntity = userService.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        Collection<? extends GrantedAuthority> authorities = userAuthorityService.getGrantedAuthorities(userEntity.isAdmin());

        return authorities;
    }
}
