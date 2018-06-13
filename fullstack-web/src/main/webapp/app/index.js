import React from 'react'
import ReactDOM from 'react-dom'
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom'
import { hot } from 'react-hot-loader'
import registerServiceWorker from './registerServiceWorker'

import 'antd/dist/antd.less'
import './index.css'

import App from './containers/App'

// 引入 mock
// if (process.env.NODE_ENV === 'development') {
//   require('./api/mocks').default.bootstrap()
// }

let HotApp = hot(module)(App)

ReactDOM.render(<HotApp />, document.getElementById('root'))
registerServiceWorker()
