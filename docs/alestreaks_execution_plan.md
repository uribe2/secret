# AleStreaks — Android Build Plan (Kotlin + Firebase + AI)

## Confirmed decisions
- **App name:** `AleStreaks`
- **Application ID:** `com.malejalmeja.streaks`
- **Platform/stack:** **Android native** with **Kotlin**
- **AI:** **Yes** (lightweight, practical assistant features)

## 1) Create the Android project
1. In Android Studio, create a new project:
   - Template: **Empty Activity**
   - Language: **Kotlin**
   - Minimum SDK: **26+** (recommended for geofencing reliability)
2. Set package/application ID to:
   - `com.malejalmeja.streaks`
3. Use Jetpack Compose UI.

## 2) Firebase setup order (exact)
1. Create Firebase project named `AleStreaks`.
2. Add Android app using package name `com.malejalmeja.streaks`.
3. Download `google-services.json` and place it in `app/`.
4. Enable products:
   - Firestore
   - Cloud Messaging
   - (Optional but recommended) Authentication
   - Cloud Functions
5. Add Firebase SDK dependencies in Gradle and sync.
6. Create Firestore rules scoped by `request.auth.uid`.
7. Configure indexes after first query errors suggest them.

## 3) Core app modules
- **Tasks**: create/edit/archive, icon, color, target cadence.
- **Completions**: daily/weekly marks and streak calculation.
- **Reminders (time)**: local scheduled notifications.
- **Reminders (location)**: geofences (arrive/leave) with radius.
- **Settings**: notification windows, quiet hours, backup/export.
- **AI helper**: suggestion cards (best reminder time, title cleanup, weekly insights).

## 4) Firestore collections
- `users/{uid}`
- `users/{uid}/tasks/{taskId}`
- `users/{uid}/tasks/{taskId}/completions/{yyyyMMdd}`
- `users/{uid}/reminders/{reminderId}`
- `users/{uid}/aiInsights/{insightId}`

### Task document (example fields)
- `title: string`
- `icon: string`
- `color: string`
- `cadenceType: "daily" | "weekly"`
- `targetCount: number`
- `active: boolean`
- `createdAt: timestamp`

### Reminder document (example fields)
- `taskId: string`
- `type: "time" | "location"`
- `timeOfDay: "HH:mm"`
- `daysOfWeek: number[]`
- `lat: number`
- `lng: number`
- `radiusMeters: number`
- `enabled: boolean`

## 5) Permissions you must configure in AndroidManifest
- `POST_NOTIFICATIONS` (Android 13+)
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `ACCESS_BACKGROUND_LOCATION` (for geofence reminders when app is not foreground)
- `FOREGROUND_SERVICE` (if needed by your reminder strategy)

## 6) AI feature scope for MVP
Keep AI small and useful:
1. **Smart reminder suggestion** based on completion history.
2. **Task text improver** (shorter/clearer task names).
3. **Weekly summary** (“You completed 18/21 planned actions”).

Implementation note:
- Run AI calls from Cloud Functions (server-side) and write results to `aiInsights`.
- App only reads suggestions and lets you apply them with one tap.

## 7) 2-week MVP sequence
### Week 1
- Day 1–2: project setup, Firebase connect, auth/firestore wiring
- Day 3–4: task CRUD + home list UI
- Day 5: completion marking + streak calculations
- Day 6–7: time reminders + notification UX

### Week 2
- Day 8–9: geofencing reminders + permission flows
- Day 10: settings screen + quiet hours
- Day 11: AI suggestion endpoint + UI cards
- Day 12: QA on reboot/background behavior
- Day 13: polish + icon personalization
- Day 14: final test and internal release build

## 8) Immediate next action
Start the Android project now with:
- **Name:** `AleStreaks`
- **Package:** `com.malejalmeja.streaks`
- **Language:** Kotlin

Then connect Firebase before writing feature code.
