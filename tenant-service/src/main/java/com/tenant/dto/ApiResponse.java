package com.tenant.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ApiResponse<E> {

    private E data;
}
