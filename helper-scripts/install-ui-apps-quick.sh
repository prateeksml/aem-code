#!/usr/bin/env bash

mvn com.spotify.fmt:fmt-maven-plugin:format
mvn clean install -PautoInstallPackage -DskipScriptPrecompilation=true -Daem.port=4502  -Daem.analyser.skip=true -Dskip.npm -DskipTests
