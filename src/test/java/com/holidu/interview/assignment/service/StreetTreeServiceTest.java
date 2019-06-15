package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.model.StreetTreeData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class StreetTreeServiceTest {

    @InjectMocks
    private StreetTreeService subject;

    @Spy
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(subject, "streetTreeUrl", "https://data.cityofnewyork.us/resource/nwxe-4ae8.json");
    }

    @Test
    public void testRetrieveCommonStreetTree() {
        double latitude = 40.72309177;
        double longitude = -73.84421522;
        double radius = 2000;

        List<StreetTreeData> streetTreeDataList = new ArrayList<>(1);
        streetTreeDataList.add(buildStreetTreeData(
                "1027 GRAND STREET", "red maple", "40.72309177", "-73.84421522"));

        Map<String, Integer> expected = new HashMap<>();
        expected.put("red maple", 1);


        ResponseEntity<List<StreetTreeData>> listResponseEntity = ResponseEntity.ok(streetTreeDataList);
        doReturn(listResponseEntity).when(restTemplate).exchange("https://data.cityofnewyork.us/resource/nwxe-4ae8.json",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<StreetTreeData>>() {
                });

        Map<String, Integer> result = subject.retrieveCommonStreetTree(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertThat(result, is(expected));
    }

    @Test
    public void testRetrieveCommonStreetTreeWhenStreetTreeDataIsNull() {
        double latitude = 40.72309177;
        double longitude = -73.84421522;
        double radius = 2000;

        List<StreetTreeData> streetTreeDataList = new ArrayList<>(1);
        streetTreeDataList.add(buildStreetTreeData(
                "1027 GRAND STREET", "red maple", "40.72309177", "-73.84421522"));

        Map<String, Integer> expected = new HashMap<>();
        expected.put("red maple", 1);

        doReturn(null).when(restTemplate).exchange("https://data.cityofnewyork.us/resource/nwxe-4ae8.json",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<StreetTreeData>>() {
                });

        Map<String, Integer> result = subject.retrieveCommonStreetTree(latitude, longitude, radius);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRetrieveCommonStreetTreeWhenMoreThenOneTree() {
        double latitude = 40.72309177;
        double longitude = -73.84421522;
        double radius = 2000;

        List<StreetTreeData> streetTreeDataList = new ArrayList<>(1);
        streetTreeDataList.add(buildStreetTreeData(
                "1027 GRAND STREET", "red maple", "40.72309177", "-73.84421522"));
        streetTreeDataList.add(buildStreetTreeData(
                "1028 GRAND STREET", "red maple", "40.72309178", "-73.84421522"));


        Map<String, Integer> expected = new HashMap<>();
        expected.put("red maple", 2);


        ResponseEntity<List<StreetTreeData>> listResponseEntity = ResponseEntity.ok(streetTreeDataList);
        doReturn(listResponseEntity).when(restTemplate).exchange("https://data.cityofnewyork.us/resource/nwxe-4ae8.json",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<StreetTreeData>>() {
                });

        Map<String, Integer> result = subject.retrieveCommonStreetTree(latitude, longitude, radius);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertThat(result, is(expected));
    }

    private StreetTreeData buildStreetTreeData(String address, String commonName, String latitude, String longitude) {
        StreetTreeData streetTreeData = new StreetTreeData();
        streetTreeData.setAddress(address);
        streetTreeData.setSpcCommon(commonName);
        streetTreeData.setLatitude(latitude);
        streetTreeData.setLongitude(longitude);
        return streetTreeData;
    }
}
