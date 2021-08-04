package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.user.UserAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserAddOwnerDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.user.UserDtoResponse;
import com.crm.servicebackend.exception.domain.AuthException;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.Role;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.repository.UserRepository;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static com.crm.servicebackend.constant.response.auth.AuthResponseCode.INVALID_USERNAME_OR_PASSWORD_CODE;
import static com.crm.servicebackend.constant.response.auth.AuthResponseCodeMessage.INVALID_USERNAME_OR_PASSWORD_MESSAGE;
import static com.crm.servicebackend.constant.response.user.UserResponseCode.USER_EXISTS_BY_USERNAME_CODE;
import static com.crm.servicebackend.constant.response.user.UserResponseMessage.USER_EXISTS_BY_USERNAME_MESSAGE;


@Service(value = "userService")
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final ServiceCenterService serviceCenterService;
    private final ExperienceModelService experienceModelService;
    private final BCryptPasswordEncoder bcryptEncoder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, ServiceCenterService serviceCenterService, ExperienceModelService experienceModelService, BCryptPasswordEncoder bcryptEncoder, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
        this.experienceModelService = experienceModelService;
        this.bcryptEncoder = bcryptEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user == null){
            throw new AuthException(INVALID_USERNAME_OR_PASSWORD_MESSAGE, INVALID_USERNAME_OR_PASSWORD_CODE);
        }
        return user;
    }

    public User getByUsername(String username) {
        User user = repository.findByUsername(username);
        if(user == null){
            throw new AuthException(INVALID_USERNAME_OR_PASSWORD_MESSAGE, INVALID_USERNAME_OR_PASSWORD_CODE);
        }
        return user;
    }


    public UserDtoResponse add(Long serviceCenterId, UserAddDtoRequest dto, User user) {
        if (existsByUsername(dto.getUsername()))
            throw new DtoException(USER_EXISTS_BY_USERNAME_MESSAGE, USER_EXISTS_BY_USERNAME_CODE);
        User model = new User();
        model = addDtoToModel(dto);

        if (user.getRoles().contains(Role.OWNER)) {
            model.setRoles(dto.getRoles());
        } else {
            Set<Role> roles = dto.getRoles();
            roles.remove(Role.OWNER);
            model.setRoles(roles);
        }
        model.setEnabled(true);
        model.setExperienceModel(experienceModelService.get(dto.getExperienceModelId(), serviceCenterId));
        model.setPassword(passwordEncoder.encode(dto.getPassword()));
        model.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(model));
    }


    public Map<String, Object> getAll(Long serviceCenterId, int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterId(serviceCenterId, pageable)));
    }

    public Map<String, Object> getAllAndFilter(Long serviceCenterId, int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAndFilter(serviceCenterId, title, pageable)));
    }

    public User update(Long userId, Long serviceCenterId, UserUpdateDtoRequest dto, User user) {
        if (existsByUsernameAndIdNotLike(dto.getUsername(), userId))
            throw new DtoException(USER_EXISTS_BY_USERNAME_MESSAGE, USER_EXISTS_BY_USERNAME_CODE);
        User model = get(userId, serviceCenterId);
        model = updateDtoToModel(model, dto);
        if (model.getRoles() != dto.getRoles()) {
            if (user.getRoles().contains(Role.OWNER)) {
                model.setRoles(dto.getRoles());
            } else {
                Set<Role> roles = dto.getRoles();
                roles.remove(Role.OWNER);
                model.setRoles(roles);
            }
        }
        if (!model.getExperienceModel().getId().equals(dto.getExperienceModelId()))
            model.setExperienceModel(experienceModelService.get(dto.getExperienceModelId(), serviceCenterId));
        if (dto.getPassword().length() != 60)
            model.setPassword(passwordEncoder.encode(dto.getPassword()));
        return save(model);
    }

    public User get(Long userId, Long serviceCenterId) {
        return repository.getByIdAndServiceCenterId(userId, serviceCenterId);
    }

    public Page<UserDtoResponse> pageToDtoPage(Page<User> modelPage) {
        final Page<UserDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public UserDtoResponse modelToDtoResponse(User model) {
        return UserFacade.modelToDtoResponse(model);
    }

    public User updateDtoToModel(User model, UserUpdateDtoRequest dto) {
        return UserFacade.updateDtoToModel(model, dto);
    }

    public User addDtoToModel(UserAddDtoRequest dto) {
        return UserFacade.addDtoToModel(dto);
    }

    public User addOwnerDtoToModel(UserAddOwnerDtoRequest dto){
        return UserFacade.addOwnerDtoToModel(dto);
    }
    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long userId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(userId, serviceCenterId);
    }

    public Boolean existsByUsername(String username){
        return repository.existsByUsername(username);
    }

    public Boolean existsByUsernameAndIdNotLike(String username, Long userId){
        return repository.existsByUsernameAndIdNotLike(username, userId);
    }

}
