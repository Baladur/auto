module.exports={
  entry: [
    './app/app.js'
  ],
  output:{
      filename:'./public/js/bundle.js'
  },
  module: {
      loaders: [
      {
        test: /\.js$/,
        exclude: /(node_modules|bower_components)/,
        loader: 'babel',
        query:{
          plugins: ['transform-decorators-legacy' ],
          presets:['react','es2015']
        }
      },
      {
          test: /\.css$/,
          loader: 'style-loader'
      },
      {
          test: /\.css$/,
          loader: 'css-loader',
          query: {
              modules: true
          }
      }
    ]
  },
  target: "electron"
}

