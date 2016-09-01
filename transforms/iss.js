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

    const props = styleExpr.properties.map(function(property) {
      // This is what we write out to a stylesheet, with a selector governed
      // by the same name we pass as the literal value below.
      console.log(j(property.value).toSource());
      return j.property('init', property.key, j.literal('swag'));
    });

    return j.objectExpression(props);
  });

  return root.toSource();
};
