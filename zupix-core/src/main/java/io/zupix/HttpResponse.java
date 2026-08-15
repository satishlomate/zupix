package io.zupix;

/** Mutable-free response state used by the HTTP pipeline. */
public final class HttpResponse {
    private Response response;

    public void set(Response response) { this.response = response; }
    public Response get() { return response; }
}
