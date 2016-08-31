/**
 * This replaces every occurence of variable "foo".
 */
module.exports = function(fileInfo, api) {
  const j = api.jscodeshift;
  const root = j(fileInfo.source);

  const callExpressions = root.find(j.CallExpression, {
    callee: {
      type: 'Identifier',
      name: '__issStyleSheetCreate__',
    }
  }).filter(p => p.value.arguments.length == 1 && p.value.arguments[0].type === 'ObjectExpression');

  callExpressions.replaceWith(p => p.value.arguments[0]);

  return root.toSource();
};
