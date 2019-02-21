const SERVER_ROOT_URL = "http://" + window.location.host;

function listAllNotes(loggedInUser) {
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/notes",
        success: function (data, status) {
            generateNoteList(data, loggedInUser);
        },
        error: function () {
            console.log("Error using ajax");
        },
    });
}


function postNote() {
    console.log("in post note");
    const noteObject = {
        label: $("#new_note_label").val(),
        text: $("#new_note_text").val()
    };

    $.ajax({
        type: "POST",
        url: SERVER_ROOT_URL + "/addNote",
        data: JSON.stringify(noteObject),
        contentType: 'application/json;charset=UTF-8',
        dataType: "json",
        success: function (data, status) {

            let html = htmlUnCode($("#note_card_template_owner").html());
            let elem = ejs.render(html, {
                label: data.label,
                text: data.text,
                note_id: data.noteId
            });
            $("#all_notes_container").prepend(elem);
            $("#note_label" + data.noteId).val(data.label);
            $("#note_text" + data.noteId).text(data.text);

            $("#all-notes-tab").trigger("click");
            discardNote();

            alertify.notify('<i class="material-icons">done_all</i> Note saved successfully', 'success', 5, function () {
                console.log('dismissed');
            });
        },
        error: function (err) {
            console.log(err);
            console.log("Error using ajax");
        },
    });
}

function postChangeNote(noteId) {
    const noteObject = {
        label: $("#note_label" + noteId).val(),
        text: $("#note_text" + noteId).val(),
        noteId: noteId
    };

    $.ajax({
        type: "POST",
        url: SERVER_ROOT_URL + "/noteChanged",
        data: JSON.stringify(noteObject),
        contentType: 'application/json;charset=UTF-8',
        dataType: "json",
        success: function (data, status) {
            console.log(data);
            // add to list
        },
        error: function (err) {
            console.log(err);
            console.log("Error using ajax");
        },
    });
}

function whoAmI(callback) {
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/loggedInUser",
        success: function (data, status) {
            callback(data);
        },
        error: function () {
            console.log("Error using ajax");
        },
    });
}

function addCollabs(noteId, arrayOfPeopleToAdd) {

    if (arrayOfPeopleToAdd.length === 0) {
        alertify.notify('<i class="material-icons">error_outline</i> Successfully added collaborators', 'success', 5, function () {
            console.log('dismissed');
        });
    } else {
        let stringOfPeople = "";
        for (let i = 0; i < arrayOfPeopleToAdd.length; i++) {
            stringOfPeople += arrayOfPeopleToAdd[i].phoneNumber;
            stringOfPeople += ",";
        }
        console.log("noteId=====", noteId);
        stringOfPeople = stringOfPeople.slice(0, -1);

        console.log("stringOfPeople====", stringOfPeople);

        $.ajax({
            type: "POST",
            url: SERVER_ROOT_URL + "/addCollaborationByList/" + noteId,
            data: stringOfPeople,
            contentType: "text/plain",
            success: function (data, status) {
                console.log("data===", data);
                alertify.notify('<i class="material-icons">done_all</i> Successfully added collaborators', 'success', 5, function () {
                    console.log('dismissed');
                });
                getCollabsByNoteId(noteId);
                // add to list
            },
            error: function (err) {
                console.log(err);
                console.log("Error using ajax");
            },
        });
    }


}

function addCollab(collaboratorObj, noteId) {
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/addCollaboration/" + collaboratorObj.phoneNumber + "/" + noteId,
        success: function (data, status) {
            getCollabsByNoteId(noteId);
            alertify.notify('<i class="material-icons">done_all</i> Successfully added collaborator', 'success', 5, function () {
                console.log('dismissed');
            });
        },
        error: function (err) {
            console.log(err);
            console.log("Error using ajax");
        },
    });
}

function getCollabs(elem) {
    const noteId = elem.getAttribute("data-noteId");
    console.log("noteId" + noteId);
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/collaborators/" + noteId,
        success: function (data, status) {
            generateNoteCollabList(data, noteId)
        },
        error: function () {
            console.log("Error using ajax");
        },
    });
}

function getCollabsByNoteId(noteId) {
    console.log("noteId" + noteId);
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/collaborators/" + noteId,
        success: function (data, status) {
            generateNoteCollabList(data, noteId)
        },
        error: function () {
            console.log("Error using ajax");
        },
    });
}

function getPeopleByNoteId(noteId) {
    console.log("noteId" + noteId);
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/whoIsNotCollaborating/" + noteId,
        success: function (data, status) {
            generateNotePeopleList(data, noteId)
        },
        error: function () {
            console.log("Error using ajax");
        },
    });
}

function removeCollab(collaboratorPhone, noteId, createAlert) {
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/removeCollaboration/" + collaboratorPhone + "/" + noteId,
        success: function (data, status) {
            getCollabsByNoteId(noteId);
            if (createAlert) {
                alertify.notify('<i class="material-icons">done_all</i> Successfully removed collaborator', 'success', 5, function () {
                    console.log('dismissed');
                });
            }
        },
        error: function (err) {
            console.log(err);
            console.log("Error using ajax");
        },
    });
}

function generateNoteList(data, loggedInUser) {

    for (let i = data.length - 1; i >= 0; i--) {

        if (data[i].owner.phoneNumber === loggedInUser.phoneNumber) {
            let html = htmlUnCode($("#note_card_template_owner").html());
            let elem = ejs.render(html, {
                label: data[i].label,
                text: data[i].text,
                note_id: data[i].noteId
            });
            $("#all_notes_container").append(elem);
            $("#note_label" + data[i].noteId).val(data[i].label);
            $("#note_text" + data[i].noteId).text(data[i].text);
        } else {
            let html = htmlUnCode($("#note_card_template").html());
            let elem = ejs.render(html, {
                label: data[i].label,
                text: data[i].text,
                note_id: data[i].noteId
            });
            $("#all_notes_container").append(elem);
            $("#note_label" + data[i].noteId).val(data[i].label);
            $("#note_text" + data[i].noteId).text(data[i].text);
        }
    }
}

function generateNoteCollabList(data, noteId) {
    $("#note_collab_container" + noteId).html("");
    for (let i = 0; i < data.length; i++) {
        let html = htmlUnCode($("#collab_item_template").html());
        let elem = ejs.render(html, {
            name: data[i].name,
            email: data[i].email,
            phoneNumber: data[i].phoneNumber
        });
        $("#note_collab_container" + noteId).append(elem);
    }
}

function generateNotePeopleList(data) {
    $("#people-list-container").html("");
    for (let i = data.length - 1; i >= 0; i--) {
        let html = htmlUnCode($("#person-template").html());
        let elem = ejs.render(html, {
            name: data[i].name,
            email: data[i].email,
            phone: data[i].phoneNumber
        });
        $("#people-list-container").append(elem);
        $("#note_label" + data[i].noteId).val(data[i].label);
        $("#note_text" + data[i].noteId).text(data[i].text);
    }
}

function discardNote() {
    $("#new_note_label").val("").parent(".form-group").removeClass("is-focused is-filled");
    $("#new_note_text").val("").parent(".form-group").removeClass("is-focused is-filled");
    $("#note_save").attr("disabled", true);
}

function deleteNote(noteId) {
    $.ajax({
        type: "GET",
        url: SERVER_ROOT_URL + "/removeNote/" + noteId,
        success: function (data, status) {
            $("#note_card" + noteId).hide('slow');
            // generateNoteList(data);
            alertify.notify('<i class="material-icons">done_all</i> Note deleted', 'success', 5, function () {
                console.log('dismissed');
            });
        },
        error: function () {
            console.log("Error using ajax");
        },
    });
}

$(document).ready(function () {

    let workingNoteId = -1;
    let selectedPeople = [];
    let user = {};

    whoAmI(function (data) {
        user = data;
        listAllNotes(user);
        // listAllPeople();
        $("#username_container").append(user.name);
    });

    $("#note_save").click(function () {
        postNote()
    });

    $("#note_discard").click(function () {
        discardNote();
    });

    $("#new_note_label").keyup(function () {
        if (($("#new_note_label").val().length === 0) || ($("#new_note_text").val().length === 0)) {
            $("#note_save").attr("disabled", true);
        } else {
            $("#note_save").attr("disabled", false);
        }
    });

    $("#new_note_text").keyup(function () {
        if (($("#new_note_label").val().length === 0) && ($("#new_note_text").val().length === 0)) {
            $("#note_save").attr("disabled", true);
        } else {
            $("#note_save").attr("disabled", false);
        }
    });

    $(document).on("change", ".people-item", function () {
        if ($(this).is(":checked")) {
            console.log("checked" + $(this).attr("data-phoneNumber"));
            selectedPeople.push({
                phoneNumber: $(this).attr("data-phoneNumber"),
                name: $(this).attr("data-name"),
                email: $(this).attr("data-email")
            });
        } else {
            const index = selectedPeople.indexOf($(this).attr("data-phoneNumber"));
            selectedPeople.splice(index, 1);
        }
    });

    $(document).on("click", "#add-selected-person", function () {
        // for (let i = 0; i < selectedPeople.length; i++) {
        //     addCollab(selectedPeople[i], workingNoteId)
        // }
        addCollabs(workingNoteId, selectedPeople);
        console.log("selectedPeople" + selectedPeople);
        console.log("active note" + workingNoteId);
    });

    $(document).on("click", ".remove-person", function (event) {
        event.preventDefault();
        const phoneNum = $(this).attr("data-personPhoneNum");
        removeCollab(phoneNum, workingNoteId, true);
    });

    $(document).on("click", ".note-card", function () {
        workingNoteId = $(this).attr("data-note-id");
    });

    $(document).on("click", "#confirm_remove_note", function () {
        deleteNote(workingNoteId);
        workingNoteId = -1;
    });

    $(document).on("click", "#confirm_leave_note", function () {
        removeCollab(user.phoneNumber, workingNoteId, false);
        workingNoteId = -1;
    });

    $(document).on("click", ".toggle-people-modal", function () {
        selectedPeople = [];
        getPeopleByNoteId($(this).attr("data-noteId"));
    });

    $(document).on("keyup", ".note_label", function () {
        postChangeNote($(this).attr("data-noteId"));
    });

    $(document).on("keyup", ".note_text", function () {
        postChangeNote($(this).attr("data-noteId"));
    });

    $(".people-list-container").css('max-height', ($(document).height() * 0.4) + "px");

    function goToByScroll(id) {
        // Remove "link" from the ID
        id = id.replace("link", "");
        // Scroll
        $('html,body').animate({
                scrollTop: $("#" + id).offset().top - $(".navbar").height()
            },
            'slow');
    }

    $(".nav_scroller").click(function (e) {
        // Prevent a page reload when a link is pressed
        e.preventDefault();
        // Call the scroll function
        goToByScroll("main_container");
    });

    const client = Stomp.over(new SockJS('/noteAppStopmEndpoint'));
    client.connect({}, function (frame) {
        client.subscribe('/user/queue/messages', function (message) {
            const changeMessage = JSON.parse(message.body);
            $("#note_label" + changeMessage.note.noteId).val(changeMessage.note.label);
            $("#note_text" + changeMessage.note.noteId).val(changeMessage.note.text);
        });

        client.subscribe('/user/queue/addedCollaborations', function (message) {
            console.log("testtttttttttttttttt");
            const newNote = JSON.parse(message.body);
            let html = htmlUnCode($("#note_card_template").html());
            let elem = ejs.render(html, {
                label: newNote.label,
                text: newNote.text,
                note_id: newNote.noteId
            });
            $("#all_notes_container").prepend(elem);
            $("#note_label" + newNote.noteId).val(newNote.label);
            $("#note_text" + newNote.noteId).text(newNote.text);

            alertify.notify('<i class="material-icons">done_all</i> You just added a note collaboration', 'success', 5, function () {
                console.log('dismissed');
            });
        });

        client.subscribe('/user/queue/removedCollaborations', function (message) {
            const delNote = JSON.parse(message.body);
            $("#note_card" + delNote).hide('slow');
            alertify.notify('<i class="material-icons">done_all</i> You just left from a note collaboration', 'success', 5, function () {
                console.log('dismissed');
            });
        });

        client.subscribe('/user/queue/removedNotes', function (message) {
            const delNote = JSON.parse(message.body);
            $("#note_card" + delNote).hide('slow');
            alertify.notify('<i class="material-icons">done_all</i> A note deleted by its owner', 'success', 5, function () {
                console.log('dismissed');
            });
        });


    });
});

function htmlUnCode(htmlStr) {
    const find1 = '&lt;';
    const re1 = new RegExp(find1, 'g');
    htmlStr = htmlStr.replace(re1, '<');

    const find2 = '&gt;';
    const re2 = new RegExp(find2, 'g');
    htmlStr = htmlStr.replace(re2, '>');

    return htmlStr;
}

// get list of all people, (Not needed according to UI)
//
// function listAllPeople() {
//     $.ajax({
//         type: "GET",
//         url: SERVER_ROOT_URL + "/users",
//         success: function (data, status) {
//             generatePeopleList(data);
//         },
//         error: function () {
//             console.log("Error using ajax");
//         },
//     });
// }
//
// function generatePeopleList(data) {
//     for (let i = data.length - 1; i >= 0; i--) {
//         let html = htmlUnCode($("#person-template").html());
//         let elem = ejs.render(html, {
//             name: data[i].name,
//             email: data[i].email,
//             phone: data[i].phoneNumber
//         });
//         $("#people-list-container").append(elem);
//         $("#note_label" + data[i].noteId).val(data[i].label);
//         $("#note_text" + data[i].noteId).text(data[i].text);
//     }
// }
