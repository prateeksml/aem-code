/* eslint @typescript-eslint/no-var-requires: 0 */ // --> OFF
const postcssRTLCSS = require("postcss-rtlcss");
const { Mode } = require("postcss-rtlcss/options");
module.exports = {
  plugins: [
    postcssRTLCSS({
      mode: Mode.override,
    }),
    require("autoprefixer"),
  ],
};
