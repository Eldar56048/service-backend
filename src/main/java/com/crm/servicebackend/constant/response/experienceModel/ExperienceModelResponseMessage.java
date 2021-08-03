package com.crm.servicebackend.constant.response.experienceModel;

public class ExperienceModelResponseMessage {
    public static final String EXPERIENCE_MODEL_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String EXPERIENCE_MODEL_EXISTS_BY_NAME_MESSAGE = "Опыт с таким названием уже существует";
    public static final String EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_MESSAGE = "Опыт с таким процентом уже существует";

    public static final String EXPERIENCE_MODEL_NOT_FOUND_MESSAGE(Long experienceId) {
        return "Опыт с идентификатором № "+experienceId+" не найдено";
    }
}
