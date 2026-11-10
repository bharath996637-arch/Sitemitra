package com.example.data.model

object LocaleStrings {

    fun get(key: String, lang: Language): String {
        val strings = when (lang) {
            Language.KANNADA -> kannada
            Language.HINDI -> hindi
            Language.TELUGU -> telugu
            Language.ENGLISH -> english
        }
        return strings[key] ?: english[key] ?: key
    }

    fun getTradeName(trade: TradeSkill, lang: Language): String {
        return when (lang) {
            Language.KANNADA -> when (trade) {
                TradeSkill.ALL -> "ಎಲ್ಲಾ ಕೆಲಸಗಳು"
                TradeSkill.MASON -> "ಗಾರೆ ಮೇಸ್ತ್ರಿ / ಕಲ್ಲು ಕೆಲಸ"
                TradeSkill.ELECTRICIAN -> "ಎಲೆಕ್ಟ್ರಿಷಿಯನ್ (ವಿದ್ಯುತ್)"
                TradeSkill.PLUMBER -> "ಪ್ಲಂಬರ್ (ಕೊಳಾಯಿ)"
                TradeSkill.PAINTER -> "ಬಣ್ಣ ಬಳಿಯುವವರು"
                TradeSkill.BAR_BENDER -> "ಕಬ್ಬಿಣ / ಬಾರ್ ಬೆಂಡರ್"
                TradeSkill.CARPENTER -> "ಬಡಗಿ (ಮರಗೆಲಸ)"
                TradeSkill.HELPER -> "ಸಹಾಯಕ / ದಿನಗೂಲಿ"
                TradeSkill.WELDER -> "ವೆಲ್ಡರ್ / ಫ್ಯಾಬ್ರಿಕೇಟರ್"
            }
            Language.HINDI -> when (trade) {
                TradeSkill.ALL -> "सभी काम"
                TradeSkill.MASON -> "राजमिस्त्री (चुनाई)"
                TradeSkill.ELECTRICIAN -> "इलेक्ट्रीशियन (बिजली)"
                TradeSkill.PLUMBER -> "प्लंबर (नलसाज)"
                TradeSkill.PAINTER -> "पेंटर (रंगाई)"
                TradeSkill.BAR_BENDER -> "बार बेंडर (सरिया)"
                TradeSkill.CARPENTER -> "बढ़ई (कारपेंटर)"
                TradeSkill.HELPER -> "हेल्पर / लेबर"
                TradeSkill.WELDER -> "वेल्डर / फैब्रिकेटर"
            }
            Language.TELUGU -> when (trade) {
                TradeSkill.ALL -> "అన్ని పనులు"
                TradeSkill.MASON -> "మేస్త్రీ / రాతి పని"
                TradeSkill.ELECTRICIAN -> "ఎలక్ట్రీషియన్"
                TradeSkill.PLUMBER -> "ప్లంబర్"
                TradeSkill.PAINTER -> "పెయింటర్"
                TradeSkill.BAR_BENDER -> "బార్ బెండర్ (ఐరన్)"
                TradeSkill.CARPENTER -> "వడ్రంగి (కార్పెంటర్)"
                TradeSkill.HELPER -> "హెల్పర్ / కూలీ"
                TradeSkill.WELDER -> "వెల్డర్"
            }
            Language.ENGLISH -> trade.idName
        }
    }

    private val english = mapOf(
        "tagline" to "Find Work. Find Workers. Build Together.",
        "bengaluru_pilot" to "Bengaluru Pilot Phase 1",
        "tab_home" to "Home",
        "tab_find_work" to "Find Work",
        "tab_live_map" to "Live Map",
        "tab_my_jobs" to "My Jobs",
        "tab_profile" to "Profile",
        "tab_dashboard" to "Dashboard",
        "tab_post_job" to "Post Job",
        "tab_applicants" to "Applicants",
        "tab_attendance" to "Attendance",
        "tab_projects" to "Projects",
        "tab_reports" to "Reports",
        "urgent_hiring" to "Urgent Hiring",
        "vacancies" to "Vacancies",
        "per_day" to "/day",
        "daily_wage" to "Daily Wage",
        "apply_now" to "Apply Now",
        "applied" to "Applied",
        "call_mestri" to "Call Supervisor",
        "whatsapp_chat" to "WhatsApp",
        "view_on_map" to "Directions",
        "food_provided" to "Food Provided",
        "stay_provided" to "Stay Provided",
        "filter_skill" to "Trade Skill",
        "filter_distance" to "Max Distance",
        "filter_wage" to "Min Wage",
        "switch_role" to "Switch Role",
        "available_status" to "Available for Work",
        "on_site_status" to "Currently on Site",
        "total_jobs" to "Active Jobs",
        "workers_needed" to "Workers Needed",
        "workers_hired" to "Workers Hired",
        "present" to "Present",
        "absent" to "Absent",
        "half_day" to "Half Day",
        "shortlist" to "Shortlist",
        "accept_hire" to "Hire Worker",
        "reject" to "Reject",
        "post_new_job" to "Post New Site Job",
        "post_job_desc" to "Hire genuine workers in Bengaluru quickly",
        "admin_moderation" to "Admin & Moderation",
        "contractor_overview" to "Contractor Portfolio"
    )

    private val kannada = mapOf(
        "tagline" to "ಕೆಲಸ ಪಡೆಯಿರಿ. ಕೆಲಸಗಾರರನ್ನು ಹುಡುಕಿ. ಒಟ್ಟಿಗೆ ನಿರ್ಮಿಸಿ.",
        "bengaluru_pilot" to "ಬೆಂಗಳೂರು ಪೈಲಟ್ ಹಂತ 1",
        "tab_home" to "ಮುಖಪುಟ",
        "tab_find_work" to "ಕೆಲಸ ಹುಡುಕಿ",
        "tab_live_map" to "ನಕ್ಷೆ (ಮ್ಯಾಪ್)",
        "tab_my_jobs" to "ನನ್ನ ಕೆಲಸಗಳು",
        "tab_profile" to "ಪ್ರೊಫೈಲ್",
        "tab_dashboard" to "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್",
        "tab_post_job" to "ಕೆಲಸ ಪ್ರಕಟಿಸಿ",
        "tab_applicants" to "ಅರ್ಜಿದಾರರು",
        "tab_attendance" to "ಹಾಜರಾತಿ",
        "tab_projects" to "ಯೋಜನೆಗಳು",
        "tab_reports" to "ವರದಿಗಳು",
        "urgent_hiring" to "ತುರ್ತು ನೇಮಕಾತಿ",
        "vacancies" to "ಖಾಲಿ ಹುದ್ದೆಗಳು",
        "per_day" to "/ದಿನಕ್ಕೆ",
        "daily_wage" to "ದೈನಂದಿನ ಕೂಲಿ",
        "apply_now" to "ಅರ್ಜಿ ಸಲ್ಲಿಸಿ",
        "applied" to "ಅರ್ಜಿ ಸಲ್ಲಿಸಲಾಗಿದೆ",
        "call_mestri" to "ಮೇಸ್ತ್ರಿಗೆ ಕರೆ ಮಾಡಿ",
        "whatsapp_chat" to "ವಾಟ್ಸಾಪ್ ಸಂದೇಶ",
        "view_on_map" to "ದಾರಿ ನೋಡಿ",
        "food_provided" to "ಊಟ ಲಭ್ಯ",
        "stay_provided" to "ವಸತಿ ಲಭ್ಯ",
        "filter_skill" to "ಕೆಲಸದ ವಿಧ",
        "filter_distance" to "ಗರಿಷ್ಠ ದೂರ",
        "filter_wage" to "ಕನಿಷ್ಠ ಕೂಲಿ",
        "switch_role" to "ಖಾತೆ ಬದಲಿಸಿ",
        "available_status" to "ಕೆಲಸಕ್ಕೆ ಸಿದ್ಧ",
        "on_site_status" to "ಸೈಟ್‌ನಲ್ಲಿ ಕೆಲಸದಲ್ಲಿದ್ದಾರೆ",
        "total_jobs" to "ಸಕ್ರಿಯ ಕೆಲಸಗಳು",
        "workers_needed" to "ಅಗತ್ಯವಿರುವ ಕೆಲಸಗಾರರು",
        "workers_hired" to "ನೇಮಕಗೊಂಡ ಕೆಲಸಗಾರರು",
        "present" to "ಹಾಜರು",
        "absent" to "ಗೈರುಹಾಜರು",
        "half_day" to "ಅರ್ಧ ದಿನ",
        "shortlist" to "ಆಯ್ಕೆ ಪಟ್ಟಿ",
        "accept_hire" to "ಕೆಲಸಕ್ಕೆ ನೇಮಿಸಿ",
        "reject" to "ತಿರಸ್ಕರಿಸಿ",
        "post_new_job" to "ಹೊಸ ಸೈಟ್ ಕೆಲಸ ಪ್ರಕಟಿಸಿ",
        "post_job_desc" to "ಬೆಂಗಳೂರಿನಲ್ಲಿ ನುರಿತ ಕೆಲಸಗಾರರನ್ನು ತಕ್ಷಣ ನೇಮಿಸಿ",
        "admin_moderation" to "ಅಡ್ಮಿನ್ ತಪಾಸಣೆ",
        "contractor_overview" to "ಕಾಂಟ್ರಾಕ್ಟರ್ ಯೋಜನೆಗಳು"
    )

    private val hindi = mapOf(
        "tagline" to "काम पाएं। मजदूर पाएं। मिलकर निर्माण करें।",
        "bengaluru_pilot" to "बेंगलुरु पायलट फेज़ 1",
        "tab_home" to "होम",
        "tab_find_work" to "काम ढूंढें",
        "tab_live_map" to "लाइव मैप",
        "tab_my_jobs" to "मेरे काम",
        "tab_profile" to "प्रोफाइल",
        "tab_dashboard" to "डैशबोर्ड",
        "tab_post_job" to "काम पोस्ट करें",
        "tab_applicants" to "आवेदक",
        "tab_attendance" to "हाजिरी",
        "tab_projects" to "प्रोजेक्ट्स",
        "tab_reports" to "रिपोर्ट्स",
        "urgent_hiring" to "तुरंत भर्ती",
        "vacancies" to "जरूरत",
        "per_day" to "/दिन",
        "daily_wage" to "दैनिक मजदूरी",
        "apply_now" to "आवेदन करें",
        "applied" to "आवेदन किया",
        "call_mestri" to "सुपरवाइजर को कॉल करें",
        "whatsapp_chat" to "व्हाट्सएप",
        "view_on_map" to "रास्ता देखें",
        "food_provided" to "खाना उपलब्ध",
        "stay_provided" to "रहना उपलब्ध",
        "filter_skill" to "हुनर / ट्रेड",
        "filter_distance" to "अधिकतम दूरी",
        "filter_wage" to "न्यूनतम दिहाड़ी",
        "switch_role" to "रोल बदलें",
        "available_status" to "काम के लिए उपलब्ध",
        "on_site_status" to "साइट पर कार्यरत",
        "total_jobs" to "सक्रिय काम",
        "workers_needed" to "जरूरी कारीगर",
        "workers_hired" to "भर्ती किए गए",
        "present" to "उपस्थित",
        "absent" to "अनुपस्थित",
        "half_day" to "हाफ डे",
        "shortlist" to "शॉर्टलिस्ट",
        "accept_hire" to "काम पर रखें",
        "reject" to "अस्वीकार",
        "post_new_job" to "नया काम पोस्ट करें",
        "post_job_desc" to "बेंगलुरु में तुरंत कुशल कारीगर भर्ती करें",
        "admin_moderation" to "व्यवस्थापक नियंत्रण",
        "contractor_overview" to "ठेकेदार प्रोजेक्ट्स"
    )

    private val telugu = mapOf(
        "tagline" to "పని పొందండి. కార్మికులను కనుగొనండి. కలిసి నిర్మించండి.",
        "bengaluru_pilot" to "బెంగళూరు పైలట్ ఫేజ్ 1",
        "tab_home" to "హోమ్",
        "tab_find_work" to "పని వెతకండి",
        "tab_live_map" to "లైవ్ మ్యాప్",
        "tab_my_jobs" to "నా పనులు",
        "tab_profile" to "ప్రొఫైల్",
        "tab_dashboard" to "డ్యాష్‌బోర్డ్",
        "tab_post_job" to "పని పోస్ట్ చేయండి",
        "tab_applicants" to "దరఖాస్తుదారులు",
        "tab_attendance" to "హాజరు",
        "tab_projects" to "ప్రాజెక్టులు",
        "tab_reports" to "నివేదికలు",
        "urgent_hiring" to "తక్షణ నియామకం",
        "vacancies" to "ఖాళీలు",
        "per_day" to "/రోజుకు",
        "daily_wage" to "రోజువారీ వేతనం",
        "apply_now" to "దరఖాస్తు చేసుకోండి",
        "applied" to "దరఖాస్తు చేయబడింది",
        "call_mestri" to "మేస్త్రీకి కాల్ చేయండి",
        "whatsapp_chat" to "వాట్సాప్",
        "view_on_map" to "రూట్ చూడండి",
        "food_provided" to "భోజనం ఉంది",
        "stay_provided" to "వసతి ఉంది",
        "filter_skill" to "పని రకం",
        "filter_distance" to "గరిష్ట దూరం",
        "filter_wage" to "కనీస వేతనం",
        "switch_role" to "పాత్ర మార్చండి",
        "available_status" to "పనికి సిద్ధంగా ఉన్నారు",
        "on_site_status" to "ప్రస్తుతం సైట్‌లో ఉన్నారు",
        "total_jobs" to "యాక్టివ్ పనులు",
        "workers_needed" to "అవసరమైన కార్మికులు",
        "workers_hired" to "చేర్చుకున్న కార్మికులు",
        "present" to "హాజరు",
        "absent" to "గైర్హాజరు",
        "half_day" to "హాఫ్ డే",
        "shortlist" to "షార్ట్‌లిస్ట్",
        "accept_hire" to "నియామకం చేయండి",
        "reject" to "తిరస్కరించండి",
        "post_new_job" to "కొత్త పని పోస్ట్ చేయండి",
        "post_job_desc" to "బెంగళూరులో నైపుణ్యం కలిగిన కార్మికులను నియమించండి",
        "admin_moderation" to "అడ్మిన్ పర్యవేక్షణ",
        "contractor_overview" to "కాంట్రాక్టర్ ప్రాజెక్టులు"
    )
}
