.EXPORT_ALL_VARIABLES:
.PHONY: test

VERSION = 0.0.1
IMAGE   = healthsamurai/c3:${VERSION}

repl:
	source .env && clj -A:nrepl -e "(-main)" -r 

jar:
	clj -A:build

docker:
	docker build -t ${IMAGE} .

all: jar docker

test:
	clj -A:test


