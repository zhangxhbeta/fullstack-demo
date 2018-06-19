import React, {PureComponent, Fragment} from 'react'
import {connect} from 'dva'
import moment from 'moment'
import {
  DatePicker,
  Form,
  Input,
  Modal,
  Select,
} from 'antd'
const FormItem = Form.Item
const { Option } = Select

export default  Form.create()(props => {
  const {modalVisible, form, handleUpdate, handleModalVisible,classroomMgr,value} = props
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return
      form.resetFields()
      handleUpdate({...fieldsValue,id:value.id})
    })
  }
  return (
    <Modal
      title="修改用户"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem labelCol={{span: 5}} wrapperCol={{span: 15}} label="姓名">
        {form.getFieldDecorator('name', {
          initialValue:value.name,
          rules: [{required: true, message: '请输入姓名'}],
        })(<Input placeholder="请输入"/>)}
      </FormItem>
      <FormItem labelCol={{span: 5}} wrapperCol={{span: 15}} label="年龄">
        {form.getFieldDecorator('age', {
          initialValue:value.age,
          rules: [{required: true, message: '请输入年龄'}],
        })(<Input placeholder="请输入"/>)}
      </FormItem>
      <FormItem labelCol={{span: 5}} wrapperCol={{span: 15}} label="出生日期">
        {form.getFieldDecorator('birthday', {
          initialValue:moment(value.birthday),
          rules: [{required: true, message: '请输入出生日期'}],
        })(<DatePicker placeholder="请输入" format={'YYYY-MM-DD'}/>)}
      </FormItem>
      <FormItem labelCol={{span: 5}} wrapperCol={{span: 15}} label="班级">
        {form.getFieldDecorator('classId', {
          initialValue:value.classroomId,
          rules: [{required: true, message: '请选择班级'}],
        })(<Select placeholder="请选择" style={{width: '100%'}}>
            {classroomMgr.list.map(item => (<Option value={item.id} key={item.id}>{item.name}</Option>))}
          </Select>)}
      </FormItem>
    </Modal>
  )
})
