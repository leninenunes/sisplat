$(document).ready(function() {
    validationSucess();
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
