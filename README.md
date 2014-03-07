stevia
======
<p align="right">[![Build Status](https://travis-ci.org/persado/stevia.png?branch=master)](https://travis-ci.org/persado/stevia) </p>

Stevia is an Open Source QA Automation Testing Framework by Persado (www.persado.com). In Persado, we took the pain out of having to care about Selenium or Webdriver (or both) and unified them under a common API, with a sane and clear-cut design, ability to extend and expand (courtesy of Spring!) and a bit of sweetness. Stevia is what we got out of it: 

<p align="center"><img src="https://raw.github.com/persado/stevia/master/doc/stevia-logo.png" width="150"> </p>

## Breaking news

### 07-Mar-2014 Latest developments

It has come to our attention that some of our friends have difficulty downloading stevia from our Open Source repository, Sonatype. The best (and easiest) way to do this is via Maven dependencies; adding the repo to your pom.xml is fairly trivial:

```
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <name>OSS Sonatype Snapshot Repository</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  </repository>
</repositories>	
```

Our latest SNAPSHOT (you can see this by observing the pom.xml) can be added in your dependencies as follows:
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.8.6-SNAPSHOT</version>
</dependency>
```

Enjoy!


### 20-Jan-2014 Upcoming Stevia 0.8.0!
Dear friends, 

We are now getting ready with a 0.8.0 release, with many ideas still in progress and waiting to be merged. For the moment we've changed the way we're instantiating controllers so now you can add your own without the need to recompile. Check our wiki page [here](https://github.com/persado/stevia/wiki/Extending-web-controller-support) for the way to do this. Contact us for more ideas - play with this in our snapshot 0.8.2 version (snapshot repo location in pom.xml):

```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.8.2-SNAPSHOT</version>
</dependency>
```

### 05-Jul-2013 Stevia 0.7.0 uploaded to Maven Central!
Dear friends,

Stevia 0.7.0 has been released to Maven Central today. It will take a couple of hours for synchronisation to work, and in the mean time check the changelog via this link: <a href="https://github.com/persado/stevia/compare/stevia-core-0.6.0...stevia-core-0.7.0">Changelog between 0.6.0 and 0.7.0</a>

As usual, add this dependency on your pom.xml via the following snippet:

```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.7.0</version>
</dependency>
```


### 05-Apr-2013 Stevia 0.6.0 uploaded to Maven Central!
Stevia 0.6.0 has been released to Maven Central! In some hours from now, the synchronization will have completed and you will be able to fetch the latest artefact directly from the Central Repository. Well done to all the team for their work!
To use Stevia, when synchronization is complete, add this to your pom.xml:

```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.6.0</version>
</dependency>
```

### 04-Apr-2013 Tech Document Release for comments
A first release of our technical document is uploaded in the following link: http://goo.gl/Is3lA for user reactions gathering. Please have a good read through and/or comment if you think more details are needed.  The document will stay for a week uploaded for everyone who wishes to comment and all constructive reviews are more than welcome. For any technical inquiries and details of the release, feel free to ping as at stevia-release [at] persado.com

* * *

### Who is Persado <p align="right"><img alt="Persado" width="75" src="http://www.persado.com/templates/youandigraphics/images/logo.png"></p>
Persado is the leader in Marketing Language Engineering: the use of math and big data to unlock the DNA of selling online. 

Persado uses semantic and statistical algorithms to map marketing language and engineer the absolute best online marketing messages. It's proven. With our technology, you sell more. 

Over the past 7 years, we've analyzed billions of consumers' online interactions to identify and map the key components of marketing language. Emotions, product features, calls-to-action, formatting, amongst others, are semantically mapped in our database. For any marketing message, we first model and then produce all possible variations (up to 16 million). Using our statistical algorithms, we identify the absolute best one.




