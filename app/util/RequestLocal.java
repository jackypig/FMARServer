package util;

import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/25/14
 * Time: 5:08 PM
 */
public abstract class RequestLocal<T> {
    private Map<Http.Request, T> localByRequest;

    public RequestLocal () {
        localByRequest = new HashMap<Http.Request, T>();
    }

    protected abstract T initialize ();

    public T get(Http.Request request) {
        T local = localByRequest.get(request);
        if (local == null) {
            local = initialize();
            localByRequest.put(request, local);
        }

        return local;
    }

    public void set(Http.Request request, T value) {
        localByRequest.put(request, value);
    }

    public void clear (Http.Request request) {
        localByRequest.remove(request);
    }

    public int size() {
        return localByRequest.size();
    }
}

