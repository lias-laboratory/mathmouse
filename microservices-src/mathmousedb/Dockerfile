FROM postgres:9.6

# exposing ports to allow container to communicate with outside world
EXPOSE 5432

# Adding initialization script
ADD init-db.sh /init-db.sh
ADD init-db.sql /init-db.sql

# Define default command
# CMD ["./init-db.sh"]
