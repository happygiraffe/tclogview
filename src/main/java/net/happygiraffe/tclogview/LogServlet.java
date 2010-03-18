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
