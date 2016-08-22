(defproject org.toomuchcode/clara-rules "0.12.0"
  :description "Clara Rules Engine"
  :url "https://github.com/rbrush/clara-rules"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [prismatic/schema "1.0.1"]]
  :profiles {:dev {:dependencies [[org.clojure/math.combinatorics "0.1.3"]]}
             :provided {:dependencies [[org.clojure/clojurescript "1.7.170"]]}}
  :plugins [[lein-codox "0.9.0" :exclusions [org.clojure/clojure]]
            [lein-javadoc "0.2.0" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.1.3" :exclusions [org.clojure/clojure]]
            [lein-figwheel "0.5.2" :exclusions [org.clojure/clojure]]]
  :codox {:namespaces [clara.rules clara.rules.dsl clara.rules.accumulators
                       clara.rules.listener clara.rules.durability
                       clara.tools.inspect clara.tools.tracing]
          :metadata {:doc/format :markdown}}
  :javadoc-opts {:package-names "clara.rules"}
  :source-paths ["src/main/clojure"]
  :resource-paths []
  :test-paths ["src/test/clojure" "src/test/common"]
  :java-source-paths ["src/main/java"]
  :javac-options ["-target" "1.6" "-source" "1.6"]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [;; Simple mode compilation for tests.
                       {:id "figwheel"
                        :source-paths ["src/test/clojurescript" "src/test/common"]
                        :figwheel true
                        :compiler {:main "clara.test"
                                   :output-to "resources/public/js/simple.js"
                                   :output-dir "resources/public/js/out"
                                   :asset-path "js/out"
                                   :optimizations :none}}

                       {:id "simple"
                        :source-paths ["src/test/clojurescript" "src/test/common"]
                        :compiler {:output-to "target/js/simple.js"
                                   :optimizations :whitespace}}

                       ;; Advanced mode compilation for tests.
                       {:id "advanced"
                        :source-paths ["src/test/clojurescript" "src/test/common"]
                        :compiler {:output-to "target/js/advanced.js"
                                   :optimizations :advanced}}]

              :test-commands {"phantom-simple" ["phantomjs"
                                                "src/test/js/runner.js"
                                                "src/test/html/simple.html"]

                              "phantom-advanced" ["phantomjs"
                                                  "src/test/js/runner.js"
                                                  "src/test/html/advanced.html"]}}
  
  ;; Factoring out the duplication of this test selector function causes an error,
  ;; perhaps because Leiningen is using this as uneval'ed code.
  ;; For now just duplicate the line.
  :test-selectors {:default (complement (fn [x]
                                          (some->> x :ns ns-name str (re-matches #"^clara\.generative.*"))))
                   :generative (fn [x] (some->> x :ns ns-name str (re-matches #"^clara\.generative.*")))}
  
  :scm {:name "git"
        :url "https://github.com/rbrush/clara-rules"}
  :pom-addition [:developers [:developer
                              [:id "rbrush"]
                              [:name "Ryan Brush"]
                              [:url "http://www.toomuchcode.org"]]]
  :deploy-repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]])
