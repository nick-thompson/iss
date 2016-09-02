# iss
> [Proof of Concept] Write inline styles; ship optimized CSS stylesheets.

This project is a small ClojureScript web application which demonstrates the use of
a couple static file transformations to enable writing inline styles in your application,
but delivering CSS stylesheets to the browser.

* [Try It](#try-it)
* [Why Inline Styles?](#why-inline-styles)
* [How It Works](#how-it-works)
* [License](#license)

## Try It

Before you take this project for a test drive, you'll need to install a few global dependencies:

* [Leiningen](http://leiningen.org/) via the directions on the website.
* `rlwrap` command line utility (available on OS X with `brew install rlwrap`)
* [Node.js](https://nodejs.org/en/blog/release/v4.4.0/), for which I'm using the v4.4.x LTS version

Afterwards, once you've grabbed the project, install local dependencies with:

```bash
$ lein install
$ npm install
```

Then you can build the app with the included build scripts:

```bash
$ ./scripts/build
```

And load it up on a local server.
```bash
$ python -m SimpleHTTPServer
```

The app is a simple fork of a trivial [Om](https://github.com/omcljs/om/) example project, but notice
the class name assignment and the associated style declarations in the DOM.

## Why Inline Styles?

## How It Works

## License

Copyright (c) 2016 Nick Thompson

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
