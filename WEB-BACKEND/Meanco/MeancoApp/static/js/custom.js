/**
 * Created by esma on 18.12.2016.
 */

function follow(id) {
    console.log("Follow.");  // sanity check
    var serializedData = {
        TopicId: id
    };
    $.ajax({
        url: "/API/FollowTopic",
        type: "post",
        data: serializedData,
        cache: 'false',
        async: 'true',

        success: function (response) {
            console.log("No error.");
            if (response == "Success") {
            }
            location.reload();
        },
        error: function (response) {
            console.log(response);
            if (response == "Wrong Request") {
            } else if (response == "Follow Topic Error") {

            } else {

            }
        }
    });

}