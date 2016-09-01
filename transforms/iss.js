const fs = require('fs');

const CSSPropertyOperations = require('react/lib/CSSPropertyOperations');

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
  }).replaceWith(function(expr) {
    const styleExpr = expr.value.arguments[0];
    const batch = [];

    const props = styleExpr.properties.map(function(property) {
      // This is what we write out to a stylesheet, with a selector governed
      // by the same name we pass as the literal value below.
      const stylesObj = JSON.parse(j(property.value).toSource());
      const markup = CSSPropertyOperations.createMarkupForStyles(stylesObj, null);
      const selector = 'swag';
      const declaration = `.${selector} {\n${markup}\n}`

      batch.push(declaration);
      return j.property('init', property.key, j.literal(selector));
    });

    const output = batch.join('\n');
    fs.appendFileSync('./out/iss.css', output);
    return j.objectExpression(props);
  });

  return root.toSource();
};
