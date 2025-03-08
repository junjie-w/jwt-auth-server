run:
	mvn spring-boot:run

run-dev:
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

run-prod:
	mvn spring-boot:run -Dspring-boot.run.profiles=prod