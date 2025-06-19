# 💘 MatchMate – Android Matchmaking App

**MatchMate** is a modern Android application built with clean architecture, MVVM pattern, and offline-first support. It fetches user profiles from the [RandomUser API](https://randomuser.me/), displays them in a card-based UI, and allows users to **Accept** or **Decline** matches. The app intelligently caches data, handles flaky network conditions with retries, and calculates a custom match score.

---

## 🚀 Features

- ✅ Fetch random user profiles using **Retrofit**
- 🔁 Retry API calls on flaky network using custom retry logic
- 📶 Fully **offline-first** using **Room** for local persistence
- 💡 Match scoring based on **age proximity** and **city match**
- 💾 Save Accept/Decline state locally
- 🖼️ Card-style UI with profile image, name, location, and score
- 🧠 Efficient RecyclerView updates via **DiffUtil**
- 📦 Built on **MVVM architecture** with **Hilt DI**
- 💥 Simulated flaky network for resilience testing

---

## 🧱 Architecture

The app follows **MVVM** (Model-View-ViewModel) with **Repository Pattern** for clean separation of concerns.

Presentation (Activity, Adapter)
|
ViewModel (MatchMateViewModel)
|
Repository (MatchMateRepo)
|
Data Layer (Retrofit API + Room DAO)


---

## 🧰 Tech Stack

| Category              | Libraries/Tools Used                                  |
|-----------------------|--------------------------------------------------------|
| Architecture          | MVVM, Repository Pattern                               |
| Dependency Injection  | [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) |
| Networking            | [Retrofit](https://square.github.io/retrofit/) + Gson |
| Persistence           | [Room](https://developer.android.com/training/data-storage/room) DB |
| Asynchronous          | [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) + Flow |
| UI                    | RecyclerView, ViewBinding, CardView                   |
| State Management      | StateFlow, NetworkState Wrapper                        |
| Logging & Debugging   | Logcat                                                 |

---

## 📸 Screenshots
<img src="https://github.com/user-attachments/assets/4998d101-d304-46d7-a303-0fce956602de" alt="matchMate" width="300" height="600" />

## 📸 Video
1 - https://drive.google.com/file/d/1LX-vBWgW5giTXmTtFZFRFLiWia2zMP-f/view?usp=sharing

2 - https://drive.google.com/file/d/1Qf3xeCCWIZ9ELVX1jfrcjODCfv6TIWR4/view?usp=sharing


## Built with

- **Kotlin** - As a programming language.
- **Coroutines** - For multithreading while handling requests to the server and local database.
- **Model-View-ViewModel(MVVM)** - Offers an implementation of observer design pattern.
- **Kotlin flows** - Flow is the Kotlin type that can be used to model streams of data. Just like LiveData and RxJava streams.
- **Retrofit** - A type-safe HTTP client for Android and Java
- **glide** -An image loading library for Android backed by Kotlin Coroutines.
- **Hilt** - It is arguably the most used Dependency Injection, or DI, framework for Android. Many Android projects use Dagger to simplify building and providing dependencies across the app. It gives you the ability to create specific scopes, modules, and components, where each forms a piece of a puzzle: The dependency graph.


---

## 📂 Project Structure

```text
.
├── adapter/                 # RecyclerView Adapter + DiffUtil
├── di/                     # Hilt Modules (NetworkModule, DatabaseModule)
├── modelclass/             # Data models (MatchMate, Login, etc.)
├── repo/                   # MatchMateRepo – handles API & DB interaction
├── retrofit/               # Retrofit API Interface
├── room/                   # Room DAO + Entity + Database
├── util/                   # MatchScoreCalculator, NetworkState, Retry logic
├── viewModels/             # MatchMateViewModel
└── MainActivity.kt         # Main UI Logic
