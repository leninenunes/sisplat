$(document).ready(function() {
    validationSucess();
    maskInput();
});

function validationSucess(){
    $(".modal").modal('hide');
    $(".alert").click(function(){
        $(this).hide();
    });
    
    $(".alert").show(function(){
        $(this).delay(3000).fadeOut(1000);
    });
}

function maskInput(){
    $(".date").datepicker({ nextText: "", prevText: "", changeMonth: true, changeYear: true, dateFormat: 'dd/mm/yy'}).mask("99/99/9999");
    $(".money").maskMoney({thousands:'.', decimal:',', prefix: 'R$ '});
    $(".number").maskMoney({thousands:'.', decimal:','});
}
