package com.example.appliances.model.response;

import com.example.appliances.enums.ReturnStatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReturnFilialResponse {
    private Long returnId;
    private ReturnStatusEnum status;

}
