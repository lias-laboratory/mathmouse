#!/bin/sh

# Create Rabbitmq user
rabbitmqctl add_user comparatorsender comparatorsender 2>/dev/null
rabbitmqctl set_permissions -p / comparatorsender  ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user comparatorreceiver comparatorreceiver 2>/dev/null
rabbitmqctl set_permissions -p / comparatorreceiver ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user dockermanagersender dockermanagersender 2>/dev/null
rabbitmqctl set_permissions -p / dockermanagersender  ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user dockermanagerreceiver dockermanagerreceiver 2>/dev/null
rabbitmqctl set_permissions -p / dockermanagerreceiver ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user dbguisender dbguisender 2>/dev/null
rabbitmqctl set_permissions -p / dbguisender  ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user dbguireceiver dbguireceiver 2>/dev/null
rabbitmqctl set_permissions -p / dbguireceiver ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user dbcoresender dbcoresender 2>/dev/null
rabbitmqctl set_permissions -p / dbcoresender  ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user dbcorereceiver dbcorereceiver 2>/dev/null
rabbitmqctl set_permissions -p / dbcorereceiver ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user populatorsender populatorsender 2>/dev/null
rabbitmqctl set_permissions -p / populatorsender ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user populatorreceiver populatorreceiver 2>/dev/null
rabbitmqctl set_permissions -p / populatorreceiver ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user generatorsender generatorsender 2>/dev/null
rabbitmqctl set_permissions -p / generatorsender ".*" ".*" ".*" 2>/dev/null
rabbitmqctl add_user generatorreceiver generatorreceiver 2>/dev/null
rabbitmqctl set_permissions -p / generatorreceiver ".*" ".*" ".*" 2>/dev/null
