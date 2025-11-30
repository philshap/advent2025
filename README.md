Advent 2025 in Java

To run from the command line:

- get the AoC session token by loading any AoC input page in a web browser
  and copy the value of the `session` Cookie.

- compile:
```shell
cd src
javac -d out src/advent2025/*.java
```

- run
```shell
SESSION_TOKEN="<AoC session token>" java -cp out advent2025.Main
```

- run tests
 ```shell
java -cp out advent2025.Main test
```
