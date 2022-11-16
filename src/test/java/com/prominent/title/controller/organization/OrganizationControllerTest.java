package com.prominent.title.controller.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.organization.*;
import com.prominent.title.dto.user.UserIdAndNameDto;
import com.prominent.title.exception.OrganizationNotFoundException;
import com.prominent.title.service.organization.OrganizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
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
    void add_organization_test_200_ok() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        OrganizationEntryDto organizationEntryDto = new OrganizationEntryDto(organizationDto, paymentInformationDto);

        when(organizationService.add(any())).thenReturn(organizationEntryDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/organization/entry")
                        .content(objectMapper.writeValueAsString(organizationEntryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void add_organization_already_exist_test_409_conflict() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        OrganizationEntryDto organizationEntryDto = new OrganizationEntryDto(organizationDto, paymentInformationDto);

        when(organizationService.add(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/organization/entry")
                        .content(objectMapper.writeValueAsString(organizationEntryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void edit_organization_test_200_ok() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Surya", "2025550162", "Surya@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        OrganizationEntryDto organizationEntryDto = new OrganizationEntryDto(organizationDto, paymentInformationDto);

        when(organizationService.add(any())).thenReturn(organizationEntryDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/organization/edit")
                        .content(objectMapper.writeValueAsString(organizationEntryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void delete_organization_test_200_ok() throws Exception {

        when(organizationService.deleteOrganization(anyInt())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/organization/delete")
                        .param("organizationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void delete_organization_test_404_not_found() throws Exception {

        when(organizationService.deleteOrganization(anyInt())).thenThrow(OrganizationNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/organization/delete")
                        .param("organizationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void get_organization_test_200_ok() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        List<UserIdAndNameDto> organizationBrokers = new ArrayList<>();
        organizationBrokers.add(new UserIdAndNameDto(10, "Virat", "Kohli", "virat@gmail.com", "8605588552"));

        OrganizationDtoWithBrokers organizationDtoWithBrokers = new OrganizationDtoWithBrokers(organizationDto, paymentInformationDto, organizationBrokers);
        when(organizationService.getOrganization(anyInt())).thenReturn(organizationDtoWithBrokers);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/organization/get")
                        .param("organizationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void get_organization_test_404_not_found() throws Exception {

        OrganizationDto organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        PaymentInformationDto paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        List<UserIdAndNameDto> organizationBrokers = new ArrayList<>();
        organizationBrokers.add(new UserIdAndNameDto(10, "Virat", "Kohli", "virat@gmail.com", "8605588552"));

        OrganizationDtoWithBrokers organizationDtoWithBrokers = new OrganizationDtoWithBrokers(organizationDto, paymentInformationDto, organizationBrokers);
        when(organizationService.getOrganization(anyInt())).thenThrow(OrganizationNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/organization/get")
                        .param("organizationId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void search_organization_test_200_ok() throws Exception {

        List<OrganizationSearchDto> organizationSearchDtos = new ArrayList<>();
        organizationSearchDtos.add(new OrganizationSearchDto(1, "Test-Organization", "Test"));
        when(organizationService.searchOrganization(any())).thenReturn(organizationSearchDtos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/organization/search")
                        .param("searchTerm", "Test-Organization")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
