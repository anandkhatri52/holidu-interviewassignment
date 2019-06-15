package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.model.StreetTreeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.holidu.interview.assignment.utility.CommonUtility.calculateDistance;
import static java.lang.Double.parseDouble;
import static java.util.Collections.EMPTY_LIST;

@Service
public class StreetTreeService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${street.tree.url}")
    private String streetTreeUrl;

    public Map<String, Integer> retrieveCommonStreetTree(double latitude, double longitude, double radius) {
        Map<String, Integer> commonTreeCountMap = new HashMap<>();
        List<StreetTreeData> streetTreeDataList = getStreetTreeData();

        if (!CollectionUtils.isEmpty(streetTreeDataList)) {
            streetTreeDataList
                    .stream()
                    .filter(streetTreeData -> Objects.nonNull(streetTreeData.getSpcCommon()))
                    .forEach(streetTreeData -> {
                        double distance = calculateDistance(latitude, parseDouble(streetTreeData.getLatitude()), longitude,
                                parseDouble(streetTreeData.getLongitude()));
                        if (distance <= radius) {
                            String speciesCommonName = streetTreeData.getSpcCommon();
                            if (commonTreeCountMap.containsKey(speciesCommonName)) {
                                commonTreeCountMap.put(speciesCommonName, commonTreeCountMap.get(speciesCommonName) + 1);
                            } else {
                                commonTreeCountMap.put(speciesCommonName, 1);
                            }
                        }
                    });
        }

        return commonTreeCountMap;
    }

    private List<StreetTreeData> getStreetTreeData() {
        List<StreetTreeData> streetTreeData = EMPTY_LIST;
        ResponseEntity<List<StreetTreeData>> listResponseEntity = restTemplate.exchange(
                streetTreeUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StreetTreeData>>() {
                });
        if (Objects.nonNull(listResponseEntity)) {
            streetTreeData = listResponseEntity.getBody();
        }
        return streetTreeData;
    }
}
