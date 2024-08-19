#!/bin/bash

if [ "$EUID" -ne 0 ]; then
    echo "This script must be run as root."
    exit 1
fi

./gradlew shadowjar

cp cypher /usr/local/bin/
chmod +x /usr/local/bin/cypher

if [ -f /usr/local/lib/cypher.jar ]; then
    echo "Removed old cypher.jar"
    rm /usr/local/lib/cypher.jar
fi

mkdir -p /usr/local/lib
cp build/libs/cypher.jar /usr/local/lib/

echo "Build copied successfully to /usr/local/bin."
