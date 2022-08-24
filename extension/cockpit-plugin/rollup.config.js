import babel from "@rollup/plugin-babel";
import commonjs from "@rollup/plugin-commonjs";
import resolve from "@rollup/plugin-node-resolve";
import replace from "@rollup/plugin-replace";
import typescript from '@rollup/plugin-typescript';

export default {
  input: "src/main/frontend/plugin.tsx",
  output: {
    file: "target/classes/plugin-webapp/correlate-cockpit-plugin/app/plugin.js"
  },
  plugins: [
    resolve(),
    typescript(),
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
  ],
  onwarn: (warning) => {
    // see https://stackoverflow.com/a/43556986/7189991
    if ( warning.code === 'THIS_IS_UNDEFINED' ) { return; }
    console.warn( warning.message );
}
};
