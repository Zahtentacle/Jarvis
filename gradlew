name: Jarvis Build Production

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

env:
  # Memaksa penggunaan Node.js 24 sesuai standar GitHub per Maret 2026
  FORCE_JAVASCRIPT_ACTIONS_TO_NODE24: true

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          # Mengaktifkan cache agar build berikutnya lebih cepat
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Build APK with Gradle 8.7
        # Menggunakan --no-daemon untuk stabilitas di environment Cloud
        # Menggunakan clean untuk memastikan tidak ada residu build Gradle 9.4 sebelumnya
        run: ./gradlew clean assembleDebug --no-daemon

      - name: Upload Final APK
        uses: actions/upload-artifact@v4
        with:
          name: JARVIS_WA_BLAST_FINAL
          path: app/build/outputs/apk/debug/app-debug.apk
