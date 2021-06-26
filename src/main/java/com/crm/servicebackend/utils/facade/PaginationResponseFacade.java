package com.crm.servicebackend.utils.facade;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PaginationResponseFacade {

    public static Map<String, Object> response(Page<?> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("currentPage", page.getNumber()+1);
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        return response;
    }
}
