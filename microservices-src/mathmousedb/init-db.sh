#!/bin/bash

psql -h localhost -U db_admin db_admin < init-db.sql
#docker exec -i mmw_db psql -h localhost -U db_api db_mmw < ./microservices/mmwdb/populate.sql
