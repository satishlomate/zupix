package io.zupix;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityTest {
    @AfterEach
    void clearContext() { SecurityContext.clear(); }

    @Test
    void authenticationExposesPrincipalDuringRequestAndClearsAfterward() {
        Principal principal = new Principal("satish", Set.of("admin"));
        new AuthenticationMiddleware(request -> principal).handle(request(), () ->
                assertEquals(principal, SecurityContext.current()));
        assertNull(SecurityContext.current());
    }

    @Test
    void missingAuthenticationIsRejected() {
        assertThrows(UnauthorizedException.class,
                () -> new AuthenticationMiddleware(request -> null).handle(request(), () -> {}));
    }

    @Test
    void authorizationRequiresRole() {
        SecurityContext.set(new Principal("satish", Set.of("user")));
        assertThrows(ForbiddenException.class,
                () -> new AuthorizationMiddleware("admin").handle(request(), () -> {}));
    }

    @Test
    void authorizationAllowsRequiredRole() {
        SecurityContext.set(new Principal("satish", Set.of("admin")));
        new AuthorizationMiddleware("admin").handle(request(), () -> assertEquals("satish", SecurityContext.current().name()));
    }

    private static RequestContext request() {
        return new RequestContext("GET", "/", null, java.util.Map.of(), "");
    }
}
