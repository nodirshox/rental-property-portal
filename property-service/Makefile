CURRENT_DIR=$(shell pwd)

APP=$(shell basename ${CURRENT_DIR})

APP_CMD_DIR=${CURRENT_DIR}/cmd

IMG_NAME=airbnb-property-service
REGISTRY=nodirshox

TAG=latest
ENV_TAG=latest

build-image:
	docker build --rm -t ${REGISTRY}/${IMG_NAME}:${TAG} .
	docker tag ${REGISTRY}/${IMG_NAME}:${TAG} ${REGISTRY}/${IMG_NAME}:${ENV_TAG}

push-image:
	docker push ${REGISTRY}/${IMG_NAME}:${TAG}

hub:
	docker build --rm -t ${REGISTRY}/${IMG_NAME}:${TAG} .
	docker tag ${REGISTRY}/${IMG_NAME}:${TAG} ${REGISTRY}/${IMG_NAME}:${ENV_TAG}
	docker push ${REGISTRY}/${IMG_NAME}:${TAG}