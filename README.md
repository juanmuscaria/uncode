# Uncode
Command line tool for reducing jar files to its basic public symbols, suitable as dependency, but not for JVM.

All jar resources that are not classes are also removed.

# Usage
Uncode usage is pretty straight forward, run `uncode <jar-file>` to process a jar file.
Additionally, you can run `uncode --help` to see all available options.

# Build & Install
Some distribution types may require specific java tooling, I recommend using [SDKMAN](https://sdkman.io/) to install the required tools.

### Building all distributions
* Requirements:
  * [GraalVM CE Java 17](https://www.graalvm.org/downloads/)
  * [Native Image](https://www.graalvm.org/22.1/reference-manual/native-image/#install-native-image)
* Building:
  * Run `./gradlew buildAllDistributions`
  * It will produce the following distributions in `<project root>/build/distributions/`: 
  * `uncode` - Native executable (Linux, MacOS)
  * `uncode.exe` - Native executable (Windows)
  * `uncode-<version>.deb` - Debian native package (Linux)
  * `uncode-<version>.rpm` - RPM native package (Linux)
  * `uncode-<version>.zip` - Generic build (All platforms, requires jre 17)
  * `uncode-shadow-<version>.zip` - Generic optimized build (All platforms, requires jre 17)

### Building generic distributions
* Requirements:
  * Java Development Kit 17
* Building:
  * Run `./gradlew buildGenericDistributions`
  * It will produce the following distributions in `<project root>/build/distributions/`:
  * `uncode-<version>.zip` - Generic build (All platforms, requires jre 17)
  * `uncode-shadow-<version>.zip` - Generic optimized build (All platforms, requires jre 17)

### Installing
* Native distribution:
  * For linux, you can install the native deb or rpm package or drop the `uncode` binary in your `PATH`.
  <!--TODO MACOS -->
  * For windows, you can drop the `uncode.exe` binary in your `Path`.
* Generic distribution:
  * You need to install java 17 and have your `JAVA_HOME` set.
  * Unpack `uncode-shadow-<version>.zip` in the place you would like to install it.
  * Add the bin directory to your `PATH` (`Path` System Variable on Windows).

