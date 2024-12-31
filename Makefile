include .env

define wait_for_service
    echo "Waiting for the service to start on localhost:${SERVER_PORT}/api/users..."
    until curl --output /dev/null --silent --head --fail http://localhost:${SERVER_PORT}/api/users; do printf '.'; sleep 1; done;
endef

define run_docker
    docker run --rm -e SUITE_XML=${SUITE_XML} \
    -v ./selenium-threads/src/test/resources:/test/src/test/resources \
    -v ./output/webdriver/:/test/output/webdriver/ \
    -v ./output/report/:/test/target/surefire-reports/ \
    threads-selenium-threads
endef

define debug_docker
	docker run --rm -e SUITE_XML=${SUITE_XML} \
	-v ./selenium-threads/:/test/ \
	-v ./output/webdriver/:/test/output/webdriver/ \
	-v ./output/report/:/test/target/surefire-reports/ \
	threads-selenium-threads
endef

define build_server
	docker-compose down
	docker-compose build
endef

define build_test
	docker build --build-arg HOST=${HOST} -f ./selenium-threads/Dockerfile --build-arg SERVER_PORT=${SERVER_PORT} -t threads-selenium-threads ./selenium-threads
endef

define run_server
	docker-compose up -d
endef

define stop_server
	docker-compose down
endef

build-server:
	$(call build_server)
build-test:
	$(call build_test)
build:
	$(call build_server)
	$(call build_test)
run-server:
	$(call run_server)
run-test:
	$(call wait_for_service)
	$(call run_docker)
run:
	$(call run_server)
	$(call wait_for_service)
	$(call run_docker)
	$(call stop_server)
debug:
	$(call run_server)
	$(call wait_for_service)
	$(call debug_docker)
	$(call stop_server)