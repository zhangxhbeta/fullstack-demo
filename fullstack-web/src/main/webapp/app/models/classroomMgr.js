import { queryClassroomList, removeRule, addRule } from '../api/api'

export default {
  namespace: 'classroomMgr',

  state: {
    data: {
      data: [],
      pager: {},
    },
    list:[]
  },

  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryClassroomList, payload)
      yield put({
        type: 'saveList',
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
    saveList(state, action) {
      return {
        ...state,
        list: action.payload,
      }
    },
  },
}
