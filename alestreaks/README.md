# AleStreaks (Android)

Yes — I can create the code myself, and I already scaffolded the project here.

## Do you need a repository?
- Not strictly.
- But **GitHub is strongly recommended** so you can back up code and ship faster.

## Fastest path (ASAP)
1. Open `alestreaks/` in Android Studio.
2. Create a Firebase project and register Android app with package:
   - `com.malejalmeja.streaks`
3. Drop `google-services.json` into `alestreaks/app/`.
4. Sync Gradle.
5. Run on an emulator/device.

## What is already included
- Kotlin + Jetpack Compose Android app scaffold.
- `applicationId = "com.malejalmeja.streaks"`.
- Firestore-ready `TaskRepository` scaffold.
- Location reminder manager placeholder for geofencing.

## Next coding step
- Implement auth and real user IDs.
- Add streak completion logic and reminder scheduling.
- Add AI suggestion endpoint in Cloud Functions.
