#!/bin/sh

#export DOCKERHOST=$(ifconfig | grep -E "([0-9]{1.3}\.){3}[0-9]{1,3}" | awk '{print $2}' | cut -f2 -d: | head -n1)
#export DOCKERHOST=$(ifconfig | grep -E "([0-9]{1.3}\.){3}[0-9]{1,3}" | awk '{print $2}' | cut -f2 -d: | tail -n1)
#export DOCKERHOST=192.168.43.88
export DOCKERHOST=192.168.1.111

docker-compose up
