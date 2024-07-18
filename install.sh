#!/bin/bash

if [ "$EUID" -ne 0 ]; then
    echo "This script must be run as root."
    exit 1
fi

cp cypher /usr/local/bin/
chmod +x /usr/local/bin/cypher

mkdir -p /usr/local/lib
cp build/libs/cypher.jar /usr/local/lib/

echo "Files copied successfully to /usr/local/bin."
