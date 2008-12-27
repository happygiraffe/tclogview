package net.happygiraffe.tclogview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Serve up an individual log file.
 * @author dom
 */
public class LogServlet extends AbstractLogViewerServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        serveLogFile(getLogFile(req), req, resp);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return getLogFile(req).lastModified();
    }

    private File getLogFile(HttpServletRequest req) {
        return new File(getLogDir().getFile(), getRequestedLogFile(req));
    }

    private String getRequestedLogFile(HttpServletRequest req) {
        return req.getPathInfo().substring(1);
    }

    private void serveLogFile(File file,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        // XXX What if it's more than 2Gb?
        resp.setContentLength((int) file.length());

        PrintWriter w = resp.getWriter();
        // XXX If it's not valid UTF-8, we may emit fewer bytes than we said.
        InputStreamReader isr = new InputStreamReader(new BufferedInputStream(
                new FileInputStream(file)), "UTF-8");
        int c;
        while ((c = isr.read()) != -1) {
            w.print((char) c);
        }
    }
}
