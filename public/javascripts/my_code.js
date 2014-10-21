/**
 * Created with IntelliJ IDEA.
 * User: LingXiaoshu
 * Date: 7/1/13
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function(){

    //hide the p tags
    $("p.abstract").hide();
    $(".view-abstract").click(function(){
        $(this).next().next().slideToggle(300);
    });

//    $("li.paper").mouseenter(function(){
//        $(this).next().slideToggle(300);
//    });
//    $("li.paper").mouseleave(function(){
//        $(this).next().slideToggle(300);
//    });
//    $("ul.resume").hide();
//    $("li.resume").mouseenter(function(){
//        $(this).next().slideToggle(300);
//    });
//    $("li.resume").mouseleave(function(){
//        $(this).next().slideToggle(300);
//    });
//
//    $(".theLogo1").mouseenter(function(){
//        $(this).css({"height":"360px"});
//        });
//    $(".theLogo1").mouseleave(function(){
//        $(this).css({"height":"100px"});
//    });
});