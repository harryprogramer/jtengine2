package thewall;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletTet extends HttpServlet {
    private Integer sharedCounter;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().log("init() called");
        sharedCounter = 0;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        getServletContext().log("service() called");
        int localCounter;
        synchronized (sharedCounter) {
            sharedCounter++;
            localCounter = sharedCounter;
        }
        response.getWriter().write("Incrementing the count to " + localCounter);  // accessing a local variable
    }

    @Override
    public void destroy() {
        getServletContext().log("destroy() called");
    }

    public static void main(String[] args) {
    }
}
