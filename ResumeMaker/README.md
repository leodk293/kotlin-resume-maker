# Resume Maker - Mobile App

A beautiful and modern Android mobile app built with Jetpack Compose that allows users to create, edit, download, and manage their CVs/Resumes.

## Features

✅ **Create Resumes** - Build professional resumes with a user-friendly interface  
✅ **Edit Resumes** - Update existing resumes with ease  
✅ **Download PDFs** - Export your resume as a PDF file  
✅ **Multiple Resumes** - Manage multiple CVs in one place  
✅ **Persistent Storage** - Your resumes are saved locally on your device  
✅ **Modern UI** - Beautiful Material Design 3 interface  

## Project Structure

```
ResumeMaker/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/resumemaker/app/
│           │   ├── MainActivity.kt          # Main entry point
│           │   ├── data/
│           │   │   ├── models/              # Data models (CV, Experience, Formation)
│           │   │   └── repository/          # Data repository for storage
│           │   ├── ui/
│           │   │   ├── screens/             # UI screens
│           │   │   │   ├── HomeScreen.kt    # Main screen with CV list
│           │   │   │   ├── CreateEditCVScreen.kt  # Create/Edit CV form
│           │   │   │   └── CVDetailScreen.kt      # CV preview screen
│           │   │   ├── navigation/          # Navigation setup
│           │   │   ├── theme/               # Material Design theme
│           │   │   └── viewmodel/           # ViewModel for state management
│           │   └── utils/                   # Utilities (PDF exporter)
│           └── res/                         # Resources (strings, layouts)
└── build.gradle.kts                         # Project build configuration
```

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or newer
- Android SDK with API level 24 (Android 7.0) or higher

### Installation

1. **Open the Project**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `ResumeMaker` folder and select it

2. **Sync Gradle**
   - Android Studio will automatically sync Gradle files
   - Wait for the sync to complete

3. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon) or press `Shift + F10`

### Building from Command Line

```bash
# Windows
gradlew.bat assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

## Usage

### Creating a New Resume

1. Tap the floating action button (➕) on the home screen
2. Fill in your personal information:
   - Name, Email, Phone, Address
3. Add work experiences:
   - Tap "Add Experience"
   - Fill in job title, company, dates, and description
   - You can add multiple experiences
4. Add education:
   - Tap "Add Education"
   - Fill in degree, institution, and year
5. Add skills:
   - Enter your skills in the skills field
6. Tap "Save" in the top right corner

### Editing a Resume

1. From the home screen, tap on a resume card
2. Tap the "Edit" button (pencil icon) in the top bar
3. Make your changes
4. Tap "Save" to update

### Downloading a Resume

1. Open a resume from the home screen
2. Tap the "Download PDF" button at the bottom
3. Choose where to save or share the PDF

### Deleting a Resume

1. From the home screen, tap the delete icon (🗑️) on any resume card
2. The resume will be removed immediately

## Technical Details

### Technologies Used

- **Jetpack Compose** - Modern declarative UI toolkit
- **Material Design 3** - Latest Material Design components
- **Navigation Component** - For screen navigation
- **ViewModel** - For state management
- **Coroutines & Flow** - For asynchronous operations
- **iText 7** - For PDF generation
- **Gson** - For JSON serialization
- **SharedPreferences** - For local data storage

### Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern:

- **Model**: Data classes (CV, Experience, Formation)
- **View**: Jetpack Compose screens
- **ViewModel**: Manages UI-related data and business logic
- **Repository**: Handles data operations (storage/retrieval)

### Data Storage

Currently, resumes are stored using SharedPreferences with JSON serialization. For production use, consider migrating to Room database for better performance and data integrity.

## Future Enhancements

- [ ] Cloud sync support
- [ ] Multiple resume templates
- [ ] Photo upload for profile picture
- [ ] Print directly from app
- [ ] Dark mode support (already implemented via Material 3)
- [ ] Export to other formats (Word, HTML)
- [ ] Resume templates/themes

## License

This project is open source and available for personal and commercial use.

## Support

For issues or questions, please open an issue in the project repository.

---

**Happy Resume Building!** 📄✨

