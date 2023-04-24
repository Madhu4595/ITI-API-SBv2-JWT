package com.app.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repo.RoleRepository;
import com.app.repo.UserRepository;


@Service
@Transactional
public class UserService implements UserDetailsService {
@Autowired
private UserRepository userRepository;

@Autowired
private RoleRepository roleRepository;

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	// TODO Auto-generated method stub
	User user=userRepository.findByUsername(username);
	UserDetailsPrincipal userDetailsPrincipal=new UserDetailsPrincipal(user);
	return userDetailsPrincipal;
}

public User addUser(User user) {
	Set<Role> roles= new HashSet<>();
	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    user.getRoles().forEach(role->{
	if(role.getRole_id()>0) {
		Role nrole=roleRepository.findById(role.getRole_id()).get();
		nrole.getUsers().add(user);
		roles.add(nrole);
	}
	else {
		roles.add(role);
	}
	});
	user.setRoles(roles);
	return userRepository.save(user);
}

}
