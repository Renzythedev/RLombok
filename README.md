# RLombok 🚀

This project is a simple implementation of an API that automatically generates getter and setter methods, inspired by Lombok. It was developed out of boredom and a desire to explore Java ASM's capabilities. 📦

## Features ✨

- **Automatic Getter/Setter Generation**: Just like Lombok, this API creates getter and setter methods for your classes with minimal effort.
- **ASM-Based**: Utilizes Java bytecode manipulation for a performance-friendly solution.
- **User-Friendly**: Easy to integrate and use, making coding a bit more fun! 🎉

## Motivation 😄

Lombok provides great functionality, but I wanted to create a similar solution using ASM to better understand the inner workings of bytecode manipulation. This project was developed during some idle time and aims to bring the convenience of automatic method generation to Java developers. 

## Installation ⚙️

### Dependency

If you're using Maven, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>me.renzy</groupId>
    <artifactId>RLombok</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage 🛠️

Here's an example of how to use the API:
```java
@Getter
@Setter
private static String s = "Hello world!";
```
