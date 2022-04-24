jQuery(function($) {
    ALTER = function (id) {
        $("#formularios").empty().load("./PessoaServlet?cmd=con&id=" + id + "&tp=A");
        $("#formularios").empty().load("./VeiculosServlet?cmd=con&id=" + id + "&tp=A");
    }
    VIEW = function (id) {
        $("#formularios").empty().load("./ClienteSvt?cmd=con&id=" + id + "&tp=V");
    }
    DEL = function (id) {
        if (confirm("Deseja realmente excluir este registro!")) {
            $.ajax({
                type: "POST",
                url: './ClienteSvt',
                data: {CLIDCLIENTE: id, cmd: "del", USSTATUS: 99},
                dataType: "json",
                success: function (rs) {
                    if (rs != "0") {
                        $("#CL_" + id).remove();
                    } else {
                        $("#danger").fadeIn("fast");
                        $("#msgDanger").empty();
                        $("#msgDanger").html("Problemas na exclus&atilde;o do cliente!");
                        setInterval(function () {
                            $("#danger").hide();
                        }, 10000);
                    }
                }, error: function (jqXHR, exception) {
                    switch (jqXHR.status) {
                        case 0:
                            console.log('Not connect.\n Verify Network.\n');
                            break;
                        case 404:
                            console.log('Requested page not found. [404]\n');
                            break;
                        case 500:
                            console.log('Internal Server Error [500].\n');
                            break;
                        default:
                            console.log('Erro não catalogado: JQXHR|STATUS!\n');
                    }

                    switch (exception) {
                        case 'parsererror':
                            console.log('Requested JSON parse failed.');
                            break;
                        case 'timeout':
                            console.log('Time out error.');
                            break;
                        case 'abort':
                            console.log('Ajax request aborted.');
                            break;
                    }

                    console.log('Uncaught Error.\n' + jqXHR.responseText);
                }
            });
        }
    }
});