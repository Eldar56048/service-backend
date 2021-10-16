package com.crm.servicebackend.dto.requestDto.report;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class GivenOrdersReportDtoRequest {
    @NotNull(message = "Поле Дата обязательно")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    /*@PastOrPresent*/
    Date date1;
    @NotNull(message = "Поле Дата обязательно")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    /*@PastOrPresent*/
    Date date2;
}
