package com.wash.laundry_app.command;

import com.wash.laundry_app.clients.ClientMapper;
import com.wash.laundry_app.command.attempts.OrderAttempt;
import com.wash.laundry_app.command.attempts.OrderAttemptDto;
import com.wash.laundry_app.users.UserMapper;
import com.wash.laundry_app.users.admin.CommandSummaryDto;
import com.wash.laundry_app.users.employe.CommandDetails;
import com.wash.laundry_app.users.employe.CommandDtoEmploye;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class, CommandeTapisMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommandeMapper {

    CommandeDTO toDto(Commande commande);

    CommandDtoEmploye todto(Commande commande);

    CommandDetails Todto(Commande commande);

    CommandSummaryDto TodTo(Commande commande);

    default List<OrderAttemptDto> mapAttempts(List<OrderAttempt> attempts) {
        if (attempts == null) return List.of();
        return attempts.stream()
                .map(a -> {
                    OrderAttemptDto dto = new OrderAttemptDto();
                    dto.setId(a.getId());
                    dto.setAttemptType(a.getAttemptType());
                    dto.setReason(a.getReason());
                    dto.setNotes(a.getNotes());
                    dto.setAttemptedAt(a.getAttemptedAt());
                    dto.setRescheduledTo(a.getRescheduledTo());
                    if (a.getDriver() != null) dto.setDriverName(a.getDriver().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
