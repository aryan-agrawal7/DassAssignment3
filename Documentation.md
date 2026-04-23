# IMS App - Complete Codebase Documentation

## 1. Configuration and Build System
- **Build Tool**: Gradle with Kotlin DSL (`.kts`).
- **Target Platform**: The intended SDK is Android 16 (SDK 36), but the minimum SDK Android 7.0 (SDK 24) (thus allowing some backward compatibility)
- **Core Dependencies**: 
    - **Jetpack Compose BOM**: Managed UI dependency versions.
    - **Navigation Compose**: Type-safe routing using `NavHost`.
    - **Material 3**: Modern design system implementation.
    - **Material Icons Extended**: Full icon set for cross-role actions.

## 2. High level App Structure
- `MainActivity.kt` initializes the `RootNavigation`.
- `RootNavigation.kt` handles top-level auth-swaps (Login -> Role-specific Main Screens).
-  Role-specific dashboard screens use nested `NavHost` to manage internal sub-routes (Settings, Forms, Approvals) while persisting common  bottom bars.

## 3. Local Data Storage (`MockDatabase.kt`)
- The whole system data is centralized in RAM. It is purely in memory and resets on process death so clean testing can be done.
- The main collections are `users`, `leaves`, `news`, `payslips`
- `MutableStateFlow` is used for all collections. UI components use `collectAsState()` to refresh the view instantly on data mutation.
- Thread safe mutation of in-memory lists using the `.update { ... }` block.

## 4. UI 
- **Theme (`ui/theme/`)**: Custom Material 3 theme.
- **Color Palette**: Dark-first aesthetic (`BackgroundDark`, `CardDark`) with `TealAccent` for primary actions and `TextGray` for secondary metadata.
- **Reusable Stubs**: Standardized card components to ensure consistent layout across different roles.

## 5. Implemented components workflow logic

### HR Module
#### A. Customizable Dynamic Form
- Forms for Employee Admission and Payroll are customisable (as per the requirement). 
- The system iterates over a `Template` list of `PayrollField` or `AdmissionField` objects, dynamically rendering TextFields, Dropdowns, or Numeric inputs based on the field type.
- Strict `isRequired` checks are performed by filtering the template and matching against a local `Map<String, String>` of user inputs.
#### B. Two-Stage Leave Approval
- Follow the current IMS structure of dual approval.
- Approval 1 HR: HR views pending leaves in list. Then they can approve/reject Status updated to `Pending` with `hrApprovalStatus` set to the chosen value
- Approval 2 Admin: Leaves with `hrApprovalStatus` approved are shown to Admin for final review. Admin approves/rejects and sets the final `status`.
- Employee sees final decision in their dashboard instantly. 

#### C. Single Click Payslip Approval
- Employee submits payslip request using HR-defined payroll template. Data filled by the employee is stored in `MockDatabase.payslips`
- HR views pending payslips, reviews submitted data and can approve/reject
- Employee's payslip record updated with final status and UI reflects changes instantly.


### News Management
- **RTF Support**: Supports bold (`**`), italic (`*`), and underline (`__`) markers, parsed into `AnnotatedString` for rich display.
- **Ordering**: News items use an `order` long integer; editing an item increments its `order` to "pin" it to the top of the feed.

#### A. News Feed Display
- All roles have a uniform news feed on their respective dashboards where the news is sorted in descending order by date (newest first).
- Like count and comment count displayed with real-time updates
- News items fetched from `MockDatabase.news`.
- Users can:
     - **Like/Unlike**: Click heart icon → toggles like status for the UI. and increments the like count (which is common for all users so it updates globally)
     - **View Details**: Navigates to the `InstituteNewsDetailScreen` in which the entire article can be viewed.
     - **Comment**: Add comment which adds the message to the `MockDatabase.news` which is used globally so all users can view the comment. 

#### B. News Creation (Admin Only)
- Admin enters title, content (with RTF markers), and uploads image
- On create: New `News` object is created with and current timestamp is added for sorting). StateFlow notifies all screens so all users see the new news object instantly.

#### C. News Editing (Admin Only)
- Admin selects the pencil icon on the news list page.
- The news creation page is loaded with the header title changed and the fields filled with title, content, image from the `MockDatabase News object`. 
- On save: timestamp is updated to current timestamp. Feed automatically reorders via StateFlow update and everyone now sees the updated news as the top news.

#### D. Comments System
- User enters comment text in the detailed news screen. On submit a `Comment` object is created ascociated with the `News` object currently shown on the screen.
- A separate Stateflow ensures UI updates instantly showing the comment to all users.

## 6. Assumptions & Constraints
- As the system uses a local storage instead of an actual database, the system doesnot check for uniqueness of the "Email Address" during admission. It is assumed that no duplicate credentials are created.
- **Authentication for stubbed users**: As this is a stubbed app there is a fixed list of users with passwords standardized to `123`.
- All modules which weren't chosen to be implemented are either shown as `coming soon` if they are not connected to an implemented module or stubbed completely if connected to an implemented module example the employee details is connected to the HR module (the HR has an employee list in which each list item when clicked gives details of the employee). But the profile managemnt of users comes under "Profile management" module and hence all data present there is stubbed.
- As the admission form is fully customisable (Part of the requirements) it is possible to remove the fields like full name which are needed in the HR module for other implemented end-to-end tasks. It is assumed that the **admin wont remove such important fields** when customising the form. 
- As mentioned in the doubt page (hackmd) real IMS workflows like double approval is utilised in the leave approval workflow. This is the feature which satisfies the "real IMS integration".
- News creation is limited to Admin only. This helps the usecase where the creator can edit the news item. As all news are made by the admin the admin has the sole access to edit all the news and moderate (delete) all the comments.