<!--
The MIT License (MIT)

Copyright (c) 2016 Ely Deckers

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

IOU Android is a promise-library that is based on [IOU Core](https://github.com/ioweyou/iou-core) and aside from some extensions (e.g. `IOUAndroid.when` and `AndroidPromise.fail`) adheres to the [A+ spec](https://github.com/promises-aplus/promises-spec) as closely as possible.

## Maven
-----
```xml
<dependency>
    <groupId>nl.brusque.iou</groupId>
    <artifactId>iou-android</artifactId>
    <version>1.0.0-beta-02</version>
</dependency>
```

## Gradle
-----
```
compile 'nl.brusque.iou:iou-android:1.0.0-beta-02@aar' { transitive = true }
```

## Example
-----
### Scopes
Since not all calls are allowed to execute on the UI thread (e.g. network), you will have to specify per `then` whether it will run on the UI- or a background-tread, like so:
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
    .then(new AndroidThenCallable<Integer, Void>(AndroidPromise.ExecutionScope.BACKGROUND) {
       ...
    });
```
Or more verbose
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
    .then(new AndroidThenCallable<Integer, Void>() {
        @Override
        public AndroidPromise.ExecutionScope getExecutionScope() {
            return AndroidPromise.ExecutionScope.BACKGROUND;
        }

        ...
    });
```
The IOUAndroid-constructor requires an `activity`, because it is used to invoke calls on the UI-thread with `activity.runOnUiThread`.

### Call with single then
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
        .then(new AndroidThenCallable<Integer, Void>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public Void apply(Integer input) throws Exception {
                Log.i(TAG, input.toString());

                return null;
            }
        });

iou.resolve(42); // prints "42"
```
Or more verbose (note: this notation works for all examples, but I only demonstrate it here)
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
        .then(new AndroidThenCallable<Integer, Void>() {
            public AndroidPromise.ExecutionScope getExecutionScope() {
                return AndroidPromise.ExecutionScope.BACKGROUND;
            }

            @Override
            public Void apply(Integer input) throws Exception {
                Log.i(TAG, input.toString());
                return null;
            }
        });

iou.resolve(42); // prints "42"
```
Or using `when` (note: this notation works for all examples, but I only demonstrate it here)
```java
  IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

  iou.when(42)
      .then(new AndroidThenCallable<Integer, Void>() {
              public AndroidPromise.ExecutionScope getExecutionScope() {
                  return AndroidPromise.ExecutionScope.BACKGROUND;
              }

              @Override
              public Void apply(Integer input) throws Exception {
                  Log.i(TAG, input.toString());
                  return null;
              }
          }); // prints "42"
```
### Chained or piped promise
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
        .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public Integer apply(Integer input) throws Exception {
                return input * 10;
            }
        })
        .then(new AndroidThenCallable<Integer, String>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public String apply(Integer input) throws Exception {
                return String.format("The result: %d", input);
            }
        })
        .then(new AndroidThenCallable<String, Void>(AndroidPromise.ExecutionScope.UI) {
            @Override
            public Void apply(String input) throws Exception {
                Log.i(TAG, input);

                return null;
            }
        });

iou.resolve(42); // prints "The result: 420"
```
### Rejecting a promise
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
        .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.UI) {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 42;
            }
        })
        .fail(new AndroidThenCallable<Object, Void>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public Void apply(Object input) throws Exception {
                Log.i(TAG, String.format("%s I can't do that.", input));

                return null;
            }
        });

iou.reject("I'm sorry, Dave."); // prints "I'm sorry, Dave. I can't do that."
```
Or if you like the A+ way better
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
        .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.UI) {
            @Override
            public Integer apply(Integer input) throws Exception {
                return input * 42;
            }
        }, new AndroidThenCallable<Object, Integer>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public Integer apply(Object input) throws Exception {
                Log.i(TAG, String.format("%s I can't do that.", input));

                return null;
            }
        });

iou.reject("I'm sorry, A+."); // prints "I'm sorry, A+. I can't do that."
```
### Failing a promise
```java
IOUAndroid<Integer> iou = new IOUAndroid<>(getActivity());

iou.getPromise()
        .then(new AndroidThenCallable<Integer, Integer>(AndroidPromise.ExecutionScope.BACKGROUND) {
            @Override
            public Integer apply(Integer input) throws Exception {
                throw new Exception("I just don't care.");
            }
        })
        .then(new AndroidThenCallable<Integer, Void>(AndroidPromise.ExecutionScope.UI) {
            @Override
            public Void apply(Integer input) throws Exception {
                Log.i(TAG, "What would you say you do here?");

                return null;
            }
        })
        .fail(new AndroidThenCallable<Object, Void>() {
            @Override
            public Void apply(Object reason) throws Exception {
                Log.i(TAG,
                    String.format("It's not that I'm lazy, it's that %s",
                            ((Exception)reason).getMessage()));

                return null;
            }
        });

iou.resolve(42); // prints "It's not that I'm lazy, it's that I just don't care."
```
