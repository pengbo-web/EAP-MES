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

                        <el-form-item label="" prop="custid">
                            <el-input v-model="queryParams.custid" placeholder="请输入客户" clearable
                                @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="" prop="subtime">
                            <el-date-picker clearable v-model="queryParams.subtime" type="date"
                                value-format="YYYY-MM-DD" placeholder="请选择交货时间" />
                        </el-form-item>
                        <el-form-item label="" prop="custno">
                            <el-input v-model="queryParams.custno" placeholder="请输入客户订单号" clearable
                                @keyup.enter="handleQuery" />
                        </el-form-item>
                        <el-form-item label="订单时间" prop="otime">
                            <el-date-picker clearable v-model="queryParams.otime" type="date" value-format="YYYY-MM-DD"
                                placeholder="请选择订单时间" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                        </el-form-item>

                    </el-form>
                </el-card>
            </div>
        </transition>

        <el-card shadow="never">
            <template #header>
                <el-row :gutter="10" class="mb8">
                    <!-- <el-col :span="1.5">
                        <el-button type="primary" plain icon="Plus" @click="handleAdd"
                            v-hasPermi="['mes:order:add']">新增</el-button>
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

            <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange"
                style="width: 100%" :header-cell-style="{ 'text-align': 'center' }"
                :cell-style="{ 'text-align': 'center', 'font-size': '12px', 'weight': 'bold' }" lazy
                :row-style="rowState" :cell-class-name="cellClassName" border>
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column type="expand" label="详细信息" width="80">
                    <template #default="props">
                        <div m="4">
                            <h3>计划列表</h3>
                            <el-table :data="props.row.orderList" show-summary stripe
                                :header-cell-style="{ 'text-align': 'center','color': 'rgb(179, 224.5, 156.5)', 'font-size': '10px' }"
                                :cell-style="{ 'text-align': 'center', 'font-size': '10px' }"
                                :row-style="{ height: '50px' }" lazy border>
                                <!-- 第一层 -->
                                <el-table-column type="expand" label="Bom拆分" width="80">
                                    <template #default="stockVo">
                                        <div m="4">
                                            <el-table :data="stockVo.row.stockVo.bomlist" show-summary stripe
                                                :header-cell-style="{ 'text-align': 'center', 'font-size': '10px', 'color': 'red' }"
                                                :cell-style="{ 'text-align': 'center', 'font-size': '10px' }"
                                                :row-style="{ height: '50px' }" lazy border>
                                                <el-table-column type="expand" label="Bom拆分" width="80">
                                                    <template #default="stockVo">
                                                        <!-- {{stockVo.row}} -->
                                                        <!-- 第三层 -->
                                                        <div m="4">
                                                            <el-table :data="stockVo.row.bomlist" show-summary stripe
                                                                :header-cell-style="{ 'text-align': 'center', 'font-size': '10px', 'color': 'red' }"
                                                                :cell-style="{ 'text-align': 'center', 'font-size': '10px' }"
                                                                :row-style="{ height: '50px' }" lazy border>
                                                                <el-table-column label="物料" prop="tilte" />
                                                                <el-table-column label="编号" prop="sn" />
                                                                <el-table-column label="规格" prop="gk" />
                                                                <el-table-column label="单位" prop="unitname" />
                                                                <el-table-column label="当前库存" prop="nowstock">
                                                                    <template #default="{ row }">
                                                                        {{ row.nowstock }}{{ row.unitname }}
                                                                    </template>
                                                                </el-table-column>
                                                                <el-table-column label="需求数量" prop="ncout">
                                                                    <template #default="{ row }">
                                                                        {{ row.ncout }}{{ row.ncname }}
                                                                    </template>
                                                                </el-table-column>

                                                                <el-table-column label="操作" align="center">
                                                                    <template #default="{ row }">
                                                                        <!-- {{ row.gk }} -->
                                                                        <div>
                                                                            <div class="block">
                                                                                <el-button type="danger" plain
                                                                                    size="mini" icon="Edit"
                                                                                    style="font-size: 10px;"
                                                                                    @click="preadd2(row)"
                                                                                    v-if="row.nowstock < row.ncout">采购申请</el-button>
                                                                                <el-text v-else style="font-size: 10px;"
                                                                                    type="success">库存充足</el-text>
                                                                            </div>
                                                                        </div>
                                                                    </template>
                                                                </el-table-column>
                                                            </el-table>
                                                        </div>
                                                    </template>
                                                </el-table-column>
                                                <el-table-column label="物料" prop="tilte" />
                                                <el-table-column label="编号" prop="sn" />
                                                <el-table-column label="规格" prop="gk" />
                                                <el-table-column label="规格" prop="gk" />
                                                <el-table-column label="单位" prop="unitname">
                                                </el-table-column>
                                                <el-table-column label="当前库存" prop="nowstock">
                                                    <template #default="stockVo">
                                                        {{ stockVo.row.nowstock }}{{ stockVo.row.unitname }}
                                                    </template>
                                                </el-table-column>
                                                <el-table-column label="需求数量" prop="ncout">
                                                    <template #default="stockVo">
                                                        {{ stockVo.row.ncout }}{{ stockVo.row.ncname }}
                                                    </template>
                                                </el-table-column>
                                                <el-table-column label="操作" align="center">
                                                    <template #default="scope">
                                                        <div>
                                                            <div class="block">
                                                                <div class="mt-2">
                                                                    <el-button size="mini" icon="Edit"
                                                                        style="font-size: 10px;"
                                                                        @click="addworkshow(scope.row, props.row, stockVo.row.remark)">排程计划</el-button>
                                                                </div>
                                                                <div class="mt-2">
                                                                    <el-button size="mini" icon="Edit" type="warning"
                                                                        style="font-size: 10px;"
                                                                        @click="showout(scope.row, props.row, stockVo.row)">委外加工</el-button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </template>
                                                </el-table-column>
                                            </el-table>
                                        </div>
                                    </template>
                                </el-table-column>
                                <el-table-column label="物料" prop="stockVo.tilte" />
                                <el-table-column label="编号" prop="stockVo.sn" />
                                <el-table-column label="规格" prop="stockVo.gk" />
                                <el-table-column label="规格" prop="stockVo.gk" />
                                <el-table-column label="单位" prop="stockVo.unitname" />
                                <el-table-column label="当前库存" prop="stockVo.nowstock">
                                    <template #default="scope">
                                        <el-text>{{ scope.row.stockVo.nowstock }}{{ scope.row.stockVo.unitname
                                            }}</el-text>
                                    </template>
                                </el-table-column>
                                <el-table-column label="需求数量" prop="counts">
                                    <template #default="scope">
                                        <el-text>{{ scope.row.counts }}{{ scope.row.stockVo.unitname }}</el-text>
                                    </template>
                                </el-table-column>
                                <el-table-column label="已排成数量" prop="pcount" />
                                <el-table-column label="已出库数量" prop="ckcounts" />
                                <el-table-column label="未出库数量" prop="ckcounts">
                                    <template #default="scope">
                                        <el-text>{{ scope.row.counts - scope.row.ckcounts }}</el-text>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作" align="center">
                                    <template #default="scope">
                                        <div v-if="scope.row.status == 0">
                                            <div class="block">
                                                <div class="mt-2">
                                                    <el-button type="success" plain size="mini" icon="Edit"
                                                        style="font-size: 10px;"
                                                        @click="otherupdate(scope.row)">其他完成</el-button>
                                                </div>
                                                <div class="mt-2">
                                                    <el-button type="danger" plain size="mini" icon="Edit"
                                                        style="font-size: 10px;" @click="preadd(scope.row)"
                                                        v-if="scope.row.stockVo.nowstock < scope.row.counts">采购申请</el-button>
                                                </div>
                                                <div class="mt-2">
                                                    <el-text v-if="scope.row.stockVo.nowstock >= scope.row.counts"
                                                        style="font-size: 10px;" type="success">可申请出库</el-text>
                                                </div>
                                            </div>
                                            <div class="block">
                                                <el-button size="mini" icon="Edit" style="font-size: 10px;"
                                                    @click="addworkshow(scope.row, props.row, 0)"
                                                    v-if="scope.row.stockVo.nowstock < scope.row.counts">排程计划</el-button>
                                                <div class="mt-2">
                                                    <el-button size="mini" icon="Edit" type="warning"
                                                        style="font-size: 10px;"
                                                        @click="showout(scope.row, props.row, 0)"
                                                        v-if="scope.row.stockVo.otype == '成品' && scope.row.stockVo.nowstock < scope.row.counts">委外加工</el-button>
                                                </div>
                                            </div>
                                        </div>
                                        <div v-else>
                                            <el-tag type="warning">已操作完成</el-tag>
                                        </div>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="主键" align="center" prop="id" v-if="false" />
                <el-table-column label="客户" align="center" prop="custname">
                    <template #default="scope">
                        <span class="">{{ scope.row.custname }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="交货时间" align="center" prop="subtime" width="180">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.subtime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="客户订单号" align="center" prop="custno" />
                <!-- <el-table-column label="物料信息" align="center" prop="stids" /> -->
                <el-table-column label="订单时间" align="center" prop="otime" width="180">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.otime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="附件文档" align="center" prop="otfile" />
                <el-table-column label="文件" align="center" prop="ctfile" />
                <el-table-column label="状态" align="center" prop="status">
                    <template #default="scope">
                        <el-tag v-if="scope.row.status === '0'" type="success">未计划</el-tag>
                        <el-tag v-else-if="scope.row.status === '1'" type="warning">已计划</el-tag>
                        <el-tag v-else type="primary">未确认</el-tag>
                    </template>
                </el-table-column>
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
        <el-dialog v-model="buttonLoading" title="确认信息" width="500" center>
            <div id="printView" style="padding-left: 90px; padding-right: 20px;width: 100%;">
                <table class="table" border="0pm" cellspacing="0" align="left" width="100%"
                    style="font-size: 12px;font-family: SimSun;margin-bottom: 10px; table-layout:fixed;word-wrap:break-word;word-break:break-all">
                    <tr>
                        <td align="left"> </td>
                        <td style="padding:5px; font-weight: bolder; font-size: 15px;" align="right" colspan="2">单据编号：{{
                            ww.oid
                        }}</td>
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
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="buttonLoading = false">打印单据</el-button>
                    <el-button type="primary" @click="addoutsubmit">
                        确认出库
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <el-dialog v-model="workshow" title="确认信息" width="500" center>
            <el-form :model="workform" :rules="rules" label-width="140px">

                <el-form-item label="排程名称" prop="title">
                    <el-input v-model="workform.title" :value="pname" />
                </el-form-item>
                <el-form-item label="流转号" prop="title">
                    <el-input v-model="workform.sn" :value="deta.remark" disabled />
                </el-form-item>
                <el-form-item required label="加工物料" prop="proId">
                    <el-input placeholder="请输入内容" v-model="protitle" class="input-with-select" disabled="">
                    </el-input>
                </el-form-item>
                <el-form-item label="面向类型" prop="neednum">
                    <el-radio-group v-model="workform.fromtype" disabled>
                        <el-radio v-for="dict in diy_mark" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
                            }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="是否流转" prop="neednum">
                    <el-radio-group v-model="workform.isturn">
                        <el-radio v-for="dict in isturnon" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
                            }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="所需数量" prop="neednum">

                    <el-input-number v-model="workform.neednum" placeholder="请输入所需数量" style="width: 200px!important;" />
                </el-form-item>
                <el-form-item label="是否加急" prop="istop">
                    <el-radio-group v-model="workform.istop">
                        <el-radio v-for="dict in diyisno" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
                            }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="交货日期" prop="endtime">
                    <el-date-picker clearable v-model="workform.endtime" type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择需求日期" disabled>
                    </el-date-picker>
                </el-form-item>

                <el-form-item label="备注" prop="remark">
                    <el-input v-model="workform.remark" type="textarea" placeholder="请输入内容" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button :loading="buttonLoading" type="primary" @click="submitwork">确 定</el-button>
                    <el-button @click="workshow = false">取 消</el-button>
                </div>
            </template>
        </el-dialog>
        <el-dialog title="采购申请" v-model="preshow" width="500px" append-to-body>
            <el-form ref="procurementplanFormRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="流转号" prop="sn">
                    <el-input v-model="preform.sn" placeholder="请输入流转号" disabled />
                </el-form-item>
                <el-form-item label="产品" prop="proId">
                    <el-input v-model="preform.proId" :value="protitle" placeholder="请输入产品" disabled />
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-input v-model="preform.count" placeholder="请输入数量" />
                </el-form-item>
                <el-form-item label="采购价格" prop="prices">
                    <el-input-number v-model="preform.prices" :precision="2" placeholder="请输入数量" />
                </el-form-item>
                <el-form-item label="关联" prop="workId">
                    <el-input v-model="preform.workId" placeholder="请输入任务主表" disabled />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button :loading="buttonLoading" type="primary" @click="presubmit">提 交</el-button>
                    <el-button @click="preshow = false">取 消</el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 委外加工outfrom，submitForm3，Outoder与OutorderDe -->
        <el-dialog title="出库单" v-model="outshow" width="1000px" append-to-body>
            <template #header>
                <div class="card-header">
                    <span>新增</span>
                </div>
            </template>
            <el-form ref="orderFormRef" :model="outfrom" :rules="rules" label-width="100px">
                <el-form-item label="委外名称" prop="title">
                    <el-input v-model="outfrom.title" placeholder="请输入委外名称" />
                </el-form-item>
                <el-form-item label="订单号" prop="orders">
                    <el-input v-model="outfrom.orders" placeholder="请输入订单号" disabled />
                </el-form-item>
                <el-form-item label="外部编号" prop="sn">
                    <el-input v-model="outfrom.sn" placeholder="请输入外部编号">
                        <template #prepend>
                            <el-button type="primary" icon="Search" size="small" @click="getsn">生成</el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="价格" prop="prices">
                    <el-input-number :precision="4" v-model="outfrom.prices" placeholder="请输入价格" />
                </el-form-item>
                <el-form-item label="要求完成日期" prop="subtime">
                    <el-date-picker clearable v-model="outfrom.subtime" type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择要求完成日期" disabled>
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="出库时间" prop="outtime">
                    <el-date-picker clearable v-model="outfrom.outtime" type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择出库时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="委外供应商" prop="custid">
                    <el-text type="danger" class="mr-8">{{ custname }}</el-text>
                    <el-button type="primary" :icon="Search" size="small" class="ml-8 text-sm"
                        @click="findcustom">查询</el-button>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="outfrom.remark" placeholder="请输入价格" />
                </el-form-item>
                <div class="col-span-4">
                    <div class="flex">
                        <el-text type="danger" class="text-blod">物料信息</el-text>
                    </div>
                    <div class="m-4" v-if="orderDeList.length > 0">
                        <el-table v-loading="loading" :data="orderDeList" border>
                            <el-table-column label="编号" align="center" prop="id" v-if="false" />
                            <el-table-column label="物料" align="center" prop="stockVO.tilte" />
                            <el-table-column label="编号" align="center" prop="stockVO.sn" />
                            <el-table-column label="单位" align="center" prop="stockVO.unitname">
                                <template #default="scope">
                                    <el-select v-model="scope.row.stockVO.unitname" clearable filterable
                                        placeholder="请选择表单" style="width: 120px">
                                        <el-option v-for="item in unitmeasureList" :key="item.id" :label="item.title"
                                            :value="item.title" />
                                    </el-select>
                                </template>
                            </el-table-column>
                            <el-table-column label="单价" align="center" prop="prices">
                                <template #default="scope">
                                    <el-input v-model="scope.row.prices" placeholder="请输入单价"
                                        :formatter="(value) => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                        :parser="(value) => value.replace(/\$\s?|(,*)/g, '')" />
                                </template>
                            </el-table-column>

                            <el-table-column label="是否带料" align="center" prop="prices">
                                <template #default="scope">
                                    <el-radio-group v-model="scope.row.status">
                                        <el-radio v-for="dict in diyisno" :key="dict.value"
                                            :value="parseInt(dict.value)">{{
                                                dict.label
                                            }}</el-radio>
                                    </el-radio-group>
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
                            <el-table-column label="备注" align="center" prop="prices">
                                <template #default="scope">
                                    <el-input v-model="scope.row.remark"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column label="总价" align="center" prop="allprice">
                                <template #default="scope">
                                    <el-text type="danger" class="text-blod">¥{{ (scope.row.prices *
                                        scope.row.counts).toFixed(4) }}</el-text>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button :loading="buttonLoading" type="primary" @click="submitForm3">录 入</el-button>
                    <el-button @click="cancel">重 置</el-button>
                </div>
            </template>
        </el-dialog>
        <el-dialog :title="dialog2.title" v-model="dialog2.visible" width="500px" append-to-body>
            <el-card shadow="never">
                <template #header>
                    <el-form ref="queryFormRef" :model="params2" :inline="true">
                        <el-form-item label="类型" prop="type">
                            <el-select v-model="params2.type" placeholder="请选择类型" clearable>
                                <el-option v-for="dict in customtype" :key="dict.value" :label="dict.label"
                                    :value="dict.value" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="简称" prop="descp">
                            <el-input v-model="params2.descp" placeholder="请输入简称" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item label="银行账户" prop="cardnum">
                            <el-input v-model="params2.cardnum" placeholder="请输入银行账户" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item label="全称" prop="title">
                            <el-input v-model="params2.title" placeholder="请输入全称" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item label="联系方式" prop="phone">
                            <el-input v-model="params2.phone" placeholder="请输入联系方式" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item label="社会编码" prop="shnum">
                            <el-input v-model="params2.shnum" placeholder="请输入社会编码" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item label="收货地址" prop="address">
                            <el-input v-model="params2.address" placeholder="请输入收货地址" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item label="邮寄地址" prop="address1">
                            <el-input v-model="params2.address1" placeholder="请输入邮寄地址" clearable
                                @keyup.enter="handleQuery2" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="Search" @click="handleQuery2">搜索</el-button>
                            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                        </el-form-item>
                    </el-form>
                </template>

                <el-table v-loading="loading" :data="customList" style="width: 100%"
                    :header-cell-style="{ 'text-align': 'center' }"
                    :cell-style="{ 'text-align': 'center', 'font-size': '12px' }" :row-style="{ height: '50px' }" lazy
                    border>
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

                <pagination v-show="total2 > 0" :total="total2" v-model:page="params2.pageNum"
                    v-model:limit="params2.pageSize" @pagination="getList2" />
            </el-card>
        </el-dialog>
        <!-- 添加或修改销售订单对话框 -->
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
import { listOutstock, getOutstock, delOutstock, addOutstock, updateOutstock, getunitlist } from '@/api/mes/outstock';
import { OutstockVO, OutstockQuery, OutstockForm } from '@/api/mes/outstock/types';
import { listProcurementplan, getProcurementplan, delProcurementplan, addProcurementplan, updateProcurementplan } from '@/api/mes/procurementplan';
import { ProcurementplanVO, ProcurementplanQuery, ProcurementplanForm } from '@/api/mes/procurementplan/types';
import { listWork, getWork, delWork, addWork, updateWork } from '@/api/mes/work';
import { WorkVO, WorkQuery, WorkForm } from '@/api/mes/work/types';
import { listOutorderDe, getOutorderDe, delOutorderDe, addOutorderDe, updateOutorderDe } from '@/api/mes/outorderDe';
import { listOutorder, getOutorder, delOutorder, addOutorder, updateOutorder } from '@/api/mes/outorder';
import { Search } from '@element-plus/icons-vue';
import { count } from 'console';
import { status } from 'nprogress';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { customtype, diyisno, stocktype, diy_mark, isturnon, worktype } = toRefs<any>(proxy?.useDict('customtype', 'diyisno', 'stocktype', 'diy_mark', 'isturnon', 'worktype'));
const stockList = ref<StockVO[]>([]);
const customList = ref<CustomVO[]>([]);
const orderDeList = ref<OrderDeVO[]>([]);
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
const outfrom = ref<any>({
    id: undefined,
    title: undefined,
    custid: undefined,
    order: undefined,
    sn: undefined,
    stockids: undefined,
    stocktype: undefined,
    prices: undefined,
    subtime: undefined,
    outtime: undefined,
    status: undefined,
    remark: undefined,
})
const preshow = ref(false);
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
const workshow = ref(false);
const protitle = ref('');
const outshow = ref(false);
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
const orderoutList = ref<OutorderDeVO[]>([]);
const pname = ref('');
const showout = (row: any, row2: any, sh: any) => {
    orderDeList.value = [];
    outshow.value = true;
    const today = new Date();
    const year = today.getFullYear();
    deta.value.id = row.id
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    pname.value = row2.custname + '(' + year + '-' + month + '-' + day + ')';
    outfrom.value.title = pname.value
    outfrom.value.orders = row2.id
    outfrom.value.prices = 1;
    outfrom.value.subtime = row2.subtime
    outfrom.value.stockVO = row.stockVo
    outfrom.value.fromtype = '订单委外'
    if (sh == 0) {
        orderDe.value.stockVO = row.stockVo;
        orderDe.value.stockid = row.stockVo.id;
        orderDe.value.counts = row.counts;
        orderDe.value.prices = 0;
        orderDe.value.fromtype = '订单委外'
        orderDeList.value.push(orderDe.value);
    } else {
        orderDe.value.stockVO = row;
        orderDe.value.stockid = row.id;
        orderDe.value.counts = 1;
        orderDe.value.prices = 0;
        orderDe.value.fromtype = '订单委外'
        orderDeList.value.push(orderDe.value);
    }
    orderDe.value = {};

}
const getsn = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const min = today.getMinutes() + Math.random().toFixed(4) * 10000;
    outfrom.value.sn = year + month + day + min
}
const ww = ref<any>({})
const addworkshow = (row: any, row2: any, showa: any) => {
    deta.value = row
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    pname.value = row2.custname + '(' + year + '-' + month + '-' + day + ')';
    if (showa == 0) {
        protitle.value = row.stockVo.tilte
        workform.value.proId = row.stockid
        workform.value.endtime = row2.subtime
        workform.value.neednum = row.counts
        workform.value.title = pname.value
        workform.value.sn = row.remark
        workform.value.orderid = row2.id
        workshow.value = true;
    } else {
        protitle.value = row.tilte
        workform.value.proId = row.id
        workform.value.endtime = row2.subtime
        workform.value.neednum = row.ncout
        workform.value.title = pname.value
        workform.value.sn = showa
        deta.value.remark = showa
        workform.value.orderid = row2.id
        workshow.value = true;
    }
}
const cellClassName=({row,column,rowIndex,columnIndex})=>{
     if( column.label=="客户"){
       return {
				color: 'red'
			}
     }
   }
const submitForm3 = async () => {
    if (cuid.value == 0) {
        proxy?.$modal.msgError("请选择供应商");
        return
    }
    let ids = []
    let concatenatedDataArray = []; // 在循环外部定义一个数组来存储每次循环中的数据

    await Promise.all(orderDeList.value.map(async (item) => {
        item.allprice = (item.prices * item.counts).toFixed(4)
        let rec = await addOutorderDe(item).finally(() => buttonLoading.value = false);
        let dataArray = rec.data;
        concatenatedDataArray.push(dataArray);
    }))
    let concatenatedData = concatenatedDataArray.join(',');
    outfrom.value.stockids = concatenatedData
    outfrom.value.custid = cuid.value
    outfrom.value.fromtype = "订单委外"
    await addOutorder(outfrom.value).finally(() => buttonLoading.value = false);
    //   const res = await getOrderDe(deta.value.id);
    //         Object.assign(upoder.value, res.data);
    //         let allcount = upoder.value.pcount + deta.value.counts;

    //         upoder.value.pcount = outfrom.value.count
    //         if (allcount >= upoder.value.counts)
    //             upoder.value.status = '1'
    //         await updateOrderDe(upoder.value);
    //   proxy?.$modal.msgSuccess("操作成功");
    dialog3.visible = false;
    outshow.value = false

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
const deta = ref<any>({})
const preadd = (row: any) => {
    deta.value = row
    protitle.value = row.stockVo.tilte
    preform.value.sn = row.remark
    preform.value.workId = 100
    preform.value.count = row.counts
    preform.value.status = '0'
    preform.value.proId = row.stockid
    preshow.value = true
}
const preadd2 = (row: any) => {
    deta.value = row
    protitle.value = row.tilte
    preform.value.sn = row.remark
    preform.value.workId = 100
    preform.value.count = row.counts
    preform.value.status = '0'
    preform.value.proId = row.stockid
    preshow.value = true
}
const otherupdate = async (row: any) => {
    row.status = '1'
    await updateOrderDe(row)
    proxy?.$modal.msgSuccess("已完成");
}
const btsshow = (row: any) => {
    buttonLoading.value = true;
    deta.value = row
    console.log(row)
    ww.value.custno = row.custno
    ww.value.customtitle = row.customVo.title
    ww.value.oid = row.id
    ww.value.customname = row.customVo.cname
    ww.value.phone = row.customVo.cname
    ww.value.address = row.customVo.address
    ww.value.times = row.otime
    ww.value.orders = row.orderList
}
const submit2 = (row: any) => {
    orderDe.value.stockVO = row;
    orderDe.value.stockid = row.id;
    orderDe.value.counts = 1;
    orderDeList.value.push(orderDe.value);
    orderDe.value = {};
    console.log(orderDeList.value);
}
const preform = ref<any>({
    id: undefined,
    sn: undefined,
    proId: undefined,
    count: undefined,
    workId: undefined,
    status: undefined,
    remark: undefined,
    prices: undefined
})
const workform = ref<any>({
    id: undefined,
    sn: undefined,
    title: undefined,
    fromtype: 1,
    proId: undefined,
    neednum: 1,
    currnum: undefined,
    processcount: undefined,
    checkcount: undefined,
    istop: 0,
    endtime: undefined,
    currprocess: undefined,
    orderid: undefined,
    remark: undefined,
    grow: 1,
    ismrp: 1,
    isturn: 0,
    status: 0
})
const upoder = ref<any>({
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
const addoutsubmit = async (row: any) => {
    buttonLoading.value = false;
    let params = {
        proId: deta.value.stockVo.id,
        formtype: '销售成品出库',
        count: deta.value.counts,
        sn: deta.value.remark,
        status: '1',
        remark: '1'
    }
    let res = await addOutstock(params);
    if (res.code == 200) {
        const res = await getOrderDe(deta.value.id);
        Object.assign(upoder.value, res.data);
        let allcount = upoder.value.pcount + deta.value.counts;

        upoder.value.pcount = deta.value.counts
        if (allcount >= upoder.value.counts)
            upoder.value.status = '1'
        await updateOrderDe(upoder.value);
        proxy?.$modal.msgSuccess("出库成功");
        deta.value = {}
        getList();
    }
}
const submitwork = async () => {
    let res = await addWork(workform.value);
    if (res.code == 200) {
        const res = await getOrderDe(deta.value.id);
        Object.assign(upoder.value, res.data);
        upoder.value.status = '1'
        await updateOrderDe(upoder.value);
        proxy?.$modal.msgSuccess("加工单添加成功");
        workshow.value = false
    }
}
const presubmit = async () => {
    preform.value.fromtype = '订单采购'
    let res = await addProcurementplan(preform.value);
    if (res.code == 200) {
        const res = await getOrderDe(deta.value.id);
        Object.assign(upoder.value, res.data);
        upoder.value.status = '1'
        await updateOrderDe(upoder.value);
        proxy?.$modal.msgSuccess("请购单添加成功");
        preshow.value = false
    }
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
    dialog2.visible = true;
    dialog2.title = '选择委外单位';

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
    params2.value.type = '供应商'
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
    dialog2.visible = false;
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
    console.log(row)
    const _id = row?.id || ids.value[0]
    const res = await getOrder(_id);
    Object.assign(form.value, res.data);
    form.value.fp = row.fp
    await updateOrder(form.value).finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("修改成功");
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
const rowState = (arg, rowindex) => {
    
}
onMounted(() => {
    getList();
    getList3();
});
</script>
<style>
::v-deep .el-table::before {
    border-bottom: 1px solid rgb(16, 187, 255);
    height: 4px;
}
::v-deep .el-table tr:hover>td { 
        background-color:#90c0f1; 
    }	

</style>