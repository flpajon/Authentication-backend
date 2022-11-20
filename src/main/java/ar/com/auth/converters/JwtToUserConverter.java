package ar.com.auth.converters;

import ar.com.auth.model.User;
import ar.com.auth.repositories.UserRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Transactional
@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = userRepository.findUserByUserNameAndIsEnabledTrue(jwt.getSubject()).orElseThrow(() -> new FetchNotFoundException(
                "User",
                MessageFormat.format("username {0} not found", jwt.getSubject())
        ));
        return new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());
    }
}
