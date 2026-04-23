# DassAssignment3
Institute Management App
## Assumptions
- As the system uses a local storage instead of an actual database, the system doesnot check for uniqueness of the "Email Address" during admission. It is assumed that no duplicate credentials are created.
- **Authentication for stubbed users**: As this is a stubbed app there is a fixed list of users with passwords standardized to `123`.
- All modules which weren't chosen to be implemented are either shown as `coming soon` if they are not connected to an implemented module or stubbed completely if connected to an implemented module example the employee details is connected to the HR module (the HR has an employee list in which each list item when clicked gives details of the employee). But the profile managemnt of users comes under "Profile management" module and hence all data present there is stubbed.
- As the admission form is fully customisable (Part of the requirements) it is possible to remove the fields like full name which are needed in the HR module for other implemented end-to-end tasks. It is assumed that the **admin wont remove such important fields** when customising the form. 
- As mentioned in the doubt page (hackmd) real IMS workflows like double approval is utilised in the leave approval workflow. This is the feature which satisfies the "real IMS integration".
- News creation is limited to Admin only. This helps the usecase where the creator can edit the news item. As all news are made by the admin the admin has the sole access to edit all the news and moderate (delete) all the comments.
