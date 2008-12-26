package net.happygiraffe.tclogview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A viewer for tomcat log files.
 * 
 * @author dom
 */
public class TomcatLogViewer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("contextPath", req.getContextPath());

        FileBean logDir = getLogDir();
        req.setAttribute("logDir", logDir);

        if (isIndex(req)) {
            serveIndexPage(logDir, req, resp);
        } else {
            serveLogFile(getLogFile(req), req, resp);
        }
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        if (isIndex(req)) {
            return getLogDir().getFile().lastModified();
        } else {
            return getLogFile(req).lastModified();
        }
    }

    private FileBean getLogDir() {
        return new FileBean(System.getProperty("catalina.base"), "logs");
    }

    private File getLogFile(HttpServletRequest req) {
        return new File(getLogDir().getFile(), getRequestedLogFile(req));
    }

    private List<FileBean> getLogFiles(FileBean logDir) {
        List<FileBean> logFiles = logDir.listFiles();
        // Sort in reverse date order.
        Collections.sort(logFiles, new Comparator<FileBean>() {

            public int compare(FileBean f1, FileBean f2) {
                return f2.getLastModified().compareTo(f1.getLastModified());
            }
        });
        return logFiles;
    }

    private String getRequestedLogFile(HttpServletRequest req) {
        return req.getServletPath().substring(1);
    }

    private boolean isIndex(HttpServletRequest req) {
        return "/".equals(req.getServletPath());
    }

    private void serveIndexPage(FileBean logDir, HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        List<FileBean> logFiles = getLogFiles(logDir);
        req.setAttribute("logFiles", logFiles);
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
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
