# Video Downloader

Video Downloader is a JavaFX application that allows users to download videos from various sources such as YouTube, TikTok, Instagram, and more. It supports downloading both video and audio files and provides background downloading functionality.

## Features

- Download videos from multiple sources (YouTube, TikTok, Instagram, etc.)
- Extract audio from videos and download as MP3 files
- Support image formats (JPEG, PNG, etc.)
- Preview videos before downloading
- Background downloading of videos
- Progress bar for tracking download progress
- Support for unregistered and registered users
- User registration and login system
- Download limits for unregistered and registered users
- PostgreSQL database integration for user data storage

## Prerequisites

- Java Development Kit (JDK) 8 or later
- PostgreSQL database
- Gradle (for dependency management)

## Getting Started

1. Clone the repository:
   git clone https://github.com/tilekm/video_downloader.git
2. Navigate to the project directory:
   cd video-downloader
3. Configure the database connection:
- Create file `.env` in the project root directory.
- Add lines as in `ENV_EXAMPLE` file.

4. Build the project using Gradle:
`gradle build`
5. Run the application:
`gradle run`
## Usage

1. Launch the application.
2. For unregistered users:
- You can download up to 3 videos per day.
- Paste the video URL into the input field and click the "Download" button.
3. For registered users:
- Click the "Create new account" button to create a new account.
- After registration, log in using your credentials.
- Registered users can download up to 5 videos per day.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).