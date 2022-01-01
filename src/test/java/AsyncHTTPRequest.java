import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.BasicHttpRequest;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.support.BasicRequestBuilder;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncHTTPRequest {
    @Test
    void test() throws ExecutionException, InterruptedException, IOException {
		final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
				.setSoTimeout(Timeout.ofSeconds(5))
				.build();

		final CloseableHttpAsyncClient client = HttpAsyncClients.custom()
				.setIOReactorConfig(ioReactorConfig)
				.build();

		client.start();

		final HttpHost target = new HttpHost("127.0.0.1");
		final String[] requestUris = new String[] {"/jte/editor/download/1.2"};

		for (final String requestUri: requestUris) {

			final BasicHttpRequest request = BasicRequestBuilder.get()
					.setHttpHost(target)
					.setPath(requestUri)
					.build();

			System.out.println("Executing request " + request);
			final Future<HttpResponse> future = client.execute(
					new BasicRequestProducer(request, null),
					new AbstractCharResponseConsumer<>() {

						private HttpResponse httpResponse;

						@Override
						protected void start(
								final HttpResponse response,
								final ContentType contentType) throws HttpException, IOException {
							System.out.println(request + "->" + new StatusLine(response));
							this.httpResponse = response;
						}

						@Override
						protected int capacityIncrement() {
							return Integer.MAX_VALUE;
						}

						int i;

						@Override
						protected void data(final CharBuffer data, final boolean endOfStream) throws IOException {
							while (data.hasRemaining()) {
								data.get();
							}

							if (endOfStream) {
								System.out.println(i);
							}
						}

						@Override
						protected HttpResponse buildResult() throws IOException {
							return httpResponse;
						}

						@Override
						public void failed(final Exception cause) {
							System.out.println(request + "->" + cause);
						}

						@Override
						public void releaseResources() {

						}

					}, null);
			HttpResponse response = future.get();
			System.out.println("ok kurwa: ");
		}

		System.out.println("Shutting down");
		client.close(CloseMode.GRACEFUL);
	}
}
