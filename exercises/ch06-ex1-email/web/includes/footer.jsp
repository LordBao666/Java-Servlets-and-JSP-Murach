
<%--use jsp tags --%>
<%--<%@ page import="java.util.GregorianCalendar, java.util.Calendar" %>--%>
<%--<%  --%>
<%--    GregorianCalendar currentDate = new GregorianCalendar();--%>
<%--    int currentYear = currentDate.get(Calendar.YEAR);--%>
<%--%>--%>
<%--<p>&copy; Copyright <%= currentYear %> Mike Murach &amp; Associates</p>--%>

<%--use el expression--%>
<%--this will find currentYear parameter  in order of page,request,session,context--%>
<p>&copy; Copyright ${currentYear} Mike Murach &amp; Associates</p>
</body>
</html>