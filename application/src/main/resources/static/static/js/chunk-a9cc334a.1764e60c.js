(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-a9cc334a"],{"0d67":function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-tabs",{attrs:{type:"border-card"}},[a("el-tab-pane",{attrs:{label:"设备状态"}},[a("div",{staticClass:"dashboard-container"},[a("div",{staticClass:"settings"},[a("el-row",{staticClass:"configitems"},[a("el-divider",{attrs:{"content-position":"left"}},[a("i",{staticClass:"el-icon-s-grid"},[t._v("  基本信息")])])],1),t._v(" "),a("el-row",[a("el-col",{attrs:{span:10,offset:2}},[t._v("\n            I D :\n            "),a("el-input",{staticStyle:{width:"70%"},attrs:{placeholder:"请输入内容",disabled:!0},model:{value:t.input,callback:function(e){t.input=e},expression:"input"}})],1),t._v("配置认证方式 ：\n          "),a("el-input",{staticStyle:{width:"15%"},attrs:{placeholder:"请输入内容",disabled:!0},model:{value:t.access1,callback:function(e){t.access1=e},expression:"access1"}}),t._v(" "),a("el-input",{staticStyle:{width:"25%"},attrs:{placeholder:"请输入内容"},model:{value:t.accessid,callback:function(e){t.accessid=e},expression:"accessid"}})],1),t._v(" "),a("el-row",[a("el-col",{attrs:{span:10,offset:2}},[t._v("\n            Protocol :\n            "),t._v(" "),a("el-select",{on:{focus:t.fun1,change:function(e){return t.fun2(t.value1)}},model:{value:t.value1,callback:function(e){t.value1=e},expression:"value1"}},t._l(t.selectdata,(function(t){return a("el-option",{key:t.name,attrs:{label:t.name,value:t.name}})})),1)],1),t._v("设备运行 ：\n          "),a("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#ff4949"},model:{value:t.status,callback:function(e){t.status=e},expression:"status"}})],1)],1),t._v(" "),a("el-row",[a("el-col",{attrs:{span:12}},[a("div",{staticClass:"grid-content bg-purple"},[a("div",{staticClass:"settings"},[a("el-row",{staticClass:"configitems"},[a("el-divider",{attrs:{"content-position":"left"}},[a("i",{staticClass:"el-icon-s-grid"},[t._v("  配置信息")])])],1),t._v(" "),a("el-row",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tableData.filter((function(e){return!t.search||e.name.toLowerCase().includes(t.search.toLowerCase())})),"default-sort":{prop:"date",order:"descending"}}},[a("el-table-column",{attrs:{prop:"name",label:"name",sortable:"",formatter:t.formatter,width:"100"}}),t._v(" "),a("el-table-column",{attrs:{prop:"attr",label:"attr",width:"160"}}),t._v(" "),a("el-table-column",{attrs:{align:"left"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{size:"mini",type:"warning"},on:{click:function(a){return t.peizhibianji(e.$index,e.row)}}},[t._v("编辑")]),t._v(" "),a("el-button",{attrs:{size:"mini",type:"success"},on:{click:function(a){return t.handleDelete(e.$index,e.row)}}},[t._v("保存")])]}}])})],1)],1)],1)])]),t._v(" "),a("el-col",{attrs:{span:12}},[a("div",{staticClass:"grid-content bg-purple-light"})])],1)],1)]),t._v(" "),a("el-tab-pane",{attrs:{label:"审计"}},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.audittableData.filter((function(e){return!t.search||e.name.toLowerCase().includes(t.search.toLowerCase())})),"default-sort":{prop:"date",order:"descending"}}},[a("el-table-column",{attrs:{prop:"createdTime",label:"时间",formatter:t.formatter,sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"userId.entityType",label:"类型",formatter:t.formatter,sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"entityName",label:"名称",formatter:t.formatter}}),t._v(" "),a("el-table-column",{attrs:{prop:"userName",label:"用户",sortable:""}}),t._v(" "),a("el-table-column",{attrs:{prop:"actionType",label:"操作"}}),t._v(" "),a("el-table-column",{attrs:{prop:"actionStatus",label:"状态"}}),t._v(" "),a("el-table-column",{attrs:{align:"right",label:"详情"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{size:"mini",type:"warning"},on:{click:function(a){return t.handlebianji(e.$index,e.row)}}},[t._v("详情")])]}}])})],1)],1),t._v(" "),a("el-dialog",{attrs:{title:"修改配置信息",visible:t.centerDialogVisible,width:"30%",center:""},on:{"update:visible":function(e){t.centerDialogVisible=e}}},[a("span",[a("el-row",[a("div",{staticClass:"demo-input-suffix"},[t._v("\n          name：\n          "),a("el-input",{staticStyle:{width:"20%"},model:{value:t.input1,callback:function(e){t.input1=e},expression:"input1"}}),t._v("attr:\n          "),a("el-input",{staticStyle:{width:"50%"},model:{value:t.input2,callback:function(e){t.input2=e},expression:"input2"}})],1)])],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.centerDialogVisible=!1}}},[t._v("取 消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:t.peizhitijiao}},[t._v("确 定")])],1)])],1)},i=[],l=(a("8615"),a("ac6a"),a("456d"),a("7f7f"),a("b775"));function s(t){return Object(l["a"])({url:"/api/device/"+t+"/credentials",method:"get"})}function o(t,e,a){return Object(l["a"])({url:"/api/"+t+"/"+e+"/plugin/exec?status=",method:"post",params:{cron:"0/1 * * * * ?"},data2:a})}function c(){return Object(l["a"])({url:"/api/proto/handle/plugin/all",method:"get"})}function r(t,e,a){return Object(l["a"])({url:"/api/"+t+"/"+e+"/device/config",method:"get",params:{pluginName:a}})}function u(t,e,a,n){return Object(l["a"])({url:"/api/"+t+"/"+e+"/device/save",method:"post",params:{protocol:a},data:n})}var p=a("77c4"),d={data:function(){return{peimap:{input1:this.input2},selectdata:{},peizhidata:{},input1:"",input2:"",entype:"",status:!0,input:"",access1:"",accessid:"",centerDialogVisible:!1,value1:"",tableData:[{name:"",attr:""}],audittableData:[{}]}},created:function(){this.getinfo(),this.update(),this.getaudit()},methods:{getinfo:function(){var t=this;console.log("2");var e=this.$route.query.num,a=this.$route.query.num1;console.log(e),console.log(a),this.entype=a,t.input=e,console.log(this.entype)},update:function(){var t=this;s(this.input).then((function(e){console.log(e.credentialsType),t.access1=e.credentialsType,t.accessid=e.credentialsId})),o(this.input,this.entype,this.status).then((function(t){console.log(1)}))},fun1:function(){var t=this;console.log("kaishi"),c().then((function(e){console.log(e.length),console.log("jjjj"),t.selectdata=e,console.log(t.selectdata),console.log(t.selectdata.name)}))},fun2:function(){var t=this;console.log("kaishi"),r(this.entype,this.input,this.value1).then((function(e){console.log(t.tableData.name),console.log(Object.keys(e)),t.tableData[0].name=Object.keys(e),t.tableData[0].attr=Object.values(e)}))},peizhibianji:function(t,e){this.centerDialogVisible=!0,this.input1=this.tableData[0].name,this.input2=this.tableData[0].attr},peizhitijiao:function(){var t=this;console.log("yubei"),console.log(this.peimap);this.input1[0];u(this.entype,this.input,this.input1[0],{tast:this.input2[0]}).then((function(e){t.centerDialogVisible=!1,t.fun2(),console.log(e)})),this.$message({message:"修改配置信息成功",type:"success"})},getaudit:function(){var t=this;Object(p["b"])(this.entype,this.input).then((function(e){console.log(e);for(var a=0;a<e.data.length;a++){var n=new Date(1*e.data[a].createdTime);console.log(e.data[a].createdTime);var i=n.getFullYear(),l=n.getMonth()+1,s=n.getDate(),o=n.getHours(),c=n.getMinutes(),r=n.getSeconds();l=l<10?"0"+l:l,s=s<10?"0"+s:s,o=o<10?"0"+o:o,c=c<10?"0"+c:c,r=r<10?"0"+r:r,n=i+"-"+l+"-"+s+"  "+o+":"+c+":"+r+" ",e.data[a].createdTime=n}t.audittableData=e.data}))}}},f=d,b=(a("6471"),a("2877")),h=Object(b["a"])(f,n,i,!1,null,"659c38da",null);e["default"]=h.exports},"504c":function(t,e,a){var n=a("9e1e"),i=a("0d58"),l=a("6821"),s=a("52a7").f;t.exports=function(t){return function(e){var a,o=l(e),c=i(o),r=c.length,u=0,p=[];while(r>u)a=c[u++],n&&!s.call(o,a)||p.push(t?[a,o[a]]:o[a]);return p}}},6471:function(t,e,a){"use strict";var n=a("993d"),i=a.n(n);i.a},"77c4":function(t,e,a){"use strict";a.d(e,"a",(function(){return i})),a.d(e,"b",(function(){return l}));var n=a("b775");function i(t){return Object(n["a"])({url:"api/audit/logs",method:"get",params:{limit:t}})}function l(t,e){return Object(n["a"])({url:"/api/audit/logs/entity/"+t+"/"+e,method:"get",params:{limit:11}})}},8615:function(t,e,a){var n=a("5ca1"),i=a("504c")(!1);n(n.S,"Object",{values:function(t){return i(t)}})},"993d":function(t,e,a){}}]);