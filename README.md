tclogview
=========

This is a simple webapp that allows viewing log files from inside tomcat.  To use it:

 * Build with “mvn package”
 * Copy target/tclogview.war into your `$CATALINA_BASE/webapps` directory.
 * Ensure that you have a user with the *tclogview* role in `$CATALINA_BASE/conf/tomcat-users.xml`.  e.g.
   <pre>
    &lt;role rolename="tclogview"/&gt;
    &lt;user username="chip" password="lumberjack" roles="tclogview"/&gt;
   </pre>

[Original blog post](http://happygiraffe.net/blog/2008/12/26/tclogview/)

This code is licensed under the new BSD licence.  See LICENCE.txt for details.
