This Repository is a Fork of WikiClean
=========
[![Build Status](https://travis-ci.org/lintool/wikiclean.svg?branch=master)](https://travis-ci.org/lintool/wikiclean)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.wikiclean/wikiclean/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.wikiclean/wikiclean)
[![LICENSE](https://img.shields.io/badge/license-Apache-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0)

WikiClean is a Java Wikipedia markup to plain text converter. It takes [Wikipedia XML dumps](http://en.wikipedia.org/wiki/Wikipedia:Database_download) with articles in wikimedia markup and generates clean plain text.

Why?
----

For text processing applications, we often need access to plain text, unadulterated by wikimedia markup. This is surprisingly non-trivial, as Wikipedia articles are full of complexities such as references, image captions, tables, infoboxes, etc., which are not useful for many applications.

Before setting out to write this package, I explored many of the Java alternatives for parsing Wikipedia pages described
[here](http://www.mediawiki.org/wiki/Alternative_parsers) and found none of them to be adequate (in generating clean plain text). The primarily challenge is that many of these packages aspire to be complete Wikipedia parsers and renders, whereas WikiClean was designed with a much simpler goal.

Usage
-----

It's simple to use WikiClean:

```
WikiClean cleaner = new WikiClean.Builder().build();
String content = cleaner.clean(raw);
```

Where `raw` is the raw Wikpedia XML.

The builder allows you to specify a few options:

* `withTitle` to specify whether to prepend the article title in the plain text.
* `withFooter` to specify whether to keep the sections "See also", "Reference", "Further reading", and "External links".

By default, both options are set to false.

Also, use `withLangauge` to set the language. Currently, four are supported:

* `WikiLanguage.EN`: English (default)
* `WikiLanguage.DE`: German
* `WikiLanguage.ZH`: Chinese
* `WikiLanguage.KN`: Kannada

Contributions for providing additional language support welcome!

Putting everything together, the default builder is equivalent to:

```
WikiClean cleaner =
    new WikiClean.Builder()
        .withLanguage(WikiLanguage.EN)
        .withTitle(false)
        .withFooter(false).build();
String content = cleaner.clean(raw);
```

Sample command-line invocation to read a Wikipedia dump and output plain text:

```
mvn exec:java -Dexec.mainClass=org.wikiclean.WikipediaArticlesDump \
  -Dexec.args="-input enwiki-20161220-pages-articles.xml.bz2" | less
```
<br>

### Generating .jar file with dependencies
For build the .jar file with all the dependencies run: ```mvn clean compile assembly:single```

Maven Artifacts
---------------

Latest releases of Maven artifacts are available at [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cwikiclean).

License
-------

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
