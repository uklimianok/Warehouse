package local.warehouse.app.flow.setupData;

import org.junit.jupiter.api.Test;

import local.warehouse.app.AbstractIT;

import static org.assertj.core.api.Assertions.assertThat;

public class SetupDataIT extends AbstractIT {
    @Test 
    void systemAdministratorCanCreateWarehouseSetup() {
        SetupData data = createSetupData(tokenFor("24000001", TEST_PASSWORD));
        assertThat(data.productId()).isPositive();
    }
}
