.EXPORT_ALL_VARIABLES:
.PHONY: test

VERSION = 0.0.1
IMAGE   = healthsamurai/c3:${VERSION}

repl:
	source .env && clj -A:nrepl -e "(-main)" -r 
test:
	clj -A:test

jar:
	clj -A:build

docker:
	docker build -t ${IMAGE} .

docker-push:
	docker push ${IMAGE}

all: jar docker
