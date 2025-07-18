package com.devsuperior.demo.services;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projection.UserDetailsProjection;
import com.devsuperior.demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);

        if(result.size() == 0){
            throw new UsernameNotFoundException("Username not found");
        }

        User user = new User();

        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());

        for(UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(),projection.getAuthority()));
        }
        return user;
    }
}
