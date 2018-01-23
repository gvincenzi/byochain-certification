package org.byochain.api.controller.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.URI;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.byochain.api.config.test.TestWebConfig;
import org.byochain.api.enumeration.ByoChainApiExceptionEnum;
import org.byochain.api.enumeration.ByoChainApiResponseEnum;
import org.byochain.api.exception.ByoChainApiException;
import org.byochain.api.response.ByoChainApiResponse;
import org.byochain.commons.exception.ByoChainException;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.User;
import org.byochain.services.service.ICertificationBlockService;
import org.byochain.services.service.impl.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
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
public class BlockControllerTest {
    @Autowired
    protected WebApplicationContext wac;
 
    @Autowired
    protected MockServletContext mockServletContext;
 
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    @Qualifier("certificationBlockService")
    private ICertificationBlockService blockService;
    
    @Autowired
    protected MessageSource messageSource;
 
    @Before
    public void setup() throws ByoChainException {
        this.mockMvc = webAppContextSetup(this.wac).build();
        Mockito.when(blockService.getBlocks(Matchers.any())).thenReturn(null);
        Mockito.when(blockService.getBlockByHash("TestKo")).thenReturn(null);
        Mockito.when(blockService.getBlockByHash("TestOk")).thenReturn(getMockBlock());
        Mockito.when(blockService.addBlock("TestData", getMockUser())).thenReturn(getMockBlock());
        Mockito.when(blockService.getAllBlocks()).thenReturn(getMockSetBlock());
        Mockito.when(userService.getUser(getMockUser().getUsername(), getMockUser().getPassword())).thenReturn(getMockUser());
        
    }
    
	private User getMockUser(){
    	User user = new User();
		user.setUsername("user#1");
		user.setPassword("pass#1");
		user.setEnabled(true);
		return user;
    }
    
    private Block getMockBlock(){
    	Block block = new Block();
		return block;
    }
    
    private Set<Block> getMockSetBlock(){
    	Set<Block> blocks = new HashSet<>(1);
    	blocks.add(getMockBlock());
    	return blocks;
    }
    
    @Test
    public void testGetBlocks() throws Exception {
        URI url = UriComponentsBuilder.fromUriString("/api/v1/blocks/")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, Locale.FRANCE);
        
        mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
    @Test
    public void testGetBlockByHash() throws Exception {
        URI url = UriComponentsBuilder.fromUriString("/api/v1/blocks")
        		.pathSegment("TestOk")
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, Locale.FRANCE);
        
        mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
    @Test
    public void testGetBlockByException() throws Exception {
    	String hash = "TestKo";
        URI url = UriComponentsBuilder.fromUriString("/api/v1/blocks")
        		.pathSegment(hash)
                .build().encode().toUri();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ByoChainApiException response = ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, Locale.FRANCE, hash);
        
        mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
