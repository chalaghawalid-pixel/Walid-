package com.example.ui.theme

object Localization {
    data class Translation(
        val appName: String,
        val slogan: String,
        val selectLanguage: String,
        val tabShare: String,
        val tabBluetooth: String,
        val tabFiles: String,
        val tabHistory: String,

        // Share/Send Screen
        val selectFilesFirst: String,
        val radarSearching: String,
        val radarSearchingSub: String,
        val activeTransfer: String,
        val transferSpeed: String,
        val currentFileProgress: String,
        val speedUnit: String,
        val btPermissionsError: String,
        val grantBtPermissions: String,
        val bluetoothDisabled: String,
        val enableBtButton: String,
        val scanBtButton: String,
        val stopScanButton: String,
        val noDevicesFound: String,
        val bondedDevices: String,
        val discoveredDevices: String,

        // Files Screen
        val galleryHeader: String,
        val pickRealFiles: String,
        val noFilesInCategory: String,
        val selectedFilesCount: String,
        val filesCategoryPhotos: String,
        val filesCategoryVideos: String,
        val filesCategoryApps: String,
        val filesCategoryDocuments: String,
        val sizeLabel: String,
        val scanRealMediaStore: String,

        // History Screen
        val pastTransfers: String,
        val emptyHistory: String,
        val clearHistory: String,
        val cleanAllSuccess: String,
        val bytesUnit: String,

        // General
        val completedLabel: String,
        val failedLabel: String
    )

    val English = Translation(
        appName = "AuraShare",
        slogan = "Hyper-Speed Bluetooth Beam Engine",
        selectLanguage = "العربية",
        tabShare = "Send",
        tabBluetooth = "Bluetooth",
        tabFiles = "Files",
        tabHistory = "History",

        selectFilesFirst = "Please pick files from 'Files' tab first!",
        radarSearching = "Active Bluetooth Beam Radar",
        radarSearchingSub = "Scanning for nearby Bluetooth devices for wireless pairing",
        activeTransfer = "Active Transmissions",
        transferSpeed = "Beam Velocity: ",
        currentFileProgress = "Transferred Chunk Progress",
        speedUnit = "KB/s",
        btPermissionsError = "Bluetooth & Location permissions are required to scan.",
        grantBtPermissions = "Grant Permissions",
        bluetoothDisabled = "Bluetooth is turned off on your device.",
        enableBtButton = "Turn On Bluetooth",
        scanBtButton = "Start Scanning",
        stopScanButton = "Stop Scanning",
        noDevicesFound = "No nearby active Bluetooth beams detected.",
        bondedDevices = "Paired Devices",
        discoveredDevices = "Discovered Nearby Devices",

        galleryHeader = "Device File Vault",
        pickRealFiles = "Open File Picker",
        noFilesInCategory = "No files selected. Tap 'Open File Picker' or 'Scan Local Files' to search your device.",
        selectedFilesCount = "Selected: ",
        filesCategoryPhotos = "Photos",
        filesCategoryVideos = "Videos",
        filesCategoryApps = "Apps (APK)",
        filesCategoryDocuments = "Documents",
        sizeLabel = "Size: ",
        scanRealMediaStore = "Scan Local Files",

        pastTransfers = "Bluetooth Transmission Audit",
        emptyHistory = "No historical Bluetooth transfers recorded.",
        clearHistory = "Wipe Logs",
        cleanAllSuccess = "Logs wiped safely.",
        bytesUnit = "Bytes",
        completedLabel = "COMPLETED",
        failedLabel = "FAILED"
    )

    val Arabic = Translation(
        appName = "أورا شير",
        slogan = "محرك الإرسال اللاسلكي عبر البلوتوث",
        selectLanguage = "English",
        tabShare = "إرسال",
        tabBluetooth = "بلوتوث",
        tabFiles = "الملفات",
        tabHistory = "السجل",

        selectFilesFirst = "الرجاء اختيار ملفات من علامة تبويب 'الملفات' أولاً!",
        radarSearching = "رادار البث النشط عبر البلوتوث",
        radarSearchingSub = "البحث عن الأجهزة القريبة لنقل الملفات لاسلكياً",
        activeTransfer = "عمليات الإرسال النشطة",
        transferSpeed = "سرعة الإرسال: ",
        currentFileProgress = "مستوى التقدّم للملف الحالي",
        speedUnit = "كيلوبايت/ثانية",
        btPermissionsError = "يتطلب التطبيق صلاحيات البلوتوث والموقع للبحث عن الأجهزة.",
        grantBtPermissions = "منح الصلاحيات",
        bluetoothDisabled = "البلوتوث مغلق حالياً على جهازك.",
        enableBtButton = "تفعيل البلوتوث",
        scanBtButton = "بدء البحث",
        stopScanButton = "إيقاف البحث",
        noDevicesFound = "لم يتم العثور على أجهزة بلوتوث نشطة قريبة.",
        bondedDevices = "الأجهزة المقترنة مسبقاً",
        discoveredDevices = "الأجهزة المكتشفة حديثاً",

        galleryHeader = "خزنة ملفات الهاتف",
        pickRealFiles = "اختيار ملفات حقيقية",
        noFilesInCategory = "لم يتم اختيار أي ملفات. اضغط على 'اختيار ملفات' أو 'فحص الملفات المحلية'.",
        selectedFilesCount = "الملفات المحددة: ",
        filesCategoryPhotos = "الصور",
        filesCategoryVideos = "الفيديو",
        filesCategoryApps = "التطبيقات",
        filesCategoryDocuments = "المستندات",
        sizeLabel = "الحجم: ",
        scanRealMediaStore = "فحص الملفات المحلية",

        pastTransfers = "سجل عمليات البلوتوث",
        emptyHistory = "لا يوجد سجل لعمليات النقل السابقة.",
        clearHistory = "مسح السجل بالكامل",
        cleanAllSuccess = "تم مسح سجل العمليات وتطهير الذاكرة كلياً.",
        bytesUnit = "بايت",
        completedLabel = "مكتمل",
        failedLabel = "فشل"
    )
}
