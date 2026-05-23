package com.wash.laundry_app.users.admin;

import com.wash.laundry_app.command.CommandeDTO;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class AdminOrdersResponseDTO {
    private List<CommandeDTO> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean first;
    private boolean last;
    private BigDecimal totalValue;
    private BigDecimal totalUnpaid;
    private Long totalVolumes;
}
