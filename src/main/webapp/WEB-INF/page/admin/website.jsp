<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--转发出去的jsp拼接相对路径：根据请求的URL向上找，再拼接-很多时候找不到其他关联的资源
    所以，利用contextPath去做相对路径，浏览器就会拿IP和端口在客户端拼接我们设置的相对路径--%>

<!DOCTYPE html>
<html lang="zh">
<head>
    <title>小码哥简历管理-网站信息</title>
    <%@include file="common/head.jsp" %>
</head>

<body class="theme-blue">

    <%@include file="common/middle.jsp" %>

    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <div class="card">
                        <div class="header">
                            <h2>网站信息</h2>
                        </div>
                        <div class="body">
                            <form class="form-validation" method="post" action="${ctx}/website/save">
                                <input type="hidden" name="id" value="${website.id}">
                                <div class="row">
                                    <div class="col-lg-2 col-md-2 col-sm-3 col-xs-3 form-control-label">
                                        <label for="footer">底部</label>
                                    </div>
                                    <div class="col-lg-10 col-md-10 col-sm-9 col-xs-9">
                                        <div class="form-group">
                                            <div class="form-line">
                                                <textarea name="footer" maxlength="1000"
                                                          id="footer" cols="30" rows="5"
                                                          class="form-control no-resize"
                                                          placeholder="底部">${website.footer}</textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-lg-offset-2 col-md-offset-2 col-sm-offset-3 col-xs-offset-3">
                                        <button class="btn btn-primary waves-effect m-l-15" type="submit">保存</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%@include file="common/foot.jsp" %>
    <script>
        // $('.menu .list li:nth-of-type(10)').addClass('active')
        $('.menu .list .website').addClass('active')
    </script>
</body>

</html>
