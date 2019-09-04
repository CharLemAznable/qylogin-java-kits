package com.github.charlemaznable.qylogin;

import lombok.val;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import static com.github.charlemaznable.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieValueTest {

    @Test
    public void testCookieValue() {
        val jsonString = "{\"UserId\":\"a\",\"Name\":\"b\",\"Avatar\":\"c\",\"CsrfToken\":\"d\",\"Expired\":\"2019-03-05T23:33:59.029596352+08:00\"}";
        CookieValue cookieValue = unJson(jsonString, CookieValue.class);

        assertEquals("a", cookieValue.getUserID());
        assertEquals("b", cookieValue.getName());
        assertEquals("c", cookieValue.getAvatar());
        assertEquals("d", cookieValue.getCsrfToken());
        DateTime expired = cookieValue.getExpired();
        assertEquals(2019, expired.getYear());
        assertEquals(3, expired.getMonthOfYear());
        assertEquals(5, expired.getDayOfMonth());
        assertEquals(23, expired.getHourOfDay());
        assertEquals(33, expired.getMinuteOfHour());
        assertEquals(59, expired.getSecondOfMinute());
    }
}
