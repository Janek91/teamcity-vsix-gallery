<%@ include file="/include.jsp" %>
<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<jsp:useBean id="serverEnabled" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="isGuestEnabled" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="publicFeedUrl" scope="request" type="java.lang.String" />
<jsp:useBean id="actualServerUrl" scope="request" type="java.lang.String" />

<div id="vsixSettingsTabContainer" style="padding: 0 1em 1.5em 0; display: block;" class="simpleTabs">
<div class="clr"></div>
<div style="padding-bottom: 1em"></div>

<h2>Visual Studio Extensions Gallery</h2>
  <bs:refreshable containerId="feedEnableDisable" pageUrl="#">
    <!--<div data-url="#">
      VSIX Server is
      <c:choose>
        <c:when test="${serverEnabled}">
          <strong>enabled</strong> <input type="button" class="btn btn_mini" value="Disable" onclick="return BS.NuGet.FeedServer.disableFeedServer(this);" />
        </c:when>
        <c:otherwise>
          <strong>disabled</strong> <input type="button" class="btn btn_mini" value="Enable" onclick="return BS.NuGet.FeedServer.enableFeedServer(this);" />
        </c:otherwise>
      </c:choose>
      <span><%--used for loading icon--%></span>
    </div>-->

  <c:if test="${serverEnabled}">
    <table class="runnerFormTable nugetSettings">
      <tr>
        <th>Public Feed URL:</th>
        <td>
        <c:choose>
          <c:when test="${not isGuestEnabled}">
            <div>Not available.</div>
            <span class="smallNote">
              Enable the guest user <bs:help file="Guest+User"/> login in
              TeamCity <a href="<c:url value="/admin/admin.html?item=auth"/>">Authentication</a> settings
              for public feed to work.
            </span>
          </c:when>
          <c:otherwise>
            <c:set var="url"><c:url value="${actualServerUrl}${publicFeedUrl}"/></c:set>
            <div><a href="${url}">${url}</a></div>
            <span class="smallNote">Lists all packages from builds available for the guest<bs:help file="Guest+User"/> user</span>
          </c:otherwise>
        </c:choose>
        </td>
      </tr>
    </table>
  </c:if>
  </bs:refreshable>
</div>