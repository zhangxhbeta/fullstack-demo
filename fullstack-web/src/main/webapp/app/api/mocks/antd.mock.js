import mockjs from 'mockjs'
import { getRule, postRule } from './mock/rule'
import { getActivities, getNotice, getFakeList } from './mock/api'
import { getFakeChartData } from './mock/chart'
import { getProfileBasicData } from './mock/profile'
import { getProfileAdvancedData } from './mock/profile'
import { getNotices } from './mock/notices'

export default {

  /**
   * mock bootstrap
   */
  bootstrap(mock) {
    //
    mock.onGet('/api/currentUser').reply(200, {
      name: 'Serati Ma',
      avatar: 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png',
      userid: '00000001',
      notifyCount: 12,
    })

    // mock error request
    mock.onGet('/api/users').reply(200, [
      {
        key: '1',
        name: 'John Brown',
        age: 32,
        address: 'New York No. 1 Lake Park',
      },
      {
        key: '2',
        name: 'Jim Green',
        age: 42,
        address: 'London No. 1 Lake Park',
      },
      {
        key: '3',
        name: 'Joe Black',
        age: 32,
        address: 'Sidney No. 1 Lake Park',
      },
    ])

    mock.onGet('/api/project/notice').reply(200, getNotice)
    mock.onGet('/api/activities').reply(200, getActivities)

  mock.onGet(/\/api\/rule[^/]*/).reply(config => {
      let result = getRule(config, null)
      return [
        200, result
      ]
    })
    mock.onPost(/\/api\/rule[^/]*/).reply(config => {
      let result = postRule(config, null, null, config.data)
      return [
        200, result
      ]
    })

    mock.onPost('/api/forms').reply(200, { message: 'Ok' })

    mock.onGet('/api/tags').reply(200, mockjs.mock({
      'list|100': [{ name: '@city', 'value|1-100': 150, 'type|0-2': 1 }],
    }))

    mock.onGet(/\/api\/fake_list[^/]*/).reply(config => {
      return [
        200, getFakeList(config)
      ]
    })

    mock.onGet('/api/fake_chart_data').reply(200, getFakeChartData)

    mock.onGet('/api/profile/basic').reply(200, getProfileBasicData)
    mock.onGet('/api/profile/advanced').reply(200, getProfileAdvancedData)

    mock.onPost('/api/login/account').reply((config) => {
      const { password, userName, type } = JSON.parse(config.data)
      if (password === '888888' && userName === 'admin') {
        return [
          200, {
            status: 'ok',
            type,
            currentAuthority: 'admin',
          }
        ]
      }
      if (password === '123456' && userName === 'user') {
        return [
          200, {
          status: 'ok',
          type,
          currentAuthority: 'user',
        }]
      }
      return [
        200, {
        status: 'error',
        type,
        currentAuthority: 'guest',
      }]
    })

    mock.onPost('/api/register').reply(200, { status: 'ok', currentAuthority: 'user' })

    mock.onGet('/api/notices').reply(200, getNotices)
    mock.onGet('/api/500').reply(500, {
      timestamp: 1513932555104,
      status: 500,
      error: 'error',
      message: 'error',
      path: '/base/category/list',
    })
    mock.onGet('/api/404').reply(404, {
      timestamp: 1513932643431,
      status: 404,
      error: 'Not Found',
      message: 'No message available',
      path: '/base/category/list/2121212',
    })
    mock.onGet('/api/403').reply(403, {
      timestamp: 1513932555104,
      status: 403,
      error: 'Unauthorized',
      message: 'Unauthorized',
      path: '/base/category/list',
    })
    mock.onGet('/api/401').reply(401, {
      timestamp: 1513932555104,
      status: 401,
      error: 'Unauthorized',
      message: 'Unauthorized',
      path: '/base/category/list',
    })
  }
}
