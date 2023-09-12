package com.pashonokk.dvdrental;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suite
@SelectPackages("com.pashonokk.dvdrental.integration")
class DvDRentalApplicationTests {

    @Test
    void contextLoads() {
    }

}
