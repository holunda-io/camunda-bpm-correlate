import resolve from "@rollup/plugin-node-resolve";
import commonjs from "@rollup/plugin-commonjs";
import replace from "@rollup/plugin-replace";
import babel from "@rollup/plugin-babel";

export default {
  input: "src/main/frontend/plugin.js",
  output: {
    file: "target/classes/plugin-webapp/correlate-cockpit-plugin/app/plugin.js"
  },
  plugins: [
    resolve(),
    babel({
            babelHelpers: "runtime",
            skipPreflightCheck: true,
            compact: true
          }),
    commonjs({
               include: "node_modules/**"
             }),
    replace({
              "process.env.NODE_ENV": JSON.stringify("production"),
              preventAssignment: true
            })
  ]
};
