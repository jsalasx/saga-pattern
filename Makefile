build-ms-billing:
	docker build -t my-ecommerce/ms-billing:1.0.0  -f ./ms-billing/Dockerfile .

restart-ms-billing:
	kubectl rollout restart deployment/ms-billing -n ecommerce

deploy-ms-billing: build-ms-billing restart-ms-billing



build-ms-inventory:
	docker build -t my-ecommerce/ms-inventory:1.0.0 -f ./ms-inventory/Dockerfile .

restart-ms-inventory:
	kubectl rollout restart deployment/ms-inventory -n ecommerce

deploy-ms-inventory: build-ms-inventory restart-ms-inventory

build-ms-orders:
	docker build -t my-ecommerce/ms-orders:1.0.0 -f ./ms-orders/Dockerfile .
restart-ms-orders:
	kubectl rollout restart deployment/ms-orders -n ecommerce

deploy-ms-orders: build-ms-orders restart-ms-orders

build-ms-saga-orchestrator:
	docker build -t my-ecommerce/saga-orchestrator:1.0.0 -f ./saga-orchestrator/Dockerfile .
restart-ms-saga-orchestrator:
	kubectl rollout restart deployment/saga-orchestrator -n ecommerce

deploy-ms-saga-orchestrator: build-ms-saga-orchestrator restart-ms-saga-orchestrator


deploy-all: deploy-ms-billing deploy-ms-inventory deploy-ms-orders deploy-ms-saga-orchestrator