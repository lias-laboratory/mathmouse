#!/bin/sh


# Create Rabbitmq user
( sleep 5 ; \
cd /tmp ; \
wget http://localhost:15672/cli/rabbitmqadmin ; \
mv ./rabbitmqadmin /rabbitmqadmin ; \
chmod +x /rabbitmqadmin ; \
rabbitmqctl add_user dbguisender dbguisender 2>/dev/null ; \
rabbitmqctl set_permissions -p / dbguisender  ".*" ".*" ".*" ; \
rabbitmqctl add_user dbguireceiver dbguireceiver 2>/dev/null ; \
rabbitmqctl set_permissions -p / dbguireceiver ".*" ".*" ".*") &

rabbitmq-server $@
