(defproject recepta-regis-fse "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [cprop "0.1.16"]
                 [selmer "1.12.24"]
                 [clj-time "0.15.2"]
                 [clj-http "3.10.1"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "42.2.9"]
                 [eu.europa.ec.joinup.sd-dss/dss-enumerations "5.6"]
                 [eu.europa.ec.joinup.sd-dss/dss-jaxb-parsers "5.6"]
                 [eu.europa.ec.joinup.sd-dss/dss-utils "5.6"]
                 [eu.europa.ec.joinup.sd-dss/dss-utils-apache-commons "5.6"]
                 [eu.europa.ec.joinup.sd-dss/dss-model "5.6"]
                 [eu.europa.ec.joinup.sd-dss/dss-token "5.6"]
                 [eu.europa.ec.joinup.sd-dss/dss-xades "5.6"]
                 [jarohen/chime "0.3.2"]
                 [cambium/cambium.core "0.9.3"]
                 [cambium/cambium.codec-simple "0.9.3"]
                 [cambium/cambium.logback.core "0.4.3"]]
  :repositories {"cefdigital cefdigital" "https://ec.europa.eu/cefdigital/artifact/content/repositories/esignaturedss/"}
  :java-source-paths ["src/java" "test/java"]
  :resource-paths ["resources" "resources/bcprov-jdk15on-1.64.jar" "resources/bcpkix-jdk15on-1.64.jar"]
  :main recepta-regis-fse.core
  :repl-options {:init-ns recepta-regis-fse.core})
