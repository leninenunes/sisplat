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
    $(".data-viagem").datepicker({ nextText: "", prevText: "", changeMonth: true, changeYear: true, dateFormat: 'dd/mm/yy', minDate: "0d"}).mask("99/99/9999");
    $(".data-apropriacao").datepicker({ nextText: "", prevText: "", changeMonth: true, changeYear: true, dateFormat: 'dd/mm/yy', maxDate: "0d"}).mask("99/99/9999");
    $(".money").maskMoney({thousands:'.', decimal:',', prefix: 'R$ '});
    $(".number").maskMoney({thousands:'.', decimal:','});
    
    $.mask.definitions['H'] = "[0-2]";
    $.mask.definitions['h'] = "[0-9]";
    $.mask.definitions['M'] = "[0-5]";
    $.mask.definitions['m'] = "[0-9]";

    $(".hora-viagem").mask("Hh:Mm");
    $(".hora").mask("Hh:Mm");
}
