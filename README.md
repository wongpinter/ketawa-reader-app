# Ketawa Reader App

A modern, offline-first Android client for [Ketawa.com](https://www.ketawa.com) — Indonesia's classic humor portal.

Built with **Jetpack Compose**, **Kotlin 2.0**, **Ktor**, and **Room DB**.

---

## Features

- **Modern Editorial UI:** Material 3 design system with brand-inspired palette (vibrant orange, ocean cyan, warm paper surfaces).
- **Proportional Typography:** Balanced text scale tuned for reading speed and comfortable line heights.
- **Offline Reading (Room DB):**
  - **Read-Through Caching:** Automatically caches viewed jokes to local SQLite database.
  - **Batch Downloader:** Pre-fetch entire humor categories for offline reading on the go.
- **Reader Ergonomics:**
  - One-tap font size switcher (`Small`, `Medium`, `Large`).
  - Bookmarks & favorites backed by AndroidX DataStore.
  - Native Android share intent & one-tap clipboard copy.
  - Seamless previous/next joke navigation.
- **HTML Sanitization:** Cleans legacy HTML entities (`&quot;`, `&#039;`, `&amp;`), break tags (`<br />`), and unescaped characters.

---

## Tech Stack & Architecture

- **Language:** Kotlin 2.0
- **UI Toolkit:** Jetpack Compose (Material 3)
- **Architecture:** Clean Architecture + Repository Pattern (Offline-First Single Source of Truth)
- **Dependency Injection:** Hilt (Dagger)
- **Networking:** Ktor Client (CIO engine)
- **Serialization:** `kotlinx.serialization` (JSON)
- **Local Persistence:**
  - **Room Database:** Offline jokes storage & full-text queries.
  - **Preferences DataStore:** Reader settings, font scale, and bookmarks.
- **Asynchronous Flow:** Kotlin Coroutines & Flow

---

## API Endpoints Used

Powered by the public endpoints at `https://api.ketawa.com`:
- `GET /json-index`: Latest humor feed
- `GET /json-cat-list`: Category directory
- `GET /json-cat-{catId}-{page}`: Paginated jokes by category
- `GET /json-detail-{id}`: Full joke content and navigation pointers

---

## License

MIT License.
