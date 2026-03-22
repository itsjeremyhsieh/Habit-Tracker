package com.project.habit_tracker.Security;

import com.project.habit_tracker.Model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    @Test
    void generateTokenStoresNameUsernameAndRoleClaims() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "HabitTrackerJwtSecretKeyForHmacSigningNeedsToBeAtLeastThirtyTwoBytes");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 60000L);
        jwtService.init();

        User user = new User();
        user.setName("Jeremy");
        user.setUsername("jeremy");
        user.setRole("ADMIN");

        String token = jwtService.generateToken(user);

        assertEquals("jeremy", jwtService.extractUsername(token));
        assertEquals("Jeremy", jwtService.extractAllClaims(token).get("name", String.class));
        assertEquals("jeremy", jwtService.extractAllClaims(token).get("username", String.class));
        assertEquals("ADMIN", jwtService.extractAllClaims(token).get("role", String.class));
        assertTrue(jwtService.isTokenValid(token));
    }
}

