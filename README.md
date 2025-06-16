# 🎵 MiloBeats - YouTube Music Player

A modern Android music player app that lets you search and play music from YouTube. Built with Jetpack Compose and following MVVM architecture.

## ✨ Features

- 🎯 Search for music videos on YouTube
- 🎬 Play videos with a beautiful custom player
- 🎨 Modern Material 3 UI with dark theme
- 🔄 Background playback support
- 📱 Responsive design for all screen sizes

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 29 or higher
- Kotlin 1.9.0 or higher
- JDK 1.8 or higher

### Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/MiloBeats.git
```

2. Get a YouTube Data API key:
   - Go to the [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the YouTube Data API v3
   - Create credentials (API key)
   - Copy your API key

3. Add your API key to `local.properties`:
```properties
youtubeApiKey=your_api_key_here
```

4. Build and run the project in Android Studio

## 🎮 How to Use

### Searching for Music

```kotlin
// Example of searching for tracks
viewModel.searchTracks("query")
```

The app will display search results with:
- Video thumbnail
- Title
- Channel name
- Duration

### Playing Music

```kotlin
// Example of playing a track
viewModel.selectTrack(track)
viewModel.togglePlayback()
```

The player supports:
- Play/Pause
- Skip forward/backward
- Full-screen mode
- Background playback

## 🏗️ Architecture

The app follows MVVM architecture with clean architecture principles:

- **Presentation Layer**: UI components and ViewModels
- **Domain Layer**: Use cases and business logic
- **Data Layer**: Repositories and data sources

### Key Components

- `MusicPlayerViewModel`: Manages player state and user interactions
- `YouTubeMusicRepository`: Handles YouTube API communication
- `VideoPlayer`: Custom YouTube player component
- `Track`: Data model for music tracks

## 🛠️ Dependencies

- Jetpack Compose for UI
- Hilt for dependency injection
- Retrofit for API calls
- Coil for image loading
- YouTube Player Core for video playback
- Coroutines for asynchronous operations

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎁 Gift

Here's a fun easter egg in the code:
```kotlin
// Try searching for "rick astley" in the app 😉
```

---

Made with ❤️ by [Your Name] 