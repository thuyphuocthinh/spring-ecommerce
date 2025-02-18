package com.tpt.tpt_ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIErrorResponse {
    public boolean status;
    public String message;
}
