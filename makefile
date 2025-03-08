run:
	mvn spring-boot:run

run-dev:
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

run-prod:
	mvn spring-boot:run -Dspring-boot.run.profiles=prod

test:
	mvn test

test-single:
	mvn -Dtest=$(class) test

test-verbose:
	mvn test -Dsurefire.useFile=false
