import Mock from 'mockjs'
import * as classroomData from './classroom'

const data = []
const classroomList = classroomData.getClassroomAll();
for (let i = 0; i < 86; i++) {
  const classroom = Mock.mock({
    "data|1": classroomList
  }).data
  data.push(Mock.mock({
    id: Mock.Random.id(),
    name: Mock.Random.cname(),
    'age|18-60': 1,
    birthday: parseInt(Mock.Random.datetime("T")),
    classroomId: classroom.id,
    classroomName: classroom.name,
  }))
}

export function getUsers(params = {}) {
  const {classId = "", name = "", pageNo = 1, pageSize = 20} = params
  const list = data.filter(item => item.name.indexOf(name) === 0 && ((classId && item.classroomId === classId) || !classId))

  return {
    "data": list.slice((pageNo - 1) * pageSize, pageNo * pageSize - 1),
    "pager": {
      "totalCount": list.length,
      "pageSize": pageSize,
      "pageNo": pageNo
    }
  }
}

export function addUser(params = {}) {
  const {name, age, classId, birthday} = params
  const classroom = classroomList.filter(item => item.id === classId)[0]
  const user = {
    id: Mock.Random.id(),
    classroom,
    name,
    age,
    birthday,
    classroomId: classroom.id,
    classroomName: classroom.name
  }
  data.splice(0, 0, user)
}

export function getUser(id) {
  let index = data.findIndex(item => item.id === id)
  if (index !== -1) {
    return data[index]
  } else {
    return null
  }
}

export function updateUser(params = {}, body = {}) {
  const {name, age, classId, birthday} = body
  const classroom = classroomList.filter(item => item.id === classId)[0]
  let index = data.findIndex(item => item.id === params.id)
  if (index !== -1) {
    let user = data[index]
    user = {
      ...user, name, age, birthday,
      classroomId: classroom.id,
      classroomName: classroom.name
    }
    data.splice(index, 1, user)
    return params.id
  } else {
    return -1
  }
}

export function deleteUser(params = {}) {
  let index = data.findIndex(item => item.id === params.id)
  if (index !== -1) {
    data.splice(index, 1)
    return params.id
  } else {
    return -1
  }
}
