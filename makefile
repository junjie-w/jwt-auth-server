.PHONY: run run-dev run-prod test test-single test-verbose build clean \
        docker-build docker-run docker-pull-remote docker-run-remote try-api

run:
	mvn spring-boot:run

run-dev:
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

run-prod:
	mvn spring-boot:run -Dspring-boot.run.profiles=prod

build:
	mvn clean package

clean:
	mvn clean

test:
	mvn test

test-single:
	mvn -Dtest=$(class) test

test-verbose:
	mvn test -Dsurefire.useFile=false

docker-build:
	docker build -t jwt-auth-server .

docker-run:
	docker run -p 8080:8080 jwt-auth-server

docker-pull-remote:
	docker pull junjiewu0/jwt-auth-server || docker pull --platform linux/amd64 junjiewu0/jwt-auth-server

docker-run-remote:
	docker run -p 8080:8080 junjiewu0/jwt-auth-server || docker run --platform linux/amd64 -p 8080:8080 junjiewu0/jwt-auth-server

try-api:
	./scripts/try_api.sh
