#!/usr/bin/env bash

# uncomment this line if you want debug output
# set -o xtrace

action=$1

GREEN="\033[1;32m"
NOCOLOR="\033[0m"

# find the scripts directory regardless of where this script is executed
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"
# navigate to jcr_root folder from the scripts directory.
cd ../ui.content/src/main/content/jcr_root

declare -a paths=(
        "./conf/kpmg/settings/wcm/policies"
        "./conf/kpmg/settings/wcm/templates/page-content/policies"
        "./conf/kpmg/settings/wcm/templates/page-content/structure"
        "./conf/kpmg/settings/wcm/templates/page-content/initial"
        "./conf/kpmg/settings/wcm/templates/xf-web-variation/policies"
        "./conf/kpmg/settings/dam/cfm/models"
        "./content/dam/kpmg-style-guide"
        "./content/kpmg-style-guide"
        "./content/experience-fragments/kpmgpublic/language-masters/en/site"
        "./etc/mobile/groups/kpmg"
)

## Run command for each path
for path in "${paths[@]}"
do
    echo -n $action "$path"
    output=`./repo $action -f  $path 2>&1` || echo $output
    echo -e "${GREEN} ...done${NOCOLOR}"
done