package com.seller.panel.controller;

import com.seller.panel.dto.InvitationResponse;
import com.seller.panel.dto.User;
import com.seller.panel.dto.UserResponse;
import com.seller.panel.model.Users;
import com.seller.panel.repository.UserRepository;
import com.seller.panel.service.UserService;
import com.seller.panel.util.EndPointConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping(EndPointConstants.Users.USERS_BY_ID)
    @PreAuthorize("isAuthorized('GET','/users')")
    public ResponseEntity<UserResponse> findUserById(@NotNull @PathVariable("id") Long id) {
        User user = userService.findUserByIdAndCompanyId(id, getCompanyId());
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
