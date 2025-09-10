# uIoT Library Problems

A shared library containing standardized [RFC 7807 Problem Details](https://datatracker.ietf.org/doc/html/rfc7807)
exceptions used across the entire uIoT system. This library ensures consistent error handling and API responses
throughout all microservices.

## Dependencies

This library uses [problem4j-core](https://github.com/malczuuu/problem4j) for RFC 7807 implementation.

```groovy
dependencies {
    api("com.github.malczuuu:problem4j-core:3.0.0-rc4")
}
```

## Usage

Services include this library to provide base exceptions used by HTTP services.

```groovy
dependencies {
    implementation project(":uiot-library-problems")
}
```
