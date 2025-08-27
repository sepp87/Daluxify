package dlx.client;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    private final PendingCalls pendingCalls;
    private final CountDownLatch latch;
    private boolean successful = false;
    private int code = -1;

    private List<T> result = Collections.emptyList();

    public AsyncResponse(Method method, String url, String json, Class<T> type, ApiClient client, PendingCalls pendingCalls, CountDownLatch latch) {
        this.method = method;
        this.url = url;
        this.requestBody = json;
        this.type = type;
        this.client = client;
        this.pendingCalls = pendingCalls;
        this.pendingCalls.incrementAndGet();
        this.latch = latch;
        System.out.println("PENDING CALLS " + this.pendingCalls.get());

    }

    @Override
    public void onFailure(Request request, IOException ex) {

        if (Config.LOG_EXCEPTIONS) {
            System.err.println("Request failed: " + ex.getMessage() + " 1x");
        }

        try { // can executeRequest or derializeAnGetNextPage even throw an exception?
            // API call failed once so next call is the second try
            String responseString = client.executeRequest(request, requestBody, 1);
            result = client.deserializeAndGetNextPage(responseString, type);

        } catch (Exception e) {
            System.out.println("ERROR: UNKNOWN EXCEPTION THROWN");
        } finally {
            int currentPendingCalls = pendingCalls.decrementAndGet();
            if (currentPendingCalls == 0) {
                latch.countDown();
            }
            if (Config.LOG_PROGRESS) {
                System.out.println("PENDING CALLS " + currentPendingCalls);
            }
        }
    }

    @Override
    public void onResponse(Response response) throws IOException {
        successful = response.isSuccessful();
        code = response.code();

        try ( ResponseBody responseBody = response.body()) {
            if (responseBody == null) {
                return;
            }

            // Handle successful response
            if (response.isSuccessful()) {
                // Get the response body
                String responseString = responseBody.string();
                if (Config.LOG_RESPONSE) {
                    System.out.println(responseString);
                }
                result = client.deserializeAndGetNextPage(responseString, type);

            } else {
                // Print request and response information in case of an unsuccessful call 
                if (Config.LOG_ERRORS) {
                    System.out.println(method.toString() + " " + url);
                    System.out.println("Request Body: " + requestBody);
                    System.out.println("Response Body: " + responseBody.string());
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: UNKNOWN EXCEPTION THROWN");
        } finally {
            int currentPendingCalls = pendingCalls.decrementAndGet();
            if (currentPendingCalls == 0) {
                latch.countDown();
            }
            if (Config.LOG_PROGRESS) {
                System.out.println("PENDING CALLS " + currentPendingCalls);
            }
        }
    }

    public List<T> getResult() {
        return result;
    }

    public Class getType() {
        return type;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public int getCode() {
        return code;
    }

    public AsyncResponse<T> getAsyncAgain() {
        this.pendingCalls.incrementAndGet();
        System.out.println("PENDING CALLS " + this.pendingCalls.get());
        return client.getAsync(url, type);
    }
}
