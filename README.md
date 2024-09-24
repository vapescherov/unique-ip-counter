Task
----

You have a simple text file with IPv4 addresses. One line is one address, line by line:

```
145.67.23.4
8.34.5.23
89.54.3.124
89.54.3.124
3.45.71.5
...
```

The file is unlimited in size and can occupy tens and hundreds of gigabytes.

You should calculate the number of __unique addresses__ in this file using as little memory and time as possible.
There is a "naive" algorithm for solving this problem (read line by line, put lines into HashSet).
It's better if your implementation is more complicated and faster than this naive algorithm.

Before submitting an assignment, it will be nice to check how it handles
this [file](https://ecwid-vgv-storage.s3.eu-central-1.amazonaws.com/ip_addresses.zip). Attention - the file weighs about
20Gb, and unzips to about 120Gb.

Solution
--------

1. Avoid creating String objects when parsing file to reduce garbage collection overhead.
2. Use efficient representation of IPv4. (primitive int instead of String)
3. Utilize a BitSet to store IPs efficiently. (1 bit * 2<sup>32</sup> (all possible values) = 512 MB)

I attempted to abstract the reading logic to return an `IntStream` (`IntStreamingCounter`). However, this approach
was a bit slower compared to reading the file within a while loop (`ByteBufferUniqueIPCounter`).

Usage
-----

```
mvn clean package
java -jar ./application/target/application-1.0.jar ./ip_addresses
```

Potential improvements
----------------------

1. If the file content is guaranteed to be valid, eliminate exception checks to improve performance.
2. The CPU is a bottleneck in the current implementation, so we can improve performance by parallelizing file processing
   using multiple threads.
3. To fully benefit from multithreading, efficient thread-safe version of IntSet is needed.

