package org.sagebionetworks.web.client.jsinterop;

import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class SynapseContextJsObject {

  @JsNullable
  public String accessToken;

  public String downloadCartPageUrl;
  public boolean isInExperimentalMode;
  public boolean utcTime;
  public String appId;
  public boolean withErrorBoundary;

  @JsOverlay
  public static SynapseContextJsObject create(
    String accessToken,
    boolean isInExperimentalMode,
    boolean utcTime,
    String appId
  ) {
    SynapseContextJsObject context = new SynapseContextJsObject();
    context.accessToken = accessToken;
    context.isInExperimentalMode = isInExperimentalMode;
    context.utcTime = utcTime;
    context.downloadCartPageUrl = "/DownloadCart:0";
    context.appId = appId;
    context.withErrorBoundary = true;
    return context;
  }
}
