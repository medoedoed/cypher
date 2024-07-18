#!/bin/bash

if [ "$EUID" -ne 0 ]; then
    echo "This script must be run as root."
    exit 1
fi

# Copy the 'cypher' file
cp cypher /usr/local/bin/
chmod +x /usr/local/bin/cypher

# Copy the 'cypher.jar' file
mkdir -p /usr/local/bin/lib
cp build/libs/cypher.jar /usr/local/bin/lib/

echo "Files copied successfully to /usr/local/bin."
