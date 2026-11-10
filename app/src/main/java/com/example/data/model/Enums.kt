package com.example.data.model

enum class UserRole(val displayName: String, val badge: String) {
    WORKER("Worker", "👷"),
    SUPERVISOR("Supervisor / Mestri", "👷‍♂️"),
    CONTRACTOR("Contractor", "🏢"),
    ADMIN("Admin & Moderation", "🛡️")
}

enum class TradeSkill(val idName: String, val icon: String) {
    ALL("All Trades", "🏗️"),
    MASON("Mason / Mestri", "🧱"),
    ELECTRICIAN("Electrician", "⚡"),
    PLUMBER("Plumber", "🔧"),
    PAINTER("Painter", "🖌️"),
    BAR_BENDER("Bar Bender", "⛓️"),
    CARPENTER("Carpenter", "🪚"),
    HELPER("Helper / Labour", "🧤"),
    WELDER("Welder / Fabricator", "🔥")
}

enum class Language(val code: String, val label: String, val nativeLabel: String) {
    ENGLISH("en", "English", "English"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    HINDI("hi", "Hindi", "हिंदी"),
    TELUGU("te", "Telugu", "తెలుగు")
}

enum class ApplicationStatus {
    APPLIED,
    SHORTLISTED,
    HIRED,
    REJECTED
}

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    HALF_DAY
}
