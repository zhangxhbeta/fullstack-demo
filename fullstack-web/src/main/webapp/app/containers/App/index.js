import React, { Component } from 'react'
import logo from './logo.svg'
import './App.css'
import { Button } from 'antd'

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          试试编辑 <code>src/main/webapp/app/containers/App/index.js</code> 然后保存，看看我们的热替换技术吧.
        </p>
        <Button type="primary">Button</Button>
      </div>
    )
  }
}

export default App
