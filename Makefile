build-ms-billing:
	docker build -t my-ecommerce/ms-billing:1.0.0 ./ms-billing/Dockerfile

restart-ms-billing:
	kubectl rollout restart deployment/ms-billing


build-ms-inventory:
	docker build -t my-ecommerce/ms-inventory:1.0.0 ./ms-inventory/Dockerfile

restart-ms-inventory:
	kubectl rollout restart deployment/ms-inventory

build-ms-order:
	docker build -t my-ecommerce/ms-order:1.0.0 ./ms-order/Dockerfile
restart-ms-order:
	kubectl rollout restart deployment/ms-order