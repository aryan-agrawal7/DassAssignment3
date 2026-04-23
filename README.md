# DassAssignment3
Institute Management App

## IMS App - Development Notes

## System Assumptions

1. **Custom Form Integrity**: It is assumed that the Administrator will not delete or rename essential fields in the Employee Admission Form template. The following field labels are used by the system to populate core user data and profile sections:
    - `Full Name`: Used for display across News, Dashboard, and Profile.
    - `Department`: Used for role-based categorization and profile display.
    - `Email Address`: Used to populate the Contact Information in the Employee Profile.
    - `Phone Number`: Used to populate the Contact Information in the Employee Profile.
2. **Data Consistency**: Deleting these fields from the template will cause the system to use default values (e.g., "New Employee", "General") for newly admitted staff.
3. **Conflict Handling**: The system does not currently perform uniqueness checks for "Full Name" or "Email Address" during admission. It is assumed that no duplicate credentials are created, as the system will not provide an error for conflicting names or emails.
4. **Module Stubs**: The "Employee Detailed Screen" accessible from the Employee List is currently implemented as a UI stub/placeholder, as the "Manage Users" module was not a primary requirement for this phase of the assignment.

## Tech Stack
- Kotlin / Jetpack Compose
- Material Design 3
- StateFlow for Mock In-Memory Database
