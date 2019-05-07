## FileManager

FileManager was designed to allow user easy to access Files in Project with many features extension.

## Features

* Easy to explore files/folders in the local disk
* Filtering file based on their information
* Easy to add new folder
* Easy to manage files/folders
	* Delete selected files/folders in manual way
   * Delete selected files/folders basing on predefined rules

## Dependencies

* RxAndroid <https://github.com/ReactiveX/RxAndroid>

## Installation

```
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
compile 'io.reactivex.rxjava2:rxjava:2.0.1'
```

## Requirements

* Android studio
* Gradle:2.2.3
* MminSdkVersion 14
* TargetSdkVersion 23

## Limitation
* This library use ShareReference to manage file's attributes.
## Usage

-  We need to call function init first, recommend in *create Application or create Activity*

```
	FileConfig config = FileConfig(homePath,application)
	FileManager.getInstance().init(config);
```

- Delete file

```
	FileManager.getInstance().delete(String filePath)
```

- Rename file

```
	FileManager.getInstance().rename(String filePath)
```

- Copy file

```
	FileManager.getInstance().copy(String filePath)
```

- Zip file

```
	FileManager.getInstance().zipFile(String filePath)
```
- Extract file

```
	FileManager.getInstance().extractFile(String zipPath, String desDir)
```
- Create directory

```
	FileManager.getInstance().createDir(String parentPath, String name)
```

- Filter files

```
	FilterByName rule = new FilterByName(String key);
	FileManager.getInstance().filter(dirPath,rule);
```

## History
Version: 1.0

* Initialize File Manager

## Credits
First version by Minh Pham
## License
Copyright (C) freehand <http://www.freehand.com/>