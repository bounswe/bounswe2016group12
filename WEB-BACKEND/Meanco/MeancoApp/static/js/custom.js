/**
 * Follow topic function. A request is sent to FollowTopic API to follow topics.
 */

function follow(id) {
    console.log("Follow.");  // sanity check
    var serializedData = {  // data to be sent with POST request
        TopicId: id
    };
    $.ajax({
        url: "/API/FollowTopic",
        type: "post",
        data: serializedData,
        cache: 'false',
        async: 'true',

        success: function (response) {  // No error.
            console.log("No error.");
            if (response == "Success") {
            }
            location.reload();  // Refresh page.
        },
        error: function (response) {    // Error.
            console.log(response);
            if (response == "Wrong Request") {
            } else if (response == "Follow Topic Error") {

            } else {

            }
        }
    });

}