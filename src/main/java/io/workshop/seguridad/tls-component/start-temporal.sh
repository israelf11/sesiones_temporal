export TEMPORAL_TLS_CERTS_DIR=/etc/temporal/config/certs

export TEMPORAL_LOCAL_CERT_DIR=./certs

docker network rm temporal-network
docker volume prune
docker rm -f $(docker ps -aq)
docker compose up
