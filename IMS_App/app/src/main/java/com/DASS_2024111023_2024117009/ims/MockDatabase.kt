package com.DASS_2024111023_2024117009.ims

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- DATA CLASSES ---
data class User(val id: String, val pass: String, val role: String, val name: String, val department: String, val customData: Map<String, String> = emptyMap())
data class Leave(val id: String, val empId: String, val type: String, val start: String, val end: String, val reason: String, val attachment: String?, val attachmentName: String? = null, var hrStatus: String = "Pending", var adminStatus: String = "Pending", val appliedDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) {
    val overallStatus: String get() = if (hrStatus == "Rejected" || adminStatus == "Rejected") "Rejected" else if (hrStatus == "Approved" && adminStatus == "Approved") "Approved" else "Pending"
}
data class Payslip(val id: String, val empId: String, val month: String, val basic: String, val allowances: String, val deductions: String, val net: String, var status: String = "Pending")
data class FormField(val id: String, val label: String, val type: String, val isRequired: Boolean, val isImmutable: Boolean = false, val options: List<String> = emptyList())
data class Comment(val id: String, val authorName: String, val content: String, val time: String, val isDeleted: Boolean = false)
data class News(val id: String, val title: String, val content: String, val author: String, val time: String, var likes: Int, var likedBy: List<String> = emptyList(), var comments: List<Comment> = emptyList(), val order: Long = 0L, val imageUri: String? = null)
data class Settings(var notifications: Boolean = true, var darkTheme: Boolean = true, var language: String = "English", var timezone: String = "UTC")

// --- THE SINGLETON DATABASE ---
object MockDatabase {
    var currentUser: User? = null
    val currentSettings = MutableStateFlow(Settings())

    // LIVE DATA FLOWS (UI will auto-update when these change)
    val users = MutableStateFlow(listOf(
        User("admin", "123", "Admin", "Alexander Pierce", "System Administrator", mapOf("Email Address" to "alex.admin@ims.edu", "Phone Number" to "+1 234 567 890")),
        User("hr", "123", "HR", "Alice Smith", "Human Resources", mapOf("Email Address" to "alice.hr@ims.edu", "Phone Number" to "+1 987 654 321")),
        User("employee", "123", "Employee", "Dr. Abhishek", "Engineering", mapOf("Email Address" to "abhishek@ims.edu", "Phone Number" to "+1 555 000 111")),
        User("student", "123", "Student", "Yash Tripathi", "UG 2024", mapOf("Email Address" to "yash@ims.edu", "Phone Number" to "+1 222 333 444"))
    ))

    val leaves = MutableStateFlow(listOf(
        Leave("L1", "employee", "Sick Leave", "12/05/2024", "14/05/2024", "Viral Fever", null, null, "Approved", "Approved"),
        Leave("L2", "employee", "Casual Leave", "05/06/2024", "06/06/2024", "Family function", null, null, "Approved", "Pending")
    ))

    val payslips = MutableStateFlow(listOf(
        Payslip("P1", "employee", "03/2024", "5000", "500", "100", "5400", "Approved"),
        Payslip("P2", "employee", "04/2024", "5000", "500", "100", "5400", "Approved")
    ))

    val admissionTemplate = MutableStateFlow(listOf(
        FormField("f1", "Full Name", "Short Text", true),
        FormField("f2", "Department", "Dropdown", true, options = listOf("Engineering", "Academics", "Administration")),
        FormField("f3", "Email Address", "Short Text", true),
        FormField("f4", "Phone Number", "Number", true),
        FormField("f5", "Base Salary", "Number", true)
    ))

    val studentAdmissionTemplate = MutableStateFlow(listOf(
        FormField("s1", "Student Full Name", "Short Text", true),
        FormField("s2", "Course / Program", "Dropdown", true, options = listOf("B.Tech", "M.Tech", "Ph.D", "BBA", "MBA")),
        FormField("s3", "Batch Year", "Number", true),
        FormField("s4", "Previous Education Score (%)", "Number", false)
    ))

    val payslipTemplate = MutableStateFlow(listOf(
        FormField("p1", "Basic Salary", "Number", true, isImmutable = true),
        FormField("p2", "Allowances", "Number", false),
        FormField("p3", "Deductions", "Number", false),
        FormField("p4", "Notes", "Long Text", false)
    ))

    val allNews = MutableStateFlow(listOf(
        News("N1", "End of Semester Exams", "The final exams start next week. Check schedules.", "Admin", "10/05/2024", 0, order = 1L),
        News("N2", "New Library Rules", "Extended library hours until 2 AM.", "Admin", "12/05/2024", 2, listOf("employee", "hr"), order = 2L)
    ))

    fun addLeave(leave: Leave) = leaves.update { it + leave }
    fun addPayslip(payslip: Payslip) = payslips.update { it + payslip }
    fun addUser(user: User) = users.update { it + user }

    fun updateLeaveStatusHR(leaveId: String, status: String) {
        leaves.update { list ->
            list.map {
                if (it.id == leaveId) {
                    val updatedStatus = if (status == "Rejected") "Rejected" else it.adminStatus
                    it.copy(hrStatus = status, adminStatus = updatedStatus)
                } else it
            }
        }
    }
    fun updateLeaveStatusAdmin(leaveId: String, status: String) {
        leaves.update { list ->
            list.map { if (it.id == leaveId) it.copy(adminStatus = status) else it }
        }
    }
    fun updatePayslipStatusHR(payslipId: String, status: String) {
        payslips.update { list ->
            list.map { if (it.id == payslipId) it.copy(status = status) else it }
        }
    }
    fun updatePayslipStatusAdmin(payslipId: String, status: String) {
        payslips.update { list ->
            list.map { if (it.id == payslipId) it.copy(status = status) else it }
        }
    }

    fun deleteUser(userId: String) {
        users.update { list -> list.filter { it.id != userId } }
        // Anonymize comments
        allNews.update { newsList ->
            newsList.map { news ->
                news.copy(comments = news.comments.map { if (it.authorName == userId) it.copy(authorName = "Deleted User", isDeleted = true) else it })
            }
        }
    }

    fun addNews(news: News) {
        val maxOrder = allNews.value.maxOfOrNull { it.order } ?: 0L
        allNews.update { listOf(news.copy(order = maxOrder + 1)) + it }
    }
    
    fun deleteNews(newsId: String) = allNews.update { list -> list.filter { it.id != newsId } }

    fun updateNews(newsId: String, newTitle: String, newContent: String, newImageUri: String?) {
        allNews.update { list ->
            val existing = list.find { it.id == newsId }
            if (existing != null) {
                val maxOrder = list.maxOfOrNull { it.order } ?: 0L
                val updatedNews = existing.copy(
                    title = newTitle,
                    content = newContent,
                    imageUri = newImageUri,
                    time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) + " (edited)",
                    order = maxOrder + 1
                )
                // Remove old version and add updated one at the start (to be top in sorted list)
                listOf(updatedNews) + list.filter { it.id != newsId }
            } else {
                list
            }
        }
    }

    fun toggleNewsLike(newsId: String, userId: String) {
        allNews.update { list ->
            list.map { news ->
                if (news.id == newsId) {
                    val currentLikes = news.likedBy
                    if (currentLikes.contains(userId)) {
                        news.copy(likes = news.likes - 1, likedBy = currentLikes - userId)
                    } else {
                        news.copy(likes = news.likes + 1, likedBy = currentLikes + userId)
                    }
                } else news
            }
        }
    }

    fun addNewsComment(newsId: String, comment: Comment) {
        allNews.update { list ->
            list.map { news ->
                if (news.id == newsId) {
                    news.copy(comments = news.comments + comment)
                } else news
            }
        }
    }

    fun deleteNewsComment(newsId: String, commentId: String) {
        allNews.update { list ->
            list.map { news ->
                if (news.id == newsId) {
                    news.copy(comments = news.comments.filter { it.id != commentId })
                } else news
            }
        }
    }

    // --- LEAVE UTILS (COUNT BASED) ---
    const val MAX_LEAVE_REQUESTS = 10

    fun getLeaveMetrics(empId: String): LeaveMetrics {
        val userLeaves = leaves.value.filter { it.empId == empId }
        val approved = userLeaves.count { it.overallStatus == "Approved" }
        val pending = userLeaves.count { it.overallStatus == "Pending" }
        val balance = (MAX_LEAVE_REQUESTS - approved - pending).coerceAtLeast(0)
        return LeaveMetrics(MAX_LEAVE_REQUESTS, balance, pending, approved)
    }
}

data class LeaveMetrics(val total: Int, val balance: Int, val pending: Int, val approved: Int)