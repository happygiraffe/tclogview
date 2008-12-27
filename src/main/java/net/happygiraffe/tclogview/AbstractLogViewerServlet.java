/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.happygiraffe.tclogview;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dom
 */
public class AbstractLogViewerServlet extends HttpServlet {

    public FileBean getLogDir() {
        return new FileBean(System.getProperty("catalina.base"), "logs");
    }

    public String getRequestInfo(HttpServletRequest req) {
        return "contextPath=" + req.getContextPath() + "; servletPath=" +
                req.getServletPath() + "; pathInfo=" + req.getPathInfo();
    }
}
