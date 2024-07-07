#!/bin/sh
export DOCKERHOST=$(ifconfig | grep -A2 docker0 | grep -E "[0-9]{1,3}\." | awk '{print $2}' | cut -f2 -d:)
docker-compose up
