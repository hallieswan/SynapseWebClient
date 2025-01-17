package org.sagebionetworks.web.client.jsinterop;

import java.util.List;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.sagebionetworks.repo.model.EntityType;
import org.sagebionetworks.web.client.jsni.ReferenceJSNIObject;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class EntityFinderProps extends ReactComponentProps {

  @FunctionalInterface
  @JsFunction
  public interface OnSelectCallback {
    void run(ReferenceJSNIObject[] selected);
  }

  OnSelectCallback onSelectedChange;
  boolean selectMultiple;
  String versionSelection;

  @JsNullable
  String initialScope;

  @JsNullable
  String projectId;

  String initialContainer;

  /** The following are an array of {@link org.sagebionetworks.repo.model.EntityType} */
  @JsNullable
  String[] visibleTypesInList;

  @JsNullable
  String[] visibleTypesInTree;

  @JsNullable
  String[] selectableTypes;

  @JsNullable
  boolean treeOnly;

  @JsOverlay
  public static EntityFinderProps create(
    OnSelectCallback onSelectedChange,
    boolean selectMultiple,
    String versionSelection,
    EntityFinderScope initialScope,
    String projectId,
    String initialContainer,
    List<EntityType> visibleTypesInList,
    List<EntityType> visibleTypesInTree,
    List<EntityType> selectableTypes,
    boolean treeOnly
  ) {
    EntityFinderProps props = new EntityFinderProps();
    props.onSelectedChange = onSelectedChange;
    props.selectMultiple = selectMultiple;
    props.versionSelection = versionSelection;
    props.initialScope = initialScope.getValue();
    props.projectId = projectId;
    props.initialContainer = initialContainer;
    props.visibleTypesInList = new String[visibleTypesInList.size()];
    for (int i = 0; i < visibleTypesInList.size(); i++) {
      props.visibleTypesInList[i] = visibleTypesInList.get(i).toString();
    }
    props.visibleTypesInTree = new String[visibleTypesInTree.size()];
    for (int i = 0; i < visibleTypesInTree.size(); i++) {
      props.visibleTypesInTree[i] = visibleTypesInTree.get(i).toString();
    }
    props.selectableTypes = new String[selectableTypes.size()];
    for (int i = 0; i < selectableTypes.size(); i++) {
      props.selectableTypes[i] = selectableTypes.get(i).toString();
    }
    props.treeOnly = treeOnly;
    return props;
  }
}
