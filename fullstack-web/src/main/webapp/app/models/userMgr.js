import {queryUserList, getUser, deleteUser, updateUser, createUser} from '../api/api'

export default {
  namespace: 'userMgr',

  state: {
    data: {
      data: [],
      pager: {},
    },
    detail: null,
    fields: {}
  },

  effects: {
    * fetch({payload}, {call, put}) {
      const response = yield call(queryUserList, payload)
      yield put({
        type: 'save',
        payload: response,
      })
      yield put({
        type: 'saveField',
        payload,
      })
    },
    * detail({payload, callback}, {call, put}) {
      const response = yield call(getUser, payload)
      yield put({
        type: 'saveDetail',
        payload: response,
      })
      if (callback) callback()
    },
    * add({payload, callback}, {call, put}) {
      yield call(createUser, payload)
      if (callback) callback()
    },
    * update({payload, callback}, {call, put}) {
      yield call(updateUser, payload)
      yield put({
        type: 'saveDetail',
        payload: null,
      })
      if (callback) callback()
    },
    * remove({payload, callback}, {call, put}) {
      yield call(deleteUser, payload)
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
    saveDetail(state, action) {
      return {
        ...state,
        detail: action.payload,
      }
    },
    saveField(state, action) {
      return {
        ...state,
        fields: action.payload,
      }
    },
  },
}
