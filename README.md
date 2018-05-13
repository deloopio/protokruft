# protokruft

Protokruft is a gradle plugin for generating a Kotlin DSL from generated Java Protobuf message source.

Given this proto:
```proto
syntax = "proto3";

option java_package = "org.protokruft.example1";
package AnExample1;

message Car {
    string model = 1;
    Engine engine = 2;
}

message Engine {
    int32 cc = 1;
    int32 bhp = 2;
}
```
The generated Java code from the Google protoc would be used like this:

```kotlin
    val person = Person.newBuilder()
            .setName("Hello Kitty")
            .setAddress(
                    Address.newBuilder()
                            .setNumber(123)
                            .setStreet("Hello Kitty Street")
                            .setPostcode("N304SD")
                            .build())
            .build()
```

Sprinkle on some Protokruft, and you can use it like this:
```kotlin
    val person = newPerson {
        name = "Hello Kitty"
        address = newAddress {
            number = 123
            street = "Hello Kitty Street"
            postcode = "N304SD"
        }
    }
```