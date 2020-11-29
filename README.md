# clj-tic-tac-toe

[![Build Status][gh-actions-badge]][gh-actions] [![Clojure version][clojure-v]](project.clj)

Command line tic tac toe.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Building

* Clone the project
* Build the uberjar

```bash
lein uberjar
```

## Usage

* Run the application

  * Lein command

    ```bash
    lein run
    ```

  * Java command

    ```bash
    java -jar target/uberjar/tic_tac_toe-0.1.0-SNAPSHOT-standalone.jar
    ```

## Examples

```text
lein run

Welcome to Tic-Tac-Toe!
  1   2   3 (col)
| --- | --- | --- | (row) |
| --- | --- | --- |1
| -   | -   | -   | 2 |
| --- | --- | --- |3
|---|---|---|
-> Player turn: x
Enter col:2
Enter row:2
  1   2   3 (col)
| --- | --- | --- | (row) |
| --- | --- | --- |1
| -   | x   | -   | 2 |
| --- | --- | --- |3
|---|---|---|
-> Player turn: o
Enter col:3
Enter row:3
  1   2   3 (col)
| --- | --- | --- | (row) |
| --- | --- | --- |1
| -   | x   | - | 2 |
| --- | --- || o   | 3   |
| --- | --- |  |
-> Player turn: x
Enter col:2
Enter row:1
  1   2   3 (col)
| --- | --- | --- | (row) |
| --- || x   | - | 1 |
| --- || x   | -   | 2 |
| --- | --- || o   | 3   |
| --- | --- |  |
-> Player turn: o
Enter col:3
Enter row:2
  1   2   3 (col)
| --- | --- | --- | (row) |
| --- || x   | - | 1 |
| --- || x   | o   | 2 |
| --- | --- || o   | 3   |
| --- | --- |  |
-> Player turn: x
Enter col:2
Enter row:3
  1   2   3 (col)
| --- | --- | --- | (row) |
| --- || x   | - | 1 |
| --- || x   | o | 2 |
| --- || x   | o   | 3   |
| --- | --- | --- |
nil
And the Winner is: x
Winning move was on: col2
```

## License

Copyright © 2020 Bill Howe

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
`http://www.eclipse.org/legal/epl-2.0`.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at `https://www.gnu.org/software/classpath/license.html`.

----

<!-- Named page links below: /-->

[gh-actions-badge]: https://github.com/wdhowe/clj-tic-tac-toe/workflows/ci%2Fcd/badge.svg
[gh-actions]: https://github.com/wdhowe/clj-tic-tac-toe/actions
[clojure-v]: https://img.shields.io/badge/clojure-1.10.0-blue.svg
