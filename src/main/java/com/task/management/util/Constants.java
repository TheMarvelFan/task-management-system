package com.task.management.util;

public final class Constants {
    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    // API Endpoint Constants
    public static class Endpoints {
        public static final String BASE_API_PATH = "/api";
        public static final String AUTH_PATH = BASE_API_PATH + "/auth";
        public static final String TASKS_PATH = BASE_API_PATH + "/tasks";
        public static final String SUBTASKS_PATH = BASE_API_PATH + "/subtasks";
    }

    // Validation Constants
    public static class Validation {
        // Task Validation
        public static final int TITLE_MIN_LENGTH = 3;
        public static final int TITLE_MAX_LENGTH = 100;
        public static final int DESCRIPTION_MAX_LENGTH = 500;

        // User Validation
        public static final int USERNAME_MIN_LENGTH = 3;
        public static final int USERNAME_MAX_LENGTH = 50;
        public static final int PASSWORD_MIN_LENGTH = 8;
        public static final int PASSWORD_MAX_LENGTH = 255;

        // Email Regex Pattern
        public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    }

    // Error Message Constants
    public static class ErrorMessages {
        // Authentication Errors
        public static final String UNAUTHORIZED = "Unauthorized access";
        public static final String INVALID_CREDENTIALS = "Invalid username or password";
        public static final String USER_NOT_FOUND = "User not found";

        // Validation Errors
        public static final String TITLE_LENGTH_INVALID = "Title must be between 3 and 100 characters";
        public static final String DESCRIPTION_LENGTH_INVALID = "Description must not exceed 500 characters";
        public static final String INVALID_EMAIL = "Invalid email format";
        public static final String PASSWORD_LENGTH_INVALID = "Password must be between 8 and 255 characters";

        // Task Errors
        public static final String TASK_NOT_FOUND = "Task not found";
        public static final String SUBTASK_NOT_FOUND = "Subtask not found";
        public static final String UNAUTHORIZED_TASK_ACCESS = "You are not authorized to access this task";

        // Generic Errors
        public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred";
        public static final String BAD_REQUEST = "Invalid request parameters";
    }

    // Logging Constants
    public static class LoggingMessages {
        public static final String USER_REGISTERED = "New user registered: {}";
        public static final String TASK_CREATED = "Task created by user: {}";
        public static final String SUBTASK_CREATED = "Subtask created for task: {}";
        public static final String JWT_TOKEN_GENERATED = "JWT token generated for user: {}";
    }

    // Pagination Constants
    public static class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 10;
        public static final int MAX_PAGE_SIZE = 100;
        public static final int DEFAULT_PAGE_NUMBER = 0;
    }

    // Enum Utility Methods
    public static class EnumUtils {
        public static <T extends Enum<T>> boolean isValidEnum(Class<T> enumClass, String value) {
            if (value == null) return false;
            try {
                Enum.valueOf(enumClass, value.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

    // Date and Time Constants
    public static class DateTime {
        public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
        public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String TIME_ZONE = "UTC";
    }
}
