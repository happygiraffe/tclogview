<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Log files</title>
    </head>
    <body>
        <h1>Log files</h1>
        <p>In directory <tt>${logDir}</tt>:</p>

        <table>
            <tr>
                <th>Name</th>
                <th>Size</th>
                <th>Last modified</th>
            </tr>
            <c:forEach items="${logFiles}" var="logFile">
                <tr>
                    <td class="name">
                        <a href="${contextPath}/${logFile.name}">
                            <code>${logFile.name}</code>
                        </a>
                    </td>
                    <td class="size">${logFile.length}</td>
                    <td class="mtime">${logFile.lastModifiedIso8601}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
