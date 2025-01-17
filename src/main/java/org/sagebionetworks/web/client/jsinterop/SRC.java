package org.sagebionetworks.web.client.jsinterop;

import com.google.gwt.core.client.JsArrayString;
import elemental2.dom.Blob;
import elemental2.promise.Promise;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.jsinterop.entity.actionmenu.EntityActionMenuPropsJsInterop;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class SRC {

  public static String SynapseReactClientVersion;

  @JsType(isNative = true)
  public static class SynapseComponents {

    public static ReactComponentType<EntityBadgeIconsProps> EntityBadgeIcons;
    public static ReactComponentType<DatasetEditorProps> DatasetItemsEditor;
    public static ReactComponentType<EntityFinderProps> EntityFinder;
    public static ReactComponentType<EntityFileBrowserProps> EntityFileBrowser;
    public static ReactComponentType<EvaluationCardProps> EvaluationCard;
    public static ReactComponentType<
      EvaluationEditorPageProps
    > EvaluationEditorPage;
    public static ReactComponentType<DownloadCartPageProps> DownloadCartPage;
    public static ReactComponentType<
      DownloadConfirmationProps
    > DownloadConfirmation;
    public static ReactComponentType<FullWidthAlertProps> FullWidthAlert;
    public static ReactComponentType<OrientationBannerProps> OrientationBanner;
    public static ReactComponentType<
      SchemaDrivenAnnotationEditorProps
    > SchemaDrivenAnnotationEditor;
    public static ReactComponentType<SynapseNavDrawerProps> SynapseNavDrawer;
    public static ReactComponentType<EmptyProps> FavoritesPage;
    public static ReactComponentType<EntityModalProps> EntityModal;
    public static ReactComponentType<IconSvgProps> IconSvg;
    public static ReactComponentType<EntityTypeIconProps> EntityTypeIcon;
    public static ReactComponentType<UserProfileLinksProps> UserProfileLinks;
    public static ReactComponentType<SkeletonButtonProps> SkeletonButton;
    public static ReactComponentType<
      QueryWrapperPlotNavProps
    > QueryWrapperPlotNav;
    public static ReactComponentType<
      StandaloneQueryWrapperProps
    > StandaloneQueryWrapper;
    public static ReactComponentType<ForumSearchProps> ForumSearch;
    public static ReactComponentType<ReviewerDashboardProps> ReviewerDashboard;
    public static ReactComponentType<ProvenanceGraphProps> ProvenanceGraph;
    public static ReactComponentType SynapseToastContainer;
    public static ReactComponentType<EmptyProps> OAuthManagement;
    public static ReactComponentType TrashCanList;
    public static ReactComponentType<SynapseHomepageV2Props> SynapseHomepageV2;
    public static ReactComponentType<SynapseFooterProps> SynapseFooter;
    public static ReactComponentType<ErrorPageProps> ErrorPage;
    public static ReactComponentType<LoginPageProps> LoginPage;
    public static ReactComponentType<HasAccessProps> HasAccess;
    public static ReactComponentType<UserCardProps> UserCard;
    public static ReactComponentType<HelpPopoverProps> HelpPopover;
    public static ReactComponentType<
      AccountLevelBadgesProps
    > AccountLevelBadges;
    public static ReactComponentType<PageProgressProps> PageProgress;
    public static ReactComponentType<
      TermsAndConditionsProps
    > TermsAndConditions;
    public static ReactComponentType<IDUReportProps> IDUReport;
    public static ReactComponentType CertificationQuiz;
    public static ReactComponentType<
      EntityPageBreadcrumbsProps
    > EntityPageBreadcrumbs;
    public static ReactComponentType<
      EntityPageTitleBarProps
    > EntityPageTitleBar;
    public static ReactComponentType<
      EntityActionMenuPropsJsInterop
    > EntityActionMenu;
    public static ReactComponentType<HtmlPreviewProps> HtmlPreview;
    public static ReactComponentType<
      CreatedByModifiedByProps
    > CreatedByModifiedBy;
    public static ReactComponentType<EmptyProps> SubscriptionPage;
    public static ReactComponentType<
      AccessRequirementListProps
    > AccessRequirementList;
    public static ReactComponentType<
      TableColumnSchemaEditorProps
    > TableColumnSchemaEditor;
    public static ReactComponentType<EntityHeaderTableProps> EntityHeaderTable;
    public static ReactComponentType<
      AvailableEvaluationQueueListProps
    > AvailableEvaluationQueueList;
    public static ReactComponentType<
      AccessRequirementRelatedProjectsListProps
    > AccessRequirementRelatedProjectsList;
    public static ReactComponentType<
      CreateTableViewWizardProps
    > CreateTableViewWizard;
    public static ReactComponentType<
      SqlDefinedTableEditorModalProps
    > SqlDefinedTableEditorModal;
    public static ReactComponentType<
      EntityViewScopeEditorModalProps
    > EntityViewScopeEditorModal;
    public static ReactComponentType<
      SubmissionViewScopeEditorModalProps
    > SubmissionViewScopeEditorModal;
    public static ReactComponentType<
      AccessRequirementAclEditorProps
    > AccessRequirementAclEditor;
    public static ReactComponentType<
      CreateOrUpdateAccessRequirementWizardProps
    > CreateOrUpdateAccessRequirementWizard;
    public static ReactComponentType<EmptyProps> GoogleAnalytics;
    public static ReactComponentType<
      CookieNotificationProps
    > CookiesNotification;
    public static ReactComponentType<
      EntityAclEditorModalProps
    > EntityAclEditorModal;
    public static ReactComponentType<SynapseChatProps> SynapseChat;
    public static ReactComponentType<SynapseHomepageV2Props> SynapsePlansPage;
    public static ReactComponentType<
      RejectProfileValidationRequestModalProps
    > RejectProfileValidationRequestModal;
    public static ReactComponentType<
      GovernanceMarkdownGithubProps
    > GovernanceMarkdownGithub;
    public static ReactComponentType<
      ProjectDataAvailabilityProps
    > ProjectDataAvailability;
    public static ReactComponentType<EntityUploadModalProps> EntityUploadModal;

    /**
     * Pushes a global toast message. In SWC, you should use {@link DisplayUtils#notify}, rather than calling this method directly.
     * @param message
     * @param variant
     * @param options
     */
    public static native void displayToast(
      String message,
      String variant,
      @JsNullable ToastMessageOptions options
    );

    public static native CookiePreference getCurrentCookiePreferences();
  }

  @JsType(isNative = true)
  public static class SynapseContext {

    /* We use FullContextProvider because it will provide the SynapseContext, react-query QueryContext, and MUI Theme
     context for all React trees that we render */
    public static ReactComponentType<
      SynapseReactClientFullContextProviderProps
    > FullContextProvider;
  }

  @JsType(isNative = true)
  public static class SynapseConstants {

    public static JsArrayString PERSISTENT_LOCAL_STORAGE_KEYS;
  }

  @JsType(isNative = true)
  public static class SynapseClient {

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class ProgressCallback {

      public double value;
      public double total;
    }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class FileUploadComplete {

      public String fileHandleId;
      public String fileName;
    }

    @FunctionalInterface
    @JsFunction
    public interface Progress {
      void onProgress(ProgressCallback callback);
    }

    @FunctionalInterface
    @JsFunction
    public interface IsCancelled {
      boolean isCancelled();
    }

    public static native Promise<FileUploadComplete> uploadFile(
      String accessToken,
      String filename,
      Blob file,
      int storageLocationId,
      String contentType,
      Progress progressCallback,
      IsCancelled getIsCancelled
    );
  }
}
