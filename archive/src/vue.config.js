// vue.config.js
module.exports = {
  chainWebpack: config => {
    config.module
      .rule('js')
      .test(/\.js$/)
      .exclude
      .add(/node_modules/)
      .end()
      .use('babel-loader')
      .loader('babel-loader')
      .options({
        presets: ['@babel/preset-env']
      });

    // 为 node_modules 中的特定包添加规则
    config.module
      .rule('axios')
      .test(/\.js$/)
      .include
      .add(/node_modules\/axios/)
      .end()
      .use('babel-loader')
      .loader('babel-loader')
      .options({
        presets: ['@babel/preset-env']
      });
  }
};
