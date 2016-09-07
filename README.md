# iss
> [Proof of Concept] Write inline styles; ship optimized CSS stylesheets.

This project is a small ClojureScript web application which demonstrates the use of
a couple static file transformations to enable writing inline styles in your application,
but delivering CSS stylesheets to the browser.

* [Try It](#try-it)
* [Why Inline Styles?](#why-inline-styles)
* [Problems With Inline Styles](#problems-with-inline-styles)
* [The Big Idea](#the-big-idea)
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
*Inspired by [CSS In JS](http://blog.vjeux.com/2014/javascript/react-css-in-js-nationjs.html) by [@vjeux](https://github.com/vjeux)*

We've been aware of several problems with CSS for a long time now. There have been a few attempts at solving some of
these problems via convention and methodology, such as [BEM](http://getbem.com/), [SUITCSS](https://suitcss.github.io/),
and [OOCSS](https://github.com/stubbornella/oocss), and via tools such as [Less](http://lesscss.org/) and 
[Sass](http://sass-lang.com/). But by and large these attempts fail to really solve the bigger problems we're seeing:
namespacing, dependency expression, dead code elimination, constant sharing, and minification. In the talk linked above,
Christopher (@vjeux) does a really great job of enumerating these issues and why writing styles inline solves them. I'd
strongly encourage you to give that talk a thoughtful listen.

These ideas have been around for a while, and there have been a lot of attempts at finding a true solution. There's a lot
of insight to be gleaned from looking through the various projects listed 
[here](https://github.com/FormidableLabs/radium/tree/master/docs/comparison).

## Problems With Inline Styles

Writing inline styles solves many of the problems we've come to agree upon with writing and maintaining CSS on
non-trivial web applications, but I see a couple major issues with actually shipping inline styles. Mostly this
has to do with the fact that browser implementations have evolved to optimize for the conventional best practices
we've been relying on for the past decade or so, and *not* for shipping inline styles:

#### Resource Fetching
Most moderately or highly complex web applications now rely on several external resources: one or more stylesheets,
one or more JavaScript files, media elements, etc. For that reason browsers have optimized for fetching multiple
resources: preloading, deferred fetching, async fetching, parallel downloads. We've come to rely on these features
for the performance of our highly complex web applications, and if instead of providing external CSS stylesheets which
the browser can readily handle we suddenly switch to inlining all of our CSS, we immediately invalidate these
optimizations.

#### Caching
Really this could be included in the previous point, but I think it deserves the highlight. Browser support for caching
external resources is really good, but you'll rarely find cache headers on the response servered for the root of your
application because we need the ability to update, and have no easy way of telling a browser to clear cache entries for
`www.mydomain.com/`. For external resources it's easy: just have the new version of your HTML document point to a
different resource. Losing cacheing is painful, and remember we're talking about increasing the size of our initial
payload as well.

#### Render Performance
In rendering our applications, browsers all follow a similar flow: parse relevant markup -> construct a render tree ->
layout the render tree -> paint. The construction of the render tree typically involves a separate construction of
the DOM that we all know and love, and a similar structure which we'll call the CSSOM (this is the name given in Blink,
though most modern browsers have the same structure by a different name). These two trees are then combined to form
the render tree. The CSSOM is an abstraction over the CSS declarations we've made in our stylesheets to optimize sharing
similar declarations and reduce object allocation in the render loop, because surely we want this step to be as fast as
possible if it's going to be a mainstay of the render loop. But the use of inline style attributes complicates the
optimizations available here, especially when updates to a set of inline styles is inserted into the DOM via `innerHTML`
or some similar construct which requires the parsing phase.

#### Pseudo-elements/Pseudo-classes
Last but not least, this is a complication of the developer experience. We've grown accustomed to writing selectors which
make liberal user of `:hover` classes, `:after` elements, etc., which aren't available as inline style attributes.
Using inline styles doesn't prevent us from implementing this kind of behavior, but it means we have to reinvent a 
developer-friendly way of doing it. [Radium](https://github.com/FormidableLabs/radium) again comes to mind as a good 
example, but I'm sure there's a lot more to be made of this space.

## The Big Idea

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
