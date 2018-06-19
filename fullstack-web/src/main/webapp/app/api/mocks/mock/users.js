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

export function getUsers(params={}) {
  const {classId="",name="",pageNo=1,pageSize=20}=params
  const list=data.filter(item=>item.name.indexOf(name)===0 && ((classId&& item.classroomId===classId)||!classId))

  return {
    "data": list.slice((pageNo - 1) * pageSize, pageNo * pageSize - 1),
    "pager": {
      "totalCount": list.length,
      "pageSize": pageSize,
      "pageNo": pageNo
    }
  }
}
