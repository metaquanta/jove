jove
====
Java Opencv Visualization Engine
-or-
Jmonkey OpencV Environment
-or-
(recursively) Jove OpencV Experimenter

opencv visualization in jMonkey

Process video streams, parallelized, with ease.

See the (Scala) examples in com.metaquanta.jove.examples for usage

To run the examples:

$ sbt run

The following VM options are suggests:

JAVA_OPTS="-XX:+CMSClassUnloadingEnabled -Xms512m -Xmx512m -XX:MaxDirectMemorySize=2048M -XX:MaxPermSize=384m -XX:ReservedCodeCacheSize=192m -Dfile.encoding=UTF8"

Most are merely taken from sbt defaults. However, we are intentionally lowering the heap size while raising the direct memory size. The lower heap value is needed to prompt the garbage collector to more aggressively free direct memory.
