package com.wash.laundry_app.users.employe;

import com.wash.laundry_app.command.*;
import com.wash.laundry_app.command.services.CommandeQueryService;
import com.wash.laundry_app.config.FileStorageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employe")
@AllArgsConstructor
public class EmployeController {

    private final CommandeQueryService queryService;
    private final CommandeMapper commandeMapper;
    private final CommandeService commandeService;
    private final FileStorageService fileStorageService;

    @GetMapping("/commandes")
    public ResponseEntity<List<CommandDtoEmploye>> allCommandes() {
        return ResponseEntity.ok(
                queryService.getCommandesByStatus(CommandeStatus.PENDING_PICKUP).stream()
                        .map(dto -> mapToEmployeDto(dto))
                        .toList()
        );
    }

    @GetMapping("/commandes/attente")
    public ResponseEntity<List<CommandDtoEmploye>> getPendingCommandes() {
        return ResponseEntity.ok(
                queryService.getCommandesByStatus(CommandeStatus.PENDING_PICKUP).stream()
                        .map(dto -> mapToEmployeDto(dto))
                        .toList()
        );
    }

    @GetMapping("/commandes/count/attente")
    public ResponseEntity<Long> getPendingCount() {
        return ResponseEntity.ok(commandeService.getCountByStatus(CommandeStatus.PENDING_PICKUP));
    }

    @GetMapping("/commandes/retournee")
    public ResponseEntity<List<CommandDtoEmploye>> getReturnedCommandes() {
        return ResponseEntity.ok(
                queryService.getCommandesByStatus(CommandeStatus.PICKED_UP).stream()
                        .map(dto -> mapToEmployeDto(dto))
                        .toList()
        );
    }

    @GetMapping("/commandes/count/retournee")
    public ResponseEntity<Long> getReturnedCount() {
        return ResponseEntity.ok(commandeService.getCountByStatus(CommandeStatus.PICKED_UP));
    }

    @PatchMapping("/commandes/{id}/status")
    public ResponseEntity<CommandeDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommandeStatusRequest request) {
        return ResponseEntity.ok(commandeService.updateStatus(id, request));
    }

    @GetMapping("/commandes/{id}")
    public ResponseEntity<CommandDetails> getCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getCommandeById(id));
    }

    @PostMapping("/tapis/upload")
    public ResponseEntity<List<Map<String, String>>> uploadTapisImages(
            @RequestParam("files") MultipartFile[] files) {
        List<Map<String, String>> result = Arrays.stream(files).map(file -> {
            String fileName = fileStorageService.storeFile(file);
            return Map.of("imageUrl", "/uploads/" + fileName);
        }).toList();
        return ResponseEntity.ok(result);
    }

    private CommandDtoEmploye mapToEmployeDto(CommandeDTO dto) {
        CommandDtoEmploye employe = new CommandDtoEmploye();
        employe.setId(dto.getId());
        employe.setNumeroCommande(dto.getNumeroCommande());
        employe.setStatus(dto.getStatus());
        employe.setMode(dto.getMode());
        employe.setDateCreation(dto.getDateCreation());
        employe.setDateValidation(dto.getDateValidation());
        employe.setDateLivraison(dto.getDateLivraison());
        employe.setLivreur(dto.getLivreur());
        employe.setDeliveryDriver(dto.getDeliveryDriver());
        employe.setCommandeTapis(dto.getCommandeTapis());
        employe.setCreatedAt(dto.getCreatedAt());
        employe.setUpdatedAt(dto.getUpdatedAt());
        return employe;
    }
}
