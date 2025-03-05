<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>搜索</span>
            </div>
          </template>
          <el-form ref="queryFormRef" :model="queryParams" :inline="true" size="small">

            <el-form-item label="" prop="custname">
              <el-input v-model="queryParams.custname" placeholder="请输入客户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="" prop="subtime">
              <el-date-picker clearable v-model="queryParams.subtime" type="date" value-format="YYYY-MM-DD"
                placeholder="请选择交货时间" />
            </el-form-item>
            <el-form-item label="" prop="custno">
              <el-input v-model="queryParams.custno" placeholder="请输入客户订单号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="订单时间" prop="otime">
              <el-date-picker clearable v-model="queryParams.otime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择订单时间" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>

          </el-form>
        </el-card>
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>新增</span>
            </div>
          </template>
          <el-form ref="orderFormRef" :model="form" :rules="rules" label-width="100px">
            <div class="grid grid-cols-4  gap-2 ">
              <el-form-item label="税点" prop="shui">
                <el-input v-model="form.shui" placeholder="请输入税点" number style="width: 120px;">
                  <template #append>%</template>
                </el-input>
              </el-form-item>
              <el-form-item label="客户" prop="custid">
                <el-text type="danger" class="mr-8">{{ custname }}</el-text>
                <el-button type="primary" :icon="Search" size="small" class="ml-8 text-sm"
                  @click="findcustom">查询客户</el-button>
                <el-button type="info" plain class=" text-sm" size="small" @click="addcustom2">新增新客户</el-button>
              </el-form-item>
              <el-form-item label="交货时间" prop="subtime">
                <el-date-picker clearable v-model="form.subtime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请选择交货时间">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="客户订单号" prop="custno">
                <el-input v-model="form.custno" placeholder="请输入客户订单号" />
              </el-form-item>
              <el-form-item label="付款周期" prop="cycle">
                <el-input v-model="form.cycle" placeholder="请输入付款周期">
                  <template #append>天</template>
                </el-input>
              </el-form-item>
              <el-form-item label="发票信息" prop="fp">
                <el-select v-model="form.fp" placeholder="请选择发票管理">
                  <el-option label="已开票" value="已开票"></el-option>
                  <el-option label="未开票" value="未开票"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="订单时间" prop="otime">
                <el-date-picker clearable v-model="form.otime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请选择订单时间">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
              </el-form-item>
              <el-form-item label="附件文档" prop="otfile">
                <file-upload v-model="form.otfile" />
              </el-form-item>
              <el-form-item label="其他材料" prop="ctfile">
                <file-upload v-model="form.ctfile" />
              </el-form-item>
              <div class="col-span-4">
                <div class="flex">
                  <el-text type="danger" class="text-blod">物料信息</el-text>
                  <el-button type="primary" :icon="Search" class="ml-8 text-sm" @click="findstock"
                    size="small">选择物料</el-button>
                </div>
                <div class="m-14" v-if="orderDeList.length > 0">
                  <el-table v-loading="loading" :data="orderDeList" border>
                    <el-table-column label="编号" align="center" prop="id" v-if="false" />
                    <el-table-column label="物料" align="center" prop="stockVO.tilte" />
                    <el-table-column label="编号" align="center" prop="stockVO.sn" />
                    <el-table-column label="单位" align="center" prop="stockVO.unitname" />
                    <el-table-column label="单价" align="center" prop="prices">
                      <template #default="scope">
                        <el-input v-model="scope.row.prices" placeholder="请输入单价"
                          :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                          :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
                      </template>
                    </el-table-column>
                    <el-table-column label="数量" align="center" prop="counts" width="200">
                      <template #default="scope">
                        <el-input-number v-model="scope.row.counts" :min="1">
                          <template #suffix>
                            <span>RMB</span>
                          </template>
                        </el-input-number>
                      </template>
                    </el-table-column>
                    <el-table-column label="总价" align="center" prop="allprice">
                      <template #default="scope">
                        <el-text type="danger" class="text-blod">¥{{ (scope.row.prices *
                          scope.row.counts * (form.shui / 100 + 1)).toFixed(2) }}</el-text>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" prop="allprice">
                      <template #default="scope">
                        <el-button type="danger" size="small" @click="handleDelete2(scope.row)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </div>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button :loading="buttonLoading" type="primary" @click="submitForm">录 入</el-button>
              <el-button @click="cancel">重 置</el-button>
            </div>
          </template>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:order:add']">新增</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:order:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:order:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:order:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange" style="width: 100%"
        :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
        :row-style="{ height: '50px' }" lazy border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="expand" label="详细信息" width="80">
          <template #default="props">
            <div m="4">
              <p m="t-0 b-2">客户名称: {{ props.row.customVo.descp }}</p>
              <p m="t-0 b-2">联系人: {{ props.row.customVo.cname }}</p>
              <p m="t-0 b-2">联系电话: {{ props.row.customVo.phone }}</p>
              <p m="t-0 b-2">发货地址: {{ props.row.customVo.address }}</p>

              <h3>需求列表</h3>
              <el-table :data="props.row.orderList" :border="childBorder" show-summary stripe>
                <el-table-column label="物料" prop="stockVo.tilte" />
                <el-table-column label="编号" prop="stockVo.sn" />
                <el-table-column label="规格" prop="stockVo.gk" />
                <el-table-column label="规格" prop="stockVo.gk" />
                <el-table-column label="单位" prop="stockVo.unitname" />
                <el-table-column label="数量" prop="counts" />

              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" prop="orders" width="90">
          <template #default="scope">
            <div v-if="scope.row.status == '0'">
              <div class="block mt-2" v-if="scope.row.tsta == 0">
                <div class="mt-2">
                  <el-button type="primary" plain size="small" @click="changeCk(scope.row)">出库申请</el-button>
                </div>
                <div class="mt-2">
                  <el-button type="warning" plain size="small" >加急订单</el-button>
                </div>
                <div class="mt-2">
                  <el-button type="danger" plain size="small" >发货信息</el-button>
                </div>
              </div>
              <div class="block mt-2" v-if="scope.row.tsta == 1">
                <el-tag plain size="small" type="success" class="text-blod">已提交</el-tag>
                <el-tag plain size="small" type="success" class="text-blod">未出库完成</el-tag>
              </div>
              <div class="block mt-2" v-if="scope.row.tsta == 3">
                <el-tag plain size="small" type="success" class="text-blod">已完成</el-tag>
              </div>
            </div>
            <div class="block mt-2" v-else>
              <el-tag plain size="small" type="success" class="text-blod">已完成</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="主键" align="center" prop="id" v-if="false" />

        <el-table-column label="客户" align="center" prop="custname">
          <template #default="scope">
            <span class="">{{ scope.row.custname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="税点" align="center" prop="shui" />
        <el-table-column label="交货时间" align="center" prop="subtime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.subtime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户订单号" align="center" prop="custno" />
        <el-table-column label="付款周期(天)" align="center" prop="cycle">
          <template #default="scope">
            <span>{{ scope.row.cycle }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="物料信息" align="center" prop="stids" /> -->
        <el-table-column label="发票管理" align="center" prop="fp" width="120">
          <template #default="scope">
            <el-select v-model="scope.row.fp" placeholder="请选择发票管理" @change="changeFp(scope.row)">
              <el-option label="已开票" value="已开票"></el-option>
              <el-option label="未开票" value="未开票"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="订单时间" align="center" prop="otime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.otime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="" align="center" prop="ismrp" /> -->
        <el-table-column label="附件文档" align="center" prop="otfile" />
        <el-table-column label="文件" align="center" prop="ctfile" />
        <!-- <el-table-column label="状态" align="center" prop="status" /> -->
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:order:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改销售订单对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-card shadow="never">
        <template #header>
          <el-form ref="queryFormRef" :model="params2" :inline="true">
            <el-form-item label="类型" prop="type">
              <el-select v-model="params2.type" placeholder="请选择类型" clearable>
                <el-option v-for="dict in customtype" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="简称" prop="descp">
              <el-input v-model="params2.descp" placeholder="请输入简称" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item label="银行账户" prop="cardnum">
              <el-input v-model="params2.cardnum" placeholder="请输入银行账户" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item label="全称" prop="title">
              <el-input v-model="params2.title" placeholder="请输入全称" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item label="联系方式" prop="phone">
              <el-input v-model="params2.phone" placeholder="请输入联系方式" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item label="社会编码" prop="shnum">
              <el-input v-model="params2.shnum" placeholder="请输入社会编码" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item label="收货地址" prop="address">
              <el-input v-model="params2.address" placeholder="请输入收货地址" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item label="邮寄地址" prop="address1">
              <el-input v-model="params2.address1" placeholder="请输入邮寄地址" clearable @keyup.enter="handleQuery2" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery2">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </template>

        <el-table v-loading="loading" :data="customList" style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
          :row-style="{ height: '50px' }" lazy border>
          <el-table-column label="选择" width="90" align="center">
            <template #default="scope">
              <el-button plain type="success" @click="tocheck(scope.row)">选择</el-button>
            </template>
          </el-table-column>
          <el-table-column label="编码" align="center" prop="id" v-if="false" />

          <el-table-column label="类型" align="center" prop="type">
            <template #default="scope">
              <dict-tag :options="customtype" :value="scope.row.type" />
            </template>
          </el-table-column>
          <el-table-column label="简称" align="center" prop="descp" />
          <el-table-column label="银行账户" align="center" prop="cardnum" />
          <el-table-column label="全称" align="center" prop="title" />
          <el-table-column label="联系方式" align="center" prop="phone" />
          <el-table-column label="社会编码" align="center" prop="shnum" />
          <el-table-column label="收货地址" align="center" prop="address" />
          <el-table-column label="邮寄地址" align="center" prop="address1" />
        </el-table>

        <pagination v-show="total2 > 0" :total="total2" v-model:page="params2.pageNum" v-model:limit="params2.pageSize"
          @pagination="getList2" />
      </el-card>
    </el-dialog>
    <!-- 添加客户对话框 -->
    <el-dialog :title="dialog2.title" v-model="dialog2.visible" width="500px" append-to-body>
      <el-form ref="customFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="initFormData2.type" placeholder="请选择类型">
            <el-option v-for="dict in customtype" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="简称" prop="descp">
          <el-input v-model="initFormData2.descp" placeholder="请输入简称" />
        </el-form-item>
        <el-form-item label="银行账户" prop="cardnum">
          <el-input v-model="initFormData2.cardnum" placeholder="请输入银行账户" />
        </el-form-item>
        <el-form-item label="全称" prop="title">
          <el-input v-model="initFormData2.title" placeholder="请输入全称" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="initFormData2.phone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="社会编码" prop="shnum">
          <el-input v-model="initFormData2.shnum" placeholder="请输入社会编码" />
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="initFormData2.address" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="邮寄地址" prop="address1">
          <el-input v-model="initFormData2.address1" placeholder="请输入邮寄地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="initFormData2.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm2">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 物料信息 -->
    <el-dialog :title="dialog3.title" v-model="dialog3.visible" append-to-body style="width: 800px">
      <el-card shadow="never">
        <template #header>
          <el-row :gutter="10" class="mb8">
            <el-form ref="queryFormRef" :model="paramsstock" :inline="true">
              <el-form-item label="名称" prop="tilte">
                <el-input v-model="paramsstock.tilte" placeholder="请输入名称" clearable @keyup.enter="handleQuery3" />
              </el-form-item>
              <el-form-item label="编号" prop="iid">
                <el-input v-model="paramsstock.sn" placeholder="请输入图号" clearable @keyup.enter="handleQuery3" />
              </el-form-item>
              <el-form-item label="图号" prop="iid">
                <el-input v-model="paramsstock.iid" placeholder="请输入图号" clearable @keyup.enter="handleQuery3" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery3">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>
          </el-row>
        </template>

        <el-table v-loading="loading" :data="stockList" highlight-current-row border style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
          :row-style="{ height: '50px' }" lazy>
          <el-table-column label="选择" width="" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" @click="submit2(scope.row)">确认</el-button>
            </template>
          </el-table-column>
          <el-table-column label="主键" align="center" prop="id" v-if="false" />
          <el-table-column label="货架号" align="center" prop="stockname" />
          <el-table-column label="编码" align="center" prop="sn" />
          <el-table-column label="名称" align="center" prop="tilte" />
          <el-table-column label="规格" align="center" prop="gk" />
          <el-table-column label="品类" align="center" prop="otype" />
          <el-table-column label="完成度" align="center" prop="type">
            <template #default="scope">
              <dict-tag :options="stocktype" :value="scope.row.type" />
            </template>
          </el-table-column>
          <el-table-column label="图号" align="center" prop="iid" />
          <el-table-column label="当前库存" align="center" prop="nowstock" />
          <el-table-column label="单位" align="center" prop="unitname" />
          
          <el-table-column label="报警库存" align="center" prop="warnnum" />
          <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
          <el-table-column label="是否在用" align="center" prop="status">
            <template #default="scope">
              <dict-tag :options="diyisno" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="入库批次" align="center" prop="pcount" />
        </el-table>

        <pagination v-show="total3 > 0" :total="total3" v-model:page="paramsstock.pageNum"
          v-model:limit="paramsstock.pageSize" @pagination="getList3" />
      </el-card>
    </el-dialog>
    <el-dialog v-model="showprint" :show-close="false" width="800px">
      <div id="printView" style="padding-left: 90px; padding-right: 20px;width: 100%;">
        <table class="table" border="0pm" cellspacing="0" align="left" width="100%"
          style="font-size: 12px;font-family: SimSun;margin-bottom: 10px; table-layout:fixed;word-wrap:break-word;word-break:break-all">
          <tr>
            <td align="left"> </td>
            <td style="padding:5px; font-weight: bolder; font-size: 15px;" align="right" colspan="2">单据编号：{{ ww.oid }}
            </td>
          </tr>
          <tr>
            <td align="left">购货单位:{{ ww.customtitle }}</td>
            <td align="left">联系人:{{ ww.custname }}</td>
            <!-- <td style="padding:5px;" align="left">制单人：{{ ww }}</td> -->
          </tr>
          <tr>
            <td align="left">地址:{{ ww.address }}</td>
            <td align="left">联系人:{{ ww.phone }}</td>
            <td align="left">时间:{{ ww.times }}</td>
          </tr>
        </table>

        <div style="font-size: 12px;font-family: SimSun;font-weight: bold;margin: 0 0 10px 5px;">明细</div>
        <table class="yk-table" border="1pm" cellspacing="0" align="center" width="100%"
          style="font-size: 10px;font-family: SimSun; table-layout:fixed;word-wrap:break-word;word-break:break-all">
          <tr>
            <th width="55px">序号</th>
            <th>物料</th>
            <th>编号</th>
            <th>规格</th>
            <th>合同号</th>
            <th>单位</th>
            <th>数量</th>
            <th>备注</th>
          </tr>
          <tr v-for="(item, index) in ww.orders" :key="index" align="center">
            <td>{{ index + 1 }}</td>
            <td>{{ item.stockVo.tilte }}</td>
            <td>{{ item.stockVo.sn }}</td>
            <td>{{ item.stockVo.gk }}</td>
            <td>{{ ww.custno }}</td>
            <td>{{ item.stockVo.unitname }}</td>
            <td>{{ item.counts }}</td>
            <td>{{ item.remark }}</td>
          </tr>
          <tr align="center">
            <td>签单：</td>
            <!-- <td colspan="2">{{ adAmountUpper }}</td>
            <td>{{ adAmount }}</td>
            <td /> -->
          </tr>
        </table>
      </div>

      <div class="drawer-bottom-bar" v-show="showprint">
        <el-button @click="prints" type="primary">打印</el-button>
        <el-button @click="showprint = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Order" lang="ts">
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from '@/api/mes/order';
import { OrderVO, OrderQuery, OrderForm } from '@/api/mes/order/types';
import { listCustom, getCustom, delCustom, addCustom2, updateCustom } from '@/api/mes/custom';
import { CustomVO, CustomQuery, CustomForm } from '@/api/mes/custom/types';
import { listStock, getStock, delStock, addStock, updateStock } from '@/api/mes/stock';
import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
import { listOrderDe, getOrderDe, delOrderDe, addOrderDe, updateOrderDe } from '@/api/mes/orderDe';
import { OrderDeVO, OrderDeQuery, OrderDeForm } from '@/api/mes/orderDe/types';
import { Search } from '@element-plus/icons-vue';
import printJs from 'print-js';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { customtype, diyisno, stocktype } = toRefs<any>(proxy?.useDict('customtype', 'diyisno', 'stocktype'));
const stockList = ref<StockVO[]>([]);
const customList = ref<CustomVO[]>([]);
const ww = ref<any>({})
const orderDeList = ref<OrderDeVO[]>([]);
const showprint = ref(false);
const orderDe = ref<OrderDeVO1>({
  stockVO: [],
  id: '',
  stockid: '',
  unid: '',
  prices: undefined,
  counts: 0,
  allprice: 0,
  ctime: '',
  status: '',
  remark: ''
});
const orderList = ref<OrderVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const addcust = ref(false);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const total2 = ref(0);
const total3 = ref(0);
const custname = ref('');
const cuid = ref(0);
const queryFormRef = ref<ElFormInstance>();
const orderFormRef = ref<ElFormInstance>();
interface OrderDeVO1 {
  stockVO: any[];
  id: string | number;
  stockid: string | number;
  unid: string | number;
  prices: number;
  counts: number;
  allprice: number;
  ctime: string;
  status: string;
  remark: string;
}
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog2 = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog3 = reactive<DialogOption>({
  visible: false,
  title: ''
});
const prints = () => {
  showprint.value = false
  printJs({
    printable: 'printView',
    type: 'html',
    style: '.print-css { font-size: 12px; }',
    margin: 1,
    header: '销售订单',
    headerStyle: 'font-size:20px; text-align: center;',
  })
}
const handleDelete2= (row: any) => {
  const index = orderDeList.value.findIndex(item => item === row);
    if (index !== -1) {
      orderDeList.value.splice(index, 1);
    }
}
const toprint = (row: any) => {
  console.log(row)
  ww.value.custno = row.custno
  ww.value.customtitle = row.customVo.title
  ww.value.oid = row.id
  ww.value.customname = row.customVo.cname
  ww.value.phone = row.customVo.cname
  ww.value.address = row.customVo.address
  ww.value.times = row.otime
  ww.value.orders = row.orderList
  showprint.value = true

}
const submit2 = (row: any) => {
  orderDe.value.stockVO = row;
  orderDe.value.stockid = row.id;
  orderDe.value.counts = 1;
  orderDeList.value.push(orderDe.value);
  orderDe.value = {};
  console.log(orderDeList.value);
}

const initorder = ref<any>({
  id: undefined,
  stockid: undefined,
  unid: undefined,
  prices: undefined,
  counts: undefined,
  allprice: undefined,
  ctime: undefined,
  status: undefined,
  remark: undefined,
})
const dataorder = ref<any>({
  pageNum: 1,
  pageSize: 10,
  stockid: undefined,
  unid: undefined,
  prices: undefined,
  counts: undefined,
  allprice: undefined,
  ctime: undefined,
  status: undefined,
  params: {
  }
})

const findstock = () => {
  dialog3.visible = true
  dialog3.title = '选择产品';
}
const initFormData: OrderForm = {
  id: undefined,
  shui: 13,
  custid: undefined,
  subtime: undefined,
  custno: undefined,
  cycle: undefined,
  stids: undefined,
  fp: undefined,
  otime: undefined,
  ismrp: undefined,
  otfile: undefined,
  ctfile: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<OrderForm, OrderQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    shui: undefined,
    custid: undefined,
    subtime: undefined,
    custno: undefined,
    cycle: undefined,
    stids: undefined,
    fp: undefined,
    otime: undefined,
    ismrp: undefined,
    otfile: undefined,
    ctfile: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "主键不能为空", trigger: "blur" }
    ],
  }
});
const addcustom2 = () => {
  dialog2.title = '添加客户';
  dialog2.visible = true;
}
const initFormData2 = ref<any>({
  id: undefined,
  type: undefined,
  descp: undefined,
  cardnum: undefined,
  title: undefined,
  phone: undefined,
  shnum: undefined,
  address: undefined,
  address1: undefined,
  status: undefined,
  remark: undefined,
})
const params2 = ref<any>({
  pageNum: 1,
  pageSize: 10,
  type: undefined,
  descp: undefined,
  cardnum: undefined,
  title: undefined,
  phone: undefined,
  shnum: undefined,
  address: undefined,
  address1: undefined,
  status: undefined,
  params: {
  }
})
const paramsstock = ref<any>({
  pageNum: 1,
  pageSize: 10,
  tilte: undefined,
  warnnum: undefined,
  type: undefined,
  iid: undefined,
  sn: undefined,
  stockid: undefined,
  instock: undefined,
  outstock: undefined,
  nowstock: undefined,
  unitid: undefined,
  params: {
  }
})
const initFormData3 = ref<any>({
  id: undefined,
  tilte: undefined,
  warnnum: undefined,
  type: undefined,
  iid: undefined,
  sn: undefined,
  stockid: undefined,
  instock: 0,
  outstock: 0,
  nowstock: 0,
  unitid: undefined,
  proid: undefined,
  status: undefined,
  remark: undefined,
})
const { queryParams, form, rules } = toRefs(data);
const findcustom = () => {
  dialog.visible = true;
  dialog.title = '选择客户';
  getList2();
}
/** 查询销售订单列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOrder(queryParams.value);
  orderList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
const getList2 = async () => {
  const res = await listCustom(params2.value);
  customList.value = res.rows;
  total2.value = res.total;
}

const getList3 = async () => {
  const res = await listStock(paramsstock.value);
  stockList.value = res.rows;
  total3.value = res.total;
}
const tocheck = (row: any) => {
  cuid.value = row.id
  custname.value = row.title
  dialog.visible = false;
}
const submitForm2 = async () => {
  let res = await addCustom2(initFormData2.value).finally(() => buttonLoading.value = false);
  custname.value = res.data.title
  cuid.value = res.data.id
  dialog2.visible = false;
}
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  orderDeList.value = []
  cuid.value = 0
  custname.value = undefined
  orderFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}
const handleQuery2 = () => {
  params2.value.pageNum = 1;
  getList2();
}
const handleQuery3 = () => {
  paramsstock.value.pageNum = 1;
  getList3();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  custname.value = undefined
  cuid.value = undefined
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: OrderVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加销售订单";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OrderVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOrder(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改销售订单";
}
const changeFp = async (row?: OrderVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOrder(_id);
  Object.assign(form.value, res.data);
  form.value.fp = row.fp
  await updateOrder(form.value).finally(() => buttonLoading.value = false);
  proxy?.$modal.msgSuccess("修改成功");
}
const changeCk = async (row?: OrderVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOrder(_id);
  Object.assign(form.value, res.data);
  form.value.tsta = 1
  await updateOrder(form.value).finally(() => buttonLoading.value = false);
  proxy?.$modal.msgSuccess("提交成功！");
  getList();
}
/** 提交按钮 */
const submitForm = async () => {
  if (cuid.value == 0) {
    proxy?.$modal.msgError("请选择客户");
    return
  }
  let ids = []
  let concatenatedDataArray = []; // 在循环外部定义一个数组来存储每次循环中的数据

  let ss = (form.value.shui / 100) + 1

  await Promise.all(orderDeList.value.map(async (item) => {
    item.allprice = (item.prices * item.counts * ss).toFixed(2)
    item.status = '0'
    let rec = await addOrderDe(item).finally(() => buttonLoading.value = false);
    let dataArray = rec.data;
    concatenatedDataArray.push(dataArray);
  }))
  let concatenatedData = concatenatedDataArray.join(',');
  form.value.stids = concatenatedData
  form.value.custid = cuid.value
  orderFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateOrder(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addOrder(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      reset();
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OrderVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除销售订单编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOrder(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/order/export', {
    ...queryParams.value
  }, `order_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getList3();
});
</script>
