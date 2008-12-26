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

        String servletPath = req.getServletPath();
        if ("/".equals(servletPath)) {
            serveIndexPage(logDir, req, resp);
        } else {
            final File logFile = getLogFile(logDir, servletPath.substring(1));
            serveLogFile(logFile, req, resp);
        }
    }

    private FileBean getLogDir() {
        return new FileBean(System.getProperty("catalina.base"), "logs");
    }

    private File getLogFile(FileBean logDir, String fileName) {
        File file = new File(logDir.getFile(), fileName);
        return file;
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
        PrintWriter w = resp.getWriter();
        InputStreamReader isr = new InputStreamReader(new BufferedInputStream(
                new FileInputStream(file)), "UTF-8");
        int c;
        while ((c = isr.read()) != -1) {
            w.print((char)c);
        }
    }
}
