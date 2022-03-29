package com.s097t0r1.kode.ui.main.components

import androidx.annotation.StringRes
import com.s097t0r1.domain.models.Department
import com.s097t0r1.kode.R

enum class DepartmentTabs(
    val department: Department?,
    @StringRes val title: Int
) {
    ALL(null, R.string.department_tab_all),
    ANDROID(Department.ANDROID, R.string.department_tab_android),
    IOS(Department.IOS, R.string.department_tab_ios),
    DESIGN(Department.DESIGN, R.string.department_tab_design),
    MANAGEMENT(Department.MANAGEMENT, R.string.department_tab_management),
    QA(Department.QA, R.string.department_tab_qa),
    BACK_OFFICE(Department.BACK_OFFICE, R.string.department_tab_back_office),
    FRONTED(Department.FRONTEND, R.string.department_tab_frontend),
    HR(Department.HR, R.string.department_tab_hr),
    PR(Department.PR, R.string.department_tab_pr),
    BACKEND(Department.BACKEND, R.string.department_tab_backend),
    SUPPORT(Department.SUPPORT, R.string.department_tab_support),
    ANALYTICS(Department.ANALYTICS, R.string.department_tab_analytics)
}