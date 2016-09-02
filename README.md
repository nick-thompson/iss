# iss
> [Proof of Concept] Write inline styles; ship optimized CSS stylesheets.

This project is a small ClojureScript web application which demonstrates the use of
a couple static file transformations to enable writing inline styles in your application,
but delivering CSS stylesheets to the browser.

* [Try It](#try-it)
* [Why Inline Styles?](#why-inline-styles)
* [Problems With Inline Styles](#problems-with-inline-styles)
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

## Problems With Inline Styles

## How It Works

At a high level, the only goal of this static file transformation is to take inline declarations such as:

```clojure
(defstyles even-odd
  {:app
    {:alignItems "center"
     :backgroundImage (gradient dark-blue light-blue)
     :display "flex"
     :flexGrow 1
     :justifyContent "space-around"}})
```

and produce from them the corresponding CSS asset describing their styles.

```css
._y8jk_app {
  align-items: center;
  background-image: linear-gradient(to top left, #2c3e50, #4ca1af);
  display: flex;
  flex-grow: 1;
  justify-content: space-around;
}
```

This pipeline has two stages: macro expansion at compile time from ClojureScript to JavaScript, and then a JavaScript AST
transformation which removes the inline style declaration and produces a CSS file. I find it helpful to think about the
stages in reverse order:

#### AST Transformation

#### Macro Expansion

The reason for the decision to implement this in ClojureScript is because of the first-class support for macros in
Clojure/ClojureScript. This detail enables us to write inline styles with references to global variables and helper
functions in the way we've grown accustomed to, but produce *static style descriptors* which can be analyzed and extracted
from the JavaScript AST.

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
