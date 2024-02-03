#!/usr/bin/env bash


# find the scripts directory regardless of where this script is executed
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"
# navigate to jcr_root folder from the scripts directory.
cd ../core

mvn com.spotify.fmt:fmt-maven-plugin:format
mvn clean install -PautoInstallBundle -DskipScriptPrecompilation=true -Daem.port=4502  -Daem.analyser.skip=true -Dskip.npm -DskipTests

