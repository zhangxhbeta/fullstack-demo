const { injectBabelPlugin } = require('react-app-rewired')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const cloneDeep = require('lodash/cloneDeep')
const path = require('path')

const cssRuleMatcher = (rule) => rule.test && String(rule.test) === String(/\.css$/)
const ruleChildren = (loader) => loader.use || loader.oneOf || Array.isArray(loader.loader) && loader.loader || []

const findRule = (rulesSource, ruleMatcher) => {
  const { index, rules } = findIndexAndRules(rulesSource, ruleMatcher)
  return rules[index]
}
const findIndexAndRules = (rulesSource, ruleMatcher) => {
  let result = undefined
  const rules = Array.isArray(rulesSource) ? rulesSource : ruleChildren(rulesSource)
  rules.some((rule, index) => result = ruleMatcher(rule) ? { index, rules } : findIndexAndRules(ruleChildren(rule), ruleMatcher))
  return result
}
const createLoaderMatcher = (loader) => (rule) => rule.loader && rule.loader.indexOf(`${path.sep}${loader}${path.sep}`) !== -1
const cssLoaderMatcher = createLoaderMatcher('css-loader')
const postcssLoaderMatcher = createLoaderMatcher('postcss-loader')
const fileLoaderMatcher = createLoaderMatcher('file-loader')

const addAfterRule = (rulesSource, ruleMatcher, value) => {
  const { index, rules } = findIndexAndRules(rulesSource, ruleMatcher)
  rules.splice(index + 1, 0, value)
}

const addBeforeRule = (rulesSource, ruleMatcher, value) => {
  const { index, rules } = findIndexAndRules(rulesSource, ruleMatcher)
  rules.splice(index, 0, value)
}

const lessExtension = /\.less$/
const lessModuleExtension = /\.module\.less$/
let rewireLessModule = (config, env, lessLoaderOptions = {}) => {

  const cssRule = findRule(config.module.rules, cssRuleMatcher)
  const lessRule = cloneDeep(cssRule)
  const cssModulesRule = cloneDeep(cssRule)

  cssRule.exclude = /\.module\.css$/

  const cssModulesRuleCssLoader = findRule(cssModulesRule, cssLoaderMatcher)

  cssModulesRuleCssLoader.options = Object.assign({ modules: true, localIdentName: '[local]___[hash:base64:5]' }, cssModulesRuleCssLoader.options)
  addBeforeRule(config.module.rules, fileLoaderMatcher, cssModulesRule)

  lessRule.test = lessExtension
  lessRule.exclude = lessModuleExtension
  addAfterRule(lessRule, postcssLoaderMatcher, { loader: require.resolve('less-loader'), options: lessLoaderOptions })
  addBeforeRule(config.module.rules, fileLoaderMatcher, lessRule)

  const lessModulesRule = cloneDeep(cssModulesRule)
  lessModulesRule.test = lessModuleExtension
  addAfterRule(lessModulesRule, postcssLoaderMatcher, { loader: require.resolve('less-loader'), options: lessLoaderOptions })
  addBeforeRule(config.module.rules, fileLoaderMatcher, lessModulesRule)
  return config
}

module.exports = function override(config, env) {

  // Add babel import
  config = injectBabelPlugin(['import', { libraryName: 'antd', libraryDirectory: 'es', style: true }], config)

  if (env === 'development') {
    config = injectBabelPlugin(['dva-hmr'], config)
  }

  // Add less module support
  config = rewireLessModule(config, env, {
    modifyVars: { /*"@primary-color": "#1DA57A"*/ },
  })

  // Copy files
  config.plugins.push(
    new CopyWebpackPlugin([
      { from: './node_modules/swagger-ui/dist/css', to: 'swagger-ui/dist/css' },
      { from: './node_modules/swagger-ui/dist/lib', to: 'swagger-ui/dist/lib' },
      { from: './node_modules/swagger-ui/dist/swagger-ui.min.js', to: 'swagger-ui/dist/swagger-ui.min.js' },
      { from: './src/main/webapp/swagger-ui/', to: 'swagger-ui' },
      //            { from: './src/main/webapp/static/', to: 'static' },
      //            { from: './src/main/webapp/favicon.ico', to: 'favicon.ico' },
      //            { from: './src/main/webapp/manifest.webapp', to: 'manifest.webapp' },
      //            { from: './src/main/webapp/robots.txt', to: 'robots.txt' }
    ])
  )

  return config
}
