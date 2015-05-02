<%@ include file="/include.jsp" %>
<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<jsp:useBean id="serverEnabled" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="isGuestEnabled" type="java.lang.Boolean" scope="request"/>
<jsp:useBean id="publicFeedUrl" scope="request" type="java.lang.String" />
<jsp:useBean id="actualServerUrl" scope="request" type="java.lang.String" />
<jsp:useBean id="imagesUrl" type="java.lang.String"  scope="request"/>
<jsp:useBean id="pluginVersion" type="java.lang.String"  scope="request"/>

<div id="vsixSettingsTabContainer" style="padding: 0 1em 1.5em 0; display: block;" class="simpleTabs">
  <div id="section">
    <div class="my-header">
      <img src="<c:url value='${imagesUrl}/vsix.png'/>" height="64px"/>
      <h2>Visual Studio Extensions Gallery</h2>
      <!--<span class="smallNote">v${pluginVersion}</span>-->
    </div>
    <span class="subtitle">By <a href="https://github.com/hmemcpy/teamcity-vsix-gallery">Igal Tabachnik</a></span>
  </div>

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
            <div id="vsixtFeedNotice" style="padding-top: 1em;">
              <div class="attentionComment">
                Visual Studio does not support prompting for credentials, enable guest account to activate the public feed.
              </div>
            </div>
          </c:when>
          <c:otherwise>
            <c:set var="url"><c:url value="${actualServerUrl}${publicFeedUrl}"/></c:set>
            <div><a href="${url}" style="font-weight: bold;">${url}</a></div>
            <span class="smallNote">Lists all packages from builds available for the guest<bs:help file="Guest+User"/> user</span>
          </c:otherwise>
        </c:choose>
        </td>
      </tr>
    </table>
  </c:if>

  <c:choose>
    <c:when test="${isGuestEnabled}">
      <div id="content">
        <p>Add the feed URL to the <strong>Extensions and Updates</strong> page in Visual Studio <strong>Tools - Options</strong> dialog:</p>
        <img src="<c:url value='${imagesUrl}/extensions.png'/>" />
      </div>
    </c:when>
  </c:choose>

  </bs:refreshable>
</div>