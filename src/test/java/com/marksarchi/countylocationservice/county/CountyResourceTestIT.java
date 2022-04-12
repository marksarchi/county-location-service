package com.marksarchi.countylocationservice.county;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.marksarchi.countylocationservice.dto.CountyDto;
import com.marksarchi.countylocationservice.utils.BaseTestIT;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class CountyResourceTestIT extends BaseTestIT {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void fetchCounties() throws Exception {
        var url = "/api/county/counties";
        var req = this.getRequest(url);
        ResultActions actions = this.getMockMvc().
                perform(req)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Fetched successfully"));

        var result = actions.andReturn().getResponse().getContentAsString();
        JsonNode parent = mapper.readTree(result);
        String data = parent.get("data").toString();
        List<CountyDto> countyDtoList = mapper.readValue(data, new TypeReference<>() {
        });
        assertThat(countyDtoList.size()).isEqualTo(1);
    }

    @Test
    void checkLocationTest() throws Exception {
        String payload = "{\n" +
                "    \n" +
                "     \"latitude\" :\"0.48772444651396546\" ,\n" +
                "      \"longitude\": \"35.27126916679692\" \n" +
                "    \n" +
                "}";
        var url = "/api/county/check-county";
        var req = this.postRequest(payload, url);
        ResultActions actions = getMockMvc().perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Location found successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Uasin Gishu"));
    }

    @Test
    void checkLocationTestWithInvalidPoint() throws Exception {
        String payload = "{\"latitude\" :\"6.4437805810455943\" ,\n" +
                "      \"longitude\": \"34.71645945690384\"}";
        var url = "/api/county/check-county";
        var req = this.postRequest(payload, url);
        ResultActions actions = getMockMvc().perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Location not  found any county"));
    }
}