<!--
The MIT License (MIT)

Copyright (c) 2015 Ely Deckers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->

# IOU Android

IOU Android is a Java Promise library built on [iou-android](https://git.brusque.nl/edeckers/iou-android) and adheres to the [A+ spec](https://github.com/promises-aplus/promises-spec) pretty closely, although it has a few 'conveniences' added.

## Maven
-----
```xml
<dependency>
    <groupId>nl.brusque.iou</groupId>
    <artifactId>iou-android</artifactId>
    <version>${version}</version>
</dependency>
```

## Gradle
-----
```
compile 'nl.brusque.iou:iou-android:${version}'
```

Find available versions on [Maven Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22nl.brusque.iou%22%20AND%20a%3A%22iou-android%22).

## Example
-----
Given in all examples:
```java
AndroidIOU iou         = new AndroidIOU();
AndroidPromise promise = iou.getPromise();
```

## Basic call
```java
promise
  .then(
    new AndroidThenCallable() {
      public Object call(Object input) {
        ...
      }
    },
    new AndroidThenCallable() {
      public Object call(Object input) {
        ...
      }
  });

iou.resolve("resolve"); // or iou.reject("reject");
```

## Fail-method call
This code is equivalent to the syntax from the ```Basic call```-section

```java
promise
  .then(
    new AndroidThenCallable() {
      public Object call(Object input) {
        ...
      }
    });
  .fail(
    new AndroidThenCallable() {
      public Object call(Object input) {
        ...
      }
  });

iou.resolve("resolve"); // or iou.reject("reject");
```