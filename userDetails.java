@Component
public class JdbcUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Override
    public JwtUser loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByUsername(userName);
        if(userEntity == null){
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userName));
        }
        Collection<? extends GrantedAuthority> authorities = userAuthorityService.getGrantedAuthorities(userEntity.isAdmin());
        
        JwtUser jwtUser = new JwtUser(userEntity.getId(), userEntity.getUsername(), userEntity.getFirstname(), userEntity.getLastname(), userEntity.getPassword(), userEntity.getEmail(), userEntity.getAuthType().name(), userEntity.isAdmin(), authorities, userEntity.isEnabled(),
                userEntity.getLastPasswordResetDate());
        return jwtUser;
    }

}
