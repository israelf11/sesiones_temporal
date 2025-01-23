docker network rm temporal-network
docker volume prune
docker rm -f $(docker ps -aq)
docker network create temporal-network
docker compose -f docker-compose-cass-es.yml up
