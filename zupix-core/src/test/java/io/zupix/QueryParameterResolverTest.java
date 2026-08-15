package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryParameterResolverTest {
    @Test
    void resolvesTypedQueryParameter() throws Exception {
        var method = ExampleApi.class.getDeclaredMethod("hello", String.class, Integer.class);
        Object[] values = new QueryParameterResolver().resolve(method, "name=Satish&limit=10");
        assertEquals("Satish", values[0]);
        assertEquals(10, values[1]);
    }

    static class ExampleApi {
        void hello(@QueryParam("name") String name, @QueryParam("limit") Integer limit) {}
    }
}
