.PHONY: build run test docker-up docker-down

build:
	mvn -DskipTests=false clean package

run:
	mvn spring-boot:run

test:
	mvn test

docker-up:
	docker-compose up --build

docker-down:
	docker-compose down -v
