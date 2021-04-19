$(document).ready(function() {
    validationSucess();
    $("#inicio").datepicker({ nextText: "", prevText: "", changeMonth: true, changeYear: true }).mask("99/99/9999");
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
