(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d8a35a4"],{"19b1":function(e,t,a){},"1a83":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"dashboard-container"},[a("div",{staticClass:"settings"},[a("el-row",[a("el-col",{attrs:{span:24}},[a("div",{staticClass:"grid-content bg-purple-dark"},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dialogFormVisible=!0}}},[e._v("新增设备")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:e.startall}},[e._v("启动全部")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:e.stopall1}},[e._v("停止全部")])],1)])],1),e._v(" "),a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData.filter((function(t){return!e.search||t.name.toLowerCase().includes(e.search.toLowerCase())})),"default-sort":{prop:"date",order:"descending"}}},[a("el-table-column",{attrs:{prop:"id.id",label:"NO.",formatter:e.formatter,width:"290%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"name",label:"Name",formatter:e.formatter,width:"100%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"type",label:"Type",formatter:e.formatter,width:"140%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"label",label:"Label",sortable:"",width:"90%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"additionalInfo",label:"Desc",sortable:"",width:"90%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"tenantId.entityType",label:"Group",sortable:"",width:"90%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"Status",label:"Status",sortable:"",width:"90%"}}),e._v(" "),a("el-table-column",{attrs:{prop:"createdTime",label:"CreatedTime",sortable:"",width:"160%"}}),e._v(" "),a("el-table-column",{attrs:{align:"right"},scopedSlots:e._u([{key:"header",fn:function(t){return[a("el-input",{attrs:{size:"mini",placeholder:"输入关键字搜索"},model:{value:e.search,callback:function(t){e.search=t},expression:"search"}})]}},{key:"default",fn:function(t){return[a("el-button",{attrs:{size:"mini",type:"warning"},on:{click:function(a){return e.handlebianji(t.$index,t.row)}}},[e._v("编辑")]),e._v(" "),a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(a){return e.handleHMI(t.$index,t.row)}}},[e._v("HMI")]),e._v(" "),a("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(a){return e.handleDelete(t.$index,t.row)}}},[e._v("Delete")]),e._v(" "),a("el-button",{attrs:{size:"mini",type:"success"},on:{click:function(a){return e.handlebianji1(t.$index,t.row)}}},[e._v("授权")])]}}])})],1)],1),e._v(" "),a("el-dialog",{attrs:{title:"新增设备",visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[a("el-form",{attrs:{model:e.form}},[a("el-form-item",{attrs:{label:"Name :","label-width":e.formLabelWidth}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"Type :","label-width":e.formLabelWidth}},[a("el-select",{attrs:{filterable:"","allow-create":"",placeholder:"请选择设备类型"},on:{focus:e.fun1,change:e.fun2},model:{value:e.form.type,callback:function(t){e.$set(e.form,"type",t)},expression:"form.type"}},e._l(e.selectData,(function(e){return a("el-option",{key:e.type,attrs:{label:e.type,value:e.type}})})),1)],1),e._v(" "),a("el-form-item",{attrs:{label:"label :","label-width":e.formLabelWidth}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.label,callback:function(t){e.$set(e.form,"label",t)},expression:"form.label"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"desc :","label-width":e.formLabelWidth}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.additionalInfo,callback:function(t){e.$set(e.form,"additionalInfo",t)},expression:"form.additionalInfo"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取 消")]),e._v(" "),a("el-button",{attrs:{type:"success"},on:{click:function(t){return e.handlesubmit()}}},[e._v("确 定")])],1)],1),e._v(" "),a("el-dialog",{attrs:{title:"修改设备",visible:e.dialogFormVisible1},on:{"update:visible":function(t){e.dialogFormVisible1=t}}},[a("el-form",{attrs:{model:e.form1}},[a("el-form-item",{attrs:{label:"Name :","label-width":e.formLabelWidth}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.form1.name,callback:function(t){e.$set(e.form1,"name",t)},expression:"form1.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"Type :","label-width":e.formLabelWidth}},[a("el-select",{attrs:{filterable:"","allow-create":"",placeholder:"请选择设备类型"},on:{focus:e.fun1,change:e.fun2},model:{value:e.form1.type,callback:function(t){e.$set(e.form1,"type",t)},expression:"form1.type"}},e._l(e.selectData,(function(e){return a("el-option",{key:e.type,attrs:{label:e.type,value:e.type}})})),1)],1),e._v(" "),a("el-form-item",{attrs:{label:"label :","label-width":e.formLabelWidth}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.form1.label,callback:function(t){e.$set(e.form1,"label",t)},expression:"form1.label"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"desc :","label-width":e.formLabelWidth}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.form1.additionalInfo,callback:function(t){e.$set(e.form1,"additionalInfo",t)},expression:"form1.additionalInfo"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.dialogFormVisible1=!1}}},[e._v("取 消")]),e._v(" "),a("el-button",{attrs:{type:"success"},on:{click:function(t){return e.handlesubmit1()}}},[e._v("确 定")])],1)],1)],1)},o=[],i=(a("7f7f"),a("b775"));function n(){return Object(i["a"])({url:"api/tenant/devices",method:"get",params:{limit:"11"}})}function r(){return Object(i["a"])({url:"api/device/types",method:"get"})}function s(e){return Object(i["a"])({url:"/api/device",method:"POST",data:e})}function c(e){return Object(i["a"])({url:"/api/device/"+e,method:"DELETE"})}function d(){return Object(i["a"])({url:"/api/exec/all",method:"POST"})}function f(e){return Object(i["a"])({url:"/api/stop/all",method:"POST",data:e})}function m(e){return Object(i["a"])({url:"/api/devices",method:"get",params:{deviceIds:e}})}var u={data:function(){return{selectData:{},emet:"",select:{select1:"",select2:""},search:"",deviceId:"",device:{name:"",type:"",label:"",additionalInfo:""},tableData:[{}],dialogFormVisible:!1,form:{name:"",type:"",label:"",additionalInfo:""},formLabelWidth:"120px",dialogFormVisible1:!1,form1:{name:"",type:"",label:"",additionalInfo:""},formLabel1Width:"120px"}},created:function(){this.updatedeviceTable()},methods:{updatedeviceTable:function(){var e=this;n().then((function(t){console.log(t);for(var a=0;a<t.data.length;a++){var l=new Date(1*t.data[a].createdTime);console.log(t.data[a].createdTime);var o=l.getFullYear(),i=l.getMonth()+1,n=l.getDate(),r=l.getHours(),s=l.getMinutes(),c=l.getSeconds();i=i<10?"0"+i:i,n=n<10?"0"+n:n,r=r<10?"0"+r:r,s=s<10?"0"+s:s,c=c<10?"0"+c:c,l=o+"-"+i+"-"+n+"  "+r+":"+s+":"+c+" ",t.data[a].createdTime=l}e.tableData=t.data,e.emet=t.data.id.entityType,e.form.name="",e.form.type="",e.form.label="",e.form.additionalInfo=""})),d().then((function(e){console.log(e)}))},fun1:function(){var e=this;r().then((function(t){e.selectData=t,console.log(e.selectData)}))},startall:function(){d().then((function(e){console.log(e)}))},stopall1:function(){var e=this;f(this.emet).then((function(t){console.log(e.emet)}))},handlebianji:function(e,t){var a=this;this.dialogFormVisible1=!0,console.log("kaishi"),console.log(t.id.id),m(t.id.id).then((function(e){console.log(e),a.form1.name=e[0].name,a.form1.type=e[0].type,a.form1.label=e[0].label,a.form1.additionalInfo=e[0].additionalInfo}))},handlebianji1:function(e,t){this.dialogFormVisible1=!0,console.log(this.tableData[e]),this.form1.name=t.name,this.form1.type=t.type,this.form1.label=t.label,this.form1.additionalInfo=t.additionalInfo,console.log(t.type)},handleDelete:function(e,t){var a=this;this.$confirm("此操作将永久删除该设备, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){c(t.id.id).then((function(e){a.$message({type:"success",message:"删除成功!"}),a.updatedeviceTable()}))})).catch((function(){}))},handleHMI:function(e,t){var a=this.tableData[e];console.log("zhi.id.id"),this.$router.push({path:"/HMI",query:{num:a.id.id,num1:a.id.entityType,from:"/test"}})},handlesubmit:function(){var e=this;console.log("123"),this.device.name=this.form.name,this.device.type=this.form.type,this.device.label=this.form.label,this.device.additionalInfo=this.form.additionalInfo,console.log(this.device),s(this.device).then((function(t){e.dialogFormVisible=!1,e.updatedeviceTable()}))},handlesubmit1:function(){var e=this;console.log("123"),this.device.name=this.form1.name,this.device.type=this.form1.type,this.device.label=this.form1.label,this.device.additionalInfo=this.form1.additionalInfo,console.log(this.device),s(this.device).then((function(t){e.dialogFormVisible1=!1,e.updatedeviceTable()}))}},formatter:function(e,t){return e.address}},b=u,p=(a("c992"),a("2877")),h=Object(p["a"])(b,l,o,!1,null,"103bcec2",null);t["default"]=h.exports},c992:function(e,t,a){"use strict";var l=a("19b1"),o=a.n(l);o.a}}]);