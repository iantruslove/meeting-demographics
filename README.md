# Demographics

A little web app to capture and record basic demographic data for meeting attendees.

## Prerequisites

* Leiningen 2

## Usage

`lein run -m demographics.system 8080` runs the server app on port 8080

## Dev Usage

* Fire up a `lein repl` in the project root
* Initialize the app with `(init)`
* Compile resources and start webserver with `(start)`
* Shut down the webserver with `(stop)`

## TODO

* Basic UI - mobile  
  * ✓ user clicks a +1 button
  * ☐ button enters a spinny state
  * ☐ +1 for the tuple pair sent to the server
  * ☐ server receives the +1
  * ☐ server increments the datum in its data models, and persists to storage
  * ☐ server updates its representation of the overall totals
  * ☐ server responds with new recalculated totals
  * ☐ clicked button re-renders with updated total
* ☐ Basic persistence
* ☐ Authentication

## License

Copyright © 2013 Ian Truslove

Distributed under the Eclipse Public License, the same as Clojure.
