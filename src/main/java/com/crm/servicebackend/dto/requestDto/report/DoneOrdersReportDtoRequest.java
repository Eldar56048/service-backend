package com.crm.servicebackend.dto.requestDto.report;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DoneOrdersReportDtoRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Поле Дата обязательно")
   /* @PastOrPresent*/
    Date date1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Поле Дата обязательно")
    /*@PastOrPresent*/
    Date date2;
}
