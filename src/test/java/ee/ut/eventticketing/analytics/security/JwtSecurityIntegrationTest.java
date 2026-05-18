package ee.ut.eventticketing.analytics.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class JwtSecurityIntegrationTest {

    private static final String EVENT_ID = "aaaa1111-1111-1111-1111-111111111111";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedEndpointWithoutTokenReturns401() throws Exception {
        mockMvc.perform(delete("/api/analytics/events/" + EVENT_ID))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void publicReportsWithoutTokenReturns200() throws Exception {
        mockMvc.perform(get("/api/analytics/events"))
                .andExpect(status().isOk());
    }

    @Test
    void adminDeleteWithUserTokenReturns403() throws Exception {
        mockMvc.perform(delete("/api/analytics/events/" + EVENT_ID)
                        .header("Authorization", "Bearer " + JwtTestTokens.create("USER")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void syncWithUserTokenIsAllowed() throws Exception {
        mockMvc.perform(post("/api/analytics/events/" + EVENT_ID + "/sync")
                        .header("Authorization", "Bearer " + JwtTestTokens.create("USER")))
                .andExpect(status().isOk());
    }
}
