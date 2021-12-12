package controller;

import entity.order.Order;
import entity.shipping.DeliveryInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PlaceRushOrderControllerTest {
    private PlaceRushOrderController controller = new PlaceRushOrderController();

    /**
     * Province that is not in Configs.PROVINCES is considered as invalid
     *    So only "Hà Nội" satisfies rush condition.
     */
    @ParameterizedTest
    @CsvSource(value = {
            "Ha Noi,false",
            "Hà Nội,true",
            "hà nội,false",
            "Thái Bình,false"
    })
    void checkRushProvinceCondition(String province, boolean expected) {
        DeliveryInfo info = new DeliveryInfo();
        info.setProvince(province);
        boolean isOk = controller.checkRushDeliveryInfoCondition(info);
        assertEquals(isOk, expected);
    }

    /**
     * {@code PlaceRushOrderController.validateDateInput} only returns true
     * if date string follow the format of JavaFx DatePicker value ("uuuu-MM-dd")
     * and the input date must be after the date when user places order
     */
    @ParameterizedTest
    @CsvSource(value = {
            ",false",
            "2021-12-13,true",
            "2021/12/13,false",
            "2000-12-12,false",
            "2100-13-33,false",
            "12-12-2021,false"
    })
    void validateDateInput(String dateInput, boolean expected) {
        boolean isOk = controller.validateDateInput(dateInput);
        assertEquals(expected, isOk);
    }
}