package com.android.todo.network;

public interface NetworkingConstants {

    public static final String BASE_URL = "https://to-do-web-service.herokuapp.com";

    // Request Headers
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE_JSON = "application/json";

    // Request Mappings
    public static final String USER_REQUEST_MAPPING = "user";
    public static final String NOTE_REQUEST_MAPPING = "note";

    // ****************************** Network Requests ******************************
    // User Requests
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String GET_USER_BY_ID = "userId";
    public static final String GET_ALL_USERS = "all";
    public static final String UPDATE_USER_DATA = "update";
    public static final String DELETE_USER_BY_ID = "delete";

    // Note Requests
    public static final String ADD_NOTE = "add";
    public static final String ADD_LATEST_NOTES = "addAll";
    public static final String GET_ALL_NOTES_BY_ID = "all";
    public static final String GET_ALL_UPCOMING_NOTES = "upcomingAll";
    public static final String GET_ALL_FINISHED_NOTES = "finishedAll";
    public static final String UPDATE_NOTE = "update";
    public static final String DELETE_NOTE_BY_ID = "delete";
    public static final String DELETE_ALL_NOTES_BY_ID = "deleteAll";

    // ****************************** Query Parameters ******************************
    // User Query Parameters
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String PASSWORD = "password";

    // Note Query Parameters
    public static final String NOTE_ID = "noteId";
    public static final String NOTE_NAME = "noteName";
    public static final String NOTE_DESC = "noteDesc";
    public static final String NOTE_STATE = "noteState";
    public static final String NOTE_DATE = "noteDate";
}