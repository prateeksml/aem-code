#!/usr/bin/env bash

# For Development Purposes only

action=put

GREEN="\033[1;32m"
NOCOLOR="\033[0m"

# find the scripts directory regardless of where this script is executed
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"
# navigate to jcr_root folder from the scripts directory.
cd ../ui.apps/src/main/content/jcr_root

declare -a paths=(
        "./apps/kpmg/components"
        "./apps/kpmg/clientlibs"
        "./apps/kpmg/emulators"
)

## Run command for each path
for path in "${paths[@]}"
do
    echo -n $action "$path"
    output=`./repo $action -f  $path 2>&1` || echo $output
    echo -e "${GREEN} ...done${NOCOLOR}"
done