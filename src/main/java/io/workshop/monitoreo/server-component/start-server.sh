docker network rm temporal-network
docker volume prune
docker rm -f $(docker ps -aq)
docker network create temporal-network
docker compose -f compose-postgres.yml -f compose-services.yml up --detach
