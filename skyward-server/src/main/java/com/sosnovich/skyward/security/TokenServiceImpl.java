package com.sosnovich.skyward.security;

import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.data.repository.UserRepository;
import com.sosnovich.skyward.security.api.TokenService;
import com.sosnovich.skyward.security.api.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public TokenServiceImpl(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public String createToken(Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String email = userDetails.getUsername();
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email); // Assuming your UserRepository has this method
        final String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (userEntity.isPresent()) {
            return jwtTokenProvider.createToken(email, userEntity.get().getId(), role);
        }
        throw new UsernameNotFoundException("User not found: " + email);
    }
}
