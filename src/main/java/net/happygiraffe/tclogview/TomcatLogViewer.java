/*
 * Copyright (C) 2008 by Dominic Mitchell
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

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
