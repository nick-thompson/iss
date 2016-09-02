const fs = require('fs');
const path = require('path');
const recast = require('recast');

const CSSPropertyOperations = require('react/lib/CSSPropertyOperations');
const Hashids = require('hashids');

const outPath = path.resolve('./out/iss.css');
const out = fs.createWriteStream(outPath, {flags: 'a'});
const hashids = new Hashids('iss4lyfe', 0, 'abcdefghijklmnopqrstuvwxyz');

module.exports = function(fileInfo, api) {
  const j = api.jscodeshift;
  const root = j(fileInfo.source);

  root.find(j.CallExpression, {
    callee: {
      type: 'Identifier',
      name: '__issStyleSheetCreate__',
    }
  }).filter(function(expr) {
    const args = expr.value.arguments;
    return args.length === 1 && args[0].type === 'ObjectExpression';
  }).replaceWith(function(callExpr) {
    // We've identified a CallExpression with the form:
    //
    //  var styles = __issStyleSheetCreate__({
    //    someClass: {
    //      font: 8,
    //      padding: 8
    //    },
    //    someOtherClass: {
    //      font: 8,
    //      padding: 8
    //    },
    //  });
    //
    const callArgs = callExpr.value.arguments[0];

    // For each (class, styles) pair in the above object, we extract the styles
    // and push them into an external stylesheet under a unique selector, and
    // build a set of (class, selector) pairs to be written back into the AST.
    //
    //  var styles = {
    //    someClass: 'GlaHquq0',
    //    someOtherClass: 'BG2bHbGl',
    //  };
    //
    const mapEntries = callArgs.properties.map(function(property) {
      const classIdentifier = property.key;
      const styles = JSON.parse(recast.prettyPrint(property.value).code);
      const markup = CSSPropertyOperations.createMarkupForStyles(styles, null);
      const selector = hashids.encode(Date.now());
      const declaration = `.${selector}{${markup}}`;

      // Write out the CSS declaration.
      out.write(declaration, 'utf8');

      // Return a map entry.
      return j.property('init', classIdentifier, j.literal(selector));
    });

    out.end();

    // Replace the __issStyleSheetCreate__ CallExpression with the object
    // literal mapping classIdentifiers to actual classNames.
    return j.objectExpression(mapEntries);
  });

  return root.toSource();
};
