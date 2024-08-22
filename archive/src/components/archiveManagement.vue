<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const tableData = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const fetchArchives = async (page = 1, size = 10) => {
  try {
    const response = await axios.get('/archives', {
      params: {
        page,
        size,
      },
    });
    console.log('Fetched data:', response.data); // 调试用，查看返回的数据
    tableData.value = response.data.records; // 假设返回的数据包含 records 和 total 字段
    total.value = response.data.total; // 设置总记录数
  } catch (error) {
    console.error('Error fetching archives:', error);
  }
};

onMounted(() => {
  fetchArchives(); // 组件挂载后调用函数获取数据
});
</script>

<template>
  <el-container>
    <el-header>
      <h1 style="color: #2980b9">数字档案管理系统</h1>
    </el-header>
    <el-container>
      <el-aside style="width: 200px">
        <el-menu
          default-active="2"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose">
          <el-submenu index="1">
            <template #title>
              <i class="el-icon-document "></i>
              <span>工作台</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="1-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template #title>
              <i class="el-icon-document "></i>
              <span>个人中心</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="2-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3">
            <template #title>
              <i class="el-icon-document "></i>
              <span>文件管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="3-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="4">
            <template #title>
              <i class="el-icon-document "></i>
              <span>接收管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="4-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="5">
            <template #title>
              <i class="el-icon-document "></i>
              <span>档案管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="5-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="6">
            <template #title>
              <i class="el-icon-document "></i>
              <span>档案保管</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="6-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="7">
            <template #title>
              <i class="el-icon-document "></i>
              <span>档案利用</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="7-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="8">
            <template #title>
              <i class="el-icon-document "></i>
              <span>统计分析</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="8-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="9">
            <template #title>
              <i class="el-icon-document "></i>
              <span>业务配置</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="9-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="10">
            <template #title>
              <i class="el-icon-document "></i>
              <span>系统管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="10-1">选项1</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header><b>档案馆管理</b> 业务配置 /档案馆管理</el-header>
        <el-main>
          <el-row>
            <el-button type="primary" @click="showCreateModal">创建档案馆</el-button>
            <el-button type="primary" @click="importArchive">导入</el-button>
            <el-button type="primary" @click="exportArchive">导出</el-button>
            <el-button type="primary" @click="downloadTemplate">下载导入模板</el-button>
            <!-- 添加搜索栏和查询按钮 -->
            <el-input
              v-model="searchCode"
              placeholder="输入档案馆代码"
              style="width: 200px; margin-left: 20px;"
            ></el-input>
            <el-button icon="el-icon-search" @click="searchArchive"></el-button>
          </el-row>
          <div v-if="taskStatus">
            <p>Task Status: {{ taskStatus }}</p>

          </div>

          <!-- 查询结果展示 -->
          <el-table v-if="searchResult" :data="[searchResult]" stripe style="margin-top: 20px;">
            <el-table-column prop="name" label="档案馆名称" width="180"></el-table-column>
            <el-table-column prop="code" label="档案馆代码" width="180"></el-table-column>
            <el-table-column prop="special_archivist" label="专职档案员" width="180"></el-table-column>
            <el-table-column prop="administrative_archivist" label="行政档案员" width="180"></el-table-column>
            <el-table-column prop="manager" label="公司总经理" width="180"></el-table-column>
          </el-table>

          <el-table :data="tableData" stripe style="width: 100%" row-key="code" lazy :load="load" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
            <el-table-column prop="name" label="档案馆名称" width="180"></el-table-column>
            <el-table-column prop="code" label="档案馆代码" width="180"></el-table-column>
            <el-table-column prop="special_archivist" label="专职档案员" width="180"></el-table-column>
            <el-table-column prop="administrative_archivist" label="行政档案员" width="180"></el-table-column>
            <el-table-column prop="manager" label="公司总经理" width="180"></el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <div style="text-align: center;">
                  <el-button type="primary" @click="editArchive(scope.$index)">编辑</el-button>
                  <el-button type="danger" @click="deleteArchive(scope.$index)">删除</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <!-- 编辑窗口 -->
          <div v-if="isEditing" class="modal">
            <div class="modal-content">
              <span class="close" @click="closeModal">&times;</span>
              <h3>编辑档案馆</h3>
              <form @submit.prevent="saveEdit">
                <label for="edit-name">档案馆名称:</label>
                <el-input v-model="currentArchive.name" id="edit-name" required clearable></el-input><br>
                <label for="edit-code">档案馆代码:</label>
                <el-input v-model="currentArchive.code" id="edit-code" required clearable></el-input><br>
                <label for="edit-special_archivist">专职档案员:</label>
                <el-input v-model="currentArchive.specialArchivist" id="edit-specialArchivist" required clearable></el-input><br>
                <label for="edit-administrative_archivist">行政档案员:</label>
                <el-input v-model="currentArchive.administrative_archivist" id="edit-administrative_archivist" required clearable></el-input><br>
                <label for="edit-manager">公司总经理:</label>
                <el-input v-model="currentArchive.manager" id="edit-manager" required clearable></el-input><br>
                <el-button type="primary" native-type="submit">保存</el-button>
              </form>
            </div>
          </div>

          <!-- 创建档案馆窗口 -->
          <div v-if="isCreating" class="modal">
            <div class="modal-content">
              <span class="close" @click="closeModal">&times;</span>
              <h3>创建档案馆</h3>
              <form @submit.prevent="saveCreate">
                <label for="create-name">档案馆名称:</label>
                <el-input v-model="newArchive.name" id="create-name" required clearable></el-input><br>
                <label for="create-code">档案馆代码:</label>
                <el-input v-model="newArchive.code" id="create-code" required clearable></el-input><br>
                <label for="create-special_archivist">专职档案员:</label>
                <el-input v-model="newArchive.special_archivist" id="create-special_archivist" required clearable></el-input><br>
                <label for="create-administrative_archivist">行政档案员:</label>
                <el-input v-model="newArchive.administrative_archivist" id="create-administrative_archivist" required clearable></el-input><br>
                <label for="create-manager">公司总经理:</label>
                <el-input v-model="newArchive.manager" id="create-manager" required clearable></el-input><br>
                <button type="submit">保存</button>
              </form>
            </div>
          </div>
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            :page-sizes="[10, 25, 50, 100]"
            :page-size.sync="pageSize"
            :current-page.sync="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          ></el-pagination>
          <!-- 编辑窗口和创建档案馆窗口 omitted for brevity -->
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Learn',
  data() {
    return {
      tableData: [], // 确保在 data 中初始化 tableData
      isEditing: false,
      isCreating: false,
      currentArchive: {},
      newArchive: { name: '', code: '', special_archivist: '', administrative_archivist: '', manager: '' },
      currentIndex: -1,
      pageSize: 10,
      total: 0,
      searchCode: '', // 搜索框输入的值
      searchResult: null, // 保存查询结果
      sse: null,
      taskStatus: ''
    }
  },
  methods: {
    startSse() {
      const eventSource = new EventSource('http://localhost:8080/subscribe');
      let clientId;

      eventSource.onmessage = function(event) {
        if (event.data.startsWith('clientId:')) {
          clientId = event.data.split(':')[1];
        } else {
          console.log("Message from server: " + event.data);
        }
      };
      /*
      this.sse.addEventListener('open', () => {
        console.log('SSE connection opened');
      });

      this.sse.addEventListener('error', (event) => {
        console.error('SSE connection error:', event);
      });

      this.sse.addEventListener('task-update', event => {
        console.log('Received task-update event:', event);
        this.taskStatus = event.data;  // 保存接收到的任务状态
        console.log("Task Status: ", this.taskStatus);  // 输出任务状态
      }
      );
       */

    },
    async searchArchive() {
      try {
        const response = await axios.get('/select', {
          params: { code: this.searchCode },
        });
        this.searchResult = response.data; // 将查询结果赋值给 searchResult
      } catch (error) {
        console.error('Error searching archive:', error);
        alert('查询失败，请重试。');
      }
    },
    async fetchArchives(page = 1, size = 10) {
      try {
        const response = await axios.get('/archives', {
          params: {
            page,
            size,
          },
        });
        console.log('Fetched data:', response.data); // 调试用，查看返回的数据
        this.tableData = response.data.records; // 假设返回的数据包含 records 和 total 字段
        this.total = response.data.total; // 设置总记录数
      } catch (error) {
        console.error('Error fetching archives:', error);
      }
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.fetchArchives(this.currentPage, this.pageSize);
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.fetchArchives(this.currentPage, this.pageSize);
    },
    showCreateModal() {
      this.isCreating = true;
    },
    async importArchive() {
      try {
        const response = await axios.get('/writeDb');
        alert(response.data);
        this.fetchArchives(); // 更新列表
      } catch (error) {
        console.error('Error importing archives:', error);
      }
    },
    async exportArchive() {
      try {
        const response = await axios.get('/readDb');
        alert(response.data);
      } catch (error) {
        console.error('Error exporting archives:', error);
      }
    },
    downloadTemplate() {
      alert('下载导入模板');
    },
    editArchive(index) {
      this.currentIndex = index;
      this.currentArchive = { ...this.tableData[index] };
      this.isEditing = true;
    },
    closeModal() {
      this.isEditing = false;
      this.isCreating = false;
      this.currentArchive = {};
      this.newArchive = { name: '', code: '', special_archivist: '', administrative_archivist: '', manager: '' };
      this.currentIndex = -1;
    },
    saveCreate() {
      this.tableData.push({ ...this.newArchive });
      this.closeModal();
      alert('档案馆已创建');
    },
    async saveEdit() {
      try {
        await axios.put(`/archives/${this.currentArchive.code}`, {
          name: this.currentArchive.name,
          special_archivist: this.currentArchive.special_archivist,
          administrative_archivist: this.currentArchive.administrative_archivist,
          manager: this.currentArchive.manager,
        });
        this.tableData[this.currentIndex] = this.currentArchive;
        this.closeModal();
        alert('档案馆已编辑');
      } catch (error) {
        console.error('Error updating archive:', error);
      }
    },
    async deleteArchive(index) {
      try {
        const archive = this.tableData[index];
        await axios.delete(`/archives/${archive.code}`);
        this.tableData.splice(index, 1);
        alert('档案馆已删除');
      } catch (error) {
        console.error('Error deleting archive:', error);
      }
    },
    load(tree, treeNode, resolve) {
      setTimeout(() => {
        resolve([
          {id:11, name: 'A市档案馆',code: '10001000', special_archivist: '', administrative_archivist: '', manager: ''}
        ])
      }, 1000)
    },
  },
  created() {
    this.fetchArchives(this.currentPage, this.pageSize); // 在组件创建时调用方法获取数据
  },
  mounted() {
    this.startSse();
  },
  beforeDestroy() {
    if (this.sse) {
      this.sse.close();
    }
  }
}
</script>


<style scoped>
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-content {
  max-width: 500px;
  margin: auto;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  cursor: pointer;
}
</style>
