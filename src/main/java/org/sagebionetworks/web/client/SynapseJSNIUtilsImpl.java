package org.sagebionetworks.web.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.xhr.client.XMLHttpRequest;
import elemental2.dom.Blob;
import org.sagebionetworks.repo.model.file.FileHandleAssociateType;
import org.sagebionetworks.web.client.callback.MD5Callback;
import org.sagebionetworks.web.client.jsinterop.SRC;
import org.sagebionetworks.web.client.widget.provenance.nchart.LayoutResult;
import org.sagebionetworks.web.client.widget.provenance.nchart.LayoutResultJso;
import org.sagebionetworks.web.client.widget.provenance.nchart.NChartCharacters;
import org.sagebionetworks.web.client.widget.provenance.nchart.NChartLayersArray;
import org.sagebionetworks.web.shared.WebConstants;

public class SynapseJSNIUtilsImpl implements SynapseJSNIUtils {

  private static ProgressCallback progressCallback;

  Html5Historian html5Historian = new Html5Historian();

  @Override
  public String getCurrentHistoryToken() {
    return html5Historian.getToken();
  }

  @Override
  public void highlightCodeBlocks() {
    _highlightCodeBlocks();
  }

  public static native void _highlightCodeBlocks() /*-{
		try {
			$wnd.jQuery('pre code').each(function(i, e) {
				$wnd.hljs.highlightBlock(e)
			});
		} catch (err) {
			console.error(err);
		}
	}-*/;

  @Override
  public void loadTableSorters() {
    _tablesorter();
  }

  private static native void _tablesorter() /*-{
		try {
			$wnd.jQuery('table.tablesorter').tablesorter();
		} catch (err) {
			console.error(err);
		}
	}-*/;

  @Override
  public String getBaseFileHandleUrl() {
    return GWTWrapperImpl.getRealGWTModuleBaseURL() + "filehandle";
  }

  @Override
  public String getFileHandleAssociationUrl(
    String objectId,
    FileHandleAssociateType objectType,
    String fileHandleId
  ) {
    return (
      GWTWrapperImpl.getRealGWTModuleBaseURL() +
      WebConstants.FILE_HANDLE_ASSOCIATION_SERVLET +
      "?" +
      WebConstants.ASSOCIATED_OBJECT_ID_PARAM_KEY +
      "=" +
      objectId +
      "&" +
      WebConstants.ASSOCIATED_OBJECT_TYPE_PARAM_KEY +
      "=" +
      objectType.toString() +
      "&" +
      WebConstants.FILE_HANDLE_ID_PARAM_KEY +
      "=" +
      fileHandleId
    );
  }

  /**
   * Create the url to the raw file handle id (must be the owner to access)
   *
   * @param rawFileHandleId
   * @return
   */
  @Override
  public String getRawFileHandleUrl(String fileHandleId) {
    return (
      GWTWrapperImpl.getRealGWTModuleBaseURL() +
      WebConstants.FILE_HANDLE_ASSOCIATION_SERVLET +
      "?" +
      WebConstants.FILE_HANDLE_ID_PARAM_KEY +
      "=" +
      fileHandleId
    );
  }

  @Override
  public String getAccessTokenCookieUrl() {
    return (
      GWTWrapperImpl.getRealGWTModuleBaseURL() +
      WebConstants.SESSION_COOKIE_SERVLET
    );
  }

  @Override
  public String getVersionsServletUrl() {
    return (
      GWTWrapperImpl.getRealGWTModuleBaseURL() + WebConstants.VERSIONS_SERVLET
    );
  }

  @Override
  public String getAppConfigServletUrl() {
    return (
      GWTWrapperImpl.getRealGWTModuleBaseURL() + WebConstants.APPCONFIG_SERVLET
    );
  }

  @Override
  public int randomNextInt() {
    return Random.nextInt();
  }

  @Override
  public String getLocationPath() {
    return Location.getPath();
  }

  @Override
  public String getLocationQueryString() {
    return Location.getQueryString();
  }

  @Override
  public LayoutResult nChartlayout(
    NChartLayersArray layers,
    NChartCharacters characters
  ) {
    return _nChartlayout(layers, characters);
  }

  private static final native LayoutResultJso _nChartlayout(
    NChartLayersArray layers,
    NChartCharacters characters
  ) /*-{
		var layoutResult = {};
		try {
			var debug = {
				'features' : [ 'nodes' ],
				'wireframe' : true
			};
			var conf = {
				'subnode_spacing' : 40,
				'group_styles' : {
					'pov' : {
						'stroke-width' : 3
					}
				},
				'debug' : debug
			};
			var chart = new $wnd.NChart(characters, layers, conf).calc().plot();

			// convert graph into LayoutResult
			var ncGraph = chart.graph;
			for (var i = 0; i < ncGraph.layers.length; i++) {
				var ncLayer = ncGraph.layers[i];
				for (var j = 0; j < ncLayer.nodes.length; j++) {
					var ncNode = ncLayer.nodes[j];
					var provGraphNodeId = ncNode.event;
					var xypoint = {
						'x' : ncNode.x,
						'y' : ncNode.y
					};
					if (!(provGraphNodeId in layoutResult)) {
						layoutResult[provGraphNodeId] = [];
					}
					layoutResult[provGraphNodeId].push(xypoint);
				}
			}
		} catch (err) {
			console.error(err);
		}
		return layoutResult;
	}-*/;

  @Override
  public void setPageTitle(String newTitle) {
    if (Document.get() != null) {
      Document.get().setTitle(newTitle);
    }
  }

  @Override
  public void setPageDescription(String newDescription) {
    if (Document.get() != null) {
      NodeList<Element> tags = Document.get().getElementsByTagName("meta");
      for (int i = 0; i < tags.getLength(); i++) {
        MetaElement metaTag = ((MetaElement) tags.getItem(i));
        if (metaTag.getName().equals("description")) {
          metaTag.setContent(newDescription); // doesn't seem to work
          break;
        }
      }
    }
  }

  @Override
  public void uploadFileChunk(
    String contentType,
    JavaScriptObject blob,
    Long startByte,
    Long endByte,
    String url,
    XMLHttpRequest xhr,
    ProgressCallback callback
  ) {
    SynapseJSNIUtilsImpl.progressCallback = callback;
    _directUploadBlob(contentType, blob, startByte, endByte, url, xhr);
  }

  private static final native void _directUploadBlob(
    String contentType,
    JavaScriptObject fileToUpload,
    Long startByte,
    Long endByte,
    String url,
    XMLHttpRequest xhr
  ) /*-{
		var start = parseInt(startByte) || 0;
		var end = parseInt(endByte) || fileToUpload.size - 1;
		var fileSliceToUpload;
		//in versions later than Firefox 13 and Chrome 21, Blob.slice() is not prefixed (and the vendor prefixed methods are deprecated)
		if (fileToUpload.slice) {
			fileSliceToUpload = fileToUpload.slice(start, end + 1, contentType);
		} else if (fileToUpload.mozSlice) {
			fileSliceToUpload = fileToUpload.mozSlice(start, end + 1, contentType);
		} else if (fileToUpload.webkitSlice) {
			fileSliceToUpload = fileToUpload.webkitSlice(start, end + 1, contentType);
		} else {
			throw new Error("Unable to slice file.");
		}
		xhr.upload.onprogress = $entry(@org.sagebionetworks.web.client.SynapseJSNIUtilsImpl::updateProgress(Lcom/google/gwt/core/client/JavaScriptObject;));
		xhr.open('PUT', url, true);
		//explicitly set content type
		xhr.setRequestHeader('Content-type', contentType);
		xhr.send(fileSliceToUpload);
	}-*/;

  public static void updateProgress(JavaScriptObject evt) {
    if (SynapseJSNIUtilsImpl.progressCallback != null) {
      SynapseJSNIUtilsImpl.progressCallback.updateProgress(
        _getLoaded(evt),
        _getTotal(evt)
      );
    }
  }

  private static final native double _getLoaded(JavaScriptObject evt) /*-{
		if (evt.lengthComputable) {
			return evt.loaded;
		}
		return 0;
	}-*/;

  private static final native double _getTotal(JavaScriptObject evt) /*-{
		if (evt.lengthComputable) {
			return evt.total;
		}
		return 0;
	}-*/;

  public boolean isElementExists(String elementId) {
    return Document.get().getElementById(elementId) != null;
  }

  /**
   * Return the last modified time of the File (in milliseconds since the UNIX epoch).  -1 if undefined.
   */
  @Override
  public long getLastModified(JavaScriptObject blob) {
    return new Double(_getLastModified(blob)).longValue();
  }

  private static final native double _getLastModified(
    JavaScriptObject file
  ) /*-{
		if (file.lastModified) {
			return file.lastModified;
		}
		return -1;
	}-*/;

  /**
   * Using SparkMD5 (https://github.com/satazor/SparkMD5) to (progressively by slicing the file)
   * calculate the md5.
   */
  @Override
  public void getFileMd5(Blob file, MD5Callback md5Callback) {
    _getFileMd5(file, md5Callback);
  }

  private static final native void _getFileMd5(
    Blob file,
    MD5Callback md5Callback
  ) /*-{
		if ($wnd.Worker) {
			if (!$wnd.calculateFileMd5Worker) {
			  $wnd.calculateFileMd5Worker = new $wnd.Worker("/workers/calculateFileMd5Worker.js");
			};
			$wnd.calculateFileMd5Worker.onmessage = function(event) {
				md5Callback.@org.sagebionetworks.web.client.callback.MD5Callback::setMD5(Ljava/lang/String;)(event.data);
			};
			$wnd.calculateFileMd5Worker.postMessage(file);
	  } else {
			md5Callback.@org.sagebionetworks.web.client.callback.MD5Callback::setMD5(Ljava/lang/String;)(null);
  	}
	}-*/;

  /**
   * Using SparkMD5 (https://github.com/satazor/SparkMD5) to calculate the md5 of part of a file.
   */
  @Override
  public void getFilePartMd5(
    JavaScriptObject blob,
    int currentChunk,
    Long chunkSize,
    MD5Callback md5Callback
  ) {
    _getFilePartMd5(blob, currentChunk, chunkSize.doubleValue(), md5Callback);
  }

  private static final native void _getFilePartMd5(
    JavaScriptObject file,
    int currentChunk,
    double chunkSize,
    MD5Callback md5Callback
  ) /*-{
		if ($wnd.Worker) {
			if (!$wnd.calculateFilePartMd5Worker) {
			  $wnd.calculateFilePartMd5Worker = new $wnd.Worker("/workers/calculateFilePartMd5Worker.js");
			};
			$wnd.calculateFilePartMd5Worker.onmessage = function(event) {
				md5Callback.@org.sagebionetworks.web.client.callback.MD5Callback:: setMD5(Ljava/lang/String;) (event.data);
			};
			$wnd.calculateFilePartMd5Worker.postMessage({
				file: file,
				currentChunk: currentChunk,
				chunkSize: chunkSize
			});
	  } else {
			md5Callback.@org.sagebionetworks.web.client.callback.MD5Callback:: setMD5(Ljava/lang/String;) (null);
  	}
	}-*/;

  @Override
  public boolean isFileAPISupported() {
    return _isFileAPISupported();
  }

  private static final native boolean _isFileAPISupported() /*-{
		return ($wnd.File && $wnd.FileReader && $wnd.FileList && $wnd.Blob);
	}-*/;

  /**
   * Get the url to a local file blob.
   */
  @Override
  public String getFileUrl(String fileFieldId) {
    return _getFileUrl(fileFieldId);
  }

  private static final native String _getFileUrl(String fileFieldId) /*-{
		try {
			var fileToUploadElement = $doc.getElementById(fileFieldId);
			var file = fileToUploadElement.files[0];
			return URL.createObjectURL(file);
		} catch (err) {
			return null;
		}
	}-*/;

  @Override
  public void consoleLog(String message) {
    _consoleLog(message);
  }

  public static final native void _consoleLog(String message) /*-{
		console.log(message);
	}-*/;

  @Override
  public void consoleError(String message) {
    _consoleError(message);
  }

  @Override
  public void consoleError(Throwable t) {
    _consoleError(t);
  }

  public static final native void _consoleError(Object ob) /*-{
		console.error(ob);
	}-*/;

  public void processMath(Element element) {
    // remove \(, \), \[, \]
    String tex = element.getInnerText();
    boolean isCenterDiv = tex.contains("\\[") || tex.contains("\\begin{align");
    tex = tex.replace("\\(", "").replace("\\)", "");
    tex = tex.replace("\\[", "").replace("\\]", "");
    tex = tex.replace("{align}", "{aligned}");
    String html = _processMath(tex, isCenterDiv);
    if (isCenterDiv) {
      html = "<div class=\"center\">" + html + "</div>";
    }

    element.setInnerHTML(html);
  }

  private static final native String _processMath(
    String tex,
    boolean displayModeBoolean
  ) /*-{
		try {
			return $wnd.katex.renderToString(tex, {
				displayMode : displayModeBoolean
			});
		} catch (err) {
			console.error(err);
		}
	}-*/;

  private static final native void _scrollIntoView(Element el) /*-{
		try {
			el.scrollIntoView({
				behavior : 'smooth'
			});
		} catch (err) {
			console.error(err);
		}
	}-*/;

  @Override
  public void scrollIntoView(Element el) {
    _scrollIntoView(el);
  }

  @Override
  public void loadCss(final String url) {
    final LinkElement link = Document.get().createLinkElement();
    link.setRel("stylesheet");
    link.setHref(url);
    _nativeAttachToHead(link);
  }

  @Override
  public String[] getSrcPersistentLocalStorageKeys() {
    JsArrayString jsArray = SRC.SynapseConstants.PERSISTENT_LOCAL_STORAGE_KEYS;
    String[] javaArray = new String[jsArray.length()];
    for (int i = 0; i < jsArray.length(); i++) {
      javaArray[i] = jsArray.get(i);
    }
    return javaArray;
  }

  /**
   * Attach element to head
   */
  protected static native void _nativeAttachToHead(
    JavaScriptObject scriptElement
  ) /*-{
		$doc.getElementsByTagName("head")[0].appendChild(scriptElement);
	}-*/;

  @Override
  public void initOnPopStateHandler() {
    _initOnPopStateHandler();
  }

  private static native void _initOnPopStateHandler() /*-{
		// reload the page on pop state
		//we set the source property of the state if we used pushState or replaceState
		$wnd.addEventListener("popstate", function(event) {
			var stateObj = event.state;
			if (typeof stateObj !== "undefined" && stateObj !== null
					&& typeof stateObj.source !== "undefined") {
				$wnd.location.reload(false);
			}
		});
	}-*/;

  @Override
  public boolean elementSupportsAttribute(Element el, String attribute) {
    return _elementSupportsAttribute(el.getTagName(), attribute);
  }

  private static final native boolean _elementSupportsAttribute(
    String tagName,
    String attribute
  ) /*-{
		return attribute in $doc.createElement(tagName);
	}-*/;

  boolean isFilterXssInitialized = false;

  @Override
  public String sanitizeHtml(String html) {
    if (!isFilterXssInitialized) {
      // init
      isFilterXssInitialized = initFilterXss();
    }
    return _sanitizeHtml(html);
  }

  private static final native boolean initFilterXss() /*-{
		try {
			var options = $wnd.SRC.xssOptions;
			$wnd.xss = new $wnd.filterXSS.FilterXSS(options)
			return true
		} catch (err) {
			console.error(err);
			return false;
		}
	}-*/;

  private static final native String _sanitizeHtml(String html) /*-{
		try {
			return $wnd.xss.process(html);
		} catch (err) {
			console.error(err);
		}
	}-*/;

  @Override
  public String getCurrentURL() {
    return Location.getHref();
  }

  @Override
  public String getCurrentHostName() {
    return Location.getHostName();
  }

  @Override
  public String getProtocol(String url) {
    return _getProtocol(url);
  }

  private static final native String _getProtocol(String url) /*-{
		var parser = $doc.createElement('a');
		parser.href = url;
		var v = parser.protocol; // for example, "https:"
		parser = null;
		return v;
	}-*/;

  @Override
  public String getHost(String url) {
    return _getHost(url);
  }

  private static final native String _getHost(String url) /*-{
		var parser = $doc.createElement('a');
		parser.href = url;
		var v = parser.host; // for example, "test.com:8080"
		parser = null;
		return v;
	}-*/;

  @Override
  public String getHostname(String url) {
    return _getHostname(url);
  }

  private static final native String _getHostname(String url) /*-{
		var parser = $doc.createElement('a');
		parser.href = url;
		var v = parser.hostname; // for example, "test.com"
		parser = null;
		return v;
	}-*/;

  @Override
  public String getPort(String url) {
    return _getPort(url);
  }

  private static final native String _getPort(String url) /*-{
		var parser = $doc.createElement('a');
		parser.href = url;
		var v = parser.port; // for example, "8080"
		parser = null;
		return v;
	}-*/;

  @Override
  public String getPathname(String url) {
    return _getPathname(url);
  }

  private static final native String _getPathname(String url) /*-{
		var parser = $doc.createElement('a');
		parser.href = url;
		var v = parser.pathname; // for example, "/resources/images/" 
		parser = null;
		return v;
	}-*/;

  @Override
  public void copyToClipboard() {
    try {
      _copyToClipboard();
      DisplayUtils.showInfo("Copied to clipboard");
    } catch (Throwable t) {
      consoleError(t.getMessage());
    }
  }

  private static final native String _copyToClipboard() /*-{
		$doc.execCommand('copy');
	}-*/;

  @Override
  public String getCdnEndpoint() {
    return _getCdnEndpoint();
  }

  private static final native String _getCdnEndpoint() /*-{
		// initialized in Portal.html
		return $wnd.cdnEndpoint;
	}-*/;

  @Override
  public void setIsInnerProgrammaticHistoryChange() {
    _setIsInnerProgrammaticHistoryChange();
  }

  public static native void _setIsInnerProgrammaticHistoryChange() /*-{
		// see back-forward-nav-handler.js
		// We don't want the handler to reload the page if we are programmatically changing the history.
		// Reload when the user manually clicks the browser back/forward button.
		$wnd.innerDocClick = true;
	}-*/;

  @Override
  public void showJiraIssueCollector(
    String issueSummary,
    String issueDescription,
    String jiraIssueCollectorURL,
    String principalId,
    String userDisplayName,
    String userEmailAddress,
    String synapseDataObjectId,
    String componentID,
    String accessRequirementId,
    String issuePriority
  ) {
    _showJiraIssueCollector(
      issueSummary,
      issueDescription,
      jiraIssueCollectorURL,
      principalId,
      userDisplayName,
      userEmailAddress,
      synapseDataObjectId,
      componentID,
      accessRequirementId,
      issuePriority
    );
  }

  public static native void _showJiraIssueCollector(
    String issueSummary,
    String issueDescription,
    String jiraIssueCollectorURL,
    String principalId,
    String userDisplayName,
    String userEmailAddress,
    String synapseDataObjectId,
    String componentID,
    String accessRequirementId,
    String issuePriority
  ) /*-{
		try {
			// Requires jQuery!
			$wnd.jQuery.ajax({
				url : jiraIssueCollectorURL,
				type : "get",
				cache : true,
				dataType : "script"
			});

			$wnd.ATL_JQ_PAGE_PROPS = {
				"triggerFunction" : function(showCollectorDialog) {
					showCollectorDialog();
				},

				"fieldValues" : {
					summary : issueSummary,
					description : issueDescription,
					priority : issuePriority,
					customfield_10840 : userEmailAddress,
					email : userEmailAddress,
					customfield_10841 : accessRequirementId,
					customfield_10740 : principalId,
					customfield_10741 : userDisplayName,
					customfield_10742 : synapseDataObjectId,
					fullname : userDisplayName,
					components : componentID
				// Component ID of the component added to the Jira Governance Project
				}
			};
		} catch (err) {
			console.error(err);
		}
	}-*/;

  @Override
  public String setHash(String hash) {
    return _setHash(hash);
  }

  private static final native String _setHash(String hash) /*-{
      $wnd.history.pushState('', '', hash)
    }-*/;

  @Override
  public String getHash() {
    return _getHash();
  }

  private static final native String _getHash() /*-{
      return $wnd.location.hash
    }-*/;
}
