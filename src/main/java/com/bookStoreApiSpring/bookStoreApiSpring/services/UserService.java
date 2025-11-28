package com.bookStoreApiSpring.bookStoreApiSpring.services;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.UserFormDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.BadRequestException;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iUserRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iUserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements iUserService {

    private final iUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> paginate(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Error Usuario id no encontrado!"));
    }

    @Override
    public User create(UserFormDTO userFormDTO) {
        User user = null;

        boolean existsEmail = userRepository.existsByEmail(userFormDTO.getEmail());

        if (existsEmail){
            throw new BadRequestException("ERROR DUPLICADO: El Email duplicado! ");
        }

        try {
            user = new ModelMapper().map(userFormDTO,User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            //user.setRole(User.Role.USER);

//            user.setFirstName(userFormDTO.getFirstName());
//            user.setLastName(userFormDTO.getLastName());
//            user.setFullName(userFormDTO.getFullName());
//            user.setEmail(userFormDTO.getEmail());
//            user.setPassword(userFormDTO.getPassword());
//            user.setRole(userFormDTO.getRole());
//            user.setCreatedAt(LocalDateTime.now());
        }catch (DataAccessException e){
            throw new BadRequestException("ERROR: Al crear usuario! ",e);
        }
        return userRepository.save(user);
    }

    @Override
    public User update(Integer id, UserFormDTO userFormDTO) {
        User user = findById(id);

        boolean existsEmail = userRepository.existsByEmailAndIdNot(userFormDTO.getEmail(), id);

        if (existsEmail){
            throw new BadRequestException("ERROR DUPLICADO: Email duplicado! ");
        }

        try {
            if (user != null){
                new ModelMapper().map(userFormDTO, user);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRole(User.Role.USER);
                user.setCreatedAt(LocalDateTime.now());
//                user.setFirstName(userFormDTO.getFirstName());
//                user.setLastName(userFormDTO.getLastName());
//                user.setFullName(userFormDTO.getFullName());
//                user.setEmail(userFormDTO.getEmail());
//                user.setPassword(userFormDTO.getPassword());
//                user.setRole(userFormDTO.getRole());
//                user.setUpdatedAt(LocalDateTime.now());
            }else {
                throw new BadRequestException("Error al actualizar Usuario");
            }
        }catch (DataAccessException e){
            throw new BadRequestException("Error al actualizar Usuario");
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        User userDelete = findById(id);
        userRepository.deleteById(id);
    }
}
