import axios from 'axios'
import MockAdapter from 'axios-mock-adapter'
import { Random } from 'mockjs'

import antdMock from './antd.mock'

function _randomLl(start, end) {
  let lng = Random.integer(start * 100000, end * 100000)
  return lng / 100000
}

function randomLng() {
  return _randomLl(120.41900, 120.99880)
}

function randomLat() {
  return _randomLl(29.08488, 29.74000)
}

export default {

  /**
   * mock 所有的模块
   */
  bootstrap(delay = 1000) {

    Random.extend({
      longitude: function (data) {
        return randomLng()
      },
      latitude: function (data) {
        return randomLat()
      }
    })

    const mock = new MockAdapter(axios, { delayResponse: delay })
    antdMock.bootstrap(mock)

    mock.onAny().passThrough();
  }
}
