goog.provide('stylesheet');

class StyleSheetImpl {
  constructor(map) {
    this._map = map;
  }

  getClassName(styleName) {
    return this._map.hasOwnProperty(styleName) ? this._map[styleName] : null;
  }
}

stylesheet.create = function create(map) {
  return new StyleSheetImpl(map);
};
