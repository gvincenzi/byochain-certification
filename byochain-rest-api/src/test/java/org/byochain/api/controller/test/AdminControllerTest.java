package org.byochain.api.controller.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.URI;
import java.util.Locale;

import org.byochain.api.config.test.TestWebConfig;
import org.byochain.api.enumeration.ByoChainApiExceptionEnum;
import org.byochain.api.enumeration.ByoChainApiResponseEnum;
import org.byochain.api.exception.ByoChainApiException;
import org.byochain.api.request.UserCreationRequest;
import org.byochain.api.request.UserUpdateRequest;
import org.byochain.api.response.ByoChainApiResponse;
import org.byochain.commons.exception.ByoChainException;
import org.byochain.model.entity.User;
import org.byochain.services.service.impl.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JUnit Test
 * 
 * @author Giuseppe Vincenzi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestWebConfig.class})
@ActiveProfiles("test")
public class AdminControllerTest {
    @Autowired
    protected WebApplicationContext wac;
 
    @Autowired
    protected MockServletContext mockServletContext;
 
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    protected MessageSource messageSource;
 
    @Before
    public void setup() throws ByoChainException {
        this.mockMvc = webAppContextSetup(this.wac).build();
        Mockito.when(userService.getUsers(Matchers.any())).thenReturn(null);
        Mockito.when(userService.enableUser(10L, true)).thenReturn(getMock());
        Mockito.when(userService.addUser(getMock())).thenReturn(getMock());
    }
    
    private User getMock(){
    	User user = new User();
		user.setUsername("user#1");
		return user;
    }
 
    @Test
    public void testGetUsers() throws Exception {
        URI url = UriComponentsBuilder.fromUriString("/api/v1/admin/users")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, Locale.FRANCE);
        
        mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
    @Test
    public void testGetUsersWithRoles() throws Exception {
        URI url = UriComponentsBuilder.fromUriString("/api/v1/admin/users/roles")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, Locale.FRANCE);
        
        mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
    @Test
    public void testUpdateUser() throws Exception {
    	UserUpdateRequest request = new UserUpdateRequest();
    	request.setEnable(true);
        URI url = UriComponentsBuilder.fromUriString("/api/v1/admin/users/")
        		.pathSegment("10")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, Locale.FRANCE);
        response.setData(getMock());
        
        mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void testUpdateUsersException() throws Exception {
    	UserUpdateRequest request = new UserUpdateRequest();
        URI url = UriComponentsBuilder.fromUriString("/api/v1/admin/users/")
        		.pathSegment("10")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiException response = ByoChainApiExceptionEnum.ADMIN_CONTROLLER_USER_ENABLING_MANDATORY.getExceptionBeforeServiceCall(messageSource, Locale.FRANCE);
        
        mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
    @Test
    public void testAddUser() throws Exception {
    	UserCreationRequest request = new UserCreationRequest();
    	request.setUsername(getMock().getUsername());
        URI url = UriComponentsBuilder.fromUriString("/api/v1/admin/users/")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, Locale.FRANCE);
        response.setData(getMock());
        
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void testAddUserException() throws Exception {
    	UserCreationRequest request = new UserCreationRequest();
        URI url = UriComponentsBuilder.fromUriString("/api/v1/admin/users/")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiException response = ByoChainApiExceptionEnum.ADMIN_CONTROLLER_USER_DATA_MANDATORY.getExceptionBeforeServiceCall(messageSource, Locale.FRANCE);
        
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}