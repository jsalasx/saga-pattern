build-ms-billing:
	docker build -t my-ecommerce/ms-billing:1.0.0  -f ./ms-billing/Dockerfile .

restart-ms-billing:
	kubectl rollout restart deployment/ms-billing


build-ms-inventory:
	docker build -t my-ecommerce/ms-inventory:1.0.0 -f ./ms-inventory/Dockerfile .

restart-ms-inventory:
	kubectl rollout restart deployment/ms-inventory

build-ms-orders:
	docker build -t my-ecommerce/ms-orders:1.0.0 -f ./ms-orders/Dockerfile .
restart-ms-order:
	kubectl rollout restart deployment/ms-order

build-ms-saga-orchestrator:
	docker build -t my-ecommerce/saga-orchestrator:1.0.0 -f ./saga-orchestrator/Dockerfile .
restart-ms-saga-orchestrator:
	kubectl rollout restart deployment/ms-saga-orchestrator