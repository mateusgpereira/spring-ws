package com.springws.rest.user;


import com.springws.app.SpringWsApplication;
import com.springws.app.controller.UserController;
import com.springws.app.service.AddressService;
import com.springws.app.service.UserService;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.request.UserDetailsRequestModel;
import com.springws.app.ui.model.response.UserRest;
import com.springws.factories.UserFactory;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {SpringWsApplication.class})
public class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    AddressService addressService;

    @SpyBean
    ModelMapper modelMapper;

    @BeforeEach()
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void shouldCreateUser() {
        UserDetailsRequestModel user = UserFactory.createDetailsRequestModel();

        UserDto userDto = UserFactory.createDtoFromDetailsRequestModel(user);

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        UserRest result = given()
                .auth().none()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(200)
                .extract()
                .as(UserRest.class);

        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        verify(userService, times(1)).createUser(eq(userDto));
        verify(modelMapper, times(1)).map(user, UserDto.class);
        verify(modelMapper, times(1)).map(any(UserDto.class), eq(UserRest.class));
    }
}
