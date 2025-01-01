package com.utkarsh.security_service.service;

import com.utkarsh.security_service.entity.UserCredential;
import com.utkarsh.security_service.model.CustomUserDetails;
import com.utkarsh.security_service.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credentialOptional = userCredentialRepository.findByName(username);
        return credentialOptional.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user with username: " + username + " not found"));
    }
}
