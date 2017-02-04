module.exports={
  entry:'./app/app.js',
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
          presets:['react','es2015']
        }
      }
    ]
  },
    target: "electron"
}

