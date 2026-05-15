.PHONY: run assembly

run:
	@bash -c 'set -a; source .env; set +a; sbt run'

assembly:
	@bash -c 'set -a; source .env; set +a; sbt assembly'
