package com.holidu.interview.assignment.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class CommonUtilityTest {

    @Test
    public void testCalculateDistance() {
        double latitude1 = 40.72309177;
        double longitude1 = -73.84421522;

        double latitude2 = 40.72309190;
        double longitude2 = -73.84421550;

        double expectedDistance = 0.0276718013269304;

        double result = CommonUtility.calculateDistance(latitude1, latitude2, longitude1, longitude2);

        assertThat(result, is(expectedDistance));
    }
}
