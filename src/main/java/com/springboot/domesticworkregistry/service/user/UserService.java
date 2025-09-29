package com.springboot.domesticworkregistry.service.user;

import java.util.List;

import com.springboot.domesticworkregistry.dto.user.ChangePasswordDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;
import com.springboot.domesticworkregistry.dto.user.UpdateUserDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.User;

public interface UserService {

    List<User> findAll();

    User findById(String id);

    User findByEmail(String email);

    UpdateUserDto getUserDto(String id);

    User update(UpdateUserDto form, String id);

    void delete(String id);

    User registerEmployer(RegisterUserDto form);

    User registerEmployee(RegisterUserEmployeeDto form);

    List<Contract> findContractsByEmployer(String id);

    List<Contract> findContractsByEmployee(String id);

    void changePassword(User user, ChangePasswordDto form);

}
