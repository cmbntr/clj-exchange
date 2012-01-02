.PHONY: gh-pages

checkout-gh-pages:
	git clone git@github.com:cmbntr/clj-exchange.git gh-pages
	cd gh-pages && git checkout -b gh-pages origin/gh-pages
