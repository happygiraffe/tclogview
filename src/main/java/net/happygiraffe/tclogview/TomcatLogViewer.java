package net.happygiraffe.tclogview;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A viewer for tomcat log files.
 * 
 * @author dom
 */
public class TomcatLogViewer extends AbstractLogViewerServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("contextPath", req.getContextPath());

        FileBean logDir = getLogDir();
        req.setAttribute("logDir", logDir);

        serveIndexPage(logDir, req, resp);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return getLogDir().getFile().lastModified();
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

}
