package com.prominent.title.controller.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.organization.*;
import com.prominent.title.dto.user.UserIdAndNameDto;
import com.prominent.title.service.organization.OrganizationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class OrganizationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrganizationService organizationService;

    @Test
    void addOrganizationTest() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        OrganizationEntryDto organizationEntryDto = new OrganizationEntryDto(organizationDto, paymentInformationDto);

        Mockito.when(organizationService.add(Mockito.any())).thenReturn(organizationEntryDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/organization/entry")
                        .content(objectMapper.writeValueAsString(organizationEntryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void editOrganizationTest() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Surya", "2025550162", "Surya@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        OrganizationEntryDto organizationEntryDto = new OrganizationEntryDto(organizationDto, paymentInformationDto);

        Mockito.when(organizationService.add(Mockito.any())).thenReturn(organizationEntryDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/organization/edit")
                        .content(objectMapper.writeValueAsString(organizationEntryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void deleteOrganizationTest() throws Exception {

        Mockito.when(organizationService.deleteOrganization(Mockito.anyInt())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/organization/delete")
                        .param("organizationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getOrganizationTest() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        List<UserIdAndNameDto> organizationBrokers = new ArrayList<>();
        organizationBrokers.add(new UserIdAndNameDto(10, "Virat", "Kohli", "virat@gmail.com", "8605588552"));

        OrganizationDtoWithBrokers organizationDtoWithBrokers = new OrganizationDtoWithBrokers(organizationDto, paymentInformationDto, organizationBrokers);
        Mockito.when(organizationService.getOrganization(Mockito.anyInt())).thenReturn(organizationDtoWithBrokers);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/organization/get")
                        .param("organizationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void searchOrganizationTest() throws Exception {

        List<OrganizationSearchDto> organizationSearchDtos = new ArrayList<>();
        organizationSearchDtos.add(new OrganizationSearchDto(1, "Test-Organization", "Test"));
        Mockito.when(organizationService.searchOrganization(Mockito.any())).thenReturn(organizationSearchDtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/organization/search")
                        .param("searchTerm", "Test-Organization")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
