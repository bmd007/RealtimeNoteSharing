<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <!--     Fonts and icons     -->
    <link rel="stylesheet" href="resources/ahmad/css/material-kit.css" type="text/css"/>

    <link rel="stylesheet" type="text/css"
          href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Roboto+Slab:400,700|Material+Icons"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css"/>

    <script src='https://www.google.com/recaptcha/api.js'></script>

</head>
<body class="signup-page  form-check-sign">

<div class="page-header header-filter" filter-color="purple"
     style="background-image: url('resources/ahmad/js/lib/material-kit-html-v2.0.2/BS4/assets/img/kit/free/bg7.jpg'); background-size: cover; background-position: top center;">
    <div class="container">
        <div class="row">
            <div class="col-md-4 ml-auto mr-auto">
                <div class="card card-signup">
                    <h2 class="card-title text-center">Login</h2>
                    <div class="card-body">
                        <div class="row align-content-center">

                            <c:url var="loginUrl" value="/login"/>
                            <form name="f" role="form" action="login" method="post">

                                <c:if test="${param.error != null}">
                                    <div class="alert alert-danger">
                                        <p>Invalid username and password.</p>
                                    </div>
                                </c:if>
                                <c:if test="${param.logout != null}">
                                    <div class="alert alert-success">
                                        <p>You have been logged out successfully.</p>
                                    </div>
                                </c:if>


                                <div class="form-group">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                                    <span class="input-group-text">
                                                        <i class="material-icons">phone</i>
                                                    </span>
                                        </div>
                                        <input class="form-control" placeholder="phone#" name="username" autofocus>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                                    <span class="input-group-text">
                                                        <i class="material-icons">lock_outline</i>
                                                    </span>
                                        </div>
                                        <input class="form-control" placeholder="Password" name="password"
                                               type="password">
                                    </div>
                                </div>


                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>


                                <div class="form-group">
                                    <div class="g-recaptcha"
                                         data-sitekey="6LdpK1MUAAAAAKTRMvbZ0BpAv7WMRDO7tlH2vgnE"></div>
                                </div>


                                <div class="text-center">
                                    <input type="submit" class="btn btn-lg" value="login">

                                    <a value="Signup" class="btn btn-lg" href="/register.xhtml">Signup</a>

                                </div>


                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<!--   Core JS Files   -->
<script src="../resources/ahmad/js/core/jquery.min.js"></script>
<script src="../resources/ahmad/js/core/popper.min.js"></script>
<script src="../resources/ahmad/js/bootstrap-material-design.js"></script>
<!--  Plugin for Date Time Picker and Full Calendar Plugin  -->
<script src="../resources/ahmad/js/plugins/moment.min.js"></script>
<!--	Plugin for the Datepicker, full documentation here: https://github.com/Eonasdan/bootstrap-datetimepicker -->
<script src="../resources/ahmad/js/plugins/bootstrap-datetimepicker.min.js"></script>
<!--	Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
<script src="../resources/ahmad/js/plugins/nouislider.min.js"></script>
<!-- Material Kit Core initialisations of plugins and Bootstrap Material Design Library -->
<script src="../resources/ahmad/js/material-kit.js?v=2.0.2"></script>
</body>

</html>