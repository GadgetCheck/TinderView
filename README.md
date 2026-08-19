# TinderView

A lightweight Jetpack Compose card-stack for Android. Drag, fling, rewind — the 2026 swipe feel without a 2015 View hierarchy.

<video src="docs/demo.mp4" width="280" controls muted loop playsinline></video>

The 24s clip walks the sample: pulse reveal, a below-threshold drag that springs back, like / nope / super-like throws, rewind, action-button squash, profile sheet, match burst, a real last-card swipe, empty deck, Explore, and Messages.

## Usage

```kotlin
val state = rememberCardStackState { profiles.size }

CardStack(
    items = profiles,
    key = { it.id },
    state = state,
    onSwiped = { profile, direction -> /* persist like / pass */ },
    onTopCardClick = { profile -> /* open sheet */ },
) { profile ->
    ProfileCard(profile)
}

// Same physics as a finger fling
scope.launch { state.swipe(SwipeDirection.Right) }
scope.launch { state.rewind() }
```

### Install

```kotlin
implementation(project(":cardstack"))
```

The sample app (`:app`) is the reference integration. Maven Central publishing is a follow-up.

### Properties

| Property | Default | Meaning |
| --- | --- | --- |
| `visibleCount` | `3` | Cards composed at once |
| `stackOffset` | `10.dp` | Depth offset for back cards |
| `stackScaleStep` | `0.04` | Scale drop per depth |
| `maxRotationZ` | `14f` | Peak tilt in degrees |
| `thresholdFraction` | `0.35` | Commit distance as a fraction of card width |
| `flingVelocity` | `900.dp` | Velocity (per second) that commits a throw |
| `enabledDirections` | Left, Right, Up | Physical axes that can discard |
| `enableHaptics` | `true` | One tick on threshold, one on commit |
| `enableColorWash` | `true` | Leading-edge tint while dragging |
| `snapSpring` / `throwSpring` | tuned springs | Snap-back vs exit |

Override via `CardStackProperties` or `CardStackDefaults.properties()`.

### Overlays

LIKE / NOPE / SUPER stamps receive **0f..1f** progress. Replace the defaults:

```kotlin
CardStack(
    items = profiles,
    key = { it.id },
    likeOverlay = { progress -> MyLikeStamp(progress) },
    passOverlay = { progress -> MyNopeStamp(progress) },
    superLikeOverlay = { progress -> MySuperStamp(progress) },
) { ProfileCard(it) }
```

### State

| Field / API | Role |
| --- | --- |
| `currentIndex` | Top card in the stable list |
| `canSwipe` / `canRewind` | Drive action buttons |
| `isAnimating` | Ignore extra input mid-throw |
| `swipe(direction)` | Programmatic discard |
| `rewind()` | Fly the last card back in |

The list is **not** mutated by the library. Swipe advances `currentIndex`; rewind decrements it. Use `onSwiped` for side effects only.

## Sample

`:app` is a Material 3 demo: full-bleed cards, liquid-glass action bar, Super Like, match burst, Discover grid, and Chat from liked profiles.

```
./gradlew :app:assembleDebug
```

## License

Copyright 2015 Aradh Pillai  
Copyright 2026 TinderView contributors

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).
