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
import CreateForm from './CreateForm'
import StandardTable from '../../../components/StandardTable'
import PageHeaderLayout from '../../../layouts/PageHeaderLayout'
import styles from '../../List/TableList.module.less'

const FormItem = Form.Item
const { Option } = Select

class UserMgr extends PureComponent {
  constructor(props) {
    super(props)

    this.state = {
      modalVisible: false,
      selectedRows: [],
      formValues: {},
    };
  }

  componentDidMount() {
    const {dispatch} = this.props
    dispatch({
      type: 'userMgr/fetch',
    })
    dispatch({
      type: 'classroomMgr/fetch',
    })
  }

  handleMenuClick = e => {
    const { dispatch } = this.props
    const { selectedRows } = this.state

    if (!selectedRows) return

    switch (e.key) {
      case 'remove':
        dispatch({
          type: 'rule/remove',
          payload: {
            no: selectedRows.map(row => row.no).join(','),
          },
          callback: () => {
            this.setState({
              selectedRows: [],
            })
          },
        })
        break
      default:
        break
    }
  };

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    })
  };

  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    })
  };

  handleAdd = fields => {
    this.props.dispatch({
      type: 'rule/add',
      payload: {
        description: fields.desc,
      },
    })

    message.success('添加成功')
    this.setState({
      modalVisible: false,
    })
  };

  renderSimpleForm() {
    const { form:{getFieldDecorator},classroomMgr} = this.props
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{md: 8, lg: 24, xl: 48}}>
          <Col md={8} sm={24}>
            <FormItem label="姓名">
              {getFieldDecorator('name')(<Input placeholder="请输入"/>)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="班级">
              {getFieldDecorator('classId')(
                <Select placeholder="请选择"  style={{width: '100%'}}>
                  <Option value={-1}>全部</Option>
                  {classroomMgr.list.map(item=>(<Option value={item.id} key={item.id}>{item.name}</Option>))}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button style={{marginLeft: 8}} onClick={this.handleFormReset}>
                重置
              </Button>
            </span>
          </Col>
        </Row>
      </Form>
    )
  }
  handleSearch = e => {
    e.preventDefault()

    const { dispatch, form } = this.props

    form.validateFields((err, fieldsValue) => {
      if (err) return

      const values = {
        ...fieldsValue,
        updatedAt: fieldsValue.updatedAt && fieldsValue.updatedAt.valueOf(),
      }

      this.setState({
        formValues: values,
      })
      dispatch({
        type: 'userMgr/fetch',
        payload: values,
      })
    })
  };
  render() {
    const { userMgr, loading } = this.props
    const { selectedRows, modalVisible } = this.state

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    }

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="edit">修改</Menu.Item>
        <Menu.Item key="remove">删除</Menu.Item>
      </Menu>
    )
    const columns = [
      {
        title: '姓名',
        dataIndex: 'name',
      },
      {
        title: '年龄',
        dataIndex: 'age',
      },
      {
        title:'班级',
        dataIndex: 'classroomName'
      },
      {
        title: '出生日期',
        dataIndex: 'birthday',
        sorter: true,
        render: val => <span>{moment(val).format('YYYY-MM-DD')}</span>,
      },
      {
        title: '操作',
        render: () => (
          <Fragment>
            <a href="">配置</a>
            <Divider type="vertical" />
            <a href="">订阅警报</a>
          </Fragment>
        ),
      },
    ]
    return (
      <PageHeaderLayout title="查询表格">
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>{this.renderSimpleForm()}</div>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                新建
              </Button>
              {selectedRows.length > 0 && (
                <span>
                  <Button>批量操作</Button>
                  <Dropdown overlay={menu}>
                    <Button>
                      更多操作 <Icon type="down"/>
                    </Button>
                  </Dropdown>
                </span>
              )}
            </div>
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={userMgr.data}
              columns={columns}
              rowKey={'id'}
              onSelectRow={this.handleSelectRows}
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>
        <CreateForm {...parentMethods} modalVisible={modalVisible}/>
      </PageHeaderLayout>
    )
  }
}

export default connect(({userMgr,classroomMgr, loading}) => ({
  userMgr,
  classroomMgr,
  loading: loading.models.userMgr,
}))(Form.create()(UserMgr))
