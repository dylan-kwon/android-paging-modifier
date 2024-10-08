# Android Paging Modifier

Paging Modifier is a library that helps you easily perform Create, Update, and Delete operations on Paging
without Room.

## Necessity

Jetpack Paging is generally very convenient for loading continuous data, but it is challenging to add, modify, or delete
already loaded data. Therefore, Paging typically invalidates the existing PagingSource and reloads from the current page
to handle such operations.

However, invalidating and reloading all pages even for small changes is inefficient, especially when networking is used
in the PagingSource implementation. This can lead to wasted costs for both the service provider and users.

The simplest way to solve this problem is by using Room. Even if invalidate is called to refresh the PagingSource, it
uses the already cached data in the local storage, addressing the cost issue. But, sometimes using Room can be
burdensome due to time constraints and other reasons.

This library helps you easily modify already loaded PagingData without invalidating it, all without using Room.

## Preview

<video src="https://github.com/user-attachments/assets/d1fa0f10-65af-4d23-be2e-0c8933b1d41c"></video>

## Installation

```kotlin
dependencyResolutionManagement {
    ..
    repositories {
        ..
        maven("https://maven.pkg.github.com/dylan-kwon/android-paging-modifier") {
            credentials {
                username = INPUT_YOUR_GITHUB_USER_NAME
                password = INPUT_YOUR_GITHUB_TOKEN
            }
        }
    }
}
```

```kotlin
implementation("dylan.kwon:paging-modifier-android:$version")
```

## How To Use

### Create PagerFlow

Create a pager. The method for creating it is the same as before using this library.

```kotlin
private val pager = Pager(
    config = PagingConfig(yourSettings)
) {
    YourDataPagingSource()
}
```

### Create PagingDataModifier

Create a `PagingDataModifier`. Set the generic type to be the same as the type used in the pager. For reference,
the data type specified by the generic must type include a unique key to identify instances. Unique key is used as a
basis for determining the update, deletion, and insertion positions of the data.

```kotlin
private val modifier = PagingDataModifier<YourDataType>()
```

### Combine PagerFlow with Modifier

Combine Flow<PagingData> and PagingDataModifier using the modifier function. Check the comments in the example code for
notes.

```kotlin
val pagingData = pager.flow

    // must call cachedIn before calling modifier.
    // If you do not call it, an exception will be thrown.
    .cachedIn(viewModelScope)

    // Combines Flow<PagingData> with Modifier.
    // This is a data identifier and must be unique.
    .modifier(modifier) { yourData ->
        yourData.id
    }

    // (Optional) Add additional tasks if necessary for example map.
    .doSomethings()

    // (Recommendation) Invoke cachedIn to cache the operations upstream.
    .cachedIn(viewModelScope)
```

### Insert

Insert `newData` at the position where the ID of the next data is 2.

```kotlin
modifier.insert(
    data = newData,
    condition = { beforeData, afterData ->
        afterData?.id == 2
    }
)
```

### Insert Header

Add data to the top of the list.

```kotlin
modifier.insertHeader(newData)
```

### Insert Footer

Add data to the bottom of the list.

```kotlin
modifier.insertFooter(newData)
```

### Update

Update with new data. The update criterion is the identifier of the object.

```kotlin
modifier.update(
    data.copy(
        createdAt = System.currentTimeMillis()
    )
)
```

### Delete

Delete the data. The deletion criterion is the identifier of the object.

```kotlin
modifier.delete(data)
```

## License

This project is licensed under the Apache License, Version 2.0. - see the [LICENSE](app/LICENSE.txt)
file for details.