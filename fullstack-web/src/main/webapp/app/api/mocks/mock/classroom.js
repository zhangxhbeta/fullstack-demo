import Mock from 'mockjs'

const data = []
for (let i = 0; i < 25; i++) {
  data.push(Mock.mock({
    id: Mock.Random.id(),
    name: Mock.Random.cword(2, 3)
  }))
}

export function getClassroom(pageNo, pageSize) {
  return {
    "data": data.slice((pageNo - 1) * pageSize, pageNo * pageSize - 1),
    "pager": {
      "totalCount": data.length,
      "pageSize": pageSize,
      "pageNo": pageNo
    }
  }
}

export function getClassroomAll() {
  return data
}
