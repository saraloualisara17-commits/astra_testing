package com.wash.laundry_app.users.admin;

import com.wash.laundry_app.command.CommandeStatus;
import com.wash.laundry_app.command.CommandeTapisDTO;
import com.wash.laundry_app.command.ModeCommande;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandSummaryDto {
    private Long id;
    private String numeroCommande;
    private CommandeStatus status;
    private ModeCommande mode;
    private BigDecimal montantTotal;
    private LocalDateTime dateCreation;
    private String clientNom;
    private List<CommandeTapisDTO> commandeTapis;
}
