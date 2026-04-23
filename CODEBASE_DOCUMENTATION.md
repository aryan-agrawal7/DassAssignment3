# IMS App - Complete Codebase Documentation

## 1. Project Configuration & Build System
- **Build Tool**: Gradle with Kotlin DSL (`.kts`).
- **Target Platform**: Android 16 (SDK 36) with backward compatibility to Android 7.0 (SDK 24).
- **Core Dependencies**: 
    - **Jetpack Compose BOM**: Managed UI dependency versions.
    - **Navigation Compose**: Type-safe routing using `NavHost`.
    - **Material 3**: Modern design system implementation.
    - **Material Icons Extended**: Full icon set for cross-role actions.

## 2. System Architecture
- **Entry Point**: `MainActivity.kt` initializes the `RootNavigation` composable.
- **Routing**: `RootNavigation.kt` handles top-level auth-swaps (Login -> Role-specific Main Screens).
- **Navigation Scoping**: Role-specific `MainScreens` (Admin, HR, etc.) use nested `NavHost` to manage internal sub-routes (Settings, Forms, Approvals) while persisting common UI elements like Bottom Bars.

## 3. Data Architecture (`MockDatabase.kt`)
- **Singleton Pattern**: A global `object` acts as the source of truth, centralizing all system data in RAM.
- **Reactivity**: Uses `MutableStateFlow` for all collections (`users`, `leaves`, `news`, `payslips`). UI components `collectAsState()` these flows to refresh instantly on data mutation.
- **Persistence Logic**: Purely in-memory; data resets on process death to facilitate clean testing and "zero-leak" demonstrations.
- **Atomicity**: Data updates use the `.update { ... }` block to ensure thread-safe mutations of in-memory lists.

## 4. UI & Design System
- **Theme (`ui/theme/`)**: Custom Material 3 theme.
- **Color Palette**: Dark-first aesthetic (`BackgroundDark`, `CardDark`) with `TealAccent` for primary actions and `TextGray` for secondary metadata.
- **Reusable Stubs**: Standardized card components (e.g., `FullNewsCard`, `PayslipCardStub`) ensure consistent layout across different user roles.

## 5. Core Workflow Logic
### A. Dynamic Form Engine
- **Implementation**: Forms for Employee Admission and Payroll are not hardcoded. 
- **Mechanism**: The system iterates over a `Template` list of `PayrollField` or `AdmissionField` objects, dynamically rendering TextFields, Dropdowns, or Numeric inputs based on the field type.
- **Validation**: Strict `isRequired` checks are performed by filtering the template and matching against a local `Map<String, String>` of user inputs.

### B. Approval Workflows
- **Leaves**: A two-stage approval system (HR -> Admin). Status is tracked via a `status` enum string (`Pending`, `Approved`, `Rejected`).
- **Payroll**: Employees submit requests based on HR templates; HR reviews and approves/rejects, which updates the Employee's status in real-time.

### C. File & Image Management
- **Capture**: Uses `ActivityResultContracts.GetContent()` to obtain `content://` URIs from the Android System.
- **Resolution**: `getFileName` helper uses `ContentResolver` to query the `DISPLAY_NAME` from OpenableColumns.
- **Rendering**: Images are decoded from stream via `BitmapFactory` and rendered as `ImageBitmap` to avoid heavy overhead.
- **Viewing**: Employs `Intent.ACTION_VIEW` with `FLAG_GRANT_READ_URI_PERMISSION` to allow third-party apps (PDF viewers/Gallery) to open attachments.

### D. News Management
- **RTF Support**: Supports bold (`**`), italic (`*`), and underline (`__`) markers, parsed into `AnnotatedString` for rich display.
- **Ordering**: News items use an `order` long integer; editing an item increments its `order` to "pin" it to the top of the feed.

## 6. Assumptions & Constraints
- **Uniqueness**: The system does not currently perform uniqueness checks for "Full Name" or "Email Address" during admission. It is assumed that no duplicate credentials are created, as the system will not provide an error for conflicting names or emails.
- **Auth**: Passwords are standardized to `123` for demonstration; login is validated against the in-memory `users` list.
- **Module Stubs**: The "Employee Detailed Screen" accessible from the Employee List is currently implemented as a UI stub/placeholder, as the "Manage Users" module was not a primary requirement for this phase of the assignment.
