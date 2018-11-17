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

// set up Gettext object for translate
Gettext gettext = new Gettext();
gettext.setBundles(resources);

System.out.println(gettext._(ru, "About"));
System.out.println(gettext._n(new Locale("ru"), "%s photo", 17L));
```

will printed:

```sh
О себе
17 фотографий
```

# gettext doc
http://www.gnu.org/software/gettext/manual/gettext.pdf

!!! page 113 for more details
