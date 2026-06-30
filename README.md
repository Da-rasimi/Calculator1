# SciCalc - Android Scientific Calculator

An Android Studio project (Kotlin) implementing:

- **Basic tab**: addition, subtraction, multiplication, division, parentheses
- **Scientific tab**: sin/cos/tan, asin/acos/atan, sinh/cosh/tanh, log, ln,
  sqrt, abs, exponentiation, pi/e constants, DEG/RAD toggle
- **Matrix tab**: 2x2 to 4x4 matrix addition, subtraction, multiplication,
  transpose, determinant (cofactor expansion), inverse (adjugate method)
- **Stats & Combinatorics tab**: nPr, nCr, factorial, and descriptive
  statistics (count, sum, mean, median, mode, min, max, range, variance,
  standard deviation) on a comma-separated data set

## How to open
1. Open this folder in Android Studio (Hedgehog or later).
2. Let Gradle sync (it will download the Gradle wrapper / AGP / Kotlin plugin).
3. Run on an emulator or device (minSdk 23).

## Project structure
- `ExpressionEvaluator.kt` — recursive-descent parser/evaluator used by
  both the Basic and Scientific calculator screens (shared engine, so
  scientific functions automatically work as plain math expressions, e.g.
  `sin(30) + sqrt(16) * 2`).
- `MatrixUtil.kt` — matrix algebra (add/subtract/multiply/transpose/
  determinant/inverse) for up to 4x4 matrices.
- `StatsUtil.kt` — combinatorics (nPr, nCr, n!) and descriptive statistics.
- `MainActivity.kt` + `CalcPagerAdapter.kt` — tabbed (ViewPager2 + TabLayout)
  navigation between the four fragments.
- `BasicCalculatorFragment.kt`, `ScientificCalculatorFragment.kt`,
  `MatrixFragment.kt`, `StatsCombinatoricsFragment.kt` — the four screens.

## Notes for further extension
- Add a `MemoryFragment` / M+ M- MR buttons by storing a `Double` in
  `MainActivity` and passing it via a shared `ViewModel`.
- Replace inverse's adjugate method with Gauss-Jordan elimination if you
  want better numerical stability for larger matrices.
- Add unit tests under `app/src/test/java/com/example/scicalc/` for
  `ExpressionEvaluator`, `MatrixUtil`, and `StatsUtil` — they're plain
  Kotlin classes with no Android dependencies, so they're trivially
  testable with JUnit.
