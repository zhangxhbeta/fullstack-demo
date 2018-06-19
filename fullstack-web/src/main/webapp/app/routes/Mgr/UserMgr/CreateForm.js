import React, {PureComponent, Fragment} from 'react'
import {connect} from 'dva'
import moment from 'moment'
import {
  Row,
  Col,
  Card,
  Form,
  Input,
  Select,
  Icon,
  Button,
  Dropdown,
  Menu,
  InputNumber,
  DatePicker,
  Modal,
  message,
  Badge,
  Divider,
} from 'antd'
const FormItem = Form.Item

export default   Form.create()(props => {
  const {modalVisible, form, handleAdd, handleModalVisible} = props
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return
      form.resetFields()
      handleAdd(fieldsValue)
    })
  }
  return (
    <Modal
      title="新建规则"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem labelCol={{span: 5}} wrapperCol={{span: 15}} label="描述">
        {form.getFieldDecorator('desc', {
          rules: [{required: true, message: 'Please input some description...'}],
        })(<Input placeholder="请输入"/>)}
      </FormItem>
    </Modal>
  )
})
