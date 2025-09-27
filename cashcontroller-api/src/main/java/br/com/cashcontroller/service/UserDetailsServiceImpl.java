package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AlterarSenhaDTO;
import br.com.cashcontroller.exception.SenhaInvalidaException;
import br.com.cashcontroller.model.User;
import br.com.cashcontroller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        if(userRepository.existsById(id)) {
            return userRepository.getReferenceById(id);
        } else {
            return  null;
        }
    }

    public String alterarSenha(AlterarSenhaDTO alterarSenhaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User usuarioAutenticado = (User) authentication.getPrincipal();
        Long idUsuario = usuarioAutenticado.getId();

        User usuario = findById(idUsuario);

        if(!passwordEncoder.matches(alterarSenhaDTO.getSenhaAtual(), usuario.getPassword())) {
            throw new SenhaInvalidaException("A senha atual está incorreta");
        }
        usuario.setPassword(passwordEncoder.encode(alterarSenhaDTO.getNovaSenha()));
        userRepository.save(usuario);
        return "Senha alterada com sucesso!";
    }
}
