// Empty JS for your own code to be here
$(document).ready(function(){
    var selectTeam = $("#selectTeam");

    $.ajax({
        url:"api/teams",
        type: "GET",
        success: function(result){
            // console.log(result)
            for (var team of result){
                var el = document.createElement("option");
                el.textContent = team.teamName;
                el.value =team.teamId;
                selectTeam.append(el);
            }
            // selectTeam.children().last().attr('selected',true);
            findSprintNames();
            // displayAllItems();
        }
    })

    

    $("#selectTeam").change(function(){
        // console.log($("option:selected", this));
        // console.log( $("option:selected").val())
        findSprintNames();
    })

    $("#selectSprint").change(function(){
        // console.log( $("option:selected").val())
        displayAllItems();
    })

    $.ajax({
        url:"api/retrospectives",
        type: "GET",
        success: function(result){
            // console.log(result)
            // console.log(result.length)
            // console.log(result[result.length - 1])
            $("#sprintName").text(
                result[result.length - 1].name
            )
        }
    })

    thumbClick();

    // $("input.sprint-item").on('mouseover', function(){
    //     console.log("Saving value " + $(this).val());
    //     $(this).data('val', $(this).val());
    // });
    $("input.sprint-item").change(function(){
        // userface 
        var string = $(this).val();
        var newEl = createLi($(this).parent().attr("id"), string,0);
        // var newEl =  $(this).next().children(":first").clone()
        // console.log($(this).val())
        // newEl.children(":first").text( string );  
        // newEl.find("span").text(0);
        // $(this).next().children(":first").before(newEl);
        // console.log("input changed");
        $(this).val("");
        thumbClick();
        clickonItem();

        // database 
        var sprintId = $("#selectSprint option:selected").val();
        var mood = $(this).parent().attr("id").toUpperCase();
        $.ajax({
            url: "api/addSprintItem/" + sprintId,
            type: 'PUT',
            data: {mood: mood , itemDesc: string},
            success: function (result) {
                console.log(result);
                newEl.attr("id", result.itemId);
                console.log(newEl);
                console.log(newEl.attr("id"));
            }
        })
    })

    $("#commentInput").change(function(){
        // ui
        var string = $(this).val();
        var el = document.createElement("li");
        el.textContent = string;
        $(this).parent().children().first().append(el);
        $(this).val("");

        // database
        var id = $(this).attr("itemid");
        $.ajax({
            url: "api/item/"+ id + "/comment",
            type: 'PUT',
            data: {comment: string},
            success: function (result) {
                console.log(result);
                $("li#"+id).find("span.comments").text(
                    // 10
                    result.comments.length
                )
            }
        })
    })
})


function thumbClick(){
    $(".thumb").off("click")

    $(".thumb").click(function(){
        event.stopPropagation();
        // userInterface
        // console.log($(this).next().text());
        // console.log($(this).next().text());
        $(this).next().text( parseInt($(this).next().text()) +1  ) 

        // dataBase:
        var itemId = $(this).parent().parent().attr("id");
        $.ajax({
            url:"api/item/"+ itemId + "/vote",
            type: "PUT",
            data:  null,
            success: function(result){
                console.log(result);
            }
        })
    })
}

function findSprintNames(){
    var selectSprint = $("#selectSprint");
    selectSprint.empty();
    var teamId = $("#selectTeam option:selected").val();
    if(teamId){
        $.get(
            "api/getTeamSprints/" + teamId, 
            function(data, status){
                // console.log(data);
                for (var sprint of data){
                    var el = document.createElement("option");
                    el.textContent = sprint.name;
                    el.value =sprint.retrospectiveId;
                    selectSprint.append(el);
                }
                selectSprint.children().last().attr('selected',true);
                displayAllItems();
    
            }
        )
    }

    
}

// get items from data base;
function displayAllItems(){
    //get All Items
    $(".board ul").empty();

    var sprintId = $("#selectSprint option:selected").val();
    if(sprintId){
    $.get(
        "api/retrospectiveItems/" + sprintId, 
        function(data, status){
            console.log(data);
            for(var item of data){
                addItem(item);
            }
            clickonItem();
        }
    ) 
    }

}



function addItem( item){
    var flag = item.category.toLowerCase();

    // find the ul in board
    // console.log(item.comments.length);
    var newEl = createLi(flag, item.description, item.comments.length);
    

    // insert the new El
    // var ul = $("#" + flag +" ul")
    newEl.attr("id", item.itemId);
    newEl.find("span.votescore").text(item.votes);
    
    thumbClick();
}
function createLi(flag, description, count){
    var ul = $("#" + flag +" ul")
    var newEl;
    if(ul.children().length > 0){
        newEl = ul.children(":first").clone();
        // newEl.children(":first").text( description);  
        // newEl.find("span.votescore").text(0);
        ul.children(":first").before(newEl);
    } else {
        var string = '<li class="list-item sprint-item" data-toggle="modal" data-target="#exampleModal">\
        <div> Lorem ipsum dolor sit amet</div>\
        <div class="vote">\
            <input class="comment" type="button" value="&#128172">\
            <span class="comments">0</span>\
            <input class="thumb" type="button" value="&#128077">\
            <span class="votescore">0</span>\
        </div>\
        </li>'
        newEl = $($.parseHTML(string));
        ul.append(newEl)
    }
    newEl.children(":first").text( description);  
    newEl.find("span.votescore").text(0);
    newEl.find("span.comments").text(count)
    return newEl;
}

function displayComments(id){
    $("#comment").empty();

    $.get(
    "api/item/"+id+"/comments", 
    function(data, status){
        console.log(data);
        for(var c of data){
            var el = document.createElement("li");
            el.textContent = c;
            $("#comment").append(el);
        }
    }
    ) 
}

function clickonItem(){
    $("li.list-item").off("click");
    $("li.list-item").on("click",function(){
        // console.log("what");
        var id = $(this).attr("id");
        console.log(id);
        displayComments(id);
        $("#commentInput").attr("itemId", id);
    })
}