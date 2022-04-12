package com.marksarchi.countylocationservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
public class BaseTestIT extends DataBaseIT{

    public ObjectMapper mapper = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    public MockMvc getMockMvc() {
        return this.mockMvc;
    }


    public MockHttpServletRequestBuilder getRequest(String urlMapping){
        var request  = MockMvcRequestBuilders
                .get(urlMapping)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return  request;

    }

    public MockHttpServletRequestBuilder postRequest(String object,String urlMapping) throws JsonProcessingException {
        var request  = MockMvcRequestBuilders
                .post(urlMapping)
                .accept(MediaType.APPLICATION_JSON)
                .content(object)
                .contentType(MediaType.APPLICATION_JSON);

        return  request;

    }


}
