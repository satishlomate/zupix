package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PathMatcherTest {

    @Test
    void extractsPathParameters() {
        var values = new PathMatcher("/users/{id}/posts/{postId}")
                .match("/users/42/posts/7");

        assertEquals("42", values.get("id"));
        assertEquals("7", values.get("postId"));
    }

    @Test
    void rejectsDifferentPath() {
        assertNull(new PathMatcher("/users/{id}").match("/teams/42"));
    }
}
