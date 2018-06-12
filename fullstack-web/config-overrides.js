const { injectBabelPlugin } = require('react-app-rewired');
const rewireLess = require('react-app-rewire-less');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = function override(config, env) {
    config = injectBabelPlugin(['import', { libraryName: 'antd', libraryDirectory: 'es', style: true }], config);
    config = rewireLess.withLoaderOptions({
        modifyVars: { "@primary-color": "#1DA57A" },
    })(config, env);

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

    return config;
};
