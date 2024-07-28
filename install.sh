#!/bin/bash

if [ "$EUID" -ne 0 ]; then
    echo "This script must be run as root."
    exit 1
fi

cp cypher /usr/local/bin/
chmod +x /usr/local/bin/cypher

if [ -f /usr/local/lib/cypher.jar ]; then
    echo "Removing old cypher.jar"
    rm /usr/local/lib/cypher.jar
fi

mkdir -p /usr/local/lib
cp build/libs/cypher.jar /usr/local/lib/

echo "Files copied successfully to /usr/local/bin."
