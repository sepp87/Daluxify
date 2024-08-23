package dlx.client;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Joost
 */
public class AsyncResponse<T> implements Callback {

    private final Method method;
    private final String url;
    private final String requestBody;
    private final ApiClient client;
    private final Class<T> type;
    private final AtomicInteger pendingCalls;
    private final CountDownLatch latch;

    private List<T> result = Collections.emptyList();

    public AsyncResponse(Method method, String url, String json, Class<T> type, ApiClient client, AtomicInteger pendingCalls, CountDownLatch latch) {
        this.method = method;
        this.url = url;
        this.requestBody = json;
        this.type = type;
        this.client = client;
        this.pendingCalls = pendingCalls;
        this.pendingCalls.incrementAndGet();
        this.latch = latch;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        System.err.println("Request failed: " + e.getMessage());
        if (pendingCalls.decrementAndGet() == 0) {
            latch.countDown();
        }
        System.out.println("PENDING CALLS " + pendingCalls.get());
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try ( ResponseBody responseBody = response.body()) {
            if (responseBody == null) {
                return;
            }

            // Handle successful response
            if (response.isSuccessful()) {
                // Get the response body
                String responseString = responseBody.string();
                if (App.LOG) {
                    System.out.println(responseString);
                }
                result = client.deserializeAndGetNextPage(responseString, type);

            } else {
                // Print request and response information in case of an unsuccessful call 
                if (App.LOG_ERRORS) {
                    System.out.println(method.toString() + " " + url);
                    System.out.println("Request Body: " + requestBody);
                    System.out.println("Response Body: " + responseBody.string());
                }
            }
        } finally {
            if (pendingCalls.decrementAndGet() == 0) {
                latch.countDown();
            }
            System.out.println("PENDING CALLS " + pendingCalls.get());
        }
    }

    public List<T> getResult() {
        return result;
    }

    public Class getType() {
        return type;
    }
}
