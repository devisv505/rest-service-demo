import com.devisv.rest.AppBootstrap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class AbstractTest {

    protected static final String URL = "http://localhost:7001/";

    private static final AtomicInteger port = new AtomicInteger(7000);

    public AbstractTest() {
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {

            private final ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                try {
                    return objectMapper.readValue(s, aClass);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public String writeValue(Object o) {
                try {
                    return objectMapper.writeValueAsString(o);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    @Before
    public void start() {
        AppBootstrap.main(new String[] {String.valueOf(port.incrementAndGet())});
    }

}
