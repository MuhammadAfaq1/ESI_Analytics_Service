package ee.ut.eventticketing.analytics.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String issuer, String secret) {
}
