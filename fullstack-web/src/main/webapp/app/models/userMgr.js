import { queryUserList, removeRule, addRule } from '../api/api'

export default {
  namespace: 'userMgr',

  state: {
    data: {
      data: [],
      pager: {},
    },
  },

  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryUserList, payload)
      yield put({
        type: 'save',
        payload: response,
      })
    },
    *add({ payload, callback }, { call, put }) {
      const response = yield call(addRule, payload)
      yield put({
        type: 'save',
        payload: response,
      })
      if (callback) callback()
    },
    *remove({ payload, callback }, { call, put }) {
      const response = yield call(removeRule, payload)
      yield put({
        type: 'save',
        payload: response,
      })
      if (callback) callback()
    },
  },

  reducers: {
    save(state, action) {
      return {
        ...state,
        data: action.payload,
      }
    },
  },
}