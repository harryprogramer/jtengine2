import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

public class XmlTest {
    static class Test{
        public int x = 0;
        public int y = 1;
    }

    @org.junit.jupiter.api.Test
    void test() throws JsonProcessingException {
        XmlMapper mapper = new XmlMapper();
        String xml = mapper.writeValueAsString(new Test());
        System.out.println(xml);
        Test test = mapper.readValue(xml, Test.class);
        System.out.println(test.x);
    }
}
