<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 2021/6/18
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--  header：做响应式布局  --%>
<header id="crt-header">
    <nav id="crt-nav-sm" class="crt-nav hidden-lg hidden-md">
        <ul class="clear-list">
            <li>
                <a href="${ctx}">
                    <img class="avatar avatar-42" src="${ctx}/${user.photo}" alt="">
                </a>
            </li>
            <li><a href="${ctx}/education/front"><span
                    class="crt-icon crt-icon-book"></span></a></li>
            <li><a href="${ctx}/experience/front"><span
                    class="crt-icon crt-icon-experience"></span></a></li>
            <li><a href="${ctx}/project/front"><span
                    class="crt-icon crt-icon-wrench"></span></a></li>
            <li><a href="${ctx}/contact/front"><span
                    class="crt-icon crt-icon-contact"></span></a></li>
            <li><a href="${ctx}/user/admin"><span
                    class="crt-icon crt-icon-key"></span></a></li>
        </ul>
    </nav><!-- #crt-nav-sm --></header><!-- #crt-header -->
