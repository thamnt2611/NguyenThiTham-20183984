package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderControllerTest {
    private PlaceOrderController controller;

    @BeforeEach
    void setUp() {
        controller = new PlaceOrderController();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateDeliveryInfo() {

    }

    @ParameterizedTest
    @CsvSource({
            "0123456789,true",
            "1234567891,false",
            "043533,false",
            "012d347o2e,false"
    })
    void validatePhoneNumber(String phone, boolean expected) {
        boolean isValid = controller.validatePhoneNumber(phone);
        assertEquals(expected, isValid);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void validatePhoneNumber_NotBlank(String phone) {
        assertFalse(controller.validatePhoneNumber(phone));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Nguyen Hung An,true",
            "?Nguyen ?An *,false",
            "Nguyen 23 Van,false",
            " '     ' ,false",
            "  , false",
            "Nguyễn Thị Thắm,true"

    })
    void validateName(String phone, boolean expected) {
        boolean isValid = controller.validateName(phone);
        assertEquals(expected, isValid);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "23, Chùa Bộc, Hanoi:true",
            "?Chua Boc ?Tb *:false",
            "1/23 ngõ định công thượng:true",
            " :false",
            "'   ':false"

    }, delimiter = ':')
    void validateAddress(String address, boolean expected) {
        boolean isValid = controller.validateAddress(address);
        assertEquals(expected, isValid);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Ha Noi:false",
            "Hà Nội:true",
            "Thái Bình:true",
            " :false",
            "'   ':false"

    }, delimiter = ':')
    void validateProvince(String province, boolean expected) {
        boolean isValid = controller.validateProvince(province);
        assertEquals(expected, isValid);
    }
}