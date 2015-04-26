# Demographics

A little web app to capture and record basic demographic data for meeting attendees.

## Prerequisites

* Leiningen 2

## Usage

`lein run -m demographics.system 8080` runs the server app on port 8080

## Dev Usage

### Server (clj)

* Fire up a `lein repl` in the project root
* Initialize the app with `(init)`
* Compile resources and start webserver with `(start)`
* Shut down the webserver with `(stop)`

### Client (cljs)

Run the server part, e.g. `lein ring server`, or like above.

One way to do client-side work is to edit the .cljs files and do `lein cljsbuild auto` to monitor the .cljs source files and autocompile the JavaScript.
Browser refreshes are necessary.

Another way is to live-update the code in the browser using a combination of the clojurescript browser repl, and piggieback or nREPL integration.
For a browser repl:
* Fire up another nREPL in emacs
* Into nREPL, enter:
  * `(require 'cljs.repl.browser)`
  * `(cemerick.piggieback/cljs-repl :repl-env (cljs.repl.browser/repl-env :port 9000))`
* Load http://localhost:8000 - it should connect to nREPL via port 9000

## TODO

* ☐ User can change the row and column headings
* ☐ After logging in, user can save a link to the current meeting to their profile, and be able to easily see tracked meetings.
* Basic UI - mobile
  * ✓ user clicks a +1 button
  * ☐ button enters a spinny state
  * ✓ +1 for the tuple pair sent to the server
  * ✓ server receives the +1
  * ☐ server increments the datum in its data models, and persists to storage
  * ✓ server updates its representation of the overall totals
  * ✓ server responds with new recalculated totals
  * ✓ clicked button re-renders with updated total
* ☐ Basic persistence over a server restart
* ☐ Authentication

## License

Copyright © 2013 Ian Truslove

Distributed under the Eclipse Public License, the same as Clojure.
