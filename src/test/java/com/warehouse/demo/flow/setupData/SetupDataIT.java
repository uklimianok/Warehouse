package com.warehouse.demo.flow.setupData;

import org.junit.jupiter.api.Test;

import com.warehouse.demo.AbstractIT;

import static org.assertj.core.api.Assertions.assertThat;

public class SetupDataIT extends AbstractIT {
    @Test 
    void systemAdministratorCanCreateWarehouseSetup() {
        SetupData data = createSetupData(tokenFor("24000001", TEST_PASSWORD));
        assertThat(data.productId()).isPositive();
    }
}
