# Android Habit Tracker (Streaks-like) — Project Brief

## Update based on your latest inputs
- App name: `AleStreaks`
- Candidate applicationId: `com.malejalmeja.streaks`
- Stack: Kotlin
- AI: Yes

## 1) Recommended stack (for your use case)
- **Android native** with **Kotlin + Jetpack Compose** (simple UI, fast iteration).
- **Firebase**:
  - Authentication (optional, even if only for you)
  - Cloud Firestore (tasks, streaks, reminders, locations)
  - Cloud Functions (scheduled checks / reminder logic)
  - Firebase Cloud Messaging (push notifications)
  - Crashlytics + Analytics (optional but useful)
- **Location / geofencing**:
  - Android Geofencing API + background location permission.
- **Optional AI**:
  - lightweight suggestions (e.g., reminder time recommendations, task wording cleanup, motivation summaries).

## 2) Application ID for `build.gradle`
Use reverse-domain format, lowercase, no spaces.

Examples:
- `com.yourname.streaks`
- `com.yourname.habittracker`
- `com.yourname.private.streaks`

Recommendation (private personal app):
- `com.<yourname>.streaks`

> Important: once published (or deeply integrated), changing `applicationId` is painful. Pick a stable one now.

## 3) What I need from you before implementation
1. **Preferred app name** (display name).
2. **Final `applicationId`** you want.
3. **Language/stack choice**:
   - Kotlin native (recommended), or
   - Flutter (if you want possible iOS later).
4. **Reminder model**:
   - Time-based only,
   - Location-based only,
   - Both (recommended).
5. **Streak rules**:
   - daily/weekly target,
   - allowed misses,
   - grace period.
6. **Task model**:
   - max number of tasks on home screen,
   - icon set (emoji, Material icons, custom image upload).
7. **AI scope** (optional):
   - no AI,
   - simple suggestions only,
   - deeper assistant features.
8. **Offline behavior**:
   - must work fully offline and sync later (recommended).
9. **Backup/export**:
   - JSON/CSV export needed or not.

## 4) Firebase configuration checklist
1. Create Firebase project.
2. Add Android app with your `applicationId`.
3. Download `google-services.json` into app module.
4. Enable Firestore in production mode.
5. (Optional) Enable Auth (Google/email).
6. Configure Cloud Messaging.
7. Create initial Firestore security rules (user-scoped).
8. Add indexes (if needed by your queries).
9. Set up Cloud Functions for scheduled reminder logic (if server-side reminders are used).

## 5) Suggested MVP feature list
- Create/edit/archive task.
- Choose task icon and color.
- Mark task complete for the day.
- Streak counter and history.
- Daily time reminders.
- Location reminders (arrive/leave location).
- Simple dashboard (today’s completion + current streaks).

## 6) Data model (Firestore, MVP)
- `users/{uid}`
  - profile settings
- `users/{uid}/tasks/{taskId}`
  - `title`, `icon`, `color`, `scheduleType`, `targetPerPeriod`, `isArchived`
- `users/{uid}/tasks/{taskId}/completions/{yyyyMMdd}`
  - `completedAt`, `count`
- `users/{uid}/reminders/{reminderId}`
  - `taskId`, `type` (`time`/`location`), `timeOfDay`, `daysOfWeek`, `lat`, `lng`, `radius`

## 7) Next step
Reply with:
- app name,
- exact `applicationId`,
- Kotlin or Flutter,
- whether AI is yes/no,

and I can produce a **full step-by-step implementation plan from zero**, including project structure and Firebase setup order.
