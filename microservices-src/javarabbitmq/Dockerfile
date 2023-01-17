FROM rabbitmq:3.6.9-management

# exposing ports to allow container to communicate with outside world
EXPOSE 15672 5672

# Adding initialization script
ADD init.sh /init.sh

# Makes the NODENAME static
RUN echo 'NODENAME=rabbit@localhost' > /etc/rabbitmq/rabbitmq-env.conf 

# Define default command
# CMD ["./init.sh"]