package com.android.todo.network;

public class NetworkRouter {

    public static String buildURLRequest(String requestName) {
        String url = null;

        switch (requestName) {
            case "login":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.USER_REQUEST_MAPPING + "/" + NetworkingConstants.LOGIN;
                break;
            case "getNotes":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.GET_ALL_NOTES_BY_ID;
                break;
            case "getAllUpcoming":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.GET_ALL_UPCOMING_NOTES;
                break;
            case "getAllFinished":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.GET_ALL_FINISHED_NOTES;
                break;
            case "addNote":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.ADD_NOTE;
                break;
            case "updateNote":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.UPDATE_NOTE;
                break;
            case "uploadNotes":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.ADD_LATEST_NOTES;
                break;
            case "delete":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.DELETE_NOTE_BY_ID;
                break;
            case "deleteAllNotes":
                url = NetworkingConstants.BASE_URL + "/" + NetworkingConstants.NOTE_REQUEST_MAPPING + "/" + NetworkingConstants.DELETE_ALL_NOTES_BY_ID;
                break;
        }
        return url;
    }
}
