![Release](https://jitpack.io/v/maozzz/gettextCommons.svg)


# Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
    
<dependency>
    <groupId>com.github.maozzz</groupId>
    <artifactId>gettextCommons</artifactId>
    <version>1.0</version>
</dependency>
```


# Gradle
    
```groovy

allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
	   implementation 'com.github.maozzz:gettextCommons:x.x.x'
}
```

# Usage

```java
// variable for our GettextResourceBundles
Hashtable<Locale, GettextResourceBundle> resources = new Hashtable<>();

// read binary .mo file
MoParser moParser = new MoParser();
InputStream is = Main.class.getResourceAsStream("/ru.mo");
List<Entry> entries = moParser.read(is);

// example for 1 only language
Locale ru = Locale.forLanguageTag("ru");
resources.put(ru, new GettextResourceBundle(entries, ru, new PluralAbleRu()));

GettextTranslator
Gettext gettextTranslator = new Gettext();
gettextTranslator.setBundles(resources);

System.out.println(gettextTranslator._(ru, "About"));
System.out.println(gettextTranslator._n(new Locale("ru"), "%s photo", 17L));
```

will printed:

```sh
О себе
17 фотографий
```

# gettextTranslator doc
http://www.gnu.org/software/gettextTranslator/manual/gettextTranslator.pdf

!!! page 113 for more details
