(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-227c5b41"],{7884:function(t,a,e){"use strict";e.r(a);var l=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"demo-image"},[t._l(t.fits,(function(a){return e("div",{key:a,staticClass:"block"},[e("span",{staticClass:"demonstration"},[t._v(t._s(a))]),t._v(" "),e("el-image",{staticStyle:{width:"200px",height:"300px"},attrs:{src:t.url,fit:a}})],1)})),t._v(" "),e("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tableData,border:""}},[e("el-table-column",{attrs:{fixed:"",prop:"systemName",label:"系统名称"}}),t._v(" "),e("el-table-column",{attrs:{prop:"systemVersion",label:"版本"}}),t._v(" "),e("el-table-column",{attrs:{prop:"runningTime",label:"运行时间"}}),t._v(" "),e("el-table-column",{attrs:{prop:"cpuUtilization",label:"CPU利用率"}}),t._v(" "),e("el-table-column",{attrs:{prop:"memoryUtilization",label:"内存利用率"}}),t._v(" "),e("el-table-column",{attrs:{prop:"swapUtilization",label:"swap利用率"}}),t._v(" "),e("el-table-column",{attrs:{prop:"storageSpaceUtilization",label:"存储空间利用率"}}),t._v(" "),e("el-table-column",{attrs:{prop:"numOfDevice",label:"设备个数"}}),t._v(" "),e("el-table-column",{attrs:{prop:"numOfProtocol",label:"协议数"}}),t._v(" "),e("el-table-column",{attrs:{prop:"numOfDataCache",label:"缓存数据量"}})],1)],2)},n=[],s=e("b775");function o(){return Object(s["a"])({url:"/api/systemparameter",method:"get"})}var r={data:function(){return{fits:[""],url:e("7af1"),tableData:[]}},created:function(){this.updatedsysPara()},methods:{updatedsysPara:function(){var t=this;o().then((function(a){console.log(a[0]),t.tableData=a}))}}},i=r,c=e("2877"),p=Object(c["a"])(i,l,n,!1,null,null,null);a["default"]=p.exports},"7af1":function(t,a,e){t.exports=e.p+"static/img/lo.b5d2e8e6.png"}}]);