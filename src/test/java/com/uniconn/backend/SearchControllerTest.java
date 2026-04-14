package com.uniconn.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.uniconn.backend.dtos.SearchResultDTO;
import com.uniconn.backend.services.SearchService;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class SearchControllerTest {

    private SearchController searchController;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = mock(SearchService.class);
        searchController = new SearchController(searchService);
    }

    // TestCase 1
    @Test
    void search_validQuery_returns200() {

        SearchResultDTO mockResult = new SearchResultDTO(List.of(), List.of(), List.of());
        when(searchService.search("john")).thenReturn(mockResult);
        ResponseEntity<?> response = searchController.search("john");
        assertEquals(200, response.getStatusCode().value());
        
    }

    // TestCase 2
    @Test
    void search_validQuery_returnsNonNullBody() {

        SearchResultDTO mockResult = new SearchResultDTO(List.of(), List.of(), List.of());
        when(searchService.search("test")).thenReturn(mockResult);
        ResponseEntity<?> response = searchController.search("test");
        assertNotNull(response.getBody());

    }

    // TestCase 3
    @Test
    void search_serviceThrowsException_returns400() {

        when(searchService.search("badquery")).thenThrow(new RuntimeException("Search failed"));
        ResponseEntity<?> response = searchController.search("badquery");
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Search failed", response.getBody());

    }

    // TestCase 4
    @Test
    void search_noMatchingUsers_returnsEmptyUserList() {
        SearchResultDTO mockResult = new SearchResultDTO(List.of(), List.of(), List.of());
        when(searchService.search("zzznomatch")).thenReturn(mockResult);
        ResponseEntity<?> response = searchController.search("zzznomatch");
        SearchResultDTO body = (SearchResultDTO) response.getBody();
        assertNotNull(body);
        assertTrue(body.getUsers().isEmpty());

    }

    // TestCase 5
    @Test
    void search_noMatchingCommunities_returnsEmptyCommunityList() {
        SearchResultDTO mockResult = new SearchResultDTO(List.of(), List.of(), List.of());
        when(searchService.search("zzznomatch")).thenReturn(mockResult);
        ResponseEntity<?> response = searchController.search("zzznomatch");
        SearchResultDTO body = (SearchResultDTO) response.getBody();
        assertNotNull(body);
        assertTrue(body.getCommunities().isEmpty());
        
    }
}