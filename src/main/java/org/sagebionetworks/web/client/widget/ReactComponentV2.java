package org.sagebionetworks.web.client.widget;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import jsinterop.base.JsPropertyMap;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactComponentProps;
import org.sagebionetworks.web.client.jsinterop.ReactComponentType;
import org.sagebionetworks.web.client.jsinterop.ReactDOM;
import org.sagebionetworks.web.client.jsinterop.ReactDOMRoot;
import org.sagebionetworks.web.client.jsinterop.ReactElement;

/**
 * Abstract widget that manages the lifecycle of a {@link React} component tree mounted with {@link ReactDOM}.
 * <p>
 * To use it, extend the class, supply a {@link ReactComponentType} and {@link ReactComponentProps} to the constructor,
 * and call {@link #render()} to render the React component.
 * <p>
 * This widget also manages appending child elements if the associated React component can contain children. If all
 * child widgets implement this class, then the child {@link ReactElement}s will be passed as children to the React
 * component. If any child of this component is a non-ReactComponent widget, then all child widgets (including
 * implementations of {@link ReactComponentV2}) will be injected into the node found using the component's `ref`.
 * <p>
 * The root element defaults to a `div`, but can be changed (e.g. to a `span`) using the
 * {@link #ReactComponentV2(T, P, String)} constructor.
 * <p>
 * This widget automatically unmounts the ReactComponent (if any) when this container is detached/unloaded.
 */
public abstract class ReactComponentV2<
  T extends ReactComponentType<P>, P extends ReactComponentProps
>
  extends ComplexPanel
  implements HasClickHandlers {

  private ReactDOMRoot root;
  private final ReactComponentType<P> reactComponentType;
  public P props;

  private final ArrayList<Element> nonReactChildElements = new ArrayList<>();

  public ReactComponentV2(T reactComponentType, P props) {
    this(reactComponentType, props, DivElement.TAG);
  }

  public ReactComponentV2(T reactComponentType, P props, String tag) {
    this.reactComponentType = reactComponentType;
    this.props = props;
    setElement(Document.get().createElement(tag));
  }

  /**
   * If any children of this widget do not implement this class, then we will inject them into the DOM using the React
   * component's `ref` prop. This method will update the props with a callback ref that handles appending the children
   * to the DOM node on which the ref is forwarded.
   */
  private void maybeUpdatePropsWithCallbackRef() {
    if (!this.allChildrenAreReactComponents() && getChildren().size() > 0) {
      // Create a callback ref that will allow us to inject the GWT children into the DOM
      ReactComponentProps.CallbackRef callbackRef = (Element node) -> {
        if (node != null) {
          // Once the DOM node is defined, inject each child
          getChildren()
            .forEach(w -> {
              node.appendChild(w.getElement());
              // Keep track of child elements to ensure they are removed when the component unloads or re-renders
              nonReactChildElements.add(w.getElement());
            });
        }
      };

      if (this.props == null) {
        this.props = (P) JsPropertyMap.of();
      }
      // Override the ref
      this.props.ref = callbackRef;
    }
  }

  private void createRoot() {
    if (root == null) {
      root = ReactDOM.createRoot(this.getElement());
    }
  }

  /**
   * Asynchronously (in the task queue, via setTimeout) unmounts the root and sets it to null.
   */
  private void destroyRoot() {
    // React itself may have fired this method in its render cycle. If that's the case, we cannot unmount synchronously.
    // We can asynchronously schedule unmounting the root to allow React to finish the current render cycle.
    // https://github.com/facebook/react/issues/25675
    Timer t = new Timer() {
      @Override
      public void run() {
        if (root != null) {
          root.unmount();
          root = null;
        }
      }
    };
    t.schedule(0);
  }

  /**
   * Asynchronously (in the task queue, via setTimeout) creates a root (if necessary) and renders the current reactElement.
   */
  private void createRootAndRender() {
    // This component may be a React child of another component, so retrieve the root widget that renders this component tree.
    ReactComponentV2<?, ?> componentToRender = getRootReactComponentWidget();
    // Schedule creating a root and rendering. This will necessarily run after the `destroyRoot` completes, if it was invoked.
    Timer t = new Timer() {
      @Override
      public void run() {
        componentToRender.createRoot();
        componentToRender.root.render(componentToRender.createReactElement());
      }
    };
    t.schedule(0);
  }

  private void detachNonReactChildElements() {
    if (allChildrenAreReactComponents()) {
      // No need to remove non-React child elements from this widget
      // But a descendant may contain non-React children, so recurse!
      for (Widget w : getChildren()) {
        ((ReactComponentV2<?, ?>) w).detachNonReactChildElements();
      }
    } else {
      nonReactChildElements.forEach(Element::removeFromParent);
      nonReactChildElements.clear();
    }
  }

  private ReactElement<T, P> createReactElement() {
    detachNonReactChildElements();
    maybeUpdatePropsWithCallbackRef();
    return React.createElement(
      reactComponentType,
      props,
      getChildReactElements()
    );
  }

  /**
   * @return true iff there are children, and all children are React components
   */
  private boolean allChildrenAreReactComponents() {
    boolean allChildrenAreReactComponents = getChildren().size() > 0;
    for (Widget w : getChildren()) {
      if (!(w instanceof ReactComponentV2)) {
        allChildrenAreReactComponents = false;
        break;
      }
    }
    return allChildrenAreReactComponents;
  }

  private boolean isRenderedAsReactComponentChild() {
    return (
      getParent() instanceof ReactComponentV2 &&
      ((ReactComponentV2<?, ?>) getParent()).allChildrenAreReactComponents()
    );
  }

  /**
   * This method returns the root ReactComponent widget, which is the only place where this React tree is attached to the DOM.
   */
  private ReactComponentV2<?, ?> getRootReactComponentWidget() {
    if (isRenderedAsReactComponentChild()) {
      return (
        (ReactComponentV2<?, ?>) getParent()
      ).getRootReactComponentWidget();
    } else {
      return this;
    }
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  private ReactElement<?, ?>[] getChildReactElements() {
    if (this.allChildrenAreReactComponents()) {
      // If all widget children are ReactNodes, get their ReactElements and add them as React children
      List<ReactComponentV2<?, ?>> childWidgets = new ArrayList<>();
      getChildren()
        .forEach(w -> childWidgets.add(((ReactComponentV2<?, ?>) w)));

      return childWidgets
        .stream()
        // SWC-7131: Visibility via style: {display: "none"} may not work in production
        .filter(ReactComponentV2::isVisible)
        .map(ReactComponentV2::createReactElement)
        .toArray(ReactElement<?, ?>[]::new);
    } else {
      return new ReactElement[0];
    }
  }

  public void render() {
    // This component will be rendered as a child of another React component, so destroy the root if one exists
    boolean shouldDestroyRoot = isRenderedAsReactComponentChild();

    // Schedule destroying the root, if necessary
    if (shouldDestroyRoot) {
      destroyRoot();
    }

    // Schedule rendering
    createRootAndRender();
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    // Re-render the element
    this.render();
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    this.render();
  }

  @Override
  protected void onUnload() {
    // Detach any non-React descendants that were injected into the component tree
    detachNonReactChildElements();

    destroyRoot();
  }

  /**
   * Adds a child widget.
   *
   * @param child the widget to be added
   * @throws UnsupportedOperationException if this method is not supported (most
   *           often this means that a specific overload must be called)
   */
  @Override
  public void add(Widget child) {
    // See implementation in com.google.gwt.user.client.ui.ComplexPanel

    // Detach new child
    child.removeFromParent();

    // Logical attach
    getChildren().add(child);

    // Physical attach (via React API!)
    this.render();

    // Adopt.
    adopt(child);
  }

  @Override
  public boolean remove(Widget w) {
    // See implementation in ComplexPanel

    // Validate.
    if (w.getParent() != this) {
      return false;
    }
    // Orphan.
    try {
      orphan(w);
    } finally {
      // Note - compared to ComplexPanel, we flipped logical and physical detach
      // This is because our render implementation depends on logical attachment

      // Logical detach.
      getChildren().remove(w);

      // Physical detach (via React API!)
      this.render();
    }
    return true;
  }
}
