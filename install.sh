#!/bin/bash

if [ "$EUID" -ne 0 ]; then
    echo "This script must be run as root."
    exit 1
fi

# Copy the 'cypher' file
cp cypher /usr/local/bin/
chmod +x /usr/local/bin/cypher

# Copy the 'cypher.jar' file
mkdir -p /usr/local/lib
cp build/libs/cypher.jar /usr/local/lib/

echo "Files copied successfully to /usr/local/bin."
