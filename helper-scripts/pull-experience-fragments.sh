#!/usr/bin/env bash

# find the scripts directory regardless of where this script is executed
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"

source ./experience-fragments.sh get