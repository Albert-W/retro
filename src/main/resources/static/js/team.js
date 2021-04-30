// Empty JS for your own code to be here
$(document).ready(function () {
    
    //List all teams
    listAllTeams();

    $("#selectTeam").change(function(){
        // console.log($("option:selected", this));
        // console.log( $("option:selected").val())
        var id = $("#selectTeam option:selected").attr("value");
        // console.log(currentTeamId);
        // console.log($("#selectTeam option:selected"))
        // console.log($("#selectTeam option:selected").attr("value"))
        // list all members
        listAllMembers(id);
    })

    $("#createTeam").click(function () {
        name = $("#teamName").val();
        nameCapitalized = name.charAt(0).toUpperCase() + name.slice(1)
        console.log(nameCapitalized);
        $.post(
            "api/addteam",
            { teamName: name },
            function (result) {
                $("#teamCreated").text(nameCapitalized + " Created")
                console.log(result.teamId);
                // currentTeamId = result.teamId;
                listAllTeams(result.teamId);
                // choose the one recently created
                // $("#selectTeam").children().last().attr("selected", true);
                listAllMembers(result.teamId)
            }
        )
        
    })

    $("#addMember").click(function () {
        localName = $("#memberName").val();
        var id = $("#selectTeam option:selected").attr("value");
        $.ajax({
            url: "api/addTeamMember/" + id,
            type: 'PUT',
            data: "memberName=" + localName,
            success: function (result) {
                if ($("#listOfMember").children().length > 0) {
                    $("#listOfMember").children(":first").before("<li>" + localName + "</li>");
                } else {
                    $("#listOfMember").append("<li>" + localName + "</li>");
                }
                console.log(result);
            }
        })
    })


    $("#goToSprint").click(function(){
        var id = $("#selectTeam option:selected").attr("value");
        sprintName = $("#sprintName").val();
        if(sprintName){
            $.ajax({
                url: "api/addTeamSprint/" + id,
                type: 'put',
                data: {sprintName: sprintName},
                success: function(result){
                    console.log(result);
                    // alert(result);
                }
            })
            window.open('/sprint.html')
            
        } else {
            alert("Please input the Sprint Name")
        }

    })
});

function listAllTeams( id ){
    // console.log("List All Teams With Id:" + id)
    if(id == null){
        id = 1;
    }
    
    var selectTeam = $("#selectTeam");
    selectTeam.empty();
    $.ajax({
        url:"api/teams",
        type: "GET",
        success: function(result){
            console.log(result)
            for (var team of result){
                var el = document.createElement("option");
                el.textContent = team.teamName;
                el.value =team.teamId;
                selectTeam.append(el);
                // console.log(el);
                console.log("Team.teamid:"+ id)
                if(team.teamId == id ){
                    console.log("The equal Id is:" + id);
                    el.setAttribute("selected", true);
                }
            }
            // $("option[teamId="+id+"]")
            // console.log($("option[teamId="+id+"]").teamName)
            // selectTeam.children().first().attr("selected", true);
            // var id = $("#selectTeam option:selected").attr("value");
            // console.log(id);
            listAllMembers(id);
        }
    })
    
}

function listAllMembers(id){
    // var id = $("#selectTeam option:selected").attr("value");
    // console.log(id);
    var ul = $("#listOfMember");
    ul.empty();
    $.ajax({
        url:"api/getTeamMembers/" + id,
        type: "GET",
        success: function(result){
            console.log(result)
            for (var member of result){
                var el = document.createElement("li");
                el.textContent = member.name;
                el.value =member.memberId;
                ul.append(el);
                // selectTeam.append(el);
                // console.log(member);
            }
            // selectTeam.children().last().attr('selected',true);
            // findSprintNames();
            // displayAllItems();
            
        }
    })

}