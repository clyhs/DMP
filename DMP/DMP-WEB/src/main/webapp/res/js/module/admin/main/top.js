var aTopNav = [
	'<div class="f_header" ><div class="wrapper">'
	,'<h1><span><span raw="true">数据分析管理平台</span></span></h1>'
	,'<div class="fct">'
	,'<ul>'
	,'<li><span class="user" id="admin_name"></span></li>'
	,'<li><a class="favor" href="javascript:void(addFavorite())" rel="sidebar" title="查看收藏夹" >收藏夹</a></li>'
	,'<li><a class="helpscreen" href="javascript:void(0)" title="帮组信息">帮助</a></li>'
	,'<li><a class="placard" href="javascript:void(0)" title="查看公告信息">公告</a></li>'
	,'<li><a class="editPw" href="javascript:void(changePw())" subsys="SysManm" title="修改登录密码">修改密码</a></li>'
	,'<li><a class="lockscreen" href="javascript:void(lockScreen())" title="锁定已登录界面" >锁定</a></li>'
	,'<li><a id="linkExit" class="exitscreen" href="javascript:void(Exit())" title="退出系统" >退出</a></li>'
	,'</ul>'
	,'</div>'
	,'</div></div>'
	,'<div class="f_nav"><div class="wrapper">'
	,'<ul><span>'
	,'<span  id = "toolbar_id"></span><span  id = "toolbar_id2"></span></span>'	
	,'</ul>'
	,'</div></div>'];

Ext.TopNav = Ext.extend(Ext.BoxComponent, {
	region: 'north',
	height: 105,
    id: 'TopNav',
    border: false,
    autoEl: {
        tag: 'div',
        cls: 'MainTopBG',
        html:aTopNav.join('')
    }
});