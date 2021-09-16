# recepta-regis-fse

A Clojure library designed for creating and signing (Xades) HL7-CDA for italian medical records.

- The sign engine is xades_mul.clj. It is a Clojure wrapper for the Digital Signature Service (DSS) libraries. You can find the project [here](https://github.com/esig/dss);

- hl7cda.js creates the CDA from a record. It uses [Selmer](https://github.com/yogthos/Selmer) as a template engine;
## Usage


## License

Copyright © 2021 Filippo Costalli

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
